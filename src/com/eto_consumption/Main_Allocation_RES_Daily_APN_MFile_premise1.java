package com.eto_consumption;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

import javax.measure.spi.SystemOfUnits;

import org.apache.commons.math3.analysis.function.Add;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.opencsv.CSVWriter;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

import tech.units.indriya.AbstractSystemOfUnits;

public class Main_Allocation_RES_Daily_APN_MFile_premise1 {
	static ArrayList<Geometry> tiles = new ArrayList<Geometry>();
	static Map<String, Double> allETO = new HashMap<>();
	private static final JSONParser parser = new JSONParser();
	private static String TableName = null;
//  private static final String premiseFile = "/home/shatam-100/Down/WaterView_Data/meter_locations_res_montevista.json";
//  static List<String[]> arrayListOfArrays = new ArrayList<>();
	static Map<String, String[]> hashMapOfArrays = new HashMap<>();

	static Map<String, String> check1 = new HashMap<>();
	static Map<String, String> check2 = new HashMap<>();

	
	static Map<String, String> etonotfoun = new HashMap<>();
	static ArrayList<String[]> consmData = new ArrayList<>();
	static String sqliteFilePath = "/home/shatam-100/Down/WaterView_Data/EtoDatabase.db"; // Replace with your SQLite //
																							// database file path
//    static HashSet<String> uniqueValues = new HashSet<>();  
	static TreeSet<String> uniqueValues = new TreeSet<>();

	static HashSet<String> uniqueMeters = new HashSet<>();
	static Set<String> uniquedata = new HashSet<>();

	static Map<String, String> hashMapPidPoint = new HashMap<>();

	static int count = 0;
	static int NULL = 0;

	@SuppressWarnings("unchecked")
	public static void main(String[] arg) throws Exception {
		String Folder = "/home/shatam-100/Down/WaterView_Data/FTP_DATA/Data_Folder_2024-07-01/WVBRENTWOOD" + "/";
		createConsumtionFile(Folder);
		System.out.println("Total Data Size : " + count + " | " + uniquedata.size());
		System.out.println("NULL : " + NULL);
//		System.out.println(check1.size());
//		System.out.println(check2.size());
//        for (Map.Entry<String, String> entry : check1.entrySet()) {
//            String key = entry.getKey();
//            String value = entry.getValue();
//            System.out.println("Key1: " + key );
//        }
//        for (Map.Entry<String, String> entry : check2.entrySet()) {
//            String key = entry.getKey();
//            String value = entry.getValue();
//            System.out.println("Key2: " + key );
//        }


	}

