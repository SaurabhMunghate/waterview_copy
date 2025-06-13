package com.eto_consumption;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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

//import org.geotools.data.shapefile.index.quadtree.fs.FileSystemIndexStore;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.opencsv.CSVWriter;
import com.shatam.utils.CSVToJSon;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

//import tech.units.indriya.AbstractSystemOfUnits;

public class get_cii_consumption_allocation_Working {
	static ArrayList<Geometry> tiles = new ArrayList<Geometry>();
	static Map<String, Double> allETO = new HashMap<>();
	private static final JSONParser parser = new JSONParser();
//	private static final String premiseFile = "/home/shatam-100/Down/WaterView_Data/meter_locations_res_montevista.json";
//	static List<String[]> arrayListOfArrays = new ArrayList<>();
	static Map<String, String[]> hashMapOfArrays = new HashMap<>();
	static Map<String, String> etonotfoun = new HashMap<>();
	static ArrayList<String[]> consmData = new ArrayList<>();
	static Map<String, String[]> PB = new HashMap<>();
	static Map<String, String[]> ML = new HashMap<>();
	
	private static String tableName = "";
//	private static String FolderName = "/home/shatam-100/Down/WaterView_Data/FTP_DATA/Data_Folder_2024-10-27/JURUPACOMMUN177"+"/";
	private static String FolderName = "/home/shatam-100/Down/WaterView_Data/FTP_DATA/June/WVLOMALINDA"+"/";
	private static String premise_bounds = FolderName+"prd.premise_bounds.json";
	private static String meter_locations = FolderName+"prd.meter_locations_grid.json";
	private static String filePath = FolderName+"cii_consumption_allocation.csv";

	private static String consumctionFile = FolderName+"MonthlyConsumption.csv";
	
	
	public static void main(String[] arg) throws Exception {
		U.getMapValue( premise_bounds, meter_locations,PB,ML);
		System.out.println(PB.size());
		System.out.println(ML.size());
		
//		String meterID = "5927";
		
//		String[] PB_Data = PB.get(meterID);//PB.put(meterID, new String[]{premID,row,col});
//        String premID = PB_Data[0];
//        String row = PB_Data[1];
//		String col = PB_Data[2];
//		System.out.println("meterID: "+meterID+" | row: "+row+" | col: "+col);
//		
//		String[] I_I_SLA = ML.get(premID);//ML.put(premI_D, new String[]{I,I_SLA});
//		String I_SLA = I_I_SLA[1];
//		String I = I_I_SLA[0];
//		System.out.println("premID: "+premID+" | I: "+I+" | I_SLA: "+I_SLA);
//		
//        String avgETo = null;
//		String avgPrecp = null;
//		double allocation = 0.0;
        CSVWriter writer = new CSVWriter(new FileWriter(filePath));
//        String[] header = {"MeterID", "Date", "Consumption", "PremID", "Row", "Col",
//                "Grid_Row", "Grid_Col", "I", "I_SLA", "AvgETo", "AvgPrecp"};
        String[] header = {"MeterID", "Date", "Consumption", "PremID", "Row", "Col",
                "Grid_Row", "Grid_Col", "I", "I_SLA"};

        writer.writeNext(header);

        ArrayList<String[]> als = U.readConsumption(consumctionFile);

		int i = 0;
		for (String[] data : als) {
			if(data[1].contains("year-month"))continue;
			
			String date = data[1].replace("_", "-");
			String meterID = data[0];
			String Consumption = data[2];
//			System.out.println("meterID: "+meterID+" | date: "+date+" | Consumption: "+Consumption);
			
//			String meterID = "5927";
			
			String[] PB_Data  = null;
			PB_Data = PB.get(meterID.trim());//PB.put(meterID, new String[]{premID,row,col});
			if(PB_Data==null)continue;
	        String premID = PB_Data[0];
	        String row = PB_Data[1];
			String col = PB_Data[2];
			System.out.println("meterID: "+meterID+" | row: "+row+" | col: "+col);
			
	        String grid_row = PB_Data[3];
			String grid_col = PB_Data[4];
			System.out.println("grid_row: "+grid_row+" | grid_col: "+grid_col);

			String[] I_I_SLA = ML.get(premID);//ML.put(premI_D, new String[]{I,I_SLA});
			String I_SLA = I_I_SLA[1];
			String I = I_I_SLA[0];
			System.out.println("premID: "+premID+" | I: "+I+" | I_SLA: "+I_SLA);
			
			System.out.println("date: "+date+" | Consumption: "+Consumption);
			
	        String avgETo = null; //From database
			String avgPrecp = null; //From database
//			double allocation = 0.0;
			
            String[] rowData = {meterID, date, Consumption, premID, row, col,
            		grid_row, grid_col, I, I_SLA};
            writer.writeNext(rowData);

		}
		writer.close();
        System.out.println("CSV file created successfully.");

		//		PB.get(meterID);
//		ML.get(premID);
//		@SuppressWarnings("unused")
//		double allocation =Math.abs((0.62)*((Double.parseDouble(I)*0.8*(Double.parseDouble(avgETo)-Double.parseDouble(avgPrecp)))+(1.0*Double.parseDouble(I_SLA)*Double.parseDouble(avgETo))+(1*0.45*Double.parseDouble(avgETo)))+0);

		

	}

