package com.saurabh;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class RemoveDuplicateLines {
    public static void main(String[] args) {
        String inputFileName = "/home/shatam-100/Cache/duplicated.txt"; // Replace with your input file name
        String outputFileName = "/home/shatam-100/Cache/notDuplicate.txt"; // Replace with your output file name

        removeDuplicates(inputFileName, outputFileName);
        System.out.println("Duplicate lines removed and saved to " + outputFileName);
    }

    public static void removeDuplicates(String inputFileName, String outputFileName) {
        Set<String> uniqueLines = new HashSet<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFileName));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim(); // Remove leading and trailing whitespace

                if (!uniqueLines.contains(line)) {
                    uniqueLines.add(line);
                    writer.write(line);
                    writer.newLine();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
