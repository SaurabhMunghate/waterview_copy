package com.saurabh;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

public class SplitCsvByYear {
    public static void main(String[] args) {
        try {
            String inputFile = "/home/shatam-100/Cache/WVSANJOSE_res_consumption_Eto_11_March_c2.csv"; // Replace with your input CSV file path
            CSVReader reader = new CSVReader(new FileReader(inputFile));

            Map<String, List<String[]>> yearWiseData = new HashMap<>();

            // Read the CSV data
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                if (nextLine.length > 0 && nextLine[0].equalsIgnoreCase("Date")) {
                    continue; // Skip header row
                }

                String dateStr = nextLine[0];
                String[] data = Arrays.copyOfRange(nextLine, 1, nextLine.length);

                // Extract year
                String year = dateStr.substring(0, 4);

                // Add data to the corresponding year in the map
                yearWiseData.computeIfAbsent(year, k -> new ArrayList<>()).add(nextLine);
            }

            // Close the CSV reader
            reader.close();

            // Write separate CSV files for each year
            for (Map.Entry<String, List<String[]>> entry : yearWiseData.entrySet()) {
                String outputFileName = "/home/shatam-100/Cache/"+"output_" + entry.getKey() + ".csv";
                CSVWriter writer = new CSVWriter(new FileWriter(outputFileName));

                // Write header
                writer.writeNext(entry.getValue().get(0)); // Use the header from the first row

                // Write data
                for (String[] rowData : entry.getValue()) {
                    writer.writeNext(rowData);
                }

                // Close the CSV writer
                writer.close();
            }

            System.out.println("CSV files created successfully.");
        } catch (IOException  e) {
            e.printStackTrace();
        }
    }
}
