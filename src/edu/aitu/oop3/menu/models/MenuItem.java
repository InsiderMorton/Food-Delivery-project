package edu.aitu.oop3.menu.models;

public class MenuItem {
    private int id;
    private String name;
    private int amount;
    private double price;
    private boolean available;

    public MenuItem(int id, String name, int amount, double price, boolean available) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.price = price;
        this.available = available;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getAmount() { return amount; }
    public double getPrice() { return price; }
    public boolean isAvailable() { return available; }

    public void setAmount(int amount) {
        this.amount = amount;
        if (amount <= 0) {
            this.available = false;
        }
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void setPrice(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
        this.price = price;
    }
}
