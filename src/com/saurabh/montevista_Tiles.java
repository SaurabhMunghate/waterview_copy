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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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

public class montevista_Tiles {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception, ParseException {
		String filePath = "/home/shatam-100/ResidentialWaterView/21june_12_Zoom.txt";
		String csvFilePath = filePath.replace(".txt", ".csv");//"/home/shatam-100/ResidentialWaterView/myfile14.csv";
		String premiseFilePath = "/home/shatam-100/ResidentialWaterView/premise_bounds.json";

		// Adding key-value pairs to the map
		Map<String, List<Double>> map = new HashMap<>();
		
		WKTReader wktReader = new WKTReader();
		GeometryFactory geometryFactory = new GeometryFactory();

		// Reading tiles
		Set<Geometry> tiles = new HashSet<>();
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line;
		while ((line = br.readLine()) != null) {
			tiles.add(wktReader.read(line.trim().replace(",POLYGON", "POLYGON")));
		}
		br.close();

		CSVWriter writer = new CSVWriter(new FileWriter(csvFilePath));
		writer.writeNext(new String[] { "Tile geom","premiseID", "Premise geom", "AvgEtoOfTile", "avgEtoOfPremise", "Diff betw AvgEtoOfTile & avgEtoOfPremise", "Accuracy" });
		// Reading Premises
		JSONParser parser = new JSONParser();
		JSONArray premises = (JSONArray) parser.parse(new FileReader(premiseFilePath));

		premises.forEach(o -> {
			JSONObject obj = (JSONObject) o;
			try {
				Geometry premise = wktReader.read(obj.get("the_geom").toString());
				String premiseID = obj.get("premID").toString(); 
//				System.out.println(premiseID);
				for (Geometry g : tiles) {
					
//System.out.println(g);
					if (g.intersects(premise)) {
//						System.out.println(g);
						String avgEtoG = getAvgEto(g);
						String avgEtoPremise = getAvgEto(premise);
//						String avgEtoG = "0";
//						String avgEtoPremise = "0";
						double numavgEtoG = Double.parseDouble(avgEtoG);
						double numavgEtoPremise = Double.parseDouble(avgEtoPremise);
						double Diff = numavgEtoG-numavgEtoPremise;
						double accuracy = calculateAccuracy(numavgEtoG, numavgEtoPremise);
						String Accuracy = "Accuracy: " + accuracy + "%";
//				        System.out.println("Accuracy: " + accuracy + "%");
				        addKeyValuePair(map, premiseID, numavgEtoPremise);
				        
				        writer.writeNext(new String[] { g.toString(), premiseID ,premise.toString(), avgEtoG,"" + avgEtoPremise , Diff+"", Accuracy});
					} else {
//						writer.writeNext(new String[] { g.toString(), premiseID ,premise.toString(), "avgEtoG","" + "avgEtoPremise" , "Diff"+"", "Accuracy"});
					}
				}

			} catch (ParseException e) {
				e.printStackTrace();
			} 
			catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		writer.flush();
		writer.close();
		System.out.println("Data written to " + csvFilePath);
		// Calculating average values for each key
//        calculateAverageValues(map);
	}

	private static String getAvgEto(Geometry g) throws MalformedURLException, IOException {
		// TODO Auto-generated method stub
		DecimalFormat df = new DecimalFormat("####0.00");
//		System.out.println(g.getCentroid().toString());
		String latlon[] = g.getCentroid().toString().replace("POINT (", "").replace(")", "").split(" ");
		String[] outData = { latlon[1], latlon[0] };
		String etoUrl = "https://et.water.ca.gov/api/data?appKey=08807bb2-ce9c-4b65-af3c-dec10f6f3daf&targets=lat%3D"
				+ df.format(Double.parseDouble(outData[0])) + ",lng%3D"
				+ df.format(Double.parseDouble(outData[1])) + "&startDate=" + "2010-08-01" + "&endDate="
				+ "2010-10-01" + "&dataItems=day-asce-eto&unitOfMeasure=E";
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
    
    private static void addKeyValuePair(Map<String, List<Double>> map, String key, double value) {
        if (map.containsKey(key)) {
            // Key already exists, add value to the list
            List<Double> values = map.get(key);
            values.add(value);
        } else {
            // Key doesn't exist, create a new list with the value
            List<Double> values = new ArrayList<>();
            values.add(value);
            map.put(key, values);
        }
    }
    
    private static void calculateAverageValues(Map<String, List<Double>> map) {
        for (Map.Entry<String, List<Double>> entry : map.entrySet()) {
            String key = entry.getKey();
            List<Double> values = entry.getValue();
            
            // Calculate the sum of values
            double sum = 0;
            for (double value : values) {
                sum += value;
            }
            
            // Calculate the average by dividing the sum by the number of times the key is repeated
            double average = sum / values.size();
            
            System.out.println("Key: " + key + " Average: " + average);
        }
    }}
