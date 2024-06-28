package com.validation;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class DateCounterToCSV {
	static HashSet<String> hs = new HashSet<>();

    public static void main(String[] args) {
        String url = "jdbc:sqlite:/home/shatam-100/Down/WaterView_Data/EtoDatabase.db";
        String csvFile = "/home/shatam-100/Down/WaterView_Data/output.csv";

        try (Connection conn = DriverManager.getConnection(url);
             FileWriter fw = new FileWriter(csvFile);
             PrintWriter pw = new PrintWriter(fw)) {

            DatabaseMetaData meta = conn.getMetaData();
            ResultSet tables = meta.getTables(null, null, "%", new String[]{"TABLE"});
            pw.println("Table,Date,Count"); // Write CSV header

            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                if (hasDateColumn(conn, tableName)) {
                    writeDateCountsToCSV(conn, tableName, pw);
                }
            }

            System.out.println("CSV file has been written successfully.");
            for(String st : hs) {
            	System.out.println(st);
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        System.out.println(csvFile);
    }

    private static boolean hasDateColumn(Connection conn, String tableName) throws SQLException {
        DatabaseMetaData meta = conn.getMetaData();
        ResultSet columns = meta.getColumns(null, null, tableName, "Date");
        return columns.next();
    }

    private static void writeDateCountsToCSV(Connection conn, String tableName, PrintWriter pw) {
        String sql = "SELECT Date, COUNT(*) as count FROM " + tableName + " GROUP BY Date";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String date = rs.getString("Date");
                int count = rs.getInt("count");
                pw.println(tableName+","+date + "," + count);
                hs.add(tableName+"_"+"" + "_" + count);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
