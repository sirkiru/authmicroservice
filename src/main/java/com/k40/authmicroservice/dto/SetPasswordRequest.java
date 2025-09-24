package com.k40.authmicroservice.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SetPasswordRequest {
    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    @NotBlank
    private String confirmPassword;

    @NotBlank
    private String token;

    @AssertTrue(message = "Passwords must match")
    public boolean isPasswordMatch() {
        return password != null && password.equals(confirmPassword);
    }
}
