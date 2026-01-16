	// Package where runner classes are kept
	package runners;
	
	// Cucumber TestNG base class that connects Cucumber with TestNG
	import io.cucumber.testng.AbstractTestNGCucumberTests;
	
	// Annotation used to configure Cucumber execution
	import io.cucumber.testng.CucumberOptions;
import utils.TestListener;

// TestNG annotation for providing data (used for parallel execution)
	import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
	
	/*
	 * @CucumberOptions is the heart of the runner.
	 * It tells Cucumber:
	 *  - Where feature files are
	 *  - Where step definitions are
	 *  - Which reports to generate
	 *  - Which tags to run
	 */
	@Listeners(TestListener.class)
	@CucumberOptions(
	
	        // Path of feature files
	        // Cucumber will scan this folder for all .feature files
	        features = "src/test/resources/features",
	 
	        // Glue code = step definitions + hooks
	        // Cucumber will search these packages for steps and hooks
	        		glue = {
	        			    "stepdefinitions",
	        			    "hooks"
	        			},
	
	        // Reporting plugins
	        plugin = {
	
	                // Makes console output readable
	                "pretty",
	
	                // Generates basic HTML report
	                "html:target/cucumber-reports/cucumber.html",
	
	                // Generates JSON report (used in CI/CD & reporting tools)
	                "json:target/cucumber-reports/cucumber.json",
	
	                // Extent report adapter for advanced reporting
	                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
	        },
	
	        // Removes unreadable characters from console output
	        monochrome = true,
	
	        // Publishes report link to Cucumber cloud (optional)
	        publish = true,
	
	        // Runs only scenarios tagged with @smoke
	        // You can change this to @regression, @api, etc.
	        tags = ""
	)
	
	/*
	 * TestRunner class extends AbstractTestNGCucumberTests
	 * This class bridges:
	 *   Cucumber scenarios  ---> TestNG execution engine
	 */
	
	
	public class TestRunner extends AbstractTestNGCucumberTests {
	
	    /*
	     * DataProvider method is overridden to enable parallel execution.
	     * Each scenario is treated as a separate TestNG test.
	     */
	    @Override
	
	    // parallel = true allows scenarios to run in parallel
	    @DataProvider(parallel = false)
	    public Object[][] scenarios() {
	
	        // Calls parent class method to fetch scenarios
	        return super.scenarios();
	    }
	}
