package com.orangeHRM.stepdefs;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

import com.OrangeHRM.browsers.DriverFactory;
import com.OrangeHRM.browsers.LocalDriver;
import com.OrangeHRM.constants.AppConstants;
import com.OrangeHRM.customelements.OrangeGridElement;
import com.OrangeHRM.customelements.OrangeWebElement;
import com.OrangeHRM.pages.CommonMenuPage;
import com.OrangeHRM.utilities.AppConfig;
import com.OrangeHRM.utilities.ExcelUtilities;
import com.OrangeHRM.utilities.OrangeUtils;
import com.OrangeHRM.utilities.PDFUtilities;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.netty.util.internal.ThreadLocalRandom;

public class OrangeCommonSteps extends BaseSteps {

	@Then("^user captures the text of element \"([^\"]*)\" as \"([^\"]*)\"$")
	public void user_captures_the_text_of_element_as(String locator, String mapKey) {
		System.out.println(getWebElement(locator, LocalDriver.getDriver()).getText());
		scenarioData.get().put(mapKey, getWebElement(locator, LocalDriver.getDriver()).getText().trim());
	}

	@Then("^user captures the value of element \"([^\"]*)\" as \"([^\"]*)\"$")
	public void user_captures_the_value_of_element_as(String locator, String mapKey) {
		OrangeWebElement element = getWebElement(locator, LocalDriver.getDriver());
		scenarioData.get().put(mapKey, element.getAttribute("value"));
	}

	@Then("^user waits for file \"([^\"]*)\" to download$")
	public void user_waits_for_file_something_with_extension_something_to_download(String fileName) {
		fileName = getVariableValue(fileName);
		if (fileName.contains(".")) {
			fileName = fileName.split(".xlsx")[0];
		}
		String downloadedFile = OrangeUtils.waitForFileToDownload(fileName);
		Assert.assertTrue(downloadedFile.contains(fileName), "File failed to download in 30 seconds");

	}

	@Then("^user waits for file \"([^\"]*)\" to download and save file as \"([^\"]*)\"$")
	public void user_waits_for_file_to_download_and_save_file_as(String fileName, String variableName) {
		CommonMenuPage commonMenuPage = new CommonMenuPage(LocalDriver.getDriver());
		commonMenuPage.waitForPageToLoad();
		fileName = getVariableValue(fileName);
		OrangeUtils.wait(3000);
		File downloadedFile = OrangeUtils.getlastModifiedFile(AppConstants.DOWNLOAD_FOLDER_PATH);
		int counter = 0;

		while (!downloadedFile.getName().contains(fileName) || downloadedFile.getName().contains("crdownload")) {
			OrangeUtils.wait(10000);
			downloadedFile = OrangeUtils.getlastModifiedFile(AppConstants.DOWNLOAD_FOLDER_PATH);
			counter++;
			if (counter == 6)
				break;
		}
		scenarioData.get().put(variableName, downloadedFile.getName());
		Assert.assertTrue(downloadedFile.getName().contains(fileName), "File failed to download in 60 seconds");
	}

	@Then("^verify column \"([^\"]*)\" is present in downloaded excel file \"([^\"]*)\"$")
	public void verify_column_is_present_in_downloaded_excel_file(String columnName, String fileName) {
		fileName = getVariableValue(fileName);
		columnName = getVariableValue(columnName);

		ExcelUtilities excelUtilities = new ExcelUtilities(AppConstants.DOWNLOAD_FOLDER_PATH + fileName + ".xlsx");
		Map<String, List<String>> excelReportValuesMap = excelUtilities.readExcelReportAsMap();
		Assert.assertTrue(excelReportValuesMap.containsKey(columnName.trim()),
				"Expected column " + columnName + " is not present in the excel file\n");
	}

	@Then("^verify value \"([^\"]*)\" is present in column \"([^\"]*)\" in excel file \"([^\"]*)\"$")
	public void verify_value_is_present_in_column_in_excel_file(String value, String columnName, String fileName) {
		value = getVariableValue(value);
		columnName = getVariableValue(columnName);

		if (fileName.contains(".")) {
			fileName = fileName.split(".xlsx")[0];
		}

		ExcelUtilities excelUtilities = new ExcelUtilities(AppConstants.DOWNLOAD_FOLDER_PATH + fileName + ".xlsx");
		Map<String, List<String>> excelReportValuesMap = excelUtilities.readExcelReportAsMap();

		boolean found = false;
		if (!excelReportValuesMap.containsKey(columnName)) {
			Assert.fail("Column '" + columnName + " is not pesent in the downloaded excel");
		}

		List<String> listColumnValues = excelReportValuesMap.get(columnName);

		for (String transactionExplanation : listColumnValues) {
			if (transactionExplanation.contains(value)) {
				found = true;
				break;
			}
		}
		Assert.assertTrue(found, "Expected value " + value + " is not present in the column " + columnName + " \n");
	}

