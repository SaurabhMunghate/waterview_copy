package com.eto_consumption;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class GenerateCIIConsumptionCSV {

    public static void main(String[] args) {
        // Define file paths
        String folder = "/home/shatam-100/Down/WaterView_Data/FTP_DATA/Data_Folder_2024-12/WVMONTEVISTACO226/"; // Adjust folder path as needed
        String monthlyConsumptionFile = folder + "MonthlyConsumption.csv";
        String meterLocationsFile = folder + "prd.meter_locations.csv";
        String meter_locations_row_col_pre = folder + "prd.meter_locations_row_col_pre.csv";
        String monthly_eto_rollup = folder + "monthly_eto_rollup.csv";
        String premiseBoundsFile = folder + "prd.premise_bounds.csv";
        String outputFile = folder + "cii_consumption_eto.csv";

        try {
            // Load MonthlyConsumption data (meterID -> [Date, Consumption])
            Map<String, String[]> consumptionData = loadConsumptionData(monthlyConsumptionFile);

            // Load meter locations (meterID -> premID)
            Map<String, String> meterToPremID = loadMeterLocations(meterLocationsFile);

            // Load premise bounds (premID -> [I, I_SLA])
            Map<String, String[]> premBounds = loadPremiseBounds(premiseBoundsFile);

            // Load Prece Values (premID -> [premID, Pre_Value_01_bil, Row, Col])
            Map<String, String[]> meter_Locations_Row_Col_Pre = LoadMeterToPrecipatation(meter_locations_row_col_pre);

            // Load Prece Values (premID -> [premID, Pre_Value_01_bil, Row, Col])
            Map<String, String[]> monthly_Eto_Rollup = getMonthly_eto_rollup(monthly_eto_rollup);

            // Write the output file
            try (CSVWriter writer = new CSVWriter(new FileWriter(outputFile))) {
                // Write header
                writer.writeNext(new String[]{"Date", "meterID", "premID", "String","I","I_SLA", "Consumption"});

                // Process and write data
                for (Map.Entry<String, String[]> entry : consumptionData.entrySet()) {
                    String meterID = entry.getKey();
//                    System.out.println(meterID.split(",")[0]);
                    String[] consumptionValues = entry.getValue();
                    String date = consumptionValues[0];
                    String consumption = consumptionValues[1];

                    // Get premID from meterID
                    String premID = meterToPremID.get(meterID.split(",")[0]);
                    if(premID==null)continue;
//                    System.out.println(premID);
                    if (premID != null) {
                        // Get Allocation (I) and I_SLA from premID
                        String[] bounds = premBounds.get(premID);
                        String[] prec = meter_Locations_Row_Col_Pre.get(premID); // premID, Precipatation, Row, Col
                        String[] etoValue = monthly_Eto_Rollup.get(); // premID, Precipatation, Row, Col
//                        System.out.println(prec[0]);
                        
                        if (bounds != null) {
                        	//premID,meterID,Row,Col,Precipatation
                            String I = bounds[0]; // I
                            String I_SLA = bounds[1]; // I_SLA
                            String avgETo = "1"; // avgETo
                            String avgPrecp = prec[4]; // avgPrecp
//                            System.out.println(I+" | "+I_SLA);
                            double allocation=Math.abs((0.62)*((Double.parseDouble(I)*0.8*(Double.parseDouble(avgETo)-Double.parseDouble(avgPrecp)))+(1.0*Double.parseDouble(I_SLA)*Double.parseDouble(avgETo))+(1*0.45*Double.parseDouble(avgETo)))+0);

                            // Write row to output file
                            writer.writeNext(new String[]{date, meterID, premID,allocation+"",I, I_SLA, consumption});
                        }
                    }
                }
            }
            System.out.println(consumptionData.size());
            System.out.println(meterToPremID.size());
            System.out.println(premBounds.size());

            System.out.println("File generated successfully: " + outputFile);

        } catch (IOException e) {
            System.err.println("Error processing files: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Load MonthlyConsumption data (meterID -> [Date, Consumption])
    private static Map<String, String[]> loadConsumptionData(String filePath) throws IOException {
        Map<String, String[]> data = new HashMap<>();
        try (CSVReader reader = new CSVReader(Files.newBufferedReader(Paths.get(filePath)))) {
            String[] header = reader.readNext(); // Skip the header
            String[] row;
            while ((row = reader.readNext()) != null) {
                String meterID = row[0];
                String date = row[1];
                String consumption = row[2];
//                System.out.println(meterID+date+consumption);
                data.put(meterID+","+date, new String[]{date, consumption});
            }
        }
        return data;
    }

    // Load meter locations (meterID -> premID)
    private static Map<String, String> loadMeterLocations(String filePath) throws IOException {
        Map<String, String> data = new HashMap<>();
        try (CSVReader reader = new CSVReader(Files.newBufferedReader(Paths.get(filePath)))) {
            String[] header = reader.readNext(); // Skip the header
            String[] row;
            while ((row = reader.readNext()) != null) {
                String premID = row[0];
                String meterID = row[1];
//                System.out.println(meterID+ premID);
                data.put(meterID, premID);
            }
        }
        return data;
    }
    // load meter To Precipatation 
    private static Map<String, String[]> getMonthly_eto_rollup(String filePath) throws IOException {
    	//premID	meterID	Row	Col	Pre_Value_01_bil
        Map<String, String[]> data = new HashMap<>();
        try (CSVReader reader = new CSVReader(Files.newBufferedReader(Paths.get(filePath)))) {
            String[] header = reader.readNext(); // Skip the header
            String[] row;
            while ((row = reader.readNext()) != null) {
                String year_month_grid_row_grid_col = row[1]+row[2]+row[3];
                String year_month = row[1];
                String grid_row = row[2];
                String grid_col = row[3];
                String value = row[4]; 
                data.put(year_month_grid_row_grid_col, new String[]{year_month,grid_row,grid_col,value});
            }
        }
        return data;
    }

    // load meter To Precipatation 
    private static Map<String, String[]> LoadMeterToPrecipatation(String filePath) throws IOException {
    	//premID	meterID	Row	Col	Pre_Value_01_bil
        Map<String, String[]> data = new HashMap<>();
        try (CSVReader reader = new CSVReader(Files.newBufferedReader(Paths.get(filePath)))) {
            String[] header = reader.readNext(); // Skip the header
            String[] row;
            while ((row = reader.readNext()) != null) {
                String premID = row[0];
                String meterID = row[1];
                String Row = row[2];
                String Col = row[3];
                String Precipatation = row[4]; 
                data.put(premID, new String[]{premID,meterID,Row,Col,Precipatation});
            }
        }
        return data;
    }
    
    // Load premise bounds (premID -> [I, I_SLA])
    private static Map<String, String[]> loadPremiseBounds(String filePath) throws IOException {
        Map<String, String[]> data = new HashMap<>();
        try (CSVReader reader = new CSVReader(Files.newBufferedReader(Paths.get(filePath)))) {
            String[] header = reader.readNext(); // Skip the header
            String[] row;
            while ((row = reader.readNext()) != null) {
                String premID = row[0];
                String I = row[60]; // I
                String iSLA = row[61];// I_SLA
                data.put(premID, new String[]{I, iSLA});
            }
        }
        return data;
    }
}
