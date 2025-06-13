package com.eto_consumption;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;


import org.locationtech.jts.geom.Envelope;

import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;


import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.opencsv.CSVWriter;
import com.shatam.utils.FileUtil;
import com.shatam.utils.U;
import java.util.*;
import java.io.*;

public class getDemo {

	static long AllTiles = 0;
	 private static Queue<Tile> mainTilesQueue = new LinkedList<>();
	    private static Map<String, Queue<Tile>> pendingQuadrants = new HashMap<>();
//	static Map<String, List<String>> combinedDataMap = new HashMap<>();

	public static void main(String[] args) throws Exception {
		System.out.println("start------------------------------------------");
//		getEtoForCsvTile("/home/vikas/Downloads/monte_vista_14_Zoom_16 (another copy).csv", "2022-02-04", "2022-02-04");
//		getEtoForCsvTile("/home/vikas/Downloads/monte_vista_14_Zoom_16 (copy).csv", "2022-02-04", "2022-02-04");
		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/monte_vista_14_Zoom_16.csv",  "2022-02-04", "2022-04-04");
//		getEtoForCsvTile("/home/vikas/Downloads/demo.csv",  "2022-02-04", "2022-02-04");
		System.out.println("End------------------------------------------");


	}
	
	static class Tile {
		 String id;
		 String zip;
		 String parentZip;
		    double minX, minY, maxX, maxY;
		    int level;
		    boolean processed;

		    public Tile(String id, double minX, double minY, double maxX, double maxY, int level, boolean processed) {
		        this.id = id;
		        this.minX = minX;
		        this.minY = minY;
		        this.maxX = maxX;
		        this.maxY = maxY;
		        this.level = level;
		        this.processed = processed;
		        this.zip = "";
		        this.parentZip = "";
		    }
		    public Tile(String id, double minX, double minY, double maxX, double maxY, int level, boolean processed, String parentZip) {
		        this(id, minX, minY, maxX, maxY, level, processed);
		        this.parentZip = parentZip;
		    }
		    public List<Tile> getQuadrants() {
		        if (level >= 4) return Collections.emptyList();

		        double midX = (minX + maxX) / 2;
		        double midY = (minY + maxY) / 2;
		        int nextLevel = level + 1;

//		        return Arrays.asList(
//		            new Tile(this.id + ".Q1", minX, midY, midX, maxY, nextLevel, false),
//		            new Tile(this.id + ".Q2", midX, midY, maxX, maxY, nextLevel, false),
//		            new Tile(this.id + ".Q3", minX, minY, midX, midY, nextLevel, false),
//		            new Tile(this.id + ".Q4", midX, minY, maxX, midY, nextLevel, false)
//		        );
		        return Arrays.asList(
		                new Tile(this.id + ".Q1", minX, midY, midX, maxY, nextLevel, false, this.zip),
		                new Tile(this.id + ".Q2", midX, midY, maxX, maxY, nextLevel, false, this.zip),
		                new Tile(this.id + ".Q3", minX, minY, midX, midY, nextLevel, false, this.zip),
		                new Tile(this.id + ".Q4", midX, minY, maxX, midY, nextLevel, false, this.zip)
		            );
		    }
		    
		    
	}

	private static Tile parsePolygon(String tile, String tileId) throws ParseException {
        // Extract coordinates using regex
		WKTReader reader = new WKTReader();
        Polygon polygon = (Polygon) reader.read(tile);
        Envelope envelope = polygon.getEnvelopeInternal();
        double minX = envelope.getMinX();
        double maxX =envelope.getMaxX();
        double minY =envelope.getMinY();
        double maxY =envelope.getMaxY();
        return new Tile(tileId, minX, minY, maxX, maxY, 0, false);

        
    }

	private static void getEtoForCsvTile(String listofTiles,String startYear,String endYear) throws Exception {
		 String csvFilePath = listofTiles.replace(".csv", "_Eto_Precip_")+startYear+"_To_"+endYear+"-fdd.csv";
	     List<DatePair> datePairs = getDatePairsInRange(startYear, endYear, 89);
	     CSVWriter writer = new CSVWriter(new FileWriter(csvFilePath));
//	     writer.writeNext(new String[] { "SrId", "Tiles", "Date", "Perc", "ET" });
	     writer.writeNext(new String[] { "SrId", "Tiles", "Attempt", "Perc" });
//	     writer.writeNext(new String[]{tile.id, originalPolygon, String.valueOf(tile.level), precValue});
	     for (DatePair datePair : datePairs) {
	        	System.out.println("==================================================================================================================");
	            System.out.println(datePair.getStart() + " to " + datePair.getEnd());
	    		call(datePair.getStart(),  datePair.getEnd(), writer, listofTiles);
	    		
	        }
	        LocalDate date1 = LocalDate.parse(startYear);
	        LocalDate date2 = LocalDate.parse(endYear);

	        long daysBetween = ChronoUnit.DAYS.between(date1,date2);
	        // Output the number of days

	        System.out.println(csvFilePath);
	        writer.close();
	}
	
