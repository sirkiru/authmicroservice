package com.k40.authmicroservice.service;

import com.k40.authmicroservice.dto.EmailRequest;
import com.k40.authmicroservice.dto.RegisterUserRequest;
import com.k40.authmicroservice.dto.UserResponse;
import com.k40.authmicroservice.enums.RegistrationStatus;
import com.k40.authmicroservice.enums.UserRole;
import com.k40.authmicroservice.enums.UserType;
import com.k40.authmicroservice.exception.ResourceNotFoundException;
import com.k40.authmicroservice.model.User;
import com.k40.authmicroservice.repos.ClientRepository;
import com.k40.authmicroservice.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Value("${email.service.sender}")
    private String emailSender;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public User registerUser(RegisterUserRequest request, UserType userType) {
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setMiddleName(request.getMiddleName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setUsername(request.getEmail());
        user.setIdPassportNumber(request.getIdPassportNumber());
        user.setKraPin(request.getKraPin());
        user.setNationality(request.getNationality());
        user.setIsKenyan("Kenyan".equalsIgnoreCase(request.getNationality()));
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPhysicalAddress(request.getPhysicalAddress());
        user.setCity(request.getCity());
        user.setCountry(request.getCountry());

        user.setClientId(request.getClientId());
        user.setUserType(userType);
        user.setStatus(RegistrationStatus.PENDING);

        // Assign appropriate role based on user type
        UserRole role = switch (userType) {
            case ADMIN -> UserRole.ROLE_ADMIN;
            case DIRECTOR -> UserRole.ROLE_DIRECTOR;
            case USER -> UserRole.ROLE_USER;
        };
        user.setRoles(Set.of(role));

        // Generate setup token
        String setupToken = UUID.randomUUID().toString();
        user.setResetPasswordToken(setupToken);

        User savedUser = userRepository.save(user);
        sendPasswordSetupEmail(savedUser);

        return savedUser;
    }

    public void setPassword(String token, String password) {
        User user = userRepository.findByResetPasswordToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid token"));

        user.setPassword(passwordEncoder.encode(password));
        user.setResetPasswordToken(null);
        user.setStatus(RegistrationStatus.APPROVED);
        userRepository.save(user);

        if (user.getUserType() == UserType.DIRECTOR && user.getClientId() != null) {
            clientRepository.findById(user.getClientId()).ifPresent(client -> {
                client.setStatus(RegistrationStatus.APPROVED);
                clientRepository.save(client);
            });
        }
    }

    public UserResponse getCurrentUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return mapToUserResponse(user);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    public User getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public User updateUser(String id, RegisterUserRequest request) {
        User user = getUserById(id);

        user.setFirstName(request.getFirstName());
        user.setMiddleName(request.getMiddleName());
        user.setLastName(request.getLastName());
        user.setIdPassportNumber(request.getIdPassportNumber());
        user.setKraPin(request.getKraPin());
        user.setNationality(request.getNationality());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPhysicalAddress(request.getPhysicalAddress());
        user.setCity(request.getCity());

        if (request.getCountry() != null) {
            user.setCountry(request.getCountry());
        }

        return userRepository.save(user);
    }

    public void deleteUser(String id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }

    public void requestPasswordReset(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        String resetToken = UUID.randomUUID().toString();
        user.setResetPasswordToken(resetToken);
        userRepository.save(user);

        sendPasswordResetEmail(user, resetToken);
    }

    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetPasswordToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid password reset token"));

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetPasswordToken(null);
        userRepository.save(user);
    }

    private void sendPasswordSetupEmail(User user) {
        String setupLink = frontendUrl + "/set-password?token=" + user.getResetPasswordToken();

        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setToEmail(user.getEmail());
        emailRequest.setSubject("Complete Your K40 Account Setup");
        emailRequest.setBody(buildSetupEmailBody(user, setupLink));
        emailRequest.setFromEmail(emailSender);
        emailRequest.setSenderService("auth-service");

        emailService.sendEmail(emailRequest);
    }

    private void sendPasswordResetEmail(User user, String resetToken) {
        String resetLink = frontendUrl + "/reset-password?token=" + resetToken;

        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setToEmail(user.getEmail());
        emailRequest.setSubject("Password Reset Request");
        emailRequest.setBody(buildResetEmailBody(user, resetLink));
        emailRequest.setFromEmail(emailSender);
        emailRequest.setSenderService("auth-service");

        emailService.sendEmail(emailRequest);
    }

    private String buildSetupEmailBody(User user, String setupLink) {
        return String.format(
                "Dear %s %s,\n\n" +
                        "Welcome to K40! Please click the link below to set up your password:\n\n" +
                        "%s\n\n" +
                        "This link will expire in 24 hours.\n\n" +
                        "Best regards,\n" +
                        "K40 Team",
                user.getFirstName(), user.getLastName(), setupLink);
    }

    private String buildResetEmailBody(User user, String resetLink) {
        return String.format(
                "Dear %s %s,\n\n" +
                        "We received a request to reset your password. Click the link below to proceed:\n\n" +
                        "%s\n\n" +
                        "If you didn't request this, please ignore this email.\n\n" +
                        "Best regards,\n" +
                        "K40 Team",
                user.getFirstName(), user.getLastName(), resetLink);
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.fromEntity(user);
    }
}