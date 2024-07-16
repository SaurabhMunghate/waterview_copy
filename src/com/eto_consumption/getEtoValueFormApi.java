package com.eto_consumption;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.regexp.recompile;
import org.json.JSONObject;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.opencsv.CSVWriter;
//import com.saurabh.DatePairExample.DatePair;
import com.shatam.utils.FileUtil;
import com.shatam.utils.U;
import com.vividsolutions.jts.geom.Geometry;

public class getEtoValueFormApi {
//	static String listofTiles = "/home/shatam-100/Down/WaterView_Data/16_Tiles_MonteVista.csv";
//	static String listofTiles = "/home/shatam-100/Down/WaterView_Data/Tile_San_Jose_Cover_145_Tiles.csv";
//	static String listofTiles = "/home/shatam-100/Down/WaterView_Data/409_Tiles_Western.csv";
//    static String listofTiles = "/home/shatam-100/Down/WaterView_Data/41_Tiles_jurupa.csv";
//	static String listofTiles = "/home/shatam-100/Down/WaterView_Data/All_Tiles_walnut_valley.csv";
//	static String listofTiles= "/home/shatam-100/Down/WaterView_Data/Tiles/146_redwood_city_Tiles.csv";// pending
//	static String listofTiles = "/home/shatam-100/Down/WaterView_Data/Tiles/18_nipomo_Tiles.csv";
//	static String listofTiles = "/home/shatam-100/Down/WaterView_Data/Tiles/78_Tiles_vacaville_District.csv";
//	static String listofTiles = "/home/shatam-100/Down/WaterView_Data/Tiles/33_Carlsbad_Water_District_Tiles.csv"; //running
//	static String listofTiles = "/home/shatam-100/Down/WaterView_Data/Tiles/42_roseville_Water_District_Tiles.csv";//check Console File
//    static String listofTiles = "/home/shatam-100/Down/WaterView_Data/Tiles/115_las_virgenes_Tiles.csv";pending
    
    
//	static String startYear = "2023-01-01";//Tile_San_Jose_Cover_145_Tiles
//	static String endYear = "2024-01-01";//Tile_San_Jose_Cover_145_Tiles

//    static String startYear = "2021-01-01";//409_Tiles_Western
//	static String endYear = "2023-09-17";//409_Tiles_Western 2023-09-17

//    static String startYear = "2023-09-17";//409_Tiles_Western
//	static String endYear = "2023-12-31";//409_Tiles_Western
	
//static String startYear = "2021-01-01";//41_Tiles_jurupa
//static String endYear = "2023-12-31";//41_Tiles_jurupa

	
//    static String startYear = "2019-01-01";//All_Tiles_walnut_valley
//	static String endYear = "2023-12-31";//All_Tiles_walnut_valley


//    static String startYear = "2024-01-01";//All_Tiles_walnut_valley
//	static String endYear = "2024-01-31";//All_Tiles_walnut_valley

	
//  static String startYear = "2019-01-01";//redwood_city_Tiles_146
//	static String endYear = "2023-12-31";//redwood_city_Tiles_146
	
	
//	  static String startYear = "2019-01-01";//18_nipomo_Tiles
//		static String endYear = "2024-01-31";//18_nipomo_Tiles

//		  static String startYear = "2019-01-01";//78_Tiles_vacaville_District
//			static String endYear = "2024-01-31";//78_Tiles_vacaville_District

//	  static String startYear = "2019-01-01";//33_Carlsbad_Water_District_Tiles
//		static String endYear = "2024-01-31";//33_Carlsbad_Water_District_Tiles

//		  static String startYear = "2019-01-01";//42_roseville_Water_District_Tiles
//			static String endYear = "2024-01-31";//42_roseville_Water_District_Tiles
    
//	  static String startYear = "2019-01-01";//42_roseville_Water_District_Tiles
//		static String endYear = "2024-01-31";//42_roseville_Water_District_Tiles

		


//	String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Eto_Precip_San_Jose_"+dateString+"To"+dateStringEnd+".csv";

	
	//+++++++++++++++++++++++++++++++++\

//	static String listofTiles = "/home/shatam-100/Down/WaterView_Data/16_Tiles_MonteVista.csv";
//	static String listofTiles = "/home/shatam-100/Down/WaterView_Data/Tile_San_Jose_Cover_145_Tiles.csv";
//	static String listofTiles = "/home/shatam-100/Down/WaterView_Data/409_Tiles_Western.csv";
//    static String listofTiles = "/home/shatam-100/Down/WaterView_Data/41_Tiles_jurupa.csv";
//	static String listofTiles = "/home/shatam-100/Down/WaterView_Data/All_Tiles_walnut_valley.csv";
//	static String listofTiles= "/home/shatam-100/Down/WaterView_Data/Tiles/146_redwood_city_Tiles.csv";
//	static String listofTiles = "/home/shatam-100/Down/WaterView_Data/Tiles/18_nipomo_Tiles.csv";
//	static String listofTiles = "/home/shatam-100/Down/WaterView_Data/Tiles/78_Tiles_vacaville_District.csv";
//	static String listofTiles = "/home/shatam-100/Down/WaterView_Data/Tiles/33_Carlsbad_Water_District_Tiles.csv"; //running
//	static String listofTiles = "/home/shatam-100/Down/WaterView_Data/Tiles/42_roseville_Water_District_Tiles.csv";//check Console File
//    static String listofTiles = "/home/shatam-100/Down/WaterView_Data/Tiles/115_las_virgenes_Tiles.csv";pending
    
    
//	static String startYear = "2023-01-01";//Tile_San_Jose_Cover_145_Tiles
//	static String endYear = "2024-01-01";//Tile_San_Jose_Cover_145_Tiles

//    static String startYear = "2021-01-01";//409_Tiles_Western
//	static String endYear = "2023-09-17";//409_Tiles_Western 2023-09-17

//    static String startYear = "2023-09-17";//409_Tiles_Western
//	static String endYear = "2023-12-31";//409_Tiles_Western
	
//static String startYear = "2021-01-01";//41_Tiles_jurupa
//static String endYear = "2023-12-31";//41_Tiles_jurupa

	
//    static String startYear = "2019-01-01";//All_Tiles_walnut_valley
//	static String endYear = "2023-12-31";//All_Tiles_walnut_valley


//    static String startYear = "2024-01-01";//All_Tiles_walnut_valley
//	static String endYear = "2024-01-31";//All_Tiles_walnut_valley

	
//  static String startYear = "2019-01-01";//redwood_city_Tiles_146
//	static String endYear = "2023-12-31";//redwood_city_Tiles_146
	
	
//	  static String startYear = "2019-01-01";//18_nipomo_Tiles
//		static String endYear = "2024-01-31";//18_nipomo_Tiles

//		  static String startYear = "2019-01-01";//78_Tiles_vacaville_District
//			static String endYear = "2024-01-31";//78_Tiles_vacaville_District

//	  static String startYear = "2019-01-01";//33_Carlsbad_Water_District_Tiles
//		static String endYear = "2024-01-31";//33_Carlsbad_Water_District_Tiles

//		  static String startYear = "2019-01-01";//42_roseville_Water_District_Tiles
//			static String endYear = "2024-01-31";//42_roseville_Water_District_Tiles
    
//	  static String startYear = "2019-01-01";//42_roseville_Water_District_Tiles
//		static String endYear = "2024-01-31";//42_roseville_Water_District_Tiles

	


//	String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Eto_Precip_San_Jose_"+dateString+"To"+dateStringEnd+".csv";

