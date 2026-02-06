package edu.aitu.oop3.models;

public class DineInOrder extends Order {
    private int tableNumber;

    public DineInOrder(int id, int customerId) {
        super(id, customerId, DeliveryType.DINE_IN);
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    @Override
    public String toString() {
        return "Order #" + getId() +
                " | Customer: " + getCustomerId() +
                " | Total: " + getTotalPrice() +
                " | Status: " + getStatus() +
                " | Type: Dine In | Table #" + tableNumber;
    }
}
