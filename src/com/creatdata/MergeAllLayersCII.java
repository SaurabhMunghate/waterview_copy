package com.creatdata;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geopkg.GeoPkgDataStoreFactory;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.WKTReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.shatam.utils.FileUtil;

public class MergeAllLayersCII {
	//HashMap<String, String[]> landscapeCodes=new HashMap<>();
	ArrayList<String[]> landscapeCodes=new ArrayList<String[]>();
	static String waterdistrictName="WVHEMET";
	public static void main(String[] args) {
		MergeAllLayersCII layers=new MergeAllLayersCII();
		String baseDirPath = "G:\\WaterDistrictData\\CII\\"+waterdistrictName+"\\";
		ArrayList<String> fileList= layers.listFilesInDirectory(baseDirPath);
		System.out.println(fileList);
		layers.FindUniqueCodes(fileList);
		System.out.println(layers.landscapeCodes);
		FileUtil.writeCsvFile(layers.landscapeCodes, baseDirPath+"All_Layers_Landscape_Data.csv");
		//ArrayList<String[]> landscapeData=layers.processLanadscapeFiles(fileList);
		//ArrayList<String[]> meterData=layers.processMeterFile(baseDirPath+"prd.meter_locations.csv");
		//ArrayList<String[]> premiseData=layers.processPremiseFile(baseDirPath+"prd.premise_bounds.csv");
		//layers.convertDataToGeoPackage(landscapeData,meterData,premiseData,baseDirPath);
		//FileUtil.writeCsvFile(landscapeData, baseDirPath+"All_Layers_Landscape_Data.csv");
		
	}
	public void FindUniqueCodes(ArrayList<String> fileList) {
		for (String files : fileList) {
			List<String[]> landscapeFileData=FileUtil.readCsvFileWithoutHeader(files);
			for (String[] newLine : landscapeFileData) {
				if(newLine[0].contains("Status")) continue;
				//if(!landscapeCodes.containsKey(newLine[10])) {
					String[] outArr= {newLine[10],newLine[0],newLine[2]};
					landscapeCodes.add(outArr);
					//landscapeCodes.put(newLine[10], outArr);
					break;
				//}
			}
		}
		
	}
	public void convertDataToGeoPackage(ArrayList<String[]> landscapeData,ArrayList<String[]> meterData,ArrayList<String[]> premiseData,String baseDirPath) {
		// TODO Auto-generated method stub
		try {
            // Define CRS (Coordinate Reference System), assuming WGS 84
            CoordinateReferenceSystem crs = CRS.decode("EPSG:4326");
            Connection connection = DriverManager.getConnection("jdbc:sqlite:" + baseDirPath + waterdistrictName + ".gpkg");
                Statement statement = connection.createStatement();

                // First, check if spatial extensions are enabled
                statement.execute("SELECT EnableGpkgSpatialIndex();");

                // Create spatial indexes for each table's geometry column
                String[] featureTypes = {"landscapedata", "meterdata", "premisedata"};
                for (String featureTypeName : featureTypes) {
                    // First, check if spatial index already exists
                    String checkIndexSQL = String.format(
                        "SELECT COUNT(*) FROM sqlite_master WHERE type='table' AND name='rtree_%s_geom'",
                        featureTypeName
                    );
                    ResultSet rs = statement.executeQuery(checkIndexSQL);
                    boolean indexExists = rs.getInt(1) > 0;
                    rs.close();

                    if (!indexExists) {
                        // Create the spatial index if it doesn't exist
                        String createIndexSQL = String.format(
                            "SELECT gpkgAddSpatialIndex('%s', 'geom');",
                            featureTypeName
                        );
                        statement.execute(createIndexSQL);
                        
                        // Verify the index was created
                        String verifyIndexSQL = String.format(
                            "SELECT COUNT(*) FROM rtree_%s_geom;",
                            featureTypeName
                        );
                        ResultSet verifyRs = statement.executeQuery(verifyIndexSQL);
                        int indexedFeatures = verifyRs.getInt(1);
                        verifyRs.close();
                        
                        System.out.println("Spatial index created for " + featureTypeName + 
                            " with " + indexedFeatures + " indexed features");
                    } else {
                        System.out.println("Spatial index already exists for " + featureTypeName);
                    }
                }

                // Update the gpkg_extensions table to record spatial index support
                String updateExtensionsSQL = 
                    "INSERT OR REPLACE INTO gpkg_extensions " +
                    "(table_name, column_name, extension_name, definition, scope) " +
                    "VALUES " +
                    "('landscapedata', 'geom', 'gpkg_rtree_index', 'http://www.geopackage.org/spec/#extension_rtree', 'write-only'), " +
                    "('meterdata', 'geom', 'gpkg_rtree_index', 'http://www.geopackage.org/spec/#extension_rtree', 'write-only'), " +
                    "('premisedata', 'geom', 'gpkg_rtree_index', 'http://www.geopackage.org/spec/#extension_rtree', 'write-only')";
                statement.execute(updateExtensionsSQL);

                statement.close();
                System.out.println("Spatial indexes successfully created and registered in GeoPackage.");
            // Build a feature type (schema) for the GeoPackage
            SimpleFeatureTypeBuilder landscapetypeBuilder = new SimpleFeatureTypeBuilder();
            landscapetypeBuilder.setName("landscapedata");
            landscapetypeBuilder.setCRS(crs);
            landscapetypeBuilder.add("geom", Polygon.class); // Geometry field for MultiPolygon
            landscapetypeBuilder.add("short", String.class); // Class attribute
            landscapetypeBuilder.add("description", String.class); // Description attribute
            landscapetypeBuilder.add("category", String.class); // Category attribute
            // Add other attributes as needed
            SimpleFeatureType landscapefeatureType = landscapetypeBuilder.buildFeatureType();
            
            SimpleFeatureTypeBuilder meterTypeBuilder = new SimpleFeatureTypeBuilder();
            meterTypeBuilder.setName("meterdata");
            meterTypeBuilder.setCRS(crs);
            meterTypeBuilder.add("geom", Point.class);
            meterTypeBuilder.add("premID", String.class);
            meterTypeBuilder.add("meterID", String.class);
            SimpleFeatureType meterFeatureType = meterTypeBuilder.buildFeatureType();
            
            SimpleFeatureTypeBuilder premiseTypeBuilder = new SimpleFeatureTypeBuilder();
            premiseTypeBuilder.setName("premisedata");
            premiseTypeBuilder.setCRS(crs);
            premiseTypeBuilder.add("geom", MultiPolygon.class);
            premiseTypeBuilder.add("premID", String.class);
            premiseTypeBuilder.add("I", String.class);
            premiseTypeBuilder.add("I_SLA", String.class);
            premiseTypeBuilder.add("INI", String.class);
            premiseTypeBuilder.add("INI_SLA", String.class);
            premiseTypeBuilder.add("NI", String.class);
            premiseTypeBuilder.add("Per_Alloca", String.class);
            premiseTypeBuilder.add("allocation", String.class);
            premiseTypeBuilder.add("consumption", String.class);
            SimpleFeatureType premiseFeatureType = premiseTypeBuilder.buildFeatureType();
            
            Map<String, Object> gpkgParams = new HashMap<>();
            gpkgParams.put(GeoPkgDataStoreFactory.DBTYPE.key, "geopkg");
            gpkgParams.put(GeoPkgDataStoreFactory.DATABASE.key, baseDirPath+waterdistrictName+".gpkg");
            DataStore gpkgDataStore = DataStoreFinder.getDataStore(gpkgParams);
         // Check if DataStore is created successfully
            if (gpkgDataStore == null) {
                throw new RuntimeException("Failed to create GeoPackage DataStore.");
            }

            // Create the schema if it doesn't exist
            if (!Arrays.asList(gpkgDataStore.getTypeNames()).contains("landscapedata")) {
                gpkgDataStore.createSchema(landscapefeatureType);
                System.out.println("Schema 'landscapedata' created successfully.");
            } 
            if (!Arrays.asList(gpkgDataStore.getTypeNames()).contains("meterdata")) {
                gpkgDataStore.createSchema(meterFeatureType);
                System.out.println("Schema 'meterdata' created successfully.");
            } 
            if (!Arrays.asList(gpkgDataStore.getTypeNames()).contains("premisedata")) {
                gpkgDataStore.createSchema(premiseFeatureType);
                System.out.println("Schema 'premisedata' created successfully.");
            }
            SimpleFeatureStore landscapefeatureStore = (SimpleFeatureStore) gpkgDataStore.getFeatureSource("landscapedata");
            SimpleFeatureStore meterFeatureStore = (SimpleFeatureStore) gpkgDataStore.getFeatureSource("meterdata");
            SimpleFeatureStore premiseFeatureStore = (SimpleFeatureStore) gpkgDataStore.getFeatureSource("premisedata");
          //  SimpleFeatureStore featureStore = (SimpleFeatureStore) gpkgDataStore.getFeatureSource("landscapedata");

            // Create a WKTReader to read geometries
            WKTReader wktReader = new WKTReader();

            // List to store features
            ListFeatureCollection landscapefeatureList = new ListFeatureCollection(landscapefeatureType);
            ListFeatureCollection meterFeatureList = new ListFeatureCollection(meterFeatureType);
            ListFeatureCollection premiseFeatureList = new ListFeatureCollection(premiseFeatureType);
            // Iterate over the ArrayList data and add to GeoPackage
            for (String[] row : landscapeData) {
                try {
                	if(row[0].contains("geom"))continue;
                    // Get the geometry from the 'geom' column in WKT format
                    String geomWKT = row[0]; // Assuming geom is in the first column
                    Geometry geometry =  wktReader.read(geomWKT);

                    // Create a feature builder
                    SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(landscapefeatureType);
                    if (geometry instanceof Polygon) {
                        Polygon polygon = (Polygon) geometry;
                        featureBuilder.add(polygon);
                    } else if (geometry instanceof MultiPolygon) {
                        MultiPolygon multiPolygon = (MultiPolygon) geometry;
                        featureBuilder.add(multiPolygon);
                    } else {
                        System.err.println("Unsupported geometry type: " + geometry.getGeometryType());
                        continue; // Skip unsupported geometry types
                    }
                    // Set geometry and other attributes
                    //featureBuilder.add(multiPolygon);
                    featureBuilder.add(row[1]); // Class attribute
                    featureBuilder.add(row[2]); // Description attribute
                    featureBuilder.add(row[3]); // Category attribute

                    // Create the feature and add it to the collection
                    SimpleFeature newFeature = featureBuilder.buildFeature(null);
                    landscapefeatureList.add(newFeature);
                } catch (Exception e) {
                    System.err.println("Error processing feature: " + e.getMessage());
                }
            }
            for (String[] row : meterData) {
                try {
                	if(row[0].contains("geom"))continue;
                    // Get the geometry from the 'geom' column in WKT format
                    String geomWKT = row[0]; // Assuming geom is in the first column
                    
                    Geometry geometry  =  wktReader.read(geomWKT);
                    
                    // Create a feature builder
                    SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(meterFeatureType);
                    /*if (geometry instanceof Polygon) {
                        Polygon polygon = (Polygon) geometry;
                        featureBuilder.add(polygon);
                    } else if (geometry instanceof MultiPolygon) {
                        MultiPolygon multiPolygon = (MultiPolygon) geometry;
                        featureBuilder.add(multiPolygon);
                    } else {
                        System.err.println("Unsupported geometry type: " + geometry.getGeometryType());
                        continue; // Skip unsupported geometry types
                    }*/
                    // Set geometry and other attributes
                    featureBuilder.add((Point)geometry);
                    featureBuilder.add(row[1]); // Class attribute
                    featureBuilder.add(row[2]); // Description attribute

                    // Create the feature and add it to the collection
                    SimpleFeature newFeature = featureBuilder.buildFeature(null);
                    meterFeatureList.add(newFeature);
                } catch (Exception e) {
                    System.err.println("Error processing feature: " + e.getMessage());
                }
            }
            for (String[] row : premiseData) {
                try {
                	if(row[0].contains("geom"))continue;
                    // Get the geometry from the 'geom' column in WKT format
                    String geomWKT = row[0]; // Assuming geom is in the first column
                    
                    Geometry geometry  =  wktReader.read(geomWKT);
                    
                    // Create a feature builder
                    SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(premiseFeatureType);
                    if (geometry instanceof Polygon) {
                        Polygon polygon = (Polygon) geometry;
                        featureBuilder.add(polygon);
                    } else if (geometry instanceof MultiPolygon) {
                        MultiPolygon multiPolygon = (MultiPolygon) geometry;
                        featureBuilder.add(multiPolygon);
                    } else {
                        System.err.println("Unsupported geometry type: " + geometry.getGeometryType());
                        continue; // Skip unsupported geometry types
                    }
                    // Set geometry and other attributes
                    //featureBuilder.add((Point)geometry);
                    featureBuilder.add(row[1]);//premID
                    featureBuilder.add(row[2]);//I
                    featureBuilder.add(row[3]);//I_SLA
                    featureBuilder.add(row[4]);//INI
                    featureBuilder.add(row[5]);//INI_SLA
                    featureBuilder.add(row[6]);//NI
                    featureBuilder.add(row[7]);//allocation
                    //featureBuilder.add(row[8]);
                    // Create the feature and add it to the collection
                    SimpleFeature newFeature = featureBuilder.buildFeature(null);
                    premiseFeatureList.add(newFeature);
                    
                    /*premiseTypeBuilder.add("the_geom", MultiPolygon.class);
                    premiseTypeBuilder.add("premID", String.class);
                    premiseTypeBuilder.add("I", String.class);
                    premiseTypeBuilder.add("I_SLA", String.class);
                    premiseTypeBuilder.add("INI", String.class);
                    premiseTypeBuilder.add("INI_SLA", String.class);
                    premiseTypeBuilder.add("NI", String.class);
                    premiseTypeBuilder.add("allocation", String.class);*/
                    //outList.add(new String[] {"geom","premid","I","I_SLA","INI","INI_SLA","NI","allocation"});
                } catch (Exception e) {
                    System.err.println("Error processing feature: " + e.getMessage());
                }
            }
            // Add features to the GeoPackage
            landscapefeatureStore.addFeatures(landscapefeatureList);
            meterFeatureStore.addFeatures(meterFeatureList);
            premiseFeatureStore.addFeatures(premiseFeatureList);
            System.out.println("ArrayList successfully converted to GeoPackage.");
            /*try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + baseDirPath + waterdistrictName + ".gpkg")) {
                Statement statement = connection.createStatement();

                // Create spatial indexes for each table's geometry column
                String[] featureTypes = {"landscapedata", "meterdata", "premisedata"};
                for (String featureTypeName : featureTypes) {
                    String createIndexSQL = String.format(
                        "SELECT gpkgAddSpatialIndex('%s', 'geom');", featureTypeName
                    );
                    statement.execute(createIndexSQL);
                    System.out.println("Spatial index created for " + featureTypeName);
                }
                statement.close();
            } catch (Exception e) {
                System.err.println("Error creating spatial index: " + e.getMessage());
            }*/
            // Clean up
            gpkgDataStore.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	public ArrayList<String[]> processPremiseFile(String premiseFile){
		ArrayList<String[]> outList=new ArrayList<>();
		outList.add(new String[] {"geom","premid","I","I_SLA","INI","INI_SLA","NI","allocation"});
		
			List<String[]> meterFileData=FileUtil.readCsvFileWithoutHeader(premiseFile);
			for (String[] newLine : meterFileData) {
				//U.log(Arrays.toString(newLine));
				String[] outArr= {newLine[71],newLine[0],newLine[58],newLine[59],newLine[61],newLine[62],newLine[63],""};
				outList.add(outArr);
				//break;
			}
			//break;
		
		return outList;
	}
	public ArrayList<String[]> processMeterFile(String meterFile){
		ArrayList<String[]> outList=new ArrayList<>();
		outList.add(new String[] {"geom","premid","meterid"});
		
			List<String[]> meterFileData=FileUtil.readCsvFileWithoutHeader(meterFile);
			for (String[] newLine : meterFileData) {
				//U.log(Arrays.toString(newLine));
				String[] outArr= {newLine[10],newLine[0],newLine[1]};
				outList.add(outArr);
				//break;
			}
			//break;
		
		return outList;
	}
	public ArrayList<String[]> processLanadscapeFiles(ArrayList<String> fileList){
		ArrayList<String[]> outList=new ArrayList<>();
		outList.add(new String[] {"geom","class","desc","category"});
		for (String files : fileList) {
			List<String[]> landscapeFileData=FileUtil.readCsvFileWithoutHeader(files);
			for (String[] newLine : landscapeFileData) {
				//U.log(Arrays.toString(newLine));
				String[] outArr= {newLine[12],newLine[10],newLine[2],newLine[0]};
				outList.add(outArr);
				//break;
			}
			//break;
		}
		return outList;
	}
	public ArrayList<String> listFilesInDirectory(String dirPath){
		ArrayList<String> fileArryaList=new ArrayList<>();
		File directory = new File(dirPath);
        File[] filesList = directory.listFiles();
        if (filesList != null && directory.isDirectory()) {
        	for (File file : filesList) {
                if (file.isFile()&&file.getName().endsWith(".csv")&&file.getName().startsWith("Landscape_Area")) {
                	fileArryaList.add(file.getAbsolutePath());
                    //System.out.println("File: " + file.getName());
                } 
            }
        }
        return fileArryaList;
	} 
}
