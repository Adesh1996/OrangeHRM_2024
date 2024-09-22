package com.OrangeHRM.browsers;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;

import com.OrangeHRM.utilities.AppiumServer;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

public class AndroidWebDriver implements Driver {

	@Override
	public WebDriver getDriver() {
			AppiumServer appiumServer = new AppiumServer();
			int port =4723;
			
			if (!appiumServer.checkIfServerIsRunning(port)) {
				appiumServer.startServer();
			}
			
	DesiredCapabilities capabilities = new DesiredCapabilities();
	capabilities.setCapability("deviceName", "emulator-5554");
	capabilities.setCapability("platformName", "Android");
	capabilities.setVersion("5.1.1");
	capabilities.setCapability("noReset", "true");
	capabilities.setCapability("appPackage", "com.google.android.apps.authenticatoe2");
	capabilities.setCapability("appActivity", "com.google.android.apps.authenticator.AuthenticatorActivity");
	
	try {
		return new AndroidDriver<MobileElement>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
	}
	catch (MalformedURLException e) {
		Assert.fail("Appium Server URL is not correct", e.getCause());
	}
	return null;
	}
			
	}


