package com.test;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpLoopExample {
    public static void main(String[] args) {
        String urlString = "http://192.168.0.143:8081/";

        for (int i = 1; i <= 10000; i++) {
            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                System.out.println("Request #" + i + " - Response Code: " + responseCode);

                // Optional: Read the response body
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();

                // Optional: print response body
                // System.out.println("Response Body: " + content.toString());

                connection.disconnect();

            } catch (Exception e) {
                System.err.println("Request #" + i + " failed: " + e.getMessage());
            }
        }
    }
}
