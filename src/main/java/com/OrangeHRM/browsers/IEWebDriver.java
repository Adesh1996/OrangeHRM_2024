package com.OrangeHRM.browsers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

public class IEWebDriver implements Driver {

	/*
	 * This method provides an implementation of getDriver() method for Internet Explorer Browser
	 */
	
	protected Logger log= LogManager.getLogger();
	
	public WebDriver getDriver() {
		log.info("[ Initializing Internet Explorar driver ]");
		String iePath = System.getProperty("user.dir") + "/drivers/IEDriverServer.exe";
		System.setProperty("webdriver.edge.driver", iePath);
		return new InternetExplorerDriver();
	}
}
