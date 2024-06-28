package com.validation;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public class FindDuplicatesData {
    private static final String URL = "jdbc:sqlite:/home/shatam-100/Down/WaterView_Data/EtoDatabase.db";
//    Set<String> keySet = new HashSet<>();
    static HashSet<String> hs = new HashSet<>();

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL)) {
            if (conn != null) {
                findDuplicatesInAllTables(conn);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        for(String st : hs) {
        	System.out.println(">>>>>>> "+st);
        }
    }

    private static void findDuplicatesInAllTables(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet tables = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table'");

        while (tables.next()) {
            String tableName = tables.getString("name");
            checkForDuplicates(conn, tableName);
        }
    }

    private static void checkForDuplicates(Connection conn, String tableName) throws SQLException {
        String query = String.format(
            "SELECT Date, Tiles, COUNT(*) as count " +
            "FROM %s " +
            "GROUP BY Date, Tiles " +
            "HAVING COUNT(*) > 1", tableName);

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            boolean hasDuplicates = false;
//            System.out.println("Checking table: " + tableName);
            while (rs.next()) {
                hasDuplicates = true;
                String date = rs.getString("Date");
                String tiles = rs.getString("Tiles");
                int count = rs.getInt("count");
//                System.out.printf("Duplicate found - Date: %s, Tiles: %s, Count: %d%n", date, tiles, count);
                hs.add(tableName);
            }
            if (!hasDuplicates) {
//                System.out.println("No duplicates found in table: " + tableName);
            }
        } catch (SQLException e) {
            System.out.println("Error querying table: " + tableName + " - " + e.getMessage());
        }
    }
}
