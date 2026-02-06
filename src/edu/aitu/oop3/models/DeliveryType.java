package edu.aitu.oop3.models;

public enum DeliveryType {
    PICKUP("Pickup"),
    DELIVERY("Delivery"),
    DINE_IN("Dine In");

    private final String displayName;

    DeliveryType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