	//+++++++++++++++++++++++++++++++++++++++++
	public static void main(String[] args) throws Exception {
		
//		getEtoForCsvTile(listofTiles,startYear,endYear,csvFilePath,ExceptioncsvFile);
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/fairfield44_.csv","2021-01-01","2024-01-31");//Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/115_las_virgenes_Tiles.csv","2019-01-01","2024-01-31");//Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/33_Carlsbad_Water_District_Tiles.csv","2019-01-01","2024-01-31");//Done		
//		 getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/409_Tiles_Western.csv","2024-01-01","2024-02-29"); //119 //Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/east_valley36_.csv","2021-01-01","2024-01-31");//Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/146_redwood_city_Tiles_copy.csv","2019-01-01","2024-01-31");//Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/42_roseville_Water_District_Tiles.csv","2019-01-01","2024-01-31"); //Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/7_bellflower_district_Tiles.csv","2021-01-01","2024-01-31");//done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/camarillo15_.csv","2021-01-01","2024-01-31");//done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/glendale35_.csv","2021-01-01","2024-01-31");//done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/36_banning_district_Tiles.csv","2021-01-01","2024-01-31");//done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/camrosa40_.csv","2021-01-01","2024-01-31");//Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/escondido_30.csv","2021-01-01","2024-01-31");//Done
		
