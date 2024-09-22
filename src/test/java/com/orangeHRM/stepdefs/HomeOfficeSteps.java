package com.orangeHRM.stepdefs;

import org.openqa.selenium.By;
import org.testng.Assert;

import com.OrangeHRM.browsers.LocalDriver;
import com.OrangeHRM.customelements.OrangeWidgetElement;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class HomeOfficeSteps extends BaseSteps {

	@Then("^verify text of link with index \"([^\"]*)\" in dashboard widget \"([^\"]*)\" should be \"([^\"]*)\"$")
	public void verify_text_of_link_with_index_in_dashboard_widget_should_be(String index, String widgetLocator,
			String expectedText) {
		int i = Integer.parseInt(index);
		expectedText = getVariableValue(expectedText);
		OrangeWidgetElement widget = getWidgetElement(widgetLocator, LocalDriver.getDriver());
		String actualText = widget.findElements(By.xpath(".//p")).get(i).getText().trim();
		Assert.assertTrue(actualText.contains(expectedText),
				"Expected text '" + expectedText + "' is not matching with actual text '" + actualText + "'.\n");
	}
	
	@When("^user selects link with index \"([^\"]*)\" in dashboard widget \"([^\"]*)\"$")
	public void user_selects_link_with_index_in_dashboard_widget(String index, String widgetLocator) {
		int i = Integer.parseInt(index);
		OrangeWidgetElement widget = getWidgetElement(widgetLocator, LocalDriver.getDriver());
		widget.findElements(By.xpath(".//p")).get(i).click();
	}

}
