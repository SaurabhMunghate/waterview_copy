package com.test;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ExpandCSVWithDates {

    public static void main(String[] args) {
//        String inputFile = "/home/shatam-100/Down/WaterView_Data/Precipitation_Work/output_with_normal_copy.csv";          // your input CSV
        String inputFile = "/home/shatam-100/Down/WaterView_Data/Precipitation_Work/output_with_normal_copy_1.csv";          // your input CSV

//        String outputFile = "/home/shatam-100/Down/WaterView_Data/Precipitation_Work/stations_with_dates.csv"; // output CSV
        String outputFile = "/home/shatam-100/Down/WaterView_Data/Precipitation_Work/stations_with_dates_1.csv"; // output CSV


//        LocalDate startDate = LocalDate.of(1991, 1, 1);
//        LocalDate endDate = LocalDate.of(2020, 12, 31);

        LocalDate startDate = LocalDate.of(2021, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 4, 30);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try (
            BufferedReader br = new BufferedReader(new FileReader(inputFile));
            FileWriter writer = new FileWriter(outputFile)
        ) {
            String headerLine = br.readLine(); // read the header
            if (headerLine == null) {
                System.out.println("Empty input file.");
                return;
            }

            // Add "Date" column to header
            writer.append(headerLine).append(",Date\n");

            List<String> stationRows = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                stationRows.add(line);
            }

            // Loop through each date and write each station with that date
            for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                for (String station : stationRows) {
                    writer.append(station).append(",").append(date.format(formatter)).append("\n");
                }
            }

            System.out.println("Expanded CSV created: " + outputFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
