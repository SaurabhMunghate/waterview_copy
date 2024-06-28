package com.validation;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class CSVKeyChecker {

    public static void main(String[] args) {
//    	String csvFile = "/home/shatam-100/Down/WaterView_Data/FTP_DATA/Data_Folder_2024-05-02/WVOXNARD/oxnard_res_daily_Eto_mid.csv";
//    	String csvFile = "/home/shatam-100/Down/WaterView_Data/FTP_DATA/Data_Folder_2024-05-02/WVRINCON/rincon_res_daily_Eto_mid.csv";
//    	String csvFile = "/home/shatam-100/Down/WaterView_Data/FTP_DATA/Data_Folder_2024-05-02/WVCAMROSAWATER063/camrosa_res_daily_Eto_mid.csv";
//    	String csvFile = "/home/shatam-100/Down/WaterView_Data/FTP_DATA/Data_Folder_2024-05-02/WVGLENDALECITY133/glendale_res_daily_Eto_mid.csv";
//    	String csvFile = "/home/shatam-100/Down/WaterView_Data/FTP_DATA/Data_Folder_2024-05-02/WVMYOMADUNESMU235/myoma_dunes_res_daily_Eto_mid.csv";
    	String csvFile = "/home/shatam-100/workspaces/Waterview_CII_Functionalities_vncvm/waterview_resources/data_files_backup/Data_Folder_2024-06-20/WVSANTAMONICA/santa_monica_res_daily_Eto_apn.csv"; 
    	String line;
        String cvsSplitBy = ",";
        System.out.println(csvFile);
        HashMap<String, String> hs = new HashMap<>();
        // Set to store unique keys
        Set<String> keySet = new HashSet<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            // Read the header line
            line = br.readLine();
            if (line == null) {
                System.out.println("CSV file is empty.");
                return;
            }

            // Assuming "Date and meterID" is the first column
            // Skip header line
            while ((line = br.readLine()) != null) {
                String[] values = line.split(cvsSplitBy);

                if (values.length < 1) {
                    System.out.println("Invalid CSV format.");
                    return;
                }

                String key = values[0]+values[2];
                hs.put(values[0], values[0]);

                if (keySet.contains(key)) {
                    System.out.println("Duplicate key found: " + key);
                } else {
                    keySet.add(key);
                }
            }
            System.out.println("Hash Map Size :: "+hs.size());
            System.out.println("Duplicate check completed.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
