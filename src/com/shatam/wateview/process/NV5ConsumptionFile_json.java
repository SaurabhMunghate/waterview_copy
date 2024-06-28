package com.shatam.wateview.process;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.shatam.utils.FileUtil;
import com.shatam.utils.U;
import com.shatam.waterview.geotools.ShatamGeometry;

public class NV5ConsumptionFile_json {
//	static String consumptionFileName = "/home/shatam-100/Down/WaterView_Data/MONTEVISTACO226_1/MONTEVISTACO226/NEW_prd.monthlyconsumption.json";
//	static String meterDetailsFileName = "/home/shatam-100/Down/WaterView_Data/MONTEVISTACO226_1/MONTEVISTACO226/NEW_prd.meter_locations.json";
//	//use Premise_Bounds_converted this file
//	static String landscapedatacsv = "/home/shatam-100/Down/WaterView_Data/MONTEVISTACO226_1/MONTEVISTACO226/NEW_prd.premise_bounds.json";
//	/home/shatam-100/Down/WaterView_Data/Jarupa_Consupmtion-2021-2022.csv
//	/home/shatam-100/Down/WaterView_Data/Meter_Locations_convertedData.csv
//	/home/shatam-100/Down/WaterView_Data/Premise_Bounds_convertedData.csv

	static String consumptionFileName =  "/home/shatam-100/Down/WaterView_Data/montevista_17_May_240/MONTEVISTACO226/prd.monthlyconsumption.json";
	static String meterDetailsFileName = "/home/shatam-100/Down/WaterView_Data/montevista_17_May_240/MONTEVISTACO226/prd.meter_locations.json";
	static String landscapedatacsv =     "/home/shatam-100/Cache/NEW_prd.premise_bounds (1).json";

	public static void main(String[] args) throws Exception {
		new NV5ConsumptionFile_json().processConsumptionFile(consumptionFileName);
	}

