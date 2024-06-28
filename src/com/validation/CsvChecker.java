package com.validation;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CsvChecker {
    public static void main(String[] args) {
        String csvFilePath = "/home/shatam-100/workspaces/Waterview_CII_Functionalities_vncvm/waterview_resources/data_files_backup/Data_Folder_2024-06-19/WVLASVIRGENESM189/las_virgenes_res_daily_Eto_apn.csv"; // Replace with your CSV file path
        String searchNumber = "2059-023-007";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            boolean found = false;

            while ((line = br.readLine()) != null) {
                String[] fields = line.split(","); // Assuming CSV fields are separated by commas
                for (String field : fields) {
                    if (field.trim().equals(searchNumber)) {
                        found = true;
                        break;
                    }
                }
                if (found) {
                    break;
                }
            }

            if (found) {
                System.out.println("The number " + searchNumber + " is present in the CSV file.");
            } else {
                System.out.println("The number " + searchNumber + " is not present in the CSV file.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
