package com.orangeHRM.stepdefs;

import org.openqa.selenium.By;
import org.testng.Assert;

import com.OrangeHRM.browsers.LocalDriver;
import com.OrangeHRM.customelements.OrangeWebElement;
import com.OrangeHRM.customelements.OrangeWidgetElement;

import io.cucumber.java.en.Then;   

public class OrangeWidgetSteps extends BaseSteps {
	
	@Then("^verify element \"([^\"]*)\" is present in widget \"([^\"]*)\"$")
	public void verify_element_is_present_in_widget(String locator, String widgetLocator) {
		OrangeWebElement element =   getWebElement(locator,LocalDriver.getDriver());
		OrangeWidgetElement widget = getWidgetElement(widgetLocator, LocalDriver.getDriver());
		
		widget.IsElementPresentInWidgetByText(widgetLocator);
	}
	
	
	
}
