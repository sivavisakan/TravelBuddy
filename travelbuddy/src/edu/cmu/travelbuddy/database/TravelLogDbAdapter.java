package edu.cmu.travelbuddy.database;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Class to track the travel related activities in a database
 * 
 * @author Ashwin Das
 * 
 */
class TravelLogDbAdapter {

	// Database fields
	public static final String KEY_ROWID = "_id";
	public static final String KEY_CATEGORY = "category";
	public static final String KEY_TITLE = "title";
	public static final String KEY_DESCRIPTION = "description";
	public static final String KEY_EXTRA = "extra";
	public static final String KEY_TIME = "time";
	private static final String DATABASE_TABLE = "travellog";

	private Context context;
	private SQLiteDatabase database;
	private TravelLogDatabaseHelper dbHelper;

	public TravelLogDbAdapter(Context context) {
		this.context = context;
	}

	public TravelLogDbAdapter open() throws SQLException {
		dbHelper = new TravelLogDatabaseHelper(context);
		database = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		dbHelper.close();
	}

	/**
	 * Create a new log. If the log is successfully created return the new rowId
	 * for that note, otherwise return a -1 to indicate failure.
	 */
	public long createTravelLog(String category, String title, String description, String extra) {
		ContentValues initialValues = createContentValues(category, title, description, extra);
		return database.insert(TravelLogDbAdapter.DATABASE_TABLE, null, initialValues);
	}

	/**
	 * Update the log
	 */
	public boolean updateTravelLog(long rowId, String category, String title, String description,
			String extra) {
		ContentValues updateValues = createContentValues(category, title, description, extra);
		return database.update(DATABASE_TABLE, updateValues, KEY_ROWID + "=" + rowId, null) > 0;
	}

	/**
	 * Deletes todo
	 */
	public boolean deleteTravelLog(long rowId) {
		return database.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}

	/**
	 * Return a Cursor over the list of all todo in the database * * @return
	 * Cursor over all notes
	 */
	public Cursor fetchAllTravelLogs() {
		return database.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_CATEGORY, KEY_TITLE,
				KEY_DESCRIPTION, KEY_EXTRA, KEY_TIME }, null, null, null, null, null);
	}

	/**
	 * Clears all the log entries from the database
	 */
	public void deleteAllTravelLogs() {
		database.delete(DATABASE_TABLE, null, null);
	}

	/**
	 * Return a Cursor positioned at the defined todo
	 */
	public Cursor fetchTravelLog(long rowId) throws SQLException {
		Cursor mCursor = database.query(true, DATABASE_TABLE, new String[] { KEY_ROWID,
				KEY_CATEGORY, KEY_TITLE, KEY_DESCRIPTION, KEY_EXTRA, KEY_TIME }, KEY_ROWID + "="
				+ rowId, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	private ContentValues createContentValues(String category, String title, String description,
			String extra) {
		ContentValues values = new ContentValues();
		values.put(KEY_CATEGORY, category);
		values.put(KEY_TITLE, title);
		values.put(KEY_DESCRIPTION, description);
		values.put(KEY_EXTRA, extra);

		long time = new Date().getTime();
		values.put(KEY_TIME, time);

		return values;
	}
}