	private static void createConsumtionFile(String FolderName) throws IOException, ParseException, SQLException {
		// TODO Auto-generated method stub

		long begin = System.currentTimeMillis();

//      String FolderName = "/home/shatam-100/Down/WaterView_Data/FTP_DATA/Data_Folder_2024-04-15/WVRUBIDOUX"+"/";
		String[] st = FolderName.split("/");
		String Waterdistrict_ID = st[st.length - 1];
//      System.out.println("Waterdistrict_ID : "+ Waterdistrict_ID); 
		String WD_Name = getTableName(Waterdistrict_ID);
		TableName = WD_Name.toLowerCase().trim();
//      TableName = "Eto_DB_Tiles";
		System.out.println("TableName" + TableName);
		String meter_locations_res = FolderName + "prd.parcels_res.csv";
		String meter_locations_res_convertedData = FolderName + "prd.parcels_res.json";
//        String outputjsonFile = FolderName + TableName+"_res_daily_Eto_30april.json";
		String outputCSVFile = FolderName + TableName + "_res_daily_Eto_apn.csv";

		FileWriter writer = new FileWriter(outputCSVFile);
		CSVWriter csvWriter = new CSVWriter(writer);

		// Reading consumction File
//        ArrayList<String[]> als = null;
		readAllConsumption(meter_locations_res);
//        System.out.println(als.size());
//		LocalDate startD = LocalDate.of(2017, 1, 1);
		LocalDate startD = LocalDate.of(2024, 4, 1);
//      LocalDate endD = LocalDate.of(2020, 12, 31);
		LocalDate endD = LocalDate.of(2024, 5, 31);
//		LocalDate endD = LocalDate.of(2020,12, 31);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

		LocalDate currentMonth = startD;
		while (!currentMonth.isAfter(endD)) {
//            System.out.println(formatter.format(currentMonth));
			uniqueValues.add(formatter.format(currentMonth).substring(0, 4));
			currentMonth = currentMonth.plusMonths(1);
		}

		System.out.println(uniqueValues);
		System.out.println("uniqueMeters : " + uniqueMeters.size());

		// get all sql data in hashmap
		System.out.println(FolderName);
		System.out.println("Added all sql data in hashmap");
		getAllUniqueTileSqlite(tiles);
		System.out.println("tiles : " + tiles.size());
		run();

		ObjectMapper objectMapper = new ObjectMapper();
		ArrayNode jsonArray = objectMapper.createArrayNode();

//      FileReader fileReader = new FileReader(premiseFile);
//      JSONArray premis;
//      premis = (JSONArray) parser.parse(fileReader);

		WKTReader wktReader = new WKTReader();
		Geometry premise;

//        Map<String, String> hashMapPidPoint = new HashMap<>();
		System.out.println("Adding data in hash Map :: Pid Point");
		File convertedData = new File(meter_locations_res_convertedData);

		JsonNode jsonNode = objectMapper.readTree(convertedData);
		if (jsonNode.isArray()) {
			for (JsonNode objNode : jsonNode) {
				// meter locations res MeterID the_geom
				String meterID = objNode.get("APN").asText();
				String theGeom = objNode.get("the_geom").asText();
				String APN = objNode.get("APN").asText();
//              String theGeom = objNode.get("updatedpoly").asText();
//              String theGeom = objNode.get("geometry").asText();
//              if(!meterID.contains("260250")) continue;
//				if(meterID.contains("meterID")) continue;
				hashMapPidPoint.put(meterID, theGeom + "/" + APN);
			}
		}
		System.out.println("Hash Map Creation Completed");
//      String[] header = { "Date", "premid", "PreTile", "Eto", "Tile" };
//		String[] header = { "Date", "APN", "Eto" }; //MeterID,ET_Value,Date,Source
		String[] header = { "APN", "ET_Value", "Date", "LoadDate", "Source" }; // MeterID,ET_Value,Date,Source

		csvWriter.writeNext(header);
		int i = 0;

		for (String meter : uniqueMeters) {
//            System.out.println(meter);
//            LocalDate startD = LocalDate.of(2024, 1, 1);
//            LocalDate endD = LocalDate.of(2024, 1, 31);

//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");

			LocalDate currentM = startD;
			while (!currentM.isAfter(endD)) {
//                System.out.println(meter+" | "+formatter.format(currentM));
				try {
					getDailyEto(meter, formatter.format(currentM), csvWriter);
					currentM = currentM.plusMonths(1);
//					break;
				} catch (Exception e) {
					System.out.println(tempCounter);
					e.printStackTrace();
//					break;
					// TODO: handle exception
				}
			}
		}
		System.out.println(outputCSVFile);

		long end = System.currentTimeMillis();
		long time = end - begin;
		System.out.println();
		csvWriter.close();
		System.out.println("Elapsed Time: " + (time / 1000) + " seconds");

	}

	static int tempCounter = 0;

