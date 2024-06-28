package com.shatam.wateview.process;

import java.io.BufferedReader;
import java.io.FileReader;
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
import org.json.JSONTokener;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

import com.shatam.utils.FileUtil;
import com.shatam.utils.U;
import com.shatam.waterview.geotools.ShatamGeometry;

public class CIMISWeatherStationNetwork {
	private static final double EARTH_RADIUS = 6371;

	public static void main(String[] args) throws Exception {
		System.out.println("");
//		Stri

		List<JSONObject> jsonObj = new ArrayList<JSONObject>();
//		String landscapedatacsv = "/home/shatam-100/Cache/NEW_prd.premise_bounds (1).json";

		String landscapedatacsvjsonString = "[{\"I_MWELO\":\"0\",\"LoadDate\":\"2023-04-07 03:48:33.102516\",\"T5A\":\"88.07819277858451\",\"T1B\":\"0.0\",\"T3B_RW\":\"0.0\",\"Customer\":\"TOWNCENTER ESTATES\",\"T1A\":\"488821.67443063663\",\"T10A\":\"0.0\",\"T8A_SW\":\"0.0\",\"T3A_ES\":\"0.0\",\"TotalCon\":\"0.0\",\"StartDate\":\"2020-01-03\",\"OBJECTID\":\"1\",\"T4B_RW\":\"0.0\",\"T4A_ES\":\"0.0\",\"T6A\":\"0.0\",\"the_geom\":\"POLYGON((-117.69054293718133 34.05514927128467,-117.69054810805318 34.055084712338726,-117.69054373121776 34.05508482759084,-117.69053328963837 34.05445584470063,-117.69053326305799 34.0544529775152,-117.6905311392104 34.05422569835754,-117.6905300148864 34.05410536811477,-117.69052949236827 34.054049445730385,-117.69052926982586 34.054025628150946,-117.69052781651162 34.05402564016199,-117.69039895999003 34.05402671627058,-117.6903989301463 34.054026736405525,-117.69031950179675 34.054027874217496,-117.69031835619721 34.05402699793476,-117.69021579677981 34.05402530943276,-117.69020687597278 34.05306409213981,-117.69020640523364 34.05306325839703,-117.69020309413226 34.052817686309254,-117.69020351718096 34.05281689233456,-117.69020212780511 34.052687901786236,-117.69011577230023 34.0526880666321,-117.69011522976612 34.05231338526513,-117.69010526618212 34.05229976790027,-117.69011018282903 34.05227688994315,-117.69011059723272 34.0522749616549,-117.69011110379775 34.05227495338258,-117.69011308936724 34.052274920957835,-117.69058172144948 34.0522672671334,-117.69091005176637 34.05226050901948,-117.69095186764977 34.0522479415167,-117.69100493843096 34.05224422648261,-117.69107627925821 34.05224567279371,-117.69111388632571 34.052258891923316,-117.6912509550881 34.05225687960215,-117.69131572814872 34.052246693550735,-117.6913591173969 34.052259072153035,-117.69142080477285 34.052275654064644,-117.69143199455023 34.05273918828002,-117.69141149148638 34.053046367286676,-117.69150009917469 34.0531439884511,-117.69155207524582 34.05316412508724,-117.69159530580245 34.05317108404375,-117.69159316273122 34.05317270323805,-117.69159549262174 34.053337428155025,-117.6915977684511 34.053337288602485,-117.69159631127883 34.05334935724808,-117.69159514313738 34.05349009145252,-117.69160059218353 34.053507563815366,-117.69159789909394 34.05350760835045,-117.69159855227221 34.053553788247704,-117.6915986798219 34.053553786009445,-117.69159832200934 34.0535578751452,-117.69159847388697 34.05373750814799,-117.69160047229023 34.0539380467031,-117.6916080770135 34.05395088032112,-117.69160416678764 34.053950884531986,-117.69160669698846 34.0541298057529,-117.69160354914952 34.054135333635415,-117.69160583777904 34.054393163109424,-117.6916112819565 34.054398946321214,-117.6916105040973 34.054398967304294,-117.69161075823035 34.05441694353786,-117.69161124001704 34.05445102294635,-117.6916068953168 34.05445783489377,-117.6916179193624 34.0548203866274,-117.69161699656865 34.054848624575776,-117.69161686249429 34.054848627432875,-117.69162107936754 34.05514682313109,-117.69054478347957 34.05514821346264,-117.69054293718133 34.05514927128467))\",\"T2A\":\"870.9015589850761\",\"T4A_APR\":\"0.0\",\"I_SLA\":\"0.0\",\"T3B_CG\":\"0.0\",\"T3A_RW\":\"0.0\",\"T2A_PP\":\"0.0\",\"T4A_SLA\":\"0.0\",\"T4A_RW\":\"0.0\",\"T4B_CG\":\"0.0\",\"T4B_APR\":\"0.0\",\"NI\":\"489716.88803259307\",\"T4B_SLA\":\"0.0\",\"T7A\":\"0.0\",\"T3B\":\"84927.03462431033\",\"T3A\":\"52643.868306384415\",\"T3A_CG\":\"0.0\",\"INI\":\"2929.4062926401903\",\"I\":\"138441.8044896798\",\"CorrArea\":\"631088.0988149131\",\"Source\":\"shapefiles-batch-2023-04-07-03-48-21/Premise_Bounds.dbf\",\"T4A_CG\":\"0.0\",\"premID\":\"Mnte114\",\"T3A_APR\":\"0.0\",\"T2A_RW\":\"0.0\",\"T3A_SLA\":\"0.0\",\"T8A\":\"0.0\",\"ImagYear\":\"2020\",\"T4A\":\"1771.5398921503531\",\"T4B\":\"1157.8664004898374\",\"T3B_APR\":\"0.0\",\"T10A_SLA\":\"0.0\",\"T8A_RW\":\"0.0\",\"EndDate\":\"9999-12-31\",\"T3B_SLA\":\"0.0\",\"T3B_ES\":\"0.0\",\"Shape_Leng\":\"0.0\",\"DistName\":\"MONTEVISTACO226\",\"T2A_SLA\":\"0.0\",\"Shape_Area\":\"0.0\",\"T4B_ES\":\"0.0\",\"T9A\":\"807.1354091778849\",\"INI_SLA\":\"0.0\"}\n"
				+ "]";
//				String landscapedatacsvjsonString = new String(Files.readAllBytes(Paths.get(landscapedatacsv)));
		JSONArray landscapedatacsvjsonArray = new JSONArray(landscapedatacsvjsonString);
		for (int i = 0; i < landscapedatacsvjsonArray.length(); i++) {
			JSONObject obj = new JSONObject();
			System.out.println("-------------------------" + i);
			JSONObject jsonObject = landscapedatacsvjsonArray.getJSONObject(i);
			System.out.println(jsonObject);
			String the_geom = jsonObject.getString("the_geom");
//			String I_SLA = jsonObject.getString("I_SLA");
//			String I = jsonObject.getString("I");
//			String premID = jsonObject.getString("premID");

			WKTReader reader = new WKTReader();
			Geometry geometry = reader.read(the_geom);
			geometry = ShatamGeometry.validate(geometry);
//            System.out.println(the_geom+I_SLA+I+premID);

			String latlon[] = geometry.getCentroid().toString().replace("POINT (", "").replace(")", "").split(" ");
			DecimalFormat df = new DecimalFormat("####0.00");
			String lon = df.format(Double.parseDouble(latlon[0]));
			String lat = df.format(Double.parseDouble(latlon[1]));

//			System.out.println(lat + " " + lon);
//			double latitude = Double.parseDouble(lat);
//			double longitude = Double.parseDouble(lon);

			String etoUrl = "https://et.water.ca.gov/api/data?appKey=08807bb2-ce9c-4b65-af3c-dec10f6f3daf&targets=lat%3D"
					+ lat + ",lng%3D" + lon
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
//			System.out.println("premise_bounds LatLon "+ lat+", "+lon);
			obj.put("premise_bounds LatLon ", lat + ", " + lon);
			obj.put("premise_bounds avgEto", avgEto);
			U.log("premise_bounds LatLon " + lat + ", " + lon + " |  avgEto " + avgEto);
			U.log("premise_bounds LatLon " + lon + " " + lat + " |  avgEto " + avgEto);
//		String  DistanceCalculator = DistanceCalculator(latitude , longitude);

			double sourceLat = Double.parseDouble(lat);
			double sourceLon = Double.parseDouble(lon);
			double lowestdistance = 10000;
			double newdestLat = 00;
			double newdestLon = 00;
			String Met = "";
			HashMap<Double, String> hs = new HashMap<>();

			try {
				// To Find Three smallest distances Of Station
				BufferedReader reader1 = new BufferedReader(
						new FileReader("/home/shatam-100/CIMISWeatherStationNetwork/StationData (another copy).csv"));
				String line;

				List<Double> distanceList = new ArrayList<>(); // Create a new List<Double>

				while ((line = reader1.readLine()) != null) {
					String[] parts = line.split(",");
					double destLat = Double.parseDouble(parts[0]);
					double destLon = Double.parseDouble(parts[1]);
					double destMet = Double.parseDouble(parts[2]);
					String Meter = parts[2];
//					System.out.println("Meter "+parts[2]+parts[0]+parts[1]);
//				System.out.println(destLat + "  " + destLon);

					double distance = calculateDistance(sourceLat, sourceLon, destLat, destLon);
//					System.out.println("Meter "+parts[2]+" | "+parts[0]+parts[1]+" | Distance: " + distance + " km");
					distanceList.add(distance);
					hs.put(distance, "Meter "+parts[2]+" | "+parts[1]+" "+parts[0]+" | Distance: " + distance + " km");

//					distances.a
//					printThreeSmallestDistances(distance);
					if (lowestdistance > distance) {
						lowestdistance = distance;
						Met = Meter;
						System.out.println();
						newdestLat = destLat;
						newdestLon = destLon;
						System.out.println("Met "+Met +" | distance "+distance+"  | newdestLatLon" + newdestLon + " " + newdestLat);
					}
				}
				Object[] distances = distanceList.toArray();
				Arrays.sort(distances); // Sort the distances in ascending order

				System.out.println("Three smallest distances:");
				for (int i1 = 0; i1 < 3; i1++) {
//					System.out.println(distances[i1]);
					System.out.println("------------------------------------------------");
					System.out.println("smallest distances : "+hs.get(distances[i1]));
					String metercount = U.getSectionValue(hs.get(distances[i1]), "Meter", "|").trim();
					int K=Integer.parseInt(metercount); 
					System.out.println(K);
//					return;
					CIMISWeather.processStation(K);
				}
				
				obj.put("premise_bounds lowestdistance", lowestdistance);
				obj.put("premise_bounds newdestLatLonOFStation", newdestLat + ", " + newdestLon);
				obj.put("Staion Number", Met);

				System.out.println("Nearest metro station - Distance: " + lowestdistance + " km" + "  | LatLon "
						+ newdestLat + ", " + newdestLon);
				reader1.close();
			} catch (IOException e) {e.printStackTrace();}
			
//			try {
//				
//				//To Calculate avgEto Value Of Premise From LatLong
//				DecimalFormat dff = new DecimalFormat("####0.00");
//				String etoUrll = "https://et.water.ca.gov/api/data?appKey=08807bb2-ce9c-4b65-af3c-dec10f6f3daf&targets=lat%3D"
//						+ dff.format(newdestLat) + ",lng%3D" + dff.format(newdestLon)
//						+ "&startDate=2010-08-01&endDate=2010-10-01&dataItems=day-asce-eto&unitOfMeasure=E";
//				U.downloadUsingStream(etoUrll, U.getCache(etoUrll));
//				String etoUrlCachee = U.getCache(etoUrll);
//				String etoFilee = FileUtil.readAllText(etoUrlCachee);
//				String etoValss[] = U.getValues(etoFilee, "\"DayAsceEto\":{\"Value\":\"", "\"");
//				double avgEto0 = 0.0;
//				double sumEto0 = 0.0;
//				for (String eto : etoValss) {
//					sumEto0 += Double.parseDouble(eto);
//				}
//				avgEto0 = sumEto0 / etoValss.length;
//				U.log("AvgEto From this station " + avgEto0);
//				obj.put("avgEtoValueOfStation", avgEto0);
//			} catch (Exception e) {}
			System.out.println(landscapedatacsvjsonArray.length());

			jsonObj.add(obj);
		}

//		FileUtil.writeAllText("/home/shatam-100/CIMISWeatherStationNetwork/PremiseEtoValueComapreToSatation.txt", jsonObj.toString());

	}

	private static void printThreeSmallestDistances(double[] distance) {
		// TODO Auto-generated method stub
		if (distance.length < 3) {
			System.out.println("Insufficient distances provided.");
			return;
		}

		Arrays.sort(distance); // Sort the distances in ascending order

		System.out.println("Three smallest distances:");
		for (int i = 0; i < 2; i++) {
			System.out.println(""+distance[i]);
		}
	}

	private static double calculateDistance(double sourceLat, double sourceLon, double destLat, double destLon) {
		double latDistance = Math.toRadians(destLat - sourceLat);
		double lonDistance = Math.toRadians(destLon - sourceLon);

		double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(sourceLat))
				* Math.cos(Math.toRadians(destLat)) * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		return EARTH_RADIUS * c;
	}
}
