package com.k40.authmicroservice.controllers;

import com.k40.authmicroservice.dto.*;
import com.k40.authmicroservice.exception.BadRequestException;
import com.k40.authmicroservice.model.Client;
import com.k40.authmicroservice.model.User;
import com.k40.authmicroservice.repos.UserRepository;
import com.k40.authmicroservice.service.AuthService;
import com.k40.authmicroservice.service.ClientService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register/company")
    public ResponseEntity<CompanyRegistrationResponse> registerCompany(
            @Valid @RequestBody RegisterCompanyRequest request) {
        try {
            // Register company and get client
            Client client = clientService.registerCompany(request);

            // Get the director user
            User director = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("Director not found after registration"));

            // Return using the CompanyRegistrationResponse DTO
            return ResponseEntity.ok(
                    CompanyRegistrationResponse.success(client, director));

        } catch (BadRequestException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new CompanyRegistrationResponse(
                            false,
                            e.getMessage(),
                            null,
                            null));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CompanyRegistrationResponse(
                            false,
                            "Registration failed: " + e.getMessage(),
                            null,
                            null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        return ResponseEntity.ok(authService.authenticateUser(request, response));
    }

    @PostMapping("/set-password")
    public ResponseEntity<?> setPassword(@RequestBody SetPasswordRequest request) {
        authService.setPassword(request);
        return ResponseEntity.ok("Password set successfully");
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.ok(authService.refreshToken(request, response));
    }

    @PostMapping("/request-password-reset")
    public ResponseEntity<?> requestPasswordReset(@RequestBody String email) {
        authService.requestPasswordReset(email);
        return ResponseEntity.ok("Password reset email sent");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody SetPasswordRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok("Password reset successfully");
    }

    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> logout(HttpServletResponse response) {
        return ResponseEntity.ok(authService.logout(response));
    }
}