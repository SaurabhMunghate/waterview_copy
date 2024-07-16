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

public class ReadShapeFile {
	public static void main(String argsp[]) throws Exception {
		
//		String filePath = "/home/shatam-17/NEWTESTINGFILES2020(MEXICO)/_Markup_16-Nov-2023_06_58_37/Markup_16-Nov-2023_06_58_37.shp";
//		String filePath = "/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries/camrosa/AOI.shp";
		
//		readSchema(filePath);\
//		readSchema("");
//		readSchema("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries/banning/AOI.shp");
//		readSchema("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries/bellflower/AOI.shp");
//		readSchema("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries/beverly_hills/AOI.shp");
//		readSchema("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries/brentwood/AOI.shp");
//		readSchema("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries/cal_los_angeles/AOI.shp");
//		readSchema("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries/cal_sacramento/AOI.shp");
//		readSchema("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries/cal_ventura/AOI.shp");
//		readSchema("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries/camarillo/AOI.shp");
//		readSchema("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries/camrosa/AOI.shp");
//		readSchema("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries/carlsbad/AOI.shp");
//		readSchema("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries/casitas/AOI.shp");
//		readSchema("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries/dixon/AOI.shp");
//		readSchema("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries/east_valley/AOI.shp");
//		readSchema("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries/escondido/AOI.shp");
//		readSchema("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries/fairfield/AOI.shp");
//		readSchema("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries/folsom/AOI.shp");
//		readSchema("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries/glendale/AOI.shp");
//		readSchema("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries/indian_wells/AOI.shp");
//		readSchema("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries/lake_arrowhead/AOI.shp");
//		readSchema("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries/las_virgenes/AOI.shp");
//		readSchema("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries/modesto/AOI.shp");
//		readSchema("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries/montecito/AOI.shp");
//		readSchema("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries/monte_vista/AOI.shp");
//		readSchema("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries/myoma_dunes/AOI.shp");
//		readSchema("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries/newman/AOI.shp");
//		readSchema("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries/nipomo/AOI.shp");
//		readSchema("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries/olivenhain/AOI.shp");
//		readSchema("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries/ontario/AOI.shp");
//		readSchema("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries/orchard_dale/AOI.shp");
//		readSchema("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries/oxnard/AOI.shp");
//		readSchema("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries/pomona/AOI.shp");
//		readSchema("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries/porterville/AOI.shp");
//		readSchema("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries/redwood_city/AOI.shp");
//		readSchema("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries/rialto/AOI.shp");
//		readSchema("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries/rincon/AOI.shp");
//		readSchema("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries/roseville/AOI.shp");
//		readSchema("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries/rowland/AOI.shp");
//		readSchema("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries/rubidoux/AOI.shp");
//		readSchema("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries/san_gabriel_county/AOI.shp");
//		readSchema("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries/san_jose/AOI.shp");
//		readSchema("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries/san_luis_obispo/AOI.shp");
//		readSchema("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries/santa_barbara/AOI.shp");
//		readSchema("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries/santa_monica/AOI.shp");
//		readSchema("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries/simi_valley/AOI.shp");
//		readSchema("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries/suisun/AOI.shp");
//		readSchema("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries/trabuco_canyon/AOI.shp");
//		readSchema("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries/vacaville/AOI.shp");
//		readSchema("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries/vallejo/AOI.shp");
//		readSchema("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries/walnut_valley/AOI.shp");
//		readSchema("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries/western/AOI.shp");
		
//		readSchema("/home/shatam-100/Down/WaterView_Data/San_Jose_/AOI.shp");
//		readSchema("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries/las_virgenes/AOI.shp");
		readSchema("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries_SHP/dixon/AOI.shp");
		
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
		
		
		FileUtil.writeCsvFile(writeLines, filePath.replace(".shp",".csv"));
	}
	
	public	static void readSchema(String filePath) throws IOException, FactoryException, MismatchedDimensionException, TransformException{
		String[] filename = filePath.split("/");
		System.out.println(filename[filename.length-2]);
		String Dname = filename[filename.length-2];
		System.out.println(Dname);
		
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
		filePath = filePath.replace("AOI", "");
		filePath = filePath.replace("DistrictBoundaries", "DistrictBoundaries_CSV").replace(Dname+"/", "").replace(".shp", Dname+"_.csv");
		System.out.println(filePath);
		FileUtil.writeCsvFile(writeLines,filePath );
	
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
