package edu.cmu.travelbuddy.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * A helper class to manage and creation of travel log database
 * 
 * @author Ashwin Das
 * 
 */
class TravelLogDatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "travellogdatabase";

	private static final int DATABASE_VERSION = 1;

	/**
	 * Database creation sql statement
	 */
	private static final String DATABASE_CREATE = "create table IF NOT EXISTS travellog ("
			+ TravelLogDbAdapter.KEY_ROWID + " integer primary key autoincrement, "
			+ TravelLogDbAdapter.KEY_CATEGORY + " text not null," // //
			+ TravelLogDbAdapter.KEY_TITLE + " text not null," // ////
			+ TravelLogDbAdapter.KEY_DESCRIPTION + " text not null," // ////
			+ TravelLogDbAdapter.KEY_EXTRA + " text,"// ////
			+ TravelLogDbAdapter.KEY_TIME + " integer not null);"; // //////////

	public TravelLogDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * Method is called during creation of the database
	 */
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	/**
	 * Method is called during an upgrade of the database, e.g. if you increase
	 * the database version
	 */
	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		Log.w(TravelLogDatabaseHelper.class.getName(), "Upgrading database from version "
				+ oldVersion + " to " + newVersion + ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS todo");
		onCreate(database);
	}
}
