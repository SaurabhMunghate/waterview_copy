package com.saurabh;
public class Interpolation3ToCenter {
    public static void main(String[] args) {
    	
    	/*
    	double x0 = 33.964942; // x-coordinate of point 0 //78
        double y0 = -117.33698; // y-coordinate of point 0
        double value0 = 0.0; // value at point 0

        double x1 = 34.146372; // x-coordinate of point 1 //159
        double y1 = -117.9858; // y-coordinate of point 1
        double value1 = 0.195; // value at point 1

        double x2 = 33.964942; // x-coordinate of point 2 //44
        double y2 = -117.33698; // y-coordinate of point 2
        double value2 = 0.04; // value at point 2

        double x = 34.03; // x-coordinate of the point to interpolate
        double y = -117.72; // y-coordinate of the point to interpolate
    	 */
    	
        double x0 = 1.0; // x-coordinate of point 0
        double y0 = 2.0; // y-coordinate of point 0
        double value0 = 10.0; // value at point 0

        double x1 = 3.0; // x-coordinate of point 1
        double y1 = 4.0; // y-coordinate of point 1
        double value1 = 20.0; // value at point 1

        double x2 = 5.0; // x-coordinate of point 2
        double y2 = 6.0; // y-coordinate of point 2
        double value2 = 30.0; // value at point 2

        double x = 4.0; // x-coordinate of the point to interpolate
        double y = 5.0; // y-coordinate of the point to interpolate

        double interpolatedValue = linearInterpolation(x0, y0, value0, x1, y1, value1, x2, y2, value2, x, y);

        System.out.println("Interpolated value: " + interpolatedValue);
    }

    public static double linearInterpolation(double x0, double y0, double value0, double x1, double y1, double value1, double x2, double y2, double value2, double x, double y) {
        // Calculate the weights based on the distances from the surrounding points
        double weight0 = 1.0 / Math.sqrt(Math.pow(x - x0, 2) + Math.pow(y - y0, 2));
        double weight1 = 1.0 / Math.sqrt(Math.pow(x - x1, 2) + Math.pow(y - y1, 2));
        double weight2 = 1.0 / Math.sqrt(Math.pow(x - x2, 2) + Math.pow(y - y2, 2));

        // Normalize the weights
        double totalWeight = weight0 + weight1 + weight2;
        weight0 /= totalWeight;
        weight1 /= totalWeight;
        weight2 /= totalWeight;

        // Perform linear interpolation
        double interpolatedValue = weight0 * value0 + weight1 * value1 + weight2 * value2;

        return interpolatedValue;
    }
}
