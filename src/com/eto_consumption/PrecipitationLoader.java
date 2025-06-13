package com.eto_consumption;
import java.io.*;
import java.util.*;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class PrecipitationLoader {
    public static void main(String[] args) {
        String meterLocationsFile = "/home/shatam-100/Down/WaterView_Data/FTP_DATA/Data_Folder_2024-12/WVMONTEVISTACO226/prd.meter_locations.csv";
        String monthlyConsumptionFile = "/home/shatam-100/Down/WaterView_Data/FTP_DATA/Data_Folder_2024-12/WVMONTEVISTACO226/MonthlyConsumption.csv";
        String outputFile = "/home/shatam-100/Down/WaterView_Data/FTP_DATA/Data_Folder_2024-12/WVMONTEVISTACO226/cii_consumption_eto.csv";

        Set<String> meterIds = new HashSet<>();
        Map<String, List<ConsumptionData>> consumptionDataMap = new HashMap<>();

        try {
            // Read meter IDs from prd.meter_locations.csv
            try (CSVReader reader = new CSVReader(new FileReader(meterLocationsFile))) {
                String[] nextLine;
                boolean isHeader = true;
                int meterIdIndex = -1;

                while ((nextLine = reader.readNext()) != null) {
                    if (isHeader) {
                        for (int i = 0; i < nextLine.length; i++) {
                            if (nextLine[i].equalsIgnoreCase("meterID")) {
                                meterIdIndex = i;
                                break;
                            }
                        }
                        isHeader = false;
                        continue;
                    }

                    if (meterIdIndex != -1 && nextLine.length > meterIdIndex) {
                        meterIds.add(nextLine[meterIdIndex]);
                    }
                }
            }

            // Read consumption data from MonthlyConsumption.csv
            try (CSVReader reader = new CSVReader(new FileReader(monthlyConsumptionFile))) {
                String[] nextLine;
                boolean isHeader = true;
                int meterIdIndex = -1;
                int yearMonthIndex = -1;
                int consumptionIndex = -1;

                while ((nextLine = reader.readNext()) != null) {
                    if (isHeader) {
                        // Find column indices
                        for (int i = 0; i < nextLine.length; i++) {
                            if (nextLine[i].equalsIgnoreCase("MeterID")) {
                                meterIdIndex = i;
                            } else if (nextLine[i].equalsIgnoreCase("year_month")) {
                                yearMonthIndex = i;
                            } else if (nextLine[i].equalsIgnoreCase("consumption")) {
                                consumptionIndex = i;
                            }
                        }
                        isHeader = false;
                        continue;
                    }

                    if (meterIdIndex != -1 && yearMonthIndex != -1 && consumptionIndex != -1 &&
                        nextLine.length > Math.max(Math.max(meterIdIndex, yearMonthIndex), consumptionIndex)) {
                        
                        String meterId = nextLine[meterIdIndex];
                        String yearMonth = nextLine[yearMonthIndex];
                        String consumption = nextLine[consumptionIndex];

                        consumptionDataMap
                            .computeIfAbsent(meterId, k -> new ArrayList<>())
                            .add(new ConsumptionData(yearMonth, consumption));
                    }
                }
            }

            // Create output CSV
            try (CSVWriter writer = new CSVWriter(new FileWriter(outputFile))) {
                // Write header
                String[] header = {"MeterID", "year_month", "Allocation", "Consumption"};
                writer.writeNext(header);

                // Write data for each meter ID
                for (String meterId : meterIds) {
                    List<ConsumptionData> consumptionDataList = consumptionDataMap.getOrDefault(meterId, new ArrayList<>());
                    
                    if (consumptionDataList.isEmpty()) {
                        // If no consumption data found, write a row with default values
                        String[] row = {meterId, "", "0", "0"};
                        writer.writeNext(row);
                    } else {
                        // Write a row for each consumption data entry
                        for (ConsumptionData data : consumptionDataList) {
                            String[] row = {meterId, data.yearMonth, "0", data.consumption};
                            writer.writeNext(row);
                        }
                    }
                }
            }

            System.out.println("Successfully created " + outputFile);
            System.out.println("Total number of meter IDs processed: " + meterIds.size());

        } catch (IOException e) {
            System.err.println("Error processing CSV files: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Helper class to store consumption data
    private static class ConsumptionData {
        String yearMonth;
        String consumption;

        ConsumptionData(String yearMonth, String consumption) {
            this.yearMonth = yearMonth;
            this.consumption = consumption;
        }
    }
}