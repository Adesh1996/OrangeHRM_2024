package com.orangeHRM.stepdefs;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

import com.OrangeHRM.browsers.LocalDriver;
import com.OrangeHRM.constants.AppConstants;
import com.OrangeHRM.customelements.OrangeGridElement;
import com.OrangeHRM.pages.CommonMenuPage;
import com.OrangeHRM.utilities.CSVUtilities;
import com.OrangeHRM.utilities.OrangeUtils;
import com.OrangeHRM.utilities.PDFUtilities;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class OrangeGridSteps extends BaseSteps {

	protected Logger log = LogManager.getLogger();

	@And("^user waits for grid to load on \"([^\"]*)\" screen$")
	public void user_waits_for_grid_to_load_on_screen(String className) {
		if (className != null) {
			Class<?> clazz = getClassObject(className);
			invokeMethod(LocalDriver.getDriver(), clazz, "waitForGridToLoad");
		}
	}

	@Then("^verify text \"([^\"]*)\" is present in column \"([^\"]*)\" in grid \"([^\"]*)\"$")
	public void verify_text_is_present_in_column_in_grid(String text, String columnName, String gridLocator) {
		text = getVariableValue(text);
		OrangeGridElement grid = getGridElement(gridLocator, LocalDriver.getDriver());
		CommonMenuPage commonMenuPage = new CommonMenuPage(LocalDriver.getDriver());
		commonMenuPage.waitForPageToLoad();

		List<String> columnValueList = grid.getGridColumnValues(columnName);
		if (columnValueList.isEmpty()) {
			OrangeUtils.scrollGridHorizontallyRightUntilHeaderIsVisible(columnName, grid);
			columnValueList = grid.getGridColumnValues(columnName);
		}

		boolean found = false;
		for (String string : columnValueList) {
			if (string.contains(text)) {
				found = true;
				break;
			}
		}
		Assert.assertTrue(found, "Given text '" + text + "' is not present in grid\n");

	}

	@Then("^verify text \"([^\"]*)\" is present in all the values of column \"([^\"]*)\" in grid \"([^\"]*)\"$")
	public void verify_text_is_present_in_all_the_values_of_column_in_grid(String text, String columnName,
			String gridLocator) {
		text = getVariableValue(text);
		OrangeGridElement grid = getGridElement(gridLocator, LocalDriver.getDriver());
		OrangeUtils.wait(3000);

		List<String> columnValueList = grid.getGridColumnValues(columnName);
		Assert.assertTrue(columnValueList.isEmpty(), "Given column " + columnName + " has no data");

		for (String string : columnValueList) {
			if (!string.contains(text)) {
				Assert.fail("Expected value ' " + text + " ' is present in grid");
			}
		}
	}

	@Then("^verify text \"([^\"]*)\" is not present in all the values of column \"([^\"]*)\" in grid \"([^\"]*)\"$")
	public void verify_text_is_not_present_in_all_the_values_of_column_in_grid(String text, String columnName,
			String gridLocator) {
		text = getVariableValue(text);
		OrangeGridElement grid = getGridElement(gridLocator, LocalDriver.getDriver());
		OrangeUtils.wait(3000);

		List<String> columnValueList = grid.getGridColumnValues(columnName);

		for (String string : columnValueList) {
			if (string.contains(text)) {
				Assert.fail("UnExpected value ' " + text + " ' is present in grid");
			}
		}
	}

	@When("^user clicks on link \"([^\"]*)\" of column \"([^\"]*)\" in grid \"([^\"]*)\"$")
	public void user_clicks_on_link_of_column_in_grid(String linkText, String columnName, String gridLocator) {
		linkText = getVariableValue(linkText);
		OrangeGridElement grid = getGridElement(gridLocator, LocalDriver.getDriver());
		boolean found = false;
		List<WebElement> columnElements = grid.getGridColumnWebElements(columnName);
		Assert.assertTrue(columnElements.isEmpty(),
				"Column name " + columnName + " ' is either incorrect or not visible in the grid");

		for (WebElement element : columnElements) {
			if (element.getText().trim().contains(linkText)) {
				try {
					OrangeUtils.scrollIntoView(element);
					element.findElement(By.xpath(".//a[normalize-space(text()) = '" + linkText + "']")).click();
					found = true;
					break;
				} catch (Exception e) {
					element.findElement(By.xpath(".//a")).click();
					found = true;
					break;
				}
			}
		}

		if (found == false) {
			OrangeUtils.scrolldown();
			for (WebElement element : columnElements) {
				if (element.getText().trim().contains(linkText)) {
					found = true;
					element.findElement(By.xpath(".//a")).click();
					break;

				}
			}
		}
		Assert.assertTrue(found, "Link with text " + linkText + " is not found in grid");
	}

	@Then("^verify values in column \"([^\"]*)\" should display upto \"(\\d+)\" decimal places in grid \"([^\"]*)\"$")
	public void verify_values_in_column_should_display_upto_decimal_places_in_grid(String columnName, int decimalPlaces,
			String gridLocator) {
		OrangeGridElement grid = getGridElement(gridLocator, LocalDriver.getDriver());
		List<String> columnValues = grid.getGridColumnValues(columnName);
		for (String value : columnValues) {
			if (value.contains(".")) {
				Assert.assertTrue(value.substring(value.indexOf('.') + 1).length() <= decimalPlaces,
						"Column Values have decimal digits with more than decimal places\n");
			}
		}

	}

	@When("^user clicks on link corresponding to text \"([^\"]*)\" in grid \"([^\"]*)\"$")
	public void user_clicks_on_link_corresponding_to_text_in_grid(String text, String gridLocator) {
		OrangeGridElement grid = getGridElement(gridLocator, LocalDriver.getDriver());
		text = getVariableValue(text);
		List<WebElement> elements = grid
				.findElements(By.xpath(".//[text()='" + text + "']/ancestor::div[@role='row']//a"));
		if (elements.isEmpty()) {
			Assert.fail("Link corresponding to text " + text + " is not found in the given grid");
		}
		elements.get(0).click();
		// OrangeUtils.scrollAndClick(elements.get(0));
	}

	@When("^user selects action \"([^\"]*)\" for text \"([^\"]*)\" in grid \"([^\"]*)\"$")
	public void user_selects_action_for_text_of_column_in_grid(String actionName, String columnValue,
			String gridLocator) {
		actionName = getVariableValue(actionName);
		columnValue = getVariableValue(columnValue);
		OrangeGridElement grid = getGridElement(gridLocator, LocalDriver.getDriver());

		OrangeUtils.wait(3000);

		try {
			int elementindex = Integer.parseInt(columnValue);
			List<WebElement> elements = grid.findElements(By.xpath(
					".//*[normalize-space(text())='']/ancestor::div[@role='row']//*[normalize-space(text())='Select']"));
			Assert.assertFalse(elements.isEmpty(), "Actions menu is not found in grid\n");
			OrangeUtils.scrollAndClick(elements.get(elementindex));
			OrangeUtils.wait(2000);

		} catch (Exception e) {
			List<WebElement> elements = grid.findElements(By.xpath(".//*[normalize-space(text())=' " + columnValue
					+ "']/ancestor::div[@role='row']//*[normalize-space(text())='Select']"));
			Assert.assertFalse(elements.isEmpty(), "Actions menu is not found in grid\n");
			OrangeUtils.scrollAndClick(elements.get(0));
			OrangeUtils.wait(2000);
		}

		try {
			OrangeUtils.scrollAndClick(LocalDriver.getDriver()
					.findElement(By.xpath(".//span[normalize-space(text())= '" + actionName + "'][last()]")));
		} catch (ElementNotInteractableException e) {
			OrangeUtils.scrollGridHorizontallyRightUntilHeaderIsVisible("Action", grid);
			OrangeUtils.scrollAndClick(LocalDriver.getDriver()
					.findElement(By.xpath(".//span[normalize-space(text())= '" + actionName + "'][last()]")));
		}

	}

	@And("^user clicks on link in column \"([^\"]*)\" with index \"([^\"]*)\" in grid \"([^\"]*)\"$")
	public void user_clicks_on_link_in_column_with_index_in_grid(String columnName, String index, String gridLocator) {
		columnName = getVariableValue(columnName);
		int indexValue = Integer.parseInt(index);
		OrangeGridElement grid = getGridElement(gridLocator, LocalDriver.getDriver());

		List<WebElement> columnElements = grid.getGridColumnWebElements(columnName);

		if (columnElements.isEmpty()) {
			Assert.assertFalse(columnElements.isEmpty(),
					"Either column name is wrong or no data is present in column : " + columnName + " \n");

		}
		OrangeUtils.scrollAndClick(columnElements.get(indexValue).findElement(By.xpath(".//a")));

	}

	@Then("^verify column \"([^\"]*)\" is empty in grid \"([^\"]*)\"$")
	public void verify_column_is_empty_in_grid(String columnName, String gridLocator) {
		columnName = getVariableValue(columnName);
		OrangeGridElement grid = getGridElement(gridLocator, LocalDriver.getDriver());
		List<String> columnVales = grid.getGridColumnValues(columnName);
		Assert.assertTrue(columnVales.isEmpty(), "Values are present in grid \n");
	}

	@Then("^verify column \"([^\"]*)\" is not empty in grid \"([^\"]*)\"$")
	public void verify_column_is_not_empty_in_grid(String columnName, String gridLocator) {
		columnName = getVariableValue(columnName);
		OrangeGridElement grid = getGridElement(gridLocator, LocalDriver.getDriver());
		List<String> columnVales = grid.getGridColumnValues(columnName);
		Assert.assertFalse(columnVales.isEmpty(), "Values are not present in grid \n");
	}

	@Then("^verify exported csv \"([^\"]*)\" with grid \"([^\"]*)\"$")
	public void verify_exported_csv_with_grid(String csvFileName, String gridLocator) {
		csvFileName = getVariableValue(csvFileName);
		Map<String, List<String>> uiValuesMap = getGridElement(gridLocator, LocalDriver.getDriver())
				.getGridUiValueMap();
		Assert.assertFalse(uiValuesMap.isEmpty(), "UI grid values could not be captured or is blank");
		CSVUtilities csvUtilities = new CSVUtilities(AppConstants.DOWNLOAD_FOLDER_PATH + csvFileName);
		Map<String, List<String>> csvFileMap = csvUtilities.readCsvFileAsMap();

		if (uiValuesMap.containsKey("Sequence number")) {
			uiValuesMap.get("Sequence number").remove("TOTAL");
		}

		Assert.assertFalse(csvFileMap.isEmpty(), "Exported CSV file has no data");
		Assert.assertTrue(OrangeUtils.verifyMaps(csvFileMap, uiValuesMap),
				"Grid values are not matching with exported csv file. \n");
	}

	@Then("^verify column \"([^\"]*)\" is present in grid \"([^\"]*)\"$")
	public void verify_column_is_present_in_grid(String columnName, String gridLocator) {
		columnName = getVariableValue(columnName);
		OrangeGridElement grid = getGridElement(gridLocator, LocalDriver.getDriver());
		boolean found = false;

		if (grid.getGridColumnHeaderNames().contains(columnName)) {
			found = true;
		} else {
			OrangeUtils.scrollGridHorizontallyRightUntilHeaderIsVisible(columnName, grid);
			OrangeUtils.scrollGridHorizontallyLeft(grid);
			found = true;
		}
		Assert.assertTrue(found, "Column header " + columnName + " is not present in grid.\n");
	}

	@Then("^verify column \"([^\"]*)\" is not present in grid \"([^\"]*)\"$")
	public void verify_column_is_not_present_in_grid(String columnName, String gridLocator) {
		columnName = getVariableValue(columnName);
		OrangeGridElement grid = getGridElement(gridLocator, LocalDriver.getDriver());
		List<String> columnHeader = grid.getGridColumnHeaderNames();
		Assert.assertFalse(columnHeader.contains(columnName), "Column header " + columnName + " is present in grid.\n");
	}

	@And("^user reorders grid \"([^\"]*)\" by changing position of headers$")
	public void user_reorders_grid_by_changing_position_of_headers(String gridLocator) {
		OrangeGridElement grid = getGridElement(gridLocator, LocalDriver.getDriver());
		List<WebElement> webElements = grid.getGridColumnHeaderWebElements();
		WebElement firstHeader = webElements.get(0);
		WebElement lastHeader = webElements.get(webElements.size() - 1);
		Actions builder = new Actions(LocalDriver.getDriver());
		builder.dragAndDrop(firstHeader, lastHeader).build().perform();
		OrangeUtils.wait(2000);

	}

	@When("^user captures first column header from grid \"([^\"]*)\" as \"([^\"]*)\"$")
	public void user_captures_first_column_header_from_grid_as(String gridLocator, String mapkey) {
		OrangeGridElement grid = getGridElement(gridLocator, LocalDriver.getDriver());
		List<String> columnValues = grid.getGridColumnHeaderNames();
		scenarioData.get().put(mapkey, columnValues.get(1));
	}

	@Then("^user captures value with index \"([^\"]*)\" in column \"([^\"]*)\" from grid \"([^\"]*)\" as \"([^\"]*)\"$")
	public void user_captures_value_with_index_in_column_from_grid_as(String index, String columnName,
			String gridLocator, String mapKey) {
		OrangeGridElement grid = getGridElement(gridLocator, LocalDriver.getDriver());
		OrangeUtils.scrollGridHorizontallyRightUntilHeaderIsVisible(columnName, grid);
		scenarioData.get().put(mapKey, grid.getGridColumnValues(columnName).get(Integer.parseInt(index)));
	}

	@Then("^verify first column header should be \"([^\"]*)\" in grid \"([^\"]*)\"$")
	public void verify_first_column_header_should_be_in_grid(String headerName, String gridLocator) {
		OrangeGridElement grid = getGridElement(gridLocator, LocalDriver.getDriver());
		headerName = getVariableValue(headerName);
		List<String> columnHeaders = grid.getGridColumnHeaderNames();
		Assert.assertTrue(columnHeaders.get(0).contains(headerName),
				"First column header is not matching. Actual header is " + columnHeaders.get(0)
						+ " and expected header is " + headerName + "\n");
	}

	@Then("^verify first column header should not be \"([^\"]*)\" in grid \"([^\"]*)\"$")
	public void verify_first_column_header_should_not_be_in_grid(String headerName, String gridLocator) {
		OrangeGridElement grid = getGridElement(gridLocator, LocalDriver.getDriver());
		headerName = getVariableValue(headerName);
		List<String> columnHeaders = grid.getGridColumnHeaderNames();
		Assert.assertFalse(columnHeaders.get(0).contains(headerName),
				"First column header is matching. Actual header is " + columnHeaders.get(0) + " and expected header is "
						+ headerName + "\n");
	}

	@And("^verify text \"([^\"]*)\" is present in print current page$")
	public void verify_text_is_present_in_print_current_page(String text) {
		PDFUtilities pdfUtilities;
		text = getVariableValue(text);
		String s[] = text.split(" ");
		File file = OrangeUtils.getlastModifiedFile(AppConstants.DOWNLOAD_FOLDER_PATH);
		pdfUtilities = new PDFUtilities(file);
		String pdfdata = pdfUtilities.readPdfAsString();

		for (int i = 0; i < s.length; i++) {
			Assert.assertTrue(pdfdata.contains(s[i]), "Given text " + text + " is not present in print current page");
		}
	}

	@Then("^verify ascending sort on column \"([^\"]*)\" in grid \"([^\"]*)\"$")
	public void verify_ascending_sort_on_column_in_grid(String columnName, String gridLocator) {
		OrangeGridElement grid = getGridElement(gridLocator, LocalDriver.getDriver());
		columnName = getVariableValue(columnName);

		List<String> ascendingListOfValues = grid.getGridColumnValues(columnName);
		List<String> tempList = new ArrayList<>(ascendingListOfValues);
		Collections.sort(tempList);
		Assert.assertTrue(ascendingListOfValues.equals(tempList),
				"\nColumn values are not sorted in ascending order. \nExpected values: \n" + tempList
						+ "\nActual values:\n" + ascendingListOfValues + "\n");
	}

	@Then("^verify descending sort on column \"([^\"]*)\" in grid \"([^\"]*)\"$")
	public void verify_descending_sort_on_column_in_grid(String columnName, String gridLocator) {
		OrangeGridElement grid = getGridElement(gridLocator, LocalDriver.getDriver());
		columnName = getVariableValue(columnName);

		List<String> descendingListOfValues = grid.getGridColumnValues(columnName);
		List<String> tempList = new ArrayList<>(descendingListOfValues);
		Collections.sort(tempList, Collections.reverseOrder());
		Assert.assertTrue(descendingListOfValues.equals(tempList),
				"\nColumn values are not sorted in descending order. \nExpected values: \n" + tempList
						+ "\nActual values:\n" + descendingListOfValues + "\n");
	}

	@Then("^verify column \"([^\"]*)\" has zero values in grid \"([^\"]*)\"$")
	public void verify_column_has_zero_values_in_grid(String columnName, String gridLocator) {
		columnName = getVariableValue(columnName);
		OrangeGridElement grid = getGridElement(gridLocator, LocalDriver.getDriver());
		List<String> columnValues = grid.getGridColumnValues(columnName);
		String totalValue = columnValues.get(columnValues.size() - 1);

		List<Double> values = new ArrayList<>();
		for (String value : columnValues) {
			if (!value.equals(totalValue)) {
				values.add(Double.valueOf(value));
			}
		}
		Double sum = values.stream().mapToDouble(Double::doubleValue).sum();
		Assert.assertTrue(sum == 0,
				"Sum of values is not equal to zero. \n Actual -> " + sum + "\n Expected -> " + totalValue);
	}

	@And("^user waits for grid \"([^\"]*)\" to be visible on screen$")
	public void user_waits_for_table_to_load_on_screen(String gridlocator) {
		getGridElement(gridlocator, LocalDriver.getDriver()).waitforVisible();
		OrangeUtils.wait(2000);
	}

	@And("^verify column \"([^\"]*)\" has only positive values in grid \"([^\"]*)\"$")
	public void verify_column_has_only_positive_values_in_grid(String columnName, String gridLocator) {
		columnName = getVariableValue(columnName);
		OrangeGridElement grid = getGridElement(gridLocator, LocalDriver.getDriver());

		List<String> columnValues = grid.getGridColumnValues(columnName);
		List<Double> values = new ArrayList<>();
		String totalValue = columnValues.get(columnValues.size() - 1);

		for (int i = 0; i < columnValues.size() - 1; i++) {
			String value = columnValues.get(i);
			if (value.equals(totalValue) && i != columnValues.size() - 1) {
				values.add(Double.valueOf(value));
			} else {
				values.add(Double.valueOf(value));
			}
		}
		Double minValue = values.stream().min(Double::compare).get();
		Assert.assertTrue(minValue >= 0, "Value is less than zero.\n Value -> " + minValue);
	}

	@And("^verify column \"([^\"]*)\" has only negative values in grid \"([^\"]*)\"$")
	public void verify_column_has_only_negative_values_in_grid(String columnName, String gridLocator) {
		columnName = getVariableValue(columnName);
		OrangeGridElement grid = getGridElement(gridLocator, LocalDriver.getDriver());

		List<String> columnValues = grid.getGridColumnValues(columnName);
		List<Double> values = new ArrayList<>();
		String totalValue = columnValues.get(columnValues.size() - 1);

		for (int i = 0; i < columnValues.size() - 1; i++) {
			String value = columnValues.get(i);
			if (value.equals(totalValue) && i != columnValues.size() - 1) {
				values.add(Double.valueOf(value));
			} else {
				values.add(Double.valueOf(value));
			}
		}
		Double minValue = values.stream().min(Double::compare).get();
		Assert.assertTrue(minValue < 0, "Value is less than zero.\n Value -> " + minValue);
	}

	@When("^user resets scroll bar on grid \"([^\"]*)\"$")
	public void user_resets_scroll_bar_on_grid(String gridLocator) {
		OrangeGridElement grid = getGridElement(gridLocator, LocalDriver.getDriver());
		OrangeUtils.scrollGridHorizontallyLeft(grid);
	}

	@When("^user scrolls grid \"([^\"]*)\" horizontally to action column$")
	public void user_scrolls_grid_horizontally_to_action_column(String gridLocator) {
		OrangeGridElement grid = getGridElement(gridLocator, LocalDriver.getDriver());
		OrangeUtils.scrollGridHorizontallyRightUntilHeaderIsVisible("Actions", grid);
	}

	@Then("^verify text \"([^\"]*)\" is present in grid \"([^\"]*)\"$")
	public void verify_text_is_present_in_grid(String text, String gridLocator) {
		text = getVariableValue(text);
		OrangeGridElement grid = getGridElement(gridLocator, LocalDriver.getDriver());
		grid.verifyElementPresentInGridByText(text);
	}

	@Then("^verify link text \"([^\"]*)\" is present in column \"([^\"]*)\" in grid \"([^\"]*)\"$")
	public void verify_link_text_is_present_in_column_in_grid(String linkText, String columnName, String gridLocator) {
		linkText = getVariableValue(linkText);
		OrangeGridElement grid = getGridElement(gridLocator, LocalDriver.getDriver());
		List<WebElement> columnWebElements = grid.getGridColumnWebElements(columnName);
		boolean found = false;
		for (WebElement element : columnWebElements) {
			if (element.findElement(By.xpath(".//a")).getText().trim().contains(linkText)) {
				found = true;
				break;
			}
		}
		Assert.assertTrue(found, "Given link text ' " + linkText + "' is not present in grid \n");
	}

	@When("^user clicks on link \"([^\"]*)\" corresponding to text \"([^\"]*)\" in grid \"([^\"]*)\"$")
	public void user_clicks_on_link_coresponding_to_text_in_grid(String linkText, String text, String gridLocator) {
		OrangeGridElement grid = getGridElement(gridLocator, LocalDriver.getDriver());
		linkText = getVariableValue(linkText);
		text = getVariableValue(text);
		grid.waitforVisible();
		OrangeUtils.wait(10000);

		List<WebElement> elements = grid.findElements(
				By.xpath("//div[text()='" + text + "']//parent::div//parent::div[@role='row']//div//button"));
		Assert.assertFalse(elements.isEmpty(),
				"Link '" + linkText + "' corresponding to text'" + text + "' is not found in the given table");
		// OrangeUtils.scrollAndClick(elements.get(0));
		elements.get(0).click();

	}

	@Then("^verify sum of values of columns \"([^\"]*)\" and \"([^\"]*)\" is displayed in column \"([^\"]*)\" in grid \"([^\"]*)\"$")
	public void verify_sum_of_values_of_columns_something_and_something_is_displayed_in_grid_something(String column1,
			String column2, String columnTotal, String gridLocator) {
		OrangeGridElement grid = getGridElement(gridLocator, LocalDriver.getDriver());
		List<String> column1Data = grid.getGridColumnValues(column1);
		List<String> column2Data = grid.getGridColumnValues(column2);
		List<String> columnTotalData = grid.getGridColumnValues(columnTotal);
		List<BigDecimal> actualSumList = new ArrayList<>();
		List<BigDecimal> expectedSumList = new ArrayList<>();

		for (int i = 0; i < columnTotalData.size(); i++) {
			expectedSumList.add(
					new BigDecimal(column1Data.get(i)).add(new BigDecimal(column2Data.get(i))).stripTrailingZeros());
			actualSumList.add(new BigDecimal(columnTotalData.get(i)));
		}
		Assert.assertTrue(expectedSumList.containsAll(actualSumList),
				"\n Expected : " + expectedSumList + "\n Actual : " + actualSumList);

	}

	@Then("^verify text \"([^\"]*)\" is present in panel list \"([^\"]*)\"$")
	public void verify_text_is_present_in_panel_list(String text, String panelLocator) {
		text = getVariableValue(text);
		List<WebElement> columnWebElements = LocalDriver.getDriver().findElements(By.xpath(panelLocator));

		for (WebElement element : columnWebElements) {

			if (element.getText().contains(text)) {
				Assert.fail("Unexpected value other than " + text + " is present.");
			}

		}

	}
	
	
	@Then("^verify all the values of column \"([^\"]*)\" in grid \"([^\"]*)\" are in upper case$")
	public void verify_all_the_values_of_column_in_grid_are_in_upper_case(String columnName, String gridLocator) {
		columnName = 	getVariableValue(columnName);
		OrangeGridElement grid =  getGridElement(gridLocator, LocalDriver.getDriver());
		List<String > columnValues = grid.getGridColumnValues(columnName);
		Assert.assertFalse(columnValues.isEmpty(), "Given column " + columnName + " has no data.");
		
		for(String string: columnValues) {
			if(!StringUtils.isAllUpperCase(string)) {
				Assert.fail("Value ' " + string + " is not in upper case");
			}
		}
	} 
	
	
	@Then("^verify \"([^\"]*)\" search condition with text \"([^\"]*)\" is present in all the values of column \"([^\"]*)\" in grid \"([^\"]*)\"$")
	public void verify_search_condition_with_text_is_present_in_all_values_of_column_in_grid(String condition, String text, String columnName,
			String gridLocator) {
		columnName = 	getVariableValue(columnName);
		condition = 	getVariableValue(condition);
		text = 	getVariableValue(text);
		OrangeGridElement grid =  getGridElement(gridLocator, LocalDriver.getDriver());
		List<String > columnValues = grid.getGridColumnValues(columnName);
		Assert.assertFalse(columnValues.isEmpty(), "Given column " + columnName + " has no data.");
		
		for(String string: columnValues) {
			switch (condition) {
			case "Contains":
				if(!string.contains(text)) {
					Assert.fail("Expected value ' " + text + "' is not present in grid");
				}
				continue;
			case "Equals":
				if(!string.equals(text)) {
					Assert.fail("Expected value ' " + text + "' is not present in grid");
				}
				continue;
				
			case "Begins With":
				if(!string.startsWith(text)) {
					Assert.fail("Expected value ' " + text + "' is not present in grid");
				}
				continue;
				
			case "Ends With":
				if(!string.endsWith(text)) {
					Assert.fail("Expected value ' " + text + "' is not present in grid");
				}
				continue;
				default:
					System.out.println("Condition not found");
					break;
			}
		}
		
	}
	
	@Then("^verify column \"([^\"]*)\" is \"([^\"]*)\" units or amount \"([^\"]*)\" in grid \"([^\"]*)\"$")
	public void verify_column_has_value_as_per_given_criteria(String columnName, String criteria, double unitsAmount, String gridLocator) {
		columnName = 	getVariableValue(columnName);
		String condition = 	getVariableValue(criteria);
		OrangeGridElement grid =  getGridElement(gridLocator, LocalDriver.getDriver());
		List<String> columnValues =  grid.getGridColumnValues(columnName);
		
		List <Double> values =  new ArrayList<>();
		Assert.assertFalse(columnValues.isEmpty(), "Given column " + columnName + " has no data.");
		for(String value: columnValues) {
			values.add(Double.parseDouble(value));
		}
		
		log.info("Captured values of " + columnName + ": " + values);
		
		switch(condition) {
		case "Equals":
			for(double value: values) {
				Assert.assertTrue(unitsAmount == value, " Column contain : " + value + " which is not equal to " + unitsAmount);
			}
			break;
		case "Greater Than":
			for(double value: values) {
				Assert.assertTrue( value > unitsAmount , " Column contain : " + value + " which is not greater than to" + unitsAmount);
			}
			break;
		case "Greater Than or Equal":
			for(double value: values) {
				Assert.assertTrue(value >= unitsAmount, " Column contain : " + value + " which is not greater than or equal to " + unitsAmount);
			}
			break;
		case "Less Than":
			for(double value: values) {
				Assert.assertTrue(value < unitsAmount, " Column contain : " + value + " which is not less than to" + unitsAmount);
			}
			break;
		case "Less Than or Equal":
			for(double value: values) {
				Assert.assertTrue(value <= unitsAmount, " Column contain : " + value + " which is not less than or equal to" + unitsAmount);
			}
			break;
		default:
			Assert.fail("Select proper condition");
			break;
		}
		
	}
}
