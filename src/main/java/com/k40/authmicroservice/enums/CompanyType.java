package com.k40.authmicroservice.enums;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum CompanyType {
    LIMITED_COMPANY("Limited Company"),
    SOLE_PROPRIETORSHIP("Sole Proprietorship"),
    PARTNERSHIP("Partnership"),
    NGO("Non-Governmental Organization (NGO)"),
    OTHER("Other");

    private final String displayName;

    CompanyType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    public static CompanyType fromDisplayName(String displayName) {
        return Arrays.stream(CompanyType.values())
                .filter(e -> e.displayName.equalsIgnoreCase(displayName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Invalid company type: " + displayName + ". Valid types are: " +
                                Arrays.stream(values()).map(CompanyType::getDisplayName).toList()));
    }
}