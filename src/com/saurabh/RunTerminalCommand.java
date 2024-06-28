package com.saurabh;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RunTerminalCommand {
    public static void main(String[] args) {
        try {
        	
            // Path to the JAR file
            String jarPath = "/home/shatam-100/Desktop/LoadDateCheck.jar";

            // Command to run the JAR file
            String command = "java -jar " + jarPath;
//            String command = "scp root@172.98.14.187:/root/LoadDateCheck.csv /home/shatam-100/Desktop";
//            String[] command = {"scp", "root@172.98.14.187:/root/LoadDateCheck.csv", "/home/shatam-100/Desktop"};

            
            // Command to run (example command: "ls -l")
//            String command = "ls";
//            String command = "df -h";


            // Start the process
            ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
//            ProcessBuilder processBuilder = new ProcessBuilder(command);

            Process process = processBuilder.start();

            // Read output from the process
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // Wait for the process to finish
            int exitCode = process.waitFor();

            // Print exit code
            System.out.println("Process exited with code: " + exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
