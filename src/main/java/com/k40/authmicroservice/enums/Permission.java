package com.k40.authmicroservice.enums;

import org.springframework.security.core.GrantedAuthority;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Permission implements GrantedAuthority { // Implement GrantedAuthority
    // Director permissions
    VIEW_OWN_CLIENT("View Own Client"),
    EDIT_OWN_CLIENT("Edit Own Client"),
    UPLOAD_SUPPORTING_DOCUMENTS("Upload Supporting Documents"),
    VIEW_REGISTRATION_STATUS("View Registration Status"),
    ADD_DIRECTORS_USERS("Add Directors/Users"),
    DELETE_DIRECTORS_USERS("Delete Directors/Users"),
    EDIT_USER_DATA("Edit User Data"),
    VIEW_COMPANY_REPORTS("View Company Reports"),

    // User permissions
    VIEW_PROFILE("View Profile"),
    EDIT_PROFILE("Edit Profile"),
    VIEW_OWN_COMPANY("View Own Company"),
    VIEW_NOTIFICATIONS("View Notifications");

    private final String displayName;

    @Override
    public String getAuthority() {
        return this.name();
    }

    @Override
    public String toString() {
        return displayName;
    }
}