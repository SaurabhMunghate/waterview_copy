/*
 * @ Code By : Pallavi
 * @Date : Feb2022
 * 
 * 
 * Code can be used to unzip all the files within a specified folder.
 * i/p : Folder Name.
 * o/p : All the Unziped files in the i/p folder.
 */

package com.shatam.utils;

import java.io.*;
import java.util.zip.*;

public class UnzipFilesFromDirectory {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		File f = new File(System.getProperty("user.home")+File.separator+"HazardsData/SpecialFloodHazardKentucky");
//		find(f);
		findAndDeleteFiles(f);
//		U.log(System.getProperty("user.dir"));
	}

	 private static void findAndDeleteFiles(File f) throws Exception{
		// TODO Auto-generated method stub
		 File[] paths;
			System.out.println(f);
			if(f.isFile()){
				System.out.println(f.getName()+"::from file");
				
			}else if(f.isDirectory()){
				paths = f.listFiles();
				System.out.println(f.getName());
					for(File path : paths){
						if(path.isDirectory()){
							findAndDeleteFiles(new File(path.getPath()));
							System.out.println(path.getName()+":::::::from if");
						}else{
							if(!path.getCanonicalPath().contains("csv") && !path.getCanonicalPath().contains("FLD_HAZ_AR")) {
								System.out.println(path.getName()+":::from else");
								path.delete();
							}
						}
					}
			}
	}

	public static void unzipFile(String filePath){
         	//createFolder for file extraction
		    File dir = new File(filePath.replace(".zip", ""));
		    dir.mkdir();
		    //read from zip
	        FileInputStream fis = null;
	        ZipInputStream zipIs = null;
	        ZipEntry zEntry = null;
	        try {
	            fis = new FileInputStream(filePath);
	            zipIs = new ZipInputStream(new BufferedInputStream(fis));
	            while((zEntry = zipIs.getNextEntry()) != null){
	                try{
	                    byte[] tmp = new byte[4*1024];
	                    FileOutputStream fos = null;
	                    String opFilePath = filePath.replace(".zip", "")+File.separator+zEntry.getName();
	                    System.out.println("Extracting file to "+opFilePath);
	                    fos = new FileOutputStream(opFilePath);
	                    int size = 0;
	                    while((size = zipIs.read(tmp)) != -1){
	                        fos.write(tmp, 0 , size);
	                    }
	                    fos.flush();
	                    fos.close();
	                } catch(Exception ex){
	                     
	                }
	            }
	            zipIs.close();
	        } catch (FileNotFoundException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	    }
	 
	 public static void find(File fl) throws Exception{
			File[] paths;
			System.out.println(fl);
			if(fl.isFile()){
				System.out.println(fl.getName()+"::from file");
				
			}else if(fl.isDirectory()){
				paths = fl.listFiles();
				System.out.println(fl.getName());
					for(File path : paths){
						if(path.isDirectory()){
//							find(new File(path.getPath()));
							System.out.println(path.getName());
						}else{
							System.out.println(path.getCanonicalPath()+":::from else");
							unzipFile(path.getCanonicalPath()+"");
						}
					}
			}
		}

}
