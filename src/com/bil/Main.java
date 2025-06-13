package com.bil;

import java.text.ParseException;
import java.util.Arrays;

import com.shatam.utils.U;
import com.shatam.utils.Util;

import java.io.IOException;
import java.io.RandomAccessFile;

public class Main {
    private static final float NODATA = -9999f;

	public static void main(String[] args) throws ParseException {
		
		//getPrecipitationFromRowCol
//        try (RandomAccessFile raf = new RandomAccessFile("/home/shatam-100/Down/WaterView_Data/PRISM_ppt_stable_4kmM3_20/all_bil/PRISM_ppt_stable_4kmM3_2019_all_bil/PRISM_ppt_stable_4kmM3_201901_bil.bil", "r")) {
//            int row = 444;  // Example row index
//            int col = 306;  // Example column index
//            
//            float precip = U.getPrecipitationFromRowCol(raf, row, col);
//            
//            if (precip != NODATA) {
//                System.out.printf("Precipitation at row %d, col %d: %.2f%n", row, col, precip);
//            } else {
//                System.out.println("No data available for this row and column.");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
		
		String zip = U.getBingAddress(lat, lon)[3];

	}
	public static String[] getBingAddress(String lat, String lon) throws Exception {
		String[] addr = null;
		String htm = U.getHTML("http://dev.virtualearth.net/REST/v1/Locations/" + lat + "," + lon
				+ "?o=json&jsonp=GeocodeCallback&key=Ak8RoKwrZE-IbLkkXRFae9UTXw2UhpuET1mWY9z_ZkzTYR_-TCG8pMcNIUEtiqo5");
		U.log("bing url :::: " + "http://dev.virtualearth.net/REST/v1/Locations/" + lat + "," + lon
				+ "?o=json&jsonp=GeocodeCallback&key=Ak8RoKwrZE-IbLkkXRFae9UTXw2UhpuET1mWY9z_ZkzTYR_-TCG8pMcNIUEtiqo5");
		U.log("Bing cache :::: " + "http://dev.virtualearth.net/REST/v1/Locations/" + lat + "," + lon
				+ "?o=json&jsonp=GeocodeCallback&key=Ak8RoKwrZE-IbLkkXRFae9UTXw2UhpuET1mWY9z_ZkzTYR_-TCG8pMcNIUEtiqo5");
		String[] adds = U.getValues(htm, "formattedAddress\":\"", "\"");
		for (String item : adds) {
			addr = U.getFormattedAddressBing(item);
			if (addr == null || addr[0] == "-") {
				continue;
			}
			else if(addr[0]==null || addr[1]==null || addr[2]==null || addr[3]==null) {
				addr[0] = item.split(",")[0].trim();
				addr[1] = item.split(",")[1].trim();
				addr[2] = item.split(",")[2].trim().replaceAll("-\\d{4}", "");
				addr[3] = Util.match(addr[2], "\\d+");
				addr[2] = addr[2].replaceAll("\\d+", "").trim();
				U.log("bing address => " + Arrays.toString(addr));
				return addr;
			}
			else {
				U.log("Bing Address =>  Street:" + addr[0] + " City :" + addr[1] + " state :" + addr[2] + " ZIP :"
						+ addr[3]);
				return addr;
			}

		}
		return addr;
	}
	
}
