package com.shatam.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class USStates {

	@SuppressWarnings("unused")
	private static Map<String, String> fipsCountyMap = new HashMap<>();

	static{
		/* 
		 * To used 'fipsCountMap', order should maintain for creating the object, if not then we will not get desire output 
		 */
//		loadFipsCode();
	}
	
	
/*	private static void loadFipsCode(){
		List<String[]> readLines = FileUtil.readCsvFileWithoutHeader("/home/glady/workspaces/Parcel_Cache/Hazard_Zone/CountyFipsCode.csv");
		
		for(String lines[] : readLines){
			if(!lines[0].isEmpty() && lines[0].length() == 4)
				lines[0] = "0"+lines[0];
			
			if(!lines[0].isEmpty() && lines[0].length() == 5){
				fipsCountyMap.put(lines[0], lines[1]);
			}
		}

	}
	
	public static String getCountyName(String fipsCode){
		if(!fipsCode.isEmpty() && fipsCode.length() == 4)
			fipsCode = "0"+ fipsCode;
		return fipsCountyMap.get(fipsCode);
	}
*/	
	
    public static String abbr(String name){
        name = name.trim().toLowerCase();
        for (String objname: usaState.keySet()){
            String sname =objname.trim().toLowerCase();
            if (sname.equalsIgnoreCase(name)) return usaState.get(objname);
        }
        return null;
    }

    public static Set<String> getAllFull(){
        return usaState.keySet();
    }
    
    public static Collection<String> getAllAbbr(){
        return usaState.values();
    }
    
    public static String getStateFromFips(String fips) {
    	if(fips.length() == 1) fips = "0"+fips;
		return usaStateFips.get(fips);
    }
    
    public static HashMap<String, String> usaState = new HashMap<String, String>();
    static {
    	usaState.put("Southern California", "CA");
    	usaState.put("Northern California", "CA");
        usaState.put("Alabama", "AL");
        usaState.put("Alaska", "AK");
        usaState.put("Arizona", "AZ");
        usaState.put("Arkansas", "AR");
        usaState.put("California", "CA");
        usaState.put("Colorado", "CO");
        usaState.put("Connecticut", "CT");
        usaState.put("Delaware", "DE");
        usaState.put("Florida", "FL");
        usaState.put("Georgia", "GA");
        usaState.put("Hawaii", "HI");
        usaState.put("Idaho", "ID");
        usaState.put("Illinois", "IL");
        usaState.put("Indiana", "IN");
        usaState.put("Iowa", "IA");
        usaState.put("Kansas", "KS");
        usaState.put("Kentucky", "KY");
        usaState.put("Louisiana", "LA");
        usaState.put("Maine", "ME");
        usaState.put("Maryland", "MD");
        usaState.put("Massachusetts", "MA");
        usaState.put("Michigan", "MI");
        usaState.put("Minnesota", "MN");
        usaState.put("Mississippi", "MS");
        usaState.put("Missouri", "MO");
        usaState.put("Montana", "MT");
        usaState.put("Nebraska", "NE");
        usaState.put("Nevada", "NV");
        usaState.put("New Hampshire", "NH");
        usaState.put("New Jersey", "NJ");
        usaState.put("New Mexico", "NM");
        usaState.put("New York", "NY");
        usaState.put("North Carolina", "NC");
        usaState.put("North Dakota", "ND");
        usaState.put("Ohio", "OH");
        usaState.put("Oklahoma", "OK");
        usaState.put("Oregon", "OR");
        usaState.put("Pennsylvania", "PA");
        usaState.put("Rhode Island", "RI");
        usaState.put("South Carolina", "SC");
        usaState.put("South Dakota", "SD");
        usaState.put("Tennessee", "TN");
        usaState.put("Texas", "TX");
        usaState.put("Utah", "UT");
        usaState.put("Vermont", "VT");
        usaState.put("Virginia", "VA");
        usaState.put("Washington", "WA");
        usaState.put("West Virginia", "WV");
        usaState.put("Wisconsin", "WI");
        usaState.put("Wyoming", "WY");
        usaState.put("District Of Columbia", "DC");
        usaState.put("District of Columbia", "DC");
        usaState.put("American Samoa", "AS");
        usaState.put("Guam", "GU");
        usaState.put("Northern Mariana Islands", "MP");
        usaState.put("Puerto Rico", "PR");
        usaState.put("U.S. Virgin Islands", "VI");
        
    }
    
    public static HashMap<String, String> usaStateFips = new HashMap<String, String>();
    static {
		usaStateFips.put("01","AL");
		usaStateFips.put("02","AK");
		usaStateFips.put("04","AZ");
		usaStateFips.put("05","AR" );
		usaStateFips.put("06","CA");
		usaStateFips.put("08","CO");
		usaStateFips.put("09","CT");
		usaStateFips.put("10","DE");
		usaStateFips.put("11","DC");
		usaStateFips.put("12", "FL");
		usaStateFips.put("13", "GA");
		usaStateFips.put("15", "HI");
		usaStateFips.put("16", "ID");
		usaStateFips.put("17", "IL");
		usaStateFips.put("18", "IN");
		usaStateFips.put("19", "IA");
		usaStateFips.put("20", "KS");
		usaStateFips.put("21", "KY");
		usaStateFips.put("22", "LA");
		usaStateFips.put("23", "ME");
		usaStateFips.put("24", "MD");
		usaStateFips.put("25", "MA");
		usaStateFips.put("26", "MI");
		usaStateFips.put("27", "MN");
		usaStateFips.put("28", "MS");
		usaStateFips.put("29", "MO");
		usaStateFips.put("30", "MT");
		usaStateFips.put("31", "NE");
		usaStateFips.put("32", "NV");
		usaStateFips.put("33", "NH");
		usaStateFips.put("34", "NJ");
		usaStateFips.put("35", "NM");
		usaStateFips.put("36", "NY");
		usaStateFips.put("37", "NC");
		usaStateFips.put("38", "ND");
		usaStateFips.put("39", "OH");
		usaStateFips.put("40", "OK");
		usaStateFips.put("41", "OR");
		usaStateFips.put("42", "PA");
		usaStateFips.put("44", "RI");
		usaStateFips.put("45", "SC");
		usaStateFips.put("46", "SD");
		usaStateFips.put("47", "TN");
		usaStateFips.put("48", "TX");
		usaStateFips.put("49", "UT");
		usaStateFips.put("50", "VT");
		usaStateFips.put("51", "VA");
		usaStateFips.put("53", "WA");
		usaStateFips.put("54", "WV");
		usaStateFips.put("55", "WI");
		usaStateFips.put("56", "WY");
		usaStateFips.put("60", "AS");
		usaStateFips.put("66", "GU");
		usaStateFips.put("69", "MP");
		usaStateFips.put("72", "PR");
		usaStateFips.put("78", "VI");
        
    }
    
    
    private static HashMap<String, String> fipsStateMap = new HashMap<String, String>();
    static{
		fipsStateMap.put("AL", "01");
		fipsStateMap.put("AK", "02");
		fipsStateMap.put("AZ", "04");
		fipsStateMap.put("AR", "05");
		fipsStateMap.put("CA", "06");
		fipsStateMap.put("CO", "08");
		fipsStateMap.put("CT", "09");
		fipsStateMap.put("DE", "10");
		fipsStateMap.put("DC", "11");
		fipsStateMap.put("FL", "12");
		fipsStateMap.put("GA", "13");
		fipsStateMap.put("HI", "15");
		fipsStateMap.put("ID", "16");
		fipsStateMap.put("IL", "17");
		fipsStateMap.put("IN", "18");
		fipsStateMap.put("IA", "19");
		fipsStateMap.put("KS", "20");
		fipsStateMap.put("KY", "21");
		fipsStateMap.put("LA", "22");
		fipsStateMap.put("ME", "23");
		fipsStateMap.put("MD", "24");
		fipsStateMap.put("MA", "25");
		fipsStateMap.put("MI", "26");
		fipsStateMap.put("MN", "27");
		fipsStateMap.put("MS", "28");
		fipsStateMap.put("MO", "29");
		fipsStateMap.put("MT", "30");
		fipsStateMap.put("NE", "31");
		fipsStateMap.put("NV", "32");
		fipsStateMap.put("NH", "33");
		fipsStateMap.put("NJ", "34");
		fipsStateMap.put("NM", "35");
		fipsStateMap.put("NY", "36");
		fipsStateMap.put("NC", "37");
		fipsStateMap.put("ND", "38");
		fipsStateMap.put("OH", "39");
		fipsStateMap.put("OK", "40");
		fipsStateMap.put("OR", "41");
		fipsStateMap.put("PA", "42");
		fipsStateMap.put("RI", "44");
		fipsStateMap.put("SC", "45");
		fipsStateMap.put("SD", "46");
		fipsStateMap.put("TN", "47");
		fipsStateMap.put("TX", "48");
		fipsStateMap.put("UT", "49");
		fipsStateMap.put("VT", "50");
		fipsStateMap.put("VA", "51");
		fipsStateMap.put("WA", "53");
		fipsStateMap.put("WV", "54");
		fipsStateMap.put("WI", "55");
		fipsStateMap.put("WY", "56");
		fipsStateMap.put("AS", "60");
		fipsStateMap.put("GU", "66");
		fipsStateMap.put("MP", "69");
		fipsStateMap.put("PR", "72");
		fipsStateMap.put("VI", "78");
    }

    public static String getStateFipsCode(String stateAbbr){
    	return fipsStateMap.get(stateAbbr);
    }
}
