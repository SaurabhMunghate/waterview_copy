package com.getdatacimis;
import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.json.JSONArray;
import org.json.JSONObject;

import com.shatam.utils.U;

public class JSON_to_CSV_Weather_Processor_Working {
    public static void main(String[] args) {
//    	getData();
    	getAllStationJson();
    }

	private static void getAllStationJson() {
		// TODO Auto-generated method stub
        String csvFilePath = "/home/shatam-100/workspaces/waterviewdataloading/stations.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line = br.readLine(); // Reading header

            while ((line = br.readLine()) != null) {
//            	
                String[] columns = line.split(",");
                String IsActive = columns[7].trim(); // 
                if(line.contains("False")) continue;
                String StationNbr = columns[0].trim();
                String Name = columns[1].trim();
                
                String latitude = columns[11].trim(); // Adjust column index based on your CSV file
                String longitude = columns[12].trim(); // Adjust column index based on your CSV file
                latitude = U.getSectionValue(latitude, "/", "\"").trim();
                longitude = U.getSectionValue(longitude, "/", "\"").trim();

                System.out.println("StationNbr: " +StationNbr+"IsActive: " +IsActive+" Latitude: " + latitude + ", Longitude: " + longitude);
//                String api= "https://archive-api.open-meteo.com/v1/archive?latitude="+latitude+"&longitude="+longitude+"&start_date=1991-01-01&end_date=2020-12-31&daily=cloud_cover_mean";
                String api= "https://archive-api.open-meteo.com/v1/archive?latitude="+latitude+"&longitude="+longitude+"&start_date=2021-01-01&end_date=2025-04-30&daily=cloud_cover_mean";

                U.log(api);
//                U.getHTML(api);
                U.downloadUsingStream(api, U.getCache(api));
                getData(api,StationNbr,Name);
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }

	}

	private static void getData(String api,String StationNbr,String Name) throws MalformedURLException {
		// TODO Auto-generated method stub
        String inputFilePath = U.getCache(api); // JSON file path
        String outputFilePath = U.getCache(api).replace(".txt", ".csv"); // Output CSV file path

        try {
            // Read the JSON file content
            String jsonContent = new String(Files.readAllBytes(Paths.get(inputFilePath)));
            JSONObject jsonObject = new JSONObject(jsonContent);

            // Extracting latitude and longitude
            double latitude = jsonObject.optDouble("latitude", 0.0);
            double longitude = jsonObject.optDouble("longitude", 0.0);

            // Extracting daily data
            JSONObject dailyData = jsonObject.optJSONObject("daily");
            JSONArray dates = dailyData != null ? dailyData.optJSONArray("time") : new JSONArray();
            JSONArray cloudCover = dailyData != null ? dailyData.optJSONArray("cloud_cover_mean") : new JSONArray();

            // Preparing CSV file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
                // Writing CSV header
                writer.write("StationNbr,Name,latitude,longitude,Date,cloud_cover_mean\n");

                // Writing data rows
                for (int i = 0; i < dates.length(); i++) {
                    writer.write(StationNbr+ "," +Name+ "," +latitude + "," + longitude + "," + dates.optString(i, "") + "," + cloudCover.optInt(i, 0) + "\n");
                }
            }

            System.out.println("CSV file created successfully: " + outputFilePath);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }

	}

	private static void getData() {
		// TODO Auto-generated method stub
        String inputFilePath = "/home/shatam-100/Down/WaterView_Data/Precipitation_Work/archive-api.open-meteo.txt"; // JSON file path
        String outputFilePath = "/home/shatam-100/Down/WaterView_Data/Precipitation_Work/archive-api.open-meteo/weather_data.csv"; // Output CSV file path

        try {
            // Read the JSON file content
            String jsonContent = new String(Files.readAllBytes(Paths.get(inputFilePath)));
            JSONObject jsonObject = new JSONObject(jsonContent);

            // Extracting latitude and longitude
            double latitude = jsonObject.optDouble("latitude", 0.0);
            double longitude = jsonObject.optDouble("longitude", 0.0);

            // Extracting daily data
            JSONObject dailyData = jsonObject.optJSONObject("daily");
            JSONArray dates = dailyData != null ? dailyData.optJSONArray("time") : new JSONArray();
            JSONArray cloudCover = dailyData != null ? dailyData.optJSONArray("cloud_cover_mean") : new JSONArray();

            // Preparing CSV file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
                // Writing CSV header
                writer.write("latitude,longitude,Date,cloud_cover_mean\n");

                // Writing data rows
                for (int i = 0; i < dates.length(); i++) {
                    writer.write(latitude + "," + longitude + "," + dates.optString(i, "") + "," + cloudCover.optInt(i, 0) + "\n");
                }
            }

            System.out.println("CSV file created successfully: " + outputFilePath);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }

	}
}
