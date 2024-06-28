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

public class califonia_Tiles {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception, ParseException {
		String filePath = "/home/shatam-100/ResidentialWaterView/myfile_4_Sep_Califonia.txt";
		String csvFilePath = "/home/shatam-100/ResidentialWaterView/myfile_4_Sep_Califonia.csv";
//		String premiseFilePath = "/home/shatam-100/ResidentialWaterView/premise_bounds.json";
		String premiseFilePath = "/home/shatam-100/ResidentialWaterView/san_jose.txt";

		WKTReader wktReader = new WKTReader();
		GeometryFactory geometryFactory = new GeometryFactory();

		// Reading tiles
		Set<Geometry> tiles = new HashSet<>();
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line;
		while ((line = br.readLine()) != null) {
			line = line.replace(",POLYGON", "POLYGON");
			tiles.add(wktReader.read(line.trim()));
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
//				Geometry meterLocation = wktReader.read(obj.get("the_geom").toString());
//				U.log(premise);
//				Geometry premise = wktReader.read("POLYGON((-124.20661556067111 41.99750958106321,-121.41952152414558 41.99750957862233,-119.99267215452176 41.98188784607251,-119.99267215452176 39.0063113246477,-114.65331684808925 35.02092846938844,-114.01610977116445 34.26615351867197,-114.47753555241444 33.924986281224506,-114.69176895085194 33.08679444440202,-114.46105606022694 32.67160337767535,-117.11425430241444 32.43546716327157,-117.70751602116444 33.568717166661855,-119.65209609928944 34.32061252588879,-120.52550918522694 34.48377755607159,-120.92101699772695 35.33515220162887,-122.00866348210194 36.4299832798426,-121.82738906803947 36.83992629263638,-122.05810195866447 36.95853301861354,-122.42065078678945 37.168933846754015,-122.61840469303945 37.83568211222135,-122.96447402897694 37.95272462913607,-123.06335098210195 38.302734318035306,-123.73901016178945 38.831015165753485,-123.80492796429289 39.38098386672456,-123.83376707562103 39.79373124934699,-124.2993127299179 40.216601721812765,-124.41878904827726 40.44273295264907,-124.18395633396366 40.84277390979483,-124.06859988865116 41.586558870216265,-124.20661556067111 41.99750958106321))");
//				Geometry premise = wktReader.read("POLYGON((-122.04689345773669 37.46783496848464,-121.88515021916837 37.4956846756986,-121.55708630089073 37.20335317393061,-121.62422501001865 37.10971291586404,-121.76307995323448 37.09754329670281,-122.06215220954626 37.28716600294641,-122.04689345773669 37.46783496848464))");
//				String premiseID = obj.get("premID").toString(); 
//				System.out.println(premiseID);
				
				
				for (Geometry g : tiles) {
					int i = 0;
//                System.out.println(g);
					if(g==null)continue;
					if (g.intersects(premise)) {
//						System.out.println(g);
//						String avgEtoG = getAvgEto(g);
						U.log(i+" ---- ");
//						String avgEtoPremise = getAvgEto(premise);
						String avgEtoG = "0";
						String avgEtoPremise = "0";
						double number1 = Double.parseDouble(avgEtoG);
						double number2 = Double.parseDouble(avgEtoPremise);
						double Diff = number1-number2;
						double accuracy = calculateAccuracy(number1, number2);
						String Accuracy = "Accuracy: " + accuracy + "%";
						writer.writeNext(new String[] { ","+g.toString() ,premise.toString(), avgEtoG,"" + avgEtoPremise , Diff+"", Accuracy});
						i++;
					} else {
//						writer.writeNext(new String[] { g.toString() ,premise.toString(), "avgEtoG","" + "avgEtoPremise" , "Diff"+"", "Accuracy"});
					}
					
				}
				
				
				
//				System.out.println(i);
			} catch (ParseException e) {
				e.printStackTrace();
			} 
//			catch (MalformedURLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		});
		writer.flush();
		writer.close();
		System.out.println("Data written to " + csvFilePath);
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
				+ "2023-09-31" + "&dataItems=day-asce-eto&unitOfMeasure=E";
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
