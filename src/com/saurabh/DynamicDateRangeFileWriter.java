package com.saurabh;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class DynamicDateRangeFileWriter {
    public static void main(String[] args) throws IOException, ParseException {
        ObjectMapper objectMapper = new ObjectMapper();

        // Sample JSON array
        ArrayNode jsonArray = objectMapper.createArrayNode();

        // Add your JSON data to jsonArray

        Set<String> uniqueDates = getUniqueDates(jsonArray);

        for (String dateStr : uniqueDates) {
            Date date = new SimpleDateFormat("yyyy-MM").parse(dateStr);

            String outputFileName = "output_" + dateStr.replace("-", "_") + ".json";

            // Create a new array node for the specific date range
            ArrayNode outputArray = objectMapper.createArrayNode();

            // Filter the JSON data for the current date range
            for (JsonNode jsonNode : jsonArray) {
                String dateString = jsonNode.get("Date").asText();
                Date currentDate = new SimpleDateFormat("yyyy-MM").parse(dateString);

                if (isSameMonthYear(date, currentDate)) {
                    outputArray.add(jsonNode);
                }
            }

            // Write the filtered data to the output file
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(outputFileName), outputArray);
        }
    }

    private static Set<String> getUniqueDates(ArrayNode jsonArray) {
        Set<String> uniqueDates = new HashSet<>();

        for (JsonNode jsonNode : jsonArray) {
            String dateString = jsonNode.get("Date").asText();
            uniqueDates.add(dateString);
        }

        return uniqueDates;
    }

    private static boolean isSameMonthYear(Date date1, Date date2) {
        SimpleDateFormat monthYearFormat = new SimpleDateFormat("yyyy-MM");
        return monthYearFormat.format(date1).equals(monthYearFormat.format(date2));
    }
}
