package com.saurabh;
import java.util.HashMap;
import java.util.Map;

public class InterpolationExample {

    public static void main(String[] args) {
        // Coordinates of CIMIS stations and their respective weather data
        Map<String, Station> stations = new HashMap<>();
//        stations.put("Station 78", new Station(34.0, -117.0, 25.0, 2.5));
//        stations.put("Station 159", new Station(35.0, -118.0, 26.0, 3.0));
//        stations.put("Station 44", new Station(33.0, -116.0, 24.0, 2.0));
        
        stations.put("Station 78", new Station(34.056589, -117.81307, 10.423076286727037, 0.18500000000000003));
        stations.put("Station 159", new Station(34.146372, -117.9858, 28.012213545884787, 0.16516129032258067));
        stations.put("Station 44", new Station(33.964942, -117.33698, 35.03799292172124, 0.20758064516129032));
        stations.put("Station 75", new Station(33.68845, -117.721187, 41.360721031932094, 0.17725806451612913));
        
//        stations.put("Station 78", new Station(-117.70, 34.06, 00, 0.18500000000000003));
//        stations.put("Station 159", new Station(-117.70, 37.06, 00, 0.0));
//        stations.put("Station 44", new Station(00, 00, 00, 0.20758064516129032));
//        stations.put("Station 75", new Station(0, 00, 00, 0.17725806451612913));

        // Desired location coordinates
//        double desiredLatitude = 34.06;
//        double desiredLongitude = -117.70;
        double desiredLatitude = 34.06;
        double desiredLongitude = -117.70;

        // Perform inverse distance weighting interpolation
        double interpolatedTemperature = interpolate(stations, desiredLatitude, desiredLongitude, DataType.TEMPERATURE);
        double interpolatedWindSpeed = interpolate(stations, desiredLatitude, desiredLongitude, DataType.WIND_SPEED);

        // Print the interpolated values
        System.out.println("Interpolated temperature: " + interpolatedTemperature + " °C");
        System.out.println("Interpolated wind speed: " + interpolatedWindSpeed + " m/s");
    }

    public static double interpolate(Map<String, Station> stations, double desiredLatitude, double desiredLongitude, DataType dataType) {
        double sumWeightedValues = 0.0;
        double sumWeights = 0.0;

        for (Map.Entry<String, Station> entry : stations.entrySet()) {
            Station station = entry.getValue();
            double distance = calculateDistance(desiredLatitude, desiredLongitude, station.getLatitude(), station.getLongitude());

            // Calculate weight using inverse distance weighting
            double weight = 1.0 / Math.pow(distance, 2);

            // Get the data value based on the desired data type
            double value = 0.0;
            if (dataType == DataType.TEMPERATURE) {
                value = station.getTemperature();
            } else if (dataType == DataType.WIND_SPEED) {
                value = station.getWindSpeed();
            }

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
        private double temperature;
        private double windSpeed;

        public Station(double latitude, double longitude, double temperature, double windSpeed) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.temperature = temperature;
            this.windSpeed = windSpeed;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public double getTemperature() {
            return temperature;
        }

        public double getWindSpeed() {
            return windSpeed;
        }
    }

    public enum DataType {
        TEMPERATURE,
        WIND_SPEED
    }
}
