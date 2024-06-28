package com.saurabh;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DateRangeFileWriter {
    public static void main(String[] args) throws IOException, ParseException {
        ObjectMapper objectMapper = new ObjectMapper();

        // Read data from CSV file
        List<String> lines = Files.readAllLines(Paths.get("/home/shatam-100/Cache/WVSANJOSE_res_consumption_Eto_11_March_c2.csv"));

        // Sample CSV format: Date,Value,OtherColumn
        for (String line : lines) {
        	if(line.contains("Date"))continue;
            String[] parts = line.split(",");
            String dateString = parts[0].replace("\"", ""); // Assuming the date is in the first column
            Date date = parseDate(dateString);

            // Specify the date ranges
            Date startDate2021 = parseDate("2021-01");
            Date endDate2021 = parseDate("2021-12");
            Date startDate2022 = parseDate("2022-01");
            Date endDate2022 = parseDate("2022-12");

            if (isDateInRange(date, startDate2021, endDate2021)) {
                writeToFile(line, "/home/shatam-100/Cache/"+"output_2021.csv");
            } else if (isDateInRange(date, startDate2022, endDate2022)) {
                writeToFile(line, "/home/shatam-100/Cache/"+"output_2022.csv");
            }
            // Add more conditions for other date ranges if needed
        }
    }

    private static boolean isDateInRange(Date date, Date startDate, Date endDate) {
        return date.compareTo(startDate) >= 0 && date.compareTo(endDate) <= 0;
    }

    private static void writeToFile(String line, String fileName) throws IOException {
        Files.write(Paths.get(fileName), (line + System.lineSeparator()).getBytes(), java.nio.file.StandardOpenOption.CREATE, java.nio.file.StandardOpenOption.APPEND);
    }

    private static Date parseDate(String dateString) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        return dateFormat.parse(dateString);
    }
    }