package edu.aitu.oop3.ordering.models;

import edu.aitu.oop3.delivery.models.DeliveryType;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private int id;
    private int customerId;
    private String status;
    private List<edu.aitu.oop3.ordering.models.OrderItem> items;
    private DeliveryType deliveryType;

    // New constructor with DeliveryType
    public Order(int id, int customerId, DeliveryType deliveryType) {
        this.id = id;
        this.customerId = customerId;
        this.status = "ACTIVE";
        this.items = new ArrayList<>();
        this.deliveryType = deliveryType;
    }

    // Old constructor - backward compatibility (default = DELIVERY)
    public Order(int id, int customerId) {
        this(id, customerId, DeliveryType.DELIVERY);
    }

    public void addItem(OrderItem item) {
        items.add(item);
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public int getId() { return id; }
    public int getCustomerId() { return customerId; }
    public String getStatus() { return status; }
    public DeliveryType getDeliveryType() { return deliveryType; }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotalPrice() {
        return items.stream()
                .mapToDouble(i -> i.getPriceAtOrder() * i.getQuantity())
                .sum();
    }

    @Override
    public String toString() {
        return "Order #" + id +
                " | Customer: " + customerId +
                " | Total: " + getTotalPrice() +
                " | Status: " + status +
                " | Type: " + deliveryType.getDisplayName();
    }
}
