package edu.cmu.travelbuddy.weather;

/**
 * POJO Class to hold weather information
 * 
 * @author Ashwin Das
 * 
 */
public class WeatherInfo {

	private String lowTemperature;
	private String highTemperature;
	private String condition;
	private String dayOfWeek;
	private int dayIcon;
	private int nightIcon;
	private int backgroundImage;

	public String getLowTemperature() {
		return lowTemperature;
	}

	public void setLowTemperature(String lowTemperature) {
		this.lowTemperature = lowTemperature;
	}

	public String getHighTemperature() {
		return highTemperature;
	}

	public void setHighTemperature(String highTemperature) {
		this.highTemperature = highTemperature;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	@Override
	public String toString() {
		return dayOfWeek + " : " + condition + " : High-" + highTemperature + " : Low-"
				+ lowTemperature;
	}

	public int getDayIcon() {
		return dayIcon;
	}

	public void setDayIcon(int dayIcon) {
		this.dayIcon = dayIcon;
	}

	public int getNightIcon() {
		return nightIcon;
	}

	public void setNightIcon(int nightIcon) {
		this.nightIcon = nightIcon;
	}

	public int getBackgroundImage() {
		return backgroundImage;
	}

	public void setBackgroundImage(int backgroundImage) {
		this.backgroundImage = backgroundImage;
	}

}
