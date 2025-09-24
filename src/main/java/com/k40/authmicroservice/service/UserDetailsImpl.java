package com.k40.authmicroservice.service;

import com.k40.authmicroservice.enums.RegistrationStatus;
import com.k40.authmicroservice.enums.UserType;
import com.k40.authmicroservice.model.User;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserDetailsImpl implements UserDetails {
    private final String id;
    private final String email;
    private final String password;
    private final RegistrationStatus status;
    private final Collection<? extends GrantedAuthority> authorities;
    private final UserType userType;

    public UserDetailsImpl(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.status = user.getStatus();
        this.userType = user.getUserType(); // Capture user type

        this.authorities = user.getRoles().stream()
                .flatMap(role -> role.getAuthorities().stream())
                .collect(java.util.stream.Collectors.toSet());
    }

    public String getId() {
        return id;
    }

    public UserType getUserType() {
        return userType;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !status.equals(RegistrationStatus.SUSPENDED);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return status.equals(RegistrationStatus.APPROVED);
    }
}
