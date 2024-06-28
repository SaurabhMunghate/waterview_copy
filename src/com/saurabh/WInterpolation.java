package com.saurabh;
import java.util.ArrayList;
import java.util.List;

public class WInterpolation {
    // Represents a weather station with latitude, longitude, and weather data
    private static class WeatherStation {
        private double latitude;
        private double longitude;
        private double temperature;

        public WeatherStation(double latitude, double longitude, double temperature) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.temperature = temperature;
        }
    }

    // Interpolates the temperature at the desired location
    private static double interpolateTemperature(double desiredLatitude, double desiredLongitude,
                                                 List<WeatherStation> stations) {
        double sumTemperatureWeight = 0.0;
        double sumWeight = 0.0;

        for (WeatherStation station : stations) {
            double distance = calculateDistance(desiredLatitude, desiredLongitude, station.latitude, station.longitude);

            // Check if the station is close enough to consider for interpolation
//            if (distance < 1.0) {
                double weight = 1.0 / distance;  // Calculate the weight based on the inverse distance
                System.out.println("weight ;;"+weight);
                sumTemperatureWeight += station.temperature * weight;
                sumWeight += weight;
//            }
        System.out.println(distance);
        }

        if (sumWeight > 0.0) {
            return sumTemperatureWeight / sumWeight;  // Calculate the weighted average temperature
        } else {
            return Double.NaN;  // Unable to interpolate temperature
        }
    }

    // Calculates the distance between two points using the Haversine formula
    private static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double earthRadius = 6371.0;  // Radius of the Earth in kilometers

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return earthRadius * c;
    }

    public static void main(String[] args) {
        // Create a list of weather stations with their latitude, longitude, and temperature
        List<WeatherStation> stations = new ArrayList<>();
        stations.add(new WeatherStation(34.056589, -117.81307, 0.18500000000000003));
        stations.add(new WeatherStation(34.146372, -117.9858, 0.16516129032258067));
        stations.add(new WeatherStation(33.964942, -117.33698, 0.20758064516129032));
//      stations.put("Station 78", new Station(34.056589, -117.81307, 10.423076286727037, 0.18500000000000003));
//      stations.put("Station 159", new Station(34.146372, -117.9858, 28.012213545884787, 0.16516129032258067));
//      stations.put("Station 44", new Station(33.964942, -117.33698, 35.03799292172124, 0.20758064516129032));
//      stations.put("Station 75", new Station(33.68845, -117.721187, 41.360721031932094, 0.17725806451612913));

        // Interpolate the temperature at the desired location
        double desiredLatitude = 34.06;
        double desiredLongitude = -117.70;
        double interpolatedTemperature = interpolateTemperature(desiredLatitude, desiredLongitude, stations);

        System.out.println("Interpolated Temperature: " + interpolatedTemperature);
    }
}
