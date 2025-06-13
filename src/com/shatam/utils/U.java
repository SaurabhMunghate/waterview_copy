package com.shatam.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.X509Certificate;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.text.WordUtils;




@SuppressWarnings("deprecation")
public class U {
	
	public static String getCachePath(){
		return "/home/shatam-100/WaterViewCache/"; //"/home/glady/workspaces/Parcel_Cache/";  
	}
	
	/**
	 * This method is used to download '.zip' file from resource URL and stored it on output directory path.<br>
	 * If '.zip' file is present on directory path, then it will not download from resource URL.
	 * @param zipUrl input resource URL for downloading the zip file
	 * @param outputDirPath output directory path which downloaded file will stored.
	 * @return
	 * {@code True} if file is downloaded successfully.
	 * @throws IOException
	 * @author Sawan Meshram
	 */
	
	public static boolean downloadZipFile(String zipUrl, String outputDirPath) throws IOException {
		String fileName = new File(zipUrl).getName();
		File file = new File(outputDirPath + File.separator + fileName);
		if(file.exists()){
			return true;
		}
		
		URL url = new URL(zipUrl);
		URLConnection conn = url.openConnection();
		InputStream in = conn.getInputStream();
		FileOutputStream out = new FileOutputStream(outputDirPath + File.separator + fileName);
		System.out.print("File << "+fileName+" >> downloading ...........");
		byte[] b = new byte[1024];
		int count;
		while ((count = in.read(b)) >= 0) {
			out.write(b, 0, count);
		}
		out.flush();
		out.close();
		in.close();
		System.out.println(" [Done]");
		return true;
	}
	
	/**
	 * This method is used to trim of each elements in array string. 
	 * @param vals
	 * @author Sawan Meshram
	 */
	public final static void stripAll(String [] vals){		
		Arrays.stream(vals).map(String::trim).toArray(unused -> vals);
	}
	
	/**
	 * This method is used to get current datetime from local system.
	 * @return
	 * @author Sawan Meshram
	 */
	public final static String getCurrentDate(){		
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM-dd-yyyy hh:mm:ss"));				
	}
	
	/**
	 * This method is used to remove special char from input string if that content taken from html and trim the input string
	 * @param str
	 * @return
	 * @author Sawan Meshram
	 */
	public static String getRemoveSpecialChar(String str){
		return str.replace("\r", " ").replace("\n", " ").replaceAll("\\s{2,}", " ").replace("*", "").trim();
	}
	
	/**
	 * This method is used to add element into given array string and element will be added at end position of given array.
	 * @param array
	 * @param value
	 * @return
	 * @author Sawan Meshram
	 */
	public static String[] concat(String[] array, String value) {
		 String[] result = new String[array.length + 1];
		 System.arraycopy(array, 0, result, 0, array.length);
		 result[result.length - 1] = value;
		 return result;
	}
	/**
	 * This method is used to add array elements into given array string and array element will be added at end position of given array.
	 * @param array
	 * @param values
	 * @return
	 * @author Sawan Meshram
	 */
	public static String[] concat(String[] array, String[] values) {
		 String[] result = new String[array.length + values.length];
		 System.arraycopy(array, 0, result, 0, array.length);
		 int j = 0;
		 for(int i = array.length; i < result.length; i++){
			 result[i] = values[j++];
		 }
		 return result;
	}
	/**
	 * This method is used to add array elements into given array string and array element will be added at end position of given array.
	 * @param array
	 * @param values
	 * @return
	 * @author Sawan Meshram
	 */
	public static String[] concat(String[] array, double[] values) {
		 String[] result = new String[array.length + values.length];
		 System.arraycopy(array, 0, result, 0, array.length);
		 int j = 0;
		 for(int i = array.length; i < result.length; i++){
			 result[i] = values[j++]+"";
		 }
		 return result;
	}
	
	public static boolean isEmpty(String o) {
		return (o == null || o.trim().length() == 0);
	}
	
	
	/**
	 * @author priti
	 */
	public static final String MY_PHANTOMJS_PATH = System.getProperty("user.home")+File.separator+"phantomjs";
	public static final String MY_CHROME_PATH = System.getProperty("user.home")+File.separator+"chromedriver";
	public static void setUpChromePath() {
        if (SystemUtils.IS_OS_LINUX) {
            System.setProperty("webdriver.chrome.driver", MY_CHROME_PATH);
        }
        if (SystemUtils.IS_OS_WINDOWS) {
            System.setProperty("webdriver.chrome.driver", MY_CHROME_PATH+".exe");
        }
    }
	
	public static final String MY_GECKO_PATH = System.getProperty("user.home")+File.separator+"geckodriver";
	public static void setUpGeckoPath() {
        if (SystemUtils.IS_OS_LINUX) {
            System.setProperty("webdriver.gecko.driver", MY_GECKO_PATH);
        }
        if (SystemUtils.IS_OS_WINDOWS) {
            System.setProperty("webdriver.gecko.driver", MY_GECKO_PATH+".exe");
        }
    }
	
	

	

	


	public static String getNoHtml(String html) 
	{ 
		String noHtml=null; 
		noHtml = html.toString().replaceAll("\\<.*?>"," "); 
		return noHtml; 
	}
	
	public static String getCityFromZip(String zip) throws IOException {
		// http://maps.googleapis.com/maps/api/geocode/json?address=77379&sensor=true
		String html = U.getHTML("http://ziptasticapi.com/" + zip);
		String city = U.getSectionValue(html, "city\":\"", "\"");
		return city.toLowerCase();
	}
	
	public static String [] getGoogleLatLngWithKey(String add[]) throws IOException{
		String addr = add[0] + "," + add[1] + "," + add[2];
		addr = "https://maps.googleapis.com/maps/api/geocode/json?address="
				+ URLEncoder.encode(addr, "UTF-8")+"&key=AIzaSyAeG9lLU8fWh8rWcPivHDwxLAM4ZCInpmk";
		U.log(addr);
		U.log(U.getCache(addr));
		String html = U.getHTML(addr);

		String sec = U.getSectionValue(html, "location", "status");

		String lat = U.getSectionValue(sec, "\"lat\" :", ",");
		if (lat != null)
			lat = lat.trim();
		String lng = U.getSectionValue(sec, "\"lng\" :", "}");
		if (lng != null)
			lng = lng.trim();
		String latlng[] = {"", ""};
		String status = U.getSectionValue(html, "status\" : \"", "\"");
		if(status.trim().equals("OK")){
			latlng[0] = lat;
			latlng[1] = lng;
			return latlng;
		}else
			return latlng;
	}
	
	public static String[] getGoogleAddressWithKey(String latLng[]) throws Exception{
		
		String st = latLng[0].trim() + "," + latLng[1].trim();
		String addr = "https://maps.googleapis.com/maps/api/geocode/json?latlng="+ st;
		String key = "AIzaSyAeG9lLU8fWh8rWcPivHDwxLAM4ZCInpmk";

//		U.log("With key"+U.getCache(addr));
		String html = U.getHTMLForGoogleApiWithKey(addr,key);
		String status = U.getSectionValue(html, "status\" : \"", "\"");
		
		if(status.trim().equals("OK")){
			String txt = U.getSectionValue(html, "formatted_address\" : \"", "\"");
//			U.log("Address txt Using gmap key :: " + txt);
			if (txt != null)
				txt = txt.trim();
			txt = txt.replaceAll("The Arden, |TPC Sugarloaf Country Club, ", "").replace("50 Biscayne, 50", "50");
			txt = txt.replaceAll("110 Neuse Harbour Boulevard, ", "");
			txt = txt
					.replaceAll(
							"Waterview Tower, |Liberty Towers, |THE DYLAN, |Cornerstone, |Roosevelt Towers Apartments, |Zenith, |The George Washington University,|Annapolis Towne Centre, |Waugh Chapel Towne Centre,|Brookstone, |Rockville Town Square Plaza, |University of Baltimore,|The Galleria at White Plains,|Reston Town Center,",
							"");
			String[] add = txt.split(",");
			if(add.length == 4) {
			add[3] = Util.match(add[2], "\\d+");
			add[2] = add[2].replaceAll("\\d+", "").trim();
			return add;
			}
			else 
			{
				String add1[] = new String[4];
				add1[3] = Util.match(add[3], "\\d+");
				add1[2] = add[3].replaceAll("\\d+", "").trim();
				add1[1] = add[2];
				add1[0] = add[1];				
				return add1;
			}
		}else{
			return new String[]{"","","",""};
		}
	}
	public static String getHTMLForGoogleApiWithKey(String path,String key) throws Exception {
		path = path.replaceAll(" ", "%20");
		String fileName = U.getCache(path);
		File cacheFile = new File(fileName);
		if (cacheFile.exists()) {
			if (cacheFile.length()> 400) {
				return FileUtil.readAllText(fileName);
			}else if (cacheFile.length()<400) {
				cacheFile.delete();
			}
		}
		URL url = new URL(path+"&key="+key);
		String html = null;
		//Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("45.77.146.9", 55555));
		final URLConnection urlConnection = url.openConnection(); // proxy
		// Mimic browser
		try {
			urlConnection.addRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:10.0.2) Gecko/20100101 Firefox/10.0.2");
			urlConnection.addRequestProperty("Accept", "text/css,*/*;q=0.1");
			urlConnection.addRequestProperty("Accept-Language", "en-us,en;q=0.5");
			urlConnection.addRequestProperty("Cache-Control", "max-age=0");
			urlConnection.addRequestProperty("Connection", "keep-alive");
			final InputStream inputStream = urlConnection.getInputStream();
			html = IOUtils.toString(inputStream);
			inputStream.close();
			if (!cacheFile.exists())
				FileUtil.writeAllText(fileName, html);
			return html;
		} catch (Exception e) {
			U.log(e);

		}
		return html;
	}
	
	
	public static String[] getlatlongGoogleApi(String add[]) throws IOException {
		String addr = add[0] + "," + add[1] + "," + add[2];
		addr = "https://maps.googleapis.com/maps/api/geocode/json?address="
				+ URLEncoder.encode(addr, "UTF-8");
		U.log(addr);
		U.log(U.getCache(addr));
		String html = U.getHTML(addr);
		String status = U.getSectionValue(html, "status\" : \"", "\"");
		U.log("GMAP Address Status Without Key : "+status);
		if(!status.contains("OVER_QUERY_LIMIT") && !status.contains("REQUEST_DENIED"))
		{
		String sec = U.getSectionValue(html, "location", "status");
		String lat = U.getSectionValue(sec, "\"lat\" :", ",");
		if (lat != null)
			lat = lat.trim();
		String lng = U.getSectionValue(sec, "\"lng\" :", "}");
		if (lng != null)
			lng = lng.trim();
		String latlng[] = { lat, lng };
		// U.log(lat);
		return latlng;
		}
		else if(getGoogleLatLngWithKey(add)!=null){
			return getGoogleLatLngWithKey(add);
		}
		else{
			return getlatlongHereApi(add);
		}
	}

	public static String[] getlatlongGoogleApiProxy(String add[])
			throws IOException {
		String proxy = "http://75.119.204.81:3301/gproxy?to=";
		String addr = add[0] + "," + add[1] + "," + add[2];
		addr = "https://maps.googleapis.com/maps/api/geocode/json?address="// 1138
																			// Waterlyn
																			// Drive","Clayton","NC
				+ URLEncoder.encode(addr, "UTF-8");
		proxy = proxy + addr;
		U.log(proxy);
		U.log(U.getCache(proxy));
		String html = U.getHTML(proxy);
		String sec = U.getSectionValue(html, "location", "status");
		String lat = U.getSectionValue(sec, "\"lat\" :", ",");
		if (lat != null)
			lat = lat.trim();
		String lng = U.getSectionValue(sec, "\"lng\" :", "}");
		if (lng != null)
			lng = lng.trim();
		String latlng[] = { lat, lng };
		// U.log(lat);
		return latlng;
	}

	

	public static void log(Object o) {

		System.out.println(o);
	}

	public static String getCacheFileName(String url) {

		String str = url.replaceAll("http://", "");
		str = str.replaceAll("www.", "");
		str = str.replaceAll("[^\\w]", "");
		if (str.length() > 200) {
			str = str.substring(0, 100) + str.substring(170, 190)
					+ str.length() + "-" + str.hashCode();

		}

		try {
			str = URLEncoder.encode(str, "UTF-8");
			// U.log(str);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();

		}
		return str + ".txt";
	}

	

	public static String getCache(String path) throws MalformedURLException {

		String Dname = null;
		String host = new URL(path).getHost();
		host = host.replace("www.", "");
		int dot = host.indexOf("/");
		Dname = (dot != -1) ? host.substring(0, dot) : host;

		File folder = new File(U.getCachePath() + Dname);
		if (!folder.exists())
			folder.mkdirs();
		String fileName = getCacheFileName(path);
		fileName = U.getCachePath() + Dname + "/" + fileName;
		return fileName;
	}

	

	/**
	 * @author Parag Humane
	 * @param args
	 */
	//
	//
	//

	public static String[] getAbsoluteLinks(String[] values, String baseUrl) {

		String[] Links = new String[values.length];
		for (int i = 0; i < values.length; i++) {
			if (values[i].startsWith("http"))
				Links[i] = values[i];
			else
				Links[i] = baseUrl + values[i];
		}
		return Links;
	}

	public static String getAbsoluteLink(String value, String baseUrl) {

		String link = null;
		if (value.startsWith("http"))
			link = value;
		else
			link = baseUrl + value;

		return link;
	}



	public static String getHTML(String path) throws IOException {

		path = path.replaceAll(" ", "%20");
//		 U.log(" .............."+path);
		// Thread.sleep(4000);
		String fileName = getCache(path);
		File cacheFile = new File(fileName);
		 U.log(cacheFile);
		if (cacheFile.exists())
			return FileUtil.readAllText(fileName);

		URL url = new URL(path);
		U.log(url);
		String html = null;

		// chk responce code

//		int respCode = CheckUrlForHTML(path);
//		 U.log("respCode=" + respCode);
//		 if (respCode == 200) {

		// Proxy proxy = new Proxy(Proxy.Type.HTTP, new
		// InetSocketAddress("107.151.136.218",80 ));
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
				"181.215.130.32", 3128));
		final URLConnection urlConnection = url.openConnection();
		// Mimic browser
		try {
			urlConnection
					.addRequestProperty("User-Agent",
							"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:10.0.2) Gecko/20100101 Firefox/10.0.2");
			urlConnection.addRequestProperty("Accept", "text/css,*/*;q=0.1");
			urlConnection.addRequestProperty("Accept-Language",
					"en-us,en;q=0.5");
			urlConnection.addRequestProperty("Cache-Control", "max-age=0");
			urlConnection.addRequestProperty("Connection", "keep-alive");		
			// U.log("getlink");
			final InputStream inputStream = urlConnection.getInputStream();

			html = IOUtils.toString(inputStream);
//		    U.log(html);

			// final String html = toString(inputStream);
			inputStream.close();
			if (!cacheFile.exists())
				FileUtil.writeAllText(fileName, html);

			return html;
		} catch (Exception e) {
			U.log("gethtml expection: "+e);

		}
		return html;
		/*
		 * } else { return null; }
		 */

	}
	 public static void downloadUsingStream(String urlStr, String file) throws IOException{
		 File cacheFile = new File(file);
		 long fileSizeInBytes = cacheFile.length();
//		 if(fileSizeInBytes==247) {cacheFile.delete();}
//		 if(cacheFile.exists() && fileSizeInBytes < 1024) {
//			 cacheFile.delete();
////			 urlStr = urlStr.replace("53ce8f92-2d8d-4a02-8551-801575b1e678", "08807bb2-ce9c-4b65-af3c-dec10f6f3daf");
//			 U.downloadUsingStream(urlStr, U.getCache(urlStr));
//		 }
//		 U.log(cacheFile);
		if (cacheFile.exists())return;
	        URL url = new URL(urlStr); 
	        U.log(url);
	        U.log("cacheFile "+cacheFile);
	        BufferedInputStream bis = new BufferedInputStream(url.openStream());
	        FileOutputStream fis = new FileOutputStream(file);
	        byte[] buffer = new byte[1024];
	        int count=0;
	        while((count = bis.read(buffer,0,1024)) != -1)
	        {
	            fis.write(buffer, 0, count);
	        }
	        fis.close();
	        bis.close();
	    }

	 public static void downloadUsingNIO(String urlStr, String file) throws IOException {
	        URL url = new URL(urlStr);
	        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
	        FileOutputStream fos = new FileOutputStream(file);
	        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
	        fos.close();
	        rbc.close();
	}
	private static HttpURLConnection getConn(String urlPath,
			HashMap<String, String> customHeaders) throws IOException {

		URL url = new URL(urlPath);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.addRequestProperty("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:10.0.2) Gecko/20100101 Firefox/10.0.2");
		conn.addRequestProperty("Accept",
				"text/css,application/xhtml+xml,application/xml,*/*;q=0.9");
		conn.addRequestProperty("Accept-Language", "en-us,en;q=0.5");
		conn.addRequestProperty("Cache-Control", "max-age=0");
		conn.addRequestProperty("Connection", "keep-alive");

		if (customHeaders == null || !customHeaders.containsKey("Referer")) {
			conn.addRequestProperty("Referer", "http://" + url.getHost());
		}
		if (customHeaders == null || !customHeaders.containsKey("Host")) {
			conn.addRequestProperty("Host", url.getHost());
		}

		if (customHeaders != null) {
			for (String headerName : customHeaders.keySet()) {
				conn.addRequestProperty(headerName,
						customHeaders.get(headerName));
			}
		}

		return conn;
	}// getConn

	private static String toString(InputStream inputStream) throws IOException {

		BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
		String line;

		StringBuilder output = new StringBuilder();
		while ((line = in.readLine()) != null) {
			output.append(line).append("\n");
		}
		in.close();
		return output.toString();
	}




	public static String getHtmlSection(String code, String From, String To) {

		String section = null;
		int start, end;
		start = code.indexOf(From);
		if (start != -1) {
			end = code.indexOf(To, start + From.length());
			if (start < end)
				section = code.substring(start, end);
		}
		return section;
	}
	public static String removeSectionValue(String code, String From, String To) {	
		String section = null;
		int start, end;
		start = code.indexOf(From);
		if (start != -1) {
			end = code.indexOf(To, start + From.length());
			if (start < end)	
				section= code.replace(code.substring(start + From.length(), end), "");		
				//U.log("%%%%%%%%%%%%%"+"Successfully Remove Section"+"%%%%%%%%%%%%%%%%%");		
			}else 
			{		
				section= code;		
				}		
		return section;	
		}
	
	//
	public static String getSectionValue(String code, String From, String To) {

		String section = null;
		int start, end;
		start = code.indexOf(From);
		// U.log(start);
		if (start != -1) {
			end = code.indexOf(To, start + From.length());
			if (start < end)
				section = code.substring(start + From.length(), end);

		}
		return section;

	}
	
	

	public static String[] getValues(String code, String From, String To) {

		ArrayList<String> al = new ArrayList<String>();
		int n = 0;
		String value = null;
		while (n != -1) {
			int start = code.indexOf(From, n);

			if (start != -1) {
				int end = code.indexOf(To, start + From.length());

				try {
					if (end != -1 && start < end && end < code.length())
						value = code.substring(start + From.length(), end);
				} catch (StringIndexOutOfBoundsException ex) {
					n = end;
					continue;
				}

				al.add(value);
				n = end;
			} else
				break;
		}

		Object ia[] = al.toArray();
		String[] values = new String[ia.length];

		for (int i = 0; i < values.length; i++)
			values[i] = ia[i].toString();

		return values;

	}

	public static String[] getValues(String code, String begin, String From,
			String To) {

		ArrayList<String> al = new ArrayList<String>();
		int n = 0;
		n = code.indexOf(begin, n);
		String value = null;
		while (n != -1) {
			int start = code.indexOf(From, n);

			if (start != -1) {
				int end = code.indexOf(To, start + From.length());

				try {
					if (end != -1 && start < end && end < code.length())
						value = code.substring(start + From.length(), end);
				} catch (StringIndexOutOfBoundsException ex) {
					n = end;
					continue;
				}

				al.add(value);
				n = end;
			} else
				break;
		}

		Object ia[] = al.toArray();
		String[] values = new String[ia.length];

		for (int i = 0; i < values.length; i++)
			values[i] = ia[i].toString();

		return values;

	}

	public static String[] toArray(ArrayList<String> list) {

		Object ia[] = list.toArray();
		String[] strValues = new String[ia.length];
		for (int i = 0; i < strValues.length; i++)
			strValues[i] = ia[i].toString();
		return strValues;
	}

	public static void printFile(String[] lines, String fileName) {

		Writer output = null;

		try {
			output = new BufferedWriter(new FileWriter(fileName, true));
			for (int i = 0; i < lines.length; i++)
				output.write(lines[i] + "\n");

			output.close();

		} catch (IOException ex) {
		} finally {
		}

	}

	public static void printFile(ArrayList<String> list, String fileName) {

		Writer output = null;

		try {
			output = new BufferedWriter(new FileWriter(fileName, true));

			for (String item : list)
				output.write(item + "\n");

			output.close();

		} catch (IOException ex) {
		} finally {
		}

	}

	public static String degToDecConverter(String str) {

		double s1, s3, s4;
		double val;
		String s2;
		s1 = Integer.parseInt(str.substring(0, str.indexOf("&deg;")));
		s2 = str.substring(str.indexOf(";"));
		s3 = Integer.parseInt(s2.substring(2, s2.indexOf("'")));
		s4 = Integer
				.parseInt(str.substring(str.length() - 3, str.indexOf("\"")));

		val = s1 + (s3 / 60) + (s4 / 3600);
		return Double.toString(val);
	}

	public static String[] getAddressFromLines(String[] lines) {

		final String BLANK = "-";
		String street = BLANK, city = BLANK, state = BLANK, zip = BLANK;
		String addStreet = "-";
		int len = lines.length;
		if (len >= 1) {
			for (int i = 0; i <= (len - 2); i++)
				addStreet = lines[i] + ",";
			street = addStreet;
			if (lines[len - 1].indexOf(",") != -1) {
				city = lines[len - 1].split(",")[0];
				String add = lines[len - 1].split(",")[1];
				add = add.trim();
				state = add.substring(0, 2).toUpperCase();
				zip = Util.match(add, "\\d{5}?", 0);
				if (zip == null)
					zip = BLANK;
			}

		}
		String[] add = { street, city, state, zip };
		return add;
	}



	
	
	/**
	 * @author Parag Humane
	 * @param args
	 */
	public static String getHTML(String path, String baseUrl)
			throws IOException {

		if (baseUrl != null && !path.startsWith(baseUrl))
			return getHTML(baseUrl + path);
		else
			return getHTML(path);
	}

	

	

	/**
	 * @author Parag Humane
	 * @param args
	 */
	public static String[] getGooleLatLong(String html) {

		String reg = "\\{center:\\{lat:(\\d+\\.\\d+),lng:(-\\d+\\.\\d+)";
		String reg2 = "<title>\\s*(\\d+\\.\\d+),\\s*(-\\d+\\.\\d+)";
		String match = Util.match(html, reg, 0);
		reg = (match == null) ? reg2 : reg;
		String lat = Util.match(html, reg, 1);
		String lng = Util.match(html, reg, 2);
		return new String[] { lat, lng };
	}

	public static String[] getGlatlng(String[] add) throws Exception {
		// String addr = add[0] + "+" + add[1];
		String addr = add[0] + "+" + add[1] + "+" + add[2] + "+" + add[3];
		String data = null;
		String latLong = null;
		StringBuffer input = new StringBuffer();
		String link = "https://maps.google.com/maps?daddr="
				+ URLEncoder.encode(addr, "UTF-8");

		URL url = new URL(link);

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		BufferedReader br = new BufferedReader(new InputStreamReader(
				connection.getInputStream()));

		while ((data = br.readLine()) != null) {
			input.append(data);
		}

		String path = "E:\\google.txt";
		File file = new File(path);
		FileWriter wr = new FileWriter(file);
		wr.write(input.toString());
		int x = input.indexOf("{lat:");
		int y = input.indexOf("}", x);
		U.log("X:" + x);
		U.log("Y:" + y);

		latLong = input.substring(x, y);

		latLong = latLong.replace("{lat:", "");
		latLong = latLong.replace("lng:", "");

		String[] array = latLong.split(",");
		return array;
	}

	public static String[] getGooleLatLong(String[] add) throws IOException {

		String addr = add[0] + "+" + add[1] + "+" + add[2] + "+" + add[3];
		addr = "https://maps.google.com/maps?daddr="
				+ URLEncoder.encode(addr, "UTF-8");
		U.log(addr);
		String html = U.getHTML(addr);
		return U.getGooleLatLong(html);
	}

	public static String[] getAddress(String line) {
		
		String[] ad = line.split(",");

		String[] add = new String[] { "-", "-", "-", "-" };
		int n = ad.length;
		//U.log("ad length is ::"+ad.length);
		if (ad.length >= 3) {
			for (int i = 0; i < n - 2; i++)
				add[0] = (i == 0) ? ad[i].trim() : add[0] + ", " + ad[i].trim();
			add[0] = add[0].trim().replaceAll("\\d+.\\d+\\s*,\\s*-\\d+.\\d+",
					"");

			add[1] = ad[n - 2].trim();// city
			add[2] = Util.match(ad[n - 1], "\\w+", 0); // State--or-->\\w+ OR
														// [a-z\\sa-z]+
			add[2] = (add[2] == null) ? "-" : add[2].toUpperCase();
			if (add[2] == null)
				add[2] = "-";
			if (add[2].length() > 2)
				add[2] = USStates.abbr(add[2]);
			add[3] = Util.match(ad[n - 1], "\\d{5}", 0);
			if (add[3] == null)
				add[3] = "-";
		}
		for (int i = 0; i < 1; i++) {
			if (!add[i].equals("-") && add[i].length() < 3)
				add[i] = "-";
		}

		return add;
	}

	public static String[] getMapQuestAddress(String html) {

		String section = U
				.getSectionValue(html, "singleLineAddress\":\"", "\"");
		String[] add = null;
		if (section != null)
			add = U.getAddress(section);
		return add;
	}

	public static String[] getMapQuestLatLong(String html) {

		String lat = null, lng = null;
		String reg = "\"latLng\":\\{\"lng\":(-\\d+.\\d+),\"lat\":(\\d+.\\d+)";
		String reg2 = "\"latLng\":\\{\"lat\":(\\d+.\\d+),\"lng\":(-\\d+.\\d+)";
		String match = Util.match(html, reg, 0);
		if (match == null) {
			lat = Util.match(html, reg2, 1);
			lng = Util.match(html, reg2, 2);
		} else {
			lat = Util.match(html, reg, 2);
			lng = Util.match(html, reg, 1);
		}
		return new String[] { lat, lng };
	}

	public static String[] findAddress(String code) {

		String[] add = null;
		String sec = formatAddress(code);
		//U.log("sec::::"+sec);
		String regAddress = "\\d*\\s*\\w+[^,\n]+[,\n]+\\s*\\w+[\\s,]+\\w+.*?\\d{5}";
		String reg2 = "\\d+\\s*\\w+[^,\n]+[,\n]+\\s*\\w+[^,]+,\\s*\\w+\\s*\\d{5}";
		String reg4 = "\\s*\\w+[^,\n]+[,\n]+\\s*\\w+[^,]+,\\s*\\w+\\s*\\d{5}";
		String reg3 = "\\d+[^,]+,( ?\\w+){1,5},( ?\\w+)( \\d{5})?";
		String match = Util.match(sec, regAddress, 0);

		if (match == null)
			match = Util.match(sec, reg2, 0);
		if (match == null)
			match = Util.match(sec, reg3, 0);
		if (match != null)
			add = U.getAddress(match);
		if (match != null)
			match = Util.match(sec, reg4, 0);

		return add;
	}

	public static String[] getGoogleAdd(String lat, String lon)
			throws Exception {
		String glink = "https://maps.google.com/maps?q=" + lat + "," + lon;
		U.log("Visiting :" + glink);
		// WebDriver driver=new FirefoxDriver();
		// driver.get(glink);
		String htm = U.getHTML(glink);
		U.log(U.getCache(glink));
		return U.getGoogleAddress(htm);
	}

	public static String formatAddress(String code) {

		String sec = code.replaceAll("\\&nbsp;|�|&#038;|�", " ");
		sec = sec.replaceAll(" {2,}", " ");

		sec = sec.replaceAll("\\s{2,}", "");

		sec = sec.replaceAll(
				"<br>|<br[^>]+>|</p><p[^>]+>|</p>|</br>|<BR>|<BR />|<br />",
				",");

		sec = sec.replaceAll("<[^>]+>", "");

		sec = sec.replaceFirst("[^>]+>", "");
	//	 U.log("sec:"+sec);
		//Priti-----
		 
		if (sec.contains("Ave") && !sec.contains("Ave,") && !sec.contains("Avenue") && !sec.contains("Avenue,"))
			sec = sec.replace("Ave", "Ave,");
		if (sec.contains("Rd") && !sec.contains("Rd,") && !sec.contains("Rd."))
			sec = sec.replace("Rd", "Rd,");
		
		//-----------------------
		if (sec.contains("Dr.") && !sec.contains("Dr.,"))
			sec = sec.replace("Dr.", "Dr.,");
		if (sec.contains("Drive") && !sec.contains("Drive,"))
			sec = sec.replace("Drive", "Drive,");
		if (sec.contains("Trail") && !sec.contains("Trail,") && !sec.contains("Trails"))
			sec = sec.replace("Trail", "Trail,");
		if (sec.contains("Street") && !sec.contains("Street,") && !sec.contains("Street &"))
			sec = sec.replace("Street", "Street,");
		if (sec.contains("Blvd.") && !sec.contains("Blvd.,"))
			sec = sec.replace("Blvd.", "Blvd.,");
		if (sec.contains("Avenue") && !sec.contains("Avenue,"))
			sec = sec.replace("Avenue", "Avenue,");
		if (sec.contains("Road") && !sec.contains("Road,") && !sec.contains("Roads,"))
			sec = sec.replace("Road", "Road,");
		if (sec.contains("Ct") && !sec.contains("Ct,")
				&& !sec.contains("Ct. #"))
			sec = sec.replace("Ct", "Ct,");
		if (!sec.contains("Lane <") && sec.contains("Lane")
				&& !sec.contains("Lane,"))
			sec = sec.replace("Lane", "Lane,");
		if (sec.contains("land Court") && !sec.contains("land Court.,"))
			sec = sec.replace("land Court", "land Court,");
		if (sec.contains(", St.") && !sec.contains(", St.,"))
			sec = sec.replace(", St.", " St.,");
		return sec;
	}

	public static String[] getGoogleAddress(String html) throws IOException {

		String section = U.getSectionValue(html, "addr:'", "'");
		U.log("U section==" + section);
		String[] add = null;
		if (section != null) {
			add = U.getAddress(section.replaceAll("\\\\x26", "&"));
		}
		return add;
	}

	
	
	public static int countMatch(String reg, String text) {

		Matcher m = Pattern.compile(reg).matcher(text);
		int n = 0;
		while (m.find())
			n++;
		return n;
	}// //

	public static ArrayList<String> removeDuplicates(ArrayList<String> list) {

		HashSet<String> hs = new HashSet<String>();
		hs.addAll(list);
		list.clear();
		list.addAll(hs);
		hs = null;
		return list;
	}// ///
	public static void bypassCertificate() throws Exception {
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}

			@Override
			public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
					throws java.security.cert.CertificateException {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
					throws java.security.cert.CertificateException {
				// TODO Auto-generated method stub
				
			}

			
		} };

		// Install the all-trusting trust manager
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		// Create all-trusting host name verifier
		HostnameVerifier allHostsValid = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};

		// Install the all-trusting host verifier
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	}
	//========================================================written new code by Upendra===================================================
	

	public static String removeComments(String html) {
		String[] values = U.getValues(html, "<!--", "-->");
		for (String item : values)
			html = html.replace(item, "");
		return html;

	}

	

