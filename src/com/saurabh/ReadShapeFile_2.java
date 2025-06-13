package com.saurabh;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import java.math.BigDecimal;
import com.shatam.utils.FileUtil;
import com.shatam.utils.U;
import com.vividsolutions.jts.geom.MultiPolygon;

public class ReadShapeFile_2 {
	public static void main(String argsp[]) throws Exception {
		
		String filePath = "/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/LakewoodAOI/Area_of_Interest.shp";
		readSchema(filePath);
		
/*		File files[] = new File("/home/glady/workspaces/Parcel_Cache/Hazard_Zone/other_hazard/critical_habitant/crithab_all_shapefiles/All_USA/PCH_Etheostoma_trisella_20181228/").listFiles();
		
		for(File f : files){
			if(!f.getName().endsWith(".shp"))continue;
			U.log(f.getName());
			readSchema(f.getPath());
		}
*/
	
		///home/glady/workspaces/Parcel_Cache/Hazard_Zone/other_hazard/critical_habitant/crithab_all_shapefiles/All_USA
/*		File files[] = new File("/home/glady/workspaces/Parcel_Cache/Hazard_Zone/other_hazard/critical_habitant/crithab_all_shapefiles/All_USA/FCH_Charadrius_melodus_non_Great_Lakes_20090519/").listFiles();
		for(File f : files){
			if(!f.isDirectory())continue;
			
			File innerfiles[] = new File(f.getPath()).listFiles();
			for(File f1 : innerfiles){
				if(!f1.getName().endsWith(".shp"))continue;
				U.log(f1.getName());
				readSchema(f1.getPath());
			}
		}
*/	}
	
	public static void readSchema1(String filePath)throws IOException{

		FileDataStore store = FileDataStoreFinder.getDataStore(new File(
				filePath));

		SimpleFeatureSource featureSource = store.getFeatureSource();
		U.log(featureSource.getName());
		System.out.println(featureSource.getSchema());
		
		SimpleFeatureIterator simitr = featureSource.getFeatures().features();
		List<String[]> writeLines = new ArrayList<>();
		int count = 0;
		String header[] = null;
		
		while (simitr.hasNext()) {

			SimpleFeature feature = simitr.next();

			String g = feature.getDefaultGeometryProperty().toString();
//			Polygon geo = (Polygon) feature.getAttribute( "the_geom" );
//			System.out.println("geo ="+feature.getAttribute( "the_geom" ));
		    Collection<Property> properties = feature.getProperties();

		    
			String vals[] = new String[properties.size()];
			header = new String[properties.size()];
			
		    int i = 0, j = 0;
		    for(Property property:properties){
//		    	System.out.println(i+ "> "+property.getName() + "=" + property.getValue());
		    	
		    	//line content
		    	if(property.getValue() != null)
		    		vals[i++] = property.getValue().toString();
		    	
		    	//header 
		    	if(count == 0){		    		
		    		header[j++] = property.getName().toString();
		    	}
		    
		    }
		    if(count == 0){
	    		U.log("55"+Arrays.toString(header));
	    		writeLines.add(header);
	    	}
		    U.log("55"+Arrays.toString(vals));
		    writeLines.add(vals); //add to file
		    count++;
			
//		    if(count == 1)break;
		}
		
		U.log("Total Count :"+count);
		U.log("Record count :"+writeLines.size());
		
		
		FileUtil.writeCsvFile(writeLines, filePath.replace(".shp", "_convertedData.csv"));
	}
	
