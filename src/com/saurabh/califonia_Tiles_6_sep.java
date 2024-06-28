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

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.shatam.utils.FileUtil;
import com.shatam.utils.U;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class califonia_Tiles_6_sep {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception, ParseException {
//		call();
		getAllMeterData();
	}

	private static void getAllMeterData() throws Exception {
		// TODO Auto-generated method stub
		String tilesfilePath = "/home/shatam-100/ResidentialWaterView/Tiles_6_sep_14Zoom_19.txt"; //All Tiles In San jose
		
		String outPutcsvFilePath = "/home/shatam-100/ResidentialWaterView/Tiles_8_sep_14Zoom_2.31.csv"; //OutPut File

		String meterFile = "/home/shatam-100/ResidentialWaterView/meter_locations_res_convertedData.csv"; //Meter Location File

		FileReader meterfilereader = new FileReader(meterFile);
		CSVReader csvReader = new CSVReader(meterfilereader);

		String[] meternextRecord;

		CSVWriter writer = new CSVWriter(new FileWriter(outPutcsvFilePath));

		
		WKTReader wktReader = new WKTReader();

		Set<Geometry> tiles = new HashSet<>();
		BufferedReader br = new BufferedReader(new FileReader(tilesfilePath));
		String line;
		while ((line = br.readLine()) != null) {
			line = line.replace(",POLYGON", "POLYGON");
			tiles.add(wktReader.read(line.trim()));
		}
		br.close();

		// we are going to read data line by line
		while ((meternextRecord = csvReader.readNext()) != null) {
//			System.out.println(meternextRecord[0]);
			if(meternextRecord[0].contains("the_geom")) continue;
			Geometry meter_the_geom = wktReader.read(meternextRecord[0].toString());
			for (Geometry g : tiles) {

				if (g.intersects(meter_the_geom)) {
					String avgEtoG = "0.00";
					writer.writeNext(new String[] { meter_the_geom.toString(), avgEtoG.toString(), g.toString() });

				}
			}
		}
		writer.flush();
		writer.close();
		System.out.println("Data written to " + outPutcsvFilePath);
	}

	@SuppressWarnings("unchecked")
	private static void call() throws Exception {
		// TODO Auto-generated method stub

//		String filePath = "/home/shatam-100/ResidentialWaterView/4_Sep_Califonia.txt";
		String tilesfilePath = "/home/shatam-100/ResidentialWaterView/Tiles_6_sep_14Zoom_19.txt";
//		String csvFilePath = "/home/shatam-100/ResidentialWaterView/6_Sep_Califonia_san_jose.csv";
		String csvFilePath = "/home/shatam-100/ResidentialWaterView/Tiles_6_sep_14Zoom_19.csv";
//		String premiseFilePath = "/home/shatam-100/ResidentialWaterView/premise_bounds.json";
		String premiseFilePath = "/home/shatam-100/ResidentialWaterView/san_jose.txt";
		String meterFile = "/home/shatam-100/ResidentialWaterView/meter_locations_res_convertedData.csv";

		FileReader meterfilereader = new FileReader(meterFile);
		CSVReader csvReader = new CSVReader(meterfilereader);

		String[] meternextRecord;

		// we are going to read data line by line
//        while ((meternextRecord = csvReader.readNext()) != null) {
//        	System.out.println(meternextRecord[0]);
//
////            for (String cell : meternextRecord) {
////                System.out.print(cell + "\t");
////            }
//            System.out.println();
//        }

//        String[] nextRecord;
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

		CSVWriter writer = new CSVWriter(new FileWriter(csvFilePath));
		writer.writeNext(new String[] { "Tile geom", "premiseID", "Premise geom", "AvgEtoOfTile", "avgEtoOfPremise",
				"Diff betw AvgEtoOfTile & avgEtoOfPremise", "Accuracy" });
		// Reading Premises
		JSONParser parser = new JSONParser();
		JSONArray premises = (JSONArray) parser.parse(new FileReader(premiseFilePath));

		Geometry premise = wktReader.read(
				"POLYGON((-122.04689345773669 37.46783496848464,-121.88515021916837 37.4956846756986,-121.55708630089073 37.20335317393061,-121.62422501001865 37.10971291586404,-121.76307995323448 37.09754329670281,-122.06215220954626 37.28716600294641,-122.04689345773669 37.46783496848464))");

		int j = 0;
		premises.forEach(o -> {
			JSONObject obj = (JSONObject) o;
			try {
				Geometry meter_the_geom = wktReader.read(obj.get("the_geom").toString());
//			U.log(j+""+o);
				int i = 0;

				for (Geometry g : tiles) {

					if (g.intersects(meter_the_geom)) {
//						String avgEtoG = getAvgEto(g);
						String avgEtoG = "0.00";
//						U.log(i+" -- "+g);

//						writer.writeNext(new String[] {meter_the_geom.toString()});
						writer.writeNext(new String[] { meter_the_geom.toString(), avgEtoG.toString(), g.toString() });
					} else if (!g.intersects(meter_the_geom)) {

					} else {
						U.log(j + " -- " + g);

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
		System.out.println("Data written to " + csvFilePath);

	}

	private static String getAvgEto(Geometry g) throws MalformedURLException, IOException {
		// TODO Auto-generated method stub
		DecimalFormat df = new DecimalFormat("####0.00");
//		System.out.println(g.getCentroid().toString());
		String latlon[] = g.getCentroid().toString().replace("POINT (", "").replace(")", "").split(" ");
		String[] outData = { latlon[1], latlon[0] };
		String etoUrl = "https://et.water.ca.gov/api/data?appKey=08807bb2-ce9c-4b65-af3c-dec10f6f3daf&targets=lat%3D"
				+ df.format(Double.parseDouble(outData[0])) + ",lng%3D" + df.format(Double.parseDouble(outData[1]))
				+ "&startDate=" + "2023-07-01" + "&endDate=" + "2023-09-31" + "&dataItems=day-asce-eto&unitOfMeasure=E";
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
		return avgEto + "";
	}

	public static double calculateAccuracy(double correctValue, double estimatedValue) {
		double difference = Math.abs(correctValue - estimatedValue);
		double accuracy = (difference / correctValue) * 100.0;
		return 100.0 - accuracy;
	}

}
