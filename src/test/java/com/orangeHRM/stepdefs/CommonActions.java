package com.orangeHRM.stepdefs;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.OrangeHRM.browsers.LocalDriver;
import com.OrangeHRM.constants.AppConstants;
import com.OrangeHRM.customelements.OrangeWebElement;
import com.OrangeHRM.utilities.OrangeUtils;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;

public class CommonActions extends BaseSteps {

	@And("^user navigates to url \"([^\"]*)\"$")
	public void navigate_to_url(String url) {
		LocalDriver.getDriver().get(getVariableValue(url));
	}

	@And("^user navigates back in the browser")
	public void user_navigates_back_in_the_browser() {
		LocalDriver.getDriver().navigate().back();
	}

	@And("^user clicks on \"([^\"]*)\"$")
	public void user_clicks_on(String locator) {
		getWebElement(locator, LocalDriver.getDriver()).click();
		OrangeUtils.wait(1000);
	}

	@And("^user clicks on \"([^\"]*)\" using js$")
	public void user_clicks_on_using_js(String locator) {
		getWebElement(locator, LocalDriver.getDriver()).jsClick();
		OrangeUtils.wait(1000);
	}

	@And("^user clicks on \"([^\"]*)\" using js scroll and click$")
	public void user_clicks_on_using_js_scroll_and_click(String locator) {
		getWebElement(locator, LocalDriver.getDriver()).scrollAndClick();
		OrangeUtils.wait(1000);
	}

	@And("^user clears text of textbox \"([^\"]*)\"$")
	public void user_clears_text_of_textbox(String locator) {
		OrangeWebElement element = getWebElement(locator, LocalDriver.getDriver());
		element.click();
		OrangeUtils.wait(2000);
		element.clearText();
		OrangeUtils.wait(5000);
	}

	@And("^user enters value \"([^\"]*)\" in textbox \"([^\"]*)\"$")
	public void user_enters_value_in_textbox(String value, String locator) {
		value = getVariableValue(value);
		OrangeWebElement element = getWebElement(locator, LocalDriver.getDriver());
		element.click();
		OrangeUtils.wait(2000);
		element.sendKeys(value);
		OrangeUtils.wait(2000);
	}

	@And("^user enters value \"([^\"]*)\" in textbox \"([^\"]*)\" using js$")
	public void user_enters_value_in_textbox_using_js(String value, String locator) {
		value = getVariableValue(value);
		OrangeWebElement element = getWebElement(locator, LocalDriver.getDriver());
		WebElement e = LocalDriver.getDriver().findElement(element.getBy());
		JavascriptExecutor jsExecutor = (JavascriptExecutor) LocalDriver.getDriver();
		jsExecutor.executeScript("arguments[0].value = arguments[1]", e, value);
		e.sendKeys(Keys.RETURN);
		OrangeUtils.wait(2000);
	}

	@Then("^user switches to frame elemet \"([^\"]*)\"$")
	public void user_switches_to_frame_elemet(String locator) {
		OrangeUtils.switchToFrame(LocalDriver.getDriver(), getWebElement(locator, LocalDriver.getDriver()));
	}

	@Then("^user switches to default content$")
	public void user_switches_to_default_content() {
		LocalDriver.getDriver().switchTo().defaultContent();
	}

	@Then("^user switches to parent frame$")
	public void user_switches_to_parent_frame() {
		LocalDriver.getDriver().switchTo().parentFrame();
	}

	@Then("^user switches to new window$")
	public void user_switches_to_new_window() {
		parentWindowHandle.set(LocalDriver.getDriver().getWindowHandle());
		OrangeUtils.switchToNewWindow(LocalDriver.getDriver());
	}

	@Then("^user switches back to parent window$")
	public void user_switches_back_to_parent_window() {
		LocalDriver.getDriver().close();
		LocalDriver.getDriver().switchTo().window(parentWindowHandle.get());
	}

	@Then("^user hovers over element \"([^\"]*)\"$")
	public void user_hovers_over_element(String locator) {
		getWebElement(locator, LocalDriver.getDriver()).moveToElement();

	}

	@Then("^verify that user is on \"([^\"]*)\" screen$")
	public void verify_that_user_is_on_screen(String pageClassName) {
		Class<?> clazz = getClassObject(pageClassName);
		invokeMethod(LocalDriver.getDriver(), clazz, "waitForPageToLoad");
		OrangeUtils.wait(2000);
	}

