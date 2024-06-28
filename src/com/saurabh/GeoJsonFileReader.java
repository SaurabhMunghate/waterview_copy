package com.saurabh;
import java.io.*;
import java.nio.file.*;
import java.util.stream.*;

import com.opencsv.CSVWriter;

public class GeoJsonFileReader {
    // Specify input and output directories
    static String inputDirectory = "/home/shatam-100/Down/WaterView_Data/Tiles/";
    static String outputFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/output_file_path.txt";

    static CSVWriter writercsv;

    // Static initialization block to initialize writercsv
    static {
        try {
            writercsv = new CSVWriter(new FileWriter(new File(outputFilePath.replace(".txt", ".csv"))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }   

    public static void main(String[] args) throws IOException {
    	 String[] header = { "Water_district", "the_geom" }; 
    	    writercsv.writeNext(header); 
        // Collect all files with ".geojson" extension and read them as strings
        try (Stream<Path> paths = Files.walk(Paths.get(inputDirectory))) {
            String combinedGeoJson = paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".geojson"))
                    .map(GeoJsonFileReader::readFileAsString)
                    .collect(Collectors.joining("\n"));

            // Write combined contents to the output file
            writeStringToFile(outputFilePath, combinedGeoJson, writercsv);
            writercsv.close(); 
            System.out.println("Combined GeoJSON files written to: " + outputFilePath.length());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String readFileAsString(Path path) {
        try {
        	System.out.println(path);
            byte[] bytes = Files.readAllBytes(path);
//            System.out.println(new String(bytes));
            String districtname = path+"";
            districtname = districtname.replace("/home/shatam-100/Down/WaterView_Data/Tiles/", "")
            		.replace("_boundary.geojson", "")
            		.replace("_Boundary.geojson", "")
            		.replace("_Water_District", "");
            String[] data2 = { districtname, new String(bytes).trim() }; 
            writercsv.writeNext(data2);
            return new String(bytes);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private static void writeStringToFile(String filePath, String content, CSVWriter writercsv) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(","+content);
            
        }
    }
}
