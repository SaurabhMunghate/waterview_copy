package com.bil;
import java.io.*;
import java.util.regex.*;
import java.util.*;

public class ExtractZipFromCSV {
    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader("/home/shatam-100/Down/WaterView_Data/FTP_DATA/Data_Folder_2024-12/meter_locations_u1.csv"));
        BufferedWriter writer = new BufferedWriter(new FileWriter("/home/shatam-100/Down/WaterView_Data/FTP_DATA/Data_Folder_2024-12/meter_locations_u2.csv"));

        String header = reader.readLine();
        writer.write(header + ",zip\n");

        String line;
        while ((line = reader.readLine()) != null) {
            String[] columns = line.split(",");
            String theGeom = columns[0]; // Assuming 'the_geom' is the first column
            theGeom =theGeom.replace("POINT(", "").replace(")", "");
//            System.out.println(theGeom);
            String[] latLon = theGeom.split(" ");
            System.out.println(latLon[1]+latLon[0]);
            if (latLon != null) {
                String[] zip = U.getBingAddress(latLon[1]+"", latLon[0]+"");
                writer.write(line + "," + zip + "\n");
            }
        }
        reader.close();
        writer.close();
    }

    public static double[] extractLatLon(String pointStr) {
        Pattern pattern = Pattern.compile("POINT\\((-?\\d+\\.\\d+) (-?\\d+\\.\\d+)\\)");
        Matcher matcher = pattern.matcher(pointStr);
        if (matcher.find()) {
            return new double[]{Double.parseDouble(matcher.group(2)), Double.parseDouble(matcher.group(1))};
        }
        return null;
    }

//    public static String getBingAddress(double lat, double lon) {
//        // Simulating the function call
//        return "12345"; // Dummy ZIP code for testing
//    }
}
