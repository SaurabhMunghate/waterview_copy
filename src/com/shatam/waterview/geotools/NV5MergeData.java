package com.shatam.waterview.geotools;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;
import java.util.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.dataformat.csv.*;
import com.shatam.utils.FileUtil;
import com.shatam.utils.U;

public class NV5MergeData {
	String premiseId = "MtNg0002";
	String[] landscapeCodes= {"1A","1B","2A","3A","3B","4A","4B","5A","6A","9A"};
	public static void main(String[] args) {
		new NV5MergeData().processLandscapeAreaShape();
		// new NV5MergeData().addMeterInfo();
		//new NV5MergeData().csvToJSON();
	}

	private void csvToJSON() {
		// TODO Auto-generated method stub
		String fileName = "/home/chinmay/Downloads/MONTEVISTACO226_Meter_Locations.csv";
		File input = new File(fileName);
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
			FileUtil.writeAllText(fileName.replace(".csv", "_json.json"), objectMapper.writeValueAsString(jsonObj));
			System.out.println("Json File Created Successfully");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void addMeterInfo() {

		String buildingCsv = "/home/chinmay/Downloads/SAWPA-DUMMIE-DATA-SAMPLE/20220525-From-Zach-ALL/CSV/Landscape_Area_Buildings.csv";
		List<String[]> landscapeData = FileUtil.readCsvFile(buildingCsv);
		ArrayList<String[]> outData = new ArrayList<>();
		String header[] = { "Polygon", "PremID", "Class" };
		outData.add(header);
		for (String[] landscapeRow : landscapeData) {
			if (landscapeRow[3].equals("Irrigated")) {
				String outarr[] = { landscapeRow[0], landscapeRow[1], landscapeRow[2] };
				outData.add(outarr);
			}
		}
	}

	String meterLocationdatacsv = "/home/chinmay/Downloads/SAWPA-DUMMIE-DATA-SAMPLE/20220525-From-Zach-ALL/CSV/Meter_Locations_convertedData.csv";
	String landscapedatacsv = "/home/chinmay/Downloads/MONTEVISTACO226_Landscape_Area.csv";

	private void processLandscapeAreaShape() {
		List<String[]> landscapeData = FileUtil.readCsvFile(landscapedatacsv);
		HashMap<String,Integer>landscapeCode=new HashMap<String,Integer>();
		for (String[] landscapeRow : landscapeData) {
			int i=0;
			if(landscapeCode.containsKey(landscapeRow[3]))
				i=landscapeCode.get(landscapeRow[3]);
//			System.out.println(i+"=="+landscapeRow[3]+"--"+landscapeCode.containsKey(landscapeRow[3]));
			landscapeCode.put(landscapeRow[3],++i);
		}
		U.log(landscapeCode);
//		for (String landscapeCode : landscapeCodes) {
//			ArrayList<String[]> outData = new ArrayList<>();
//			String header[] = { "Polygon", "PremID", "Class", "Description","Area" };
//			outData.add(header);
//			for (String[] landscapeRow : landscapeData) {
//				// if(landscapeRow[3].equals("Irrigated")) {
//				if (landscapeRow[2].equals(landscapeCode)) {
//					String outarr[] = { landscapeRow[0], landscapeRow[1], landscapeRow[2], landscapeRow[6], landscapeRow[4] };
//					outData.add(outarr);
//				}
//			}
//			FileUtil.writeCsvFile(outData,
//					"/home/chinmay/Downloads/SAWPA-DUMMIE-DATA-SAMPLE/20220525-From-Zach-ALL/CSV/Landscape_Area_"+landscapeCode+".csv");
//		}
		
		
		// FileUtil.writeCsvFile(outData, 
		// "/home/chinmay/Downloads/SAWPA-DUMMIE-DATA-SAMPLE/20220525-From-Zach-ALL/CSV/testdata.csv");
	}
}
