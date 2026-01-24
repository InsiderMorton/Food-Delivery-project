package services;

import exceptions.*;
import models.MenuItem;
import models.Order;
import repositories.MenuRepository;
import repositories.OrderRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class OrderService {
    private final MenuRepository menuRepo = new MenuRepository();
    private final OrderRepository orderRepo = new OrderRepository();

    // User Story 1: Place an Order (Сделать заказ)
    public void placeOrder(int customerId, Map<Integer, Integer> items)
            throws SQLException, MenuItemNotAvailableException, InvalidQuantityException {

        double totalAmount = 0;

        // 1. Валидация
        for (Map.Entry<Integer, Integer> entry : items.entrySet()) {
            int menuId = entry.getKey();
            int quantity = entry.getValue();

            if (quantity <= 0) throw new InvalidQuantityException("Quantity must be positive!");

            MenuItem item = menuRepo.getById(menuId);
            if (item == null) throw new MenuItemNotAvailableException("Item ID " + menuId + " not found.");
            if (!item.isAvailable()) throw new MenuItemNotAvailableException("Item " + item.getName() + " is sold out.");

            totalAmount += item.getPrice() * quantity;
        }

        // 2. Создание заказа
        int orderId = orderRepo.createOrder(customerId, totalAmount);

        // 3. Сохранение деталей
        for (Map.Entry<Integer, Integer> entry : items.entrySet()) {
            MenuItem item = menuRepo.getById(entry.getKey());
            orderRepo.addOrderItem(orderId, item.getId(), entry.getValue(), item.getPrice());
        }

        System.out.println("Order #" + orderId + " placed successfully. Total: " + totalAmount);
    }

    // User Story 2: View Active Orders
    public void viewActiveOrders() {
        List<Order> orders = orderRepo.getActiveOrders();
        System.out.println("--- Active Orders ---");
        for (Order o : orders) {
            System.out.println(o);
        }
    }

    // User Story 3: Complete Order
    public void completeOrder(int orderId) throws SQLException, OrderNotFoundException {
        // В реальности здесь нужно проверить, существует ли заказ в БД
        orderRepo.updateStatus(orderId, "COMPLETED");
        System.out.println("Order #" + orderId + " marked as COMPLETED.");
    }
}