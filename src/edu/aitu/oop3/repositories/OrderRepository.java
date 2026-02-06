package edu.aitu.oop3.repositories;

import edu.aitu.oop3.db.DatabaseConnection;
import edu.aitu.oop3.factories.DeliveryFactory;
import edu.aitu.oop3.models.DeliveryType;
import edu.aitu.oop3.models.Order;
import edu.aitu.oop3.models.OrderItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderRepository implements Repository<Order, Integer> {

    @Override
    public Order save(Order entity) {
        String orderSql = "INSERT INTO orders (customer_id, status, total_price) VALUES (?, ?, ?)";
        String itemSql = "INSERT INTO order_items (order_id, menu_item_id, quantity, price_at_order) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            // 1. Сохранить заказ
            PreparedStatement orderStmt = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS);
            orderStmt.setInt(1, entity.getCustomerId());
            orderStmt.setString(2, entity.getStatus());
            orderStmt.setDouble(3, entity.getTotalPrice());
            orderStmt.executeUpdate();

            ResultSet keys = orderStmt.getGeneratedKeys();
            if (keys.next()) {
                int orderId = keys.getInt(1);

                // 2. Сохранить товары заказа
                PreparedStatement itemStmt = conn.prepareStatement(itemSql);
                for (OrderItem item : entity.getItems()) {
                    itemStmt.setInt(1, orderId);
                    itemStmt.setInt(2, item.getMenuItemId());
                    itemStmt.setInt(3, item.getQuantity());
                    itemStmt.setDouble(4, item.getPriceAtOrder());
                    itemStmt.addBatch();
                }
                itemStmt.executeBatch();

                // 3. Вернуть заказ с реальным ID
                Order savedOrder = new Order(orderId, entity.getCustomerId(), entity.getDeliveryType());
                savedOrder.setStatus(entity.getStatus());
                entity.getItems().forEach(savedOrder::addItem);
                return savedOrder;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Optional<Order> findById(Integer id) {
        String orderSql = "SELECT * FROM orders WHERE id=?";
        String itemsSql = "SELECT * FROM order_items WHERE order_id=?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            // 1. Загрузить заказ
            PreparedStatement orderStmt = conn.prepareStatement(orderSql);
            orderStmt.setInt(1, id);
            ResultSet rs = orderStmt.executeQuery();

            if (rs.next()) {
                Order order = new Order(rs.getInt("id"), rs.getInt("customer_id"));
                order.setStatus(rs.getString("status"));

                // 2. Загрузить товары заказа
                PreparedStatement itemsStmt = conn.prepareStatement(itemsSql);
                itemsStmt.setInt(1, id);
                ResultSet itemsRs = itemsStmt.executeQuery();

                while (itemsRs.next()) {
                    OrderItem item = new OrderItem(
                            itemsRs.getInt("id"),
                            itemsRs.getInt("order_id"),
                            itemsRs.getInt("menu_item_id"),
                            itemsRs.getInt("quantity"),
                            itemsRs.getDouble("price_at_order")
                    );
                    order.addItem(item);
                }

                return Optional.of(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Order> findAll() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                int orderId = rs.getInt("id");
                int customerId = rs.getInt("customer_id");


                Order order = DeliveryFactory.createOrder(DeliveryType.DELIVERY, orderId, customerId);
                order.setStatus(rs.getString("status"));
                order.setTotalPrice(rs.getDouble("total_price"));


                String itemsSql = "SELECT * FROM order_items WHERE order_id = ?";
                try (PreparedStatement itemStmt = conn.prepareStatement(itemsSql)) {
                    itemStmt.setInt(1, orderId);
                    ResultSet itemRs = itemStmt.executeQuery();
                    while (itemRs.next()) {
                        order.addItem(new OrderItem(
                                itemRs.getInt("id"),
                                orderId,
                                itemRs.getInt("menu_item_id"),
                                itemRs.getInt("quantity"),
                                itemRs.getDouble("price_at_order")
                        ));
                    }
                }


                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    public Order update(Order entity) {
        String sql = "UPDATE orders SET status=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, entity.getStatus());
            stmt.setInt(2, entity.getId());
            stmt.executeUpdate();
            return entity;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM orders WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public int count() {
        String sql = "SELECT COUNT(*) FROM orders";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public boolean existsById(Integer id) {
        String sql = "SELECT 1 FROM orders WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}