	private static void getDailyEto(String meter, String date, CSVWriter csvWriter) throws ParseException {
		// TODO Auto-generated method stub
		WKTReader wktReader = new WKTReader();
		Geometry premise;
		String APN_val = "";

//    	if (meter.contains("meterID"))
//			continue;
//		if (meter.contains("meter_uid"))
//			continue;
		String theGeom = "";
//		System.out.println(meter+hashMapPidPoint.get(meter).toString().split("/")[1]);
		try {
			String[] theGeomValue = hashMapPidPoint.get(meter).toString().split("/");
			APN_val = theGeomValue[1];
			theGeom = theGeomValue[0];
		} catch (Exception e) {
			System.out.println(meter + "   |   " + date);
//			i++;
//			return;
//			continue;
		}

		String myPreTile = "";
		if (theGeom.contains("null"))
			return;

		premise = wktReader.read(theGeom.trim());
		int k = 0;
		String tile = "";
//		try {

//		for (Geometry g : tiles) {
////			System.out.println(g);
////			System.out.println(premise);
//			if(g==null)continue;
////			try {
//				g.intersects(premise);
////			} catch (Exception e) {
////				continue;
////			}
//			if (g.intersects(premise)) {
//				myPreTile = g.toString();
//				k++;
//			}
//		}
//		String sss = "POLYGON ((-121.90567016601562 38.34273329203373,-121.9049835205078 37.99886819160375,-122.43576049804688 37.995621565376595,-122.43164062499999 38.34273329203373,-121.90567016601562 38.34273329203373))";

//		Geometry HHH = 	wktReader.read(sss.trim());

		for (Geometry g : tiles) {
//		System.out.println(tempCounter++;);
			// System.out.println("premise : "+premise);
//			System.out.println("g : "+g);

//			tempCounter++;
//			if (g == null)
//				continue;
			if (premise == null)
				continue;
			if (g.intersects(premise)) {
				myPreTile = g.toString();
//		        System.out.println(myPreTile+date);
				k++;
			}
		}
//		myPreTile = "POLYGON ((-121.90567016601562 38.34273329203373,-121.9049835205078 37.99886819160375,-122.43576049804688 37.995621565376595,-122.43164062499999 38.34273329203373,-121.90567016601562 38.34273329203373))";

//		} catch (Exception e) {
//			// TODO: handle exception
//		}
		Double ETo = 0.0;

		String avgETo = "";
		String key = myPreTile + date.replace("", "");
//		key = key.trim().replaceAll(",-12", ", -12").replace("POLYGON (( ", "POLYGON ((");
//		System.out.println(hashMapOfArrays.get(key.trim())[0]);
		

		String[] gethashvalue1 = hashMapOfArrays.get(key.trim());

		if (gethashvalue1 == null) {
//			System.out.println("key "+key);
//			key
//			check1.put(key, key);
//			System.out.println("NULL");
			NULL++;
			return;
			
		}
		;
		for (String day : gethashvalue1) {
//			System.out.println("day : "+day);
			
//			
			if (day == null || day.length() < 1) {
				continue;
			}

			String[] DEto = day.split(",");
			String date_apn = DEto[0] + "_" + APN_val; // Combining DEto[0] and meter to create a unique key

			if (uniquedata.contains(date_apn)) {
				continue; // Skip if it's a duplicate
			}
			uniquedata.add(date_apn); // Add the unique key to the set
//			if(APN_val.contains("1635")) {
//				System.out.println(date_apn);
//			}

//			String[] DEto = day.split(",");
//			String[] row = {  DEto[0], meter, DEto[1] };
//			String[] row = { DEto[0], APN_val, DEto[1] };
			String eto_val = DEto[1];
			String Da = DEto[0];
			String[] row = { APN_val, eto_val, Da, "", "" }; // "APN","ET_Value","LoadDate","Date","Source"
//			System.out.println( APN_val+eto_val+Da+""+ "" );

			csvWriter.writeNext(row);
			count++;
		}
	}

