package com.shatam.waterview.geotools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.UIManager;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.Transaction;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.swing.data.JFileDataStoreChooser;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.shatam.utils.FileUtil;
import com.shatam.utils.U;
//import com.vividsolutions.jts.geom.Coordinate;
//import com.vividsolutions.jts.geom.GeometryFactory;
//import com.vividsolutions.jts.geom.Point;
//import com.vividsolutions.jts.geom.Polygon;


public class Csv2Shape {

	public static void main(String[] args) throws Exception {

        String inputCsvPath = "/home/glady/workspaces/Parcel_Cache/Shape/ExtractDeltaComputerSystem_ColbertCounty_Sample.csv";

        
        /*
         * We use the DataUtilities class to create a FeatureType that will describe the data in our
         * shapefile.
         *
         * See also the createFeatureType method below for another, more flexible approach.
         */
        //"the_geom:Point:srid=4326,"
        SimpleFeatureType TYPE =
                DataUtilities.createType(
                        "Location",
                        "the_geom:Polygon,"
                                + // <- the geometry attribute: Point type
                                "parcel:String,"
//                                + // <- a String attribute
//                                "number:Integer," // a number attribute
                                +"name:String,area:String,appaised:String,assessed:String,street:String,desc:String,tax:String,ppin:String,account:String,"
                                +"taxprice:String,taxstatus:String,wkid:String,latestwkid:String,refurl:String"
                        );
        CoordinateReferenceSystem crs = CRS.decode( "EPSG:4326" );

      
        TYPE = DataUtilities.createSubType( TYPE, null, crs );
        
        System.out.println("TYPE:" + TYPE);
        
        
        /*
         * A list to collect features as we create them.
         */
        List<SimpleFeature> features = new ArrayList<>();

        /*
         * GeometryFactory will be used to create the geometry attribute of each feature,
         * using a Point object for the location.
         */
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE);
         
        //File Read
        List<String[]> readLines = FileUtil.readCsvFileWithoutHeader(inputCsvPath);
        
        for(String tokens[] : readLines) {
/*        	if(tokens[16].length()<3)
            	tokens[16] = "0";
            
            if(tokens[17].length()<3)
            	tokens[16] = "0";
*/
        	
        	if(tokens[15].isEmpty())continue;
        	
            //read csv record
/*            double latitude = Double.parseDouble(tokens[16].replaceAll("\\]|\\[|\"", ""));
            double longitude = Double.parseDouble(tokens[17].replaceAll("\\[|\\]|\"", ""));
*/            String name = tokens[0];
            String acre = tokens[1];
            String appraised = tokens[2];
            String assessed = tokens[3];
            String parcel = (tokens[4].replaceAll("\\.|-", ""));
            String street = tokens[5];
            String desc = tokens[6];
            String tax = tokens[7];
            String ppin = tokens[8];
            String account = tokens[9];
            String taxprice = tokens[10].replaceAll("\\.", "");
            String taxstatus = tokens[11];
            String refurl = tokens[12];
            String wkid = tokens[13];
            String latestwkid = tokens[14];
            
            String geodata = tokens[15];

            //organized the data for shape file
            /* Longitude (= x coord) first ! */
//            Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));
           
            Polygon polygon =  geometryFactory.createPolygon(getCoordinates(geodata));

            featureBuilder.add(polygon);
            featureBuilder.add(parcel);
            featureBuilder.add(name);
            featureBuilder.add(acre);
            featureBuilder.add(appraised);
            featureBuilder.add(assessed);
            featureBuilder.add(street);
            featureBuilder.add(desc);
            featureBuilder.add(tax);
            featureBuilder.add(ppin);
            featureBuilder.add(account);
            featureBuilder.add(taxprice);
            featureBuilder.add(taxstatus);
            featureBuilder.add(wkid);
            featureBuilder.add(latestwkid);
            featureBuilder.add(refurl);
//           
            SimpleFeature feature = featureBuilder.buildFeature(null);
            features.add(feature);
        }
        
        /*
         * Get an output file name and create the new shapefile
         */
        File newFile = getNewShapeFile(new File(inputCsvPath));

        ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();

        Map<String, Serializable> params = new HashMap<>();
        params.put("url", newFile.toURI().toURL());
        params.put("create spatial index", Boolean.TRUE);

        ShapefileDataStore newDataStore =
                (ShapefileDataStore) dataStoreFactory.createNewDataStore(params);

        /*
         * TYPE is used as a template to describe the file contents
         */
        newDataStore.createSchema(TYPE);
        
        
        
        /*
         * Write the features to the shapefile
         */
        Transaction transaction = new DefaultTransaction("create");

        String typeName = newDataStore.getTypeNames()[0];
        SimpleFeatureSource featureSource = newDataStore.getFeatureSource(typeName);
        SimpleFeatureType SHAPE_TYPE = featureSource.getSchema();
        /*
         * The Shapefile format has a couple limitations:
         * - "the_geom" is always first, and used for the geometry attribute name
         * - "the_geom" must be of type Point, MultiPoint, MuiltiLineString, MultiPolygon
         * - Attribute names are limited in length
         * - Not all data types are supported (example Timestamp represented as Date)
         *
         * Each data store has different limitations so check the resulting SimpleFeatureType.
         */
        System.out.println("SHAPE:" + SHAPE_TYPE);

        if (featureSource instanceof SimpleFeatureStore) {
            SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;
            /*
             * SimpleFeatureStore has a method to add features from a
             * SimpleFeatureCollection object, so we use the ListFeatureCollection
             * class to wrap our list of features.
             */
            SimpleFeatureCollection collection = new ListFeatureCollection(TYPE, features);
            featureStore.setTransaction(transaction);
            try {
                featureStore.addFeatures(collection);
                transaction.commit();
            } catch (Exception problem) {
                problem.printStackTrace();
                transaction.rollback();
            } finally {
                transaction.close();
            }
            System.exit(0); // success!
        } else {
            System.out.println(typeName + " does not support read/write access");
            System.exit(1);
        }
    }
	
	
	private static Coordinate[] getCoordinates(String str){
    	Coordinate[] coords  = null;
    	
		String vals[] = str.replaceAll("\\[\\[|\\]\\]", "").split("\\],\\[");
		coords = new Coordinate[vals.length];
		int i = 0;
		for(String val : vals){
			coords[i++] = new Coordinate(Double.parseDouble(val.split(",")[0].trim()), Double.parseDouble(val.split(",")[1].trim())); //lng, lat
		}
		return coords;
    }
	
	 /**
     * Prompt the user for the name and path to use for the output shapefile
     *
     * @param csvFile the input csv file used to create a default shapefile name
     * @return name and path for the shapefile as a new File object
     */
    private static File getNewShapeFile(File csvFile) {
        String path = csvFile.getAbsolutePath();
        String newPath = path.substring(0, path.length() - 4) + ".shp";
        return new File(newPath);
    }
	
}
