package edu.cmu.travelbuddy.database;

import java.util.Date;

/**
 * POJO class for TravelLog in the database
 * 
 * @author Ashwin Das
 * 
 */
public class TravelLogInfo {

	private String category;
	private String title;
	private String description;
	private String extra;
	private Date dateTime;

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	@Override
	public String toString() {
		return "Category : " + category + " Title: " + title + " Description: " + description
				+ " Extra: " + extra + " DateTime:" + dateTime;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

}
