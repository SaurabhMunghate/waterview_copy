package com.bil;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.supercsv.io.CsvListReader;
import org.supercsv.prefs.CsvPreference;

import com.shatam.scrapper.ShatamFirefox;
import com.shatam.utils.CookieManager;
import com.shatam.utils.DerivedPropertyType;
import com.shatam.utils.FileUtil;
import com.shatam.utils.PropertyStatus;
import com.shatam.utils.PropertyType;
import com.shatam.utils.U;
import com.shatam.utils.USStates;
import com.shatam.utils.Util;

public class U {
    // Constants from the header file
    private static final int NROWS = 621;
    private static final int NCOLS = 1405;
    private static final int BYTES_PER_VALUE = 4; // 32-bit float
    private static final float NODATA = -9999f;

    public static float getPrecipitationFromRowCol(RandomAccessFile randomAccessFile, int row, int col) throws IOException {
        // Check if row and column are within bounds
        if (row < 0 || row >= NROWS || col < 0 || col >= NCOLS) {
            return NODATA;  // Return NODATA if out of bounds
        }

        // Calculate file position based on row and column
        long position = (long) row * NCOLS * BYTES_PER_VALUE + (long) col * BYTES_PER_VALUE;

        // Seek to the position and read the float value
        randomAccessFile.seek(position);
        float value = randomAccessFile.readFloat();

        return value == NODATA ? NODATA : value;  // Return the value if valid, otherwise NODATA
    }


	public final static void stripAll(String[] vals) {
		Arrays.stream(vals).map(String::trim).toArray(unused -> vals);
	}

	/**
	 * @author priti
	 */
	public static final String MY_PHANTOMJS_PATH = System.getProperty("user.home") + File.separator + "phantomjs";
	public static final String MY_CHROME_PATH = System.getProperty("user.home") + File.separator + "chromedriver";

	public static void setUpChromePath() {
		if (SystemUtils.IS_OS_LINUX) {
			System.setProperty("webdriver.chrome.driver", MY_CHROME_PATH);
		}
		if (SystemUtils.IS_OS_WINDOWS) {
			System.setProperty("webdriver.chrome.driver", MY_CHROME_PATH + ".exe");
		}
	}

	public static final String MY_GECKO_PATH = System.getProperty("user.home") + File.separator + "geckodriver";

	public static void setUpGeckoPath() {
		if (SystemUtils.IS_OS_LINUX) {
			System.setProperty("webdriver.gecko.driver", MY_GECKO_PATH);
		}
		if (SystemUtils.IS_OS_WINDOWS) {
			System.setProperty("webdriver.gecko.driver", MY_GECKO_PATH + ".exe");
		}
		clearFirefoxConsoleLogs();
	}

	public static WebDriver headLessDriver = null;

