package com.bil;
import java.io.*;

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

    /**
     * Reads and writes all rows, columns, and values to a CSV file.
     * 
     * @param randomAccessFile An already opened RandomAccessFile handle to the BIL file
     * @param csvFilePath Path to the output CSV file
     * @throws IOException If there's an error reading the file or writing to the CSV
     */
    public static void writeAllDataToCSV(RandomAccessFile randomAccessFile, String csvFilePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath))) {
            // Write the header row
            writer.write("Row,Column,Value\n");

            // Loop through all rows and columns
            for (int row = 0; row < NROWS; row++) {
                for (int col = 0; col < NCOLS; col++) {
                    // Calculate file position
                    long position = (long)row * NCOLS * BYTES_PER_VALUE + (long)col * BYTES_PER_VALUE;

                    // Seek to position and read the float value
                    randomAccessFile.seek(position);
                    float value = randomAccessFile.readFloat();

                    // Write the row, column, and value to the CSV
                    writer.write(String.format("%d,%d,%.2f\n", row, col, value));
                }
            }
        }

        System.out.println("Data successfully written to: " + csvFilePath);
    }
    

    // Example usage
    public static void main(String[] args) {
//        String bilFilePath = "/home/shatam-100/Down/WaterView_Data/PRISM_ppt_stable_4kmM3_20/all_bil/PRISM_ppt_stable_4kmM3_2019_all_bil/PRISM_ppt_stable_4kmM3_201901_bil.bil"; // Path to the .bil file
//        String csvFilePath = "/home/shatam-100/Down/WaterView_Data/PRISM_ppt_stable_4kmM3_20/all_bil/precipitation_data.csv"; // Path to the output CSV file

        String bilFilePath = "/home/shatam-100/Down/WaterView_Data/PRISM_ppt_stable_4kmM3_20/PRISM_ppt_provisional_4kmM3_202502_bil/PRISM_ppt_provisional_4kmM3_202502_bil.bil"; // Path to the .bil file
        String csvFilePath = "/home/shatam-100/Down/WaterView_Data/PRISM_ppt_stable_4kmM3_20/all_bil/all_bil_25/PRISM_ppt_stable_4kmM3_202502_bil.csv"; // Path to the output CSV file

        try (RandomAccessFile raf = new RandomAccessFile(bilFilePath, "r")) {
            // Write all rows, columns, and values to the CSV file
            writeAllDataToCSV(raf, csvFilePath);
            // Get the value at the specified row and column

        } catch (IOException e) {
            e.printStackTrace();
        }
        

        
    }
}
