/**
 * @author Sawan Meshram
 * @date 14 June 2022
 */
package com.shatam.utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

//import static java.nio.file.Files.createDirectories;

public class ShatamUnZip {
//	public static void main(String[] args) throws IOException {
//		unZip("/home/shatam-25/Downloads/Federal_Hazard/County_Subdivision/cb_2018_02_cousub_500k.zip");
//	}
	public static void unZip(String sourceZipFile) throws IOException{
		 unZip(sourceZipFile, sourceZipFile.replace(".zip", ""));
	}
	
	public static void unZip(String sourceZipFile, String outputDirectory) throws IOException
	{
//		File folder = new File(outputDirectory);
//		createDirectories(folder.toPath());
	
		try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(sourceZipFile)))
		{
			ZipEntry nextEntry = zipInputStream.getNextEntry();
			while (nextEntry != null){
				File newFile = new File(outputDirectory, nextEntry.getName());
				if(nextEntry.isDirectory()) newFile.mkdirs();
				else {
					File parent = newFile.getParentFile();
					if(!parent.exists()) parent.mkdirs();
				}
				System.out.println("unzip '"+newFile+"'");
				writeFile(zipInputStream, newFile);
			
				nextEntry = zipInputStream.getNextEntry();
			}
			zipInputStream.closeEntry();
		}
	}

	private static void writeFile(ZipInputStream inputStream, File file) throws IOException {
		byte[] buffer = new byte[1024];
		file.createNewFile();
		try (FileOutputStream fileOutputStream = new FileOutputStream(file)){
			int length;
			while ((length = inputStream.read(buffer)) != -1){
				fileOutputStream.write(buffer, 0, length);
			}
		}
	}
}
