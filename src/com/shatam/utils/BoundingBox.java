/**
 * @author Sawan Meshram
 * @date 9 Sept 2021
 */
package com.shatam.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class BoundingBox {
	public static void main(String[] args) {
		boolean check;
		check = IsWithinBoundaryBox(-124.124999999865, 41.2499999991692, -124.000000000306, 41.3749999988581, 41.319676, -124.060080);
		U.log(check);
		
		check = IsWithinBoundaryBox(-124.124999999865, 41.2499999991692, -124.000000000306, 41.3749999988581, 41.359931, -124.044824);
		U.log(check);
		
		double[][] coordinates = {
			{41.325004, -124.024739},
			{41.319676, -124.060080},
			{41.374617, -123.995557},
			{41.369593, -124.006200},
			{41.249417, -124.124732},
			{41.251418, -124.121041},
			{41.253547, -124.095635}
		};
		
		for(double[] points : coordinates){
			U.log(Arrays.toString(points));
			check = IsWithinBoundaryBox(-124.124999999865, 41.2499999991692, -124.000000000306, 41.3749999988581, points[0], points[1]);
			U.log(check);
		}

	}
	
	@SuppressWarnings("unused")
	private BoundingBox() {	
	}

	private double xmin = 0, ymin = 0, xmax = 0, ymax = 0;
	public BoundingBox(double xmin, double ymin, double xmax, double ymax){
		this.xmin = xmin;
		this.ymin = ymin;
		this.xmax = xmax;
		this.ymax = ymax;
	}

	public static String createPolygon(BoundingBox b){
		return new StringBuilder().append("POLYGON ((")
				.append(b.xmin).append(" ").append(b.ymin).append(", ")
				.append(b.xmax).append(" ").append(b.ymin).append(", ")
				.append(b.xmax).append(" ").append(b.ymax).append(", ")
				.append(b.xmin).append(" ").append(b.ymax).append("))").toString();
	}
	
	
	public String createPolygon(){
		return createPolygon(this);
	}

	
	public static String createMultiPolygon(List<BoundingBox> listBoundingBox){
		
		StringBuilder sb = new StringBuilder().append("MULTIPOLYGON (");
		
		for(BoundingBox b : listBoundingBox){
			sb.append("((")
			.append(b.xmin).append(" ").append(b.ymin).append(", ")
			.append(b.xmax).append(" ").append(b.ymin).append(", ")
			.append(b.xmax).append(" ").append(b.ymax).append(", ")
			.append(b.xmin).append(" ").append(b.ymax)
			.append("))")
			.append(", ");
		}
		return sb.replace(sb.lastIndexOf(", "), sb.lastIndexOf(", ")+2, ")").toString();
	}
	
	public String createMultiPolygon(){
		List<BoundingBox> list = new ArrayList<>();
		list.add(this);
		return createMultiPolygon(list);
	}
	
	public static List<BoundingBox> gridBoundingBox(BoundingBox b, int grid){
		return gridBoundingBox(b, grid, grid);
	}
	
	
	public static List<BoundingBox> gridBoundingBox(BoundingBox b, int gridX, int gridY){
		U.log("BoundingBox Grid for 'X' = '"+ gridX + "' & 'Y' = '"+gridY +"'");
		double grid_x = ((b.getXmax() - b.getXmin()) / gridX);
		double grid_y = ((b.getYmax() - b.getYmin()) / gridY);
		
		List<BoundingBox> list = new ArrayList<>();

		for(int i = 0; i < gridX; i++){
			for(int j = 0; j < gridY; j++){
				
				double _xmin = b.getXmin() + (i * grid_x);
				double _ymin = b.getYmin() + (j * grid_y);
				
				double _xmax = _xmin + grid_x;
				double _ymax = _ymin + grid_y;
				
				list.add(new BoundingBox(_xmin, _ymin, _xmax, _ymax));
			}
		}
		U.log("Total grid form = "+list.size());
		return list;
	}

	
	public double getXmin() {
		return xmin;
	}

	public double getYmin() {
		return ymin;
	}

	public double getXmax() {
		return xmax;
	}

	public double getYmax() {
		return ymax;
	}

	public static boolean IsWithinBoundaryBox(double xmin, double ymin, double xmax, double ymax, double latitude, double longitude){
		
		if (longitude > xmin && longitude < xmax &&
				latitude > ymin && latitude < ymax)
			return true;
		
		return false;
	}

	@Override
	public String toString() {
		return "BoundingBox [xmin=" + xmin + ", ymin=" + ymin + ", xmax=" + xmax + ", ymax=" + ymax + "]";
	}
	
/*	@Override
	public String toString() {
		return new StringBuilder().append("[[").append(xmin).append(" ").append(ymin)
				.append("],[").append(xmax).append(" ").append(ymax).append("]]").toString();
	}*/
	
}
