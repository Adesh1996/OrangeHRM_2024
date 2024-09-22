package com.orangeHRM.stepdefs;

import com.OrangeHRM.browsers.LocalDriver;
import com.OrangeHRM.customelements.OrangeGridElement;
import com.OrangeHRM.customelements.OrangeTableElement;
import com.OrangeHRM.customelements.OrangeWebElement;
import com.OrangeHRM.utilities.OrangeUtils;

import io.cucumber.java.en.Then;

public class UIVerificationSteps extends BaseSteps{
	
	
	@Then("^validate that element \"([^\"]*)\" is visible$")
	public void validate_that_element_something_is_visible(String locator) {
	OrangeWebElement element= 	getWebElement(locator, LocalDriver.getDriver());
	boolean isVisible= false;
	if(element.isPresent()) {
		try {
			isVisible = element.isDisplayed();
		}catch(Exception e) {
			//Do nothing here if element is not visible
		}
	}
	OrangeUtils.wait(2000);
	OrangeUtils.getSoftAssert().assertTrue(isVisible, "Element " + locator + " is not visible on screen\n");
	}
	
	
	@Then("^validate that table \"([^\"]*)\" is visible$")
	public void validate_that_table_something_is_visible(String tableLocator) {
	OrangeTableElement element= getTableElement(tableLocator, LocalDriver.getDriver());
	OrangeUtils.wait(2000);
	OrangeUtils.getSoftAssert().assertTrue(element.isDisplayed(), "Table " + tableLocator + " is not visible on screen\n");
	}
	
	
	@Then("^validate that grid \"([^\"]*)\" is visible$")
	public void validate_that_grid_something_is_visible(String gridLocator) {
	OrangeGridElement element= getGridElement(gridLocator, LocalDriver.getDriver());
	OrangeUtils.wait(2000);
	OrangeUtils.getSoftAssert().assertTrue(element.isDisplayed(), "Grid " + gridLocator + " is not visible on screen\n");
	}
}
