package com.k40.authmicroservice.enums;

import java.util.Arrays;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BusinessActivity {
    AGRICULTURE("Agriculture"),
    CONSTRUCTION("Construction"),
    EDUCATION("Education"),
    FINANCIAL_SERVICES("Financial Services"),
    HEALTHCARE("Healthcare"),
    HOSPITALITY("Hospitality"),
    INFORMATION_TECHNOLOGY("Information Technology"),
    MANUFACTURING("Manufacturing"),
    REAL_ESTATE("Real Estate"),
    RETAIL("Retail"),
    TRANSPORTATION("Transportation"),
    WHOLESALE_TRADE("Wholesale Trade"),
    OTHER("Other");

    private final String displayName;

    @Override
    public String toString() {
        return displayName;
    }

    public static BusinessActivity fromDisplayName(String displayName) {
        return Arrays.stream(values())
                .filter(e -> e.displayName.equals(displayName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid business activity"));
    }
}
