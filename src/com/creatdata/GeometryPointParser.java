package com.creatdata;
import java.io.*;
import java.util.*;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class GeometryPointParser {
    
    static class Point {
        double latitude;
        double longitude;
        
        Point(double longitude, double latitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }
        
        @Override
        public String toString() {
            return "Latitude: " + latitude + ", Longitude: " + longitude;
        }
    }
    
    public static Point parseGeometryPoint(String geomPoint) {
        try {
            // Remove "POINT" and extract coordinates
            String coordinates = geomPoint.replace("POINT(", "")
                                       .replace(")", "")
                                       .trim();
            
            // Split into longitude and latitude
            String[] parts = coordinates.split("\\s+");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid geometry point format");
            }
            
            double longitude = Double.parseDouble(parts[0]);
            double latitude = Double.parseDouble(parts[1]);
            
            return new Point(longitude, latitude);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to parse geometry point: " + geomPoint, e);
        }
    }
    
    public static void processCSVFile(String inputFile, String outputFile) {
        try (CSVReader reader = new CSVReader(new FileReader(inputFile));
             CSVWriter writer = new CSVWriter(new FileWriter(outputFile))) {
            
            // Read header
            String[] header = reader.readNext();
            int geomColumnIndex = -1;
            
            // Find the_geom column index
            for (int i = 0; i < header.length; i++) {
                if (header[i].equals("the_geom")) {
                    geomColumnIndex = i;
                    break;
                }
            }
            
            if (geomColumnIndex == -1) {
                throw new IllegalArgumentException("Column 'the_geom' not found in CSV");
            }
            
            // Write header for output CSV
            String[] newHeader = {"Original_Geometry", "Latitude", "Longitude", "Precip"};
            writer.writeNext(newHeader);
            
            // Process each row
            String[] row;
            while ((row = reader.readNext()) != null) {
                String geomValue = row[geomColumnIndex];
                Point point = parseGeometryPoint(geomValue);
                float precip = BILReader.getPrecipitation(point.latitude, point.longitude);
                
                String[] newRow = {
                    geomValue,
                    String.valueOf(point.latitude),
                    String.valueOf(point.longitude),
                    String.valueOf(precip)
                };
                writer.writeNext(newRow);
                
                // Print to console as well
                System.out.println("Original: " + geomValue);
                System.out.println(point);
                System.out.println();
            }
            
            System.out.println("Processing completed. Results written to " + outputFile);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
//        if (args.length != 2) {
//            System.out.println("Usage: java GeometryPointParser <input_csv> <output_csv>");
//            return;
//        }
        
        String inputFile = "/home/shatam-100/Down/WaterView_Data/FTP_DATA/Data_Folder_2024-12/WVMONTEVISTACO226/prd.meter_locations.csv";//args[0];
        String outputFile = "/home/shatam-100/Down/WaterView_Data/FTP_DATA/Data_Folder_2024-12/WVMONTEVISTACO226/prd.meter_locations_1.csv";//args[1];
        processCSVFile(inputFile, outputFile);
    }
}