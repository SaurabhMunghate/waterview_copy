package com.saurabh;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class CompareTextFiles {
    public static void main(String[] args) {
        String file1Path = "/home/shatam-100/ResidentialWaterView/meter_point.txt"; // Replace with the path to your first text file
        String file2Path = "/home/shatam-100/ResidentialWaterView/new_point.txt"; // Replace with the path to your second text file

        try {
            Set<String> lines1 = new HashSet<>();
            Set<String> lines2 = new HashSet<>();

            // Read lines from the first file and add them to lines1 set
            BufferedReader reader1 = new BufferedReader(new FileReader(file1Path));
            String line;
            while ((line = reader1.readLine()) != null) {
                lines1.add(line);
            }
            reader1.close();

            // Read lines from the second file and add them to lines2 set
            BufferedReader reader2 = new BufferedReader(new FileReader(file2Path));
            while ((line = reader2.readLine()) != null) {
                lines2.add(line);
            }
            reader2.close();

            // Find lines that are in file1 but not in file2
            Set<String> linesNotInFile2 = new HashSet<>(lines1);
            linesNotInFile2.removeAll(lines2);

            // Print the lines that are not in file2
            for (String notInFile2 : linesNotInFile2) {
                System.out.println(notInFile2);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
