package com.OrangeHRM.browsers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;



public class HeadlessDriver implements Driver {

	/*
	 * This method provides an implementation of getDriver() method for Headless
	 * Chrome Browser
	 */

	protected Logger log = LogManager.getLogger();

	public WebDriver getDriver() {
		log.info("[ Initializing Headless chrome driver ]");
//		String chromePath = System.getProperty("user.dir") + "/drivers/chromedriver.exe";
//		System.setProperty("webdriver.chrome.driver", chromePath);
		
		
		ChromeOptions options = new ChromeOptions();
		options.merge(options);
		//options.addArguments("--no-sandbox");
		options.addArguments("--headless=new");
//		options.addArguments("disable-gpu");
//		options.addArguments("--proxy-server='direct://'");
//		options.addArguments("--proxy-bypass-list=*");
		options.addArguments("--window-size=1300,1000");
//		options.addArguments("--no-default-browser-check");
//		options.addArguments("--no-proxy-server");
		
		WebDriver driver = new ChromeDriver(options);

		return driver;
	}
}
