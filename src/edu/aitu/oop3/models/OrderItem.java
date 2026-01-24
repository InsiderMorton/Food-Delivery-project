package edu.aitu.oop3.models;

public class OrderItem {

    private int id;
    private int orderId;
    private int menuItemId;
    private int quantity;
    private double priceAtOrder;

    public OrderItem(int id, int orderId, int menuItemId, int quantity, double priceAtOrder) {
        this.id = id;
        this.orderId = orderId;
        this.menuItemId = menuItemId;
        this.quantity = quantity;
        this.priceAtOrder = priceAtOrder;
    }

    public int getId() { return id; }
    public int getOrderId() { return orderId; }
    public int getMenuItemId() { return menuItemId; }
    public int getQuantity() { return quantity; }
    public double getPriceAtOrder() { return priceAtOrder; }
}

