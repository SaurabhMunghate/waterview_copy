package com.saurabh;
public class Main {
    public static void main(String[] args) {
        String polygonString = "POLYGON ((-122.080078125 37.35269280367274, -122.10205078125 37.35269280367274, -122.10205078125 37.33522435930638, -122.080078125 37.33522435930638, -122.080078125 37.35269280367274))";

        // Remove "POLYGON ((" and "))" from the string
        String cleanPolygonString = polygonString.substring(10, polygonString.length() - 2);

        // Split the coordinates by comma and space
        String[] coordinates = cleanPolygonString.split(", ");

        // Extract x, y, x1, y1 coordinates
        double x = Double.parseDouble(coordinates[0].split(" ")[0]);
        double y = Double.parseDouble(coordinates[0].split(" ")[1]);
        double x1 = Double.parseDouble(coordinates[2].split(" ")[0]);
        double y1 = Double.parseDouble(coordinates[2].split(" ")[1]);

        // Form key using coordinates
        String key = String.format("%.6f_%.6f_%.6f_%.6f", x, y, x1, y1);
        System.out.println("Key: " + key);
    }
}
