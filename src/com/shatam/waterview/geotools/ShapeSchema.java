package com.shatam.waterview.geotools;

import java.io.File;
import java.io.IOException;

import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.opengis.feature.simple.SimpleFeature;

import com.shatam.utils.FileUtil;
import com.shatam.utils.U;

public class ShapeSchema {

	private static String filePath = 
			"/home/glady/workspaces/Parcel_Cache/Tester/Sawan/15_Nov_21/MaderaCounty_Zoning/ZONING.shp";
	
	public static void main(String[] args) throws IOException{
		U.log("File Path :"+filePath);
		findShapeSchema();
		
/*		File files[] = new File("/home/glady/workspaces/Parcel_Cache/Tester/Sawan/5_July_21/KingCountyParcelGIS/").listFiles();
		for(File f : files){
			if(!f.getName().endsWith(".shp"))continue;
			U.log(f.getName());
			filePath = f.getPath();
			findShapeSchema();
		}
*/		
	}
		
	public static void findShapeSchema() throws IOException {

		FileDataStore store = FileDataStoreFinder.getDataStore(new File(filePath));

		SimpleFeatureSource featureSource = store.getFeatureSource();
		
		System.out.println(">>>>>>>> "+featureSource.getSchema());
		
		SimpleFeatureIterator simitr = featureSource.getFeatures().features();

		int count = 0;
		
		StringBuffer sb = new StringBuffer();
		while (simitr.hasNext()) {

			SimpleFeature f = simitr.next();

			String g = f.getDefaultGeometryProperty().toString();

//			System.out.println("============ "+g);
			sb.append(g);
			count++;
			if(count == 1)break;
		}//eof while
		simitr.close();
		
//		FileUtil.writeAllText("/home/glady/workspaces/Parcel_Cache/Hazard_Zone/other_hazard/critical_habitant/crithab_all_shapefiles/file.txt", sb.toString());

	
		/*
		 * Find WKT-ID value
		 */
		String name = U.getSectionValue(sb.toString(), "MultiPolygon crs=PROJCS[\"", "\"");
		if(name == null)
			name = U.getSectionValue(sb.toString(), "MultiLineString crs=PROJCS[\"", "\"");
		if(name == null)
			name = U.getSectionValue(sb.toString(), "Point crs=PROJCS[\"", "\"");
		if(name == null)
			name = U.getSectionValue(sb.toString(), "MultiPolygon crs=GEOGCS[\"", "\"");
		
//		U.log("name -->"+name);

		
		if(name == null){
			U.log("WKT-ID name is not found. check or update proper section...");
			return;
		}
		String wktId = getWktId(name);
		
		if(wktId == null){
			U.log("WKT-ID is not match with our ID's list...");
			return;
		}
		
		U.log(">>> Found Match WKT-ID :"+wktId);
		
	}
	
	public static String getWktId(String name) throws IOException{
		String url = "https://developers.arcgis.com/rest/services-reference/enterprise/projected-coordinate-systems.htm";
		
		String html = U.getHTML(url);
		String section [] = U.getValues(html, "<tr class=\"align-middle\">", "</tr>");
		U.log("Total WKT-ID :"+section.length);
		
		String str = null;
		for(String sec : section){
			String vals[] = U.getValues(sec, "<td outputclass", "</td>");
			
			String wktId = vals[0].replaceAll("^(.*?)><p id=\"(.*?)\">|</p>", "");
			String wktName = vals[1].replaceAll("^(.*?)><p id=\"(.*?)\">|</p>", "");
			if(wktName.equals(name)){
				str = wktId;
				break;
			}
			
/*			for(String val : vals){
				val = val.replaceAll("^(.*?)><p id=\"(.*?)\">|</p>", "");
				U.log(val);
			}
*/			
//			break;
		}//eof for
		return str;
	}
}
