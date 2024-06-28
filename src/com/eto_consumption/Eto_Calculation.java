package com.eto_consumption;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.locationtech.jts.geom.Coordinate;

import com.opencsv.CSVWriter;
import com.shatam.utils.U;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class Eto_Calculation {
    static ArrayList<String> dataList = new ArrayList<>();
    static ArrayList<Geometry> tiles = new ArrayList<Geometry>();

	static String txtFile = "/home/shatam-100/Down/WaterView_Data/Tiles/Califonia_14_Zoom.txt";
//	static String txtFile = "/home/shatam-100/Down/WaterView_Data/Tiles/Califonia_13_zoom.txt";
//	static String txtFile = "/home/shatam-100/Down/WaterView_Data/Tiles/Califonia_12_zoom.txt";
//    static String txtFile = "/home/shatam-100/Down/WaterView_Data/Tiles/Califonia_11_zoom.txt";
//    static String txtFile = "/home/shatam-100/Down/WaterView_Data/Tiles/Califonia_10_zoom.txt";
//    static String txtFile = "/home/shatam-100/Down/WaterView_Data/Tiles/Califonia_9_zoom.txt";
//    static String txtFile = "/home/shatam-100/Down/WaterView_Data/Tiles/Califonia_8_zoom.txt";
    static String zoom = "14";

    private static String readGeoJSONFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        byte[] bytes = Files.readAllBytes(path);
        return new String(bytes);
    }

	public static void main(String[] arg) throws SQLException, ParseException, IOException {

//		System.out.println(allcsv);
//        String outputDirectory = "/home/shatam-100/Down/WaterView_Data/FTP_DATA/18_March_WD/Data_Folder_2024-03-17/WVWESTERN";
//        File outputDir = new File(outputDirectory);
//        outputDir.mkdirs();
//		File[] csvFilePaths = new File(allcsv).listFiles((dir, name) -> name.toLowerCase().endsWith(".geojson"));
//		if (csvFilePaths != null) {
//			for (File csvFilePath : csvFilePaths) {
//				try {
//					System.out.println(csvFilePath);
//					CreatTilesofBound(csvFilePath.toString());
//
//				} catch (Exception e) {
//				}
//			}
//		}
		
//		CreatTilesofBound("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries_geojson/vallejo_.geojson");
		CreatTilesofBound("/home/shatam-100/Cache/1_roseville.geojson");


	}


	static void getEtoForPreTile(String preTile) throws IOException, SQLException {

		// TODO Auto-generated method stub
		String outputCsv = "/home/shatam-100/Down/WaterView_Data/outputCsv.csv";
		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/SAN_JOSE_ETo_DB_Tiles.csv";
		String jdbcUrl = "jdbc:mysql://localhost:3306/eto"; // Update with your database URL
		String dbUser = "root"; // Update with your database username
		String dbPassword = "root"; // Update with your database password

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		WKTReader wktReader = new WKTReader();
		
		CSVWriter writer = new CSVWriter(new FileWriter(outputCsv));
//		try {
			connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword);
			String sql = "SELECT * FROM Eto_DB_Tiles WHERE Tiles =\"" + preTile + "\";";
			System.out.println(sql);
			preparedStatement = connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery(); // Execute the query
			
			writer.writeNext(new String[] { "Date" , "Precip", "ET_Value" ,"Tiles"});

			while (resultSet.next()) {
				String Date = resultSet.getString("Date");
				Double Precip = resultSet.getDouble("Precip");
				Double ET_Value = resultSet.getDouble("ET_Value");
				String Tiles = resultSet.getString("Tiles");
//				System.out.println("Date: " + Date + " Precip: " + Precip + " ET_Value: " + ET_Value + " Tiles: " + Tiles);
				writer.writeNext(new String[] { Date , Precip.toString() , ET_Value.toString() ,Tiles});

			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}

	}

	static String getTileForPremise(ArrayList<Geometry> tile, String prem) throws ParseException {
		// TODO Auto-generated method stub
		String myPreTile = null;
//      System.out.println(tile);
//      System.out.println(prem);
		WKTReader wktReader = new WKTReader();

		Geometry premise = wktReader.read(prem);
		for (Geometry g : tile) {
//System.out.println(g);
			if (g.intersects(premise)) {
				myPreTile = g.toString();
		        dataList.add(g+"");
//			System.out.println(g);
			} else {

			}
		}
		return myPreTile;
	}

	static void getAllUniqueTile(ArrayList<Geometry> til) throws SQLException, ParseException {
		// TODO Auto-generated method stub
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/SAN_JOSE_ETo_DB_Tiles.csv";
		String jdbcUrl = "jdbc:mysql://localhost:3306/eto";
		String dbUser = "root";
		String dbPassword = "root";

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		WKTReader wktReader = new WKTReader();
		try {
			connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword);
			String sql = "SELECT DISTINCT Tiles FROM Eto_DB_Tiles;";
			preparedStatement = connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery(); // Execute the query

			// You can now process the results in the 'resultSet'
			while (resultSet.next()) {
				String tile = resultSet.getString("Tiles");
//                System.out.println("" + tile);
				til.add(wktReader.read(tile.trim()));
			}
//			System.out.println(til.size());
		} catch (SQLException e) {
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
	private static void CreatTilesofBound(String filePath) throws ParseException, IOException {
		String premise = readGeoJSONFile(filePath);

//		getAllUniqueTile(tiles);
//		String txtFile = "/home/shatam-100/Downloads/Thu Apr 25 2024 11_27_34 GMT+0530 (India Standard Time)Tiles_List.txt";
//		String txtFile = "/home/shatam-100/Down/WaterView_Data/All_Califonia_Tiles.txt";

//		String txtFile = "/home/shatam-100/Downloads/Downloads_WV/Tue Jun 11 2024 14_55_24 GMT+0530 (India Standard Time)Tiles_List.txt";

//		String txtFile = "/home/shatam-100/Down/WaterView_Data/Tiles/Califonia_13_Zoom.txt";
//		String txtFile = "/home/shatam-100/Down/WaterView_Data/Tiles/Califonia_12_Zoom.txt";
//		String txtFile = "/home/shatam-100/Downloads/Tue Apr 02 2024 09_52_48 GMT+0530 (India Standard Time)Tiles_List.txt";
//		String txtFile = "/home/shatam-100/Down/WaterView_Data/Tiles/san_jose_all_meters_zoom_13.txt";
//		String txtFile = "/home/shatam-100/Down/WaterView_Data/Tiles/Thu Mar 21 2024 13_01_23 GMT+0530 (India Standard Time)Tiles_List.txt";
//		String txtFile = "/home/shatam-100/Cache/Mon Mar 11 2024 13_01_45 GMT+0530 (India Standard Time)Tiles_List.txt";
		
		

		

//		POLYGON ((-117.70751953125 34.05265942137598, -117.7294921875 34.05265942137598, -117.7294921875 34.034452609676435, -117.70751953125 34.034452609676435, -117.70751953125 34.05265942137598))
		GeometryFactory gf = new GeometryFactory();

		try (BufferedReader reader = new BufferedReader(new FileReader(txtFile))) {
		    String line;
		    WKTReader wktReader = new WKTReader();

		    while ((line = reader.readLine()) != null) {
//		    	U.log(line.trim());
		        tiles.add(wktReader.read(line.trim().replaceAll(",POLYGON", "POLYGON").replaceAll(",POINT", "POINT")));
		    }
//		    return;
		} catch (IOException | ParseException e) {
		    // Handle exceptions
		}
		for (Geometry geometry : tiles) {
//			U.log(geometry);
		}


		U.log(tiles.size());
		String PreTile = getTileForPremise(tiles, premise);
//		System.out.println("PreTile :: "+PreTile);

//		getEtoForPreTile(PreTile);
		
		// Text file path

        // Write data to text file using a for loop
        try {
            System.out.println("dataList :: "+dataList.size());

        	 // Create a set to store unique data
            Set<String> uniqueData = new HashSet<>(dataList);
            
            // Convert set back to list if needed
            List<String> uniqueList = new ArrayList<>(uniqueData);
			filePath = filePath.replace("DistrictBoundaries_geojson", "DistrictBoundaries_TILES")
            		.replace(".geojson", zoom+"_Zoom_"+uniqueList.size()+".csv");
            
            FileWriter writerFile = new FileWriter(filePath);
;
            // Write data rows
//            for (String data : uniqueList) {
//                writer.append(data);
//                writer.append("\n");
//            }
            try (CSVWriter writer = new CSVWriter(writerFile)) {
            	writer.writeNext(new String[]{"Tiles"});
                for (String data : uniqueList) {
                    // Writing each data item to a separate row
                    writer.writeNext(new String[]{data});
                }
                System.out.println("Data has been written to CSV file successfully!");
            } catch (IOException e) {
                e.printStackTrace();
            }


            System.out.println("uniqueList :: "+uniqueList.size());
//            writer.flush();
//            writer.close();

            System.out.println("Data has been written to " + filePath + " successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

}
