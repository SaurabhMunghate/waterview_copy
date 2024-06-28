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

public class Nv5DataPremiseData {
	static String fileName = "/home/chinmay/Downloads/SAWPA-DUMMIE-DATA-SAMPLE/MonteVistaData/MONTEVISTACO226_Premise_Bounds.csv";

	public static void main(String[] args) {
		new Nv5DataPremiseData().checkIfPolygonsAreValid(fileName);
		// new CSVToJSon().csvToJSON(fileName,fileName.replace(".csv", "_json.json"));
	}

	@SuppressWarnings("unchecked")
	public void checkIfPolygonsAreValid(String inpfileName) {
		File input = new File(inpfileName);
		try {
			CsvSchema csv = CsvSchema.emptySchema().withHeader();
			CsvMapper csvMapper = new CsvMapper();
			MappingIterator<Map<?, ?>> mappingIterator = csvMapper.reader().forType(Map.class).with(csv)
					.readValues(input);
			List<Map<?, ?>> list = mappingIterator.readAll();
			List<JSONObject> jsonObj = new ArrayList<JSONObject>();
			for (Map<?, ?> data : list) {
				JSONObject obj = new JSONObject(data);
				WKTReader reader = new WKTReader();
				try {
					Geometry geom = reader.read(obj.get("the_geom").toString());
					geom = ShatamGeometry.validate(geom);
					System.out.println(geom.getCentroid());
					System.out.println(obj.get("I"));
					System.out.println(obj.get("I_SLA"));
					//(0.62) *
					obj.put("the_geom", geom.toString());
					break;
				} catch (ParseException e) {
					e.printStackTrace();
				}
				jsonObj.add(obj);
			}
			ObjectMapper objectMapper = new ObjectMapper();
			//FileUtil.writeAllText(inpfileName.replace(".csv", "_json.json"), objectMapper.writeValueAsString(jsonObj));
			System.out.println("Json File Created Successfully");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
