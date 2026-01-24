package edu.aitu.oop3.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Renamed from Database_connection to DatabaseConnection to match Main.java
public class DatabaseConnection {

    // Your Supabase credentials
    private static final String URL = "jdbc:postgresql://aws-1-ap-northeast-2.pooler.supabase.com:5432/postgres";
    private static final String USER = "postgres.yncbsdetxhyvpvgdahcm";
    private static final String PASSWORD = "Maga2708!!!!";

    private DatabaseConnection() {
        // Private constructor to prevent instantiation
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}