package edu.cmu.travelbuddy.database;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Database adapter for expense tracking
 * 
 * @author Ashwin Das
 * 
 */
public class ExpenseDbAdapter {

	// Database fields
	public static final String KEY_ROWID = "_id";
	public static final String KEY_NAME = "expensename";
	public static final String KEY_DESCRIPTION = "description";
	public static final String KEY_AMOUNT = "amount";
	public static final String KEY_TAG = "tag";
	public static final String KEY_LOCATION = "location";
	public static final String KEY_TIME = "time";
	private static final String DATABASE_TABLE = "expense";

	private Context context;
	private SQLiteDatabase database;
	private ExpenseDatabaseHelper dbHelper;

	public ExpenseDbAdapter(Context context) {
		this.context = context;
	}

	public ExpenseDbAdapter open() throws SQLException {
		dbHelper = new ExpenseDatabaseHelper(context);
		database = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		dbHelper.close();
	}

	/**
	 * Create a new todo If the todo is successfully created return the new
	 * rowId for that note, otherwise return a -1 to indicate failure.
	 */
	public long createTodo(String name, String description, float amount, String tag,
			String location) {
		ContentValues initialValues = createContentValues(name, description, amount, tag, location);

		return database.insert(DATABASE_TABLE, null, initialValues);
	}

	/**
	 * Update the todo
	 */
	public boolean updateTodo(long rowId, String name, String description, float amount,
			String tag, String location) {
		ContentValues updateValues = createContentValues(name, description, amount, tag, location);
		return database.update(DATABASE_TABLE, updateValues, KEY_ROWID + "=" + rowId, null) > 0;
	}

	/**
	 * Deletes todo
	 */
	public boolean deleteTodo(long rowId) {
		return database.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}

	/**
	 * Return a Cursor over the list of all todo in the database * * @return
	 * Cursor over all notes
	 */
	public Cursor fetchAllTodos() {
		return database.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_NAME, KEY_DESCRIPTION,
				KEY_AMOUNT, KEY_TAG, KEY_LOCATION, KEY_TIME }, null, null, null, null, null);
	}

	/**
	 * Return a Cursor positioned at the defined todo
	 */
	public Cursor fetchTodo(long rowId) throws SQLException {
		Cursor mCursor = database.query(true, DATABASE_TABLE, new String[] { KEY_ROWID, KEY_NAME,
				KEY_DESCRIPTION, KEY_AMOUNT, KEY_TAG, KEY_LOCATION, KEY_TIME }, KEY_ROWID + "="
				+ rowId, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	private ContentValues createContentValues(String name, String description, float amount,
			String tag, String location) {
		ContentValues values = new ContentValues();
		values.put(KEY_NAME, name);
		values.put(KEY_DESCRIPTION, description);
		values.put(KEY_AMOUNT, amount);
		values.put(KEY_TAG, tag);
		values.put(KEY_LOCATION, location);
		
		long time = new Date().getTime();
		values.put(KEY_TIME, time);
		
		return values;
	}
}