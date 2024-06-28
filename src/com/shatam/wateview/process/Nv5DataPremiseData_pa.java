package com.shatam.wateview.process;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.shatam.utils.CSVToJSon;
import com.shatam.utils.FileUtil;
import com.shatam.utils.U;
import com.shatam.waterview.geotools.ShatamGeometry;

import org.json.simple.JSONObject;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

public class Nv5DataPremiseData_pa {
//	static String fileName = "/home/shatam-17/NEWTESTINGFILES2020(MEXICO)/BetaTestingBatch-2022-12-21/MonteVista-2022-12-21/WaterView/shapefiles/Premise_Bounds_convertedData.csv";
	static String fileName = "/home/shatam-100/Cache/Premise_Boundaries_convertedData_json.csv";

	public static void main(String[] args) {
		new Nv5DataPremiseData_pa().checkIfPolygonsAreValid(fileName);
		// new CSVToJSon().csvToJSON(fileName,fileName.replace(".csv", "_json.json"));
	}

	@SuppressWarnings("unchecked")
	public void checkIfPolygonsAreValid(String inpfileName) {
		File input = new File(inpfileName);
		double INI = 0.0;
		double INI_SLA = 0.0;
		double NI = 0.0;
		double I = 0.0;
		double I_SLA = 0.0;
		try {
			CsvSchema csv = CsvSchema.emptySchema().withHeader();
			CsvMapper csvMapper = new CsvMapper();
			MappingIterator<Map<?, ?>> mappingIterator = csvMapper.reader().forType(Map.class).with(csv)
					.readValues(input);
			List<Map<?, ?>> list = mappingIterator.readAll();
			List<JSONObject> jsonObj = new ArrayList<JSONObject>();
			for (Map<?, ?> data : list) {
				System.out.println(data);
				JSONObject obj = new JSONObject(data);
				WKTReader reader = new WKTReader();
//				try {
					Geometry geom = reader.read(obj.get("the_geom").toString());
					geom = ShatamGeometry.validate(geom);
					obj.put("the_geom", geom.toString());
//					System.out.println("premId"+obj.get("PremID")+":"+obj.get("T1A")+":"+obj.get("T1B")+":"+obj.get("T5A")+":"+obj.get("T6A")
//					+":"+obj.get("T9A")+":"+obj.get("T10A")+"");
					NI=Double.parseDouble(obj.get("T1A").toString())+Double.parseDouble(obj.get("T1B").toString())+Double.parseDouble(obj.get("T5A").toString())+Double.parseDouble(obj.get("T6A").toString())+Double.parseDouble(obj.get("T9A").toString())+Double.parseDouble(obj.get("T10A").toString());
					INI = Double.parseDouble(obj.get("T4B").toString())+Double.parseDouble(obj.get("T4A").toString());
					INI_SLA = Double.parseDouble(obj.get("T4A_APR").toString())+Double.parseDouble(obj.get("T4A_CG").toString())+Double.parseDouble(obj.get("T4A_RW").toString())+Double.parseDouble(obj.get("T4A_ES").toString())+Double.parseDouble(obj.get("T4B_APR").toString())+Double.parseDouble(obj.get("T4B_CG").toString())+Double.parseDouble(obj.get("T4B_RW").toString())+Double.parseDouble(obj.get("T4B_ES").toString());
					I = Double.parseDouble(obj.get("T2A").toString())+Double.parseDouble(obj.get("T3A").toString())+Double.parseDouble(obj.get("T3B").toString())+Double.parseDouble(obj.get("T7A").toString());
					I_SLA = Double.parseDouble(obj.get("T2A_PP").toString())+Double.parseDouble(obj.get("T2A_RW").toString())+Double.parseDouble(obj.get("T3A_APR").toString())+Double.parseDouble(obj.get("T3A_CG").toString())+Double.parseDouble(obj.get("T3A_RW").toString())+Double.parseDouble(obj.get("T3A_ES").toString())+Double.parseDouble(obj.get("T3B_APR").toString())+Double.parseDouble(obj.get("T3B_CG").toString())+Double.parseDouble(obj.get("T3B_RW").toString())+Double.parseDouble(obj.get("T3B_ES").toString())+Double.parseDouble(obj.get("T7A").toString())+Double.parseDouble(obj.get("T8A_RW").toString())+Double.parseDouble(obj.get("T8A_SW").toString());
					obj.put("I",I+"");
					obj.put("I_SLA",I_SLA+"");
					obj.put("INI",INI+"");
					obj.put("INI_SLA",INI_SLA+"");
					obj.put("NI",NI+"");
					System.out.println("Total NI: "+ NI+" Total INI: "+INI+"Total INI_SLA: "+INI_SLA);
//				} catch (ParseException e) {
//					e.printStackTrace();
//				}
				System.out.println(obj.toString());
				jsonObj.add(obj);
			}
			ObjectMapper objectMapper = new ObjectMapper();
			FileUtil.writeAllText("/home/shatam-100/Cache/Premise_Boundaries_convertedData_json (copy).json", objectMapper.writeValueAsString(jsonObj));
			System.out.println("Json File Created Successfully");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
