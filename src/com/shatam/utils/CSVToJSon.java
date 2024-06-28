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
		String csvfile = "/home/shatam-100/Down/WaterView_Data/FTP_DATA/sql_file/users_district.csv";
		csvToJSON(csvfile,csvfile);
	}

	public static void csvToJSON(String inputFileName, String outputFileName) {
		File input = new File(inputFileName);
		outputFileName = outputFileName.replace(".csv", ".json");
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
