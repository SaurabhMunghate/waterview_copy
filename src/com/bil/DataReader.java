package com.bil;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DataReader {

    public static void main(String[] args) {
        String filePath = "/home/shatam-100/Down/WaterView_Data/PRISM_ppt_stable_4kmM3_20/all_bil/all_bil/test1.csv"; // Change this to your file path
        String targetValue1 = "444"; // The value you're looking for in the first column
        String targetValue2 = "306"; // The value you're looking for in the second column
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int column1Index = -1;
            int column2Index = -1;
            
            // Read header line to find the index of the columns
            if ((line = br.readLine()) != null) {
                String[] headers = line.split(",");
                // Assuming you're looking for specific headers for column 1 and column 2
                // If you need to refer to specific column names, modify this part
                for (int i = 0; i < headers.length; i++) {
                    if (headers[i].trim().equalsIgnoreCase("Row")) {
                        column1Index = i;
                    }
                    if (headers[i].trim().equalsIgnoreCase("Column")) {
                        column2Index = i;
                    }
                }
            }
            
            // Read data lines and check for matching values in column 1 and column 2
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (column1Index >= 0 && column2Index >= 0) {
                    // Check if column 1 is 444 and column 2 is 306
                    if (values[column1Index].equals(targetValue1) && values[column2Index].equals(targetValue2)) {
                        System.out.println("Found matching line: " + line);
                    }
                }
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
