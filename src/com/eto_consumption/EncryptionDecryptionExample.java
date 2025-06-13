package com.eto_consumption;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKTWriter;
//import org.locationtech.jts.io.hex.HexReader;

import com.shatam.utils.FileUtil;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class EncryptionDecryptionExample {
	public static void main(String[] args) throws Exception {
		String iv = "ZvHZDBanA8EXeTD/H+XFaQ==";
		String derived = "0y+SueKkOJe/vVGOeXiieg==";
        String fileName = "/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/District_Boundary_json/District_Boundary_json_sanantonio.json";

		// Encrypt the data
		String message = FileUtil.readAllText(
				fileName);
		String encryptedText = getEncryptedText(message, derived, iv);
		System.out.println("Encrypted Text: " + message.length());
//		System.out.println(encryptedText);
		FileUtil.writeAllText(
				fileName.replace(".json", "")+"_encryptedText.json",
				encryptedText);

		
		String encrypted = getEncryptedText("Hi this is saurabh", derived, iv);
		System.out.println("encrypted : "+encrypted);
		String decrypted = getDecryptedText("7PYkhgOzMj5o2QxIXwp0BrpaT0curc88REKF2eBrzUU=", derived, iv);
		System.out.println("decrypted : "+decrypted);
//		File folder = new File( "/home /shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-20240 4/District_Boundary_json");
		File folder = new File(" /home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/ District_Boundary_json_Test1");
		File[] files = folder.listFiles();
		try {

			for (File file : files) {
				if (file.isFile()) {
					String mes = folder + "/" + file.getName();
					System.out.println(file.getName());
					String message1 = FileUtil.readAllText(mes);
//              EncryptionFile(folder+"/"+file.getName());
//              String decryptedText = getDecryptedText(message, derived, iv);
					String encryptedText1 = getEncryptedText(message1, derived, iv);
//              System.out.println(encryptedText.length());
//					String writeAll = "/home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/District_Boundary_json_Encrypted"+ "";
					String writeAll = " /home/shatam-100/Down/WaterView_Data/Tiles/District-Boundaries-202404/ District_Boundary_json_Test1"+ "";
					FileUtil.writeAllText(writeAll + "/" + file.getName(), encryptedText1);
					System.out.println(writeAll + "/" + file.getName());

//              FileUtil.writeAllText(writeAll+"/"+file.getName().replace("_T", "_").replace(".json", "_json.json"), encryptedText);	
//                

//                 Decrypt the data
//                if(mes.contains("_T")) {
//                    message = FileUtil.readAllText(mes.replace("_T", "_"));
//                    String decryptedText = getDecryptedText(message, derived, iv);
//                    System.out.println("Decrypted Text: " + decryptedText.length());
//                    String writeAll = "/home/shatam-100/Down/WaterView_Data/VPS_DATA/MONTE_VISTA_Encrypted"+""; 
//                    FileUtil.writeAllText(writeAll+"/"+file.getName().replace("_T", "_").replace(".json", "_json.json"), decryptedText);	

//                }

				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

//        File fol = new File("/home/shatam-100/CODE_Repository/EagleAerialCIIWater_EncryptDecrypt_Updated_6April_2/EagleAerialCIIWater_EncryptDecrypt/EagleAerialCIIWater/EagleAerialCIIWater/App_Data/Districts/MONTEVISTACO226/DataFile/Nonencrypted");
//        File[] fil = fol.listFiles();
//        
//        for (File file : fil) {
//            if (file.isFile()) {
//            	String mes = fol+"/"+file.getName();
//                System.out.println(file.getName());
//                if(file.getName().contains("Data_Folder"))continue;
////                message = FileUtil.readAllText(mes);
////              EncryptionFile(folder+"/"+file.getName());
//                String decryptedText = getDecryptedText(mes, derived, iv);
//                try {
//                	System.out.println(decryptedText.substring(0, 100));
//				} catch (Exception e) {
//					System.out.println(e);
//					// TODO: handle exception
//				}
//                
//
//            }
//        }
		// Decrypt the data
//        String decryptedText = getDecryptedText(fileName, derived, iv);
//        System.out.println("Decrypted Text: " + decryptedText.length());
//      FileUtil.writeAllText("/home/shatam-100/Downloads/Premise_Boundaries_convertedData_json (2).json", decryptedText);


	}

	public static String getDecryptedFile(String fileName, String sharedSecret, String IvText) throws Exception {
		StringBuilder data = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)))) {
			String line;
			while ((line = br.readLine()) != null) {
				data.append(line);
			}
		}

		if (data.length() > 0) {
			Cipher aes = Cipher.getInstance("AES/CBC/PKCS5Padding");
			SecretKeySpec keySpec = new SecretKeySpec(Base64.getDecoder().decode(sharedSecret), "AES");
			IvParameterSpec ivSpec = new IvParameterSpec(Base64.getDecoder().decode(IvText));
			aes.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
			byte[] decrypted = aes.doFinal(Base64.getDecoder().decode(data.toString()));
			return new String(decrypted, StandardCharsets.UTF_8);
		}
		return "";
	}
	public static String getDecryptedText(String encryptedText, String sharedSecret, String IvText) throws Exception {
	    if (encryptedText != null && !encryptedText.isEmpty()) {
	        Cipher aes = Cipher.getInstance("AES/CBC/PKCS5Padding");
	        SecretKeySpec keySpec = new SecretKeySpec(Base64.getDecoder().decode(sharedSecret), "AES");
	        IvParameterSpec ivSpec = new IvParameterSpec(Base64.getDecoder().decode(IvText));
	        aes.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
	        byte[] decrypted = aes.doFinal(Base64.getDecoder().decode(encryptedText));
	        return new String(decrypted, StandardCharsets.UTF_8);
	    }
	    return "";
	}
	public static String getEncryptedText(String plainText, String sharedSecret, String IvText) throws Exception {
		Cipher aes = Cipher.getInstance("AES/CBC/PKCS5Padding");
		SecretKeySpec keySpec = new SecretKeySpec(Base64.getDecoder().decode(sharedSecret), "AES");
		IvParameterSpec ivSpec = new IvParameterSpec(Base64.getDecoder().decode(IvText));
		aes.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
		byte[] encrypted = aes.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
		return Base64.getEncoder().encodeToString(encrypted);
	}
}
