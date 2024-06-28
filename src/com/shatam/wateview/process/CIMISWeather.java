package com.shatam.wateview.process;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

import com.shatam.utils.FileUtil;
import com.shatam.utils.U;
import com.shatam.waterview.geotools.ShatamGeometry;

public class CIMISWeather {
	private static final double EARTH_RADIUS = 6371;

	public static void main(String[] args) throws Exception {
		System.out.println("");

		//To find Out Eto And Precipitaction From Lat Long
//		int i = 64;
//		new CIMISWeather().processStation(78);
//		new CIMISWeather().processStation(159);
//		new CIMISWeather().processStation(44);
		
		//to find out Eto From lat , lon
//		String lat = "34.03";
//		String lon = "-117.72";
//		processStation(lat , lon);
		//ZOOM 15
//		U.log("ZOOM 15");
		WKTReader reader = new WKTReader();
		String geometry1 = "POLYGON((-124.20661556067111 41.99750958106321,-121.41952152414558 41.99750957862233,-119.99267215452176 41.98188784607251,-119.99267215452176 39.0063113246477,-114.65331684808925 35.02092846938844,-114.01610977116445 34.26615351867197,-114.47753555241444 33.924986281224506,-114.69176895085194 33.08679444440202,-114.46105606022694 32.67160337767535,-117.11425430241444 32.43546716327157,-117.70751602116444 33.568717166661855,-119.65209609928944 34.32061252588879,-120.52550918522694 34.48377755607159,-120.92101699772695 35.33515220162887,-122.00866348210194 36.4299832798426,-121.82738906803947 36.83992629263638,-122.05810195866447 36.95853301861354,-122.42065078678945 37.168933846754015,-122.61840469303945 37.83568211222135,-122.96447402897694 37.95272462913607,-123.06335098210195 38.302734318035306,-123.73901016178945 38.831015165753485,-123.80492796429289 39.38098386672456,-123.83376707562103 39.79373124934699,-124.2993127299179 40.216601721812765,-124.41878904827726 40.44273295264907,-124.18395633396366 40.84277390979483,-124.06859988865116 41.586558870216265,-124.20661556067111 41.99750958106321))"
				+ "";
		String geometry2 = "POLYGON ((-117.685546875 34.08906131584994, -117.70751953125 34.08906131584994, -117.70751953125 34.0708623237663, -117.685546875 34.0708623237663, -117.685546875 34.08906131584994))";
		String geometry3 = "POLYGON ((-117.685546875 34.05265942137598, -117.70751953125 34.05265942137598, -117.70751953125 34.034452609676435, -117.685546875 34.034452609676435, -117.685546875 34.05265942137598))\n"
				+ ""
				+ ""
				+ "";


		Geometry geom1 = reader.read(geometry3);
		System.out.println(geom1.getCentroid());
		String ge = geom1.getCentroid().toString();
		String[] st = ge.split(" ");
		System.out.println(st[1]+" "+st[2]);
//		Geometry geom2 = reader.read(geometry2);
//		System.out.println(geom2.getCentroid());
//		Geometry g = new  Geometry();
//		processStation(st[1].replace("(", ""),st[2].replace(")", "")); //-117.68555535707216 34.089058163150085
//		processStation("-117.68538428993828","34.10461583228762");
//		processStation("-117.68586112723631","34.08512880679221");
		
//		processStation("-117.65076471133455","34.1067676193814");
//		processStation("-117.65124155780597","34.083233173001744");
//		processStation("-117.65133692290932","34.08607660794948");
//		processStation("-117.65114618222528","34.087814214428235");
		
//		processStation("-117.68528772541946","34.09966059008727");
//		processStation("-117.68528772541946","34.101476887709936");
//		processStation("-117.68519236031612","34.103056244551425");
//		processStation("-117.68538309052279","34.10463557299913");
//		processStation("-117.68528772541946","34.10613590750279");
//		processStation("-117.68500163010944","34.10771517738165");
		
		processStation("-117.68538309756077","34.096027877528726");
		processStation("-117.68538309756077","34.09436941298705");
		processStation("-117.6854784626641","34.092631941052204");
		processStation("-117.6854784626641","34.09073647368848");
		processStation("-117.6854784626641","34.08884096605263");
		processStation("-117.68566919287076","34.086787451012285");
		processStation("-117.68585993355482","34.08512880543208");
		processStation("-117.68585993355482","34.08339114167312");
		processStation("-117.68595529865814","34.081495471757194");
		processStation("-117.6861460393422","34.079757733439806");
		processStation("-117.68624140444554","34.07809894799264");
		
//		processStation("-117.70633935928343","34.07822059033094");
//		processStation("-117.68767118453977","34.083445637855235");
//		processStation("-117.68762826919553","34.07761631233788");
		//processStationID 134 Stations Only EToCalculator
//		new CIMISWeather().processStationID();
//		int zip = 91799;
		
		/*
<zip-code></zip-code>
<zip-code></zip-code>
<zip-code></zip-code>
<zip-code></zip-code>
<zip-code></zip-code>
<zip-code></zip-code>
<zip-code></zip-code>
		 */

//		new CIMISWeather().precipitationZip(93280);
//		new CIMISWeather().precipitationZip(94805);
//		new CIMISWeather().precipitationZip(95616);
//		new CIMISWeather().precipitationZip(94808);
//		new CIMISWeather().precipitationZip(94804);
//		new CIMISWeather().precipitationZip(93202);
//		new CIMISWeather().precipitationZip(92516);
//		new CIMISWeather().precipitationZip(94707);
//		new CIMISWeather().precipitationZip(94706);
//		new CIMISWeather().precipitationZip(94806);
		
//		new CIMISWeather().preciEtoLatLon();
//		
//		FromLatLongFindNearestStation();

//		new CIMISWeather().center();
//		double lon = -117.72;
//		double lat = 34.03;
//		new CIMISWeather().DistanceCalculator(lat , lon);

		// ,POINT(-117.6562315473366 33.985461719362846),POINT(-117.65622484181407 33.98541044303744))
//		double sourceLat= 33.985461719362846;
//		double sourceLon= -117.6562315473366;
//		double destLat = 33.98541044303744;
//		double destLon = -117.65622484181407;
//		double distance = calculateDistance(sourceLat, sourceLon, destLat, destLon);
//		System.out.println("Distance: " + distance + " km");

	}

