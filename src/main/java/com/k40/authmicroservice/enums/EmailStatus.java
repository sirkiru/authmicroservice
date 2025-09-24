package com.k40.authmicroservice.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EmailStatus {
    PENDING("Pending"),
    SENT("Sent"),
    DELIVERED("Delivered"),
    FAILED("Failed");

    private final String displayName;
}