	@Then("^verify value \"([^\"]*)\" is not present in column \"([^\"]*)\" in excel file \"([^\"]*)\"$")
	public void verify_value_is_not_present_in_column_in_excel_file(String value, String columnName, String fileName) {
		value = getVariableValue(value);
		columnName = getVariableValue(columnName);
		if (fileName.contains(".")) {
			fileName = fileName.split(".xlsx")[0];
		}
		ExcelUtilities excelUtilities = new ExcelUtilities(AppConstants.DOWNLOAD_FOLDER_PATH + fileName + ".xlsx");
		Map<String, List<String>> excelReportValuesMap = excelUtilities.readExcelReportAsMap();

		if (!excelReportValuesMap.containsKey(columnName)) {
			Assert.fail("Column '" + columnName + " is not pesent in the downloaded excel");
		}

		List<String> listColumnValues = excelReportValuesMap.get(columnName);

		for (String transactionExplanation : listColumnValues) {
			if (transactionExplanation.contains(value)) {
				Assert.fail("Unexpected value " + value + " is present in the column " + columnName);
			}
		}

	}

	@Then("^user enters value \"([^\"]*)\" with random suffix into textbox \"([^\"]*)\"$")
	public void user_enters_value_with_random_suffix_into_textbox(String value, String locator) {
		value = getVariableValue(value);
		value = value + new Random().nextInt(100);
		getWebElement(locator, LocalDriver.getDriver()).sendKeys(value);
	}

	@Then("^user selects value \"([^\"]*)\" from angular dropdown \"([^\"]*)\"$")
	public void user_selects_value_from_angular_dropdown(String dropdownOption, String dropdownLocator) {
		dropdownOption = getVariableValue(dropdownOption);
		getWebElement(dropdownLocator, LocalDriver.getDriver()).click();
		OrangeUtils.wait(5000);

		List<WebElement> list = LocalDriver.getDriver()
				.findElements(By.xpath("//mat-option[*[normalize-space(text())='" + dropdownOption + "']]"));

		if (list.isEmpty()) {
			list = LocalDriver.getDriver().findElements(
					By.xpath("(//mat-option//*[normalize-space(text())='" + dropdownOption + "'])[last()]"));
		}

		if (list.isEmpty()) {
			LocalDriver.getDriver().findElements(
					By.xpath("//mat-option//*[contains(normalize-space(text()),'" + dropdownOption + "')]"));
		}

		if (list.isEmpty()) {
			LocalDriver.getDriver().findElements(By.xpath("//mat-option//*[@title='" + dropdownOption + "']"));
		}
		Assert.assertFalse(list.isEmpty(), "Value : " + dropdownOption + " is not found in dropdown");
	}

	@Then("^user selects checkbox with index \"([^\"]*)\" in grid \"([^\"]*)\"$")
	public void user_selects_checkbox_with_index_in_grid(String index, String gridLocator) {
		OrangeGridElement grid = getGridElement(gridLocator, LocalDriver.getDriver());
		WebElement element = null;
		if (grid.findElements(By.xpath(".//input[@type='checkbox']")).size() > 1) {
			element = grid.findElements(By.xpath(".//input[@type='checkbox']")).get(Integer.parseInt(index));
			OrangeUtils.scrollAndClick(element);
		} else if (grid.findElements(By.xpath(".//span[@class='ag-selection-checkbox']")).size() > 1) {
			element = grid.findElements(By.xpath(".//span[@class='ag-selection-checkbox']"))
					.get(Integer.parseInt(index));
			OrangeUtils.scrollAndClick(element);
		}
	}

	/*
	 * @And("^user selects checkbox \"([^\"]*)\"$") public void
	 * user_selects_checkbox(String locator) { OrangeWebElement checkbox =
	 * getWebElement(locator, LocalDriver.getDriver()); checkbox.waitforVisible();
	 * if(checkbox.getAttribute("aria-checked"). equalsIgnoreCase("false")) {
	 * checkbox.click(); } }
	 */

