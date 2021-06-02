package resources;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import static org.hamcrest.Matchers.*;

public class Utils {

	public static RequestSpecification reqspec;
	public static Logger logger;

	public RequestSpecification requestSpecification() throws IOException {
		String uri = getGlobalValue("BASEURI");
		String appID = getGlobalValue("APPID");
		String city = Parameters.getCity();

		PrintStream log = new PrintStream(new FileOutputStream("logging.txt"));
		{
			reqspec = new RequestSpecBuilder().setBaseUri(uri)
					.addQueryParam("q", city)
					.addQueryParam("appid", appID)
					.addQueryParam("units", "metric")
					.addFilter(RequestLoggingFilter.logRequestTo(log))
					.addFilter(ResponseLoggingFilter.logResponseTo(log))
					.setContentType(ContentType.JSON).build();
		}
		return reqspec;
	}

	public Logger loggingtoTxtFile() {
		logger = Logger.getLogger("Weather API");
		PropertyConfigurator.configure("log4j.properties");
		return logger;
	}

	public String getGlobalValue(String key) throws IOException {
		Properties prop = new Properties();
		FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "/config.properties");
		prop.load(fis);
		return prop.getProperty(key);
	}

	public static String getDayOfWeek(String date) {
		String finalDay = null;
		try {
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
			Date dt1 = format1.parse(date);
			DateFormat format2 = new SimpleDateFormat("EEEE");
			finalDay = format2.format(dt1);
		} catch (ParseException e) {
			logger.error("Failed to determine the day of week");
			e.printStackTrace();
		}
		return finalDay;
	}

}
