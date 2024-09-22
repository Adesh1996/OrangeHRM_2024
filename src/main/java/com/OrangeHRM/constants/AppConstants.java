 package com.OrangeHRM.constants;

import com.OrangeHRM.utilities.AppConfig;

/*
 * This class sets all the given properties to constants
 */

public final class AppConstants {

	private AppConstants() {
		throw new IllegalStateException("AppConstants Class");
	}

	private static String applicationURL = null;

	public static final String USER_DIRECTORY = System.getProperty("user.dir");
	public static final String APPLICATION_ENV = AppConfig.propertyReader().getProperty("application.env");
	public static final String UPLOAD_FOLDER_PATH = System.getProperty("user.dir") + "\\test-data\\upload\\";
	public static final String DOWNLOAD_FOLDER_PATH = System.getProperty("user.home") + "/Downloads/";

	public static final String BROWSER_TYPE = AppConfig.propertyReader().getProperty("browser.type");
	public static final String TESTRAIL_BASE_URL = AppConfig.propertyReader().getProperty("testrail.baseurl");
	public static final Boolean TESTRAIL_UPDATE_FLAG = Boolean
			.valueOf(AppConfig.propertyReader().getProperty("testrail.update.flag"));
	public static final String TESTRAIL_RUNID = AppConfig.propertyReader().getProperty("testrail.runid");
	public static final String TESTRAIL_USERNAME = AppConfig.propertyReader().getProperty("testrail.username");
	public static final String TESTRAIL_APIKEY = AppConfig.propertyReader().getProperty("testrail.apikey");

	// Timeouts
	public static final int PAGE_LOAD_TIMEOUT = Integer
			.parseInt(AppConfig.propertyReader().getProperty("pageLoad.timeout"));
	
	public static final int FRAME_LOAD_TIMEOUT = Integer
			.parseInt(AppConfig.propertyReader().getProperty("frameLoad.timeout"));
	
	public static final int POPUP_TIMEOUT = Integer.parseInt(AppConfig.propertyReader().getProperty("popup.timeout"));
	public static final int SCRIPT_TIMEOUT = Integer.parseInt(AppConfig.propertyReader().getProperty("script.timeout"));
	public static final int WAIT_TIMEOUT = Integer.parseInt(AppConfig.propertyReader().getProperty("wait.timeout"));
		
	
	//Properties read and store it in variable : used with bat file
	public static final String APPLICATION_CLIENT = System.getProperty("clientName");
	
	//Reports Path
	public static final String QUERY_FILE_PATH =System.getProperty("user.dir") + "src/test/resorces/DBQueries/";
	
	//Runtime Properties
	public static final String RUNTIME_PROPERTY_PATH = System.getProperty("user.dir")+ 
			"/src/test/resources/Runtime.properties";
	
	
	//set application URL
	public static String getApplicationURL() {
		switch(APPLICATION_ENV.toLowerCase())
		{
		case "orangehrm" :
			applicationURL = AppConfig.propertyReader().getProperty("application.url.orangeHRM");
			break;
			
		case "orangehrmuat" :
			applicationURL = AppConfig.propertyReader().getProperty("application.url.orangeHRMUAT");
			break;
		}
		return applicationURL;
	}
	
	
	// Web Services constants
	public static final String SERVICE_BASE_URL_ORANGEHRM = AppConfig.propertyReader().getProperty("service.baseurl.orangehrm");
	public static final int SERVICE_PORT_ORANGEHRM =Integer.parseInt(AppConfig.propertyReader().getProperty("port.orangehrm"));

	public static final String SERVICE_BASE_URL_ORANGEHRMUAT = AppConfig.propertyReader().getProperty("service.baseurl.orangehrmUAT");
	public static final int SERVICE_PORT_ORANGEHRMUAT =Integer.parseInt(AppConfig.propertyReader().getProperty("port.orangehrmUAT"));
	
	//Set DB properties 
	public static final String orangeHrm_DB = AppConfig.propertyReader().getProperty("orangeHrm.database");
	public static final String orangeHrmUAT_DB = AppConfig.propertyReader().getProperty("orangeHrm.database.UAT");
}