	@Then("^verify element \"([^\"]*)\" is visible$")
	public void verify_element_is_visible(String locator) {
		getWebElement(locator, LocalDriver.getDriver()).assertVisible();
	}

	@Then("^verify element \"([^\"]*)\" is selected$")
	public void verify_element_is_selected(String locator) {
		getWebElement(locator, LocalDriver.getDriver()).assertSelected();
	}

	@Then("^verify element \"([^\"]*)\" is not visible$")
	public void verify_element_is_not_visible(String locator) {
		getWebElement(locator, LocalDriver.getDriver()).assertNotVisible();
	}

	@Then("^verify table \"([^\"]*)\" is not visible$")
	public void verify_table_is_not_visible(String locator) {
		getTableElement(locator, LocalDriver.getDriver()).assertNotVisible();
	}

	@Then("^verify grid \"([^\"]*)\" is visible$")
	public void verify_grid_is_visible(String gridlocator) {
		getGridElement(gridlocator, LocalDriver.getDriver()).asserVisible();
	}

	@Then("^verify grid \"([^\"]*)\" is not visible$")
	public void verify_grid_is_not_visible(String gridlocator) {
		getGridElement(gridlocator, LocalDriver.getDriver()).assertNotVisible();
	}

	@Then("^verify table \"([^\"]*)\" is visible$")
	public void verify_table_is_visible(String locator) {
		getTableElement(locator, LocalDriver.getDriver()).assertVisible();
	}

	@Then("^user accepts the alert")
	public void user_accepts_the_alert() {
		WebDriverWait wait = new WebDriverWait(LocalDriver.getDriver(), Duration.ofSeconds(AppConstants.WAIT_TIMEOUT));
		wait.until(ExpectedConditions.alertIsPresent());
		Alert alert = LocalDriver.getDriver().switchTo().alert();
		alert.accept();
	}

	@Then("^user dismisses the alert")
	public void user_dismisses_the_alert() {
		WebDriverWait wait = new WebDriverWait(LocalDriver.getDriver(), Duration.ofSeconds(AppConstants.WAIT_TIMEOUT));
		wait.until(ExpectedConditions.alertIsPresent());
		Alert alert = LocalDriver.getDriver().switchTo().alert();
		alert.dismiss();
	}

	@Then("^verify input element \"([^\"]*)\" value should be \"([^\"]*)\"$")
	public void verify_input_element_value_should_be(String locator, String value) {
		value = getVariableValue(value);
		getWebElement(locator, LocalDriver.getDriver()).assertAttribute("value", value);
	}

	@Then("^verify text of element \"([^\"]*)\" should be \"([^\"]*)\"$")
	public void verify_text_of_element_should_be(String locator, String text) {
		text = getVariableValue(text);
		getWebElement(locator, LocalDriver.getDriver()).assertText(text.trim());
	}

	@Then("^verify attribute \"([^\"]*)\" of element \"([^\"]*)\" should be \"([^\"]*)\"$")
	public void verify_attribute_of_element_should_be(String attributeName, String locator, String attributeValue) {
		attributeValue = getVariableValue(attributeValue);
		getWebElement(locator, LocalDriver.getDriver()).assertAttribute(attributeName, attributeValue);
	}

	@Then("^verify text of element \"([^\"]*)\" should contain \"([^\"]*)\"$")
	public void verify_text_of_element_should_contain(String locator, String text) {
		text = getVariableValue(text);
		getWebElement(locator, LocalDriver.getDriver()).assertContainsText(text);
	}

	@Then("^verify text of element \"([^\"]*)\" should not contain \"([^\"]*)\"$")
	public void verify_text_of_element_should_not_contain(String locator, String text) {
		text = getVariableValue(text);
		String value = getWebElement(locator, LocalDriver.getDriver()).getText();
		Assert.assertFalse(value.contains(text), "Unexpected text " + text + " is present in the given element");
	}

	@Then("^verify element \"([^\"]*)\" is enabled$")
	public void verify_element_is_enabled(String locator) {
		getWebElement(locator, LocalDriver.getDriver()).assertEnabled();
	}

	@Then("^verify element \"([^\"]*)\" is disabled$")
	public void verify_element_is_disabled(String locator) {
		getWebElement(locator, LocalDriver.getDriver()).assertDisabled();
	}

