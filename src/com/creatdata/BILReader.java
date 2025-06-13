package com.creatdata;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class BILReader {
    // Constants from the header file
    private static final int NROWS = 621;
    private static final int NCOLS = 1405;
    private static final float ULXMAP = -125.0f;
    private static final float ULYMAP = 49.916666666667f;
    private static final float XDIM = 0.041666666667f;
    private static final float YDIM = 0.041666666667f;
    private static final float NODATA = -9999f;
    private static final int BYTES_PER_VALUE = 4; // 32-bit float

    public static float getPrecipitation( double latitude, 
            double longitude) throws IOException {
    	String bilFilePath = "/home/shatam-100/Downloads/Downloads_WV/PRISM_ppt_stable_4kmM3_202401_bil/PRISM_ppt_stable_4kmM3_202401_bil.bil";

        try (RandomAccessFile randomAccessFile = new RandomAccessFile(bilFilePath, "r")) {
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
    }

    public static void main(String[] args) {
        try {
            // Your local BIL file path
//            String bilFilePath = "/home/shatam-100/Downloads/Downloads_WV/PRISM_ppt_stable_4kmM3_202401_bil/PRISM_ppt_stable_4kmM3_202401_bil.bil";
            
            // Example coordinates (Portland, OR) //33.536625290412694, -117.70330631421919
//            double lat = 45.5231;
//            double lon = -122.6765;

            double lat = 33.536625290412694;
            double lon = -117.70330631421919;

            
            float precip = getPrecipitation(lat, lon);
            
            if (precip != NODATA) {
                System.out.printf("Precipitation at %.4f, %.4f: %.2f mm%n", 
                    lat, lon, precip);
            } else {
                System.out.println("No data available for these coordinates");
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}