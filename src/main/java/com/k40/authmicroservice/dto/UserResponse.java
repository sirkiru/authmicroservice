package com.k40.authmicroservice.dto;

import com.k40.authmicroservice.model.User;
import lombok.Data;

@Data
public class UserResponse {
    // Personal Information
    private String id;
    private String firstName;
    private String middleName;
    private String lastName;

    // Authentication Information
    private String username;
    private String email;

    // Identification Information
    private String idPassportNumber;
    private String kraPin;
    private String nationality;
    private Boolean isKenyan;

    // Contact Information
    private String phonePrefix;
    private String phoneNumber;
    private String physicalAddress;
    private String city;
    private String postalAddress;
    private String country;

    // Role and Status Information
    private String userType;
    private String status;
    private String clientId;

    public static UserResponse fromEntity(User user) {
        UserResponse response = new UserResponse();
        
        // Personal Information
        response.setId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setMiddleName(user.getMiddleName());
        response.setLastName(user.getLastName());
        
        // Authentication Information
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        
        // Identification Information
        response.setIdPassportNumber(user.getIdPassportNumber());
        response.setKraPin(user.getKraPin());
        response.setNationality(user.getNationality());
        response.setIsKenyan(user.getIsKenyan());
        
        // Contact Information
        response.setPhonePrefix(user.getPhonePrefix());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setPhysicalAddress(user.getPhysicalAddress());
        response.setCity(user.getCity());
        response.setPostalAddress(user.getPostalAddress());
        response.setCountry(user.getCountry());
        
        // Role and Status Information
        response.setUserType(user.getUserType() != null ? user.getUserType().toString() : null);
        response.setStatus(user.getStatus() != null ? user.getStatus().toString() : null);
        response.setClientId(user.getClientId());
        
        return response;
    }
}