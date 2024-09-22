package com.orangeHRM.stepdefs;

import java.util.List;
import java.util.Map;

import com.OrangeHRM.utilities.DBUtils;

import io.cucumber.java.en.When;

public class DBSteps extends BaseSteps {

	@When("^user executes db query \"([^\"]*)\"$")
	public void user_executes_db_query(String query) {
		DBUtils dbUtils = new DBUtils();
		dbUtils.executeQuery(query);
	}
	
	@When("^user executes update db query \"([^\"]*)\"$")
	public void user_executes_update_db_query(String query) {
		DBUtils dbUtils = new DBUtils();
		dbUtils.executeUpdate(query);
	}
	
	@When("^user executes db query \"([^\"]*)\" and extracts data for column \"([^\"]*)\" as \"([^\"]*)\"$")
	public void user_executes_db_query_and_extracts_data_for_column_something_as_something(String query,
			String columnName, String variableName) {
		DBUtils dbUtils = new DBUtils();
		query = getVariableValue(query);
		Map<String, List<String>> dbData = dbUtils.executeQuery(query);
		scenarioData.get().put(variableName, dbData.get(columnName).get(0));
	}
	
}
