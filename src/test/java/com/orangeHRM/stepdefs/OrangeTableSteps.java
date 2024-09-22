package com.orangeHRM.stepdefs;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.OrangeHRM.browsers.LocalDriver;
import com.OrangeHRM.customelements.OrangeTableElement;
import com.OrangeHRM.customelements.OrangeWebElement;
import com.OrangeHRM.utilities.OrangeUtils;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class OrangeTableSteps extends BaseSteps {

	@And("^user waits for table \"([^\"]*)\" to be visible on screen$")
	public void user_waits_for_table_to_load_on_screen(String locator) {
		getTableElement(locator, LocalDriver.getDriver()).waitforVisible();
		OrangeUtils.wait(2000);
	}

	@Then("^verify column \"([^\"]*)\" is present in table \"([^\"]*)\"$")
	public void verify_column_is_present_in_table(String columnName, String tableLocator) {
		OrangeTableElement table = getTableElement(tableLocator, LocalDriver.getDriver());
		columnName = getVariableValue(columnName);
		List<String> columnHeader = table.getTableHeaderNames();
		Assert.assertTrue(columnHeader.contains(columnName), "Column header is not present in table.\n");
	}

	@Then("^verify text \"([^\"]*)\" is present in table \"([^\"]*)\"$")
	public void verify_text_is_present_in_table(String text, String tableLocator) {
		OrangeTableElement table = getTableElement(tableLocator, LocalDriver.getDriver());
		text = getVariableValue(text);
		table.verifyElementPresentInTableByText(text);
	}

	@Then("^verify text \"([^\"]*)\" is present in column \"([^\"]*)\" in table \"([^\"]*)\"$")
	public void verify_text_is_present_in_column_in_table(String text, String columnName, String tableLocator) {
		OrangeTableElement table = getTableElement(tableLocator, LocalDriver.getDriver());
		text = getVariableValue(text);
		List<String> columnValueList = table.getTableColumnValues(columnName);
		Assert.assertFalse(columnValueList.isEmpty(),
				"Column name ' " + columnName + "' is either incorrect or column is empty");

		boolean found = false;
		for (String string : columnValueList) {
			if (string.contains(text)) {
				found = true;
				break;
			}
		}
		Assert.assertTrue(found, "Expected text ' " + text + "' is not present in table\n");

	}

	@Then("^verify text \"([^\"]*)\" is not present in column \"([^\"]*)\" in table \"([^\"]*)\"$")
	public void verify_text_is_not_present_in_column_in_table(String text, String columnName, String tableLocator) {
		OrangeTableElement table = getTableElement(tableLocator, LocalDriver.getDriver());
		text = getVariableValue(text);
		List<String> columnValueList = table.getTableColumnValues(columnName);
		Assert.assertFalse(columnValueList.isEmpty(),
				"Column name ' " + columnName + "' is either incorrect or column is empty");

		boolean found = false;
		for (String string : columnValueList) {
			if (string.contains(text)) {
				found = true;
				break;
			}
		}
		Assert.assertFalse(found, "UnExpected text ' " + text + "' is present in table\n");

	}
	
	@When("^user clicks on link \"([^\"]*)\" of column \"([^\"]*)\" in table \"([^\"]*)\"$")
	public void user_clicks_on_link_of_column_in_table(String linkText, String columnName, String tableLocator) {
		OrangeTableElement table = getTableElement(tableLocator, LocalDriver.getDriver());
		linkText = getVariableValue(linkText);
		columnName = getVariableValue(columnName);
		List<WebElement> columnElement =  table.getTableColumnWebElements(columnName);
		Assert.assertFalse(columnElement.isEmpty(), "Column name " + columnName + " is either incorrect or column is empty");
		
		for(WebElement webElement: columnElement) {
			if(webElement.getText().trim().equals(linkText)) {
				//OrangeUtils.scrollAndClick(webElement.findElement(By.xpath("//div")));
				OrangeUtils.scrollIntoView(webElement.findElement(By.xpath("//div")));
				webElement.findElement(By.xpath(".//div")).click();
				break;
			} 
		}
	}
	
	@When("^user clicks on link \"([^\"]*)\" corresponding to text \"([^\"]*)\" in table \"([^\"]*)\"$")
	public void user_clicks_on_link_coresponding_to_text_in_table(String linkText, String text, String tableLocator) {
		OrangeTableElement table = getTableElement(tableLocator, LocalDriver.getDriver());
		linkText = getVariableValue(linkText);
		text = getVariableValue(text);
		table.waitforVisible();
		OrangeUtils.wait(10000);
		
		List<WebElement> elements = table.findElements(By.xpath("//div[text()='" + text + "']//parent::div//parent::div[@role='row']//div//button"));
		Assert.assertFalse(elements.isEmpty(),
				"Link '" + linkText + "' corresponding to text'" + text + "' is not found in the given table");
		//OrangeUtils.scrollAndClick(elements.get(0));
		elements.get(0).click();

	}

	
	@Then("^verify column \"([^\"]*)\" is empty in \"([^\"]*)\" table")
	public void verify_column_is_empty_in_table(String columnName, String tableLocator) {
		columnName = getVariableValue(columnName);
		OrangeTableElement table=    getTableElement(tableLocator, LocalDriver.getDriver());
		List<String> columnValues = table.getTableColumnValues(columnName);
		Assert.assertTrue(columnValues.isEmpty(), "Values are present in table.\n");
	}
	

}
