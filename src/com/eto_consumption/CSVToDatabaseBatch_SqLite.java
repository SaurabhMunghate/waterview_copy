package com.eto_consumption;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.opencsv.CSVReader;

public class CSVToDatabaseBatch_SqLite {
	private static String TableName = null;

	public static void main(String[] args) throws Exception {
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/camrosa40__Eto_Precip_2022-01-01_To_2022-12-31_3_May_c1.csv";
        TableName ="camrosa";
        
        System.out.println(TableName);
        Connection connection = null;
        BufferedReader reader = null;
        CSVReader csvReader = null;
        String sqliteFilePath = "/home/shatam-100/Down/WaterView_Data/EtoDatabase.db"; // path to your SQLite database file

//        String csvFilePath = "/home/shatam-100/Down/WaterView_Data/SAN_JOSE_ETo_DB_Tiles.csv"; // path to your CSV file
//        String csvFilePath = "/home/shatam-100/Down/WaterView_Data/MONTEVISTACO226_ETo_DB_Tiles.csv"; // path to your CSV file
//        String csvFilePath = "/home/shatam-100/Down/WaterView_Data/41_Tiles_jurupa_Eto_Precip_2021-01-01_To_2023-12-31.csv"; // path to your CSV file
    	
//    	String csvFilePath = "/home/shatam-100/Down/WaterView_Data/All_Tiles_walnut_valley_Eto_Precip_2019-01-01_To_2023-12-31_27_Feb_c2.csv";//walnut valley
//    	String csvFilePath = "/home/shatam-100/Down/WaterView_Data/All_Tiles_walnut_valley_Eto_Precip_2024-01-01_To_2024-01-31_28_Feb_c1.csv"; 

//    	String csvFilePath = "/home/shatam-100/Down/WaterView_Data/409_Tiles_Western_Eto_Precip_2021-01-01_To_2023-09-17_28_Feb_c1.csv"; 
//    	String csvFilePath = "/home/shatam-100/Down/WaterView_Data/409_Tiles_Western_Eto_Precip_2023-09-17_To_2023-12-31_28_Feb_c1.csv";

//    	String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/18_nipomo_Tiles_Eto_Precip_2019-01-01_To_2024-01-31_28_Feb_c1.csv";
//        String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/78_Tiles_vacaville_District_Eto_Precip_2019-01-01_To_2024-01-31_28_Feb_c2.csv";

//        String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/camarillo15__Eto_Precip_2021-01-01_To_2024-01-31_4_March_c1_Final.csv";
//        String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/7_bellflower_district_Tiles_Eto_Precip_2021-01-01_To_2024-01-31_4_March_c1_Final.csv";
//        String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/42_roseville_Water_District_Tiles_Eto_Precip_2019-01-01_To_2024-01-31_4_March_c1_Final.csv";
//        String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/glendale35__Eto_Precip_2021-01-01_To_2024-01-31_4_March_c1_Final.csv";
//        String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/146_redwood_city_Tiles_copy_Eto_Precip_2019-01-01_To_2024-01-31_4_March_c1_Final.csv";
//        String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/escondido_30_Eto_Precip_2021-01-01_To_2024-01-31_4_March_c1_Final.csv";
//        String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/camrosa40__Eto_Precip_2021-01-01_To_2024-01-31_4_March_c1_Final.csv";
//        String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/36_banning_district_Tiles_Eto_Precip_2021-01-01_To_2024-01-31_14_March_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/409_Tiles_Western_Eto_Precip_2024-01-01_To_2024-02-29_14_March_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/east_valley36__Eto_Precip_2021-01-01_To_2024-01-31_14_March_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/orchard_dale_boundary_7__Eto_Precip_2021-01-01_To_2024-01-31_19_March_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/myoma_dunes_boundary_4__Eto_Precip_2021-01-01_To_2024-01-31_19_March_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/rowland_boundary_18__Eto_Precip_2020-01-01_To_2024-01-31_19_March_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/rialto_boundary_30__Eto_Precip_2020-01-01_To_2024-01-31_19_March_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/suisun_boundary_9__Eto_Precip_2020-01-01_To_2024-01-31_19_March_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/trabuco_canyon_boundary_21__Eto_Precip_2021-01-01_To_2024-01-31_19_March_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/porterville_boundary_27__Eto_Precip_2021-01-01_To_2024-01-31_19_March_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/rubidoux_boundary_14__Eto_Precip_2021-01-01_To_2024-01-31_19_March_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/vallejo_boundary_44__Eto_Precip_2021-01-01_To_2024-01-31_19_March_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/pomona_boundary_28__Eto_Precip_2021-01-01_To_2024-01-31_19_March_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/newman_boundary_12__Eto_Precip_2021-01-01_To_2024-01-31_19_March_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/ontario_boundary_46__Eto_Precip_2021-01-01_To_2024-01-31_19_March_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/oxnard_boundary_53__Eto_Precip_2021-01-01_To_2024-01-31_19_March_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/folsom_boundary_38_All_Eto_Precip_2024-01-01_To_2024-02-29_19_March_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/brentwood_boundary_18__Eto_Precip_2023-01-01_To_2024-02-29_19_March_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/brentwood_boundary1_4__Eto_Precip_2023-01-01_To_2024-02-29_19_March_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/brentwood_boundary2_5__Eto_Precip_2023-01-01_To_2024-02-29_19_March_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/folsom_boundary_38_All_Eto_Precip_2022-01-01_To_2023-12-31_19_March_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/brentwood_boundary_18_All_Eto_Precip_2021-01-01_To_2023-12-31_19_March_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/33_Carlsbad_Water_District_Tiles_Eto_Precip_2019-01-01_To_2024-01-31_19_March_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/115_las_virgenes_Tiles_Eto_Precip_2019-01-01_To_2024-01-31_19_March_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/fairfield44__Eto_Precip_2021-01-01_To_2024-01-31_19_March_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/modesto_boundary_179__Eto_Precip_2021-01-01_To_2024-01-31_19_March_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/lake_arrowhead_boundary_86__Eto_Precip_2021-01-01_To_2024-01-31_19_March_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/montecito_boundary_21__Eto_Precip_2021-01-01_To_2024-01-31_19_March_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/dixon_boundary_6__Eto_Precip_2021-01-01_To_2024-01-31_19_March_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/beverly_hills_boundary_10__Eto_Precip_2021-01-01_To_2024-01-31_19_March_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/santa_monica_boundary_12__Eto_Precip_2021-01-01_To_2024-01-31_19_March_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/casitas_boundary_122__Eto_Precip_2021-01-01_To_2024-01-31_19_March_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/indian_wells_boundary_11Zoom_51__Eto_Precip_2023-01-01_To_2024-01-31_21_March_c1.csv";//er
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/cal_sacramento_boundary_11Zoom_48__Eto_Precip_2021-01-01_To_2024-01-31_21_March_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/cal_los_angeles_boundary_11Zoom_43__Eto_Precip_2021-01-01_To_2024-01-31_21_March_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/cal_ventura_boundary_11Zoom_30__Eto_Precip_2021-01-01_To_2024-01-31_21_March_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/san_gabriel_county_boundary_12Zoom_19__Eto_Precip_2021-01-01_To_2024-01-31_21_March_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/santa_barbara_boundary_13Zoom_21__Eto_Precip_2021-01-01_To_2024-01-31_21_March_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/rincon_boundary_13Zoom_7__Eto_Precip_2021-01-01_To_2024-01-31_21_March_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/simi_valley_boundary_52__Eto_Precip_2021-01-01_To_2024-01-31_21_March_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/lake_arrowhead_boundary_combo88_15April_14_Eto_Precip_2024-02-01_To_2024-03-31_21_March_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/rialto_boundary_30__Eto_Precip_2024-02-01_To_2024-03-31_21_March_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/camrosa40__Eto_Precip_2024-01-01_To_2024-03-31_21_March_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/trabuco_canyon_boundary_21__Eto_Precip_2024-02-01_To_2024-03-31_21_March_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/indian_wells_35_14_zoom_Eto_Precip_2023-01-01_To_2024-03-31_21_March_c1.csv";
//		String csvFilePath = "/home/shatam-100/Cache/1Tile_14Zoom_1__Eto_Precip_2024-01-01_To_2024-01-31_21_March_c1.csv";
//		String csvFilePath = "/home/shatam-100/Cache/1Tile_14Zoom_1__Eto_Precip_2023-01-01_To_2023-01-31_21_March_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/san_jose_monte_vista_160_Tiles_Eto_Precip_2023-09-12_To_2023-09-30_21_March_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/myoma_dunes_boundary_4__Eto_Precip_2024-02-01_To_2024-03-31_21_March_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/District_Boundary_GeoJson/rincon_boundary (copy)_13Zoom_20__Eto_Precip_2020-01-01_To_2024-03-31_21_March_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/115_las_virgenes_Tiles_Eto_Precip_2018-01-01_To_2018-12-31_21_March_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/san_jose_2_Tiles_Eto_Precip_2020-01-01_To_2024-01-31_21_March_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/san_jose_2_Tiles_non_Added_Eto_Precip_2020-01-01_To_2024-01-31_21_March_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/porterville_boundary_30_other_1_Eto_Precip_2021-01-01_To_2024-01-31_21_March_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/146_redwood_city_Tiles_Eto_Precip_2024-02-01_To_2024-03-31_21_March_c1.csv";
		
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/Jan_April_Month_eto/7_bellflower_district_Tiles_Eto_Precip_2024-02-01_To_2024-04-29_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/Jan_April_Month_eto/18_nipomo_Tiles_Eto_Precip_2024-02-01_To_2024-04-30_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/Jan_April_Month_eto/36_banning_district_Tiles_Eto_Precip_2024-02-01_To_2024-04-29_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/Jan_April_Month_eto/38_Tiles_vacaville_District_Eto_Precip_2024-02-01_To_2024-04-29_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/Jan_April_Month_eto/42_roseville_Water_District_Tiles_Eto_Precip_2024-02-01_To_2024-04-30_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/Jan_April_Month_eto/115_las_virgenes_Tiles_Eto_Precip_2024-02-01_To_2024-04-30_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/Jan_April_Month_eto/All_Tiles_walnut_valley_Eto_Precip_2024-02-01_To_2024-04-30_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/Jan_April_Month_eto/brentwood_boundary_18_All_Eto_Precip_2024-02-01_To_2024-04-29_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/Jan_April_Month_eto/camarillo15__Eto_Precip_2024-02-01_To_2024-04-29_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/Jan_April_Month_eto/camrosa40__Eto_Precip_2024-04-01_To_2024-04-29_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/Jan_April_Month_eto/escondido_30_Eto_Precip_2024-02-01_To_2024-04-29_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/Jan_April_Month_eto/fairfield44__Eto_Precip_2024-02-01_To_2024-04-29_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/Jan_April_Month_eto/folsom_boundary_38_All_Eto_Precip_2024-03-01_To_2024-04-29_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/Jan_April_Month_eto/glendale35__Eto_Precip_2024-02-01_To_2024-04-29_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/Jan_April_Month_eto/indian_wells_35_14_zoom_Eto_Precip_2024-04-01_To_2024-04-29_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/Jan_April_Month_eto/lake_arrowhead_boundary_86__Eto_Precip_2024-04-01_To_2024-04-29_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/Jan_April_Month_eto/modesto_boundary_179__Eto_Precip_2024-02-01_To_2024-04-29_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/Jan_April_Month_eto/monte_vista_14_Zoom_16_Eto_Precip_2024-02-01_To_2024-04-30_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/Jan_April_Month_eto/myoma_dunes_boundary_4__Eto_Precip_2024-04-01_To_2024-04-29_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/Jan_April_Month_eto/newman_boundary_12__Eto_Precip_2024-02-01_To_2024-04-29_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/Jan_April_Month_eto/orchard_dale_boundary_7__Eto_Precip_2024-02-01_To_2024-04-29_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/Jan_April_Month_eto/pomona_boundary_28__Eto_Precip_2024-02-01_To_2024-04-29_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/Jan_April_Month_eto/rialto_boundary_30__Eto_Precip_2024-04-01_To_2024-04-29_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/Jan_April_Month_eto/rincon_boundary (copy)_13Zoom_20__Eto_Precip_2024-04-01_To_2024-04-29_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/Jan_April_Month_eto/rowland_boundary_18__Eto_Precip_2024-02-01_To_2024-04-29_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/Jan_April_Month_eto/rubidoux_boundary_14__Eto_Precip_2024-02-01_To_2024-04-29_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/Jan_April_Month_eto/suisun_boundary_9__Eto_Precip_2024-02-01_To_2024-04-29_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/Jan_April_Month_eto/Tile_San_Jose_Cover_145_Tiles_Eto_Precip_2024-02-01_To_2024-04-30_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/Jan_April_Month_eto/trabuco_canyon_boundary_21__Eto_Precip_2024-04-01_To_2024-04-29_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/Jan_April_Month_eto/vallejo_boundary_44__Eto_Precip_2024-02-01_To_2024-04-29_3_May_c1.csv";
		
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/Jan_April_Month_eto/33_Carlsbad_Water_District_Tiles_Eto_Precip_2024-02-01_To_2024-04-30_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/Jan_April_Month_eto/146_redwood_city_Tiles_Eto_Precip_2024-04-01_To_2024-04-30_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/Jan_April_Month_eto/beverly_hills_boundary_10__Eto_Precip_2024-02-01_To_2024-04-29_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/Jan_April_Month_eto/cal_los_angeles_boundary_11Zoom_43__Eto_Precip_2024-02-01_To_2024-04-29_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/Jan_April_Month_eto/cal_sacramento_boundary_11Zoom_48__Eto_Precip_2024-02-01_To_2024-04-29_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/Jan_April_Month_eto/cal_ventura_boundary_11Zoom_30__Eto_Precip_2024-02-01_To_2024-04-29_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/Jan_April_Month_eto/casitas_boundary_122__Eto_Precip_2024-02-01_To_2024-04-29_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/Jan_April_Month_eto/dixon_boundary_6__Eto_Precip_2024-02-01_To_2024-04-29_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/Jan_April_Month_eto/east_valley36__Eto_Precip_2024-02-01_To_2024-04-29_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/Jan_April_Month_eto/montecito_boundary_21__Eto_Precip_2024-02-01_To_2024-04-29_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/Jan_April_Month_eto/ontario_boundary_46__Eto_Precip_2024-02-01_To_2024-04-29_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/Jan_April_Month_eto/oxnard_boundary_53__Eto_Precip_2024-02-01_To_2024-04-29_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/Jan_April_Month_eto/porterville_boundary_30__Eto_Precip_2024-02-01_To_2024-04-29_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/Jan_April_Month_eto/san_gabriel_county_boundary_12Zoom_19__Eto_Precip_2024-02-01_To_2024-04-29_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/Jan_April_Month_eto/santa_barbara_boundary_13Zoom_21__Eto_Precip_2024-02-01_To_2024-04-29_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/Jan_April_Month_eto/santa_monica_boundary_12__Eto_Precip_2024-02-01_To_2024-04-29_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/Jan_April_Month_eto/simi_valley_boundary_52__Eto_Precip_2024-02-01_To_2024-04-29_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/115_las_virgenes_Tiles_Eto_Precip_2017-01-01_To_2017-12-01_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/115_las_virgenes_Tiles_Eto_Precip_2018-07-01_To_2018-08-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/santa_barbara_boundary_13Zoom_21__Eto_Precip_2024-02-01_To_2024-04-29_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/santa_monica_boundary_12__Eto_Precip_2024-02-01_To_2024-04-29_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/myoma_dunes_boundary_4__Eto_Precip_2024-04-31_To_2024-05-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/camrosa40__Eto_Precip_2024-01-01_To_2024-05-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/Tile_San_Jose_Cover_145_Tiles_Eto_Precip_2024-04-31_To_2024-05-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/rowland_boundary_18__Eto_Precip_2024-04-30_To_2024-05-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/dixon_boundary_6__Eto_Precip_2024-04-30_To_2024-05-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/33_Carlsbad_Water_District_Tiles_Eto_Precip_2024-04-30_To_2024-05-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/santa_monica_boundary_12__Eto_Precip_2024-04-30_To_2024-05-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/146_redwood_city_Tiles_Eto_Precip_2024-04-30_To_2024-05-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/District_Boundary_GeoJson/rincon_boundary (copy)_13Zoom_20__Eto_Precip_2024-04-30_To_2024-05-31_3_May_c1.csv";
		
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/monte_vista_14_Zoom_16_Eto_Precip_2024-04-30_To_2024-05-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/camarillo15__Eto_Precip_2024-04-30_To_2024-05-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/brentwood_boundary_18_All_Eto_Precip_2024-04-30_To_2024-05-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/115_las_virgenes_Tiles_Eto_Precip_2024-04-30_To_2024-05-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/42_roseville_Water_District_Tiles_Eto_Precip_2024-04-30_To_2024-05-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/36_banning_district_Tiles_Eto_Precip_2024-04-30_To_2024-05-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/18_nipomo_Tiles_Eto_Precip_2024-04-30_To_2024-05-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/7_bellflower_district_Tiles_Eto_Precip_2024-04-30_To_2024-05-31_3_May_c1.csv";

//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/montecito_boundary_21__Eto_Precip_2024-04-30_To_2024-05-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/modesto_boundary_179__Eto_Precip_2024-04-30_To_2024-05-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/lake_arrowhead_boundary_86__Eto_Precip_2024-04-30_To_2024-05-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/indian_wells_35_14_zoom_Eto_Precip_2024-04-30_To_2024-05-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/glendale35__Eto_Precip_2024-04-30_To_2024-05-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/folsom_boundary_38_All_Eto_Precip_2024-04-30_To_2024-05-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/fairfield44__Eto_Precip_2024-04-30_To_2024-05-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/escondido_30_Eto_Precip_2024-04-30_To_2024-05-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/east_valley36__Eto_Precip_2024-04-30_To_2024-05-31_3_May_c1.csv";
		
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/rialto_boundary_30__Eto_Precip_2024-04-30_To_2024-05-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/pomona_boundary_28__Eto_Precip_2024-04-30_To_2024-05-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/oxnard_boundary_53__Eto_Precip_2024-04-30_To_2024-05-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/orchard_dale_boundary_7__Eto_Precip_2024-04-30_To_2024-05-31_3_May_c1.csv";
		
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/san_gabriel_county_boundary_12Zoom_19__Eto_Precip_2024-04-30_To_2024-05-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/newman_boundary_12__Eto_Precip_2024-04-30_To_2024-05-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/cal_ventura_boundary_11Zoom_30__Eto_Precip_2024-04-30_To_2024-05-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/simi_valley_boundary_52__Eto_Precip_2024-04-30_To_2024-05-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/porterville_boundary_30__Eto_Precip_2024-04-30_To_2024-05-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/ontario_boundary_46__Eto_Precip_2024-04-30_To_2024-05-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/cal_sacramento_boundary_11Zoom_48__Eto_Precip_2024-04-30_To_2024-05-31_3_May_c1.csv";//null
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/cal_los_angeles_boundary_11Zoom_43__Eto_Precip_2024-04-30_To_2024-05-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/casitas_boundary_122__Eto_Precip_2024-04-30_To_2024-05-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/vallejo_boundary_44__Eto_Precip_2024-04-30_To_2024-05-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/trabuco_canyon_boundary_21__Eto_Precip_2024-04-30_To_2024-05-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/suisun_boundary_9__Eto_Precip_2024-04-30_To_2024-05-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/rubidoux_boundary_14__Eto_Precip_2024-04-30_To_2024-05-31_3_May_c1.csv";
		
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/santa_barbara_boundary_13Zoom_21__Eto_Precip_2024-04-30_To_2024-05-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/dixon_boundary_6__Eto_Precip_2024-04-30_To_2024-05-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/beverly_hills_boundary_10__Eto_Precip_2024-04-30_To_2024-05-31_3_May_c1.csv";
		
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/All_Tiles_walnut_valley_Eto_Precip_2024-04-30_To_2024-05-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/escondido_final_14_Zoom_36_Eto_Precip_2022-01-01_To_2024-05-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/escondido_5_tiles_Eto_Precip_2021-01-01_To_2024-05-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/33_Carlsbad_Water_District_Tiles_Eto_Precip_2024-05-01_To_2024-05-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/indian_wells_35_14_zoom_Eto_Precip_2021-01-01_To_2022-12-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/Escondido_7_Eto_Precip_2021-01-01_To_2024-05-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/modesto_2_Eto_Precip_2021-01-01_To_2024-05-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/modesto_2_Eto_Precip_2023-12-17_To_2024-05-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/norco_14_Zoom_17_Eto_Precip_2021-01-01_To_2024-05-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/cal_los_angeles_12_Zoom_6_Eto_Precip_2021-01-01_To_2024-05-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/walnut_valley_14_Zoom_38_Eto_Precip_2020-07-01_To_2020-08-30_3_May_c1.csv";
		
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/cal_ventura__12_Zoom_5_Eto_Precip_2019-01-01_To_2024-05-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/las_vergenes__custom_Zoom_2_Eto_Precip_2019-01-01_To_2024-05-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/vallejo__12_Zoom_13_Eto_Precip_2019-01-01_To_2024-05-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/western_14_Zoom_123__Eto_Precip_2021-01-01_To_2024-05-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/lake_arrowhead__12_Zoom_1_Eto_Precip_2021-01-01_To_2024-05-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries_TILES/rancho__11_Zoom_4_Eto_Precip_2021-01-01_To_2024-05-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/santa_barbara_boundary_13Zoom_21__Eto_Precip_2021-01-01_To_2024-01-31_21_March_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries_TILES/vallejo_8_Zoom_2_Eto_Precip_2021-01-01_To_2024-05-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/las_vergenes__custom_Zoom_2_Eto_Precip_2017-01-01_To_2020-12-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries_TILES/vacaville__14_Zoom_39_Eto_Precip_2024-04-30_To_2024-05-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Cache/western14_Zoom_27_Eto_Precip_2021-01-01_To_2024-05-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries_TILES/cal_sacramento__11_Zoom_6_Eto_Precip_2021-01-01_To_2024-05-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/pomona_boundary_28__Eto_Precip_2020-01-01_To_2020-12-31_3_May_c1.csv";
//		String csvFilePath = "/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/DistrictBoundaries_TILES/simi_valley_11_Zoom_4_Eto_Precip_2021-01-01_To_2024-01-31_1_March_c1.csv";


        try {
            // Load the SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");
            
            // Connect to the SQLite database
            connection = DriverManager.getConnection("jdbc:sqlite:" + sqliteFilePath);

            connection.setAutoCommit(false);

            reader = new BufferedReader(new FileReader(csvFilePath));
            csvReader = new CSVReader(reader);

            String[] nextLine;
//            String sql = "INSERT INTO Eto_DB_Tiles (Tiles, Date, Precip, ET_Value) VALUES (?, ?, ?, ?)";
            String sql = "INSERT INTO "+TableName+" (Tiles, Date, Precip, ET_Value, CREATED_DATE) VALUES (?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // Skip the CSV header line
            csvReader.readNext();
            // Get the current date and time
            LocalDateTime currentDateTime = LocalDateTime.now();

            // Format the date and time (optional)
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = currentDateTime.format(formatter);

            
            int batchSize = 20000; // Set a batch size for optimizing the insert operation
            int currentBatchSize = 0;
            int Count = 0;

            while ((nextLine = csvReader.readNext()) != null) {
                String tiles = nextLine[1];
                String date = nextLine[2];
                String perc = nextLine[3];
                double et = 0.0;
                try {
                	et = Double.parseDouble(nextLine[4]); // Parse 'et' as a double
				} catch (Exception e) {
					System.out.println(nextLine[4]+"");
					
					// TODO: handle exception
				}
//                String polygonString = "POLYGON ((-122.080078125 37.35269280367274, -122.10205078125 37.35269280367274, -122.10205078125 37.33522435930638, -122.080078125 37.33522435930638, -122.080078125 37.35269280367274))";

                String polygonString = tiles;
                // Remove "POLYGON ((" and "))" from the string
                String cleanPolygonString = polygonString.substring(10, polygonString.length() - 2);

                // Split the coordinates by comma and space
//                String[] coordinates = cleanPolygonString.split(", ");

                // Extract x, y, x1, y1 coordinates
//                double x = Double.parseDouble(coordinates[0].split(" ")[0]);
//                double y = Double.parseDouble(coordinates[0].split(" ")[1]);
//                double x1 = Double.parseDouble(coordinates[2].split(" ")[0]);
//                double y1 = Double.parseDouble(coordinates[2].split(" ")[1]);

                // Form key using coordinates
//                String tiles_key = String.format("%.6f_%.6f_%.6f_%.6f", x, y, x1, y1);
//                System.out.println("Key: " + tiles_key);

                preparedStatement.setString(1, polygonString);
                preparedStatement.setString(2, date);
                preparedStatement.setString(3, perc);
                preparedStatement.setDouble(4, et);
                preparedStatement.setString(5, formattedDateTime);

                preparedStatement.addBatch();
                currentBatchSize++;
                
//                System.out.println(currentBatchSize);
                
                // Execute the batch when it reaches the specified batch size
                if (currentBatchSize % batchSize == 0) {
                	System.out.println(currentBatchSize);
                    preparedStatement.executeBatch();
                    connection.commit();
                    currentBatchSize = 0;
                }
                Count++;
            }
            System.out.println("Total Count : "+Count);
            // Execute any remaining batched statements
            preparedStatement.executeBatch();
            connection.commit();

            System.out.println("Data imported successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close resources in a finally block
            if (csvReader != null) {
                csvReader.close();
            }
            if (reader != null) {
                reader.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }
}