	public	static void readSchema(String filePath) throws IOException, FactoryException, MismatchedDimensionException, TransformException{
		U.log("filePath =="+filePath);
		FileDataStore store = FileDataStoreFinder.getDataStore(new File(filePath));
//		ShapefileDataStore store = new ShapefileDataStore(new File(filePath).toURL());
		
		SimpleFeatureSource featureSource = store.getFeatureSource();
		SimpleFeatureType schema = store.getSchema();
//		U.log(featureSource.getName());
		System.out.println(">>>>>>>> "+featureSource.getSchema());
		U.log(schema);
//		System.out.println("-----------"+schema.getCoordinateReferenceSystem());
		
		SimpleFeatureIterator simitr = featureSource.getFeatures().features();
		List<String[]> writeLines = new ArrayList<>();
		int count = 0;
		String header[] = null;
		
		CoordinateReferenceSystem dataCRS = schema.getCoordinateReferenceSystem();//CRS.decode("EPSG:26910");//
        CoordinateReferenceSystem worldCRS = CRS.decode("EPSG:4326", true);// map.getCoordinateReferenceSystem();
        boolean lenient = true; // allow for some error due to different datums
        MathTransform transform = CRS.findMathTransform(dataCRS, worldCRS, lenient);//CRS.findMathTransform(dataCRS, worldCRS, lenient);
		
		while (simitr.hasNext()) {

			SimpleFeature feature = simitr.next();

//		    Geometry geometry = (Geometry) feature.getDefaultGeometry();
/*			Geometry geometry = (Geometry) feature.getDefaultGeometry();//.toString()
			Geometry geometry2 = JTS.transform(geometry, transform);
*/			
//			System.out.println(geometry.isSimple() && geometry2.isSimple());
/*			if(!geometry.isSimple())
				U.log(geometry2);
*/			
//			Polygon geo = (Polygon) feature.getAttribute( "the_geom" );
//			System.out.println("geo ="+feature.getAttribute( "the_geom" ));
		    Collection<Property> properties = feature.getProperties();
		    
			String vals[] = new String[properties.size()];
			header = new String[properties.size()];
			U.log(properties.size());
		    int i = 0, j = 0;
		    for(Property property:properties){
		    	//if(!property.getName().toString().contains("ZCTA5CE20") && !property.getName().toString().contains("the_geom"))continue;
		    	//line content
		    	if(property.getValue() != null){
		    		if(property.getName().toString().equals("the_geom")){
//		    			vals[i++] = geometry2.toText();
//		    			vals[i++] = JTS.transform(geometry, transform).toText();
		    			vals[i++] = JTS.transform((Geometry)feature.getDefaultGeometry(), transform).toText();
//		    			U.log(vals[i-1]);
//		    			U.log(geometry2.toText());
		    		}
		    		else if(property.getName().toString().contains("MeterID")) {
				    	vals[i++] = new BigDecimal(property.getValue().toString()).toBigInteger().toString();
			    	}else
		    			vals[i++] = property.getValue().toString();
		    	}
		    	
		    	//header 
		    	if(count == 0){		    		
		    		header[j++] = property.getName().toString();
		    	}
		    
		    }
		    if(count == 0){
//	    		U.log(Arrays.toString(header));
	    		writeLines.add(header);
	    	}
		    writeLines.add(vals); //add to file
		    count++;
			
//		    if(count == 1)break;
		}
		
		U.log("Total Count :"+count);
		U.log("Record count :"+writeLines.size());

		FileUtil.writeCsvFile(writeLines, filePath.replace(".shp", "_convertedData.csv"));
	
	}
	public static void main1(String argsp[]) throws IOException {

		String filePath = "/home/glady/workspaces/Parcel_Cache/Colbert County_ AL/tl_2017_01033_faces.shp";

		FileDataStore store = FileDataStoreFinder.getDataStore(new File(
				filePath));

		SimpleFeatureSource featureSource = store.getFeatureSource();
		
		System.out.println(featureSource.getSchema());
		
		SimpleFeatureIterator simitr = featureSource.getFeatures().features();

		List<String[]> writeLines = new ArrayList<>();
		String header[] = {
				"TFID","STATEFP10","COUNTYFP10","TRACTCE10","BLKGRPCE10","BLOCKCE10","SUFFIX1CE","ZCTA5CE10","UACE10","PUMACE10","STATEFP","COUNTYFP",
				"TRACTCE","BLKGRPCE","COUSUBFP","SUBMCDFP","ESTATEFP","CONCTYFP","PLACEFP","AIANNHFP","AIANNHCE","COMPTYP","TRSUBFP","TRSUBCE","ANRCFP",
				"TTRACTCE","TBLKGPCE","ELSDLEA","SCSDLEA","UNSDLEA","CD115FP","SLDUST","SLDLST","CSAFP","CBSAFP","METDIVFP","CNECTAFP","NECTAFP","NCTADVFP","LWFLAG",
				"OFFSET","ATOTAL","INTPTLAT","INTPTLON","the_geom"
		};
		writeLines.add(header);
		int count = 0;
		
		while (simitr.hasNext()) {

			SimpleFeature feature = simitr.next();

			String g = feature.getDefaultGeometryProperty().toString();
			MultiPolygon geo = (MultiPolygon) feature.getAttribute( "the_geom" );
//			System.out.println("geo ="+geo);
			
			
		    Collection<Property> properties = feature.getProperties();
//		    U.log(properties.size());
		    String vals[] = new String[header.length];
		    int i = 0;
		    for(Property property:properties){
	//	    	System.out.println(i+ "> "+property.getName() + "=" + property.getValue());
		    	if(property.getName().toString().equals("the_geom")){
		    		vals[header.length-1] = ""+ property.getValue();
		    		continue;
		    	}
		    	vals[i++] = ""+ property.getValue();
			    
			    
		    }
		    writeLines.add(vals);
			count++;
//			if(count == 1)break;
		}//eof while
//		simitr.close();
		U.log("Total Count :"+count);
		FileUtil.writeCsvFile(writeLines, "/home/glady/workspaces/Parcel_Cache/Colbert County_ AL/tl_2017_01033.csv");
		
	}//eof main
}
