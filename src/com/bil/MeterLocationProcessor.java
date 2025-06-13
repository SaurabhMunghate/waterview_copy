package com.bil;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MeterLocationProcessor {
	
	private static final double ORIGIN_LON = -125.000000000000;
    private static final double ORIGIN_LAT = 49.916666666667;
    private static final double CELL_SIZE = 0.041666666667;
    private static final int NROWS = 621;
    private static final int NCOLS = 1405;
    
    // Constants (Adjust as per your data)
//    static final double ORIGIN_LAT = 40.0;  // Example value
//    static final double ORIGIN_LON = -100.0; // Example value
//    static final int NCOLS = 1000;  // Number of columns
//    static final int NROWS = 1000;  // Number of rows
//    static final double CELL_SIZE = 0.01; // Grid cell size in degrees

    // Regular expression to extract lat/lon from "POINT(lon lat)"
    private static final Pattern POINT_PATTERN = Pattern.compile("POINT\\((-?\\d+\\.\\d+) (-?\\d+\\.\\d+)\\)");

    public static void main(String[] args) {
        String inputFile = "/home/shatam-100/Down/WaterView_Data/FTP_DATA/Data_Folder_2024-12/WVMONTEVISTACO226_29Jan/prd.meter_locations.csv";  // Input CSV file
        String outputFile = "/home/shatam-100/Down/WaterView_Data/FTP_DATA/Data_Folder_2024-12/WVMONTEVISTACO226_29Jan/prd.meter_locations_updated.csv"; // Output CSV file with row, col

        try (CSVReader reader = new CSVReader(new FileReader(inputFile));
             CSVWriter writer = new CSVWriter(new FileWriter(outputFile))) {

            List<String[]> allRows = reader.readAll(); // Read all rows
            if (allRows.isEmpty()) {
                System.out.println("Empty CSV file!");
                return;
            }

            // Find the index of 'the_geom' column
            String[] header = allRows.get(0);
            int geomIndex = findColumnIndex(header, "the_geom");
            if (geomIndex == -1) {
                System.err.println("Error: Column 'the_geom' not found in CSV!");
                return;
            }

            // Add headers with new columns
            String[] newHeader = new String[header.length + 2];
            System.arraycopy(header, 0, newHeader, 0, header.length);
            newHeader[newHeader.length - 2] = "Row";
            newHeader[newHeader.length - 1] = "Col";
            writer.writeNext(newHeader);

            // Process each row (excluding header)
            for (int i = 1; i < allRows.size(); i++) {
                String[] row = allRows.get(i);
                if (row.length <= geomIndex) continue; // Ensure column exists

                try {
                    // Extract lat/lon from 'the_geom' column
                    double[] latLon = extractLatLon(row[geomIndex]);
                    if (latLon == null) {
                        System.err.println("Skipping row due to invalid geometry: " + row[geomIndex]);
                        continue;
                    }

                    double lon = latLon[0]; // First value is lon
                    double lat = latLon[1]; // Second value is lat
                    System.out.println("Processing lat/lon: " + lat + ", " + lon);

                    int[] rowCol = getRowCol(lat, lon);

                    // Append row & col values
                    String[] newRow = new String[row.length + 2];
                    System.arraycopy(row, 0, newRow, 0, row.length);
                    newRow[newRow.length - 2] = String.valueOf(rowCol[1]); // Row
                    newRow[newRow.length - 1] = String.valueOf(rowCol[0]); // Col

                    writer.writeNext(newRow);
                } catch (Exception e) {
                    System.err.println("Error processing row: " + String.join(",", row) + " -> " + e.getMessage());
                }
            }

            System.out.println("Updated CSV saved as: " + outputFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Find the index of a column by name
    public static int findColumnIndex(String[] header, String columnName) {
        for (int i = 0; i < header.length; i++) {
            if (header[i].trim().equalsIgnoreCase(columnName)) {
                return i;
            }
        }
        return -1; // Not found
    }

    // Extracts lat/lon from "POINT(lon lat)"
    public static double[] extractLatLon(String pointStr) {
        Matcher matcher = POINT_PATTERN.matcher(pointStr);
        if (matcher.matches()) {
            double lon = Double.parseDouble(matcher.group(1));
            double lat = Double.parseDouble(matcher.group(2));
            return new double[]{lon, lat};
        }
        return null; // Return null if format is incorrect
    }

    // Function to calculate row and column
    public static int[] getRowCol( double lat, double lon) throws IOException {
        // Check if coordinates are within bounds
        if (lon < ORIGIN_LON || 
            lat > ORIGIN_LAT || 
            lon > ORIGIN_LON + (NCOLS * CELL_SIZE) ||
            lat < ORIGIN_LAT - (NROWS * CELL_SIZE)) {
            return new int[]{-9999,-9999};
        }

        // Calculate row and column indices
        int col = (int)Math.round((lon - ORIGIN_LON) / CELL_SIZE);
        int row = (int)Math.round((ORIGIN_LAT - lat) / CELL_SIZE);
        //System.out.println("col: "+col);
        //System.out.println("row: "+row);
        return new int[]{col,row};
    }
}
