package com.k40.authmicroservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserRequest {
    @NotBlank
    private String firstName;

    private String middleName;

    @NotBlank
    private String lastName;

    @NotBlank
    @Email
    private String email;

    private String idPassportNumber;

    private String kraPin;

    private String nationality;

    private String phoneNumber;

    private String physicalAddress;

    private String city;

    // private Country country;
    private String country;

    private String clientId;
}
