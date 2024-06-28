package com.saurabh;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.io.WKTWriter;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import java.io.File;
import java.io.IOException;

public class ShapefileToWKTConverter_1 {

    public static void main(String[] args) throws IOException, MismatchedDimensionException, TransformException {
        String shapefilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries/banning/AOI.shp";

        // Load the shapefile
        File shapefile = new File(shapefilePath);
        ShapefileDataStore dataStore = new ShapefileDataStore(shapefile.toURI().toURL());
        SimpleFeatureIterator features = dataStore.getFeatureSource().getFeatures().features();

        // Define a GeometryFactory and WKTWriter
        GeometryFactory geometryFactory = new GeometryFactory();
        WKTWriter wktWriter = new WKTWriter();

        // Get the default CRS of the shapefile
        CoordinateReferenceSystem sourceCRS = dataStore.getSchema().getCoordinateReferenceSystem();

        // Get the transform to WGS84 CRS
        MathTransform transform;
        try {
            transform = CRS.findMathTransform(sourceCRS, CRS.decode("EPSG:4326"), true);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // Iterate over features
        while (features.hasNext()) {
            SimpleFeature feature = features.next();

            // Get geometry attribute
            Geometry geometry = (Geometry) feature.getDefaultGeometry();

            // Convert the geometry to WKT
            String wkt = convertGeometryToWKT(geometry, geometryFactory, wktWriter);

            // Output WKT
            System.out.println("WKT for Feature ID " + feature.getID() + ":");
            System.out.println(wkt);
        }

        // Close the feature iterator and data store
        features.close();
        dataStore.dispose();
    }

    private static String convertGeometryToWKT(Geometry geometry, GeometryFactory geometryFactory, WKTWriter wktWriter) {
        // Convert geometry to WKT string
        String wkt = wktWriter.write(geometry);

        // Split the WKT string by space
        String[] tokens = wkt.split("\\s+");

        // Swap the latitude and longitude coordinates
        StringBuilder sb = new StringBuilder();
        for (String token : tokens) {
            String[] coords = token.split(",");
            if (coords.length == 2) {
                sb.append(coords[1]).append(" ").append(coords[0]).append(",");
            } else {
                sb.append(token).append(" ");
            }
        }

        // Remove the trailing comma
        sb.setLength(sb.length() - 1);

        return sb.toString();
    }
}
