package com.bil;
import java.io.*;
import java.util.*;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.WKTReader;
import com.opencsv.*;

public class GeometryIntersectionWithDate {
    public static void main(String[] args) throws Exception {
        // CSV readers for both files
        BufferedReader reader1 = new BufferedReader(new FileReader("/home/shatam-100/Down/WaterView_Data/FTP_DATA/Data_Folder_2024-12/WVMONTEVISTACO226_29Jan/monte_vista_total.csv"));
        BufferedReader reader2 = new BufferedReader(new FileReader("/home/shatam-100/Down/WaterView_Data/FTP_DATA/Data_Folder_2024-12/WVMONTEVISTACO226_29Jan/prd.meter_locations_updated_copy.csv"));
        BufferedWriter writer = new BufferedWriter(new FileWriter("/home/shatam-100/Down/WaterView_Data/FTP_DATA/Data_Folder_2024-12/WVMONTEVISTACO226_29Jan/monte_vista_with_precip.csv"));

        CSVReader csvReader1 = new CSVReader(reader1);
        CSVReader csvReader2 = new CSVReader(reader2);
        CSVWriter csvWriter = new CSVWriter(writer);

        // Read headers of both CSV files
        String[] header1 = csvReader1.readNext(); // for monte_vista_total.csv
        String[] header2 = csvReader2.readNext(); // for meter_locations_updated_copy.csv
        
        // Add new header for the updated file
        List<String> newHeader = new ArrayList<>(Arrays.asList(header2));
        newHeader.add("TotalPrecip");
        csvWriter.writeNext(newHeader.toArray(new String[0]));

        // Initialize geometry reader
        WKTReader wktReader = new WKTReader();

        // Map to store Tile geometries and their associated TotalPrecip values, keyed by Date
        Map<String, Map<Geometry, String>> tilePrecipMap = new HashMap<>();

        // Read first CSV (monte_vista_total.csv) and store geometries with associated TotalPrecip for each Date
        String[] row;
        while ((row = csvReader1.readNext()) != null) {
            String date = row[0]; // Assuming 'Date' column is in the first index
            Geometry tileGeometry = wktReader.read(row[1]); // Assuming 'Tiles' column is in the second index
            String totalPrecip = row[2];  // TotalPrecip is in the third column

            // Store TotalPrecip values in a map keyed by date and geometry
            tilePrecipMap.putIfAbsent(date, new HashMap<>());
            tilePrecipMap.get(date).put(tileGeometry, totalPrecip);
        }
        csvReader1.close();

        // Read second CSV (prd.meter_locations_updated_copy.csv) and check for intersections and matching Date
        while ((row = csvReader2.readNext()) != null) {
            String meterDate = row[1]; // Assuming 'Date' column is in the second index
            Geometry meterGeometry = wktReader.read(row[0]); // Assuming 'the_geom' column is in the first index

            String totalPrecip = "";
            if (tilePrecipMap.containsKey(meterDate)) {
                // Only proceed if there's matching date
                Map<Geometry, String> tilePrecipForDate = tilePrecipMap.get(meterDate);

                // Check for intersection
                for (Map.Entry<Geometry, String> entry : tilePrecipForDate.entrySet()) {
                    if (entry.getKey().intersects(meterGeometry)) {
                        totalPrecip = entry.getValue();
                        break;
                    }
                }
            }

            // Add TotalPrecip to the row if an intersection is found
            List<String> newRow = new ArrayList<>(Arrays.asList(row));
            newRow.add(totalPrecip);
            csvWriter.writeNext(newRow.toArray(new String[0]));
        }
        csvReader2.close();
        csvWriter.close();
    }
}
