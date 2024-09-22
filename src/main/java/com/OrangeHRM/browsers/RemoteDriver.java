package com.OrangeHRM.browsers;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;

public class RemoteDriver implements Driver{
	
	/**
	 * This method provides an implementation of getDriver() method for 
	 * RemoteWebDriver used for selenium Grid
	 * 
	 * @author Adesh.Khedkar
	 */

	protected Logger log = LogManager.getLogger();

	public WebDriver getDriver() {
		
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setBrowserName("chrome");
		capabilities.setPlatform(Platform.WINDOWS);
		
		try {
			log.info("[ Initializing remote driver ]");
			URL url = new URL("http://172.17.0.2:4444/wd/hub"); 
			return new RemoteWebDriver(url, capabilities);
		}
		catch(MalformedURLException e) {
			Assert.fail("Remote Server URL is not correct", e.getCause());
		}
		catch(Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
		
		return null;


	}
	
	
}
