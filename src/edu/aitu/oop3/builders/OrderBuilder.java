package edu.aitu.oop3.builders;

import edu.aitu.oop3.factories.DeliveryFactory;
import edu.aitu.oop3.models.*;

import java.util.ArrayList;
import java.util.List;

public class OrderBuilder {
    private final int id;
    private int customerId;
    private DeliveryType deliveryType = DeliveryType.DELIVERY;
    private final List<OrderItem> items = new ArrayList<>();
    private String deliveryAddress;
    private int tableNumber;

    public OrderBuilder(int id) {
        this.id = id;
    }

    public OrderBuilder setCustomerId(int customerId) {
        this.customerId = customerId;
        return this;
    }

    public OrderBuilder setDeliveryType(DeliveryType deliveryType) {
        this.deliveryType = deliveryType;
        return this;
    }

    public OrderBuilder addItem(int menuItemId, int quantity, double priceAtOrder) {
        items.add(new OrderItem(items.size() + 1, id, menuItemId, quantity, priceAtOrder));
        return this;
    }

    public OrderBuilder setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
        return this;
    }

    public OrderBuilder setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
        return this;
    }

    public Order build() {
        // Uses Factory to create the correct subtype
        Order order = DeliveryFactory.createOrder(deliveryType, id, customerId);

        // Lambda + method reference: add all items
        items.forEach(order::addItem);

        // Set subtype-specific fields
        if (order instanceof DeliveryOrder && deliveryAddress != null) {
            ((DeliveryOrder) order).setDeliveryAddress(deliveryAddress);
        }

        if (order instanceof DineInOrder) {
            ((DineInOrder) order).setTableNumber(tableNumber);
        }

        return order;
    }
}
