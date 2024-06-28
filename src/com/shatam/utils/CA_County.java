/**
 * @author Sawan Meshram
 * @date 12 Nov 2021
 */
package com.shatam.utils;

public enum CA_County {
	SIERRA ("Sierra"),
	SACRAMENTO ("Sacramento"),
	SANTA_BARBARA ("Santa Barbara"),
	CALAVERAS ("Calaveras"),
	VENTURA ("Ventura"),
	LOS_ANGELES ("Los Angeles"),
	SONOMA ("Sonoma"),
	KINGS ("Kings"),
	SAN_DIEGO ("San Diego"),
	PLACER ("Placer"),
	SAN_FRANCISCO ("San Francisco"),
	MARIN ("Marin"),
	MARIPOSA ("Mariposa"),
	LASSEN ("Lassen"),
	NAPA ("Napa"),
	SHASTA ("Shasta"),
	MONTEREY ("Monterey"),
	TRINITY ("Trinity"),
	MENDOCINO ("Mendocino"),
	INYO ("Inyo"),
	MONO ("Mono"),
	TUOLUMNE ("Tuolumne"),
	SOLANO ("Solano"),
	SAN_BERNARDINO ("San Bernardino"),
	CONTRA_COSTA ("Contra Costa"),
	ALPINE ("Alpine"),
	EL_DORADO ("El Dorado"),
	YOLO ("Yolo"),
	YUBA ("Yuba"),
	SAN_BENITO ("San Benito"),
	HUMBOLDT ("Humboldt"),
	RIVERSIDE ("Riverside"),
	KERN ("Kern"),
	COLUSA ("Colusa"),
	DEL_NORTE ("Del Norte"),
	MODOC ("Modoc"),
	FRESNO ("Fresno"),
	MADERA ("Madera"),
	SANTA_CLARA ("Santa Clara"),
	TEHAMA ("Tehama"),
	SAN_JOAQUIN ("San Joaquin"),
	ALAMEDA ("Alameda"),
	NEVADA ("Nevada"),
	BUTTE ("Butte"),
	MERCED ("Merced"),
	TULARE ("Tulare"),
	STANISLAUS ("Stanislaus"),
	ORANGE ("Orange"),
	IMPERIAL ("Imperial"),
	SUTTER ("Sutter"),
	AMADOR ("Amador"),
	LAKE ("Lake"),
	PLUMAS ("Plumas"),
	SAN_MATEO ("San Mateo"),
	SISKIYOU ("Siskiyou"),
	SANTA_CRUZ ("Santa Cruz"),
	GLENN ("Glenn"),
	SAN_LUIS_OBISPO ("San Luis Obispo");
	
	private final String county;
	private CA_County(String county) {
		this.county = county;
	}
	
	public String getCounty(){
		return this.county;
	}
}
