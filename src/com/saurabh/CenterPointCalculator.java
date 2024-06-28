package com.saurabh;
import java.awt.geom.Point2D;

public class CenterPointCalculator {
    public static void main(String[] args) {
        // Create two points
        Point2D.Double point1 = new Point2D.Double(-117.72949218750001, 34.05265942137598);
        Point2D.Double point2 = new Point2D.Double(-117.70751953125, 34.05265942137598);

        // Calculate the center point
        Point2D.Double centerPoint = calculateCenterPoint(point1, point2);

        // Print the coordinates of the center point
        System.out.println("Center Point: " + centerPoint.x + ", " + centerPoint.y);
    }

    private static Point2D.Double calculateCenterPoint(Point2D.Double point1, Point2D.Double point2) {
        double centerX = (point1.x + point2.x) / 2;
        double centerY = (point1.y + point2.y) / 2;
        return new Point2D.Double(centerX, centerY);
    }
}
