/**
 * MessageDigestDemo.java
 * Authors: Emily Kauffman & Jeffrey James
 * IS2150, Spring 2018, University of Pittsburgh
 * 
 * Question One: Write a program to demonstrate the use of hashing using MD5 and SHA scheme and the MessageDigest class.
 * Your program should allow a user to enter a string; the program then hashes it and outputs the result.
 * You will need to consult Java API documentation to learn how to use Java classes.
 * 
 * You can download and install the documentation yourself, or you can access them from this URL:
 * http://java.sun.com/j2se/1.4.2/docs/api/index.html
 * 
 * References:
 *   https://docs.oracle.com/javase/7/docs/api/index.html?java/security/MessageDigest.html
 *   https://examples.javacodegeeks.com/core-java/security/java-security-messagedigest-example/
 */
import java.util.Scanner;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MessageDigestDemo
{
    /**
     * Takes some data, hashes it using the supplied algorithm, and then returns the hash.
     */
    private static StringBuffer hashString (String data, String algorithm) throws NoSuchAlgorithmException {
        
        MessageDigest md = MessageDigest.getInstance(algorithm.toUpperCase());
        md.update(data.getBytes());
        byte[] digest = md.digest();
        StringBuffer hexStr = new StringBuffer();
        
        for (byte bytes : digest) {
            hexStr.append(String.format("%02x", bytes & 0xff));
        }

        return hexStr;

    }

    public static void main (String[] args) throws NoSuchAlgorithmException 
    {
        Scanner scan = new Scanner(System.in);
        String data; //message to be passed in from user

        //Takes in user input to be hashed
        System.out.println("Enter a string to hash.");
        data = scan.nextLine();
        scan.close();

        //Prints the data according to selected algorithm
        System.out.println("String to hash: " + data);
        System.out.println("MD5:\t\t" + hashString(data, "MD5"));
        System.out.println("SHA1:\t\t" + hashString(data, "SHA"));
        System.out.println("SHA-256:\t" + hashString(data, "SHA-256"));

    }
}