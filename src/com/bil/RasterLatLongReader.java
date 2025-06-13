package com.bil;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.referencing.CRS;
import org.opengis.coverage.grid.GridCoordinates2D;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

import java.io.File;

public class RasterLatLongReader {

    public static void main(String[] args) {
        try {
            // Path to the .bil file
            File rasterFile = new File("path_to_your_file.bil");

            // Find the appropriate format
            AbstractGridFormat format = GridFormatFinder.findFormat(rasterFile);
            GridCoverage2DReader reader = format.getReader(rasterFile);

            // Read the coverage
            GridCoverage2D coverage = reader.read(null);

            // Get the CRS (Coordinate Reference System)
            CoordinateReferenceSystem crs = coverage.getCoordinateReferenceSystem2D();

            // Get the raster envelope (bounding box) and grid dimensions
            GridEnvelope2D gridRange = coverage.getGridGeometry().getGridRange2D();

            // Transform between grid and world coordinates
            MathTransform gridToCRS = coverage.getGridGeometry().getGridToCRS();

            // Loop through all rows and columns
            for (int row = 0; row < gridRange.height; row++) {
                for (int col = 0; col < gridRange.width; col++) {
                    // Convert grid coordinates (row, col) to real-world coordinates (lat, long)
                    GridCoordinates2D gridCoord = new GridCoordinates2D(col, row);
                    DirectPosition2D worldCoord = new DirectPosition2D();
                    gridToCRS.transform(gridCoord, worldCoord);

                    // Print row, column, latitude, and longitude
                    System.out.printf("Row: %d, Column: %d, Latitude: %.6f, Longitude: %.6f%n",
                            row, col, worldCoord.y, worldCoord.x);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
