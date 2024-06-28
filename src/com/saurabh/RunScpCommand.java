package com.saurabh;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RunScpCommand {
    public static void main(String[] args) {
        try {
            // Command to run (scp command)
            String command = "scp root@172.98.14.187:/root/LoadDateCheck.csv /home/shatam-100/Desktop";

            // Start the process
            ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", command);
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
