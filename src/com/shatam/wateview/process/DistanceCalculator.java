package com.shatam.wateview.process;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DistanceCalculator {
    private static final double EARTH_RADIUS = 6371; // Earth's radius in kilometers

    public static void main(String[] args) {
//        double sourceLat = 34.079961819808105;
//        double sourceLon = -117.69653320312497;
        
//        double sourceLat = 34.09815885624053;
//        double sourceLon = -117.69653320312503;
    	
    	double sourceLat = 34.08512880679221;
        double sourceLon = -117.68586112723631;
        

        double destLat= 34.10461583228762;
		double destLon= -117.68538428993828;
		double distance = calculateDistance(sourceLat, sourceLon, destLat, destLon);
		System.out.println(distance);
//        try {
//            BufferedReader reader = new BufferedReader(new FileReader("/home/shatam-100/CIMISWeatherStationNetwork/StationData (another copy).csv"));
//            String line;
//
//            while ((line = reader.readLine()) != null) {
//                String[] parts = line.split(",");
//                double destLat = Double.parseDouble(parts[0]);
//                double destLon = Double.parseDouble(parts[1]);
//                System.out.println(destLat+"  "+destLon);
//
//                double distance = calculateDistance(sourceLat, sourceLon, destLat, destLon);
//                System.out.println("Distance: " + distance + " km");
//            }
//
//            reader.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private static double calculateDistance(double sourceLat, double sourceLon, double destLat, double destLon) {
        double latDistance = Math.toRadians(destLat - sourceLat);
        double lonDistance = Math.toRadians(destLon - sourceLon);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(sourceLat)) * Math.cos(Math.toRadians(destLat))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }
}
