package com.saurabh;
public class AccuracyCalculator {
    
    public static double calculateAccuracy(double correctValue, double estimatedValue) {
        double difference = Math.abs(correctValue - estimatedValue);
        double accuracy = (difference / correctValue) * 100.0;
        return 100.0 - accuracy;
    }
    
    public static void main(String[] args) {
        double correctValue = 10.0;
        double estimatedValue = 9.5;
        
        double accuracy = calculateAccuracy(correctValue, estimatedValue);
        System.out.println("Accuracy: " + accuracy + "%");
    }
}