	private static void processStation(String lon, String lat) throws MalformedURLException, IOException {
		// TODO Auto-generated method stub
//		String lon = df.format(Double.parseDouble(lon));
//		String lat = df.format(Double.parseDouble(latlon[1]));

//		System.out.println(lat + " " + lon);
//		double latitude = Double.parseDouble(lat);
//		double longitude = Double.parseDouble(lon);
		
		DecimalFormat df = new DecimalFormat("####0.00");
		String latitude = df.format(Double.parseDouble(lat));
		String longitude = df.format(Double.parseDouble(lon));
		
//		System.out.println(latitude + " " + longitude);

//		try {
			String etoUrl = "https://et.water.ca.gov/api/data?appKey=08807bb2-ce9c-4b65-af3c-dec10f6f3daf&targets=lat%3D"
					+ latitude + ",lng%3D" + longitude
					+ "&startDate=2010-08-01&endDate=2010-10-01&dataItems=day-asce-eto&unitOfMeasure=E";

//			U.log("etoUrl ::" + etoUrl);
			U.downloadUsingStream(etoUrl, U.getCache(etoUrl));
			String etoUrlCache = U.getCache(etoUrl);
//		U.log("etoUrlCache "+etoUrlCache);
			String etoFile = FileUtil.readAllText(etoUrlCache);
			String etoVals[] = U.getValues(etoFile, "\"DayAsceEto\":{\"Value\":\"", "\"");
			double avgEto = 0.0;
			double sumEto = 0.0;
			for (String eto : etoVals) {
				sumEto += Double.parseDouble(eto);
//			U.log("sumEto "+sumEto);
			}
			avgEto = sumEto / etoVals.length;
			
//			obj.put("premise_bounds LatLon ", lat + ", " + lon);
//			obj.put("premise_bounds avgEto", avgEto);
			U.log("premise_bounds LatLon " + lat + ", " + lon + " |  avgEto " + avgEto);
			U.log(",POINT(" + lon + " " + lat + ")" );
//			U.log("premise_bounds LatLon " + lon + " " + lat + " |  avgEto " + avgEto);
//		} catch (Exception e) {}
		
	}

