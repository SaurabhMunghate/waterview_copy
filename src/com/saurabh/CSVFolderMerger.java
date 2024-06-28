package com.saurabh;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CSVFolderMerger {

    public static void main(String[] args) {
        String inputFolderPath = "/home/shatam-100/Down/WaterView_Data/CSVFolderMerger"; // Replace with your input folder path
        String outputFilePath = "/home/shatam-100/Down/WaterView_Data/CSVFolderMerger/CSV_Folder_Merger.csv"; // Replace with your desired output file path

        try {
            mergeCSVFilesFromFolder(inputFolderPath, outputFilePath);
            System.out.println("CSV files from the folder merged successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void mergeCSVFilesFromFolder(String inputFolderPath, String outputFilePath) throws IOException {
        File folder = new File(inputFolderPath);
        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".csv"));

        if (files != null && files.length > 0) {
            try (BufferedWriter csvWriter = new BufferedWriter(new FileWriter(outputFilePath))) {
                // Read and write the header from the first file
                printHeader(files[0], csvWriter);

                // Iterate through each file in the folder and append its data to the output file
                for (File file : files) {
                	System.out.println(file);
                    printRecords(file, csvWriter);
                }
            }
        } else {
            System.out.println("No CSV files found in the folder.");
        }
    }

    private static void printHeader(File file, BufferedWriter csvWriter) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String header = reader.readLine();
            if (header != null) {
                csvWriter.write(header);
                csvWriter.newLine();
            }
        }
    }

    private static void printRecords(File file, BufferedWriter csvWriter) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            // Skip the header line
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                csvWriter.write(line);
                csvWriter.newLine();
            }
        }
    }
}
