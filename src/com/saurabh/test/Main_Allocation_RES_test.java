package com.saurabh.test;

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
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.opencsv.CSVWriter;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

import tech.units.indriya.AbstractSystemOfUnits;

public class Main_Allocation_RES_test {
	static ArrayList<Geometry> tiles = new ArrayList<Geometry>();
	static Map<String, Double> allETO = new HashMap<>();
	private static final JSONParser parser = new JSONParser();
	private static String TableName = null;
//	private static final String premiseFile = "/home/shatam-100/Down/WaterView_Data/meter_locations_res_montevista.json";
//	static List<String[]> arrayListOfArrays = new ArrayList<>();
	static Map<String, String[]> hashMapOfArrays = new HashMap<>();
	static Map<String, String> etonotfoun = new HashMap<>();
	static ArrayList<String[]> consmData = new ArrayList<>();
//	static String sqliteFilePath = "/home/shatam-100/Down/WaterView_Data/EtoDatabase.db"; // Replace with your SQLite  // database file path
	static String sqliteFilePath = "/home/shatam-100/Down/WaterView_Data/DB_SQLITE/eto.db";
	static HashSet<String> uniqueValues = new HashSet<>();
	static Map<String, String> keyvaluemap = new HashMap<>();

	@SuppressWarnings("unchecked")
	public static void main(String[] arg) throws Exception {
		long begin = System.currentTimeMillis();

		TableName = "Eto_DB_Tiles";

		String FolderName = "/home/shatam-100/Down/WaterView_Data/FTP_DATA/3_April/WVSANJOSE"+"/";
		String consumctionFile = FolderName + "MonthlyConsumptionRes.csv";
		String meter_locations_res_convertedData = FolderName + "prd.meter_locations_res.json";
		String outputjsonFile = FolderName + TableName+"_res_consumption_Eto_20_March.json";
		String outputCSVFile = FolderName + TableName+"_res_consumption_Eto_20_March.csv";

//		String consumctionFile = "/home/shatam-100/Down/WaterView_Data/FTP_DATA/4_March_2024_3WV/WVROSEVILLE_2024_03_04_011/NEW_MonthlyConsumptionRes.csv";
//		String meter_locations_res_convertedData = "/home/shatam-100/Down/WaterView_Data/FTP_DATA/4_March_2024_3WV/WVROSEVILLE_2024_03_04_011/NEW_prd.meter_locations_res.json";
//		String outputjsonFile = "/home/shatam-100/Down/WaterView_Data/FTP_DATA/4_March_2024_3WV/WVROSEVILLE_2024_03_04_011/WVROSEVILLE_res_consumption_Eto_4_March_c1.json";
//		String outputCSVFile = "/home/shatam-100/Down/WaterView_Data/FTP_DATA/4_March_2024_3WV/WVROSEVILLE_2024_03_04_011/WVROSEVILLE_res_consumption_Eto_4_March_c1.csv";

		FileWriter writer = new FileWriter(outputCSVFile);
		CSVWriter csvWriter = new CSVWriter(writer);

		// Reading consumction File
		ArrayList<String[]> als = null;
		als = readConsumption(consumctionFile);
		System.out.println(als.size());
		System.out.println(uniqueValues);
		
		// get all sql data in hashmap
		System.out.println(FolderName);
		System.out.println("Added all sql data in hashmap");
		System.out.println( );
		run();

		ObjectMapper objectMapper = new ObjectMapper();
		ArrayNode jsonArray = objectMapper.createArrayNode();

//		FileReader fileReader = new FileReader(premiseFile);
//		JSONArray premis;
//		premis = (JSONArray) parser.parse(fileReader);
		

		WKTReader wktReader = new WKTReader();
		Geometry premise;

		Map<String, String> hashMapPidPoint = new HashMap<>();
		System.out.println("Adding data in hash Map :: Pid Point");
		File convertedData = new File(meter_locations_res_convertedData);

		JsonNode jsonNode = objectMapper.readTree(convertedData);
		if (jsonNode.isArray()) {
			for (JsonNode objNode : jsonNode) {
				// meter locations res MeterID the_geom
				String meterID = objNode.get("meterID").asText();
				String theGeom = objNode.get("the_geom").asText();
//				String meterID = objNode.get("meterID").asText();
//				String theGeom = objNode.get("updatedpoly").asText();
//				String theGeom = objNode.get("geometry").asText();
//				if(!meterID.contains("100604")) continue;
				hashMapPidPoint.put(meterID, theGeom);
			}
		}
		System.out.println("Hash Map Creation Completed");
//		String[] header = { "Date", "premid", "PreTile", "Eto", "Tile" };
		String[] header = { "Date", "APN", "PreTile", "Eto", "Tile", "Consumtion" };

		csvWriter.writeNext(header);
		int i = 0;
		for (String[] data : als) {
			String Consumtion = data[2];
			String Pid = data[0];
			String Date = data[1].replace("_", "-").trim();
			String formattedDate = "";
			try {
				String originalDateString = Date;
			} catch (Exception e) {
			}
			String date = Date;

			if (Pid.contains("meterID"))
				continue;
			if (Pid.contains("meter_uid"))
				continue;
			String Year12Date = "";
//			if (i == 1) {
//				System.out.println(i + " | " + Date);
//				char r1 = (char) (Date.charAt(3));
//				char r2 = (char) (Date.charAt(3) + 1);
////				String Year12Date = Date.replace(r1,r2 );
//				Year12Date = Date.substring(0, 3) + r2 + Date.substring(3 + 1);
//
//				System.out.println("Date " + Date.charAt(3));
//				System.out.println(Year12Date);
//			}
			String theGeom = "";
			try {
				theGeom = hashMapPidPoint.get(Pid).toString();
			} catch (Exception e) {
				System.out.println(Pid + "   |   " + date);
				i++;
				continue;
			}

			String myPreTile = "";

			premise = wktReader.read(theGeom);
			int k = 0;
			String tile = "";
//			try {

			for (Geometry g : tiles) {
				if (g.intersects(premise)) {
					myPreTile = g.toString();
					k++;
				}
			}

//			} catch (Exception e) {
//				// TODO: handle exception
//			}
			Double ETo = 0.0;

			String avgETo = "";
			String key = myPreTile + date.replace("", "");

			key = key.trim();
//			System.out.println(key);
			String[] gethashvalue1 = hashMapOfArrays.get(key);
			try {
				avgETo = gethashvalue1[1];
			} catch (Exception e) {
				etonotfoun.put(key, key);
				System.out.println("myPreTile : " + myPreTile);
				System.out.println("key : " + key);
				System.out.println(premise);
				avgETo = "0.0";
				// TODO: handle exception
			}

			// Iterate over the entries and print them
			for (Map.Entry<String, String> entry : etonotfoun.entrySet()) {
//	            System.out.println("Key: " + entry.getKey());
			}
			if (key.contains("2023-01")) {
//				System.out.println("date : " + date);
//				System.out.println("avgETo : " + avgETo);
//				return;
			}

//	        double allocation=(0.62)*((Double.parseDouble(dataArr[3])*0.8*(avgEto-avgprecip))+(1.0*Double.parseDouble(dataArr[2])*avgEto)+(1*0.45*avgEto))+0;
			ObjectNode jsonObject = objectMapper.createObjectNode();
			jsonObject.put("Date", date);
			jsonObject.put("APN", Pid);
			jsonObject.put("PreTile", theGeom);
			jsonObject.put("Eto", avgETo);
			jsonObject.put("Tile", tile);
			jsonObject.put("Consumtion", Consumtion);
			jsonArray.add(jsonObject);
			String[] row = { date, Pid, theGeom, avgETo, tile, Consumtion };
			csvWriter.writeNext(row);
			i++;

//			return;
		}
		outputjsonFile = outputjsonFile.replace(".txt", i + "_.txt");
		objectMapper.writeValue(new File(outputjsonFile), jsonArray);
		System.out.println(outputjsonFile);
		System.out.println(outputCSVFile);

		long end = System.currentTimeMillis();
		long time = end - begin;
		System.out.println();
		csvWriter.close();
		System.out.println("Elapsed Time: " + (time / 1000) + " seconds");
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

//			System.out.println(i + "  |  " + Mid);
//			i++;
			consmData.add(new String[] { Mid, date, consumption });
		}

