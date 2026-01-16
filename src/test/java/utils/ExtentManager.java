package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {

    private static ExtentReports extent;

    public static ExtentReports getInstance() {
        if (extent == null) {

            ExtentSparkReporter spark =
                    new ExtentSparkReporter("target/ExtentReport.html");

            spark.config().setReportName("API Automation Report");
            spark.config().setDocumentTitle("API Test Results");

            extent = new ExtentReports();
            extent.attachReporter(spark);

            
            extent.setSystemInfo("Framework", "Cucumber + TestNG");
            extent.setSystemInfo("Type", "API Automation");
            extent.setSystemInfo("Author", "Manish");
        }
        return extent;
    }
}

