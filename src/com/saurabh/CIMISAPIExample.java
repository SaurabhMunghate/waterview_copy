package com.saurabh;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class CIMISAPIExample {
    public static void main(String[] args) {
//        String apiUrl = "http://et.water.ca.gov/api/data?appKey=08807bb2-ce9c-4b65-af3c-dec10f6f3daf&targets=20,19,17&startDate=2010-01-01&endDate=2010-01-06";
        String apiUrl = "http://et.water.ca.gov/api/data?appKey=08807bb2-ce9c-4b65-af3c-dec10f6f3daf&targets=2,8,127&startDate=2010-01-01&endDate=2010-01-05";
//        String apiUrl = "https://et.water.ca.gov/api/station/10";
        String fileName = "response.json"; // Name for the output file
        
        try {
            // Create the URL object
            URL url = new URL(apiUrl);

            // Create the HttpURLConnection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the request method and headers
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");

            // Get the response code
            int responseCode = connection.getResponseCode();

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
}
