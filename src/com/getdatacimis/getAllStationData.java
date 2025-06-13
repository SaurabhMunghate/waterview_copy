package com.getdatacimis;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
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

import org.json.JSONObject;
import org.locationtech.jts.geom.Coordinate;

//import com.eto_consumption.getEtoValueFormApi_Working.DatePair;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.opencsv.CSVWriter;
import com.shatam.utils.FileUtil;
import com.shatam.utils.U;

public class getAllStationData {
	static long daysBetweenA = 0;
	static long AllTiles = 0;
	static HashMap<String, String> hsZip = new HashMap<String, String>();
	static HashMap<String, String> hsSta = new HashMap<String, String>();
	
	public static void main(String[] args) throws Exception {
		String html = U.getHTML("https://et.water.ca.gov/api/station");
		String[] ziplist = U.getValues(html, "<zip-code>", "</zip-code>");
		U.log(ziplist.length);
		for(String zip : ziplist) {
			hsZip.put(zip, zip);
		}
		U.log("hsZip : "+hsZip.size());
		String[] stalist = U.getValues(html, "<station station-nbr=\"", "\"");
		U.log(stalist.length);
		for(String sta : stalist) {
			hsSta.put(sta, sta);
		}
		U.log("hsSta : "+hsSta.size());

		
		System.out.println("HHHHH");
		getEtoForCsvTile1("", "1991-01-01", "2025-04-30");//Done
	}

	
    private static void getEtoForCsvTile1(String listofTiles,String startYear,String endYear) throws Exception {
        String csvFilePath = listofTiles.replace(".csv", "_Eto_Precip_")+startYear+"_To_"+endYear+"_3_May_c1.csv";
//        String ExceptioncsvFile = listofTiles.replace(".csv", "_ExceptioncsvFile_")+startYear+"_To_"+endYear+"_14_March_c1.csv";
        
        System.out.println(listofTiles);
    	System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

//    	System.out.println(csvFilePath);
        List<DatePair> datePairs = getDatePairsInRange1(startYear, endYear, 90);

        System.out.println("Date Pairs within the range with a 90-d;ay difference:");
        CSVWriter writer = new CSVWriter(new FileWriter(csvFilePath));
//        CSVWriter exceptioncsvFile = new CSVWriter(new FileWriter(ExceptioncsvFile));

		writer.writeNext(new String[] { "SrId", "Tiles", "Date", "Perc", "ET" });

        for (DatePair datePair : datePairs) {
        	System.out.println("==================================================================================================================");
//            System.out.println(datePair.getStart() + " to " + datePair.getEnd());
            getData("&startDate="+datePair.getStart()+"&endDate="+datePair.getEnd());
//    		call(datePair.getStart(),  datePair.getEnd(), writer, listofTiles);
//            break;
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
	private static void getData(String string) throws MalformedURLException, IOException {
		String url = "";

        for (String key : hsSta.keySet()) {
        	System.out.println(key);
          url = "https://et.water.ca.gov/api/data?appKey=53ce8f92-2d8d-4a02-8551-801575b1e678&targets="+key+string+"&dataItems=day-asce-eto&unitOfMeasure=E";
    		U.downloadUsingStream(url, U.getCache(url));

        }

//        url = "https://et.water.ca.gov/api/data?appKey=53ce8f92-2d8d-4a02-8551-801575b1e678&targets="+threeKeys+string;
//		U.downloadUsingStream(url, U.getCache(url));

	}

	private static void getData1(String string) throws MalformedURLException, IOException {
//		U.log(hsSta.size());
        // Printing keys in pairs of three
		String url = "";
        StringBuilder threeKeys = new StringBuilder();
        int count = 0;
        for (String key : hsSta.keySet()) {
            if (count > 0) {
                threeKeys.append(",");
            }
            threeKeys.append(key);
            count++;

            if (count == 3) {
//                System.out.println(threeKeys);
//              System.out.println();
              url = "https://et.water.ca.gov/api/data?appKey=53ce8f92-2d8d-4a02-8551-801575b1e678&targets="+threeKeys+string;
      		U.downloadUsingStream(url, U.getCache(url));
  
              threeKeys.setLength(0);
                count = 0;
            }
        }

        // Print the remaining keys if they are less than three
        if (count > 0) {
//            System.out.println(threeKeys);
//          System.out.println("https://et.water.ca.gov/api/data?appKey=53ce8f92-2d8d-4a02-8551-801575b1e678&targets="+threeKeys+string);
          url = "https://et.water.ca.gov/api/data?appKey=53ce8f92-2d8d-4a02-8551-801575b1e678&targets="+threeKeys+string;
  		U.downloadUsingStream(url, U.getCache(url));

        }

		// TODO Auto-generated method stub
//        for(int i= 0 ;i<274;i++) {
//        	System.out.println(i);
//            System.out.println("https://et.water.ca.gov/api/data?appKey=53ce8f92-2d8d-4a02-8551-801575b1e678&targets="+i+string);
//        }
//		String threeDate = "";
//		for (int i = 1; i <= 274; i++) {
//		    if (i % 3 == 1) {
//		        if (!threeDate.isEmpty()) {
////		            System.out.println(threeDate);
//		        	threeDate = threeDate.replace("269", "");
//		        	String Url = "https://et.water.ca.gov/api/data?appKey=53ce8f92-2d8d-4a02-8551-801575b1e678&targets="+threeDate+string;
////		System.out.println(Url);
//		U.downloadUsingStream(Url, U.getCache(Url));
//		        }
//		        threeDate = Integer.toString(i);
//		    } else {
//		        threeDate += "," + i;
//		    }
//		}
//		System.out.println(threeDate); // Print the last group

	}


	private static List<DatePair> getDatePairsInRange1(String startDateStr, String endDateStr, int dayDifference) {
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
}
