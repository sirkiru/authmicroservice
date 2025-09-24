package com.k40.authmicroservice.dto;

import java.util.List;

import com.k40.authmicroservice.enums.UserType;

import lombok.Data;

@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String refreshToken;
    private String id;
    private String username;
    private String email;
    private UserType userType;
    private List<String> roles;

    public JwtResponse(String token, String refreshToken, String id,
            String username, String email, UserType userType,
            List<String> roles) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.userType = userType;
        this.roles = roles;
    }
}