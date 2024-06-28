package com.saurabh.test;
import java.io.*;
import java.sql.*;

public class SQLiteBackup {
    // SQLite database URL
    private static final String url = "jdbc:sqlite:path_to_your_database.db";

    // Output file path
    private static final String outputFilePath = "backup.sql";

    public static void main(String[] args) {
        try {
            // Open output file
            PrintWriter writer = new PrintWriter(new FileWriter(outputFilePath));

            // Establish connection to the SQLite database
            try (Connection conn = DriverManager.getConnection(url)) {
                // Get a list of all tables
                DatabaseMetaData meta = conn.getMetaData();
                ResultSet tables = meta.getTables(null, null, null, new String[]{"TABLE"});

                // Iterate through each table
                while (tables.next()) {	
                    String tableName = tables.getString("TABLE_NAME");
                    writer.println("-- Table: " + tableName);
                    System.out.println("-- Table: " + tableName);

                    // Fetch data from the table
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);

                    // Iterate through the result set and write data to the output file
                    ResultSetMetaData rsmd = rs.getMetaData();
                    int numColumns = rsmd.getColumnCount();
//                    while (rs.next()) {
//                        writer.print("INSERT INTO " + tableName + " VALUES (");
//                        for (int i = 1; i <= numColumns; i++) {
//                            if (i > 1) {
//                                writer.print(", ");
//                            }
//                            writer.print("'" + rs.getString(i) + "'");
//                        }
//                        writer.println(");");
//                    }
                    rs.close();
                    stmt.close();
                    writer.println();
                }
            }

            // Close output file
            writer.close();
            System.out.println("Backup completed successfully. Data saved to: " + outputFilePath);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
