package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;

import config.EnvManager;
import io.cucumber.testng.PickleWrapper;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    private static final ExtentReports extent = ExtentManager.getInstance();
    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    private static final ThreadLocal<String> scenarioName = new ThreadLocal<>();

    @Override
    public void onStart(ITestContext context) {

        extent.setSystemInfo(
            "Environment",
            EnvManager.getEnv().name()
        );

        LoggerUtil.info(
            "==== TEST SUITE STARTED | ENV = " + EnvManager.getEnv().name() + " ===="
        );
        System.out.println("****** TEST SUITE STARTED | ENV = " + EnvManager.getEnv().name() + " ******"
        );
    }

    
    @Override
    public void onTestStart(ITestResult result) {

        String name = "Unknown Scenario";

        if (result.getParameters().length > 0
                && result.getParameters()[0] instanceof PickleWrapper) {

            PickleWrapper pickle = (PickleWrapper) result.getParameters()[0];
            name = pickle.getPickle().getName();
        }

        scenarioName.set(name);

        // ✅ CREATE TEST ONLY ONCE (THIS FIXES TEST-ID ISSUE)
        ExtentTest test = extent.createTest(name);
        extentTest.set(test);

        LoggerUtil.info("STARTED SCENARIO : " + name);
    }

    @Override
    public void onTestSuccess(ITestResult result) {

        extentTest.get().pass(
                MarkupHelper.createLabel(
                        "PASSED",
                        ExtentColor.GREEN
                )
        );
    }

    @Override
    public void onTestFailure(ITestResult result) {

        extentTest.get().fail(
                MarkupHelper.createLabel(
                        "FAILED",
                        ExtentColor.RED
                )
        );

        extentTest.get().fail(result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {

        extentTest.get().skip(
                MarkupHelper.createLabel(
                        "SKIPPED",
                        ExtentColor.ORANGE
                )
        );
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
        LoggerUtil.info("==== TEST SUITE FINISHED ====");
        System.out.println("==== TEST SUITE FINISHED ====");
    }
    
    public static ExtentTest getTest() {
        return extentTest.get();
    }
    
    
}
