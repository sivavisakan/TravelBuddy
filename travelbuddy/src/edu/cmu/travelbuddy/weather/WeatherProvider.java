package edu.cmu.travelbuddy.weather;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.cmu.travelbuddy.R;

import android.util.Log;

/**
 * WeatherProvider is a class that queries Google Weather - XML on the URL and
 * reads this data and stores it in WeatherInfo objects.
 * 
 * @author Ashwin Das
 */
public class WeatherProvider {

	private static final String GOOGLE_WEATHER_URL = "http://www.google.com/ig/api?weather=";

	private String currentCondition;
	private String currentTemperatureF;
	private String currentTemperatureC;
	private String currentHumidity;
	private String currentWind;

	private List<WeatherInfo> forecastList = new ArrayList<WeatherInfo>();

	private HashMap<String, Integer> dayIconMap;

	private HashMap<String, Integer> nightIconMap;

	private HashMap<String, Integer> backgroundImages;

	/**
	 * Checks the weather information for the given code and populates the
	 * Weather Model.
	 * 
	 * @param postalCode
	 */
	public void checkLatestWeatherConditions(String postalCode) {
		try {
			String link = GOOGLE_WEATHER_URL + postalCode;
			URL googleWeatherService = new URL(link.replaceAll(" ", "%20"));
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			builderFactory.setNamespaceAware(true);
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			Document document = builder.parse(googleWeatherService.openStream());
			
			initializeWeatherImages();

			/* Getting the current weather conditions */
			checkCurrentConditions(document);

			/* REFRESH - Clearing Old forecast list to add new items below */
			forecastList.clear();

			/* Getting the forecasted weather conditions */
			checkForecastConditions(document);

		} catch (MalformedURLException e) {
			Log.w("T_WEATHERPROVIDER", e);
		} catch (ParserConfigurationException e) {
			Log.w("T_WEATHERPROVIDER", e);
		} catch (SAXException e) {
			Log.w("T_WEATHERPROVIDER", e);
		} catch (IOException e) {
			Log.w("T_WEATHERPROVIDER", e);
		}
	}

