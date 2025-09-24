package com.k40.authmicroservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterCompanyRequest {

    @NotBlank
    private String companyName;

    @NotBlank
    private String companyNumber;

    // private CompanyType companyType;
    private String companyType;

    // private BusinessActivity businessActivity;
    private String businessActivity;

    private String kraPin;

    private String physicalAddress;

    private String city;

    // private Country country;
    private String country;

    private String firstName;

    private String lastName;

    @Email
    private String email;

    private String phoneNumber;

    private String idPassportNumber;

    @NotBlank
    private String userKraPin;

}