		//14 March
//		Thread.sleep(60*1000*60*5);
//		1,125
//		13
//		1,125/90 = 12.5 
//		12.5*12
//		Uploaded
//		 getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/myoma_dunes_boundary_4_.csv","2021-01-01","2024-01-31");//10 //Done
//		 getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/orchard_dale_boundary_7_.csv","2021-01-01","2024-01-31");//18 //Done
//		 getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/rowland_boundary_18_.csv","2020-01-01","2024-01-31");//46 //Done
//		 getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/rialto_boundary_30_.csv","2020-01-01","2024-01-31");//78 //Done
//		 getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/suisun_boundary_9_.csv","2020-01-01","2024-01-31"); //23 //Done
//		 getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/trabuco_canyon_boundary_21_.csv","2021-01-01","2024-01-31"); //54 //Done
//		 getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/porterville_boundary_27_.csv","2021-01-01","2024-01-31"); //70 //Done
//		 getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/porterville_boundary_27_other_3.csv","2021-01-01","2024-01-31"); //Done
//		 getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/porterville_boundary_30_other_1.csv","2021-01-01","2024-01-31"); 

//		 getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/rubidoux_boundary_14_.csv","2021-01-01","2024-01-31");//36 OLD CACHE //Done
//		 getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/vallejo_boundary_44_.csv","2021-01-01","2024-01-31"); //114 //Done
//		 getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/pomona_boundary_28_.csv","2021-01-01","2024-01-31");//72 //Done
//		 getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/newman_boundary_12_.csv","2021-01-01","2024-01-31"); //30 //Done
//		 getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/ontario_boundary_46_.csv","2021-01-01","2024-01-31"); //119 //Done
//		 getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/oxnard_boundary_53_.csv","2021-01-01","2024-01-31");//137 //Done
//		 getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/modesto_boundary_179_.csv","2021-01-01","2024-01-31");//467 //Done
//		 getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/lake_arrowhead_boundary_86_.csv","2021-01-01","2024-01-31");//223 //Done
//		 getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/montecito_boundary_21_.csv","2021-01-01","2024-01-31");//54 //Done
		
//		 getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/folsom_boundary_38_.csv","2024-01-01","2024-02-29"); //Done
//		 getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/folsom_boundary2_14_.csv","2024-01-01","2024-02-29"); //Done
//		 getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/folsom_boundary1_6_.csv","2024-01-01","2024-02-29"); //Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/folsom_boundary_38_All.csv","2024-01-01","2024-02-29"); //Done
//		 getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/folsom_boundary_38_All.csv","2022-01-01","2023-12-31"); // Done
		
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/brentwood_boundary_18_.csv","2023-01-01","2024-02-29"); //Done
//		 getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/brentwood_boundary1_4_.csv","2023-01-01","2024-02-29"); //Done
//		 getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/brentwood_boundary2_5_.csv","2023-01-01","2024-02-29"); //Done
//		 getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/brentwood_boundary_18_All.csv","2021-01-01","2023-12-31"); // Done

		
//		 getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/casitas_boundary_122_.csv","2021-01-01","2024-01-31"); //317 //Done
//		 getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/dixon_boundary_6_.csv","2021-01-01","2024-01-31");   //Done
//		 getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/beverly_hills_boundary_10_.csv","2021-01-01","2024-01-31"); //Done
//		 getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/santa_monica_boundary_12_.csv","2021-01-01","2024-01-31"); //Done
//		 getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/simi_valley_boundary_52_.csv","2021-01-01","2024-01-31"); //Done
		 
//		 getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/indian_wells_boundary_11Zoom_51_.csv","2023-01-01","2024-01-31");//Done
//		 getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/cal_sacramento_boundary_11Zoom_48_.csv","2021-01-01","2024-01-31");//Done
//		 getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/cal_los_angeles_boundary_11Zoom_43_.csv","2021-01-01","2024-01-31");//Done
//		 getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/cal_ventura_boundary_11Zoom_30_.csv","2021-01-01","2024-01-31");//Done
//		 getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/san_gabriel_county_boundary_12Zoom_19_.csv","2021-01-01","2024-01-31");//Done
//		 getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/santa_barbara_boundary_13Zoom_21_.csv","2021-01-01","2024-01-31");//done
//		 getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/rincon_boundary_13Zoom_7_.csv","2021-01-01","2024-01-31");//Done
		
		
//		 getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/san_jose_1.csv","2021-01-01","2021-02-01"); //
//		 getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/san_jose_all_meters_Only_zoom_13.csv","2021-01-01","2021-02-01"); //
//		 getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/lake_arrowhead_boundary_combo88_15April_14.csv","2024-02-01","2024-03-31");//223 //Done
//		 getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/rialto_boundary_30_.csv","2024-02-01","2024-03-31");//78 //Done
//			getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/camrosa40_.csv","2024-01-01","2024-03-31");//Done
//			getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/trabuco_canyon_boundary_21_.csv","2024-02-01","2024-03-31");//Done
//			getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/indian_wells_35_14_zoom.csv","2023-01-01","2024-03-31");//
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/indian_wells_35_14_zoom.csv","2021-01-01","2022-12-31");//
//		getEtoForCsvTile("/home/shatam-100/Cache/1Tile_14Zoom_1_.csv","2024-01-01","2024-01-31");//
//		getEtoForCsvTile("/home/shatam-100/Cache/1Tile_14Zoom_1_.csv","2023-01-01","2023-01-31");//
		
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/san_jose_monte_vista_160_Tiles.csv","2023-09-12","2023-09-30");//
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/myoma_dunes_boundary_4_.csv","2024-02-01","2024-03-31");//10 //Done
		
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/District_Boundary_GeoJson/rincon_boundary (copy)_13Zoom_20_.csv","2020-01-01","2024-03-31");//10 //Done	
		
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/115_las_virgenes_Tiles.csv","2018-01-01","2018-12-31");//Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/san_jose_2_Tiles.csv","2020-01-01","2024-01-31");//Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/san_jose_2_Tiles_non_Added.csv","2020-01-01","2024-01-31");//Done
//		 getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/oxnard_boundary_53_1.csv", "2021-01-01", "2024-01-31");
//		 getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/146_redwood_city_Tiles.csv", "2024-02-01", "2024-03-31");
		
		// 2024-04-31 ( April Month )
