package com.Precipatation_Work;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import com.fasterxml.jackson.databind.deser.DataFormatReaders.Match;
import com.shatam.utils.FileUtil;
import com.shatam.utils.Util;
import com.test.U;

public class Precipatation_Work1 {
	public static void main(String[] srt) throws IOException {
		System.out.println("HIII");
		String html = U.getHTML("https://et.water.ca.gov/api/station");
		String[] ziplist = U.getValues(html, "<zip-code>", "</zip-code>");
		U.log(ziplist.length);
		HashMap<String, String> hs = new HashMap<String, String>();
		for(String st : ziplist) {
			hs.put(st, st);
		}
		U.log(hs.size());
//		U.log(">>>>>>>>"+Util.matchAll(html, "[\\s\\w\\W]{30}<station station-nbr=\"\\d+\"[\\s\\w\\W]{30}",0));

		System.out.println("https://et.water.ca.gov/api/stationzipcode");
		String stationzipcode = U.getHTML("https://et.water.ca.gov/api/stationzipcode");
		String[] stationzipcodelist = U.getValues(html, "<zip-code>", "</zip-code>");
		U.log(ziplist.length);
		HashMap<String, String> Stationhs = new HashMap<String, String>();
		for(String st : stationzipcodelist) {
			Stationhs.put(st, st);
		}
		U.log(Stationhs.size());
//		stationzipcode = stationzipcode.replaceAll("<connect-date>\\d+/\\d+/\\d+</connect-date>", "")
//				.replaceAll("<disconnect-date>\\d+/\\d+/\\d+</disconnect-date>", "");
//		U.log(">>>>>>>>"+Util.matchAll(stationzipcode, "[\\s\\w\\W]{10}<is-active>True</is-active>[\\s\\w\\W]{0}",0));
		
        String filePath = "/home/shatam-100/Down/WaterView_Data/Precipitation_Work/precipatation_2024-12-31.csv";

		String[] allStationData = U.getValues(html, "<station", "</station>");

        try (FileWriter writer = new FileWriter(filePath)) {
            // Write CSV header
            writer.append("station_nbr,regional_office,Latitude,Longitude,startDate,endDate,precValue,Zips\n");
//            writer.append("station_nbr,regional_office,Latitude,Longitude,precValue,Zip\n");

            for (String st : allStationData) {
//                U.log(st); // Assuming U.log logs the station name or info
            	if(st.contains("False"))continue;

                // Replace with actual logic to get lat/long per station
                String station_nbr = U.getSectionValue(st, "nbr=\"", "\"");
                String regional_office = U.getSectionValue(st, "regional-office=\"", "\"");
                String latitude = U.getSectionValue(st, "<hms-latitude>", "</hms-latitude>");
                latitude = U.getSectionValue(latitude+"$", "/", "$");
                String longitude = U.getSectionValue(st, "<hms-longitude>", "</hms-longitude>");
                longitude = U.getSectionValue(longitude+"$", "/", "$");
                String zip_codes = U.getSectionValue(st, "<zip-codes>", "</zip-codes>")
                		.replaceAll("<zip-code>", "").replaceAll("</zip-code>", ",").replaceAll("\\n", "");
                
				String zip= U.getSectionValue(st, "<zip-code>", "</zip-code>");
				if(zip==null)continue;
				String precipUrl = "https://et.water.ca.gov/api/data?appKey=53ce8f92-2d8d-4a02-8551-801575b1e678&targets="
						+ zip.trim() + "&startDate=" + "2024-12-01" + "&endDate=" + "2024-12-31"
						+ "&dataItems=day-precip&unitOfMeasure=E";

				String PrecCache = U.getCache(precipUrl);

				try {
				U.downloadUsingStream(precipUrl,PrecCache );
				} catch (Exception e) {
//					e.printStackTrace();
//					U.log(U.getCache(precipUrl));
//					U.log(zip);
//					U.log(Tiles);
//					U.log(precipUrl);
					continue;
//					// TODO: handle exception
				}
				String precipFile = FileUtil.readAllText(PrecCache);
				double precValue = 0.0;
				if(precipFile.contains("Value")) {
					String[] PrecVal = U.getValues(precipFile, "\"Value\":\"", "\",");
					for(String val : PrecVal) {
//						U.log(val);
						double n = Double.parseDouble(val);
						precValue+=n;
					}

				}

                // Write to CSV
                writer.append(station_nbr)
                .append(",").append(regional_office)
                .append(",").append(latitude)
                .append(",").append(longitude)
                 .append(",").append("2024-12-01")
                .append(",").append("2024-12-31")
                .append(",").append(precValue+"")
                .append(",").append(zip).append("\n");
            }

            System.out.println("CSV file written successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        }
        
//		String[] allStationData = U.getValues(html, "<station station", "</station>");
//		U.log(allStationData.length);
//		for(String st : allStationData) {
//			U.log(st);
//			String latitude = "113123";
//			String longitude = "6346346";
//		}

	}

}
