package com.ecommerce.entity;

public enum Role {
    CUSTOMER(1, "CUSTOMER", "Customer"),
    ADMIN(2, "ADMIN", "Admin");

    private final int code;
    private final String type;
    private final String displayName;

    Role(int code, String type, String displayName) {
        this.code = code;
        this.type = type;
        this.displayName = displayName;
    }

    public int getCode() {
        return code;
    }

    public String getType() {
        return type;
    }

    public String getDisplayName() {
        return displayName;
    }
}