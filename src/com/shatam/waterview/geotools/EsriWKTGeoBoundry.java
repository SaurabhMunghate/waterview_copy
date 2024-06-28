package com.shatam.waterview.geotools;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.shatam.utils.FileUtil;
import com.shatam.utils.U;
import com.shatam.utils.Util;
import com.vividsolutions.jts.geom.Envelope;



public class EsriWKTGeoBoundry {

	public static void main(String[] args) throws IOException{
		

		
		
		// TODO Auto-generated method stub
/*		String filePath = "/home/glady/workspaces/Parcel_Cache/WKT-ID_List_GCS.csv";
		List<String[]> readLines = new ArrayList<>();
		
//		String url  = "https://spatialreference.org/ref/epsg/2820/";
		
//		String html = U.getHTML("https://developers.arcgis.com/rest/services-reference/projected-coordinate-systems.htm");
		String html = U.getHTML("https://developers.arcgis.com/rest/services-reference/geographic-coordinate-systems.htm");
		
		String section = U.getSectionValue(html, "<tbody class=\"align-middle\">", "</tbody>");
//		U.log(section);
		String sections[] = U.getValues(section, "<tr class=\"align-middle\">", "</tr>");
		U.log(sections.length);
		
		for(String sec : sections){
			String wktId = Util.match(sec, ">(\\d+)</p>", 1);
			U.log(wktId);
			readLines.add(new String[]{wktId});
		}
		U.log(readLines.size());

		FileUtil.writeCsvFile(readLines, filePath);
	*/	
		
		//Find nearest coordinate system
/*		String lat = "41.94", lng ="-86.59";
		
		List<String[]> readLines = FileUtil.readCsvFile(filePath);
		List<String[]> writeLines = new ArrayList<>();
		writeLines.add(new String[]{"WKT_ID","Is_Within_Boundary"});
		int count = 0;
		for(String lines[] : readLines){
			String url = "https://spatialreference.org/ref/epsg/"+lines[0]+"/";
			U.log("count ::"+count);
			U.log(url);
			count ++;
			
			//if(count <= 5000) continue;
//			if(count <= 3000 ) continue;
			
			//String html = U.getHTML(url);
			String html = getHTML(url);
			if(html == null){
				writeLines.add(new String[]{lines[0], ""});
				continue;
			}
			String section = U.getSectionValue(html, "<b>WGS84 Bounds</b>", "<script>");
			
			boolean flag = false;
			if(section != null){
				section = section.replaceAll(":|\n", "").trim();
				U.log(section);
				
				if(section.contains(",")){
					String vals[] = section.split(",");
					if(vals.length == 4){
						
						flag = isWithinBoundary(lat, lng, vals[1].trim(), vals[0].trim(), vals[3].trim(), vals[2].trim());
						U.log("is_within :"+flag);
					}
				}
			}
			if(flag)writeLines.add(new String[]{lines[0], "True"});
			else writeLines.add(new String[]{lines[0], "False"});
			
		}
		
		FileUtil.writeCsvFile(writeLines, filePath.replace(".csv", "_Check_Boundary.csv"));
*/		
	}
	
	
	
	public static boolean isWithinBoundary(String latitude,String longitude,String northEastLat,String northEastLon,String southWestLat,String southWestLon){
		
		try {
			return isWithinBoundary (Double.parseDouble(latitude),Double.parseDouble(longitude),
					Double.parseDouble(northEastLat),Double.parseDouble(northEastLon),
					Double.parseDouble(southWestLat),Double.parseDouble(southWestLon));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean isWithinBoundary(double latitude,double longitude,double northEastLat,double northEastLon,double southWestLat,double southWestLon) {
		
		if (latitude <= northEastLat && Math.abs (longitude) >= Math.abs (northEastLon)) { // NorthEast Checking.
			if (latitude >= southWestLat && Math.abs (longitude) <= Math.abs (southWestLon)) { // NorthEast Checking.
				return true;
			}
		}
		return false;				
	}
	
	public static String getHTML(String path) throws IOException {

		path = path.replaceAll(" ", "%20");
		// U.log(" .............."+path);
		// Thread.sleep(4000);
		String fileName = U.getCache(path);
		File cacheFile = new File(fileName);
		if (cacheFile.exists())
			return FileUtil.readAllText(fileName);

//		U.log(url);
		String html = null;


//		URL url = new URL(path);
//		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
//				"181.215.130.32", 3128));
//		final URLConnection urlConnection = url.openConnection();
//		try {
//			urlConnection.addRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:10.0.2) Gecko/20100101 Firefox/10.0.2");
//			urlConnection.addRequestProperty("Accept", "text/css,*/*;q=0.1");
//			urlConnection.addRequestProperty("Accept-Language",
//					"en-us,en;q=0.5");
//			urlConnection.addRequestProperty("Cache-Control", "max-age=0");
//			urlConnection.addRequestProperty("Connection", "keep-alive");		
//			final InputStream inputStream = urlConnection.getInputStream();
//
//			html = IOUtils.toString(inputStream);
//
//			inputStream.close();
//			if (!cacheFile.exists())
//				FileUtil.writeAllText(fileName, html);
//
//			return html;
//		} catch (Exception e) {
//			U.log("gethtml expection: "+e);
//
//		}
		
		return html;
		

	}

}
