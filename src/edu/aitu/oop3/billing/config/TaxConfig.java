package edu.aitu.oop3.billing.config;

public class TaxConfig {
    private static TaxConfig instance;

    private double taxRate;
    private double deliveryFee;
    private double pickupDiscount;

    // Private constructor - cannot create via new TaxConfig()
    private TaxConfig() {
        this.taxRate = 0.12;          // 12% tax
        this.deliveryFee = 500.0;     // flat delivery fee
        this.pickupDiscount = 0.05;   // 5% discount for pickup
    }

    // Single entry point - always returns the same instance
    public static TaxConfig getInstance() {
        if (instance == null) {
            instance = new TaxConfig();
        }
        return instance;
    }

    public double getTaxRate() { return taxRate; }
    public void setTaxRate(double taxRate) { this.taxRate = taxRate; }

    public double getDeliveryFee() { return deliveryFee; }
    public void setDeliveryFee(double deliveryFee) { this.deliveryFee = deliveryFee; }

    public double getPickupDiscount() { return pickupDiscount; }
    public void setPickupDiscount(double pickupDiscount) { this.pickupDiscount = pickupDiscount; }

    public double calculateTax(double amount) {
        return amount * taxRate;
    }

    public double calculateTotal(double amount) {
        return amount + calculateTax(amount);
    }
}
