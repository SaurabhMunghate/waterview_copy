package com.shatam.wateview.process;

import java.io.File;
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

import org.json.JSONObject;
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

public class NV5ConsumptionFile_csv {
//	static String consumptionFileName = "/home/shatam-100/CODE Repository/Jurupa_11_29_2022_withCache/Jurupa_11_29_2022/Consumption/Jarupa_Consupmtion-2021-2022.csv";
//	static String meterDetailsFileName = "/home/shatam-100/CODE Repository/Jurupa-2022-12-21/WaterView/shapefiles/csv_files_frmshp/Meter_Locations_convertedData.csv";
//	//use Premise_Bounds_converted this file
//	static String landscapedatacsv = "/home/shatam-100/CODE Repository/Jurupa_11_29_2022_withCache/Jurupa_11_29_2022/shapeFiles/Premise_Bounds_convertedData.csv";

	  static String consumptionFileName =  "/home/shatam-100/Down/WaterView_Data/Jarupa_Consupmtion-2021-2022.csv";
	  static String meterDetailsFileName = "/home/shatam-100/Down/WaterView_Data/Meter_Locations_convertedData.csv";
	  static String landscapedatacsv =     "/home/shatam-100/Down/WaterView_Data/Premise_Bounds_convertedData.csv";
	public static void main(String[] args) {
		new NV5ConsumptionFile_csv().processConsumptionFile(consumptionFileName);
	}

