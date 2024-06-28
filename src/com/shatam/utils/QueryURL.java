package com.shatam.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class QueryURL {

	
	private List<String> queryList = new ArrayList<>();
	
	public void addToQuery(String key, String value){
		value = encodeValue(value);
		queryList.add(key+"="+value);
	}
	
	public String getQuery(){
		
		StringBuilder sb = new StringBuilder();
		for(String q : queryList){
			sb.append(q).append("&");
		}
		sb.replace(sb.lastIndexOf("&"), sb.lastIndexOf("&")+2, "");
		
		return sb.toString();
	}
	
	private String encodeValue(String val){
		
		String[] encodeChar = {"\"", ",", "{", "}", ":", " "};

		for(String enChar : encodeChar){
			val = val.replace(""+enChar, encodeChar(enChar));
		}
		return val;
		
	}
	
	private String encodeChar(String val){
		
		try {
			return URLEncoder.encode(val, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return val;
	}
}