	@And("^user selects checkbox \"([^\"]*)\"$")
	public void user_selects_checkbox(String locator) {
		OrangeWebElement checkbox = getWebElement(locator, LocalDriver.getDriver());
		checkbox.waitforVisible();
		if (!checkbox.isSelected()) {
			checkbox.click();
		}
	}

	/*
	 * @And("^user deselects checkbox \"([^\"]*)\"$") public void
	 * user_deselects_checkbox(String locator) { OrangeWebElement checkbox =
	 * getWebElement(locator, LocalDriver.getDriver()); checkbox.waitforVisible();
	 * if(checkbox.getAttribute("aria-checked").equalsIgnoreCase("true")) {
	 * checkbox.click(); } }
	 */

	@And("^user deselects checkbox \"([^\"]*)\"$")
	public void user_deselects_checkbox(String locator) {
		OrangeWebElement checkbox = getWebElement(locator, LocalDriver.getDriver());
		checkbox.waitforVisible();
		if (checkbox.isSelected()) {
			checkbox.click();
		}
	}

	/**
	 * 
	 * @param value
	 * @param locator : Pass the list as locator(ex. //div[@role='listbox'])
	 */

	@Then("^verify value \"([^\"]*)\" is displayed in list \"([^\"]*)\"$")
	public void verify_value_is_displayed_in_list(String value, String locator) {
		value = getVariableValue(value);
		OrangeWebElement list = getWebElement(locator, LocalDriver.getDriver());
		list.waitforVisible();
		boolean found = false;
		List<WebElement> serachResults = list.findElements(By.xpath("//div"));
		List<String> actualTextList = new ArrayList<>();

		for (WebElement element : serachResults) {
			actualTextList.add(element.getText());
		}

		for (String name : actualTextList) {
			if (name.contains(value)) {
				found = true;
				break;
			}
		}
		Assert.assertTrue(found, "Expected " + value + " is not visible in list\n");
	}

	@Then("^verify value \"([^\"]*)\" is not displayed in list \"([^\"]*)\"$")
	public void verify_value_is_not_displayed_in_list(String value, String locator) {
		value = getVariableValue(value);
		OrangeWebElement list = getWebElement(locator, LocalDriver.getDriver());
		list.waitforVisible();
		List<WebElement> serachResults = list.findElements(By.xpath("//div"));
		List<String> actualTextList = new ArrayList<>();

		for (WebElement element : serachResults) {
			actualTextList.add(element.getText());
		}

		for (String name : actualTextList) {
			if (name.contains(value)) {
				Assert.fail("Unexpected " + value + " is visible in list\n");
			}
		}
	}

	@Then("^verify checkbox \"([^\"]*)\" is selected$")
	public void verify_checkbox_is_selected(String locator) {
		OrangeWebElement checkbox = getWebElement(locator, LocalDriver.getDriver());
		checkbox.waitforVisible();
		/*
		 * if(checkbox.getAttribute("aria-checked").equalsIgnoreCase("false")) {
		 * Assert.fail("Checkbox is not selected \n"); }
		 */
		if (checkbox.isSelected()) {
			Assert.fail("Checkbox is not selected \n");
		}

	}

	@Then("^verify checkbox \"([^\"]*)\" is not selected$")
	public void verify_checkbox_is_not_selected(String locator) {
		OrangeWebElement checkbox = getWebElement(locator, LocalDriver.getDriver());
		checkbox.waitforVisible();
		/*
		 * if(checkbox.getAttribute("aria-checked").equalsIgnoreCase("true")) {
		 * Assert.fail("Checkbox is not selected \n"); }
		 */
		if (!checkbox.isSelected()) {
			Assert.fail("Checkbox is selected \n");
		}
	}

	@When("^user selects all checkbox in column settings panel$")
	public void user_selects_all_checkbox_in_column_settings_panel() {
		CommonMenuPage commonMenuPage = new CommonMenuPage(LocalDriver.getDriver());
		commonMenuPage.headerColumnSettings.waitforVisible();
		for (WebElement checkbox : commonMenuPage.listCheckboxColumnSettings) {
			/*
			 * if(checkbox.getAttribute("aria-checked").equalsIgnoreCase("false")) {
			 * OrangeUtils.scrollAndClick(checkbox); }
			 */
			if (!checkbox.isSelected()) {
				OrangeUtils.scrollAndClick(checkbox);
			}
			commonMenuPage.buttonSaveColumnSettings.click();
			commonMenuPage.waitForPageToLoad();
			OrangeUtils.wait(10000);

		}
	}

