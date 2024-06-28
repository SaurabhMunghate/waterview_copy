/**
 * @author Sawan Meshram
 */
package com.shatam.waterview.geotools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.operation.polygonize.Polygonizer;

import com.shatam.utils.BoundingBox;
import com.shatam.utils.CA_County;
import com.shatam.utils.FileUtil;
import com.shatam.utils.U;
import com.shatam.utils.USA_State;
import com.shatam.utils.USStates;

public class ShatamGeometry {
/*	public static void main(String[] args) {
		load_CA_Counties_Boundaries();
		BoundingBox box = getBoundingBox(CA_County.ALPINE);
		U.log(box);
		List<BoundingBox> boundingBoxList = BoundingBox.gridBoundingBox(box, 5);
		
	}*/
	/**
	 * Get / create a valid version of the geometry given. If the geometry is a polygon or multi polygon, self intersections /
	 * inconsistencies are fixed. Otherwise the geometry is returned.
	 * 
	 * @param geom
	 * @return a geometry 
	 */
	public static Geometry validate(Geometry geom){
	    if(geom instanceof Polygon){
	        if(geom.isValid()){
	            geom.normalize(); // validate does not pick up rings in the wrong order - this will fix that
	            return geom; // If the polygon is valid just return it
	        }
	        Polygonizer polygonizer = new Polygonizer();
	        addPolygon((Polygon)geom, polygonizer);
	        return toPolygonGeometry(polygonizer.getPolygons(), geom.getFactory());
	    }else if(geom instanceof MultiPolygon){
	        if(geom.isValid()){
	            geom.normalize(); // validate does not pick up rings in the wrong order - this will fix that
	            return geom; // If the multipolygon is valid just return it
	        }
	        Polygonizer polygonizer = new Polygonizer();
	        for(int n = geom.getNumGeometries(); n-- > 0;){
	            addPolygon((Polygon)geom.getGeometryN(n), polygonizer);
	        }
	        return toPolygonGeometry(polygonizer.getPolygons(), geom.getFactory());
	    }else{
	        return geom; // In my case, I only care about polygon / multipolygon geometries
	    }
	}

	/**
	 * Add all line strings from the polygon given to the polygonizer given
	 * 
	 * @param polygon polygon from which to extract line strings
	 * @param polygonizer polygonizer
	 */
	static void addPolygon(Polygon polygon, Polygonizer polygonizer){
	    addLineString(polygon.getExteriorRing(), polygonizer);
	    for(int n = polygon.getNumInteriorRing(); n-- > 0;){
	        addLineString(polygon.getInteriorRingN(n), polygonizer);
	    }
	}

	/**
	 * Add the linestring given to the polygonizer
	 * 
	 * @param linestring line string
	 * @param polygonizer polygonizer
	 */
	static void addLineString(LineString lineString, Polygonizer polygonizer){

	    if(lineString instanceof LinearRing){ // LinearRings are treated differently to line strings : we need a LineString NOT a LinearRing
	        lineString = lineString.getFactory().createLineString(lineString.getCoordinateSequence());
	    }

	    // unioning the linestring with the point makes any self intersections explicit.
	    Point point = lineString.getFactory().createPoint(lineString.getCoordinateN(0));
	    Geometry toAdd = lineString.union(point); 

	    //Add result to polygonizer
	    polygonizer.add(toAdd);
	}
	/**
	 * Get a geometry from a collection of polygons.
	 * 
	 * @param polygons collection
	 * @param factory factory to generate MultiPolygon if required
	 * @return null if there were no polygons, the polygon if there was only one, or a MultiPolygon containing all polygons otherwise
	 */
	static Geometry toPolygonGeometry(Collection<Polygon> polygons, GeometryFactory factory){
	    switch(polygons.size()){
	        case 0:
	            return null; // No valid polygons!
	        case 1:
	            return polygons.iterator().next(); // single polygon - no need to wrap
	        default:
	            //polygons may still overlap! Need to sym difference them
	            Iterator<Polygon> iter = polygons.iterator();
	            Geometry ret = iter.next();
	            while(iter.hasNext()){
	                ret = ret.symDifference(iter.next());
	            }
	            return ret;
	    }
	}
	private static final String CALIFORNIA_STATE_WKT_BOUNDARY_FILE = "/home/shatam-25/project_workspace/Other_Project/Parcel_Cache/Hazard_Zone/California_state_boundary.csv";
	
