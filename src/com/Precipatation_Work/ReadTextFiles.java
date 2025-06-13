package com.Precipatation_Work;

import java.io.*;
import java.util.HashMap;
import java.util.Map.Entry;

import com.test.U;

public class ReadTextFiles {
	public static void main(String[] args) {
		String folderPath = "/home/shatam-100/Down/WaterView_Data/Precipitation_Work/et.water.ca.gov_2024-2025/et.water.ca.gov";
		File folder = new File(folderPath);
		HashMap<String, String> hm = new HashMap<>();
		HashMap<String, String> Ahm = new HashMap<>();
		HashMap<String, String> valueHs = new HashMap<>();
		HashMap<String, String> novalueHs = new HashMap<>();
		// Check if folder exists and is a directory
		if (folder.exists() && folder.isDirectory()) {
			File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt")); // Filter only .txt files

			if (files != null && files.length > 0) {
				for (File file : files) {
//                    System.out.println("--- " + file.getName() + " ---");
					String data = readFile(file);
					String zipData = U.getSectionValue(data, "\"ZipCodes\":\"", "\",");
//                    U.log(U.getSectionValue(data, "\"ZipCodes\":\"", "\","));
					hm.put(zipData, zipData);
					if (data.contains("Value")) {
//                    	U.log(U.getSectionValue(data, "\"Value\":\"", "\""));
						valueHs.put(zipData, zipData);
						
					} else {
//						U.log(file);
						String novalueHs1 = U.getSectionValue(file.toString(), "targets", "startDate");
						novalueHs.put(novalueHs1, novalueHs1);
					}

//                    System.out.println(data);
//                    System.out.println("\n" + "-".repeat(50) + "\n"); // Separate files
				}
			} else {
				System.out.println("No .txt files found in the directory.");
			}
		} else {
			System.out.println("Invalid directory path.");
		}
		U.log(hm.size());
		for (Entry<String, String> entry : hm.entrySet()) {
//            System.out.println(" -> " + entry.getValue());
			if (entry.getValue() == null)
				continue;
			if (entry.getValue().contains(",")) {
				String[] Zipall = entry.getValue().split(", ");
				for (String stt : Zipall) {
//            		System.out.println(stt);
					Ahm.put(stt, stt);
				}
			} else {
				Ahm.put(entry.getValue(), entry.getValue());
//            	System.out.println(" -> " + entry.getValue());
			}
		}
		U.log(Ahm.size());
		U.log(valueHs.size());
		U.log("novalueHs : "+novalueHs.size());
		for (Entry<String, String> entry : valueHs.entrySet()) {
			System.out.println("" + entry.getValue());
		}
	}

	private static String readFile(File file) {
		StringBuilder content = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = br.readLine()) != null) {
				content.append(line).append("\n"); // Append each line with a newline
			}
		} catch (IOException e) {
			System.out.println("Error reading file: " + file.getName());
			e.printStackTrace();
		}
		return content.toString(); // Return complete file content
	}
}
