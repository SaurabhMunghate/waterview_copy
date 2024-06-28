package com.saurabh;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KeyOfGeom {
    public static void main(String[] args) {
        String polygonString = "POLYGON ((-122.080078125 37.35269280367274, -122.10205078125 37.35269280367274, -122.10205078125 37.33522435930638, -122.080078125 37.33522435930638, -122.080078125 37.35269280367274))";

        // Extract coordinates using regex
        Pattern pattern = Pattern.compile("-?\\d+\\.\\d+");
        Matcher matcher = pattern.matcher(polygonString);

        // Extract x, y, x1, y1 coordinates
        double x = 0, y = 0, x1 = 0, y1 = 0;
        int i = 0;
        while (matcher.find()) {
            double coordinate = Double.parseDouble(matcher.group());
            switch (i) {
                case 0:
                    x = coordinate;
                    break;
                case 1:
                    y = coordinate;
                    break;
                case 2:
                    x1 = coordinate;
                    break;
                case 3:
                    y1 = coordinate;
                    break;
            }
            i++;
        }

        // Form key using coordinates
        String key = String.format("%.6f_%.6f_%.6f_%.6f", x, y, x1, y1);
        System.out.println("Key: " + key);
    }
}
