package com.OrangeHRM.browsers;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class ChromeWebDriver implements Driver {

	/*
	 * This method provides an implementation of getDriver() method for chrome
	 */

	protected Logger log = LogManager.getLogger();

	public  WebDriver getDriver() {
		log.info("[ Initializing chrome driver ]");
		
		
		  //Convensional way to automate chromedriver by giving path of driver
		 // String chromePath = System.getProperty("user.dir") + "/drivers/chromedriver.exe";
		  //System.setProperty("webdriver.chrome.driver", chromePath);
		 

		//WebDriverManager.chromedriver().setup(); --> use it with older selenium version and bonegarsia , To automate Driver
		
	
		
		  ChromeOptions options = new ChromeOptions();
		  options.addArguments("--remote-allow-origins=*");
		  Map<String, Object> preferences = new HashMap<>();
		  preferences.put("plugins.plugins_disabled",new String[] { "Chrome PDF Viewer" });
		  preferences.put("plugins.always_open_pdf_externally", true);
		  options.setExperimentalOption("prefs", preferences);
		 
		WebDriver driver = new ChromeDriver(options);
		return driver;
		//return new ChromeDriver();  
	}    
	

}