	private static final String CALIFORNIA_STATE_EACH_COUNTY_WKT_BOUNDARY_FILE = "/home/shatam-25/project_workspace/Other_Project/Parcel_Cache/Hazard_Zone/CA_Counties_TIGER2016.csv";
	
	private static final String USA_EACH_STATE_WKT_BOUNDARY_FILE = "/home/chinmay/Downloads/SAWPA-DUMMIE-DATA-SAMPLE/cb_2018_us_state_500k.csv";
	
	private static final String USA_EACH_COUNTY_WKT_BOUNDARY_FILE = "/home/shatam-25/project_workspace/Other_Project/Parcel_Cache/Hazard_Zone/cb_2018_us_county_500k.csv";
	

	private static Map<CA_County, String> countiesWKTMap = new HashMap<>();
	private static Map<USA_State, String> stateWKTMap = new HashMap<>();
	private static Map<USA_State, BoundingBox> boundingBoxUSAMap = new HashMap<>();
	private static Map<String, String> fipsCountyMap = new HashMap<>();
	private static Map<String, String> fipsWktMap = new HashMap<>();
	private static Map<String, List<String>> stateFipsMap = new HashMap<>();

	
	private static WKTReader wktReaderSRID = new WKTReader();

	/**
	 * This method is used to return the geometry object by reading string WKT.
	 * @param wkt
	 * @return
	 */
	public static Geometry toGeometry(String wkt){
	    if(wkt == null) {
	      return null;
	    }
	    Geometry geometry = null;
		try {
			geometry = wktReaderSRID.read(wkt);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	    return geometry;
	}
	
	public static Geometry toGeometry(USA_State state){
	    return toGeometry(getStateWkt(state));
	}
	
	public static Geometry toGeometry(CA_County county){
	    return toGeometry(getCountyWkt(county));
	}
	
	public static Geometry toGeometryFipsCode(String fipsCode){
	    return toGeometry(getCountyWkt(fipsCode));
	}
	
	
	/**
	 * This method is used to return the geometry object of California state.
	 * @return
	 */
	public static Geometry getCaliforniaStateGeometry(){
		return toGeometry(FileUtil.readCsvFileWithoutHeader(CALIFORNIA_STATE_WKT_BOUNDARY_FILE).get(0)[0]);
	}
	
	/**
	 * This method is used to initialized the each county boundaries of California state. 
	 */
	public static void load_CA_Counties_Boundaries(){
		U.log("Loading county boundaries of CA state  .....");
		List<String[]> readLines = FileUtil.readCsvFileWithoutHeader(CALIFORNIA_STATE_EACH_COUNTY_WKT_BOUNDARY_FILE);
		
		for(String lines[] : readLines){
//			lines[5] = lines[5].toLowerCase().trim().replace(" ", "_");
//			U.log(lines[5]);
//			System.out.print(lines[5] + ": ");
			
			for(CA_County county : CA_County.values()){
				if(county.getCounty().equals(lines[5])){
//					U.log("True");
					countiesWKTMap.put(county, lines[0]);
					break;
				}else continue;
//				U.log(county.getCounty());
			}
		}
		U.log("Done reading of county boundaries of CA state .....");
		U.log("County Map Size :"+countiesWKTMap.size());
	}
	
	/**
	 * This method is used to initialized the each state boundaries of United State of America (USA). 
	 */
	public static void load_USA_State_Boundaries(){
		U.log("Loading USA state boundaries  .....");
		List<String[]> readLines = FileUtil.readCsvFileWithoutHeader(USA_EACH_STATE_WKT_BOUNDARY_FILE);
		
		for(String lines[] : readLines){
			
			for(USA_State state : USA_State.values()){
				if(state.getState().equals(lines[5])){
					stateWKTMap.put(state, lines[0]); //load WKT to map
					boundingBoxUSAMap.put(state, getBoundingBox(lines[0])); //load BoundingBox to map
					break;
				}else continue;
			}
		}
		U.log("Done reading of state boundaries of USA .....");
		U.log("USA State Map Size :"+stateWKTMap.size());
	}
	
	/**
	 * This method is used to initialized the each county boundaries of United State of America (USA). 
	 */
	public static void load_USA_Counties_Boundaries(){
		List<String[]> readLines = FileUtil.readCsvFileWithoutHeader(USA_EACH_COUNTY_WKT_BOUNDARY_FILE);
		
		readLines.forEach(lines ->{
//			if(lines[5].length() == 4) lines[5] = "0"+lines[5]; //fips code			
			lines[5] = getCorrectFipsCode(lines[5]); //fips code

			
//			if(lines[1].length() == 1) lines[1] = "0" + lines[1]; //state code
			lines[1] = getCorrectStateCode(lines[1]); //state code
			
			if(!lines[5].isEmpty()){
				fipsCountyMap.put(lines[5], lines[6]); //fips code, county name
				fipsWktMap.put(lines[5], lines[0]); //fips code, wkt 
				
				List<String> fipsList = null;
				if(stateFipsMap.containsKey(lines[1])) //state code
					fipsList = stateFipsMap.get(lines[1]); //state code
				else
					fipsList = new ArrayList<>();
				
				fipsList.add(lines[5]); //fips code
				stateFipsMap.put(lines[1], fipsList); //state code, fips list
			}
			
		});
		
		U.log("Fips + County Map Size ::"+fipsCountyMap.size());
		U.log("Fips + WKT Map Size ::"+fipsWktMap.size());
		U.log("State Code + List of Fips Map Size ::"+stateFipsMap.size());
	}
	
	/**
	 * This method is used to return WKT of California state county. 
	 * @param county
	 * @return
	 */
	public static String getCountyWkt(CA_County county){
		if(countiesWKTMap.isEmpty()) throw new IllegalArgumentException("ShatamGeometry.load_CA_Counties_Boundaries() is not called.");
		return countiesWKTMap.get(county);
	}
	
	/**
	 * This method is used to return state WKT of USA. 
	 * @param state
	 * @return
	 */
	public static String getStateWkt(USA_State state){
		if(stateWKTMap.isEmpty()) throw new IllegalArgumentException("ShatamGeometry.load_USA_State_Boundaries() is not called.");
		return stateWKTMap.get(state);
	}
	
	/**
	 * This method is used to return county name of USA.
	 * @param fipsCode
	 * @return
	 */
	public static String getCountyWkt(String fipsCode){
		if(fipsWktMap.isEmpty()) throw new IllegalArgumentException("ShatamGeometry.load_USA_Counties_Boundaries() is not called.");
		
//		if(fipsCode.length() == 4) fipsCode = "0" + fipsCode;
		if(!fipsCode.isEmpty()) fipsCode = getCorrectFipsCode(fipsCode);

		
		if(fipsCode.equals("46113")) fipsCode = "46102"; //The FIPS code changes from 46113 to a 46102.
//		 It was changed to 46102 when the county was renamed from Shannon County to Oglala County in May 2015.
		
		return fipsWktMap.get(fipsCode);
	}
	
	public static Envelope getEnvelope(String wkt){
		return toGeometry(wkt).getEnvelopeInternal();
	}
	
	public static Envelope getEnvelope(Geometry geom){
		return geom.getEnvelopeInternal();
	}
	
	public static Envelope getEnvelope(CA_County county){
		return toGeometry(county).getEnvelopeInternal();
	}
	
	public static Envelope getEnvelope(USA_State state){
		return toGeometry(state).getEnvelopeInternal();
	}
	
	public static Envelope getEnvelopeFipsCode(String fipsCode){
		return toGeometryFipsCode(fipsCode).getEnvelopeInternal();
	}
	
	/**
	 * This method is used to return BoundingBox object of input county of California state.
	 * @param county
	 * @return
	 */
	public static BoundingBox getBoundingBox(CA_County county){
//		Geometry the_geom = toGeometry(getCountyWkt(county));
//		Envelope en = the_geom.getEnvelopeInternal();
		Envelope en = getEnvelope(county);
		return new BoundingBox(en.getMinX(), en.getMinY(), en.getMaxX(), en.getMaxY());
	}
	
	/**
	 * This method is used to return BoundingBox object of input state of USA.
	 * @param state
	 * @return
	 */
	public static BoundingBox getBoundingBox(USA_State state){
		if(boundingBoxUSAMap.isEmpty()) throw new IllegalArgumentException("ShatamGeometry.load_USA_State_Boundaries() is not called.");
		
		if(boundingBoxUSAMap.containsKey(state)) return boundingBoxUSAMap.get(state);
		
//		Geometry the_geom = toGeometry(getStateWkt(state));
//		Envelope en = the_geom.getEnvelopeInternal();
		Envelope en = getEnvelope(state);
		return new BoundingBox(en.getMinX(), en.getMinY(), en.getMaxX(), en.getMaxY());
	}
	

	
	/**
	 * This method is used to return BoundingBox object of input WKT.
	 * @param wkt
	 * @return
	 */
	public static BoundingBox getBoundingBox(String wkt){
//		Geometry the_geom = toGeometry(wkt);
//		Envelope en = the_geom.getEnvelopeInternal();		
		Envelope en = getEnvelope(wkt);
		return new BoundingBox(en.getMinX(), en.getMinY(), en.getMaxX(), en.getMaxY());
	}
	
	/**
	 * This method is used to return BoundingBox object of input Geometry.
	 * @param geom
	 * @return
	 */
	public static BoundingBox getBoundingBox(Geometry geom){
		Envelope en = geom.getEnvelopeInternal();
		return new BoundingBox(en.getMinX(), en.getMinY(), en.getMaxX(), en.getMaxY());
	}
	
	
	/**
	 * This method is used to return county name by giving FIPS code.
	 * @param fipsCode
	 * @return
	 */
	public static String getCountyName(String fipsCode){
		if(fipsCountyMap.isEmpty()) throw new IllegalArgumentException("ShatamGeometry.load_USA_Counties_Boundaries() is not called.");
		
//		if(!fipsCode.isEmpty() && fipsCode.length() == 4)
//			fipsCode = "0"+ fipsCode;
		if(!fipsCode.isEmpty()) fipsCode = getCorrectFipsCode(fipsCode);
		
		return fipsCountyMap.get(fipsCode);
	}

	/**
	 * This method is used to return county FIPS code (5-digit) list of a particular state fips code (2-digit)
	 * @param stateCode
	 * @return
	 */
	public static List<String> getFipsListByStateCode(String stateCode){
		if(stateFipsMap.isEmpty()) throw new IllegalArgumentException("ShatamGeometry.load_USA_Counties_Boundaries() is not called.");
//		if(stateCode.length() == 1) stateCode = "0"+ stateCode;
		stateCode = getCorrectStateCode(stateCode);
		
		return stateFipsMap.get(stateCode);
	}
	
	/**
	 * This method is used to return county FIPS code (5-digit) list of a particular state (State should be in abbreviation)
	 * @param state
	 * @return
	 */
	public static List<String> getFipsListByState(String state){
		return getFipsListByStateCode(USStates.getStateFipsCode(state));
	}
	
	public static List<String> getFipsListByState(USA_State state){
		return getFipsListByStateCode(USStates.getStateFipsCode(state.toString()));
	}
	
	
	public static boolean intersect(Envelope e1, Envelope e2) {
		if(e1.intersects(e2))return true;
		return false;
	}
	
	private static String getCorrectFipsCode(String fipsCode) {
		return (fipsCode.length() == 4) ? fipsCode = "0"+fipsCode : fipsCode ; //fips code
	}
	
	private static String getCorrectStateCode(String stateCode) {
		return (stateCode.length() == 1) ? stateCode = "0"+stateCode : stateCode ; //state code
	}
}
