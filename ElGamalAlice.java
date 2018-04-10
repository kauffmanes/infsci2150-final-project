import java.io.*;
import java.net.*;
import java.security.*;
import java.math.BigInteger;
import java.nio.charset.*;

/*
* Authors: Jeffrey James & Emily Kauffman
* IS2150, Spring 2018, University of Pittsburgh
*
* Implements El Gamal key exchange
* Run ElGamalBob.java first, then start ElGamalAlice.java
*/

public class ElGamalAlice
{
	private static BigInteger computeY(BigInteger p, BigInteger g, BigInteger d)
	{
		// compute y = g^d mod p
		BigInteger y = g.modPow(d, p);
		return y;
	}

	private static BigInteger computeK(BigInteger p, int mStrength)
	{
		BigInteger pMin = p.subtract(BigInteger.ONE); // (p-1)
		BigInteger ktemp = BigInteger.ZERO;
		BigInteger k = BigInteger.ZERO;
		// compute 0 < k < p-1 with gcd(k, p-1) = 1
		while (!ktemp.equals(BigInteger.ONE)) {
			SecureRandom nSecureRandom = new SecureRandom();
			k = new BigInteger(mStrength, nSecureRandom);
			ktemp = k.gcd(pMin);
		}
		return k;
	}
	
	private static BigInteger computeA(BigInteger p, BigInteger g, BigInteger k)
	{
		// compute a = g^k mod p
		BigInteger a = g.modPow(k, p);
		return a;
	}

	private static BigInteger computeB(	String message, BigInteger d, BigInteger a, BigInteger k, BigInteger p)
	{
		// compute message hash
		byte[] hash;
		try {
			MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
			hash = sha256.digest(message.getBytes(StandardCharsets.UTF_8));
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
		BigInteger message_hash = new BigInteger(hash);
		// compute b = (message-da)k^-1 mod(p-1)
		BigInteger b = message_hash.subtract(d.multiply(a)); // (message-(d*a))
		BigInteger pMin = p.subtract(BigInteger.ONE); // (p-1)
		b = b.multiply(k.modInverse(pMin)); // b*(k^-1 mod (p-1))
		b = b.mod(pMin); // mod (p-1)
		return b;
	}

	public static void main(String[] args) throws Exception 
	{
		String message = "The quick brown fox jumps over the lazy dog.";

		//String host = "paradox.sis.pitt.edu";
		String host = "127.0.0.1";
		int port = 7999;
		Socket s = new Socket(host, port);
		ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());

		// You should consult BigInteger class in Java API documentation to find out what it is.
		BigInteger y, g, p; // public key
		BigInteger d; // private key

		int mStrength = 1024; // key bit length
		SecureRandom mSecureRandom = new SecureRandom(); // a cryptographically strong pseudo-random number

		// Create a BigInterger with mStrength bit length that is highly likely to be prime.
		// (The '16' determines the probability that p is prime. Refer to BigInteger documentation.)
		p = new BigInteger(mStrength, 16, mSecureRandom);
		
		// Create a randomly generated BigInteger of length mStrength-1
		g = new BigInteger(mStrength-1, mSecureRandom);
		d = new BigInteger(mStrength-1, mSecureRandom);

		y = computeY(p, g, d);

		// At this point, you have both the public key and the private key. Now compute the signature.
		BigInteger k = computeK(p, mStrength);
		BigInteger a = computeA(p, g, k);
		BigInteger b = computeB(message, d, a, k, p);

		// send public key
		os.writeObject(y);
		os.writeObject(g);
		os.writeObject(p);

		// send message
		os.writeObject(message);
		
		// send signature
		os.writeObject(a);
		os.writeObject(b);
		System.out.println("... Objects written");
		
		s.close();
	}
}