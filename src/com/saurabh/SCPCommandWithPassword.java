package com.saurabh;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class SCPCommandWithPassword {
    public static void main(String[] args) {
        try {
            System.out.println("Starting SCP process...");

            // Command to run
            String[] command = {"scp", "root@172.98.14.187:/root/LoadDateCheck.csv", "/home/shatam-100/Desktop"};

            // Start the process
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            Process process = processBuilder.start();

            // Get input stream to read process output
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            // Get error stream to read process errors
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            // Get output stream to provide password if prompted
            OutputStream outputStream = process.getOutputStream();

            System.out.println("Waiting for SCP process output...");

            
            // Read output from the process
            String line;
            
         // After reading standard output
            System.out.println("SCP process output completed.");

            // Read error output from the process
            System.out.println("Reading SCP process error stream...");
            while ((line = errorReader.readLine()) != null) {
                System.out.println("Error: " + line);
            }

            // Wait for the process to finish
            System.out.println("Waiting for SCP process to complete...");
            int exitCode = process.waitFor();

            
//            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
//                if (line.contains("password:")) {
//                    System.out.println("Password prompt detected. Providing password...");
//                    // Provide the password if prompted
//                    String password = "your_password\n"; // Replace with your actual password
//                    outputStream.write(password.getBytes());
//                    outputStream.flush();
//                    System.out.println("Password provided.");
//                }
//            }

//            System.out.println("SCP process output completed.");

            // Read error output from the process
//            while ((line = errorReader.readLine()) != null) {
//                System.out.println("Error: " + line);
//            }

            // Wait for the process to finish
//            System.out.println("Waiting for SCP process to complete...");
//            int exitCode = process.waitFor();

            // Print exit code
            System.out.println("Process exited with code: " + exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
