package edu.aitu.oop3;

import edu.aitu.oop3.builders.OrderBuilder;
import edu.aitu.oop3.config.TaxConfig;
import edu.aitu.oop3.factories.DeliveryFactory;
import edu.aitu.oop3.models.*;
import edu.aitu.oop3.repositories.MenuRepository;
import edu.aitu.oop3.services.OrderService;

import java.util.List;

public class App {
    public static void run() {
        OrderService service = new OrderService();

        // ============================================================
        // DATABASE CHECK - убедимся что данные есть
        // ============================================================
        System.out.println("===== DATABASE CHECK =====");
        try {
            MenuRepository menuRepo = new MenuRepository();
            List<MenuItem> allItems = menuRepo.findAll();
            System.out.println("Items in database: " + allItems.size());
            allItems.forEach(item -> System.out.println("  - " + item.getName()));
            System.out.println();
        } catch (Exception e) {
            System.err.println("DATABASE ERROR: " + e.getMessage());
            return;
        }

        // ============================================================
        // 1. SINGLETON — TaxConfig
        // One instance shared across the entire application
        // ============================================================
        System.out.println("===== SINGLETON: TaxConfig =====");
        TaxConfig config1 = TaxConfig.getInstance();
        TaxConfig config2 = TaxConfig.getInstance();
        System.out.println("Tax Rate:         " + (config1.getTaxRate() * 100) + "%");
        System.out.println("Delivery Fee:     " + config1.getDeliveryFee());
        System.out.println("Pickup Discount:  " + (config1.getPickupDiscount() * 100) + "%");
        System.out.println("Same instance:    " + (config1 == config2));
        System.out.println();

        // ============================================================
        // 2. FACTORY — DeliveryFactory
        // Creates correct subtype based on DeliveryType enum
        // ============================================================
        System.out.println("===== FACTORY: DeliveryFactory =====");
        Order factoryPickup   = DeliveryFactory.createOrder(DeliveryType.PICKUP, 100, 1);
        Order factoryDelivery = DeliveryFactory.createOrder(DeliveryType.DELIVERY, 101, 1);
        Order factoryDineIn   = DeliveryFactory.createOrder(DeliveryType.DINE_IN, 102, 1);
        System.out.println("Created: " + factoryPickup.getClass().getSimpleName());
        System.out.println("Created: " + factoryDelivery.getClass().getSimpleName());
        System.out.println("Created: " + factoryDineIn.getClass().getSimpleName());
        System.out.println();

        // ============================================================
        // 3. BUILDER — OrderBuilder
        // Builds orders step by step, uses Factory internally
        // ============================================================
        System.out.println("===== BUILDER: OrderBuilder =====");

        Order deliveryOrder = new OrderBuilder(1)
                .setCustomerId(1)
                .setDeliveryType(DeliveryType.DELIVERY)
                .addItem(1, 2, 2500)  // Pizza x2
                .addItem(2, 1, 1200)  // Burger x1
                .setDeliveryAddress("123 Main Street")
                .build();
        System.out.println("Built Delivery Order: " + deliveryOrder);

        Order pickupOrder = new OrderBuilder(2)
                .setCustomerId(2)
                .setDeliveryType(DeliveryType.PICKUP)
                .addItem(1, 3, 2500)  // Pizza x3
                .build();
        System.out.println("Built Pickup Order:   " + pickupOrder);

        Order dineInOrder = new OrderBuilder(3)
                .setCustomerId(3)
                .setDeliveryType(DeliveryType.DINE_IN)
                .addItem(2, 2, 1200)  // Burger x2
                .addItem(1, 1, 2500)  // Pizza x1
                .setTableNumber(7)
                .build();
        System.out.println("Built DineIn Order:   " + dineInOrder);
        System.out.println();

        // ============================================================
        // 4. GENERICS + REPOSITORY INTEGRATION
        // Service methods return Result<Order>
        // Save all 3 orders to database
        // ============================================================
        System.out.println("===== GENERICS: Result<T> + Repository Integration =====");

        try {
            // Place all 3 orders in database
            System.out.println("--- Placing Delivery Order ---");
            Result<Order> deliveryResult = service.placeOrder(deliveryOrder);
            System.out.println(deliveryResult);
            System.out.println();

            System.out.println("--- Placing Pickup Order ---");
            Result<Order> pickupResult = service.placeOrder(pickupOrder);
            System.out.println(pickupResult);
            System.out.println();

            System.out.println("--- Placing DineIn Order ---");
            Result<Order> dineInResult = service.placeOrder(dineInOrder);
            System.out.println(dineInResult);
            System.out.println();

            // View all active orders from database
            System.out.println("--- Active Orders from Database ---");
            service.viewActiveOrders().forEach(System.out::println);
            System.out.println();

            // Complete the delivery order
            if (deliveryResult.isSuccess()) {
                System.out.println("--- Completing Delivery Order ---");
                Result<Order> completeResult = service.completeOrder(deliveryResult.getData().getId());
                System.out.println(completeResult);
                System.out.println();
            }

            // View active orders again (delivery should be completed)
            System.out.println("--- Active Orders After Completion ---");
            service.viewActiveOrders().forEach(System.out::println);
            System.out.println();

        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }

        // ============================================================
        // 5. LAMBDAS — Stream filtering, sorting, aggregation
        // Working with in-memory list for demonstration
        // ============================================================
        System.out.println("===== LAMBDAS: Stream Operations =====");

        // Get all orders from database
        List<Order> allOrders = service.viewActiveOrders();

        System.out.println("--- Filter: only PICKUP orders ---");
        allOrders.stream()
                .filter(o -> o.getDeliveryType() == DeliveryType.PICKUP)
                .forEach(o -> System.out.println("  " + o));

        System.out.println("\n--- Sorted by price (high → low) ---");
        allOrders.stream()
                .sorted((a, b) -> Double.compare(b.getTotalPrice(), a.getTotalPrice()))
                .forEach(o -> System.out.println("  " + o));

        System.out.println("\n--- Total revenue from active orders ---");
        double totalRevenue = allOrders.stream()
                .mapToDouble(Order::getTotalPrice)
                .sum();
        System.out.println("  Total: " + totalRevenue);
    }
}