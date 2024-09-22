package com.OrangeHRM.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.testng.Assert;

import com.OrangeHRM.constants.AppConstants;

public class FileUtils {

	private FileUtils() {
	throw new IllegalStateException("Utility Class");
	}
	
	
	public static String readFileAsString(String filePath) {
		
		File file = new File (System.getProperty("user.dir") + "src/test/resources/DBQueries/" + filePath);
		
		try(BufferedReader br = new BufferedReader(new FileReader(file))){
			String line = null;
			StringBuilder sb = new StringBuilder();
			while((line=br.readLine()) != null) {
				sb.append(line + " ");
			}
			return new String(sb);
		}
		catch(Exception e) {
			Assert.fail(e.getLocalizedMessage());
			return null;
		}
	}
	
	/**
	 * This method reads SQL query from the given text file and returns it as
	 * String to be used by DBUtils class
	 * 
	 * @param queryFileName
	 * 			- name of the query file with extension '.txt'
	 * @return 
	 * 
	 * @return 
	 */
	public String readQueryFileAsString(String queryFileName) {
		File file = new File(AppConstants.QUERY_FILE_PATH + queryFileName);
		
		try(BufferedReader br = new BufferedReader(new FileReader(file))){
			String line = null;
			StringBuilder sb = new StringBuilder();
			while((line=br.readLine()) != null) {
				sb.append(line + " ");
			}
			return new String(sb);
		}
		catch(Exception e) {
			Assert.fail(e.getLocalizedMessage());
			return null;
		}
	}
	
	
}
