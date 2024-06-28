/**
 * @author Sawan Meshram
 * @date 22 March 2021
 */
package com.shatam.waterview.geotools;

import java.util.Arrays;

import com.shatam.utils.U;

public class MercatorTransform {
    public final static double NORTH_POLE = 90.0;
    public final static double SOUTH_POLE = -NORTH_POLE;
    public final static double DATELINE = 180.0;
    public final static double LON_RANGE = 360.0;

    final public static transient double wgs84_earthEquatorialRadiusMeters_D = 6378137.0;
    private static double latfac = wgs84_earthEquatorialRadiusMeters_D;
    private static double lonfac = wgs84_earthEquatorialRadiusMeters_D;

    final public static transient double HALF_PI_D = Math.PI / 2.0d;

    public static void main(String[] args) {
    	U.log(Arrays.toString(inverse(-10854655.49193323f,5674707.0621537212f)));
    	
    	U.log(Arrays.toString(forward(5674707.0621537212, 10854655.49193323)));
    	
    	U.log(Arrays.toString(inverse(215.588375480962f, 398.7562512619188f)));
    	
    	U.log(Arrays.toString(forward(215.588375480962f, 398.7562512619188f)));
    	
    	U.log(Arrays.toString(forward(1185658.249893412f,1266833.119907409f)));
    	
    	
    	U.log(Arrays.toString(forward(21.1002452, -79.1253801)));
    	U.log(Arrays.toString(inverse(-8808197.021556221f, 2403835.778797005f)));
    	
    	U.log(Arrays.toString(inverse(-8808197.0215562209f, 2403835.7787970058f)));
    	
    	U.log(Arrays.toString(inverse(-12363154.13212189, 3712537.4059351026)));
    	U.log(Arrays.toString(inverse(-12363125.468236282, 3712566.06982071)));
    	
    	
    }
    /**
     * Returns google projection coordinates from wgs84 lat,long coordinates
     */
    public static double[] forward(double lat, double lon) {

        lat = normalizeLatitude(lat);
        lon = wrapLongitude(lon);

        double latrad = Math.toRadians(lat);
        double lonrad = Math.toRadians(lon);

        double lat_m = latfac * Math.log(Math.tan(((latrad + HALF_PI_D) / 2d)));
        double lon_m = lonfac * lonrad;

        double[] x = { lon_m, lat_m };
        return x;
    }

    /**
     * Returns wgs84 lat,long coordinates from google projection coordinates
     */
    public static double[] inverse(double lon_m, double lat_m) {
        double latrad = (2d * Math.atan(Math.exp(lat_m / latfac))) - HALF_PI_D;
        double lonrad = lon_m / lonfac;

        double lat = Math.toDegrees(latrad);
        double lon = Math.toDegrees(lonrad);

        lat = normalizeLatitude(lat);
        lon = wrapLongitude(lon);
        double[] x = { lat, lon };

        return x;
    }

    private static double wrapLongitude(double lon) {
        if ((lon < -DATELINE) || (lon > DATELINE)) {
            lon += DATELINE;
            lon = lon % LON_RANGE;
            lon = (lon < 0) ? DATELINE + lon : -DATELINE + lon;
        }
        return lon;
    }

    private static double normalizeLatitude(double lat) {
        if (lat > NORTH_POLE) {
            lat = NORTH_POLE;
        }
        if (lat < SOUTH_POLE) {
            lat = SOUTH_POLE;
        }
        return lat;
    }

}