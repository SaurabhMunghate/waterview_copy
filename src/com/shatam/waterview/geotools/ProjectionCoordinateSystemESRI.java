package com.shatam.waterview.geotools;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.shatam.utils.U;
import com.shatam.utils.Util;

public class ProjectionCoordinateSystemESRI {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		ProjectionCoordinateSystemESRI crs = ProjectionCoordinateSystemESRI.loadCRS();
		U.log("Name ::"+crs.getSpatialNameESRI(102599));
		U.log("Name ::"+crs.getSpatialNameESRI(102600));
		U.log("Name ::"+crs.getSpatialNameESRI(2229));
		
		U.log("EPSG:"+crs.getESPGNameFromSpatialESRI(102599));
	}
	
	
	private ProjectionCoordinateSystemESRI(){
		try {
			loadESRI();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			loadESRI_EPSG();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static ProjectionCoordinateSystemESRI loadCRS(){
		
		return new ProjectionCoordinateSystemESRI();
	}

	private Map<Integer,String> crsMap = new HashMap<>();
	private Map<Integer, Integer> ESRI_EPSG_map = new HashMap<>();
	
	public String getSpatialNameESRI(int srid){
		return crsMap.get(srid);
	}
	
	public int getESPGNameFromSpatialESRI(int srid){
		return ESRI_EPSG_map.get(srid);
	}
	
	
	private void loadESRI_EPSG() throws IOException{
		String url = "https://wiki.spatialmanager.com/index.php/Coordinate_Systems_objects_list";
		String html = U.getHTML(url);
		String section = U.getSectionValue(html, "Coordinate Reference Systems</span></h2>", "</ul>");
//		U.log(section);
		String sections [] = U.getValues(section, "<li>", "</li>");
//		U.log(sections.length);
		int count=0;
		for(String epsgVal : sections){
			List<String> esriList = Util.matchAll(epsgVal, "Esri\\s?:\\s?(\\d+)", 1);
			if(esriList.size()> 0){
//				U.log(epsgVal);
				count++;
				for(String esriCRS :esriList){
//					U.log(Util.match(epsgVal, "EPSG:(\\d+)").replaceAll("\\W+|[a-zA-Z]", ""));
					ESRI_EPSG_map.put(Integer.parseInt(esriCRS), Integer.parseInt(Util.match(epsgVal, "EPSG:(\\d+)").replaceAll("\\W+|[a-zA-Z]", "")));
				}
			}
		}
		U.log("Found EPSG ::"+count);
		U.log("Total ESRI ::"+ESRI_EPSG_map.size());
	}
	
	private void loadESRI() throws IOException{
		String url = "https://developers.arcgis.com/rest/services-reference/enterprise/projected-coordinate-systems.htm";
		
		String html = U.getHTML(url);
		String section [] = U.getValues(html, "<tr class=\"align-middle\">", "</tr>");
//		U.log("Total WKT-ID :"+section.length);
		
//		String str = null;
		for(String sec : section){
			
			String vals[] = U.getValues(sec, "<td outputclass", "</td>");
			
			String wktId = vals[0].replaceAll("^(.*?)><p id=\"(.*?)\">|</p>", "");
			String wktName = vals[1].replaceAll("^(.*?)><p id=\"(.*?)\">|</p>", "");
			crsMap.put(Integer.parseInt(wktId), wktName);
		}//eof for
		U.log("Total ESRI Projection count ::"+crsMap.size());
	}
}
