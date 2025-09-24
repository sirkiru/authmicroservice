package com.k40.authmicroservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.k40.authmicroservice.enums.Permission;
import com.k40.authmicroservice.enums.RegistrationStatus;
import com.k40.authmicroservice.enums.UserRole;
import com.k40.authmicroservice.enums.UserType;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@Document(collection = "users")
public class User {
    @Id
    private String id;

    // Personal Information
    private String firstName;
    private String middleName;
    private String lastName;

    // Authentication Information
    @Indexed(unique = true)
    private String username;

    @Indexed(unique = true)
    private String email;

    private String password;

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
    // private Country country;
    private String country;

    // Role and Status Information
    private UserType userType;
    private Set<UserRole> roles = new HashSet<>();
    private RegistrationStatus status = RegistrationStatus.PENDING;
    private LocalDateTime registrationDate = LocalDateTime.now();

    // Client Association
    private String clientId;

    // Password Reset
    @Indexed(unique = true, sparse = true)
    private String resetPasswordToken;

    // Consolidated permissions method
    public Set<Permission> getAllPermissions() {
        return roles.stream()
                .flatMap(role -> role.getPermissions().stream())
                .collect(Collectors.toSet());
    }
}
