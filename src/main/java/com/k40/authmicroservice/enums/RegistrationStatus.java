package com.k40.authmicroservice.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RegistrationStatus {
    PENDING("Pending"),
    APPROVED("Approved"),
    REJECTED("Rejected"),
    SUSPENDED("Suspended");

    private final String displayName;

    @Override
    public String toString() {
        return displayName;
    }
}