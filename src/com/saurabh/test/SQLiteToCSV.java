package com.saurabh.test;
import java.io.FileWriter;
import java.sql.*;

import com.opencsv.CSVWriter;

public class SQLiteToCSV {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:/home/shatam-100/Down/WaterView_Data/EtoDatabase.db";
        Connection conn = null;

        try {
            // Connect to SQLite database
            conn = DriverManager.getConnection(url);
            DatabaseMetaData metaData = conn.getMetaData();

            // Retrieve all table names
            ResultSet tables = metaData.getTables(null, null, null, new String[]{"TABLE"});
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                System.out.println("Processing table: " + tableName);

                // Fetch data from the current table
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);

                // Write data to CSV
                try (CSVWriter writer = new CSVWriter(new FileWriter("/home/shatam-100/Down/WaterView_Data/DB_SQLITE/" +tableName + ".csv"))) {
                    ResultSetMetaData metaData1 = resultSet.getMetaData();
                    int columnCount = metaData1.getColumnCount();
                    // Write headers
                    String[] headers = new String[columnCount];
                    for (int i = 0; i < columnCount; i++) {
                        headers[i] = metaData1.getColumnName(i + 1);
                    }
                    writer.writeNext(headers);
                    // Write data rows
                    while (resultSet.next()) {
                        String[] row = new String[columnCount];
                        for (int i = 0; i < columnCount; i++) {
                            row[i] = resultSet.getString(i + 1);
                        }
                        writer.writeNext(row);
                    }
                    System.out.println("Data written to " + tableName + ".csv");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
