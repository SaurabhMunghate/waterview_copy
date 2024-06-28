package com.shatam.wateview.process;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.MultiValueMap;
import org.json.simple.JSONObject;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.shatam.utils.FileUtil;
import com.shatam.waterview.geotools.ShatamGeometry;

public class NV5DataLandscapeArea {
	public static void main(String[] args) {
		new NV5DataLandscapeArea().processLandscapeAreaShape(landscapedatacsv);
	}
	static String landscapedatacsv = "/home/chinmay/Downloads/SAWPA-DUMMIE-DATA-SAMPLE/MonteVistaData/MONTEVISTACO226_Landscape_Area.csv";
	String[] landscapeCodes= {"1A","1B","2A","3A","3B","4A","4B","5A","6A","9A"};
	MultiValueMap landscapeProcessedData=new MultiValueMap();
	@SuppressWarnings("unchecked")
	private void processLandscapeAreaShape(String inpfileName) {
		File input = new File(inpfileName);
		try {
			CsvSchema csv = CsvSchema.emptySchema().withHeader();
			CsvMapper csvMapper = new CsvMapper();
			MappingIterator<Map<?, ?>> mappingIterator = csvMapper.reader().forType(Map.class).with(csv)
					.readValues(input);
			List<Map<?, ?>> list = mappingIterator.readAll();
			
			for (Map<?, ?> data : list) {
				JSONObject obj = new JSONObject(data);
				WKTReader reader = new WKTReader();
				try {
					Geometry geom = reader.read(obj.get("the_geom").toString());
					geom = ShatamGeometry.validate(geom);
					obj.put("the_geom", geom.toString());
					landscapeProcessedData.put(obj.get("Class"), obj);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			System.out.print("=="+landscapeProcessedData.size());
			Iterator<String> it = landscapeProcessedData.keySet().iterator();
			ObjectMapper objectMapper = new ObjectMapper();
			while (it.hasNext()) {
				List<JSONObject> jsonObj = new ArrayList<JSONObject>();
				String theKey = (String)it.next();
				Collection<JSONObject> values=landscapeProcessedData.getCollection(theKey);
				for (JSONObject jsonObject : values) {
					jsonObj.add(jsonObject);
				}
				FileUtil.writeAllText(inpfileName.replace(".csv", "_"+theKey+"_json.json"), objectMapper.writeValueAsString(jsonObj));
				System.out.println("Json File Created Successfully "+theKey);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