	@When("^user deselects all checkbox in column settings panel$")
	public void user_deselects_all_checkbox_in_column_settings_panel() {
		CommonMenuPage commonMenuPage = new CommonMenuPage(LocalDriver.getDriver());
		commonMenuPage.headerColumnSettings.waitforVisible();
		for (WebElement checkbox : commonMenuPage.listCheckboxColumnSettings) {
			/*
			 * if(checkbox.getAttribute("aria-checked").equalsIgnoreCase("true")) {
			 * OrangeUtils.scrollAndClick(checkbox); }
			 */
			if (checkbox.isSelected()) {
				OrangeUtils.scrollAndClick(checkbox);
			}
			commonMenuPage.buttonSaveColumnSettings.click();
			commonMenuPage.waitForPageToLoad();
			OrangeUtils.wait(10000);

		}
	}

	@Then("^user waits for \"([^\"]*)\" minutes$")
	public void user_waits_for_minutes(String time) {
		time = getVariableValue(time);
		OrangeUtils.wait(Integer.parseInt(time) * 60 * 1000);
	}

	@Then("^user waits for \"([^\"]*)\" seconds$")
	public void user_waits_for_seconds(String time) {
		time = getVariableValue(time);
		OrangeUtils.wait(Integer.parseInt(time) * 1000);
	}

