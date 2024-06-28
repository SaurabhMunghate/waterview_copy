/**
 * @author Sawan Meshram
 * @date 15 March 2022
 */
package com.shatam.waterview.geotools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.geotools.geometry.jts.JTS;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.Point;

import com.opencsv.CSVWriter;
import com.shatam.utils.FileUtil;
import com.shatam.utils.U;
//import com.vividsolutions.jts.geom.Envelope;
//import com.vividsolutions.jts.geom.Geometry;
//import com.vividsolutions.jts.geom.GeometryCollection;
//import com.vividsolutions.jts.geom.GeometryFactory;
//import com.vividsolutions.jts.geom.LineString;
//import com.vividsolutions.jts.geom.MultiPolygon;
//import com.vividsolutions.jts.geom.Point;
//import com.vividsolutions.jts.geom.Polygon;

public class ShatamSplitGeometry {
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		long start = System.currentTimeMillis();
		
		//without holes
//		test("POLYGON((-116.21337890625 42.488301979602255,-107.55615234375001 42.55308028895581,-105.75439453125001 43.64402584769951,-103.68896484375001 41.60722821271716,-104.43603515625 39.84228602074339,-110.23681640625001 39.77476948529545,-116.87255859375 39.97712009843963,-116.21337890625 42.488301979602255))");
		
		//with single holes
//		test("POLYGON((-118.05908203124999 43.73935207915471,-116.52099609374999 45.58328975600631,-110.72021484375 45.95114968669142,-106.36962890624999 45.026950453185464,-106.19384765625 41.672911819602064,-109.75341796875 39.707186656826565,-114.98291015624999 39.80853604144593,-115.55419921874999 41.93497650054661,-119.94873046874997 39.605688178320804,-120.82763671875 40.71395582628605,-121.88232421874997 41.93497650054661,-122.49755859374999 42.84375132629023,-119.59716796874999 42.261049162113835,-119.81689453124999 44.11914151643737,-118.05908203124999 43.73935207915471),(-113.97216796874997 43.421008829947255,-112.21435546875 44.402391829093915,-109.70947265625001 43.992814500489885,-107.68798828124999 43.45291889355465,-107.20458984374999 42.163403424224015,-108.61083984375 40.71395582628605,-110.32470703124997 41.44272637767213,-113.62060546874999 41.44272637767213,-114.58740234374999 42.32606244456201,-115.77392578125 42.58544425738492,-118.49853515625 41.804078144272324,-118.19091796875 42.45588764197166,-117.04833984375 43.06888777416961,-115.11474609374999 42.94033923363182,-113.97216796874997 43.421008829947255))");
		
		//with double homes
//		test("POLYGON((-118.05908203124999 43.73935207915471,-116.52099609374999 45.58328975600631,-110.72021484375 45.95114968669142,-106.36962890624999 45.026950453185464,-106.19384765625 41.672911819602064,-109.75341796875 39.707186656826565,-114.98291015624999 39.80853604144593,-115.55419921874999 41.93497650054661,-119.94873046874997 39.605688178320804,-120.82763671875 40.71395582628605,-121.88232421874997 41.93497650054661,-122.49755859374999 42.84375132629023,-119.59716796874999 42.261049162113835,-119.81689453124999 44.11914151643737,-118.05908203124999 43.73935207915471),(-113.97216796874997 43.421008829947255,-112.21435546875 44.402391829093915,-109.70947265625001 43.992814500489885,-107.68798828124999 43.45291889355465,-107.20458984374999 42.16340342422404,-108.61083984375 40.71395582628605,-110.32470703124997 41.44272637767213,-113.62060546874999 41.44272637767213,-114.58740234374999 42.32606244456201,-115.77392578125 42.58544425738492,-118.49853515625 41.804078144272324,-118.19091796875 42.45588764197166,-117.04833984375 43.06888777416961,-115.11474609374999 42.94033923363182,-113.97216796874997 43.421008829947255),(-120.64086914062499 41.55792157780422,-119.67407226562499 41.820455096140336,-118.37768554687499 40.88029480552822,-119.300537109375 40.145289295676605,-120.64086914062499 41.55792157780422))");
	
//		String WKT = FileUtil.readAllText("/home/glady/workspaces/Parcel_Cache/Tester/Sawan/23_Feb_23/Vaild/Valid_2.txt");
//		String WKT = FileUtil.readAllText("/home/glady/workspaces/Parcel_Cache/Tester/Sawan/23_Feb_23/Invalid/InValid_3_Output.txt");
		
//		String WKT = FileUtil.readAllText("/home/glady/workspaces/Parcel_Cache/Hazard_Zone/Dam_Inundation/Approved_InnundationBoundaries_As_of_Jul01_2021-10_WKT_Convert_BBox_2_Overlap_Correct_Fixed_Separate_WKT.txt");
//		test(WKT);

//		testWithCsv("/home/glady/workspaces/Parcel_Cache/Hazard_Zone/Dam_Inundation/Approved_InnundationBoundaries_As_of_Jul01_2021-10_WKT_Convert_BBox_2_Overlap_Correct_Fixed_BBox_Update.csv");
		
//		createSplitCsv("/home/glady/workspaces/Parcel_Cache/Hazard_Zone/Dam_Inundation/Approved_InnundationBoundaries_As_of_Jul01_2021-10_WKT_Convert_BBox_2_Overlap_Correct_Fixed_BBox_Update_Separate.csv");
		
/*		createSplitGeometryWithCache("/home/glady/workspaces/Parcel_Cache/Hazard_Zone/Dam_Inundation/Valid/Dam_Inundation_Fixed_11_Fixed.csv"
				,"/home/glady/workspaces/Parcel_Cache/Hazard_Zone/Dam_Inundation/Valid/geom_cache/", 5);
*/		
		
/*		generateFileFromSplitGeometryWithCache("/home/glady/workspaces/Parcel_Cache/Hazard_Zone/Dam_Inundation/Valid/Dam_Inundation_Fixed_11_Fixed.csv", 
				"/home/glady/workspaces/Parcel_Cache/Hazard_Zone/Dam_Inundation/Valid/geom_cache/");
*/		
		
