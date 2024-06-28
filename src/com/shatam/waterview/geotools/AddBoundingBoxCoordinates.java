/**
 * @author Sawan Meshram
 * @date 8 Feb 2022
 */
package com.shatam.waterview.geotools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.shatam.utils.BoundingBox;
import com.shatam.utils.FileUtil;
import com.shatam.utils.U;

public class AddBoundingBoxCoordinates {

	private static String filePath = "/home/glady/workspaces/Parcel_Cache/Hazard_Zone/other_hazard/Federal/Wildland_Fire/WFIGS_-_Wildland_Fire_Perimeters_Full_History_Records_Only_Geo.csv";
//			"/home/glady/workspaces/Parcel_Cache/Hazard_Zone/other_hazard/Federal/Wildland_Fire/InteragencyFirePerimeterHistory_3_Only_Geo_State.csv";
//			"/home/glady/workspaces/Parcel_Cache/Tester/Pallavi/15_Feb_22/LongTermDroughtwithMap_Report.csv"; 
//			"/home/glady/workspaces/Parcel_Cache/Tester/Vidya/US_Landslide_1/US_Landslide_poly_5_Records_State.csv";

	public static void main(String[] args) {
		addBoundingBoxCoordinates();
		
//		addBoundingBoxCoordinates(29);
		
		updatedBoundingBoxCoordinates();
		
//		updatedBoundingBoxCoordinates(0);
		
	}
	
	private static void addBoundingBoxCoordinates(){
		addBoundingBoxCoordinates(0);
	}
	
	private static void addBoundingBoxCoordinates(int idxGeo){
		
		U.log("Geometry Index at File :"+idxGeo);
		List<String[]> readLines = FileUtil.readCsvFile(filePath);
		
		List<String[]> writeLines = new ArrayList<>();
		
		U.log(Arrays.toString(readLines.get(0)));
		
		Iterator<String[]> it = readLines.iterator();
		String[] lines = null;
		int i = 0;
		int count = 0;
		
	
		while(it.hasNext()){
			lines = it.next();
		
			if(i++ == 0){
				String header[] = {"X_MIN","Y_MIN","X_MAX","Y_MAX"};
				writeLines.add(U.concat(lines, header));
				continue;
			}

			if(!lines[idxGeo].endsWith("))")){ //geo
				U.log(lines[idxGeo]);
				it.remove();
				count++;
				continue;
			}
			
/*			//for not converting the geometry
			if(lines[0].length() < 5){
//				U.log(temp);
				it.remove();
				count++;
				continue;
			}*/
			
//			U.log(lines[0]);
			
			BoundingBox e = ShatamGeometry.getBoundingBox(lines[idxGeo]);
			double[] boundingBox = {e.getXmin(), e.getYmin(), e.getXmax(), e.getYmax()};
//			U.log(Arrays.toString(boundingBox));
//			U.log(e);
//			break;

			writeLines.add(U.concat(lines, boundingBox));
		}
		
		FileUtil.writeCsvFile(writeLines, filePath.replace(".csv", "_BBox.csv"));
		
		U.log("New records :"+readLines.size());
		U.log("Total removed count :"+count);
	}
	
	private static void updatedBoundingBoxCoordinates(){
		updatedBoundingBoxCoordinates(0);
	}
	
	private static void updatedBoundingBoxCoordinates(int idxGeo){
		
		U.log("Geometry Index at File :"+idxGeo);
		List<String[]> readLines = FileUtil.readCsvFile(filePath);
		
		int idxMinX = 0, idxMinY = 0, idxMaxX = 0, idxMaxY = 0;
		
		U.log(Arrays.toString(readLines.get(0)));
		
		Iterator<String[]> it = readLines.iterator();
		String[] lines = null;
		int i = 0;
		int count = 0;
		
	
		while(it.hasNext()){
			lines = it.next();
		
			if(i++ == 0){
				for(int k = 0; k < lines.length; k++){
					if(lines[k].equals("X_MIN")) idxMinX = k;
					else if(lines[k].equals("Y_MIN")) idxMinY = k;
					else if(lines[k].equals("X_MAX")) idxMaxX = k;
					else if(lines[k].equals("Y_MAX")) idxMaxY = k;
				}
				continue;
			}

			if(!lines[idxGeo].endsWith("))")){ //geo
				U.log(lines[idxGeo]);
				it.remove();
				count++;
				continue;
			}

			
//			U.log(lines[0]);
			
			BoundingBox e = ShatamGeometry.getBoundingBox(lines[idxGeo]);

			lines[idxMinX] = ""+e.getXmin();
			lines[idxMinY] = ""+e.getYmin();
			lines[idxMaxX] = ""+e.getXmax();
			lines[idxMaxY] = ""+e.getYmax();
			
		}
		
		FileUtil.writeCsvFile(readLines, filePath.replace(".csv", "_BBox_Update.csv"));
		
		U.log("New records :"+readLines.size());
		U.log("Total removed count :"+count);
	}
}
