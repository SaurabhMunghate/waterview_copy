package com.saurabh;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ExtractGeom {

    public static void main(String[] args) throws IOException {
        String csvFile = "/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries_CSV/banning_.csv"; // Replace with your actual CSV file path
        String txtFile = "/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries_CSV/banning_.txt"; // Replace with your desired TXT file name
        String delimiter = ","; // Change delimiter if needed (e.g., ";", "\t")
        int geomColumnIndex = 1; // Assuming "the_geom" is the second column (index 1)

        BufferedReader reader = new BufferedReader(new FileReader(csvFile));
        FileWriter writer = new FileWriter(txtFile);

        // Skip the header row (optional, adjust if needed)
        reader.readLine();

        String line;
        while ((line = reader.readLine()) != null) {
            String[] values = line.split(delimiter);
            writer.write(values[geomColumnIndex] + "\n");
        }

        reader.close();
        writer.close();

        System.out.println("Geom values extracted to " + txtFile);
    }
}