		verifyGeometryIsValid("/home/glady/workspaces/Parcel_Cache/Hazard_Zone/Dam_Inundation/Valid/Split_Geom/Dam_Inundation_Fixed_12_Fixed_Split_Stored.csv");
		
		U.log("Total time to exec in ms : "+(System.currentTimeMillis() - start));
	}
	
	private static void verifyGeometryIsValid(String filePath){
		U.log("filePath :" + filePath);
		List<String[]> readLines = FileUtil.readCsvFileWithoutHeader(filePath);
		int foundInvalid = 0;
		for(String[] lines : readLines){
			Geometry geom = ShatamGeometry.toGeometry(lines[0]);
			
			if(!geom.isValid())
				foundInvalid++;
		}
		
		U.log("Total invalid geometry found :: "+foundInvalid);
		
	}

	private static int fileCnt = 0;
	private static void writeLineStringGeom(Geometry g){
		try {
			FileUtil.writeAllText("/home/glady/workspaces/Parcel_Cache/Hazard_Zone/Dam_Inundation/Valid/LineResult_"+(fileCnt++)+".txt", g.toString());
			U.log("File is written.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * This method is used to generate ".csv" file from reading splitting geometry cache from given directory cache path
	 * @param filePath contains input csv file
	 * @param dirCachePath contains directory path for splitting geometry
	 * @throws IOException
	 */
	public static void generateFileFromSplitGeometryWithCache(String filePath, String dirCachePath) throws IOException{
		U.log("File Path ::"+filePath);
		List<String[]> writeLines = new ArrayList<>();
		List<String[]> readLines = FileUtil.readCsvFile(filePath);

		int i = 0;
		boolean noCache = false;
		U.log("Start processing to load split geometry ......");
		
		for(String[] lines : readLines){
			if(i++ == 0){
				writeLines.add(lines); //add header on file
				continue;
			}
			
			String geomHashCode = ""+lines[0].hashCode();
			
			U.log(i +" \t"+geomHashCode+" \t"+lines[1]);
			
			//check if file is exist or not
			if(!new File(dirCachePath + geomHashCode + ".txt").exists()){
				U.log("Geometry cache is not found.");
				noCache = true;
				break;
			}
			
			String splitGeometry[] = FileUtil.readAllText(dirCachePath + geomHashCode + ".txt").split("\n");
			
			for(String geom : splitGeometry){

				String vals[] = new String[lines.length];

				vals[0] = geom.trim();	
				for(int j = 1; j < lines.length; j++){
					vals[j] = lines[j];
				}
				writeLines.add(vals); //add to csv
			}//eof for
			
		}
		
		if(writeLines.size() > 1 && !noCache){
			FileUtil.writeCsvFile(writeLines, filePath.replace(".csv", "_Split_Stored.csv"));
		}
	}
	
	/**
	 * This method is used to create splitting geometry and stored at directory path.
	 * @param filePath contains input csv file
	 * @param dirCachePath contains directory path to store splitting geometry
 	 * @param grid set value for grid
	 * @throws IOException
	 */
	public static void createSplitGeometryWithCache(String filePath, String dirCachePath, int grid) throws IOException{
		U.log("File Path ::"+filePath);
		List<String[]> readLines = FileUtil.readCsvFileWithoutHeader(filePath);
		int i = 0;
		int notSplitCount = 0;

		U.log("Start processing to create split geometry ......");
		
		for(String[] lines : readLines){
			i++;
			
			String geomHashCode = ""+lines[0].hashCode();
			
			U.log(i +" \t"+geomHashCode);
			
			//check if file is exist
			if(new File(dirCachePath + geomHashCode + ".txt").exists()){
				continue;
			}
			
			Geometry geom = ShatamGeometry.toGeometry(lines[0]);
//			U.log(geom.getNumGeometries());
			List<Geometry> geometries = ShatamSplitGeometry.splitOriginalGeometry(geom, grid); //set max number to maximum splitting
			
			StringBuilder sb = new StringBuilder();
			for(Geometry g1 : geometries){
				
				sb.append(g1.toString());
				sb.append("\n");
			}
			
			if(geometries.size() > 0){
				FileUtil.writeAllText(dirCachePath+geomHashCode+".txt" , sb.toString());
				U.log("Stored.");
			}else{
				notSplitCount++;
			}
			
		}
		
		U.log("Total no. for not splitting geometry ::"+notSplitCount);
	}
	
	private static void createSplitCsv(String filePath) throws IOException{
		U.log("File Path ::"+filePath);
		List<String[]> readLines = FileUtil.readCsvFile(filePath);
		int i = 0;
		
		CSVWriter writer = new CSVWriter(new FileWriter(filePath.replace(".csv", "_Split.csv")));

		U.log("Start processing to create split geometry ......");
		for(String[] lines : readLines){
			if(i++ == 0){
				writer.writeNext(lines);
				continue;
			}
			
			Geometry geom = ShatamGeometry.toGeometry(lines[0]);
//			U.log(geom.getNumGeometries());
			List<Geometry> geometries = ShatamSplitGeometry.splitOriginalGeometry(geom, 10); //set max number to maximum splitting
			
			for(Geometry g1 : geometries){
				
				
				String vals[] = new String[lines.length];

				vals[0] = g1.toString();	
				for(int j = 1; j < lines.length; j++){
					vals[j] = lines[j];
				}
				writer.writeNext(vals);
			}
		}
		writer.flush();
		writer.close();
		
		U.log("File is written at :"+filePath.replace(".csv", "_Split.csv"));
	}
	
	
	private static void testWithCsv(String filePath) throws IOException{
		U.log("File Path ::"+filePath);
		List<String[]> readLines = FileUtil.readCsvFileWithoutHeader(filePath);
		int i = 0;
		

		U.log("Start processing to create split geometry ......");
		for(String[] lines : readLines){
			U.log("i ::"+(i++));
			Geometry geom = ShatamGeometry.toGeometry(lines[0]);
//			U.log(geom.getNumGeometries());
			List<Geometry> geometries = ShatamSplitGeometry.splitOriginalGeometry(geom, 2); //set max number to maximum splitting
			
			for(Geometry g1 : geometries){
				U.log(g1.getArea() +"\t"+g1.getLength());
			}
		}
	}
	
	
	private static void test(String WKT){
		Geometry geom = ShatamGeometry.toGeometry(WKT);
		U.log(geom.getNumGeometries());
		List<Geometry> geometries = ShatamSplitGeometry.splitOriginalGeometry(geom, 4); //split(geom);
//		geometries.forEach(System.out::println);

		U.log(geometries.size());
		
		GeometryFactory gf = new GeometryFactory();
		GeometryCollection collection = gf.createGeometryCollection(geometries.toArray(new Geometry[] {}));
		U.log(collection.getNumGeometries());
//		U.log(collection);
		
		int j = 1;
		for(Geometry g1 : geometries){
			
			U.log(">>>"+g1.getArea() +"\t"+g1.getLength());
			
			/*if(g1.getLength() > 0.1){
				List<Geometry> geometries1 = ShatamSplitGeometry.splitOriginalGeometry(g1, 2);
				for(Geometry g2 : geometries1){
					U.log(""+g2.getArea() +"\t"+g2.getLength());
				}
			}*/
				
			try {
				FileUtil.writeAllText("/home/glady/workspaces/Parcel_Cache/Hazard_Zone/Dam_Inundation/Result_"+(j++)+".txt", g1.toString());
				U.log("File is written.");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
/*		try {
			FileUtil.writeAllText("/home/glady/workspaces/Parcel_Cache/Tester/Sawan/23_Feb_23/Invalid/InValid_3_Output_split.txt", collection.toString());
			U.log("File is written.");
		} catch (IOException e) {
			e.printStackTrace();
		}
*/	}

	public static List<Geometry> splitOriginalGeometry(Geometry geom, int grid){
		return splitOriginalGeometry(geom, grid, grid);
	}
	
	
	private static GeometryFactory geomFactory = new GeometryFactory();

	public static List<Geometry> splitOriginalGeometry(Geometry geom, int gridX, int gridY){
		
		U.log(geom.isValid());
		final Envelope envelope = geom.getEnvelopeInternal();
		double minX = envelope.getMinX();
		double maxX = envelope.getMaxX();
		
//		double midX = minX + (maxX - minX) / 2.0;
		double minY = envelope.getMinY();
		double maxY = envelope.getMaxY();
//		double midY = minY + (maxY - minY) / 2.0;
		
		double grid_x = ((maxX - minX) / gridX);
		double grid_y = ((maxY - minY) / gridY);
		
		List<Geometry> list = new ArrayList<>();


		boolean otherGeomFlag = false;
		
		for(int i = 0; i < gridX; i++){
			for(int j = 0; j < gridY; j++){
				
				double _xmin = minX + (i * grid_x);
				double _ymin = minY + (j * grid_y);
				
				double _xmax = _xmin + grid_x;
				double _ymax = _ymin + grid_y;
				
				Envelope env = new Envelope(_xmin, _xmax, _ymin, _ymax);
				
				Geometry splitGeom = null;
				try{
					splitGeom = JTS.toGeometry(env).intersection(geom);
				}catch(com.vividsolutions.jts.geom.TopologyException ex){
					U.log(ex);
					otherGeomFlag = true;
					break;
				}
				
				if(!splitGeom.isEmpty()){
					
					if(splitGeom instanceof Polygon){
//						U.log(splitGeom.getGeometryType());
/*						Polygon[] polys = new Polygon[1];
						polys[0] = (Polygon) splitGeom;
						splitGeom = geomFactory.createMultiPolygon(polys);
*/						
						splitGeom = convertPolygonToMultiPolygon(splitGeom);
					}
					if(splitGeom instanceof MultiPolygon)
						list.add(splitGeom);
					
					else if(splitGeom instanceof GeometryCollection){
						Geometry collectionGeom = convertGeometryCollectionToMultiPolygon(splitGeom);
						if(collectionGeom != null)
							list.add(collectionGeom);
						else{
							System.err.println("Found New shape ::"+splitGeom.getGeometryType() +" >> Not Process");
							otherGeomFlag = true;
							break;
						}
					}
					else{
						

						System.err.println("Found New shape ::"+splitGeom.getGeometryType());
//						U.log(splitGeom);
//						convertGeometryCollectionToMultiPolygon(splitGeom);
						
//						writeLineStringGeom(splitGeom);
//						U.log(convertGeometryCollectionToMultiPolygon(splitGeom));
						otherGeomFlag = true;
						break;
					}
				}
			}//eof for inner
			
			if(otherGeomFlag)break;
		}//eof for outer
		
		if(otherGeomFlag) list.clear();
		U.log("Total grid form = "+list.size());
		return list;
	}
	
	private static Geometry convertGeometryCollectionToMultiPolygon(Geometry input){
		List<Geometry> list = new ArrayList<>();
		
		int countLine = 0;
		int countPoint = 0;
		for(int i = 0; i< input.getNumGeometries(); i++){
			if(input.getGeometryN(i) instanceof Polygon || input.getGeometryN(i) instanceof MultiPolygon)
				list.add(input.getGeometryN(i));
			
			else if(input.getGeometryN(i) instanceof LineString){
				countLine++;
			}
			else if(input.getGeometryN(i) instanceof Point){
				countPoint++;
			}
		}
		U.log("Total Lines ::"+countLine);
		U.log("Total Point ::"+countPoint);
		
		/*
		 * If more than one LineString or Point contains in GeometryCollection, then we don't have confidence either we will get proper polygon or not.
		 */
//		if(countLine > 2){
//			writeLineStringGeom(input);
//		}
		if(countLine > 1 || countPoint > 1){
			return null;
		}
		
		
//		U.log(input);
		
		MultiPolygon result = null;
		if(list.size() > 1){
		    result = (MultiPolygon) geomFactory.buildGeometry(list);
		    result = (MultiPolygon) result.union();
		    
		    return result;
		}
		else if(list.size() == 1){
		    return convertPolygonToMultiPolygon(list.get(0));
		}
		return null;
	}
	
	private static Geometry convertPolygonToMultiPolygon(Geometry input){
		Polygon[] polys = new Polygon[1];
		polys[0] = (Polygon) input;
//		input = geomFactory.createMultiPolygon(polys);
		
		return geomFactory.createMultiPolygon(polys);
	}
}