	@SuppressWarnings("unchecked")
	public static void main1(String[] arg) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		ArrayNode jsonArray = objectMapper.createArrayNode();

		
		Map<String, String> hashMapPidPoint = new HashMap<>();
		File convertedData = new File(premise_bounds);

		JsonNode jsonNode = objectMapper.readTree(convertedData);
		if (jsonNode.isArray()) {
			for (JsonNode objNode : jsonNode) {
				String premID = objNode.has("premID") ? objNode.get("premID").asText() : objNode.get("PremID").asText();
				String theGeom = objNode.has("the_geom") ? objNode.get("the_geom").asText() : objNode.get("geometry").asText();
				hashMapPidPoint.put(premID, theGeom);
			}
		}

		
//		U.csvToJSON(premise_bounds);
//		U.csvToJSON(meter_locations);
		String[] st = FolderName.split("/");
		String Waterdistrict_ID = st[st.length-1];
//		System.out.println("Waterdistrict_ID : "+ Waterdistrict_ID); 
		String WD_Name = getTableName(Waterdistrict_ID);
		tableName = WD_Name.toLowerCase().trim();

		
		long begin = System.currentTimeMillis();
//		String outputjsonFile = FolderName+tableName+"_cii_consumption_eto.json";
//		String outputCSVFile = FolderName+tableName+"_cii_consumption_eto.csv";
		String outputjsonFile = FolderName+"cii_consumption_eto.json";
		String outputCSVFile = FolderName+"cii_consumption_eto.csv";

		FileWriter writer = new FileWriter(outputCSVFile);
		CSVWriter csvWriter = new CSVWriter(writer);


		
		// get all sql data in hashmap
		System.out.println("Added all sql data in hashmap");
		run();
	    JosnFileMerge();

		
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


//		FileReader fileReader = new FileReader(premiseFile);
//		JSONArray premis;
//		premis = (JSONArray) parser.parse(fileReader);
		ArrayList<String[]> als = null;
//		ArrayList<String[]> alss = null;
		als = readConsumption(consumctionFile);

//		System.out.println(alss.size());

