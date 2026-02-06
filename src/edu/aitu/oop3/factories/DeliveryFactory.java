package edu.aitu.oop3.factories;

import edu.aitu.oop3.models.*;

public class DeliveryFactory {

    // Private constructor - utility class, no instances
    private DeliveryFactory() {}

    public static Order createOrder(DeliveryType type, int id, int customerId) {
        switch (type) {
            case PICKUP:
                return new PickupOrder(id, customerId);
            case DELIVERY:
                return new DeliveryOrder(id, customerId);
            case DINE_IN:
                return new DineInOrder(id, customerId);
            default:
                throw new IllegalArgumentException("Unknown delivery type: " + type);
        }
    }
}
