package com.saurabh;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.opencsv.CSVWriter;
import com.shatam.utils.FileUtil;
import com.shatam.utils.U;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class califonia_Tiles_4_sep {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception, ParseException {
//		String filePath = "/home/shatam-100/ResidentialWaterView/4_Sep_Califonia.txt";
		String tilesfilePath = "/home/shatam-100/ResidentialWaterView/145_Tile_San_Jose.txt";
//		String csvFilePath = "/home/shatam-100/ResidentialWaterView/6_Sep_Califonia_san_jose.csv";
		String OutputcsvFilePath = "/home/shatam-100/ResidentialWaterView/Tiles_8_sep_14Zoom_2.53.csv";
//		String premiseFilePath = "/home/shatam-100/ResidentialWaterView/premise_bounds.json";
		String premiseFilePath = "/home/shatam-100/ResidentialWaterView/san_jose.txt";
		
		WKTReader wktReader = new WKTReader();
		GeometryFactory geometryFactory = new GeometryFactory();

		// Reading tiles
		Set<Geometry> tiles = new HashSet<>();
		BufferedReader br = new BufferedReader(new FileReader(tilesfilePath));
		String line;
		while ((line = br.readLine()) != null) {
			line = line.replace(",POLYGON", "POLYGON");
			tiles.add(wktReader.read(line.trim()));
		}
		br.close();

		CSVWriter writer = new CSVWriter(new FileWriter(OutputcsvFilePath));
		writer.writeNext(new String[] { "Meter_the_geom","ETO_Values", "Tiles_poly"});
		// Reading Premises
		JSONParser parser = new JSONParser();
		JSONArray premises = (JSONArray) parser.parse(new FileReader(premiseFilePath));
		
		Geometry premise = wktReader.read("POLYGON((-122.04689345773669 37.46783496848464,-121.88515021916837 37.4956846756986,-121.55708630089073 37.20335317393061,-121.62422501001865 37.10971291586404,-121.76307995323448 37.09754329670281,-122.06215220954626 37.28716600294641,-122.04689345773669 37.46783496848464))");
		
		int j = 0;
		premises.forEach(o -> {
			JSONObject obj = (JSONObject) o; 
		try {
			Geometry meter_the_geom = wktReader.read(obj.get("the_geom").toString());
//			U.log(j+""+o);
			int i = 0;
			
			for (Geometry g : tiles) {
				
				if (g.intersects(meter_the_geom)) {
						String avgEtoG = getAvgEto(g);
//					    String avgEtoG = "0.00";
//						U.log(i+" -- "+avgEtoG+" -- "+g);
						
//						writer.writeNext(new String[] {meter_the_geom.toString()});
						writer.writeNext(new String[] { meter_the_geom.toString(),avgEtoG.toString(), g.toString()});
				}else {

				}
				
				i++;
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			U.log("handle exception");
		}	
		});

		writer.flush();
		writer.close();
		System.out.println("Data written to " + OutputcsvFilePath);
	}



	private static String getAvgEto(Geometry g) throws MalformedURLException, IOException {
		// TODO Auto-generated method stub
		DecimalFormat df = new DecimalFormat("####0.00");
//		System.out.println(g.getCentroid().toString());
		String latlon[] = g.getCentroid().toString().replace("POINT (", "").replace(")", "").split(" ");
		String[] outData = { latlon[1], latlon[0] };
		String etoUrl = "https://et.water.ca.gov/api/data?appKey=08807bb2-ce9c-4b65-af3c-dec10f6f3daf&targets=lat%3D"
				+ df.format(Double.parseDouble(outData[0])) + ",lng%3D"
				+ df.format(Double.parseDouble(outData[1])) + "&startDate=" + "2023-07-01" + "&endDate="
				+ "2023-09-01" + "&dataItems=day-asce-eto&unitOfMeasure=E";
//		U.log("etoUrl ::" + etoUrl);
		U.downloadUsingStream(etoUrl, U.getCache(etoUrl));
		String etoUrlCache = U.getCache(etoUrl);
//		U.log("etoUrlCache " + etoUrlCache);
		String etoFile = FileUtil.readAllText(etoUrlCache);
		String etoVals[] = U.getValues(etoFile, "\"DayAsceEto\":{\"Value\":\"", "\"");
		double avgEto = 0.0;
		double sumEto = 0.0;
		for (String eto : etoVals) {
			sumEto += Double.parseDouble(eto);
//			U.log("sumEto "+sumEto);
		}
		avgEto = sumEto / etoVals.length;
//		U.log("avgEto " + avgEto);
		return avgEto+"";
	}
    public static double calculateAccuracy(double correctValue, double estimatedValue) {
        double difference = Math.abs(correctValue - estimatedValue);
        double accuracy = (difference / correctValue) * 100.0;
        return 100.0 - accuracy;
    }
	private static void GeometryCoverCheck(String poly1, String poly2) throws ParseException {
		WKTReader reader = new WKTReader();
		String geometry1 = poly1;
		String geometry2 = poly2;
		Geometry geom1 = reader.read(geometry1);
		Geometry geom2 = reader.read(geometry2);
		boolean covers = geom1.intersects(geom2);
		if (covers)
			System.out.println("Geometry 1 intersects Geometry 2: " + covers);
	}

	private static void ApiCall(String poly1, String poly2) {
		// TODO Auto-generated method stub
		try {
			// Specify the URL of the API you want to call
			String apiUrl = "http://127.0.0.1:8080/PostReqData?poly1=" + poly1 + "&poly2=" + poly2;

			System.out.println(apiUrl);
			Thread.sleep(1000);
			// Create a URL object with the API URL
			URL url = new URL(apiUrl);

			// Open a connection to the API URL
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			// Set the request method
			connection.setRequestMethod("GET");

			// Get the API response code
			int responseCode = connection.getResponseCode();

			// Check if the response code is successful
			if (responseCode == HttpURLConnection.HTTP_OK) {
				// Create a BufferedReader to read the API response
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				StringBuilder response = new StringBuilder();
				String line;

				// Read the response line by line
				while ((line = reader.readLine()) != null) {
					response.append(line);
				}
				reader.close();

				// Print the response
				System.out.println("API Response: " + response.toString());

				// Get the return type of the API response
				Class<?> returnType = response.getClass();

				// Print the return type
				System.out.println("Return Type: " + returnType.getName());
			} else {
				System.out.println("API request failed with response code: " + responseCode);
			}

			// Close the connection
			connection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
