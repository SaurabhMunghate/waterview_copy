package com.shatam.utils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CheckNonUTFCharacter {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
/*		String s;
        Process p;
        try {
        	String file = "/home/glady/workspaces/Parcel_Cache/Colbert_County_AL/ExtractDeltaComputerSystem_ColbertCounty_All/ExtractDeltaComputerSystem_ColbertCounty_Merge_All.csv";
            p = Runtime.getRuntime().exec("perl -ne 'print \"$. $_\" if m/[\\x80-\\xFF]/' "+file);
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((s = br.readLine()) != null)
                System.out.println("line: " + s);
            p.waitFor();
            System.out.println ("exit: " + p.exitValue());
            p.destroy();
        } catch (Exception e) {}*/
		
/*
 *  perl -ne 'print "$. $_" if m/[\x80-\xFF]/'  /home/glady/workspaces/Parcel_Cache/Colbert_County_AL/ExtractDeltaComputerSystem_ColbertCounty_All/ExtractDeltaComputerSystem_ColbertCounty_Merge_All.csv
 * 		
 */
		
		ProcessBuilder builder = new ProcessBuilder(
	            "bash", "-c", "perl -ne 'print \"\\$\\. \\$_\" if m\\/[\\\\x80-\\\\xFF]\\/'  /home/glady/workspaces/Parcel_Cache/County_Data/Madera_County_CA/Madera_County-CA_Parcels-All_Geo_Convert.csv");
	        builder.redirectErrorStream(true);
	        Process p = builder.start();
	        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
	        String line;
	        while (true) {
	            line = r.readLine();
	            if (line == null) { break; }
	            System.out.println(line);
	        } 
	}

}