	private static void call(String dateString,String dateStringEnd,CSVWriter writer, String listofTiles) 
			throws Exception {
		
		File input = new File(listofTiles);
		CsvSchema csv = CsvSchema.emptySchema().withHeader();
		CsvMapper csvMapper = new CsvMapper();
		MappingIterator<Map<?, ?>> mappingIterator = csvMapper.reader().forType(Map.class).with(csv)
				.readValues(input);
		List<Map<?, ?>> list = mappingIterator.readAll();
		 List<Tile> mainTiles = new ArrayList<>();
		 System.out.println(mainTiles.size());
		AllTiles = list.size();
		U.log("total records: " + list.size());
		Map<String, String> originalPolygons = new HashMap<>();
		try  {
			BufferedReader br = new BufferedReader(new FileReader(listofTiles));
            String line;
            int tileIndex = 1;  // To name the tiles uniquely
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
            	System.out.println(line);
            	 if (isFirstLine) { // Skip header
            	        isFirstLine = false;
            	        continue;
            	    }
                System.out.println(line);
                	String originalPolygon = line.replace("\"", "");
                    Tile tile = parsePolygon(originalPolygon, "Tile" + tileIndex);
//                    System.out.println(tile.);
                    if (tile != null) {
                        mainTiles.add(tile);
                        originalPolygons.put(tile.id, originalPolygon);
                        tileIndex++;
                    }

            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
		System.out.println(mainTiles.size());
		for (Tile tile : mainTiles) {
            mainTilesQueue.add(tile);
            pendingQuadrants.put(tile.id, new LinkedList<>());
        }
//		while (!mainTilesQueue.isEmpty() || hasPendingQuadrants()) {
//            // Process main tile
//            if (!mainTilesQueue.isEmpty()) {
//                Tile tile = mainTilesQueue.poll();
//                System.out.println("Fetching data for MAIN TILE: " + tile.id);
//
//                boolean result = fetchFromAPI(tile,dateString,dateStringEnd);
//                if (result) {
//
//                	String originalPolygon = originalPolygons.get(tile.id); // Retrieve original polygon
//                    String precValue = getPrecValue(tile);
//                    writer.writeNext(new String[]{tile.id, originalPolygon, String.valueOf(tile.level), precValue});
//
//                    System.out.println("✅ Data found for " + tile.id + ", stopping further processing.");
//                    continue;
//                }
		while (!mainTilesQueue.isEmpty() || hasPendingQuadrants()) {
	        if (!mainTilesQueue.isEmpty()) {
	            Tile tile = mainTilesQueue.poll();
	            System.out.println("Fetching data for MAIN TILE: " + tile.id);

	            Map<String, String> precData = fetchFromAPI(tile, dateString, dateStringEnd);
	            if (precData != null) {
	                boolean hasValidData = false;
	                
	                for (String value : precData.values()) {
	                    if (value != null) {
	                        hasValidData = true;
	                        break;
	                    }
	                }

	                if (hasValidData) {
	                    String originalPolygon = originalPolygons.get(tile.id);
	                    
	                    for (Map.Entry<String, String> entry : precData.entrySet()) {
	                        String date = entry.getKey();
	                        String precValue = entry.getValue();
	                        
	                        // Store null as "NULL" in CSV or handle differently
	                        writer.writeNext(new String[]{tile.id, originalPolygon, String.valueOf(tile.level), date, 
	                                                      (precValue != null) ? precValue : "NULL"});
	                    }

	                    System.out.println("✅ Data found for " + tile.id + ", stopping further processing.");
	                    continue;
	                }
	            }

                // If data is not found, add the first quadrant to pending
                List<Tile> quadrants = tile.getQuadrants();
                if (!quadrants.isEmpty()) {
                    pendingQuadrants.get(tile.id).addAll(quadrants);
                }
            }

            // Process quadrants in an alternating order
            processQuadrantsAlternately(mainTiles,dateString,dateStringEnd);
        }

	}

	
//	private static boolean fetchFromAPI(Tile tile,String dateString,String dateStringEnd) throws Exception {
//		double centroidX = (tile.minX + tile.maxX) / 2;
//        double centroidY = (tile.minY + tile.maxY) / 2;
//        System.out.println("Fetching data for " + tile.id + " at centroid (" + centroidX + ", " + centroidY + ")");
//
//		String lat = Double.toString(centroidY);//centroid.y + "";
//		String lon = Double.toString(centroidX); //centroid.x + "";
//		System.out.println(lat+" "+lon);
//		
////		 System.out.println("df.format(lat).toString()" + df.format(lat)+""+ "df.format(lon).toString()" +df.format(lon)+"");		
//		 String zip  =U.getBingAddress(lat,lon)[3];
//		 tile.zip = zip; 
//		 System.out.println(zip);
//		 
//		// **Endpoint Condition: Stop processing if ZIP is the same as parent**
//		    if (tile.parentZip != null && tile.parentZip.equals(zip)) {
//		        System.out.println("Skipping quadrant " + tile.id + " as ZIP matches parent (" + tile.parentZip + ")");
//		        return false;
//		    }
//		Map<String, List<String>> combinedDataMap = calculatePrep(dateString, dateStringEnd, zip);
//		for (Map.Entry<String, List<String>> entry : combinedDataMap.entrySet()) {
////	        String Prec = entry.getValue().isEmpty() ? "0.0" : entry.getValue().get(0).replace("Precipitation: ", "");
//
//			String date = entry.getKey();
//			List<String> values = entry.getValue();
//            String Prec = "0.0";
//            System.out.println();
//            System.out.println(values.size()>1);
////            String eto =values.get(0).replace("ET: ", "");
//            if(values.size()>0) {
//            System.out.println(values.toString());
//            	
//            Prec =values.get(0).replace("Precipitation: ", "");
//            }
//            else {
//            	Prec = "0.0";
//
//				continue;
//			}
//            
////            U.log(Prec);
//            if (Prec == null || Prec.equalsIgnoreCase("null")) {
//                Prec = "0.0";
//                return false;
//                
//            }
//            }
////        return random.nextInt(10) < 3; 
//        return true;// Simulate API response with 30% success rate
//    }
	private static Map<String, String> fetchFromAPI(Tile tile, String dateString, String dateStringEnd) throws Exception {
	    double centroidX = (tile.minX + tile.maxX) / 2;
	    double centroidY = (tile.minY + tile.maxY) / 2;
	    System.out.println("Fetching data for " + tile.id + " at centroid (" + centroidX + ", " + centroidY + ")");

	    String lat = Double.toString(centroidY);
	    String lon = Double.toString(centroidX);
	    System.out.println(lat + " " + lon);

	    // Get ZIP code
	    String zip = U.getBingAddress(lat, lon)[3];
	    tile.zip = zip;
	    System.out.println("--------");
	    System.out.println(zip);
	    System.out.println(tile.parentZip);
	    // **Check if ZIP matches parent**
	    if (tile.parentZip != null && tile.parentZip.equals(zip)) {
	        System.out.println("Skipping quadrant " + tile.id + " as ZIP matches parent (" + tile.parentZip + ")");
	        return null; // Skip processing
	    }

	    // Fetch precipitation data
	    Map<String, List<String>> combinedDataMap = calculatePrep(dateString, dateStringEnd, zip);
	    Map<String, String> finalDataMap = new HashMap<>();

	    // Loop through all expected dates
	    LocalDate startDate = LocalDate.parse(dateString);
	    LocalDate endDate = LocalDate.parse(dateStringEnd);
	    for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
	        String dateStr = date.toString(); // Format YYYY-MM-DD
	        List<String> values = combinedDataMap.getOrDefault(dateStr, new ArrayList<>());
	        System.out.println(values.toString());
	        String prec = null; // Store null if no value is found
	        if (!values.isEmpty()) {
	            prec = values.get(0).replace("Precipitation: ", "");
	        }

	        if (prec != null && prec.equalsIgnoreCase("null")) {
	            prec = null; // Ensure we store `null` instead of a string "null"
	        }

	        finalDataMap.put(dateStr, prec);
	    }

	    return finalDataMap;
	}
	private static  Map<String, List<String>> calculatePrep(String dateString,String dateStringEnd,String zip ) throws MalformedURLException, IOException  {
		String precipUrl = "https://et.water.ca.gov/api/data?appKey=53ce8f92-2d8d-4a02-8551-801575b1e678&targets="+ zip.trim() + "&startDate=" + dateString + "&endDate=" + dateStringEnd+ "&dataItems=day-precip&unitOfMeasure=E";
		String PrecCache = U.getCache(precipUrl);
		
		U.downloadUsingStream(precipUrl,PrecCache );
		Map<String, List<String>> combinedDataMap = new HashMap<>();
		String precipFile = FileUtil.readAllText(PrecCache);
		String precipVals[] = U.getValues(precipFile, "{\"Date\":", "\"}");
		for (String precipsec : precipVals) {
	
				String Date = U.getSectionValue(precipsec, "\"", "\",");
				String Precip_Value = U.getSectionValue(precipsec, "{\"Value\":\"", "\",");
			    combinedDataMap.computeIfAbsent(Date, k -> new ArrayList<>()).add("Precipitation: " + Precip_Value);
			}
		
		return combinedDataMap;
	}

