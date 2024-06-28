package com.saurabh;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.opencsv.CSVWriter;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

import tech.units.indriya.AbstractSystemOfUnits;

public class Main_NV5ConsumptionFile {
	static ArrayList<Geometry> tiles = new ArrayList<Geometry>();
	static Map<String, Double> allETO = new HashMap<>();
	private static final JSONParser parser = new JSONParser();
//	private static final String premiseFile = "/home/shatam-100/Down/WaterView_Data/meter_locations_res_montevista.json";
//	static List<String[]> arrayListOfArrays = new ArrayList<>();
	static Map<String, String[]> hashMapOfArrays = new HashMap<>();
	static Map<String, String> etonotfoun = new HashMap<>();
	static ArrayList<String[]> consmData = new ArrayList<>();

	@SuppressWarnings("unchecked")
	public static void main(String[] arg) throws Exception {
		long begin = System.currentTimeMillis();
		
//		String consumctionFile = "/home/shatam-100/Down/WaterView_Data/sanjose_consumption_res_all.csv";
//		String outputjsonFile = "/home/shatam-100/Down/WaterView_Data/SanJose_Consumtion_Eto_03_Oct_4.03_.txt";
//		String outputCSVFile = "/home/shatam-100/Down/WaterView_Data/SanJose_Consumtion_Eto_03_Oct_4.03_.csv";
		
//		String consumctionFile = "/home/shatam-100/Down/WaterView_Data/MonteVistaRes-TestDataSet/montevista_consumption_res_all.csv";
		String consumctionFile = "/home/shatam-100/Down/WaterView_Data/MonteVistaMonthlyNormalized.csv";
		
		
//		String consumctionFile = "/home/shatam-100/Down/WaterView_Data/montevista_consumption_res_all_40.csv";
		String outputjsonFile = "/home/shatam-100/Down/WaterView_Data/montevista_consumption_Eto_8_Feb_11.json";
		String outputCSVFile = "/home/shatam-100/Down/WaterView_Data/montevista_consumption_Eto_8_Feb_11.csv";
		
		FileWriter writer = new FileWriter(outputCSVFile);
		CSVWriter csvWriter = new CSVWriter(writer);

		// get all sql data in hashmap
		System.out.println("Added all sql data in hashmap");
		run();

		
//		for (Map.Entry<String, String[]> entry : hashMapOfArrays.entrySet()) {
//		    String key = entry.getKey();
//		    String[] valueArray = entry.getValue();
//
//		    System.out.println("Key: " + key);
//		    System.out.println("Value:");
//
//		    for (String val : valueArray) {
//		        System.out.println(val);
//		    }
//		}

		ObjectMapper objectMapper = new ObjectMapper();
		ArrayNode jsonArray = objectMapper.createArrayNode();

//		FileReader fileReader = new FileReader(premiseFile);
//		JSONArray premis;
//		premis = (JSONArray) parser.parse(fileReader);
		ArrayList<String[]> als = null;
//		ArrayList<String[]> alss = null;
		als = readConsumption(consumctionFile);

//		System.out.println(alss.size());

		WKTReader wktReader = new WKTReader();
		Geometry premise;

		Map<String, String> hashMapPidPoint = new HashMap<>();
		System.out.println("Adding data in hash Map :: Pid Point");
                           
//		String meter_locations_res_convertedData = "/home/shatam-100/Down/WaterView_Data/meter_locations_res_montevista.json";
		String meter_locations_res_convertedData = "/home/shatam-100/Down/WaterView_Data/MONTEVISTACO226_2024_02_05_18_Decrypted/NEW_prd.meter_locations.json";

//		String meter_locations_res_convertedData = "/home/shatam-100/Down/WaterView_Data/meter_locations_res_convertedData.txt";
		// Read the JSON file
		File convertedData = new File(meter_locations_res_convertedData);

		JsonNode jsonNode = objectMapper.readTree(convertedData);
		if (jsonNode.isArray()) {
			for (JsonNode objNode : jsonNode) {
				//meter locations res MeterID the_geom
				String meterID = objNode.get("MeterID").asText();
				String theGeom = objNode.get("the_geom").asText();
//				String theGeom = objNode.get("geometry").asText();
				hashMapPidPoint.put(meterID, theGeom);
			}
		}
		System.out.println("Hash Map Creation Completed");
//		String[] header = { "Date", "premid", "PreTile", "Eto", "Tile" };
		String[] header = { "Date", "APN", "PreTile", "Eto", "Tile" };

		csvWriter.writeNext(header);
		int i = 0;
		for (String[] data : als) {
			//consumption File MeterID	ReadingDate
//			String Pid = data[0];
//			String date = data[1];
			
			String Pid = data[1];
//			String date = data[2];
			String formattedDate = "";
			try {
		        String originalDateString = data[2];
		        formattedDate = LocalDate.parse(originalDateString, DateTimeFormatter.ofPattern("M/d/yyyy")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			} catch (Exception e) {
				// TODO: handle exception
			}
//	        System.out.println(formattedDate);

			String date = formattedDate;

			if (Pid.contains("MeterID"))
				continue;
			if (Pid.contains("meter_uid"))
				continue;
//			System.out.println(i);
//			System.out.println(Pid);
//			System.out.println(date);

			String theGeom = "";
			try {
				theGeom = hashMapPidPoint.get(Pid).toString();
			} catch (Exception e) {
				System.out.println(Pid+"   |   "+date);
//				System.out.print(date);
				continue;
				// TODO: handle exception
			}

			String myPreTile = "";

			premise = wktReader.read(theGeom);
			int k = 0;
			String tile = "";
//			try {

				for (Geometry g : tiles) {
	//				System.out.println(premise);
	//				System.out.println(g);
					if (g.intersects(premise)) {
						myPreTile = g.toString();
	//					System.out.println(premise);
	//					System.out.println(g);
						k++;
					}
				}

//			} catch (Exception e) {
//				// TODO: handle exception
//			}
			Double ETo = 0.0;

			String avgETo = "";
			String key = myPreTile + date.replace("-01", "");

			if (date.equals("2020-01-01"))
				key = myPreTile + "2020-01";
			if (date.equals("2021-01-01"))
				key = myPreTile + "2021-01";
			if (date.equals("2022-01-01"))
				key = myPreTile + "2022-01";
			if (date.equals("2023-01-01"))
				key = myPreTile + "2023-01";

			key = key.trim();
//			System.out.println(key);
			String[] gethashvalue1 = hashMapOfArrays.get(key);
			try {
				avgETo = gethashvalue1[1];
			} catch (Exception e) {
				etonotfoun.put(key, key);
//				System.out.println(key);
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
			ObjectNode jsonObject = objectMapper.createObjectNode();
			jsonObject.put("Date", date);
			jsonObject.put("APN", Pid);
			jsonObject.put("PreTile", theGeom);
			jsonObject.put("Eto", avgETo);
			jsonObject.put("Tile", tile);
			jsonArray.add(jsonObject);
//			System.out.println(avgETo);
			String[] row = { date, Pid, theGeom, avgETo, tile };
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
			i++;
			consmData.add(new String[] { Mid, date, consumption });
		}

		System.out.println(consmData.size());
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
		getAllUniqueTileSqlite(tiles);
		System.out.println(tiles.size());

		// Create a thread pool for parallel processing
		ExecutorService executorService = Executors.newFixedThreadPool(10);

		for (Geometry geometry : tiles) {
			executorService.submit(() -> {
				try {
//                	System.out.println(tiles);
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

	public static void getAllUniqueTileSqlite(ArrayList<Geometry> til) throws SQLException, ParseException {
		String sqliteFilePath = "/home/shatam-100/Down/WaterView_Data/EtoDatabase.db"; // your SQLite database file path

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		WKTReader wktReader = new WKTReader();

		try {
			// Load the SQLite JDBC driver
			Class.forName("org.sqlite.JDBC");

			// Connect to the SQLite database
			connection = DriverManager.getConnection("jdbc:sqlite:" + sqliteFilePath);

			String sql = "SELECT DISTINCT Tiles FROM Eto_DB_Tiles;";
			preparedStatement = connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery(); // Execute the query

			// You can now process the results in the 'resultSet'
			while (resultSet.next()) {
				String tile = resultSet.getString("Tiles");
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

//	static void getAllUniqueTile(ArrayList<Geometry> til) throws SQLException, ParseException {
//		// TODO Auto-generated method stub
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/SAN_JOSE_ETo_DB_Tiles.csv";
//		String jdbcUrl = "jdbc:mysql://localhost:3306/eto";
//		String dbUser = "root";
//		String dbPassword = "root";
//
//		Connection connection = null;
//		PreparedStatement preparedStatement = null;
//		ResultSet resultSet = null;
//		WKTReader wktReader = new WKTReader();
//		try {
//			connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword);
//			String sql = "SELECT DISTINCT Tiles FROM Eto_DB_Tiles;";
//			preparedStatement = connection.prepareStatement(sql);
//			resultSet = preparedStatement.executeQuery(); // Execute the query
//
//			// You can now process the results in the 'resultSet'
//			while (resultSet.next()) {
//				String tile = resultSet.getString("Tiles");
////                System.out.println("" + tile);
//				til.add(wktReader.read(tile.trim()));
//			}
////			System.out.println(til.size());
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			// Close resources in reverse order
//			if (resultSet != null) {
//				resultSet.close();
//			}
//			if (preparedStatement != null) {
//				preparedStatement.close();
//			}
//			if (connection != null) {
//				connection.close();
//			}
//		}
//	}

	public static void getAllEtoOfAllTilesSqlite(Geometry geometry) throws SQLException {
		String sqliteFilePath = "/home/shatam-100/Down/WaterView_Data/EtoDatabase.db"; // Replace with your SQLite
																					   // database file path

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			// Load the SQLite JDBC driver
			Class.forName("org.sqlite.JDBC");

			// Connect to the SQLite database
			connection = DriverManager.getConnection("jdbc:sqlite:" + sqliteFilePath);

			String whereClause = "Tiles = ?";

			String[] months = { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" };
			for (String month : months) {
				String date = "2023-" + month;
				String da = date;

				String sql = "SELECT Date, Precip, ET_Value, Tiles FROM Eto_DB_Tiles WHERE (" + whereClause
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

//                    String[] array = { Date, Precip.toString(), ET_Value.toString(), Tiles };
//                    arrayListOfArrays.add(array);
					avgeto += ET_Value;
//					System.out.println(avgeto +"   |   "+ ET_Value);
//					System.out.println(i +"   |   "+ ET_Value);

					i++;
				}
//				System.out.println("System.out.println :: "+i);
//				System.out.println(" avgeto ::: "+avgeto/i + " | "+geometry.toString()+" | "+da);
				String[] arr = { da, (avgeto / i) + "" };
				String key = geometry + date;
//				System.out.println(avgeto / i);
//				System.out.println(key);
//				System.out.println(arr[0]+"  |  "+arr[1]);
				hashMapOfArrays.put(key.trim(), arr);
			}
			for (String month : months) {
				String date = "2022-" + month;
				String da = date;

				String sql = "SELECT Date, Precip, ET_Value, Tiles FROM Eto_DB_Tiles WHERE (" + whereClause
						+ ") AND Date LIKE ?";
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setString(1, geometry.toString());
				preparedStatement.setString(2, "%" + da + "%");
				resultSet = preparedStatement.executeQuery();
				Double ET_Value = 0.0;
				Double avgeto = 0.0;
				int i = 0;
				while (resultSet.next()) {
					String Date = resultSet.getString("Date");
					Double Precip = resultSet.getDouble("Precip");
					ET_Value = resultSet.getDouble("ET_Value");
					String Tiles = resultSet.getString("Tiles");

//                    String[] array = { Date, Precip.toString(), ET_Value.toString(), Tiles };
//                    arrayListOfArrays.add(array);
					avgeto += ET_Value;
//					System.out.println(avgeto +"   |   "+ ET_Value);
					i++;
				}
//				System.out.println(" avgeto ::: "+avgeto/i+ "/n"+geometry.toString()+"/n"+da);
				String[] arr = { da, (avgeto / i) + "" };
				String key = geometry + date;
//				System.out.println(key);
//				System.out.println(arr[0]+"  |  "+arr[1]);
				hashMapOfArrays.put(key.trim(), arr);
			}
			for (String month : months) {
				String date = "2021-" + month;
				String da = date;

				String sql = "SELECT Date, Precip, ET_Value, Tiles FROM Eto_DB_Tiles WHERE (" + whereClause
						+ ") AND Date LIKE ?";
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setString(1, geometry.toString());
				preparedStatement.setString(2, "%" + da + "%");
				resultSet = preparedStatement.executeQuery();
				Double ET_Value = 0.0;
				Double avgeto = 0.0;
				int i = 0;
				while (resultSet.next()) {
					String Date = resultSet.getString("Date");
					Double Precip = resultSet.getDouble("Precip");
					ET_Value = resultSet.getDouble("ET_Value");
					String Tiles = resultSet.getString("Tiles");

//                    String[] array = { Date, Precip.toString(), ET_Value.toString(), Tiles };
//                    arrayListOfArrays.add(array);
					avgeto += ET_Value;
//					System.out.println(avgeto +"   |   "+ ET_Value);
					i++;
				}
//				System.out.println(" avgeto ::: "+avgeto/i+ "/n"+geometry.toString()+"/n"+da);
				String[] arr = { da, (avgeto / i) + "" };
				String key = geometry + date;
//				System.out.println(key);
//				System.out.println(arr[0]+"  |  "+arr[1]);
				hashMapOfArrays.put(key.trim(), arr);
			}
			for (String month : months) {
				String date = "2020-" + month;
				String da = date;

				String sql = "SELECT Date, Precip, ET_Value, Tiles FROM Eto_DB_Tiles WHERE (" + whereClause
						+ ") AND Date LIKE ?";
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setString(1, geometry.toString());
				preparedStatement.setString(2, "%" + da + "%");
				resultSet = preparedStatement.executeQuery();
				Double ET_Value = 0.0;
				Double avgeto = 0.0;
				int i = 0;
				while (resultSet.next()) {
					String Date = resultSet.getString("Date");
					Double Precip = resultSet.getDouble("Precip");
					ET_Value = resultSet.getDouble("ET_Value");
					String Tiles = resultSet.getString("Tiles");

//                    String[] array = { Date, Precip.toString(), ET_Value.toString(), Tiles };
//                    arrayListOfArrays.add(array);
					avgeto += ET_Value;
//					System.out.println(avgeto +"   |   "+ ET_Value);
					i++;
				}
//				System.out.println(" avgeto ::: "+avgeto/i+ "/n"+geometry.toString()+"/n"+da);
				String[] arr = { da, (avgeto / i) + "" };
				String key = geometry + date;
				//System.out.println(key);
				hashMapOfArrays.put(key.trim(), arr);
			}
			for (String month : months) {
				String date = "2019-" + month;
				String da = date;

				String sql = "SELECT Date, Precip, ET_Value, Tiles FROM Eto_DB_Tiles WHERE (" + whereClause
						+ ") AND Date LIKE ?";
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setString(1, geometry.toString());
				preparedStatement.setString(2, "%" + da + "%");
				resultSet = preparedStatement.executeQuery();
				Double ET_Value = 0.0;
				Double avgeto = 0.0;
				int i = 0;
				while (resultSet.next()) {
					String Date = resultSet.getString("Date");
					Double Precip = resultSet.getDouble("Precip");
					ET_Value = resultSet.getDouble("ET_Value");
					String Tiles = resultSet.getString("Tiles");

//                    String[] array = { Date, Precip.toString(), ET_Value.toString(), Tiles };
//                    arrayListOfArrays.add(array);
					avgeto += ET_Value;
//					System.out.println(avgeto +"   |   "+ ET_Value);
					i++;
				}
//				System.out.println(" avgeto ::: "+avgeto/i+ "/n"+geometry.toString()+"/n"+da);
				String[] arr = { da, (avgeto / i) + "" };
				String key = geometry + date;
//				System.out.println(key);
//				System.out.println(arr[0]+"  |  "+arr[1]);
				hashMapOfArrays.put(key.trim(), arr);
			}

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

//	private static void getAllEtoOfAllTiles(Geometry geometry) throws SQLException {
//		String jdbcUrl = "jdbc:mysql://localhost:3306/eto";
//		String dbUser = "root";
//		String dbPassword = "root";
//
//		Connection connection = null;
//		PreparedStatement preparedStatement = null;
//		ResultSet resultSet = null;
//
//		try {
//			connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword);
//
//			String whereClause = "Tiles = ?";
//
//			String[] months = { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" };
//			for (String month : months) {
//				String date = "2023-" + month;
//				String da = date;
////                System.out.println(date);
////                String da = date.replace("01", "1").replace("02", "2").replace("03", "3").replace("04", "4").replace("05", "5")
////                        .replace("06", "6").replace("07", "7").replace("08", "8").replace("09", "9");
//
//				// Use a prepared statement with parameters
//				String sql = "SELECT Date, Precip, ET_Value, Tiles FROM Eto_DB_Tiles WHERE (" + whereClause
//						+ ") AND Date LIKE ?";
//				preparedStatement = connection.prepareStatement(sql);
//				preparedStatement.setString(1, geometry.toString());
//				preparedStatement.setString(2, "%" + da + "%");
//				resultSet = preparedStatement.executeQuery();
//				Double ET_Value = 0.0;
//				Double avgeto = 0.0;
//				int i = 0;
////				System.out.println(geometry+da);
//				while (resultSet.next()) {
//					String Date = resultSet.getString("Date");
//					Double Precip = resultSet.getDouble("Precip");
//					ET_Value = resultSet.getDouble("ET_Value");
//					String Tiles = resultSet.getString("Tiles");
//
////					String[] array = { Date, Precip.toString(), ET_Value.toString(), Tiles };
////					arrayListOfArrays.add(array);
////					System.out.println(ET_Value);
//					avgeto += ET_Value;
//					i++;
//				}
//				avgeto = avgeto / i;
////				System.out.println("ET_Value :: "+avgeto);
//				String[] arr = { da, avgeto.toString() };
//				String key = geometry + da;
//				ET_Value = 0.0;
////				System.out.println(key);
////                System.out.println(arr);
//				hashMapOfArrays.put(key.trim(), arr);
//			}
//			for (String month : months) {
//				String date = "2022-" + month;
//				String da = date;
////                System.out.println(date);
////                String da = date.replace("01", "1").replace("02", "2").replace("03", "3").replace("04", "4").replace("05", "5")
////                        .replace("06", "6").replace("07", "7").replace("08", "8").replace("09", "9");
//
//				// Use a prepared statement with parameters
//				String sql = "SELECT Date, Precip, ET_Value, Tiles FROM Eto_DB_Tiles WHERE (" + whereClause
//						+ ") AND Date LIKE ?";
//				preparedStatement = connection.prepareStatement(sql);
//				preparedStatement.setString(1, geometry.toString());
//				preparedStatement.setString(2, "%" + da + "%");
//				resultSet = preparedStatement.executeQuery();
//				Double ET_Value = 0.0;
//				while (resultSet.next()) {
//					String Date = resultSet.getString("Date");
//					Double Precip = resultSet.getDouble("Precip");
//					ET_Value = resultSet.getDouble("ET_Value");
//					String Tiles = resultSet.getString("Tiles");
//
////					String[] array = { Date, Precip.toString(), ET_Value.toString(), Tiles };
////					arrayListOfArrays.add(array);
//
//				}
//				String[] arr = { da, ET_Value.toString() };
//				String key = geometry + da;
////				System.out.println(key);
////                System.out.println(arr);
//				hashMapOfArrays.put(key.trim(), arr);
//			}
//		} finally {
//			// Close database resources in a finally block
//			if (resultSet != null) {
//				resultSet.close();
//			}
//			if (preparedStatement != null) {
//				preparedStatement.close();
//			}
//			if (connection != null) {
//				connection.close();
//			}
//		}
//	}
}