	@SuppressWarnings("unchecked")
	private void processConsumptionFile(String inpfileName) throws Exception {
		// TODO Auto-generated method stub

//		FileUtil.readJsonFile(consumptionFileName);
//		FileUtil.readJsonFile(meterDetailsFileName);
//		FileUtil.readJsonFile(landscapedatacsv);
		HashMap<String, String> meterMap = new HashMap<>();
		HashMap<String, String[]> prmiseMap = new HashMap<>();
//		try {
		// Read the contents of the JSON file as a string
		String meterDetailsjsonString = new String(Files.readAllBytes(Paths.get(meterDetailsFileName)));

		// Create a JSON array from the string
		JSONArray meterDetailsjsonArray = new JSONArray(meterDetailsjsonString);

		for (int i = 0; i < meterDetailsjsonArray.length(); i++) {
			JSONObject jsonObject = meterDetailsjsonArray.getJSONObject(i);

			String premID = jsonObject.getString("premID");
			String meterID = jsonObject.getString("meterID");
			meterMap.put(meterID, premID);
//                System.out.println(premID + meterID);
//				break;
//				continue;
		}
//		System.out.println(meterMap.size());
//		} catch (JSONException e) {
////            System.err.println("Error while parsing JSON: " + e.getMessage());
//		}

		String landscapedatacsvjsonString = new String(Files.readAllBytes(Paths.get(landscapedatacsv)));
		JSONArray landscapedatacsvjsonArray = new JSONArray(landscapedatacsvjsonString);
		for (int i = 0; i < landscapedatacsvjsonArray.length(); i++) {
			JSONObject jsonObject = landscapedatacsvjsonArray.getJSONObject(i);
			
			
//			Double T2A = jsonObject.getDouble("T2A");
//			Double T3A = jsonObject.getDouble("T3A");
//			Double T3B = jsonObject.getDouble("T3B");
//			Double T7A = jsonObject.getDouble("T7A");
			
//			Double I = T2A+T3A+T3B+T7A;
//			System.out.println(T2A+" + "+T3A+" + "+T3B+" + "+T7A);
//			System.out.println(I);
			
//			Double T2A_PP = jsonObject.getDouble("T2A_PP");
//			Double T2A_RW = jsonObject.getDouble("T2A_RW");
//			Double T3A_APR = jsonObject.getDouble("T3A_APR");
//			Double T3A_CG = jsonObject.getDouble("T3A_CG");
//			Double T3A_RW = jsonObject.getDouble("T3A_RW");
//			Double T3A_ES = jsonObject.getDouble("T3A_ES");
//			Double T3B_APR= jsonObject.getDouble("T3B_APR");
//			Double T3B_CG = jsonObject.getDouble("T3B_CG");
//			Double T3B_RW = jsonObject.getDouble("T3B_RW");
//			Double T3B_ES = jsonObject.getDouble("T3B_ES");
//			T7A = jsonObject.getDouble("T7A"); 
//			Double T8A_RW = jsonObject.getDouble("T8A_RW");
//			Double T8A_SW = jsonObject.getDouble("T8A_SW");
			
//			Double I_SLA = T2A_PP+T2A_RW+T3A_APR+T3A_CG+T3A_RW+T3A_ES+T3B_APR+T3B+T3B_CG+T3B_RW+T3B_ES+T7A+T8A_RW+T8A_SW;
//			System.out.println(T2A_PP+"+"+T2A_RW+"+"+T3A_APR+"+"+T3A_CG+"+"+T3A_RW+"+"+T3A_ES+"+"+T3B+"+"+T3B_CG+"+"+T3B_RW+"+"+T3B_ES+"+"+T8A_RW+"+"+T8A_SW);
//			System.out.println(I_SLA);
			
			
			String the_geom = jsonObject.getString("the_geom");
			String I_SLA = jsonObject.getString("I_SLA");
			String I = jsonObject.getString("I");
			String premID = jsonObject.getString("premID");

			WKTReader reader = new WKTReader();
			Geometry geometry = reader.read(the_geom);
			geometry = ShatamGeometry.validate(geometry);
//            System.out.println(the_geom+I_SLA+I+premID);

			String latlon[] = geometry.getCentroid().toString().replace("POINT (", "").replace(")", "").split(" ");
			String[] outData = { latlon[1], latlon[0], I_SLA.toString(), I.toString(), jsonObject.toString() };
			prmiseMap.put(premID, outData);
//			System.out.println(premID+ "  |  " +I_SLA+" | "+ I+" | "+ latlon[1]+ latlon[0]+  jsonObject.toString() );

//			break;
		}
//		System.out.println(prmiseMap.size());

//		File input = new File(inpfileName);
		String inpfilename = new String(Files.readAllBytes(Paths.get(inpfileName)));
		JSONArray consumptionjsonArray = new JSONArray(inpfilename);
		List<JSONObject> jsonObj = new ArrayList<JSONObject>();

//		try {
//		for (int i = 0; i < consumptionjsonArray.length(); i++) {
		for (int i = 0; i < 10; i++) {
			JSONObject jsonObject = consumptionjsonArray.getJSONObject(i);
//			System.out.println(jsonObject.toString());
			System.out.println(i);
//			if(i==511)continue;

			SimpleDateFormat fromUser = new SimpleDateFormat("MM/dd/yyyy");
			SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
			JSONObject obj = new JSONObject();

			String district_id = jsonObject.getString("districtId");
			String meter_uid = jsonObject.getString("id");
			String reading_date = jsonObject.getString("Date");
//			String the_geom = jsonObject.getString("the_geom");
			String usage_ccf = jsonObject.getString("consumption");

			obj.put("districtId", district_id);
			obj.put("id", meter_uid);

//			String reformattedStr = myFormat.format(fromUser.parse(reading_date.toString()));

//			obj.put("Date", reading_date); //2023-01-30
			obj.put("Date", "2023-01-30"); //
			obj.put("consumption", usage_ccf);
			obj.put("premid", meterMap.get(meter_uid));

//			System.out.println(district_id+meter_uid+usage_ccf+meter_uid);
//			System.out.println(meterMap);

			String dataArr[] = prmiseMap.get(meterMap.get(meter_uid));

			if(dataArr==null) {
//				U.log("meter_uid "+meter_uid+" dataArr is null");
				continue;
			}
			System.out.println(dataArr);

			Calendar calendar = Calendar.getInstance();
//			calendar.setTime(fromUser.parse(reading_date));
			calendar.add(Calendar.MONTH, 1);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			calendar.add(Calendar.DATE, -1);

			Date lastDayOfMonth = calendar.getTime();

			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//			if (meterMap.get(meter_uid) == null)
//				continue;
			DecimalFormat df = new DecimalFormat("####0.00");
//			U.log(Arrays.toString(dataArr));
//			U.log(reading_date);
			String readingdate = reading_date;// sdf.format(fromUser.parse(reading_date));
			String lastDay = sdf.format(lastDayOfMonth);
			lastDay = "2020-09-30";
			System.out.println(dataArr[0]+" "+dataArr[1]);
			String etoUrl = "https://et.water.ca.gov/api/data?appKey=08807bb2-ce9c-4b65-af3c-dec10f6f3daf&targets=lat%3D"
					+ df.format(Double.parseDouble(dataArr[0])) + ",lng%3D" + df.format(Double.parseDouble(dataArr[1]))
					+ "&startDate=" + "2010-08-01" + "&endDate=" + "2010-10-01"
					+ "&dataItems=day-asce-eto&unitOfMeasure=E";

			U.log("etoUrl ::"+etoUrl);
			U.downloadUsingStream(etoUrl, U.getCache(etoUrl));
			String etoUrlCache = U.getCache(etoUrl);
			U.log("etoUrlCache "+etoUrlCache);
			String etoFile = FileUtil.readAllText(etoUrlCache);
			String etoVals[] = U.getValues(etoFile, "\"DayAsceEto\":{\"Value\":\"", "\"");
			double avgEto = 0.0;
			double sumEto = 0.0;
			for (String eto : etoVals) {
				sumEto += Double.parseDouble(eto);
//				U.log("sumEto "+sumEto);
			}
			avgEto = sumEto / etoVals.length;
			U.log("avgEto "+avgEto);

//			U.log("avgEto "+avgEto);
//			for (String eto : etoVals) {
//				sumEto += Double.parseDouble(eto);
////				U.log("sumEto1 "+sumEto);
//			}
//			avgEto = sumEto / etoVals.length;
//			U.log("avgEto1 "+avgEto);

//			U.log(etoVals.length);
//			U.log(avgEto);
			String zip = U.getSectionValue(etoFile, "\"ZipCodes\":\"", "\"");
			readingdate = reading_date; // sdf.format(fromUser.parse(reading_date));
			U.log("zip " + zip);
			String precipUrl = "https://et.water.ca.gov/api/data?appKey=08807bb2-ce9c-4b65-af3c-dec10f6f3daf&targets="
					+ zip.trim() + "&startDate=" + "2010-08-01" + "&endDate=" + "2010-10-01"
					+ "&dataItems=day-precip&unitOfMeasure=E";
//			U.log("precipUrl :: "+precipUrl);
			U.downloadUsingStream(precipUrl, U.getCache(precipUrl));
			String precipUrlCache = U.getCache(precipUrl);
//			U.log("precipUrlCache :: " + precipUrlCache);
			String precipFile = FileUtil.readAllText(precipUrlCache);
			String precipVals[] = U.getValues(precipFile, "\"DayPrecip\":{\"Value\":\"", "\"");
			double avgprecip = 0.0;
			double sumprecip = 0.0;

//			U.log("precipVals.length "+precipVals.length);
			if (precipVals.length > 0) {
				for (String precip : precipVals) {
					sumprecip += Double.parseDouble(precip);
//					U.log("sumprecip "+sumprecip);
				}
				avgprecip = sumprecip / precipVals.length;
			}
			U.log("avgprecip : "+avgprecip);
//			U.log(sumprecip);
//			U.log(avgEto);
//			U.log("avgprecip  " + avgprecip);
//			U.log(dataArr[3]);
//			U.log(avgEto +" + "+  avgprecip);
//			U.log(dataArr[2]);
			double allocation = (0.62) * ((Double.parseDouble(dataArr[3]) * 0.8 * (avgEto - avgprecip))+ (1.0 * Double.parseDouble(dataArr[2]) * avgEto));
//			double allocation=(0.62)*((Double.parseDouble(dataArr[3])*0.8*(avgEto-avgprecip))+(1.0*Double.parseDouble(dataArr[2])*avgEto));
//			double allocation = (0.62) * ((Double.parseDouble(dataArr[3]) * 0.8 * (avgEto - avgprecip))+ (1.0 * Double.parseDouble(dataArr[2]) * avgEto));
			// U.log(Double.parseDouble(dataArr[3])*0.8*(avgEto-avgprecip));
//			U.log(dataArr[3]);// Check I Value in Premise Bound
//			U.log(avgEto);
//			U.log(avgprecip);//Its is negative No need to update
//			U.log(dataArr[2]);// Check I_SLA Value in Premise Bound
//			U.log("Allocation " + allocation);
//			U.log("Usage||| " + data.get("usage_ccf"));

			obj.put("allocation", "" + Math.abs(allocation));
			obj.put("Lat_Long", "" + dataArr[0]+","+dataArr[1]);
//			obj.put("Long", "" + );

			obj.put("avgEto", "" + avgEto);
			obj.put("avgprecip", "" + avgprecip);
			obj.put("zip", "" + zip.trim());

			// if(allocation< 0)
			// throw new Exception();
			// (0.62) * { [LACII x 0.8 x (ETo – Peff)] + (1.0 x SLA x ETo) + (LAMWELO x 0.45
			// x ETo)} + Exempt Water Use Volume
			// String precip;
			// JsonParser parser = new JsonParser();
//				      JsonObject json = (JSONObject) parser.parse(etoFile);  
//				      U.log(json.get("Data"));
			jsonObj.add(obj);
//			break;
		}
//		} catch (Exception e) {
//			System.out.println("+++++++++++++++++++==");
//			// TODO: handle exception
//		}
		ObjectMapper objectMapper = new ObjectMapper();
//		U.log(inpfileName.replace(".csv", "_json.json")); 
//		U.log("/home/shatam-100/Down/WaterView_Data/consumption.txt");

//		FileUtil.writeAllText("/home/shatam-100/Down/WaterView_Data/jurupa_17_May_240/JURUPACOMMUN177/consumption.json", jsonObj.toString());
//		FileUtil.writeAllText(inpfileName.replace(".csv", "_json.txt"), jsonObj.toString());
//		FileUtil.writeAllText(inpfileName.replace(".csv", "_json.json"), jsonObj.toString());
		FileUtil.writeAllText("/home/shatam-100/Cache/consumption.json", jsonObj.toString());
		System.out.println("Json File Created Successfully");

		System.out.println("++++++++++++++++++++++++++DONE+++++++++++++++++++++++++++++");
	}
}