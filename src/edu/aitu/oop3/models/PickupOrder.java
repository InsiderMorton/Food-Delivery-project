package edu.aitu.oop3.models;

import edu.aitu.oop3.config.TaxConfig;

public class PickupOrder extends Order {

    public PickupOrder(int id, int customerId) {
        super(id, customerId, DeliveryType.PICKUP);
    }

    @Override
    public double getTotalPrice() {
        double basePrice = super.getTotalPrice();
        double discount = TaxConfig.getInstance().getPickupDiscount();
        return basePrice * (1 - discount);
    }

    @Override
    public String toString() {
        return "Order #" + getId() +
                " | Customer: " + getCustomerId() +
                " | Subtotal: " + super.getTotalPrice() +
                " | Discount: " + (TaxConfig.getInstance().getPickupDiscount() * 100) + "%" +
                " | Total: " + getTotalPrice() +
                " | Status: " + getStatus() +
                " | Type: Pickup";
    }
}
