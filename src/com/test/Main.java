package com.test;

import java.io.IOException;
import java.util.Base64;

import com.shatam.utils.FileUtil;
import com.shatam.utils.U;

class Counter {
    static int count = 0; // Static variable

    Counter() {
        count++; // Incrementing static variable
    }

    void displayCount() {
        System.out.println("Count: " + count);
    }
}

public class Main {
    public static void main(String[] args) throws IOException {
//        Counter c1 = new Counter();
//        Counter c2 = new Counter();
//        Counter c3 = new Counter();
//        c2.displayCount(); // Output: Count: 3
//        
//        String str = "Hello";
//        str =  "Hello";
//    	String DecryptedDataHtml = "";
		String comHtml = U.getHTML("https://qpublic.schneidercorp.com/Application.aspx?AppID=1048&PageTypeID=4&KeyValue=890140530000");
//		String[] allDecryptedData = U.getValues(comHtml, "data-widget-config=\\\"", "\\\">");
//		U.log(allDecryptedData.length);
//		for(String st :allDecryptedData) {
////			U.log(st);
//			DecryptedDataHtml += getDecryptedData(st);
//		}
//		FileUtil.writeAllText("/home/shatam-100/Cache/Data.txt",   DecryptedDataHtml);

    }

	public static String getDecryptedData(String widgetBinding){
		   byte[] decodedBytes = Base64.getDecoder().decode(widgetBinding);
		   String decodedString = new String(decodedBytes);
		   //System.out.println(decodedString);
		   return decodedString;
		}
}
