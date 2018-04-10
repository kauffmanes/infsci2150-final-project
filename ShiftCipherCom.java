import java.util.Scanner;

public class ShiftCipherCom {
	public static void main(String[] args) {
		// Initialize plaintext alphabet
		String[] alphabet = {"A","B","C","D","E","F","G","H","I","J","K","L",
			"M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
		
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
		String[] cipheralphabet = new String[26];
		for (int j = 0; j < cipheralphabet.length; j++)
			cipheralphabet[j] = alphabet[(j+knum)%26];
		
		// Send input to encryption or decryption method
		Scanner in3;
		String text;
		int textlength;
		switch(x) {
			case 'E': //get ciphertext
				System.out.println("Enter message to encrypt:");
				in3 = new Scanner(System.in);
				text = in3.nextLine();
				textlength = text.length();
				System.out.println();
				System.out.println("ciphertext: " + encrypt(text,textlength,knum,alphabet));
				System.out.println();
				break;
			case 'D': //get plaintext
				System.out.println("Enter message to decrypt:");
				in3 = new Scanner(System.in);
				text = in3.nextLine();
				textlength = text.length();
				System.out.println();
				System.out.println("plaintext: " + decrypt(text,textlength,knum,cipheralphabet));
				System.out.println();
				break;
			default:
				System.out.println("Incorrect input - E/D");
				break;
		} //end switch
	} //end main
	
	// Encryption method
	private static String encrypt(String plaintext, int length, int shiftkey, String[] alphabet) {
		StringBuilder ciphertext = new StringBuilder();
		char c;
		for (int i = 0; i < length; i++) {
			c = plaintext.charAt(i);
			if (Character.isLetter(c)) { //encrypt only if c is letter
				c = Character.toUpperCase(c);
				String ch = Character.toString(c);
				for (int j = 0; j < alphabet.length; j++) {
					if (ch.equals(alphabet[j])) {
						int r = (j+shiftkey)%26; //encryption algorithm
						String cha = alphabet[r];
						c = cha.charAt(0);
					} //end if
		    	} //end for
			} //end if
			if (Character.isLetter(c) || c == ' ') //ignore non-letter characters
				ciphertext.append(c);
		} //end for
		return ciphertext.toString();
	} //end encrypt
	
	// Decryption method
	private static String decrypt(String ciphertext, int length, int shiftkey, String[] alphabet) {
		StringBuilder plaintext = new StringBuilder();
		char c;
		for (int i = 0; i < length; i++) {
			c = ciphertext.charAt(i);
			if (Character.isLetter(c)) { //decrypt only if c is letter
				c = Character.toUpperCase(c);
				String ch = Character.toString(c);
				for (int j = 0; j < alphabet.length; j++) {
					if (ch.equals(alphabet[j])) {
						int r = (j-shiftkey)%26; //decryption algorithm
						if (r<0)
							r = r + 26;
						String cha = alphabet[r];
						c = cha.charAt(0);
					} //end if
				} //end for
		    } //end if
			if (Character.isLetter(c) || c == ' ') //ignore non-letter characters
				plaintext.append(c); //return upper case characters
		} //end for
		return plaintext.toString();
	} //end decrypt
} //end ShiftCipher