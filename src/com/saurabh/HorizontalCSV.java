package com.saurabh;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HorizontalCSV {

    public static void main(String[] args) {
        String csvFile = "/home/shatam-100/Down/WaterView_Data/FTP_DATA/LoadDateData/LoadDateCheckCount3.csv";
        String line;
        String delimiter = "\t"; // Assuming tab-separated values

        Map<String, Map<String, String>> dataMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                String[] data = line.split(delimiter);
                if (data.length >= 3) {
                    String waterDistrictID = data[0].trim();
                    String tableName = data[1].trim();
                    String count = data[2].trim();

                    dataMap.putIfAbsent(waterDistrictID, new HashMap<>());
                    dataMap.get(waterDistrictID).put(tableName, count);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Print header
        if (!dataMap.isEmpty()) {
            System.out.print("Waterdistrict_ID\t");
            for (String tableName : dataMap.values().iterator().next().keySet()) {
                System.out.print(tableName + "\t");
            }
            System.out.println();

            // Print data
            for (Map.Entry<String, Map<String, String>> entry : dataMap.entrySet()) {
                System.out.print(entry.getKey() + "\t");
                for (String count : entry.getValue().values()) {
                    System.out.print(count + "\t");
                }
                System.out.println();
            }
        }
    }
}
