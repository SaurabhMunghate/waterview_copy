package com.saurabh;

import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.io.WKTWriter;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ShapefileToWKTConverter {
	static String wkt_final1 ="GEOMETRYCOLLECTION(";

	public static void main(String[] args) throws Exception {
		String shapefilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries/";
		File folder = new File(shapefilePath);
		if (folder.exists() && folder.isDirectory()) {
			// List files in the folder
			File[] files = folder.listFiles();
			System.out.println(files.length);
			for (File fil : files) {
				System.out.println(fil);
//            	fil = fil+"/AOI.shp";
				call(fil + "/AOI.shp");
			}
		}
		String WV_District = "/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries_Json/WV_District.txt";
//		FileWriter writer = new FileWriter(WV_District);
//		writer.write(wkt_final1);
		try (FileWriter writer = new FileWriter(WV_District)) {
			writer.write(wkt_final1);
//            System.out.println("Text written to the file successfully!");
		} catch (IOException e) {
			System.err.println("An error occurred while writing to the file: " + e.getMessage());
		}
//    	call(shapefilePath);
	}

	private static void call(String shapefilePath) throws Exception {
		// TODO Auto-generated method stub
		// Load the shapefile
		File shapefile = new File(shapefilePath);
		ShapefileDataStore dataStore = new ShapefileDataStore(shapefile.toURI().toURL());
		SimpleFeatureIterator features = dataStore.getFeatureSource().getFeatures().features();

		// Define a GeometryFactory and WKTWriter
		GeometryFactory geometryFactory = new GeometryFactory();
		WKTWriter wktWriter = new WKTWriter();

		// Get the default CRS of the shapefile
		CoordinateReferenceSystem sourceCRS = dataStore.getSchema().getCoordinateReferenceSystem();

		// Get the transform to WGS84 CRS
		MathTransform transform;
		try {
			transform = CRS.findMathTransform(sourceCRS, CRS.decode("EPSG:4326"), true);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		String wkt_final = "";
		// Iterate over features
		while (features.hasNext()) {
			SimpleFeature feature = features.next();

			// Get geometry attribute and transform to WGS84 (EPSG:4326)
			Geometry geometry = (Geometry) feature.getDefaultGeometry();
			Geometry transformedGeometry = JTS.transform(geometry, transform);

			// Convert transformed geometry to WKT
			String wkt = wktWriter.write(transformedGeometry);

			// Output WKT
//            System.out.println("WKT for Feature ID " + feature.getID() + ":");
			wkt = wkt.replace("MULTIPOLYGON (((", "").replace(")))", "");
			String[] wkt_arry = wkt.split(",");

			for (String st : wkt_arry) {
				String[] lat_lon = st.trim().split(" ");
				wkt_final = wkt_final + (lat_lon[1] + " " + lat_lon[0]) + ", ";
				wkt_final =wkt_final.replace("(", "").replace(")", "");
			}
			wkt_final = "MULTIPOLYGON ((( "+wkt_final + ")))";
			wkt_final = wkt_final.replace(", )))", " )))");
			wkt_final1 += ", "+wkt_final;
//            System.out.println(wkt_final);

		}
//        String file_Path="/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/";
		FileDataWrite(wkt_final,shapefilePath);
		// Close the feature iterator and data store
		features.close();
		dataStore.dispose();

	}

	private static void FileDataWrite(String textToWrite, String filePath) {
		// TODO Auto-generated method stub
		// Write the text to the file
		String[] district = filePath.split("/");
		System.out.println(district[district.length - 2]);
		filePath = filePath + ".geojson";
		filePath = filePath.replace("/AOI.shp", "").replace("DistrictBoundaries", "DistrictBoundaries_Json");
		try (FileWriter writer = new FileWriter(filePath)) {
			writer.write(textToWrite);
//            System.out.println("Text written to the file successfully!");
		} catch (IOException e) {
			System.err.println("An error occurred while writing to the file: " + e.getMessage());
		}
		System.out.println("File Path : " + filePath);
	}
}
