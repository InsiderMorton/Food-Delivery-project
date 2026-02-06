package edu.aitu.oop3.models;

import edu.aitu.oop3.config.TaxConfig;

public class DeliveryOrder extends Order {
    private String deliveryAddress;

    public DeliveryOrder(int id, int customerId) {
        super(id, customerId, DeliveryType.DELIVERY);
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    @Override
    public double getTotalPrice() {
        return super.getTotalPrice() + TaxConfig.getInstance().getDeliveryFee();
    }

    @Override
    public String toString() {
        return "Order #" + getId() +
                " | Customer: " + getCustomerId() +
                " | Subtotal: " + super.getTotalPrice() +
                " | Delivery Fee: " + TaxConfig.getInstance().getDeliveryFee() +
                " | Total: " + getTotalPrice() +
                " | Status: " + getStatus() +
                " | Type: Delivery to " + deliveryAddress;
    }
}
