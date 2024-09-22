package com.OrangeHRM.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class AppConfig {
	
	private AppConfig() {
		throw new IllegalStateException("Utility Class");
	}
	
	/*
	 * This method is used to read the property file
	 * 
	 * @return property file object
	 */
	
	public static Properties propertyReader() {
		FileReader reader;
		Properties prop = new Properties();
		
		try {
			reader = new FileReader("application.properties");
			prop.load(reader);
		}
		catch(IOException e) {
			Logger.getLogger(AppConfig.class.getName()).log(Level.SEVERE, "The properties file could not be loaded",e);
		}
		return prop;
	}
	
	
	public static void setProperty(String key, String value, String fileLocation) {
		FileOutputStream OutputStream = null;
		FileInputStream InputStream = null;
		Properties prop = null;
		try {
			prop = new Properties();
			File file = new File(fileLocation);
			file.createNewFile();
			InputStream = new FileInputStream(file);
			prop.load(InputStream);
			OutputStream = new FileOutputStream(fileLocation);
			prop.setProperty(key, value);
			prop.store(OutputStream, null);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				InputStream.close();
				OutputStream.close();
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public static String getProperty(String key, String fileLocation) {
		FileInputStream InputStream = null;
		Properties prop = null;
		String value = null;
		try {
			prop = new Properties();
			InputStream = new FileInputStream(fileLocation);
			prop.load(InputStream);
			value = prop.getProperty(key);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				InputStream.close();
			}  
			catch(IOException e) {
				e.printStackTrace();
			}
		}
		return value;
	}
	
}