//				getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/monte_vista_14_Zoom_16.csv","2024-02-01","2024-04-30");
//				getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/Tile_San_Jose_Cover_145_Tiles.csv","2024-02-01","2024-04-30");
//				getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/All_Tiles_walnut_valley.csv","2024-02-01","2024-04-30");
//				getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/146_redwood_city_Tiles.csv","2024-04-01","2024-04-30"); //NO
//				getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/409_Tiles_Western.csv","2024-03-01","2024-04-30"); //NO
//				getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/33_Carlsbad_Water_District_Tiles.csv", "2024-02-01", "2024-04-30");
//				getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/115_las_virgenes_Tiles.csv", "2024-02-01", "2024-04-30");
//				getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/18_nipomo_Tiles.csv", "2024-02-01", "2024-04-30");
//				getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/42_roseville_Water_District_Tiles.csv", "2024-02-01", "2024-04-30");
//				getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/38_Tiles_vacaville_District.csv", "2024-02-01", "2024-04-29");
//				getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/36_banning_district_Tiles.csv", "2024-02-01", "2024-04-29");
//				getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/7_bellflower_district_Tiles.csv", "2024-02-01", "2024-04-29");
//				getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/brentwood_boundary_18_All.csv", "2024-02-01", "2024-04-29");
//				getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/camarillo15_.csv", "2024-02-01", "2024-04-29");
//				getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/camrosa40_.csv","2024-04-01","2024-04-29");
//				getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/east_valley36_.csv", "2024-02-01", "2024-04-29");
//				getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/escondido_30.csv", "2024-02-01", "2024-04-29");
//				getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/fairfield44_.csv", "2024-02-01", "2024-04-29");
//				getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/folsom_boundary_38_All.csv","2024-03-01","2024-04-29");
//				getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/glendale35_.csv", "2024-02-01", "2024-04-29");
//				getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/indian_wells_35_14_zoom.csv","2024-04-01","2024-04-29");
//				getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/lake_arrowhead_boundary_86_.csv","2024-04-01","2024-04-29");
//				getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/modesto_boundary_179_.csv", "2024-02-01", "2024-04-29");
//				getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/montecito_boundary_21_.csv", "2024-02-01", "2024-04-29");
//				getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/myoma_dunes_boundary_4_.csv","2024-04-01","2024-04-29");
//				getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/orchard_dale_boundary_7_.csv", "2024-02-01", "2024-04-29");
//				getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/oxnard_boundary_53_.csv", "2024-02-01", "2024-04-29");
//				getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/pomona_boundary_28_.csv", "2024-02-01", "2024-04-29");
//				getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/rialto_boundary_30_.csv","2024-04-01","2024-04-29");
//				getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/District_Boundary_GeoJson/rincon_boundary (copy)_13Zoom_20_.csv","2024-04-01","2024-04-29");
//				getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/rowland_boundary_18_.csv","2024-02-01","2024-04-29");
//				getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/rubidoux_boundary_14_.csv","2024-02-01","2024-04-29");
//				getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/suisun_boundary_9_.csv","2024-02-01","2024-04-29"); //23
//				getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/trabuco_canyon_boundary_21_.csv","2024-04-01","2024-04-29");
//				getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/vallejo_boundary_44_.csv","2024-02-01","2024-04-29"); //114
//				getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/casitas_boundary_122_.csv","2024-02-01","2024-04-29"); //317
//				getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/cal_los_angeles_boundary_11Zoom_43_.csv","2024-02-01","2024-04-29");
//				getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/ontario_boundary_46_.csv","2024-02-01","2024-04-29"); //119
//				getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/porterville_boundary_30_.csv","2024-02-01","2024-04-29"); //70
//				getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/cal_sacramento_boundary_11Zoom_48_.csv","2024-02-01","2024-04-29");
//				getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/simi_valley_boundary_52_.csv","2024-02-01","2024-04-29");
//				getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/cal_ventura_boundary_11Zoom_30_.csv","2024-02-01","2024-04-29");
//				getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/san_gabriel_county_boundary_12Zoom_19_.csv","2024-02-01","2024-04-29");
//				getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/newman_boundary_12_.csv","2024-02-01","2024-04-29"); //30
//				getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/santa_monica_boundary_12_.csv","2024-02-01","2024-04-29");
//				getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/beverly_hills_boundary_10_.csv","2024-02-01","2024-04-29");
//				getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/dixon_boundary_6_.csv","2024-02-01","2024-04-29");
//				getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/santa_barbara_boundary_13Zoom_21_.csv","2024-02-01","2024-04-29");
		
		// 2024-04-31 ( April Month )
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/146_redwood_city_Tiles.csv","2024-04-01","2024-04-30"); //Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/409_Tiles_Western.csv","2024-03-01","2024-04-30"); //NO
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/33_Carlsbad_Water_District_Tiles.csv", "2024-02-01", "2024-04-30"); //Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/east_valley36_.csv", "2024-02-01", "2024-04-29"); //Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/montecito_boundary_21_.csv", "2024-02-01", "2024-04-29"); //Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/oxnard_boundary_53_.csv", "2024-02-01", "2024-04-29"); //Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/casitas_boundary_122_.csv","2024-02-01","2024-04-29"); //317//Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/cal_los_angeles_boundary_11Zoom_43_.csv","2024-02-01","2024-04-29");// Done 2
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/ontario_boundary_46_.csv","2024-02-01","2024-04-29"); //119 //Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/porterville_boundary_30_.csv","2024-02-01","2024-04-29"); //70 // Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/cal_sacramento_boundary_11Zoom_48_.csv","2024-02-01","2024-04-29"); //Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/simi_valley_boundary_52_.csv","2024-02-01","2024-04-29"); //Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/cal_ventura_boundary_11Zoom_30_.csv","2024-02-01","2024-04-29"); //Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/san_gabriel_county_boundary_12Zoom_19_.csv","2024-02-01","2024-04-29"); //Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/santa_monica_boundary_12_.csv","2024-02-01","2024-04-29"); //Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/beverly_hills_boundary_10_.csv","2024-02-01","2024-04-29"); //Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/dixon_boundary_6_.csv","2024-02-01","2024-04-29"); //Done
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/santa_barbara_boundary_13Zoom_21_.csv","2024-02-01","2024-04-29"); //Done

