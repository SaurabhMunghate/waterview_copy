package com.saurabh;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ShapefileToPolygonConverter {

    public static void main(String[] args) {
        try {
            File shapefile = new File("/home/shatam-100/Down/WaterView_Data/District_Boundary_western/DistrictBoundary.shp");
            convertToPolygon(shapefile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void convertToPolygon(File shapefile) throws IOException {
        FileDataStore store = FileDataStoreFinder.getDataStore(shapefile);
        SimpleFeatureCollection features = store.getFeatureSource().getFeatures();

        // Define the new polygon feature type
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("PolygonFeatureType");
        builder.setCRS(features.getSchema().getCoordinateReferenceSystem());
        builder.add("geometry", MultiPolygon.class);

        // Add attributes from the original feature type using existing attribute descriptors
        List<AttributeDescriptor> attributes = features.getSchema().getAttributeDescriptors();
        for (AttributeDescriptor attribute : attributes) {
            // Use the existing attribute descriptor to add attributes
            builder.add((AttributeDescriptor) attribute);
        }

        SimpleFeatureType newFeatureType = builder.buildFeatureType();

        // Create a new feature collection with the new feature type
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(newFeatureType);
        SimpleFeatureIterator iterator = features.features();

        while (iterator.hasNext()) {
            SimpleFeature originalFeature = iterator.next();
            Geometry originalGeometry = (Geometry) originalFeature.getDefaultGeometry();

            // Convert the original geometry to a polygon (if necessary)
            GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
            MultiPolygon polygon = geometryFactory.createMultiPolygon((Polygon[]) new Geometry[]{originalGeometry});

            // Create a new feature with the polygon geometry
            featureBuilder.add(polygon);

            // Add attributes from the original feature
            for (AttributeDescriptor attribute : attributes) {
                featureBuilder.add(originalFeature.getAttribute(attribute.getName()));
            }

            SimpleFeature newFeature = featureBuilder.buildFeature(null);
            // Do something with the new feature, e.g., save it to a new shapefile
            // (implementation depends on your specific use case)
            System.out.println(newFeature);
        }

        iterator.close();
        store.dispose();
    }
}