		System.out.println(consmData.size());
		return consmData;
	}

	public static ArrayList<String[]> readConsumptionFromJson(String consumptionFile) throws IOException {
//	    System.out.println("Reading Consumption Data");

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
					if(values[1].contains("year")) {
						continue;
					}
					uniqueValues.add(values[1].substring(0, 4));
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

	static void run() throws SQLException, ParseException {
		long begin = System.currentTimeMillis();

		System.out.println("Demo");
		getAllKeyValue();
		getAllUniqueTileSqlite(tiles);
		
		System.out.println(tiles.size());

		// Create a thread pool for parallel processing
		ExecutorService executorService = Executors.newFixedThreadPool(10);

		for (Geometry geometry : tiles) {
			executorService.submit(() -> {
				try {
//                	System.out.println(geometry);
					getAllEtoOfAllTilesSqlite(geometry);
				} catch (SQLException e) {
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
//		System.out.println("Elapsed Time: " + time + " milli seconds");
		System.out.println("Elapsed Time: " + (time / 1000) + " seconds");
	}

	private static void getAllKeyValue() {
//		System.out.println("getAllKeyValue");

        // JDBC connection variables
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            // Load the SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");

            // Create a connection to the database
            connection = DriverManager.getConnection("jdbc:sqlite:" + sqliteFilePath);

            // Create a statement
            statement = connection.createStatement();

            // Execute a query to retrieve data from eto_table
            resultSet = statement.executeQuery("SELECT * FROM key_table");

            // Iterate through the result set and add data to the HashMap
            while (resultSet.next()) {
                String key = resultSet.getString("Pkey"); // Assuming the first column is the key
                String value = resultSet.getString("Tiles"); // Assuming the second column is the value
                keyvaluemap.put(key, value);
            }

            // Print the HashMap for verification
//            System.out.println(hashMap);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the database resources
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
//        System.out.println("key value map : "+keyvaluemap);
}

	public static void getAllUniqueTileSqlite(ArrayList<Geometry> til) throws SQLException, ParseException {
//		String sqliteFilePath = "/home/shatam-100/Down/WaterView_Data/EtoDatabase.db"; // your SQLite database file path
//		String sqliteFilePath = "/home/shatam-100/Down/WaterView_Data/EtoDatabase.db"; // your SQLite database file path

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
				String polygonString = getTile(tile);
//				String polygonString = keyvaluemap.get(tile);
//				System.out.println(polygonString);
				til.add(wktReader.read(polygonString.trim()));
				
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

	private static String getTile(String tile) {
		// TODO Auto-generated method stub
//		System.out.println(tile);
        String[] coordinates = tile.split("_");
        double x = Double.parseDouble(coordinates[0]);
        double y = Double.parseDouble(coordinates[1]);
        double x1 = Double.parseDouble(coordinates[2]);
        double y1 = Double.parseDouble(coordinates[3]);
        String polygonString = "POLYGON ((" + x + " " + y + ", " + x1 + " " + y + ", " + x1 + " " + y1 + ", " + x + " " + y1 + ", " + x + " " + y + "))";
//        System.out.println("Polygon String: " + polygonString);

		return polygonString;
	}

	public static void getAllEtoOfAllTilesSqlite(Geometry geometry) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			// Load the SQLite JDBC driver
			Class.forName("org.sqlite.JDBC");

			// Connect to the SQLite database
			connection = DriverManager.getConnection("jdbc:sqlite:" + sqliteFilePath);

			String whereClause = "Tiles = ?";
//			System.out.println();
			String[] months = { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" };
			for(String st : uniqueValues) {
//				System.out.print(st);
				for (String month : months) {
					String date = st.trim()+"-" + month;
					String da = date;

					String sql = "SELECT Date, Precip, ET_Value, Tiles FROM " + TableName + " WHERE (" + whereClause
							+ ") AND Date LIKE ?";
					preparedStatement = connection.prepareStatement(sql);
					preparedStatement.setString(1, geometry.toString());
					preparedStatement.setString(2, "%" + da + "%");
					resultSet = preparedStatement.executeQuery();
					Double ET_Value = 0.0000;
					Double avgeto = 0.0000;
					int i = 0;
					while (resultSet.next()) {
						String Date = resultSet.getString("Date");
						Double Precip = resultSet.getDouble("Precip");
						ET_Value = resultSet.getDouble("ET_Value");
						String Tiles = resultSet.getString("Tiles");

//	                    String[] array = { Date, Precip.toString(), ET_Value.toString(), Tiles };
//	                    arrayListOfArrays.add(array);
						avgeto += ET_Value;
//						System.out.println(avgeto +"   |   "+ ET_Value);
//						System.out.println(i +"   |   "+ ET_Value);

						i++;
					}
//					System.out.println("System.out.println :: "+i);
//					System.out.println(" avgeto ::: "+avgeto/i + " | "+geometry.toString()+" | "+da);
					String[] arr = { da, (avgeto) + "" };
					String key = geometry + date;
//					System.out.println(avgeto / i);
//					System.out.println(key);
//					System.out.println(arr[0]+"  |  "+arr[1]);
					hashMapOfArrays.put(key.trim(), arr);
				}
			}
//			for (String month : months) {
//				String date = "2023-" + month;
//				String da = date;
//
//				String sql = "SELECT Date, Precip, ET_Value, Tiles FROM " + TableName + " WHERE (" + whereClause
//						+ ") AND Date LIKE ?";
//				preparedStatement = connection.prepareStatement(sql);
//				preparedStatement.setString(1, geometry.toString());
//				preparedStatement.setString(2, "%" + da + "%");
//				resultSet = preparedStatement.executeQuery();
//				Double ET_Value = 0.0000;
//				Double avgeto = 0.0000;
//				int i = 0;
//				while (resultSet.next()) {
//					String Date = resultSet.getString("Date");
//					Double Precip = resultSet.getDouble("Precip");
//					ET_Value = resultSet.getDouble("ET_Value");
//					String Tiles = resultSet.getString("Tiles");
//
////                    String[] array = { Date, Precip.toString(), ET_Value.toString(), Tiles };
////                    arrayListOfArrays.add(array);
//					avgeto += ET_Value;
////					System.out.println(avgeto +"   |   "+ ET_Value);
////					System.out.println(i +"   |   "+ ET_Value);
//
//					i++;
//				}
////				System.out.println("System.out.println :: "+i);
////				System.out.println(" avgeto ::: "+avgeto/i + " | "+geometry.toString()+" | "+da);
//				String[] arr = { da, (avgeto) + "" };
//				String key = geometry + date;
////				System.out.println(avgeto / i);
////				System.out.println(key);
////				System.out.println(arr[0]+"  |  "+arr[1]);
//				hashMapOfArrays.put(key.trim(), arr);
//			}
//			for (String month : months) {
//				String date = "2022-" + month;
//				String da = date;
//
//				String sql = "SELECT Date, Precip, ET_Value, Tiles FROM " + TableName + " WHERE (" + whereClause
//						+ ") AND Date LIKE ?";
//				preparedStatement = connection.prepareStatement(sql);
//				preparedStatement.setString(1, geometry.toString());
//				preparedStatement.setString(2, "%" + da + "%");
//				resultSet = preparedStatement.executeQuery();
//				Double ET_Value = 0.0;
//				Double avgeto = 0.0;
//				int i = 0;
//				while (resultSet.next()) {
//					String Date = resultSet.getString("Date");
//					Double Precip = resultSet.getDouble("Precip");
//					ET_Value = resultSet.getDouble("ET_Value");
//					String Tiles = resultSet.getString("Tiles");
//
////                    String[] array = { Date, Precip.toString(), ET_Value.toString(), Tiles };
////                    arrayListOfArrays.add(array);
//					avgeto += ET_Value;
////					System.out.println(avgeto +"   |   "+ ET_Value);
//					i++;
//				}
////				System.out.println(" avgeto ::: "+avgeto/i+ "/n"+geometry.toString()+"/n"+da);
//				String[] arr = { da, (avgeto) + "" };
//				String key = geometry + date;
////				System.out.println(key);
////				System.out.println(arr[0]+"  |  "+arr[1]);
//				hashMapOfArrays.put(key.trim(), arr);
//			}
//			for (String month : months) {
//				String date = "2021-" + month;
//				String da = date;
//
//				String sql = "SELECT Date, Precip, ET_Value, Tiles FROM " + TableName + " WHERE (" + whereClause
//						+ ") AND Date LIKE ?";
//				preparedStatement = connection.prepareStatement(sql);
//				preparedStatement.setString(1, geometry.toString());
//				preparedStatement.setString(2, "%" + da + "%");
//				resultSet = preparedStatement.executeQuery();
//				Double ET_Value = 0.0;
//				Double avgeto = 0.0;
//				int i = 0;
//				while (resultSet.next()) {
//					String Date = resultSet.getString("Date");
//					Double Precip = resultSet.getDouble("Precip");
//					ET_Value = resultSet.getDouble("ET_Value");
//					String Tiles = resultSet.getString("Tiles");
//
////                    String[] array = { Date, Precip.toString(), ET_Value.toString(), Tiles };
////                    arrayListOfArrays.add(array);
//					avgeto += ET_Value;
////					System.out.println(avgeto +"   |   "+ ET_Value);
//					i++;
//				}
////				System.out.println(" avgeto ::: "+avgeto/i+ "/n"+geometry.toString()+"/n"+da);
//				String[] arr = { da, (avgeto) + "" };
//				String key = geometry + date;
////				System.out.println(key);
////				System.out.println(arr[0]+"  |  "+arr[1]);
//				hashMapOfArrays.put(key.trim(), arr);
//			}
//			for (String month : months) {
//				String date = "2020-" + month;
//				String da = date;
//
//				String sql = "SELECT Date, Precip, ET_Value, Tiles FROM " + TableName + " WHERE (" + whereClause
//						+ ") AND Date LIKE ?";
//				preparedStatement = connection.prepareStatement(sql);
//				preparedStatement.setString(1, geometry.toString());
//				preparedStatement.setString(2, "%" + da + "%");
//				resultSet = preparedStatement.executeQuery();
//				Double ET_Value = 0.0;
//				Double avgeto = 0.0;
//				int i = 0;
//				while (resultSet.next()) {
//					String Date = resultSet.getString("Date");
//					Double Precip = resultSet.getDouble("Precip");
//					ET_Value = resultSet.getDouble("ET_Value");
//					String Tiles = resultSet.getString("Tiles");
//
////                    String[] array = { Date, Precip.toString(), ET_Value.toString(), Tiles };
////                    arrayListOfArrays.add(array);
//					avgeto += ET_Value;
////					System.out.println(avgeto +"   |   "+ ET_Value);
//					i++;
//				}
////				System.out.println(" avgeto ::: "+avgeto/i+ "/n"+geometry.toString()+"/n"+da);
//				String[] arr = { da, (avgeto) + "" };
//				String key = geometry + date;
//				// System.out.println(key);
//				hashMapOfArrays.put(key.trim(), arr);
//			}
//			for (String month : months) {
//				String date = "2019-" + month;
//				String da = date;
//
//				String sql = "SELECT Date, Precip, ET_Value, Tiles FROM " + TableName + " WHERE (" + whereClause
//						+ ") AND Date LIKE ?";
//				preparedStatement = connection.prepareStatement(sql);
//				preparedStatement.setString(1, geometry.toString());
//				preparedStatement.setString(2, "%" + da + "%");
//				resultSet = preparedStatement.executeQuery();
//				Double ET_Value = 0.0;
//				Double avgeto = 0.0;
//				int i = 0;
//				while (resultSet.next()) {
//					String Date = resultSet.getString("Date");
//					Double Precip = resultSet.getDouble("Precip");
//					ET_Value = resultSet.getDouble("ET_Value");
//					String Tiles = resultSet.getString("Tiles");
//
////                    String[] array = { Date, Precip.toString(), ET_Value.toString(), Tiles };
////                    arrayListOfArrays.add(array);
//					avgeto += ET_Value;
////					System.out.println(avgeto +"   |   "+ ET_Value);
//					i++;
//				}
////				System.out.println(" avgeto ::: "+avgeto/i+ "/n"+geometry.toString()+"/n"+da);
//				String[] arr = { da, (avgeto) + "" };
//				String key = geometry + date;
////				System.out.println(key);
////				System.out.println(arr[0]+"  |  "+arr[1]);
//				hashMapOfArrays.put(key.trim(), arr);
//			}

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			// Close database resources in a finally block
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
}
