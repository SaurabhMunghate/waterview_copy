package com.saurabh;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DatePairExample {
    public static void main(String[] args) {
        String startYear = "2019-01-01";
        String endYear = "2024-01-01";

        List<DatePair> datePairs = getDatePairsInRange(startYear, endYear, 90);

        System.out.println("Date Pairs within the range with a 90-day difference:");
        for (DatePair datePair : datePairs) {
            System.out.println(datePair.getStart() + " to " + datePair.getEnd());
        }
    }

    private static List<DatePair> getDatePairsInRange(String startDateStr, String endDateStr, int dayDifference) {
        List<DatePair> datePairs = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(startDateStr, formatter);
        LocalDate endDate = LocalDate.parse(endDateStr, formatter);

        while (startDate.plusDays(dayDifference).isBefore(endDate)) {
            LocalDate endDateOfPair = startDate.plusDays(dayDifference);
            datePairs.add(new DatePair(startDate.format(formatter), endDateOfPair.format(formatter)));
            startDate = endDateOfPair.plusDays(1);
        }

        // Add the remaining days as a separate pair if applicable
        if (!startDate.isAfter(endDate)) {
            datePairs.add(new DatePair(startDate.format(formatter), endDate.format(formatter)));
        }

        return datePairs;
    }

    private static class DatePair {
        private final String start;
        private final String end;

        public DatePair(String start, String end) {
            this.start = start;
            this.end = end;
        }

        public String getStart() {
            return start;
        }

        public String getEnd() {
            return end;
        }
    }
}
