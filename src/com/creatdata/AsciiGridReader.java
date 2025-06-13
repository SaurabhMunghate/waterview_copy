package com.creatdata;
//import org.locationtech.proj4j.*;

import ucar.nc2.dataset.CoordinateTransform;

import java.io.*;
import java.util.*;

public class AsciiGridReader {
    static class Grid {
        int ncols;
        int nrows;
        double xllcorner;
        double yllcorner;
        double cellsize;
        double nodata_value;
        double[][] data;
        CoordinateTransform transformer;

        public Grid(int ncols, int nrows, double xllcorner, double yllcorner, 
                   double cellsize, double nodata_value, double[][] data, 
                   CoordinateTransform transformer) {
            this.ncols = ncols;
            this.nrows = nrows;
            this.xllcorner = xllcorner;
            this.yllcorner = yllcorner;
            this.cellsize = cellsize;
            this.nodata_value = nodata_value;
            this.data = data;
            this.transformer = transformer;
        }
    }

    public static Grid readAsciiGrid(String filePath, int dstEpsg) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            // Read header
            int ncols = Integer.parseInt(reader.readLine().split("\\s+")[1]);
            int nrows = Integer.parseInt(reader.readLine().split("\\s+")[1]);
            double xllcorner = Double.parseDouble(reader.readLine().split("\\s+")[1]);
            double yllcorner = Double.parseDouble(reader.readLine().split("\\s+")[1]);
            double cellsize = Double.parseDouble(reader.readLine().split("\\s+")[1]);
            double nodata_value = Double.parseDouble(reader.readLine().split("\\s+")[1]);

            // Read data
            double[][] data = new double[nrows][ncols];
            String line;
            int row = 0;
            while ((line = reader.readLine()) != null && row < nrows) {
                String[] values = line.trim().split("\\s+");
                for (int col = 0; col < ncols && col < values.length; col++) {
                    data[row][col] = Double.parseDouble(values[col]);
                }
                row++;
            }

            // Create transformer
            CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();
            CRSFactory crsFactory = new CRSFactory();

            CoordinateReferenceSystem sourceCRS = crsFactory.createFromName("EPSG:4326");
            CoordinateReferenceSystem targetCRS = crsFactory.createFromName("EPSG:" + dstEpsg);
            CoordinateTransform transformer = ctFactory.createTransform(sourceCRS, targetCRS);

            return new Grid(ncols, nrows, xllcorner, yllcorner, cellsize, 
                          nodata_value, data, transformer);
        }
    }

    public static Double getValueAtCoords(Grid grid, double lat, double lon) {
        // Transform coordinates
        ProjCoordinate sourceCoord = new ProjCoordinate(lon, lat);
        ProjCoordinate targetCoord = new ProjCoordinate();
        grid.transformer.transform(sourceCoord, targetCoord);

        // Calculate row and column indices
        int col = (int)((targetCoord.x - grid.xllcorner) / grid.cellsize);
        int row = (int)((grid.yllcorner + grid.nrows * grid.cellsize - targetCoord.y) / 
                       grid.cellsize);

        // Check if indices are within bounds
        if (row >= 0 && row < grid.nrows && col >= 0 && col < grid.ncols) {
            return grid.data[row][col];
        } else {
            return null; // or return grid.nodata_value
        }
    }

    public static void main(String[] args) {
        try {
            String filePath = "/home/shatam-100/Down/WaterView_Data/FTP_DATA/January/ETo.asc";
            int dstEpsg = 3310; // California Albers Equal Area
            Grid grid = readAsciiGrid(filePath, dstEpsg);

            double latitude = 33.536625290412694;  // Example latitude (So Cal)
            double longitude = -117.70330631421919; // Example longitude (So Cal)

            Double value = getValueAtCoords(grid, latitude, longitude);
            System.out.printf("Value at (%f, %f): %s%n", latitude, longitude, value);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}