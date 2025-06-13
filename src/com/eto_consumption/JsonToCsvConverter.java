package com.eto_consumption;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

public class JsonToCsvConverter {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static void main(String[] args) throws IOException {
    	String inputDirectory = "/home/shatam-100/Down/WaterView_Data/FTP_DATA/Data_Folder_2024-07-17 ";
    	getFolderJson("/home/shatam-100/Down/WaterView_Data/FTP_DATA/June/WVSANCLEMENTEC310");

    	
//    	getFolderJson("");
//    	getFolderJson("/home/shatam-100/Down/WaterView_Data/FTP_DATA/Data_Folder_2024-10-08/JURUPACOMMUN177");
//    	getFolderJson("/home/shatam-100/Down/WaterView_Data/FTP_DATA/Data_Folder_2024-10-08/WVBRENTWOOD");
//    	getFolderJson("/home/shatam-100/Down/WaterView_Data/FTP_DATA/Data_Folder_2024-10-08/WVCAMARILLO");
//    	getFolderJson("/home/shatam-100/Down/WaterView_Data/FTP_DATA/Data_Folder_2024-10-08/WVCAMROSAWATER063");
//    	getFolderJson("/home/shatam-100/Down/WaterView_Data/FTP_DATA/Data_Folder_2024-10-08/WVCASITAS");
//    	getFolderJson("/home/shatam-100/Down/WaterView_Data/FTP_DATA/Data_Folder_2024-10-08/WVEASTVALLEY");
//    	getFolderJson("/home/shatam-100/Down/WaterView_Data/FTP_DATA/Data_Folder_2024-10-08/WVGLENDALECITY133");
//    	getFolderJson("/home/shatam-100/Down/WaterView_Data/FTP_DATA/Data_Folder_2024-10-08/WVLAKEARROWHEAD");
//    	getFolderJson("/home/shatam-100/Down/WaterView_Data/FTP_DATA/Data_Folder_2024-10-08/WVMONTEVISTACO226");
//    	getFolderJson("/home/shatam-100/Down/WaterView_Data/FTP_DATA/Data_Folder_2024-10-08/WVONTARIO");
//    	getFolderJson("/home/shatam-100/Down/WaterView_Data/FTP_DATA/Data_Folder_2024-10-08/WVORCHARDDALE");
//    	getFolderJson("/home/shatam-100/Down/WaterView_Data/FTP_DATA/Data_Folder_2024-10-08/WVPOMONA");
//    	getFolderJson("/home/shatam-100/Down/WaterView_Data/FTP_DATA/Data_Folder_2024-10-08/WVREDWOODCITY");
//    	getFolderJson("/home/shatam-100/Down/WaterView_Data/FTP_DATA/Data_Folder_2024-10-08/WVRIALTO");
//    	getFolderJson("/home/shatam-100/Down/WaterView_Data/FTP_DATA/Data_Folder_2024-10-08/WVRINCON");
//    	getFolderJson("/home/shatam-100/Down/WaterView_Data/FTP_DATA/Data_Folder_2024-10-08/WVSANGABRIELCOUNTY");
//    	getFolderJson("/home/shatam-100/Down/WaterView_Data/FTP_DATA/Data_Folder_2024-10-08/WVSIMIVALLEY");
//    	getFolderJson("/home/shatam-100/Down/WaterView_Data/FTP_DATA/Data_Folder_2024-10-08/WVTRABUCOCANYON");
//    	getFolderJson("/home/shatam-100/Down/WaterView_Data/FTP_DATA/Data_Folder_2024-10-08/WVVACAVILLECIT374");


//    	getFolderJson("/home/shatam-100/Down/WaterView_Data/FTP_DATA/Data_Folder_2024-09-10/WVSANGABRIELCOUNTY");
            //+++++++++++++++++++++++++++++++++++++++++++++++++++++
            File directory = new File(inputDirectory);
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        // Recursively process subdirectories
//                    	System.out.println(file);
//                        processDirectory(file);
                    	convertJsonToCsv(file, inputDirectory);
//                    	getFolderJson(file.toString());
                    } else if (file.isFile() && file.getName().toLowerCase().endsWith(".json")) {
                        // Process JSON file
                        try {
//                            convertJsonToCsv(file, directory.getAbsolutePath());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            
            //+++++++++++++++++++++++++++++++++++++++++++++++++++++
            
//    	String json = "/home/shatam-100/Down/WaterView_Data/FTP_DATA/prd.billing_address_res.json";
//    	String csv = "/home/shatam-100/Down/WaterView_Data/FTP_DATA/";
//            convertJsonToCsv(new  File(json), csv);
    }

    private static void getFolderJson(String inputDirectory) {
		// TODO Auto-generated method stub
    	System.out.println(inputDirectory);
//      String outputDirectory = "/home/shatam-100/Down/WaterView_Data/FTP_DATA/18_March_WD/Data_Folder_2024-03-17/WVWESTERN";
//      File outputDir = new File(outputDirectory);
//      outputDir.mkdirs();
      File[] jsonFiles = new File(inputDirectory).listFiles((dir, name) -> name.toLowerCase().endsWith(".json"));
      if (jsonFiles != null) {
          for (File jsonFile : jsonFiles) {
             try {
          	   System.out.println(jsonFile);
          	   convertJsonToCsv(jsonFile, inputDirectory);
			} catch (Exception e) {
			}
          }
      
      }

		
	}

	private static void convertJsonToCsv(File jsonFile, String outputDirectory) throws IOException {
        JsonNode rootNode = MAPPER.readTree(jsonFile);

        // Create CSV file
        String csvFileName = jsonFile.getName().replace(".json", ".csv");
        File csvFile = new File(outputDirectory, csvFileName);
        FileWriter csvWriter = new FileWriter(csvFile);

        // Process JSON data
        if (rootNode.isArray()) {
            processArrayNode((ArrayNode) rootNode, csvWriter);
        } else if (rootNode.isObject()) {
            processObjectNode((ObjectNode) rootNode, csvWriter);
        }

        // Close CSV writer
        csvWriter.close();

        System.out.println("Conversion completed for: " + jsonFile.getName());
    }

    private static void processArrayNode(ArrayNode arrayNode, FileWriter csvWriter) throws IOException {
        if (arrayNode.size() > 0) {
            // Get field names from the first object
            ObjectNode firstObject = (ObjectNode) arrayNode.get(0);
            Iterator<String> fieldNames = firstObject.fieldNames();

            // Write header row
            while (fieldNames.hasNext()) {
                csvWriter.append(escapeSpecialCharacters(fieldNames.next()));
                if (fieldNames.hasNext()) {
                    csvWriter.append(",");
                }
            }
            csvWriter.append("\n");

            // Write data rows
            for (JsonNode node : arrayNode) {
                ObjectNode objectNode = (ObjectNode) node;
                Iterator<String> values = objectNode.fieldNames();

                while (values.hasNext()) {
                    csvWriter.append(escapeSpecialCharacters(objectNode.get(values.next()).asText()));
                    if (values.hasNext()) {
                        csvWriter.append(",");
                    }
                }
                csvWriter.append("\n");
            }
        }
    }

    private static void processObjectNode(ObjectNode objectNode, FileWriter csvWriter) throws IOException {
        Iterator<String> fieldNames = objectNode.fieldNames();

        // Write header row
        while (fieldNames.hasNext()) {
            csvWriter.append(escapeSpecialCharacters(fieldNames.next()));
            if (fieldNames.hasNext()) {
                csvWriter.append(",");
            }
        }
        csvWriter.append("\n");

        // Write data row
        Iterator<JsonNode> fieldValues = objectNode.elements();
        while (fieldValues.hasNext()) {
            csvWriter.append(escapeSpecialCharacters(fieldValues.next().asText()));
            if (fieldValues.hasNext()) {
                csvWriter.append(",");
            }
        }
        csvWriter.append("\n");
    }

    private static String escapeSpecialCharacters(String data) {
        if (data.contains(",") || data.contains("\"") || data.contains("'") || data.contains("\n") || data.contains("\r")) {
            data = data.replace("\"", "\"\"");
            data = "\"" + data + "\"";
        }
        return data;
    }
}
