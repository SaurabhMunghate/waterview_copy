package com.eto_consumption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnectionTest {

    public static void main(String[] args) {
        String jdbcUrl = "jdbc:sqlite:/home/shatam-100/Down/WaterView_Data/Precipitation_all/Precipitation1.db";

        try (Connection connection = DriverManager.getConnection(jdbcUrl)) {
            System.out.println("Connected to SQLite database successfully.");
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
        }
    }
}
