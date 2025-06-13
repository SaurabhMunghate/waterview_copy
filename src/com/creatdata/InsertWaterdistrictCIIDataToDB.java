package com.creatdata;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.shatam.utils.FileUtil;
import com.shatam.utils.U;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTReader;

public class InsertWaterdistrictCIIDataToDB {
	static HashSet<String> uniqueKey = new HashSet<>();
	static String baseDirPath="G:\\WaterDistrictData\\CII\\WVLOMALINDA\\";
	static String meterLocationFilePath = baseDirPath+"prd.meter_locations.csv";
	static String sourceAddressFilePath = baseDirPath+"prd.source_address.csv";
	static String billingAddressFilePath = baseDirPath+"prd.billing_address.csv";
	static String premiseFilePath = baseDirPath+"prd.premise_bounds.csv";
	static String consumptionFilePath = baseDirPath+"cii_consumption_eto.csv";
	static String waterDisrtictName = "WVLOMALINDA";
	static String codeBase = "CII";
	static boolean testFlag = true;
	public static void main(String[] args) throws ClassNotFoundException {
		String jdbcUrl = "jdbc:sqlite:DB/" + waterDisrtictName + "_" + codeBase + ".sqlite";
		Class.forName("org.sqlite.JDBC");
		try (Connection connection = DriverManager.getConnection(jdbcUrl)) {
			Statement statement = connection.createStatement();
			String createTableSQL = "CREATE TABLE premiseInfo ( premID TEXT PRIMARY KEY, Customer TEXT, Consumption REAL, Allocation REAL, percentAllocation REAL, LastMonth TEXT, T1A REAL, T1B REAL, T2A REAL, T2A_PP REAL, T2A_PP_RW REAL, T3A REAL, T3A_APR REAL, T3A_APR_RW REAL, T3A_CG REAL,  T3A_CG_RW REAL, T3A_RW REAL, T3A_ES REAL, T3A_ES_RW REAL, T3A_CE REAL, T3A_CE_RW REAL, T3B REAL, T3B_APR REAL, T3B_APR_RW REAL, T3B_CG REAL, T3B_CG_RW REAL, T3B_RW REAL, T3B_ES REAL,  T3B_ES_RW REAL, T3B_CE REAL, T3B_CE_RW REAL, T4A REAL, T4A_APR REAL, T4A_APR_RW REAL, T4A_CG REAL, T4A_CG_RW REAL, T4A_RW REAL, T4A_ES REAL, T4A_ES_RW REAL, T4A_CE REAL, T4A_CE_RW REAL,    T4B REAL, T4B_APR REAL, T4B_APR_RW REAL, T4B_CG REAL, T4B_CG_RW REAL, T4B_RW REAL, T4B_ES REAL, T4B_ES_RW REAL, T4B_CE REAL, T4B_CE_RW REAL, T5A REAL, T6A REAL, T7A REAL, T7A_RW REAL,    T8A REAL, T8A_RW REAL, T8A_SW REAL, T8A_SW_RW REAL, T9A REAL, T10A REAL, T10A_RW REAL, I REAL, I_SLA REAL, I_MWELO TEXT, INI 	REAL, INI_SLA REAL, NI REAL, CorrArea REAL, ImagYear TEXT, StartDate TEXT, EndDate TEXT, LoadDate TEXT)";
			String createMetersTableSQL = "CREATE TABLE meters(premID TEXT, MeterID TEXT, BillingCompany TEXT, BillingName TEXT, BillingAddress TEXT, BillingCity TEXT, BillingState TEXT, BillingZipCode TEXT, Phone TEXT, Email  TEXT, Customer TEXT, MeterAddress TEXT, MeterCity TEXT, MeterState TEXT, MeterZipCode TEXT,LAND_USE_GRP TEXT, LAND_USE_DTL TEXT, MeterSize TEXT, IndClass TEXT, MeterType TEXT, Exempt TEXT, WaterType TEXT)";
			String consumptionTableSQL = "CREATE TABLE Consumption_Table (premID TEXT, ReadingDate TEXT, Allocation REAL,Consumption REAL, Per_Allocation REAL)";
			String uniqueIndex = "CREATE UNIQUE INDEX metersIndex ON meters(premID, MeterID)";
			String unique2Index = "CREATE UNIQUE INDEX ConsumptionIndex ON Consumption_Table(premID, ReadingDate)";
			String createPolygonTableSQL = "CREATE TABLE premiseGeomData (premID TEXT PRIMARY KEY, the_geom GEOMETRY)";
			HashMap<String, String[]> ConsumptionList = new HashMap<String, String[]>();
			if (testFlag) {
				statement.executeUpdate(createTableSQL);
				statement.executeUpdate(createMetersTableSQL);
				statement.executeUpdate(uniqueIndex);
				statement.executeUpdate(consumptionTableSQL);
				statement.executeUpdate(unique2Index);
				statement.executeUpdate(createPolygonTableSQL);
			}
			List<String[]> parcelInfoList = new ArrayList<String[]>();
			List<String[]> metersList = new ArrayList<String[]>();
			// HashMap<String,String[]> ConsumptionList=new HashMap<String,String[]>();

			HashMap<String, String> meterApnMap = new HashMap<String, String>();
			HashMap<String, String[]> apnSourceAddressMap = new HashMap<String, String[]>();
			HashMap<String, String[]> apnbillingAddressMap = new HashMap<String, String[]>();
			HashMap<String, String> apnParcelMap = new HashMap<String, String>();
			List<String[]> meterLocationData = FileUtil.readCsvFile(meterLocationFilePath);
			// 0=> MeterID, 1=> APN
			// break;
			for (String[] meterLocation : meterLocationData) {
				if (meterLocation[0].contains("MeterId"))
					continue;
				meterApnMap.put(meterLocation[1], meterLocation[0]);
			}
			System.out.println("Meter Map"+ meterApnMap.size());
			List<String[]> sourceAddressData = FileUtil.readCsvFile(sourceAddressFilePath);
			// 0=> meterID, 1=> Customer, 2=> MeterAddress, 3=> MeterCity, 4=> MeterState,
			// 5=> MeterZipCode, 6=> LAND_USE_GRP, 7=> LAND_USE_DTL, 8=> MeterSize, 9=>
			// IndClass, 10=> MeterType, 11=> Exempt, 12=> WaterType
			
			//0=> meterID, 1=> Customer, 2=> MeterAddress, 3=> MeterCity, 4=> MeterState
			//5=> MeterZipCode, NA=> LAND_USE_GRP, NA=> LAND_USE_DTL, 6=> MeterSize, 7=>IndClass
			//, 8=> MeterType, 9=> Exempt, 10=> WaterType,	StartDate	EndDate	LoadDate	Source	modifiedby	DistName	OBJECTID


			for (String[] sourceAddress : sourceAddressData) {
				if (meterApnMap.containsKey(sourceAddress[0])) {
					String[] sourceAddressArr = { sourceAddress[1], sourceAddress[2], sourceAddress[3],
							sourceAddress[4], sourceAddress[5], "nan", "nan", sourceAddress[6],
							sourceAddress[7], sourceAddress[8], sourceAddress[9], sourceAddress[10] };
					apnSourceAddressMap.put(meterApnMap.get(sourceAddress[0]), sourceAddressArr);
				}
				// break;
			}
			List<String[]> billingAddressData = FileUtil.readCsvFile(billingAddressFilePath);
			// 0=> meterID, 1=> BillingCompany, 2=> BillingName, 3=> BillingAddress, 4=>
			// BillingCity, 5=> BillingState, 6=> BillingZipCode, 7=> Phone, 8=> Email

			for (String[] billingAddress : billingAddressData) {
				if (meterApnMap.containsKey(billingAddress[0])) {
					String[] billingAddressArr = { billingAddress[1], billingAddress[2], billingAddress[3],
							billingAddress[4], billingAddress[5], billingAddress[6], billingAddress[7],
							billingAddress[8] };
					apnbillingAddressMap.put(meterApnMap.get(billingAddress[0]), billingAddressArr);
				}
				// break;
			}
			for (String meterID : meterApnMap.keySet()) {
				String apn = meterApnMap.get(meterID);
				if (apn.contains("APN")||apn.contains("premID"))
					continue;
				String billingAddressArr[] = apnbillingAddressMap.get(apn);
				String sourceAddressArr[] = apnSourceAddressMap.get(apn);
				if (sourceAddressArr == null) {
					sourceAddressArr = new String[] { "", "", "", "", "", "", "", "", "", "", "", "" };
				}
				if (billingAddressArr == null) {
					billingAddressArr = new String[] { "", "", "", "", "", "", "", "", "" };
				}
				String outArr[] = { apn.trim(), meterID.trim(), billingAddressArr[0].trim(),
						billingAddressArr[1].trim(), billingAddressArr[2].trim(), billingAddressArr[3].trim(),
						billingAddressArr[4].trim(), billingAddressArr[5].trim(), billingAddressArr[6].trim(),
						billingAddressArr[7].trim(), sourceAddressArr[0].trim(), sourceAddressArr[1].trim(),
						sourceAddressArr[2].trim(), sourceAddressArr[3].trim(), sourceAddressArr[4].trim(),
						sourceAddressArr[5].trim(), sourceAddressArr[6].trim(), sourceAddressArr[7].trim(),
						sourceAddressArr[8].trim(), sourceAddressArr[9].trim(), sourceAddressArr[10].trim(),
						sourceAddressArr[11].trim() };
				String[] updatedArray = Arrays.stream(outArr)
						.map(s -> s.equals("NA") || s.equals("NAN") || s.equals("nan") || s.equals("")|| s.equals("null")|| s.equals("Null") ? null : s)
						.toArray(String[]::new);
				metersList.add(updatedArray);
				// break;
			}
			List<String[]> parcelData = FileUtil.readCsvFile(premiseFilePath);
			for (String[] parcel : parcelData) {
				if (parcel[0].contains("premID")) {
					//System.out.println(Arrays.toString(parcel));
					continue;
				}
				
				
				String[] outArr = { parcel[0],parcel[1], "", "", "", "", parcel[2], parcel[3], parcel[4], parcel[5], parcel[6],parcel[7],parcel[8],parcel[9], parcel[10],parcel[11],parcel[12],parcel[13],parcel[14], parcel[15],parcel[16],parcel[17], parcel[18], parcel[19],parcel[20],parcel[21],parcel[22], parcel[23],parcel[24],parcel[25],parcel[26],parcel[27], parcel[28],parcel[29],parcel[30],parcel[31],parcel[32],parcel[33],parcel[34], parcel[35],parcel[36],parcel[37], parcel[38], parcel[39],parcel[40],parcel[41],parcel[42], parcel[43],parcel[44],parcel[45],parcel[46],parcel[47], parcel[48],parcel[49],parcel[50], parcel[51], parcel[52],parcel[53],parcel[54],parcel[55], parcel[56],parcel[57],parcel[58],parcel[59],parcel[60], parcel[61],parcel[62],parcel[63],parcel[64],parcel[65],parcel[66],parcel[67],parcel[68]};
				apnParcelMap.put(parcel[0], parcel[71]);
				//String[] outArr= {newLine[71],newLine[0],newLine[58],newLine[59],newLine[61],newLine[62],newLine[63],""};
				//premID	Customer	T1A	T1B	T2A	T2A_PP	T2A_RW	T3A	T3A_APR	T3A_APR_RW	T3A_CG	T3A_CG_RW	T3A_RW	T3A_ES	T3A_ES_RW	T3A_CE	T3A_CE_RW	T3B	T3B_APR	T3B_APR_RW	T3B_CG	T3B_CG_RW	T3B_RW	T3B_ES
				// 0         1           2   3   4   5       6       7  8        9           10       11         12      13      14           15      16         17 18        19          20      21         22      23   
				
				//T3B_ES_RW	T3B_CE	T3B_CE_RW	T4A	T4A_APR	T4A_APR_RW	T4A_CG	T4A_CG_RW	T4A_RW	T4A_ES	T4A_ES_RW	T4A_CE	T4A_CE_RW	T4B	T4B_APR	T4B_APR_RW	T4B_CG	T4B_CG_RW	T4B_RW	T4B_ES	T4B_ES_RW	T4B_CE	T4B_CE_RW	T5A	T6A	T7A	T7A_RW	T8A	T8A_RW	T8A_SW	T8A_SW_RW	T9A	T10A	T10A_RW	I	I_SLA	I_MWELO	INI	INI_SLA	NI	CorrArea	ImagYear	StartDate	EndDate	LoadDate	Source	modifiedby	the_geom	DistName	OBJECTID
				//   24       25      26        27    28       29         30      31          32     33       34         35       36        37    38     39          40        41        42       43      44         45        46        47  48  49   50     51  52      53        54        55  56       57    58   59      60          61     62   63          64           65          66    67          68         69        70          71
				
				//premID TEXT PRIMARY KEY, Customer TEXT, Consumption, Allocation,percentAlloction, LastMonth, T1A REAL, T1B REAL, T2A REAL, T2A_PP REAL, T2A_PP_RW REAL, T3A REAL, T3A_APR REAL, T3A_APR_RW REAL, T3A_CG REAL,  T3A_CG_RW REAL, T3A_RW REAL, T3A_ES REAL, T3A_ES_RW REAL, T3A_CE REAL, T3A_CE_RW REAL, T3B REAL, T3B_APR REAL, T3B_APR_RW REAL, T3B_CG REAL, T3B_CG_RW REAL, T3B_RW REAL, T3B_ES REAL,  T3B_ES_RW REAL, T3B_CE REAL, T3B_CE_RW REAL, T4A REAL, T4A_APR REAL, T4A_APR_RW REAL, T4A_CG REAL, T4A_CG_RW REAL, T4A_RW REAL, T4A_ES REAL, T4A_ES_RW REAL, T4A_CE REAL, T4A_CE_RW REAL,    T4B REAL, T4B_APR REAL, T4B_APR_RW REAL, T4B_CG REAL, T4B_CG_RW REAL, T4B_RW REAL, T4B_ES REAL, T4B_ES_RW REAL, T4B_CE REAL, T4B_CE_RW REAL, T5A REAL, T6A REAL, T7A REAL, T7A_RW REAL,    T8A REAL, T8A_RW REAL, T8A_SW REAL, T8A_SW_RW REAL, T9A REAL, T10A REAL, T10A_RW REAL, I REAL, I_SLA REAL, I_MWELO TEXT, INI 	REAL, INI_SLA REAL, NI REAL, CorrArea REAL, ImagYear TEXT, StartDate TEXT, EndDate TEXT, LoadDate TEXT

				// APN TEXT PRIMARY KEY,CONSUMPTION REAL,ALLOCATION REAL,LASTMONTH TEXT,percentAllocation,TOTAL_AREA REAL, I_AREA REAL, II_AREA REAL, INI_100 REAL, INI_AREA REAL, TOTAL_NI REAL, MED_HH_SIZE REAL, No_OF_UNITS REAL, ImageYear INTEGER, StartDate TEXT,EndDate TEXT
				// 1 					2 					3 				4 			5					 6				 7 				8 			9 				10 				11 			12 					13 					14 				15			16	
				//U.log(outArr.length);
				//System.out.println(Arrays.toString(outArr));
				parcelInfoList.add(outArr);
				//break;
			}
			
			BufferedReader reader = new BufferedReader(new FileReader(consumptionFilePath));

			String line = null;
			int counteer = 0;
			while ((line = reader.readLine()) != null) {
				if (line.contains("premID"))
					continue;
				counteer++;
				String nextLine[] = line.split(",");
				String key = nextLine[2].replace("\"", "") + nextLine[0].replace("\"", "");
				if (ConsumptionList.containsKey(key)) {
					String outArr[] = ConsumptionList.get(key);
					Double consumption = Double.parseDouble(nextLine[4].replace("\"", "")) + Double.parseDouble(outArr[3]);
					outArr[3] = consumption + "";
					outArr[4] = (Double.parseDouble(outArr[2]) / Double.parseDouble(outArr[3])) * 100 + "";
					ConsumptionList.put(key, outArr);
				} else {
					//APN, READING, ALLOCATION,CONSUMPTION, PER_ALLOCATION
					String outArr[] = { nextLine[2].replace("\"", ""), nextLine[0].replace("\"", ""),nextLine[3].replace("\"", ""), nextLine[4].replace("\"", ""),"" + (Double.parseDouble(nextLine[4].replace("\"", ""))/Double.parseDouble(nextLine[3].replace("\"", ""))) * 100 };
					ConsumptionList.put(key, outArr);
				}
			}
			reader.close();
			
			String insertPointSQL = "INSERT INTO premiseInfo VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			if (testFlag) {
				PreparedStatement preparedStatement = connection.prepareStatement(insertPointSQL);
				int count = 0;
				connection.setAutoCommit(false);
				for (String[] parcel : parcelInfoList) {
					int paramIndex = 1;
					if (!uniqueKey.add(parcel[0]))
						continue;
					////premID	Customer	T1A	T1B	T2A	T2A_PP	T2A_RW	T3A	T3A_APR	T3A_APR_RW	T3A_CG	T3A_CG_RW	T3A_RW	T3A_ES	T3A_ES_RW	T3A_CE	T3A_CE_RW	T3B	T3B_APR	T3B_APR_RW	T3B_CG	T3B_CG_RW	T3B_RW	T3B_ES
					// 0         1           2   3   4   5       6       7  8        9           10       11         12      13      14           15      16         17 18        19          20      21         22      23   
					
					//T3B_ES_RW	T3B_CE	T3B_CE_RW	T4A	T4A_APR	T4A_APR_RW	T4A_CG	T4A_CG_RW	T4A_RW	T4A_ES	T4A_ES_RW	T4A_CE	T4A_CE_RW	T4B	T4B_APR	T4B_APR_RW	T4B_CG	T4B_CG_RW	T4B_RW	T4B_ES	T4B_ES_RW	T4B_CE	T4B_CE_RW	T5A	T6A	T7A	T7A_RW	T8A	T8A_RW	T8A_SW	T8A_SW_RW	T9A	T10A	T10A_RW	I	I_SLA	I_MWELO	INI	INI_SLA	NI	CorrArea	ImagYear	StartDate	EndDate	LoadDate	Source	modifiedby	the_geom	DistName	OBJECTID
					//   24       25      26        27    28       29         30      31          32     33       34         35       36        37    38     39          40        41        42       43      44         45        46        47  48  49   50     51  52      53        54        55  56       57    58   59      60          61     62   63          64           65          66    67          68         69        70          71
					
					//{ parcel[0],parcel[1], "", "", "", "", parcel[2], parcel[3], parcel[4], parcel[5], parcel[6],parcel[7],parcel[8],parcel[9], parcel[10],parcel[11],parcel[12],parcel[13],parcel[14], parcel[15],parcel[16],parcel[17], parcel[18], parcel[19],parcel[20],parcel[21],parcel[22], parcel[23],parcel[24],parcel[25],parcel[26],parcel[27], parcel[28],parcel[29],parcel[30],parcel[31],parcel[32],parcel[33],parcel[34], parcel[35],parcel[36],parcel[37], parcel[38], parcel[39],parcel[40],parcel[41],parcel[42], parcel[43],parcel[44],parcel[45],parcel[46],parcel[47], parcel[48],parcel[49],parcel[50], parcel[51], parcel[52],parcel[53],parcel[54],parcel[55], parcel[56],parcel[57],parcel[58],parcel[59],parcel[60], parcel[61],parcel[62],parcel[63],parcel[64],parcel[65],parcel[66],parcel[67]};
					//		0			1	 2   3   4   5    6T1A        7T1B       8T2A       9T2A_PP      10      
					preparedStatement.setString(paramIndex++, parcel[0]);// premID
					preparedStatement.setString(paramIndex++, parcel[1]);// Customer
					preparedStatement.setDouble(paramIndex++,
							parcel[2].length() > 0 ? Double.parseDouble(parcel[2]) : 0.0);// Consumption
					preparedStatement.setDouble(paramIndex++,
							parcel[3].length() > 0 ? Double.parseDouble(parcel[3]) : 0.0);// ALLOCATION
					preparedStatement.setDouble(paramIndex++,
							parcel[4].length() > 0 ? Double.parseDouble(parcel[4]) : 0.0);// percentAlloction
					preparedStatement.setString(paramIndex++, parcel[5]);// LASTMONTH
					preparedStatement.setDouble(paramIndex++,
							parcel[6].length() > 0 ? Double.parseDouble(parcel[6]) : 0.0);// T1A
					preparedStatement.setDouble(paramIndex++,
							parcel[7].length() > 0 ? Double.parseDouble(parcel[7]) : 0.0);// T1B
					preparedStatement.setDouble(paramIndex++,
							parcel[8].length() > 0 ? Double.parseDouble(parcel[8]) : 0.0);// T2A
					preparedStatement.setDouble(paramIndex++,
							parcel[9].length() > 0 ? Double.parseDouble(parcel[9]) : 0.0);// T2A_PP
					preparedStatement.setDouble(paramIndex++,
							parcel[10].length() > 0 ? Double.parseDouble(parcel[10]) : 0.0);// T2A_RW
					preparedStatement.setDouble(paramIndex++,
							parcel[11].length() > 0 ? Double.parseDouble(parcel[11]) : 0.0);// T3A
					preparedStatement.setDouble(paramIndex++,
							parcel[12].length() > 0 ? Double.parseDouble(parcel[12]) : 0.0);// T3A_APR
					preparedStatement.setDouble(paramIndex++,
							parcel[13].length() > 0 ? Double.parseDouble(parcel[13]) : 0.0);// T3A_APR_RW
					preparedStatement.setDouble(paramIndex++,
							parcel[14].length() > 0 ? Double.parseDouble(parcel[14]) : 0.0);// T3A_CG
					preparedStatement.setDouble(paramIndex++,
							parcel[15].length() > 0 ? Double.parseDouble(parcel[15]) : 0.0);// T3A_CG_RW
					preparedStatement.setDouble(paramIndex++,
							parcel[16].length() > 0 ? Double.parseDouble(parcel[16]) : 0.0);// T3A_RW
					preparedStatement.setDouble(paramIndex++,
							parcel[17].length() > 0 ? Double.parseDouble(parcel[17]) : 0.0);// T3A_ES
					preparedStatement.setDouble(paramIndex++,
							parcel[18].length() > 0 ? Double.parseDouble(parcel[18]) : 0.0);// T3A_ES_RW
					preparedStatement.setDouble(paramIndex++,
							parcel[19].length() > 0 ? Double.parseDouble(parcel[19]) : 0.0);// T3A_CE
					preparedStatement.setDouble(paramIndex++,
							parcel[20].length() > 0 ? Double.parseDouble(parcel[20]) : 0.0);// T3A_CE_RW
					preparedStatement.setDouble(paramIndex++,
							parcel[21].length() > 0 ? Double.parseDouble(parcel[21]) : 0.0);// T3B
					preparedStatement.setDouble(paramIndex++,
							parcel[22].length() > 0 ? Double.parseDouble(parcel[22]) : 0.0);// T3B_APR
					preparedStatement.setDouble(paramIndex++,
							parcel[23].length() > 0 ? Double.parseDouble(parcel[23]) : 0.0);// T3B_APR_RW
					preparedStatement.setDouble(paramIndex++,
							parcel[24].length() > 0 ? Double.parseDouble(parcel[24]) : 0.0);// T3B_CG_RW
					preparedStatement.setDouble(paramIndex++,
							parcel[25].length() > 0 ? Double.parseDouble(parcel[25]) : 0.0);// T3B_RW
					preparedStatement.setDouble(paramIndex++,
							parcel[26].length() > 0 ? Double.parseDouble(parcel[26]) : 0.0);// T3B_ES
					preparedStatement.setDouble(paramIndex++,
							parcel[27].length() > 0 ? Double.parseDouble(parcel[27]) : 0.0);// T3B_ES_RW
					preparedStatement.setDouble(paramIndex++,
							parcel[28].length() > 0 ? Double.parseDouble(parcel[28]) : 0.0);// T3B_CE
					preparedStatement.setDouble(paramIndex++,
							parcel[29].length() > 0 ? Double.parseDouble(parcel[29]) : 0.0);// T3B_CE_RW
					preparedStatement.setDouble(paramIndex++,
							parcel[30].length() > 0 ? Double.parseDouble(parcel[30]) : 0.0);// T4A
					preparedStatement.setDouble(paramIndex++,
							parcel[31].length() > 0 ? Double.parseDouble(parcel[31]) : 0.0);// T4A_APR
					preparedStatement.setDouble(paramIndex++,
							parcel[32].length() > 0 ? Double.parseDouble(parcel[32]) : 0.0);// T4A_APR_RW
					preparedStatement.setDouble(paramIndex++,
							parcel[33].length() > 0 ? Double.parseDouble(parcel[33]) : 0.0);// T4A_CG
					preparedStatement.setDouble(paramIndex++,
							parcel[34].length() > 0 ? Double.parseDouble(parcel[34]) : 0.0);// T4A_CG_RW
					preparedStatement.setDouble(paramIndex++,
							parcel[35].length() > 0 ? Double.parseDouble(parcel[35]) : 0.0);// T4A_RW
					preparedStatement.setDouble(paramIndex++,
							parcel[36].length() > 0 ? Double.parseDouble(parcel[36]) : 0.0);// T4A_ES
					preparedStatement.setDouble(paramIndex++,
							parcel[37].length() > 0 ? Double.parseDouble(parcel[37]) : 0.0);// T4A_ES_RW
					preparedStatement.setDouble(paramIndex++,
							parcel[38].length() > 0 ? Double.parseDouble(parcel[38]) : 0.0);// T4A_CE
					preparedStatement.setDouble(paramIndex++,
							parcel[39].length() > 0 ? Double.parseDouble(parcel[39]) : 0.0);// T4A_CE_RW
					preparedStatement.setDouble(paramIndex++,
							parcel[40].length() > 0 ? Double.parseDouble(parcel[40]) : 0.0);// T4B
					preparedStatement.setDouble(paramIndex++,
							parcel[41].length() > 0 ? Double.parseDouble(parcel[41]) : 0.0);// T4B_APR
					preparedStatement.setDouble(paramIndex++,
							parcel[42].length() > 0 ? Double.parseDouble(parcel[42]) : 0.0);// T4B_APR_RW
					preparedStatement.setDouble(paramIndex++,
							parcel[43].length() > 0 ? Double.parseDouble(parcel[43]) : 0.0);// T4B_CG
					preparedStatement.setDouble(paramIndex++,
							parcel[44].length() > 0 ? Double.parseDouble(parcel[44]) : 0.0);// T4B_CG_RW
					preparedStatement.setDouble(paramIndex++,
							parcel[45].length() > 0 ? Double.parseDouble(parcel[45]) : 0.0);// T4B_RW
					preparedStatement.setDouble(paramIndex++,
							parcel[46].length() > 0 ? Double.parseDouble(parcel[46]) : 0.0);// T4B_ES
					preparedStatement.setDouble(paramIndex++,
							parcel[47].length() > 0 ? Double.parseDouble(parcel[47]) : 0.0);// T4B_ES_RW
					preparedStatement.setDouble(paramIndex++,
							parcel[48].length() > 0 ? Double.parseDouble(parcel[48]) : 0.0);// T4B_CE
					preparedStatement.setDouble(paramIndex++,
							parcel[49].length() > 0 ? Double.parseDouble(parcel[49]) : 0.0);// T4B_CE_RW
					preparedStatement.setDouble(paramIndex++,
							parcel[50].length() > 0 ? Double.parseDouble(parcel[50]) : 0.0);// T5A
					preparedStatement.setDouble(paramIndex++,
							parcel[51].length() > 0 ? Double.parseDouble(parcel[51]) : 0.0);// T6A
					preparedStatement.setDouble(paramIndex++,
							parcel[52].length() > 0 ? Double.parseDouble(parcel[52]) : 0.0);// T7A
					preparedStatement.setDouble(paramIndex++,
							parcel[53].length() > 0 ? Double.parseDouble(parcel[53]) : 0.0);// T7A_RW
					preparedStatement.setDouble(paramIndex++,
							parcel[54].length() > 0 ? Double.parseDouble(parcel[54]) : 0.0);// T8A
					preparedStatement.setDouble(paramIndex++,
							parcel[55].length() > 0 ? Double.parseDouble(parcel[55]) : 0.0);// T8A_RW
					preparedStatement.setDouble(paramIndex++,
							parcel[56].length() > 0 ? Double.parseDouble(parcel[56]) : 0.0);// T8A_SW
					preparedStatement.setDouble(paramIndex++,
							parcel[57].length() > 0 ? Double.parseDouble(parcel[57]) : 0.0);// T8A_SW_RW
					preparedStatement.setDouble(paramIndex++,
							parcel[58].length() > 0 ? Double.parseDouble(parcel[58]) : 0.0);// T9A
					preparedStatement.setDouble(paramIndex++,
							parcel[59].length() > 0 ? Double.parseDouble(parcel[59]) : 0.0);// T10A
					preparedStatement.setDouble(paramIndex++,
							parcel[60].length() > 0 ? Double.parseDouble(parcel[60]) : 0.0);// T10A_RW
					preparedStatement.setDouble(paramIndex++,
							parcel[61].length() > 0 ? Double.parseDouble(parcel[61]) : 0.0);// I
					preparedStatement.setDouble(paramIndex++,
							parcel[62].length() > 0 ? Double.parseDouble(parcel[62]) : 0.0);// I_SLA
					preparedStatement.setDouble(paramIndex++,
							parcel[63].length() > 0 ? Double.parseDouble(parcel[63]) : 0.0);// I_MWELO
					preparedStatement.setDouble(paramIndex++,
							parcel[64].length() > 0 ? Double.parseDouble(parcel[64]) : 0.0);// INI
					preparedStatement.setDouble(paramIndex++,
							parcel[65].length() > 0 ? Double.parseDouble(parcel[65]) : 0.0);// INI_SLA
					preparedStatement.setDouble(paramIndex++,
							parcel[66].length() > 0 ? Double.parseDouble(parcel[66]) : 0.0);// NI
					preparedStatement.setDouble(paramIndex++,
							parcel[67].length() > 0 ? Double.parseDouble(parcel[67]) : 0.0);// CorrArea
					preparedStatement.setDouble(paramIndex++,
							parcel[68].length() > 0 ? Double.parseDouble(parcel[68]) : 0.0);// CorrArea
					//
					preparedStatement.setInt(paramIndex++, parcel[69].length() > 0 ? Integer.parseInt(parcel[69]) : 0);// ImageYear
					preparedStatement.setString(paramIndex++, parcel[70]);// StartDate
					preparedStatement.setString(paramIndex++, parcel[71]);// EndDate
					preparedStatement.setString(paramIndex++, parcel[72]);// Load Date
					// preparedStatement.setString(paramIndex++, parcel[15]);
					preparedStatement.addBatch();
					if (count++ % 20000 == 0) {
						int[] result = preparedStatement.executeBatch();
						if (result.length == 20000) {
							System.out.println("Data inserted successfully. " + count + " - " + result.length);
							connection.commit();
						}
					}
				}
				
				int[] result = preparedStatement.executeBatch();
				System.out.println("Data inserted successfully. " + count + " - " + result.length);
				connection.commit();

				insertPointSQL = "INSERT INTO meters VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				// meters(APN TEXT, MeterID TEXT, BillingCompany TEXT, BillingName TEXT,
				// BillingAddress TEXT, BillingCity TEXT, BillingState TEXT, BillingZipCode
				// TEXT, Phone TEXT, Email TEXT, Customer TEXT, MeterAddress TEXT, MeterCity
				// TEXT, MeterState TEXT, MeterZipCode TEXT,
				// LAND_USE_GRP TEXT, LAND_USE_DTL TEXT, MeterSize TEXT, IndClass TEXT,
				// MeterType TEXT, Exempt TEXT, WaterType TEXT)";
				preparedStatement = connection.prepareStatement(insertPointSQL);
				count = 0;
				connection.setAutoCommit(false);
				for (String[] meter : metersList) {
					// U.log(Arrays.toString(meter));
					if (meter[0] == null || meter[0].trim().length() == 0)
						continue;
					int paramIndex = 1;
					preparedStatement.setString(paramIndex++, meter[0]);// APN
					preparedStatement.setString(paramIndex++, meter[1]);// MeterID
					preparedStatement.setString(paramIndex++, meter[2]);// BillingCompany
					preparedStatement.setString(paramIndex++, meter[3]);// BillingName
					preparedStatement.setString(paramIndex++, meter[4]);// BillingAddress
					preparedStatement.setString(paramIndex++, meter[5]);// BillingCity
					preparedStatement.setString(paramIndex++, meter[6]);// BillingState
					preparedStatement.setString(paramIndex++, meter[7]);// BillingZipCode
					preparedStatement.setString(paramIndex++, meter[8]);// Phone
					preparedStatement.setString(paramIndex++, meter[9]);// Email
					preparedStatement.setString(paramIndex++, meter[10]);// Customer
					preparedStatement.setString(paramIndex++, meter[11]);// MeterAddress
					preparedStatement.setString(paramIndex++, meter[12]);// MeterCity
					preparedStatement.setString(paramIndex++, meter[13]);// MeterState
					preparedStatement.setString(paramIndex++, meter[14]);// MeterZipCode
					preparedStatement.setString(paramIndex++, meter[15]);// LAND_USE_GRP
					preparedStatement.setString(paramIndex++, meter[16]);// LAND_USE_DTL
					preparedStatement.setString(paramIndex++, meter[17]);// MeterSize
					preparedStatement.setString(paramIndex++, meter[18]);// IndClass
					preparedStatement.setString(paramIndex++, meter[19]);// MeterType
					preparedStatement.setString(paramIndex++, meter[20]);// Exempt
					preparedStatement.setString(paramIndex++, meter[21]);// WaterType
					preparedStatement.addBatch();
					if (count++ % 20000 == 0) {
						result = preparedStatement.executeBatch();
						if (result.length == 20000) {
							System.out.println("Data inserted successfully. " + count + " - " + result.length);
							connection.commit();
						}
					}
				}
				result = preparedStatement.executeBatch();
				System.out.println("Data inserted successfully. " + count + " - " + result.length);
				connection.commit();

				// APN TEXT, ReadingDate TEXT, Allocation REAL,Consumption REAL, Per_Allocation
				// REAL
				insertPointSQL = "INSERT INTO Consumption_Table VALUES (?, ?, ?, ?, ?)";
				preparedStatement = connection.prepareStatement(insertPointSQL);
		
				count = 0;
				for (String keys : ConsumptionList.keySet()) {
					String[] value = ConsumptionList.get(keys);
					// U.log(Arrays.toString(value));
					int paramIndex = 1;
					preparedStatement.setString(paramIndex++, value[0]);// APN
					preparedStatement.setString(paramIndex++, value[1]);// ReadingDate
					preparedStatement.setDouble(paramIndex++, value[2].length() > 0 ? Double.parseDouble(value[2]) : 0.0);// Allocation
					preparedStatement.setDouble(paramIndex++, value[3].length() > 0 ? Double.parseDouble(value[3]) : 0.0);// Consumption
					preparedStatement.setDouble(paramIndex++, value[4].length() > 0 ? Double.parseDouble(value[4]) : 0.0);// Per_Allocation

					preparedStatement.addBatch();
					if (count++ % 40000 == 0) {
						result = preparedStatement.executeBatch();
						if (result.length == 40000) {
							System.out.println("Data inserted successfully. " + count + " - " + result.length);
							connection.commit();
						}
					}
					// break;
				}
				result = preparedStatement.executeBatch();
				System.out.println("Data inserted successfully. " + count + " - " + result.length);
				String insertGeomData="INSERT INTO premiseGeomData VALUES (?, ?)";
				preparedStatement = connection.prepareStatement(insertGeomData);
				count = 0;
				connection.setAutoCommit(false);
				 WKTReader wktReader = new WKTReader();
				for (String apn : apnParcelMap.keySet()) {
					int paramIndex = 1;
					Geometry geometry = wktReader.read(apnParcelMap.get(apn));
					Geometry reducedPrecisionGeometry = reducePrecision(geometry, 6);
					preparedStatement.setString(paramIndex++, apn);// APN
					preparedStatement.setString(paramIndex++, reducedPrecisionGeometry.toString());// the_geom
					preparedStatement.addBatch();
					if (count++ % 20000 == 0) {
						result = preparedStatement.executeBatch();
						if (result.length == 20000) {
							System.out.println("Data inserted successfully. " + count + " - " + result.length);
							connection.commit();
						}
					}
				}
				result = preparedStatement.executeBatch();
				System.out.println("Data inserted successfully. " + count + " - " + result.length);
				connection.commit();
				String updateSQL = "UPDATE PremiseInfo SET LASTMONTH = (SELECT MAX(ReadingDate) AS LASTMONTH FROM (SELECT ReadingDate FROM consumption_table WHERE consumption_table.premID = PremiseInfo.premID ORDER BY ReadingDate DESC LIMIT 1) AS subquery), ALLOCATION = (SELECT Allocation AS Allocation FROM (SELECT Allocation FROM consumption_table WHERE consumption_table.premID = PremiseInfo.premID ORDER BY ReadingDate DESC LIMIT 1) AS subquery), CONSUMPTION = (SELECT Consumption AS Consumption FROM (SELECT Consumption FROM consumption_table WHERE consumption_table.premID = PremiseInfo.premID ORDER BY ReadingDate DESC LIMIT 1) AS subquery)";
				statement.executeUpdate(updateSQL);
				connection.commit();
				updateSQL = "update PremiseInfo SET percentAllocation=(CONSUMPTION/ALLOCATION)*100;";
				statement.executeUpdate(updateSQL);
				connection.commit();
				updateSQL = "update Consumption_Table SET PER_ALLOCATION=(CONSUMPTION/ALLOCATION)*100;";
				statement.executeUpdate(updateSQL);
				connection.commit();
				//MergeAllLayersCII layers=new MergeAllLayersCII();
				GeoPackageCreator creator=new GeoPackageCreator(baseDirPath, waterDisrtictName);
				//String baseDirPath = "G:\\WaterDistrictData\\Data_Folder_2024-10-08\\CII\\"+waterdistrictName+"\\";
				ArrayList<String> fileList= creator.listFilesInDirectory(baseDirPath);
				ArrayList<String[]> landscapeData=creator.processLanadscapeFiles(fileList);
				ArrayList<String[]> meterData=creator.processMeterFile(meterLocationFilePath);
				ArrayList<String[]> premiseData=processPremiseFile(statement);
				
				creator.createGeoPackage(landscapeData, meterData, premiseData);
				//layers.convertDataToGeoPackage(landscapeData,meterData,premiseData,baseDirPath);
			}
			U.log(counteer);
			U.log(ConsumptionList.size());
			if (testFlag) {
				connection.setAutoCommit(true);
				connection.close();
			}
			System.out.println("Data inserted successfully.");

		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static ArrayList<String[]> processPremiseFile(Statement statement) throws SQLException{
		ArrayList<String[]> outList=new ArrayList<>();
		//outList.add(new String[] {"geom","premid","I","I_SLA","INI","INI_SLA","NI","allocation"});
		String selQuery="SELECT pg.the_geom,pi.premID, pi.I, pi.I_SLA, pi.INI, pi.INI_SLA, pi.NI,pi.percentAllocation,pi.Allocation,pi.Consumption FROM premiseInfo AS pi JOIN premiseGeomData AS pg ON pi.premID = pg.premID; ";
		ResultSet resultSet = statement.executeQuery(selQuery);
		while (resultSet.next()) {
			String[] outArr= {resultSet.getObject("the_geom")+"",resultSet.getString("premID"),resultSet.getString("I"),resultSet.getString("I_SLA"),resultSet.getString("INI"),resultSet.getString("INI_SLA"),resultSet.getString("NI"),resultSet.getString("percentAllocation"),resultSet.getString("Allocation"),resultSet.getString("Consumption")};
			outList.add(outArr);
		}
		
//			for (String[] newLine : meterFileData) {
//				//U.log(Arrays.toString(newLine));
//				String[] outArr= {newLine[71],newLine[0],newLine[58],newLine[59],newLine[61],newLine[62],newLine[63],""};
//				outList.add(outArr);
//				//break;
//			}
			//break;
		
		return outList;
	}
	private static Geometry reducePrecision(Geometry geometry, int precision) {
        // Iterate over coordinates and round them to the specified precision
        for (Coordinate coordinate : geometry.getCoordinates()) {
            coordinate.x = round(coordinate.x, precision);
            coordinate.y = round(coordinate.y, precision);
        }

        return geometry;
    }
	 private static double round(double value, int precision) {
	        double scale = Math.pow(10, precision);
	        return Math.round(value * scale) / scale;
	    }
}
