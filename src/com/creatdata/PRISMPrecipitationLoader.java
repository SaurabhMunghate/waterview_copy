package com.creatdata;
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.*;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.zip.GZIPInputStream;

public class PRISMPrecipitationLoader {
    // Constants from the header file
    private static final int NROWS = 621;
    private static final int NCOLS = 1405;
    private static final float ULXMAP = -125.0f;
    private static final float ULYMAP = 49.916666666667f;
    private static final float XDIM = 0.041666666667f;
    private static final float YDIM = 0.041666666667f;
    private static final float NODATA = -9999f;
    private static final int BYTES_PER_VALUE = 4; // 32-bit float

    private static final String BASE_URL = "https://ftp.prism.oregonstate.edu/monthly/ppt/2024/";
    
    /**
     * Downloads and processes precipitation data for a specific year and month
     */
    public static File downloadPrecipitationData(int year, int month) throws IOException {
        YearMonth ym = YearMonth.of(year, month);
        String filename = String.format("PRISM_ppt_stable_4kmM3_%04d%02d_bil", year, month);
        String gzipFilename = filename + ".gz";
        String urlString = BASE_URL + gzipFilename;
        
        // Create temp directory if it doesn't exist
        Path tempDir = Paths.get("temp");
        Files.createDirectories(tempDir);
        
        // Download and decompress file
        Path outputPath = tempDir.resolve(filename);
        if (!Files.exists(outputPath)) {
            System.out.println("Downloading " + urlString);
            URL url = new URL(urlString);
            
            try (InputStream in = new GZIPInputStream(url.openStream());
                 OutputStream out = Files.newOutputStream(outputPath)) {
                byte[] buffer = new byte[8192];
                int len;
                while ((len = in.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                }
            }
        }
        
        return outputPath.toFile();
    }

    /**
     * Reads precipitation value from a BIL file for given coordinates
     * @return Precipitation in millimeters, or NODATA if coordinates are out of bounds
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
        
        // Read the bytes
        randomAccessFile.seek(position);
        byte[] bytes = new byte[4];
        randomAccessFile.readFully(bytes);
        
        // Convert bytes to float using little-endian byte order
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.order(ByteOrder.LITTLE_ENDIAN);  // Intel byte ordering
        float value = buffer.getFloat();
        
        return value == NODATA ? NODATA : value;
    }

    /**
     * Gets precipitation for a specific location and month
     * @return Precipitation in millimeters
     */
    public static float getPrecipitationForMonth(double latitude, double longitude, 
            int year, int month) throws IOException {
        File bilFile = downloadPrecipitationData(year, month);
        
        try (RandomAccessFile raf = new RandomAccessFile(bilFile, "r")) {
            return getPrecipitation(raf, latitude, longitude);
        }
    }

    public static void main(String[] args) {
        try {
            // Example: Get precipitation for Portland, OR for January 2024
            double lat = 45.5231;
            double lon = -122.6765;
            int year = 2024;
            int month = 1;

            float precip = getPrecipitationForMonth(lat, lon, year, month);
            
            if (precip != NODATA) {
                System.out.printf("Precipitation at %.4f, %.4f for %d-%02d: %.2f mm%n", 
                    lat, lon, year, month, precip);
            } else {
                System.out.println("No data available for these coordinates");
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}