	@Then("^user waits for element \"([^\"]*)\" to be visible$")
	public void user_waits_for_element_to_be_visible(String locator) {
		getWebElement(locator, LocalDriver.getDriver()).waitforVisible();
		OrangeUtils.wait(2000);
	}

	@Then("^user waits for element \"([^\"]*)\" to be invisible$")
	public void user_waits_for_element_to_be_invisible(String locator) {
		getWebElement(locator, LocalDriver.getDriver()).waitforInvisible();
		OrangeUtils.wait(2000);
	}

	@Then("^user captures current window as \"([^\"]*)\"$")
	public void user_captures_current_window_as(String mapKey) {
		scenarioData.get().put(mapKey, LocalDriver.getDriver().getWindowHandle());
	}

	@Then("^user clicks on \"([^\"]*)\" if visible$")
	public void user_clicks_on_if_visible(String locator) {
		OrangeWebElement element = getWebElement(locator, LocalDriver.getDriver());
		if (element.isPresent()) {
			element.click();
		}
	}

	@Then("^verify text of element \"([^\"]*)\" should contain date \"([^\"]*)\"$")
	public void verify_text_of_element_should_contain_date(String locator, String text) {
		text = getVariableValue(text);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
		DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		String date = LocalDate.parse(text, formatter).format(formatter2);
		getWebElement(locator, LocalDriver.getDriver()).assertContainsText(date);
	}

	@Then("^user drags \"([^\"]*)\" and drops it at \"([^\"]*)\"$")
	public void user_drags_and_drops_it_at(String sourceElementLocator, String destinationElementLocator) {
		WebElement sourceElement = getWebElement(sourceElementLocator, LocalDriver.getDriver()).getElement();
		WebElement destinationElement = getWebElement(destinationElementLocator, LocalDriver.getDriver()).getElement();
		OrangeUtils.dragAndDropElement(LocalDriver.getDriver(), sourceElement, destinationElement);
	}

	@Then("^verify text of alert should be \"([^\"]*)\"$")
	public void verify_text_of_alert_should_be(String expectedText) {
		WebDriverWait wait = new WebDriverWait(LocalDriver.getDriver(), Duration.ofSeconds(AppConstants.WAIT_TIMEOUT));
		wait.until(ExpectedConditions.alertIsPresent());
		Alert alert = LocalDriver.getDriver().switchTo().alert();
		String alertText = alert.getText();
		Assert.assertTrue(alertText.equals(expectedText));
	}

	@Then("^user opens a new browser window and switches to it$")
	public void user_opens_a_new_browser_window_and_switches_to_it() {
		parentWindowHandle.set(LocalDriver.getDriver().getWindowHandle());
		OrangeUtils.openNewWindow(LocalDriver.getDriver());
	}

	@Then("^verify widget \"([^\"]*)\" is visible$")
	public void verify_widget_is_visible(String locator) {
	getWidgetElement(locator, LocalDriver.getDriver()).assertVisble();
	}
	
	@Then("^verify user is stopped at breakpoint")
	public void verify_user_is_stopped_at_breakpoint() {
	    OrangeUtils.wait(3000);
	}
	
	@Then("^verify element \"([^\"]*)\" is displaying$")
	public void verify_element_is_displaying(String locator) {
	 getWebElement(locator, LocalDriver.getDriver()).assertVisible();
	}
	
	@Then("^user scroll and clicks on \"([^\"]*)\"$")
	public void user_scroll_and_clicks_on(String locator) {
		OrangeWebElement element = getWebElement(locator, LocalDriver.getDriver());
		element.scrollAndClick();
	}

	@Then("^user scroll the element \"([^\"]*)\"$")
	public void user_scroll_the_element(String string) {
	  JavascriptExecutor javascriptExecutor = (JavascriptExecutor)LocalDriver.getDriver();
	  javascriptExecutor.executeScript("window.scrollBy(0,600)", "");
	}
	
	@Then("^user scroll down the page$")
	public void user_scroll_down_the_page() {
	  JavascriptExecutor javascriptExecutor = (JavascriptExecutor)LocalDriver.getDriver();
	  javascriptExecutor.executeScript("document.querySelector('ruf-app-canvas.fis-override.ruf-app-canvas').scrollBy(0,400)");
	}
	