		WKTReader wktReader = new WKTReader();
		Geometry premise;

//		System.out.println("Hash Map Creation Completed");
//		String[] header = { "Date", "premid", "PreTile", "Eto", "Tile" };
//		String[] header = { "Date", "meterID", "premID", "PreTile", "Eto","Precip", "Tile", "Allocation", "Consumption" };
		String[] header = { "Date", "meterID", "premID", "Allocation", "Consumption" };
//		 date, Pid, premID, theGeom, avgETo,avgPrecp, tile,allocation+"" ,Consumption
		csvWriter.writeNext(header);
		int i = 0;
		for (String[] data : als) {
			//consumption File MeterID	ReadingDate
//			String date = data[2];
//			String meterID = data[0];
//			String Consumption = data[1];
			
			String date = data[1].replace("_", "-");
			String meterID = data[0];
			String Consumption = data[2];
			
			String ID = PB.get(meterID);
			if (meterID.contains("MeterID"))
				continue;
			if (meterID.contains("meter_uid"))
				continue;
			
//			if(!ID.contains("Mnte122")&&!ID.contains("Mnte120"))continue;
//			if(!ID.contains("Mnte122"))continue;
			if(ID==null)continue;
//			if(!ID.contains("5007097"))continue;
			
//			System.out.println("Date "+date+ " meterID "+ meterID+" Consumption "+Consumption);
//			String Pid = data[1];
//			String date = data[2];
			String formattedDate = "";
			try {
		        String originalDateString = data[2];
		        formattedDate = LocalDate.parse(originalDateString, DateTimeFormatter.ofPattern("M/d/yyyy")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			} catch (Exception e) {
				// TODO: handle exception
			}
//	        System.out.println(formattedDate);

//			String date = formattedDate;


//			System.out.println(i);
//			System.out.println(Pid);
//			System.out.println(date);
//			System.out.println(ID);
			
			String theGeom = "";
			String the_geom = "";
			try {
//				theGeom = hashMapPidPoint.get(meterID).toString(); // Function to get PreTile from meter_locations_res_convertedData file
				the_geom = hashMapPidPoint.get(ID).toString();
				Geometry point = wktReader.read(the_geom);
				Geometry theGeomGeometry = point.getCentroid();
				theGeom = theGeomGeometry+"";
//				System.out.println("theGeom : "+theGeom);
			} catch (Exception e) {
				System.out.println(meterID+"   ||   "+date);
//				System.out.print(date);
				continue;
//				return;
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
//						System.out.println(premise);
//						System.out.println(g);
						k++;
					}
				}

//			} catch (Exception e) {
//				// TODO: handle exception
//			}
			Double ETo = 0.0;

			String avgETo = "";
			String avgPrecp = "";

			String key = myPreTile + date;
//			String key = myPreTile;
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
				avgPrecp = gethashvalue1[2];
			} catch (Exception e) {
				etonotfoun.put(key, key);
//				U.log(date);
//				System.out.println(key);
				avgETo = "0.0";
				// TODO: handle exception
			}
	        // Iterate over the entries and print them
	        for (Map.Entry<String, String> entry : etonotfoun.entrySet()) {
//	            System.out.println("Key: " + entry.getKey());
	        }
			
//			String dataArr[3] = "";//I
//			String dataArr[2] = "";//I_SLA
//			3 I
//			2 I_SLA
//			PB.get(premID);
//			premID
			String premID = PB.get(meterID);
			String ISLA[] = ML.get(premID);
//			ML.put(premI_D, new String[]{I,I_SLA});
			String I = ISLA[0];
			String I_SLA = ISLA[1];
//			System.out.println("premID "+premID+" I :"+I+" I_SLA :"+I_SLA);
//	        System.out.println("Eto "+ avgETo+" PEFF "+ avgPrecp);
//	        avgPrecp = "0";
			double allocation = 0.0;
			try {
				allocation=Math.abs((0.62)*((Double.parseDouble(I)*0.8*(Double.parseDouble(avgETo)-Double.parseDouble(avgPrecp)))+(1.0*Double.parseDouble(I_SLA)*Double.parseDouble(avgETo))+(1*0.45*Double.parseDouble(avgETo)))+0);
			} catch (Exception e) {
				avgPrecp = "0";
				allocation=Math.abs((0.62)*((Double.parseDouble(I)*0.8*(Double.parseDouble(avgETo)-Double.parseDouble(avgPrecp)))+(1.0*Double.parseDouble(I_SLA)*Double.parseDouble(avgETo))+(1*0.45*Double.parseDouble(avgETo)))+0);
//				e.getStackTrace();
//				// TODO: handle exception
			}
//			System.out.println("allocation1 :: "+allocation);
			//One CCF is equivalent to 748 gallons
	        allocation = allocation/748.052;
//			double allocation = (0.62) * ((Double.parseDouble(I) * 0.8 * (avgEto - avgprecip))
//					+ (1.0 * Double.parseDouble(I_SLA) * avgEto));
	        if(allocation==0.0)System.out.println("avgETo :: "+avgETo+"the_geom :: "+the_geom);

//			System.out.println("allocation2 :: "+allocation);
//			System.out.println("the_geom :: "+the_geom);
			ObjectNode jsonObject = objectMapper.createObjectNode();
			jsonObject.put("Date", date);
			jsonObject.put("MeterID", meterID);
			jsonObject.put("premID", premID);
//			jsonObject.put("PreTile", the_geom);
//			jsonObject.put("Eto", avgETo);
//			jsonObject.put("PEFF", avgPrecp);
//			jsonObject.put("Tile", tile);
			jsonObject.put("allocation", allocation+"");
			jsonObject.put("Consumption", Consumption);
			jsonArray.add(jsonObject);
//			System.out.println(avgETo);
//			String[] row = { date, meterID, premID, the_geom, avgETo,avgPrecp, tile,allocation+"" ,Consumption};
			String[] row = { date, meterID, premID, allocation+"" ,Consumption};

//			 "Date", "meterID", "premID", "PreTile", "Eto","Precip", "Tile", "Allocation", "Consumption" 
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
		csvWriter.close();
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
	private static void JosnFileMerge() throws IOException {
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
				String premID = objNode.has("premID") ? objNode.get("premID").asText() : objNode.get("PremID").asText();
				String meterID = objNode.has("MeterID") ? objNode.get("MeterID").asText() : objNode.get("meterID").asText();
				PB.put(meterID, premID);
				
			}
		}
		
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

			String sql = "SELECT DISTINCT Tiles FROM "+tableName+";";
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
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/SAN_JOSE_jurupa.csv";
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
//			String sql = "SELECT DISTINCT Tiles FROM jurupa;";
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
				String date = "2024-" + month;
				String da = date;

				String sql = "SELECT Date, Precip, ET_Value, Tiles FROM "+tableName+" WHERE (" + whereClause
						+ ") AND Date LIKE ?";
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setString(1, geometry.toString());
				preparedStatement.setString(2, "%" + da + "%");
				resultSet = preparedStatement.executeQuery();
				Double ET_Value = 0.0000;
				Double avgeto = 0.0000;
				Double avgprep = 0.0000;
				int i = 0;
				while (resultSet.next()) {
					String Date = resultSet.getString("Date");
					Double Precip = resultSet.getDouble("Precip");
					ET_Value = resultSet.getDouble("ET_Value");
					String Tiles = resultSet.getString("Tiles");

//                    String[] array = { Date, Precip.toString(), ET_Value.toString(), Tiles };
//                    arrayListOfArrays.add(array);
					avgeto += ET_Value;
					avgprep+= Precip;
//					System.out.println(avgeto +"   |   "+ ET_Value);
//					System.out.println(i +"   |   "+ ET_Value);

					i++;
				}
