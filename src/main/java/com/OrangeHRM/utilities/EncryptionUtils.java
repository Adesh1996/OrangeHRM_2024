package com.OrangeHRM.utilities;

import java.util.Base64;

public class EncryptionUtils {

	public static void main(String[] args) throws InterruptedException {
		String token = "admin123";
		
		String encodedString = new String(Base64.getEncoder().encode(token.getBytes()));
		System.out.println(encodedString);
		String decodedString = new String(Base64.getDecoder().decode(encodedString));
		System.out.println(decodedString);
		System.out.println(encodedString(token));
	}
	
	
	/**
	 * This method takes the string as a input and 
	 * converts it into encoded format.
	 * @param string token
	 * @return encodedString
	 * 
	 */
	public static String encodedString(String token) {
		String encodedString = new String(Base64.getEncoder().encode(token.getBytes()));
		return encodedString;
	}
	
	/**
	 * This method takes the encoded string as a input and 
	 * converts it into decoded format.
	 * @param encodedString
	 * @return decodedString
	 * 
	 */
	public static String decodedString(String encodedString) {
		String decodedString = new String(Base64.getDecoder().decode(encodedString));
		return decodedString;
	}
	
}