	@And("^user press ESC button$")
	public void user_press_esc_button() {
		OrangeUtils.wait(1500);
		Robot robot;
		try {
			robot = new Robot();
			robot.keyPress(KeyEvent.VK_ESCAPE);
			robot.keyRelease(KeyEvent.VK_ESCAPE);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	@And("^user press keyboard \"([^\"]*)\" button$")
	public void user_press_keyboard_button(String key) {
		Actions actionBuilder = new Actions(LocalDriver.getDriver());
		actionBuilder.sendKeys(key).build().perform();
	}
	
	@And("^user perform a page refresh$")
	public void user_perform_page_refresh() {
		LocalDriver.getDriver().navigate().refresh();
	}

	@When("^user expands vertical accordian \"([^\"]*)\"$")
	public void user_expands_accordian(String locator) {
		OrangeWebElement element = getWebElement(locator, LocalDriver.getDriver());
		WebElement webElement = element.getElement();
		/*
		 * String style = webElement.findElement(By.
		 * xpath("//button[@class='oxd-icon-button oxd-main-menu-button']"))
		 * .getAttribute("style"); if (style.contains("rotate(0deg)")) {
		 * element.click(); OrangeUtils.wait(2000); }
		 * Assert.assertTrue(webElement.findElement(By.
		 * xpath("//button[@class='oxd-icon-button oxd-main-menu-button']"))
		 * .getAttribute("style").contains("rotate(180deg)"),
		 * "Accordian failed to expand");
		 */
		String style = webElement.findElement(By.xpath("//button[@class='oxd-icon-button oxd-main-menu-button']//i"))
				.getAttribute("class");
		if(style.contains("oxd-icon bi-chevron-right")) {
			element.click();
			OrangeUtils.wait(2000);
		}
		Assert.assertTrue(webElement.findElement(By.xpath("//button[@class='oxd-icon-button oxd-main-menu-button']//i"))
				.getAttribute("class").contains("oxd-icon bi-chevron-left"), "Accordian failed to collapse");

	}

	@When("^user collapse vertical accordian \"([^\"]*)\"$")
	public void user_collapse_vertical_accordian(String locator) {
		OrangeWebElement element = getWebElement(locator, LocalDriver.getDriver());
		WebElement webElement = element.getElement();

		/*
		 * String style = webElement.findElement(By.
		 * xpath("//button[@class='oxd-icon-button oxd-main-menu-button']"))
		 * .getAttribute("style"); if (style.contains("rotate(180deg)")) {
		 * element.click(); OrangeUtils.wait(2000); }
		 * Assert.assertTrue(webElement.findElement(By.
		 * xpath("//button[@class='oxd-icon-button oxd-main-menu-button']"))
		 * .getAttribute("style").contains("rotate(0deg)"),
		 * "Accordian failed to collapse");
		 */
		String style = webElement.findElement(By.xpath("//button[@class='oxd-icon-button oxd-main-menu-button']//i"))
				.getAttribute("class");
		if(style.contains("oxd-icon bi-chevron-left")) {
			element.click();
			OrangeUtils.wait(2000);
		}
		Assert.assertTrue(webElement.findElement(By.xpath("//button[@class='oxd-icon-button oxd-main-menu-button']//i"))
				.getAttribute("class").contains("oxd-icon bi-chevron-right"), "Accordian failed to collapse");
	}

	@When("^verify vertical accordian \"([^\"]*)\" is collapsed$")
	public void verify_vertical_accordian_is_collapsed(String locator) {
		OrangeWebElement element = getWebElement(locator, LocalDriver.getDriver());
		WebElement webElement = element.getElement();
		Boolean flag = false;
		String style = webElement.findElement(By.xpath("//button[@class='oxd-icon-button oxd-main-menu-button']//i"))
				.getAttribute("class");
		if (style.contains("oxd-icon bi-chevron-right")) {
			flag = true;
		}
		Assert.assertTrue(flag, "Accordian is not collapsed as expected");
	}

	@When("^verify vertical accordian \"([^\"]*)\" is expanded$")
	public void verify_vertical_accordian_is_expanded(String locator) {
		OrangeWebElement element = getWebElement(locator, LocalDriver.getDriver());
		WebElement webElement = element.getElement();
		Boolean flag = false;
		String style = webElement.findElement(By.xpath("//button[@class='oxd-icon-button oxd-main-menu-button']//i"))
				.getAttribute("class");
		if (style.contains("oxd-icon bi-chevron-left")) {
			flag = true;
		}
		Assert.assertTrue(flag, "Accordian is not expanded as expected");
	}
	
	@Then("^verify downloaded print contains value \"([^\"]*)\"$")
	public void verify_downloaded_print_contains_value(String expectedValue) {
		expectedValue = getVariableValue(expectedValue);
		PDFUtilities pdfUtilities;
		parentWindowHandle.set(LocalDriver.getDriver().getWindowHandle());
		OrangeUtils.switchToNewWindow(LocalDriver.getDriver());
		OrangeUtils.wait(2000);
		File file = OrangeUtils.getlastModifiedFile(System.getProperty("user.home") + "/Downloads/");
		pdfUtilities = new PDFUtilities(file);
		String pdfdata = pdfUtilities.readPdfAsString();
		Assert.assertTrue(pdfdata.contains(expectedValue), "Downloaded PDF does not contains value");
		LocalDriver.getDriver().switchTo().window(parentWindowHandle.get());
	}
	
	/**
	 * Before using to this method , Click on calendar first, means calendar from which you want to select date
	 * @param strDate
	 */
	@And("^user selects date \"([^\"]*)\" given in YYYY-M-dd format from \"([^\"]*)\" calender$")
	public void user_selects_date_from_datepicker(String strDate, String locator) {
		OrangeWebElement datedropdown = getWebElement(locator, LocalDriver.getDriver());
		strDate =  getVariableValue(strDate);
		CommonMenuPage commonMenuPage = new CommonMenuPage(LocalDriver.getDriver());
		String selectedDate = null;
		String[] strArray = strDate.split("-");
		String year = strArray[0];
		String month = commonMenuPage.getMonthName(strDate);
		String date = strArray[2];
		if(date.startsWith("0")) {
			date = date.replace("0", "");
		}
		commonMenuPage.indicatorMonthYear.waitforVisible(); 
		String yearLocator = "//li[contains(@class,'oxd-calendar-dropdown--option') and text()='" + year + "']" ;
		String monthLocator = "//li[contains(@class,'oxd-calendar-dropdown--option') and text()='" + month + "']" ;
		String dayLocator = "//div[@class='oxd-calendar-date'][normalize-space()='"+date +"']";
		
		commonMenuPage.buttonChooseYear.click();
		WebElement element = LocalDriver.getDriver().findElement(By.xpath(yearLocator));
		OrangeUtils.wait(2000);
		OrangeUtils.jsClickWebElement(LocalDriver.getDriver(), element);

		
		commonMenuPage.buttonChooseMonth.click();
		element = LocalDriver.getDriver().findElement(By.xpath(monthLocator));
		OrangeUtils.wait(2000);
		OrangeUtils.jsClickWebElement(LocalDriver.getDriver(), element);
		
		element = LocalDriver.getDriver().findElement(By.xpath(dayLocator));
		OrangeUtils.wait(2000);
		OrangeUtils.jsClickWebElement(LocalDriver.getDriver(), element);
		selectedDate = datedropdown.getAttribute("value").trim();
		Assert.assertEquals(selectedDate, strDate,  "Selected date is not matched with given date");
	}
	
	@Then("^user captures text of element \"([^\"]*)\" and save as \"([^\"]*)\" in runtime properties file$")
	public void user_captures_text_of_element_and_save_as_in_runtime_properties_file(String locator, String saveAs) {
	    OrangeWebElement element =  getWebElement(locator, LocalDriver.getDriver());
	    String capturedText = element.getText().trim();
	    AppConfig.setProperty(saveAs, capturedText, AppConstants.RUNTIME_PROPERTY_PATH);
	}
	
	@Then("^user captures text of element \"([^\"]*)\" by using attribute and save as \"([^\"]*)\" in runtime properties file$")
	public void user_captures_text_of_element_by_using_attribute_and_save_as_in_runtime_properties_file(String locator, String saveAs) {
	    OrangeWebElement element =  getWebElement(locator, LocalDriver.getDriver());
	    String capturedText = element.getAttribute("value").trim();
	    AppConfig.setProperty(saveAs, capturedText, AppConstants.RUNTIME_PROPERTY_PATH);
	}
	
	@Then("^user selects previous month last date from \"([^\"]*)\" datepicker$")
	public void user_selects_previous_month_last_date_from_datepicker(String locator) {
		CommonMenuPage commonMenuPage = new CommonMenuPage(LocalDriver.getDriver());
		String previousMonthLatDate = commonMenuPage.getPreviousMonthLastDate();
		user_selects_date_from_datepicker(previousMonthLatDate , locator);
	}
	
	@And("^user enters value with random number into textbox \"([^\"]*)\" and store in variable as \"([^\"]*)\"$")
	public void user_enters_value_with_random_number_into_textbox_and_store_in_variable_as(String locator, String mapKey) {
		 OrangeWebElement element = getWebElement(locator, LocalDriver.getDriver());
		 double randomNumber = ThreadLocalRandom.current().nextDouble(1,3);
		 DecimalFormat df_obj = new DecimalFormat("#.#");
		 String formatedNumber = df_obj.format(randomNumber);
		 scenarioData.get().put(mapKey, formatedNumber);
		 element.sendKeys(formatedNumber);
	}
	
	@Then("^verify exported file name \"([^\"]*)\" in downloads$")
	public void verify_exported_file_name_in_downloads(String fileName) {
		fileName = getVariableValue(fileName);
		File downloadedFile = OrangeUtils.getlastModifiedFile(AppConstants.DOWNLOAD_FOLDER_PATH);
		int counter = 0;
		
		while(!downloadedFile.getName().contains(fileName)) {
			OrangeUtils.wait(5000);
			downloadedFile = OrangeUtils.getlastModifiedFile(AppConstants.DOWNLOAD_FOLDER_PATH);
			counter++;
			if(counter == 6) {
				break;
			}
		}
		Assert.assertTrue(downloadedFile.getName().contains(fileName), "File failed to download in 30 seconds");
	}
	
	@And("^verify value of \"([^\"]*)\" in runtime properties file is equal to \"([^\"]*)\"$")
	public void verify_value_of_something_in_runtime_proprty_file_is_equal_to_something(String key, String actualValue) {
		OrangeWebElement value = getWebElement(actualValue, LocalDriver.getDriver());
		actualValue = value.getText();
		String expectedValue = AppConfig.getProperty(key, AppConstants.RUNTIME_PROPERTY_PATH);
		Assert.assertEquals(actualValue, expectedValue , "Actual value : " + actualValue + " does not matched withe expected value : " + expectedValue);
	}
	
	@And("^user opens new browser$")
	public void user_opens_new_browser() {
		 WebDriver driver =  DriverFactory.getDriver(AppConstants.BROWSER_TYPE);
		 driver.manage().window().maximize();
		 driver.get(AppConstants.getApplicationURL());
		 LocalDriver.setDriver(driver);
	}
	
	@And("^user closes current browser instance")
	public void user_closes_current_browser() {
		LocalDriver.getDriver().close();
	}
	
	
	@When("^user reset default state of \"([^\"]*)\" screen$")
	public void user_reset_default_state_of_something_screen(String pageClassName) {
		Class<?> clazz = getClassObject(pageClassName);
		invokeMethod(LocalDriver.getDriver(), clazz, "setDefaultState");
	}
}
