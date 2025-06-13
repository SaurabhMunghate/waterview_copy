package com.eto_consumption;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.shatam.utils.FileUtil;

public class U {
	
//	static Map<String, String> PB = new HashMap<>();
//	static Map<String, String[]> ML = new HashMap<>();

	public static void csvToJSON(String inputFileName) {
		inputFileName = inputFileName.replace(".json", ".csv");
//		inputFileName = inputFileName.replace(".csv", ".json");
		
		File input = new File(inputFileName);
		
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
			FileUtil.writeAllText(inputFileName.replace(".csv", ".json"), objectMapper.writeValueAsString(jsonObj));
			System.out.println(inputFileName.replace(".csv", ".json"));
			System.out.println("Json File Created Successfully");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	static void getMapValue(String premise_bounds,String meter_locations,Map<String, String[]> PB,Map<String, String[]> ML) throws IOException {
		File premisebounds = new File(premise_bounds);
		File meterlocations = new File(meter_locations);

		ObjectMapper objectMapper = new ObjectMapper();
		ArrayNode jsonArray = objectMapper.createArrayNode();
		
//        Map<String, String> PB = new HashMap<>();
		JsonNode premise = objectMapper.readTree(premisebounds);
		if (premise.isArray()) {
			for (JsonNode objNode : premise) {
				//meter locations res MeterID the_geom
				String premI_D = "";
				try {
					premI_D = objNode.get("premID").asText();
				} catch (Exception e) {
					premI_D = objNode.get("PremID").asText();
				}
				String I_SLA = "0";
				try {
					I_SLA = objNode.get("I_SLA").asText();
				} catch (Exception e) {
				}
				String I = "0";
				try {
					I = objNode.get("I").asText();
				} catch (Exception e) {
				}
//				System.out.println(premI_D+" | "+ I_SLA+" | "+ I);
				ML.put(premI_D, new String[]{I,I_SLA});
			}
		}
//        Map<String, String[]> ML = new HashMap<>();

		JsonNode meter = objectMapper.readTree(meterlocations);
		if (meter.isArray()) {
			for (JsonNode objNode : meter) {
//				System.out.println(meter);
				String premID = objNode.has("premID") ? objNode.get("premID").asText() : objNode.get("PremID").asText();
				String meterID = objNode.has("MeterID") ? objNode.get("MeterID").asText() : objNode.get("meterID").asText();
				String row =objNode.get("row").asText();
				String col =objNode.get("col").asText();
				String grid_row =objNode.get("grid_row").asText();
				String grid_col =objNode.get("grid_col").asText();

//				System.out.println(meterID);
//				PB.put(meterID, premID);
				PB.put(meterID, new String[]{premID,row,col,grid_row,grid_col});
				
			}
		}
		
	}
	public static ArrayList<String[]> readConsumption(String consumptionFile) throws IOException {
		System.out.println("Reading Consumption Data");

		ArrayList<String[]> consmData = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(consumptionFile))) {
			String line;
			int lineNumber = 0;
			while ((line = reader.readLine()) != null) {
				lineNumber++;
				String[] values = line.split(",");
				if (values.length >= 3) {
					String mid = values[0];
					String date = values[1];
					String consumption = values[2];
					
//					System.out.println(lineNumber + " | Mid: " + mid);
					consmData.add(new String[] { mid, date, consumption });
				} else {
					System.err.println("Invalid data on line " + lineNumber);
				}
			}
		} catch (IOException e) {
			// Handle the exception or rethrow it as needed
			throw e;
		}

		System.out.println("Read " + consmData.size() + " records");
		return consmData;
	}

}