/*	public static String[] getLatLngBingMap(String add[]) throws Exception {

		String addr = add[0] + " " + add[1] + " " + add[2] + " " + add[3];
		ShatamFirefox driver1 = null;
		String bingLatLng[] = { "", "" };
		driver1 = ShatamFirefox.getInstance();
		driver1.open("http://localhost/bing.html");
		driver1.wait("#txtWhere");
		driver1.getOne("#txtWhere").sendKeys(addr);
		driver1.wait("#btn");
		driver1.getOne("#btn").click();
		String binghtml = driver1.getHtml();
		// driver.wait(1000);
		U.log(binghtml);
		String secmap = U.getSectionValue(binghtml, "<div id=\"resultsDiv\"",
				"</body>");
		bingLatLng[0] = U.getSectionValue(secmap,
				"<span id=\"span1\">Latitude:", "<br>").trim();
		bingLatLng[1] = U.getSectionValue(secmap, "<br>Longitude:", "</span>");

		U.log("lat:" + bingLatLng[0]);
		U.log("lng:" + bingLatLng[1]);
		return bingLatLng;
	}*/

	public static String[] getBingAddress(String lat, String lon) throws Exception {
		String[] addr = null;
//		U.log(U.getCache("http://dev.virtualearth.net/REST/v1/Locations/" + lat + "," + lon+ "?o=json&jsonp=GeocodeCallback&key=Ak8RoKwrZE-IbLkkXRFae9UTXw2UhpuET1mWY9z_ZkzTYR_-TCG8pMcNIUEtiqo5"));
		String htm = U.getHTML("http://dev.virtualearth.net/REST/v1/Locations/" + lat + "," + lon
				+ "?o=json&jsonp=GeocodeCallback&key=Ak8RoKwrZE-IbLkkXRFae9UTXw2UhpuET1mWY9z_ZkzTYR_-TCG8pMcNIUEtiqo5");
		//Anqg-XzYo-sBPlzOWFHIcjC3F8s17P_O7L4RrevsHVg4fJk6g_eEmUBphtSn4ySg
		 
		String[] adds = U.getValues(htm, "formattedAddress\":\"", "\"");
		for (String item : adds) {
			item = item.replace(", United States", "");
			item = item.replace(", USA", "");
			addr = U.getAddress(item);
			if (addr == null || addr[0] == "-")
				continue;
			else {
				U.log("Bing Address =>  Street:" + addr[0] + " City :" + addr[1] + " state :" + addr[2] + " ZIP :"
						+ addr[3]);
				return addr;
			}

		}
		return addr;
	}
	
	public static String[] getBingAddress1(String lat, String lon)
			throws Exception {
		String[] addr = null;
		String htm = U
				.getHTML("http://dev.virtualearth.net/REST/v1/Locations/"
						+ lat
						+ ","
						+ lon
						+ "?o=json&jsonp=GeocodeCallback&key=Anqg-XzYo-sBPlzOWFHIcjC3F8s17P_O7L4RrevsHVg4fJk6g_eEmUBphtSn4ySg");
		String[] adds = U.getValues(htm, "formattedAddress\":\"", "\"");
		for (String item : adds) {
			addr = U.getAddress(item);
			if (addr == null || addr[0] == "-")
				continue;
			else {
				U.log("Bing Address =>  Street:" + addr[0] + " City :"
						+ addr[1] + " state :" + addr[2] + " ZIP :" + addr[3]);
				return addr;
			}

		}
		return addr;
	}

	public static String[] getBingLatLong(String[] address) throws Exception {
		String[] latLong = { null, null };
		if (address == null || address[0] == "-" || address[1] == "-"
				|| address[2] == "-")
			return latLong;
		else {
			String line = address[0] + "," + address[1] + "," + address[2]
					+ " " + address[3];
			try {
				latLong = getBingLatLong(line);
			} catch (java.io.IOException ex) {
				return latLong;
			}
		}
		return latLong;
	}

	public static String[] getBingLatLong(String addressLine) throws Exception {
		String[] latLong = new String[2];
		if (addressLine == null || addressLine.trim().length() == 0)
			return null;
		addressLine = addressLine.trim().replaceAll("\\+", " ");
		String geocodeRequest = "http://dev.virtualearth.net/REST/v1/Locations/'"
				+ addressLine
				+ "'?o=xml&key=Anqg-XzYo-sBPlzOWFHIcjC3F8s17P_O7L4RrevsHVg4fJk6g_eEmUBphtSn4ySg";
		// U.log("-----------------addressline-----------"+geocodeRequest);
		String xml = U.getHTML(geocodeRequest);
		// U.log("--------------------------xml---------------------------------"+xml);
		latLong[0] = U.getSectionValue(xml, "<Latitude>", "</Latitude>");
		latLong[1] = U.getSectionValue(xml, "<Longitude>", "</Longitude>");
		return latLong;
	}

	public static boolean isMatchedLocation(String[] add, String[] addr) {
		boolean isMatched = false;
		for (int i = 0; i < 3; i++) {
			if (add[i] == null || addr[i] == null || add[i].equals("-")
					|| addr[i].equals("-"))
				return isMatched;
		}

		if (addr[3].equals(add[3]))
			isMatched = true;
		else {
			if (addr[1].equalsIgnoreCase(add[1])
					&& addr[2].equalsIgnoreCase(add[2]))
				isMatched = true;
			else {
				if (addr[2].equalsIgnoreCase(add[2])
						&& add[3].substring(0, 3).equals(
								addr[3].substring(0, 3)))
					isMatched = true;
			}
		}
		return isMatched;
	}

	public static boolean isValidLatLong(String[] latLon, String[] add)
			throws Exception {
		boolean isValid = false;
		if (latLon == null || latLon[1] == null || add == null)
			return isValid;

		U.log("lat :" + latLon[0] + " Long :" + latLon[1]);

		if (latLon[0] == "-" || latLon[1] == "-" || add[3] == "-"
				|| add[1] == "-" || add[2] == "-")
			return isValid;

		boolean isMatched = false;
		String[] addr = null;
		addr = U.getBingAddress(latLon[0], latLon[1]);
		if (addr != null) {
			isMatched = isMatchedLocation(add, addr);
			if (isMatched)
				return true;
		}
		addr = U.getGoogleAdd(latLon[0], latLon[1]);
		isMatched = isMatchedLocation(add, addr);
		if (isMatched)
			return true;

		return isValid;
	}

	public static String getCapitalise(String line) {	
		if(line!=null) {
			line = line.trim();
		}
		if(line==null || line.length() < 2) {
			return "";
		}
		
		line = line.toLowerCase();		
		line = line.trim().replaceAll("\\s+", " ");
		String[] words = line.trim().split(" ");
		String capital = null, wd;
		for (String word : words) {
			wd = word.substring(0, 1).toUpperCase() + word.substring(1);
			capital = (capital == null) ? wd : capital + " " + wd;
		}
		return capital;
	}

	


	public static String[] getAddressFromSec(String address, String skipLines) {

		String[] OUT = { "-", "-", "-", "-" };
		if (address != null) {
			String[] ADD = U.getZipStateCity(address);
			OUT[1] = ADD[0];
			OUT[2] = ADD[1];
			OUT[3] = ADD[2];
			String street = "-";
			// String ignore_ADD=;
			//U.log("ADD[0]::" + ADD[0]);
			if (ADD[0] != null || ADD[1] != null) {
				//U.log("ADDRESS :    " + address);
				int streetSec = -1;
				String[] addSections = U.getValues(address, ">", "<");
				int counter = 0;
				if (addSections.length > 0) {
					for (String secLoc : addSections) {
						//U.log("***&  " + secLoc);
						if (secLoc.contains("Rd") || !secLoc.contains("Draper")
								&& secLoc.contains("Dr")
								|| secLoc.contains("Blvd")
								|| secLoc.contains("Boulevard")) {
							counter++;
							//U.log("***&  secLoc 1");
							continue;
						}
						String chkSec = Util.match(secLoc, ADD[1]);
						if (chkSec != null) {
							streetSec = counter;
							break;
						}
						//U.log("ADD[0]::" + ADD[0]);
						if (ADD[0] != null)
							chkSec = Util.match(secLoc, ADD[0]);
						if (chkSec != null) {
							streetSec = counter;
							break;
						}
						counter++;
					}
				}

				//U.log("@@@  " + streetSec);

				if (streetSec == 0)
					street = addSections[0];
				if (streetSec == 1)
					street = addSections[0];
				if (streetSec == 2) {
					String find = Util.match(addSections[0] + addSections[1],
							skipLines);
					if (find != null) {
						if (!addSections[0].contains(find))
							street = addSections[0];
						if (!addSections[1].contains(find))
							street = addSections[1];
					} else // if(find==null)
					{
						street = addSections[0] + ", " + addSections[1];
					}
				}
			}
			OUT[0] = street;
		}
		return OUT;
	}

	public static String[] getZipStateCity(String addSec) { // getZipStateCity
		String[] ZSC = { "-", "-", "-" };

		String state = getState(addSec);
		//U.log("state==" + state);
		String zip = getZip(addSec, state);
		//U.log("%%%%%  :" + zip);
		if (zip != "-") {
			String ST = Util.match(zip, "\\w+");
			if (ST != "-") {
				String check = Util.match(state, ST);
				if (check == null) {
					zip = Util.match(zip, "\\w+\\W+(\\d{4,5})", 1);
					//U.log("*********" + zip);
					String Real = "\\w+\\W+" + zip;
					//U.log("Real*********" + Real);
					String stateReal = Util.match(addSec, Real);
					if (stateReal != null) {
						if (stateReal.contains("Hampshire")
								|| stateReal.contains("Jersey")
								|| stateReal.contains("Mexico")
								|| stateReal.contains("York")
								|| stateReal.contains("Carolina")
								|| stateReal.contains("Dakota")
								|| stateReal.contains("Island")
								|| stateReal.contains("Virginia")) {
							Real = "\\w+\\s\\w+\\W+" + zip;
							stateReal = Util.match(addSec, Real);
							state = Util.match(stateReal, "\\w+\\s\\w+");
							
						} else {
							state = Util.match(stateReal, "\\w+");
							
						}
					}
				}
			}
		}
		ZSC[1] = state;
		ZSC[2] = Util.match(zip, "\\d{4,5}");
		if (ZSC[2] == null)
			ZSC[2] = "-";
		String city = getCity(addSec, state);
		//U.log("********************@*" + city);
		String chkCity = city + "(.*?)<"; // \\s\\w{2}
		chkCity = Util.match(addSec, chkCity);
		if (chkCity != null) {
			String chkCity2 = chkCity.replace(city, "");
			if (chkCity2.contains("Rd") || chkCity2.contains("Dr")
					|| chkCity2.contains("Blvd")
					|| chkCity2.contains("Boulevard")
					|| chkCity2.contains("Park")) {
				String LOCaddSec = addSec.replace(chkCity, "");
				String LOCcity = getCity(LOCaddSec, state);
				U.log("POP**  :" + LOCcity);
				if (LOCcity.trim() != null && LOCcity.trim().length() > 3)
					city = LOCcity;
			}
		}
		ZSC[0] = city;
		//U.log(ZSC[0]+"::::::::::city");
		return ZSC;
	}

	private static String getState(String addSec) { // getZipStateCity
		
		String ignoreStreet = "1350 N New York St";
		addSec = addSec.replace(ignoreStreet, "");

		String state = Util
				.match(addSec,
						"Alabama|Alaska|Arizona|Arkansas|California|Colorado|Connecticut|Delaware|Florida|Georgia|Hawaii|Idaho|Illinois|Indiana|Iowa|Kansas|Kentucky|Louisiana|Maine|Maryland|Massachusetts|Michigan|Minnesota|Mississippi|Missouri|Montana|Nebraska|Nevada|New Hampshire|New Jersey|New Mexico|New York|North Carolina|North Dakota|Ohio|Oklahoma|Oregon|Pennsylvania|Rhode Island|South Carolina|South Dakota|Tennessee|Texas|Utah|Vermont|West Virginia|Virginia|Washington|Wisconsin|Wyoming");

		String[] sameName = { "Washington", "Maine", "Delaware", "Missouri",
				"Virginia", "Nevada", "Iowa", "Colorado" };
		//U.log("my state::"+state);
		if (state != null) {
			for (String same : sameName) {
				if (state.contains(same)) {
					addSec = addSec.replaceAll(same, "##@@");
					String LOLstate = getState(addSec);
					//U.log("LOLstate : " + LOLstate);
					if (LOLstate == null || LOLstate.length() < 3)
						state = same;
					else
						state = LOLstate;
					addSec = addSec.replace("##@@", same);
				}
			}
		}
		if (state == null) {
			state = Util.match(addSec, " [A-Z]{2} ");
		}
		if (state == null) {
			state = Util
					.match(addSec,
							"Alabama|Alaska|Arizona|Arkansas|California|Colorado|Connecticut|Delaware|Florida|Georgia|Hawaii|Idaho|Illinois|Indiana|Iowa|Kansas|Kentucky|Louisiana|Maine|Maryland|Massachusetts|Michigan|Minnesota|Mississippi|Missouri|Montana|Nebraska|Nevada|New Hampshire|New Jersey|New Mexico|New York|North Carolina|North Dakota|Ohio|Oklahoma|Oregon|Pennsylvania|Rhode Island|South Carolina|South Dakota|Tennessee|Texas|Utah|Vermont|West Virginia|Virginia|Washington|Wisconsin|Wyoming");
			if (state != null)
				return state;
		}

		if (state == null)
			state = "-";
		return state;
	}

	private static String getZip(String addSec, String state) { // getZipStateCity
		String match = state + "\\W+\\d{5}";
		String zip = Util.match(addSec, match);
		if (zip == null) {
			match = state + "\\W+\\d{4,5}";
			zip = Util.match(addSec, match);
		}
		if (zip == null) {
			zip = Util.match(addSec, "\\W+\\d{5}");
		}
		if (zip == null)
			zip = "-";
		return zip;
	}

	public static String getCity(String addSec, String state) { // getZipStateCity
		U.log("*#*  :" + state +": *#*");
		if (state.length() > 2)
			state = USStates.abbr(state.trim());
		U.log(state);
		// addSec=addSec.replace("766 Austin Lane", "");
		String ingoreStreet = "2820 Bellevue Way NE|2303 San Marcos|Santa Rosa Villas|8541 N. Walnut Way|208 S. Auburn Heights Lane|2228 Tracy Place|3200 Osprey Lane|Kannapolis Parkway|1350 N New York St|8500 N. Fallbrook Avenue|Woodruff Road|Hopewell Road|Old Spartanburg Road|Zebulon Road|Kailua Road|Evans Mill Road|Arnold Overlook Lane|Wellington Woods Avenue|11405 Pacey's Pond Circle|405 San Luis Obispo St|4307 Walnut Avenue|12422 Mar Vista Street";
		addSec=addSec.replaceAll(ingoreStreet, "");
		String city = "-";
		if (state.trim().contains("Alabama") || state.contains("AL")) {
			city = Util
					.match(addSec,
							"Hunstville|Montevallo|Magnolia Springs|Orange Beach|Auburn|Alabaster|Albertville|Alexander City|Anniston|Athens|Atmore|Banks|Bessemer|Birmingham|Brewton|Center Point|Cottonton|Cullman|Daleville|Daphne|Decatur|Dothan|Enterprise|Eufaula|Fairfield|Fairhope|Florence|Foley|Forestdale|Fort Payne|Gadsden|Gardendale|Grove Hill|Hartselle|Helena|Homewood|Hoover|Hueytown|Huntsville|Jackson|Jasper|Kimberly|Leeds|Lisman|Lowndesboro|McCalla|Madison|Millbrook|Mobile|Monroeville|Montgomery|Mountain Brook|Muscle Shoals|Northport|Opelika|Oxford|Ozark|Pelham|Phenix City|Prattville|Prichard|Repton|Robertsdale|Saks|Saraland|Scottsboro|Selma|Sheffield|Slocomb|Smiths|Sylacauga|Talladega|Tallassee|Theodore|Tillmans Corner|Toney|Troy|Trussville|Tuscaloosa|Tuskegee|Vestavia([,\\W])?Hills|Warrior|Calera|Pinson|Chelsea|Springville|Moody|Fultondale|Odenville|Liberty Park Area|VestaviaHills|Hazel Green|Meridianville|Owens Cross Roads|Spanish Fort|Loxley|Semmes|Gulf Shores|Summerdale|Deatsville|Wetumpka|Vance|Moundville|Vestavia-Hills|Fulton|Harvest|Margaret");
		}
		if (state.contains("Alaska") || state.contains("AK")) {
			city = Util
					.match(addSec,
							"Anchorage|College|Fairbanks|Juneau|Kodiak|Adak|Akhiok|Akiachak|Akiak|Akutan|Alakanuk|Bettles|Bethel|Big Lake|Bristol Bay|Coffman Cove|Cordova|Crow Village|Delta Junction|Dillingham|Ester|Fairbanks|Haines|Homer|Hoonah|Kenai|Ketchikan|Knik|Kotzebue|Matanuska-Susitna Borough|Nome|North Pole|Palmer|Pelican|Petsburg|Seward|Sitka|Skagway|Soldotna|Thorne Bay|Unalaska|Valdez|Wasilla|Whittier|Wrangell");
		}
		if (state.contains("Arizona") || state.contains("AZ")) {
			city = Util
					.match(addSec,
							"Barcelona|Apache Junction|Avondale|Bullhead City|Casa Grande|Casas Adobes|Catalina Foothills|Cave Creek|Chambers|Chandler|Chinle|Cottonwood-Verde Village|Douglas|Drexel Heights|Eloy|Flagstaff|Florence|Flowing Wells|Fortuna Foothills|Fountain Hills|Fredonia|Gilbert|Glendale|Globe|Goodyear|Green Valley|Heber|Holbrook|Houck|Lake Havasu City|Marana|Mayer|Mesa|Mohave Valley|New Kingman-Butler|New River|Nogales|Oro Valley|Paradise Valley|Payson|Peoria|Phoenix|Pima|Prescott Valley|Prescott|Quartzsite|Rimrock|Riviera|Sacaton|Safford|San Luis|Scottsdale|Sedona|Show Low|Sierra Vista Southeast|Sierra Vista|Sun City West|Sun City|Sun Lakes|Surprise|Tacna|Tanque Verde|Teec Nos Pos|Tempe|Tonalea|Tucson|Whiteriver|Willcox|San Tan Valley|Maricopa|Buckeye|Queen Creek|Sahuarita|Vail|Yuma|Page");
		}
		if (state.contains("Arkansas") || state.contains("AR")) {
			city = Util
					.match(addSec,
							"Amity|Arkadelphia|Atkins|Batesville|Bella Vista|Bentonville|Benton|Blytheville|Cabot|Camden|Conway|Dermott|El Dorado|Fayetteville|Forrest City|Fort Smith|Franklin|Hampton|Hardy|Harrison|Heber Springs|Hermitage|Hope|Hot Springs|Jonesboro|Little Rock|Magnolia|Malvern|Marmaduke|Marshall|Maumelle|Mountain Home|Mountain View|Newport|Norfork|North Little Rock|Oden|Ola|Paragould|Pine Bluff|Pocahontas|Rogers|Russellville|Salem|Searcy|Sherwood|Shirley|Siloam Springs|Sparkman|Springdale|Texarkana|Van Buren|Waldron|West Memphis|Wynne");
		}

		if (state.contains("California") || state.contains("CA")) {
			city = Util
					.match(addSec,
							"Arabella|Winchester|Diablo Grande|Rolling Hills Estates|Adelanto|Agoura Hills|Alameda|Alamo|Albany|Alhambra|Aliso Viejo|Alpine|Altadena|Alta|Alum Rock|Anaheim Hills|Anaheim|Angels Camp|Angwin|Antioch|Apple Valley|Aptos|Arcadia|Arcata|Arden-Arcade|Arroyo Grande|Artesia|Arvin|Ashland|Atascadero|Atwater|Auburn|Avenal|Avocado Heights|Azusa|Bakersfield|Baker|Baldwin Park|Banning|Barstow|Bass Lake|Bay Point|Baywood-Los Osos|Beaumont|Bell Gardens|Bellflower|Belmont|Benicia|Berkeley|Beverly Hills|Big Bear City|Big Creek|Blackhawk-Camino Tassajara|Bloomington|Blythe|Bonita|Bostonia|Brawley|Brea|Brentwood|Buena Park|Burbank|Burlingame|Cabazon|Calabasas|Calexico|Camarillo|Cameron Park|Campbell|Canoga Park|Capitola|Carlsbad|Carmel|Carmichael|Carpinteria|Carson|Casa de Oro-Mount Helix|Castro Valley|Cathedral City|Central Valley|Ceres|Cerritos|Chatsworth|Cherryland|Chico|Chino Hills|Chino|Chowchilla|Chula Vista|Citrus Heights|Citrus|Claremont|Clayton|Clearlake|Cloverdale|Clovis|Coachella|Coalinga|Colton|Commerce|Compton|Concord|Corcoran|Corning|Coronado|Corona|Costa Mesa|Cotati|Coto de Caza|Covina|Crescent City|Crestline|Cudahy|Culver City|Cupertino|Cypress|Daly City|Dana Point|Danville|Delano|Desert Hot Springs|Diamond Bar|Dinuba|Dixon|Downey|Duarte|Dublin|East Hemet|East Los Angeles|East Palo Alto|East San Gabriel|El Cajon|El Centro|El Cerrito|El Dorado Hills|El Monte|El Segundo|El Sobrante|Elk Grove|Encinitas|Encino|Escondido|Esparto|Eureka|Fair Oaks|Fairfield|Fallbrook|Fillmore|Florence-Graham|Florin|Folsom|Fontana|Foothill Farms|Foothill Ranch|Fort Bragg|Fortuna|Foster City|Fountain Valley|Fremont|Fresno|Fullerton|Galt|Garden Grove|Gardena|Gilroy|Glen Avon|Glendale|Glendora|Goleta|Grand Terrace|Granite Bay|Grass Valley|Greenfield|Grover Beach|Gualala|Guerneville|Hacienda Heights|Half Moon Bay|Hanford|Hawaiian Gardens|Hawthorne|Hayward|Healdsburg|Hemet|Hercules|Hermosa Beach|Hesperia|Highland|Hillsborough|Hollister|Hollywood|Homewood|Huntington Beach|Huntington Park|Imperial Beach|Indio|Inglewood|Irvine|Isla Vista|Jackson|Janesville|Junction City|Keene|King City|La Canada-Flintridge|La Crescenta-Montrose|La Habra|La Jolla|La Mesa|La Mirada|La Palma|La Presa|La Puente|La Quinta|La Riviera|La Verne|Lafayette|Laguna Beach|Laguna Hills|Laguna Niguel|Laguna Woods|Laguna|Lake Elsinore|Lake Forest|Lake Los Angeles|Lakehead|Lakeside|Lakewood|Lamont|Lancaster|Larkspur|Lathrop|Lawndale|Laytonville|Lemon Grove|Lemoore|Lennox|Lincoln|Linda|Lindsay|Live Oak|Livermore|Livingston|Lodi|Loma Linda|Lomita|Lompoc|Long Beach|Los Alamitos|Los Altos|Los Angeles|Los Banos|Los Gatos|Lynwood|Madera|Magalia|Malibu|Manhattan Beach|Manteca|Marina Del Rey|Marina|Martinez|Marysville|Maywood|McKinleyville|Menlo Park|Merced|Mill Valley|Millbrae|Milpitas|Mineral|Mira Loma|Mission Viejo|Modesto|Monrovia|Montclair|Montebello|Montecito|Monterey Park|Monterey|Moorpark|Moraga|Moreno Valley|Morgan Hill|Morro Bay|Mount Aukum|Mountain View|Murrieta|Napa|National City|Newark|Newberry Springs|Newport Beach|Nicolaus|Nipomo|Norco|North Auburn|North Fair Oaks|North Highlands|North Hollywood|Northridge|Norwalk|Novato|Oakdale|Oakland|Oakley|Oceanside|Oildale|Olivehurst|Ontario|Orangevale|Orange|Orcutt|Orinda|Orleans|Oroville|Oxnard|Pacific Grove|Pacifica|Pacoima|Palm Desert|Palm Springs|Palmdale|Palo Alto|Palos Verdes Estates|Panorama City|Paradise|Paradise, NVParamount|Parkway-South Sacramento|Parlier|Pasadena|Paso Robles|Patterson|Pedley|Perris|Petaluma|Philo|Pico Rivera|Piedmont|Pine Valley|Pinecrest|Pinole|Pioneer|Pittsburg|Placentia|Placerville|Playa Del Rey|Pleasant Hill|Pleasanton|Pomona|Port Hueneme|Porterville|Portola|Poway|Prunedale|Ramona|Rancho Cordova|Rancho Cucamonga|Rancho Mirage|Rancho Palos Verdes|Rancho San Diego|Rancho Santa Margarita|Red Bluff|Redding|Redlands|Redondo Beach|Redwood City|Redwood Estates|Reedley|Rialto|Richmond|Ridgecrest|Rio Linda|Ripon|Riverbank|Riverdale|Riverside|Rocklin|Rodeo|Rohnert Park|Rosamond|Rosemead|Rosemont|Roseville|Rossmoor|Rowland Heights|Rubidoux|Sacramento|Salida|Salinas|San Andreas|San Anselmo|San Bernardino|San Bruno|San Carlos|San Clemente|San Diego|San Dimas|San Fernando|San Francisco|San Gabriel|San Jacinto|San Jose|San Juan Capistrano|San Leandro|San Lorenzo|San Luis Obispo|San Marcos|San Marino|San Mateo|San Pablo|San Pedro|San Quentin|San Rafael|San Ramon|San Ysidro|Sanger|Santa Ana|Santa Barbara|Santa Clara|Santa Clarita|Santa Cruz|Santa Fe Springs|Santa Maria|Santa Monica|Santa Paula|Santa Rosa|Santee|Saratoga,|Sausalito|Scotts Valley|Seal Beach|Seaside|Sebastopol|Selma|Shafter|Sherman Oaks|Sierra Madre|Simi Valley|Solana Beach|Soledad|Sonora|South El Monte|South Gate|South Lake Tahoe|South Pasadena|South San Francisco|South San Jose Hills|South Whittier|South Yuba City|Spring Valley|Springville|Stanford|Stanton|Stockton|Suisun City|Sun City|Sun Valley|Sunnyvale|Susanville|Sutter Creek|Sylmar|Tamalpais-Homestead Valley|Tecate|Tehachapi|Temecula|Temple City|Thousand Oaks|Torrance|Tracy|Truckee|Tulare|Turlock|Tustin Foothills|Tustin|Twentynine Palms|Twin Bridges|Ukiah|Union City|Upland|Upper Lake|Vacaville|Valencia|Valinda|Valle Vista|Vallecito|Vallejo|Valley Springs|Van Nuys|Venice|Ventura|Victorville|View Park-Windsor Hills|Vincent|Vineyard|Visalia|Vista|Walnut Creek|Walnut Park|Walnut|Warner Springs|Wasco|Watsonville|West Carson|West Covina|West Hollywood|West Puente Valley|West Sacramento|West Whittier-Los Nietos|Westminster|Westmont|Whittier|Wildomar|Willits|Willowbrook|Wilmington|Windsor|Winter Gardens|Woodland Hills|Woodland|Yorba Linda|Yuba City|Yucaipa|Yucca Valley|Eastvale|Calimesa|Jurupa Valley|Menifee");
		}
		if (state.contains("Colorado") || state.contains("CO")) {
			city = Util
					.match(addSec,
							"Severance|Fort Lupton|Clifton|Aguilar|Alamosa|Arvada|Aspen|Aurora|Berkley|Black Forest|Boulder|Brighton|Broomfield|Buena Vista|Canon City|Castle Rock|Castlewood|Cimarron Hills|Colorado Springs|Columbine|Commerce City|Craig|Crested Butte|Crowley|Denver|Dillon|Dinosaur|Durango|Eads|Englewood|Estes Park|Fairplay|Federal Heights|Fort Carson|Fort Collins|Fort Morgan|Fountain|Glenwood Springs|Golden|Grand Junction|Greeley|Greenwood Village|Gunnison|Highlands Ranch|Hugo|Ken Caryl|Lafayette|Lakewood|Laporte|Leadville|Littleton|Longmont|Louisville|Loveland|Monte Vista|Montrose|Northglenn|Pagosa Springs|Parker|Pine|Pueblo West|Pueblo|Salida|San Luis|Security-Widefield|Sherrelwood|Southglenn|Steamboat Springs|Sterling|Thornton|Trinidad|U S A F Academy|Vail|Walden|Walsenburg|Welby|Westminster|Wheat Ridge|Windsor|Woodland Park|Centennial|Elizabeth|Firestone|Frederick|Erie|Timnath|Wellington|Lochbuie");
		}
		if (state.contains("Connecticut") || state.contains("CT")) {
			city = Util
					.match(addSec,
							"Ansonia|Avon|Berlin|Bethel|Bloomfield|Branford|Bridgeport|Bristol|Brookfield|Central Manchester|Cheshire|Clinton|Colchester|Conning Towers-Nautilus Park|Coventry|Cromwell|Danbury|Darien|Derby|East Hampton|East Hartford|East Haven|East Lyme|Ellington|Enfield|Fairfield|Farmington|Gales Ferry|Glastonbury|Granby|Greenwich|Griswold|Groton|Guilford|Hamden|Hartford|Killingly|Ledyard|Madison|Manchester|Mansfield|Meriden|Middletown|Milford|Monroe|Montville|Mystic|Naugatuck|New Britain|New Canaan|New Fairfield|New Haven|New London|New Milford|Newington|Newtown|North Branford|North Haven|Norwalk|Norwich|Old Saybrook|Orange|Plainfield|Plainville|Plymouth|Ridgefield|Rocky Hill|Seymour|Shelton|Simsbury|Somers|South Windsor|Southbury|Southington|Stafford|Stamford|Stonington|Storrs|Stratford|Suffield|Thomaston|Tolland|Torrington|Trumbull|Unionville|Vernon|Wallingford Center|Wallingford|Washington Depot|Waterbury|Waterford|Watertown|West Hartford|West Haven|Weston|Westport|Wethersfield|Willimantic|Wilton|Winchester|Windham|Windsor Locks|Windsor|Winsted|Wolcott");
		}
		if (state.contains("Delaware") || state.contains("DE")) {
			city = Util
					.match(addSec,
							"Magnolia|Frederica|Milton|Smyrna|Milford|Dagsboro|Ocean View|Frankford|Bear|Brookside|Dover|Glasgow|Hockessin|New Castle|Newark|Pike Creek|Selbyville|Wilmington|Townsend|Middletown|Fenwick Island");
		}
		if (state.contains("Florida") || state.contains("FL")) {
			city = Util
					.match(addSec,
							"Seffner|Wildwood|Port St. Joe|Southport|Mulberry|Mt. Dora|Debary|Valrico|Mims|Apollo Beach|Palm Shores|Alachua|Altamonte Springs|Apopka|Atlantic Beach|Auburndale|Aventura|Azalea Park|Bartow|Bayonet Point|Bayshore Gardens|Bellair-Meadowbrook Terrace|Belle Glade|Bellview|Bloomingdale|Boca Del Mar|Boca Raton|Bonita Springs|Boynton Beach|Bradenton Beach|Bradenton|Brandon|Brent|Brooksville|Brownsville|Bunnell|Callaway|Canal Point|Cape Coral|Carol City|Casselberry|Citrus Park|Citrus Ridge|Clearwater|Cocoa Beach|Cocoa|Coconut Creek|Conway|Cooper City|Coral Gables|Coral Springs|Coral Terrace|Country Club|Country Walk|Crestview|Cutler Ridge|Cutler|Cypress Lake|Dade City|Dania Beach|Davie|Daytona Beach|De Bary|De Funiak Springs|De Land|Deerfield Beach|Delray Beach|Deltona|Doral|Dunedin|East Lake|Edgewater|Egypt Lake-Leto|Elfers|Englewood|Ensley|Eustis|Fairview Shores|Fernandina Beach|Ferry Pass|Flagler Beach|Florida Ridge|Forest City|Fort Lauderdale|Fort Myers Beach|Fort Myers|Fort Pierce|Fleming Island|Fort Walton Beach|Fountainbleau|Fruit Cove|Fruitville|Gainesville|Gladeview|Glenvar Heights|Golden Gate|Golden Glades|Gonzalez|Greater Carrollwood|Greater Northdale|Greater Sun Center|Greenacres|Gulf Gate Estates|Gulfport|Haines City|Hallandale|Hamptons at Boca Raton|Hialeah Gardens|Hialeah|Hobe Sound|Holiday|Holly Hill|Hollywood|Homestead|Homosassa Springs|Orlando|Hosford|Hudson|Immokalee|Interlachen|Inverness|Iona|Ives Estates|Jacksonville Beach|Jacksonville|Jasmine Estates|Jensen Beach|Jupiter|Kendale Lakes|Kendall West|Kendall|Key Biscayne|Key Largo|Key West|Keystone|Kings Point|Kissimmee|Lady Lake|Lake Butler|Lake City|Lake Magdalene|Lake Mary|Lake Wales|Lake Worth Corridor|Lake Worth|Lakeland Highlands|Lakeland|Lakewood Park|Land O' Lakes|Largo|Lauderdale Lakes|Lauderhill|Leesburg|Lehigh Acres|Leisure City|Lighthouse Point|Lockhart|Longwood|Lutz|Lynn Haven|Maitland|Marathon|Marco Island|Marco|Margate|Mary Esther|Meadow Woods|Melbourne|Merritt Island|Miami Beach|Miami Lakes|Miami Shores|Miami Springs|Miami Gardens|Miami|Middleburg|Milton|Miramar|Montverde|Mount Dora|Myrtle Grove|Naples|New Port Richey|New Smyrna Beach|Niceville|Nokomis|Norland|North Fort Myers|North Lauderdale|North Miami Beach|North Miami|North Palm Beach|North Port|Oak Ridge|Oakland Park|Ocala|Ocoee|Ojus|Okeechobee|Oldsmar|Olympia Heights|Opa-Locka|Orange City|Orange Park|Ormond Beach|Oviedo|Palatka|Palm Bay|Palm Beach Gardens|Palm Beach|Palm City|Palm Coast|Palm Harbor|Palm River-Clair Mel|Palm Springs|Palm Valley|Palmetto Estates|Palmetto|Panama City|Parkland|Pembroke Pines|Pensacola|Pine Hills|Pinecrest|Pinellas Park|Pinewood|Placida|Plant City|Poinciana|Pompano Beach|Port Charlotte|Port Orange|Port Richey|Port Saint Joe|Port Salerno|Port St. John|Port St. Lucie|Princeton|Punta Gorda|Quincy|Richmond West|Riverview|Riviera Beach|Rockledge|Royal Palm Beach|Ruskin|Safety Harbor|San Carlos Park|Sandalfoot Cove|Sanderson|Sanford|Sarasota Springs|Sarasota|Scott Lake|Sebastian|Sebring|Seminole|South Bradenton|South Daytona|South Miami Heights|South Miami|South Venice|Spring Hill|Stuart|St. Augustine|St. Cloud|St. Petersburg|Summerland Key|Sunny Isles Beach|Sunrise|Sweetwater|Tallahassee|Tamarac|Tamiami|Tampa|Tarpon Springs|Temple Terrace|The Crossings|The Hammocks|Titusville|Town 'n' Country|Union Park|University Park|University|Upper Grand Lagoon|Venice|Vero Beach South|Vero Beach|Villas|Warrington|Wekiwa Springs|Wellington|West and East Lealman|West Little River|West Palm Beach|West Pensacola|Westchase|Westchester|Weston|Westwood Lakes|Wewahitchka|Wilton Manors|Winter Garden|Winter Haven|Winter Park|Winter Springs|Wright|Yulee|Yeehaw Junction|Zephyrhills|Windermere|Grand Island|Saint Cloud|Tavares|Lake Alfred|Davenport|Harmony|Clermont|Deland|Groveland|Lake Helen|West Melbourne|St. Johns|Callahan|Green Cove Springs|Saint Augustine|Beverly Beach|Freeport|Inlet Beach|Santa Rosa Beach|Panama City Beach|Point Washington|Pace|Navarre|Cantonment|Gulf Breeze|Miami Dade County|Haverhill|Port St Lucie|Osprey|Sun City Center|Thonotosassa|Trinity|Wimauma|Gibsonton|Wesley Chapel|Ellenton|Land O Lakes|St Petersburg|Viera|Jacksonville|Lakeside|Plantation|Alva,|Point Washington");
		}
		if (state.contains("Georgia") || state.contains("GA")) {
			city = Util
					.match(addSec,
							"Villa Rica|Lithia Springs|Bishop|Senoia|Stockbridge|Grayson|Austell|Bethlehem|Rex|Loganville|Grovetown|Hephzibah|Acworth|Albany|Alpharetta|Alto|Americus|Athens|Atlanta,|Augusta|Bainbridge|Belvedere Park|Brunswick|Buford|Calhoun|Candler-McAfee|Carrollton|Cartersville|Cochran|College Park|Columbus|Commerce|Conyers|Cordele|Covington|Dahlonega|Dalton|Dawson|Decatur|Demorest|Douglasville|Douglas|Druid Hills|Dublin|Duluth|Dunwoody|Dacula|East Point|Elberton|Evans|Fayetteville|Forest Park|Fort Benning South|Fort Stewart|Gainesville|Garden City|Georgetown|Griffin|Hinesville|Jonesboro|Kennesaw|Kingsland|La Grange|Lawrenceville|Lilburn|Lithonia|Mableton|Macon|Marietta|Martinez|Milledgeville|Monroe|Morrow|Moultrie|Mountain Park|Newnan|Newton|Norcross|North Atlanta|North Decatur|North Druid Hills|Panthersville|Peachtree City|Powder Springs|Redan|Reidsville|Riverdale|Rome|Roswell|Sandy Springs|Savannah|Smyrna|Snellville|Statesboro|Stone Mountain|St. Marys|St. Simons|Sugar Hill|Thomasville|Tifton|Toccoa,|Tucker|Union City|Union Point|Valdosta|Vidalia|Warner Robins|Waycross|Wilmington Island|Winder|Woodstock|Cumming|Hampton|Ellenwood|Hiram|Mcdonough|Dallas|Locust Grove|Fairburn|Flowery Branch|Canton|Suwanee|Byron|Kathleen|Perry|Guyton|Pooler|Richmond Hill");
		}
		if (state.contains("Hawaii") || state.contains("HI")) {
			city = Util
					.match(addSec,
							"Aiea|Ewa Beach|Halawa|Hanalei|Hilo|Honolulu|Kahului|Kailua Kona|Kailua|Kaneohe Station|Kaneohe|Kihei|Lahaina|Laupahoehoe|Lihue|Makakilo City|Makawao|Mililani Town|Nanakuli|Pearl City|Schofield Barracks|Wahiawa|Waianae|Wailuku|Waimalu|Waipahu|Waipio|Poipu - Koloa|Kapolei|Makakilo|Kamuela|Kona/ Kohala Coast");
		}
		if (state.contains("Idaho") || state.contains("ID")) {
			city = Util
					.match(addSec,
							"Ashton|Blackfoot|Boise|Caldwell|Cambridge|Coeur d'Alene|Cottonwood|Culdesac|Dubois|Eagle|Emmett|Fairfield|Garden City|Grace|Grangeville|Harrison|Idaho City|Idaho Falls|Lewiston|Malad City|Meridian|Moore|Moscow|Mountain Home|Nampa|North Fork|Paris|Pocatello|Post Falls|Rexburg|Rupert|Sandpoint|Shoshone|Sun Valley|Twin Falls|Wallace");
		}
		if (state.contains("Illinois") || state.contains("IL")) {
			city = Util
					.match(addSec,
							"Antioch|Yorkville|Addison|Algonquin|Alsip|Alton|Arlington Heights|Aurora|Barrington|Bartlett|Batavia|Beach Park|Beecher City|Belleville|Bellwood|Belvidere|Bensenville|Berwyn|Blandinsville|Bloomingdale|Bloomington|Blue Island|Bolingbrook|Bourbonnais|Bradley|Bridgeview|Brimfield|Brookfield|Buffalo Grove|Burbank|Burr Ridge|Cahokia|Calumet City|Canton|Carbondale|Carol Stream|Carpentersville|Cary|Centralia|Champaign|Charleston|Chicago Heights|Chicago Ridge|Chicago|Cicero|Collinsville|Country Club Hills|Crest Hill|Crestwood|Crystal Lake|Danville|Darien|Decatur|Deerfield|DeKalb|Des Plaines|Dixon|Dolton|Downers Grove|East Moline|East Peoria|East Saint Louis|East St. Louis|Edwardsville|Effingham|Elgin|Elk Grove Village|Elmhurst|Elmwood Park|Evanston|Evergreen Park|Fairview Heights|Flossmoor|Forest Park|Frankfort|Franklin Park|Freeport|Gages Lake|Galesburg|Geneva|Glen Carbon|Glen Ellyn|Glendale Heights|Glenview|Godfrey|Goodings Grove|Granite City|Grayslake|Gurnee|Hanover Park|Harvey|Hazel Crest|Herrin|Hickory Hills|Highland Park|Hillside|Hinsdale|Hoffman Estates|Homewood|Jacksonville|Joliet|Justice|Kankakee|Kewanee|La Grange Park|La Grange|Lake Forest|Lake in the Hills|Lake Zurich|Lansing|Lemont|Libertyville|Lincolnwood|Lincoln|Lindenhurst|Lisle|Lockport|Lombard|Loves Park|Lyons|Machesney Park|Macomb|Marion|Markham|Mascoutah|Matteson|Mattoon|Maywood|McHenry|Melrose Park|Midlothian|Mokena|Moline|Morris|Morton Grove|Morton|Mount Prospect|Mount Vernon|Mundelein|Murphysboro|Naperville|New Lenox|Niles|Normal|Norridge|North Aurora|North Chicago|Northbrook|Northlake|O'Fallon|Oak Forest|Oak Lawn|Oak Park|Orland Park|Oswego|Ottawa|Palatine|Palos Heights|Palos Hills|Palos Park|Park Forest|Park Ridge|Pekin|Peoria|Petersburg|Plainfield|Pontiac|Prairie Du Rocher|Prospect Heights|Quincy|Rantoul|Richton Park|River Forest|River Grove|Riverdale|Rock Island|Rockford|Rolling Meadows|Romeoville|Roselle|Round Lake Beach|Sauk Village|Schaumburg|Schiller Park|Skokie|South Elgin|South Holland|Springfield|Sterling|Streamwood|Streator|St. Charles|Summit|Swansea|Sycamore|Taylorville|Tinley Park|Urbana|Vernon Hills|Villa Park|Warrenville|Washington|Waukegan|West Chicago|Westchester|Western Springs|Westmont|Wheaton|Wheeling|Wilmette|Winnetka|Wood Dale|Wood River|Woodfield-Schaumburg|Woodridge|Woodstock|Worth|Zion|Pingree Grove|Volo|Huntley|Glencoe|Woodridge");
		}
		if (state.contains("Indiana") || state.contains("IN")) {
			city = Util
					.match(addSec,
							"Schereville|Anderson|Auburn|Bedford|Beech Grove|Birdseye|Bloomington|Brownsburg|Carmel|Chesterton|Clarksville|Columbus|Connersville|Crawfordsville|Crown Point|Dyer|Earl Park|East Chicago|Elizabethtown|Elkhart|Evansville|Fishers|Fort Wayne|Fowler|Frankfort|Franklin|Gary|Goshen|Granger|Greenfield|Greensburg|Greenwood|Griffith|Hammond|Highland|Hobart|Huntington|Indianapolis|Jasper|Jeffersonville|Kokomo|La Porte|Lafayette|Lake Station|Lawrence|Lebanon|Logansport|Lowell|Madison|Marion|Martinsville|Merrillville|Michigan City|Mishawaka|Muncie|Munster|New Albany|New Castle|New Haven|Newburgh|Noblesville|Peru|Plainfield|Portage|Portland|Princeton|Richmond|Schererville|Seymour|Shelbyville|South Bend|Speedway|Sullivan|Terre Haute|Valparaiso|Vincennes|Wabash|Warsaw|Washington|West Lafayette");
		}
		if (state.contains("Iowa") || state.contains("IA")) {
			city = Util
					.match(addSec,
							"Alden|Altoona|Amana|Ames|Ankeny|Bettendorf|Boone|Burlington|Carroll|Cedar Falls|Cedar Rapids|Charles City|Churdan|Clinton|Clive|Collins|Coralville|Council Bluffs|Davenport|Davis City|Des Moines|Dubuque|Fort Dodge|Fort Madison|Grinnell|Indianola|Iowa City|Keokuk|Logan|Manning|Marion|Marshalltown|Mason City|Mingo|Montezuma|Muscatine|New Hampton|Newton|Ocheyedan|Oskaloosa|Ottumwa|Red Oak|Sheldon|Shenandoah|Shenandoah|Sioux City|Spencer|Storm Lake|Thurman|Underwood|Union|Urbandale|Waterloo|West Des Moines");
		}
		if (state.contains("Kansas") || state.contains("KS")) {
			city = Util
					.match(addSec,
							"Alma|Arkansas City|Atchison|Beloit|Caldwell|Coffeyville|De Soto|Derby|Dodge City|El Dorado|Emporia|Enterprise|Garden City|Gardner|Great Bend|Hays|Hiawatha|Hutchinson|Junction City|Kansas City|Kingman|Lawrence|Leavenworth|Leawood|Lenexa|Liberal|Madison|Manhattan|Marysville|McPherson|Merriam|Newton|Olathe|Ottawa|Overland Park|Parsons|Phillipsburg|Pittsburg|Prairie Village|Salina|Shawnee|Topeka|Westmoreland|Wichita|Winfield");
		}
		if (state.contains("Kentucky") || state.contains("KY")) {
			city = Util
					.match(addSec,
							"Ashland|Barbourville|Bardstown|Berea|Bowling Green|Burlington|Burnside|Campbellsville|Carrollton|Covington|Danville|Elizabethtown|Erlanger|Fern Creek|Florence|Fort Campbell North|Fort Knox|Fort Thomas|Frankfort|Georgetown|Glasgow|Harlan|Henderson|Highview|Hopkinsville|Independence|Jeffersontown|La Grange|Lancaster|Lexington|London|Louisa|Louisville|Madisonville|Mayfield|Middlesborough|Murray|Newburg|Newport|Nicholasville|Okolona|Owensboro|Paducah|Pikeville|Pleasure Ridge Park|Radcliff|Richmond|Shelbyville|Shively|Somerset|Stanton|St. Matthews|Valley Station|Winchester");
		}
		if (state.contains("Louisiana") || state.contains("LA")) {
			city = Util
					.match(addSec,
							"Rayne|Abbeville|Alexandria|Angie|Baker|Bastrop|Baton Rouge|Bayou Cane|Bogalusa|Bossier City|Boyce|Chalmette|Convent|Covington|Crowley|Denham Springs|Destrehan|Estelle|Eunice|Fort Polk South|Gonzales|Gretna|Hammond|Harvey|Houma|Jefferson|Jennings|Kenner|La Place|Lafayette|Lake Charles|Leesville|Logansport|Luling|Mandeville|Marrero|Meraux|Merrydale|Metairie|Minden|Monroe|Morgan City|Moss Bluff|Natchitoches|New Iberia|New Orleans|Opelousas|Pineville|Plaquemine|Raceland|Rayville|Reserve|River Ridge|Ruston|Shreveport|Slidell|Sulphur|Tallulah|Terrytown|Thibodaux|Timberlane|West Monroe|Westwego|Woodmere|Zachary|From Baton Rouge|Addis|Prairieville|Watson|Madisonville|Ponchatoula|Robert|Duson|Carencro|Youngsville");
		}
		if (state.contains("Maine") || state.contains("ME")) {
			city = Util
					.match(addSec,
							"Ashland|Auburn|Augusta|Bangor|Biddeford|Brunswick|Castine|Danforth|Falmouth|Freeport|Gorham|Greenville|Guilford|Jay|Kennebunk|Lewiston|Limestone|Machias|Orono|Portland|Princeton|Saco|Sanford|Scarborough|South Portland|Southwest Harbor|Stonington|Surry|Waterville|Westbrook|Windham|York");
		}
		if (state.contains("Maryland") || state.contains("MD")) {
			city = Util
					.match(addSec,
							"Accokeek|Edgewater|White Plains|Ijamsville|Marriottsville|Sykesville|Clarksburg|Laytonsville|Clarksville|Frankford|Millersville|Aberdeen Proving Ground|Aberdeen|Adelphi|Annapolis|Arbutus|Arnold|Aspen Hill|Ballenger Creek|Baltimore|Bel Air North|Bel Air South|Bel Air|Beltsville|Bethesda|Bowie|Brooklyn Park|Calverton|Cambridge|Camp Springs|Carney|Catonsville|Chesapeake Ranch Estates-Drum Point|Chillum|Church Hill|Clinton|Cockeysville|Colesville|College Park|Columbia|Coral Hills|Crofton|Cumberland|Damascus|Dundalk|East Riverdale|Easton|Edgewood|Eldersburg|Elkridge|Elkton|Ellicott City|Essex|Fairland|Ferndale|Forestville|Fort Washington|Frederick|Fulton|Friendly|Gaithersburg|Germantown|Glen Burnie|Glenn Dale|Greater Landover|Greater Upper Marlboro|Green Haven|Green Valley|Greenbelt|Hagerstown|Halfway|Havre de Grace|Hillcrest Heights|Hyattsville|Joppatowne|Kettering|Lake Shore|Langley Park|Lanham-Seabrook|Lansdowne-Baltimore Highlands|Laurel|Lexington Park|Linganore-Bartonsville|Lochearn|Lutherville-Timonium|Manchester|Mays Chapel|Middle River|Milford Mill|Montgomery Village|New Carrollton|North Bethesda|North Laurel|North Potomac|Ocean Pines|Odenton|Olney|Overlea|Owings Mills|Oxon Hill-Glassmanor|Parkville|Parole|Pasadena|Perry Hall|Pikesville|Potomac|Randallstown|Redland|Reisterstown|Ridgely|Riviera Beach|Rockville|Rosaryville|Rosedale|Rossville|Salisbury|Savage-Guilford|Severna Park|Severn|Silver Spring|South Gate|South Laurel|St. Charles|Suitland-Silver Hill|Takoma Park|Towson|Upper Marlboro|Waldorf|Walker Mill|Westernport|Westminster|Wheaton-Glenmont|White Oak|Woodlawn|Woodstock|New Market|Brandywine|Glenarden|Mitchellville|Cooksville|Hanover|Marriottsville");

		}
		if (state.contains("Massachusetts") || state.contains("MA")) {
			city = Util
					.match(addSec,
							"Abington|Acton|Acushnet|Agawam|Amesbury|Amherst Center|Amherst|Andover|Arlington|Ashburnham|Ashland|Athol|Attleboro|Auburn|Ayer|Barnstable Town|Bedford|Belchertown|Bellingham|Belmont|Beverly|Billerica|Boston|Bourne|Braintree|Brewster|Bridgewater|Brockton|Brookline|Burlington|Buzzards Bay|Cambridge|Canton|Carver|Centerville|Charlemont|Charlton|Chelmsford|Chelsea|Chicopee|Clinton|Concord|Dalton|Danvers|Dartmouth|Dedham|Dennis|Dracut|Dudley|Duxbury|East Bridgewater|East Longmeadow|Easthampton|Easton|Everett|Fairhaven|Fall River|Falmouth|Fitchburg|Foxborough|Framingham|Franklin|Gardner|Gloucester|Grafton|Greenfield|Groton|Hanover|Harwich|Haverhill|Hingham|Holbrook|Holden|Holliston|Holyoke|Hopkinton|Hudson|Hull|Ipswich|John Fitzgerald Kennedy|Kingston|Lawrence|Lee|Leicester|Leominster|Lexington|Longmeadow|Lowell|Ludlow|Lynnfield|Lynn|Malden|Mansfield|Marblehead|Marlborough|Marshfield|Mashpee|Maynard|Medfield|Medford|Medway|Melrose|Methuen|Middlesex Essex Gmf|Milford|Millbury|Milton|Nantucket|Natick|Needham|New Bedford|Newburyport|Newtonville|Newton|Norfolk|North Adams|North Andover|North Attleborough Center|North Dighton|North Easton|North Falmouth|North Reading|Northampton|Northborough|Northbridge|Northfield|Norton|Norwood|Orange|Oxford|Palmer|Peabody|Pembroke|Pepperell|Pittsfield|Plymouth|Quincy|Randolph|Raynham|Reading|Rehoboth|Revere|Rockland|Salem|Sandwich|Saugus|Scituate|Seekonk|Sharon|Shrewsbury|Somerset|Somerville|South Egremont|South Hadley|South Yarmouth|Southborough|Southbridge|Spencer|Springfield|Stockbridge|Stoneham|Stoughton|Sudbury|Swampscott|Swansea|Taunton|Tewksbury|Turners Falls|Tyngsborough|Uxbridge|Vineyard Haven|Wakefield|Walpole|Waltham|Wareham|Warren|Watertown|Wayland|Webster|Wellesley|West Springfield|West Upton|Westborough|Westfield|Westford|Weston|Westport|Westwood|Weymouth|Whitman|Wilbraham|Wilmington|Winchester|Winthrop|Woburn|Worcester|Wrentham|Yarmouth");
		}
		if (state.contains("Michigan") || state.contains("MI")) {
			city = Util
					.match(addSec,
							"Ada|Adrian|Algonac|Allen Park|Allendale|Alma|Alpena|Alpine|Ann Arbor|Antwerp|Attica|Auburn Hills|Bangor|Battle Creek|Bay City|Bedford|Beecher|Benton Harbor|Benton|Berkley|Berrien Springs|Beverly Hills|Big Rapids|Birmingham|Blackman|Bloomfield Hills|Bloomfield Township|Bloomfield|Brandon|Bridgeport|Brighton|Brimley|Brownstown|Buena Vista|Burton|Byron|Cadillac|Cannon|Canton|Cascade|Charlevoix|Chesterfield|Clawson|Clinton|Coldwater|Commerce|Comstock Park|Comstock|Cutlerville|Davison|De Witt|Dearborn Heights|Dearborn|Delhi|Delta|Detroit|East Grand Rapids|East Lansing|East Tawas|Eastpointe|Eckerman|Ecorse|Emmett|Escanaba|Farmington Hills|Farmington|Fenton|Ferndale|Flint|Flushing|Forest Hills|Fort Gratiot|Frankenmuth|Fraser|Frenchtown|Fruitport|Gaines|Garden City|Garfield|Genesee|Genoa|Georgetown|Grand Blanc|Grand Haven|Grand Rapids|Grandville|Grayling|Green Oak|Grosse Ile|Grosse Pointe Park|Grosse Pointe Woods|Grosse Pointe|Hamburg|Hamtramck|Harbor Springs|Harper Woods|Harrison|Hartland|Haslett|Hazel Park|Highland Park|Highland|Holland|Holly|Holt|Houghton|Howell|Huron|Independence|Inkster|Ionia|Jackson|Jenison|Kalamazoo|Kentwood|Kincheloe|Lansing|Leoni|Lincoln Park|Lincoln|Livonia|Lyon|Macomb|Madison Heights|Marquette|Marshall|Melvindale|Meridian|Midland|Milford|Minden City|Monitor|Monroe|Mount Clemens|Mount Morris|Mount Pleasant|Mundy|Muskegon Heights|Muskegon|Niles|Northview|Northville|Norton Shores|Novi|Oak Park|Oakland|Okemos|Orion|Oscoda|Oshtemo|Owosso|Oxford|Park|Pittsfield|Plainfield|Plymouth Township|Plymouth|Pontiac|Port Huron|Portage|Redford|Rhodes|Riverview|Rochester Hills|Rochester|Rockford|Romulus|Roseville|Royal Oak|Saginaw Township North|Saginaw Township South|Saginaw|Sault Ste. Marie|Scio|Shelby|South Lyon|Southfield|Southgate|Spring Lake|Springfield|Sterling Heights|Sturgis|St. Clair Shores|St. Joseph|Summit|Sumpter|Superior|Taylor|Texas|Thomas|Traverse City|Trenton|Troy|Utica|Van Buren|Vassar|Vienna|Walker|Warren|Washington|Waterford|Waverly|Wayne|West Bloomfield Township|West Bloomfield|Westland|White Lake|Whitehall|Wixom|Woodhaven|Wyandotte|Wyoming|Ypsilanti");
		}
		if (state.contains("Minnesota") || state.contains("MN")) {
			city = Util
					.match(addSec,
							"Dundas|Albert Lea|Andover|Anoka|Apple Valley|Austin|Bemidji|Blaine|Bloomington|Brainerd|Brook Park|Brooklyn Center|Brooklyn Park|Buffalo|Burnsville|Center City|Champlin|Chanhassen|Chaska|Cloquet|Columbia Heights|Cook|Coon Rapids|Cottage Grove|Cotton|Crystal|Deer River|Detroit Lakes|Duluth|Eagan|East Bethel|Eden Prairie|Edina|Elk River|Fairmont|Faribault|Farmington|Fergus Falls|Fridley|Golden Valley|Graceville|Grand Rapids|Grasston|Ham Lake|Hastings|Hibbing|Hill City|Hopkins|Howard Lake|Hugo|Hutchinson|Inver Grove Heights|Kerrick|Lakeville|Lino Lakes|Long Prairie|Mankato|Maple Grove|Maple Plain|Maplewood|Marshall|Meadowlands|Mendota Heights|Minneapolis|Minnetonka|Monticello|Moorhead|Motley|Mounds View|New Brighton|New Hope|New Ulm|North Mankato|North St. Paul|Northfield|Norwood|Oakdale|Orr|Owatonna|Plymouth|Prior Lake|Ramsey|Red Wing|Richfield|Robbinsdale|Rochester|Roosevelt|Rosemount|Roseville|Rush City|Sauk Centre|Sauk Rapids|Savage|Sebeka|Shakopee|Shevlin|Shoreview|Silver Bay|Solway|South St. Paul|Stillwater|St. Cloud|St. Louis Park|St. Paul|Vadnais Heights|Virginia|West St. Paul|White Bear Lake|White Bear|Willmar|Winona|Woodbury|Worthington|Wrenshall|Young America|Otsego|St. Michael|Minnetrista");
		}
		if (state.contains("Mississippi") || state.contains("MS")) {
			city = Util
					.match(addSec,
							"Bay Saint Louis|Biloxi|Brandon|Canton|Clarksdale|Cleveland|Clinton|Columbus|Corinth|Drew|Flora|Gautier|Greenville|Greenwood|Grenada|Gulfport|Hattiesburg|Hermanville|Horn Lake|Indianola|Jackson|Laurel|Long Beach|Madison|Magee|McComb|Meridian|Moss Point|Natchez|Ocean Springs|Olive Branch|Oxford|Pascagoula|Pearl|Picayune|Ridgeland|Rolling Fork|Southaven|Starkville|Tupelo|Vicksburg|Walls|West Point|Wiggins|Woodville|Yazoo City|D'iberville|Pass Christian");
		}
		if (state.contains("Missouri") || state.contains("MO")) {
			city = Util
					.match(addSec,
							"Moberly|Affton|Arnold|Ballwin|Bellefontaine Neighbors|Belton|Berkeley|Blue Springs|Bridgeton|Butler|Cape Girardeau|Carthage|Chesterfield|Chillicothe|Clayton|Columbia|Concord|Craig|Crestwood|Creve Coeur|Excelsior Springs|Farmington|Fenton|Ferguson|Florissant|Forest City|Fort Leonard Wood|Fulton|Gladstone|Grandview|Grant City|Hannibal|Hazelwood|Independence|Jackson|Jefferson City|Jennings|Joplin|Kansas City|Kennett|Kirksville|Kirkwood|Lake St. Louis|Lamar|Lebanon|Lee's Summit|Lemay|Liberty|Manchester|Marshall|Maryland Heights|Maryville|Maysville|Mehlville|Mexico|Neosho|Newtown|Nixa|Norborne|O' Fallon|Oakville|Osceola|Overland|Poplar Bluff|Raymore|Raytown|Rock Port|Rolla|Sedalia|Sikeston|Spanish Lake|Springfield|St. Ann|St. Charles|St. Joseph|St. Louis|St. Peters|Town and Country|Trenton|University City|Warrensburg|Washington|Webster Groves|Wentzville|West Plains|Wildwood");
		}
		if (state.contains("Montana") || state.contains("MT")) {
			city = Util
					.match(addSec,
							"Absarokee|Arlee|Ashland|Billings|Bonner|Bozeman|Broadus|Butte|Columbia Falls|Cooke City|Cut Bank|Deer Lodge|Dillon|East Helena|Fallon|Frenchtown|Glasgow|Great Falls|Hamilton|Hardin|Helena|Kalispell|Laurel|Lewistown|Miles City|Missoula|Red Lodge|Roberts|Roundup|Scobey|Toston|West Glacier|Winnett");
		}
		if (state.contains("Nebraska") || state.contains("NE")) {
			city = Util
					.match(addSec,
							"Bassett|Beatrice|Bellevue|Blair|Bruno|Chalco|Columbus|Fremont|Grand Island|Hastings|Holdrege|Kearney|La Vista|Lexington|Lincoln|Lyman|Norfolk|North Bend|North Platte|Omaha|Papillion|Scottsbluff|Sidney|South Sioux City|Valentine|Valley");
		}
		if (state.contains("Nevada") || state.contains("NV")) {
			city = Util
					.match(addSec,
							"Crystal Bay|Carson City|Henderson|Indian Springs|Jean|Pahrump|Boulder City|Elko|Enterprise|Gardnerville Ranchos|Hawthorne|Las Vegas|North Las Vegas|Reno|Searchlight|Sparks|Spring Creek|Spring Valley|Sun Valley|Sunrise Manor|Whitney|Winchester|Zephyr Cove|Alamo|Amargosa Valley|Ash Springs|Austin|Baker|Battle Mountain|Beatty|Beowawe|Blue Diamond|Boulder City|Bunkerville|Cal-Nev-Ari|Caliente|Carlin|Carson City|Cold Springs|Crystal|Crystal Bay|Dayton|Denio|Duckwater|Dyer|East|Ely|Elko|Empire|Enterprise|Eureka|Fallon|Fernley|Gabbs|Gardnervillle|Gerlach|Golden Valley|Goldfield|Goodsprings|Hawthorne|Henderson|Imlay|Incline Village|Indian Hills|Indian Springs|Jackpot|Jarbidge|Jean|Jiggs|Johnson Lane|Kingsbury|Las Vegas|Laughlin|Lemmon Valley|Logandale|Lovelock|Lund|McDermitt|McGill|Mesquite|Minden|Moapa Town|Moapa Valley|Montello|Mount Charleston|Nixon|North Las Vegas|Orovada|Overton|Owyhee|Pahrump|Panaca|Paradise|Pioche|Rachel|Reno|Round Hill Village|Round Mountain|Sandy Valley|Schurz|Searchlight|Silver Park|Silver Springs|Sloan|Smith|Spanish Springs|Sparks|Spring Creek|Spring Valley|Stateline|Summerlin South|Sun Valley|Sunrise Manor|Sutcliffe|Tonopah|Tuscarora|Verdi|Virginia City|Wadsworth|Wells|West Wendover|Winnemucca|Whitney|Winchester|Yerington|Zephyr Cove");
		}
		if (state.contains("New Hampshire") || state.contains("NH")) {
			city = Util
					.match(addSec,
							"Amherst|Bedford|Berlin|Claremont|Concord|Conway|Cornish Flat|Derry|Dover|Durham|Exeter|Goffstown|Hampton|Hanover|Hooksett|Hudson|Keene|Laconia|Lebanon|Londonderry|Madison|Manchester|Merrimack|Milford|Nashua|Pelham|Peterborough|Portsmouth|Rochester|Salem|Somersworth|Twin Mountain|Walpole|Windham|Wolfeboro");
		}
		if (state.contains("New Jersey") || state.contains("NJ")) {
			city = Util
					.match(addSec,
							"Manahawkin|Monroe Township|Evesham Township|Mount Olive|Aberdeen|Allenhurst|Asbury Park|Atlantic City|Avenel|Barclay-Kingston|Barnegat|Basking Ridge|Bayonne|Beachwood|Belleville|Bellmawr|Belmar|Bergenfield|Berkeley Heights|Berkeley|Bernards Township|Bloomfield|Bound Brook|Branchburg|Branchville|Brick|Bridgeton|Bridgewater|Brigantine|Browns Mills|Burlington|Caldwell|Camden|Carteret|Cedar Grove|Chatham|Cherry Hill Mall|Cherry Hill|Cinnaminson|City of Orange|Clark|Cliffside Park|Clifton|Clinton|Collingswood|Colonia|Colts Neck|Cranbury|Cranford|Delran|Denville|Deptford|Dover|Dumont|East Brunswick|East Hanover|East Orange|East Windsor|Eatontown|Echelon|Edison|Egg Harbor Township|Egg Harbor|Elizabeth|Elmwood Park|Englewood|Evesham|Ewing|Fair Lawn|Fairview|Florence|Fords|Fort Lee|Franklin Lakes|Franklin|Freehold|Galloway|Garfield|Glassboro|Glen Rock|Gloucester City|Gloucester|Greentree|Guttenberg|Hackensack|Hackettstown|Haddonfield|Haddon|Haledon|Hamilton|Hammonton|Hanover|Harrison|Hasbrouck Heights|Hawthorne|Hazlet|Highland Park|Hillsborough|Hillsdale|Hillside|Hoboken|Holiday City-Berkeley|Holmdel|Hopatcong|Hopewell|Howell|Irvington|Iselin|Jackson|Jefferson|Jersey City|Keansburg|Kearny|Kenilworth|Kirkwood Voorhees|Lacey|Lakehurst|Lakewood|Lawrence|Leisure Village West-Pine Lake Park|Lincoln Park|Lindenwold|Linden|Linwood|Little Egg Harbor|Little Falls|Little Ferry|Livingston|Lodi|Long Branch|Lower|Lumberton|Lyndhurst|Madison|Mahwah|Manalapan|Manchester|Mantua|Manville|Maple Shade|Maplewood|Marlboro|Marlton|Medford|Mercerville-Hamilton Square|Metuchen|Middlesex|Middletown|Middle|Millburn|Millville|Monroe|Montclair|Montgomery|Montville|Moorestown-Lenola|Moorestown|Morganville|Morristown|Morris|Mount Holly|Mount Laurel|Neptune|New Brunswick|New Milford|New Providence|Newark|North Arlington|North Bergen|North Brunswick Township|North Brunswick|North Plainfield|Nutley|Oakland|Ocean Acres|Ocean City|Ocean|Old Bridge|Orange|Palisades Park|Paramus|Parsippany-Troy Hills|Passaic|Paterson|Pemberton|Pennsauken|Pennsville|Pequannock|Perth Amboy|Phillipsburg|Pine Hill|Piscataway|Plainfield|Plainsboro|Pleasantville|Point Pleasant|Pompton Lakes|Princeton Meadows|Princeton|Rahway|Ramsey|Randolph|Raritan|Readington|Red Bank|Ridgefield Park|Ridgefield|Ridgewood|Ringwood|River Edge|Riverside|Rockaway|Roselle Park|Roselle|Roxbury|Rutherford|Saddle Brook|Sayreville|Scotch Plains|Secaucus|Somers Point|Somerset|Somerville|South Amboy|South Brunswick|South Orange Village|South Orange|South Plainfield|South River|Southampton|Sparta|Springdale|Springfield|Stafford|Succasunna-Kenvil|Summit|Teaneck|Tenafly|Tinton Falls|Toms River|Totowa|Trenton|Union City|Union|Upper|Ventnor City|Vernon|Verona|Vineland|Voorhees|Wallington|Wall|Wanaque|Wantage|Warren|Washington|Waterford|Wayne|Weehawken|West Caldwell|West Deptford|West Freehold|West Milford|West New York|West Orange|West Paterson|West Windsor|Westfield|Westwood|Williamstown|Willingboro|Winslow|Woodbridge|Woodbury|Wyckoff|Egg Harbor Township|Manchester Township");
		}
		if (state.contains("New Mexico") || state.contains("NM")) {
			city = Util
					.match(addSec,
							"Deming|Alamogordo|Albuquerque|Anthony|Artesia|Bernalillo|Carlsbad|Cloudcroft|Clovis|Cuba|Eagle Nest|Espanola|Estancia|Farmington|Gallup|Grants|Hobbs|La Mesa|Las Cruces|Las Vegas|Lordsburg|Los Alamos|Los Lunas|Moriarty|North Valley|Penasco|Peralta|Portales|Rio Rancho|Roswell|Santa Fe|Santa Rosa|Silver City|South Valley|Springer|Sunland Park|Tucumcari|Wagon Mound|Watrous");
		}
		if (state.contains("New York") || state.contains("NY")) {
			city = Util
					.match(addSec,
							"Medford|Clay|Albany|Alden|Amherst|Amsterdam|Antwerp|Arcadia|Arlington|Auburn|Aurora|Babylon|Baldwin|Batavia|Bath|Bay Shore|Beacon|Bedford|Beekman|Bellmore|Bethlehem|Bethpage|Binghamton|Blooming Grove|Brentwood|Brighton|Bronx|Brookhaven|Brooklyn|Brunswick|Buffalo|Camillus|Canandaigua|Canton|Carmel|Carthage|Catskill|Centereach|Central Islip|Cheektowaga|Chenango|Chester|Chili|Cicero|Clarence|Clarkstown|Clifton Park|Cohoes|Colonie|Commack|Copiague|Coram|Corning|Cornwall|Cortlandt|Cortland|Coxsackie|Croton-On-Hudson|De Witt|Deer Park|Depew|Dix Hills|Dobbs Ferry|Dryden|Dunkirk|East Fishkill|East Greenbush|East Hampton|East Islip|East Massapequa|East Meadow|East Northport|East Patchogue|East Rockaway|Eastchester|Elma|Elmira|Elmont|Elwood|Endicott|Endwell|Evans|Fairmount|Fallsburg|Far Rockaway|Farmingdale|Farmington|Farmingville|Fishkill|Floral Park|Flushing|Fort Drum|Franklin Square|Fredonia|Freeport|Fultonville|Fulton|Garden City|Gates-North Gates|Gates|Geddes|Geneva|Georgetown|German Flatts|Glen Cove|Glens Falls|Glenville|Gloversville|Goshen|Grand Island|Great Neck|Greece|Greenburgh|Greenlawn|Groveland|Guilderland|Halfmoon|Hamburg|Hampton Bays|Harrison|Hauppauge|Haverstraw|Hempstead|Henrietta|Hicksville|Highlands|Holbrook|Holtsville|Horseheads|Huntington Station|Huntington|Hyde Park|Irondequoit|Islip|Ithaca|Jamaica|Jamestown|Jefferson Valley-Yorktown|Jericho|Johnson City|Keeseville|Kenmore|Kent|Kings Park|Kingsbury|Kingston|Kirkland|Kiryas Joel|La Grange|Lackawanna|Lake Grove|Lake Ronkonkoma|Lancaster|Lansing|Latham|Le Ray|Levittown|Lewisboro|Lewiston|Lindenhurst|Liverpool|Lockport|Long Beach|Long Island City|Lynbrook|Lyon Mountain|Lysander|Malone|Malta|Mamakating|Mamaroneck|Manhattan|Manlius|Manorville|Massapequa Park|Massapequa|Massena|Mastic Beach|Mastic|Melville|Merrick|Middletown|Miller Place|Milton|Mineola|Monroe|Monsey|Montgomery|Moreau|Mount Pleasant|Mount Vernon|Nanuet|Nesconset|New Cassel|New Castle|New City|New Hartford|New Hyde Park|New Paltz|New Rochelle|New Windsor|New York|Newburgh|Newcomb|Niagara Falls|Niskayuna|North Amityville|North Babylon|North Bangor|North Bay Shore|North Bellmore|North Castle|North Greenbush|North Hempstead|North Lawrence|North Lindenhurst|North Massapequa|North Merrick|North New Hyde Park|North Tonawanda|North Valley Stream|North Wantagh|Oceanside|Ogdensburg|Ogden|Olean|Oneida|Oneonta|Onondaga|Orangetown|Orchard Park|Ossining|Oswego|Owego|Oyster Bay|Parma|Patchogue|Patterson|Pearl River|Peekskill|Pelham|Penfield|Perinton|Pittsford|Plainview|Plattsburgh|Pleasantville|Pomfret|Port Chester|Port Washington|Potsdam|Poughkeepsie|Putnam Valley|Queensbury|Queens|Ramapo|Red Hook|Rexford|Ridge|Riverhead|Rochester|Rockville Centre|Rocky Point|Rodman|Rome|Ronkonkoma|Roosevelt|Rotterdam|Rye|Salina|Salisbury|Saranac Lake|Saratoga Springs|Saugerties|Sayville|Scarsdale|Schenectady|Schodack|Seaford|Selden|Setauket-East Setauket|Shawangunk|Shirley|Smithtown|Somers|South Farmingdale|Southampton|Southeast|Southold|Southport|Spring Valley|Staten Island|Stephentown|Stony Brook|Stony Point|St. James|Suffern|Sullivan|Sweden|Syosset|Syracuse|Tarrytown|Terryville|Thompson|Ticonderoga|Tonawanda|Troy|Ulster|Uniondale|Union|Utica|Valley Stream|Van Buren|Vestal|Wallkill|Wantagh|Wappinger|Warwick|Watertown|Watervliet|Wawarsing|Webster|West Babylon|West Haverstraw|West Hempstead|West Islip|West Nyack|West Point|West Seneca|Westbury|Westerlo|Wheatfield|White Plains|Whitestown|Wilton|Woodmere|Wyandanch|Yonkers|Yorktown");
		}
		if (state.contains("North Carolina") || state.contains("NC")) {
			city = Util
					.match(addSec,
							"Sunset Beach|Willow Springs|Stallings|Castle Hayne|Knightdale|Supply|Holden Beach|Holly Ridge|Bolivia|Wesley Chapel|Davie|Forsyth|Marvin|Court King|Clayton|Gibsonville|Oak Ridge|Stanly|Landis|Spencer|Summerfield|Winston- Salem|Glen Lane|Elon|Pineville|Morrisville|Troutman|Locust|McLeansville|Waxhaw|Sherrills Ford|Midland|Wando|Wendell|Pfafftown|Whitsett|Albemarle|Apex|Asheboro|Asheville|Banner Elk|Boone|Burlington|Carrboro|Cary|Chapel Hill|Charlotte|Clemmons|Concord|Cornelius|Creedmoor|Durham|Denver|Dallas|Eden|Elizabeth City|Elm City|Fayetteville|Fort Bragg|Garner|Gastonia|Goldsboro|Graham|Greensboro|Greenville|Havelock|Hendersonville|Henderson|Hertford|Hickory|High Point|Haw River|Hope Mills|Huntersville|Indian Trail|Jacksonville|Kannapolis|Kernersville|Kinston|Laurinburg|Lenoir|Lexington|Lincolnton|Lumberton|Mount Holly|Masonboro|Matthews|Mount HollyMint Hill|Monroe|Mooresville|Morganton|Mount Airy|New Bern|Newton|North Wilkesboro|Piney Green|Pittsboro|Raeford|Raleigh|Reidsville|Roanoke Rapids|Rocky Mount|Rural Hall|Salisbury|Sanford|Sealevel|Shallotte|Shelby|Smithfield|Southern Pines|Southport|Statesville|Tarboro|Thomasville|Wake Forest|Wilmington|Wilson|Winston-Salem|Carolina Shores|Davidson|Belmont|Bunnlevel|Spring Lake|Winston Salem|Swansboro|Sneads Ferry|Zebulon|Leland|Hampstead|Mint Hill|Ogden");
		}
		if (state.contains("North Dakota") || state.contains("ND")) {
			city = Util
					.match(addSec,
							"Bismarck|Dickinson|Fargo|Grand Forks|Gwinner|Jamestown|Mandan|Michigan|Minot|Wahpeton|West Fargo|Williston|Mint Hill");
		}
		if (state.contains("Ohio") || state.contains("OH")) {
			city = Util
					.match(addSec,
							"Akron|Alliance|Amesville|Amherst|Ashland|Ashtabula|Athens|Aurora|Austintown|Avon Lake|Avon|Barberton|Bay Village|Beachwood|Beavercreek|Bedford Heights|Bedford|Bellefontaine|Berea|Bexley|Blue Ash|Boardman|Bowling Green|Brecksville|Bridgetown North|Broadview Heights|Brook Park|Brooklyn|Brunswick|Bucyrus|Cambridge|Canton|Celina|Centerville|Chillicothe|Cincinnati|Circleville|Clayton|Cleveland Heights|Cleveland|Columbus|Conneaut|Continental|Coshocton|Cuyahoga Falls|Dayton|Defiance|Delaware|Dover|Dublin|East Cleveland|East Liverpool|Eastlake|Elyria|Englewood|Euclid|Fairborn|Fairfield|Fairview Park|Findlay|Finneytown|Forest Park|Forestville|Fostoria|Franklin|Fremont|Gahanna|Galion|Garfield Heights|Germantown|Girard|Greenfield|Greenville|Green|Grove City|Hamilton|Hilliard|Huber Heights|Hudson|Ironton|Kent|Kettering|Lakewood|Lancaster|Landen|Langsville|Laurelville|Lebanon|Leipsic|Lima|Lorain|Loveland|Lucasville|Lyndhurst|Mansfield|Maple Heights|Marietta|Marion|Marysville|Mason|Massillon|Maumee|Mayfield Heights|Medina|Mentor|Miamisburg|Middleburg Heights|Middletown|Montgomery|Mount Vernon|Nelsonville|New Philadelphia|Newark|Niles|North Canton|North College Hill|North Olmsted|North Ridgeville|North Royalton|Northbrook|Northfield|Norton|Norwalk|Norwood|Novelty|Oregon|Oxford|Painesville|Parma Heights|Parma|Pataskala|Perrysburg|Piqua|Portsmouth|Ravenna|Rawson|Reading|Reynoldsburg|Richmond Heights|Riverside|Rocky River|Rossburg|Salem|Sandusky|Seven Hills|Shaker Heights|Sharonville|Shiloh|Sidney|Solon|South Euclid|Springboro|Springdale|Springfield|Steubenville|Stow|Streetsboro|Strongsville|Struthers|Sylvania|Tallmadge|Tiffin|Tipp City|Toledo|Trotwood|Troy|Twinsburg|University Heights|Upper Arlington|Urbana|Van Wert|Vandalia|Vermilion|Wadsworth|Warrensville Heights|Warren|Washington|West Carrollton City|Westerville|Westlake|White Oak|Whitehall|Wickliffe|Willard|Willoughby|Willowick|Wilmington|Wooster|Worthington|Xenia|Youngstown|Zanesville");
		}
		if (state.contains("Oklahoma") || state.contains("OK")) {
			city = Util
					.match(addSec,
							"Piedmont|Ada|Altus|Ardmore|Atoka|Bartlesville|Beaver|Bethany|Bixby|Broken Arrow|Chickasha|Chouteau|Claremore|Del City|Duncan|Durant|Edmond|El Reno|Elk City|Enid|Guymon|Lawton|McAlester|Miami|Midwest City|Moore|Muskogee|Mustang|Norman|Nowata|Oklahoma City|Okmulgee|Owasso|Pawhuska|Ponca City|Pryor|Sand Springs|Sapulpa|Seminole|Shawnee|Stillwater|Tahlequah|The Village|Tulsa|Wagoner|Woodward|Yukon|Choctaw");
		}
		if (state.contains("Oregon") || state.contains("OR")) {
			city = Util
					.match(addSec,
							"Clackamas|Albany|Aloha|Altamont|Arlington|Ashland|Baker|Banks|Beaverton|Bend|Burns|Canby|Cannon Beach|Cave Junction|Cedar Mill|Central Point|Chiloquin|Cloverdale|Coos Bay|Corvallis|Cottage Grove|Dallas|Eugene|Forest Grove|Four Corners|Gladstone|Grants Pass|Gresham|Hayesville|Heppner|Hermiston|Hillsboro|Jefferson|Keizer|Klamath Falls|La Grande|Lake Oswego|Lebanon|Lincoln City|Lowell|Lyons|McMinnville|Medford|Mill City|Milwaukie|Newberg|Newport|Oak Grove|Oatfield|Ontario|Oregon City|Pendleton|Portland|Prairie City|Redmond|Roseburg|Salem|Sherwood|Siletz|Silver Lake|Springfield|St. Helens|The Dalles|Tigard|Tillamook|Troutdale|Tualatin|Waldport|West Linn|Wilsonville|Woodburn|Happy Valley|Scappoose|Lancaster|Newtown Square|Bensalem");
		}
		if (state.contains("Pennsylvania") || state.contains("PA")) {
			city = Util
					.match(addSec,
							"Newtown Square|Abington|Aliquippa|Allentown|Altoona|Antrim|Ardmore|Ashville|Aston|Back Mountain|Baldwin|Beaver Springs|Beech Creek|Bellefonte|Bensalem|Berwick|Bethel Park|Bethlehem|Bloomsburg|Blue Bell|Blue Ridge Summit|Boswell|Boyers|Brentwood|Bristol|Broomall|Buckingham|Butler|Caln|Camp Hill|Carlisle|Carnot-Moon|Center|Chambersburg|Cheltenham|Chester|Chestnuthill|Coal|Coatesville|Collegeville|Colonial Park|Columbia|Concordville|Conshohocken|Coolbaugh|Cornwall|Cranberry|Creekside|Cumru|Dallas|Danville|Darby|Derry|Dover|Downingtown|Doylestown|Drexel Hill|Dry Run|Dunmore|East Goshen|East Hempfield|East Lampeter|East Norriton|East Pennsboro|Easton|Easttown|Edinboro|Elizabethtown|Elizabeth|Emmaus|Ephrata|Erie|Exeter|Exton|Fairview|Falls|Ferguson|Fernway|Fort Washington|Franconia|Franklin Park|Frenchville|Fullerton|Genesee|Gettysburg|Greene|Greensburg|Guilford|Hampden|Hampton Township|Hampton|Hanover|Harborcreek|Harleysville|Harrisburg|Harrison Township|Harrison|Hatfield|Haverford|Hazleton|Hempfield|Hermitage|Hershey|Hilltown|Holmes|Hopewell|Horsham|Hustontown|Hyndman|Indiana|Irvine|Irvona|Jeannette|Jersey Shore|Johnstown|Karthaus|King of Prussia|Kingston|Kittanning|Lake Lynn|Lancaster|Langhorne|Lansdale|Lansdowne|Lebanon|Leesport|Levittown|Limerick|Logan|Lower Allen|Lower Burrell|Lower Gwynedd|Lower Macungie|Lower Makefield|Lower Merion|Lower Moreland|Lower Paxton|Lower Pottsgrove|Lower Providence|Lower Salford|Lower Southampton|Loyalsock|Manchester|Manheim|Manor,|Marple|McCandless Township|McCandless|McKeesport|Meadville|Media|Middle Smithfield|Middletown|Mill Hall|Millcreek|Montgomeryville|Montgomery|Moon|Morrisville|Mount Jewett|Mount Lebanon|Mount Pleasant|Mountain Top|Muhlenberg|Muncy Valley|Munhall|Nanticoke|Needmore|Nether Providence Township|Nether Providence|New Britain|New Castle|New Kensington|New Wilmington|Newberry|Newtown|Norristown|North Bend|North Fayette|North Huntingdon|North Lebanon|North Middleton|North Strabane|North Union|North Versailles|North Wales|North Whitehall|Northampton|Nuangola|Oil City|Olanta|Osceola Mills|Palmer|Patton|Penn Hills|Penn|Peters|Philadelphia|Phoenixville|Pittsburgh|Pittston|Plains|Plumstead|Plum|Plymouth|Pottstown|Pottsville|Punxsutawney|Quarryville|Radnor Township|Radnor|Reading|Richland|Ridley|Robinson Township|Robinson|Ronks|Ross Township|Ross|Rostraver|Salisbury|Sandy|Scott Township|Scott|Scranton|Sewickley|Shaler Township|Shaler|Shamokin|Sharon|Shiloh|Silver Spring|Souderton|South Fayette|South Middleton|South Park Township|South Park|South Union|South Whitehall|Southampton|Spring Garden|Spring Grove|Springettsbury|Springfield|Spring|State College|Stroud|St. Marys|Sunbury|Susquehanna|Swatara|Towamencin|Tredyffrin|Trout Run|Twin Rocks|Uniontown|Unity|Upper Allen|Upper Chichester|Upper Darby|Upper Dublin|Upper Gwynedd|Upper Macungie|Upper Merion|Upper Moreland|Upper Providence Township|Upper Providence|Upper Saucon|Upper Southampton|Upper St. Clair|Uwchlan|Valley Forge|Warminster|Warrendale|Warren|Warrington|Warwick|Washington|Wayne|Weigelstown|West Bradford|West Chester|West Deer|West Goshen|West Hempfield|West Lampeter|West Manchester|West Mifflin|West Milton|West Norriton|West Whiteland|Westfield|Westtown|Whitehall|Whitemarsh|White|Whitpain|Wilkes-Barre|Wilkinsburg|Williamsport|Willistown|Willow Grove|Windsor|Woodlyn|Yeadon|York");
		}
		if (state.contains("Rhode Island") || state.contains("RI")) {
			city = Util
					.match(addSec,
							"Barrington|Bristol|Burrillville|Central Falls|Coventry|Cranston|Cumberland|East Greenwich|East Providence|Harrisville|Johnston|Lincoln|Middletown|Narragansett|Newport East|Newport|North Kingstown|North Providence|North Smithfield|Pawtucket|Portsmouth|Providence|Scituate|Smithfield|South Kingstown|Tiverton|Valley Falls|Wakefield|Warren|Warwick|West Warwick|Westerly|Woonsocket");
		}
		if (state.contains("South Carolina") || state.contains("SC")) {
			city = Util
					.match(addSec,
							"Pelzer|Johns Island|Inman|Woodruff|West Ashley|Huger|Simpsonville|Powdersville|Graniteville|Monks Corner|Pineville|Clover|Hopkins|Lyman|Boiling Springs|Wellford|Isle of Palms|Lancaster|Wando|Aiken|Anderson|Beaufort|Berea|Cayce|Charleston|Clemson|Columbia|Conway|Darlington|Dentsville|Easley|Florence|Forest Acres|Gaffney|Gantt|Georgetown|Goose Creek|Greenville|Greenwood|Greer|Hanahan|Hilton Head|Hilton Head Island|Hodges|Honea Path|Irmo|Ladson|Mauldin|Mount Pleasant|Moore|Myrtle Beach|Newberry|North Augusta|North Charleston|North Myrtle Beach|Orangeburg|Parker|Red Hill|Ridgeland|Rock Hill|Seneca|Seven Oaks|Simpsonville|Socastee|Spartanburg|St. Andrews|Summerville|Sumter|Taylors|Wade Hampton|West Columbia|Lake Wylie|Fort Mill|Indian Land|Moncks Corner|Model Homemount Pleasant|James Island|Lexington|Blythewood|Gilbert|Chapin|Elgin|Piedmont|Fountain Inn|Williamston|Duncan|Belton|Ladys Island|Bluffton|Murrells Inlet|Longs|Little River|Surfside Beach|Pawleys Island|Tega Cay|York");
		}
		if (state.contains("South Dakota") || state.contains("SD")) {
			city = Util
					.match(addSec,
							"Aberdeen|Belle Fourche|Beresford|Box Elder|Brandon|Brookings|Canton|Chamberlain|Dell Rapids|Hot Springs|Huron|Lead|Lennox|Madison|Milbank|Mitchell|North Sioux City|Pierre|Rapid City|Redfield|Sioux Falls|Sisseton|Spearfish|Sturgis|Vermillion|Watertown|Winner|Yankton");
		}
		if (state.contains("Tennessee") || state.contains("TN")) {
			city = Util
					.match(addSec,
							"Lenoir City|Pleasant View|Mascot|Corryton|Cleveland|Ardmore|Athens|Bartlett|Bloomingdale|Bolivar|Brentwood|Bristol|Brownsville|Chattanooga|Church Hill|Clarksville|Collierville|Columbia|Cookeville|Crossville|Dickson|Dyersburg|East Brainerd|East Ridge|Elgin|Elizabethton|Farragut|Franklin|Gallatin|Germantown|Goodlettsville|Greeneville|Hendersonville|Jackson|Johnson City|Kingsport|Knoxville|La Vergne|Lawrenceburg|Lebanon|Lewisburg|Madison|Martin|Maryville|McMinnville|Memphis|Middle Valley|Millington|Morristown|Mount Juliet|Mountain City|Murfreesboro|Nashville|Oak Ridge|Red Bank|Sevierville|Shelbyville|Smyrna|Sneedville|Soddy-Daisy|Springfield|Tullahoma|Union City|Cane Ridge|Spring Hill|Nolensville");
		}
		if (state.contains("Texas") || state.contains("TX")) {
			city = Util
					.match(addSec,
							"Pinehurst|Fulshear|Dripping Springs|Bee Cave|Aubrey|Red Oak|New Caney|Argyle|Hackberry|Rosharon|Haslet|Midlothian|Aledo|Lorena|Rosenburg|Spring,|Willis|Lavon|Luling|Ponder|Heath,|Nevada|Anna|Abilene|Addison|Alamo|Aldine|Alice|Allen|Alpine|Alvin|Amarillo|Anderson|Angleton|Arlington|Atascocita|Athens|Austin,|Balch Springs|Bay City|Baytown|Beaumont|Bedford|Beeville|Bellaire|Belton|Benbrook|Big Spring|Boerne|Borger|Brenham|Brownsville|Brownwood|Brushy Creek|Bryan|Burkburnett|Burleson|Canyon Lake|Carrollton|Castroville|Cedar Hill|Cedar Park|Channelview|Channing|Cinco Ranch|Cleburne|Cleveland|Cloverleaf|Clute|College Station|Colleyville|Columbus|Conroe|Converse|Coppell|Copperas Cove|Corinth|Corpus Christi|Corsicana|Cotulla|Cypress|Dallas|Deer Park|Del Rio|Denison|Denton|DeSoto|Dickinson|Donna|Dumas|Duncanville|Eagle Pass|Edinburg|El Campo|El Paso|Euless|Edgecliff Village|Farmers Branch|Flower Mound|Forest Hill|Fort Hood|Fort Worth|Freeport|Friendswood|Frisco|Gainesville|Galena Park|Galveston,|Garland|Gatesville|Georgetown|Gillett|Grand Prairie|Grapevine|Greenville|Groves|Haltom City|Harker Heights|Harlingen|Henderson|Hereford|Hewitt|Hickory Creek|Highland Village|Houston|Humble|Huntsville|Hurst|Irving|Jacinto City|Jacksonville|Jollyville|Katy,|Keller|Kenedy|Kerrville|Kilgore|Killeen|Kingsville|La Homa|La Marque|La Porte|Lake Jackson|Lancaster|Laredo|League City|Leander|Levelland|Lewisville|Lockhart|Longview|Lubbock|Lufkin|Mansfield|Marshall|McAllen|McKinney|Mercedes|Mesquite|Midland|Mineral Wells|Mission Bend|Mission|Missouri City|Mount Pleasant|Nacogdoches|Navasota|Nederland|New Braunfels|New Territory|North Richland Hills|Odessa|Orange|Palestine|Pampa|Paris|Pasadena|Pearland|Pecan Grove|Pflugerville|Pharr|Plainview|Plano|Port Arthur|Port Isabel|Port Lavaca|Port Neches|Portland|Presidio|Raymondville|Richardson|Richmond|Rio Grande City|Roanoke|Robstown|Rockwall|Rosenberg|Round Rock|Round Top|Rowlett|Saginaw|San Angelo|San Antonio|San Benito|San Elizario|San Marcos|Santa Fe|Schertz|Seagoville|Seguin|Sherman|Slaton|Snyder|Socorro|Somerville|South Houston|Southlake|Spring|Stafford|Stamford|Stephenville|Sudan|Sugar Land|Sulphur Springs|Sweetwater|Taylor|Temple|Terrell|Texarkana|Texas City|The Colony|The Woodlands|Three Rivers|Tomball|Tyler|Universal City|University Park|Uvalde|Vernon|Victoria|Vidor|Waco|Watauga|Waxahachie|Weatherford|Wells Branch|Weslaco|West Odessa|West University Place|White Settlement|Wichita Falls|Wilson|Wylie|Elgin|Manor,|Kyle|Briarcreek|Hutto|Bastrop|Sales Office For Info|Spicewood|Oak Point|Princeton|Little Elm|Josephine|Royse City|Van Alstyne|Crossroads|Melissa|Celina|Prosper|Forney|Nevada|Fate|Sachse|Lucas|Horizon City|Jarrell|Azle|Glenn Heights|Northlake|Magnolia|Manvel|Porter|Fresno|Iowa Colony|Montgomery|Cibolo|Troy|Farmersville");
		}
		if (state.contains("Utah") || state.contains("UT")) {
			city = Util
					.match(addSec,
							"Syracuse|Mapleton|Saratoga Springs|Altamont|American Fork|Blanding|Bountiful|Brigham City|Canyon Rim|Cedar City|Centerville|Clearfield|Clinton|Cottonwood Heights|Cottonwood West|Draper|East Millcreek|Farmington|Hatch|Holladay|Kamas|Kanab|Kaysville|Kearns|Layton|Lehi|Logan|Magna|Midvale|Millcreek|Monroe|Morgan|Murray|North Ogden|Ogden|Oquirrh|Orem|Park City|Payson|Pleasant Grove|Provo|Richfield|Riverton|Roy|Salt Lake City|Sandy|South Jordan|South Ogden|South Salt Lake|Spanish Fork|Springville|St. George|Taylorsville|Tooele|Vernal|West Jordan|West Valley City|Herriman|Bluffdale|South Weber|Highland|North Salt Lake|Saratoga,");
		}
		if (state.contains("Vermont") || state.contains("VT")) {
			city = Util
					.match(addSec,
							"Barton|Bennington|Brattleboro|Burlington|Canaan|Chester|Colchester|East Montpelier|Eden|Essex Junction|Essex|Fair Haven|Hartford|Middlebury|Montpelier|Morrisville|Poultney|Readsboro|Rutland|South Burlington|Townshend");
		}
		if (state.contains("Virginia") || state.contains("VA")) {
			city = Util
					.match(addSec,
							"Aldie|Alexandria|Annandale|Arlington|Bailey's Crossroads|Blacksburg|Bon Air|Bowling Green|Boydton|Bristol|Bull Run|Burke|Carrsville|Cave Spring|Centreville|Chantilly|Charlottesville|Chesapeake|Chester|Chincoteague|Christiansburg|Colonial Heights|Dale City|Danville|Dillwyn|East Highland Park|Fairfax|Falls Church|Farmville|Floyd|Stafford|Ford|Fort Hunt|Franconia|Fredericksburg|Front Royal|Glen Allen|Goochland|Groveton|Grundy|Hampton|Harrisonburg|Herndon|Highland Springs|Hollins|Hopewell|Hybla Valley|Idylwood|Jefferson|Keswick|King George|Lake Ridge|Lakeside|Laurel|Leesburg|Lincolnia|Lorton|Louisa|Lynchburg|Madison Heights|Madison|Manassas Park|Manassas|Martinsville|McLean|Meadowview|Mechanicsville|Merrifield|Middletown|Midlothian|Montclair|Montross|Mount Vernon|Newington|Newport News|Norfolk|Norton|Oak Hall|Oakton|Orange|Petersburg|Poquoson|Portsmouth|Purcellville|Quantico|Radford|Red Ash|Reston|Richmond|Roanoke|Rose Hill|Roseland|Salem|Saluda|Scottsville|Sperryville|Springfield|Staunton|Suffolk|Tazewell|Timberlake|Tuckahoe|Tysons Corner|Unionville|Vienna|Virginia Beach|Waynesboro|West Point|West Springfield|Williamsburg|Winchester|Wolf Trap|Woodbridge|Yorktown|Gainesville|Ashburn|Dumfries|Dulles|Oak Hill|Dunn Loring");
		}
		if (state.contains("Washington") || state.contains("WA")) {
			city = Util
					.match(addSec,
							"Cle Elum|Granite Falls|Bainbridge Island|Graham|Edgewood|Ridgefield|Battle Ground|Orting|Aberdeen|Alderwood Manor|Anacortes|Arlington|Auburn|Bainbridge Island|Bellevue|Bellingham|Bothell|Bow|Bremerton|Bryn Mawr-Skyway|Burien|Camano|Camas|Cascade-Fairwood|Centralia|Cottage Lake|Covington|Creston|Des Moines|Dishman|East Hill-Meridian|East Renton Highlands|East Wenatchee Bench|Edmonds|Elk Plain|Ellensburg|Enumclaw|Everett|Farmington|Federal Way|Five Corners|Fort Lewis|Hadlock|Harrington|Hunters|Inglewood-Finn Hill|Issaquah|Kelso|Kenmore|Kennewick|Kent|Kettle Falls|Kingsgate|Kirkland|Lacey|Lacrosse|Lake Forest Park|Lakebay|Lakeland North|Lakeland South|Lakewood|Lea Hill|Long Beach|Longview|Lynnwood|Maple Valley|Martha Lake|Marysville|Mercer Island|Mill Creek|Monroe|Moses Lake|Mount Vernon|Mountlake Terrace|Mukilteo|Naches|Nine Mile Falls|North Creek|North Marysville|Oak Harbor|Odessa|Olympia|Opportunity|Orchards|Othello|Paine Field-Lake Stickney|Parkland|Pasco|Picnic Point-North Lynnwood|Port Angeles|Port Orchard|Prairie Ridge|Pullman|Puyallup|Quincy|Redmond|Renton|Richland|Riverton-Boulevard Park|Rosalia|Salmon Creek|Sammamish|SeaTac|Seattle Hill-Silver Firs|Seattle,|Sequim|Shoreline|Silverdale|South Hill|Spanaway|Spokane|Sprague|Sunnyside|Tacoma|Tukwila|Tumwater|Union Hill-Novelty Hill|University Place|Valleyford|Vancouver|Vashon|Walla Walla|Wenatchee|West Lake Stevens|Lake Stevens|West Valley|White Center|Winthrop|Yakima|Woodinville|Bonney Lake|Snohomish");
		}
		if (state.contains("West Virginia") || state.contains("WV")) {
			city = Util
					.match(addSec,
							"Martinsburg|Berkeley|Aurora|Beckley|Bluefield|Burnsville|Charleston|Clarksburg|Cross Lanes|Fairmont|French Creek|Harpers Ferry|Huntington|Kingwood|Lewisburg|Martinsburg|Morgantown|Parkersburg|Paw Paw|Pineville|Princeton|Rivesville|Rock Cave|Slatyfork|South Charleston|St. Albans|Teays Valley|Vienna|Weirton|Wheeling|White Sulphur Springs");
		}
		if (state.contains("Wisconsin") || state.contains("WI")) {
			city = Util
					.match(addSec,
							"Allouez|Amery|Appleton|Ashwaubenon|Baraboo|Beaver Dam|Bellevue Town|Bellevue|Beloit|Bonduel|Brookfield|Brown Deer|Brownsville|Caledonia|Camp Douglas|Cedarburg|Chippewa Falls|Columbus|Cudahy|Dallas|De Pere|De Soto|Dodgeville|Eau Claire|Ellsworth|Ferryville|Fitchburg|Fond du Lac|Fort Atkinson|Franklin|Friendship|Germantown|Glendale|Glenwood City|Grafton|Grand Chute|Green Bay|Greendale|Greenfield|Hales Corners|Hartford|Howard|Iola|Janesville|Kaukauna|Kenosha|Krakow|La Crosse|Lena|Little Chute|Madison|Manitowoc|Marinette|Marshfield|Mason|Menasha|Menomonee Falls|Menomonie|Mequon|Merrill|Middleton|Milwaukee|Mondovi|Monroe|Mount Pleasant|Muskego|Neenah|New Berlin|New Holstein|Oak Creek|Oconomowoc|Onalaska|Oshkosh|Pewaukee|Pleasant Prairie|Plover|Port Washington|Racine|Randolph|Richfield|River Falls|Sheboygan|Shorewood|Somerset|South Milwaukee|Stevens Point|Stoughton|Sun Prairie|Superior|Two Rivers|Viola|Watertown|Waukesha|Waupun|Wausau|Wauwatosa|West Allis|West Bend|Weston|Whitefish Bay|Whitewater|Winter|Wisconsin Rapids");
		}
		if (state.contains("Wyoming") || state.contains("WY")) {
			city = Util
					.match(addSec,
							"Casper|Cheyenne|Cody|Evanston|Gillette|Green River|Laramie|Rock River|Rock Springs|Sheridan|Basin|Buffalo|Douglas|Kemmerer|Lander|Newcastle|Powell|Rawlins|Riverton|Torrington|Worland");
		}
		return city;
	}






