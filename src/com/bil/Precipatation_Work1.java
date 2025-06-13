package com.bil;
import java.io.*;
import java.util.*;
import com.opencsv.CSVReader;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTReader;

public class Precipatation_Work1 {
    public static void main(String[] args) {
        String MLfinalC = "/home/shatam-100/Down/WaterView_Data/PRISM/prd.meter_locations_finalcomparison.csv";
        String premiseBound = "/home/shatam-100/Down/WaterView_Data/PRISM/prd.premise_bounds.csv";
        String DataBase = "/home/shatam-100/Down/WaterView_Data/PRISM/precipitation_database.csv";
        Map<String, String> MLfinalCH = new HashMap<>();
        Map<String, String> dataMap = new HashMap<>();
        Map<String, String> DataBaseH = new HashMap<>();
        
        CreatHashMap(MLfinalC, MLfinalCH, "the_geom", "premID");
        CreatHashMap(premiseBound, dataMap, "premID", "the_geom");
        CreatHashMap(DataBase, DataBaseH, "the_geom", "totalSum");
        
        // Print the HashMap (for verification)
//        for (Map.Entry<String, String> entry : DataBaseH.entrySet()) {
//        	String the_geom = entry.getKey();
//            for (Map.Entry<String, String> entry1 : DataBase.entrySet()) {
//            	Geojson geo = entry1.getKey() ;
//            	the_geom.intesect()
//                System.out.println("the_geom: " + entry1.getKey() + ", totalSum: " + entry1.getValue());
//            }
//        }
        
        
        WKTReader wktReader = new WKTReader();

        for (Map.Entry<String, String> entry : MLfinalCH.entrySet()) {
            String the_geom_wkt = entry.getKey();
            try {
                Geometry the_geom = wktReader.read(the_geom_wkt);

                for (Map.Entry<String, String> entry1 : DataBaseH.entrySet()) {
                    try {
                        Geometry geo = wktReader.read(entry1.getKey());

                        if (the_geom.intersects(geo)) {
//                            System.out.println("the_geom: " + entry1.getKey() + ", totalSum: " + entry1.getValue());
//                            System.out.println("the_geom: " + the_geom.toString()+ ", geo: " + geo.toString());
                        	String totalSum =entry1.getValue()+""; 
                        	if(totalSum.contentEquals("0.0")) {
                        		System.out.println(entry.getValue());
                        		System.out.println(dataMap.get(entry.getValue()));
//                        		totalSum  = getTotalSum(entry.getValue());
                        	}else {
                        		System.out.println("the_geom: " + the_geom+ ", totalSum:" + totalSum+"|");	
							}
                        	

                            break;
                        }
                    } catch (Exception e) {
//                        System.err.println("Error reading geometry from entry1: " + entry1.getKey());
//                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
//                System.err.println("Error reading geometry from entry: " + entry.getKey());
//                e.printStackTrace();
            }
        }

        
//        for (Map.Entry<String, String> entry : dataMap.entrySet()) {
//            System.out.println("premID: " + entry.getKey() + ", the_geom: " + entry.getValue());
//        }
//        for (Map.Entry<String, String> entry : PrecData.entrySet()) {
//            System.out.println("premID: " + entry.getKey() + ", the_geom: " + entry.getValue());
//        }
    }

	private static void CreatHashMap(String csvFilecsvFile, Map<String, String> dataMap,String keyCol,String valCol) {
		// TODO Auto-generated method stub
        try (CSVReader reader = new CSVReader(new FileReader(csvFilecsvFile))) {
            // Read the header line
            String[] headers = reader.readNext();
            if (headers == null) {
                System.out.println("Empty CSV file");
                return;
            }
            
            // Find indexes of required columns
            int premIDIndex = -1;
            int theGeomIndex = -1;
            
            for (int i = 0; i < headers.length; i++) {
                if (headers[i].trim().equalsIgnoreCase(keyCol)) {
                    premIDIndex = i;
                } else if (headers[i].trim().equalsIgnoreCase(valCol)) {
                    theGeomIndex = i;
                }
            }
            
            if (premIDIndex == -1 || theGeomIndex == -1) {
                System.out.println("Required columns not found in CSV");
                return;
            }
            
            // Read data and populate HashMap
            String[] values;
            while ((values = reader.readNext()) != null) {
                if (values.length > Math.max(premIDIndex, theGeomIndex)) {
                    dataMap.put(values[premIDIndex].trim(), values[theGeomIndex].trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

	}
}
