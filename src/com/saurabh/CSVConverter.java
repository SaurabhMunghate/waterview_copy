package com.saurabh;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONObject;

public class CSVConverter {
    public static void main(String[] args) {
        String csvFile = "/home/shatam-100/Down/WaterView_Data/Address.csv"; // Replace with your CSV file path
        int numberOfColumns = 6; // Replace with the number of columns in your CSV

        JSONArray jsonArray = new JSONArray();
        JSONArray AlljsonArray = new JSONArray();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] columns = line.split(",");

                JSONArray columnData = new JSONArray();
                for (int i = 0; i < numberOfColumns; i++) {
                    if (i < columns.length) {
                    	if(columns[i].contains("the_geom"))continue;
                    	if(columns[i].contains("POINT"))columns[i]="";
                        columnData.put(columns[i].replaceAll("\"", ""));
                    } else {
                        columnData.put("");
                    }
                }

                jsonArray.put(columnData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create a JSON object with the converted data
//        JSONObject jsonObject = new JSONObject();
        AlljsonArray.put(jsonArray);

        // Print the JSON object
        writeToFile(jsonArray.toString(),"/home/shatam-100/Down/WaterView_Data/OutPutDataAll.txt");
//        System.out.println(AlljsonArray);
    }

    public static void writeToFile(String content, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(content);
            System.out.println("Content has been written to the file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
