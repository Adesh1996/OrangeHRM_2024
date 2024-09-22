package com.orangeHRM.stepdefs;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;

import com.OrangeHRM.browsers.LocalDriver;
import com.OrangeHRM.pages.MyInfoPage;
import com.OrangeHRM.utilities.OrangeUtils;

import io.cucumber.java.en.And;

public class DocumentsSteps extends BaseSteps {

	/**
	 * 
	 * @param documentName (Provide file extension also)
	 * @param value        (folder name from which you want to upload file)
	 */
	@And("^user uploads document \"([^\"]*)\" from file path \"([^\"]*)\"$")
	public void user_uploads_document_from_file_path(String documentName, String value) {
		value = getVariableValue(value);
		documentName = getVariableValue(documentName);
		LocalDriver.getDriver().findElement(By.xpath("//input[@class='oxd-file-input']"))
				.sendKeys(System.getProperty("user.dir") + "\\test-data\\" + value + "\\" + documentName);

		OrangeUtils.wait(5000);
		Assert.assertEquals(
				LocalDriver.getDriver().findElement(By.xpath("//div[@class='oxd-file-input-div']")).getText().trim(),
				documentName);
	}

	@And("^user uploads document \"([^\"]*)\" from file path \"([^\"]*)\" new$")
	public void user_uploads_document_from_file_path_new(String documentName, String value) {
		value = getVariableValue(value);
		documentName = getVariableValue(documentName);

		JavascriptExecutor jsExecutor = (JavascriptExecutor) LocalDriver.getDriver();
		jsExecutor.executeScript("document.getElementById('file').style.display='block'");

		LocalDriver.getDriver().findElement(By.xpath("//input[@class='oxd-file-input']"))
				.sendKeys(System.getProperty("user.dir") + "\\test-data\\" + value + "\\" + documentName);
		jsExecutor.executeScript("document.getElementById('file').style.display='none'");

		OrangeUtils.wait(5000);
		Assert.assertEquals(
				LocalDriver.getDriver().findElement(By.xpath("//div[@class='oxd-file-input-div']")).getText().trim(),
				documentName);
	}
}
