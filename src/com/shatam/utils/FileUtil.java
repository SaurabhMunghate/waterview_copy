package com.shatam.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;


public class FileUtil {

    public static void makeDir(String path){
        File folder = new File(path);
        if (!folder.exists())
            folder.mkdirs();
    }

    public static String readAllText(String path) throws IOException {

        File aFile = new File(path);

        StringBuilder contents = new StringBuilder();


        BufferedReader input = new BufferedReader(new FileReader(aFile));
        try {
            String line = null; //not declared within while loop

            while ((line = input.readLine()) != null) {
                contents.append(line);
                contents.append(System.getProperty("line.separator"));
            }
        } finally {
            input.close();
        }


        return contents.toString();
        
    }//readAllText()


    public static void writeAllText(String path, String aContents) throws FileNotFoundException, IOException {

        File aFile = new File(path);

        //use buffering
        Writer output = new BufferedWriter(new FileWriter(aFile));
        try {
            //FileWriter always assumes default encoding is OK!
            output.write(aContents);
        } finally {
            output.close();
        }
    }//writeAllText()
    
    public static void readJsonFile(String fileName) throws IOException {
        try {
            // Read the contents of the JSON file as a string
            String jsonString = new String(Files.readAllBytes(Paths.get(fileName)));

            // Create a JSON array from the string
            JSONArray jsonArray = new JSONArray(jsonString);

            // Iterate over the JSON array
            for (int i = 0; i < jsonArray.length(); i++) {
                // Get the JSON object at the current index
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                System.out.println(jsonObject);

                // Extract values by property name
//                String premid = jsonObject.getString("premid");
//                String allocation = jsonObject.getString("allocation");
//                String districtId = jsonObject.getString("districtId");
//                String consumption = jsonObject.getString("consumption");
//                String id = jsonObject.getString("id");
//                String date = jsonObject.getString("Date");

                // Print the extracted values
//                System.out.println("premid: " + premid);
//                System.out.println("allocation: " + allocation);
//                System.out.println("districtId: " + districtId);
//                System.out.println("consumption: " + consumption);
//                System.out.println("id: " + id);
//                System.out.println("Date: " + date);
//                System.out.println();
            }
        } catch (JSONException e) {
//            System.err.println("Error while parsing JSON: " + e.getMessage());
        }
    }

    public static List<String[]>  readCsvFile(String filePath){
		List<String[]>  readLines = null;
		try(CSVReader reader = new CSVReader(new FileReader(filePath));)
		{
			readLines = reader.readAll();
		}catch (Exception e) {
			e.printStackTrace();
		}
		U.log("Load input file...... Done");
		U.log("Size of record is ::"+readLines.size());
		return readLines;
	}
	
	/**
	 * 
	 * @param fileName
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static List<String[]> readCsvFileWithoutHeader(String fileName){
		List<String[]> readLines = null;
		try(CSVReader reader = new CSVReader(new FileReader(fileName),',','"',1);){
			readLines = reader.readAll();
		} catch (IOException e) {
			e.printStackTrace();
		}
		U.log("Size of record is ::"+readLines.size());
		return readLines;
	}
	/**
	 * 
	 * @param writeLines
	 * @param filePath
	 */
	@SuppressWarnings("deprecation")
	public static void writeCsvFile(List<String[]> writeLines, String filePath){
		try(CSVWriter writer = new CSVWriter(new FileWriter(filePath),',');){
			writer.writeAll(writeLines);
			writer.flush();
		}catch(IOException e){
			e.printStackTrace();			
		}
		U.log("Done writing here.. at "+filePath);
	}
	/**
	 * 
	 * @param writeLines
	 * @param file
	 */
	public static void writeCsvFile(List<String[]> writeLines, File file){
		writeCsvFile(writeLines, file.getPath());
	}
	
	/**
	 * 
	 * @param writeLines
	 * @param filePath
	 */
	public static void writeCsvFile(ArrayList<String[]> writeLines, String filePath){
		List<String[]> lines = new ArrayList<>(writeLines);
		writeCsvFile(lines, filePath);
	}
	
	/**
	 * 
	 * @param writeLines
	 * @param filePath
	 */
	public static void writeCsvFile(Collection<String[]> writeLines, String filePath){
		List<String[]> lines = new ArrayList<>(writeLines);
		writeCsvFile(lines, filePath);
	}

	/**
	 * 
	 * @param writeLines
	 * @param filePath
	 * @param append
	 */
	@SuppressWarnings("deprecation")
	public static void writeCsvFile(List<String[]> writeLines, String filePath, boolean append){
		try(CSVWriter writer = new CSVWriter(new FileWriter(filePath, append),',');){
			writer.writeAll(writeLines);
			writer.flush();
		}catch(IOException e){
			e.printStackTrace();			
		}
		U.log("Done writing here..");
	}
	/**
	 * 
	 * @param writeLines
	 * @param filePath
	 * @param append
	 */
	public static void writeCsvFile(ArrayList<String[]> writeLines, String filePath, boolean append){
		List<String[]> lines = new ArrayList<>(writeLines);
		writeCsvFile(lines, filePath, append);
	}
	/**
	 * 
	 * @param writeLines
	 * @param filePath
	 * @param append
	 */
	public static void writeCsvFile(Collection<String[]> writeLines, String filePath, boolean append){
		List<String[]> lines = new ArrayList<>(writeLines);
		writeCsvFile(lines, filePath, append);
	}
	
	@SuppressWarnings("deprecation")
	public static void writeCsvFile(String[] header, List<String[]> writeLines, String filePath){
		try(CSVWriter writer = new CSVWriter(new FileWriter(filePath),',');){
			writer.writeNext(header);
			writer.writeAll(writeLines);
			writer.flush();
		}catch(IOException e){
			e.printStackTrace();			
		}
		U.log("Done writing here.."+filePath);
	}
	/**
	 * 
	 * @param header
	 * @param writeLines
	 * @param filePath
	 */
	public static void writeCsvFile(String[] header, ArrayList<String[]> writeLines, String filePath){
		List<String[]> lines = new ArrayList<>(writeLines);
		writeCsvFile(header, lines, filePath);
	}
	
	public static void writeCsvFile(String[] header, Collection<String[]> writeLines, String filePath){
		List<String[]> lines = new ArrayList<>(writeLines);
		writeCsvFile(header, lines, filePath);
	}

}//class FileUtil
