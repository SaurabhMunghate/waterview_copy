package com.bil;
import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.WKTReader;
import com.opencsv.*;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import java.text.SimpleDateFormat;

public class ExtractZipFromCSV2 {
    public static void main(String[] args) throws Exception {
        BufferedReader reader1 = new BufferedReader(new FileReader("/home/shatam-100/Down/WaterView_Data/Precipitation_all/monte_vista.csv"));
        BufferedReader reader2 = new BufferedReader(new FileReader("/home/shatam-100/Down/WaterView_Data/FTP_DATA/Data_Folder_2024-12/meter_locations_u1.csv"));
        BufferedWriter writer = new BufferedWriter(new FileWriter("/home/shatam-100/Down/WaterView_Data/FTP_DATA/Data_Folder_2024-12/meter_locations_u3.csv"));
        
        CSVReader csvReader1 = new CSVReader(reader1);
        CSVReader csvReader2 = new CSVReader(reader2);
        CSVWriter csvWriter = new CSVWriter(writer);

        String[] header1 = csvReader1.readNext();
        String[] header2 = csvReader2.readNext();
        
        List<String> newHeader = new ArrayList<>(Arrays.asList(header2));
        newHeader.add("intersect_value");
        csvWriter.writeNext(newHeader.toArray(new String[0]));

        // Using Guava's ArrayListMultimap to store multiple values for a single key
        Multimap<String, String> polygonDateMap = ArrayListMultimap.create();
        WKTReader wktReader = new WKTReader();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");

        // Read first CSV and store polygons with their column[1] value grouped by date and polygon
        String[] row;
        while ((row = csvReader1.readNext()) != null) {
            Geometry polygon = wktReader.read(row[2]);
            String date = row[1];
            String yearMonth = dateFormat.format(new SimpleDateFormat("yyyy-MM-dd").parse(date));  // Extract year and month
            String polygonKey = polygon.toText();  // Use polygon WKT text as a key

            String key = yearMonth + "_" + polygonKey;  // Create a composite key (year-month + polygon)
//            System.out.println(row[4]+key);
            polygonDateMap.put(key, row[2]);  // Add multiple values for the same key
        }
        csvReader1.close();

        // Read second CSV and check intersections
        while ((row = csvReader2.readNext()) != null) {
            Geometry premise = wktReader.read(row[0]);
            String intersectValue = "";

            // Check for intersections with polygons grouped by date and polygon
            for (Entry<String, String> entry : polygonDateMap.entries()) {
                String key = entry.getKey();
                String[] keyParts = key.split("_");
                String yearMonth = keyParts[0];  // Extract year-month from the key
                String polygonWKT = keyParts[1];  // Extract polygon WKT from the key
                Geometry polygon = wktReader.read(polygonWKT);

                if (polygon.intersects(premise)) {
                    // If intersection is found, concatenate all intersected values for that polygon and month
                    intersectValue = String.join(",", entry.getValue());  // Join multiple values with commas
                    System.out.println(intersectValue+entry.getKey());
                    break;
                }
            }

            List<String> newRow = new ArrayList<>(Arrays.asList(row));
            newRow.add(intersectValue);
            csvWriter.writeNext(newRow.toArray(new String[0]));
        }
        csvReader2.close();
        csvWriter.close();
    }
}
