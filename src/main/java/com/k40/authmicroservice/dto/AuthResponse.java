package com.k40.authmicroservice.dto;

import lombok.Data;

import java.util.List;

import com.k40.authmicroservice.enums.UserType;

@Data
public class AuthResponse {
    private String id;
    private String username;
    private String email;
    private UserType userType;
    private List<String> roles;

    public AuthResponse(String id, String username, String email,
            UserType userType, List<String> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.userType = userType;
        this.roles = roles;
    }
}
