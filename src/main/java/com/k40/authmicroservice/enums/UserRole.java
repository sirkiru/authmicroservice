package com.k40.authmicroservice.enums;

import org.springframework.security.core.GrantedAuthority;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    ROLE_ADMIN(Set.of(Permission.values())),

    ROLE_DIRECTOR(Set.of(
            Permission.VIEW_OWN_CLIENT,
            Permission.EDIT_OWN_CLIENT,
            Permission.UPLOAD_SUPPORTING_DOCUMENTS,
            Permission.VIEW_REGISTRATION_STATUS,
            Permission.ADD_DIRECTORS_USERS,
            Permission.DELETE_DIRECTORS_USERS,
            Permission.EDIT_USER_DATA,
            Permission.VIEW_COMPANY_REPORTS)),

    ROLE_USER(Set.of(
            Permission.VIEW_PROFILE,
            Permission.EDIT_PROFILE,
            Permission.VIEW_OWN_COMPANY,
            Permission.VIEW_NOTIFICATIONS));

    private final Set<Permission> permissions;

    public Set<GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(this.name()));

        permissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.name()))
                .forEach(authorities::add);

        return authorities;
    }

    public Set<GrantedAuthority> getGrantedAuthorities() {
        return getAuthorities().stream()
                .map(auth -> (GrantedAuthority) auth)
                .collect(Collectors.toSet());
    }
}
