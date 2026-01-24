package edu.aitu.oop3.services;

import edu.aitu.oop3.exceptions.InvalidQuantityException;
import edu.aitu.oop3.exceptions.MenuItemNotAvailableException;
import edu.aitu.oop3.exceptions.OrderNotFoundException;
import edu.aitu.oop3.models.MenuItem;
import edu.aitu.oop3.models.Order;
import edu.aitu.oop3.models.OrderItem;
import edu.aitu.oop3.repositories.MenuRepository;
import edu.aitu.oop3.repositories.OrderRepository;

import java.util.List;
import java.util.stream.Collectors;

public class OrderService {

    private final MenuRepository menuRepo = new MenuRepository();
    private final OrderRepository orderRepo = new OrderRepository();

    // User Story 1: Place Order
    public Order placeOrder(Order order)
            throws InvalidQuantityException, MenuItemNotAvailableException {

        if (order.getItems().isEmpty()) {
            throw new InvalidQuantityException("Order must contain at least one item");
        }

        for (OrderItem item : order.getItems()) {

            if (item.getQuantity() <= 0) {
                throw new InvalidQuantityException("Invalid quantity");
            }

            MenuItem menuItem = menuRepo.findById(item.getMenuItemId())
                    .orElseThrow(() ->
                            new MenuItemNotAvailableException("Menu item not found")
                    );

            if (!menuItem.isAvailable()) {
                throw new MenuItemNotAvailableException("Menu item is not available");
            }

            if (item.getQuantity() > menuItem.getAmount()) {
                throw new InvalidQuantityException("Not enough items in stock");
            }
        }

        Order savedOrder = orderRepo.save(order);

        System.out.println("Order #" + savedOrder.getId() +
                " placed. Total: " + savedOrder.getTotalPrice());

        return savedOrder;
    }

    // User Story 2: View Active Orders
    public List<Order> viewActiveOrders() {
        return orderRepo.findAll()
                .stream()
                .filter(o -> "ACTIVE".equals(o.getStatus()))
                .collect(Collectors.toList());
    }

    // User Story 3: Complete Order
    public void completeOrder(int orderId) throws OrderNotFoundException {

        Order order = orderRepo.findById(orderId)
                .orElseThrow(() ->
                        new OrderNotFoundException("Order not found")
                );

        order.setStatus("COMPLETED");
        orderRepo.update(order);

        System.out.println("Order #" + orderId + " completed");
    }
}