//				System.out.println("System.out.println :: "+i);
//				System.out.println(" avgeto ::: "+avgeto/i + " | "+geometry.toString()+" | "+da);
//				if((avgprep)+""== "NaN") {
//					System.out.println("NNNNNAAAAAANNNNN");
//				}
				String[] arr = { da, avgeto + "" , avgprep+""};
				String key = geometry + date;
//				System.out.println( da+ " | "+ avgeto + "  | " + avgprep+"");
//				System.out.println(key);
//				System.out.println(arr[0]+"  |  "+arr[1]);

				hashMapOfArrays.put(key.trim(), arr);
			}
			for (String month : months) {
				String date = "2023-" + month;
				String da = date;

				String sql = "SELECT Date, Precip, ET_Value, Tiles FROM "+tableName+" WHERE (" + whereClause
						+ ") AND Date LIKE ?";
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setString(1, geometry.toString());
				preparedStatement.setString(2, "%" + da + "%");
				resultSet = preparedStatement.executeQuery();
				Double ET_Value = 0.0000;
				Double avgeto = 0.0000;
				Double avgprep = 0.0000;
				int i = 0;
				while (resultSet.next()) {
					String Date = resultSet.getString("Date");
					Double Precip = resultSet.getDouble("Precip");
					ET_Value = resultSet.getDouble("ET_Value");
					String Tiles = resultSet.getString("Tiles");

//                    String[] array = { Date, Precip.toString(), ET_Value.toString(), Tiles };
//                    arrayListOfArrays.add(array);
					avgeto += ET_Value;
					avgprep+= Precip;
//					System.out.println(avgeto +"   |   "+ ET_Value);
//					System.out.println(i +"   |   "+ ET_Value);

					i++;
				}
//				System.out.println("System.out.println :: "+i);
//				System.out.println(" avgeto ::: "+avgeto/i + " | "+geometry.toString()+" | "+da);
//				if((avgprep)+""== "NaN") {
//					System.out.println("NNNNNAAAAAANNNNN");
//				}
				String[] arr = { da, avgeto + "" , avgprep+""};
				String key = geometry + date;
//				System.out.println( da+ " | "+ avgeto + "  | " + avgprep+"");
//				System.out.println(key);
//				System.out.println(arr[0]+"  |  "+arr[1]);

				hashMapOfArrays.put(key.trim(), arr);
			}
			for (String month : months) {
				String date = "2022-" + month;
				String da = date;

				String sql = "SELECT Date, Precip, ET_Value, Tiles FROM "+tableName+" WHERE (" + whereClause
						+ ") AND Date LIKE ?";
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setString(1, geometry.toString());
				preparedStatement.setString(2, "%" + da + "%");
				resultSet = preparedStatement.executeQuery();
				Double ET_Value = 0.0;
				Double avgeto = 0.0;
				Double avgprep = 0.0000;

				int i = 0;
				while (resultSet.next()) {
					String Date = resultSet.getString("Date");
					Double Precip = resultSet.getDouble("Precip");
					ET_Value = resultSet.getDouble("ET_Value");
					String Tiles = resultSet.getString("Tiles");

//                    String[] array = { Date, Precip.toString(), ET_Value.toString(), Tiles };
//                    arrayListOfArrays.add(array);
					avgeto += ET_Value;
					avgprep+= Precip;
//					System.out.println(avgeto +"   |   "+ ET_Value);
					i++;
				}
//				System.out.println(" avgeto ::: "+avgeto/i+ "/n"+geometry.toString()+"/n"+da);
				String[] arr = { da, avgeto + "" ,avgprep+ ""};
				String key = geometry + date;
//				System.out.println(key);
//				System.out.println(arr[0]+"  |  "+arr[1]);
				hashMapOfArrays.put(key.trim(), arr);
			}
			for (String month : months) {
				String date = "2021-" + month;
				String da = date;

				String sql = "SELECT Date, Precip, ET_Value, Tiles FROM "+tableName+" WHERE (" + whereClause
						+ ") AND Date LIKE ?";
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setString(1, geometry.toString());
				preparedStatement.setString(2, "%" + da + "%");
				resultSet = preparedStatement.executeQuery();
				Double ET_Value = 0.0;
				Double avgeto = 0.0;
				Double avgprep=0.0;		
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
				String[] arr = { da, avgeto + "",avgprep +""};
				String key = geometry + date;
//				System.out.println(key);
//				System.out.println(arr[0]+"  |  "+arr[1]);
				hashMapOfArrays.put(key.trim(), arr);
			}
			for (String month : months) {
				String date = "2020-" + month;
				String da = date;

				String sql = "SELECT Date, Precip, ET_Value, Tiles FROM "+tableName+" WHERE (" + whereClause
						+ ") AND Date LIKE ?";
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setString(1, geometry.toString());
				preparedStatement.setString(2, "%" + da + "%");
				resultSet = preparedStatement.executeQuery();
				Double ET_Value = 0.0;
				Double avgeto = 0.0;
				Double avgprp = 0.0;

				int i = 0;
				while (resultSet.next()) {
					String Date = resultSet.getString("Date");
					Double Precip = resultSet.getDouble("Precip");
					ET_Value = resultSet.getDouble("ET_Value");
					String Tiles = resultSet.getString("Tiles");

//                    String[] array = { Date, Precip.toString(), ET_Value.toString(), Tiles };
//                    arrayListOfArrays.add(array);
					avgeto += ET_Value;
					avgprp+= Precip;
//					System.out.println(avgeto +"   |   "+ ET_Value);
					i++;
				}
//				System.out.println(" avgeto ::: "+avgeto/i+ "/n"+geometry.toString()+"/n"+da);
				String[] arr = { da, (avgeto ) + "", (avgprp)+"" };
				String key = geometry + date;
				//System.out.println(key);
				hashMapOfArrays.put(key.trim(), arr);
			}
			for (String month : months) {
				String date = "2019-" + month;
				String da = date;

				String sql = "SELECT Date, Precip, ET_Value, Tiles FROM "+tableName+" WHERE (" + whereClause
						+ ") AND Date LIKE ?";
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setString(1, geometry.toString());
				preparedStatement.setString(2, "%" + da + "%");
				resultSet = preparedStatement.executeQuery();
				Double ET_Value = 0.0;
				Double avgeto = 0.0;
				Double avgprp = 0.0;
				int i = 0;
				while (resultSet.next()) {
					String Date = resultSet.getString("Date");
					Double Precip = resultSet.getDouble("Precip");
					ET_Value = resultSet.getDouble("ET_Value");
					String Tiles = resultSet.getString("Tiles");

//                    String[] array = { Date, Precip.toString(), ET_Value.toString(), Tiles };
//                    arrayListOfArrays.add(array);
					avgeto += ET_Value;
					avgprp+= Precip;
//					System.out.println(avgeto +"   |   "+ ET_Value);
					i++;
				}
//				System.out.println(" avgeto ::: "+avgeto/i+ "/n"+geometry.toString()+"/n"+da);
				String[] arr = { da, (avgeto ) + "",(avgprp ) + "" };
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
//				String sql = "SELECT Date, Precip, ET_Value, Tiles FROM "+tableName+" WHERE (" + whereClause
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
//				String sql = "SELECT Date, Precip, ET_Value, Tiles FROM jurupa WHERE (" + whereClause
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
