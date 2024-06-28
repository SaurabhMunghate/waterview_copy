package com.saurabh;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class Months {
    public static void main(String[] args) {
        LocalDate startDate = LocalDate.of(2021, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        LocalDate currentMonth = startDate;
        while (!currentMonth.isAfter(endDate)) {
            System.out.println(formatter.format(currentMonth));
            currentMonth = currentMonth.plusMonths(1);
        }
    }
}
