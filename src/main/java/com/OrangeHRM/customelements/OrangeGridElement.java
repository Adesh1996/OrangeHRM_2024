package com.OrangeHRM.customelements;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.OrangeHRM.constants.AppConstants;


/**
 *  OrangeGridElement is a CustomWebElement developed to handle UI grid functions
 *  with ease. It provides direct access to helper methods to perform certain
 *  functions on grid.
 **/

public class OrangeGridElement extends CustomWebElement {
	protected Logger log = LogManager.getLogger();	
	
	/**
	 * Constructor 
	 * 
	 * @param webDriver
	 *  			The webDriver is used to interact with web browser.
	 *  @param by 
	 *  			The locator used to identify the element(s) on the website.
	 */
	
	public OrangeGridElement(WebDriver webDriver, By by) {
		super(webDriver, by);
	
	}
	
	
	/**
	 * This method converts CustomWebElement and returns default WebElement
	 * 
	 * @return
	 * 
	 * @return WebElement
	 **/
	public WebElement getElement() {
		return getWebDriver().findElement(getBy());
	}
	
	/**
	 * Finds an element which uses the locator of this element as base.
	 * 
	 * @return The found sub web element of this complex web element.
	 **/
	public WebElement findElement(By locator) {
		return getElement().findElement(locator);
	}
	
	/**
	 * Finds an elements which uses the locator of this element as base.
	 * 
	 * @return The found sub web element of this complex web element.
	 **/
	public List<WebElement> findElements(By locator) {
		return getElement().findElements(locator);
	}
	  
	
	/**
	 * Is this element displayed or not? This method avoids the problem of 
	 * having to parse an element's "style" attribute.
	 * 
	 * @return Whether or not the element  is displayed
	 **/
	public boolean isDisplayed() {
		return getElement().isDisplayed();
	}
	
	/**
	 * This method returns true or false based on the element is present in DOM
	 * or not. It avoids NoSuchElementException by not calling the isDisplayed()
	 * method on element.
	 * 
	 * @return Whether or not the element is present in DOM
	 **/
	public boolean isPresent() {
		boolean flag = true;
		if (getWebDriver().findElements(getBy()).isEmpty())
			flag= false;
		return flag;
	}
	
	public List<WebElement> getGridColumnHeaderWebElements(){
		return getElement().findElements(By.xpath("//div[@role='columnheader']"));
	}
	
	public List<String> getGridColumnHeaderNames(){
		List<WebElement> headerElements = getGridColumnHeaderWebElements();
		List<String> gridHeaderNames = new ArrayList<>();
		for (WebElement webElement : headerElements) {
			gridHeaderNames.add(webElement.getText());
		}
		return gridHeaderNames;
	}
	
	public List<WebElement> getGridColumnWebElements(String columnName){
		List<WebElement> columnWebElements = new ArrayList<>();
		List<String> columnHeaders = getGridColumnHeaderNames();
		
		for(int i =0; i<columnHeaders.size(); i++) {
			if (columnHeaders.get(i).length() >0 && columnHeaders.get(i).trim().equals(columnName)) {
				i++;
				//If first column of grid is not a header, increment i to skip that column
				if(!getElement()
						.findElements(By.xpath(".//div[@class='oxd-checkbox-wrapper']")).isEmpty()) {
					i++;
				}
				columnWebElements = getElement()
						.findElements(By.xpath("//div[@role='cell'][" + i +"]"));
				log.info("Captured elements of column : " + columnName);
				break;
			}
		}
		return columnWebElements;  
	}
	
	
	public List<String> getGridColumnValues(String columnName){
		waitforVisible();
		List<WebElement> columnWebElements = getGridColumnWebElements(columnName);
		List<String> columnUiValues = new ArrayList<>();
		String uiValue = null;
		
		for (WebElement element : columnWebElements) {
			uiValue = element.getText().trim();
			if(!uiValue.equals(columnName) && uiValue.length() > 0) {
				uiValue = formatUiValue(uiValue);
				columnUiValues.add(uiValue.trim());
				
			}
		}
		return columnUiValues;
	}
	
	
	public Map<String, List<String>> getGridUiValueMap(){
		Map <String, List<String>> uiValuesMap = new HashMap<>();
		List<String> headers = getGridColumnHeaderNames();
		for(String header : headers) {
			if (header.length() >0) {
				uiValuesMap.put(header, getGridColumnValues(header));
			}
		}
		log.info("Got UI Values in map");
		return uiValuesMap;
	}
	
	
	public boolean IsElementPresentInGridByText(String text) {
		WebDriverWait wait = new WebDriverWait(getWebDriver(), Duration.ofSeconds(AppConstants.WAIT_TIMEOUT));
		List <WebElement> list = wait.until(ExpectedConditions
				.visibilityOfAllElementsLocatedBy(By.xpath("//*[normalize-space(text()) = '" + text + "']")));
		return !list.isEmpty();
	}
	
	public void verifyElementPresentInGridByText(String text) {
		Assert.assertTrue(IsElementPresentInGridByText(text), text + " is not present in grid");
	}
	
	public void clickElementInGridByText(String text) {
		WebDriverWait wait = new WebDriverWait(getWebDriver(), Duration.ofSeconds(AppConstants.WAIT_TIMEOUT));
		List <WebElement> list = wait.until(ExpectedConditions
				.visibilityOfAllElementsLocatedBy(By.xpath("//*[normalize-space(text()) = '" + text + "']")));
		list.get(0).click();
	}
	
	public WebElement waitforVisible() {
		WebDriverWait wait = new WebDriverWait(getWebDriver(), Duration.ofSeconds(AppConstants.WAIT_TIMEOUT));
		return wait.until(ExpectedConditions.visibilityOfElementLocated(getBy()));
	}
	
	
	/**
	 * This method verifies if a given element is visible on screen. If not, it
	 * will throw an AsserationError.
	 **/
	public void asserVisible() {
		waitforVisible();
		Assert.assertTrue(getElement().isDisplayed(), "Grid is not visible on screen");
	}
	
	/**
	 * This method verifies if a given element is not visible on screen. If it
	 * is visible, will throw an AsserationError.
	 **/
	public void assertNotVisible() {
		boolean flag = false;
		try {
			getElement().isDisplayed();
		}catch (Exception e) {
			flag= true;
		}
		Assert.assertTrue(flag, "Unwanted grid is visible on screen");
	}
	
	private String formatUiValue(String uiValue) {
		if (uiValue.contains(".00")) {
			uiValue = uiValue.replace(".00", "");
		}
		if (uiValue.endsWith("AM") || uiValue.endsWith("PM") && uiValue.trim().contains(" ")) {
			uiValue = uiValue.substring(0,uiValue.indexOf(' '));
		}
		if (uiValue.contains(".")) {
			try {
				BigDecimal decimal = new BigDecimal(uiValue).stripTrailingZeros();
				uiValue = decimal.toPlainString();
			}
			catch (NumberFormatException e) {
				//Do nothing if visible uivalue is not a number
			}
		}
		return uiValue;
	}
	
	
}
