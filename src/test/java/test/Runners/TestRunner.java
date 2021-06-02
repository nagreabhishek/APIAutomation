package test.Runners;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features", 
				plugin = {"pretty", "json:target/jsonReports/cucumber-report.json", "html:target/cucumber-reports"},
				glue = {"test.Runners", "stepDefinition"},
				tags="@holidaymaker",
				monochrome=true,
				dryRun=false
				)

public class TestRunner {


}
