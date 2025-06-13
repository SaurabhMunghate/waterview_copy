package com.eto_consumption;

import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.opencsv.CSVWriter;
import com.shatam.utils.FileUtil;
import com.shatam.utils.U;

public class GetCimisData {
    static long daysBetweenA = 0;
    static long AllTiles = 0;

    public static void main(String[] args) throws Exception {
        getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/monte_vista_14_Zoom_16.csv", "2024-10-01", "2024-12-31");
        
    }

    private static void getEtoForCsvTile(String listofTiles, String startYear, String endYear) throws Exception {
        String csvFilePath = listofTiles.replace(".csv", "_Eto_Precip_") + startYear + "_To_" + endYear + "_3_May_c1.csv";

        System.out.println("Processing: " + listofTiles);
        
        List<DatePair> datePairs = getDatePairsInRange(startYear, endYear, 89);
        
        CSVWriter writer = new CSVWriter(new FileWriter(csvFilePath));
        writer.writeNext(new String[]{"SrId", "Tiles", "Date", "Perc", "ET"});

        for (DatePair datePair : datePairs) {
            call(datePair.getStart(), datePair.getEnd(), writer, listofTiles);
        }

        long daysBetween = ChronoUnit.DAYS.between(LocalDate.parse(startYear), LocalDate.parse(endYear));
        daysBetweenA = daysBetween;
        
        System.out.println("Total Days: " + daysBetween);
        System.out.println("Total Entries: " + (AllTiles * daysBetweenA));
        System.out.println("File Saved: " + csvFilePath);
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

        public String getStart() { return start; }
        public String getEnd() { return end; }
    }

    private static void call(String dateString, String dateStringEnd, CSVWriter writer, String listofTiles) throws Exception {
        File input = new File(listofTiles);
        CsvSchema csv = CsvSchema.emptySchema().withHeader();
        CsvMapper csvMapper = new CsvMapper();
        MappingIterator<Map<?, ?>> mappingIterator = csvMapper.reader().forType(Map.class).with(csv).readValues(input);
        List<Map<?, ?>> list = mappingIterator.readAll();
        AllTiles = list.size();
        
        System.out.println("Total Records: " + list.size());
        DecimalFormat df = new DecimalFormat("####0.00");
        
        for (Map<?, ?> data : list) {
            String Tiles = data.get("Tiles").toString();
            if (Tiles == null || Tiles.contains("Tiles")) continue;
            
            Coordinate centroid = calculateCentroid(Tiles);
            String lat = df.format(centroid.y);
            String lon = df.format(centroid.x);

            String etoUrl = "https://et.water.ca.gov/api/data?appKey=53ce8f92-2d8d-4a02-8551-801575b1e678&targets=lat%3D"
                    + lat + ",lng%3D" + lon + "&startDate=" + dateString + "&endDate=" + dateStringEnd + "&dataItems=day-asce-eto&unitOfMeasure=E";
            
            U.downloadUsingStream(etoUrl, U.getCache(etoUrl));
            String etoFile = FileUtil.readAllText(U.getCache(etoUrl));
            String[] etoVals = U.getValues(etoFile, "{\"Date\"", "}}");
			String zip = U.getSectionValue(etoFile, "\"ZipCodes\":\"", "\"");
			if (zip == null) zip = U.getSectionValue(etoFile, "zip-code=\"", "\"");

            String precipUrl = "https://et.water.ca.gov/api/data?appKey=53ce8f92-2d8d-4a02-8551-801575b1e678&targets="
                    + zip.trim() + "&startDate=" + dateString + "&endDate=" + dateStringEnd + "&dataItems=day-precip&unitOfMeasure=E";
            
            U.downloadUsingStream(precipUrl, U.getCache(precipUrl));
            String precipFile = FileUtil.readAllText(U.getCache(precipUrl));
            String[] precipVals = U.getValues(precipFile, "{\"Date\":", "\"}");

            Map<String, List<String>> combinedDataMap = new HashMap<>();
            for (String eto : etoVals) {
				String Date = U.getSectionValue(eto, ":\"", "\",");
				String ET_Value = U.getSectionValue(eto, "{\"Value\":\"", "\",");
                combinedDataMap.computeIfAbsent(Date, k -> new ArrayList<>()).add(ET_Value);
            }

            for (String precip : precipVals) {
				String Date = U.getSectionValue(precip, "\"", "\",");
				String Precip_Value = U.getSectionValue(precip, "{\"Value\":\"", "\",");
                combinedDataMap.computeIfAbsent(Date, k -> new ArrayList<>()).add(Precip_Value);
            }

            for (Map.Entry<String, List<String>> entry : combinedDataMap.entrySet()) {
//            	System.out.println(entry);
                writer.writeNext(new String[]{"0", Tiles, entry.getKey(), entry.getValue().get(1), entry.getValue().get(0)});
            }
        }
    }

    private static Coordinate calculateCentroid(String tileCoordinates) throws ParseException {
        WKTReader reader = new WKTReader();
        org.locationtech.jts.geom.Geometry geometry = reader.read(tileCoordinates);
        Point centroid = geometry.getCentroid();
        return new Coordinate(centroid.getX(), centroid.getY());
    }
}
