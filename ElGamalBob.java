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

public class ElGamalBob
{
	private static boolean verifySignature(	BigInteger y, BigInteger g, BigInteger p, BigInteger a, BigInteger b, String message)
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
		
		// verify signature y^a * a^b mod p = g^m mod p
		boolean verify = false;
		BigInteger left = y.modPow(a, p);
		left = left.multiply(a.modPow(b, p));
		left = left.mod(p);
		BigInteger right = g.modPow(message_hash, p);
		if (left.equals(right)) {
			verify = true;
		}
		return verify;
	}

	public static void main(String[] args) throws Exception 
	{
		int port = 7999;
		ServerSocket s = new ServerSocket(port);
		System.out.println("... waiting for Alice to connect ...");
		Socket client = s.accept();
		ObjectInputStream is = new ObjectInputStream(client.getInputStream());

		// read public key
		BigInteger y = (BigInteger)is.readObject();
		BigInteger g = (BigInteger)is.readObject();
		BigInteger p = (BigInteger)is.readObject();

		// read message
		String message = (String)is.readObject();

		// read signature
		BigInteger a = (BigInteger)is.readObject();
		BigInteger b = (BigInteger)is.readObject();

		boolean result = verifySignature(y, g, p, a, b, message);

		System.out.println(message);

		if (result == true)
			System.out.println("Signature verified.");
		else
			System.out.println("Signature verification failed.");

		s.close();
	}
}