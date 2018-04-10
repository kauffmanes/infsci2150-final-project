import java.util.Scanner;
import java.io.*;

/*
* Authors: Jeffrey James & Emily Kauffman
* IS2150, Spring 2018, University of Pittsburgh
*
* Implements a simple shift cipher in encryption or decryption mode
* User inputs mode, shift key, and text file name
* Output is ciphertext/plaintext to text file and screen
*/

public class ShiftCipherFile {
	public static void main(String[] args) {
		// Initialize plaintext alphabet
		char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
		
		// Get user input
		System.out.println("Encrypt or Decrypt - E/D:");
		Scanner in1 = new Scanner(System.in);
		String response = in1.next();
		char x = Character.toUpperCase(response.charAt(0));
		System.out.println("Enter shift key:");
		Scanner in2 = new Scanner(System.in);
		int knum = 0;
		
		// Validate shift key
		if(in2.hasNextInt()) {
			knum = in2.nextInt();
		}
		else {
			System.out.println();
			System.out.println("Invalid shift! Key between 1 & 25 needed!");
			System.out.println();
			return;
		}
		if (knum < 1 || knum > 25) {
			System.out.println();
			System.out.println("Invalid shift! Key between 1 & 25 needed!");
			System.out.println();
			return;
		}
		
		// Create cipher alphabet
		char[] cipheralphabet = new char[26];
		for (int j = 0; j < cipheralphabet.length; j++)
			cipheralphabet[j] = alphabet[(j+knum)%26];
		
		// Send input to encryption or decryption method
		Scanner in3;
		String input3;
		String plaintext = null;
		String ciphertext = null;
		int textlength;
		File inFile;
		switch(x) {
			case 'E': //get ciphertext
				System.out.println("Full name of file to encrypt:");
				in3 = new Scanner(System.in);
				input3 = in3.nextLine();
				inFile = new File(input3);
				String outFileE = getOutFile(inFile, 1);
				try {
				plaintext = readFile(inFile);
				}
				catch(IOException ex) {
					ex.printStackTrace();
				}
				System.out.println();
				//encrypt
				ciphertext = encrypt(plaintext, knum, alphabet);
				System.out.println("ciphertext: " + ciphertext);
				//write out file
				writeFile(ciphertext, outFileE);
				break;
			case 'D': //get plaintext
				System.out.println("Full name of file to decrypt:");
				in3 = new Scanner(System.in);
				input3 = in3.nextLine();
				inFile = new File(input3);
				String outFileD = getOutFile(inFile, 0);
				try {
				ciphertext = readFile(inFile);
				}
				catch(IOException ex) {
					ex.printStackTrace();
				}
				System.out.println();
				//decrypt
				plaintext = decrypt(ciphertext, knum, cipheralphabet);
				System.out.println("plaintext: " + plaintext);
				//write out file
				writeFile(plaintext, outFileD);
				break;
			default:
				System.out.println("Incorrect input - E/D");
				break;
		}
	} //end main
	
	// Output file naming method
	private static String getOutFile(File inFile, int mode) {
		String input = inFile.toString();
		String ext = "";
		String base = inFile.getName();
		int i = input.lastIndexOf('.');
		String outFile = "_";
		if (i > 0) {
	    	ext = input.substring(i+1);
			base = base.substring(0, i);
			if (mode == 1)
				outFile = base + "_e." + ext;
			else if (mode == 0)
				outFile = base + "_d." + ext;
		}
		else {
			if (mode == 1)
				outFile = base + "_e";
			else if (mode == 0)
				outFile = base + "_d";
		}
		return outFile;
	} //end getOutFile
	
	// Read file into String method
	private static String readFile(File inFile) throws IOException {
		StringBuilder fileContents = new StringBuilder((int)inFile.length());
		String lineSeparator = System.getProperty("line.separator");
		String input = inFile.toString();
		String line = null;
		try {
			FileReader fileReader = new FileReader(input);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

		    while((line = bufferedReader.readLine()) != null) {
				fileContents.append(line + lineSeparator);
		}
		bufferedReader.close();
		}
		catch(FileNotFoundException ex) {
			System.out.println("Unable to open file '" + input + "'");                
		}
		catch(IOException ex) {
		            ex.printStackTrace();
		}
		return fileContents.toString();
	}
	
	// Write file to system method
	private static void writeFile(String ciphertext, String outFile) {
		try {
			FileWriter fileWriter = new FileWriter(outFile);
		    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(ciphertext);
            bufferedWriter.close();
			System.out.println();
			System.out.println("Output written to: " + outFile);
		}
		catch(IOException ex) {
			ex.printStackTrace();
		}
	}
	
	// Encryption method
	private static String encrypt(String plaintext, int shiftkey, char[] alphabet) {
		StringBuilder ciphertext = new StringBuilder();
		char c;
		String ch, ach;
		System.out.println("plaintext: " + plaintext);
		int length = plaintext.length();
		for (int i = 0; i < length; i++) {
			c = plaintext.charAt(i);
			if (Character.isLetter(c)) { //encrypt only if c is letter
				c = Character.toUpperCase(c);
				ch = Character.toString(c);
				for (int j = 0; j < alphabet.length; j++) {
					ach = Character.toString(alphabet[j]);
					if (ch.equals(ach)) {
						int r = (j+shiftkey)%26; //encryption algorithm
						String cha = Character.toString(alphabet[r]);
						c = cha.charAt(0);
					}
		    	}
			}
			if (Character.isLetter(c) || c == ' ')
				ciphertext.append(c); //return upper-case characters
		}
		return ciphertext.toString();
	} //end encrypt
	
	// Decryption method
	private static String decrypt(String ciphertext, int shiftkey, char[] alphabet) {
		StringBuilder plaintext = new StringBuilder();
		char c;
		String ch, ach;
		System.out.println("ciphertext: " + ciphertext);
		int length = ciphertext.length();
		for (int i = 0; i < length; i++) {
			c = ciphertext.charAt(i);
			if (Character.isLetter(c)) { //decrypt only if c is letter
				c = Character.toUpperCase(c);
				ch = Character.toString(c);
				for (int j = 0; j < alphabet.length; j++) {
					ach = Character.toString(alphabet[j]);
					if (ch.equals(ach)) {
						int r = (j-shiftkey)%26; //decryption algorithm
						if (r<0)
							r = r + 26;
						String cha = Character.toString(alphabet[r]);
						c = cha.charAt(0);
					}
				}
		    }
			if (Character.isLetter(c) || c == ' ')
				plaintext.append(c); //return upper-case characters
		}
		return plaintext.toString();
	} //end decrypt
} //end ShiftCipherFile