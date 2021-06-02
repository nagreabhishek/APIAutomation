package resources;

public class Parameters {

	private static String dest;
	private static String day;

	public static void setCity(String city) {
		dest = city;
	}

	public static String getCity() {
		return dest;
	}

	public static void setDayOfWeek(String dayOfWeek) {
		day = dayOfWeek;
	}

	public static String getDayOfWeek() {
		return day;
	}
	
}
