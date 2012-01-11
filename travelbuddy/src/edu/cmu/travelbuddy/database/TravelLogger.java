package edu.cmu.travelbuddy.database;

import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;

/**
 * Class to provide apis for easily accessing db for logging user activities
 * 
 * @author Ashwin Das
 * 
 */
public class TravelLogger {

	private static TravelLogger singletonObject;
	private static TravelLogDbAdapter dbHelper;

	/** A private Constructor prevents any other class from instantiating. */
	private TravelLogger() {

	}

	public static synchronized TravelLogger getInstance(Context context) {
		dbHelper = new TravelLogDbAdapter(context);
		dbHelper.open();
		if (singletonObject == null) {
			singletonObject = new TravelLogger();
		}
		return singletonObject;
	}

	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	/**
	 * Log the information required by passing the appropraite params
	 * 
	 * @param category
	 * @param title
	 * @param description
	 * @param extra
	 * @return
	 */
	public long log(String category, String title, String description, String extra) {
		return dbHelper.createTravelLog(category, title, description, extra);
	}

	/**
	 * Fetches all the logs in the database
	 * 
	 * @return
	 */
	public ArrayList<TravelLogInfo> getAllLogs() {
		Cursor cursor = dbHelper.fetchAllTravelLogs();
		ArrayList<TravelLogInfo> list = new ArrayList<TravelLogInfo>();
		int extraColumnIndex = cursor.getColumnIndex(TravelLogDbAdapter.KEY_EXTRA);
		int titleColumnIndex = cursor.getColumnIndex(TravelLogDbAdapter.KEY_TITLE);
		int categoryColumnIndex = cursor.getColumnIndex(TravelLogDbAdapter.KEY_CATEGORY);
		int descColumnIndex = cursor.getColumnIndex(TravelLogDbAdapter.KEY_DESCRIPTION);
		while (cursor.moveToNext()) {
			TravelLogInfo info = new TravelLogInfo();
			info.setCategory(cursor.getString(categoryColumnIndex));
			info.setTitle(cursor.getString(titleColumnIndex));
			info.setDescription(cursor.getString(descColumnIndex));
			info.setExtra(cursor.getString(extraColumnIndex));

			long millis = cursor.getLong(cursor.getColumnIndex(TravelLogDbAdapter.KEY_TIME));
			Date dateTime = new Date(millis);
			info.setDateTime(dateTime);
			
			list.add(info);
		}
		return list;

	}

	/**
	 * Clears all the existing logs
	 */
	public void clearAllLogs() {
		dbHelper.deleteAllTravelLogs();
	}

}
