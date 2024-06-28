package com.saurabh;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.shatam.utils.U;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTReader;

public class CSVMatcher {
    public static void main(String[] args) throws Exception {
        String firstCsvFile = "/home/shatam-100/ResidentialWaterView/riversideCounty.csv";
        String secondCsvFile = "/home/shatam-100/ResidentialWaterView/Riverside_County_Data1 (copy).csv";

        Set<String> firstColumnData = getColumnData(firstCsvFile, 0);
        Set<String> secondColumnData = getColumnData(secondCsvFile, 1);

        Set<Geometry> tiles = new HashSet<>();
        WKTReader wktReader = new WKTReader();
        
        System.out.println(secondColumnData.size());
        System.out.println(firstColumnData.size());
        BufferedReader br = new BufferedReader(new FileReader(secondCsvFile));
        String line;
        while ((line = br.readLine()) != null) {
        	if(line.contains("the_geom")) continue;
        	line = U.getSectionValue(line, "POLYGON", "))");
        	line = "POLYGON"+line+"))";
        	if(line.contains("null"))return;
        	try {
        		tiles.add(wktReader.read(line.trim()));
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}
        System.out.println(tiles.size());
        // Find the matching data
//        Set<String> matchedData = new HashSet<>(firstColumnData);
//        matchedData.retainAll(secondColumnData);

        // Print the matched data
//        for (String item : matchedData) {
//            System.out.println(item);
//        }
    }

    private static Set<String> getColumnData(String csvFile, int columnIndex) {
        Set<String> columnData = new HashSet<>();
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                String[] columns = line.split(",");
                if (columns.length > columnIndex) {
                    columnData.add(columns[columnIndex]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return columnData;
    }
}
