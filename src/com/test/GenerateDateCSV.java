package com.test;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class GenerateDateCSV {

    public static void main(String[] args) {
        // Define start and end dates
        LocalDate startDate = LocalDate.of(1991, 1, 1);
        LocalDate endDate = LocalDate.of(2020, 12, 31);

        // Define CSV file output path
//        String outputFile = "/home/shatam-100/Down/WaterView_Data/Precipitation_Work/output_with_normal_copy.csv";
        String outputFile = "/home/shatam-100/Down/WaterView_Data/Precipitation_Work/output_with_normal_copy_1.csv";

        // Date format 
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try (FileWriter writer = new FileWriter(outputFile)) {
            // Write header
            writer.append("Date\n");

            // Loop through each day and write to CSV
            LocalDate currentDate = startDate;
            while (!currentDate.isAfter(endDate)) {
                writer.append(currentDate.format(formatter)).append("\n");
                currentDate = currentDate.plusDays(1);
            }

            System.out.println("CSV file created successfully: " + outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