	@Then("^user captures attribute \"([^\"]*)\" of element \"([^\"]*)\" as \"([^\"]*)\"$")
	public void user_captures_attribute_of_element_as(String attributeName, String locator, String mapKey) {
		OrangeWebElement element = getWebElement(locator, LocalDriver.getDriver());
		String value= element.getAttribute(attributeName).trim();
		scenarioData.get().put(mapKey, value);
	}
	
	@Then("^user selects option with index \"([^\"]*)\" in dropdown \"([^\"]*)\"$")
	public void user_selects_option_with_index_in_dropdown(String index, String locator) {
		int i = Integer.parseInt(index);
		OrangeWebElement dropdown = getWebElement(locator, LocalDriver.getDriver());
		dropdown.findElements(By.xpath("//mat-option//span")).get(i).click();
	}
	
	@Then("^user double clicks on \"([^\"]*)\"$")
	public void user_double_clicks_on(String locator) {
	   OrangeUtils.wait(2000);
	   getWebElement(locator, LocalDriver.getDriver()).doubleClick();
	   OrangeUtils.wait(2000);
	}
	

	@Then("^verify element count of \"([^\"]*)\" is not more than \"([^\"]*)\"$")
	public void verify_element_count_of_is_not_more_than(String locator, String size) {
	   OrangeWebElement element = getWebElement(locator, LocalDriver.getDriver());
	   ArrayList <WebElement> e = (ArrayList<WebElement>) LocalDriver.getDriver().findElements(element.getBy());
	   int len = e.size();
	   Assert.assertTrue(len<= Integer.parseInt(size));
	}
	
	@Then("^verify message \"([^\"]*)\" is displayed$")
	public void verify_message_is_displayed(String expectedText) {
		WebElement dialoge = LocalDriver.getDriver().findElement(By.xpath("//mat-dialog-content//p"));
		String msg = dialoge.getText();
		Assert.assertTrue(msg.contains(expectedText));
	}
	
	@Then("^user uploads file \"([^\"]*)\"$")
	public void user_uploads_file(String fileName) {
	   fileName = getVariableValue(fileName);
	   String filePath = AppConstants.UPLOAD_FOLDER_PATH + fileName;
	   OrangeUtils.wait(2000);
	   OrangeUtils.runExecutable(AppConstants.UPLOAD_FOLDER_PATH + "FileUpload.vbs", filePath);
	   OrangeUtils.wait(2000);
	   Assert.assertTrue(LocalDriver.getDriver().findElements(By.xpath("//div[@class='oxd-file-input-div']")).get(1).getText().trim()
			   .equals(fileName));
	}
	
	@Then("^verify text of element \"([^\"]*)\" is present in list \"(.*?)\"$")
	public void verify_text_of_element_is_present_in_list(String locator, String text) {
	   OrangeWebElement element = getWebElement(locator, LocalDriver.getDriver());
	   boolean match = false;
	   List<String> portfolioType = Arrays.asList(text.split("\\s*, \\s*"));
	   String portfolioTypeValue = element.getText().trim();
	   for (String type: portfolioType) {
		   if(portfolioTypeValue.equalsIgnoreCase(type)) {
			   match = true;
			   break;
		   }
	   }
	   Assert.assertTrue(match, "Value of " + locator + " not matching with values present in list " + text + ". Please check list or xpath of locator");
	}
	
	@Then("^verify options \"([^\"]*)\" present in dropdown \"([^\"]*)\"$")
	public void verify_options_something(ArrayList<String>values,String locator) {
		OrangeWebElement element = getWebElement(locator, LocalDriver.getDriver());
		
		for(int i=0; i<=values.size(); i++) {
			if(!(element.getElement().findElement(By.xpath("//mat-option//span[text()='" + values.get(i)+"']")).isDisplayed())) {
				Assert.fail("Option " + values.get(i)+ " is not present in drop down");
			break;
			}
		}
	}
	
	@Then("^verify option \"([^\"]*)\"  is present at \"([^\"]*)\" postion$")
	public void verify_element_is_present_at_postion(String ExpectedText,String order) {
		
		ExpectedText = getVariableValue(ExpectedText);
		String xpath = "(//div[@role='menuitem'])" + "[" + order+ "]" ;
		OrangeUtils.wait(3000);
		String Actualtext = LocalDriver.getDriver().findElement(By.xpath(xpath)).getText();
		Assert.assertEquals(Actualtext, ExpectedText, Actualtext + "is present at " + order + "postion");
		
	}
	
}
