package com.validation;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UniqueDateCounter {

    public static void main(String[] args) {
        // Path to your SQLite database
        String url = "jdbc:sqlite:path/to/your/database.db";

        // SQL query to get unique dates with their counts
        String sql = "SELECT Date, COUNT(*) as count FROM your_table GROUP BY Date";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Loop through the result set and print the unique dates with their counts
            while (rs.next()) {
                String date = rs.getString("Date");
                int count = rs.getInt("count");
                System.out.println("Date: " + date + ", Count: " + count);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
