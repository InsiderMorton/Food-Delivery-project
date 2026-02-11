package edu.aitu.oop3.ordering.services;

import edu.aitu.oop3.billing.config.TaxConfig;
import edu.aitu.oop3.menu.models.MenuItem;
import edu.aitu.oop3.shared.exceptions.InvalidQuantityException;
import edu.aitu.oop3.shared.exceptions.MenuItemNotAvailableException;
import edu.aitu.oop3.shared.exceptions.OrderNotFoundException;
import edu.aitu.oop3.ordering.models.*;
import edu.aitu.oop3.menu.repositories.MenuRepository;
import edu.aitu.oop3.ordering.repositories.OrderRepository;
import edu.aitu.oop3.shared.models.Result;

import java.util.List;
import java.util.stream.Collectors;

public class OrderService {
    private final MenuRepository menuRepo = new MenuRepository();
    private final OrderRepository orderRepo = new OrderRepository();

    // Returns Result<Order> instead of plain Order (Generics)
    public Result<Order> placeOrder(Order order)
            throws InvalidQuantityException, MenuItemNotAvailableException {

        if (order.getItems().isEmpty()) {
            throw new InvalidQuantityException("Order must contain at least one item");
        }

        for (OrderItem item : order.getItems()) {
            if (item.getQuantity() <= 0) {
                throw new InvalidQuantityException("Invalid quantity for item: " + item.getMenuItemId());
            }

            MenuItem menuItem = menuRepo.findById(item.getMenuItemId())
                    .orElseThrow(() ->
                            new MenuItemNotAvailableException("Menu item not found: " + item.getMenuItemId())
                    );

            if (!menuItem.isAvailable()) {
                throw new MenuItemNotAvailableException("Menu item is not available: " + menuItem.getName());
            }

            if (item.getQuantity() > menuItem.getAmount()) {
                throw new InvalidQuantityException("Not enough stock for: " + menuItem.getName());
            }
        }

        Order savedOrder = orderRepo.save(order);

        // Singleton TaxConfig for price breakdown
        TaxConfig taxConfig = TaxConfig.getInstance();
        double subtotal = savedOrder.getTotalPrice();
        double tax = taxConfig.calculateTax(subtotal);

        System.out.println("Order #" + savedOrder.getId() + " placed.");
        System.out.println("  Type: " + savedOrder.getDeliveryType().getDisplayName());
        System.out.println("  Subtotal: " + subtotal);
        System.out.println("  Tax (" + (taxConfig.getTaxRate() * 100) + "%): " + tax);
        System.out.println("  Total with tax: " + taxConfig.calculateTotal(subtotal));

        return Result.ok(savedOrder);
    }

    // Lambda + Stream: filter active orders
    public List<Order> viewActiveOrders() {
        return orderRepo.findAll()
                .stream()
                .filter(o -> "ACTIVE".equals(o.getStatus()))
                .collect(Collectors.toList());
    }

    // Returns Result<Order>
    public Result<Order> completeOrder(int orderId) throws OrderNotFoundException {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() ->
                        new OrderNotFoundException("Order not found: " + orderId)
                );

        order.setStatus("COMPLETED");
        orderRepo.update(order);

        System.out.println("Order #" + orderId + " completed");
        return Result.ok(order);
    }
}
