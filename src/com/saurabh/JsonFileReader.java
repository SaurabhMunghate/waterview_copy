package com.saurabh;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonFileReader {
    public static void main(String[] args) {
        String filePath = "/home/shatam-100/CODE_Repository/EagleAerialCIIWater_EncryptDecrypt_Updated_6April/EagleAerialCIIWater_EncryptDecrypt/EagleAerialCIIWater/EagleAerialCIIWater/App_Data/Districts/MONTEVISTACO226/consumption_table_json.json"; // Replace with the actual file path

        try {
            // Read the contents of the JSON file as a string
            String jsonString = new String(Files.readAllBytes(Paths.get(filePath)));

            // Create a JSON array from the string
            JSONArray jsonArray = new JSONArray(jsonString);

            // Iterate over the JSON array
            for (int i = 0; i < jsonArray.length(); i++) {
                // Get the JSON object at the current index
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                // Extract values by property name
                String premid = jsonObject.getString("premid");
                String allocation = jsonObject.getString("allocation");
                String districtId = jsonObject.getString("districtId");
                String consumption = jsonObject.getString("consumption");
                String id = jsonObject.getString("id");
                String date = jsonObject.getString("Date");

                // Print the extracted values
                System.out.println("premid: " + premid);
                System.out.println("allocation: " + allocation);
                System.out.println("districtId: " + districtId);
                System.out.println("consumption: " + consumption);
                System.out.println("id: " + id);
                System.out.println("Date: " + date);
                System.out.println();
            }
        } catch (JSONException e) {
            System.err.println("Error while parsing JSON: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error while reading file: " + e.getMessage());
        }
    }
}
