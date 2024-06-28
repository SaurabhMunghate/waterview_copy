package com.saurabh;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.File;

public class RunSSHCommand {
    public static void main(String[] args) {
        try {
            // Path to the expect script // chmod +x ssh_expect.sh
//            String userHome = System.getProperty("user.home");

            // Construct the script path relative to the user's home directory
            String scriptPath = "/home/shatam-100/Down/WaterView_Data/ssh/ssh_expect.sh";

            // Verify that the script exists and is executable
            File scriptFile = new File(scriptPath);
            if (!scriptFile.exists() || !scriptFile.canExecute()) {
                System.err.println("Script file does not exist or is not executable: " + scriptPath);
                return;
            }

            // Command to be executed
            String[] command = {"/bin/sh", "-c", scriptPath};

            // Create a ProcessBuilder with the command
            ProcessBuilder processBuilder = new ProcessBuilder(command);

            // Start the process
            Process process = processBuilder.start();

            // Get the input stream to read the output of the command
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // Get the error stream to read any errors
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                System.err.println(errorLine);
            }

            // Wait for the process to complete
            int exitCode = process.waitFor();
            System.out.println("Exited with code: " + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
