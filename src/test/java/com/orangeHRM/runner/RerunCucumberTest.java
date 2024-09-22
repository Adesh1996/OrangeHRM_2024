package com.orangeHRM.runner;

import java.awt.Desktop;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.plexus.util.FileUtils;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;

import com.OrangeHRM.constants.AppConstants;
import com.OrangeHRM.utilities.ExcelUtilities;
import com.orangeHRM.stepdefs.CucumberHooks;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(plugin = { "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
		"rerun:report/rerun123.txt" }, features = "@report/rerun.txt", glue = "com.orangeHRM.stepdefs", tags = "not (@pass or @fail or @hold)")

public class RerunCucumberTest extends AbstractTestNGCucumberTests {
	
	@Override
	@DataProvider(parallel = true)
	public Object[][] scenarios(){
		return super.scenarios();
	}
	
	@BeforeSuite(alwaysRun = true)
	public void flushExcelReport() {
		ExcelUtilities excelUtilities = new ExcelUtilities(AppConstants.USER_DIRECTORY + "/report/ExcelReport.xlsx");
		excelUtilities.deleteAllRowsExceptHeader();
	}
	
	@AfterSuite(alwaysRun = true)
	public void openReport() {
		try {
			//Opening the current Report file
			File reportFile = new File(AppConstants.USER_DIRECTORY + "/report/SparkReport.html");
			Desktop desktop = Desktop.getDesktop();
			if(reportFile.exists()) {
				desktop.open(reportFile);
			}
			
			//Backing up current report file with current timestamp
			File reportBackup = new File(AppConstants.USER_DIRECTORY + "/report/archive/SparkReport_" + AppConstants.APPLICATION_ENV + "_"
					+ new SimpleDateFormat("dd-MM_HH-mm-ss").format(new Date()) + ".html");
			FileUtils.copyFile(reportFile, reportBackup);
			
			//Creating excel report
			ExcelUtilities excelUtilities = new ExcelUtilities(AppConstants.USER_DIRECTORY + "/report/ExcelReport.xlsx");
			excelUtilities.writeResultsToExcelReport(CucumberHooks.resultMap);
		}
		catch(Exception e) {
			e.printStackTrace();
			Assert.fail(" " , e.getCause());
		}
	}

}