    private static void processQuadrantsAlternately(List<Tile> mainTiles,String dateString,String dateStringEnd) throws Exception {
        boolean processed = false;
        for (Tile tile : mainTiles) {
            Queue<Tile> queue = pendingQuadrants.get(tile.id);
            if (!queue.isEmpty()) {
                Tile quadrant = queue.poll();
                System.out.println("Fetching data for QUADRANT: " + quadrant.id);
                System.out.println("Fetching data for QUADRANT: " + quadrant.minX + " "+quadrant.minY+" "+quadrant.maxX+" "+quadrant.maxY);

//                boolean result = fetchFromAPI(quadrant, dateString, dateStringEnd);
//                if (!result && quadrant.level < 4) {
//                    List<Tile> subQuadrants = quadrant.getQuadrants();
//                    if (!subQuadrants.isEmpty()) {
//                        queue.addAll(subQuadrants);
//                    }
//                }
                Map<String, String> precData = fetchFromAPI(quadrant, dateString, dateStringEnd);

                // Check if precipitation data contains valid values
                boolean hasValidData = false;
                if (precData != null) {
                    for (String value : precData.values()) {
                        if (value != null) {
                            hasValidData = true;
                            break;
                        }
                    }
                }

                // If no valid precipitation data found and max level not reached, divide into sub-quadrants
                if (!hasValidData && quadrant.level < 4) {
                    List<Tile> subQuadrants = quadrant.getQuadrants();
                    if (!subQuadrants.isEmpty()) {
                        queue.addAll(subQuadrants);
                    }
                }
                processed = true;
            }
        }

        // If no quadrants were processed, stop looping
        if (!processed) {
            pendingQuadrants.clear();
        }
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
	 
	 
	 private static boolean hasPendingQuadrants() {
	        return pendingQuadrants.values().stream().anyMatch(q -> !q.isEmpty());
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
 
//	static class Tile {
// String id;
// double minX, minY, maxX, maxY;
// int level;
// boolean isQuadrant;
// int nextQuadrantIndex = 0; // To track next quadrant processing
// String polygonWKT;
//
// Tile(String id, double minX, double minY, double maxX, double maxY, int level, boolean isQuadrant) {
//     this.id = id;
//     this.minX = minX;
//     this.minY = minY;
//     this.maxX = maxX;
//     this.maxY = maxY;
//     this.level = level;
//     this.isQuadrant = isQuadrant;
// }
// Tile(String id, String polygonWKT, int level, boolean isQuadrant) {
//     this.id = id;
//     this.polygonWKT = polygonWKT;
//     this.level = level;
//     this.isQuadrant = isQuadrant;
// }
// // Generate the next quadrant tile
// Tile getNextQuadrant() {
// 
//     if (nextQuadrantIndex >= 4) {
//         return null; // No more quadrants left to process
//     }
//
//     double midX = (minX + maxX) / 2;
//     double midY = (minY + maxY) / 2;
//     double newMinX, newMinY, newMaxX, newMaxY;
//
//     switch (nextQuadrantIndex) {
//         case 0: // Q1 - Top-left
//             newMinX = minX; newMaxX = midX;
//             newMinY = midY; newMaxY = maxY;
//             break;
//         case 1: // Q2 - Top-right
//             newMinX = midX; newMaxX = maxX;
//             newMinY = midY; newMaxY = maxY;
//             break;
//         case 2: // Q3 - Bottom-left
//             newMinX = minX; newMaxX = midX;
//             newMinY = minY; newMaxY = midY;
//             break;
//         case 3: // Q4 - Bottom-right
//             newMinX = midX; newMaxX = maxX;
//             newMinY = minY; newMaxY = midY;
//             break;
//         default:
//             throw new IllegalArgumentException("Invalid quadrant index");
//     }
//     //converting co-ordinates to polygon
//     Tile quadrant = new Tile(id + ".Q" + (nextQuadrantIndex + 1), newMinX, newMinY, newMaxX, newMaxY, level + 1, true);
//     nextQuadrantIndex++;
//     return quadrant;
// }
}