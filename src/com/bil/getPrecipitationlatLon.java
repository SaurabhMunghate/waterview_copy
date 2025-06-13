package com.bil;

import java.io.IOException;
import java.io.RandomAccessFile;

public class getPrecipitationlatLon {
    // Constants from the header file
    private static final int NROWS = 621;
    private static final int NCOLS = 1405;
    private static final float ULXMAP = -125.0f;
    private static final float ULYMAP = 49.916666666667f;
    private static final float XDIM = 0.041666666667f;
    private static final float YDIM = 0.041666666667f;
    private static final float NODATA = -9999f;
    private static final int BYTES_PER_VALUE = 4; // 32-bit float

    /**
     * Reads precipitation value from a BIL file for given coordinates
     * 
     * @param randomAccessFile An already opened RandomAccessFile handle to the BIL file
     * @param latitude The latitude coordinate
     * @param longitude The longitude coordinate
     * @return The precipitation value as a float, or NODATA if coordinates are out of bounds
     * @throws IOException If there's an error reading the file
     */
    public static float getPrecipitation(RandomAccessFile randomAccessFile, double latitude, 
            double longitude) throws IOException {
        
        // Check if coordinates are within bounds
        if (longitude < ULXMAP || 
            longitude > ULXMAP + (NCOLS * XDIM) ||
            latitude > ULYMAP || 
            latitude < ULYMAP - (NROWS * YDIM)) {
            return NODATA;
        }

        // Calculate pixel coordinates
        int col = (int)Math.round((longitude - ULXMAP) / XDIM);
        int row = (int)Math.round((ULYMAP - latitude) / YDIM);

        // Ensure we don't exceed array bounds
        col = Math.min(Math.max(col, 0), NCOLS - 1);
        row = Math.min(Math.max(row, 0), NROWS - 1);

        // Calculate file position
        long position = (long)row * NCOLS * BYTES_PER_VALUE + (long)col * BYTES_PER_VALUE;
        
        // Seek to position and read the float value
        randomAccessFile.seek(position);
        float value = randomAccessFile.readFloat();
        System.out.println(row+"  "+col);
        return value == NODATA ? NODATA : value;
    }

    // Example usage
    public static void main(String[] args) {
        try (RandomAccessFile raf = new RandomAccessFile("/home/shatam-100/Down/WaterView_Data/PRISM_ppt_stable_4kmM3_20/all_bil/PRISM_ppt_stable_4kmM3_2019_all_bil/PRISM_ppt_stable_4kmM3_201901_bil.bil", "r")) {
            double lat = 41.166668;  // Example: Portland, OR
            double lon = -122.708336;
            
            float precip = getPrecipitation(raf, lat, lon);
            
            if (precip != NODATA) {
                System.out.printf("1 Precipitation at %.4f, %.4f: %.2f%n", lat, lon, precip);
            } else {
                System.out.println("No data available for these coordinates");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}