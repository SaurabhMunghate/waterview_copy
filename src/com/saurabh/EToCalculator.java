package com.saurabh;
import java.math.BigDecimal;

public class EToCalculator {
    // Constants for CIMIS equation
    private static final double Kc = 0.85; // Crop coefficient
    private static final double EToReference = 0.23; // Reference evapotranspiration constant

    public static void main(String[] args) {
        // Data from CIMIS stations
        double airTmpAvg1 = 69.3; // F
        double relHumAvg1 = 69; // %
        double solRadAvg1 = 617; // Ly/day
        double windSpdAvg1 = 2.4; // MPH

        double airTmpAvg2 = 70.1; // F
        double relHumAvg2 = 61; // %
        double solRadAvg2 = 530; // Ly/day
        double windSpdAvg2 = 3.2; // MPH

        // Calculate ETo using CIMIS equation for each station
        double eto1 = calculateETo(airTmpAvg1, relHumAvg1, solRadAvg1, windSpdAvg1);
        double eto2 = calculateETo(airTmpAvg2, relHumAvg2, solRadAvg2, windSpdAvg2);

        // Print the ETo values for each station
        System.out.println("ETo for Station 1: " + eto1 + " inches");
        System.out.println("ETo for Station 2: " + eto2 + " inches");
    }

    private static double calculateETo(double airTmpAvg, double relHumAvg, double solRadAvg, double windSpdAvg) {
        // Convert temperature from Fahrenheit to Celsius
        double airTmpC = (airTmpAvg - 32) / 1.8;

        // Calculate the saturation vapor pressure
        double saturationVaporPressure = 0.6108 * Math.exp((17.27 * airTmpC) / (airTmpC + 237.3));

        // Calculate the actual vapor pressure
        double vaporPressure = (relHumAvg / 100) * saturationVaporPressure;

        // Calculate the slope of the saturation vapor pressure curve
        double slope = (4098 * saturationVaporPressure) / Math.pow((airTmpC + 237.3), 2);

        // Calculate the net solar radiation
        double netSolarRadiation = (1 - 0.23) * solRadAvg;

        // Calculate the wind function
        double windFunction = (slope * (0.34 * windSpdAvg + 0.14));

        // Calculate the ETo
        double eto = (0.408 * netSolarRadiation - windFunction) * Kc + EToReference;

        // Round the ETo value to two decimal places
        BigDecimal roundedETo = new BigDecimal(eto).setScale(2, BigDecimal.ROUND_HALF_UP);

        return roundedETo.doubleValue();
    }
}
