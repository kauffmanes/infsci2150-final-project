import java.io.*;
import java.nio.file.*;
import java.security.*;
import java.security.spec.*;
import java.security.interfaces.*;
import java.security.KeyStore.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.text.NumberFormat;
import java.text.DecimalFormat;
import javax.xml.bind.DatatypeConverter;

/**
 * A class for employing symmetric encryption.
 * Author: Jeffrey A. James
 * April 2017
*/

public class CryptoOpSym {
    public static void compute(int keySize, String algorithm, String transform, File inFile) {
        int mode1 = 1;
		//Get output file name
		File outFile1 = getOutFile(mode1, inFile, algorithm);
		//Get secret key
		SecretKey secretKey = getKey(keySize, algorithm);
		
		//Encrypt
		byte[] iv = null;
		long start1 = System.currentTimeMillis(); //start timing of encryption
		byte[] ivout = cryptor(Cipher.ENCRYPT_MODE, secretKey, iv, algorithm, transform, inFile, outFile1);
		long end1 = System.currentTimeMillis(); //end timing of encryption
		NumberFormat totaltime1 = new DecimalFormat("#0.00000");
		System.out.println("Execution time of " + transform + " encryption is " + totaltime1.format((end1 - start1) / 1000d) + " seconds");
		System.out.println("Written to: " + outFile1);

		//Decrypt
        int mode2 = 0;
		File outFile2 = getOutFile(mode2, outFile1, algorithm);
		long start2 = System.currentTimeMillis(); //start timing of decryption
		cryptor(Cipher.DECRYPT_MODE, secretKey, ivout, algorithm, transform, outFile1, outFile2);
		long end2 = System.currentTimeMillis(); //end timing of encryption
		NumberFormat totaltime2 = new DecimalFormat("#0.00000");
		System.out.println("Execution time of " + transform + " decryption is " + totaltime2.format((end2 - start2) / 1000d) + " seconds");
		System.out.println("Written to: " + outFile2);
	} //end compute
	
	private static File getOutFile(int mode, File inFile, String algorithm) {
		String input = inFile.toString();
		String ext = "";
		String base = inFile.getName();
		int i = input.lastIndexOf('.');
		File outFile = new File(input);
		if (i > 0) {
	    	ext = input.substring(i+1);
			base = base.substring(0, i);
			if (mode == 1)
				outFile = new File(base + "_e" + algorithm + "." + ext);
			else if (mode == 0)
				outFile = new File(base + "_d" + algorithm + "." + ext);
		}
		else {
			if (mode == 1)
				outFile = new File(base + "_e" + algorithm);
			else if (mode == 0)
				outFile = new File(base + "_d" + algorithm);
		}
		return outFile;
	} //end getOutFile
	
	private static SecretKey getKey(int keySize, String algorithm) {
		SecretKey secretKey = null;
		try {
			//Generate symmetric key
			KeyGenerator keyGen = KeyGenerator.getInstance(algorithm);
			//SecureRandom random = SecureRandom.getInstance("NativePRNGBlocking");
			//keyGen.init(keySize, random);
			keyGen.init(keySize);
			secretKey =  keyGen.generateKey();
			
			//Print symmetric key
			String encodedKey = DatatypeConverter.printHexBinary(secretKey.getEncoded());
			System.out.println("Secret key in hex: " + encodedKey);

			return secretKey;
		} catch(Exception e){
			e.printStackTrace();
			return secretKey;
		}
	} //end getKey
	
	private static byte[] cryptor(int cipherMode, SecretKey secretKey, byte[] iv, String algorithm, String transform, File inFile, File outFile) {
		//Encrypt/decrypt
		byte[] ivout = null;
		try {
	        Cipher cipher = Cipher.getInstance(transform);
			if (cipherMode == 1) { //for encryption
	       		cipher.init(cipherMode, secretKey);
			}
			else if (cipherMode == 2) { //for decryption
				IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
	       		cipher.init(cipherMode, secretKey, ivParameterSpec);
			}
			
			FileInputStream fis = new FileInputStream(inFile);
			FileOutputStream fos = new FileOutputStream(outFile);
			CipherOutputStream cos = new CipherOutputStream(fos , cipher);

			try {
			    byte[] buffer = new byte[8192];
			    int count;
			    while ((count = fis.read(buffer)) > 0) {
			        cos.write(buffer, 0, count);
			    }
			} finally {
			    cos.close();
			    fis.close();
			}
			if (cipherMode == 1) {
				ivout = cipher.getIV();
				return ivout;
			}
		} catch(GeneralSecurityException e1) {
			throw new IllegalStateException("Error during encryption/decryption", e1);
		} catch(IOException e2) {
		}
		return ivout;
	} //end cryptor
} //end class