package com.eto_consumption;
import java.io.*;
import java.util.*;
import java.text.*;

public class CsvProcessor {
    public static void main(String[] args) {
        // Specify the input and output CSV file paths
        String inputFile = "/home/shatam-100/Down/WaterView_Data/eto_cache_file/eto_values_all_cal.csv";
        String outputFile = "/home/shatam-100/Down/WaterView_Data/eto_cache_file/eto_values_all_cal_output.csv";

        // Create a map to store the totals
        Map<String, Double> totals = new HashMap<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            
            // Skip header if it exists
            reader.readLine();

            // Process each line in the CSV file
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");
                
                if (columns.length == 4) {
                    String gridRow = columns[0].trim();
                    String gridCol = columns[1].trim();
                    double value = Double.parseDouble(columns[2].trim());
                    String yearMonth = columns[3].trim().substring(0, 7); // Get year-month part (YYYY-MM)
                    
                    // Create a unique key based on grid_row, grid_col, and year_month
                    String key = gridRow + "_" + gridCol + "_" + yearMonth;
                    
                    // Add value to the existing total for that key
                    totals.put(key, totals.getOrDefault(key, 0.0) + value);
                }
            }

            // Write the results to a new CSV file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
                // Write header
                writer.write("grid_row,grid_col,year_month,total_value\n");
                
                // Write the summed totals
                for (Map.Entry<String, Double> entry : totals.entrySet()) {
                    String[] keyParts = entry.getKey().split("_");
                    writer.write(keyParts[0] + "," + keyParts[1] + "," + keyParts[2] + "," + entry.getValue() + "\n");
                }
                
            }

            System.out.println("Processing complete. Output saved to: " + outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
