package com.shatam.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

public class CSVToJSon {

	public static void main(String[] arg) throws Exception {
//		String csvfile = "/home/shatam-100/Down/WaterView_Data/FTP_DATA/AprilData/users_district_2may.csv";
//		csvToJSON(csvfile,csvfile);
//		getFolderJson("/home/shatam-100/Down/WaterView_Data/FTP_DATA/Data_Folder_2024-10-27/JURUPACOMMUN177");
		getFolderJson("/home/shatam-100/Down/WaterView_Data/FTP_DATA/AprilData/WVMONTEVISTACO226");


	}
    private static void getFolderJson(String inputDirectory) {
		// TODO Auto-generated method stub
    	System.out.println(inputDirectory);
//      String outputDirectory = "/home/shatam-100/Down/WaterView_Data/FTP_DATA/18_March_WD/Data_Folder_2024-03-17/WVWESTERN";
//      File outputDir = new File(outputDirectory);
//      outputDir.mkdirs();
      File[] jsonFiles = new File(inputDirectory).listFiles((dir, name) -> name.toLowerCase().endsWith(".csv"));
      if (jsonFiles != null) {
          for (File jsonFile : jsonFiles) {
             try {
          	   System.out.println(jsonFile);
          	 csvToJSON(jsonFile+"", inputDirectory+"");
			} catch (Exception e) {
			}
          }
      
      }

		
	}
	public static void csvToJSON(String inputFileName, String outputFileName) {
		File input = new File(inputFileName);
		outputFileName = inputFileName.replace(".csv", ".json");
		try {
			CsvSchema csv = CsvSchema.emptySchema().withHeader();
			CsvMapper csvMapper = new CsvMapper();
			MappingIterator<Map<?, ?>> mappingIterator = csvMapper.reader().forType(Map.class).with(csv)
					.readValues(input);
			List<Map<?, ?>> list = mappingIterator.readAll();
			List<JSONObject> jsonObj = new ArrayList<JSONObject>();
			for (Map<?, ?> data : list) {
				JSONObject obj = new JSONObject(data);
				jsonObj.add(obj);
			}
			ObjectMapper objectMapper = new ObjectMapper();
			FileUtil.writeAllText(outputFileName, objectMapper.writeValueAsString(jsonObj));
			System.out.println("Json File Created Successfully");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
