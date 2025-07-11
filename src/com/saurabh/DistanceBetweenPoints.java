package com.saurabh;
import java.util.Scanner;

public class DistanceBetweenPoints {

    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);

//        System.out.print("Enter the latitude of the first point: ");
//        double latitude1 = scanner.nextDouble();
//
//        System.out.print("Enter the longitude of the first point: ");
//        double longitude1 = scanner.nextDouble();
//
//        System.out.print("Enter the latitude of the second point: ");
//        double latitude2 = scanner.nextDouble();
//
//        System.out.print("Enter the longitude of the second point: ");
//        double longitude2 = scanner.nextDouble();

//    	Latitude: 34.054936781229486
//    	Longitude: -117.70750983403565
    	//- , 
    	//-121.87545776367188 40.00026797264675 -121.24649047851561 40.00237193587648
        double latitude1 = 40.00237193587648 ;
        double longitude1 = -121.24649047851561;
        double latitude2= 40.00026797264675;
        double longitude2 = -121.87545776367188;
		double distance = calculateDistance(latitude1, longitude1, latitude2, longitude2);

        System.out.println("The distance between the two points is " + distance + " kilometers.");
    }

    private static double calculateDistance(double latitude1, double longitude1, double latitude2, double longitude2) {
        final double R = 6371; // Radius of the earth in kilometers

        double latDistance = Math.toRadians(latitude2 - latitude1);
        double lonDistance = Math.toRadians(longitude2 - longitude1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(Math.toRadians(latitude1)) * Math.cos(Math.toRadians(latitude2)) *
                        Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }
}
