package com.saurabh;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class SplitCsvByMonth {
    public static void main(String[] args) {
        try {
            String inputFile = "/home/shatam-100/Cache/WVSANJOSE_res_consumption_Eto_11_March_c2.csv"; // Replace with your input CSV file path
            CSVReader reader = new CSVReader(new FileReader(inputFile));

            Map<String, List<String[]>> monthWiseData = new HashMap<>();

            // Read the CSV data
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                if (nextLine.length > 0 && nextLine[0].equalsIgnoreCase("Date")) {
                    continue; // Skip header row
                }
//
                String dateStr = nextLine[0];
                String[] data = Arrays.copyOfRange(nextLine, 1, nextLine.length);
//
//                // Extract year and month
//                String yearMonth = dateStr.substring(0, 7);
//
//                // Add data to the corresponding month in the map
            	
                monthWiseData.computeIfAbsent(dateStr, k -> new ArrayList<>()).add(data);
            }
//            System.out.println(monthWiseData.size());

            // Close the CSV reader
//            reader.close();

            // Write separate CSV files for each 12-month range
            for (Map.Entry<String, List<String[]>> entry : monthWiseData.entrySet()) {
            	System.out.println(entry.getKey());
//                String outputFileName = "/home/shatam-100/Cache/"+"output_" + entry.getKey().replace("-", "_") + ".csv";
//                CSVWriter writer = new CSVWriter(new FileWriter(outputFileName));
//
//                // Write header
//                List<String> headerList = new ArrayList<>();
//                headerList.add("Date");
//                headerList.addAll(Arrays.asList("APN", "PreTile", "Eto", "Tile", "Consumtion")); // Adjust column names
//                writer.writeNext(headerList.toArray(new String[0]));
//
//                // Write data
                for (String[] rowData : entry.getValue()) {
                    List<String> rowList = new ArrayList<>();
                    rowList.add(entry.getKey() + ""); // Assuming the day is always 01
                    rowList.addAll(Arrays.asList(rowData));
                    
//                    writer.writeNext(rowList.toArray(new String[0]));
                }
                System.out.println(entry.getValue().size());
//
//                // Close the CSV writer
//                writer.close();
            }

            System.out.println("CSV files created successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
