package com.saurabh;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvToGeoJson {
	public static void main(String[] args) {

		String allcsv = "/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries_CSV";

		System.out.println(allcsv);
//        String outputDirectory = "/home/shatam-100/Down/WaterView_Data/FTP_DATA/18_March_WD/Data_Folder_2024-03-17/WVWESTERN";
//        File outputDir = new File(outputDirectory);
//        outputDir.mkdirs();
		File[] csvFilePaths = new File(allcsv).listFiles((dir, name) -> name.toLowerCase().endsWith(".csv"));
		if (csvFilePaths != null) {
			for (File csvFilePath : csvFilePaths) {
				try {
					System.out.println(csvFilePath);
//					String distric_name = 
					String[] st = csvFilePath.toString().split("/");
					String db_name = st[st.length-1];
//					System.out.println("Waterdistrict_ID : "+ Waterdistrict_ID); 
					String WD_ID = getTableName(db_name.replace("_.csv", ""));
					System.out.println(WD_ID);
//					getTableName();
					
					convertCsvToGeoJson(csvFilePath,WD_ID, db_name);
				} catch (Exception e) {
				}
			}
		}
	}

	private static void convertCsvToGeoJson(File csvFilePath, String WD_ID, String db_name) {
		// TODO Auto-generated method stub

		List<String> geometries = new ArrayList<>();

		try (CSVReader csvReader = new CSVReader(new FileReader(csvFilePath))) {
			String[] values;
			boolean isFirstRow = true;
			int geomIndex = -1;

			while ((values = csvReader.readNext()) != null) {
				if (isFirstRow) {
					// Find the index of the_geom column
					for (int i = 0; i < values.length; i++) {
						if ("the_geom".equals(values[i])) {
							geomIndex = i;
							break;
						}
					}
					if (geomIndex == -1) {
						throw new RuntimeException("the_geom column not found in CSV file");
					}
					isFirstRow = false;
				} else {
					// Add geometry to the list
					geometries.add(values[geomIndex]);
				}
			}

			// Write geometries to text file
			try (FileWriter writer = new FileWriter(csvFilePath.toString()
					.replace("DistrictBoundaries_CSV/", "District_Boundary_json/District_Boundary_json_").replace("_.csv", ".json"))) {
				for (String geom : geometries) {
					geom  = "{\"DistrictID\":\""+WD_ID+"\",\"Name\":\""+db_name.replace("_.csv", "").replace("_", "")+"\",\"Description\":\"\",\"Contact_Name_1\":\"\",\"Contact_Name_2\":\"\",\"Contact_Name_3\":\"\",\"source_address\":\"\",\"source_city\":\"\",\"source_state\":\"\",\"source_zip\":\"\",\"email1\":\"\",\"email2\":\"\",\"email3\":\"\",\"phone1\":\"\",\"phone2\":\"\",\"logo\":\"\",\"url\":\"\",\"Centroid\":\"\",\"zoom\":\"14\",\"Polygon\":\""+geom +"\"}" ;
					writer.write(geom + System.lineSeparator());
				}
			}

		} catch (Exception e) {
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