	private static void FromLatLongFindNearestStation() throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		double lowestdistance = 10000;
		double newdestLat = 0000;
		double newdestLon = 0000;
		String meter = "";
		String data = "";
		try {
			BufferedReader reader1 = new BufferedReader(
					new FileReader("/home/shatam-100/CIMISWeatherStationNetwork/StationData (another copy).csv"));
			String line;

			while ((line = reader1.readLine()) != null) {
				String[] parts = line.split(",");
				double destLat = Double.parseDouble(parts[0]);
				double destLon = Double.parseDouble(parts[1]);
				data += "POINT(" + destLon + " " + destLat + "),";
				String met = parts[2];
//			System.out.println(destLat + "  " + destLon);
				double destLatt = Double.parseDouble("35.53");
				double destLonn = Double.parseDouble("-119.28");

				double distance = calculateDistance(destLatt, destLonn, destLat, destLon);

				System.out.println("Distance: " + distance + " km");
				if (lowestdistance > distance) {
					lowestdistance = distance;
					meter = met;
					newdestLat = destLat;
					newdestLon = destLon;
				}
			}

			reader1.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		FileUtil.writeAllText("/home/shatam-100/Cache/GEOMETRYCOLLECTIONsatationData.txt",
				"GEOMETRYCOLLECTION(" + data.toString() + ")");
		System.out.println("Meter " + meter + " Nearest metro station - Distance: " + lowestdistance + " km"
				+ "  | LatLon " + newdestLat + ", " + newdestLon);

	}

	private void center() throws ParseException {
		String the_geom = "POLYGON((-117.71559739134003 34.03340591155764,-117.7155920080976 34.0333138283809,-117.7156941819317 34.03330222459843,-117.71569670815491 34.0332456632766,-117.71573252225343 34.033251212227725,-117.71579137861826 34.033302629713134,-117.71580427618429 34.03329805799535,-117.71582532031177 34.03329268361158,-117.71584390509369 34.03329209034429,-117.71586710113642 34.03329255566909,-117.71587400405458 34.03329462632692,-117.7158878657864 34.033296836579865,-117.7159003441405 34.03330674781033,-117.71608028118371 34.03330456068129,-117.71609337434597 34.033293230216145,-117.71612234145736 34.0332947773386,-117.71615003699173 34.03330016331314,-117.71629529171135 34.033293415819145,-117.71630890197646 34.03330431579725,-117.7164852478359 34.033305920366246,-117.71649602133557 34.03329454333522,-117.71659350061309 34.033294566048355,-117.71660407852669 34.033289947722494,-117.71662048338857 34.03328448015721,-117.71663909610056 34.03328292122927,-117.7166516583603 34.03328993578508,-117.71667454709406 34.0333010217843,-117.71681609887646 34.03330192809862,-117.7168267885208 34.033293447628694,-117.71696254127892 34.0332942374792,-117.71709017542194 34.0332948643924,-117.71717365327498 34.033297504156835,-117.71718696972937 34.033278449318374,-117.71720925735274 34.03327020133857,-117.71722665438595 34.033270550124115,-117.71723916083468 34.033279495677164,-117.71725163935571 34.03328940675968,-117.71736190438419 34.03328871905573,-117.71736453120947 34.033278144716675,-117.7173835907673 34.03326113717104,-117.71740322361768 34.033264429023944,-117.71741923742084 34.03327247877762,-117.71742950807173 34.03327848121616,-117.71742792934437 34.033292940930934,-117.71743483230351 34.033295011494005,-117.71748003667933 34.03329688377225,-117.71750478351936 34.03328385457911,-117.71768455291476 34.03328745810143,-117.71769018437702 34.033293367525715,-117.71776557155344 34.03329487859202,-117.7177681853122 34.03332487979512,-117.71852371979507 34.03332263137274,-117.71970512539016 34.0333211788047,-117.7200919108584 34.033284462230995,-117.72008998346833 34.03324406123089,-117.72172954546312 34.03323739451834,-117.7221460963624 34.033237145558175,-117.72297934777718 34.03323149521606,-117.72347019069704 34.0332292931825,-117.72394670620815 34.033223368743194,-117.72411187570302 34.03321637039197,-117.72413258359403 34.033213351306884,-117.72414660094985 34.03317870905549,-117.72414666768017 34.03317854413915,-117.72420839509107 34.033220218109506,-117.72420853990548 34.03322516936289,-117.72420614918161 34.033227840826896,-117.72413609419144 34.03330612219446,-117.72316853677837 34.0333211123026,-117.72150801744165 34.03333939722482,-117.72009383630021 34.03332482234967,-117.72009360067759 34.03331988335188,-117.71953124224156 34.03339498358909,-117.71659780527051 34.033386431020645,-117.71569011226634 34.03339334291327,-117.71569012863877 34.033392976340565,-117.71559739134003 34.03340591155764))";
		// TODO Auto-generated method stub
		WKTReader reader = new WKTReader();
		Geometry geometry = reader.read(the_geom);
		geometry = ShatamGeometry.validate(geometry);
//        System.out.println(the_geom+I_SLA+I+premID);

		String latlon[] = geometry.getCentroid().toString().replace("POINT (", "").replace(")", "").split(" ");
		DecimalFormat df = new DecimalFormat("####0.00");
		String lon = df.format(Double.parseDouble(latlon[0]));
		String lat = df.format(Double.parseDouble(latlon[1]));
		System.out.println(lat + " , " + lon);

	}

