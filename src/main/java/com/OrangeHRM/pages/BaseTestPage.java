package com.OrangeHRM.pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.OrangeHRM.constants.AppConstants;

public abstract class BaseTestPage {
	
	
	
	/**
	 * This method waits for JavaScript to properly load within a given
	 * PageLoadTimeout. Additionally, it calls waitUntilAngularReady method to check
	 * if Angular is ready.
	 * 
	 * @author Adesh.Khedkar
	 * @param driver
	 * @return
	 **/
	
	protected boolean isPageLoaded(WebDriver driver) {
		boolean isPageLoaded = waitforAjaxtoComplete(driver);
		waitUntilAngularReady(driver);
		waitForLoadingSpinner(driver);
		return isPageLoaded;
	}
	
	/**
	 * This method waits for Angular to be ready within a given scriptTimeout by
	 * checking that there are no pending http requests in the queue.
	 * 
	 * @author Adesh.Khedkar
	 * @param driver
	 **/
	protected void waitUntilAngularReady(WebDriver driver) {
		try {
			if (!(Boolean) ((JavascriptExecutor) driver)
				.executeScript("return angular.element(document).injector(['ng']) === undefined")) {
				new WebDriverWait(driver,Duration.ofSeconds(AppConstants.SCRIPT_TIMEOUT))
				.until(webDriver -> ((JavascriptExecutor) webDriver)
				.executeScript("return angular.element(document).injector(['ng']).get('$http').pendingRequests.length"))
				.equals(0);}
				
			}catch(Exception e) {
				//Do nothing if angular is not defined on page
			}
		}
	
	protected boolean waitforAjaxtoComplete(WebDriver driver) {
		return  new WebDriverWait(driver,Duration.ofSeconds(AppConstants.SCRIPT_TIMEOUT))
				.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState")
						.equals("complete"));
	}
	
	protected void waitForLoadingSpinner(WebDriver driver) {
		//Loading spinner after spinner
		try{List<WebElement> loadingSpinner = driver.findElements(By.xpath("//div[@class='oxd-loading-spinner']"));
		if(!loadingSpinner.isEmpty()) {
			new WebDriverWait(driver, Duration.ofSeconds(AppConstants.PAGE_LOAD_TIMEOUT))
			.until(ExpectedConditions.invisibilityOf(loadingSpinner.get(0)));
		}
		
		//Loading text inside grid
		loadingSpinner = driver.findElements(By.xpath("//span[normalize-space(text())='Loading']"));
		if(!loadingSpinner.isEmpty()) {
			new WebDriverWait(driver, Duration.ofSeconds(AppConstants.PAGE_LOAD_TIMEOUT))
			.until(ExpectedConditions.invisibilityOfAllElements(loadingSpinner));
		}}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	protected long startTime = 0;
	protected long endTime = 0;
	protected long pageLoadTime = 0;
	
	public abstract void waitForPageToLoad();

}
