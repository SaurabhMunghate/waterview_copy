package com.saurabh;
public class DistanceCalculator {
    
    public static final double EARTH_RADIUS_KM = 6371.0; // Earth radius in kilometers
    
    public static void main(String[] args) {
//        double lat1 = 37.33522435930638; // Latitude of point 1
//        double lon1 = -121.92626953125001; // Longitude of point 1
//        double lat2 = 37.33522435930638; // Latitude of point 2
//        double lon2 = -121.94824218749999; // Longitude of point 2
    	
        double lat1 = 37.33; // Latitude of point 1
        double lon1 = -121.92; // Longitude of point 1
        double lat2 = 37.33; // Latitude of point 2
        double lon2 = -121.91; // Longitude of point 2

        
        double distance = calculateDistance(lat1, lon1, lat2, lon2);
        
        System.out.println("Distance between the points: " + distance + " kilometers");
    }
    
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Convert latitude and longitude from degrees to radians
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);
        
        // Haversine formula
        double dlon = lon2Rad - lon1Rad;
        double dlat = lat2Rad - lat1Rad;
        double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.pow(Math.sin(dlon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = EARTH_RADIUS_KM * c;
        
        return distance;
    }
}
