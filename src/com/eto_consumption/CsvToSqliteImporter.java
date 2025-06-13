package com.eto_consumption;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;

public class CsvToSqliteImporter {
    public static void main(String[] args) {
        String csvFilePath = "/home/shatam-100/Down/WaterView_Data/FTP_DATA/Data_Folder_2025-02-15/WVVACAVILLECIT374/cii_consumption_eto.csv";  // Update this path
        String sqliteUrl = "jdbc:sqlite:/home/shatam-100/Down/WaterView_Data/Precipitation_all/Precipitation1.db";  // Update this path

        try (Connection conn = DriverManager.getConnection(sqliteUrl)) {
            if (conn != null) {
                System.out.println("Connected to SQLite.");

                // Step 1: Read the CSV header (first row)
                BufferedReader reader = new BufferedReader(new FileReader(csvFilePath));
                String headerLine = reader.readLine();
                if (headerLine == null) {
                    System.out.println("CSV file is empty.");
                    return;
                }

                String[] columns = headerLine.split(",");
                
                // Step 2: Create table if it does not exist
                String tableName = "your_table_name";
                createTable(conn, tableName, columns);

                // Step 3: Insert the data
                String insertSql = buildInsertSql(tableName, columns);
                PreparedStatement preparedStatement = conn.prepareStatement(insertSql);

                String row;
                while ((row = reader.readLine()) != null) {
                    String[] values = row.split(",");
                    for (int i = 0; i < values.length; i++) {
                        preparedStatement.setString(i + 1, values[i].trim());
                    }
                    preparedStatement.addBatch();
                }

                preparedStatement.executeBatch();
                System.out.println("CSV data imported successfully.");
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTable(Connection conn, String tableName, String[] columns) throws SQLException {
        StringBuilder createTableSql = new StringBuilder("CREATE TABLE IF NOT EXISTS " + tableName + " (");
        for (String column : columns) {
            createTableSql.append(column.trim()).append(" TEXT, ");
        }
        createTableSql.setLength(createTableSql.length() - 2);  // Remove trailing comma and space
        createTableSql.append(");");

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSql.toString());
        }
    }

    private static String buildInsertSql(String tableName, String[] columns) {
        StringBuilder insertSql = new StringBuilder("INSERT INTO " + tableName + " (");
        for (String column : columns) {
            insertSql.append(column.trim()).append(", ");
        }
        insertSql.setLength(insertSql.length() - 2);  // Remove trailing comma and space
        insertSql.append(") VALUES (");
        for (int i = 0; i < columns.length; i++) {
            insertSql.append("?, ");
        }
        insertSql.setLength(insertSql.length() - 2);  // Remove trailing comma and space
        insertSql.append(");");
        return insertSql.toString();
    }
}
