package com.saurabh.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import com.opencsv.CSVReader;

public class CSVToDatabaseBatch_SqLite_test {
	
	static String csvFilePath = "/home/shatam-100/Down/WaterView_Data/DB_SQLITE/";

//	static String[] table = csvFilePath.split("/");
//    System.out.println(table[table.length-1]);
//	private static String TableName = table[table.length - 1].replace(".csv", "");
	private static String TableName ="";
//	static Map<String, String> mapKey = new HashMap<>();
	static String sqliteFilePath = "/home/shatam-100/Down/WaterView_Data/DB_SQLITE/eto.db"; // path to your SQLite
																							// database file
	static String url = "jdbc:sqlite:" + sqliteFilePath;

	public static void main(String[] args) throws Exception {
        File folder = new File(csvFilePath);

        // Check if the path exists and is a directory
        if (folder.exists() && folder.isDirectory()) {
            // List files in the directory
            File[] files = folder.listFiles();
            if (files != null) {
                System.out.println("Files in folder " + csvFilePath + ":");
                int i = 0;
                for (File file : files) {
//                    if(i== 10) {
                        System.out.println(i+" "+file.getName());

                        TableName = file.getName().replace(".csv", "");
                        if(!file.getName().contains(".csv"))continue;
                        mainCall(TableName);
                       
//                    }
                    i++;
                }
            } else {
                System.out.println("No files found in folder " + csvFilePath);
            }
        } else {
            System.out.println("Folder " + csvFilePath + " does not exist or is not a directory");
        }

		
	}

	private static void mainCall(String TableName) throws Exception {
		// TODO Auto-generated method stub

		System.out.println(TableName);
		CreateTable(TableName);
//		vacuumDatabase(sqliteFilePath);
        insert_into();
	}

	private static void insert_into() throws Exception {
		// TODO Auto-generated method stub
		Map<String, String> mapKey = new HashMap<>();
		Map<String, Integer> keyValueMap = new HashMap<>();
		int keyCounter = 1;

		Connection connection = null;
		BufferedReader reader = null;
		CSVReader csvReader = null;

		// Load the SQLite JDBC driver
		Class.forName("org.sqlite.JDBC");

		// Connect to the SQLite database
		connection = DriverManager.getConnection("jdbc:sqlite:" + sqliteFilePath);

		connection.setAutoCommit(false);

		reader = new BufferedReader(new FileReader(csvFilePath+TableName+".csv"));
		csvReader = new CSVReader(reader);

		String[] nextLine;
//        String sql = "INSERT INTO Eto_DB_Tiles (Tiles, Date, Precip, ET_Value) VALUES (?, ?, ?, ?)";
		String sql = "INSERT INTO " + TableName
				+ " (Tiles, Date, Precip, ET_Value, CREATED_DATE) VALUES (?, ?, ?, ?, ?)";

		PreparedStatement preparedStatement = connection.prepareStatement(sql);

		// Skip the CSV header line
		csvReader.readNext();
		// Get the current date and time
		LocalDateTime currentDateTime = LocalDateTime.now();

		// Format the date and time (optional)
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String formattedDateTime = currentDateTime.format(formatter);

		int batchSize = 20000; // Set a batch size for optimizing the insert operation
		int currentBatchSize = 0;
		int Count = 0;

		while ((nextLine = csvReader.readNext()) != null) {
			String tiles = nextLine[2];
			String date = nextLine[1];
			String perc = nextLine[3];
			double et = 0.0;
			try {
				et = Double.parseDouble(nextLine[4]); // Parse 'et' as a double
			} catch (Exception e) {
				System.out.println(nextLine[4] + "");

				// TODO: handle exception
			}
//            String polygonString = "POLYGON ((-122.080078125 37.35269280367274, -122.10205078125 37.35269280367274, -122.10205078125 37.33522435930638, -122.080078125 37.33522435930638, -122.080078125 37.35269280367274))";

			String polygonString = tiles;
			// Remove "POLYGON ((" and "))" from the string
			String cleanPolygonString = polygonString.substring(10, polygonString.length() - 2);

			// Split the coordinates by comma and space
			String[] coordinates = cleanPolygonString.split(", ");

			// Extract x, y, x1, y1 coordinates
			double x = Double.parseDouble(coordinates[0].split(" ")[0]);
			double y = Double.parseDouble(coordinates[0].split(" ")[1]);
			double x1 = Double.parseDouble(coordinates[2].split(" ")[0]);
			double y1 = Double.parseDouble(coordinates[2].split(" ")[1]);

			// Form key using coordinates
//			String tiles_key = String.format("%.6f_%.6f_%.6f_%.6f", x, y, x1, y1);
			String tiles_key1 = x + "_" + y + "_" + x1 + "_" + y1;
			String tiles_key = TableName+"_"+keyCounter;
			
		    // If the value is already in the map, assign the same key, otherwise assign a new key
		    if (keyValueMap.containsKey(tiles)) {
		        // If the value is already in the map, get its key
		        int key = keyValueMap.get(tiles);
//		        System.out.println(keyCounter);
		        keyValueMap.put(tiles, key);
		    } else {
		        // If the value is not in the map, assign a new key and increment the counter
//		    	System.out.println(keyCounter);
		        keyValueMap.put(tiles, keyCounter);
		        keyCounter++;
		    }

		    
//            System.out.println("Key: " + tiles_key);
			mapKey.put(tiles_key1, tiles);
			preparedStatement.setString(1, tiles_key1);
			preparedStatement.setString(2, date);
			preparedStatement.setString(3, perc);
			preparedStatement.setDouble(4, et);
			preparedStatement.setString(5, formattedDateTime);

			preparedStatement.addBatch();
			currentBatchSize++;

//            System.out.println(currentBatchSize);

			// Execute the batch when it reaches the specified batch size
			if (currentBatchSize % batchSize == 0) {
				System.out.println(currentBatchSize);
				preparedStatement.executeBatch();
				connection.commit();
				currentBatchSize = 0;
			}
			Count++;
		}
		System.out.println("Total Count : " + Count);
		// Execute any remaining batched statements
		preparedStatement.executeBatch();
		connection.commit();
//		System.out.println(mapKey);
		insertDataBatch(mapKey);
		System.out.println("Data imported successfully!");

	
	}

