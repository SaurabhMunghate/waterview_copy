package com.saurabh;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ModifyJSON {
    public static void main(String[] args) {
        // Path to your JSON file
        String filePath = "/home/shatam-100/Down/WaterView_Data/FTP_DATA/LoadDateData/users_district.json";

        try {
            // Read the JSON file
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(filePath));
            JSONArray jsonArray = (JSONArray) obj;

            // Iterate through each object in the JSON array
            for (Object o : jsonArray) {
                JSONObject jsonObject = (JSONObject) o;
                
                String reportId = (String) jsonObject.get("name");
                System.out.println("name: " + reportId);

                
                // Update the "report_id" field to "saurabh"
                jsonObject.put("name", getTableName(reportId.toLowerCase()));
            }

            // Write the modified JSON content back to the file
            FileWriter fileWriter = new FileWriter(filePath);
            fileWriter.write(jsonArray.toJSONString());
            fileWriter.flush();
            fileWriter.close();

            System.out.println("JSON file updated successfully!");

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
	private static String getTableName(String Waterdistrict_ID) {
		// TODO Auto-generated method stub
        String filePath = "/home/shatam-100/Down/WaterView_Data/Tiles/db_config.json";
        String waterDistrictId = null;
        String waterDistrictName = null;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // Parse the JSON content
            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(br).getAsJsonObject();

            // Get the "Waterdistrict_Tables" array
            JsonArray waterDistrictTables = jsonObject.getAsJsonArray("Waterdistrict_Tables");

            // Iterate through the array to find the desired WaterDistrict_ID

            for (JsonElement element : waterDistrictTables) {
                JsonObject districtObject = element.getAsJsonObject();
                String currentId = districtObject.get("Waterdistrict_Name").getAsString();
                if (Waterdistrict_ID.equals(currentId)) {
                    waterDistrictId = currentId;
                    waterDistrictName = districtObject.get("Waterdistrict_ID").getAsString();
                    break;
                }
            }

            // Print the WaterDistrict_ID if found
            if (waterDistrictId != null) {
                System.out.println("waterDistrictName : " + waterDistrictName);
                return waterDistrictName;
            } else {
                System.out.println("WaterDistrict_ID for WVMONTEVISTACO226 not found.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return waterDistrictName;
		
	}

}
