package com.eto_consumption;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class GetUsersDistrict {

    private static final String DB_URL_POSTGRES = "jdbc:postgresql://backend-systems-pg.postgres.database.azure.com:5432/backend_systems?user=milanadmin&password=MyiUr9peJo2ppaMaReSlrF$12&sslmode=require";
    private final static String USER_POSTGRES = "milanadmin";
    private final static String PASSWORD_POSTGRES = "budalica$1";

    
    public static void main(String[] args) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        
        try {
            // Load PostgreSQL JDBC Driver
            Class.forName("org.postgresql.Driver");

            // Establish Connection
            connection = DriverManager.getConnection(DB_URL_POSTGRES, USER_POSTGRES, PASSWORD_POSTGRES);
            statement = connection.createStatement();

            // Execute Query
            String query = "SELECT * FROM public.\"users_district\";";
            resultSet = statement.executeQuery(query);

            // Convert ResultSet to JSON
            ObjectMapper objectMapper = new ObjectMapper();
            ArrayNode jsonArray = objectMapper.createArrayNode();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (resultSet.next()) {
                ObjectNode jsonObject = objectMapper.createObjectNode();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    jsonObject.put(columnName, resultSet.getString(i));
                }
                jsonArray.add(jsonObject);
            }

            // Write JSON to file
            try (FileWriter file = new FileWriter("users_district.json")) {
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, jsonArray);
                System.out.println("JSON data successfully written to users_district.json");
            }

        } catch (ClassNotFoundException | SQLException | IOException e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