	private static void vacuumDatabase(String sqliteFilePath) {
		// TODO Auto-generated method stub
		String url = "jdbc:sqlite:" + sqliteFilePath;

		try (Connection conn = DriverManager.getConnection(url); Statement stmt = conn.createStatement()) {
			// Execute VACUUM command
			stmt.executeUpdate("VACUUM;");
			System.out.println("Database vacuumed successfully.");
		} catch (SQLException e) {
			System.out.println("Error vacuuming database: " + e.getMessage());
		}

	}

	private static void CreateTable(String tableName) {
		// TODO Auto-generated method stub
//        String tableName = "camrosa";
		String createStatement = "CREATE TABLE IF NOT EXISTS " + tableName + " (\n" + "    SrNo INTEGER,\n"
				+ "    Date DATE,\n" + "    Tiles VARCHAR(255),\n" + "    Precip INT,\n" + "    ET_Value INT,\n"
				+ "    CREATED_DATE DATETIME,\n" + "    UPDATED_DATE DATETIME,\n" + "    Tile_ID INT\n" + ");";

		// Call the createTable method with table name and create statement
//        createTable(tableName, createStatement);
		try (Connection conn = DriverManager.getConnection(url); Statement stmt = conn.createStatement()) {
			// Execute SQL statement to create table
			stmt.execute(createStatement);
			System.out.println("Table '" + tableName + "' created successfully.");
		} catch (SQLException e) {
			System.out.println("Error creating table: " + e.getMessage());
		}

	}
    public static void insertDataBatch(Map<String, String> map) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + sqliteFilePath)) {
            conn.setAutoCommit(false); // Disable auto-commit

            String sql = "INSERT INTO key_table (Pkey, Tiles, CREATED_DATE) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                LocalDateTime currentDateTime = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDateTime = currentDateTime.format(formatter);

                for (Map.Entry<String, String> entry : map.entrySet()) {
                    pstmt.setString(1, entry.getKey()); // Set key
                    pstmt.setString(2, entry.getValue()); // Set value
                    pstmt.setString(3, formattedDateTime); // Set timestamp
                    pstmt.addBatch(); // Add the prepared statement to the batch
                }

                int[] affectedRows = pstmt.executeBatch(); // Execute batch of statements

                conn.commit(); // Commit transaction
                System.out.println("Inserted " + affectedRows.length + " keys successfully.");
            } catch (SQLException e) {
                conn.rollback(); // Rollback transaction if an error occurs
                System.out.println("Transaction rolled back due to error: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Error connecting to database: " + e.getMessage());
        }
    }

	public static void insertData(Map<String, String> map) {
//    	System.out.println("insert Data");
		try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + sqliteFilePath)) {
			// SQL query to insert data
			System.out.println("SQL query to insert key value");
			String sql = "INSERT INTO key_table (Pkey, Tiles, CREATED_DATE) VALUES (?, ?, ?)";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			LocalDateTime currentDateTime = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			String formattedDateTime = currentDateTime.format(formatter);

			// Iterate through the map and insert each key-value pair
			for (Map.Entry<String, String> entry : map.entrySet()) {
				pstmt.setString(1, entry.getKey()); // Set key
				pstmt.setString(2, entry.getValue()); // Set value
				pstmt.setString(3, formattedDateTime); // Set value
				pstmt.executeUpdate();
			}
			System.out.println("Key inserted successfully.");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

}
