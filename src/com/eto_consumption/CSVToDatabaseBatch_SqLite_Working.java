package com.eto_consumption;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.opencsv.CSVReader;

public class CSVToDatabaseBatch_SqLite_Working {
	private static String TableName = null;

	public static void main(String[] args) throws Exception {
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/monte_vista_14_Zoom_16_Eto_Precip_2025-01-01_To_2025-03-31_3_May_c1.csv";
		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries_TILES/san_clemente_9_Zoom_1_Eto_Precip_2024-08-01_To_2025-05-31_3_May_c1.csv";

        TableName ="san_clemente";
        
        System.out.println(TableName);
        Connection connection = null;
        BufferedReader reader = null;
        CSVReader csvReader = null;
        String sqliteFilePath = "/home/shatam-100/Down/WaterView_Data/EtoDatabase.db"; // path to your SQLite database file

        try {
            // Load the SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");
            
            // Connect to the SQLite database
            connection = DriverManager.getConnection("jdbc:sqlite:" + sqliteFilePath);

            connection.setAutoCommit(false);

            reader = new BufferedReader(new FileReader(csvFilePath));
            csvReader = new CSVReader(reader);

            String[] nextLine;
//            String sql = "INSERT INTO Eto_DB_Tiles (Tiles, Date, Precip, ET_Value) VALUES (?, ?, ?, ?)";
            String sql = "INSERT INTO "+TableName+" (Tiles, Date, Precip, ET_Value, CREATED_DATE) VALUES (?, ?, ?, ?, ?)";

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
                String tiles = nextLine[1];
                String date = nextLine[2];
                String perc = nextLine[3];
                double et = 0.0;
                try {
                	et = Double.parseDouble(nextLine[4]); // Parse 'et' as a double
				} catch (Exception e) {
					System.out.println(nextLine[4]+"");
					
					// TODO: handle exception
				}
//                String polygonString = "POLYGON ((-122.080078125 37.35269280367274, -122.10205078125 37.35269280367274, -122.10205078125 37.33522435930638, -122.080078125 37.33522435930638, -122.080078125 37.35269280367274))";

                String polygonString = tiles;
                // Remove "POLYGON ((" and "))" from the string
                String cleanPolygonString = polygonString.substring(10, polygonString.length() - 2);

                // Split the coordinates by comma and space
//                String[] coordinates = cleanPolygonString.split(", ");

                // Extract x, y, x1, y1 coordinates
//                double x = Double.parseDouble(coordinates[0].split(" ")[0]);
//                double y = Double.parseDouble(coordinates[0].split(" ")[1]);
//                double x1 = Double.parseDouble(coordinates[2].split(" ")[0]);
//                double y1 = Double.parseDouble(coordinates[2].split(" ")[1]);

                // Form key using coordinates
//                String tiles_key = String.format("%.6f_%.6f_%.6f_%.6f", x, y, x1, y1);
//                System.out.println("Key: " + tiles_key);

                preparedStatement.setString(1, polygonString);
                preparedStatement.setString(2, date);
                preparedStatement.setString(3, perc);
                preparedStatement.setDouble(4, et);
                preparedStatement.setString(5, formattedDateTime);

                preparedStatement.addBatch();
                currentBatchSize++;
                
//                System.out.println(currentBatchSize);
                
                // Execute the batch when it reaches the specified batch size
                if (currentBatchSize % batchSize == 0) {
                	System.out.println(currentBatchSize);
                    preparedStatement.executeBatch();
                    connection.commit();
                    currentBatchSize = 0;
                }
                Count++;
            }
            System.out.println("Total Count : "+Count);
            // Execute any remaining batched statements
            preparedStatement.executeBatch();
            connection.commit();

            System.out.println("Data imported successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close resources in a finally block
            if (csvReader != null) {
                csvReader.close();
            }
            if (reader != null) {
                reader.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }
}
