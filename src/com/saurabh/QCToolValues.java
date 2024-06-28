package com.saurabh;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

//import com.fandoo.utils.U;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

//import com.fasterxml.jackson.core.JsonParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;


public class QCToolValues {
	public static final String SYSTEM_PATH=System.getProperty("user.home");
	
	public static void main(String[] args) throws IOException {
		//fir ETO Calculations
		String apiData="";
		String api="https://et.water.ca.gov/api/data?appKey=08807bb2-ce9c-4b65-af3c-dec10f6f3daf&targets=lat%3D34.08,lng%3D-117.69&startDate=2023-02-01&endDate=2023-04-28&dataItems=day-asce-eto&unitOfMeasure=E";
		String fileName = getCache(api);
		File fileName2=new File(fileName);
		if(!fileName2.exists()) {
			System.out.println("Already File Not Exist");
			fileName2.createNewFile();
			try {
				// Replace the API link with the actual API endpoint URL
			    URL url = new URL(api);
			    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			    conn.setRequestMethod("GET");
			    conn.setRequestProperty("Accept", "application/json");
			    if (conn.getResponseCode() != 200) {
			    	throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			    }
			    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			    String output;
			    System.out.println("Output from API endpoint:\n");
			    while ((output = br.readLine()) != null) {
			    	apiData+=output;  
			    }
			    try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
		            bw.write(apiData);
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
			    conn.disconnect();
	
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			System.out.println("Already File Exist");
			 try (BufferedReader br = new BufferedReader(new FileReader(fileName2))) {
		            String line;
		            while ((line = br.readLine()) != null) {
		            	apiData+=line;
		            }
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		}
		JsonParser json = new JsonParser();
		JsonArray jsonArray=json.parse(apiData).getAsJsonObject().get("Data").getAsJsonObject().get("Providers").getAsJsonArray().get(0).getAsJsonObject().get("Records").getAsJsonArray();
		float averageValue=0;
		for(JsonElement jsonElement:jsonArray) {
			String date=jsonElement.getAsJsonObject().get("Date").getAsString();
			String coordinate=jsonElement.getAsJsonObject().get("Coordinate").getAsString();
			String zipCodes=jsonElement.getAsJsonObject().get("ZipCodes").getAsString();
			Float value=jsonElement.getAsJsonObject().get("DayAsceEto").getAsJsonObject().get("Value").getAsFloat();
			String unit=jsonElement.getAsJsonObject().get("DayAsceEto").getAsJsonObject().get("Unit").getAsString();
			System.out.println(value);
			averageValue=averageValue+value;
		}
		averageValue=averageValue/jsonArray.size();
		System.out.println("averageValue :: "+averageValue);
	}
	
	
	public static String getCache(String path) throws IOException {

		String Dname = null;
		String host = new URL(path).getHost();
		host = host.replace("www.", "");
		int dot = host.indexOf("/");
		Dname = (dot != -1) ? host.substring(0, dot) : host;

		File folder = new File(SYSTEM_PATH+"/Documents/ETOApiData/" + Dname);
		if (!folder.exists())
			folder.mkdirs();
		String fileName = getCacheFileName2(path);
		fileName = SYSTEM_PATH+"/Documents/ETOApiData/" + Dname + "/" + fileName;
		return fileName;
	}

	public static String getCacheFileName2(String url) {

		String str = url.replaceAll("http://", "");
		str = str.replaceAll("www.", "");
		str = str.replaceAll("[^\\w]", "");
		if (str.length() > 200) {
			str = str.substring(0, 100) + str.substring(170, 190) + str.length() + "-" + str.hashCode();

		}

		try {
			str = URLEncoder.encode(str, "UTF-8");
			// U.log(str);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();

		}
		return str + ".txt";
	}
	
	
}

