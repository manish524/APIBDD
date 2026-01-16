package hooks;

import config.ConfigReader;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import utils.LoggerUtil;
import utils.RequestSpecBuilderUtil;
import utils.TokenManager; // make sure this is your TokenManager class

public class Hooks {

    @Before
    public void setUp() {
    	RestAssured.requestSpecification = RequestSpecBuilderUtil.getRequestSpec();
        LoggerUtil.info("Authorization header set with Bearer token");
    }

    @After
    public void tearDown() {
        LoggerUtil.info("Scenario execution completed");
    }
}
