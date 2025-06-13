package com.bil;

import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GetPrecipitationLatLonCSV {
    // Constants from the header file
    private static final int NROWS = 621;
    private static final int NCOLS = 1405;
    private static final float ULXMAP = -125.0f;
    private static final float ULYMAP = 49.916666666667f;
    private static final float XDIM = 0.041666666667f;
    private static final float YDIM = 0.041666666667f;
    private static final float NODATA = -9999f;
    private static final int BYTES_PER_VALUE = 4; // 32-bit float

    public static void main(String[] args) throws ParseException {
        String bilFilePath = "/home/shatam-100/Down/WaterView_Data/PRISM_ppt_stable_4kmM3_20/all_bil/PRISM_ppt_stable_4kmM3_2019_all_bil/PRISM_ppt_stable_4kmM3_201901_bil.bil";
        String csvFilePath = "/home/shatam-100/Down/WaterView_Data/PRISM_ppt_stable_4kmM3_20/all_bil/precipitation_data1.csv";

        try (RandomAccessFile raf = new RandomAccessFile(bilFilePath, "r");
             FileWriter csvWriter = new FileWriter(csvFilePath)) {

            // Write the header row to the CSV file
            csvWriter.write("Row,Column,Date,Precipitation\n");

            for (int row = 0; row < NROWS; row++) {
                for (int col = 0; col < NCOLS; col++) {
                    // Calculate latitude and longitude for the current row and column
                    double latitude = ULYMAP - (row * YDIM);
                    double longitude = ULXMAP + (col * XDIM);

                    // Calculate the file position
                    long position = (long) row * NCOLS * BYTES_PER_VALUE + (long) col * BYTES_PER_VALUE;

                    // Read the precipitation value
                    raf.seek(position);
                    float value = raf.readFloat();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");  // Define the format
                    Date date = dateFormat.parse("2019-01");  // Parse the string into a Date object

                    // Format the Date object as a string in the desired format
                    String Date = dateFormat.format(date);

                    // Write the row, column, latitude, longitude, and precipitation to the CSV
                    csvWriter.write(String.format("%d,%d,%s,%.2f\n", row, col, Date, value));

                }
            }

            System.out.println("CSV file generated successfully: " + csvFilePath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