	private void DistanceCalculator(double lat, double lon) throws MalformedURLException, IOException {
		// TODO Auto-generated method stub
//		double sourceLat = 34.03;
//		double sourceLon = -117.72;
		double sourceLat = lat;
		double sourceLon = lon;
		double lowestdistance = 10000;
		double newdestLat = 00;
		double newdestLon = 00;

		try {
			BufferedReader reader = new BufferedReader(
					new FileReader("/home/shatam-100/CIMISWeatherStationNetwork/StationData (another copy).csv"));
			String line;

			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");
				double destLat = Double.parseDouble(parts[0]);
				double destLon = Double.parseDouble(parts[1]);
//				System.out.println(destLat + "  " + destLon);

				double distance = calculateDistance(sourceLat, sourceLon, destLat, destLon);
//				System.out.println("Distance: " + distance + " km");
				if (lowestdistance > distance) {
					lowestdistance = distance;
					newdestLat = destLat;
					newdestLon = destLon;
				}
			}

			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Nearest metro station - Distance: " + lowestdistance + " km" + "  | LatLon " + newdestLat
				+ ", " + newdestLon);
		DecimalFormat df = new DecimalFormat("####0.00");
		String etoUrl = "https://et.water.ca.gov/api/data?appKey=08807bb2-ce9c-4b65-af3c-dec10f6f3daf&targets=lat%3D"
				+ df.format(newdestLat) + ",lng%3D" + df.format(newdestLon)
				+ "&startDate=2010-08-01&endDate=2010-10-01&dataItems=day-asce-eto&unitOfMeasure=E";
////
//		U.log("etoUrl ::" + etoUrl);
		U.downloadUsingStream(etoUrl, U.getCache(etoUrl));
		String etoUrlCache = U.getCache(etoUrl);
//	U.log("etoUrlCache "+etoUrlCache);
		String etoFile = FileUtil.readAllText(etoUrlCache);
		String etoVals[] = U.getValues(etoFile, "\"DayAsceEto\":{\"Value\":\"", "\"");
		double avgEto = 0.0;
		double sumEto = 0.0;
		for (String eto : etoVals) {
			sumEto += Double.parseDouble(eto);
//		U.log("sumEto "+sumEto);
		}
		avgEto = sumEto / etoVals.length;
		U.log("AvgEto From this station " + avgEto);

	}

	private String etoFromLatLong(double newdestLat, double newdestLon) throws MalformedURLException, IOException {
		// TODO Auto-generated method stub
//		double latitude = Double.parseDouble(newdestLat);
//		double longitude = Double.parseDouble(newdestLon);

		DecimalFormat df = new DecimalFormat("####0.00");
		String etoUrl = "https://et.water.ca.gov/api/data?appKey=08807bb2-ce9c-4b65-af3c-dec10f6f3daf&targets=lat%3D"
				+ df.format(newdestLat) + ",lng%3D" + df.format(newdestLon)
				+ "&startDate=2010-08-01&endDate=2010-10-01&dataItems=day-asce-eto&unitOfMeasure=E";
////
//		U.log("etoUrl ::" + etoUrl);
		U.downloadUsingStream(etoUrl, U.getCache(etoUrl));
		String etoUrlCache = U.getCache(etoUrl);
//	U.log("etoUrlCache "+etoUrlCache);
		String etoFile = FileUtil.readAllText(etoUrlCache);
		String etoVals[] = U.getValues(etoFile, "\"DayAsceEto\":{\"Value\":\"", "\"");
		double avgEto = 0.0;
		double sumEto = 0.0;
		for (String eto : etoVals) {
			sumEto += Double.parseDouble(eto);
//		U.log("sumEto "+sumEto);
		}
		avgEto = sumEto / etoVals.length;
		U.log("AvgEto From this station " + avgEto);
		return "" + "AvgEto From this station " + avgEto;
	}

	private static double calculateDistance(double sourceLat, double sourceLon, double destLat, double destLon) {
		double latDistance = Math.toRadians(destLat - sourceLat);
		double lonDistance = Math.toRadians(destLon - sourceLon);

		double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(sourceLat))
				* Math.cos(Math.toRadians(destLat)) * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		return EARTH_RADIUS * c;
	}

	private void preciEtoLatLon() throws MalformedURLException, IOException, ParseException {
		JSONObject obj = new JSONObject();
		List<JSONObject> jsonObj = new ArrayList<JSONObject>();
		String landscapedatacsv = "/home/shatam-100/Cache/NEW_prd.premise_bounds (1).json";

		String landscapedatacsvjsonString = new String(Files.readAllBytes(Paths.get(landscapedatacsv)));
		JSONArray landscapedatacsvjsonArray = new JSONArray(landscapedatacsvjsonString);
		for (int i = 0; i < landscapedatacsvjsonArray.length(); i++) {
			System.out.println("-------------------------" + i);
			JSONObject jsonObject = landscapedatacsvjsonArray.getJSONObject(i);
			String the_geom = jsonObject.getString("the_geom");
			String I_SLA = jsonObject.getString("I_SLA");
			String I = jsonObject.getString("I");
			String premID = jsonObject.getString("premID");

			WKTReader reader = new WKTReader();
			Geometry geometry = reader.read(the_geom);
			geometry = ShatamGeometry.validate(geometry);
//            System.out.println(the_geom+I_SLA+I+premID);

			String latlon[] = geometry.getCentroid().toString().replace("POINT (", "").replace(")", "").split(" ");
			DecimalFormat df = new DecimalFormat("####0.00");
			String lon = df.format(Double.parseDouble(latlon[0]));
			String lat = df.format(Double.parseDouble(latlon[1]));

//			System.out.println(lat + " " + lon);
			double latitude = Double.parseDouble(lat);
			double longitude = Double.parseDouble(lon);

			String etoUrl = "https://et.water.ca.gov/api/data?appKey=08807bb2-ce9c-4b65-af3c-dec10f6f3daf&targets=lat%3D"
					+ lat + ",lng%3D" + lon
					+ "&startDate=2010-08-01&endDate=2010-10-01&dataItems=day-asce-eto&unitOfMeasure=E";
////
//			U.log("etoUrl ::" + etoUrl);
			U.downloadUsingStream(etoUrl, U.getCache(etoUrl));
			String etoUrlCache = U.getCache(etoUrl);
//		U.log("etoUrlCache "+etoUrlCache);
			String etoFile = FileUtil.readAllText(etoUrlCache);
			String etoVals[] = U.getValues(etoFile, "\"DayAsceEto\":{\"Value\":\"", "\"");
			double avgEto = 0.0;
			double sumEto = 0.0;
			for (String eto : etoVals) {
				sumEto += Double.parseDouble(eto);
//			U.log("sumEto "+sumEto);
			}
			avgEto = sumEto / etoVals.length;
			obj.put("premise_bounds", "premise_bounds LatLon " + lat + ", " + lon + " |  avgEto " + avgEto);
			U.log("premise_bounds LatLon " + lat + ", " + lon + " |  avgEto " + avgEto);
//		String  DistanceCalculator = DistanceCalculator(latitude , longitude);

			double sourceLat = Double.parseDouble(lat);
			double sourceLon = Double.parseDouble(lon);
			double lowestdistance = 10000;
			double newdestLat = 00;
			double newdestLon = 00;

			try {
				BufferedReader reader1 = new BufferedReader(
						new FileReader("/home/shatam-100/CIMISWeatherStationNetwork/StationData (another copy).csv"));
				String line;

				while ((line = reader1.readLine()) != null) {
					String[] parts = line.split(",");
					double destLat = Double.parseDouble(parts[0]);
					double destLon = Double.parseDouble(parts[1]);
//				System.out.println(destLat + "  " + destLon);

					double distance = calculateDistance(sourceLat, sourceLon, destLat, destLon);
//				System.out.println("Distance: " + distance + " km");
					if (lowestdistance > distance) {
						lowestdistance = distance;
						newdestLat = destLat;
						newdestLon = destLon;
					}
				}

				reader1.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			System.out.println("Nearest metro station - Distance: " + lowestdistance + " km" + "  | LatLon "
					+ newdestLat + ", " + newdestLon);
//			etoFromLatLong(newdestLat, newdestLon);
		}
		jsonObj.add(obj);
		FileUtil.writeAllText("/home/shatam-100/Cache/textFile.txt", jsonObj.toString());
	}

	private void precipitationZip(int zip) throws MalformedURLException, IOException {
		// TODO Auto-generated method stub
//		int zip = 95627;
		System.out.println("Zip : " + zip);
		String precipUrl = "https://et.water.ca.gov/api/data?appKey=08807bb2-ce9c-4b65-af3c-dec10f6f3daf&targets=" + zip
				+ "&startDate=2010-08-01&endDate=2010-10-01&dataItems=day-precip&unitOfMeasure=E";
//		U.log("precipUrl :: " + precipUrl);
		U.downloadUsingStream(precipUrl, U.getCache(precipUrl));
		String precipUrlCache = U.getCache(precipUrl);
//		U.log("precipUrlCache :: " + precipUrlCache);
		String precipFile = FileUtil.readAllText(precipUrlCache);
		String precipVals[] = U.getValues(precipFile, "\"DayPrecip\":{\"Value\":\"", "\"");
		double avgprecip = 0.0;
		double sumprecip = 0.0;

//		U.log("precipVals.length "+precipVals.length);
		if (precipVals.length > 0) {
			for (String precip : precipVals) {
				sumprecip += Double.parseDouble(precip);
//				U.log("sumprecip "+sumprecip);
			}
			avgprecip = sumprecip / precipVals.length;
		}
		System.out.println("avgprecip : " + avgprecip);
	}

	private void processStationID() throws MalformedURLException, IOException {
		// TODO Auto-generated method stub
		String data = "";
        ArrayList<Integer> numbers = new ArrayList<>(Arrays.asList(
                2, 5, 6, 7, 12, 13, 15, 35, 39, 41, 43, 44, 47, 52, 62, 64, 68, 70, 71, 75, 77, 78, 80, 83, 84, 87,
                88, 90, 91, 99, 103, 104, 105, 106, 107, 113, 114, 116, 117, 124, 125, 126, 129, 131, 135, 136, 139,
                140, 143, 144, 146, 147, 148, 150, 151, 152, 153, 157, 158, 159, 160, 163, 165, 170, 171, 173, 174,
                175, 178, 179, 181, 182, 183, 184, 187, 189, 191, 192, 193, 194, 195, 197, 198, 199, 200, 202, 204,
                205, 206, 207, 208, 209, 210, 211, 212, 213, 214, 215, 216, 217, 218, 219, 220, 221, 222, 223, 224,
                225, 226, 227, 228, 229, 231, 232, 233, 235, 236, 237, 239, 240, 241, 242, 243, 244, 245, 246, 247,
                248, 249, 250, 251, 252, 253, 254, 256));

//        int[] numbersArray = createNumbersArray(numbers);
//        System.out.println(Arrays.toString(numbersArray));
		try {
			for (int i = 1; i < 1000; i++) {
				if (!numbers.contains(i)) {
	                continue; // Return null if number is not found in the ArrayList
	            }
				System.out.println(i);
//			int i = 2;
//				if() {
//					
//				}
				String stationNumUrl = "https://et.water.ca.gov/api/station/" + i;
				System.out.println(stationNumUrl);
				U.downloadUsingStream(stationNumUrl, U.getCache(stationNumUrl));
				String stationNumUrlCache = U.getCache(stationNumUrl);
//				U.log("cimiSataionCache "+stationNumUrlCache);
				String cimiSataionFile = FileUtil.readAllText(stationNumUrlCache);
//				System.out.println(cimiSataionFile);
				String sss = U.getSectionValue(cimiSataionFile, "ZipCodes\":[", "]");
				String latlong = U.getSectionValue(cimiSataionFile, "W /", "\"")
						+ U.getSectionValue(cimiSataionFile, "N /", "\"");
				String latlong1 = U.getSectionValue(cimiSataionFile, "N /", "\"") + ","
						+ U.getSectionValue(cimiSataionFile, "W /", "\"");
				latlong = "POINT(" + latlong + "),";
//				data += stationNumUrl + "  " + latlong1 + "   latlong--" + latlong + "  //  " + cimiSataionFile + "\n";
				data += latlong;
//				data += sss;
				System.out.println(latlong);
				System.out.println(sss);
				System.out.println();
//				i++;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		FileUtil.writeAllText("/home/shatam-100/CIMISWeatherStationNetwork/135_GEOMETRYCOLLECTION.txt", "GEOMETRYCOLLECTION(" + data + ")");
//		FileUtil.writeAllText("/home/shatam-100/Cache/textFile.txt", "" + data + "");
//		FileUtil.writeAllText("/home/shatam-100/Cache/textFile.txt", data.replace("\"\"9", "\",\"9")+"");
//		CheckDoubleZip(data);
		System.out.println("DONE");
	}

	private void CheckDoubleZip(String data) throws IOException {
		// TODO Auto-generated method stub
		String text = FileUtil.readAllText("/home/shatam-100/Cache/textFile.txt");
		String[] st = text.split(",");
		HashMap<String, String> hs = new HashMap<>();
		for (String s : st) {
			System.out.println(s);
			hs.put(s.trim(), s.trim());
		}
		System.out.println(hs.size());

		System.out.println(st.length);
		Map<String, Integer> stringCountMap = new HashMap<>();

		// Count the occurrences of each string
		for (String s : st) {
			stringCountMap.put(s, stringCountMap.getOrDefault(s, 0) + 1);
		}

		// Print the repeated strings
		for (Map.Entry<String, Integer> entry : stringCountMap.entrySet()) {
			if (entry.getValue() > 1) {
				System.out.println("Repeated string: " + entry.getKey());
			}
		}
	}

	static void processStation(int i) throws Exception {
		// TODO Auto-generated method stub
//		for (int i = 0; i < 256; i++) {
//		int i = 226;
//			try {

		System.out.println();
				System.out.println("Satation : " + i);
		String cimiSataionUrl = "http://et.water.ca.gov/api/data?appKey=08807bb2-ce9c-4b65-af3c-dec10f6f3daf&targets="+i+"&startDate=2010-08-01&endDate=2010-10-01";
//				String cimiSataionUrl = "http://et.water.ca.gov/api/data?appKey=08807bb2-ce9c-4b65-af3c-dec10f6f3daf&targets=2&startDate=2010-01-01&endDate=2010-03-01";
//		U.downloadUsingStream(cimiSataionUrl, U.getCache(cimiSataionUrl));
				U.getData(cimiSataionUrl, U.getCache(cimiSataionUrl));
				String cimiSataionCache = U.getCache(cimiSataionUrl);
				U.log("cimiSataionCache " + cimiSataionCache);
				String cimiSataionFile = FileUtil.readAllText(cimiSataionCache);
//		String zip_code[] = U.getValues(cimiSataionFile, "zip-code=\"", "\"");
//		String station[] = U.getValues(cimiSataionFile, "station=\"", "\"");
				String name= "";
				String precip[] = U.getValues(cimiSataionFile, "day-precip qc", "day");
				String tmpavg[] = U.getValues(cimiSataionFile, "day-air-tmp-avg qc", "day");
				String tmpmax[] = U.getValues(cimiSataionFile, "day-air-tmp-max qc", "day");
				String tmpmin[] = U.getValues(cimiSataionFile, "day-air-tmp-min qc", "day");
				String windrun[] = U.getValues(cimiSataionFile, "day-wind-run qc", "day");
				String windspdavg[] = U.getValues(cimiSataionFile, "day-wind-spd-avg qc", "day");
				String soiltmpavg[] = U.getValues(cimiSataionFile, "day-soil-tmp-avg", "day");
				String solradavg[] = U.getValues(cimiSataionFile, "day-sol-rad-avg", "day");
				String vappres[] = U.getValues(cimiSataionFile, "day-vap-pres-avg", "day");
//				String precip[] = U.getValues(cimiSataionFile, "day-precip qc", "day");
				
				AvgCalculater(precip, "precip");
				AvgCalculater(tmpavg, "tmpavg");
				AvgCalculater(tmpmax, "tmpmax");
				AvgCalculater(tmpmin, "tmpmin");
				AvgCalculater(windrun, "windrun");
				AvgCalculater(windspdavg, "windspdavg");
				AvgCalculater(soiltmpavg, "soiltmpavg");
				AvgCalculater(solradavg, "solradavg");
				AvgCalculater(vappres, "vappres");
				
				
				
//		String eto[] = U.getValues(cimiSataionFile, "day-asce-eto qc=\"", "\"");
//			String precip[] = U.getValues(cimiSataionFile, "day-precip qc=\" ", "day-precip");
				String etoVals[] = U.getValues(cimiSataionFile, "day-asce-eto qc", "day-asce-eto");
//		System.out.println(cimiSataionVals.length);
//		for (String pre : precip) {
////			System.out.println(pre);
//		}
//		System.out.println();
//		for (String et : eto) {
//			System.out.println(et);
//		}
//		System.out.println();
//		for (String zip : zip_code) {
//			System.out.println(zip);
//		}
//		System.out.println();
//		for (String st : station) {
//			System.out.println(st);
//		}
				double avgEto = 0.0;
				double sumEto = 0.0;
				for (String et : etoVals) {
					et = U.getSectionValue(et, ">", "<");
					sumEto += Double.parseDouble(et);
//			U.log("sumEto "+sumEto);
				}
				avgEto = sumEto / etoVals.length;
				System.out.println("avgEto :" + avgEto);
				double avgprecip = 0.0;
				double sumprecip = 0.0;
				if (precip.length > 0) {
					for (String pre : precip) {
						pre = U.getSectionValue(pre, ">", "<");
//						System.out.println(pre);
						sumprecip += Double.parseDouble(pre);
//				U.log("sumprecip "+sumprecip);
					}
					avgprecip = sumprecip / precip.length;
				}
				System.out.println("avgprecip :" + avgprecip);
//			} catch (Exception e) {
//				// TODO: handle exception
//			}
//		}
	}

	private static void AvgCalculater(String[] tmpavg, String name) {
		// TODO Auto-generated method stub
//		try {
			double avg = 0.0;
			double sump = 0.0;
			if (tmpavg.length > 0) {
				for (String tmp : tmpavg) {
					tmp = U.getSectionValue(tmp, ">", "<");
//					System.out.println(pre);
					try {
						sump += Double.parseDouble(tmp);
					} catch (Exception e) {
						// TODO: handle exception
					}
					
//			U.log("sumprecip "+sumprecip);
				}
				avg = sump / tmpavg.length;
			}
			System.out.println("avg Value " +name+" :"+ avg);
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
		
	}
}
