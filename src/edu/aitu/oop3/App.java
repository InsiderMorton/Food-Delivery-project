package edu.aitu.oop3;

import edu.aitu.oop3.delivery.models.DeliveryType;
import edu.aitu.oop3.ordering.builders.OrderBuilder;
import edu.aitu.oop3.billing.config.TaxConfig;
import edu.aitu.oop3.delivery.factories.DeliveryFactory;
import edu.aitu.oop3.ordering.models.*;
import edu.aitu.oop3.ordering.services.OrderService;
import edu.aitu.oop3.shared.models.Result;

import java.util.Arrays;
import java.util.List;

public class App {
    public static void run() {
        OrderService service = new OrderService();



        // One instance shared across the entire application

        System.out.println("===== SINGLETON: TaxConfig =====");
        TaxConfig config1 = TaxConfig.getInstance();
        TaxConfig config2 = TaxConfig.getInstance();
        System.out.println("Tax Rate:         " + (config1.getTaxRate() * 100) + "%");
        System.out.println("Delivery Fee:     " + config1.getDeliveryFee());
        System.out.println("Pickup Discount:  " + (config1.getPickupDiscount() * 100) + "%");
        System.out.println("Same instance:    " + (config1 == config2));
        System.out.println();



        // Creates correct subtype based on DeliveryType enum

        System.out.println("===== FACTORY: DeliveryFactory =====");
        Order factoryPickup   = DeliveryFactory.createOrder(DeliveryType.PICKUP, 100, 1);
        Order factoryDelivery = DeliveryFactory.createOrder(DeliveryType.DELIVERY, 101, 1);
        Order factoryDineIn   = DeliveryFactory.createOrder(DeliveryType.DINE_IN, 102, 1);
        System.out.println("Created: " + factoryPickup.getClass().getSimpleName());
        System.out.println("Created: " + factoryDelivery.getClass().getSimpleName());
        System.out.println("Created: " + factoryDineIn.getClass().getSimpleName());
        System.out.println();



        // Builds orders step by step, uses Factory internally

        System.out.println("===== BUILDER: OrderBuilder =====");

        Order deliveryOrder = new OrderBuilder(1)
                .setCustomerId(1)
                .setDeliveryType(DeliveryType.DELIVERY)
                .addItem(1, 2, 2500)
                .addItem(2, 1, 1200)
                .setDeliveryAddress("123 Main Street")
                .build();
        System.out.println("Delivery: " + deliveryOrder);

        Order pickupOrder = new OrderBuilder(2)
                .setCustomerId(2)
                .setDeliveryType(DeliveryType.PICKUP)
                .addItem(1, 3, 2500)
                .build();
        System.out.println("Pickup:   " + pickupOrder);

        Order dineInOrder = new OrderBuilder(3)
                .setCustomerId(3)
                .setDeliveryType(DeliveryType.DINE_IN)
                .addItem(2, 2, 1200)
                .addItem(1, 1, 2500)
                .setTableNumber(7)
                .build();
        System.out.println("DineIn:   " + dineInOrder);
        System.out.println();



        // Service methods now return Result<Order>

        System.out.println("===== GENERICS: Result<T> =====");
        try {
            Result<Order> placeResult = service.placeOrder(deliveryOrder);
            System.out.println(placeResult);

            if (placeResult.isSuccess()) {
                Order saved = placeResult.getData();
                Result<Order> completeResult = service.completeOrder(saved.getId());
                System.out.println(completeResult);
            }
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
        }
        System.out.println();


        // 5. LAMBDAS — Stream filtering, sorting, aggregation

        System.out.println("===== LAMBDAS: Stream Operations =====");
        List<Order> allOrders = Arrays.asList(deliveryOrder, pickupOrder, dineInOrder);

        System.out.println("--- Filter: only PICKUP orders ---");
        allOrders.stream()
                .filter(o -> o.getDeliveryType() == DeliveryType.PICKUP)
                .forEach(o -> System.out.println("  " + o));

        System.out.println("--- Sorted by price (high → low) ---");
        allOrders.stream()
                .sorted((a, b) -> Double.compare(b.getTotalPrice(), a.getTotalPrice()))
                .forEach(o -> System.out.println("  " + o));

        System.out.println("--- Total revenue across all orders ---");
        double totalRevenue = allOrders.stream()
                .mapToDouble(Order::getTotalPrice)
                .sum();
        System.out.println("  Total: " + totalRevenue);
    }
}
