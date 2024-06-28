package com.saurabh;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public class SSHExample {
    public static void main(String[] args) {
        // Define the SSH command parameters
        String username = "root";
        String remoteHost = "172.98.14.187"; // Replace with your actual remote host address

        // Construct the SSH command
        String sshCommand = String.format("ssh %s@%s", username, remoteHost);

        try {
            // Execute the SSH command
            ProcessBuilder builder = new ProcessBuilder();
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                // Windows requires running the command in a cmd shell
                builder.command("cmd.exe", "/c", sshCommand);
            } else {
                // Unix/Linux uses bash
                builder.command("bash", "-c", sshCommand);
            }

            Process process = builder.start();

            // Wait for the process to complete
            boolean completed = process.waitFor(60, TimeUnit.SECONDS);

            if (completed && process.exitValue() == 0) {
                System.out.println("SSH connection successful.");
            } else {
                // Process error stream to get more details
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    System.err.println(line);
                }
                System.err.println("SSH connection failed.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
