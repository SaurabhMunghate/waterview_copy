package com.eto_consumption;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

public class GridData {

    public int ncols;
    public int nrows;
    public double xllcorner;
    public double yllcorner;
    public double cellsize;
    public double nodata_value;
    public double[][] data;

    // Constructor or initialization methods
    public GridData(int ncols, int nrows, double xllcorner, double yllcorner, double cellsize, double nodata_value, double[][] data) {
        this.ncols = ncols;
        this.nrows = nrows;
        this.xllcorner = xllcorner;
        this.yllcorner = yllcorner;
        this.cellsize = cellsize;
        this.nodata_value = nodata_value;
        this.data = data;
    }

    // Method to read the grid from an ASCII file
    public static GridData readAsciiGrid(String fileName, String epsg) throws IOException {
        // Assuming you are reading a standard ASCII grid file format
        File file = new File(fileName);
        if (!file.exists()) {
            throw new IOException("File not found: " + fileName);
        }

        // Read the ASCII grid header and data
        List<String> lines = Files.readAllLines(Paths.get(fileName));

        // Extract header values
        int ncols = Integer.parseInt(lines.get(0).split(" ")[1]);
        int nrows = Integer.parseInt(lines.get(1).split(" ")[1]);
        double xllcorner = Double.parseDouble(lines.get(2).split(" ")[1]);
        double yllcorner = Double.parseDouble(lines.get(3).split(" ")[1]);
        double cellsize = Double.parseDouble(lines.get(4).split(" ")[1]);
        double nodata_value = Double.parseDouble(lines.get(5).split(" ")[1]);

        // Read grid data
        double[][] data = new double[nrows][ncols];
        for (int i = 0; i < nrows; i++) {
            String[] row = lines.get(i + 6).split(" ");
            for (int j = 0; j < ncols; j++) {
                data[i][j] = Double.parseDouble(row[j]);
            }
        }

        // Return the grid data object
        return new GridData(ncols, nrows, xllcorner, yllcorner, cellsize, nodata_value, data);
    }

    // Method to get the value at specific coordinates (lat, lon)
    public Double getValueAtCoords(double lat, double lon) {
        // Calculate the x, y coordinates using the transformation logic
        // For simplicity, assuming the transformation has already been handled
        int col = (int) ((lon - this.xllcorner) / this.cellsize);
        int row = (int) ((this.yllcorner + this.nrows * this.cellsize - lat) / this.cellsize);

        // Check if the coordinates are within bounds
        if (row >= 0 && row < nrows && col >= 0 && col < ncols) {
            return data[row][col];
        } else {
            return this.nodata_value;
        }
    }
}
