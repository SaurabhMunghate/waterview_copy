//package com.shatam.cimis;
package com.creatdata;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.*;

import org.apache.commons.collections.TreeBag;
import org.geotools.data.*;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.simple.*;
import org.geotools.geopkg.GeoPkgDataStoreFactory;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.WKTReader;
import org.opengis.feature.simple.*;
import org.geotools.referencing.CRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.shatam.utils.FileUtil;

public class GeoPackageCreator {
    private final String baseDirPath;
    private final String waterdistrictName;
    private final CoordinateReferenceSystem crs;
    private DataStore gpkgDataStore;
    private Connection connection;
    private static final String GEOM_COLUMN = "geom";
    
    public GeoPackageCreator(String baseDirPath, String waterdistrictName) throws Exception {
        this.baseDirPath = baseDirPath;
        this.waterdistrictName = waterdistrictName;
        this.crs = CRS.decode("EPSG:4326");
        initialize();
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
    private void initialize() throws Exception {
        // Initialize GeoPackage store
        Map<String, Object> gpkgParams = new HashMap<>();
        gpkgParams.put(GeoPkgDataStoreFactory.DBTYPE.key, "geopkg");
        gpkgParams.put(GeoPkgDataStoreFactory.DATABASE.key, baseDirPath + waterdistrictName + ".gpkg");
        gpkgDataStore = DataStoreFinder.getDataStore(gpkgParams);
        
        if (gpkgDataStore == null) {
            throw new RuntimeException("Failed to create GeoPackage DataStore.");
        }
        Properties prop = new Properties();
		prop.setProperty("enable_shared_cache", "true");
		prop.setProperty("enable_load_extension", "true");
		prop.setProperty("enable_spatialite", "true");
        
        connection = DriverManager.getConnection("jdbc:sqlite:" + baseDirPath + waterdistrictName + ".gpkg",prop);
        
    }
    
    private SimpleFeatureType createFeatureType(String name, Class<?> geometryType, Map<String, Class<?>> attributes) {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName(name);
        builder.setCRS(crs);
        builder.add(GEOM_COLUMN, geometryType);
        
        for (Map.Entry<String, Class<?>> attribute : attributes.entrySet()) {
        	System.out.println(attribute.getKey()+" -> "+attribute.getValue());
            builder.add(attribute.getKey(), attribute.getValue());
        }
        
        return builder.buildFeatureType();
    }
    
    private void createSchema(SimpleFeatureType featureType) throws IOException {
        if (!Arrays.asList(gpkgDataStore.getTypeNames()).contains(featureType.getTypeName())) {
            gpkgDataStore.createSchema(featureType);
            System.out.println("Schema '" + featureType.getTypeName() + "' created successfully.");
        }
    }
    
    private void addFeatures(String typeName, SimpleFeatureType featureType, List<String[]> data) throws Exception {
        SimpleFeatureStore featureStore = (SimpleFeatureStore) gpkgDataStore.getFeatureSource(typeName);
        ListFeatureCollection featureList = new ListFeatureCollection(featureType);
        WKTReader wktReader = new WKTReader();
        
        for (String[] row : data) {
            if (row[0].contains(GEOM_COLUMN)) continue;
            
            try {
                Geometry geometry = wktReader.read(row[0]);
                SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(featureType);
                if (geometry instanceof Polygon) {
                    Polygon polygon = (Polygon) geometry;
                    featureBuilder.add(polygon);
                } else if (geometry instanceof MultiPolygon) {
                    MultiPolygon multiPolygon = (MultiPolygon) geometry;
                    featureBuilder.add(multiPolygon);
                }else if (geometry instanceof Point) {
                	Point multiPolygon = (Point) geometry;
                    featureBuilder.add(multiPolygon);
                }
                //else 
                // Add geometry
                //if (geometry.getClass().equals(featureType.getGeometryDescriptor().getType().getBinding())) {
                //    featureBuilder.add(geometry);
                //} 
                else {
                    System.err.println("Geometry type mismatch for " + typeName + ": " + geometry.getGeometryType());
                    continue;
                }
                
                // Add attributes
                for (int i = 1; i < row.length; i++) {
                    featureBuilder.add(row[i]);
                }
                
                SimpleFeature feature = featureBuilder.buildFeature(null);
                featureList.add(feature);
            } catch (Exception e) {
                System.err.println("Error processing feature in " + typeName + ": " + e.getMessage());
            }
        }
        
        featureStore.addFeatures(featureList);
    }
    /* //			resultSet.getObject("the_geom")+"",
            //resultSet.getString("premID"),
            //resultSet.getString("I"),
            //resultSet.getString("I_SLA"),
            //resultSet.getString("INI"),
            //resultSet.getString("INI_SLA"),
            //resultSet.getString("NI"),
            //resultSet.getString("percentAllocation"),
            //resultSet.getString("Allocation"),
            //resultSet.getString("Consumption")};
     * */
    private void createSpatialIndex() throws SQLException {
    	try (Statement statement = connection.createStatement()) {
    		statement.execute("SELECT load_extension('G:\\mod_spatialite-5.1.0-win-amd64\\mod_spatialite.dll');");
            String[] featureTypes = {"landscapedata", "meterdata", "premisedata"};
            
            // Create spatial indexes
            for (String featureType : featureTypes) {
                try {
                    // Create RTree index table
                    String createRTreeSQL = String.format(
                        "CREATE VIRTUAL TABLE rtree_%s_geom USING rtree(" +
                        "id, minx, maxx, miny, maxy)",
                        featureType
                    );
                    statement.execute(createRTreeSQL);

                    // Populate the RTree index
                    String populateIndexSQL = String.format(
                        "INSERT INTO rtree_%s_geom " +
                        "SELECT fid, ST_MinX(geom), ST_MaxX(geom), ST_MinY(geom), ST_MaxY(geom) " +
                        "FROM %s",
                        featureType, featureType
                    );
                    statement.execute(populateIndexSQL);

                    // Create triggers to maintain the spatial index
                    createIndexTriggers(statement, featureType);

                    System.out.println("Spatial index created for " + featureType);
                } catch (SQLException e) {
                    if (e.getMessage().contains("table already exists")) {
                        System.out.println("Spatial index already exists for " + featureType);
                    } else {
                        throw e;
                    }
                }
            }

            // Update gpkg_extensions table
            updateGpkgExtensions(statement, featureTypes);
            
            System.out.println("Spatial indexes successfully created and registered.");
        }
    }
    
    private void createSpatialIndexForTable(Statement statement, String featureType) throws SQLException {
        // Check if index exists
        String checkIndexSQL = String.format(
            "SELECT COUNT(*) FROM sqlite_master WHERE type='table' AND name='rtree_%s_%s'",
            featureType, GEOM_COLUMN
        );
        
        try (ResultSet rs = statement.executeQuery(checkIndexSQL)) {
            if (rs.getInt(1) == 0) {
                // Create index
                statement.execute(String.format(
                    "SELECT gpkgAddSpatialIndex('%s', '%s');",
                    featureType, GEOM_COLUMN
                ));
                
                // Verify index
                try (ResultSet verifyRs = statement.executeQuery(
                    String.format("SELECT COUNT(*) FROM rtree_%s_%s;", featureType, GEOM_COLUMN))) {
                    System.out.println("Spatial index created for " + featureType + 
                        " with " + verifyRs.getInt(1) + " indexed features");
                }
            } else {
                System.out.println("Spatial index already exists for " + featureType);
            }
        }
    }
    private void createIndexTriggers(Statement statement, String featureType) throws SQLException {
        // Insert trigger
        String insertTrigger = String.format(
            "CREATE TRIGGER rtree_%s_geom_insert AFTER INSERT ON %s " +
            "WHEN (NEW.geom NOT NULL AND NOT ST_IsEmpty(NEW.geom)) " +
            "BEGIN " +
            "  INSERT OR REPLACE INTO rtree_%s_geom VALUES (" +
            "    NEW.fid, " +
            "    ST_MinX(NEW.geom), ST_MaxX(NEW.geom), " +
            "    ST_MinY(NEW.geom), ST_MaxY(NEW.geom)" +
            "  ); " +
            "END;",
            featureType, featureType, featureType
        );
        statement.execute(insertTrigger);

        // Update trigger
        String updateTrigger = String.format(
            "CREATE TRIGGER rtree_%s_geom_update AFTER UPDATE ON %s " +
            "WHEN (NEW.geom NOT NULL AND NOT ST_IsEmpty(NEW.geom)) " +
            "BEGIN " +
            "  INSERT OR REPLACE INTO rtree_%s_geom VALUES (" +
            "    NEW.fid, " +
            "    ST_MinX(NEW.geom), ST_MaxX(NEW.geom), " +
            "    ST_MinY(NEW.geom), ST_MaxY(NEW.geom)" +
            "  ); " +
            "END;",
            featureType, featureType, featureType
        );
        statement.execute(updateTrigger);

        // Delete trigger
        String deleteTrigger = String.format(
            "CREATE TRIGGER rtree_%s_geom_delete AFTER DELETE ON %s " +
            "WHEN (OLD.geom NOT NULL AND NOT ST_IsEmpty(OLD.geom)) " +
            "BEGIN " +
            "  DELETE FROM rtree_%s_geom WHERE id = OLD.fid; " +
            "END;",
            featureType, featureType, featureType
        );
        statement.execute(deleteTrigger);
    }
    private void updateGpkgExtensions(Statement statement, String[] featureTypes) throws SQLException {
    	statement.execute(
    	        "CREATE TABLE IF NOT EXISTS gpkg_extensions (" +
    	        "  table_name TEXT," +
    	        "  column_name TEXT," +
    	        "  extension_name TEXT NOT NULL," +
    	        "  definition TEXT NOT NULL," +
    	        "  scope TEXT NOT NULL," +
    	        "  CONSTRAINT ge_tce UNIQUE (table_name, column_name, extension_name)" +
    	        ")"
    	    );

    	    // Insert extension records
    	    StringBuilder sql = new StringBuilder(
    	        "INSERT OR REPLACE INTO gpkg_extensions (table_name, column_name, extension_name, definition, scope) VALUES "
    	    );
    	    
    	    for (int i = 0; i < featureTypes.length; i++) {
    	        if (i > 0) sql.append(",");
    	        sql.append(String.format(
    	            "('%s', '%s', 'gpkg_rtree_index', 'http://www.geopackage.org/spec/#extension_rtree', 'write-only')",
    	            featureTypes[i], GEOM_COLUMN
    	        ));
    	    }
    	    
    	    statement.execute(sql.toString());
    }
    
    public void createGeoPackage(List<String[]> landscapeData, List<String[]> meterData, List<String[]> premiseData) {
        try {
            // Create feature types
            Map<String, Class<?>> landscapeAttributes = new LinkedHashMap<>();
            landscapeAttributes.put("short", String.class);
            landscapeAttributes.put("description", String.class);
            landscapeAttributes.put("category", String.class);
            SimpleFeatureType landscapeFeatureType = createFeatureType("landscapedata", MultiPolygon.class, landscapeAttributes);
            
            Map<String, Class<?>> meterAttributes = new LinkedHashMap<>();
            meterAttributes.put("premID", String.class);
            meterAttributes.put("meterID", String.class);
            SimpleFeatureType meterFeatureType = createFeatureType("meterdata", Point.class, meterAttributes);
            
            Map<String, Class<?>> premiseAttributes = new LinkedHashMap<String, Class<?>>();
            premiseAttributes.put("premID", String.class);
            premiseAttributes.put("I", String.class);
            premiseAttributes.put("I_SLA", String.class);
            premiseAttributes.put("INI", String.class);
            premiseAttributes.put("INI_SLA", String.class);
            premiseAttributes.put("NI", String.class);
            premiseAttributes.put("Per_Alloca", String.class);
            premiseAttributes.put("allocation", String.class);
            premiseAttributes.put("consumption", String.class);
            SimpleFeatureType premiseFeatureType = createFeatureType("premisedata", MultiPolygon.class, premiseAttributes);
            //			resultSet.getObject("the_geom")+"",
            //resultSet.getString("premID"),
            //resultSet.getString("I"),
            //resultSet.getString("I_SLA"),
            //resultSet.getString("INI"),
            //resultSet.getString("INI_SLA"),
            //resultSet.getString("NI"),
            //resultSet.getString("percentAllocation"),
            //resultSet.getString("Allocation"),
            //resultSet.getString("Consumption")};

            // Create schemas
            createSchema(landscapeFeatureType);
            createSchema(meterFeatureType);
            createSchema(premiseFeatureType);
            
            // Add features
            addFeatures("landscapedata", landscapeFeatureType, landscapeData);
            addFeatures("meterdata", meterFeatureType, meterData);
            addFeatures("premisedata", premiseFeatureType, premiseData);
            
            // Create spatial indexes
            createSpatialIndex();
            
        } catch (Exception e) {
            System.err.println("Error creating GeoPackage: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cleanup();
        }
    }
    
    private void cleanup() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
            if (gpkgDataStore != null) {
                gpkgDataStore.dispose();
            }
        } catch (Exception e) {
            System.err.println("Error during cleanup: " + e.getMessage());
        }
    }
}