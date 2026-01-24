package edu.aitu.oop3.repositories;

import edu.aitu.oop3.db.DatabaseConnection;
import edu.aitu.oop3.models.MenuItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MenuRepository implements Repository<MenuItem, Integer> {

    @Override
    public MenuItem save(MenuItem entity) {
        String sql = "INSERT INTO menu_items (name, amount, price, is_available) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, entity.getName());
            stmt.setInt(2, entity.getAmount());
            stmt.setDouble(3, entity.getPrice());
            stmt.setBoolean(4, entity.isAvailable());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                return new MenuItem(
                        keys.getInt(1),
                        entity.getName(),
                        entity.getAmount(),
                        entity.getPrice(),
                        entity.isAvailable()
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Optional<MenuItem> findById(Integer id) {
        String sql = "SELECT * FROM menu_items WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(new MenuItem(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("amount"),
                        rs.getDouble("price"),
                        rs.getBoolean("is_available")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<MenuItem> findAll() {
        List<MenuItem> items = new ArrayList<>();
        String sql = "SELECT * FROM menu_items";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                items.add(new MenuItem(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("amount"),
                        rs.getDouble("price"),
                        rs.getBoolean("is_available")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    @Override
    public MenuItem update(MenuItem entity) {
        String sql = "UPDATE menu_items SET name=?, amount=?, price=?, is_available=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, entity.getName());
            stmt.setInt(2, entity.getAmount());
            stmt.setDouble(3, entity.getPrice());
            stmt.setBoolean(4, entity.isAvailable());
            stmt.setInt(5, entity.getId());
            stmt.executeUpdate();
            return entity;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM menu_items WHERE id=?";
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
        String sql = "SELECT COUNT(*) FROM menu_items";
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
        String sql = "SELECT 1 FROM menu_items WHERE id=?";
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
