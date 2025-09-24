package com.k40.authmicroservice.dto;

import lombok.Data;

@Data
public class RefreshTokenResponse {
    private String message;
    private int expiresIn;

    public RefreshTokenResponse(String message, int expiresIn) {
        this.message = message;
        this.expiresIn = expiresIn;
    }
}