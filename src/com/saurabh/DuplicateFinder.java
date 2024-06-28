package com.saurabh;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;

public class DuplicateFinder {

    public static void main(String[] args) {
        String url = "jdbc:sqlite:/path/to/your/database.db"; // Replace with your database path
        String csvFilePath = "/path/to/output/duplicates.csv"; // Replace with your desired CSV output path

        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement();
             FileWriter csvWriter = new FileWriter(csvFilePath)) {

            // SQL query to find duplicates based on Date and Tiles
            String query = "SELECT Date, Tiles, COUNT(*) as count FROM your_table_name GROUP BY Date, Tiles HAVING count > 1";

            ResultSet resultSet = statement.executeQuery(query);

            // Write header to CSV file
            csvWriter.append("Date,Tiles\n");

            // Write duplicate data to CSV file
            while (resultSet.next()) {
                String date = resultSet.getString("Date");
                String tiles = resultSet.getString("Tiles");

                // Write duplicate data to CSV file
                csvWriter.append(date).append(",").append(tiles).append("\n");
            }

            System.out.println("Duplicate data has been written to " + csvFilePath);

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