	/**
	 * Initializes all the weather images
	 */
	private void initializeWeatherImages() {
		dayIconMap = new HashMap<String, Integer>();

		dayIconMap.put("/ig/images/weather/chance_of_rain.gif", R.drawable.w_chances_of_rain);
		dayIconMap.put("/ig/images/weather/sunny.gif", R.drawable.w_sunny);
		dayIconMap.put("/ig/images/weather/mostly_sunny.gif", R.drawable.w_mostly_sunny);
		dayIconMap.put("/ig/images/weather/partly_cloudy.gif", R.drawable.w_partly_cloudy);
		dayIconMap.put("/ig/images/weather/mostly_cloudy.gif", R.drawable.w_mostly_cloudy);
		dayIconMap.put("/ig/images/weather/chance_of_storm.gif", R.drawable.w_chance_of_storm);
		dayIconMap.put("/ig/images/weather/rain.gif", R.drawable.w_rain);
		dayIconMap.put("/ig/images/weather/chance_of_rain.gif", R.drawable.w_chances_of_rain);
		dayIconMap.put("/ig/images/weather/chance_of_snow.gif", R.drawable.w_chances_of_snow);
		dayIconMap.put("/ig/images/weather/cloudy.gif", R.drawable.w_cloudy);
		dayIconMap.put("/ig/images/weather/mist.gif", R.drawable.w_mist);
		dayIconMap.put("/ig/images/weather/storm.gif", R.drawable.w_storm);
		dayIconMap.put("/ig/images/weather/thunderstorm.gif", R.drawable.w_thunderstorm);
		dayIconMap.put("/ig/images/weather/chance_of_tstorm.gif", R.drawable.w_chances_of_ts);
		dayIconMap.put("/ig/images/weather/sleet.gif", R.drawable.w_sleet);
		dayIconMap.put("/ig/images/weather/snow.gif", R.drawable.w_snow);
		dayIconMap.put("/ig/images/weather/icy.gif", R.drawable.w_icy);
		dayIconMap.put("/ig/images/weather/dust.gif", R.drawable.w_dust);
		dayIconMap.put("/ig/images/weather/fog.gif", R.drawable.w_fog);
		dayIconMap.put("/ig/images/weather/smoke.gif", R.drawable.w_smoke);
		dayIconMap.put("/ig/images/weather/haze.gif", R.drawable.w_haze);
		dayIconMap.put("/ig/images/weather/flurries.gif", R.drawable.w_flurries);

		nightIconMap = new HashMap<String, Integer>();
		nightIconMap.put("/ig/images/weather/chance_of_rain.gif", R.drawable.w_chances_of_rain);
		nightIconMap.put("/ig/images/weather/sunny.gif", R.drawable.w_sunny_n);
		nightIconMap.put("/ig/images/weather/mostly_sunny.gif", R.drawable.w_mostly_sunny_n);
		nightIconMap.put("/ig/images/weather/partly_cloudy.gif", R.drawable.w_partly_cloudy_n);
		nightIconMap.put("/ig/images/weather/mostly_cloudy.gif", R.drawable.w_mostly_cloudy_n);
		nightIconMap.put("/ig/images/weather/chance_of_storm.gif", R.drawable.w_chance_of_storm_n);
		nightIconMap.put("/ig/images/weather/rain.gif", R.drawable.w_rain_n);
		nightIconMap.put("/ig/images/weather/chance_of_snow.gif", R.drawable.w_chances_of_snow_n);
		nightIconMap.put("/ig/images/weather/cloudy.gif", R.drawable.w_cloudy_n);
		nightIconMap.put("/ig/images/weather/mist.gif", R.drawable.w_mist_n);
		nightIconMap.put("/ig/images/weather/storm.gif", R.drawable.w_storm);
		nightIconMap.put("/ig/images/weather/thunderstorm.gif", R.drawable.w_thunderstorm_n);
		nightIconMap.put("/ig/images/weather/chance_of_tstorm.gif", R.drawable.w_chances_of_ts);
		nightIconMap.put("/ig/images/weather/sleet.gif", R.drawable.w_sleet_n);
		nightIconMap.put("/ig/images/weather/snow.gif", R.drawable.w_snow_n);
		nightIconMap.put("/ig/images/weather/icy.gif", R.drawable.w_icy_n);
		nightIconMap.put("/ig/images/weather/dust.gif", R.drawable.w_dust_n);
		nightIconMap.put("/ig/images/weather/fog.gif", R.drawable.w_fog_n);
		nightIconMap.put("/ig/images/weather/smoke.gif", R.drawable.w_smoke_n);
		nightIconMap.put("/ig/images/weather/haze.gif", R.drawable.w_haze_n);
		nightIconMap.put("/ig/images/weather/flurries.gif", R.drawable.w_flurries_n);

		backgroundImages = new HashMap<String, Integer>();
		backgroundImages.put("/ig/images/weather/chance_of_rain.gif", R.drawable.rain_bg);
		backgroundImages.put("/ig/images/weather/sunny.gif", R.drawable.sunny_bg);
		backgroundImages.put("/ig/images/weather/mostly_sunny.gif", R.drawable.sunny_bg);
		backgroundImages.put("/ig/images/weather/partly_cloudy.gif", R.drawable.cloudy_bg);
		backgroundImages.put("/ig/images/weather/mostly_cloudy.gif", R.drawable.cloudy_bg);
		backgroundImages.put("/ig/images/weather/chance_of_storm.gif", R.drawable.rain_bg);
		backgroundImages.put("/ig/images/weather/rain.gif", R.drawable.rain_bg);
		backgroundImages.put("/ig/images/weather/chance_of_snow.gif", R.drawable.snow_bg);
		backgroundImages.put("/ig/images/weather/cloudy.gif", R.drawable.cloudy_bg);
		backgroundImages.put("/ig/images/weather/mist.gif", R.drawable.rain_bg);
		backgroundImages.put("/ig/images/weather/storm.gif", R.drawable.rain_bg);
		backgroundImages.put("/ig/images/weather/thunderstorm.gif", R.drawable.rain_bg);
		backgroundImages.put("/ig/images/weather/chance_of_tstorm.gif", R.drawable.rain_bg);
		backgroundImages.put("/ig/images/weather/sleet.gif", R.drawable.snow_bg);
		backgroundImages.put("/ig/images/weather/snow.gif", R.drawable.snow_bg);
		backgroundImages.put("/ig/images/weather/icy.gif", R.drawable.snow_bg);
		backgroundImages.put("/ig/images/weather/dust.gif", R.drawable.cloudy_bg);
		backgroundImages.put("/ig/images/weather/fog.gif", R.drawable.cloudy_bg);
		backgroundImages.put("/ig/images/weather/smoke.gif", R.drawable.cloudy_bg);
		backgroundImages.put("/ig/images/weather/haze.gif", R.drawable.cloudy_bg);
		backgroundImages.put("/ig/images/weather/flurries.gif", R.drawable.snow_bg);

	}

