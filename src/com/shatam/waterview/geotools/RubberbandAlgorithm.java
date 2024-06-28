/**
 * @author Sawan Meshram
 */
package com.shatam.waterview.geotools;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

import com.shatam.utils.U;
//import com.vividsolutions.jts.geom.Geometry;
//import com.vividsolutions.jts.geom.GeometryFactory;
//import com.vividsolutions.jts.geom.MultiPolygon;
//import com.vividsolutions.jts.geom.Polygon;
//import com.vividsolutions.jts.io.ParseException;
//import com.vividsolutions.jts.io.WKTReader;
//import com.vividsolutions.jts.geom.LineString;
//import com.vividsolutions.jts.geom.LinearRing;
//import com.vividsolutions.jts.geom.MultiLineString;
//import com.vividsolutions.jts.geom.MultiPoint;
//import com.vividsolutions.jts.geom.Point;

public class RubberbandAlgorithm {
	private static WKTReader reader = new WKTReader();
	
	private static GeometryFactory geomFactory = new GeometryFactory();

	public static String createRubberbandMultipolygonWKT(String multiPolygonWKT) throws ParseException{
//		Geometry g = reader.read(multiPolygonWKT);
//		Geometry convexHull = g.convexHull();
		
		Geometry convexHull = reader.read(multiPolygonWKT).convexHull();
		
//		Geometry convexHull = ShatamGeometry.toGeometry(multiPolygonWKT).convexHull();
/*		if(convexHull instanceof LineString || convexHull instanceof LinearRing || convexHull instanceof MultiLineString
				|| convexHull instanceof Point || convexHull instanceof MultiPoint || convexHull instanceof MultiPolygon) return null;
*/				
				

		if(convexHull instanceof Polygon) {
			Polygon[] polys = new Polygon[1];
			polys[0] = (Polygon) convexHull;
/*			MultiPolygon mPoly = geomFactory.createMultiPolygon(polys);
			U.log("Geometry ::"+mPoly.toText());
			U.log("Geometry-1 ::"+mPoly.toString());
*/			
			return geomFactory.createMultiPolygon(polys).toText();
		}
		return null;
	}

	
	public static Geometry createRubberband(String multiPolygonWKT){
	
		Geometry convexHull = null;
		try {
			convexHull = reader.read(multiPolygonWKT).convexHull();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
			

		if(convexHull instanceof Polygon) {
			Polygon[] polys = new Polygon[1];
			polys[0] = (Polygon) convexHull;
/*			MultiPolygon mPoly = geomFactory.createMultiPolygon(polys);
			U.log("Geometry ::"+mPoly.toText());
			U.log("Geometry-1 ::"+mPoly.toString());
*/			
			return geomFactory.createMultiPolygon(polys);
		}
		return null;
	}
}
