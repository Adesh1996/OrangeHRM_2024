
Feature: Test me

  I want to verify framework basic structure
@AdeshK
  Scenario: 123456|Test
    Given user is on OrangeHRM Login Page
    Given user logs OrangeHRM with "#UserName"
    #Then verify text of element "CommonMenuPage.linkTime" should be "Time"
    #Then user collapse vertical accordian "CommonMenuPage.panelDashboard"
    #Then user expands vertical accordian "CommonMenuPage.panelDashboard"
    #Then verify vertical accordian "CommonMenuPage.panelDashboard" is expanded
    #Then user logs out of OrangeHRM
    #Then verify downloaded print contains value ""
    And user clicks on "CommonMenuPage.linkPerformance"
    And user clicks on "CommonMenuPage.buttonFromDate"
    Then user selects date "2021-12-05" given in YYYY-M-dd format from "CommonMenuPage.buttonFromDate" calender
    And user clicks on "CommonMenuPage.buttonToDate"
    Then user selects date "2022-12-05" given in YYYY-M-dd format from "CommonMenuPage.buttonToDate" calender

  #And user captures text of element "CommonMenuPage.linkTime" and save as "xyzC" in runtime properties file
  #Then user selects previous month last date from "CommonMenuPage.buttonFromDate" datepicker
  Scenario: 457896|Test file upload
    Given user is on OrangeHRM Login Page
    Then user logs OrangeHRM with "#UserName"
    When user clicks on "CommonMenuPage.linkMyInfo"
    Then verify that user is on "MyInfoPage" screen
    And user clicks on "MyInfoPage.linkpersonalDetails"
    And user clicks on "MyInfoPage.buttonAdd"
    Then verify element "MyInfoPage.buttonBrowse" is visible
    And user uploads document "DummyImage.png" from file path "upload"
@AdeshK
  Scenario: 125689|Test report steps
    Given user is on OrangeHRM Login Page
    Then user logs OrangeHRM with "#UserName"
    When user clicks on "CommonMenuPage.linkLeave"
    Then verify that user is on "LeavePage" screen
    And user selects report category "Reports.LeaveEntitlementsandUsageReport"
    And user waits for "2" minutes
@AdeshK
  Scenario: 124578|Test dashboard widget
    Given user is on OrangeHRM Login Page
    Then user logs OrangeHRM with "#UserName"
    Then verify that user is on "DashboardPage" screen
    And verify text of link with index "1" in dashboard widget "DashboardPage.widgetMyActions" should be "Leave Requests to Approve"
    Then user selects link with index "1" in dashboard widget "DashboardPage.widgetMyActions"
    Then verify that user is on "LeavePage" screen
    And user logs out of OrangeHRM

@AdeshK
  Scenario: 124578789456|Test table methods
    Given user is on OrangeHRM Login Page
    Then user logs OrangeHRM with "#UserName"
    Then verify that user is on "DashboardPage" screen
    When user clicks on "CommonMenuPage.linkRecruitment"
    Then user waits for table "RecruitmentPage.tableRecruitment" to be visible on screen
    And verify column "Vacancy" is present in table "RecruitmentPage.tableRecruitment"
    And verify text "Linda Jane Anderson" is present in table "RecruitmentPage.tableRecruitment"
    Then verify text "Rejected" is present in column "Status" in table "RecruitmentPage.tableRecruitment"
    Then verify text "Rejected1" is not present in column "Status" in table "RecruitmentPage.tableRecruitment"
    #Link check
    When user clicks on "CommonMenuPage.linkTime"
    Then user waits for table "TimePage.tableTimesheetsPendingAction" to be visible on screen
    And user clicks on link " View " corresponding to text "Charlie  Carter" in table "TimePage.tableTimesheetsPendingAction"
    # And user waits for "1" minutes
    #
    When user clicks on "CommonMenuPage.linkPIM"
    Then user waits for table "PIMPage.tableEmployeeInformation" to be visible on screen
    Then user clicks on link "Aaliyah" of column "First (& Middle) Name" in table "PIMPage.tableEmployeeInformation"
    And user logs out of OrangeHRM


  Scenario: 111|Test browser methods
    Given user is on OrangeHRM Login Page
    Then user logs OrangeHRM with "#UserName"
    Then verify that user is on "DashboardPage" screen
    When user clicks on "CommonMenuPage.linkRecruitment"
    And user closes current browser instance
    And user opens new browser
  	#And user waits for "1" minutes
  
    Scenario: 1111|Test through console
    Given user is on OrangeHRM Login Page
    And user enters credentials from command line
    Then verify that user is on "DashboardPage" screen
    ## Below step is used to reset the page to default
    And user reset default state of "DashboardPage" screen
    * user clicks on "CommonMenuPage.linkRecruitment"