	// Clear selenium log from console
	public static void clearFirefoxConsoleLogs() {
		System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE, "true");
		System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE,
				System.getProperty("user.home") + File.separator + "Selenium_logs.txt");
		//U.log("[::: Clear Firefox Console Logs ::::]");
	}

	public static FirefoxOptions getFirefoxOptions() {
		FirefoxOptions options = new FirefoxOptions();
		options.addArguments("--headless");
		// Disable geolocation
		options.addArguments("--disable-geolocation");
		// Disable images
		options.addPreference("permissions.default.image", 2);
		// flash player
		options.addPreference("plugin.state.flash", 2);
		return options;
	}

	public static FirefoxProfile getFirefoxProfile() {
		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference("permissions.default.image", 2);
		return profile;
	}

	public static FirefoxBinary getFirefoxBinary() {
		File pathToBinary = new File(System.getProperty("user.home") + File.separator + "firefox/firefox");
		FirefoxBinary ffBinary = new FirefoxBinary(pathToBinary);
		return ffBinary;
	}

	public static DesiredCapabilities getFirefoxCapabilities() {
		DesiredCapabilities capabilities = DesiredCapabilities.firefox();
		// capabilities.setJavascriptEnabled(true);
		capabilities.setCapability(FirefoxOptions.FIREFOX_OPTIONS, getFirefoxOptions());

		return capabilities;
	}

	public static String getHtmlHeadlessFirefox(String url, WebDriver driver) throws IOException, InterruptedException {

		String html = null;
		String Dname = null;

		String host = new URL(url).getHost();
		host = host.replace("www.", "");
		int dot = host.indexOf("/");
		Dname = (dot != -1) ? host.substring(0, dot) : host;
		File folder = null;

		folder = new File(U.getCachePath() + Dname);
		if (!folder.exists())

			folder.mkdirs();
		String fileName = U.getCacheFileName(url);

		fileName = U.getCachePath() + Dname + "/" + fileName;

		File f = new File(fileName);
		if (f.exists()) {
			return html = FileUtil.readAllText(fileName);
			// //U.log("Reading done");
		}
		// int respCode = CheckUrlForHTML(url);
		Thread.sleep(8000);
		// if(respCode==200)
		{

			if (!f.exists()) {
				BufferedWriter writer = new BufferedWriter(new FileWriter(f));
				driver.get(url);
				((JavascriptExecutor) driver).executeScript("window.scrollBy(0,400)", "");
				html = driver.getPageSource();
				// //U.log("Current Url : "+ driver.getCurrentUrl());
				Thread.sleep(2000);
				writer.append(html);
				writer.close();

			} else {
				if (f.exists()) {
					html = FileUtil.readAllText(fileName);
					//U.log("Reading done");
				}
			}
			return html;
		}

	}// //

	/**
	 * @author priti
	 */
	// Format million price
	public static String formatMillionPrices(String html) {
		Matcher millionPrice = Pattern.compile("\\$\\d\\.\\d M", Pattern.CASE_INSENSITIVE).matcher(html);
		while (millionPrice.find()) {
			// //U.log(mat.group());
			String floorMatch = millionPrice.group().replace(" M", "00,000").replace(".", ","); // $1.3 M
			html = html.replace(millionPrice.group(), floorMatch);
		} // end millionPrice
		return html;
	}

	public static String getHardCodedPath() {
		String Regex = "Harcoded Builders";
		String Filename = System.getProperty("user.home");
		if (Filename.contains("/")) {
			Regex = "/Cache/Harcoded Builders/";
		} else {
			Regex = "\\Cache\\Harcoded Builders\\";
		}
		Filename = Filename.concat(Regex);
		if (!Filename.equals(Regex)) {
			Regex = Regex.toLowerCase();
		}
		return Filename;
	}

	public static String getNoHtml(String html) {
		String noHtml = null;
		noHtml = html.toString().replaceAll("\\<.*?>", " ");
		return noHtml;
	}

	public static String getCityFromZip(String zip) throws IOException {
		// http://maps.googleapis.com/maps/api/geocode/json?address=77379&sensor=true
		String html = U.getHTML("http://ziptasticapi.com/" + zip);
		String city = U.getSectionValue(html, "city\":\"", "\"");
		return city.toLowerCase();
	}

	public static String[] getGoogleLatLngWithKey(String add[]) throws IOException {
		String addr = add[0] + "," + add[1] + "," + add[2];
		addr = "https://maps.googleapis.com/maps/api/geocode/json?address=" + URLEncoder.encode(addr, "UTF-8")
//				+ "&key=AIzaSyAeG9lLU8fWh8rWcPivHDwxLAM4ZCInpmk";
		        + "&key=Ak8RoKwrZE-IbLkkXRFae9UTXw2UhpuET1mWY9z_ZkzTYR_-TCG8pMcNIUEtiqo5";
  
		
		//U.log(addr);
		U.log(U.getCache(addr));
		String html = U.getHTML(addr);

		String sec = U.getSectionValue(html, "location", "status");

		String lat = U.getSectionValue(sec, "\"lat\" :", ",");
		if (lat != null)
			lat = lat.trim();
		String lng = U.getSectionValue(sec, "\"lng\" :", "}");
		if (lng != null)
			lng = lng.trim();
		String latlng[] = { "", "" };
		String status = U.getSectionValue(html, "status\" : \"", "\"");
		if (status.trim().equals("OK")) {
			latlng[0] = lat;
			latlng[1] = lng;
			return latlng;
		} else
			return latlng;
	}

	public static String[] getGoogleAddressWithKey(String latLng[]) throws Exception {

String[] add= getBingAddress(latLng[0] , latLng[1]);
			//		String st = latLng[0].trim() + "," + latLng[1].trim();
//		String addr = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + st;
////		String key = "AIzaSyAeG9lLU8fWh8rWcPivHDwxLAM4ZCInpmk";
//		String key = "Ak8RoKwrZE-IbLkkXRFae9UTXw2UhpuET1mWY9z_ZkzTYR_-TCG8pMcNIUEtiqo5";
//
//		U.log("With key" + U.getCache(addr));
//		String html = U.getHTMLForGoogleApiWithKey(addr, key);
//		String status = U.getSectionValue(html, "status\" : \"", "\"");
//
//		if (status.trim().equals("OK")) {
//			String txt = U.getSectionValue(html, "formatted_address\" : \"", "\"");
//			U.log("Address txt Using gmap key :: " + txt);
//			if (txt != null)
//				txt = txt.trim();
//			txt = txt.replace("7970 U.S, Co Rd 11", "7970 US Co Rd 11")
//					.replace("Brooklyn Trails, 22202 Porter Mountain Trl", "22202 Porter Mountain Trl")
//					.replaceAll("The Arden, |TPC Sugarloaf Country Club, ", "").replace("50 Biscayne, 50", "50");
//			txt = txt.replaceAll("110 Neuse Harbour Boulevard, ", "");
//			txt = txt.replaceAll(
//					"Waterview Tower, |Liberty Towers, |THE DYLAN, |Cornerstone, |Roosevelt Towers Apartments, |Zenith, |The George Washington University,|Annapolis Towne Centre, |Waugh Chapel Towne Centre,|Brookstone, |Rockville Town Square Plaza, |University of Baltimore,|The Galleria at White Plains,|Reston Town Center,",
//					"");
//			String[] add = txt.split(",");
//			add[3] = Util.match(add[2], "\\d+");
//			add[2] = add[2].replaceAll("\\d+", "").trim();
			return add;
//		} else {
//			return new String[] { "", "", "", "" };
//		}
	}

	public static String getHTMLForGoogleApiWithKey(String path, String key) throws Exception {
		path = path.replaceAll(" ", "%20");
		String fileName = U.getCache(path);
		File cacheFile = new File(fileName);
		if (cacheFile.exists()) {
			if (cacheFile.length() > 400) {
				return FileUtil.readAllText(fileName);
			} else if (cacheFile.length() < 400) {
				cacheFile.delete();
			}
		}
		URL url = new URL(path + "&key=" + key);
		String html = null;
		// Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("45.77.146.9",
		// 55555));
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
			//U.log(e);

		}
		return html;
	}

	public static String[] getAddressGoogleApi(String latlng[]) throws Exception {
		return getNewBingAddress(latlng); 
//		String st = latlng[0].trim() + "," + latlng[1].trim();
//		String[] add = getBingAddress(latlng[0] , latlng[1]);
//		String addr = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + st;
//		U.log(st);
//
//		U.log(U.getCache(addr));
//		String html = U.getPageSource(addr);
//		String status = U.getSectionValue(html, "status\" : \"", "\"");
//		U.log("GMAP Address Status Without Key : " + status);
//		if (!status.contains("OVER_QUERY_LIMIT") && !status.contains("REQUEST_DENIED")) {
//			String txt = U.getSectionValue(html, "formatted_address\" : \"", "\"");
////			U.log("txt:: " + txt);
//
//			if (txt.contains("+")) {
//				txt = U.getSectionValue(html.replace("\"formatted_address\" : \"" + txt + "\",", ""),
//						"formatted_address\" : \"", "\"");
//			}
//			if (txt != null)
//				txt = txt.trim();
//			txt = txt.replace("Hummingbird Trail, 2954-2980 Kuehner Dr", "2954-2980 Kuehner Dr")
//					.replace("7970 U.S, Co Rd 11", "7970 US Co Rd 11")
//					.replace("7100 E, 7100 East Green Lake Dr N", "7100 East Green Lake Dr N")
//					.replace("Brooklyn Trails, 22202 Porter Mountain Trl", "22202 Porter Mountain Trl")
//					.replace("Foothill Dr &, E Vista Way", "Foothill Dr & E Vista Way")
//					.replace("16063 S, Selfridge Cir", "16063 S Selfridge Cir")
//					.replace("13th Ave &, Grangeville Blvd", "13th Ave & Grangeville Blvd")
//					.replace("N Clovis Ave &, E Shepherd Ave", "N Clovis Ave & E Shepherd Ave")
//					.replace("Paseo Del Sur &, Templeton St", "Paseo Del Sur & Templeton St")
//					.replace("N Dinuba Blvd &, Riverway Dr", "N Dinuba Blvd & Riverway Dr")
//					.replace("Williams Airport, 20266 Airfield Ln", "20266 Airfield Ln")
//					.replace("Blue Crane Cir, South Carolina 29588", "Blue Crane Cir, Myrtle Beach, SC 29588")
//					.replace("Rehbein and, County Hwy 21", "Rehbein and County Hwy 21")
//					.replace("Summers Cor, 104 Rushes Row", "Summers Cor 104 Rushes Row")
//					.replace("5637 Lowell, Ayers Cliff St", "5637 Lowell Ayers Cliff St")
//					.replace("N Clovis Ave &, E Shepherd Ave", "N Clovis Ave & E Shepherd Ave").replace("Indiana", "IN")
//					.replace("Midtown Ave / MUSC Health East Cooper, South Carolina 29464",
//							"Midtown Ave , Mt Pleasant, SC 29464")
//					.replace("Shaw Ave and, N Grantland Ave", "Shaw Ave and N Grantland Ave")
//					.replace("770D-A, Lighthouse Dr", "770D-A Lighthouse Dr")
//					.replaceAll("The Arden, |TPC Sugarloaf Country Club, ", "");
//			txt = txt.replaceAll("110 Neuse Harbour Boulevard, ", "").replace("M239+9H Socastee, SC, USA",
//					"Myrtle Beach, Socastee, SC 29588, USA");
//			txt = txt.replaceAll(
//					"Waterview Tower, |Liberty Towers, |THE DYLAN, |Cornerstone, |Roosevelt Towers Apartments, |Zenith, |The George Washington University,|Annapolis Towne Centre, |Waugh Chapel Towne Centre,|Brookstone, |Rockville Town Square Plaza, |University of Baltimore,|The Galleria at White Plains,|Reston Town Center,",
//					"");
//			String[] add = txt.split(",");
//
//			add[3] = Util.match(add[2], "\\d+");
//			add[2] = add[2].replaceAll("\\d+", "").trim();
//
//			 //U.log("zip:::::::"+add[3]);
//			return add;
//		} else if (getGoogleAddressWithKey(latlng) != null) {
//			U.log("latlnglatlnglatlng"+latlng.toString());
//			return getGoogleAddressWithKey(latlng);
//		} else {
//			return getAddressHereApi(latlng);
//		}
	}

	public static String[] getAddressGoogleApiProxy(String latlng[]) throws IOException {
		String st = latlng[0].trim() + "," + latlng[1].trim();
		String proxy = "http://75.119.204.81:3302/gproxy?to=";

		String addr = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + st;
		proxy = proxy + addr;
		//U.log(proxy);

		//U.log(U.getCache(proxy));
		String html = U.getPageSource(proxy);

		String txt = U.getSectionValue(html, "formatted_address\" : \"", "\"");
		//U.log("txt:: " + txt);
		if (txt != null)
			txt = txt.trim();
		txt = txt.replaceAll("The Arden, |TPC Sugarloaf Country Club, ", "");
		txt = txt.replaceAll("110 Neuse Harbour Boulevard, ", "");
		txt = txt.replaceAll(
				"Waterview Tower, |Liberty Towers, |THE DYLAN, |Cornerstone, |Roosevelt Towers Apartments, |Zenith, |The George Washington University,|Annapolis Towne Centre, |Waugh Chapel Towne Centre,|Brookstone, |Rockville Town Square Plaza, |University of Baltimore,|The Galleria at White Plains,|Reston Town Center,",
				"");
		String[] add = txt.split(",");
		add[3] = Util.match(add[2], "\\d+");
		add[2] = add[2].replaceAll("\\d+", "").trim();

		// //U.log(lat);
		return add;
	}

	public static String[] getlatlongGoogleApi(String add[]) throws Exception {
		return getNewBingLatLong(add);
//		String[] latlng =getBingLatLong(add);
//		String addr = add[0] + "," + add[1] + "," + add[2];
//		addr = "https://maps.googleapis.com/maps/api/geocode/json?address=" + URLEncoder.encode(addr, "UTF-8");
//		//U.log(addr);
//		//U.log(U.getCache(addr));
//		String html = U.getHTML(addr);
//		String status = U.getSectionValue(html, "status\" : \"", "\"");
//		//U.log("GMAP Address Status Without Key : " + status);
//		if (!status.contains("OVER_QUERY_LIMIT") && !status.contains("REQUEST_DENIED")) {
//			String sec = U.getSectionValue(html, "location", "status");
//			String lat = U.getSectionValue(sec, "\"lat\" :", ",");
//			if (lat != null)
//				lat = lat.trim();
//			String lng = U.getSectionValue(sec, "\"lng\" :", "}");
//			if (lng != null)
//				lng = lng.trim();
////			String latlng[] = { lat, lng };
//			 //U.log(lat);
//			return latlng;
//		}else if (getBingLatLong( add) != null) {
//			return getBingLatLong(add);
//		} else {
//			return getlatlongHereApi(add);
//		}
	}

	public static String[] getlatlongGoogleApiProxy(String add[]) throws IOException {
		String proxy = "http://75.119.204.81:3301/gproxy?to=";
		String addr = add[0] + "," + add[1] + "," + add[2];
		addr = "https://maps.googleapis.com/maps/api/geocode/json?address="// 1138
																			// Waterlyn
																			// Drive","Clayton","NC
				+ URLEncoder.encode(addr, "UTF-8");
		proxy = proxy + addr;
		//U.log(proxy);
		//U.log(U.getCache(proxy));
		String html = U.getHTML(proxy);
		String sec = U.getSectionValue(html, "location", "status");
		String lat = U.getSectionValue(sec, "\"lat\" :", ",");
		if (lat != null)
			lat = lat.trim();
		String lng = U.getSectionValue(sec, "\"lng\" :", "}");
		if (lng != null)
			lng = lng.trim();
		String latlng[] = { lat, lng };
		// //U.log(lat);
		return latlng;
	}

	private static ShatamFirefox driver;
	private static HashMap<String, String> communityType = new HashMap<String, String>() {
		{
			put("is a Swim Community|Swim Community", "Swim Community");
			put("lakeside location|Lakeview Lots Available|lakeside living|lakefront adventures|lake-style living|living at Lakeside|Lakeside Estates|luxurious lakeside community|lakeside living|Lakeside Master Planned Community|a lakeside community|accessible lakeside lifestyle|beautiful lakeside community|Lake community|[l|L]akeside [c|C]ommunity,|, Lakeside Community| Lakeside section of|lakeside community|features lakeside fun",
					"Lakeside Community");
			put("Luxury 62\\+ Community|62+ ACTIVE ADULT COMMUNITY|A New 62+|62 years of age or older|aged 62 and older|62\\+ age-restricted|62\\+ Senior Apartment Community|62 years of age or better|62 years and older|aged 62 and better|62\\+ apartment community",
					"62+ Community");
			put("Active Adult Living|active adult|active-adult |active adult|active adult|Active Adult</li>",
					"Active Adult");
			put("Golf Lake Estates| golf, |golf and|Golf Center</li>|<li>Nearby Golf,|golf resorts|round of golf | Golf Country Club|golf, and|include golf |golfer’s paradise| championship golf |Golf Trail|Needwood Golf Course|Golf course and|<span>Golf</span>|TopGolf|Golf Course</td>|golf memberships|golfing|golfing area|<li>Golf</li>|private golf holes|Top Golf</span>|Golf\\s+</li>| Golf Club|Golf Club</a>|Golf &amp; Country Club|Golf & Country Club| Golf Club,|Golf Club |golf course|golf courses|golf resort community|golf communities|gated golf & tennis community|Golf Course|Golf Course</h4>|golf courses|Golf and Country Club|Golf Community|golf &amp; country clubs",
					"Golf Course");
			put("Age-Restricted to 55\\+|55\\+ senior community|55 \\+ community|55+ Community|adults 55 and over|adults aged 55+|his fun-filled 55|seniors 55 and over|55\\+ Condos|55\\+ age restricted community|residents age 55 and up|bring the 55\\+ Live Better|buyers 55 & over|55\\+ Apartments|Best 55\\+ Lifestyle|community for ages 55\\+|ages 55\\+ |55\\+ Life|55\\+ Community for|over the age of 55| Amazing 55\\+ Lifestyle| 55\\+ townhome living|55\\+ Age-Qualified|55\\+ Epcon Community|55-plus community|residents ages 55\\+|55\\+ Luxury Retirement |55\\+ Living|55\\+ lifestyle community|active adult 55\\+|55\\+ Retirement Living|55\\+ resort community|55\\+ single-family|55-and-better couples|55+ living|ages 55 and up|community for those 55\\+|55 and older|active adults 55\\+|55\\+ age-restricted|55\\+ resort-class community|55\\+ gated community|\\+55 age group|Best 55\\+ Communities|55-and-better living|Active Adults aged 55 or better|55 years or better in residence|55 plus residents |55-years of age and better|55-Plus communities|55 and over gated|55 and over community|active 55-and-better lifestyle| 55-and-better community|55 and Bette|55\\+ Residents|55 and older community|55+ neighborhood|55+ community|Community 55\\+|55\\+ Active Adult|\\(55\\+\\) community|active adult \\(55\\+\\)|55\\+ age-qualified community|\\(55\\+\\) gated community| lifestyle for 55\\+ |55\\+ neighborhood|55\\+ residents| 55\\+ Community|55\\+ Active|community \\(55 years \\+\\) |active adults age 55",
					"55+ Community");
//			put("\\d{2}\\+ luxury community", "55 Plus Community");
			put("Waterfront at The Vineyards on Lake Wylie|Waterfront at McLean|Waterfront Homesites|both waterfront|water-front homesites|Waterfront Community|waterfront amenities | waterfront homes|magnificent water features|serene waterfront community|waterfront neighborhood |exclusive waterfront living|beautiful waterfront views|waterfront lifestyle|Waterfront Resort-style Living|waterfront community|waterfront lots",
					"Waterfront Community");
			put("lakes, enhancing|Lake offering|Lakefront|lakefront homesites|lakefront resort style|lake front townhomes|lakefront and parkside living|Lakefront Series|lake-front Community|lakefront community|lake front community",
					"Lakefront Community");

			put("A Master Plan Community|master-planned amenities|Master Plan Community</li>|Master-Planned Community|masterfully planned|master-planned community|Master-Planned Community| best master-plans of |master-plan community|Masterplanned Community|masterplanned community|Master-plan living|planned master community|Master Planned|masterfully-planned community|masterfully planned community| masterplan|Master-Planned|master-planned|master planned|master- planned|Master Plan|Master Plan|master-planned|Master Planned Community|masterplanned community",
					"Master Planned");
			put("Green Community", "Green Community");
			put("resort-like|Resort-Style|Resort-Worthy|resort-style|resort-style|resort-inspired|resort-inspired community|enowned resort|resort-class lake living| resort-like living|resorts pool|resort-style infinity pool|luxurious resorts|resort-inspired recreational |resort-inspired clubhouse |resort-inspired living|resort-style swimming pools|Resort- style pool|Amenities/Resort|resort experience|resort-like activities|resort-style living|Resort-like Pool|resort-inspired amenities|resort-style pool|Resort Style Pool|Resort Lifestyle|resort inspired community|resort like style amenities|resort-type lifestyle|resort-like amenities|resort-like environment| luxurious resort-style communities|Casino Resort| luxury resort|golf courses and resort |Golf and Resort|resort class amenities|Resort-style community|Golf Resort| The Resort Series|resort-inspired lifestyle|The Resort at the Groves|resort-like lifestyle|resort style amenities|resort-style amenities|resort pleasures|Resort Homes|Resort Pool|Resort golf course|resort community|Resort-style|Resort-Style|Resort and Golf Course|resort living|resort-style|posh resort|Resort style|resort features|Resort-style|resort style living| resort pool|resort-style|Smokey Resort|resort inspired amenity|Resort-style|Resort-style|resort amenities|resort-style amenities|Waterfront Resort-style Living|Resort-Style Living",
					"Resort Style");
			put("gated community|gated-community|Manned, Gated Security| <li>Gated,|Gated</div>|Gated, Natural Gas Community|Access Gate|Gated, Pool|Gated luxury|Private gated community|Gated Community|Controlled Access/Gated|Gated\\s*</p>|<br>Gated<br>|<li>Gated</li>|Gate House|gated community|gated single family|Gated Security|gated |Gated |Gated Community</li>| gated, ",
					"Gated Community");
			put("Country Club-Style|Country Club Community|Country Club", "Country Club");
			put("riverfront|Riverfront Homesites|riverfront property|Riverfront Community|riverfront community|riverfront land|Riverfront offers",
					"Riverfront Community");

		}
	};



	private static HashMap<String, String> commTypes = new HashMap<String, String>() {

		{

			put("single\\s*-?\\s*family", "Single Family");
			put("multi\\s*-?\\s*family", "Multi-Family Dwellings");
			put("town\\s*-?\\s*homes", "Townhomes");
			put("tower and estate residences", "Tower & Estate Residences");
			put(" condo(minium)? ", "Condominium");
			put("55_plus", "55_plus");
			put("carriage homes", "Carriage Homes");

		}
	};

	public static void log(Object o) {

		System.out.println(o);
	}

	public static String getCacheFileName(String url) {

		String str = url.replaceAll("http://", "");
		str = str.replaceAll("www.", "");
		str = str.replaceAll("[^\\w]", "");
		if (str.length() > 200) {
			str = str.substring(0, 100) + str.substring(170, 190) + str.length() + "-" + str.hashCode();

		}

		try {
			str = URLEncoder.encode(str, "UTF-8");
			// //U.log(str);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();

		}
		return str + ".txt";
	}

	public static String getnote(String html) {
		html = html.replaceAll(" {2,} ", "");

		String note = "-", match;
		String pnote = "-";
		for (String item : notes) {
			// //U.log(html);
			match = Util.match(html, item, 0);

			if (match != null) {
				// // //U.log(html);
				// //U.log("::kirti");
				html = html.toUpperCase().replace(item.toUpperCase(), "");
				// //U.log(html);
				//U.log("item :" + item);
				note = U.getCapitalise(match.toLowerCase());
				if (pnote.equals("-"))
					pnote = note;
				else
					pnote = pnote + ", " + note;
			}

		}
		return pnote;
	}

	private static ArrayList<String> notes = new ArrayList<String>() {
		{
			add("100% leased");
//			add("Presale");
			add("PRESALES EARLY 2023");
			add("presale begin early 2023");
			add("presale early 2023");
			add("PHASE 1 NOW OPEN FOR PRESALE");
			add("Phase 2 is now for sale");
//			add("Opening for Sales Fall 2021");
			add("PRECONSTRUCTION PRICING");
			add("Pre-Construction Opportunity");
			add("Just Released for Sale");
			add("Pre-Grand Opening Pricing Now");
			add("Lot For Sale");
			add("Final Home for Sale");
			add("Pre-Grand Opening Phase Release|Pre Grand Opening|Pre-Grand Opening");
			add("Now Open for Sales|now open for leasing|Leasing Late Summer 2020|Leasing 2020");
			add("New Town Homes for Sale|opening for sales January 2020|open for sales Fall 2019|Opening for Sales|RE-OPEN FOR SALES");
			add("Open Now For sales");
			add("Now leasing newest phase|Leasing 2022|PRELEASING 2017|Now Leasing|Lease Now|NOW LEASING|pre-leasing spring 2019|PRE-LEASING|Pre-Lease|NOW PRE-LEASING");
			add("Now Pre-Selling Phase II|Now Pre-Selling New Phase|Phase 2 Now Pre-Selling|Pre-sales coming Summer 2022|Pre-Selling Begins Summer 2022|Pre-sales starting June 1|Pre-Selling Fall 2022|now pre-selling|Pre-Selling|Now open for pre-sales|now open for pre-sale|pre-sales|Now Pre-Selling Phase 2|NOW PRE-SELLING PHASE II|Beginning Pre-Sales December 2019|Pre-selling|Pre-Selling This Fall|Pre-Selling late 2019|Pre-selling March 15|Now Pre-Selling Phase 4|Pre-Selling October 2019|Pre-selling November 2019|Now Pre-Selling Section 2|Pre-Selling New Section|NEW SECTION NOW PRE-SELLING|New Phase Pre-Selling in Spring|Pre-sales 2 available homesites|Now Pre-Selling New Phase|New Phase Now Preselling|Now Pre-Selling Final Section|NOW PRE-SELLING NEW PHASE|Now Pre-Selling Final Phase|Now Pre-Selling Phase II|PRESALES JULY 2019|Now Pre-Selling Phase II|Preselling Fall 2017|Waterfront lots available for presale|Now Pre-Selling Phase II|pre-selling February 2020|New Phase Now Pre-Selling|NOW OPEN FOR PRESALES|NOW OPEN FOR PRE-SALE|Pre-Selling New Phase|Preselling Phase 4|Now Pre-Selling|now pre selling|Final Phase Now Preselling|Pre-Selling NOW|Now Pre-Selling| NOW PRE-SELLING| Now Pre-Selling |Now pre-selling|Now Pre-Selling|Final Phase Now Preselling|Now Preselling New Phase|Presales now open|Now Preselling|Now Pre-Selling|Now Pre-Selling|PRE-SELLING|PRE-SELLING|PRE-SELLING|Preselling Now|now preselling|Preselling|Pre Selling|Pre-Sell");
			add("pre-construction sales");
			add("Phase II is now open!!");
			// add("Pre-Construction Opportunity|PRE-CONSTRUCTION PRICING NOW
			// AVAILABLE|pre-construction prices|Pre-Construction Pricing|Pre-Construction
			// Pricing ?|Pre Construction Pricing ?");
			add("Now accepting home site reservations");
//			add("Leasing Early 2022");
//			add("Leasing Summer 2022");
//			add("Leasing 2022");
		}
	};

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

	public static String post(String urlPath, HashMap<String, String> customHeaders, String data) throws Exception {

		// ----- Do the cookies
		HashSet<String> cookies = new HashSet<String>();

		{
			if (customHeaders != null) {
				URLConnection urlConn = getConn(customHeaders.get("Referer"), customHeaders);
				// //U.log("Getting cookies from: " +
				// customHeaders.get("Referer"));
				cookies = CookieManager.getCookies(urlConn);
			}
		}

		// Send data
		URLConnection conn = getConn(urlPath, customHeaders);
		CookieManager.setCookies(conn, cookies);

		OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		wr.write(data);
		wr.flush();
		StringBuffer buf = new StringBuffer();
		// Get the response
		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line;
		while ((line = rd.readLine()) != null) {
			buf.append(line).append("\n");
		}
		wr.close();
		rd.close();

		String html = buf.toString();

		return html;

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

	/**
	 * @param args
	 * @throws IOException
	 */
	// New function that mimics browser

	public static String getHtml(String url, WebDriver driver, Boolean flag) throws Exception {
		// WebDriver driver = new FirefoxDriver();
		String html = null;
		String Dname = null;
		String host = new URL(url).getHost();
		host = host.replace("www.", "");
		int dot = host.indexOf("/");
		Dname = (dot != -1) ? host.substring(0, dot) : host;
		File folder = null;
		if (flag == true)
			folder = new File(U.getCachePath() + Dname + "Quickkk");
		else
			folder = new File(U.getCachePath() + Dname);
		if (!folder.exists())
			folder.mkdirs();
		String fileName = U.getCacheFileName(url);

		// fileName = "C:/cache/" + Dname + "/" + fileName;
		if (flag == true)
			fileName = U.getCachePath() + Dname + "Quickkk" + "/" + fileName;
		else {
			fileName = U.getCachePath() + Dname + "/" + fileName;
		}

		File f = new File(fileName);
		if (f.exists())
			return html = FileUtil.readAllText(fileName);
		if (!f.exists()) {

			BufferedWriter writer = new BufferedWriter(new FileWriter(f));

			driver.get(url);

			((JavascriptExecutor) driver).executeScript("window.scrollBy(0,3000)", ""); // y value '400' can be

			try {
				WebElement option = null;

				option = driver.findElement(By.id("cntQMI"));// --------//*[@id="cntQMI"]
				option.click();
				Thread.sleep(3000);
				option.click();
				Thread.sleep(3000);
				//U.log("::::::::::::click succusfull1:::::::::");

			} catch (Exception e) {
				//U.log("click unsuccusfull1" + e.toString());
			}

			//U.log("Current URL:::" + driver.getCurrentUrl());
			html = driver.getPageSource();
			Thread.sleep(2 * 1000);

			writer.append(html);
			writer.close();

		} else {
			if (f.exists())
				html = FileUtil.readAllText(fileName);
		}

		return html;

	}


/*	public static void getHtmlDelete(String url, WebDriver driver) throws Exception {
		// WebDriver driver = new FirefoxDriver();

		String html = null;
		String Dname = null;

		String host = new URL(url).getHost();
		host = host.replace("www.", "");
		int dot = host.indexOf("/");
		Dname = (dot != -1) ? host.substring(0, dot) : host;
		File folder = null;

		folder = new File(U.getCachePath() + Dname);
		if (!folder.exists())
			folder.mkdirs();

		String fileName = U.getCacheFileName(url);

		fileName = U.getCachePath() + Dname + "/" + fileName;

		File f = new File(fileName);

		if (f.delete()) {
			log("Successfully Deleted=======");
		}

	}
*/
	public static String getHtml(String url, WebDriver driver) throws Exception {
		U.log(url);
		// WebDriver driver = new FirefoxDriver();

		String html = null;
		String Dname = null;

		String host = new URL(url).getHost();
		host = host.replace("www.", "");
		int dot = host.indexOf("/");
		Dname = (dot != -1) ? host.substring(0, dot) : host;
		File folder = null;

		folder = new File(U.getCachePath() + Dname);
		if (!folder.exists())
			folder.mkdirs();

		String fileName = U.getCacheFileName(url);

		fileName = U.getCachePath() + Dname + "/" + fileName;

		File f = new File(fileName);
		if (f.exists()) {
			return html = FileUtil.readAllText(fileName);
			// //U.log("Reading done");
		}

		// if(respCode==200)
		{

			if (!f.exists()) {
				synchronized (driver) {

					BufferedWriter writer = new BufferedWriter(new FileWriter(f));
					Thread.sleep(10000);
					driver.get(url);

					// //U.log("after::::"+url);
					Thread.sleep(5000);
					((JavascriptExecutor) driver).executeScript("window.scrollBy(0,15000)", "");
					Thread.sleep(3000);
					// //U.log("Current URL:::" + driver.getCurrentUrl());
					html = driver.getPageSource();
					Thread.sleep(2000);

					writer.append(html);
					writer.close();

				}
			} else {
				if (f.exists()) {
					html = FileUtil.readAllText(fileName);
					//U.log("Reading done");
				}
			}
			return html;
		}
		// else{
		// return null;
		// }
	}

	public static String getHtml(String url, WebDriver driver, String id) throws Exception {

		String html = null;
		String Dname = null;
		String host = new URL(url).getHost();
		host = host.replace("www.", "");
		int dot = host.indexOf("/");
		Dname = (dot != -1) ? host.substring(0, dot) : host;
		File folder = null;
		folder = new File(U.getCachePath() + Dname);
		if (!folder.exists())
			folder.mkdirs();
		String fileName = U.getCacheFileName(url);
		fileName = U.getCachePath() + Dname + "/" + fileName;
		File f = new File(fileName);
		if (f.exists())
			return html = FileUtil.readAllText(fileName);
		if (!f.exists()) {
			synchronized (driver) {

				BufferedWriter writer = new BufferedWriter(new FileWriter(f));
				//U.log("in gethtml==" + url);
				driver.get(url);
				Thread.sleep(5000);
				Actions dragger = new Actions(driver);
				WebElement draggablePartOfScrollbar = driver.findElement(By.id(id));
				int numberOfPixelsToDragTheScrollbarDown = 50;
				for (int i = 10; i < 500; i = i + numberOfPixelsToDragTheScrollbarDown) {
					try {
						dragger.moveToElement(draggablePartOfScrollbar).click()
								.moveByOffset(0, numberOfPixelsToDragTheScrollbarDown).release().perform();
						Thread.sleep(1000L);
					} catch (Exception e1) {
					}
				}
				//U.log("Current URL:::" + driver.getCurrentUrl());
				html = driver.getPageSource();
				writer.append(html);
				writer.close();
			}
		} else {
			if (f.exists())
				html = FileUtil.readAllText(fileName);
		}
		return html;

	}

	public static int CheckUrlForHTML(String path) {
		// TODO Auto-generated method stub
		int respCode = 0;
		try {

			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpPost request1 = new HttpPost(path);
			HttpResponse response1 = httpclient.execute(request1);
			respCode = response1.getStatusLine().getStatusCode();
		} catch (Exception ex) {

			//U.log(ex);
			return respCode;
		}
		return respCode;
	}

	public static String getHTML(String path) throws IOException {

		path = path.replaceAll(" ", "%20");
//		 U.log(""+path);
		// Thread.sleep(4000);
		String fileName = getCache(path);
//		U.log(fileName);
		File cacheFile = new File(fileName);
		if (cacheFile.exists())
			return FileUtil.readAllText(fileName);
		URL url = new URL(path);
//		U.log(url);
		String html = null;

		// chk responce code

//		int respCode = CheckUrlForHTML(path);
//		 //U.log("respCode=" + respCode);
//		 if (respCode == 200) {

		// Proxy proxy = new Proxy(Proxy.Type.HTTP, new //"157.90.199.133", 1080)
		// InetSocketAddress("107.151.136.218",80 )); //"181.215.130.32", 8080)
		// //"134.209.69.46", 8080 //"157.90.199.129", 1080)
//		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("104.149.3.3", 8080));
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("192.168.0.1", 8080));

		final URLConnection urlConnection = url.openConnection();
		// Mimic browser
		try {
			urlConnection.addRequestProperty("User-Agent",
					"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.4951.64 Safari/537.36");
			urlConnection.addRequestProperty("accept", "application/json, text/javascript, */*; q=0.01");
			urlConnection.addRequestProperty("accept-language", "en-GB,en-US;q=0.9,en;q=0.8");
			urlConnection.addRequestProperty("cache-control", "no-cache");
			urlConnection.addRequestProperty("referer", "https://www.c-rock.com/communities-austin");
			urlConnection.addRequestProperty("authority", "tiles.regrid.com");
			urlConnection.addRequestProperty("path", "/api/v1/parcels/16/10594/25363.json");
			urlConnection.addRequestProperty("origin", "https://app.regrid.com");
			urlConnection.addRequestProperty("cookie",
					"_fbp=fb.1.1646369270108.61888341; visitor_id478992=405823352; visitor_id478992-hash=9320b3a9fe77e6f9929f33c97de004c25f1cc196677379fc49a3d33b0019bbe074c83de8fe25d91ba54d304f47441dd2ac26a9b7; _gcl_au=1.1.404625297.1655698270; _gid=GA1.2.1837601758.1655698270; dm_timezone_offset=-330; dm_last_visit=1655698270330; dm_total_visits=7; _gat_UA-34098240-1=1; _gat_gtag_UA_178855985_1=1; dm_last_page_view=1655698556846; dm_this_page_view=1655698565752; _ga_HW6GTCCMQT=GS1.1.1655698270.13.1.1655698565.0; _ga=GA1.1.888920987.1646369268; _sp_id.d2cb=69a90e2e58ac7477.1646369269.7.1655698566.1646629494; _sp_ses.d2cb=1655700366248");
			// //U.log("getlink");
			final InputStream inputStream = urlConnection.getInputStream();

			html = IOUtils.toString(inputStream);
//		    //U.log(html);

			// final String html = toString(inputStream);
			inputStream.close();
			if (!cacheFile.exists())
				FileUtil.writeAllText(fileName, html);

			return html;
		} catch (Exception e) {
			//U.log("gethtml expection: " + e);

		}
		return html;
		/*
		 * } else { return null; }
		 */

	}

	private static HttpURLConnection getConn(String urlPath, HashMap<String, String> customHeaders) throws IOException {

		URL url = new URL(urlPath);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.addRequestProperty("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:10.0.2) Gecko/20100101 Firefox/10.0.2");
		conn.addRequestProperty("Accept", "text/css,application/xhtml+xml,application/xml,*/*;q=0.9");
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
				conn.addRequestProperty(headerName, customHeaders.get(headerName));
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

	// Original Parag's code
/*	public static String getHTML2(String path) {

		StringBuilder output = new StringBuilder();

		try {

			URL myUrl = new URL(path);
			BufferedReader in = new BufferedReader(new InputStreamReader(myUrl.openStream()));

			String line;

			while ((line = in.readLine()) != null) {
				output.append(line).append("\n");
				// System.out.println(line);
			}
			// System.out.println(line);

			in.close();

		} catch (IOException ex) {
		} finally {
		}

		return output.toString();
	}*/

	public static boolean isEmpty(String o) {

		return (o == null || o.trim().length() == 0);
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
				section = code.replace(code.substring(start + From.length(), end), "");
			// //U.log("%%%%%%%%%%%%%"+"Successfully Remove Section"+"%%%%%%%%%%%%%%%%%");
		} else {
			section = code;
		}
		return section;
	}

	//
	public static String getSectionValue(String code, String From, String To) {
		String section = null;
		int start, end;
		start = code.indexOf(From);
		// //U.log(start);
		if (start != -1) {
			end = code.indexOf(To, start + From.length());
			if (start < end)
				section = code.substring(start + From.length(), end);

		}
		return section;
	}

	public static String getCachePath() {
		String Regex = "Cache";
		String Filename = System.getProperty("user.home");
		// //U.log(Filename+"filename");
		if (Filename.contains("/")) {
			Regex = "/Cache/";
		} else {
			Regex = "\\Cache\\";
		}
		Filename = Filename.concat(Regex);
		// //U.log("filename :::"+Filename);
		if (!Filename.equals(Regex)) {
			Regex = Regex.toLowerCase();
		}
		return Filename;
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

	public static String[] getValues(String code, String begin, String From, String To) {

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
		s4 = Integer.parseInt(str.substring(str.length() - 3, str.indexOf("\"")));

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
	public static int[] getMaxAndMin(int[] a) {

		int Max = a[0], Min = a[0];
		for (int i = 0; i < a.length; i++) {
			if (a[i] > Max)
				Max = a[i];
			else if (a[i] < Min) {
				Min = a[i];
			}
		}
		int[] arr = new int[2];
		arr[0] = Max;
		arr[1] = Min;
		return arr;
	}

	/**
	 * @author Parag Humane
	 * @param args
	 */

	private static int[] getIntMaxMin(String code, String regExp, int group) {

		ArrayList<String> list = Util.matchAll(code, regExp, group);
		ArrayList<Integer> intList = new ArrayList<Integer>();
		int val;
		boolean isPrice = false;
		for (int i = 0; i < list.size(); i++) {
			isPrice = list.get(i).contains("$");
			String str = list.get(i).replaceAll(",|\\$", "");
			ArrayList<String> intTemp = Util.matchAll(str, "\\d{3,}", 0);
			for (String item : intTemp) {

				val = Integer.valueOf(item);
				if (isPrice && val > 20000)
					intList.add(val);
				if (!isPrice && val > 0)
					intList.add(val);

			}
		}

		int[] intVal = new int[intList.size()];
		for (int i = 0; i < intList.size(); i++) {
			intVal[i] = intList.get(i);
			// ///U.log(intList.get(i))
			;
		}
		if (intVal.length != 0) {
			intVal = U.getMaxAndMin(intVal);
			if (intVal[1] == 0 && intVal[0] != 0)
				return new int[] { intVal[0], intVal[1] };
			return new int[] { intVal[1], intVal[0] };
		}
		return new int[] { 0, 0 };
	}

	/**
	 * @author Parag Humane
	 * @param args
	 */
	public static String[] getPrices(String code, String regExp, int group) {

		int[] values = getIntMaxMin(code, regExp, group);
//		 //U.log(values[0]+ "  "+ values[1]);
		String minPrice = NumberFormat.getCurrencyInstance(Locale.US).format(values[0]).replaceAll("\\.00", "");
		String maxPrice = NumberFormat.getCurrencyInstance(Locale.US).format(values[1]).replaceAll("\\.00", "");
//		 //U.log(minPrice+ "  "+ maxPrice);
		minPrice = (values[0] > 20000) ? minPrice : null;
		maxPrice = (values[1] > 20000) ? maxPrice : null;
		if (maxPrice != null) {
			if (maxPrice.equals(minPrice))
				maxPrice = null;
		}
		if (minPrice == null && maxPrice != null) {
			minPrice = maxPrice;
			maxPrice = null;
		}

		return new String[] { minPrice, maxPrice };
	}

	/**
	 * @author Parag Humane
	 * @param args
	 */
	public static String getHTML(String path, String baseUrl) throws IOException {

		if (baseUrl != null && !path.startsWith(baseUrl))
			return getHTML(baseUrl + path);
		else
			return getHTML(path);
	}

	/**
	 * @author Parag Humane
	 * @param args
	 */
	public static String[] getSqareFeet(String code, String regExp, int group) {

		int[] values = getIntMaxMin(code, regExp, group);
		String minSqf = (values[0] != 0) ? "" + values[0] : null;
		String maxSqf = (values[1] != 0) ? "" + values[1] : null;

		if (maxSqf != null) {
			if (maxSqf.equals(minSqf))
				maxSqf = null;
		}

		return new String[] { minSqf, maxSqf };
	}

	/**
	 * @author Parag Humane
	 * @param args
	 */
	public static String getPropertyTypes(String code, String regExp, int group) {

		ArrayList<String> list = Util.matchAll(code, regExp, group);
		ArrayList<String> temp = new ArrayList<String>();
		StringBuilder type = null;

		if (list.size() != 0) {
			for (String item : list) {
				item = item.trim();
				if (item.equals("�") || item.length() == 0)
					continue;
				if (!temp.contains(item)) {
					temp.add(item);
					if (type == null) {
						type = new StringBuilder();
						type.append(item);
					} else
						type.append(", " + item);
				}
			}

		}

		if (type == null) {
			type = new StringBuilder();
		} else if (type.length() > 50) {
			String pType = type.toString();
			pType = pType.substring(0, 51);
			pType = Util.match(pType, "(.*?,)+", 0);
			pType = pType.substring(0, pType.length() - 1);
			type = new StringBuilder(pType);
		}
		return type.toString();
	}// /

	/**
	 * @author Parag Humane
	 * @param args
	 */
/*	public static String[] getGooleLatLong(String html) {

		String reg = "\\{center:\\{lat:(\\d+\\.\\d+),lng:(-\\d+\\.\\d+)";
		String reg2 = "<title>\\s*(\\d+\\.\\d+),\\s*(-\\d+\\.\\d+)";
		String match = Util.match(html, reg, 0);
		reg = (match == null) ? reg2 : reg;
		String lat = Util.match(html, reg, 1);
		String lng = Util.match(html, reg, 2);
		return new String[] { lat, lng };
	}
*/
/*	public static String[] getGlatlng(String[] add) throws Exception {
		// String addr = add[0] + "+" + add[1];
		String addr = add[0] + "+" + add[1] + "+" + add[2] + "+" + add[3];
		String data = null;
		String latLong = null;
		StringBuffer input = new StringBuffer();
		String link = "https://maps.google.com/maps?daddr=" + URLEncoder.encode(addr, "UTF-8");

		URL url = new URL(link);

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

		while ((data = br.readLine()) != null) {
			input.append(data);
		}

		String path = "E:\\google.txt";
		File file = new File(path);
		FileWriter wr = new FileWriter(file);
		wr.write(input.toString());
		int x = input.indexOf("{lat:");
		int y = input.indexOf("}", x);
		//U.log("X:" + x);
		//U.log("Y:" + y);

		latLong = input.substring(x, y);

		latLong = latLong.replace("{lat:", "");
		latLong = latLong.replace("lng:", "");

		String[] array = latLong.split(",");
		return array;
	}*/

/*	public static String[] getGooleLatLong(String[] add) throws IOException {

		String addr = add[0] + "+" + add[1] + "+" + add[2] + "+" + add[3];
		addr = "https://maps.google.com/maps?daddr=" + URLEncoder.encode(addr, "UTF-8");
		//U.log(addr);
		String html = U.getHTML(addr);
		return U.getGooleLatLong(html);
	}
*/
	public static String[] getAddress(String line) {

		String[] ad = line.split(",");

		String[] add = new String[] { "-", "-", "-", "-" };
		int n = ad.length;
		// //U.log("ad length is ::"+ad.length);
		if (ad.length >= 3) {
			for (int i = 0; i < n - 2; i++)
				add[0] = (i == 0) ? ad[i].trim() : add[0] + ", " + ad[i].trim();
			add[0] = add[0].trim().replaceAll("\\d+.\\d+\\s*,\\s*-\\d+.\\d+", "");

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
public static String[] getAddress1(String line) {
		
		String[] ad = line.split(",");

		String[] add = new String[] { "-", "-", "-", "-" };
		int n = ad.length;
		// U.log("ad length is ::"+ad.length);
		if (ad.length >= 3) {
			for (int i = 0; i < n - 2; i++)
				add[0] = (i == 0) ? ad[i].trim() : add[0] + ", " + ad[i].trim();
			add[0] = add[0].trim().replaceAll("\\d+.\\d+\\s*,\\s*-\\d+.\\d+", "");
	
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
/*	public static String[] getMapQuestAddress(String html) {

		String section = U.getSectionValue(html, "singleLineAddress\":\"", "\"");
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
*/
	public static String[] findAddress(String code) {

		String[] add = null;
		String sec = formatAddress(code);
		// //U.log("sec::::"+sec);
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

/*	public static String[] getGoogleAdd(String lat, String lon) throws Exception {
		String glink = "https://maps.google.com/maps?q=" + lat + "," + lon;
		//U.log("Visiting :" + glink);
		// WebDriver driver=new FirefoxDriver();
		// driver.get(glink);
		String htm = U.getHTML(glink);
		//U.log(U.getCache(glink));
		return U.getGoogleAddress(htm);
	}
*/
	public static String formatAddress(String code) {

		String sec = code.replaceAll("\\&nbsp;|�|&#038;|�", " ");
		sec = sec.replaceAll(" {2,}", " ");

		sec = sec.replaceAll("\\s{2,}", "");

		sec = sec.replaceAll("<br>|<br[^>]+>|</p><p[^>]+>|</p>|</br>|<BR>|<BR />|<br />", ",");

		sec = sec.replaceAll("<[^>]+>", "");

		sec = sec.replaceFirst("[^>]+>", "");
		// //U.log("sec:"+sec);
		// Priti-----

		if (sec.contains("Ave") && !sec.contains("Ave,") && !sec.contains("Avenue") && !sec.contains("Avenue,"))
			sec = sec.replace("Ave", "Ave,");
		if (sec.contains("Rd") && !sec.contains("Rd,") && !sec.contains("Rd."))
			sec = sec.replace("Rd", "Rd,");

		// -----------------------
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
		if (sec.contains("Ct") && !sec.contains("Ct,") && !sec.contains("Ct. #"))
			sec = sec.replace("Ct", "Ct,");
		if (!sec.contains("Lane <") && sec.contains("Lane") && !sec.contains("Lane,"))
			sec = sec.replace("Lane", "Lane,");
		if (sec.contains("land Court") && !sec.contains("land Court.,"))
			sec = sec.replace("land Court", "land Court,");
		if (sec.contains(", St.") && !sec.contains(", St.,"))
			sec = sec.replace(", St.", " St.,");
		return sec;
	}

/*	public static String[] getGoogleAddress(String html) throws IOException {

		String section = U.getSectionValue(html, "addr:'", "'");
		//U.log("U section==" + section);
		String[] add = null;
		if (section != null) {
			add = U.getAddress(section.replaceAll("\\\\x26", "&"));
		}
		return add;
	}
*/
	/**
	 * @author Parag Humane
	 * @param args
	 */
	public static String getPageSource(String url) throws IOException {

		String html = null;
		String fileName = U.getCache(url);
		File cacheFile = new File(fileName);
		if (cacheFile.exists())
			return FileUtil.readAllText(fileName);
		WebDriver driver = new HtmlUnitDriver();

		driver.get(url);
		html = driver.getPageSource();
		if (!cacheFile.exists())
			FileUtil.writeAllText(fileName, html);
		return html;
	}// //

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

	static {
		try {
			bypassCertificate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

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

	// ========================================================written new code by
	// Upendra===================================================
	static String propTypeIgnore = "[v|V]illage|[L|l]uxurious [O|o]wner(.*?)s [b|B]athroom";

	public static String getNewPropType(String html) throws Exception {
		html = getNoHtml(html);
		html = html.replaceAll("&nbsp;", " ").toLowerCase().replaceAll(propTypeIgnore.toLowerCase(), "");
		BufferedReader bufReader = new BufferedReader(new FileReader("resources/propTypes.txt"));
		String match, list = "";
		String line = bufReader.readLine();
		while (line != null) {
			if (line.contains("\"\""))
				throw new Exception("Check Property File in line " + line);
			line = line.replaceAll("\"\\s*,\\s*\"", "\",\"");
			String[] proeV = line.split("\",\"");
			// log("hello:->"+proeV[0]);
			match = Util.match(html, proeV[0].replace("\"", ""), 0);
			if (match != null) {
				proeV[1] = proeV[1].replace("\"", "");
				//U.log("match Ptype::" + match);
				if (!list.contains(proeV[1])) {
					list = (list.length() == 0) ? proeV[1] : list + ", " + proeV[1];
				}
			}
			line = bufReader.readLine();
		}

		list = (list.length() < 4) ? "-" : list;
		bufReader.close();
		return list;
	}

	// ========================================================written new code by
	// Upendra===================================================
	private static final String dpropTypeIgnore = "branch";

	public static String getNewdCommType(String html) throws Exception {
		BufferedReader bufReader = new BufferedReader(new FileReader("resources/dcommTypes.txt"));

		String match, dType = "";
		String line = bufReader.readLine();
		while (line != null) {
			if (line.contains("\"\""))
				throw new Exception("Check DProperty File in line " + line);
			line = line.replaceAll("\"\\s*,\\s*\"", "\",\"");
			String[] proeV = line.split("\",\"");
			html = getNoHtml(html);
//				//U.log(html.contains("ranch"));
			html = html.replaceAll("&nbsp;", " ").toLowerCase().replaceAll(dpropTypeIgnore.toLowerCase(), "");
			match = Util.match(html, proeV[0].replace("\"", "").trim(), 0);
			if (match != null) {
				proeV[1] = proeV[1].replace("\"", "");
				//U.log("match::" + match);
				if (!dType.contains(proeV[1])) {
					dType = (dType.length() == 0) ? proeV[1] : dType + ", " + proeV[1];
				}
			}
			line = bufReader.readLine();
		}
		dType = (dType.length() < 4) ? "-" : dType;
		bufReader.close();
		return dType;
	}

	// ========================================================written new code by
	// Upendra===================================================
	public static String getNewPropStatus(String html) throws Exception {
		BufferedReader bufReader = new BufferedReader(new FileReader("resources/propStatus.txt"));
		html = getNoHtml(html);
		html = html.replaceAll(" {2,} ", "");
		html = html.replaceAll("&nbsp;", " ").toLowerCase().replaceAll(PropertyStatus.propStatusIgnore.toLowerCase(), "");
		// //U.log("propstatushtml--------------------------------------"+html);
		String status = "-", match;
		String pStatus = "-";
		String line = bufReader.readLine();
		// log(line);
		while (line != null) {
			line = line.replace("\"", "");
			if (line.contains("\"\""))
				throw new Exception("Check Property status File  in line " + line);
			match = Util.match(html, line, 0);
			if (match != null) {
				//U.log("item :" + line);
				html = html.toUpperCase().replace(line.toUpperCase(), "");
				status = U.getCapitalise(match.toLowerCase());
				if (pStatus.equals("-"))
					pStatus = status;
				else
					pStatus = pStatus + ", " + status;
			}
			line = bufReader.readLine();

		}
		bufReader.close();
		return pStatus;
	}

	public static String getPropType(String html) {
		HashSet<String> type = new HashSet<String>();
		String match, list = "", value;
		Iterator<?> i = PropertyType.propTypes.entrySet().iterator();
		while (i.hasNext()) {
			Map.Entry me = (Map.Entry) i.next();
			value = me.getValue().toString();
			html = html.replaceAll("Cabinet|cabinet", "");
			match = Util.match(html, me.getKey().toString(), 0);
			if (match != null) {
				U.log("match Ptype::" + match);
				if (!list.contains(value)) {
					list = (list.length() == 0) ? value : list + ", " + value;
				}
			}
		}
		list = (list.length() < 4) ? "-" : list;
		return list;
	}

	public static String getPropStatus(String html) {
		html = html.replaceAll(" {2,} ", "");
		html = html.replaceAll("&nbsp;", " ").toLowerCase().replaceAll(PropertyStatus.propStatusIgnore.toLowerCase(), "");
		// //U.log("propstatushtml--------------------------------------"+html);
		String status = "-", match;
		String pStatus = "-";
		for (String item : PropertyStatus.propStatus) {
			// //U.log(html);
			match = Util.match(html, item, 0);

			if (match != null) {
				 U.log("match::"+match);
				// //U.log("::kirti");
				html = html.toUpperCase().replace(item.toUpperCase(), "");
//				 //U.log("html:::"+html);
//				U.log("item :" + item);
				status = U.getCapitalise(match.toLowerCase());
				if (pStatus.equals("-"))
					pStatus = status;
				else
					pStatus = pStatus + ", " + status;
			}

		}
		return pStatus;
	}

	public static String getCommType(String html) {

		return U.getCommunityType(html);
	}

	public static String getdCommType(String html) {

		HashSet<String> type = new HashSet<String>();
		String match, list = "", value;
		Iterator<?> i = DerivedPropertyType.dcommTypes.entrySet().iterator();
		while (i.hasNext()) {
			Map.Entry me = (Map.Entry) i.next();
			value = me.getValue().toString();
			match = Util.match(html, me.getKey().toString(), 0);
			if (match != null) {
				U.log(value + "::::::::" + match);
				if (!list.equals(value)) {
					list = (list.length() == 0) ? value : list + ", " + value;
				}
			}
		}
		// //U.log("list : " + list);
		list = (list.length() < 4) ? "-" : list;
		return list;
	}

	public static String getCommunityType(String html) {

		HashSet<String> type = new HashSet<String>();
		String match, list = "", value;
		Iterator<?> i = communityType.entrySet().iterator();
		while (i.hasNext()) {
			Map.Entry me = (Map.Entry) i.next();
			value = me.getValue().toString();

			match = Util.match(html, me.getKey().toString(), 0);
			if (match != null) {
				U.log("match ctype:::" + match);
				if (!list.contains(value)) {
					list = (list.length() == 0) ? value : list + ", " + value;
				}
			}
		}
		list = (list.length() < 4) ? "-" : list;
		return list;
	}

	public static String removeComments(String html) {
		String[] values = U.getValues(html, "<!--", "-->");
		for (String item : values)
			html = html.replace(item, "");
		return html;

	}

/*	public static boolean isvalideLatLng1(String add[], String lat, String lng) throws Exception {

		// FirefoxDriver driver2;
		if (lat != "" || lat == null) {
			String link = "https://maps.google.com/maps?q=" + lat + "," + lng;
			String htm = U.getHTML(link);
			String gAdd[] = U.getGoogleAddress(htm);
			//U.log(gAdd[0] + " " + gAdd[1] + " " + gAdd[2] + " " + gAdd[3]);
			if (gAdd[3].equals(add[3]))
				return true;
			else if (gAdd[1].equalsIgnoreCase(add[1]) && gAdd[2].equalsIgnoreCase(add[2])) {
				return true;
			}

			else {
				// http://maps.yahoo.com/place/?lat=37.71915&lon=-121.845803&q=37.71915%2C-121.845803
				link = "http://maps.yahoo.com/place/?lat=" + lat + "&lon=" + lng + "&q=" + lat + "," + lng;

				driver = ShatamFirefox.getInstance();

				driver.open(link);
				driver.getOne("#yucs-sprop_button").click();
				htm = driver.getHtml();

				// driver2 = new FirefoxDriver();
				// driver2.
				String url = driver.getUrl();
				//U.log(url);

				//URLDecoder decode = new URLDecoder();
				try {
					url = URLDecoder.decode(url, "UTF-8");
					//U.log("Address:" + url);
					//U.log(add[0] + " " + add[1] + " " + add[2] + " " + add[3]);
					if (url.contains(add[1]))
						return true;
					if (url.contains(add[3]))
						return true;
				} catch (UnsupportedEncodingException ex) {
					ex.printStackTrace();
				}
				// decode.decode(url);
				// //U.log(url);
				// driver.close();
			}
			return false;
			// if(gAdd[1])
		}
		return false;
	}
*/
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
		//U.log(binghtml);
		String secmap = U.getSectionValue(binghtml, "<div id=\"resultsDiv\"", "</body>");
		bingLatLng[0] = U.getSectionValue(secmap, "<span id=\"span1\">Latitude:", "<br>").trim();
		bingLatLng[1] = U.getSectionValue(secmap, "<br>Longitude:", "</span>");

		//U.log("lat:" + bingLatLng[0]);
		//U.log("lng:" + bingLatLng[1]);
		return bingLatLng;
	}
*/
	public static String[] getBingAddress(String lat, String lon) throws Exception {
		String[] addr = null;
		String htm = U.getHTML("http://dev.virtualearth.net/REST/v1/Locations/" + lat + "," + lon
				+ "?o=json&jsonp=GeocodeCallback&key=Ak8RoKwrZE-IbLkkXRFae9UTXw2UhpuET1mWY9z_ZkzTYR_-TCG8pMcNIUEtiqo5");
		U.log("bing url :::: " + "http://dev.virtualearth.net/REST/v1/Locations/" + lat + "," + lon
				+ "?o=json&jsonp=GeocodeCallback&key=Ak8RoKwrZE-IbLkkXRFae9UTXw2UhpuET1mWY9z_ZkzTYR_-TCG8pMcNIUEtiqo5");
		U.log("Bing cache :::: " + "http://dev.virtualearth.net/REST/v1/Locations/" + lat + "," + lon
				+ "?o=json&jsonp=GeocodeCallback&key=Ak8RoKwrZE-IbLkkXRFae9UTXw2UhpuET1mWY9z_ZkzTYR_-TCG8pMcNIUEtiqo5");
		String[] adds = U.getValues(htm, "formattedAddress\":\"", "\"");
		for (String item : adds) {
			addr = U.getFormattedAddressBing(item);
			if (addr == null || addr[0] == "-") {
				continue;
			}
			else if(addr[0]==null || addr[1]==null || addr[2]==null || addr[3]==null) {
				addr[0] = item.split(",")[0].trim();
				addr[1] = item.split(",")[1].trim();
				addr[2] = item.split(",")[2].trim().replaceAll("-\\d{4}", "");
				addr[3] = Util.match(addr[2], "\\d+");
				addr[2] = addr[2].replaceAll("\\d+", "").trim();
				U.log("bing address => " + Arrays.toString(addr));
				return addr;
			}
			else {
				U.log("Bing Address =>  Street:" + addr[0] + " City :" + addr[1] + " state :" + addr[2] + " ZIP :"
						+ addr[3]);
				return addr;
			}

		}
		return addr;
	}
	
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

	public static String[] getBingAddress1(String lat, String lon) throws Exception {
		U.log("getBingAddress");
		String[] addr = null;
//		String htm = U.getHTML("http://dev.virtualearth.net/REST/v1/Locations/" + lat + "," + lon
//				+ "?o=json&jsonp=GeocodeCallback&key=Anqg-XzYo-sBPlzOWFHIcjC3F8s17P_O7L4RrevsHVg4fJk6g_eEmUBphtSn4ySg");
//				+ "?o=json&jsonp=GeocodeCallback&key=Ak8RoKwrZE-IbLkkXRFae9UTXw2UhpuET1mWY9z_ZkzTYR_-TCG8pMcNIUEtiqo5");
		String htm = U.getHTML("http://dev.virtualearth.net/REST/v1/Locations/" + lat + "," + lon
				+ "?o=json&jsonp=GeocodeCallback&key=Ak8RoKwrZE-IbLkkXRFae9UTXw2UhpuET1mWY9z_ZkzTYR_-TCG8pMcNIUEtiqo5");
U.log("http://dev.virtualearth.net/REST/v1/Locations/" + lat + "," + lon
		+ "?o=json&jsonp=GeocodeCallback&key=Ak8RoKwrZE-IbLkkXRFae9UTXw2UhpuET1mWY9z_ZkzTYR_-TCG8pMcNIUEtiqo5");


		String fileName = getCache("http://dev.virtualearth.net/REST/v1/Locations/" + lat + "," + lon
				+ "?o=json&jsonp=GeocodeCallback&key=Ak8RoKwrZE-IbLkkXRFae9UTXw2UhpuET1mWY9z_ZkzTYR_-TCG8pMcNIUEtiqo5");
		U.log(fileName);
		String[] adds = U.getValues(htm, "formattedAddress\":\"", "\"");
		for (String item : adds) {
			U.log(item);
			item = item.replace(", United States", "");
			item = item.replace(", USA", "");
			addr = U.getAddress(item);
			U.log(addr);
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

	public static String[] getBingLatLong(String[] address) throws Exception {
		String[] latLong = { null, null };
		if (address == null || address[0] == "-" || address[1] == "-" || address[2] == "-")
			return latLong;
		else {
			String line = address[0] + "," + address[1] + "," + address[2] + " " + address[3];
			try {
//				U.log(line);
				latLong = getBingLatLong(line);
			} catch (java.io.IOException ex) {
				return latLong;
			}
		}
		U.log(latLong[0]+latLong[1]);
		return latLong;
	}

//	public static String[] getBingLatLong(String addressLine) throws Exception {
//		String[] latLong = new String[2];
//		if (addressLine == null || addressLine.trim().length() == 0)
//			return null;
//		addressLine = addressLine.trim().replaceAll("\\+", " ");
//		String geocodeRequest = "http://dev.virtualearth.net/REST/v1/Locations/'" + addressLine
////				+ "'?o=xml&key=Anqg-XzYo-sBPlzOWFHIcjC3F8s17P_O7L4RrevsHVg4fJk6g_eEmUBphtSn4ySg";
//				+ "'?o=xml&key=Ak8RoKwrZE-IbLkkXRFae9UTXw2UhpuET1mWY9z_ZkzTYR_-TCG8pMcNIUEtiqo5";
//
//		// //U.log("-----------------addressline-----------"+geocodeRequest);
//		String xml = U.getHTML(geocodeRequest);
//		// //U.log("--------------------------xml---------------------------------"+xml);
//		latLong[0] = U.getSectionValue(xml, "<Latitude>", "</Latitude>");
//		latLong[1] = U.getSectionValue(xml, "<Longitude>", "</Longitude>");
//		return latLong;
//	}
	public static String[] getBingLatLong(String addressLine) throws Exception {
		String[] latLong = new String[2];
		if (addressLine == null || addressLine.trim().length() == 0)
			return null;
		addressLine = addressLine.trim().replaceAll("\\+", " ");
		String geocodeRequest = "http://dev.virtualearth.net/REST/v1/Locations/'" + addressLine
				+ "'?o=xml&key=Ak8RoKwrZE-IbLkkXRFae9UTXw2UhpuET1mWY9z_ZkzTYR_-TCG8pMcNIUEtiqo5";
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
			if (add[i] == null || addr[i] == null || add[i].equals("-") || addr[i].equals("-"))
				return isMatched;
		}

		if (addr[3].equals(add[3]))
			isMatched = true;
		else {
			if (addr[1].equalsIgnoreCase(add[1]) && addr[2].equalsIgnoreCase(add[2]))
				isMatched = true;
			else {
				if (addr[2].equalsIgnoreCase(add[2]) && add[3].substring(0, 3).equals(addr[3].substring(0, 3)))
					isMatched = true;
			}
		}
		return isMatched;
	}

/*	public static boolean isValidLatLong(String[] latLon, String[] add) throws Exception {
		boolean isValid = false;
		if (latLon == null || latLon[1] == null || add == null)
			return isValid;

		//U.log("lat :" + latLon[0] + " Long :" + latLon[1]);

		if (latLon[0] == "-" || latLon[1] == "-" || add[3] == "-" || add[1] == "-" || add[2] == "-")
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
	}*/

	public static String getCapitalise(String line) {
		line = line.trim().replaceAll("\\s+", " ");
		String[] words = line.trim().split(" ");
		String capital = null, wd;
		for (String word : words) {
			wd = word.substring(0, 1).toUpperCase() + word.substring(1);
			capital = (capital == null) ? wd : capital + " " + wd;
		}
		return capital;
	}

/*	public static String[] getBuilderList() {
		String path = "C://cache//builderlist.txt";
		int nline = 212;
		String[] textData = new String[nline];
		try {
			FileReader fr = new FileReader(path);
			BufferedReader br = new BufferedReader(fr);
			for (int i = 0; i < nline; i++) {
				textData[i] = br.readLine();
				// //U.log(textData[i]);
			}
			br.close();
		} catch (Exception e) {
		}
		return textData;
	}
*/
/*	public static String[] getAddressFromSec(String address, String skipLines) {

		String[] OUT = { "-", "-", "-", "-" };
		if (address != null) {
			String[] ADD = U.getZipStateCity(address);
			OUT[1] = ADD[0];
			OUT[2] = ADD[1];
			OUT[3] = ADD[2];
			String street = "-";
			// String ignore_ADD=;
			// //U.log("ADD[0]::" + ADD[0]);
			if (ADD[0] != null || ADD[1] != null) {
				// //U.log("ADDRESS : " + address);
				int streetSec = -1;
				String[] addSections = U.getValues(address, ">", "<");
				int counter = 0;
				if (addSections.length > 0) {
					for (String secLoc : addSections) {
						// //U.log("***& " + secLoc);
						if (secLoc.contains("Rd") || !secLoc.contains("Draper") && secLoc.contains("Dr")
								|| secLoc.contains("Blvd") || secLoc.contains("Boulevard")) {
							counter++;
							// //U.log("***& secLoc 1");
							continue;
						}
						String chkSec = Util.match(secLoc, ADD[1]);
						if (chkSec != null) {
							streetSec = counter;
							break;
						}
						// //U.log("ADD[0]::" + ADD[0]);
						if (ADD[0] != null)
							chkSec = Util.match(secLoc, ADD[0]);
						if (chkSec != null) {
							streetSec = counter;
							break;
						}
						counter++;
					}
				}

				// //U.log("@@@ " + streetSec);

				if (streetSec == 0)
					street = addSections[0];
				if (streetSec == 1)
					street = addSections[0];
				if (streetSec == 2) {
					String find = Util.match(addSections[0] + addSections[1], skipLines);
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
	}*/

/*	public static String[] getZipStateCity(String addSec) { // getZipStateCity
		String[] ZSC = { "-", "-", "-" };

		String state = getState(addSec);
		// //U.log("state==" + state);
		String zip = getZip(addSec, state);
		// //U.log("%%%%% :" + zip);
		if (zip != "-") {
			String ST = Util.match(zip, "\\w+");
			if (ST != "-") {
				String check = Util.match(state, ST);
				if (check == null) {
					zip = Util.match(zip, "\\w+\\W+(\\d{4,5})", 1);
					// //U.log("*********" + zip);
					String Real = "\\w+\\W+" + zip;
					// //U.log("Real*********" + Real);
					String stateReal = Util.match(addSec, Real);
					if (stateReal != null) {
						if (stateReal.contains("Hampshire") || stateReal.contains("Jersey")
								|| stateReal.contains("Mexico") || stateReal.contains("York")
								|| stateReal.contains("Carolina") || stateReal.contains("Dakota")
								|| stateReal.contains("Island") || stateReal.contains("Virginia")) {
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
		// //U.log("********************@*" + city);
		String chkCity = city + "(.*?)<"; // \\s\\w{2}
		chkCity = Util.match(addSec, chkCity);
		if (chkCity != null) {
			String chkCity2 = chkCity.replace(city, "");
			if (chkCity2.contains("Rd") || chkCity2.contains("Dr") || chkCity2.contains("Blvd")
					|| chkCity2.contains("Boulevard") || chkCity2.contains("Park")) {
				String LOCaddSec = addSec.replace(chkCity, "");
				String LOCcity = getCity(LOCaddSec, state);
				//U.log("POP**  :" + LOCcity);
				if (LOCcity.trim() != null && LOCcity.trim().length() > 3)
					city = LOCcity;
			}
		}
		ZSC[0] = city;
		// //U.log(ZSC[0]+"::::::::::city");
		return ZSC;
	}*/

/*	public static String getStateAbbr(String state) {
		if (state.trim().contains("Alabama") || state.contains("AL"))
			return "AL";
		if (state.contains("Alaska") || state.contains("AK"))
			return "AK";
		if (state.contains("Arizona") || state.contains("AZ"))
			return "AZ";
		if (state.contains("Arkansas") || state.contains("AR"))
			return "AR";

		if (state.contains("California") || state.contains("CA"))
			return "CA";

		if (state.contains("Colorado") || state.contains("CO"))
			return "CO";

		if (state.contains("Connecticut") || state.contains("CT"))
			return "CT";
		if (state.contains("Delaware") || state.contains("DE"))
			return "DE";
		if (state.contains("Florida") || state.contains("FL"))
			return "FL";

		if (state.contains("Georgia") || state.contains("GA"))
			return "GA";
		if (state.contains("Hawaii") || state.contains("HI"))
			return "HI";
		if (state.contains("Idaho") || state.contains("ID"))
			return "ID";

		if (state.contains("Illinois") || state.contains("IL"))
			return "IL";
		if (state.contains("Indiana") || state.contains("IN"))
			return "IN";
		if (state.contains("Iowa") || state.contains("IA"))
			return "IA";
		if (state.contains("Kansas") || state.contains("KS"))
			return "KS";
		if (state.contains("Kentucky") || state.contains("KY"))
			return "KY";
		if (state.contains("Louisiana") || state.contains("LA"))
			return "LA";
		if (state.contains("Maine") || state.contains("ME"))
			return "ME";
		if (state.contains("Maryland") || state.contains("MD"))
			return "MD";
		if (state.contains("Massachusetts") || state.contains("MA"))
			return "MA";
		if (state.contains("Michigan") || state.contains("MI"))
			return "MI";
		if (state.contains("Minnesota") || state.contains("MN"))
			return "MN";
		if (state.contains("Mississippi") || state.contains("MS"))
			return "MS";
		if (state.contains("Missouri") || state.contains("MO"))
			return "MO";
		if (state.contains("Montana") || state.contains("MT"))
			return "MT";
		if (state.contains("Nebraska") || state.contains("NE"))
			return "NE";
		if (state.contains("Nevada") || state.contains("NV"))
			return "NV";
		if (state.contains("New Hampshire") || state.contains("NH"))
			return "NH";
		if (state.contains("New Jersey") || state.contains("NJ"))
			return "NJ";
		if (state.contains("New Mexico") || state.contains("NM"))
			return "NM";
		if (state.contains("New York") || state.contains("NY"))
			return "NY";
		if (state.contains("North Carolina") || state.contains("NC"))
			return "NC";
		if (state.contains("North Dakota") || state.contains("ND"))
			return "ND";
		if (state.contains("Ohio") || state.contains("OH"))
			return "OH";
		if (state.contains("Oklahoma") || state.contains("OK"))
			return "OK";
		if (state.contains("Oregon") || state.contains("OR"))
			return "OR";
		if (state.contains("Pennsylvania") || state.contains("PA"))
			return "PA";
		if (state.contains("Rhode Island") || state.contains("RI"))
			return "RI";
		if (state.contains("South Carolina") || state.contains("SC"))
			return "SC";
		if (state.contains("South Dakota") || state.contains("SD"))
			return "SD";
		if (state.contains("Tennessee") || state.contains("TN"))
			return "TN";
		if (state.contains("Texas") || state.contains("TX"))
			return "TX";
		if (state.contains("Utah") || state.contains("UT"))
			return "UT";
		if (state.contains("Vermont") || state.contains("VT"))
			return "VT";
		if (state.contains("Virginia") || state.contains("VA"))
			return "VA";
		if (state.contains("Washington") || state.contains("WA"))
			return "WA";
		if (state.contains("West Virginia") || state.contains("WV"))
			return "WV";
		if (state.contains("Wisconsin") || state.contains("WI"))
			return "WI";
		if (state.contains("Wyoming") || state.contains("WY"))
			return "WY";
		else
			return null;
	}
*/
/*	private static String getState(String addSec) { // getZipStateCity

		String ignoreStreet = "1350 N New York St";
		addSec = addSec.replace(ignoreStreet, "");

		String state = Util.match(addSec,
				"Alabama|Alaska|Arizona|Arkansas|California|Colorado|Connecticut|Delaware|Florida|Georgia|Hawaii|Idaho|Illinois|Indiana|Iowa|Kansas|Kentucky|Louisiana|Maine|Maryland|Massachusetts|Michigan|Minnesota|Mississippi|Missouri|Montana|Nebraska|Nevada|New Hampshire|New Jersey|New Mexico|New York|North Carolina|North Dakota|Ohio|Oklahoma|Oregon|Pennsylvania|Rhode Island|South Carolina|South Dakota|Tennessee|Texas|Utah|Vermont|West Virginia|Virginia|Washington|Wisconsin|Wyoming");

		String[] sameName = { "Washington", "Maine", "Delaware", "Missouri", "Virginia", "Nevada", "Iowa", "Colorado" };
		// //U.log("my state::"+state);
		if (state != null) {
			for (String same : sameName) {
				if (state.contains(same)) {
					addSec = addSec.replaceAll(same, "##@@");
					String LOLstate = getState(addSec);
					// //U.log("LOLstate : " + LOLstate);
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
			state = Util.match(addSec,
					"Alabama|Alaska|Arizona|Arkansas|California|Colorado|Connecticut|Delaware|Florida|Georgia|Hawaii|Idaho|Illinois|Indiana|Iowa|Kansas|Kentucky|Louisiana|Maine|Maryland|Massachusetts|Michigan|Minnesota|Mississippi|Missouri|Montana|Nebraska|Nevada|New Hampshire|New Jersey|New Mexico|New York|North Carolina|North Dakota|Ohio|Oklahoma|Oregon|Pennsylvania|Rhode Island|South Carolina|South Dakota|Tennessee|Texas|Utah|Vermont|West Virginia|Virginia|Washington|Wisconsin|Wyoming");
			if (state != null)
				return state;
		}

		if (state == null)
			state = "-";
		return state;
	}*/

/*	private static String getZip(String addSec, String state) { // getZipStateCity
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
*/
	public static String getCity(String addSec, String state) { // getZipStateCity
		//U.log("*#*  :" + state + ": *#*");
		if (state.length() > 2)
			state = USStates.abbr(state.trim());
		//U.log(state);
		// addSec=addSec.replace("766 Austin Lane", "");
		String ingoreStreet = "2820 Bellevue Way NE|2303 San Marcos|Santa Rosa Villas|8541 N. Walnut Way|208 S. Auburn Heights Lane|2228 Tracy Place|3200 Osprey Lane|Kannapolis Parkway|1350 N New York St|8500 N. Fallbrook Avenue|Woodruff Road|Hopewell Road|Old Spartanburg Road|Zebulon Road|Kailua Road|Evans Mill Road|Arnold Overlook Lane|Wellington Woods Avenue|11405 Pacey's Pond Circle|405 San Luis Obispo St|4307 Walnut Avenue|12422 Mar Vista Street";
		addSec = addSec.replaceAll(ingoreStreet, "");
		String city = "-";
		if (state.trim().contains("Alabama") || state.contains("AL")) {
			city = Util.match(addSec,
					"Hunstville|Montevallo|Magnolia Springs|Orange Beach|Auburn|Alabaster|Albertville|Alexander City|Anniston|Athens|Atmore|Banks|Bessemer|Birmingham|Brewton|Center Point|Cottonton|Cullman|Daleville|Daphne|Decatur|Dothan|Enterprise|Eufaula|Fairfield|Fairhope|Florence|Foley|Forestdale|Fort Payne|Gadsden|Gardendale|Grove Hill|Hartselle|Helena|Homewood|Hoover|Hueytown|Huntsville|Jackson|Jasper|Kimberly|Leeds|Lisman|Lowndesboro|McCalla|Madison|Millbrook|Mobile|Monroeville|Montgomery|Mountain Brook|Muscle Shoals|Northport|Opelika|Oxford|Ozark|Pelham|Phenix City|Prattville|Prichard|Repton|Robertsdale|Saks|Saraland|Scottsboro|Selma|Sheffield|Slocomb|Smiths|Sylacauga|Talladega|Tallassee|Theodore|Tillmans Corner|Toney|Troy|Trussville|Tuscaloosa|Tuskegee|Vestavia([,\\W])?Hills|Warrior|Calera|Pinson|Chelsea|Springville|Moody|Fultondale|Odenville|Liberty Park Area|VestaviaHills|Hazel Green|Meridianville|Owens Cross Roads|Spanish Fort|Loxley|Semmes|Gulf Shores|Summerdale|Deatsville|Wetumpka|Vance|Moundville|Vestavia-Hills|Fulton|Harvest|Margaret");
		}
		if (state.contains("Alaska") || state.contains("AK")) {
			city = Util.match(addSec,
					"Anchorage|College|Fairbanks|Juneau|Kodiak|Adak|Akhiok|Akiachak|Akiak|Akutan|Alakanuk|Bettles|Bethel|Big Lake|Bristol Bay|Coffman Cove|Cordova|Crow Village|Delta Junction|Dillingham|Ester|Fairbanks|Haines|Homer|Hoonah|Kenai|Ketchikan|Knik|Kotzebue|Matanuska-Susitna Borough|Nome|North Pole|Palmer|Pelican|Petsburg|Seward|Sitka|Skagway|Soldotna|Thorne Bay|Unalaska|Valdez|Wasilla|Whittier|Wrangell");
		}
		if (state.contains("Arizona") || state.contains("AZ")) {
			city = Util.match(addSec,
					"Barcelona|Apache Junction|Avondale|Bullhead City|Casa Grande|Casas Adobes|Catalina Foothills|Cave Creek|Chambers|Chandler|Chinle|Cottonwood-Verde Village|Douglas|Drexel Heights|Eloy|Flagstaff|Florence|Flowing Wells|Fortuna Foothills|Fountain Hills|Fredonia|Gilbert|Glendale|Globe|Goodyear|Green Valley|Heber|Holbrook|Houck|Lake Havasu City|Marana|Mayer|Mesa|Mohave Valley|New Kingman-Butler|New River|Nogales|Oro Valley|Paradise Valley|Payson|Peoria|Phoenix|Pima|Prescott Valley|Prescott|Quartzsite|Rimrock|Riviera|Sacaton|Safford|San Luis|Scottsdale|Sedona|Show Low|Sierra Vista Southeast|Sierra Vista|Sun City West|Sun City|Sun Lakes|Surprise|Tacna|Tanque Verde|Teec Nos Pos|Tempe|Tonalea|Tucson|Whiteriver|Willcox|San Tan Valley|Maricopa|Buckeye|Queen Creek|Sahuarita|Vail|Yuma|Page");
		}
		if (state.contains("Arkansas") || state.contains("AR")) {
			city = Util.match(addSec,
					"Amity|Arkadelphia|Atkins|Batesville|Bella Vista|Bentonville|Benton|Blytheville|Cabot|Camden|Conway|Dermott|El Dorado|Fayetteville|Forrest City|Fort Smith|Franklin|Hampton|Hardy|Harrison|Heber Springs|Hermitage|Hope|Hot Springs|Jonesboro|Little Rock|Magnolia|Malvern|Marmaduke|Marshall|Maumelle|Mountain Home|Mountain View|Newport|Norfork|North Little Rock|Oden|Ola|Paragould|Pine Bluff|Pocahontas|Rogers|Russellville|Salem|Searcy|Sherwood|Shirley|Siloam Springs|Sparkman|Springdale|Texarkana|Van Buren|Waldron|West Memphis|Wynne");
		}

		if (state.contains("California") || state.contains("CA")) {
			city = Util.match(addSec,
					"Arabella|Winchester|Diablo Grande|Rolling Hills Estates|Adelanto|Agoura Hills|Alameda|Alamo|Albany|Alhambra|Aliso Viejo|Alpine|Altadena|Alta|Alum Rock|Anaheim Hills|Anaheim|Angels Camp|Angwin|Antioch|Apple Valley|Aptos|Arcadia|Arcata|Arden-Arcade|Arroyo Grande|Artesia|Arvin|Ashland|Atascadero|Atwater|Auburn|Avenal|Avocado Heights|Azusa|Bakersfield|Baker|Baldwin Park|Banning|Barstow|Bass Lake|Bay Point|Baywood-Los Osos|Beaumont|Bell Gardens|Bellflower|Belmont|Benicia|Berkeley|Beverly Hills|Big Bear City|Big Creek|Blackhawk-Camino Tassajara|Bloomington|Blythe|Bonita|Bostonia|Brawley|Brea|Brentwood|Buena Park|Burbank|Burlingame|Cabazon|Calabasas|Calexico|Camarillo|Cameron Park|Campbell|Canoga Park|Capitola|Carlsbad|Carmel|Carmichael|Carpinteria|Carson|Casa de Oro-Mount Helix|Castro Valley|Cathedral City|Central Valley|Ceres|Cerritos|Chatsworth|Cherryland|Chico|Chino Hills|Chino|Chowchilla|Chula Vista|Citrus Heights|Citrus|Claremont|Clayton|Clearlake|Cloverdale|Clovis|Coachella|Coalinga|Colton|Commerce|Compton|Concord|Corcoran|Corning|Coronado|Corona|Costa Mesa|Cotati|Coto de Caza|Covina|Crescent City|Crestline|Cudahy|Culver City|Cupertino|Cypress|Daly City|Dana Point|Danville|Delano|Desert Hot Springs|Diamond Bar|Dinuba|Dixon|Downey|Duarte|Dublin|East Hemet|East Los Angeles|East Palo Alto|East San Gabriel|El Cajon|El Centro|El Cerrito|El Dorado Hills|El Monte|El Segundo|El Sobrante|Elk Grove|Encinitas|Encino|Escondido|Esparto|Eureka|Fair Oaks|Fairfield|Fallbrook|Fillmore|Florence-Graham|Florin|Folsom|Fontana|Foothill Farms|Foothill Ranch|Fort Bragg|Fortuna|Foster City|Fountain Valley|Fremont|Fresno|Fullerton|Galt|Garden Grove|Gardena|Gilroy|Glen Avon|Glendale|Glendora|Goleta|Grand Terrace|Granite Bay|Grass Valley|Greenfield|Grover Beach|Gualala|Guerneville|Hacienda Heights|Half Moon Bay|Hanford|Hawaiian Gardens|Hawthorne|Hayward|Healdsburg|Hemet|Hercules|Hermosa Beach|Hesperia|Highland|Hillsborough|Hollister|Hollywood|Homewood|Huntington Beach|Huntington Park|Imperial Beach|Indio|Inglewood|Irvine|Isla Vista|Jackson|Janesville|Junction City|Keene|King City|La Canada-Flintridge|La Crescenta-Montrose|La Habra|La Jolla|La Mesa|La Mirada|La Palma|La Presa|La Puente|La Quinta|La Riviera|La Verne|Lafayette|Laguna Beach|Laguna Hills|Laguna Niguel|Laguna Woods|Laguna|Lake Elsinore|Lake Forest|Lake Los Angeles|Lakehead|Lakeside|Lakewood|Lamont|Lancaster|Larkspur|Lathrop|Lawndale|Laytonville|Lemon Grove|Lemoore|Lennox|Lincoln|Linda|Lindsay|Live Oak|Livermore|Livingston|Lodi|Loma Linda|Lomita|Lompoc|Long Beach|Los Alamitos|Los Altos|Los Angeles|Los Banos|Los Gatos|Lynwood|Madera|Magalia|Malibu|Manhattan Beach|Manteca|Marina Del Rey|Marina|Martinez|Marysville|Maywood|McKinleyville|Menlo Park|Merced|Mill Valley|Millbrae|Milpitas|Mineral|Mira Loma|Mission Viejo|Modesto|Monrovia|Montclair|Montebello|Montecito|Monterey Park|Monterey|Moorpark|Moraga|Moreno Valley|Morgan Hill|Morro Bay|Mount Aukum|Mountain View|Murrieta|Napa|National City|Newark|Newberry Springs|Newport Beach|Nicolaus|Nipomo|Norco|North Auburn|North Fair Oaks|North Highlands|North Hollywood|Northridge|Norwalk|Novato|Oakdale|Oakland|Oakley|Oceanside|Oildale|Olivehurst|Ontario|Orangevale|Orange|Orcutt|Orinda|Orleans|Oroville|Oxnard|Pacific Grove|Pacifica|Pacoima|Palm Desert|Palm Springs|Palmdale|Palo Alto|Palos Verdes Estates|Panorama City|Paradise|Paradise, NVParamount|Parkway-South Sacramento|Parlier|Pasadena|Paso Robles|Patterson|Pedley|Perris|Petaluma|Philo|Pico Rivera|Piedmont|Pine Valley|Pinecrest|Pinole|Pioneer|Pittsburg|Placentia|Placerville|Playa Del Rey|Pleasant Hill|Pleasanton|Pomona|Port Hueneme|Porterville|Portola|Poway|Prunedale|Ramona|Rancho Cordova|Rancho Cucamonga|Rancho Mirage|Rancho Palos Verdes|Rancho San Diego|Rancho Santa Margarita|Red Bluff|Redding|Redlands|Redondo Beach|Redwood City|Redwood Estates|Reedley|Rialto|Richmond|Ridgecrest|Rio Linda|Ripon|Riverbank|Riverdale|Riverside|Rocklin|Rodeo|Rohnert Park|Rosamond|Rosemead|Rosemont|Roseville|Rossmoor|Rowland Heights|Rubidoux|Sacramento|Salida|Salinas|San Andreas|San Anselmo|San Bernardino|San Bruno|San Carlos|San Clemente|San Diego|San Dimas|San Fernando|San Francisco|San Gabriel|San Jacinto|San Jose|San Juan Capistrano|San Leandro|San Lorenzo|San Luis Obispo|San Marcos|San Marino|San Mateo|San Pablo|San Pedro|San Quentin|San Rafael|San Ramon|San Ysidro|Sanger|Santa Ana|Santa Barbara|Santa Clara|Santa Clarita|Santa Cruz|Santa Fe Springs|Santa Maria|Santa Monica|Santa Paula|Santa Rosa|Santee|Saratoga,|Sausalito|Scotts Valley|Seal Beach|Seaside|Sebastopol|Selma|Shafter|Sherman Oaks|Sierra Madre|Simi Valley|Solana Beach|Soledad|Sonora|South El Monte|South Gate|South Lake Tahoe|South Pasadena|South San Francisco|South San Jose Hills|South Whittier|South Yuba City|Spring Valley|Springville|Stanford|Stanton|Stockton|Suisun City|Sun City|Sun Valley|Sunnyvale|Susanville|Sutter Creek|Sylmar|Tamalpais-Homestead Valley|Tecate|Tehachapi|Temecula|Temple City|Thousand Oaks|Torrance|Tracy|Truckee|Tulare|Turlock|Tustin Foothills|Tustin|Twentynine Palms|Twin Bridges|Ukiah|Union City|Upland|Upper Lake|Vacaville|Valencia|Valinda|Valle Vista|Vallecito|Vallejo|Valley Springs|Van Nuys|Venice|Ventura|Victorville|View Park-Windsor Hills|Vincent|Vineyard|Visalia|Vista|Walnut Creek|Walnut Park|Walnut|Warner Springs|Wasco|Watsonville|West Carson|West Covina|West Hollywood|West Puente Valley|West Sacramento|West Whittier-Los Nietos|Westminster|Westmont|Whittier|Wildomar|Willits|Willowbrook|Wilmington|Windsor|Winter Gardens|Woodland Hills|Woodland|Yorba Linda|Yuba City|Yucaipa|Yucca Valley|Eastvale|Calimesa|Jurupa Valley|Menifee");
		}
		if (state.contains("Colorado") || state.contains("CO")) {
			city = Util.match(addSec,
					"Severance|Fort Lupton|Clifton|Aguilar|Alamosa|Arvada|Aspen|Aurora|Berkley|Black Forest|Boulder|Brighton|Broomfield|Buena Vista|Canon City|Castle Rock|Castlewood|Cimarron Hills|Colorado Springs|Columbine|Commerce City|Craig|Crested Butte|Crowley|Denver|Dillon|Dinosaur|Durango|Eads|Englewood|Estes Park|Fairplay|Federal Heights|Fort Carson|Fort Collins|Fort Morgan|Fountain|Glenwood Springs|Golden|Grand Junction|Greeley|Greenwood Village|Gunnison|Highlands Ranch|Hugo|Ken Caryl|Lafayette|Lakewood|Laporte|Leadville|Littleton|Longmont|Louisville|Loveland|Monte Vista|Montrose|Northglenn|Pagosa Springs|Parker|Pine|Pueblo West|Pueblo|Salida|San Luis|Security-Widefield|Sherrelwood|Southglenn|Steamboat Springs|Sterling|Thornton|Trinidad|U S A F Academy|Vail|Walden|Walsenburg|Welby|Westminster|Wheat Ridge|Windsor|Woodland Park|Centennial|Elizabeth|Firestone|Frederick|Erie|Timnath|Wellington|Lochbuie");
		}
		if (state.contains("Connecticut") || state.contains("CT")) {
			city = Util.match(addSec,
					"Ansonia|Avon|Berlin|Bethel|Bloomfield|Branford|Bridgeport|Bristol|Brookfield|Central Manchester|Cheshire|Clinton|Colchester|Conning Towers-Nautilus Park|Coventry|Cromwell|Danbury|Darien|Derby|East Hampton|East Hartford|East Haven|East Lyme|Ellington|Enfield|Fairfield|Farmington|Gales Ferry|Glastonbury|Granby|Greenwich|Griswold|Groton|Guilford|Hamden|Hartford|Killingly|Ledyard|Madison|Manchester|Mansfield|Meriden|Middletown|Milford|Monroe|Montville|Mystic|Naugatuck|New Britain|New Canaan|New Fairfield|New Haven|New London|New Milford|Newington|Newtown|North Branford|North Haven|Norwalk|Norwich|Old Saybrook|Orange|Plainfield|Plainville|Plymouth|Ridgefield|Rocky Hill|Seymour|Shelton|Simsbury|Somers|South Windsor|Southbury|Southington|Stafford|Stamford|Stonington|Storrs|Stratford|Suffield|Thomaston|Tolland|Torrington|Trumbull|Unionville|Vernon|Wallingford Center|Wallingford|Washington Depot|Waterbury|Waterford|Watertown|West Hartford|West Haven|Weston|Westport|Wethersfield|Willimantic|Wilton|Winchester|Windham|Windsor Locks|Windsor|Winsted|Wolcott");
		}
		if (state.contains("Delaware") || state.contains("DE")) {
			city = Util.match(addSec,
					"Magnolia|Frederica|Milton|Smyrna|Milford|Dagsboro|Ocean View|Frankford|Bear|Brookside|Dover|Glasgow|Hockessin|New Castle|Newark|Pike Creek|Selbyville|Wilmington|Townsend|Middletown|Fenwick Island");
		}
		if (state.contains("Florida") || state.contains("FL")) {
			city = Util.match(addSec,
					"Seffner|Wildwood|Port St. Joe|Southport|Mulberry|Mt. Dora|Debary|Valrico|Mims|Apollo Beach|Palm Shores|Alachua|Altamonte Springs|Apopka|Atlantic Beach|Auburndale|Aventura|Azalea Park|Bartow|Bayonet Point|Bayshore Gardens|Bellair-Meadowbrook Terrace|Belle Glade|Bellview|Bloomingdale|Boca Del Mar|Boca Raton|Bonita Springs|Boynton Beach|Bradenton Beach|Bradenton|Brandon|Brent|Brooksville|Brownsville|Bunnell|Callaway|Canal Point|Cape Coral|Carol City|Casselberry|Citrus Park|Citrus Ridge|Clearwater|Cocoa Beach|Cocoa|Coconut Creek|Conway|Cooper City|Coral Gables|Coral Springs|Coral Terrace|Country Club|Country Walk|Crestview|Cutler Ridge|Cutler|Cypress Lake|Dade City|Dania Beach|Davie|Daytona Beach|De Bary|De Funiak Springs|De Land|Deerfield Beach|Delray Beach|Deltona|Doral|Dunedin|East Lake|Edgewater|Egypt Lake-Leto|Elfers|Englewood|Ensley|Eustis|Fairview Shores|Fernandina Beach|Ferry Pass|Flagler Beach|Florida Ridge|Forest City|Fort Lauderdale|Fort Myers Beach|Fort Myers|Fort Pierce|Fleming Island|Fort Walton Beach|Fountainbleau|Fruit Cove|Fruitville|Gainesville|Gladeview|Glenvar Heights|Golden Gate|Golden Glades|Gonzalez|Greater Carrollwood|Greater Northdale|Greater Sun Center|Greenacres|Gulf Gate Estates|Gulfport|Haines City|Hallandale|Hamptons at Boca Raton|Hialeah Gardens|Hialeah|Hobe Sound|Holiday|Holly Hill|Hollywood|Homestead|Homosassa Springs|Orlando|Hosford|Hudson|Immokalee|Interlachen|Inverness|Iona|Ives Estates|Jacksonville Beach|Jacksonville|Jasmine Estates|Jensen Beach|Jupiter|Kendale Lakes|Kendall West|Kendall|Key Biscayne|Key Largo|Key West|Keystone|Kings Point|Kissimmee|Lady Lake|Lake Butler|Lake City|Lake Magdalene|Lake Mary|Lake Wales|Lake Worth Corridor|Lake Worth|Lakeland Highlands|Lakeland|Lakewood Park|Land O' Lakes|Largo|Lauderdale Lakes|Lauderhill|Leesburg|Lehigh Acres|Leisure City|Lighthouse Point|Lockhart|Longwood|Lutz|Lynn Haven|Maitland|Marathon|Marco Island|Marco|Margate|Mary Esther|Meadow Woods|Melbourne|Merritt Island|Miami Beach|Miami Lakes|Miami Shores|Miami Springs|Miami Gardens|Miami|Middleburg|Milton|Miramar|Montverde|Mount Dora|Myrtle Grove|Naples|New Port Richey|New Smyrna Beach|Niceville|Nokomis|Norland|North Fort Myers|North Lauderdale|North Miami Beach|North Miami|North Palm Beach|North Port|Oak Ridge|Oakland Park|Ocala|Ocoee|Ojus|Okeechobee|Oldsmar|Olympia Heights|Opa-Locka|Orange City|Orange Park|Ormond Beach|Oviedo|Palatka|Palm Bay|Palm Beach Gardens|Palm Beach|Palm City|Palm Coast|Palm Harbor|Palm River-Clair Mel|Palm Springs|Palm Valley|Palmetto Estates|Palmetto|Panama City|Parkland|Pembroke Pines|Pensacola|Pine Hills|Pinecrest|Pinellas Park|Pinewood|Placida|Plant City|Poinciana|Pompano Beach|Port Charlotte|Port Orange|Port Richey|Port Saint Joe|Port Salerno|Port St. John|Port St. Lucie|Princeton|Punta Gorda|Quincy|Richmond West|Riverview|Riviera Beach|Rockledge|Royal Palm Beach|Ruskin|Safety Harbor|San Carlos Park|Sandalfoot Cove|Sanderson|Sanford|Sarasota Springs|Sarasota|Scott Lake|Sebastian|Sebring|Seminole|South Bradenton|South Daytona|South Miami Heights|South Miami|South Venice|Spring Hill|Stuart|St. Augustine|St. Cloud|St. Petersburg|Summerland Key|Sunny Isles Beach|Sunrise|Sweetwater|Tallahassee|Tamarac|Tamiami|Tampa|Tarpon Springs|Temple Terrace|The Crossings|The Hammocks|Titusville|Town 'n' Country|Union Park|University Park|University|Upper Grand Lagoon|Venice|Vero Beach South|Vero Beach|Villas|Warrington|Wekiwa Springs|Wellington|West and East Lealman|West Little River|West Palm Beach|West Pensacola|Westchase|Westchester|Weston|Westwood Lakes|Wewahitchka|Wilton Manors|Winter Garden|Winter Haven|Winter Park|Winter Springs|Wright|Yulee|Yeehaw Junction|Zephyrhills|Windermere|Grand Island|Saint Cloud|Tavares|Lake Alfred|Davenport|Harmony|Clermont|Deland|Groveland|Lake Helen|West Melbourne|St. Johns|Callahan|Green Cove Springs|Saint Augustine|Beverly Beach|Freeport|Inlet Beach|Santa Rosa Beach|Panama City Beach|Point Washington|Pace|Navarre|Cantonment|Gulf Breeze|Miami Dade County|Haverhill|Port St Lucie|Osprey|Sun City Center|Thonotosassa|Trinity|Wimauma|Gibsonton|Wesley Chapel|Ellenton|Land O Lakes|St Petersburg|Viera|Jacksonville|Lakeside|Plantation|Alva,|Point Washington");
		}
		if (state.contains("Georgia") || state.contains("GA")) {
			city = Util.match(addSec,
					"Villa Rica|Lithia Springs|Bishop|Senoia|Stockbridge|Grayson|Austell|Bethlehem|Rex|Loganville|Grovetown|Hephzibah|Acworth|Albany|Alpharetta|Alto|Americus|Athens|Atlanta,|Augusta|Bainbridge|Belvedere Park|Brunswick|Buford|Calhoun|Candler-McAfee|Carrollton|Cartersville|Cochran|College Park|Columbus|Commerce|Conyers|Cordele|Covington|Dahlonega|Dalton|Dawson|Decatur|Demorest|Douglasville|Douglas|Druid Hills|Dublin|Duluth|Dunwoody|Dacula|East Point|Elberton|Evans|Fayetteville|Forest Park|Fort Benning South|Fort Stewart|Gainesville|Garden City|Georgetown|Griffin|Hinesville|Jonesboro|Kennesaw|Kingsland|La Grange|Lawrenceville|Lilburn|Lithonia|Mableton|Macon|Marietta|Martinez|Milledgeville|Monroe|Morrow|Moultrie|Mountain Park|Newnan|Newton|Norcross|North Atlanta|North Decatur|North Druid Hills|Panthersville|Peachtree City|Powder Springs|Redan|Reidsville|Riverdale|Rome|Roswell|Sandy Springs|Savannah|Smyrna|Snellville|Statesboro|Stone Mountain|St. Marys|St. Simons|Sugar Hill|Thomasville|Tifton|Toccoa,|Tucker|Union City|Union Point|Valdosta|Vidalia|Warner Robins|Waycross|Wilmington Island|Winder|Woodstock|Cumming|Hampton|Ellenwood|Hiram|Mcdonough|Dallas|Locust Grove|Fairburn|Flowery Branch|Canton|Suwanee|Byron|Kathleen|Perry|Guyton|Pooler|Richmond Hill");
		}
		if (state.contains("Hawaii") || state.contains("HI")) {
			city = Util.match(addSec,
					"Aiea|Ewa Beach|Halawa|Hanalei|Hilo|Honolulu|Kahului|Kailua Kona|Kailua|Kaneohe Station|Kaneohe|Kihei|Lahaina|Laupahoehoe|Lihue|Makakilo City|Makawao|Mililani Town|Nanakuli|Pearl City|Schofield Barracks|Wahiawa|Waianae|Wailuku|Waimalu|Waipahu|Waipio|Poipu - Koloa|Kapolei|Makakilo|Kamuela|Kona/ Kohala Coast");
		}
		if (state.contains("Idaho") || state.contains("ID")) {
			city = Util.match(addSec,
					"Ashton|Blackfoot|Boise|Caldwell|Cambridge|Coeur d'Alene|Cottonwood|Culdesac|Dubois|Eagle|Emmett|Fairfield|Garden City|Grace|Grangeville|Harrison|Idaho City|Idaho Falls|Lewiston|Malad City|Meridian|Moore|Moscow|Mountain Home|Nampa|North Fork|Paris|Pocatello|Post Falls|Rexburg|Rupert|Sandpoint|Shoshone|Sun Valley|Twin Falls|Wallace");
		}
		if (state.contains("Illinois") || state.contains("IL")) {
			city = Util.match(addSec,
					"Antioch|Yorkville|Addison|Algonquin|Alsip|Alton|Arlington Heights|Aurora|Barrington|Bartlett|Batavia|Beach Park|Beecher City|Belleville|Bellwood|Belvidere|Bensenville|Berwyn|Blandinsville|Bloomingdale|Bloomington|Blue Island|Bolingbrook|Bourbonnais|Bradley|Bridgeview|Brimfield|Brookfield|Buffalo Grove|Burbank|Burr Ridge|Cahokia|Calumet City|Canton|Carbondale|Carol Stream|Carpentersville|Cary|Centralia|Champaign|Charleston|Chicago Heights|Chicago Ridge|Chicago|Cicero|Collinsville|Country Club Hills|Crest Hill|Crestwood|Crystal Lake|Danville|Darien|Decatur|Deerfield|DeKalb|Des Plaines|Dixon|Dolton|Downers Grove|East Moline|East Peoria|East Saint Louis|East St. Louis|Edwardsville|Effingham|Elgin|Elk Grove Village|Elmhurst|Elmwood Park|Evanston|Evergreen Park|Fairview Heights|Flossmoor|Forest Park|Frankfort|Franklin Park|Freeport|Gages Lake|Galesburg|Geneva|Glen Carbon|Glen Ellyn|Glendale Heights|Glenview|Godfrey|Goodings Grove|Granite City|Grayslake|Gurnee|Hanover Park|Harvey|Hazel Crest|Herrin|Hickory Hills|Highland Park|Hillside|Hinsdale|Hoffman Estates|Homewood|Jacksonville|Joliet|Justice|Kankakee|Kewanee|La Grange Park|La Grange|Lake Forest|Lake in the Hills|Lake Zurich|Lansing|Lemont|Libertyville|Lincolnwood|Lincoln|Lindenhurst|Lisle|Lockport|Lombard|Loves Park|Lyons|Machesney Park|Macomb|Marion|Markham|Mascoutah|Matteson|Mattoon|Maywood|McHenry|Melrose Park|Midlothian|Mokena|Moline|Morris|Morton Grove|Morton|Mount Prospect|Mount Vernon|Mundelein|Murphysboro|Naperville|New Lenox|Niles|Normal|Norridge|North Aurora|North Chicago|Northbrook|Northlake|O'Fallon|Oak Forest|Oak Lawn|Oak Park|Orland Park|Oswego|Ottawa|Palatine|Palos Heights|Palos Hills|Palos Park|Park Forest|Park Ridge|Pekin|Peoria|Petersburg|Plainfield|Pontiac|Prairie Du Rocher|Prospect Heights|Quincy|Rantoul|Richton Park|River Forest|River Grove|Riverdale|Rock Island|Rockford|Rolling Meadows|Romeoville|Roselle|Round Lake Beach|Sauk Village|Schaumburg|Schiller Park|Skokie|South Elgin|South Holland|Springfield|Sterling|Streamwood|Streator|St. Charles|Summit|Swansea|Sycamore|Taylorville|Tinley Park|Urbana|Vernon Hills|Villa Park|Warrenville|Washington|Waukegan|West Chicago|Westchester|Western Springs|Westmont|Wheaton|Wheeling|Wilmette|Winnetka|Wood Dale|Wood River|Woodfield-Schaumburg|Woodridge|Woodstock|Worth|Zion|Pingree Grove|Volo|Huntley|Glencoe|Woodridge");
		}
		if (state.contains("Indiana") || state.contains("IN")) {
			city = Util.match(addSec,
					"Schereville|Anderson|Auburn|Bedford|Beech Grove|Birdseye|Bloomington|Brownsburg|Carmel|Chesterton|Clarksville|Columbus|Connersville|Crawfordsville|Crown Point|Dyer|Earl Park|East Chicago|Elizabethtown|Elkhart|Evansville|Fishers|Fort Wayne|Fowler|Frankfort|Franklin|Gary|Goshen|Granger|Greenfield|Greensburg|Greenwood|Griffith|Hammond|Highland|Hobart|Huntington|Indianapolis|Jasper|Jeffersonville|Kokomo|La Porte|Lafayette|Lake Station|Lawrence|Lebanon|Logansport|Lowell|Madison|Marion|Martinsville|Merrillville|Michigan City|Mishawaka|Muncie|Munster|New Albany|New Castle|New Haven|Newburgh|Noblesville|Peru|Plainfield|Portage|Portland|Princeton|Richmond|Schererville|Seymour|Shelbyville|South Bend|Speedway|Sullivan|Terre Haute|Valparaiso|Vincennes|Wabash|Warsaw|Washington|West Lafayette");
		}
		if (state.contains("Iowa") || state.contains("IA")) {
			city = Util.match(addSec,
					"Alden|Altoona|Amana|Ames|Ankeny|Bettendorf|Boone|Burlington|Carroll|Cedar Falls|Cedar Rapids|Charles City|Churdan|Clinton|Clive|Collins|Coralville|Council Bluffs|Davenport|Davis City|Des Moines|Dubuque|Fort Dodge|Fort Madison|Grinnell|Indianola|Iowa City|Keokuk|Logan|Manning|Marion|Marshalltown|Mason City|Mingo|Montezuma|Muscatine|New Hampton|Newton|Ocheyedan|Oskaloosa|Ottumwa|Red Oak|Sheldon|Shenandoah|Shenandoah|Sioux City|Spencer|Storm Lake|Thurman|Underwood|Union|Urbandale|Waterloo|West Des Moines");
		}
		if (state.contains("Kansas") || state.contains("KS")) {
			city = Util.match(addSec,
					"Alma|Arkansas City|Atchison|Beloit|Caldwell|Coffeyville|De Soto|Derby|Dodge City|El Dorado|Emporia|Enterprise|Garden City|Gardner|Great Bend|Hays|Hiawatha|Hutchinson|Junction City|Kansas City|Kingman|Lawrence|Leavenworth|Leawood|Lenexa|Liberal|Madison|Manhattan|Marysville|McPherson|Merriam|Newton|Olathe|Ottawa|Overland Park|Parsons|Phillipsburg|Pittsburg|Prairie Village|Salina|Shawnee|Topeka|Westmoreland|Wichita|Winfield");
		}
		if (state.contains("Kentucky") || state.contains("KY")) {
			city = Util.match(addSec,
					"Ashland|Barbourville|Bardstown|Berea|Bowling Green|Burlington|Burnside|Campbellsville|Carrollton|Covington|Danville|Elizabethtown|Erlanger|Fern Creek|Florence|Fort Campbell North|Fort Knox|Fort Thomas|Frankfort|Georgetown|Glasgow|Harlan|Henderson|Highview|Hopkinsville|Independence|Jeffersontown|La Grange|Lancaster|Lexington|London|Louisa|Louisville|Madisonville|Mayfield|Middlesborough|Murray|Newburg|Newport|Nicholasville|Okolona|Owensboro|Paducah|Pikeville|Pleasure Ridge Park|Radcliff|Richmond|Shelbyville|Shively|Somerset|Stanton|St. Matthews|Valley Station|Winchester");
		}
		if (state.contains("Louisiana") || state.contains("LA")) {
			city = Util.match(addSec,
					"Rayne|Abbeville|Alexandria|Angie|Baker|Bastrop|Baton Rouge|Bayou Cane|Bogalusa|Bossier City|Boyce|Chalmette|Convent|Covington|Crowley|Denham Springs|Destrehan|Estelle|Eunice|Fort Polk South|Gonzales|Gretna|Hammond|Harvey|Houma|Jefferson|Jennings|Kenner|La Place|Lafayette|Lake Charles|Leesville|Logansport|Luling|Mandeville|Marrero|Meraux|Merrydale|Metairie|Minden|Monroe|Morgan City|Moss Bluff|Natchitoches|New Iberia|New Orleans|Opelousas|Pineville|Plaquemine|Raceland|Rayville|Reserve|River Ridge|Ruston|Shreveport|Slidell|Sulphur|Tallulah|Terrytown|Thibodaux|Timberlane|West Monroe|Westwego|Woodmere|Zachary|From Baton Rouge|Addis|Prairieville|Watson|Madisonville|Ponchatoula|Robert|Duson|Carencro|Youngsville");
		}
		if (state.contains("Maine") || state.contains("ME")) {
			city = Util.match(addSec,
					"Ashland|Auburn|Augusta|Bangor|Biddeford|Brunswick|Castine|Danforth|Falmouth|Freeport|Gorham|Greenville|Guilford|Jay|Kennebunk|Lewiston|Limestone|Machias|Orono|Portland|Princeton|Saco|Sanford|Scarborough|South Portland|Southwest Harbor|Stonington|Surry|Waterville|Westbrook|Windham|York");
		}
		if (state.contains("Maryland") || state.contains("MD")) {
			city = Util.match(addSec,
					"Accokeek|Edgewater|White Plains|Ijamsville|Marriottsville|Sykesville|Clarksburg|Laytonsville|Clarksville|Frankford|Millersville|Aberdeen Proving Ground|Aberdeen|Adelphi|Annapolis|Arbutus|Arnold|Aspen Hill|Ballenger Creek|Baltimore|Bel Air North|Bel Air South|Bel Air|Beltsville|Bethesda|Bowie|Brooklyn Park|Calverton|Cambridge|Camp Springs|Carney|Catonsville|Chesapeake Ranch Estates-Drum Point|Chillum|Church Hill|Clinton|Cockeysville|Colesville|College Park|Columbia|Coral Hills|Crofton|Cumberland|Damascus|Dundalk|East Riverdale|Easton|Edgewood|Eldersburg|Elkridge|Elkton|Ellicott City|Essex|Fairland|Ferndale|Forestville|Fort Washington|Frederick|Fulton|Friendly|Gaithersburg|Germantown|Glen Burnie|Glenn Dale|Greater Landover|Greater Upper Marlboro|Green Haven|Green Valley|Greenbelt|Hagerstown|Halfway|Havre de Grace|Hillcrest Heights|Hyattsville|Joppatowne|Kettering|Lake Shore|Langley Park|Lanham-Seabrook|Lansdowne-Baltimore Highlands|Laurel|Lexington Park|Linganore-Bartonsville|Lochearn|Lutherville-Timonium|Manchester|Mays Chapel|Middle River|Milford Mill|Montgomery Village|New Carrollton|North Bethesda|North Laurel|North Potomac|Ocean Pines|Odenton|Olney|Overlea|Owings Mills|Oxon Hill-Glassmanor|Parkville|Parole|Pasadena|Perry Hall|Pikesville|Potomac|Randallstown|Redland|Reisterstown|Ridgely|Riviera Beach|Rockville|Rosaryville|Rosedale|Rossville|Salisbury|Savage-Guilford|Severna Park|Severn|Silver Spring|South Gate|South Laurel|St. Charles|Suitland-Silver Hill|Takoma Park|Towson|Upper Marlboro|Waldorf|Walker Mill|Westernport|Westminster|Wheaton-Glenmont|White Oak|Woodlawn|Woodstock|New Market|Brandywine|Glenarden|Mitchellville|Cooksville|Hanover|Marriottsville");

		}
		if (state.contains("Massachusetts") || state.contains("MA")) {
			city = Util.match(addSec,
					"Abington|Acton|Acushnet|Agawam|Amesbury|Amherst Center|Amherst|Andover|Arlington|Ashburnham|Ashland|Athol|Attleboro|Auburn|Ayer|Barnstable Town|Bedford|Belchertown|Bellingham|Belmont|Beverly|Billerica|Boston|Bourne|Braintree|Brewster|Bridgewater|Brockton|Brookline|Burlington|Buzzards Bay|Cambridge|Canton|Carver|Centerville|Charlemont|Charlton|Chelmsford|Chelsea|Chicopee|Clinton|Concord|Dalton|Danvers|Dartmouth|Dedham|Dennis|Dracut|Dudley|Duxbury|East Bridgewater|East Longmeadow|Easthampton|Easton|Everett|Fairhaven|Fall River|Falmouth|Fitchburg|Foxborough|Framingham|Franklin|Gardner|Gloucester|Grafton|Greenfield|Groton|Hanover|Harwich|Haverhill|Hingham|Holbrook|Holden|Holliston|Holyoke|Hopkinton|Hudson|Hull|Ipswich|John Fitzgerald Kennedy|Kingston|Lawrence|Lee|Leicester|Leominster|Lexington|Longmeadow|Lowell|Ludlow|Lynnfield|Lynn|Malden|Mansfield|Marblehead|Marlborough|Marshfield|Mashpee|Maynard|Medfield|Medford|Medway|Melrose|Methuen|Middlesex Essex Gmf|Milford|Millbury|Milton|Nantucket|Natick|Needham|New Bedford|Newburyport|Newtonville|Newton|Norfolk|North Adams|North Andover|North Attleborough Center|North Dighton|North Easton|North Falmouth|North Reading|Northampton|Northborough|Northbridge|Northfield|Norton|Norwood|Orange|Oxford|Palmer|Peabody|Pembroke|Pepperell|Pittsfield|Plymouth|Quincy|Randolph|Raynham|Reading|Rehoboth|Revere|Rockland|Salem|Sandwich|Saugus|Scituate|Seekonk|Sharon|Shrewsbury|Somerset|Somerville|South Egremont|South Hadley|South Yarmouth|Southborough|Southbridge|Spencer|Springfield|Stockbridge|Stoneham|Stoughton|Sudbury|Swampscott|Swansea|Taunton|Tewksbury|Turners Falls|Tyngsborough|Uxbridge|Vineyard Haven|Wakefield|Walpole|Waltham|Wareham|Warren|Watertown|Wayland|Webster|Wellesley|West Springfield|West Upton|Westborough|Westfield|Westford|Weston|Westport|Westwood|Weymouth|Whitman|Wilbraham|Wilmington|Winchester|Winthrop|Woburn|Worcester|Wrentham|Yarmouth");
		}
		if (state.contains("Michigan") || state.contains("MI")) {
			city = Util.match(addSec,
					"Ada|Adrian|Algonac|Allen Park|Allendale|Alma|Alpena|Alpine|Ann Arbor|Antwerp|Attica|Auburn Hills|Bangor|Battle Creek|Bay City|Bedford|Beecher|Benton Harbor|Benton|Berkley|Berrien Springs|Beverly Hills|Big Rapids|Birmingham|Blackman|Bloomfield Hills|Bloomfield Township|Bloomfield|Brandon|Bridgeport|Brighton|Brimley|Brownstown|Buena Vista|Burton|Byron|Cadillac|Cannon|Canton|Cascade|Charlevoix|Chesterfield|Clawson|Clinton|Coldwater|Commerce|Comstock Park|Comstock|Cutlerville|Davison|De Witt|Dearborn Heights|Dearborn|Delhi|Delta|Detroit|East Grand Rapids|East Lansing|East Tawas|Eastpointe|Eckerman|Ecorse|Emmett|Escanaba|Farmington Hills|Farmington|Fenton|Ferndale|Flint|Flushing|Forest Hills|Fort Gratiot|Frankenmuth|Fraser|Frenchtown|Fruitport|Gaines|Garden City|Garfield|Genesee|Genoa|Georgetown|Grand Blanc|Grand Haven|Grand Rapids|Grandville|Grayling|Green Oak|Grosse Ile|Grosse Pointe Park|Grosse Pointe Woods|Grosse Pointe|Hamburg|Hamtramck|Harbor Springs|Harper Woods|Harrison|Hartland|Haslett|Hazel Park|Highland Park|Highland|Holland|Holly|Holt|Houghton|Howell|Huron|Independence|Inkster|Ionia|Jackson|Jenison|Kalamazoo|Kentwood|Kincheloe|Lansing|Leoni|Lincoln Park|Lincoln|Livonia|Lyon|Macomb|Madison Heights|Marquette|Marshall|Melvindale|Meridian|Midland|Milford|Minden City|Monitor|Monroe|Mount Clemens|Mount Morris|Mount Pleasant|Mundy|Muskegon Heights|Muskegon|Niles|Northview|Northville|Norton Shores|Novi|Oak Park|Oakland|Okemos|Orion|Oscoda|Oshtemo|Owosso|Oxford|Park|Pittsfield|Plainfield|Plymouth Township|Plymouth|Pontiac|Port Huron|Portage|Redford|Rhodes|Riverview|Rochester Hills|Rochester|Rockford|Romulus|Roseville|Royal Oak|Saginaw Township North|Saginaw Township South|Saginaw|Sault Ste. Marie|Scio|Shelby|South Lyon|Southfield|Southgate|Spring Lake|Springfield|Sterling Heights|Sturgis|St. Clair Shores|St. Joseph|Summit|Sumpter|Superior|Taylor|Texas|Thomas|Traverse City|Trenton|Troy|Utica|Van Buren|Vassar|Vienna|Walker|Warren|Washington|Waterford|Waverly|Wayne|West Bloomfield Township|West Bloomfield|Westland|White Lake|Whitehall|Wixom|Woodhaven|Wyandotte|Wyoming|Ypsilanti");
		}
		if (state.contains("Minnesota") || state.contains("MN")) {
			city = Util.match(addSec,
					"Dundas|Albert Lea|Andover|Anoka|Apple Valley|Austin|Bemidji|Blaine|Bloomington|Brainerd|Brook Park|Brooklyn Center|Brooklyn Park|Buffalo|Burnsville|Center City|Champlin|Chanhassen|Chaska|Cloquet|Columbia Heights|Cook|Coon Rapids|Cottage Grove|Cotton|Crystal|Deer River|Detroit Lakes|Duluth|Eagan|East Bethel|Eden Prairie|Edina|Elk River|Fairmont|Faribault|Farmington|Fergus Falls|Fridley|Golden Valley|Graceville|Grand Rapids|Grasston|Ham Lake|Hastings|Hibbing|Hill City|Hopkins|Howard Lake|Hugo|Hutchinson|Inver Grove Heights|Kerrick|Lakeville|Lino Lakes|Long Prairie|Mankato|Maple Grove|Maple Plain|Maplewood|Marshall|Meadowlands|Mendota Heights|Minneapolis|Minnetonka|Monticello|Moorhead|Motley|Mounds View|New Brighton|New Hope|New Ulm|North Mankato|North St. Paul|Northfield|Norwood|Oakdale|Orr|Owatonna|Plymouth|Prior Lake|Ramsey|Red Wing|Richfield|Robbinsdale|Rochester|Roosevelt|Rosemount|Roseville|Rush City|Sauk Centre|Sauk Rapids|Savage|Sebeka|Shakopee|Shevlin|Shoreview|Silver Bay|Solway|South St. Paul|Stillwater|St. Cloud|St. Louis Park|St. Paul|Vadnais Heights|Virginia|West St. Paul|White Bear Lake|White Bear|Willmar|Winona|Woodbury|Worthington|Wrenshall|Young America|Otsego|St. Michael|Minnetrista");
		}
		if (state.contains("Mississippi") || state.contains("MS")) {
			city = Util.match(addSec,
					"Bay Saint Louis|Biloxi|Brandon|Canton|Clarksdale|Cleveland|Clinton|Columbus|Corinth|Drew|Flora|Gautier|Greenville|Greenwood|Grenada|Gulfport|Hattiesburg|Hermanville|Horn Lake|Indianola|Jackson|Laurel|Long Beach|Madison|Magee|McComb|Meridian|Moss Point|Natchez|Ocean Springs|Olive Branch|Oxford|Pascagoula|Pearl|Picayune|Ridgeland|Rolling Fork|Southaven|Starkville|Tupelo|Vicksburg|Walls|West Point|Wiggins|Woodville|Yazoo City|D'iberville|Pass Christian");
		}
		if (state.contains("Missouri") || state.contains("MO")) {
			city = Util.match(addSec,
					"Moberly|Affton|Arnold|Ballwin|Bellefontaine Neighbors|Belton|Berkeley|Blue Springs|Bridgeton|Butler|Cape Girardeau|Carthage|Chesterfield|Chillicothe|Clayton|Columbia|Concord|Craig|Crestwood|Creve Coeur|Excelsior Springs|Farmington|Fenton|Ferguson|Florissant|Forest City|Fort Leonard Wood|Fulton|Gladstone|Grandview|Grant City|Hannibal|Hazelwood|Independence|Jackson|Jefferson City|Jennings|Joplin|Kansas City|Kennett|Kirksville|Kirkwood|Lake St. Louis|Lamar|Lebanon|Lee's Summit|Lemay|Liberty|Manchester|Marshall|Maryland Heights|Maryville|Maysville|Mehlville|Mexico|Neosho|Newtown|Nixa|Norborne|O' Fallon|Oakville|Osceola|Overland|Poplar Bluff|Raymore|Raytown|Rock Port|Rolla|Sedalia|Sikeston|Spanish Lake|Springfield|St. Ann|St. Charles|St. Joseph|St. Louis|St. Peters|Town and Country|Trenton|University City|Warrensburg|Washington|Webster Groves|Wentzville|West Plains|Wildwood");
		}
		if (state.contains("Montana") || state.contains("MT")) {
			city = Util.match(addSec,
					"Absarokee|Arlee|Ashland|Billings|Bonner|Bozeman|Broadus|Butte|Columbia Falls|Cooke City|Cut Bank|Deer Lodge|Dillon|East Helena|Fallon|Frenchtown|Glasgow|Great Falls|Hamilton|Hardin|Helena|Kalispell|Laurel|Lewistown|Miles City|Missoula|Red Lodge|Roberts|Roundup|Scobey|Toston|West Glacier|Winnett");
		}
		if (state.contains("Nebraska") || state.contains("NE")) {
			city = Util.match(addSec,
					"Bassett|Beatrice|Bellevue|Blair|Bruno|Chalco|Columbus|Fremont|Grand Island|Hastings|Holdrege|Kearney|La Vista|Lexington|Lincoln|Lyman|Norfolk|North Bend|North Platte|Omaha|Papillion|Scottsbluff|Sidney|South Sioux City|Valentine|Valley");
		}
		if (state.contains("Nevada") || state.contains("NV")) {
			city = Util.match(addSec,
					"Crystal Bay|Carson City|Henderson|Indian Springs|Jean|Pahrump|Boulder City|Elko|Enterprise|Gardnerville Ranchos|Hawthorne|Las Vegas|North Las Vegas|Reno|Searchlight|Sparks|Spring Creek|Spring Valley|Sun Valley|Sunrise Manor|Whitney|Winchester|Zephyr Cove|Alamo|Amargosa Valley|Ash Springs|Austin|Baker|Battle Mountain|Beatty|Beowawe|Blue Diamond|Boulder City|Bunkerville|Cal-Nev-Ari|Caliente|Carlin|Carson City|Cold Springs|Crystal|Crystal Bay|Dayton|Denio|Duckwater|Dyer|East|Ely|Elko|Empire|Enterprise|Eureka|Fallon|Fernley|Gabbs|Gardnervillle|Gerlach|Golden Valley|Goldfield|Goodsprings|Hawthorne|Henderson|Imlay|Incline Village|Indian Hills|Indian Springs|Jackpot|Jarbidge|Jean|Jiggs|Johnson Lane|Kingsbury|Las Vegas|Laughlin|Lemmon Valley|Logandale|Lovelock|Lund|McDermitt|McGill|Mesquite|Minden|Moapa Town|Moapa Valley|Montello|Mount Charleston|Nixon|North Las Vegas|Orovada|Overton|Owyhee|Pahrump|Panaca|Paradise|Pioche|Rachel|Reno|Round Hill Village|Round Mountain|Sandy Valley|Schurz|Searchlight|Silver Park|Silver Springs|Sloan|Smith|Spanish Springs|Sparks|Spring Creek|Spring Valley|Stateline|Summerlin South|Sun Valley|Sunrise Manor|Sutcliffe|Tonopah|Tuscarora|Verdi|Virginia City|Wadsworth|Wells|West Wendover|Winnemucca|Whitney|Winchester|Yerington|Zephyr Cove");
		}
		if (state.contains("New Hampshire") || state.contains("NH")) {
			city = Util.match(addSec,
					"Amherst|Bedford|Berlin|Claremont|Concord|Conway|Cornish Flat|Derry|Dover|Durham|Exeter|Goffstown|Hampton|Hanover|Hooksett|Hudson|Keene|Laconia|Lebanon|Londonderry|Madison|Manchester|Merrimack|Milford|Nashua|Pelham|Peterborough|Portsmouth|Rochester|Salem|Somersworth|Twin Mountain|Walpole|Windham|Wolfeboro");
		}
		if (state.contains("New Jersey") || state.contains("NJ")) {
			city = Util.match(addSec,
					"Manahawkin|Monroe Township|Evesham Township|Mount Olive|Aberdeen|Allenhurst|Asbury Park|Atlantic City|Avenel|Barclay-Kingston|Barnegat|Basking Ridge|Bayonne|Beachwood|Belleville|Bellmawr|Belmar|Bergenfield|Berkeley Heights|Berkeley|Bernards Township|Bloomfield|Bound Brook|Branchburg|Branchville|Brick|Bridgeton|Bridgewater|Brigantine|Browns Mills|Burlington|Caldwell|Camden|Carteret|Cedar Grove|Chatham|Cherry Hill Mall|Cherry Hill|Cinnaminson|City of Orange|Clark|Cliffside Park|Clifton|Clinton|Collingswood|Colonia|Colts Neck|Cranbury|Cranford|Delran|Denville|Deptford|Dover|Dumont|East Brunswick|East Hanover|East Orange|East Windsor|Eatontown|Echelon|Edison|Egg Harbor Township|Egg Harbor|Elizabeth|Elmwood Park|Englewood|Evesham|Ewing|Fair Lawn|Fairview|Florence|Fords|Fort Lee|Franklin Lakes|Franklin|Freehold|Galloway|Garfield|Glassboro|Glen Rock|Gloucester City|Gloucester|Greentree|Guttenberg|Hackensack|Hackettstown|Haddonfield|Haddon|Haledon|Hamilton|Hammonton|Hanover|Harrison|Hasbrouck Heights|Hawthorne|Hazlet|Highland Park|Hillsborough|Hillsdale|Hillside|Hoboken|Holiday City-Berkeley|Holmdel|Hopatcong|Hopewell|Howell|Irvington|Iselin|Jackson|Jefferson|Jersey City|Keansburg|Kearny|Kenilworth|Kirkwood Voorhees|Lacey|Lakehurst|Lakewood|Lawrence|Leisure Village West-Pine Lake Park|Lincoln Park|Lindenwold|Linden|Linwood|Little Egg Harbor|Little Falls|Little Ferry|Livingston|Lodi|Long Branch|Lower|Lumberton|Lyndhurst|Madison|Mahwah|Manalapan|Manchester|Mantua|Manville|Maple Shade|Maplewood|Marlboro|Marlton|Medford|Mercerville-Hamilton Square|Metuchen|Middlesex|Middletown|Middle|Millburn|Millville|Monroe|Montclair|Montgomery|Montville|Moorestown-Lenola|Moorestown|Morganville|Morristown|Morris|Mount Holly|Mount Laurel|Neptune|New Brunswick|New Milford|New Providence|Newark|North Arlington|North Bergen|North Brunswick Township|North Brunswick|North Plainfield|Nutley|Oakland|Ocean Acres|Ocean City|Ocean|Old Bridge|Orange|Palisades Park|Paramus|Parsippany-Troy Hills|Passaic|Paterson|Pemberton|Pennsauken|Pennsville|Pequannock|Perth Amboy|Phillipsburg|Pine Hill|Piscataway|Plainfield|Plainsboro|Pleasantville|Point Pleasant|Pompton Lakes|Princeton Meadows|Princeton|Rahway|Ramsey|Randolph|Raritan|Readington|Red Bank|Ridgefield Park|Ridgefield|Ridgewood|Ringwood|River Edge|Riverside|Rockaway|Roselle Park|Roselle|Roxbury|Rutherford|Saddle Brook|Sayreville|Scotch Plains|Secaucus|Somers Point|Somerset|Somerville|South Amboy|South Brunswick|South Orange Village|South Orange|South Plainfield|South River|Southampton|Sparta|Springdale|Springfield|Stafford|Succasunna-Kenvil|Summit|Teaneck|Tenafly|Tinton Falls|Toms River|Totowa|Trenton|Union City|Union|Upper|Ventnor City|Vernon|Verona|Vineland|Voorhees|Wallington|Wall|Wanaque|Wantage|Warren|Washington|Waterford|Wayne|Weehawken|West Caldwell|West Deptford|West Freehold|West Milford|West New York|West Orange|West Paterson|West Windsor|Westfield|Westwood|Williamstown|Willingboro|Winslow|Woodbridge|Woodbury|Wyckoff|Egg Harbor Township|Manchester Township");
		}
		if (state.contains("New Mexico") || state.contains("NM")) {
			city = Util.match(addSec,
					"Deming|Alamogordo|Albuquerque|Anthony|Artesia|Bernalillo|Carlsbad|Cloudcroft|Clovis|Cuba|Eagle Nest|Espanola|Estancia|Farmington|Gallup|Grants|Hobbs|La Mesa|Las Cruces|Las Vegas|Lordsburg|Los Alamos|Los Lunas|Moriarty|North Valley|Penasco|Peralta|Portales|Rio Rancho|Roswell|Santa Fe|Santa Rosa|Silver City|South Valley|Springer|Sunland Park|Tucumcari|Wagon Mound|Watrous");
		}
		if (state.contains("New York") || state.contains("NY")) {
			city = Util.match(addSec,
					"Medford|Clay|Albany|Alden|Amherst|Amsterdam|Antwerp|Arcadia|Arlington|Auburn|Aurora|Babylon|Baldwin|Batavia|Bath|Bay Shore|Beacon|Bedford|Beekman|Bellmore|Bethlehem|Bethpage|Binghamton|Blooming Grove|Brentwood|Brighton|Bronx|Brookhaven|Brooklyn|Brunswick|Buffalo|Camillus|Canandaigua|Canton|Carmel|Carthage|Catskill|Centereach|Central Islip|Cheektowaga|Chenango|Chester|Chili|Cicero|Clarence|Clarkstown|Clifton Park|Cohoes|Colonie|Commack|Copiague|Coram|Corning|Cornwall|Cortlandt|Cortland|Coxsackie|Croton-On-Hudson|De Witt|Deer Park|Depew|Dix Hills|Dobbs Ferry|Dryden|Dunkirk|East Fishkill|East Greenbush|East Hampton|East Islip|East Massapequa|East Meadow|East Northport|East Patchogue|East Rockaway|Eastchester|Elma|Elmira|Elmont|Elwood|Endicott|Endwell|Evans|Fairmount|Fallsburg|Far Rockaway|Farmingdale|Farmington|Farmingville|Fishkill|Floral Park|Flushing|Fort Drum|Franklin Square|Fredonia|Freeport|Fultonville|Fulton|Garden City|Gates-North Gates|Gates|Geddes|Geneva|Georgetown|German Flatts|Glen Cove|Glens Falls|Glenville|Gloversville|Goshen|Grand Island|Great Neck|Greece|Greenburgh|Greenlawn|Groveland|Guilderland|Halfmoon|Hamburg|Hampton Bays|Harrison|Hauppauge|Haverstraw|Hempstead|Henrietta|Hicksville|Highlands|Holbrook|Holtsville|Horseheads|Huntington Station|Huntington|Hyde Park|Irondequoit|Islip|Ithaca|Jamaica|Jamestown|Jefferson Valley-Yorktown|Jericho|Johnson City|Keeseville|Kenmore|Kent|Kings Park|Kingsbury|Kingston|Kirkland|Kiryas Joel|La Grange|Lackawanna|Lake Grove|Lake Ronkonkoma|Lancaster|Lansing|Latham|Le Ray|Levittown|Lewisboro|Lewiston|Lindenhurst|Liverpool|Lockport|Long Beach|Long Island City|Lynbrook|Lyon Mountain|Lysander|Malone|Malta|Mamakating|Mamaroneck|Manhattan|Manlius|Manorville|Massapequa Park|Massapequa|Massena|Mastic Beach|Mastic|Melville|Merrick|Middletown|Miller Place|Milton|Mineola|Monroe|Monsey|Montgomery|Moreau|Mount Pleasant|Mount Vernon|Nanuet|Nesconset|New Cassel|New Castle|New City|New Hartford|New Hyde Park|New Paltz|New Rochelle|New Windsor|New York|Newburgh|Newcomb|Niagara Falls|Niskayuna|North Amityville|North Babylon|North Bangor|North Bay Shore|North Bellmore|North Castle|North Greenbush|North Hempstead|North Lawrence|North Lindenhurst|North Massapequa|North Merrick|North New Hyde Park|North Tonawanda|North Valley Stream|North Wantagh|Oceanside|Ogdensburg|Ogden|Olean|Oneida|Oneonta|Onondaga|Orangetown|Orchard Park|Ossining|Oswego|Owego|Oyster Bay|Parma|Patchogue|Patterson|Pearl River|Peekskill|Pelham|Penfield|Perinton|Pittsford|Plainview|Plattsburgh|Pleasantville|Pomfret|Port Chester|Port Washington|Potsdam|Poughkeepsie|Putnam Valley|Queensbury|Queens|Ramapo|Red Hook|Rexford|Ridge|Riverhead|Rochester|Rockville Centre|Rocky Point|Rodman|Rome|Ronkonkoma|Roosevelt|Rotterdam|Rye|Salina|Salisbury|Saranac Lake|Saratoga Springs|Saugerties|Sayville|Scarsdale|Schenectady|Schodack|Seaford|Selden|Setauket-East Setauket|Shawangunk|Shirley|Smithtown|Somers|South Farmingdale|Southampton|Southeast|Southold|Southport|Spring Valley|Staten Island|Stephentown|Stony Brook|Stony Point|St. James|Suffern|Sullivan|Sweden|Syosset|Syracuse|Tarrytown|Terryville|Thompson|Ticonderoga|Tonawanda|Troy|Ulster|Uniondale|Union|Utica|Valley Stream|Van Buren|Vestal|Wallkill|Wantagh|Wappinger|Warwick|Watertown|Watervliet|Wawarsing|Webster|West Babylon|West Haverstraw|West Hempstead|West Islip|West Nyack|West Point|West Seneca|Westbury|Westerlo|Wheatfield|White Plains|Whitestown|Wilton|Woodmere|Wyandanch|Yonkers|Yorktown");
		}
		if (state.contains("North Carolina") || state.contains("NC")) {
			city = Util.match(addSec,
					"Sunset Beach|Willow Springs|Stallings|Castle Hayne|Knightdale|Supply|Holden Beach|Holly Ridge|Bolivia|Wesley Chapel|Davie|Forsyth|Marvin|Court King|Clayton|Gibsonville|Oak Ridge|Stanly|Landis|Spencer|Summerfield|Winston- Salem|Glen Lane|Elon|Pineville|Morrisville|Troutman|Locust|McLeansville|Waxhaw|Sherrills Ford|Midland|Wando|Wendell|Pfafftown|Whitsett|Albemarle|Apex|Asheboro|Asheville|Banner Elk|Boone|Burlington|Carrboro|Cary|Chapel Hill|Charlotte|Clemmons|Concord|Cornelius|Creedmoor|Durham|Denver|Dallas|Eden|Elizabeth City|Elm City|Fayetteville|Fort Bragg|Garner|Gastonia|Goldsboro|Graham|Greensboro|Greenville|Havelock|Hendersonville|Henderson|Hertford|Hickory|High Point|Haw River|Hope Mills|Huntersville|Indian Trail|Jacksonville|Kannapolis|Kernersville|Kinston|Laurinburg|Lenoir|Lexington|Lincolnton|Lumberton|Mount Holly|Masonboro|Matthews|Mount HollyMint Hill|Monroe|Mooresville|Morganton|Mount Airy|New Bern|Newton|North Wilkesboro|Piney Green|Pittsboro|Raeford|Raleigh|Reidsville|Roanoke Rapids|Rocky Mount|Rural Hall|Salisbury|Sanford|Sealevel|Shallotte|Shelby|Smithfield|Southern Pines|Southport|Statesville|Tarboro|Thomasville|Wake Forest|Wilmington|Wilson|Winston-Salem|Carolina Shores|Davidson|Belmont|Bunnlevel|Spring Lake|Winston Salem|Swansboro|Sneads Ferry|Zebulon|Leland|Hampstead|Mint Hill|Ogden");
		}
		if (state.contains("North Dakota") || state.contains("ND")) {
			city = Util.match(addSec,
					"Bismarck|Dickinson|Fargo|Grand Forks|Gwinner|Jamestown|Mandan|Michigan|Minot|Wahpeton|West Fargo|Williston|Mint Hill");
		}
		if (state.contains("Ohio") || state.contains("OH")) {
			city = Util.match(addSec,
					"Akron|Alliance|Amesville|Amherst|Ashland|Ashtabula|Athens|Aurora|Austintown|Avon Lake|Avon|Barberton|Bay Village|Beachwood|Beavercreek|Bedford Heights|Bedford|Bellefontaine|Berea|Bexley|Blue Ash|Boardman|Bowling Green|Brecksville|Bridgetown North|Broadview Heights|Brook Park|Brooklyn|Brunswick|Bucyrus|Cambridge|Canton|Celina|Centerville|Chillicothe|Cincinnati|Circleville|Clayton|Cleveland Heights|Cleveland|Columbus|Conneaut|Continental|Coshocton|Cuyahoga Falls|Dayton|Defiance|Delaware|Dover|Dublin|East Cleveland|East Liverpool|Eastlake|Elyria|Englewood|Euclid|Fairborn|Fairfield|Fairview Park|Findlay|Finneytown|Forest Park|Forestville|Fostoria|Franklin|Fremont|Gahanna|Galion|Garfield Heights|Germantown|Girard|Greenfield|Greenville|Green|Grove City|Hamilton|Hilliard|Huber Heights|Hudson|Ironton|Kent|Kettering|Lakewood|Lancaster|Landen|Langsville|Laurelville|Lebanon|Leipsic|Lima|Lorain|Loveland|Lucasville|Lyndhurst|Mansfield|Maple Heights|Marietta|Marion|Marysville|Mason|Massillon|Maumee|Mayfield Heights|Medina|Mentor|Miamisburg|Middleburg Heights|Middletown|Montgomery|Mount Vernon|Nelsonville|New Philadelphia|Newark|Niles|North Canton|North College Hill|North Olmsted|North Ridgeville|North Royalton|Northbrook|Northfield|Norton|Norwalk|Norwood|Novelty|Oregon|Oxford|Painesville|Parma Heights|Parma|Pataskala|Perrysburg|Piqua|Portsmouth|Ravenna|Rawson|Reading|Reynoldsburg|Richmond Heights|Riverside|Rocky River|Rossburg|Salem|Sandusky|Seven Hills|Shaker Heights|Sharonville|Shiloh|Sidney|Solon|South Euclid|Springboro|Springdale|Springfield|Steubenville|Stow|Streetsboro|Strongsville|Struthers|Sylvania|Tallmadge|Tiffin|Tipp City|Toledo|Trotwood|Troy|Twinsburg|University Heights|Upper Arlington|Urbana|Van Wert|Vandalia|Vermilion|Wadsworth|Warrensville Heights|Warren|Washington|West Carrollton City|Westerville|Westlake|White Oak|Whitehall|Wickliffe|Willard|Willoughby|Willowick|Wilmington|Wooster|Worthington|Xenia|Youngstown|Zanesville");
		}
		if (state.contains("Oklahoma") || state.contains("OK")) {
			city = Util.match(addSec,
					"Piedmont|Ada|Altus|Ardmore|Atoka|Bartlesville|Beaver|Bethany|Bixby|Broken Arrow|Chickasha|Chouteau|Claremore|Del City|Duncan|Durant|Edmond|El Reno|Elk City|Enid|Guymon|Lawton|McAlester|Miami|Midwest City|Moore|Muskogee|Mustang|Norman|Nowata|Oklahoma City|Okmulgee|Owasso|Pawhuska|Ponca City|Pryor|Sand Springs|Sapulpa|Seminole|Shawnee|Stillwater|Tahlequah|The Village|Tulsa|Wagoner|Woodward|Yukon|Choctaw");
		}
		if (state.contains("Oregon") || state.contains("OR")) {
			city = Util.match(addSec,
					"Clackamas|Albany|Aloha|Altamont|Arlington|Ashland|Baker|Banks|Beaverton|Bend|Burns|Canby|Cannon Beach|Cave Junction|Cedar Mill|Central Point|Chiloquin|Cloverdale|Coos Bay|Corvallis|Cottage Grove|Dallas|Eugene|Forest Grove|Four Corners|Gladstone|Grants Pass|Gresham|Hayesville|Heppner|Hermiston|Hillsboro|Jefferson|Keizer|Klamath Falls|La Grande|Lake Oswego|Lebanon|Lincoln City|Lowell|Lyons|McMinnville|Medford|Mill City|Milwaukie|Newberg|Newport|Oak Grove|Oatfield|Ontario|Oregon City|Pendleton|Portland|Prairie City|Redmond|Roseburg|Salem|Sherwood|Siletz|Silver Lake|Springfield|St. Helens|The Dalles|Tigard|Tillamook|Troutdale|Tualatin|Waldport|West Linn|Wilsonville|Woodburn|Happy Valley|Scappoose|Lancaster|Newtown Square|Bensalem");
		}
		if (state.contains("Pennsylvania") || state.contains("PA")) {
			city = Util.match(addSec,
					"Newtown Square|Abington|Aliquippa|Allentown|Altoona|Antrim|Ardmore|Ashville|Aston|Back Mountain|Baldwin|Beaver Springs|Beech Creek|Bellefonte|Bensalem|Berwick|Bethel Park|Bethlehem|Bloomsburg|Blue Bell|Blue Ridge Summit|Boswell|Boyers|Brentwood|Bristol|Broomall|Buckingham|Butler|Caln|Camp Hill|Carlisle|Carnot-Moon|Center|Chambersburg|Cheltenham|Chester|Chestnuthill|Coal|Coatesville|Collegeville|Colonial Park|Columbia|Concordville|Conshohocken|Coolbaugh|Cornwall|Cranberry|Creekside|Cumru|Dallas|Danville|Darby|Derry|Dover|Downingtown|Doylestown|Drexel Hill|Dry Run|Dunmore|East Goshen|East Hempfield|East Lampeter|East Norriton|East Pennsboro|Easton|Easttown|Edinboro|Elizabethtown|Elizabeth|Emmaus|Ephrata|Erie|Exeter|Exton|Fairview|Falls|Ferguson|Fernway|Fort Washington|Franconia|Franklin Park|Frenchville|Fullerton|Genesee|Gettysburg|Greene|Greensburg|Guilford|Hampden|Hampton Township|Hampton|Hanover|Harborcreek|Harleysville|Harrisburg|Harrison Township|Harrison|Hatfield|Haverford|Hazleton|Hempfield|Hermitage|Hershey|Hilltown|Holmes|Hopewell|Horsham|Hustontown|Hyndman|Indiana|Irvine|Irvona|Jeannette|Jersey Shore|Johnstown|Karthaus|King of Prussia|Kingston|Kittanning|Lake Lynn|Lancaster|Langhorne|Lansdale|Lansdowne|Lebanon|Leesport|Levittown|Limerick|Logan|Lower Allen|Lower Burrell|Lower Gwynedd|Lower Macungie|Lower Makefield|Lower Merion|Lower Moreland|Lower Paxton|Lower Pottsgrove|Lower Providence|Lower Salford|Lower Southampton|Loyalsock|Manchester|Manheim|Manor,|Marple|McCandless Township|McCandless|McKeesport|Meadville|Media|Middle Smithfield|Middletown|Mill Hall|Millcreek|Montgomeryville|Montgomery|Moon|Morrisville|Mount Jewett|Mount Lebanon|Mount Pleasant|Mountain Top|Muhlenberg|Muncy Valley|Munhall|Nanticoke|Needmore|Nether Providence Township|Nether Providence|New Britain|New Castle|New Kensington|New Wilmington|Newberry|Newtown|Norristown|North Bend|North Fayette|North Huntingdon|North Lebanon|North Middleton|North Strabane|North Union|North Versailles|North Wales|North Whitehall|Northampton|Nuangola|Oil City|Olanta|Osceola Mills|Palmer|Patton|Penn Hills|Penn|Peters|Philadelphia|Phoenixville|Pittsburgh|Pittston|Plains|Plumstead|Plum|Plymouth|Pottstown|Pottsville|Punxsutawney|Quarryville|Radnor Township|Radnor|Reading|Richland|Ridley|Robinson Township|Robinson|Ronks|Ross Township|Ross|Rostraver|Salisbury|Sandy|Scott Township|Scott|Scranton|Sewickley|Shaler Township|Shaler|Shamokin|Sharon|Shiloh|Silver Spring|Souderton|South Fayette|South Middleton|South Park Township|South Park|South Union|South Whitehall|Southampton|Spring Garden|Spring Grove|Springettsbury|Springfield|Spring|State College|Stroud|St. Marys|Sunbury|Susquehanna|Swatara|Towamencin|Tredyffrin|Trout Run|Twin Rocks|Uniontown|Unity|Upper Allen|Upper Chichester|Upper Darby|Upper Dublin|Upper Gwynedd|Upper Macungie|Upper Merion|Upper Moreland|Upper Providence Township|Upper Providence|Upper Saucon|Upper Southampton|Upper St. Clair|Uwchlan|Valley Forge|Warminster|Warrendale|Warren|Warrington|Warwick|Washington|Wayne|Weigelstown|West Bradford|West Chester|West Deer|West Goshen|West Hempfield|West Lampeter|West Manchester|West Mifflin|West Milton|West Norriton|West Whiteland|Westfield|Westtown|Whitehall|Whitemarsh|White|Whitpain|Wilkes-Barre|Wilkinsburg|Williamsport|Willistown|Willow Grove|Windsor|Woodlyn|Yeadon|York");
		}
		if (state.contains("Rhode Island") || state.contains("RI")) {
			city = Util.match(addSec,
					"Barrington|Bristol|Burrillville|Central Falls|Coventry|Cranston|Cumberland|East Greenwich|East Providence|Harrisville|Johnston|Lincoln|Middletown|Narragansett|Newport East|Newport|North Kingstown|North Providence|North Smithfield|Pawtucket|Portsmouth|Providence|Scituate|Smithfield|South Kingstown|Tiverton|Valley Falls|Wakefield|Warren|Warwick|West Warwick|Westerly|Woonsocket");
		}
		if (state.contains("South Carolina") || state.contains("SC")) {
			city = Util.match(addSec,
					"Pelzer|Johns Island|Inman|Woodruff|West Ashley|Huger|Simpsonville|Powdersville|Graniteville|Monks Corner|Pineville|Clover|Hopkins|Lyman|Boiling Springs|Wellford|Isle of Palms|Lancaster|Wando|Aiken|Anderson|Beaufort|Berea|Cayce|Charleston|Clemson|Columbia|Conway|Darlington|Dentsville|Easley|Florence|Forest Acres|Gaffney|Gantt|Georgetown|Goose Creek|Greenville|Greenwood|Greer|Hanahan|Hilton Head|Hilton Head Island|Hodges|Honea Path|Irmo|Ladson|Mauldin|Mount Pleasant|Moore|Myrtle Beach|Newberry|North Augusta|North Charleston|North Myrtle Beach|Orangeburg|Parker|Red Hill|Ridgeland|Rock Hill|Seneca|Seven Oaks|Simpsonville|Socastee|Spartanburg|St. Andrews|Summerville|Sumter|Taylors|Wade Hampton|West Columbia|Lake Wylie|Fort Mill|Indian Land|Moncks Corner|Model Homemount Pleasant|James Island|Lexington|Blythewood|Gilbert|Chapin|Elgin|Piedmont|Fountain Inn|Williamston|Duncan|Belton|Ladys Island|Bluffton|Murrells Inlet|Longs|Little River|Surfside Beach|Pawleys Island|Tega Cay|York");
		}
		if (state.contains("South Dakota") || state.contains("SD")) {
			city = Util.match(addSec,
					"Aberdeen|Belle Fourche|Beresford|Box Elder|Brandon|Brookings|Canton|Chamberlain|Dell Rapids|Hot Springs|Huron|Lead|Lennox|Madison|Milbank|Mitchell|North Sioux City|Pierre|Rapid City|Redfield|Sioux Falls|Sisseton|Spearfish|Sturgis|Vermillion|Watertown|Winner|Yankton");
		}
		if (state.contains("Tennessee") || state.contains("TN")) {
			city = Util.match(addSec,
					"Lenoir City|Pleasant View|Mascot|Corryton|Cleveland|Ardmore|Athens|Bartlett|Bloomingdale|Bolivar|Brentwood|Bristol|Brownsville|Chattanooga|Church Hill|Clarksville|Collierville|Columbia|Cookeville|Crossville|Dickson|Dyersburg|East Brainerd|East Ridge|Elgin|Elizabethton|Farragut|Franklin|Gallatin|Germantown|Goodlettsville|Greeneville|Hendersonville|Jackson|Johnson City|Kingsport|Knoxville|La Vergne|Lawrenceburg|Lebanon|Lewisburg|Madison|Martin|Maryville|McMinnville|Memphis|Middle Valley|Millington|Morristown|Mount Juliet|Mountain City|Murfreesboro|Nashville|Oak Ridge|Red Bank|Sevierville|Shelbyville|Smyrna|Sneedville|Soddy-Daisy|Springfield|Tullahoma|Union City|Cane Ridge|Spring Hill|Nolensville");
		}
		if (state.contains("Texas") || state.contains("TX")) {
			city = Util.match(addSec,
					"Pinehurst|Fulshear|Dripping Springs|Bee Cave|Aubrey|Red Oak|New Caney|Argyle|Hackberry|Rosharon|Haslet|Midlothian|Aledo|Lorena|Rosenburg|Spring,|Willis|Lavon|Luling|Ponder|Heath,|Nevada|Anna|Abilene|Addison|Alamo|Aldine|Alice|Allen|Alpine|Alvin|Amarillo|Anderson|Angleton|Arlington|Atascocita|Athens|Austin,|Balch Springs|Bay City|Baytown|Beaumont|Bedford|Beeville|Bellaire|Belton|Benbrook|Big Spring|Boerne|Borger|Brenham|Brownsville|Brownwood|Brushy Creek|Bryan|Burkburnett|Burleson|Canyon Lake|Carrollton|Castroville|Cedar Hill|Cedar Park|Channelview|Channing|Cinco Ranch|Cleburne|Cleveland|Cloverleaf|Clute|College Station|Colleyville|Columbus|Conroe|Converse|Coppell|Copperas Cove|Corinth|Corpus Christi|Corsicana|Cotulla|Cypress|Dallas|Deer Park|Del Rio|Denison|Denton|DeSoto|Dickinson|Donna|Dumas|Duncanville|Eagle Pass|Edinburg|El Campo|El Paso|Euless|Edgecliff Village|Farmers Branch|Flower Mound|Forest Hill|Fort Hood|Fort Worth|Freeport|Friendswood|Frisco|Gainesville|Galena Park|Galveston,|Garland|Gatesville|Georgetown|Gillett|Grand Prairie|Grapevine|Greenville|Groves|Haltom City|Harker Heights|Harlingen|Henderson|Hereford|Hewitt|Hickory Creek|Highland Village|Houston|Humble|Huntsville|Hurst|Irving|Jacinto City|Jacksonville|Jollyville|Katy,|Keller|Kenedy|Kerrville|Kilgore|Killeen|Kingsville|La Homa|La Marque|La Porte|Lake Jackson|Lancaster|Laredo|League City|Leander|Levelland|Lewisville|Lockhart|Longview|Lubbock|Lufkin|Mansfield|Marshall|McAllen|McKinney|Mercedes|Mesquite|Midland|Mineral Wells|Mission Bend|Mission|Missouri City|Mount Pleasant|Nacogdoches|Navasota|Nederland|New Braunfels|New Territory|North Richland Hills|Odessa|Orange|Palestine|Pampa|Paris|Pasadena|Pearland|Pecan Grove|Pflugerville|Pharr|Plainview|Plano|Port Arthur|Port Isabel|Port Lavaca|Port Neches|Portland|Presidio|Raymondville|Richardson|Richmond|Rio Grande City|Roanoke|Robstown|Rockwall|Rosenberg|Round Rock|Round Top|Rowlett|Saginaw|San Angelo|San Antonio|San Benito|San Elizario|San Marcos|Santa Fe|Schertz|Seagoville|Seguin|Sherman|Slaton|Snyder|Socorro|Somerville|South Houston|Southlake|Spring|Stafford|Stamford|Stephenville|Sudan|Sugar Land|Sulphur Springs|Sweetwater|Taylor|Temple|Terrell|Texarkana|Texas City|The Colony|The Woodlands|Three Rivers|Tomball|Tyler|Universal City|University Park|Uvalde|Vernon|Victoria|Vidor|Waco|Watauga|Waxahachie|Weatherford|Wells Branch|Weslaco|West Odessa|West University Place|White Settlement|Wichita Falls|Wilson|Wylie|Elgin|Manor,|Kyle|Briarcreek|Hutto|Bastrop|Sales Office For Info|Spicewood|Oak Point|Princeton|Little Elm|Josephine|Royse City|Van Alstyne|Crossroads|Melissa|Celina|Prosper|Forney|Nevada|Fate|Sachse|Lucas|Horizon City|Jarrell|Azle|Glenn Heights|Northlake|Magnolia|Manvel|Porter|Fresno|Iowa Colony|Montgomery|Cibolo|Troy|Farmersville|Winters");
		}
		if (state.contains("Utah") || state.contains("UT")) {
			city = Util.match(addSec,
					"Syracuse|Mapleton|Saratoga Springs|Altamont|American Fork|Blanding|Bountiful|Brigham City|Canyon Rim|Cedar City|Centerville|Clearfield|Clinton|Cottonwood Heights|Cottonwood West|Draper|East Millcreek|Farmington|Hatch|Holladay|Kamas|Kanab|Kaysville|Kearns|Layton|Lehi|Logan|Magna|Midvale|Millcreek|Monroe|Morgan|Murray|North Ogden|Ogden|Oquirrh|Orem|Park City|Payson|Pleasant Grove|Provo|Richfield|Riverton|Roy|Salt Lake City|Sandy|South Jordan|South Ogden|South Salt Lake|Spanish Fork|Springville|St. George|Taylorsville|Tooele|Vernal|West Jordan|West Valley City|Herriman|Bluffdale|South Weber|Highland|North Salt Lake|Saratoga,");
		}
		if (state.contains("Vermont") || state.contains("VT")) {
			city = Util.match(addSec,
					"Barton|Bennington|Brattleboro|Burlington|Canaan|Chester|Colchester|East Montpelier|Eden|Essex Junction|Essex|Fair Haven|Hartford|Middlebury|Montpelier|Morrisville|Poultney|Readsboro|Rutland|South Burlington|Townshend");
		}
		if (state.contains("Virginia") || state.contains("VA")) {
			city = Util.match(addSec,
					"Aldie|Alexandria|Annandale|Arlington|Bailey's Crossroads|Blacksburg|Bon Air|Bowling Green|Boydton|Bristol|Bull Run|Burke|Carrsville|Cave Spring|Centreville|Chantilly|Charlottesville|Chesapeake|Chester|Chincoteague|Christiansburg|Colonial Heights|Dale City|Danville|Dillwyn|East Highland Park|Fairfax|Falls Church|Farmville|Floyd|Stafford|Ford|Fort Hunt|Franconia|Fredericksburg|Front Royal|Glen Allen|Goochland|Groveton|Grundy|Hampton|Harrisonburg|Herndon|Highland Springs|Hollins|Hopewell|Hybla Valley|Idylwood|Jefferson|Keswick|King George|Lake Ridge|Lakeside|Laurel|Leesburg|Lincolnia|Lorton|Louisa|Lynchburg|Madison Heights|Madison|Manassas Park|Manassas|Martinsville|McLean|Meadowview|Mechanicsville|Merrifield|Middletown|Midlothian|Montclair|Montross|Mount Vernon|Newington|Newport News|Norfolk|Norton|Oak Hall|Oakton|Orange|Petersburg|Poquoson|Portsmouth|Purcellville|Quantico|Radford|Red Ash|Reston|Richmond|Roanoke|Rose Hill|Roseland|Salem|Saluda|Scottsville|Sperryville|Springfield|Staunton|Suffolk|Tazewell|Timberlake|Tuckahoe|Tysons Corner|Unionville|Vienna|Virginia Beach|Waynesboro|West Point|West Springfield|Williamsburg|Winchester|Wolf Trap|Woodbridge|Yorktown|Gainesville|Ashburn|Dumfries|Dulles|Oak Hill|Dunn Loring");
		}
		if (state.contains("Washington") || state.contains("WA")) {
			city = Util.match(addSec,
					"Cle Elum|Granite Falls|Bainbridge Island|Graham|Edgewood|Ridgefield|Battle Ground|Orting|Aberdeen|Alderwood Manor|Anacortes|Arlington|Auburn|Bainbridge Island|Bellevue|Bellingham|Bothell|Bow|Bremerton|Bryn Mawr-Skyway|Burien|Camano|Camas|Cascade-Fairwood|Centralia|Cottage Lake|Covington|Creston|Des Moines|Dishman|East Hill-Meridian|East Renton Highlands|East Wenatchee Bench|Edmonds|Elk Plain|Ellensburg|Enumclaw|Everett|Farmington|Federal Way|Five Corners|Fort Lewis|Hadlock|Harrington|Hunters|Inglewood-Finn Hill|Issaquah|Kelso|Kenmore|Kennewick|Kent|Kettle Falls|Kingsgate|Kirkland|Lacey|Lacrosse|Lake Forest Park|Lakebay|Lakeland North|Lakeland South|Lakewood|Lea Hill|Long Beach|Longview|Lynnwood|Maple Valley|Martha Lake|Marysville|Mercer Island|Mill Creek|Monroe|Moses Lake|Mount Vernon|Mountlake Terrace|Mukilteo|Naches|Nine Mile Falls|North Creek|North Marysville|Oak Harbor|Odessa|Olympia|Opportunity|Orchards|Othello|Paine Field-Lake Stickney|Parkland|Pasco|Picnic Point-North Lynnwood|Port Angeles|Port Orchard|Prairie Ridge|Pullman|Puyallup|Quincy|Redmond|Renton|Richland|Riverton-Boulevard Park|Rosalia|Salmon Creek|Sammamish|SeaTac|Seattle Hill-Silver Firs|Seattle,|Sequim|Shoreline|Silverdale|South Hill|Spanaway|Spokane|Sprague|Sunnyside|Tacoma|Tukwila|Tumwater|Union Hill-Novelty Hill|University Place|Valleyford|Vancouver|Vashon|Walla Walla|Wenatchee|West Lake Stevens|Lake Stevens|West Valley|White Center|Winthrop|Yakima|Woodinville|Bonney Lake|Snohomish");
		}
		if (state.contains("West Virginia") || state.contains("WV")) {
			city = Util.match(addSec,
					"Martinsburg|Berkeley|Aurora|Beckley|Bluefield|Burnsville|Charleston|Clarksburg|Cross Lanes|Fairmont|French Creek|Harpers Ferry|Huntington|Kingwood|Lewisburg|Martinsburg|Morgantown|Parkersburg|Paw Paw|Pineville|Princeton|Rivesville|Rock Cave|Slatyfork|South Charleston|St. Albans|Teays Valley|Vienna|Weirton|Wheeling|White Sulphur Springs");
		}
		if (state.contains("Wisconsin") || state.contains("WI")) {
			city = Util.match(addSec,
					"Allouez|Amery|Appleton|Ashwaubenon|Baraboo|Beaver Dam|Bellevue Town|Bellevue|Beloit|Bonduel|Brookfield|Brown Deer|Brownsville|Caledonia|Camp Douglas|Cedarburg|Chippewa Falls|Columbus|Cudahy|Dallas|De Pere|De Soto|Dodgeville|Eau Claire|Ellsworth|Ferryville|Fitchburg|Fond du Lac|Fort Atkinson|Franklin|Friendship|Germantown|Glendale|Glenwood City|Grafton|Grand Chute|Green Bay|Greendale|Greenfield|Hales Corners|Hartford|Howard|Iola|Janesville|Kaukauna|Kenosha|Krakow|La Crosse|Lena|Little Chute|Madison|Manitowoc|Marinette|Marshfield|Mason|Menasha|Menomonee Falls|Menomonie|Mequon|Merrill|Middleton|Milwaukee|Mondovi|Monroe|Mount Pleasant|Muskego|Neenah|New Berlin|New Holstein|Oak Creek|Oconomowoc|Onalaska|Oshkosh|Pewaukee|Pleasant Prairie|Plover|Port Washington|Racine|Randolph|Richfield|River Falls|Sheboygan|Shorewood|Somerset|South Milwaukee|Stevens Point|Stoughton|Sun Prairie|Superior|Two Rivers|Viola|Watertown|Waukesha|Waupun|Wausau|Wauwatosa|West Allis|West Bend|Weston|Whitefish Bay|Whitewater|Winter|Wisconsin Rapids");
		}
		if (state.contains("Wyoming") || state.contains("WY")) {
			city = Util.match(addSec,
					"Casper|Cheyenne|Cody|Evanston|Gillette|Green River|Laramie|Rock River|Rock Springs|Sheridan|Basin|Buffalo|Douglas|Kemmerer|Lander|Newcastle|Powell|Rawlins|Riverton|Torrington|Worland");
		}
		return city;
	}

//	static String commUrl = "";

/*	public static String getHardcodedAddress(String builderName, String comUrl) throws Exception {
//		commUrl = comUrl;
		String newFile = System.getProperty("user.home") + "/Harcoded Builders/" + builderName + ".csv";
		CsvListReader newFileReader = new CsvListReader(new FileReader(newFile), CsvPreference.STANDARD_PREFERENCE);
		List<String> newCsvRow = null;
		int count = 0;
		while ((newCsvRow = newFileReader.read()) != null) {

			if (count > 0) {
				// //U.log(newCsvRow.size());
				// builderName=newCsvRow.get(1);

				String aaa = getBuilderObj(newCsvRow);
				if (aaa != null)
					return aaa;
			}
			count++;
		}

		newFileReader.close();
		return null;
	}

	public static String getBuilderObj(List<String> newCsvRow) {
		if (commUrl.contains(newCsvRow.get(1).trim())) {
			return (newCsvRow.get(2) + "," + newCsvRow.get(3) + "," + newCsvRow.get(4) + "," + newCsvRow.get(5) + ","
					+ newCsvRow.get(6) + "," + newCsvRow.get(7) + "," + newCsvRow.get(8) + "," + newCsvRow.get(9) + ","
					+ newCsvRow.get(13)

			);
		}
		return null;

	}*/

/*	public static String sendComment(ArrayList<String> newCsvRow) throws InterruptedException {
		StringBuilder sb = new StringBuilder();
		// //U.log(newCsvRow.size());
		for (int i = 1; i < newCsvRow.size(); i++) {

			if (newCsvRow.get(i).trim().replace("-", "").length() < 1) {
				// //U.log(i+"==="+newCsvRow.get(i).trim());
				// Thread.sleep(4000);
				switch (i) {

				case 1:
					if (sb.length() > 3)
						sb.append(",Builder Name has Blank");
					else
						sb.append("Builder Name has Blank");
					break;

				case 2:
					if (sb.length() > 3)
						sb.append(",Builder URL has Blank");
					else
						sb.append("Builder URL has Blank");
					break;

				case 3:
					if (sb.length() > 3)
						sb.append(",Community Name has Blank");
					else
						sb.append("Community Name has Blank");
					break;

				case 4:
					if (sb.length() > 3)
						sb.append(",Community URL has Blank");
					else
						sb.append("Community URL has Blank");
					break;

				case 5:
					if (sb.length() > 3)
						sb.append(",Community Type has Blank");
					else
						sb.append("Community Type has Blank");
					break;

				case 6:
					if (sb.length() > 3)
						sb.append(",Address has Blank");
					else
						sb.append("address has Blank");
					break;

				case 7:
					if (sb.length() > 3)
						sb.append(",City has Blank");
					else
						sb.append("City has Blank");
					break;

				case 8:
					if (sb.length() > 3)
						sb.append(",State has Blank");
					else
						sb.append("State has Blank");
					break;

				case 9:
					if (sb.length() > 3)
						sb.append(",Zip has Blank");
					else
						sb.append("zip has Blank");
					break;

				case 10:
					if (sb.length() > 3)
						sb.append(",Lattitude and longitude has Blank");
					else
						sb.append("Lattitude and longitude has Blank");
					break;

				case 12:
					if (sb.length() > 3)
						sb.append(",Geo code has Blank");
					else
						sb.append("Geo code has Blank");
					break;

				case 13:
					if (sb.length() > 3)
						sb.append(",Property type has Blank");
					else
						sb.append("Property type has Blank");
					break;

				case 14:
					if (sb.length() > 3)
						sb.append(",Derived property type has Blank");
					else
						sb.append("Derived property type has Blank");
					break;

				case 15:
					if (sb.length() > 3)
						sb.append(",Property Status has Blank");
					else
						sb.append("Property Status has Blank");
					break;

				case 16:
					if (sb.length() > 3)
						sb.append(",Min Price value has Blank");
					else
						sb.append("Min Price value has Blank");
					break;

				case 17:
					if (sb.length() > 3)
						sb.append(",Max Price value has Blank");
					else
						sb.append("Max Price value has Blank");
					break;

				case 18:
					if (sb.length() > 3)
						sb.append(",Min sqft value has Blank");
					else
						sb.append("Min sqft value has Blank");
					break;

				case 19:
					if (sb.length() > 3)
						sb.append(",Max sqft value has Blank");
					else
						sb.append("Max sqft value has Blank");
					break;

				default:
					break;
				}
			}
		}
		return sb.toString();
	}*/

/*	public static boolean isvalideLatLng(String add[], String lat, String lng) throws Exception {

		// ChromeDriver driver2;
		if (lat != "" || lat == null) {
			String link = "https://maps.google.com/maps?q=" + lat + "," + lng;
			String htm = U.getHTML(link);
			String gAdd[] = U.getGoogleAddress(htm);
			//U.log(gAdd[0] + " " + gAdd[1] + " " + gAdd[2] + " " + gAdd[3]);
			if (gAdd[3].equals(add[3]))
				return true;
			else if (gAdd[1].equalsIgnoreCase(add[1]) && gAdd[2].equalsIgnoreCase(add[2])) {
				return true;
			}

			else {
				// http://maps.yahoo.com/place/?lat=37.71915&lon=-121.845803&q=37.71915%2C-121.845803
				link = "http://maps.yahoo.com/place/?lat=" + lat + "&lon=" + lng + "&q=" + lat + "," + lng;

				ShatamChrome driver1 = ShatamChrome.getInstance();

				driver1.open(link);
				driver1.getOne("#yucs-sprop_button").click();
				htm = driver1.getHtml();

				// driver2 = new ChromeDriver();
				// driver2.
				String url = driver.getUrl();
				//U.log(url);

				//URLDecoder decode = new URLDecoder();
				try {
					url = URLDecoder.decode(url, "UTF-8");
					//U.log("Address:" + url);
					//U.log(add[0] + " " + add[1] + " " + add[2] + " " + add[3]);
					if (url.contains(add[1]))
						return true;
					if (url.contains(add[3]))
						return true;
				} catch (UnsupportedEncodingException ex) {
					ex.printStackTrace();
				}
				// decode.decode(url);
				// //U.log(url);
				// driver.close();
			}
			return false;
			// if(gAdd[1])
		}
		return false;
	}*/

//	private static HttpURLConnection getConn1(String urlPath, HashMap<String, String> customHeaders) throws IOException {
//
//		URL url = new URL(urlPath);
//		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//		conn.setDoOutput(true);
//		conn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:10.0.2) Gecko/20100101 Chrome/10.0.2");
//		conn.addRequestProperty("Accept", "text/css,application/xhtml+xml,application/xml,*/*;q=0.9");
//		conn.addRequestProperty("Accept-Language", "en-us,en;q=0.5");
//		conn.addRequestProperty("Cache-Control", "max-age=0");
//		conn.addRequestProperty("Connection", "keep-alive");
//
//		if (customHeaders == null || !customHeaders.containsKey("Referer")) {
//			conn.addRequestProperty("Referer", "http://" + url.getHost());
//		}
//		if (customHeaders == null || !customHeaders.containsKey("Host")) {
//			conn.addRequestProperty("Host", url.getHost());
//		}
//
//		if (customHeaders != null) {
//			for (String headerName : customHeaders.keySet()) {
//				conn.addRequestProperty(headerName, customHeaders.get(headerName));
//			}
//		}
//
//		return conn;
//	}// getConn

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
		//U.log(binghtml);
		String secmap = U.getSectionValue(binghtml, "<div id=\"resultsDiv\"", "</body>");
		bingLatLng[0] = U.getSectionValue(secmap, "<span id=\"span1\">Latitude:", "<br>").trim();
		bingLatLng[1] = U.getSectionValue(secmap, "<br>Longitude:", "</span>");

		//U.log("lat:" + bingLatLng[0]);
		//U.log("lng:" + bingLatLng[1]);
		return bingLatLng;
	}
*/
	public static String[] getCoordinatesGoogleApi(String add[]) throws IOException {
		String addr = add[0].trim() + "," + add[1].trim() + "," + add[2].trim();
		addr = "https://maps.googleapis.com/maps/api/geocode/json?address="// 1138
																			// Waterlyn
																			// Drive","Clayton","NC
				+ URLEncoder.encode(addr, "UTF-8");
		//U.log(addr);
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
		//U.log(addr);
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

/*	public static String getHardcodedAddress1(String builderName, String comUrl) throws Exception {
		commUrl = comUrl;
		String newFile = "/home/shatam-17/Harcoded Builders/" + builderName + ".csv";
		CsvListReader newFileReader = new CsvListReader(new FileReader(newFile), CsvPreference.STANDARD_PREFERENCE);
		List<String> newCsvRow = null;
		int count = 0;
		while ((newCsvRow = newFileReader.read()) != null) {

			if (count > 0) {
				// //U.log(newCsvRow.size());
				// builderName=newCsvRow.get(1);

				String aaa = getBuilderObj(newCsvRow);
				if (aaa != null)
					return aaa;
			}
			count++;
		}

		newFileReader.close();
		return null;
	}
*/
	public static String getHTMLwithProxy(String path) throws IOException {

		// System.setProperty("http.proxyHost", "104.130.132.119");
		// System.setProperty("http.proxyPort", "3128");
		// System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
		path = path.replaceAll(" ", "%20");
		// //U.log(" .............."+path);
		// Thread.sleep(4000);
		String fileName = getCache(path);

		// //U.log("filename:::" + fileName);

		File cacheFile = new File(fileName);
		if (cacheFile.exists())
			return FileUtil.readAllText(fileName);

		URL url = new URL(path);

		String html = null;

		// chk responce code

		// int respCode = CheckUrlForHTML(path);
		// //U.log("respCode=" + respCode);

		{

			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("47.252.4.64", 8888));
			final URLConnection urlConnection = url.openConnection(proxy);

			// Mimic browser
			try {
				urlConnection.addRequestProperty("User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:10.0.2) Gecko/20100101 Firefox/10.0.2");
				urlConnection.addRequestProperty("Accept", "text/css,*/*;q=0.1");
				urlConnection.addRequestProperty("Accept-Language", "en-us,en;q=0.5");
				urlConnection.addRequestProperty("Cache-Control", "max-age=0");
				urlConnection.addRequestProperty("Connection", "keep-alive");
				// //U.log("getlink");
				final InputStream inputStream = urlConnection.getInputStream();

				html = IOUtils.toString(inputStream);
				// final String html = toString(inputStream);
				inputStream.close();

				if (!cacheFile.exists())
					FileUtil.writeAllText(fileName, html);

				return html;
			} catch (Exception e) {
				//U.log(e);

			}
			return html;
		}

	}

	public static String sendPostRequestAcceptJson(String requestUrl, String payload) throws IOException {
		String fileName = U.getCache(requestUrl + payload);
		File cacheFile = new File(fileName);
		// log(fileName);
		if (cacheFile.exists())
			return FileUtil.readAllText(fileName);
		StringBuffer jsonString = new StringBuffer();
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("content-type", "application/json");
			connection.setRequestProperty("referer",
					"https://www.lennar.com/find-a-home?homeCoords=76.96377741792551%2C-48.206937616576994%2C-39.492261192976244%2C-149.105375116577&commCoords=76.96377741792551%2C-48.206937616576994%2C-39.492261192976244%2C-149.105375116577&zoom=3");
			connection.setRequestProperty("origin", "https://www.lennar.com");
//	        connection.setRequestProperty("x-requested-with", "XMLHttpRequest");
//	        connection.setRequestProperty("user-agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/70.0.3538.77 Chrome/70.0.3538.77 Safari/537.36");
//	        connection.setRequestProperty("cookie", "OT-SessionId=665fec61-b2b7-40fd-88f8-31aa3d514c07; otuvid=A847C352-361E-4A77-BFAA-7EA003EACF6D; notice_preferences=100; notice_behavior=none; _ga=GA1.2.465485978.1562663182; _gid=GA1.2.857089724.1562663182; ak_bmsc=19A565C56B445C5CDEC3FB8164F64981687C36477A1700000859245D58764417~plHir8g753AQ4jnlSBXW7rA28ZxyXq7HPgIJI8QBwx4J+vN9J+CbWshygDarWzDOIhCRc0tWbFqvRAcOHcIPtFR2rHWmobKo+An3LlEE9sPGv3AKJVm75k8/aBvRi3Y8yB4Gh9lfbRmM9qMYfLTwsxhyVz17vM90qmgq1ChYjDIRovuKCYhWmHyDLEHYxd5DPeRrUaAWgEHr4tyhkTkT9rrOBQPLhcHHFjNeIXJfGPnzuUVIN00hsj/atrZtm0GBOC; __gads=ID=c7a0886b4ef2aca9:T=1562663181:S=ALNI_MYU9xFIUi26LDumAw3KFWGyEpFF8Q; OT_dtp_values=covers=2&datetime=2019-07-09 19:00; _csrf=xCJFVNLyun7GmB7I8S9EPSSj; spredCKE=redcount=0; aCKE=4e41c827-cd3e-4917-8f1b-cc5fa7f1f11a; lsCKE=cbref=1&ors=otcom; s_cc=true; s_fid=00AE7231ED57FB36-0EA1676D9565D377; s_dl=1; s_sq=%5B%5BB%5D%5D; s_vi=[CS]v1|2E922EA005031655-60001184E00078BD[CE]; tlrCKE=2019-07-09+09%3a25%3a49Z; s_nr=1562664350445-New; _gat_UA-52354388-1=1; notice_gdpr_prefs=0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100:; OT-Session-Update-Date=1562665903; ftc=x=2019-07-09T10%3A51%3A43&c=1&pt1=1&pt2=1&er=0; lvCKE=lvmreg=%2C0&histmreg=57%2C0%7C358%2C0%7C%2C0; bm_sv=503A3652B0E2E4FAB009B06B5CFDEC7C~HdCtMzHuVx9H0mAFZXPyr2ywwoxHzi2bTNE0CdlyXqfdWzuB6JUAC4JHV3kVuKrRIfd4eYvyz2RT/Ckp1lV18FKxCRIFVF68hF6x6ztjjzdawbVB8IBHncGQ8ELCzpQ4nMt/kH0pLsBlgBWlQh7O9MPWznkERaM368kWogh3uww=");
//	        connection.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
//	        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
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

	////////////////////// new Code////////////////////////////////////
	public static String getRedirectedURL(String mainDomain, String url) throws IOException {
		if (!url.contains("http"))
			url = mainDomain + url;
		HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
		con.setInstanceFollowRedirects(false);
		con.connect();
		con.getInputStream();

		//U.log("response code : " + con.getResponseCode());
		if (con.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM
				|| con.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP) {
			String redirectUrl = con.getHeaderField("Location");
			return getRedirectedURL(mainDomain, redirectUrl);
		}
		return url;
	}

	public static String getGoogleZipFromAddressWithKey(String inputadd[]) throws IOException {

		String st = inputadd[0].trim() + "," + inputadd[1].trim() + "," + inputadd[2].trim();
		String addr = "https://maps.googleapis.com/maps/api/geocode/json?address=" + st
				+ "&key=AIzaSyDhsO7Ska4s4zUW_68R1n8OhRHJuZP5gm8";
		//U.log(addr);

		//U.log(U.getCache(addr));
		String html = U.getHTML(addr);
		String status = U.getSectionValue(html, "status\" : \"", "\"");

		if (status.trim().equals("OK")) {
			String txt = U.getSectionValue(html, "formatted_address\" : \"", "\"");
			//U.log("Address txt Using gmap key :: " + txt);
			if (txt != null)
				txt = txt.trim();
			txt = txt.replace("7970 U.S, Co Rd 11", "7970 US Co Rd 11")
					.replaceAll("The Arden, |TPC Sugarloaf Country Club, ", "").replace("50 Biscayne, 50", "50");
			txt = txt.replaceAll("110 Neuse Harbour Boulevard, ", "");
			txt = txt.replaceAll(
					"Suite CNewark, |Waterview Tower, |Liberty Towers, |THE DYLAN, |Cornerstone, |Roosevelt Towers Apartments, |Zenith, |The George Washington University,|Annapolis Towne Centre, |Waugh Chapel Towne Centre,|Brookstone, |Rockville Town Square Plaza, |University of Baltimore,|The Galleria at White Plains,|Reston Town Center,",
					"");
			String[] add = txt.split(",");
			add[3] = Util.match(add[2], "\\d+");
			add[2] = add[2].replaceAll("\\d+", "").trim();
			return add[3];
		} else {
			return "-";
		}
	}

	public static final String hereApiID = "aGa8KgrWvrUCGqoLCSL9";
	public static final String hereApiCode = "Va551VVoWCaWx7JIMok8eA";

	public static String[] getlatlongHereApi(String add[]) throws IOException {
		String addr = add[0] + "," + add[1] + "," + add[2];
//		U.log(addr);
		addr = "https://geocoder.api.here.com/6.2/geocode.json?searchtext=" + URLEncoder.encode(addr, "UTF-8")
				+ "&app_id=" + hereApiID + "&app_code=" + hereApiCode + "&gen=9";
		//U.log(addr);
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
		// //U.log(lat);
		return latlng;

	}

	public static String[] getAddressHereApi(String latlng[]) throws Exception {
	 return U.getNewBingAddress(latlng);
//		String st = latlng[0].trim() + "%2C" + latlng[1].trim()
//				+ "%2C100&mode=retrieveAddresses&maxresults=1&gen=9&app_id=" + hereApiID + "&app_code=" + hereApiCode;
//		String addr = "https://reverse.geocoder.api.here.com/6.2/reversegeocode.json?prox=" + st;
//		//U.log(addr);
//		U.log(U.getCache(addr));
//		String html = U.getPageSource(addr);
//		String txt = U.getSectionValue(html, "\"Address\":{\"Label\":\"", "\"");
//		//U.log("txt:: " + txt);
//		if (txt != null)
//			txt = txt.trim();
//		txt = txt.replaceAll("The Arden, |TPC Sugarloaf Country Club, ", "");
//		txt = txt.replaceAll("110 Neuse Harbour Boulevard, ", "");
//		txt = txt.replaceAll(
//				"Waterview Tower, |Liberty Towers, |THE DYLAN, |Cornerstone, |Roosevelt Towers Apartments, |Zenith, |The George Washington University,|Annapolis Towne Centre, |Waugh Chapel Towne Centre,|Brookstone, |Rockville Town Square Plaza, |University of Baltimore,|The Galleria at White Plains,|Reston Town Center,",
//				"");
//		String[] add = txt.split(",");
//		if (add.length == 5) {
//			String newAdd[] = { "", "", "", "" };
//			newAdd[0] = add[0] + "," + add[1];
//			newAdd[1] = add[2].trim();
//			newAdd[3] = Util.match(add[3], "\\d+");
//			newAdd[2] = add[3].replaceAll("\\d+", "").trim();
//			return newAdd;
//		} else {
//			add[1] = add[1].trim();
//			add[3] = Util.match(add[2], "\\d+");
//			add[2] = add[2].replaceAll("\\d+", "").trim();
//			return add;
//		}
	}

	public static String[] getNewBingLatLong(String[] address) {
		// TODO Auto-generated method stub

		String[] latLong = new String[2];
		String addressLine = address[0] + "," + address[1] + "," + address[2] + "," + address[3];
		U.log(addressLine);
		addressLine = addressLine.replace(" #", "");
		if (addressLine == null || addressLine.trim().length() == 0)
			return null;
		addressLine = addressLine.trim().replaceAll("\\+", " ");
		String geocodeRequest = "http://dev.virtualearth.net/REST/v1/Locations/'" + addressLine
				+ "'?o=xml&key=AninuaBy5n6ekoNCLHltcvwcnBGA7llKkNDBNO5XOuHNHJKAyo0BQ8jH_AhhP6Gl";
		// //U.log("-----------------addressline-----------"+geocodeRequest);
		try {
			String xml = U.getHTML(geocodeRequest);
			// //U.log("--------------------------xml---------------------------------"+xml);
			latLong[0] = U.getSectionValue(xml, "<Latitude>", "</Latitude>");
			latLong[1] = U.getSectionValue(xml, "<Longitude>", "</Longitude>");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return latLong;

	}

	public static String[] getNewBingAddress(String[] latlong) {
		// TODO Auto-generated method stub

		String[] addr = null;
		try {
			String htm = U.getHTML("http://dev.virtualearth.net/REST/v1/Locations/" + latlong[0] + "," + latlong[1]
					+ "?o=json&jsonp=GeocodeCallback&key=AninuaBy5n6ekoNCLHltcvwcnBGA7llKkNDBNO5XOuHNHJKAyo0BQ8jH_AhhP6Gl");
			String[] adds = U.getValues(htm, "formattedAddress\":\"", "\"");
			U.log(Arrays.toString(adds));
			for (String item : adds) {
//				addr = U.getAddress(item);
				item = item.replace(", United States", "");
				item = item.replace(", USA", "");
//				addr = U.getAddress(item);
				addr = U.getAddress(item);
				if (addr == null || addr[0] == "-")
					continue;
				else {
//					U.log("Bing Address =>  Street:" + addr[0] + " City :" + addr[1] + " state :" + addr[2] + " ZIP :"
//							+ addr[3]);
					return addr;
				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return addr;
	}

	/**
	 * @author Pallavi Meshram
	 */
	// Format million price
	public static String formatMillionPricesNew(String html) {
//			html="'>From $1.4 m";
		html = html.replaceAll("Million|million", "m");
		Matcher millionPrice = Pattern.compile("\\$\\d\\.(\\d*) m|M", Pattern.CASE_INSENSITIVE).matcher(html);
		String zeroString = "";
		int i;
		while (millionPrice.find()) {
			int count = millionPrice.group(1).length() + 1;
			for (i = 0; i <= 3 - count; i++) {
				zeroString += 0;
			}
			String floorMatch = millionPrice.group().replace(" m| M", zeroString + ",000").replace(".", ","); // $1.3 M
																												// //$1.32
																												// M
																												// //$1.234
																												// M
			html = html.replace(millionPrice.group(), floorMatch);
		} // end millionPrice
//			//U.log(html);
		return html;
	}
	
	public static ArrayList<String[]> readCSVFile(String path) throws Exception {
		ArrayList<String[]> ar = new ArrayList<String[]>();
		CsvListReader newFileReader = new CsvListReader(new FileReader(path), CsvPreference.STANDARD_PREFERENCE);
		List<String> listRow = null;

		int count = 0;
		while ((listRow = newFileReader.read()) != null) {
			// if (count > 0)
			{
				// //U.log(listRow.get(1));
				String[] aSicData = listRow.toArray(new String[0]);
				ar.add(aSicData);
			}
			count++;

		}
		newFileReader.close();

		// //U.log(count);
		return ar;
	}
	//function 
/*	public static void sleepFive() throws InterruptedException {
		Thread.sleep(1000*60*5);
		
	}
	public static void sleepTen() throws InterruptedException {
		Thread.sleep(1000*60*10);
		
	}
	public static void sleepSix() throws InterruptedException {
		Thread.sleep(1000*60*6);
		
	}
	public static void sleepTwelve() throws InterruptedException {
		Thread.sleep(1000*60*12);
		
	}*/

	public static String html2text(String html) {
	    return Jsoup.parse(html).text();
	}





}
