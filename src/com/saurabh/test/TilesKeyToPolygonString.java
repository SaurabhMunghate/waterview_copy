package com.saurabh.test;
public class TilesKeyToPolygonString {

    public static void main(String[] args) {
        String tiles_key = "-117.53173828125_33.90689555128867_-117.5537109375_33.88865750124074"; // Replace with your tiles_key
        
        // Split the tiles_key to extract coordinates
        String[] coordinates = tiles_key.split("_");
        double x = Double.parseDouble(coordinates[0]);
        double y = Double.parseDouble(coordinates[1]);
        double x1 = Double.parseDouble(coordinates[2]);
        double y1 = Double.parseDouble(coordinates[3]);
        String polygonString = "POLYGON ((" + x + " " + y + ", " + x1 + " " + y + ", " + x1 + " " + y1 + ", " + x + " " + y1 + ", " + x + " " + y + "))";
        System.out.println("Polygon String: " + polygonString);
        
        
        // Form the polygon string
//        String polygonString = String.format("POLYGON ((%.6f %.6f, %.6f %.6f, %.6f %.6f, %.6f %.6f, %.6f %.6f))",
//                                              x, y,
//                                              x1, y,
//                                              x1, y1,
//                                              x, y1,
//                                              x, y);
        

        
    }
}