/*	public static String[] getLatLngBingMap1(String add[]) throws Exception {

		String addr = add[0] + " " + add[1] + " " + add[2] + " " + add[3];
		ShatamChrome driver1 = null;
		String bingLatLng[] = { "", "" };
		driver1 = ShatamChrome.getInstance();
		driver1.open("http://localhost/bing.html");
		driver1.wait("#txtWhere");
		driver1.getOne("#txtWhere").sendKeys(addr);
		driver1.wait("#btn");
		driver1.getOne("#btn").click();
		String binghtml = driver1.getHtml();
		// driver.wait(1000);
		U.log(binghtml);
		String secmap = U.getSectionValue(binghtml, "<div id=\"resultsDiv\"",
				"</body>");
		bingLatLng[0] = U.getSectionValue(secmap,
				"<span id=\"span1\">Latitude:", "<br>").trim();
		bingLatLng[1] = U.getSectionValue(secmap, "<br>Longitude:", "</span>");

		U.log("lat:" + bingLatLng[0]);
		U.log("lng:" + bingLatLng[1]);
		return bingLatLng;
	}*/

	public static String[] getCoordinatesGoogleApi(String add[])
			throws IOException {
		String addr = add[0].trim() + "," + add[1].trim() + "," + add[2].trim();
		addr = "https://maps.googleapis.com/maps/api/geocode/json?address="// 1138
																			// Waterlyn
																			// Drive","Clayton","NC
				+ URLEncoder.encode(addr, "UTF-8");
		U.log(addr);
		String html = U.getHTML(addr);
		String locationSec = U.getSectionValue(html, "location\" : {", "}");
		String lat = U.getSectionValue(locationSec, "\"lat\" :", ",");
		String lng = Util.match(locationSec, "lng\" : (\\-\\d+\\.\\d+)", 1);

		locationSec = U.getSectionValue(html, "northeast\" : {", "}");
		String nLat = U.getSectionValue(locationSec, "\"lat\" :", ",");
		String nLng = Util.match(locationSec, "lng\" : (\\-\\d+\\.\\d+)", 1);

		locationSec = U.getSectionValue(html, "southwest\" : {", "}");
		String sLat = U.getSectionValue(locationSec, "\"lat\" :", ",");
		String sLng = Util.match(locationSec, "lng\" : (\\-\\d+\\.\\d+)", 1);

		String latlng[] = { lat, lng, sLat, sLng, nLat, nLng };

		return latlng;
	}

	public static String[] getCoordinatesZip(String zip) throws IOException {
		// addr = add[0].trim() + "," + add[1].trim() + "," + add[2].trim();
		String addr = "https://maps.googleapis.com/maps/api/geocode/json?address="// 1138
																					// Waterlyn
																					// Drive","Clayton","NC
				+ URLEncoder.encode(zip, "UTF-8");
		U.log(addr);
		String html = U.getHTML(addr);
		String locationSec = U.getSectionValue(html, "location\" : {", "}");
		String lat = U.getSectionValue(locationSec, "\"lat\" :", ",");
		String lng = Util.match(locationSec, "lng\" : (\\-\\d+\\.\\d+)", 1);

		locationSec = U.getSectionValue(html, "northeast\" : {", "}");
		String nLat = U.getSectionValue(locationSec, "\"lat\" :", ",");
		String nLng = Util.match(locationSec, "lng\" : (\\-\\d+\\.\\d+)", 1);

		locationSec = U.getSectionValue(html, "southwest\" : {", "}");
		String sLat = U.getSectionValue(locationSec, "\"lat\" :", ",");
		String sLng = Util.match(locationSec, "lng\" : (\\-\\d+\\.\\d+)", 1);

		String latlng[] = { sLat, sLng, nLat, nLng };

		return latlng;
	}

	

	
	public static String getHTMLwithProxy(String path) throws IOException {

		// System.setProperty("http.proxyHost", "104.130.132.119");
		// System.setProperty("http.proxyPort", "3128");
		//System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
		path = path.replaceAll(" ", "%20");
		// U.log(" .............."+path);
		// Thread.sleep(4000);
		String fileName = getCache(path);

		//U.log("filename:::" + fileName);

		File cacheFile = new File(fileName);
		if (cacheFile.exists())
			return FileUtil.readAllText(fileName);

		URL url = new URL(path);

		String html = null;

		// chk responce code

		// int respCode = CheckUrlForHTML(path);
		// U.log("respCode=" + respCode);
			
		{
			//"13.82.16.76", 3128));
			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
					"45.33.90.184",8080));//"23.225.92.19	",30022));//"216.228.69.202",32170));//"198.143.178.77",3128));//"66.82.22.79	",80)
			final URLConnection urlConnection = url.openConnection(proxy);

			// Mimic browser
			try {
				urlConnection
						.addRequestProperty("User-Agent",
								"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:10.0.2) Gecko/20100101 Firefox/10.0.2");
				urlConnection
						.addRequestProperty("Accept", "text/css,*/*;q=0.1");
				urlConnection.addRequestProperty("Accept-Language",
						"en-us,en;q=0.5");
				urlConnection.addRequestProperty("Cache-Control", "max-age=0");
				urlConnection.addRequestProperty("Connection", "keep-alive");
				// U.log("getlink");
				final InputStream inputStream = urlConnection.getInputStream();

				html = IOUtils.toString(inputStream);
				// final String html = toString(inputStream);
				inputStream.close();

				if (!cacheFile.exists())
					FileUtil.writeAllText(fileName, html);

				return html;
			} catch (Exception e) {
				U.log(e);

			}
			return html;
		}
	}
	
	public static String getHTMLwithProxy(String path, String ip, int port) throws IOException {

		// System.setProperty("http.proxyHost", "104.130.132.119");
		// System.setProperty("http.proxyPort", "3128");
		//System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
		path = path.replaceAll(" ", "%20");
		// U.log(" .............."+path);
		// Thread.sleep(4000);
		String fileName = getCache(path);

		//U.log("filename:::" + fileName);

		File cacheFile = new File(fileName);
		if (cacheFile.exists())
			return FileUtil.readAllText(fileName);

		URL url = new URL(path);

		String html = null;

		// chk responce code

		// int respCode = CheckUrlForHTML(path);
		// U.log("respCode=" + respCode);
			
		{
			//"13.82.16.76", 3128));
			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
					ip, port));//"23.225.92.19	",30022));//"216.228.69.202",32170));//"198.143.178.77",3128));//"66.82.22.79	",80)
			final URLConnection urlConnection = url.openConnection(proxy);

			// Mimic browser
			try {
				urlConnection
						.addRequestProperty("User-Agent",
								"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:10.0.2) Gecko/20100101 Firefox/10.0.2");
				urlConnection
						.addRequestProperty("Accept", "text/css,*/*;q=0.1");
				urlConnection.addRequestProperty("Accept-Language",
						"en-us,en;q=0.5");
				urlConnection.addRequestProperty("Cache-Control", "max-age=0");
				urlConnection.addRequestProperty("Connection", "keep-alive");
				// U.log("getlink");
				final InputStream inputStream = urlConnection.getInputStream();

				html = IOUtils.toString(inputStream);
				// final String html = toString(inputStream);
				inputStream.close();

				if (!cacheFile.exists())
					FileUtil.writeAllText(fileName, html);

				return html;
			} catch (Exception e) {
				U.log(e);

			}
			return html;
		}
	}
	
	
	public static String sendPostRequestAcceptJson(String requestUrl, String payload) throws IOException {
		String fileName=U.getCache(requestUrl+payload);
		File cacheFile = new File(fileName);
		//log(fileName);
		if (cacheFile.exists())
			return FileUtil.readAllText(fileName);
		StringBuffer jsonString = new StringBuffer();
	    try {
	        URL url = new URL(requestUrl);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setDoInput(true);
	        connection.setDoOutput(true);
	        connection.setRequestMethod("POST");
//	        connection.setRequestProperty("csrf-token", "I7lZaFQo-GLVTxyV0VKSY21DVTQHTRH3cAHM");
//	        connection.setRequestProperty("ot-originaluri", "/mexico-city-mexico-restaurant-listings");
//	        connection.setRequestProperty("ot-requestid", "9b201e5b-45c3-475a-9fe5-8eb51cfbdf73");
//	        connection.setRequestProperty("x-requested-with", "XMLHttpRequest");
//	        connection.setRequestProperty("user-agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/70.0.3538.77 Chrome/70.0.3538.77 Safari/537.36");
//	        connection.setRequestProperty("cookie", "OT-SessionId=665fec61-b2b7-40fd-88f8-31aa3d514c07; otuvid=A847C352-361E-4A77-BFAA-7EA003EACF6D; notice_preferences=100; notice_behavior=none; _ga=GA1.2.465485978.1562663182; _gid=GA1.2.857089724.1562663182; ak_bmsc=19A565C56B445C5CDEC3FB8164F64981687C36477A1700000859245D58764417~plHir8g753AQ4jnlSBXW7rA28ZxyXq7HPgIJI8QBwx4J+vN9J+CbWshygDarWzDOIhCRc0tWbFqvRAcOHcIPtFR2rHWmobKo+An3LlEE9sPGv3AKJVm75k8/aBvRi3Y8yB4Gh9lfbRmM9qMYfLTwsxhyVz17vM90qmgq1ChYjDIRovuKCYhWmHyDLEHYxd5DPeRrUaAWgEHr4tyhkTkT9rrOBQPLhcHHFjNeIXJfGPnzuUVIN00hsj/atrZtm0GBOC; __gads=ID=c7a0886b4ef2aca9:T=1562663181:S=ALNI_MYU9xFIUi26LDumAw3KFWGyEpFF8Q; OT_dtp_values=covers=2&datetime=2019-07-09 19:00; _csrf=xCJFVNLyun7GmB7I8S9EPSSj; spredCKE=redcount=0; aCKE=4e41c827-cd3e-4917-8f1b-cc5fa7f1f11a; lsCKE=cbref=1&ors=otcom; s_cc=true; s_fid=00AE7231ED57FB36-0EA1676D9565D377; s_dl=1; s_sq=%5B%5BB%5D%5D; s_vi=[CS]v1|2E922EA005031655-60001184E00078BD[CE]; tlrCKE=2019-07-09+09%3a25%3a49Z; s_nr=1562664350445-New; _gat_UA-52354388-1=1; notice_gdpr_prefs=0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100:; OT-Session-Update-Date=1562665903; ftc=x=2019-07-09T10%3A51%3A43&c=1&pt1=1&pt2=1&er=0; lvCKE=lvmreg=%2C0&histmreg=57%2C0%7C358%2C0%7C%2C0; bm_sv=503A3652B0E2E4FAB009B06B5CFDEC7C~HdCtMzHuVx9H0mAFZXPyr2ywwoxHzi2bTNE0CdlyXqfdWzuB6JUAC4JHV3kVuKrRIfd4eYvyz2RT/Ckp1lV18FKxCRIFVF68hF6x6ztjjzdawbVB8IBHncGQ8ELCzpQ4nMt/kH0pLsBlgBWlQh7O9MPWznkERaM368kWogh3uww=");
//	        connection.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
	        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
	        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
	        writer.write(payload);
	        writer.close();
	        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	        String line;
	        while ((line = br.readLine()) != null) {
	                jsonString.append(line);
	        }
	        br.close();
	        connection.disconnect();
	    } catch (Exception e) {
	          throw new RuntimeException(e.getMessage());
	    }
	    if (!cacheFile.exists())
			FileUtil.writeAllText(fileName, jsonString.toString());
	    return jsonString.toString();
	}
	//////////////////////new Code////////////////////////////////////
	public static String getRedirectedURL( String mainDomain,String url) throws IOException {
		if(!url.contains("http"))url = mainDomain+url;
	    HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
	    con.setInstanceFollowRedirects(false);
	    con.connect();
	    con.getInputStream();
	    
	    U.log("response code : "+con.getResponseCode());
	    if (con.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM || con.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP) {
	        String redirectUrl = con.getHeaderField("Location");
	        return getRedirectedURL(mainDomain,redirectUrl);
	    }
	    return url;
	}
	
	public static String getGoogleZipFromAddressWithKey(String inputadd[]) throws IOException{
		
		String st = inputadd[0].trim() + "," + inputadd[1].trim() + "," + inputadd[2].trim();
			String addr = "https://maps.googleapis.com/maps/api/geocode/json?address="
					+ st+"&key=AIzaSyDhsO7Ska4s4zUW_68R1n8OhRHJuZP5gm8";
//			U.log(addr);

//			U.log(U.getCache(addr));
			String html = U.getHTML(addr);
			String status = U.getSectionValue(html, "status\" : \"", "\"");
			
			if(status.trim().equals("OK")){
				String txt = U.getSectionValue(html, "formatted_address\" : \"", "\"");
//				U.log("Address txt Using gmap key :: " + txt);
				if (txt != null)
					txt = txt.trim();
				txt = txt.replaceAll("The Arden, |TPC Sugarloaf Country Club, ", "").replace("50 Biscayne, 50", "50");
				txt = txt.replaceAll("110 Neuse Harbour Boulevard, ", "");
				txt = txt
						.replaceAll(
								"Suite CNewark, |Waterview Tower, |Liberty Towers, |THE DYLAN, |Cornerstone, |Roosevelt Towers Apartments, |Zenith, |The George Washington University,|Annapolis Towne Centre, |Waugh Chapel Towne Centre,|Brookstone, |Rockville Town Square Plaza, |University of Baltimore,|The Galleria at White Plains,|Reston Town Center,",
								"");
				String[] add = txt.split(",");
				add[3] = Util.match(add[2], "\\d+");
				add[2] = add[2].replaceAll("\\d+", "").trim();
				return add[3];
			}else{
				return "-";
			}
		}
	
	public static final String hereApiID="aGa8KgrWvrUCGqoLCSL9";
	public static final String hereApiCode="Va551VVoWCaWx7JIMok8eA";
	public static String[] getlatlongHereApi(String add[]) throws IOException {
		String addr = add[0] + "," + add[1] + "," + add[2];
		addr = "https://geocoder.api.here.com/6.2/geocode.json?searchtext=" + URLEncoder.encode(addr, "UTF-8") + "&app_id=" + hereApiID + "&app_code=" + hereApiCode + "&gen=9";
		U.log(addr);
		U.log(U.getCache(addr));
		String html = U.getHTML(addr);
		String sec = U.getSectionValue(html, "\"DisplayPosition\":{", "\"NavigationPosition\":");
		String lat = U.getSectionValue(sec, "\"Latitude\":", ",");
		if (lat != null)
			lat = lat.trim();
		String lng = U.getSectionValue(sec, "\"Longitude\":", "}");
		if (lng != null)
			lng = lng.trim();
		String latlng[] = { lat, lng };
		// U.log(lat);
		return latlng;
		
	}
	
	public static String[] getNewBingLatLong(String[] address) {
		// TODO Auto-generated method stub
		
			String[] latLong = new String[2];
			String addressLine=address[0]+","+address[1]+","+address[2]+","+address[3];
			if (addressLine == null || addressLine.trim().length() == 0)
				return null;
			addressLine = addressLine.trim().replaceAll("\\+", " ");
			String geocodeRequest = "http://dev.virtualearth.net/REST/v1/Locations/'"
					+ addressLine
					+ "'?o=xml&key=AninuaBy5n6ekoNCLHltcvwcnBGA7llKkNDBNO5XOuHNHJKAyo0BQ8jH_AhhP6Gl";
			// U.log("-----------------addressline-----------"+geocodeRequest);
			try
			{
			String xml = U.getHTML(geocodeRequest);
			// U.log("--------------------------xml---------------------------------"+xml);
			latLong[0] = U.getSectionValue(xml, "<Latitude>", "</Latitude>");
			latLong[1] = U.getSectionValue(xml, "<Longitude>", "</Longitude>");
			}catch (Exception e) {
				e.printStackTrace();
			}
			return latLong;
		
	}
	
	public static String[] getNewBingAddress(String[] latlong) {
		// TODO Auto-generated method stub
		
		String[] addr = null;
		try {
		String htm = U.getHTML("http://dev.virtualearth.net/REST/v1/Locations/"
						+ latlong[0]
						+ ","
						+ latlong[1]
						+ "?o=json&jsonp=GeocodeCallback&key=AninuaBy5n6ekoNCLHltcvwcnBGA7llKkNDBNO5XOuHNHJKAyo0BQ8jH_AhhP6Gl");
		String[] adds = U.getValues(htm, "formattedAddress\":\"", "\"");
		U.log(htm);
		for (String item : adds) {
			addr = U.getAddress(item);
			if (addr == null || addr[0] == "-")
				continue;
			else {
				U.log("Bing Address =>  Street:" + addr[0] + " City :"
						+ addr[1] + " state :" + addr[2] + " ZIP :" + addr[3]);
				return addr;
			}

		}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return addr;
	}
	
		
		
		
		
		public static boolean matches(String input, String regexp){
			return Pattern.compile(regexp).matcher(input).matches();
		}
		
		
		public static String toTitleCase(String givenString) {

			char[] c = { '\'', '-', ',', ' ', ';', '/', '(', ')', '!', '@', '#', ':', '|', '*', '&', '+', '$','.' };
//			char[] c = { '-', ',', ' ', ';', '/', '(', ')', '!', '@', '#', ':', '|', '*', '&', '+', '$','.' };
			//givenString = WordUtils.capitalizeFully(givenString, c).replaceAll("[$&+:=?@#|'<>^*()%!]", "");
			givenString = WordUtils.capitalizeFully(givenString, c);
			givenString = givenString.replaceAll("\\s{2,}", " ").replaceAll("\\-{2,}", "-").trim();
			// givenString=givenString.replaceAll("[$&+,:=?@|'<>^*()%!]", "");
			if (givenString.length() > 1) {
				if ("{[$&+,:=?@|'<>^*%!-]}".contains(givenString.substring(0, 1))) { //()
					// log("hello");
					givenString = givenString.substring(1, givenString.length()).trim();
				}
				if ("{[$&+,:=?@|'<>^*%!-]}".contains(givenString.substring(givenString.length() - 1, givenString.length()))) { //()
					givenString = givenString.substring(0, givenString.length() - 1).trim();
				}
			}
			return givenString.replace(" ;", ";").replace(" -", "-").replace("- ", "-").replace("-", " - ")
					.replace("'S", "'s").trim();
		}

		public static void getData(String cimiSataionUrl, String file) {
			// TODO Auto-generated method stub
			String apiUrl = cimiSataionUrl;
			 String fileName = file;
			 File cacheFile = new File(file);
//			 U.log(cacheFile);
			if (cacheFile.exists())return;
	        try {
	            // Create the URL object
	            URL url = new URL(apiUrl);

	            // Create the HttpURLConnection
	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

	            // Set the request method and headers
	            connection.setRequestMethod("GET");
	            connection.setRequestProperty("Content-Type", "application/xml");

	            // Check if a redirection response was received
	            int responseCode = connection.getResponseCode();
	            if (responseCode == HttpURLConnection.HTTP_MOVED_TEMP) {
	                // Get the new location URL from the "Location" header
	                String newUrl = connection.getHeaderField("Location");

	                // Create a new connection to the new URL
	                connection = (HttpURLConnection) new URL(newUrl).openConnection();

	                // Set the request method and headers for the new connection
	                connection.setRequestMethod("GET");
	                connection.setRequestProperty("Content-Type", "application/xml");
	            }

	            // Get the response code again
	            responseCode = connection.getResponseCode();

	            // If the request was successful (response code 200)
	            if (responseCode == HttpURLConnection.HTTP_OK) {
	                // Read the response data
	                BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
	                FileOutputStream fileOutputStream = new FileOutputStream(fileName);

	                byte[] buffer = new byte[1024];
	                int bytesRead;
	                while ((bytesRead = in.read(buffer)) != -1) {
	                    fileOutputStream.write(buffer, 0, bytesRead);
	                }

	                // Close the streams
	                in.close();
	                fileOutputStream.close();

	                System.out.println("Response saved to " + fileName);
	            } else {
	                System.out.println("Error: " + responseCode);
	            }

	            // Disconnect the connection
	            connection.disconnect();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
			
		}
	    public static void writeAllText(String path, String aContents) throws FileNotFoundException, IOException {

	        File aFile = new File(path);

	        //use buffering
	        Writer output = new BufferedWriter(new FileWriter(aFile));
	        try {
	            //FileWriter always assumes default encoding is OK!
	            output.write(aContents);
	        } finally {
//	            output.close();
	        }
	    }//writeAllText()

		public static String[] getFormattedAddressBing(String formattedAddress) {
		    // Split the address using commas
		    String[] parts = formattedAddress.split(",");
		    String[] address = new String[] { "-", "-", "-", "-" };
		    int n = parts.length;
		    // If the address has enough parts to extract details
		    if (n >= 4) {
		        // Extract street address (all parts except the last 3)
		        for (int i = 0; i < n - 3; i++) {
		            address[0] = (i == 0) ? parts[i].trim() : address[0] + ", " + parts[i].trim();
		        }
		        // Extract city
		        address[1] = parts[n - 3].trim();
		        // Extract state (assume it's the first word in the second-to-last part)
		        String stateAndZip = parts[n - 2].trim();
		        address[2] = Util.match(stateAndZip, "\\w+", 0); // Use regex to find the state abbreviation
		        address[2] = (address[2] == null) ? "-" : address[2].toUpperCase();
		        // If state name is longer than 2 letters, abbreviate it using USStates.abbr
		        if (address[2].length() > 2) {
		            address[2] = USStates.abbr(address[2]);
		        }
		        // Extract ZIP code (5 digits, might have an optional dash and extra digits)
		        address[3] = Util.match(stateAndZip, "\\d{5}", 0);
		        address[3] = address[3].contains("-") ? address[3].substring(0, address[3].indexOf("-")) : address[3];
		        if (address[3] == null) {
		            address[3] = "-";
		        }
		    }
		    // Validate street address
		    if (!address[0].equals("-") && address[0].length() < 3) {

		        address[0] = "-";
		    }
		    return address;
		}
		
		
}