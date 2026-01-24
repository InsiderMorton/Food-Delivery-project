package models;

public class Order {
    private int id;
    private int customerId;
    private double totalPrice;
    private String status;

    public Order(int id, int customerId, double totalPrice, String status) {
        this.id = id;
        this.customerId = customerId;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public int getId() { return id; }
    public double getTotalPrice() { return totalPrice; }
    public String getStatus() { return status; }

    @Override
    public String toString() {
        return "Order #" + id + " | Customer: " + customerId + " | Total: " + totalPrice + " | Status: " + status;
    }
}