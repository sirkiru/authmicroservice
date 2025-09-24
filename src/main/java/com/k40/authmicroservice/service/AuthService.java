package com.k40.authmicroservice.service;

import com.k40.authmicroservice.dto.AuthResponse;
import com.k40.authmicroservice.dto.LoginRequest;
import com.k40.authmicroservice.dto.MessageResponse;
import com.k40.authmicroservice.dto.RefreshTokenResponse;
import com.k40.authmicroservice.dto.SetPasswordRequest;
import com.k40.authmicroservice.exception.BadRequestException;
import com.k40.authmicroservice.exception.ResourceNotFoundException;
import com.k40.authmicroservice.model.User;
import com.k40.authmicroservice.repos.UserRepository;
import com.k40.authmicroservice.security.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsService userDetailsService;

    public AuthResponse authenticateUser(LoginRequest loginRequest, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            String jwt = jwtUtils.generateJwtToken(authentication);
            String refreshToken = jwtUtils.generateRefreshToken(authentication);

            // Set tokens in HTTP-only cookies
            addTokenCookies(jwt, refreshToken, response);

            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            User user = userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            return new AuthResponse(
                    userDetails.getId(),
                    userDetails.getUsername(),
                    userDetails.getEmail(),
                    user.getUserType(),
                    roles);
        } catch (BadCredentialsException e) {
            throw new BadRequestException("Invalid email or password");
        }
    }

    public void setPassword(SetPasswordRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("Passwords do not match");
        }
        userService.setPassword(request.getToken(), request.getPassword());
    }

    public RefreshTokenResponse refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = getRefreshTokenFromCookies(request);
        if (refreshToken == null || !jwtUtils.validateJwtToken(refreshToken)) {
            clearTokenCookies(response);
            throw new BadRequestException("Invalid or expired refresh token");
        }

        String username = jwtUtils.getUserNameFromJwtToken(refreshToken);
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities());

        String newAccessToken = jwtUtils.generateJwtToken(authentication);
        String newRefreshToken = jwtUtils.generateRefreshToken(authentication);

        // Update cookies with new tokens
        addTokenCookies(newAccessToken, newRefreshToken, response);

        return new RefreshTokenResponse(
                "Access token refreshed successfully",
                jwtUtils.getJwtExpirationMs() / 1000);
    }

    public void requestPasswordReset(String email) {
        userService.requestPasswordReset(email);
    }

    public void resetPassword(SetPasswordRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("Passwords do not match");
        }
        userService.resetPassword(request.getToken(), request.getPassword());
    }

    public MessageResponse logout(HttpServletResponse response) {
        clearTokenCookies(response);
        SecurityContextHolder.clearContext();
        return new MessageResponse("Logout successful");
    }

    private void addTokenCookies(String accessToken, String refreshToken, HttpServletResponse response) {
        // Access Token Cookie
        Cookie accessCookie = new Cookie("accessToken", accessToken);
        accessCookie.setHttpOnly(true);
        accessCookie.setSecure(true);
        accessCookie.setPath("/");
        accessCookie.setMaxAge(jwtUtils.getJwtExpirationMs() / 1000);
        response.addCookie(accessCookie);

        // Refresh Token Cookie
        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(jwtUtils.getJwtRefreshExpirationMs() / 1000);
        response.addCookie(refreshCookie);
    }

    private void clearTokenCookies(HttpServletResponse response) {
        // Clear access token cookie
        Cookie accessCookie = new Cookie("accessToken", null);
        accessCookie.setHttpOnly(true);
        accessCookie.setSecure(true);
        accessCookie.setPath("/");
        accessCookie.setMaxAge(0);
        response.addCookie(accessCookie);

        // Clear refresh token cookie
        Cookie refreshCookie = new Cookie("refreshToken", null);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(0);
        response.addCookie(refreshCookie);
    }

    private String getRefreshTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}