//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/Western_District_Boundary_14_Zoom_120.csv","2021-01-01","2024-04-30"); //NO
		
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/115_las_virgenes_Tiles.csv", "2017-01-01", "2017-01-01");
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries_TILES/simi_valley_11_Zoom_4.csv","2024-02-01","2024-05-31");

		//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//		getEtoForCsvTile(listofTiles,startYear,endYear,csvFilePath,ExceptioncsvFile);
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/7_bellflower_district_Tiles.csv","2021-01-01","2024-01-31");
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/camarillo15_.csv","2021-01-01","2024-01-31");
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/glendale35_.csv","2021-01-01","2024-01-31");
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries_TILES/simi_valley_11_Zoom_4.csv","2021-01-01","2024-01-31");
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries_TILES/casitas_11_Zoom_6.csv","2021-01-01","2024-05-31");
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries_TILES/san_clemente_9_Zoom_1.csv","2021-01-01","2024-05-31");
//		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries_TILES/simi_valley_11_Zoom_4.csv","2024-02-01","2024-05-31");
		getEtoForCsvTile("/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries_TILES/norco__14_Zoom_17.csv","2024-01-01","2024-06-30");

		
	}
    private static void getEtoForCsvTile(String listofTiles,String startYear,String endYear) throws Exception {
        String csvFilePath = listofTiles.replace(".csv", "_Eto_Precip_")+startYear+"_To_"+endYear+"_1_March_c1.csv";
        String ExceptioncsvFile = listofTiles.replace(".csv", "_ExceptioncsvFile_")+startYear+"_To_"+endYear+"_1_March_c1.csv";

    	System.out.println(csvFilePath);
        List<DatePair> datePairs = getDatePairsInRange(startYear, endYear, 89);

        System.out.println("Date Pairs within the range with a 90-day difference:");
        CSVWriter writer = new CSVWriter(new FileWriter(csvFilePath));
        CSVWriter exceptioncsvFile = new CSVWriter(new FileWriter(ExceptioncsvFile));

		writer.writeNext(new String[] { "SrId", "Tiles", "Date", "Perc", "ET" });

        for (DatePair datePair : datePairs) {
        	System.out.println("==================================================================================================================");
            System.out.println(datePair.getStart() + " to " + datePair.getEnd());

    		call(datePair.getStart(),  datePair.getEnd(), writer,exceptioncsvFile, listofTiles);
    		
        }
        System.out.println(csvFilePath);
        writer.close();
	}
	private static List<DatePair> getDatePairsInRange(String startDateStr, String endDateStr, int dayDifference) {
        List<DatePair> datePairs = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(startDateStr, formatter);
        LocalDate endDate = LocalDate.parse(endDateStr, formatter);

        while (startDate.plusDays(dayDifference).isBefore(endDate)) {
            LocalDate endDateOfPair = startDate.plusDays(dayDifference);
            datePairs.add(new DatePair(startDate.format(formatter), endDateOfPair.format(formatter)));
            startDate = endDateOfPair.plusDays(1);
        }

        // Add the remaining days as a separate pair if applicable
        if (!startDate.isAfter(endDate)) {
            datePairs.add(new DatePair(startDate.format(formatter), endDate.format(formatter)));
        }

        return datePairs;
    }

    private static class DatePair {
        private final String start;
        private final String end;

        public DatePair(String start, String end) {
            this.start = start;
            this.end = end;
        }

        public String getStart() {
            return start;
        }

        public String getEnd() {
            return end;
        }
    }
	private static void call(String dateString,String dateStringEnd,CSVWriter writer,CSVWriter exceptioncsvFile, String listofTiles) throws Exception {

		File input = new File(listofTiles);
		int count = 0;
		try {
			CsvSchema csv = CsvSchema.emptySchema().withHeader();
			CsvMapper csvMapper = new CsvMapper();
			MappingIterator<Map<?, ?>> mappingIterator = csvMapper.reader().forType(Map.class).with(csv)
					.readValues(input);
			List<Map<?, ?>> list = mappingIterator.readAll();
			List<JSONObject> jsonObj = new ArrayList<JSONObject>();
			U.log("total records: " + list.size());
			int i = 0;
			for (Map<?, ?> data : list) {
//				U.log(i);
				if(data.get("Tiles").toString().contains("Tiles"))return;
				Calendar calendar = Calendar.getInstance();

				SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date parsedDate = inputFormat.parse(dateString);

				String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(parsedDate);

				calendar.setTime(parsedDate);
				calendar.add(Calendar.MONTH, 1);
				calendar.set(Calendar.DAY_OF_MONTH, 1);
				calendar.add(Calendar.DATE, -1);

				DecimalFormat df = new DecimalFormat("####0.00");
				Date lastDayOfMonth = calendar.getTime();
				DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

				String Tiles = data.get("Tiles").toString();
//				U.log("Tile :: " + data.get("Tiles").toString());
				Coordinate centroid ;
//				try {
					centroid = calculateCentroid(data.get("Tiles").toString());
//				} catch (Exception e) {
//					U.log(data.get("Tiles").toString());
//					// TODO: handle exception
//				}
//				System.out.println("Centroid Latitude: " + centroid.y);
//				System.out.println("Centroid Longitude: " + centroid.x);
				String lat = centroid.y + "";
				String lon = centroid.x + "";

//				if(lat.contains("33.55")&&lon.contains("-117.19")) {
//					U.log(lat + "  |  " + lon);
//					lat = "33.55";
//					lon = "-117.18";
//				}
				double Lat = Double.parseDouble(lat);
				double Lon = Double.parseDouble(lon);
//				if(lat.contains("33.17")&&lon.contains("-117.36")) {
//					U.log(lat + "  |  " + lon);
//					lat = "33.17";
//					lon = "-117.37";
//				}
//				U.log(dateString);
//				U.log(lastDayOfMonth);

				String etoUrl = "https://et.water.ca.gov/api/data?appKey=53ce8f92-2d8d-4a02-8551-801575b1e678&targets=lat%3D"
						+ df.format(Lat) + ",lng%3D" + df.format(Lon)
						+ "&startDate=" + dateString + "&endDate=" + dateStringEnd
						+ "&dataItems=day-asce-eto&unitOfMeasure=E";

				if(etoUrl.contains("=lat%3D34.23,lng%3D-119.27&")) 
					etoUrl = etoUrl.replace("=lat%3D34.23,lng%3D-119.27&", "=lat%3D34.38,lng%3D-119.27&");
				if(etoUrl.contains("=lat%3D34.52,lng%3D-119.09&startDate=2021-01-01")) 
					etoUrl = etoUrl.replace("=lat%3D34.52,lng%3D-119.09&startDate=2021-01-01", "=lat%3D34.52,lng%3D-119.27&startDate=2021-01-01");
				if(etoUrl.contains("=lat%3D34.52,lng%3D-119.09&startDate=2021-01-01")) 
					etoUrl = etoUrl.replace("=lat%3D34.52,lng%3D-119.09&startDate=2021-01-01", "=lat%3D34.52,lng%3D-119.27&startDate=2021-01-01");
				if(etoUrl.contains("=lat%3D34.38,lng%3D-119.09&startDate=2023-06-20")) 
					etoUrl = etoUrl.replace("=lat%3D34.38,lng%3D-119.09&startDate=2023-06-20", "=lat%3D34.38,lng%3D-119.27&startDate=2023-06-20");
				if(etoUrl.contains("lat%3D33.43,lng%3D-117.77&")) 
					etoUrl = etoUrl.replace("lat%3D33.43,lng%3D-117.77&", "lat%3D33.48,lng%3D-117.30&");

//				U.log(etoUrl);
//				U.log(U.getCache(etoUrl));
				String filepath = U.getCache(etoUrl);
//				U.log(new File(filepath).exists());
//				String etoFile = "", precipFile = "";
				double avgEto = 0.0;
				double avgprecip = 0.0;
				double sumprecip = 0.0;
				double sumEto = 0.0;
				String Precip = "";
				String SrNo = "0";
				try {
//					U.log(etoUrl);
					U.downloadUsingStream(etoUrl, U.getCache(etoUrl));
				} catch (Exception e) {
					e.printStackTrace();
					U.log(Tiles);
					U.log(U.getCache(etoUrl));
					U.log(etoUrl);
					continue;
//					// TODO: handle exception
				}
				String etoFile = "";
//				try {
				etoFile = FileUtil.readAllText(U.getCache(etoUrl));
//				} catch (Exception e) {
//					System.out.println("");
//					continue;
////					return;
//					// TODO: handle exception
//				}
//				if(etoFile.contains("Request Rejected"))continue;


				
				String etoVals[] = U.getValues(etoFile, "{\"Date\"", "}}");

				String zip = U.getSectionValue(etoFile, "\"ZipCodes\":\"", "\"");
				if (zip == null) {
//					try {
						zip = U.getSectionValue(etoFile, "zip-code=\"", "\"");
//						U.log(zip);
//					} catch (Exception e) {
//						U.log("TODO: handle exception1");
//						// TODO: handle exception
//					}
				}
				if (zip == null) {
//					U.log(etoUrl);
//					U.log(U.getCache(etoUrl));
					String[] latlag  ;
					double latValue;
					latValue = Double.parseDouble(lat.toString());
					String formattedLat = df.format(latValue); 
					double LonValue;
					LonValue = Double.parseDouble(lon.toString());
					String formattedLon = df.format(LonValue); 
					try {
//					System.out.println( formattedLat +"  |  "+formattedLon );
						latlag = new String[] {formattedLat, formattedLon };
						if(formattedLon.contains("-117.19")) formattedLon = "-117.18";
					} catch (Exception e) {		
						
					    System.err.println("Error: " + e.getMessage());

						e.getStackTrace();
						U.log( df.format(formattedLat)+" | "+ df.format(formattedLon) );
						U.log("TODO: handle exception 2");
						U.log(etoUrl);
						U.log(Tiles);
						continue;
						// TODO: handle exception
					}
					String add[] ;
					try {
						add = U.getGoogleAddressWithKey(latlag);
						zip = add[3];
					} catch (Exception e) {
						// TODO: handle exception
						String st = latlag[0].trim() + "," + latlag[1].trim();
						String addr = "https://maps.googleapis.com/maps/api/geocode/json?latlng="+ st;
						String key = "AIzaSyAeG9lLU8fWh8rWcPivHDwxLAM4ZCInpmk";
//						U.log(latlag[0].trim() + " " + latlag[1].trim());
//						U.log("With key"+U.getCache(addr));
						
						String html = U.getHTMLForGoogleApiWithKey(addr,key);
						String txt = U.getSectionValue(html, "formatted_address\" : \"", "\"");
						String ZIPS_LIST[] = U.getValues(html, "formatted_address\" : \"", "\"");
//						System.out.println(ZIPS_LIST[2]);
				        String zipCode = extractZipCode(ZIPS_LIST[1]);
				        zip = zipCode;
//						U.log(zip);
//						writer.writeNext(etoVals);
//						continue;
					}
					
					


//					U.log(Arrays.toString(add));
					
				}if(zip==null) {
					System.out.println(Tiles);
					continue;
				}
				if(!(zip== null) && zip.contains("92878"))zip = "92882";
				String precipUrl = "https://et.water.ca.gov/api/data?appKey=53ce8f92-2d8d-4a02-8551-801575b1e678&targets="
						+ zip.trim() + "&startDate=" + dateString + "&endDate=" + dateStringEnd
						+ "&dataItems=day-precip&unitOfMeasure=E";
//				U.log(precipUrl);
				try {
				U.downloadUsingStream(precipUrl, U.getCache(precipUrl));
				} catch (Exception e) {
//					e.printStackTrace();
					U.log(U.getCache(etoUrl));
					U.log(zip);
					U.log(Tiles);
					U.log(precipUrl);
					continue;
					// TODO: handle exception
				}
				String precipFile = FileUtil.readAllText(U.getCache(precipUrl));
//				U.log(U.getCache(precipUrl));
//				U.log(precipFile);
				String precipVals[] = U.getValues(precipFile, "{\"Date\":", "\"}");
//				U.log(precipVals.length);
				Map<String, List<String>> combinedDataMap = new HashMap<>();

//				U.log("------------------------------------");
				for (String eto : etoVals) {
//                    U.log(sec);
					String Date = U.getSectionValue(eto, ":\"", "\",");
					String ET_Value = U.getSectionValue(eto, "{\"Value\":\"", "\",");
//					U.log(Date + "  |ET_Value  " + ET_Value);
					// "SrId", "Tiles", "Date", "Perc", "ET"
//					U.writeAllText("/home/shatam-100/Cache/untitled.txt", "ET: " + ET_Value);
					combinedDataMap.computeIfAbsent(Date, k -> new ArrayList<>()).add("ET: " + ET_Value);
				}
				
				for (String precipsec : precipVals) {
//                  U.log(precipsec);
					String Date = U.getSectionValue(precipsec, "\"", "\",");
					String Precip_Value = U.getSectionValue(precipsec, "{\"Value\":\"", "\",");
//					U.log(Date + "  |Precip_Value  " + Precip_Value);
//					writer.writeNext(new String[] { SrNo, Tiles,Date, Precip_Value, "ET_Value" });
					// "SrId", "Tiles", "Date", "Perc", "ET"
//					U.writeAllText("/home/shatam-100/Cache/untitled.txt", "Precipitation: " + Precip_Value);
				    combinedDataMap.computeIfAbsent(Date, k -> new ArrayList<>()).add("Precipitation: " + Precip_Value);
				}

				
				
				for (Map.Entry<String, List<String>> entry : combinedDataMap.entrySet()) {
					String date = entry.getKey();
					List<String> values = entry.getValue();
		            String Prec = "0.0";
//		            System.out.println();
		            String eto =values.get(0).replace("ET: ", "");
		            if(values.size()>1) {
//		            System.out.println(values.toString());
		            	
		            Prec =values.get(1).replace("Precipitation: ", "");
		            }else {
		            	Prec = "0.0";
		            	System.out.println(date+" | "+values.toString());
		            	System.out.println(etoUrl);
		            	System.out.println(Tiles);
		            	System.out.println(U.getCache(etoUrl));
		            	System.out.println(precipUrl);
		            	System.out.println(U.getCache(precipUrl));
						System.out.println("0.0");
						return;
					}
		            
//		            U.log(Prec);
		            if (Prec == null || Prec.equalsIgnoreCase("null")) {
		                Prec = "0.0";
		            }
//		            U.log(Prec);
//		            U.log(date + " | " + String.join(" | ", values));
//		            U.log(date + " | Precipitation: " + values.get(0) + " | ET: " + values.get(1));
//		            System.out.println( SrNo+ Tiles+ date+ Prec);
//					try {
						writer.writeNext(new String[] { SrNo, Tiles, date, Prec,eto });
//					} catch (Exception e) {
////			            System.out.println(values.get(0)+" | "+values.get(1));
//						e.printStackTrace();
////			            U.log(date + " | Precipitation: " + values.get(0) + " | ET: " );
//						U.log("Exception");
//						// TODO: handle exception
//					}

				}

//				for (String eto : etoVals) {
//					sumEto += Double.parseDouble(eto);
//				}
//				avgEto = sumEto / etoVals.length;
//				U.log("avgEto :: " + avgEto);
//				U.log("avgprecip  " + avgprecip);
//                writer.writeNext(new String[]{avgEto + "", avgprecip + ""});
//				return;
//				if(dateStringEnd.contains("2020-09-21")) {
//					return;
//				}
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
//			if (writer != null) {
//				writer.close();
//			}
		}

	}

    private static String extractZipCode(String address) {
        // Define the pattern for a 5-digit ZIP code
        Pattern pattern = Pattern.compile("\\b\\d{5}\\b");

        // Create a matcher with the input address
        Matcher matcher = pattern.matcher(address);

        // Find the first occurrence of the pattern in the address
        if (matcher.find()) {
            return matcher.group();
        } else {
            return null;
        }
    }

	static class CombinedValues {
		private String precipitation;
		private String ET;

		public String getPrecipitation() {
			return precipitation;
		}

		public void setPrecipitation(String precipitation) {
			this.precipitation = precipitation;
		}

		public String getET() {
			return ET;
		}

		public void setET(String ET) {
			this.ET = ET;
		}
	}

	private static Coordinate calculateCentroid(String tileCoordinates)
			throws com.vividsolutions.jts.io.ParseException, ParseException {
		WKTReader reader = new WKTReader();
		org.locationtech.jts.geom.Geometry geometry = reader.read(tileCoordinates);
		Point centroid = geometry.getCentroid();
		return new Coordinate(centroid.getX(), centroid.getY());
	}
}
