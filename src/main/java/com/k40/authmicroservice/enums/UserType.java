package com.k40.authmicroservice.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserType {
    DIRECTOR("Director"),
    USER("Standard User"),
    ADMIN("Administrator");

    private final String displayName;

    @Override
    public String toString() {
        return displayName;
    }
}