	private static String getTableName(String Waterdistrict_ID) {
		// TODO Auto-generated method stub
		String filePath = "/home/shatam-100/workspaces/Waterview_CII_Functionalities/waterview_resources/configuration_files/db_config.json";
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
				String currentId = districtObject.get("Waterdistrict_ID").getAsString();
				if (Waterdistrict_ID.equals(currentId)) {
					waterDistrictId = currentId;
					waterDistrictName = districtObject.get("Waterdistrict_Name").getAsString();
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

	public static ArrayList<String[]> readConsumptionn(String consumptionFile) throws Exception {
		System.out.println(" Read Consumption ");

		BufferedReader reader = new BufferedReader(new FileReader(consumptionFile));
		String line;
		int i = 0;
		while ((line = reader.readLine()) != null) {
			String[] values = line.split(",");
			String Mid = values[0];
			String date = values[1];
			String consumption = values[2];

//          System.out.println(i + "  |  " + Mid);
//          i++;
			consmData.add(new String[] { Mid, date, consumption });
		}

		System.out.println(consmData.size());
		return consmData;
	}

	public static ArrayList<String[]> readConsumptionFromJson(String consumptionFile) throws IOException {
//      System.out.println("Reading Consumption Data");

		ArrayList<String[]> consmData = new ArrayList<>();
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			// Read JSON file and convert it to a List of Maps
			List<Map<String, String>> jsonData = objectMapper.readValue(new File(consumptionFile),
					new TypeReference<List<Map<String, String>>>() {
					});

			int lineNumber = 0;
			for (Map<String, String> data : jsonData) {
				lineNumber++;

				// Extract values from the Map
				String mid = data.get("meterID");
				String date = data.get("year_month");
				String consumption = data.get("consumption");
				date = date.replace("_", "-");

				if (mid != null && date != null && consumption != null) {
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
					if (values[1].contains("year")) {
						continue;
					}
					try {
						values[1] = values[1].replace("\"", "");
					} catch (Exception e) {
						// TODO: handle exception
					}
					uniqueValues.add(values[1].substring(0, 4));
//                  System.out.println(lineNumber + " | Mid: " + mid);
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

	public static ArrayList<String[]> readAllConsumption(String consumptionFile) throws IOException {
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
//                    String date = values[1];
//                    String consumption = values[2];
					if (values[1].contains("year")) {
						continue;
					}
					try {
						values[1] = values[1].replace("\"", "");
					} catch (Exception e) {
						// TODO: handle exception
					}
//                    uniqueValues.add(values[1].substring(0, 4));
					uniqueMeters.add(mid);
//                  System.out.println(lineNumber + " | Mid: " + mid);
//                    consmData.add(new String[] { mid, date, consumption });
				} else {
//                    System.err.println("Invalid data on line " + lineNumber);
				}
			}
		} catch (IOException e) {
			// Handle the exception or rethrow it as needed
			throw e;
		}

//        System.out.println("Read " + consmData.size() + " records");
		return consmData;
	}

	static void run() throws SQLException, ParseException, ParseException {
		long begin = System.currentTimeMillis();

		System.out.println("Demo");
		getAllUniqueTileSqlite(tiles);
		System.out.println(tiles.size());

		// Create a thread pool for parallel processing
		ExecutorService executorService = Executors.newFixedThreadPool(10);

		for (Geometry geometry : tiles) {
			executorService.submit(() -> {
				try {
//                  System.out.println(tiles);
//                	 System.out.println(geometry);
					getAllEtoOfAllTilesSqlite(geometry);
				} catch (SQLException | ClassNotFoundException e) {
					e.printStackTrace();
				}
			});
		}

		// Shutdown the thread pool
		executorService.shutdown();

		// Wait for all tasks to complete
		while (!executorService.isTerminated()) {
			// Wait for tasks to complete
		}

		System.out.println("hashMapOfArrays :: " + hashMapOfArrays.size());

		long end = System.currentTimeMillis();

		long time = end - begin;
		System.out.println();
//      System.out.println("Elapsed Time: " + time + " milli seconds");
		System.out.println("Elapsed Time: " + (time / 1000) + " seconds");
	}

	public static void getAllUniqueTileSqlite(ArrayList<Geometry> til) throws SQLException, ParseException {
//      String sqliteFilePath = "/home/shatam-100/Down/WaterView_Data/EtoDatabase.db"; // your SQLite database file path
//      String sqliteFilePath = "/home/shatam-100/Down/WaterView_Data/EtoDatabase.db"; // your SQLite database file path

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		WKTReader wktReader = new WKTReader();

		try {
			// Load the SQLite JDBC driver
			Class.forName("org.sqlite.JDBC");

			// Connect to the SQLite database
			connection = DriverManager.getConnection("jdbc:sqlite:" + sqliteFilePath);

			String sql = "SELECT DISTINCT Tiles FROM " + TableName + ";";
			preparedStatement = connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery(); // Execute the query

			// You can now process the results in the 'resultSet'
			while (resultSet.next()) {
				String tile = resultSet.getString("Tiles");
				System.out.println(tile.trim());
				til.add(wktReader.read(tile.trim()));
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			// Close resources in reverse order
			if (resultSet != null) {
				resultSet.close();
			}
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (connection != null) {
				connection.close();
			}
		}
	}

	public static void getAllEtoOfAllTilesSqlite(Geometry geometry) throws SQLException, ClassNotFoundException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

//        System.out.println("geometrygeometry");
//        try {
		// Load the SQLite JDBC driver
		Class.forName("org.sqlite.JDBC");

		// Connect to the SQLite database
		connection = DriverManager.getConnection("jdbc:sqlite:" + sqliteFilePath);

		String whereClause = "Tiles = ?";

		String[] months = { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" };
//            String[] months1 = { "01", "02", "03" };
//          String[] months = { "01" };
//            System.out.println("geometrygeometry1");

		for (String st : uniqueValues) {

			System.out.println(st);
			for (String month : months) {

				String date = st.trim() + "-" + month;
				String da = date;
//                    System.out.println(da);

//				String sql = "SELECT Date, Precip, ET_Value, Tiles FROM " + TableName + " WHERE (" + whereClause
//						+ ") AND Date LIKE ?";
				String sql = "SELECT Date, Precip, ET_Value, Tiles FROM " + TableName + " WHERE (" + whereClause
						+ ") AND Date LIKE ?";

//                    System.out.println(sql);
				String st_geometry = geometry.toString().trim();
				st_geometry = st_geometry.replaceAll("POLYGON ", "POLYGON").replaceAll(" -1", "-1");
				
        		System.out.println("SELECT Date, Precip, ET_Value, Tiles FROM "+TableName+" WHERE ("+st_geometry+") AND Date LIKE "+da);
//        		System.out.println(st_geometry);

				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setString(1, st_geometry);
				preparedStatement.setString(2, "%" + da + "%");
				resultSet = preparedStatement.executeQuery();
				Double ET_Value = 0.0000;
				Double avgeto = 0.0000;
				int i = 0;
				String[] daily = new String[31];
				while (resultSet.next()) {
					String Date = resultSet.getString("Date");
					Double Precip = resultSet.getDouble("Precip");
					ET_Value = resultSet.getDouble("ET_Value");
					String Tiles = resultSet.getString("Tiles");

//                      String[] array = { Date, Precip.toString(), ET_Value.toString(), Tiles };
//                      arrayListOfArrays.add(array);
					avgeto += ET_Value;
//                      System.out.println(avgeto +"   |   "+ ET_Value);
//                      System.out.println(i +"   |   "+ ET_Value);
//                      System.out.println(i +" | "+Date+" | "+ ET_Value);
					daily[i] = Date + "," + ET_Value;

					i++;
				}
                  System.out.println("System.out.println :: "+i);
//                  System.out.println(" avgeto ::: "+avgeto/i + " | "+geometry.toString()+" | "+da);
//                  String[] arr = { da, (avgeto) + "" };
				String key = geometry + date;
//                  System.out.println(avgeto / i);
//                  System.out.println(key.trim());

//                  System.out.println(arr[0]+"  |  "+arr[1]);
//                  hashMapOfArrays.put(key.trim(), arr);
//                    System.out.println(daily[0]);
//				check2.put(key, key);
				hashMapOfArrays.put(key.trim(), daily);
			}
//                break;
		}
	}
}
