package com.saurabh;
import java.io.BufferedReader;
import java.io.FileReader;
import org.json.JSONArray;
import org.json.JSONObject;

public class JSONReader {
    public static void main(String[] args) {
        try {
            // Specify the file path of the JSON file
            String filePath = "/home/shatam-100/Sublime/Mumbai.txt";

            // Create a FileReader object with the file path
            FileReader fileReader = new FileReader(filePath);

            // Create a BufferedReader to read the file
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            StringBuilder jsonContent = new StringBuilder();
            String line;

            // Read the file line by line and append it to StringBuilder
            while ((line = bufferedReader.readLine()) != null) {
//                jsonContent.append(line);
            	if(line.length()<1)continue;
            	JSONArray jsonObject = new JSONArray(line.split("-"));
            	if(!(jsonObject.length()==2)) continue;
            	System.out.println(jsonObject);
            }

            // Close the BufferedReader
            bufferedReader.close();

            // Parse the JSON content
//            JSONArray jsonObject = new JSONArray(jsonContent.toString());

            // Extract the value of the _geom key
//            JSONObject geomArray = jsonObject.("the_geom");

            // Print the values of _geom key
//            for (int i = 0; i < jsonObject.length(); i++) {
//            	JSONObject geomArray = jsonObject.optJSONObject(jsonObject.get(i));
//            	System.out.println(i);
//                System.out.println(jsonObject.get(i));
                
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
