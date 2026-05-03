package com.ecommerce.entity;

public enum OrderStatus {
    CREATED(1, "CREATED", "Created"),
    CONFIRMED(2, "CONFIRMED", "Confirmed"),
    PACKED(3, "PACKED", "Packed"),
    OUT_FOR_DELIVERY(4, "OUT_FOR_DELIVERY", "Out for delivery"),
    DELIVERED(5, "DELIVERED", "Delivered"),
    CANCELLED(6, "CANCELLED", "Cancelled");

    private final int code;
    private final String type;
    private final String display;

    OrderStatus(int code, String type, String display) {
        this.code = code;
        this.type = type;
        this.display = display;
    }

    public int getCode() {
        return code;
    }

    public String getType() {
        return type;
    }

    public String getDisplay() {
        return display;
    }
}
