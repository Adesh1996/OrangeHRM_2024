package com.OrangeHRM.customelements;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
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
 * OrangeTableElement is a customWebElement developed to handle UI html table
 * function with ease. It provides direct access to helper methods to perform 
 * certain functions on table.
 * @author Aadesh.Khedkar
 *
 */

public class OrangeTableElement extends OrangeWebElement {
	
	protected Logger log = LogManager.getLogger();
	
	/**
	 * Constructor
	 * 
	 * @param webDriver
	 * 			The webDriver used to interact with the web browser.
	 * @param by
	 * 			The locator is used to identify the element(s) on the website.
	 */
	public OrangeTableElement(WebDriver webDriver, By by) {
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
	
	/**
	 * This method verifies if a given element is visible on screen. If not, it
	 * will throw an AsserationError.
	 **/
	public void asserVisible() {
		waitforVisible();
		Assert.assertTrue(getElement().isDisplayed(), "Element is not visible on screen");
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
		Assert.assertTrue(flag, "Unwanted element is visible on screen");
	}
	
	public WebElement waitforVisible() {
		WebDriverWait wait = new WebDriverWait(getWebDriver(), Duration.ofSeconds(AppConstants.WAIT_TIMEOUT));
		return wait.until(ExpectedConditions.visibilityOfElementLocated(getBy()));
	}
	
	public List<WebElement> getTableHeaderWebElements() {
		List<WebElement> headerElementsList = getElement().findElements(By.xpath("//div[@role='columnheader']"));
		
		if (headerElementsList.isEmpty()) {
			headerElementsList = getElement().findElements(By.xpath("//div[@role='columnheader'][not(@style='display:none;')]"));
		}
		if (headerElementsList.isEmpty()) {
			headerElementsList = getElement().findElements(By.xpath(".//tr/th"));
		}
		return headerElementsList;
	}
	
	public List<String> getTableHeaderNames() {
		List<WebElement> headerElements = getTableHeaderWebElements();
		List<String> gridHeaderNames = new ArrayList<>();
		for (WebElement webElement: headerElements) {
			gridHeaderNames.add(webElement.getText());
		}
		return gridHeaderNames;
	}
	
	public List<WebElement> getTableColumnWebElements(String columnName){
		List<WebElement> columnWebElements = new ArrayList<>();
		List<String> columnHeaders = getTableHeaderNames();
		
		for(int i =0; i<columnHeaders.size(); i++) {
			if (columnHeaders.get(i).length() >0 && columnHeaders.get(i).trim().equals(columnName)) {
				i++;
				columnWebElements = getElement().findElements(By.xpath("//div[@role='cell']/div["+i+"]"));
				if(columnWebElements.isEmpty()) {
					columnWebElements = getElement().findElements(By.xpath("//div[@role='cell'][not(@style='display:none;')][" + i + "]"));
				}
				if(columnWebElements.isEmpty()) {
					columnWebElements = getElement().findElements(By.xpath(".//tr/td[" + i + "]"));
				}
				
				log.info("Captured elements of column : " + columnName);
				break;
			}
		}
		return columnWebElements;  
	}
	
	public List<String> getTableColumnValues(String columnName){
		List<WebElement> columnWebElements = getTableColumnWebElements(columnName);
		List<String> columnUiValues = new ArrayList<>();
		String uiValue = null;
		
		for (WebElement element : columnWebElements) {
			if(!element.getText().equals(columnName)) {
				if(element.getText().trim().length() >0 && columnName.contains("value")) {
					uiValue = element.getText().replaceAll(",", "");
					uiValue = uiValue.replace(".00", "");
					uiValue = uiValue.replace("$", "");
					uiValue = uiValue.replace("USD", "");
					uiValue = uiValue.replace("GBP", "");
					uiValue = uiValue.replace("JPY", "").trim();
					uiValue = formatUiValue(uiValue);
				}else {
					uiValue = element.getText().trim();
				}
				columnUiValues.add(uiValue.trim());
			}
		}
		return columnUiValues;
	}
	
	
	public Map<String, List<String>> getTableUiValuesMap() {
		Map<String, List<String>> uiValuesMap = new HashMap<>();
		List<String> headers = getTableHeaderNames();
		
		for(String header: headers) {
			if(header.length()> 0 ) {
				uiValuesMap.put(header, getTableColumnValues(header));
			}
		}
		log.info("Got UI Values in map");
		return uiValuesMap;
	}
	
	public List<String> getHeaderlessTableColumnValues(String header){
	
		List<String> columnUiValues = new ArrayList<>();
		String uiValue = null;
		List<WebElement> columnWebElements = null;
		if(header.equalsIgnoreCase("Category name")) {
			columnWebElements = getElement().findElements(By.xpath(".//tr//td[1]"));
			for (WebElement element : columnWebElements) {
				uiValue = element.getText().split("\n")[0].trim();
				columnUiValues.add(uiValue);
			}
		}

		return columnUiValues;
	}
	
	
	public Map<String, List<String>> getHeaderlessTableUiValuesMap(String... headerNames) {
		Map<String, List<String>> uiValuesMap = new HashMap<>();
		List<String> headers = Arrays.asList(headerNames);
		for (String header: headers) {
			if(header.length() > 0) {
				uiValuesMap.put(header, getHeaderlessTableColumnValues(header));
			}
		}
		log.info("Got UI Values in Map");
		return uiValuesMap;
	}
	
	public boolean IsElementPresentInTableByText(String text) {
		WebDriverWait wait = new WebDriverWait(getWebDriver(), Duration.ofSeconds(AppConstants.WAIT_TIMEOUT));
		List <WebElement> list = wait.until(ExpectedConditions
				.visibilityOfAllElementsLocatedBy(By.xpath("//*[normalize-space(text()) = '" + text + "']")));
		return !list.isEmpty();
	}
	
	public void verifyElementPresentInTableByText(String text) {
		Assert.assertTrue(IsElementPresentInTableByText(text), text + " is not present in table");
	}
	
	private String formatUiValue(String uiValue) {
		if(uiValue.contains(".00")) {
			uiValue = uiValue.replace(".00", "");
		}
		if(uiValue.contains(",")) {
			uiValue = uiValue.replaceAll(",", "");
		}
		if(uiValue.contains(".")) {
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
