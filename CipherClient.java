/**
 * CipherClient.java
 * Authors: Emily Kauffman & Jeffrey James
 * IS2150, Spring 2018, University of Pittsburgh
 * 
 * Question Two: In this part of the assignment, the client program CipherClient should (1) generate a DES key and stores the key in a file, (2) encrypts the given
 * String object using that key and sends the encrypted object over the socket to the server. The server program CipherServer then uses the key that was previously
 * generated by the client to decrypt the incoming object. The server obtains the key by reading it from the same file that the client previously generated. The
 * server should then print out the decypted message. For this part of the assignment, you will need to consult external sources and documentations on how to
 * generate   a DES key, write to or read from a file, and perform encryption/decryption of an object.
 * 
 * References:
 * 
 * TODO
 * Fix encoding issues - decode and encode using same algorithm
 * 
 */
import java.io.*;
import java.net.Socket;
import java.util.*;
import javax.crypto.*;
import java.security.*;

public class CipherClient
{
    /**
     * Generates a secret key using DES
     */
    private static SecretKey generateDESKey () throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("DES");
        return kgen.generateKey();
    }

    /**
     * Base64 encodes the SecretKey (to avoid issues with special characters)
     * and then saves the key to a file.
     */
    private static void saveKey (SecretKey key) {

        String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());
        PrintWriter out = null;

        try {
            out = new PrintWriter("id_des");
            out.println(encodedKey);
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (out != null) { out.close(); }
            else { System.out.println("PrintWriter not open..."); }
        }

    }

    /**
     * Encrypts provided data using the accompanying SecretKey using DES and returns
     * encrypted data as a String.
     */
    private static String encryptData(String data, SecretKey key) throws Exception {

        byte[] unencryptedMessage = data.getBytes();
        byte[] encryptedMessage;

        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding"); //type, mode, padding
        
        cipher.init(Cipher.ENCRYPT_MODE, key); //ENCRYPT_MODE - Constant used to initialize cipher to encryption mode.
        encryptedMessage = cipher.doFinal(unencryptedMessage);
        
        return new String(encryptedMessage);
    }

    public static void main (String[] args) throws Exception
    {
        Scanner scan = new Scanner(System.in);
        String data; //unencrypted message
        SecretKey key;
        String encryptedData; //processe data - is now encrypted

        // Get data to encrypt
        System.out.println("Enter a message to decrypt and hit enter...");
        data = scan.nextLine();
        scan.close();

        // Handle key generation/encoding/save
        key = generateDESKey(); // Generate the DES key
        saveKey(key); // Save the key to a file for server to use later
        encryptedData = encryptData(data, key); // Encrypts a given string object using that key

        //todo - take this out then
        System.out.println("Sending encrypted data: '" + encryptedData + "'");

        //Connect to the open server socket
        //Send encrypted object over the socket to the server
        String host = "127.0.0.1";
        int port = 7999;
        Socket sock = new Socket(host, port);
        ObjectOutputStream outStream = new ObjectOutputStream(sock.getOutputStream());
        
        outStream.writeObject(encryptedData);
        sock.close();
                                                                                                 
    }

}