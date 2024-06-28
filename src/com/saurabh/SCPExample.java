package com.saurabh;
import java.io.*;
import java.util.concurrent.TimeUnit;

import javax.measure.spi.SystemOfUnits;

public class SCPExample {
    public static void main(String[] args) {
        // Define the SCP command parameters
//        String username = "remote_username";
//        String remoteHost = "remote_host_address";
//        String remoteFilePath = "/path/on/remote/host/destination.txt";
//        String localFilePath = "/path/on/local/system/source.txt";
        
        String username = "root";
        String remoteHost = "172.98.14.187";
        String remoteFilePath = "/root/LoadDateCheck.csv";
        String localFilePath = "/home/shatam-100/Desktop";
        
        // Construct the SCP command
        String scpCommand = String.format("scp %s %s@%s:%s", localFilePath, username, remoteHost, remoteFilePath);
//        shatam-100@saurabh:~$ scp root@172.98.14.187:/root/LoadDateCheck.csv /home/shatam-100/Desktop
        try {
            // Execute the SCP command
            ProcessBuilder builder = new ProcessBuilder();
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                // Windows requires running the command in a cmd shell
                builder.command("cmd.exe", "/c", scpCommand);
            } else {
                // Unix/Linux uses bash
                builder.command("bash", "-c", scpCommand);
            }

            Process process = builder.start();
            System.out.println("builder.start");
            // Wait for the process to complete
            boolean completed = process.waitFor(60, TimeUnit.SECONDS);
            System.out.println("builder.start");

            if (completed && process.exitValue() == 0) {
                System.out.println("SCP transfer successful.");
            } else {
                // Process error stream to get more details
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    System.err.println(line);
                }
                System.err.println("SCP transfer failed.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }


    }
}
