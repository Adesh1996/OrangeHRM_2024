package com.OrangeHRM.browsers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class FirefoxWebDriver implements Driver {

	/*
	 * This method provides an implementation of getDriver() method for Firefox
	 */

	protected Logger log = LogManager.getLogger();

	public WebDriver getDriver() {
		log.info("[ Initializing FireFox driver ]");
		String firefoxPath = System.getProperty("user.dir") + "/drivers/geckodriver.exe";
		System.setProperty("webdriver.gecko.driver", firefoxPath);

		FirefoxOptions options = new FirefoxOptions()
				.addPreference("plugins.plugins_disabled", new String[] { "Chrome PDF Viewer" })
				.addPreference("plugins.always_open_pdf_externally", true);

		return new FirefoxDriver(options);
	}
}