	/**
	 * Checking the forecasted weather conditions
	 * 
	 * @param document
	 */
	private void checkForecastConditions(Document document) {
		NodeList forecastConditionsNode = document.getElementsByTagName("forecast_conditions");
		for (int i = 0; i < forecastConditionsNode.getLength(); i++) {

			WeatherInfo weatherInfo = new WeatherInfo();

			Element element = (Element) forecastConditionsNode.item(i);

			NodeList conditionNL = element.getElementsByTagName("condition");
			Element line = (Element) conditionNL.item(0);
			weatherInfo.setCondition(line.getAttribute("data"));

			NodeList highNode = element.getElementsByTagName("high");
			line = (Element) highNode.item(0);
			weatherInfo.setHighTemperature(line.getAttribute("data"));

			NodeList lowNode = element.getElementsByTagName("low");
			line = (Element) lowNode.item(0);
			weatherInfo.setLowTemperature(line.getAttribute("data"));

			NodeList dayNode = element.getElementsByTagName("day_of_week");
			line = (Element) dayNode.item(0);
			weatherInfo.setDayOfWeek(line.getAttribute("data"));

			NodeList imageNode = element.getElementsByTagName("icon");
			line = (Element) imageNode.item(0);
			String image = line.getAttribute("data");

			if (dayIconMap.containsKey(image)) {
				weatherInfo.setDayIcon(dayIconMap.get(image));
			} else {
				weatherInfo.setDayIcon(R.drawable.w_partly_cloudy);
			}

			if (nightIconMap.containsKey(image)) {
				weatherInfo.setNightIcon(nightIconMap.get(image));
			} else {
				weatherInfo.setNightIcon(R.drawable.w_partly_cloudy_n);
			}

			if (backgroundImages.containsKey(image)) {
				weatherInfo.setNightIcon(backgroundImages.get(image));
			} else {
				weatherInfo.setNightIcon(R.drawable.sunny_bg);
			}

			forecastList.add(weatherInfo);
		}
	}

	/**
	 * Check the curren_conditions in the parsed document
	 * 
	 * @param document
	 */
	private void checkCurrentConditions(Document document) {
		NodeList currentConditionsNode = document.getElementsByTagName("current_conditions");
		if (currentConditionsNode.getLength() <= 0) {
			return;
		}
		Element element = (Element) currentConditionsNode.item(0);

		NodeList conditionNL = element.getElementsByTagName("condition");
		Element line = (Element) conditionNL.item(0);
		currentCondition = line.getAttribute("data");

		NodeList tempFNode = element.getElementsByTagName("temp_f");
		line = (Element) tempFNode.item(0);
		currentTemperatureF = line.getAttribute("data");

		NodeList tempCNode = element.getElementsByTagName("temp_c");
		line = (Element) tempCNode.item(0);
		currentTemperatureC = line.getAttribute("data");

		NodeList humidityNode = element.getElementsByTagName("humidity");
		line = (Element) humidityNode.item(0);
		currentHumidity = line.getAttribute("data");

		NodeList windNode = element.getElementsByTagName("wind_condition");
		line = (Element) windNode.item(0);
		currentWind = line.getAttribute("data");
	}

	/**
	 * Returns the current conditions in English readable form
	 * 
	 * @return
	 */
	public String getCurrentCondition() {
		return currentCondition;
	}

	/**
	 * Current temperature in Fahrenheit
	 * 
	 * @return
	 */
	public String getCurrentTemperatureF() {
		return currentTemperatureF;
	}

	/**
	 * Current temperature in Celcius
	 * 
	 * @return
	 */
	public String getCurrentTemperatureC() {
		return currentTemperatureC;
	}

	public String getCurrentHumidity() {
		return currentHumidity;
	}

	/**
	 * Current wind speed like -> Wind: S at 4 mph
	 * 
	 * @return
	 */
	public String getCurrentWind() {
		return currentWind;
	}

	/**
	 * Returns the forecasted list including present day's forecast
	 * 
	 * @return
	 */
	public List<WeatherInfo> getForecastList() {
		return forecastList;
	}

}