	@SuppressWarnings("unchecked")
	private void processConsumptionFile(String inpfileName) {
		// TODO Auto-generated method stub
		HashMap<String, String> meterMap = new HashMap<>();
		HashMap<String, String[]> prmiseMap = new HashMap<>();
		List<String[]> meterData = FileUtil.readCsvFile(meterDetailsFileName);
		for (String[] meters : meterData) {
			if (meters[0].contains("the_geom"))
				continue;
			meterMap.put(meters[3], meters[2]);
//			System.out.println("meters[3], meters[2]"+meters[3]+ meters[2]);
		}
		try {
			File landscapedatacsvinput = new File(landscapedatacsv);
			CsvSchema csv = CsvSchema.emptySchema().withHeader();
			CsvMapper csvMapper = new CsvMapper();
			MappingIterator<Map<?, ?>> mappingIterator = csvMapper.reader().forType(Map.class).with(csv)
					.readValues(landscapedatacsvinput);

			List<Map<?, ?>> list = mappingIterator.readAll();
			for (Map<?, ?> data : list) {
				JSONObject obj = new JSONObject(data);
				WKTReader reader = new WKTReader();
				try {
					Geometry geom = reader.read(obj.get("the_geom").toString());
					geom = ShatamGeometry.validate(geom);
					// U.log(obj.toString());
					String latlon[] = geom.getCentroid().toString().replace("POINT (", "").replace(")", "").split(" ");
					String[] outData = { latlon[1], latlon[0], obj.get("I_SLA").toString(), obj.get("I").toString(),
							obj.toString() };
					prmiseMap.put(obj.get("premID").toString(), outData);
//					U.log(obj.get("premID").toString());
//					 break;
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		File input = new File(inpfileName);
		try {
			CsvSchema csv = CsvSchema.emptySchema().withHeader();
			CsvMapper csvMapper = new CsvMapper();
			MappingIterator<Map<?, ?>> mappingIterator = csvMapper.reader().forType(Map.class).with(csv)
					.readValues(input);
			List<Map<?, ?>> list = mappingIterator.readAll();
			List<JSONObject> jsonObj = new ArrayList<JSONObject>();
			int count=0;
			for (Map<?, ?> data : list) {
				count++;
				//for range count run 
//				if(count<5000|| count>6000) {
//					continue;
//				}
//				U.log("count :: "+count);
				SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd");
				//SimpleDateFormat fromUser = new SimpleDateFormat("MM/dd/yyyy");
				SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
				JSONObject obj = new JSONObject();
				obj.put("districtId", data.get("district_id"));
				obj.put("id", data.get("meter_uid"));
				String reformattedStr = myFormat.format(fromUser.parse(data.get("read_date").toString()));
//				String reformattedStr = myFormat.format(fromUser.parse(data.get("reading_date").toString()));
				obj.put("Date", reformattedStr);
				obj.put("consumption", data.get("usage_ccf"));
				obj.put("premid", meterMap.get(data.get("meter_uid")));
				//U.log(obj.toString());
				String dataArr[] = prmiseMap.get(meterMap.get(data.get("meter_uid")));
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(fromUser.parse(data.get("read_date").toString()));
//				calendar.setTime(fromUser.parse(data.get("reading_date").toString()));
				calendar.add(Calendar.MONTH, 1);
				calendar.set(Calendar.DAY_OF_MONTH, 1);
				calendar.add(Calendar.DATE, -1);
				Date lastDayOfMonth = calendar.getTime();
				DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				
				if (meterMap.get(data.get("meter_uid")) == null) {
//					U.log(data.get("meter_uid"));
					continue;
				}
				
				DecimalFormat df = new DecimalFormat("####0.00");
//				U.log(Arrays.toString(dataArr));
				String etoUrl = "https://et.water.ca.gov/api/data?appKey=08807bb2-ce9c-4b65-af3c-dec10f6f3daf&targets=lat%3D"
						+ df.format(Double.parseDouble(dataArr[0])) + ",lng%3D"
						+ df.format(Double.parseDouble(dataArr[1])) + "&startDate="
						+ sdf.format(fromUser.parse(data.get("read_date").toString())) + "&endDate="
						+ sdf.format(lastDayOfMonth) + "&dataItems=day-asce-eto&unitOfMeasure=E";
				 U.log("etoUrl :: "+etoUrl);
				U.downloadUsingStream(etoUrl, U.getCache(etoUrl));
				String etoFile = FileUtil.readAllText(U.getCache(etoUrl));
				String etoVals[] = U.getValues(etoFile, "\"DayAsceEto\":{\"Value\":\"", "\"");
				double avgEto = 0.0;
				double sumEto = 0.0;
				for (String eto : etoVals) {
					sumEto += Double.parseDouble(eto);
				}
				avgEto = sumEto / etoVals.length;

				 U.log(etoVals.length);
//				U.log("avgEto ::"+avgEto);
				String zip = U.getSectionValue(etoFile, "\"ZipCodes\":\"", "\"");
				if(zip==null) continue;
				
				String precipUrl = "https://et.water.ca.gov/api/data?appKey=08807bb2-ce9c-4b65-af3c-dec10f6f3daf&targets="
						+ zip.trim() + "&startDate=" + sdf.format(fromUser.parse(data.get("read_date").toString()))
						+ "&endDate=" + sdf.format(lastDayOfMonth) + "&dataItems=day-precip&unitOfMeasure=E";
				U.log("precipUrl :: "+precipUrl);
				U.downloadUsingStream(precipUrl, U.getCache(precipUrl));
				String precipFile = FileUtil.readAllText(U.getCache(precipUrl));
				 U.log(precipFile);
				String precipVals[] = U.getValues(precipFile, "\"DayPrecip\":{\"Value\":\"", "\"");
				double avgprecip = 0.0;
				double sumprecip = 0.0;
				if (precipVals.length > 0) {
					for (String precip : precipVals) {
						sumprecip += Double.parseDouble(precip);
					}
					avgprecip = sumprecip / precipVals.length;
				}
				U.log(precipVals.length);
				U.log(sumprecip);
				U.log(avgEto);
				U.log("avgprecip  " + avgprecip);
				double allocation = (0.62) * ((Double.parseDouble(dataArr[3]) * 0.8 * (avgEto - avgprecip))+ (1.0 * Double.parseDouble(dataArr[2]) * avgEto));
				// U.log(Double.parseDouble(dataArr[3])*0.8*(avgEto-avgprecip));
				U.log("Allocation " + allocation);
				U.log("Usage||| " + data.get("usage_ccf"));

				obj.put("allocation", "" + Math.abs(allocation));
				// if(allocation< 0)
				// throw new Exception();
				// (0.62) * { [LACII x 0.8 x (ETo – Peff)] + (1.0 x SLA x ETo) + (LAMWELO x 0.45
				// x ETo)} + Exempt Water Use Volume
				// String precip;
				// JsonParser parser = new JsonParser();
//				        JsonObject json = (JSONObject) parser.parse(etoFile);  
//				        U.log(json.get("Data"));
				jsonObj.add(obj);
						break;
			}
			U.log(count);
			ObjectMapper objectMapper = new ObjectMapper();
			U.log("nv5consumptionfile :: " + inpfileName.replace(".csv", "_json.json"));
//			FileUtil.writeAllText(inpfileName.replace(".csv", "_json.txt"), jsonObj.toString());
//			FileUtil.writeAllText(inpfileName.replace(".csv", "_json.json"), jsonObj.toString());
			System.out.println("Json File Created Successfully");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}