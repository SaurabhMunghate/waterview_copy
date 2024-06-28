package com.saurabh;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RunJarWithInput {
    public static void main(String[] args) {
        try {
            // Command to run the JAR file
            String command = "java -jar LoadDateCheck.jar";

            // Start the process
            Process process = Runtime.getRuntime().exec(command);

            // Provide input to the process (if needed)
            String input = "g26VAZ8zesr54B0ARi";
            process.getOutputStream().write(input.getBytes());
            process.getOutputStream().flush();

            // Read output from the process (if needed)
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
