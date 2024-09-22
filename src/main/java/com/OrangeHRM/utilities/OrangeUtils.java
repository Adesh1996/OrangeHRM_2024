package com.OrangeHRM.utilities;

import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import com.OrangeHRM.browsers.LocalDriver;
import com.OrangeHRM.constants.AppConstants;
import com.OrangeHRM.customelements.OrangeGridElement;
import com.OrangeHRM.customelements.OrangeTableElement;
import com.OrangeHRM.customelements.OrangeWebElement;

import freemarker.core.LocalContext;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

public class OrangeUtils {
	
	
	private static Logger log = LogManager.getLogger();
	
	
	private OrangeUtils() {
		throw new IllegalStateException("Utility Class");
	}
	
	private static Map<Integer, SoftAssert> sfAssertMap = new HashMap<>();
	
	public static SoftAssert getSoftAssert() {
		return sfAssertMap.get((int)(Thread.currentThread().getId()));
	}
	
	public static void  setSoftAssert(SoftAssert sfAssert) {
		sfAssertMap.put((int) (Thread.currentThread().getId()), sfAssert);
	}
	
	/**
	 * This method takes screenshot and returns the path to it in file system
	 * 
	 * @param methodName
	 * 			- Name of the calling method
	 * 
	 * @param driver
	 * 			- Current instance of webDriver
	 * @return 
	 * 
	 * @return - String path
	 **/
	public static byte[] takeFullScreenShot(WebDriver driver) {
		Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000))
				.takeScreenshot(driver);
		
		BufferedImage bImage = screenshot.getImage();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ImageIO.write(bImage, "png", bos);
		}catch (IOException e) {
			e.printStackTrace();
		}
		return bos.toByteArray();
	}
	
	/**
	 *  Returns current date in given format
	 *  
	 *  @return date in String
	 **/
	public static String getCurrentDate(String dateFormat) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
			Date date = new Date();
			return formatter.format(date);
		}
		catch(Exception e) {
			Assert.fail(e.getLocalizedMessage());
			return null;
		}
	}
	
	
	/**
	 * Returns UI values in a key value pair map
	 * 
	 * @author Adesh.Khedkar
	 * 
	 * @param keyLabels
	 * 			-List of key label webelements in UI
	 * @param valueLabels
	 * 			-List of value label webelements in UI
	 * 
	 * @return
	 **/
	public static Map<String, String> getUIValuesMap(List <WebElement> keyLabels, List<WebElement> valueLabels){
		Map<String, String> uiValuesMap = new HashMap<>();
		
		for (int i = 0; i<keyLabels.size(); i++) {
			uiValuesMap.put(keyLabels.get(i).getText(), valueLabels.get(i).getText());
		}
		Map<String, String> formatedUiValuesMap = new HashMap<>();
		String value = null;
		
		for(Map.Entry<String, String> entry : uiValuesMap.entrySet()) {
			String key = entry.getKey();
			value= uiValuesMap.get(entry.getKey());
			
			if(value.contains("$")) {
				value = value.replace("$", "").trim();
			}
			if(value.contains("CAD")) {
				value = value.replace("CAD", "").trim();
			}
			if(value.contains(".00")) {
				value = value.replace(".00", "").trim();
			}
			if(value.contains("%")) {
				value = value.replace("%", "").trim();
			}
			if(value.endsWith(".0")) {
				int index = value.indexOf(".");
				String subs = value.substring(index);
				if(subs.length() == 2) {
					value = value.replace(".0","").trim();
				}
			}
			
			if(value.contains(".")) {
				try {
				BigDecimal decimal = new BigDecimal(value).stripTrailingZeros();
				value= decimal.toPlainString();}
				catch(NumberFormatException e) {
					//Do nothing if uiValue is not a number
				}
			}
			formatedUiValuesMap.put(entry.getKey(), value.trim());
		}
		return formatedUiValuesMap;
	}
	
	/**
	 * This method takes UI values and their mapping to JSON attributes to form a 
	 * Map, which can be compared with formatted JSON response map for validation
	 * 
	 * @author Adesh.Khedkar
	 * 
	 * @param uiMapToServiceAttrib
	 * 			- Mapping of UI values to JSON service attributes
	 * @param uiValuesMap
	 * 			- Actual UI values map as seen on the page
	 * @return 
	 * 
	 * @return
	 **/
	public static Map<String, String> getResponseMapper(Map<String, String> uiMapToServiceAttrib, 
			Map<String, String> uiValuesMap) {
		
		Map<String, String> responseMapper = new HashMap<>();
		for(Map.Entry<String, String> entry : uiMapToServiceAttrib.entrySet()) {
			if(uiValuesMap.containsKey(entry.getKey())) {
				responseMapper.put(uiMapToServiceAttrib.get(entry.getKey()), uiValuesMap.get(entry.getKey()));
			}
		}
		return responseMapper;
	}
	
	/**
	 * This method takes UI values and their mapping to JSON attributes to form a 
	 * Map, which can be compared with formatted JSON response map for validation
	 * 
	 * @author Adesh.Khedkar
	 * 
	 * @param uiMapToServiceAttrib
	 * 			- Mapping of UI values to JSON service attributes
	 * @param uiValuesMap
	 * 			- Actual UI values map as seen on the page
	 * @return 
	 * 
	 * @return
	 **/
	public static Map<String,List<String>> getGridResponseMapper(Map<String, String> uiMapToServiceAttrib, 
			Map<String, List<String>> uiValuesMap) {
		
		Map<String, List<String>> responseMapper = new HashMap<>();
		for(Map.Entry<String, String> entry : uiMapToServiceAttrib.entrySet()) {
			if(uiValuesMap.containsKey(entry.getKey())) {
				responseMapper.put(uiMapToServiceAttrib.get(entry.getKey()), uiValuesMap.get(entry.getKey()));
			}
		}
		return responseMapper;
	}
	
	/**
	 * This method checks if a given mapSubset is actually a subset of map
	 * 
	 * @author Adesh.Khedkar
	 * 
	 * @param map
	 * 			- Given map
	 * @param mapSubset
	 * 			- Given mapSubset
	 * @return 
	 * @return
	 * 
	 **/
	public static boolean mapContains(Map<String, String>map, Map<String, String>mapSubset) {
		boolean result = true;
		for(Map.Entry<String, String> entry : mapSubset.entrySet()) {
			if(map.keySet().contains(entry.getKey()) 
					&& ! (map.get(entry.getKey()).equals(mapSubset.get(entry.getKey())))) {
				result = false;
				System.out.println(entry.getKey());
				log.error("Expected : " + map.get(entry.getKey()) + "\nActual: " +  mapSubset.get(entry.getKey()));
				break;
			}
		}
		return result;
	}
	
	/**
	 *	This method checks if a given mapSubset is actually a subset of map
	 *
	 * @author Adesh.Khedkar
	 * 
	 * @param map
	 * 			- Given map
	 * 
	 * @param mapSubset
	 * 			- Given mapSubset
	 * @return 	
	 **/
	public static boolean verifyMaps(Map<String, List<String>>map, Map<String,List< String>>mapSubset) {
		boolean result = true;
		for(Map.Entry<String,List< String>> entry : mapSubset.entrySet()) {
			if(map.keySet().contains(entry.getKey()) 
					&& ! (map.get(entry.getKey()).equals(mapSubset.get(entry.getKey())))) {
				result = false;
				System.out.println(entry.getKey());
				log.error("Expected value for " + entry.getKey() + "\n " +  map.get(entry.getKey())
				+ "nActual Value : \n" + mapSubset.get(entry.getKey()));
				break;
			}
		}
		return result;
	}
	
	/**
	 * This method checks if a given mapSubset is actually a subset of map
	 * 
	 * @author Adesh.Khedkar
	 * 
	 * @param dbMap
	 * 			-Given map
	 * 
	 * @param excelMap
	 * 			-Given mapSubset
	 * @return 
	 * 
	 * @return
	 **/
	public static boolean verifyReportMaps(Map<String, List<String>> dbMap, Map <String, List<String>> excelMap) {
		boolean result = true;
		for (Map.Entry<String, List<String>> entry : excelMap.entrySet()) {
			if(dbMap.keySet().contains(entry.getKey())) {
				List<String> excelValues = excelMap.get(entry.getKey());
				List<String> dbValues = dbMap.get(entry.getKey());
				Collections.sort(excelValues);
				Collections.sort(dbValues);
				
				if(!(dbValues.equals(excelValues))) {
					result = false;
					log.error("Expected value for " + entry.getKey() + " : " + dbMap.get(entry.getKey())
					+ "\nActual Value: " + excelMap.get(entry.getKey()));
					break;
				}
			}
		}
		return result;
	}
	
	
	
	/**
	 * This method verifies UI field values are matching with one returned from service
	 * 
	 * @author Adesh.Khedkar
	 * 
	 * @param resourceUrl
	 * 			- URL of the service resource
	 * @param keyLabels
	 * 			- List of key label Web elements in UI
	 * @param valueLabels
	 * 			- List of value label Web elements in UI
	 * @param uiToServiceMap
	 * 			- UI field to service attribute mapping
	 * @return
	 **/
	public static void verifyUIFieldsFromServiceResponse(String resourceUrl, List<WebElement> keyLabels,
			List<WebElement> valueLabels, Map<String, String> uiToServiceMap) {
		WebServiceHandler webServiceHandler = new WebServiceHandler(resourceUrl);
		Map<String, String> responseMap = webServiceHandler.getPlainJsonResponseAsMap();
		Map<String, String> uiValues = getUIValuesMap(keyLabels, valueLabels);
		Assert.assertFalse(uiValues.isEmpty(), "UI fields values could not be captured and are empty.\n");
		Map<String, String> responseMapper = getResponseMapper(uiToServiceMap, uiValues);
		Assert.assertFalse(responseMapper.isEmpty(), "UI fields are not mapped with correct service attribute.\n");
		Assert.assertTrue(mapContains(responseMap, responseMapper), "Ui Values are not matching with service response");
		
	}
	
	/**
	 * This method verifies the given UI Grid with Service response
	 * 
	 * @author Adesh.Khedkar
	 * 
	 * @param resourceUrl
	 * 			- URL of the service resource
	 * @param element 
	 * 			- UI Grid Element in form of "OrangeGridElement" object
	 * @param uiToServiceMap
	 * 			- UI field to service attribute mapping for the given grid
	 * @return
	 */
	public static void verifyGrid(String resourceUrl, OrangeGridElement element, Map<String, String>uiToServiceMap) {
		element.waitforVisible();
		ServiceUtilities serviceUtilities = new ServiceUtilities();
		Map<String, List<String>> responseMap = serviceUtilities.getComplexServiceResponse(resourceUrl);
		Map<String, List<String>> responseMapper = getGridResponseMapper(uiToServiceMap, element.getGridUiValueMap());
		Assert.assertFalse(responseMapper.isEmpty(), "UI fields are not mapped with correct service attribute.\n");
		Assert.assertTrue(verifyMaps(responseMap, responseMapper), 
				"Grid values are not matching with Service Response");
	}
	
	
	public static void verifyTable(String resourceUrl, OrangeTableElement element, Map<String, String>uiToServiceMap) {
		element.waitforVisible();
		ServiceUtilities serviceUtilities = new ServiceUtilities();
		Map<String, List<String>> responseMap = serviceUtilities.getComplexServiceResponse(resourceUrl);
		Map<String, List<String>> responseMapper = getGridResponseMapper(uiToServiceMap, element.getTableUiValuesMap());
		Assert.assertFalse(responseMapper.isEmpty(), "UI fields are not mapped with correct service attribute.\n");
		Assert.assertTrue(verifyMaps(responseMap, responseMapper), 
				"Table values are not matching with Service Response");
	}
	
	
	public static void verifyHeaderlessTable(String resourceUrl, OrangeTableElement element, Map<String, String>uiToServiceMap
			, String... headerNames) {
		element.waitforVisible();
		ServiceUtilities serviceUtilities = new ServiceUtilities();
		Map<String, List<String>> responseMap = serviceUtilities.getComplexServiceResponse(resourceUrl);
		Map<String, List<String>> responseMapper = getGridResponseMapper(uiToServiceMap, element.getHeaderlessTableUiValuesMap(headerNames));
		Assert.assertFalse(responseMapper.isEmpty(), "UI fields are not mapped with correct service attribute.\n");
		Assert.assertTrue(verifyMaps(responseMap, responseMapper), 
				"Table values are not matching with Service Response");
	}
	
	
	/**
	 * This method verifies the given UI Grid with Service response
	 * (it is used for complex nested JSON response)
	 * 
	 * @author Adesh.Khedkar
	 * 
	 * @param resourceUrl
	 * 			- URL of the service resource
	 * @param element 
	 * 			- UI Grid Element in form of "OrangeGridElement" object
	 * @param uiToServiceMap
	 * 			- UI field to service attribute mapping for the given grid
	 * @return
	 **/
	public static void verifyRelationshipGrid(String resourceUrl, OrangeGridElement element, Map<String, String>uiToServiceMap) {
		element.waitforVisible();
		ServiceUtilities serviceUtilities = new ServiceUtilities();
		Map<String, List<String>> responseMap = serviceUtilities.getComplexServiceResponseforNestedJsonArray(resourceUrl);
		Map<String, List<String>> responseMapper = getGridResponseMapper(uiToServiceMap, element.getGridUiValueMap());
		Assert.assertFalse(responseMapper.isEmpty(), "UI fields are not mapped with correct service attribute.\n");
		Assert.assertTrue(verifyMaps(responseMap, responseMapper), 
				"Grid values are not matching with Service Response");
	}
	
	/**
	 * Utility to fetch data from excel and pass it to dataprovider
	 * @return 
	 * 
	 * @throw Exception   
	 */
	public static Map<String,String> getData(String testName, ExcelUtilities excelUtil) {
		HashMap<String, String> table = new HashMap<>();
		List<String>sheetNames = excelUtil.getSheetNames();
		String currentSheet = "";
		int testCaseStartRowNum = 0;
		for(String sheetName : sheetNames) {
			testCaseStartRowNum = excelUtil.findRow(sheetName, testName);
			if(testCaseStartRowNum >=0) {
				break;
			}
		}
		if(testCaseStartRowNum < 0) {
			log.error("***** Test Case: " + testName + "Not found in Test Data sheet for environment : "
					+ AppConstants.APPLICATION_ENV + " . Please add an entry of this test case.*****");
			return table;
		}
		
		int testDataKeyStartRow = testCaseStartRowNum + 1;
		int testDataValueStartRow  = testCaseStartRowNum + 2;
		
		int testDataSetRows = 0;
		while (!excelUtil.getCellData(currentSheet, testDataValueStartRow + testDataSetRows, 0).equals("")) {
			testDataSetRows++;
		}
		
		int totaldataColumns = 0;
		while (!excelUtil.getCellData(currentSheet, testDataValueStartRow, totaldataColumns).equals("")) {
			totaldataColumns++;
		}
		
		int dataSetStartRow = testDataValueStartRow;
		
		for (int rowNum = dataSetStartRow; rowNum<(testDataValueStartRow + testDataSetRows); 
				rowNum ++, dataSetStartRow++) {
			for (int colNum = 0; colNum < totaldataColumns; colNum ++  ) {
				table.put(excelUtil.getCellData(currentSheet, testDataKeyStartRow, colNum),
						excelUtil.getCellData(currentSheet, dataSetStartRow, colNum));
			}
		}
		return table;  
	}
	
	/**
	 * This method is used to click screen without giving element information. This
	 * is specific to Angular panels md-menu
	 * 
	 * @author Adesh.Khedkar
	 * 
	 * @return
	 */
	public static void robotClickNoElement() {
		try {
			wait(5000);
			Robot robot = new Robot();
			robot.mouseMove(300, 300);
			robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
			robot.mouseRelease(InputEvent.BUTTON2_DOWN_MASK);
		}
		catch(Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}
	
	/**
	 * Enters random text in a given input field by appending random number to the 
	 * given prefix
	 * 
	 * @author Adesh.Khedkar
	 * 
	 * @param element
	 * @param prefix
	 * @return 
	 * @return
	 */
	public static String enterRandomText(OrangeWebElement element, String prefix) {
		String randomText = prefix + Integer.toString(new Random().nextInt(9999));
		element.sendKeys(randomText);
		return randomText;
	}
	
	/**
	 * static wait for the given time in seconds
	 * 
	 * @param seconds 
	 **/
	public static void wait(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			Assert.fail(e.getLocalizedMessage());
			Thread.currentThread().interrupt();
		}
	}
	
	
	public static void switchToFrame(WebDriver driver, OrangeWebElement frame) {
		frame.waitforVisible(Duration.ofSeconds(AppConstants.FRAME_LOAD_TIMEOUT));
		driver.switchTo().frame(driver.findElement(frame.getBy()));
	}
	
	public static String acceptAlert(WebDriver driver) {
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(AppConstants.WAIT_TIMEOUT));
		wait.until(ExpectedConditions.alertIsPresent());
		Alert alert = driver.switchTo().alert();
		String alertText = alert.getText();
		alert.accept();
		return alertText;
	}
	
	public static String dismissAlert(WebDriver driver) {
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(AppConstants.WAIT_TIMEOUT));
		wait.until(ExpectedConditions.alertIsPresent());
		Alert alert = driver.switchTo().alert();
		String alertText = alert.getText();
		alert.dismiss();
		return alertText;
	}
	
	
	public static void dragAndDropElement(WebDriver driver, WebElement fromElement, WebElement toElement) {
		Actions builder = new Actions(driver);
		builder.clickAndHold(fromElement).moveToElement(toElement).release(toElement).build().perform();
	}
	
	public static void doubleClickElement(WebDriver driver, WebElement element) {
		Actions builder = new Actions(driver);
		builder.doubleClick().build().perform();
	}
	
	public static String openNewWindow(WebDriver driver) {
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("window.open();");
		switchToNewWindow(driver);
		return driver.getWindowHandle();
	}
	
	public static String switchToNewWindow(WebDriver driver) {
		Set<String> windowHandles = driver.getWindowHandles();
		for(String window : windowHandles) {
			wait(2000);
			driver.switchTo().window(window);
		}
		return driver.getWindowHandle();
	}
	
	/**
	 * Returns last modified file in a given directory
	 * 
	 * @param dir
	 * @return last modified file
	 * 
	 * @author Adesh.Khedkar
	 * @return 
	 **/
	public static File getlastModifiedFile(String dir) {
		File dirPath = new File(dir);
		File[] files = dirPath.listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				return pathname.isFile();
			}
			
		});
		
		long lastModified = Long.MIN_VALUE;
		File lastModifiedFile = null;
		for (File file : files) {
			if(file.lastModified() > lastModified) {
				lastModifiedFile = file;
				lastModified = file.lastModified();
			}
		}
		return lastModifiedFile;
	}
	
	public static void scrollAndClick(WebElement element) {
		OrangeUtils.wait(2000);
		JavascriptExecutor js = (JavascriptExecutor) LocalDriver.getDriver();
		js.executeScript("arguments[0].scrollIntoView();", element);
		log.info("Click by scrolling on " + element.getText());
		js.executeScript("arguments[0].style.border='2px dashed green'", element);
		js.executeAsyncScript("arguments[0].click();", element);
	}
	
	
	public static void scrollGridHorizontallyRightUntilHeaderIsVisible(String columnName, OrangeGridElement grid) {
		WebElement scroller = grid.findElement(By.xpath(".//div[@ref='eBodyHorizontalScrollContainer']"));
		JavascriptExecutor jsExecutor = (JavascriptExecutor) LocalDriver.getDriver();
		
		int maxScrollWidth = Integer.parseInt(scroller.getCssValue("width").replace("px", ""));
		String locator = grid.getBy().toString();
		locator = locator.substring(locator.indexOf(':') + 2 );
		locator = locator + ".//div[@ref='eBodyHorizontalScrollContainer']";
		int counter = 1000;
		while (scroller.isDisplayed()) {
			if(grid.getGridColumnHeaderNames().contains(columnName)) {
				break;
			}else {
				jsExecutor.executeScript("document.querySelector(\"" + locator + "\").scrollLeft += 600;");
				counter +=600;
			}
			if(counter > maxScrollWidth + 1000) {
				Assert.fail("Given column header is not visible in grid");
			}
		}
	}
	
	public static void scrollGridHorizontallyLeft(OrangeGridElement grid) {
		WebElement scroller = grid.findElement(By.xpath(".//div[@ref='eBodyHorizontalScrollContainer']"));
		JavascriptExecutor jsExecutor = (JavascriptExecutor) LocalDriver.getDriver();
		
		int maxScrollWidth = Integer.parseInt(scroller.getCssValue("width").replace("px", ""));
		String locator = grid.getBy().toString();
		locator = locator.substring(locator.indexOf(':') + 2 );
		locator = locator + ".//div[@ref='eBodyHorizontalScrollContainer']";
		jsExecutor.executeScript(
				"document.querySelector(\"" + locator + "\").scrollLeft -= " + maxScrollWidth + 1000 + ";"); 
	}  
	
	public static void compareImages(Screenshot expected, Screenshot actual) {
		ImageDiff diff = new ImageDiffer().makeDiff(expected, actual);
		BufferedImage diffImage = diff.getMarkedImage();
		
		File outputfile = new File("Failed Screen Images/ashotOutput.jpg");
		try {
			ImageIO.write(diffImage, "jpg", outputfile);
		}
		catch(IOException e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}
	
	public static void runExecutable(String pathToExecutable) {
		try {
			Runtime.getRuntime().exec(pathToExecutable);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void performRightClick(WebElement webElement) {
		Actions actions = new Actions(LocalDriver.getDriver());
		actions.contextClick(webElement).perform();
	}
	
	public static String waitForFileToDownload(String fileName) {
		File downloadedFile = OrangeUtils.getlastModifiedFile(AppConstants.DOWNLOAD_FOLDER_PATH);
		int counter = 0;
		while   (! downloadedFile.getName().contains(fileName) ||  downloadedFile.getName().contains("crdownload")) {
			OrangeUtils.wait(5000);
			downloadedFile = OrangeUtils.getlastModifiedFile(AppConstants.DOWNLOAD_FOLDER_PATH);
			counter ++;
			if(counter ==6)
				break;
		}
		return downloadedFile.getName();
	}
	
	public static void jsClickWebElement(WebDriver driver, WebElement element) {
		OrangeUtils.wait(2000);
		if(element.isDisplayed() && element.isEnabled()) {
			JavascriptExecutor jsExecutor = (JavascriptExecutor) LocalDriver.getDriver();
			log.info("Click using JS on " + element.getText());
			jsExecutor.executeScript("arguments[0].click();", element);
		}  
	}    
	
	public static void moveToWebElement(WebDriver driver,WebElement element) {
		new Actions (LocalDriver.getDriver()).moveToElement(element).build().perform();
	}
	
	public static boolean isVisible(WebElement element) {
		try {
			return element.isDisplayed();
		}
		catch(NoSuchElementException e) {
			return false;
		}
	}
	
	public static void scrolldown() {
		JavascriptExecutor jsExecutor = (JavascriptExecutor) LocalDriver.getDriver();
		jsExecutor.executeScript("document.querySelector('div.ag-body-viewport.ag-layout-normal.ag-row-no-animation').scrollBy(0,200)"); 
	}
	
	public static void scrollIntoView(WebElement element) {
		OrangeUtils.wait(2000);
		JavascriptExecutor jsExecutor = (JavascriptExecutor) LocalDriver.getDriver();
		jsExecutor.executeScript("arguments[0].scrollIntoView();", element);
		log.info("Scrolling on " + element.getText());
	}
	
	public static void runExecutable(String pathToExecutable, String uploadFilePath) {
		try {
			Runtime.getRuntime().exec("WScript " + pathToExecutable + " " + uploadFilePath);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
}
