package com.shatam.waterview.geotools;

import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;

import org.geotools.geometry.jts.JTS;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

import com.shatam.utils.BoundingBox;
import com.shatam.utils.U;
public class Test {

	public static void main(String[] args) throws org.locationtech.jts.io.ParseException {
		
		WKTReader wkt = new WKTReader();
//		Geometry pl = (Geometry) wkt.read("POLYGON ((-73.73967247299998 40.76039775000004, -73.73980592999999 40.76034869400007, -73.73986747199996 40.760326073000044, -73.74002266099995 40.76057356200005, -73.73982766399996 40.76064523900004, -73.73967247299998 40.76039775000004))");
		Geometry pl = (Geometry) wkt.read("POLYGON((-110.98388671875 44.93369638969466,-103.99658203125001 45.026950453185464,-104.04052734375001 40.88029480552822,-111.07177734375 40.91351257612757,-110.98388671875 44.93369638969466))");
		Envelope e = pl.getEnvelopeInternal();
		
		double xMin = e.getMinX();
		double yMin = e.getMinY();
		double xMax = e.getMaxX(); 
		double yMax = e.getMaxY();
		
		//grid(5, 5, xMin, yMin, xMax, yMax);
		
		BoundingBox b2 = new BoundingBox(xMin, yMin, xMax, yMax);
		U.log(BoundingBox.createMultiPolygon(BoundingBox.gridBoundingBox(b2, 5)));
		
		
		//U.log(xMin+"\t"+yMin+"\t"+xMax+"\t"+yMax);
		
		U.log(BoundingBox.createPolygon(b2));
		
		List<BoundingBox> list = BoundingBox.gridBoundingBox(b2, 5);
		
		for(BoundingBox b : list) {
			//U.log(b.getXmin());
			//U.log(b.createPolygon());
			U.log(b.createMultiPolygon());
		}
//		U.log(e.centre().x+"-- "+e.centre().y);
//		split();
		
		BoundingBox b3 = new BoundingBox(21.0529736, 78.9952182, 21.2310022, 79.1788461);
		U.log(b3.createPolygon());
	}
	
	public static List<Geometry> split() throws ParseException {
	    List<Geometry> ret = new ArrayList<>();
	    
	    WKTReader wkt = new WKTReader();
	    Polygon p = (Polygon) wkt.read("POLYGON ((-73.73967247299998 40.76039775000004, -73.73980592999999 40.76034869400007, -73.73986747199996 40.760326073000044, -73.74002266099995 40.76057356200005, -73.73982766399996 40.76064523900004, -73.73967247299998 40.76039775000004))");
	    
	    
	    final Envelope envelope = p.getEnvelopeInternal();
	    double minX = envelope.getMinX();
	    double maxX = envelope.getMaxX();
	    double midX = minX + (maxX - minX) / 2.0;
	    double minY = envelope.getMinY();
	    double maxY = envelope.getMaxY();
	    double midY = minY + (maxY - minY) / 2.0;

	    Envelope llEnv = new Envelope(minX, midX, minY, midY);
	    Envelope lrEnv = new Envelope(midX, maxX, minY, midY);
	    Envelope ulEnv = new Envelope(minX, midX, midY, maxY);
	    Envelope urEnv = new Envelope(midX, maxX, midY, maxY);
	    Geometry ll = JTS.toGeometry(llEnv).intersection(p);
	    Geometry lr = JTS.toGeometry(lrEnv).intersection(p);
	    Geometry ul = JTS.toGeometry(ulEnv).intersection(p);
	    Geometry ur = JTS.toGeometry(urEnv).intersection(p);
	    ret.add(ll);
	    ret.add(lr);
	    ret.add(ul);
	    ret.add(ur);

	    U.log(ll+"\t"+lr+"\t"+ul+"\t"+ur);
	    return ret;
	  }
	
	
	public static void grid(int x, int y, double xMin, double yMin, double xMax, double yMax ) {
		
		double xD = xMax - xMin;
		double yD = yMax - yMin;
		
		double xV = xD/x;
		double yV = yD/y;
		
		for(int j=0; j<=x; j++) {
			
			double xMidMin = xMin+(j*xV);
			double xMidMax = xMin+((j+1)*xV);
			
			U.log("X: "+xMidMin+"\t"+xMidMax);
			
		}
		
		for(int i=0; i<=y; i++) {
			
			double yMidMin = yMin+(i*yV);
			double yMidMax = yMin+((i+1)*yV);
			
			U.log("Y: "+yMidMin+"\t"+yMidMax);
		}
		
		
	}
}
