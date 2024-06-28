package com.saurabh;
import java.util.HashMap;
import java.util.Map;

public class InterpolationExampleForLatLongs {

    public static void main(String[] args) {
        // Coordinates and average values for CIMIS stations
        Map<String, Station> stations = new HashMap<>();
        stations.put("Station 78", new Station(34.056589, -117.81307, 0.18500000000000003, 0.185));
        stations.put("Station 159", new Station(34.146372, -117.9858, 0.1641666666666667, 0.195));
        stations.put("Station 44", new Station(33.964942, -117.33698, 0.04, 0.21032786885245902));

        // Desired location coordinates
        double desiredLatitude = 34.03;
        double desiredLongitude = -117.72;

        // Perform inverse distance weighting interpolation for ETo and precipitation
        double interpolatedETo = interpolate(stations, desiredLatitude, desiredLongitude, DataType.ETO);
        double interpolatedPrecipitation = interpolate(stations, desiredLatitude, desiredLongitude, DataType.PRECIPITATION);

        // Print the interpolated values
        System.out.println("Interpolated ETo: " + interpolatedETo + " mm/day");
        System.out.println("Interpolated Precipitation: " + interpolatedPrecipitation + " mm/day");
    }

    public static double interpolate(Map<String, Station> stations, double desiredLatitude, double desiredLongitude, DataType dataType) {
        double sumWeightedValues = 0.0;
        double sumWeights = 0.0;

        for (Map.Entry<String, Station> entry : stations.entrySet()) {
            Station station = entry.getValue();
            double distance = calculateDistance(desiredLatitude, desiredLongitude, station.getLatitude(), station.getLongitude());
//            System.out.println("distance ::"+distance);

            // Calculate weight using inverse distance weighting
            double weight = 1.0 / Math.pow(distance, 2);
//            System.out.println("weight ::"+weight);

            // Get the data value based on the desired data type
            double value = 0.0;
            if (dataType == DataType.ETO) {
                value = station.getETo();
            } else if (dataType == DataType.PRECIPITATION) {
                value = station.getPrecipitation();
            }

//            System.out.println("value - "+value);
            // Accumulate the weighted values and weights
            sumWeightedValues += weight * value;
            sumWeights += weight;
        }

        // Perform the interpolation
        return sumWeightedValues / sumWeights;
    }

    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Calculate the distance between two points using the Haversine formula
        double R = 6371; // Earth's radius in kilometers
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    public static class Station {
        private double latitude;
        private double longitude;
        private double ETo;
        private double precipitation;

        public Station(double latitude, double longitude, double ETo, double precipitation) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.ETo = ETo;
            this.precipitation = precipitation;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public double getETo() {
            return ETo;
        }

        public double getPrecipitation() {
            return precipitation;
        }
    }

    public enum DataType {
        ETO,
        PRECIPITATION
    }
}
