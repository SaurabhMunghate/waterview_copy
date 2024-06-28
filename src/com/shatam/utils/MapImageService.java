package com.shatam.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.json.JSONArray;
import org.json.JSONObject;

import com.shatam.utils.FileUtil;
import com.shatam.utils.U;

public class MapImageService {
	private static final String IMAGE_FILE_PATH = "/home/glady/workspaces/Parcel_Cache/Web/Map_Images/fresno_ca.png";

	private static final int IMAGE_WIDTH_IN_PX = 10000;//max allowed in free version

	private static final int IMAGE_HEIGHT_IN_PX = 10000;//max allowed in free version

	private static final int MINIMUM_IMAGE_PADDING_IN_PX = 10;//gap from border

	private final static double QUARTERPI = Math.PI / 4.0;
	private final String fileName = "/home/glady/workspaces/Parcel_Cache/Web/fresno_ca.json";

	public static void main(String[] args) throws Exception { 
		new MapImageService().execute();
	}// API-MAP?14/4209/620

	public void execute() throws Exception {
		U.log("Initialize the process ......");
		String inputdata = FileUtil.readAllText(fileName);
		U.log("Reading the file done.");
		U.log("Starting to create image.......");
		JSONArray jsonArr = new JSONArray(inputdata);
		// for (int i = 0; i < jsonArr.length(); i++) {
		// JSONObject jsonObj = jsonArr.getJSONObject(i);
		// }
		// int x=4209;
		// int y=620;
		// double[][] dataArr=new double[][] {
		// {40.48358265392247, -87.93408474245096},
		// {40.22614059905587, -87.92996486920255},
		// {40.22299499697277, -87.94095119786496},
		// {39.88030315008772, -87.93545803353376},
		// {39.8781954301804, -87.61410792015829},
		// {39.868658801683694, -87.61858589903296},
		// {39.86660181273516, -87.56192285901186},
		// {39.88030315008772, -87.57702906092267},
		// {39.88451839555291, -87.53445703735584},
		// {40.490938236784, -87.52578647128118}
		// };
		// configuring the buffered image and graphics to draw the map
		BufferedImage bufferedImage = new BufferedImage(IMAGE_WIDTH_IN_PX, IMAGE_HEIGHT_IN_PX,
				BufferedImage.TYPE_INT_ARGB);

		Graphics2D g = bufferedImage.createGraphics();
		Map<RenderingHints.Key, Object> map = new HashMap<RenderingHints.Key, Object>();
		map.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		map.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		map.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		RenderingHints renderHints = new RenderingHints(map);
		g.setRenderingHints(renderHints);

		// min and max coordinates, used in the computation below
		Point2D.Double minXY = new Point2D.Double(-1, -1);
		Point2D.Double maxXY = new Point2D.Double(-1, -1);

		Collection<Collection<Point2D.Double>> countyData = new ArrayList<Collection<Point2D.Double>>();


		for (int i = 0; i < jsonArr.length(); i++) {
			Collection<Point2D.Double> lonLat = new ArrayList<Point2D.Double>();
			JSONObject jsonObj = jsonArr.getJSONObject(i);
			//U.log(jsonObj.toString());
			// for (CountyBoundary countyBoundary : county.getCountyBoundaries()) {
			JSONArray polyArr = jsonObj.getJSONArray("Location");
			for (int j = 0; j < polyArr.getJSONArray(0).length(); j++) {

				// convert to radian
				double longitude = polyArr.getJSONArray(0).getJSONObject(j).getDouble("Lng") * Math.PI / 180;
				double latitude = polyArr.getJSONArray(0).getJSONObject(j).getDouble("Lat") * Math.PI / 180;

				Point2D.Double xy = new Point2D.Double();
				xy.x = longitude;
				xy.y = Math.log(Math.tan(QUARTERPI + 0.5 * latitude));

				minXY.x = (minXY.x == -1) ? xy.x : Math.min(minXY.x, xy.x);
				minXY.y = (minXY.y == -1) ? xy.y : Math.min(minXY.y, xy.y);
				
				lonLat.add(xy);
				// }
			}
			countyData.add(lonLat);
		}
		
		// }
		// readjust coordinate to ensure there are no negative values
		for (Collection<Point2D.Double> points : countyData) {
			for (Point2D.Double point : points) {
				point.x = point.x - minXY.x;
				point.y = point.y - minXY.y;
				// now, we need to keep track the max X and Y values
				maxXY.x = (maxXY.x == -1) ? point.x : Math.max(maxXY.x, point.x);
				maxXY.y = (maxXY.y == -1) ? point.y : Math.max(maxXY.y, point.y);
			}
		}

		int paddingBothSides = MINIMUM_IMAGE_PADDING_IN_PX * 2;
		int mapWidth = IMAGE_WIDTH_IN_PX - paddingBothSides;
		int mapHeight = IMAGE_HEIGHT_IN_PX - paddingBothSides;

		double mapWidthRatio = mapWidth / maxXY.x;
		double mapHeightRatio = mapHeight / maxXY.y;

		double globalRatio = Math.min(mapWidthRatio, mapHeightRatio);

		double heightPadding = (IMAGE_HEIGHT_IN_PX - (globalRatio * maxXY.y)) / 2;
		double widthPadding = (IMAGE_WIDTH_IN_PX - (globalRatio * maxXY.x)) / 2;

		for (Collection<Point2D.Double> points : countyData) {
			Polygon polygon = new Polygon();

			for (Point2D.Double point : points) {
				int adjustedX = (int) (widthPadding + (point.getX() * globalRatio));

				int adjustedY = (int) (IMAGE_HEIGHT_IN_PX - heightPadding - (point.getY() * globalRatio));
				//U.log(adjustedX + " " + adjustedY);
				// if(adjustedX<x&&adjustedY<y)
//				for (x=0; x<= width / tileWidth; x++)
//			    {
//			            for (y=0; y<= height / tileHeight ; y++)
//			            {
//			                    g.drawImage(tile, x * tileWidth, y * tileHeight, this);
//			            }
//			    }
				polygon.addPoint(adjustedX, adjustedY);

			}
			g.setColor(Color.decode("#B3B3B3"));
			//g.setColor(Color.red);
			g.drawPolygon(polygon);
		}

		// create the image file
		ImageIO.write(bufferedImage, "PNG", new File(IMAGE_FILE_PATH));
		U.log("File is written at "+IMAGE_FILE_PATH);
	}
}
