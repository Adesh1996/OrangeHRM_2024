package com.OrangeHRM.pages;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.OrangeHRM.constants.AppConstants;
import com.OrangeHRM.customelements.CustomElementFieldDecorator;
import com.OrangeHRM.customelements.OrangeWebElement;

public class CommonMenuPage extends BaseTestPage{
	
	WebDriver driver;

	public CommonMenuPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(new CustomElementFieldDecorator(driver, driver), this);
	}
	
	
	
	@FindBy(xpath = "//input[@placeholder='Search']")
	public OrangeWebElement textboxSearch;
	
	@FindBy(xpath = "//span[text()[normalize-space() = 'Admin']]")
	public OrangeWebElement linkAdmin;

	@FindBy(xpath = "//span[text()[normalize-space() = 'PIM']]")
	public OrangeWebElement linkPIM;
	
	@FindBy(xpath = "//span[text()[normalize-space() = 'Leave']]")
	public OrangeWebElement linkLeave;
	
	@FindBy(xpath="//span[text()[normalize-space() = 'Time']]")
	public OrangeWebElement linkTime;
	
	@FindBy(xpath="//span[text()[normalize-space() = 'Recruitment']]")
	public OrangeWebElement linkRecruitment;
	
	@FindBy(xpath="//span[text()[normalize-space() = 'My Info']]")
	public OrangeWebElement linkMyInfo;
	
	@FindBy(xpath="//span[text()[normalize-space() = 'Performance']]")
	public OrangeWebElement linkPerformance;
	
	@FindBy(xpath="//span[text()[normalize-space() = 'Dashboard']]")
	public OrangeWebElement linkDashboard;
	
	@FindBy(xpath="//span[text()[normalize-space() = 'Directory']]")
	public OrangeWebElement linkDirectory;
	
	@FindBy(xpath="//span[text()[normalize-space() = 'Maintenance']]")
	public OrangeWebElement linkMaintenance;
	
	@FindBy(xpath="//span[text()[normalize-space() = 'Buzz']]")
	public OrangeWebElement linkBuzz;
	
	//Profile and Profile options
	@FindBy(xpath="//li[contains(@class,'oxd-userdropdown')]//span")
	public OrangeWebElement linkProfile;
	
	@FindBy(xpath="//a[text()[normalize-space() = 'About']]")
	public OrangeWebElement linkAbout;
	
	@FindBy(xpath="//a[text()[normalize-space() = 'Support']]")
	public OrangeWebElement linkSupport;
	
	@FindBy(xpath="//a[text()[normalize-space() = 'Change Password']]")
	public OrangeWebElement linkChangePassword;
	
	@FindBy(xpath="//a[text()[normalize-space() = 'Logout']]")
	public OrangeWebElement linkLogout;
	
	@FindBy(xpath="//columnSetting header xpath")
	public OrangeWebElement headerColumnSettings;
	
	@FindBy(xpath="//xpath of list of checkbox")
	public List<WebElement> listCheckboxColumnSettings;
	
	@FindBy(xpath="//columnSetting save button")
	public OrangeWebElement buttonSaveColumnSettings;
	
	@FindBy(xpath="//button[@class='oxd-icon-button oxd-main-menu-button']")
	public OrangeWebElement panelDashboard;
	
	//Calendar Locators : 
	@FindBy(xpath="//ul[@class='oxd-calendar-selector']")
	public OrangeWebElement indicatorMonthYear;
	
	@FindBy(xpath="//li[@class='oxd-calendar-selector-year']")
	public OrangeWebElement buttonChooseYear;
	
	@FindBy(xpath="//li[@class='oxd-calendar-selector-month']")
	public OrangeWebElement buttonChooseMonth;
	
	@FindBy(xpath="//label[normalize-space()='From Date']/../..//input[@placeholder='yyyy-mm-dd']")
	public OrangeWebElement buttonFromDate;
	
	@FindBy(xpath="//label[normalize-space()='To Date']/../..//input[@placeholder='yyyy-mm-dd']")
	public OrangeWebElement buttonToDate;
	
	
	/**
	 * This method is used to logout from application
	 */
	public void doLogout() {
		linkProfile.click();  
		linkLogout.waitforVisible();
		linkLogout.click();
	}
	
	public String getMonthName(String strDate) {
		Date date = new Date();
		String month = null;
		try {
		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-M-dd");
		date = formatter1.parse(strDate);
		formatter1 = new SimpleDateFormat("MMMM");
		month = formatter1.format(date).toString();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return month;
		
	}
	
	public String getPreviousMonthLastDate() {
		Date myDate;
		String previousMonthLastDate = null;
		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		int max = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		calendar.set(Calendar.DAY_OF_MONTH, max);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.ENGLISH);
		
		try {
			myDate = sdf.parse(formatter1.format(calendar.getTime()));
			sdf.applyPattern("EEEE, yyyy-MM-dd");
			String smyDate = sdf.format(myDate);
			System.out.println(smyDate);
			
			if(smyDate.contains("Sunday")) {
				calendar.set(Calendar.DAY_OF_MONTH, max-2);
				previousMonthLastDate = formatter1.format(calendar.getTime());
			} else if(smyDate.contains("Saturday")) {
				calendar.set(Calendar.DAY_OF_MONTH, max-1);
				previousMonthLastDate = formatter1.format(calendar.getTime());
			}else {
				calendar.set(Calendar.DAY_OF_MONTH, max);
				previousMonthLastDate = formatter1.format(calendar.getTime());
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println(previousMonthLastDate);
		return previousMonthLastDate;
	}
	
	@Override
	public void waitForPageToLoad() {
		startTime = System.currentTimeMillis();
		if(!super.isPageLoaded(driver)) {
			Assert.fail("Timed out after " + AppConstants.PAGE_LOAD_TIMEOUT + " seconds\n");
		}
		
	}

}
