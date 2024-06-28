package com.saurabh;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DiffAvgEtoPremiseG {

    public static void main(String[] args) {
        String csvFile = "/home/shatam-100/ResidentialWaterView/Zoom_0_14_Data.csv";
        String line;
        String csvSplitBy = "\n";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                String[] columns = line.split(csvSplitBy);
                for (String column : columns) {
                	String[] col = column.split("$");
                    System.out.println(col[0]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}