package com.test;
import java.io.File;

public class LargeFileFinder {

    public static void main(String[] args) {
        // Starting directory (modify this path as needed)
        String directoryPath = "/home/shatam-100/";  // e.g., "C:/", "/home/user/"
        
        // Minimum size in bytes (e.g., 100 MB = 100 * 1024 * 1024)
        long minSizeInBytes = 300L * 1024 * 1024;

        findLargeFiles(new File(directoryPath), minSizeInBytes);
    }

    public static void findLargeFiles(File dir, long minSize) {
        if (!dir.exists()) {
            System.out.println("Directory does not exist: " + dir.getAbsolutePath());
            return;
        }

        File[] files = dir.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                findLargeFiles(file, minSize);
            } else if (file.length() >= minSize) {
                System.out.printf("File: %s, Size: %.2f MB%n", file.getAbsolutePath(), file.length() / (1024.0 * 1024));
            }
        }
    }
}
