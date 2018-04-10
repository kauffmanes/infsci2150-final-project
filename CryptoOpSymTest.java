import java.util.Scanner;
import java.io.*;

/**
 * A test for the CryptoOpSym class.
 * Author: Jeffrey A. James
 * April 2017
*/

public class CryptoOpSymTest {
	public static void main(String[] args) {
		//Algorithms: AES, DES, DESede, Blowfish
		//Transformations: /CBC/NoPadding, /CBC/PKCS5Padding, /ECB/NoPadding, /ECB/PKCS5Padding
		
		//Initialization parameters
		int keySize = 128; //128, 192, or 256-bit 
		String algorithm = "AES";
		//String transform = "AES/CBC/PKCS5Padding";
		String transform = "AES/CBC/NoPadding";
		//int keySize = 128; //32-448 bit
		//String algorithm = "Blowfish";
		//String transform = "Blowfish/CBC/PKCS5Padding";
		//int keySize = 64; //64-bit
		//String algorithm = "DES";
		//String transform = "DES/CBC/PKCS5Padding";
		//int keySize = 192;
		//String algorithm = "DESede";
		//String transform = "DESede/CBC/PKCS5Padding";

		//Get file from user
		System.out.println("Full name of file to encrypt/decrypt:");
		Scanner in2 = new Scanner(System.in);
		String input2 = in2.nextLine();
		File inFile = new File(input2);
		
		//Encrypt and then decrypt file
		CryptoOpSym.compute(keySize, algorithm, transform, inFile);
	}
}