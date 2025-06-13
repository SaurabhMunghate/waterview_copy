package com.eto_consumption;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

import com.shatam.utils.U;

public class FunctionFinder {

	public static void main(String[] args) { 
//        String functionName = "outputArr";
		System.out.println("----");
		String functionName = "connection.setRequestProperty(\"referer";
		System.out.println(functionName);
		String directoryPath = "/home/shatam-100/git/shatambuilder/BuildersCode";

		System.out.println("hi");
		try {
			Path startPath = Paths.get(directoryPath);
			Files.walk(startPath).filter(Files::isRegularFile).forEach(path -> {

				try {
					if (path.toString().contains("node_modules")) {    
						return;
					}
//                        	System.out.println(path);
					List<String> lines = Files.readAllLines(path);
					for (String line : lines) {
						if (line.contains(functionName)) {
							System.out.println("Function " + "" + " found in file: " + path.toString());
							break;
						}
					}
				} catch (IOException e) {
//                            e.printStackTrace();
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Done");
	}
}
