package com.saurabh;
import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class GeometryCoverCheck {
    public static void main(String[] args) {
        try {
            // Create a WKTReader to parse geometries from WKT format
            WKTReader reader = new WKTReader();

            // Example geometries to check
            String geometry1 = "POLYGON((-117.8173828125 34.12544756511612,-117.861328125 34.12544756511612,-117.861328125 34.089061315849946,-117.8173828125 34.12544756511612))";
            String geometry2 = "POLYGON((-117.830078125 34.120141154517495,-117.85595703125 34.120141154517495,-117.85595703125 34.10386818326174,-117.830078125 34.120141154517495))";

            // Parse the geometries
            Geometry geom1 = reader.read(geometry1);
            Geometry geom2 = reader.read(geometry2);

            // Check if geom1 covers geom2
            boolean covers = geom1.covers(geom2);

            // Print the result
            System.out.println("Geometry 1 covers Geometry 2: " + covers);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
