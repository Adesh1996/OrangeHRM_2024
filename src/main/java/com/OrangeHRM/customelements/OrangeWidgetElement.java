package com.OrangeHRM.customelements;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
 * OrangeWidgetElement is a CustomWebElement developed to handle UI html table
 * functions with ease. It provides direct access to helper methods to perform
 * certain functions on table.
 * 
 * @author Aadesh.Khedkar
 **/


public class OrangeWidgetElement extends CustomWebElement {
	
	protected Logger log = LogManager.getLogger();
	
	/**
	 * Constructor
	 * 
	 * @param webDriver
	 * 			The webDriver used to interact with the web browser.
	 * 
	 * @param by 
	 *  		The locator is used to identify the element(s) on the website.
	 **/
	
	public OrangeWidgetElement(WebDriver webDriver, By by) {
		super(webDriver, by);
	}
	
	/**
	 *  This method converts CustomWebElement and returns default WebElement
	 *  
	 *  @return WebElement
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
	 * Is this element displayed or not? This method avoids the problem of having to
	 * parse an element's "style" attribute.
	 * 
	 * @return Whether or not the element is displayed.
	 **/
	public void isDisplayed() {
		getElement().isDisplayed();
	}

	/**
	 * This method returns true or false based on element is present in DOM or not.
	 * It avoids NoSuchElementException by not calling the isDisplayed() method on
	 * element
	 * 
	 * @return Whether or not the element is present in DOM
	 **/
	public boolean isPresent() {
		boolean flag = true;
		if(getWebDriver().findElements(getBy()).isEmpty()) 
			flag = false;
			return flag;
	}
	
	/**
	 * This method verifies if a given element is visible on screen. If not, it
	 * will throw an AsserationError.
	 **/
	public void assertVisble() {
		waitforVisible();
		Assert.assertTrue(getElement().isDisplayed(), "Widget is not visible on screen");
	}
	
	/**
	 * This method verifies if a given element is not visible on screen. If it
	 * is visible, will throw an AsserationError.
	 **/
	public void assertNotVisble() {
		boolean flag = false;
		try {
			getElement().isDisplayed();
		}catch (Exception e) {
			flag= true;
		}
		Assert.assertTrue(flag, "Unwanted Widget is visible on screen");
	}
	
	public WebElement waitforVisible() {
		WebDriverWait wait = new WebDriverWait(getWebDriver(), Duration.ofSeconds(AppConstants.WAIT_TIMEOUT));
		return wait.until(ExpectedConditions.visibilityOfElementLocated(getBy()));
	}
	
	public List<WebElement> getWidgetHeaderWebElements(){
		List<WebElement> headerElementsList =  getElement().findElements(By.xpath("//div[@role='columnheader']"));
		return headerElementsList;
	}
	
	public List<String> getWidgetHeaderNames(){
		List<WebElement> headerElements = getWidgetHeaderWebElements();
		List<String> gridHeaderNames = new ArrayList<>();
		for (WebElement webElement : headerElements) {
			gridHeaderNames.add(webElement.getText());
		}
		return gridHeaderNames;
	}
	
	public List<WebElement> getWidgetColumnWebElements(String columnName){
		List<WebElement> columnWebElements = new ArrayList<>();
		List<String> columnHeaders = getWidgetHeaderNames();
		
		for(int i =0; i<columnHeaders.size(); i++) {
			if (columnHeaders.get(i).length() >0 && columnHeaders.get(i).trim().equals(columnName)) {
				i++;
				columnWebElements = getElement()
						.findElements(By.xpath("//div[@class='table-body']/div/div[" + i +"]"));
				log.info("Captured elements of column : " + columnName);
				break;
			}
		}
		return columnWebElements;  
	}
	
	
	public List<String> getWidgetColumnValues(String columnName){
		waitforVisible();
		List<WebElement> columnWebElements = getWidgetColumnWebElements(columnName);
		List<String> columnUiValues = new ArrayList<>();
		String uiValue = null;
		
		for (WebElement element : columnWebElements) {
			uiValue = element.getText().trim();
			//remove this check after the defect is fixed
			if(uiValue.equals("End of List")) {
				continue;
			}
			if (!uiValue.equals(columnName)) {
				if(uiValue.length() >0 && (columnName.contains("alue") || (columnName.contains("Amount") ))) {
					uiValue= element.getText().replaceAll(",", "");
					uiValue = uiValue.replace("$", "");
					uiValue = uiValue.replace("USD", "");
					uiValue = uiValue.replace("CAD", "");
					uiValue = uiValue.replace("GBP", "");
					uiValue = uiValue.replace("JPY", "");
					uiValue = uiValue.replace("%", "");
					uiValue = formatUiValue(uiValue.trim());
				}
			}
		}
		return columnUiValues;
	}
	
	
	public Map<String, List<String>> getWidgetUiValuesMap(){
		Map <String, List<String>> uiValuesMap = new HashMap<>();
		List<String> headers = getWidgetHeaderNames();
		for(String header : headers) {
			if (header.length() >0) {
				uiValuesMap.put(header, getWidgetColumnValues(header));
			}
		}
		log.info("Got UI Values in map");
		return uiValuesMap;
	}
	
	
	/**
	 * This method takes Ui values and their mapping to JSON attributes to form a 
	 * Map, which can be compared with formated JSON response map for validation
	 * 
	 * @author Adesh.Khedkar
	 * 
	 * @param uiMapToServiceAttrib
	 * 		-Mapping of Ui values to JSON service attributes
	 * 
	 * @param uiValuesMap
	 * 		- Actual UI values map as seen on the page
	 **/
	public Map<String, List<String>> getWidgetResponseMapper (Map<String, String> uiMapToServiceAttrib,
			Map<String, List<String>> uiValuesMap){
		Map<String, List<String>> responseMapper = new HashMap<>();
		for(Map.Entry<String, String> entry : uiMapToServiceAttrib.entrySet()) {
			if(uiValuesMap.containsKey(entry.getKey())) {
				responseMapper.put(uiMapToServiceAttrib.get(entry.getKey()), uiValuesMap.get(entry.getValue()));
			}
		}
		return responseMapper;
	}
	
	public boolean IsElementPresentInWidgetByText(String text) {
		WebDriverWait wait = new WebDriverWait(getWebDriver(), Duration.ofSeconds(AppConstants.WAIT_TIMEOUT));
		List <WebElement> list = wait.until(ExpectedConditions
				.visibilityOfAllElementsLocatedBy(By.xpath("//*[normalize-space(text()) = '" + text + "']")));
		return !list.isEmpty();
	}
	
	private String formatUiValue(String uiValue) {
		if (uiValue.contains(".00")) {
			uiValue = uiValue.replace(".00", "");
		}
		if (uiValue.contains(",")) {
			uiValue = uiValue.replace(",", "");
		}
		if (uiValue.contains(".")) {
			try{
				BigDecimal decimal = new BigDecimal(uiValue).stripTrailingZeros();
				uiValue = decimal.toPlainString();
			}catch (NumberFormatException e) {
				//Do nothing if uiValue is not a number
			}
		}
		
		if(uiValue.contains("/")) {
			try{
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yy");
				LocalDate date = LocalDate.parse(uiValue.trim(), dtf);
				dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
				uiValue = date.format(dtf);
			}catch (DateTimeParseException e) {
				//Do nothing if uiValue is not a date
			}
		}
		return uiValue.trim();
	}

}
