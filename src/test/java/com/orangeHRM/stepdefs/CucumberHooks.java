package com.orangeHRM.stepdefs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import com.OrangeHRM.browsers.DriverFactory;
import com.OrangeHRM.browsers.LocalDriver;
import com.OrangeHRM.constants.AppConstants;
import com.OrangeHRM.pages.CommonMenuPage;
import com.OrangeHRM.utilities.OrangeUtils;
import com.OrangeHRM.utilities.TestRailClient;

import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class CucumberHooks {
	
	private Logger log;
	public static Map<String, String> resultMap = new ConcurrentHashMap<>();
	public static Map<String, List<String>> resultMap1 = new ConcurrentHashMap<>();
	
	@Before
	public void setup(Scenario scenario) {
		log = LogManager.getLogger(scenario.getName());
		WebDriver driver = DriverFactory.getDriver(AppConstants.BROWSER_TYPE);
		driver.manage().window().maximize();
		LocalDriver.setDriver(driver);
		BaseSteps baseSteps = new BaseSteps();
		baseSteps.setScenerio(scenario);
		log.info("Started execuation of scenerio : > ***** " + scenario.getName() + " *****");
		OrangeUtils.setSoftAssert(new SoftAssert());
	}
	
	@AfterStep
	public void addScreenshotOnFailure(Scenario scenario) {
		if (scenario.isFailed()) {
			log.info("Scenerio Failed... Taking Screenshot");
			
			try {
				Thread.sleep(2000);
			byte [] screenshot = OrangeUtils.takeFullScreenShot(LocalDriver.getDriver());
			scenario.attach(screenshot, "image/png", scenario.getName());		
			}
			catch(Exception e) {
				log.info("Couldn't capture screenshot");
				Assert.fail("Failed to capture screenshot"  + e.getCause());
			}
			CommonMenuPage CommonMenuPage = new CommonMenuPage(LocalDriver.getDriver());
			CommonMenuPage.doLogout();
		}
	}  
	
	@After
	public void tearDown(Scenario scenario) {
		log.info("Finished execuation of scenario : > *****" + scenario.getName() + " *****");
		log.info("[ closing driver ]");
		
		if(LocalDriver.getDriver() != null) {
			LocalDriver.getDriver().quit();
		}
		OrangeUtils.getSoftAssert().assertAll();
		
		//creating result map for excel report
		resultMap.put(scenario.getName(), scenario.getStatus().toString());
		
		ArrayList <String> list = new ArrayList<>();
		list.add(scenario.getStatus().toString());
		
		for (String s : scenario.getSourceTagNames()) {
			list.add(s);
		}
		resultMap1.put(scenario.getName(), list);
		
		//Updating status in test rail
		if(Boolean.TRUE.equals(AppConstants.TESTRAIL_UPDATE_FLAG)) {
			TestRailClient client = new TestRailClient(AppConstants.TESTRAIL_BASE_URL, AppConstants.TESTRAIL_USERNAME,
					AppConstants.TESTRAIL_APIKEY);
			client.updateTestRail(scenario.getName(), scenario.getStatus().toString());
		}
	}
	
}
