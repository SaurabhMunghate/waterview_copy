package com.shatam.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class AbstractScrapper {
	private int dupRecord = 0;
	private String countyName = null;
	private String state = null;
	
	private Set<String> uniqueParcel = new HashSet<>();
	private Set<String> uniqueUrl = new HashSet<>();
	private List<String[]> writeLines = new ArrayList<>();

	private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
	private LocalDateTime now = LocalDateTime.now();
	
	
	public AbstractScrapper(String countyName, String state) {
		this.countyName = countyName;
		this.state = state;
	}
	
	protected abstract void innerProcess() throws Exception;
	
	public void start() throws Exception {
		synchronized (this) {
			innerProcess();
		}
	}
	
	public boolean checkUniqueParcel(String parcel){
		if(uniqueParcel.contains(parcel)){
			dupRecord++;
			return false;
		}
		return uniqueParcel.add(parcel);
	}
	
	public boolean checkUniqueUrl(String url){
		if(uniqueUrl.contains(url))return false;
		return uniqueUrl.add(url);
	}
	
	public String getExtractTime(){
		return dtf.format(now);
	}
	
	public void addHeader(String[] header){
		writeLines.add(0, header);
	}
	
	public void addRecord(String[] record){
		writeLines.add(record);
	}
	
	public void write(){
		U.log("Total :" + (writeLines.size()-1));
		FileUtil.writeCsvFile(writeLines, U.getCachePath()+ countyName+"_County_"+state+".csv");
	}
	
	@Override
	public String toString() {
		return "Total Duplicate Records :"+dupRecord;
	}
	
}
