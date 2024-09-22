package com.OrangeHRM.browsers;


import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;

public class LocalDriver {

	public LocalDriver() {
		throw new IllegalStateException("Driver Class");
	}
	
	private static Map<Integer, WebDriver> driverMap = new HashMap<>();
	
	public static WebDriver getDriver() {
		return driverMap.get((int) (Thread.currentThread().getId()));
	}
	
	public static void setDriver(WebDriver driver) {
		driverMap.put((int) (Thread.currentThread().getId()), driver);
	}
	
	

}
