package edu.aitu.oop3.db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class Database_connection {
    private static final String URL =
            "jdbc:postgresql://aws-1-ap-northeast-2.pooler.supabase.com:5432/postgres";
    private static final String USER = "postgres.yncbsdetxhyvpvgdahcm";
    private static final String PASSWORD = "Maga2708!!!!"; // ← DATABASE PASSWORD
    private Database_connection() {
        // no instances
    }
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}