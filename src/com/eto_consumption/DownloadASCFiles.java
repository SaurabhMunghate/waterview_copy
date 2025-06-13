package com.eto_consumption;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

//import com.shatam.DataLoad.ETOFetchFromASCFiles.GridData;
import com.shatam.utils.U;

import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class DownloadASCFiles {
	static List<String> UrlList = new ArrayList<String>();
	static List<String> fileNameList = new ArrayList<String>();
	public String parcelFileData="";
	public static void main(String[] args) {
		String directoryPath = "G:\\WaterDistrictData\\GeoPackages";
	      
	      // Using File class create an object for specific directory
	      File directory = new File(directoryPath);
	      
	      // Using listFiles method we get all the files of a directory 
	      // return type of listFiles is array
	      File[] files = directory.listFiles();
	      
	      // Print name of the all files present in that path
	      if (files != null) {
	        for (File file : files) {
	          System.out.println(file.getName().replace(".gpkg", ""));
	        }
	      }
//G:\WaterDistrictData\GeoPackages
		DownloadASCFiles daf=new DownloadASCFiles();
		daf.parcelFileData="";
//		String startDate = "2022-01-01";// "2022-01-01", "2022-12-31"
//		String endDate = "2024-05-01";
//		String startDate = "2024-05-01";// "2022-01-01", "2022-12-31"
//		String endDate = "2024-12-31";
		
//		String startDate = "2020-10-01";// "2022-01-01", "2022-12-31"
//		String endDate = "2021-12-31";

		String startDate = "2020-10-01";// "2022-01-01", "2022-12-31"
		String endDate = "2024-12-31";

		
		daf.startEtoProcess(startDate, endDate);
	}
	//{ SrNo, Tiles, date, Prec,eto }
	public void startEtoProcess(String startDate,String endDate) {
		try {
			generateURLs(startDate, endDate);
			downloadData();
			System.out.println("Parcel File Using: "+parcelFileData);
//			getEtoValues();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void getEtoValues() throws IOException {
		// TODO Auto-generated method stub
		ETOFetchFromASCFiles etoFetch=new ETOFetchFromASCFiles();
		for (String fileName : fileNameList) {
			U.log(fileName);
			GridData grid = etoFetch.readAsciiGrid(fileName, "EPSG:3310");
			double latitude = 33.536625290412694; // Example latitude (So Cal)
			double longitude = -117.70330631421919; // Example longitude (So Cal)

			Double value = etoFetch.getValueAtCoords(grid, latitude, longitude);
			System.out.println("Value at (" + latitude + ", " + longitude + "): " + value);
		}
	}

	private void downloadData() throws IOException {
		for (String urlStr : UrlList) {
			String fileName = "/home/shatam-100/Down/WaterView_Data/FTP_DATA/17Feb/Eto-" + urlStr.replace("https://spatialcimis.water.ca.gov/cimis/", "")
					.replace("/ETo.asc.gz", "").replace("/", "-") + ".asc.gz";
			U.downloadUsingStream(urlStr, fileName);
			decompress(fileName);
			fileNameList.add(fileName.replace(".gz", ""));
		}
	}

	static DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	// https://spatialcimis.water.ca.gov/cimis/2024/06/01/
	private void generateURLs(String startDate, String endDate) throws Exception {
		LocalDate start = LocalDate.parse(startDate);
		LocalDate end = LocalDate.parse(endDate);
		while (!start.isAfter(end)) {
			String tempDateArr[] = start.toString().split("-");
			String url = "https://spatialcimis.water.ca.gov/cimis/" + tempDateArr[0] + "/" + tempDateArr[1] + "/"
					+ tempDateArr[2] + "/ETo.asc.gz";
			System.out.println("url : "+url);
			UrlList.add(url);
			start = start.plusDays(1);
		}
	}

	private void decompress(String fileName) {
		byte[] buffer = new byte[1024];
		try {
			if(new File(fileName.replace(".gz", "")).exists())
				return;
			GZIPInputStream is = new GZIPInputStream(new FileInputStream(fileName));
			FileOutputStream out = new FileOutputStream(fileName.replace(".gz", ""));
			int totalSize;
			while ((totalSize = is.read(buffer)) > 0) {
				out.write(buffer, 0, totalSize);
			}
			out.close();
			is.close();
			System.out.println("File Successfully decompressed");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
