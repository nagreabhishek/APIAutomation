package stepDefinition;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Assert;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.messages.internal.com.google.common.collect.LinkedListMultimap;
import io.cucumber.messages.internal.com.google.common.collect.Multimap;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import resources.Parameters;
import resources.Utils;

public class WeatherForcast extends Utils {

	RequestSpecification reqspec;
	Response response;
	int statusCode;
	String fileType;
	Multimap<String, String> finalTime;
	String dateTimeValue;
	String actualDay;
	Float eachDayTemp;
	Integer eachDayTempInt;
	ArrayList<Float> temp = new ArrayList<Float>();
	int counter = 0;
	int test =0;

	@Given("I like to holiday in {string}")
	public void i_like_to_holiday_in_city(String city) throws IOException {
		Parameters.setCity(city);
	}

	@And("I only like to holiday on {string}")
	public void i_only_like_to_holiday_on_day(String dayOfWeek) throws IOException {
		Parameters.setDayOfWeek(dayOfWeek);
	}

	@When("I look up the weather forecast")
	public void i_look_up_the_weather_forecast() throws IOException {
		reqspec = given().spec(requestSpecification());
	}

	@Then("I receive the weather forecast")
	public void i_receive_the_weather_forecast() throws IOException {
		response = reqspec.get();

		statusCode = response.getStatusCode();
		logger.info("Response status code for get call is " + statusCode);
		Assert.assertEquals(200, statusCode);

		fileType = response.header("content-type");
		Assert.assertTrue(fileType.contains("application/json"));
		logger.info("The response is valid JSON as the ContentType contains " + fileType);

		String destination = response.jsonPath().get("city.name");
		Assert.assertEquals(Parameters.getCity(), destination);
		logger.info("The response contains " + destination + " as a destination");
	}

	@And("The temperature is warmer than {int} degrees")
	public void temperature_is_warmer_than_some_degrees(Integer temperature) throws IOException, ParseException {
		finalTime = LinkedListMultimap.create();
		List<Object> weather = response.jsonPath().get("list");
		int listSize = weather.size();
		for (int i = 0; i < listSize; i++) {
			if((response.jsonPath().get("list["+i+"].main.temp")) instanceof Integer) {
				eachDayTempInt = response.jsonPath().get("list["+i+"].main.temp");
				eachDayTemp = (float) eachDayTempInt;
			} else {
				eachDayTemp = (response.jsonPath().get("list["+i+"].main.temp"));
			}
			
			dateTimeValue = response.jsonPath().get("list["+i+"].dt_txt");
			String date = dateTimeValue.split(" ")[0];

			String time = dateTimeValue.split(" ")[1];
			String expectedDay = Parameters.getDayOfWeek();
			actualDay = Utils.getDayOfWeek(date);
			
			if (actualDay.equalsIgnoreCase(expectedDay) && eachDayTemp > temperature) {
				finalTime.put(date, time);
				temp.add(eachDayTemp);
			} else if (i == (listSize -1) && finalTime.isEmpty()) {
				logger.error("Sorry, no "+expectedDay+" was found in the result list for city: "+Parameters.getCity());
			}
		}
		if (finalTime != null) {
			for (Map.Entry<String, String> entry : finalTime.entries()) {
				logger.info("Congratulations, on " + Parameters.getDayOfWeek() + " " + entry.getKey()
						+ " at " + entry.getValue() + " the weather returned is greater than " +temperature+ " degrees celsius i.e. " + temp.get(counter));
				counter++;
			}
		}

	}

}
