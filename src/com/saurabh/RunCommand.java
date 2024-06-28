package com.saurabh;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class RunCommand {
    public static void main(String[] args) {
        try {
            // Command to be executed in the shell
            String[] command = {"/bin/sh", "-c", "cd /home/shatam-100/Down/WaterView_Data/FTP_DATA/LoadDateData/ && ls"};
            // For Windows, use something like new String[]{"cmd.exe", "/c", "cd path && dir"}

            // Execute the command
            Process process = Runtime.getRuntime().exec(command);

            // Get the input stream to read the output of the command
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // Wait for the process to complete
            int exitCode = process.waitFor();
            System.out.println("Exited with code: " + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
