package com.OrangeHRM.browsers;

import org.openqa.selenium.WebDriver;

public class DriverFactory {

	private DriverFactory() {
		
		throw new IllegalStateException("Driver Factory Class");
	}
	
	/*
	 * This method returns the Webdiver instance of a given browser type Supported
	 * browser are : Chrome, Firefox, Edge, Internet Explorar 
	 */
	
	public static WebDriver getDriver(String browserType) {
		
		if (browserType != null) {
			if (browserType.equalsIgnoreCase("firefox"))
				return new FirefoxWebDriver().getDriver();
			if(browserType.equalsIgnoreCase("edge"))
				return new EdgeWebDriver().getDriver();
			if(browserType.equalsIgnoreCase("ie"))
				return new IEWebDriver().getDriver();
			if(browserType.equalsIgnoreCase("headless"))
				return new HeadlessDriver().getDriver();
		}
		return new ChromeWebDriver().getDriver();
		
	}

}
