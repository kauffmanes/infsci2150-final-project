import java.util.Scanner;
import java.util.Arrays;
import java.text.NumberFormat;

/*
* Authors: Jeffrey James & Emily Kauffman
* IS2150, Spring 2018, University of Pittsburgh
*
* Implements a cryptanalysis of a shift cipher
* User inputs a ciphertext to analyze
* Output consists of top 7 likely keys, with their plaintexts, 
* according to frequency analysis
*/

public class ShiftCipherCryptanalysis {
	public static void main(String[] args) {
		// Get user input
		System.out.println("Enter ciphertext to analyze:");
		Scanner in = new Scanner(System.in);
		String text = in.nextLine();
		int textlength = text.length();
		System.out.println();
		// Run analysis
		cryptanalysis(text, textlength);
	}
	
	// Cryptanalysis method
	private static void cryptanalysis(String ciphertext, int length) {
		// Initialize plaintext alphabet
		char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
		// Initialize frequency of English alphabets
		Double[] alphafreq = {0.08,0.015,0.03,0.04,0.13,0.02,0.015,0.06,0.065,
			0.005,0.005,0.035,0.03,0.07,0.08,0.02,0.002,0.065,0.06,0.09,0.03,
			0.01,0.015,0.005,0.02,0.002};
		double[] alphacount = new double[26];
		double[] freqcorr = new double[26];
		double[] totfreqcorr = new double[26];
		char a, c;
		String ch, ach;
		int count = 0;
		double counttext = 0.0;
		
		// Count characters in ciphertext
		for (int i = 0; i < 26; i++) {
			a = alphabet[i];
			for (int j = 0; j < length; j++) {
				c = Character.toUpperCase(ciphertext.charAt(j));
				ch = Character.toString(c);
				ach = Character.toString(alphabet[i]);
				if (Character.isLetter(c)) {
					if (ch.equals(ach))
						count++;
				}
			}
			alphacount[i] = (double) count;
			count = 0;
		}
		for (int n = 0; n < alphacount.length; n++) {
			counttext = counttext + alphacount[n];
		}
		
		// Calculate frequency correlation for each key
		int step;
		double totfreq = 0.0;
		for (int key = 0; key < 26; key ++) {
			for (int k = 0; k < 26; k++) {
				step = (k - key)%26;
				if (step < 0)
					step = step + 26;
				freqcorr[k] = (alphacount[k]/counttext)*alphafreq[step]; //f(c)*p(x)
			}
			for (int m = 0; m < 26; m++) {
				totfreq = totfreq + freqcorr[m]; //freq correlation for each key
			}
			totfreqcorr[key] = totfreq;
			totfreq = 0.0;
		}
		
		// Sort by frequency correlation and print top 7
		double[] stotfreqcorr = totfreqcorr.clone();
		Arrays.sort(stotfreqcorr); //sort ascending
		String stot, tot;
		int index = 1;
		for (int p = 25; p > 18; p--) { //only top 7 results
			for (int q = 0; q < 26; q++) {
				stot = Double.valueOf(stotfreqcorr[p]).toString();
				tot = Double.valueOf(totfreqcorr[q]).toString();
				if (stot.equals(tot)) {
					System.out.println("("+index+") key i=" + q + ", \u03D5(i)=" + String.format("%.4f", stotfreqcorr[p]));
					System.out.println("plaintext: " + decrypt(ciphertext,length,q,alphabet)); //decrypt
					System.out.println();
					index ++;
				}
			}
		}
	} //end cryptanalysis
	
	// Decryption method
	private static String decrypt(String ciphertext, int length, int shiftkey, char[] alphabet) {
		char[] cipheralphabet = new char[26];
		StringBuilder plaintext = new StringBuilder();
		char c;
		String ch, ach;
		
		// Calculate cipher alphabet			
		for (int n = 0; n < cipheralphabet.length; n++) {
			cipheralphabet[n] = alphabet[(n + shiftkey)%26];
		}

		for (int i = 0; i < length; i++) {
			c = ciphertext.charAt(i);
			if (Character.isLetter(c)) { //decrypt only if c is letter
				c = Character.toUpperCase(c);
				ch = Character.toString(c);
				for (int j = 0; j < cipheralphabet.length; j++) {
					ach = Character.toString(cipheralphabet[j]);
					if (ch.equals(ach)) {
						int r = (j-shiftkey)%26; //decryption algorithm
						if (r<0)
							r = r + 26;
						String cha = Character.toString(cipheralphabet[r]);
						c = cha.charAt(0);
					}
				}
		    }
			if (Character.isLetter(c) || c == ' ')
				plaintext.append(c); //return upper case characters
		}
		return plaintext.toString();
	} //end decrypt
} //end ShiftCipherCryptanalysis