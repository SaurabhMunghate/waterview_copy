package com.eto_consumption;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.regexp.recompile;
import org.json.JSONObject;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.opencsv.CSVWriter;
//import com.saurabh.DatePairExample.DatePair;
import com.shatam.utils.FileUtil;
import com.shatam.utils.U;
import com.vividsolutions.jts.geom.Geometry;

public class getEtoValueFormApi1 {
	static Map<String, String> etonotfoun = new HashMap<>();
	static long daysBetweenA = 0;
	static long AllTiles = 0;
	public static void main(String[] args) throws Exception {

		// 2024-08-01 ( July Month )

//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/monte_vista_14_Zoom_16.csv", "2024-07-01", "2024-07-31");//Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/Tile_San_Jose_Cover_145_Tiles.csv", "2024-07-01", "2024-07-31");//Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/All_Tiles_walnut_valley.csv", "2024-07-01", "2024-07-31");//Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/146_redwood_city_Tiles.csv", "2024-07-01", "2024-07-31");//Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries_TILES/western_14_Zoom_123_.csv", "2024-07-01", "2024-07-31");//Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/33_Carlsbad_Water_District_Tiles.csv", "2024-07-01", "2024-07-31");//Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries_TILES/las_virgenes__custom_Zoom_2.csv",   "2024-06-01", "2024-06-30");//Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/18_nipomo_Tiles.csv",   "2024-05-01", "2024-06-30");//Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/42_roseville_Water_District_Tiles.csv", "2024-07-01", "2024-07-31");//Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries_TILES/vacaville__14_Zoom_39.csv", "2024-07-01", "2024-07-31");//Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/36_banning_district_Tiles.csv",   "2024-05-01", "2024-06-30");//Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/7_bellflower_district_Tiles.csv", "2024-07-01", "2024-07-31");//Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/brentwood_boundary_18_All.csv", "2024-07-01", "2024-07-31");//Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/camarillo15_.csv", "2024-07-01", "2024-07-31");//Done
		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/camrosa40_.csv", "2022-01-01", "2022-12-31");//Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/east_valley36_.csv", "2024-07-01", "2024-07-31");//Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/escondido_final_14_Zoom_36.csv", "2024-07-01", "2024-07-31");//Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/fairfield44_.csv", "2024-07-01", "2024-07-31");//Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/folsom_boundary_38_All.csv", "2024-07-01", "2024-07-31");//Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/glendale35_.csv", "2024-07-01", "2024-07-31");//Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/indian_wells_35_14_zoom.csv", "2024-07-01", "2024-07-31");//Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/lake_arrowhead_custom_Zoom_1.csv", "2024-07-01", "2024-07-31");//Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries_TILES/modesto__14_Zoom_64.csv", "2024-07-01", "2024-07-31");//Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/montecito_boundary_21_.csv", "2024-07-01", "2024-07-31");//Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/myoma_dunes_boundary_4_.csv", "2024-07-01", "2024-07-31");//Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/orchard_dale_boundary_7_.csv", "2024-07-01", "2024-07-31");//Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/oxnard_boundary_53_.csv", "2024-07-01", "2024-07-31");
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/pomona_boundary_28_.csv", "2024-07-01", "2024-07-31");//Doen
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/rialto_boundary_30_.csv", "2024-07-01", "2024-07-31");//Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/District_Boundary_GeoJson/rincon_boundary (copy)_13Zoom_20_.csv", "2024-07-01", "2024-07-31");//Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/rowland_boundary_18_.csv", "2024-07-01", "2024-07-31");//Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/rubidoux_boundary_14_.csv", "2024-07-01", "2024-07-31");//Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/suisun_boundary_9_.csv", "2024-07-01", "2024-07-31");//Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/trabuco_canyon_boundary_21_.csv", "2024-07-01", "2024-07-31");//Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries_TILES/vallejo_8_Zoom_2.csv", "2024-07-01", "2024-07-31");
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/casitas_boundary_122_.csv", "2024-06-01", "2024-06-30");//Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/cal_los_angeles_12_Zoom_6.csv", "2024-07-01", "2024-07-31");
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/ontario_boundary_46_.csv", "2024-07-01", "2024-07-31");//Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/porterville_boundary_30_.csv", "2024-07-01", "2024-07-31"); //Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries_TILES/cal_sacramento__11_Zoom_6.csv", "2024-07-01", "2024-07-31");//Done 
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries_TILES/simi_valley_11_Zoom_4.csv", "2024-07-01", "2024-07-31");//Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/cal_ventura_12_Zoom_5.csv", "2024-07-01", "2024-07-31");//Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/san_gabriel_county_boundary_12Zoom_19_.csv", "2024-07-01", "2024-07-31");//Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/newman_boundary_12_.csv", "2024-07-01", "2024-07-31");//Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/santa_monica_boundary_12_.csv", "2024-07-01", "2024-07-31");//Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/beverly_hills_boundary_10_.csv", "2024-07-01", "2024-07-31");//Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/dixon_boundary_6_.csv", "2024-07-01", "2024-07-31");//Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/santa_barbara_boundary_13Zoom_21_.csv", "2024-07-01", "2024-07-31");//Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries_TILES/norco__14_Zoom_17.csv", "2024-07-01", "2024-07-31");//Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries_TILES/rancho__11_Zoom_4.csv", "2024-07-01", "2024-07-31");//Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries_TILES/san_clemente_9_Zoom_1.csv", "2024-07-01", "2024-07-31");//Done

		for (Map.Entry<String, String> entry : etonotfoun.entrySet()) {
            System.out.println("Key: " + entry.getKey());
		}
	}
    private static void getEtoForCsvTile(String listofTiles,String startYear,String endYear) throws Exception {
        String csvFilePath = listofTiles.replace(".csv", "_Eto_Precip_")+startYear+"_To_"+endYear+"_3_May_c1.csv";
//        String ExceptioncsvFile = listofTiles.replace(".csv", "_ExceptioncsvFile_")+startYear+"_To_"+endYear+"_14_March_c1.csv";
        
        System.out.println(listofTiles);
    	System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

    	System.out.println(csvFilePath);
        List<DatePair> datePairs = getDatePairsInRange(startYear, endYear, 89);

        System.out.println("Date Pairs within the range with a 90-d;ay difference:");
        CSVWriter writer = new CSVWriter(new FileWriter(csvFilePath));
//        CSVWriter exceptioncsvFile = new CSVWriter(new FileWriter(ExceptioncsvFile));

		writer.writeNext(new String[] { "SrId", "Tiles", "Date", "Perc", "ET" });

        for (DatePair datePair : datePairs) {
        	System.out.println("==================================================================================================================");
            System.out.println(datePair.getStart() + " to " + datePair.getEnd());
    		call(datePair.getStart(),  datePair.getEnd(), writer, listofTiles);
        }
        LocalDate date1 = LocalDate.parse(startYear);
        LocalDate date2 = LocalDate.parse(endYear);

        long daysBetween = ChronoUnit.DAYS.between(date1,date2 );
        daysBetweenA = daysBetween;
        // Output the number of days
        System.out.println("Number of days between " + startYear + " and " + endYear + " is " + daysBetween + " days.");

        System.out.println(AllTiles*daysBetweenA);
        System.out.println(csvFilePath);
        writer.close();
	}
	private static List<DatePair> getDatePairsInRange(String startDateStr, String endDateStr, int dayDifference) {
        List<DatePair> datePairs = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(startDateStr, formatter);
        LocalDate endDate = LocalDate.parse(endDateStr, formatter);

        while (startDate.plusDays(dayDifference).isBefore(endDate)) {
            LocalDate endDateOfPair = startDate.plusDays(dayDifference);
            datePairs.add(new DatePair(startDate.format(formatter), endDateOfPair.format(formatter)));
            startDate = endDateOfPair.plusDays(1);
        }

        // Add the remaining days as a separate pair if applicable
        if (!startDate.isAfter(endDate)) {
            datePairs.add(new DatePair(startDate.format(formatter), endDate.format(formatter)));
        }

        return datePairs;
    }

    private static class DatePair {
        private final String start;
        private final String end;

        public DatePair(String start, String end) {
            this.start = start;
            this.end = end;
        }

        public String getStart() {
            return start;
        }

        public String getEnd() {
            return end;
        }
    }
	private static void call(String dateString,String dateStringEnd,CSVWriter writer, String listofTiles) 
			throws Exception {

		File input = new File(listofTiles);
		int count = 0;
		try {
			CsvSchema csv = CsvSchema.emptySchema().withHeader();
			CsvMapper csvMapper = new CsvMapper();
			MappingIterator<Map<?, ?>> mappingIterator = csvMapper.reader().forType(Map.class).with(csv)
					.readValues(input);
			List<Map<?, ?>> list = mappingIterator.readAll();
			List<JSONObject> jsonObj = new ArrayList<JSONObject>();
			AllTiles = list.size();
			U.log("total records: " + list.size());
			int i = 0;
			for (Map<?, ?> data : list) {
//				U.log(i);
				if(data.get("Tiles").toString().contains("Tiles"))return;
				Calendar calendar = Calendar.getInstance();

				SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date parsedDate = inputFormat.parse(dateString);

				String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(parsedDate);

				calendar.setTime(parsedDate);
				calendar.add(Calendar.MONTH, 1);
				calendar.set(Calendar.DAY_OF_MONTH, 1);
				calendar.add(Calendar.DATE, -1);

				DecimalFormat df = new DecimalFormat("####0.00");
				Date lastDayOfMonth = calendar.getTime();
				DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

				String Tiles = data.get("Tiles").toString();
//				U.log("Tile :: " + data.get("Tiles").toString());
				if(Tiles==null)continue;
				Coordinate centroid ;
//				try {
					centroid = calculateCentroid(data.get("Tiles").toString());
//				} catch (Exception e) {
//					U.log(data.get("Tiles").toString());
//					// TODO: handle exception
//				}
//				System.out.println("Centroid Latitude: " + centroid.y);
//				System.out.println("Centroid Longitude: " + centroid.x);
				String lat = centroid.y + "";
				String lon = centroid.x + "";

				double Lat = Double.parseDouble(lat);
				double Lon = Double.parseDouble(lon);
//				U.log(dateString);
//				U.log(lastDayOfMonth);
//				System.out.println("lat : "+df.format(Lat) + " ,lng : " + df.format(Lon));

				String etoUrl = "https://et.water.ca.gov/api/data?appKey=53ce8f92-2d8d-4a02-8551-801575b1e678&targets=lat%3D"
						+ df.format(Lat) + ",lng%3D" + df.format(Lon)
						+ "&startDate=" + dateString + "&endDate=" + dateStringEnd
						+ "&dataItems=day-asce-eto&unitOfMeasure=E";

//				if(etoUrl.contains("34.12")&& etoUrl.contains("-119.17")) {
//					etoUrl = etoUrl.replace("34.12", "34.15").replace("-119.17", "-119.16");
//				}if(etoUrl.contains("34.21")&& etoUrl.contains("-119.26")) {
//					etoUrl = etoUrl.replace("-119.17", "-119.22");
//				}if(etoUrl.contains("34.21")&& etoUrl.contains("-119.26")) {
//					etoUrl = etoUrl.replace("34.21", "34.21").replace("-119.26", "-119.21");
//				}if(etoUrl.contains("34.08")&& etoUrl.contains("-119.06")) {
//					etoUrl = etoUrl.replace("34.08", "34.11").replace("-119.06", "-119.07");
//				}if(etoUrl.contains("34.19")&& etoUrl.contains("-119.26")) {
//					etoUrl = etoUrl.replace("34.19", "34.20").replace("-119.26", "-119.22");
//				}if(etoUrl.contains("34.15")&& etoUrl.contains("-119.23")) {
//					etoUrl = etoUrl.replace("34.15", "34.15").replace("-119.23", "-119.21");
//				}if(etoUrl.contains("34.13")&& etoUrl.contains("-119.19")) {
//					etoUrl = etoUrl.replace("34.13", "34.16").replace("-119.19", "-119.17");
//				}if(etoUrl.contains("33.13")&& etoUrl.contains("-117.34")) {
//					etoUrl = etoUrl.replace("33.13", "33.13").replace("-117.34", "-117.32");
//				}if(etoUrl.contains("33.17")&& etoUrl.contains("-117.37")) {
//					etoUrl = etoUrl.replace("33.17", "33.17").replace("-117.37", "-117.36");
//				}if(etoUrl.contains("33.09")&& etoUrl.contains("117.32")) {
//					etoUrl = etoUrl.replace("33.09", "33.10").replace("117.32", "117.29");
//				}
//				if(etoUrl.contains("34.21")&& etoUrl.contains("117.32")) {
//					etoUrl = etoUrl.replace("34.21", "34.20").replace("117.32", "117.31");
//				}
//				if(etoUrl.contains("34.41")&& etoUrl.contains("119.63")) {
//					etoUrl = etoUrl.replace("34.41", "34.42").replace("119.63", "119.63");
//				}
//				if(etoUrl.contains("34.41")&& etoUrl.contains("119.59")) {
//					etoUrl = etoUrl.replace("34.41", "34.42").replace("119.59", "119.58");
//				}
//				if(etoUrl.contains("34.41")&& etoUrl.contains("119.56")) {
//					etoUrl = etoUrl.replace("34.41", "34.41").replace("119.56", "119.55");
//				}
//				if(etoUrl.contains("34.26")&& etoUrl.contains("119.28")) {
//					etoUrl = etoUrl.replace("34.26", "34.26").replace("119.28", "119.27");
//				}
//				if(etoUrl.contains("34.33")&& etoUrl.contains("119.41")) {
//					etoUrl = etoUrl.replace("34.33", "34.33").replace("119.41", "119.40");
//				}
				if(etoUrl.contains("lat%3D34.28,lng%3D-119.34")) {
					etoUrl = etoUrl.replace("lat%3D34.28,lng%3D-119.34", "lat%3D34.28,lng%3D-119.31");
				}if(etoUrl.contains("lat%3D34.35,lng%3D-119.43")) {
					etoUrl = etoUrl.replace("lat%3D34.35,lng%3D-119.43", "lat%3D34.35,lng%3D-119.41");
				}if(etoUrl.contains("lat%3D34.33,lng%3D-119.41")) {
					etoUrl = etoUrl.replace("lat%3D34.33,lng%3D-119.41", "lat%3D34.33,lng%3D-119.40");
				}
				if(etoUrl.contains("lat%3D34.37,lng%3D-119.48")) {
					etoUrl = etoUrl.replace("lat%3D34.37,lng%3D-119.48", "lat%3D34.37,lng%3D-119.47");
				}
				if(etoUrl.contains("lat%3D33.55,lng%3D-117.17")) {
					etoUrl = etoUrl.replace("lat%3D33.55,lng%3D-117.17", "lat%3D33.55,lng%3D-117.18");
				}if(etoUrl.contains("lat%3D33.55,lng%3D-117.19")) {
					etoUrl = etoUrl.replace("lat%3D33.55,lng%3D-117.19", "lat%3D33.55,lng%3D-117.18");
				}if(etoUrl.contains("lat%3D33.53,lng%3D-117.17")) {
					etoUrl = etoUrl.replace("lat%3D33.53,lng%3D-117.17", "lat%3D33.55,lng%3D-117.18");
				}if(etoUrl.contains("3D33.13,lng%3D-117.34")) {
					etoUrl = etoUrl.replace("3D33.13,lng%3D-117.34", "3D33.13,lng%3D-117.32");
				}if(etoUrl.contains("3D33.09,lng%3D-117.32")) {
					etoUrl = etoUrl.replace("3D33.09,lng%3D-117.32", "3D33.09,lng%3D-117.30");
				}

				
				if(etoUrl.contains("lat%3D34.40,lng%3D-119.77")) {
					etoUrl = etoUrl.replace("lat%3D34.40,lng%3D-119.77", "lat%3D34.40,lng%3D-119.73");
				}if(etoUrl.contains("lat%3D34.40,lng%3D-119.86")) {
					etoUrl = etoUrl.replace("lat%3D34.40,lng%3D-119.86", "lat%3D34.43,lng%3D-119.86");
				}
				
				if(etoUrl.contains("lat%3D38.62,lng%3D-121.20")) {
					etoUrl = etoUrl.replace("lat%3D38.62,lng%3D-121.20", "lat%3D38.63,lng%3D-121.21");
				}
				if(etoUrl.contains("3D38.20,lng%3D-121.55")) {
					etoUrl = etoUrl.replace("3D38.20,lng%3D-121.55", "3D38.48,lng%3D-121.55");
				}if(etoUrl.contains("lat%3D38.75,lng%3D-121.38")) {
					etoUrl = etoUrl.replace("lat%3D38.75,lng%3D-121.38", "lat%3D38.75,lng%3D-121.37");
				}
				
				if(etoUrl.contains("3D34.13,lng%3D-118.96")) {
					etoUrl = etoUrl.replace("3D34.13,lng%3D-118.96", "3D34.10,lng%3D-118.93");
				}if(etoUrl.contains("lat%3D34.20,lng%3D-118.96")) {
					etoUrl = etoUrl.replace("lat%3D34.20,lng%3D-118.96", "lat%3D34.21,lng%3D-118.95");
				}
				
				if(etoUrl.contains("lat%3D34.27,lng%3D-118.87")) {
					etoUrl = etoUrl.replace("lat%3D34.27,lng%3D-118.87", "lat%3D34.24,lng%3D-118.84");
				}if(etoUrl.contains("lat%3D34.27,lng%3D-119.05")) {
					etoUrl = etoUrl.replace("lat%3D34.27,lng%3D-119.05", "lat%3D34.24,lng%3D-119.04");
				}
				
				if(etoUrl.contains("lat%3D34.01,lng%3D-118.51")) {
					etoUrl = etoUrl.replace("lat%3D34.01,lng%3D-118.51", "lat%3D34.04,lng%3D-118.51");
				}if(etoUrl.contains("lat%3D33.99,lng%3D-118.49")) {
					etoUrl = etoUrl.replace("lat%3D33.99,lng%3D-118.49", "lat%3D33.99,lng%3D-118.47");
				}
				
				if(etoUrl.contains("lat%3D37.61,lng%3D-120.97")) {
					etoUrl = etoUrl.replace("lat%3D37.61,lng%3D-120.97", "lat%3D37.61,lng%3D-121.00");
				}if(etoUrl.contains("lat%3D37.59,lng%3D-121.01")) {
					etoUrl = etoUrl.replace("lat%3D37.59,lng%3D-121.01", "lat%3D37.61,lng%3D-121.00");
				}if(etoUrl.contains("lat%3D37.61,lng%3D-120.99")) {
					etoUrl = etoUrl.replace("lat%3D37.61,lng%3D-120.99", "lat%3D37.61,lng%3D-121.00");
				}if(etoUrl.contains("lat%3D37.59,lng%3D-121.04")) {
					etoUrl = etoUrl.replace("lat%3D37.59,lng%3D-121.04", "lat%3D37.61,lng%3D-121.00");
				}if(etoUrl.contains("lat%3D37.61,lng%3D-120.93")) {
					etoUrl = etoUrl.replace("lat%3D37.61,lng%3D-120.93", "lat%3D37.61,lng%3D-121.00");
				}if(etoUrl.contains("lat%3D37.59,lng%3D-120.99")) {
					etoUrl = etoUrl.replace("lat%3D37.59,lng%3D-120.99", "lat%3D37.61,lng%3D-121.00");
				}if(etoUrl.contains("lat%3D37.59,lng%3D-120.97")) {
					etoUrl = etoUrl.replace("lat%3D37.59,lng%3D-120.97", "lat%3D37.61,lng%3D-121.00");
				}if(etoUrl.contains("lat%3D37.61,lng%3D-121.00&startDate=2024-04-30")) {
					etoUrl = etoUrl.replace("lat%3D37.61,lng%3D-121.00&startDate=2024-04-30", "lat%3D37.61,lng%3D-121.01&startDate=2024-04-30");
				}if(etoUrl.contains("lat%3D37.61,lng%3D-121.00&startDate=2024-06-01")) {
					etoUrl = etoUrl.replace("lat%3D37.61,lng%3D-121.00&startDate=2024-06-01", "lat%3D37.61,lng%3D-121.01&startDate=2024-06-01");
				}
				
				if(etoUrl.contains("lat%3D34.17,lng%3D-118.65")) {
					etoUrl = etoUrl.replace("lat%3D34.17,lng%3D-118.65", "lat%3D34.17,lng%3D-118.64");
				}
				if(etoUrl.contains("=lat%3D33.43,lng%3D-117.77")) {
					etoUrl = etoUrl.replace("=lat%3D33.43,lng%3D-117.77", "=lat%3D33.44,lng%3D-117.24");
				}
				if(etoUrl.contains("lat%3D34.17,lng%3D-118.84")) {
					etoUrl = etoUrl.replace("lat%3D34.17,lng%3D-118.84", "lat%3D34.17,lng%3D-118.80");
				}
				if(etoUrl.contains("=lat%3D33.50,lng%3D-117.33")) {
					etoUrl = etoUrl.replace("=lat%3D33.50,lng%3D-117.33", "=lat%3D33.66,lng%3D-117.30");
				}if(etoUrl.contains("=lat%3D33.65,lng%3D-117.33")) {
					etoUrl = etoUrl.replace("=lat%3D33.65,lng%3D-117.33", "=lat%3D33.66,lng%3D-117.30");
				}if(etoUrl.contains("=lat%3D33.50,lng%3D-116.98")) {
					etoUrl = etoUrl.replace("=lat%3D33.50,lng%3D-116.98", "=lat%3D33.50,lng%3D-117.15");
				}if(etoUrl.contains("lat%3D33.50,lng%3D-117.16")) {
					etoUrl = etoUrl.replace("lat%3D33.50,lng%3D-117.16", "lat%3D33.50,lng%3D-117.15");
				}
				
				if(etoUrl.contains("lat%3D34.20,lng%3D-118.87")) {
					etoUrl = etoUrl.replace("lat%3D34.20,lng%3D-118.87", "lat%3D34.20,lng%3D-117.99");
				}
				
				if(etoUrl.contains("lat%3D38.27,lng%3D-123.05")) {
					etoUrl = etoUrl.replace("lat%3D38.27,lng%3D-123.05", "lat%3D33.15,lng%3D-117.04");
				}
				if(etoUrl.contains("lat%3D33.15,lng%3D-117.02")) {
					etoUrl = etoUrl.replace("lat%3D33.15,lng%3D-117.02", "lat%3D33.15,lng%3D-117.04");
				}if(etoUrl.contains("lat%3D33.18,lng%3D-117.08")) {
					etoUrl = etoUrl.replace("lat%3D33.18,lng%3D-117.08", "lat%3D33.17,lng%3D-117.08");
				}if(etoUrl.contains("lat%3D33.09,lng%3D-117.02")) {
					etoUrl = etoUrl.replace("lat%3D33.09,lng%3D-117.02", "lat%3D33.09,lng%3D-117.05");
				}if(etoUrl.contains("lat%3D33.09,lng%3D-117.04")) {
					etoUrl = etoUrl.replace("lat%3D33.09,lng%3D-117.04", "lat%3D33.09,lng%3D-117.05");
				}
				if(etoUrl.contains("lat%3D33.17,lng%3D-117.04")) {
					etoUrl = etoUrl.replace("lat%3D33.17,lng%3D-117.04", "lat%3D33.17,lng%3D-117.06");
				}if(etoUrl.contains("lat%3D33.17,lng%3D-117.10")) {
					etoUrl = etoUrl.replace("lat%3D33.17,lng%3D-117.10", "lat%3D33.17,lng%3D-117.06");
				}if(etoUrl.contains("=lat%3D33.11,lng%3D-117.04")) {
					etoUrl = etoUrl.replace("=lat%3D33.11,lng%3D-117.04", "=lat%3D33.17,lng%3D-117.06");
				}
				if(etoUrl.contains("=lat%3D34.27,lng%3D-117.20&")) {
					etoUrl = etoUrl.replace("=lat%3D34.27,lng%3D-117.20&", "=lat%3D34.24,lng%3D-117.21&");
				}
				if(etoUrl.contains("3D34.10,lng%3D-117.21")) {
					etoUrl = etoUrl.replace("3D34.10,lng%3D-117.21", "3D34.09,lng%3D-117.22");
				}
				if(etoUrl.contains("3D33.17,lng%3D-117.37")) {
					etoUrl = etoUrl.replace("3D33.17,lng%3D-117.37", "3D33.17,lng%3D-117.35");
				}
				if(etoUrl.contains("34.09,lng%3D-119.27")) {
					etoUrl = etoUrl.replace("34.09,lng%3D-119.27", "34.21,lng%3D-119.16");
				}if(etoUrl.contains("33.94,lng%3D-118.56")) {
					etoUrl = etoUrl.replace("33.94,lng%3D-118.56", "34.03,lng%3D-118.44");
				}if(etoUrl.contains("33.65,lng%3D-118.39")) {
					etoUrl = etoUrl.replace("33.65,lng%3D-118.39", "33.75,lng%3D-118.30");
				}if(etoUrl.contains("34.23,lng%3D-119.27")) {
					etoUrl = etoUrl.replace("34.23,lng%3D-119.27", "34.29,lng%3D-119.21");
				}if(etoUrl.contains("33.65,lng%3D-118.21")) {
					etoUrl = etoUrl.replace("33.65,lng%3D-118.21", "33.81,lng%3D-118.26");
				}if(etoUrl.contains("34.40,lng%3D-119.69")) {
					etoUrl = etoUrl.replace("34.40,lng%3D-119.69", "34.41,lng%3D-119.70");
				}if(etoUrl.contains("3D33.12,lng%3D-117.00")) {
					etoUrl = etoUrl.replace("3D33.12,lng%3D-117.00", "3D33.13,lng%3D-117.00");
				}if(etoUrl.contains("3D33.05,lng%3D-117.14")) {
					etoUrl = etoUrl.replace("3D33.05,lng%3D-117.14", "3D33.05,lng%3D-117.15");
				}if(etoUrl.contains("3D33.12,lng%3D-117.05")) {
					etoUrl = etoUrl.replace("3D33.12,lng%3D-117.05", "3D33.13,lng%3D-117.10");
				}if(etoUrl.contains("3D33.13,lng%3D-117.00")) {
					etoUrl = etoUrl.replace("3D33.13,lng%3D-117.00", "3D33.13,lng%3D-117.10");
				}if(etoUrl.contains("3D33.08,lng%3D-117.05")) {
					etoUrl = etoUrl.replace("3D33.08,lng%3D-117.05", "3D33.09,lng%3D-117.05");
				}if(etoUrl.contains("3D33.08,lng%3D-117.05")) {
					etoUrl = etoUrl.replace("3D33.08,lng%3D-117.05", "3D33.09,lng%3D-117.05");
				}if(etoUrl.contains("3D33.08,lng%3D-117.05")) {
					etoUrl = etoUrl.replace("3D33.08,lng%3D-117.05", "3D33.09,lng%3D-117.05");
				}if(etoUrl.contains("3D33.08,lng%3D-117.09")) {
					etoUrl = etoUrl.replace("3D33.08,lng%3D-117.09", "3D33.08,lng%3D-117.10");
				}if(etoUrl.contains("3D37.41,lng%3D-121.85&startDate=2022-12-16")) {
					etoUrl = etoUrl.replace("3D37.41,lng%3D-121.85&startDate=2022-12-16", "3D37.42,lng%3D-121.83&startDate=2022-12-16");
				}if(etoUrl.contains("lat%3D37.42,lng%3D-121.83&")) {
					etoUrl = etoUrl.replace("lat%3D37.42,lng%3D-121.83&", "lat%3D37.40,lng%3D-121.85&");
				}
				if(etoUrl.contains("37.24,lng%3D-121.87&startDate=2023-12-11")) {
					etoUrl = etoUrl.replace("37.24,lng%3D-121.87&startDate=2023-12-11", "37.23,lng%3D-121.88&startDate=2023-12-11");
				}if(etoUrl.contains("3D36.11,lng%3D-119.06&")) {
					etoUrl = etoUrl.replace("3D36.11,lng%3D-119.06&", "3D36.10,lng%3D-119.04&");
				}
				if(etoUrl.contains("3D33.09,lng%3D-117.32&startDate=2024-02-01")) {
					etoUrl = etoUrl.replace("3D33.09,lng%3D-117.32&startDate=2024-02-01", "3D33.11,lng%3D-117.21&startDate=2024-02-01");
				}if(etoUrl.contains("33.13,lng%3D-117.34&startDate=2024-02-01")) {
					etoUrl = etoUrl.replace("33.13,lng%3D-117.34&startDate=2024-02-01", "33.18,lng%3D-117.14&startDate=2024-02-01");
				}if(etoUrl.contains("33.17,lng%3D-117.37&startDate=2024-02-01&")) {
					etoUrl = etoUrl.replace("33.17,lng%3D-117.37&startDate=2024-02-01&", "33.17,lng%3D-117.37&startDate=2024-02-01&");
				}
				if(etoUrl.contains("3D37.61,lng%3D-121.00")) {
					etoUrl = etoUrl.replace("3D37.61,lng%3D-121.00", "3D37.61,lng%3D-121.01");
				}
				if(etoUrl.contains("lat%3D37.52,lng%3D-122.11")) {
					etoUrl = etoUrl.replace("lat%3D37.52,lng%3D-122.11", "lat%3D37.52,lng%3D-122.14");
				}if(etoUrl.contains("lat%3D34.26,lng%3D-119.28")) {
					etoUrl = etoUrl.replace("lat%3D34.26,lng%3D-119.28", "lat%3D34.26,lng%3D-119.26");
				}if(etoUrl.contains("lat%3D34.35,lng%3D-119.45")) {
					etoUrl = etoUrl.replace("lat%3D34.35,lng%3D-119.45", "lat%3D34.35,lng%3D-119.42");
				}if(etoUrl.contains("3D34.37,lng%3D-119.47")) {
					etoUrl = etoUrl.replace("3D34.37,lng%3D-119.47", "3D34.37,lng%3D-119.45");
				}if(etoUrl.contains("lat%3D34.24,lng%3D-119.28")) {
					etoUrl = etoUrl.replace("lat%3D34.24,lng%3D-119.28", "lat%3D34.24,lng%3D-119.21");
				}

//				if(etoUrl.contains("")) {
//					etoUrl = etoUrl.replace("33.65,lng%3D-118.39", "33.75,lng%3D-118.30");
//				}
//				U.log(etoUrl);
//				U.log(U.getCache(etoUrl));
//				String filepath = U.getCache(etoUrl);
//				U.log(new File(filepath).exists());
//				String etoFile = "", precipFile = "";
				double avgEto = 0.0;
				double avgprecip = 0.0;
				double sumprecip = 0.0;
				double sumEto = 0.0;
				String Precip = "";
				String SrNo = "0";
				try {
//					U.log(etoUrl);
//					U.log(U.getCache(etoUrl));
//					U.log(Tiles);
					U.downloadUsingStream(etoUrl, U.getCache(etoUrl));
					
				} catch (Exception e) {
					e.printStackTrace();
					U.log(Tiles);
					U.log(U.getCache(etoUrl));
					U.log(etoUrl);
					continue;
//					// TODO: handle exception
				}
				String etoFile = "";
//				try {
				String EtoCache = U.getCache(etoUrl);
//				EtoCache = EtoCache.replace("httpsetwatercagovapidataappKey53ce8f922d8d4a028551801575b1e678targetslat3D3410lng3D11721", "httpsetwatercagovapidataappKey53ce8f922d8d4a028551801575b1e678targetslat3D3410lng3D11724");
//				EtoCache = EtoCache.replace("httpsetwatercagovapidataappKey53ce8f922d8d4a028551801575b1e678targetslat3D3410lng3D11915startDate", "httpsetwatercagovapidataappKey53ce8f922d8d4a028551801575b1e678targetslat3D3410lng3D11912startDate");

				etoFile = FileUtil.readAllText(EtoCache);
//				} catch (Exception e) {
//					System.out.println("");
//					continue;
////					return;
//					// TODO: handle exception
//				}
//				if(etoFile.contains("Request Rejected"))continue;


				
				String etoVals[] = U.getValues(etoFile, "{\"Date\"", "}}");

				String zip = U.getSectionValue(etoFile, "\"ZipCodes\":\"", "\"");
				if (zip == null) {
//					try {
						zip = U.getSectionValue(etoFile, "zip-code=\"", "\"");
//						U.log(zip);
//					} catch (Exception e) {
//						U.log("TODO: handle exception1");
//						// TODO: handle exception
//					}
				}
				if (zip == null) {
//					U.log(etoUrl);
//					U.log(U.getCache(etoUrl));
					String[] latlag = null  ;
					double latValue;
					latValue = Double.parseDouble(lat.toString());
					String formattedLat = df.format(latValue); 
					double LonValue;
					LonValue = Double.parseDouble(lon.toString());
					String formattedLon = df.format(LonValue); 
					try {
//					System.out.println( formattedLat +"  |  "+formattedLon );
						latlag = new String[] {formattedLat, formattedLon };
						if(formattedLon.contains("-117.19")) formattedLon = "-117.18";
					} catch (Exception e) {		
						
					    System.err.println("Error: " + e.getMessage());

						e.getStackTrace();
						U.log( df.format(formattedLat)+" | "+ df.format(formattedLon) );
						U.log("TODO: handle exception 2");
						U.log(etoUrl);
						U.log(Tiles);
//						continue;
						// TODO: handle exception
					}
					String add[] ;
					try {
						add = U.getGoogleAddressWithKey(latlag);
						zip = add[3];
					} catch (Exception e) {
						// TODO: handle exception
						String st = latlag[0].trim() + "," + latlag[1].trim();
						String addr = "https://maps.googleapis.com/maps/api/geocode/json?latlng="+ st;
						String key = "AIzaSyAeG9lLU8fWh8rWcPivHDwxLAM4ZCInpmk";
//						U.log(latlag[0].trim() + " " + latlag[1].trim());
//						U.log("With key"+U.getCache(addr));
						
						String html = U.getHTMLForGoogleApiWithKey(addr,key);
						String txt = U.getSectionValue(html, "formatted_address\" : \"", "\"");
						String ZIPS_LIST[] = U.getValues(html, "formatted_address\" : \"", "\"");
//						System.out.println(ZIPS_LIST[2]);
				        String zipCode = extractZipCode(ZIPS_LIST[1]);
				        zip = zipCode;
//						U.log(zip);
//						writer.writeNext(etoVals);
//						continue;
					}
					
					


//					U.log(Arrays.toString(add));
					
				}if(zip==null) {
					System.out.println("zip is null");
					zip = "93001";
//					System.out.println(etoUrl);
//					System.out.println(Tiles);
//					continue;
				}
				if(!(zip== null) && zip.contains("92878"))zip = "92882";
				if(!(zip== null) && zip.contains("94531"))zip = "94513";
				if(!(zip== null) && zip.contains("92010"))zip = "92009";
				if(!(zip== null) && zip.contains("92011"))zip = "92009";
				if(!(zip== null) && zip.contains("94534"))zip = "94535";
				if(!(zip== null) && zip.contains("95319"))zip = "95320";
				if(!(zip== null) && zip.contains("92322"))zip = "92320";
				if(!(zip== null) && zip.contains("92391"))zip = "92392";
				if(!(zip== null) && zip.contains("91387"))zip = "91351";
				if(etoUrl.contains("34.09,lng%3D-119.27"))zip = "93033";
				if(etoUrl.contains("33.94,lng%3D-118.56"))zip = "90405";
				if(etoUrl.contains("34.23,lng%3D-119.27"))zip = "93003";
				if(etoUrl.contains("33.65,lng%3D-118.39"))zip = "90731";
				if(etoUrl.contains("33.65,lng%3D-118.21"))zip = "90220";
				if(etoUrl.contains("33.81,lng%3D-118.26"))zip = "90745";
				if(!(zip== null) && zip.contains("93064"))zip = "93063";
				if(!(zip== null) && zip.contains("92322"))zip = "92314";
				if(!(zip== null) && zip.contains("92391"))zip = "92314";
				if(!(zip== null) && zip.contains("12110"))zip = "90267";
				if(!(zip== null) && zip.contains("22743"))zip = "93267";
				if(!(zip== null) && zip.contains("93036"))zip = "93033";
				if(!(zip== null) && zip.contains("43469"))zip = "93033";
//				if(!(zip== null) && zip.contains("43469"))zip = "93033";

				if((zip== null)) { U.log(Tiles); continue;};
				String precipUrl = "https://et.water.ca.gov/api/data?appKey=53ce8f92-2d8d-4a02-8551-801575b1e678&targets="
						+ zip.trim() + "&startDate=" + dateString + "&endDate=" + dateStringEnd
						+ "&dataItems=day-precip&unitOfMeasure=E";
//				U.log(precipUrl);
				String PrecCache = U.getCache(precipUrl);

				try {
				U.downloadUsingStream(precipUrl,PrecCache );
				} catch (Exception e) {
					e.printStackTrace();
					U.log(U.getCache(precipUrl));
					U.log(zip);
					U.log(Tiles);
					U.log(precipUrl);
					continue;
					// TODO: handle exception
				}
				String precipFile = FileUtil.readAllText(PrecCache);
//				U.log(U.getCache(precipUrl));
//				U.log(precipFile);
				String precipVals[] = U.getValues(precipFile, "{\"Date\":", "\"}");
//				U.log(precipVals.length);
				Map<String, List<String>> combinedDataMap = new HashMap<>();

//				U.log("------------------------------------");
				for (String eto : etoVals) {
//                    U.log(sec);
					String Date = U.getSectionValue(eto, ":\"", "\",");
					String ET_Value = U.getSectionValue(eto, "{\"Value\":\"", "\",");
//					U.log(Date + "  |ET_Value  " + ET_Value);
					// "SrId", "Tiles", "Date", "Perc", "ET"
//					U.writeAllText("/home/shatam-100/Cache/untitled.txt", "ET: " + ET_Value);
					combinedDataMap.computeIfAbsent(Date, k -> new ArrayList<>()).add("ET: " + ET_Value);
				}
				
				for (String precipsec : precipVals) {
//                  U.log(precipsec);
					String Date = U.getSectionValue(precipsec, "\"", "\",");
					String Precip_Value = U.getSectionValue(precipsec, "{\"Value\":\"", "\",");
//					U.log(Date + "  |Precip_Value  " + Precip_Value);
//					writer.writeNext(new String[] { SrNo, Tiles,Date, Precip_Value, "ET_Value" });
					// "SrId", "Tiles", "Date", "Perc", "ET"
//					U.writeAllText("/home/shatam-100/Cache/untitled.txt", "Precipitation: " + Precip_Value);
				    combinedDataMap.computeIfAbsent(Date, k -> new ArrayList<>()).add("Precipitation: " + Precip_Value);
				}

				
				
				for (Map.Entry<String, List<String>> entry : combinedDataMap.entrySet()) {
					String date = entry.getKey();
					List<String> values = entry.getValue();
		            String Prec = "0.0";
//		            System.out.println();
		            String eto =values.get(0).replace("ET: ", "");
		            if(values.size()>1) {
//		            System.out.println(values.toString());
		            	
		            Prec =values.get(1).replace("Precipitation: ", "");
		            }else {
		            	Prec = "0.0";
//		            	System.out.println(values);
//		            	if(!values.contains("Precipitation")) {
//		            		eto = "0.0";
//		            		System.out.println(eto+" | "+date+" | "+values.toString());
//		            	}else {
//			            	System.out.println(date+" | "+values.toString());
//			            	System.out.println(etoUrl);
//			            	System.out.println(precipUrl);
//			            	System.out.println(U.getCache(etoUrl));
//			            	System.out.println(U.getCache(precipUrl));
//			            	System.out.println("0.0"+Tiles);
//							System.out.println("0.0");
			            	etonotfoun.put(Tiles, Tiles);
//						}
						continue;
					}
		            
//		            U.log(Prec);
		            if (Prec == null || Prec.equalsIgnoreCase("null")) {
		                Prec = "0.0";
		            }
//		            U.log(Prec);
//		            U.log(date + " | " + String.join(" | ", values));
//		            U.log(date + " | Precipitation: " + values.get(0) + " | ET: " + values.get(1));
//		            System.out.println( SrNo+ Tiles+ date+ Prec);
//					try {
						writer.writeNext(new String[] { SrNo, Tiles, date, Prec,eto });
//					} catch (Exception e) {
////			            System.out.println(values.get(0)+" | "+values.get(1));
//						e.printStackTrace();
////			            U.log(date + " | Precipitation: " + values.get(0) + " | ET: " );
//						U.log("Exception");
//						// TODO: handle exception
//					}

				}

//				for (String eto : etoVals) {
//					sumEto += Double.parseDouble(eto);
//				}
//				avgEto = sumEto / etoVals.length;
//				U.log("avgEto :: " + avgEto);
//				U.log("avgprecip  " + avgprecip);
//                writer.writeNext(new String[]{avgEto + "", avgprecip + ""});
//				return;
//				if(dateStringEnd.contains("2020-09-21")) {
//					return;
//				}
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
//			if (writer != null) {
//				writer.close();
//			}
		}

	}

    private static String extractZipCode(String address) {
        // Define the pattern for a 5-digit ZIP code
        Pattern pattern = Pattern.compile("\\b\\d{5}\\b");

        // Create a matcher with the input address
        Matcher matcher = pattern.matcher(address);

        // Find the first occurrence of the pattern in the address
        if (matcher.find()) {
            return matcher.group();
        } else {
            return null;
        }
    }

	static class CombinedValues {
		private String precipitation;
		private String ET;

		public String getPrecipitation() {
			return precipitation;
		}

		public void setPrecipitation(String precipitation) {
			this.precipitation = precipitation;
		}

		public String getET() {
			return ET;
		}

		public void setET(String ET) {
			this.ET = ET;
		}
	}

	private static Coordinate calculateCentroid(String tileCoordinates)
			throws com.vividsolutions.jts.io.ParseException, ParseException {
		WKTReader reader = new WKTReader();
		org.locationtech.jts.geom.Geometry geometry = reader.read(tileCoordinates);
		Point centroid = geometry.getCentroid();
		return new Coordinate(centroid.getX(), centroid.getY());
	}
}
