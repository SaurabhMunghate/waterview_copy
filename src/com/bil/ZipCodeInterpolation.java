package com.bil;
public class ZipCodeInterpolation {
    public static double interpolateValue(double[][] zipData) {
        double sumValues = 0;
        double sumWeights = 0;

        for (double[] zip : zipData) {
            double value = zip[0];   // Known value
            double weight = 1.0 / zip[1];  // Weight (1 / distance)

            sumValues += value * weight;
            sumWeights += weight;
        }

        return sumWeights > 0 ? sumValues / sumWeights : 0;  // Avoid division by zero
    }

    public static void main(String[] args) {
        // Example ZIP data: {value, distance}
        double[][] zipData = {
            {120, 2.5},  // ZIP 1: Value = 120, Distance = 2.5
            {150, 1.8},  // ZIP 2: Value = 150, Distance = 1.8
            {130, 3.0}   // ZIP 3: Value = 130, Distance = 3.0
        };

        double centerValue = interpolateValue(zipData);
        System.out.println("Estimated Center ZIP Value: " + centerValue);
    }
}
