package com.saurabh;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class FindTableName {
	public static void main(String[] arg) {
		System.out.println(getTableName("monte_vista"));
		
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
