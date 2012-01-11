package edu.cmu.travelbuddy.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Database helper for expense tracking
 * @author Ashwin Das
 * 
 */
public class ExpenseDatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "expensedatabase";

	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table IF NOT EXISTS expense ("
			+ ExpenseDbAdapter.KEY_ROWID + " integer primary key autoincrement, "
			+ ExpenseDbAdapter.KEY_NAME + " text not null," // //
			+ ExpenseDbAdapter.KEY_DESCRIPTION + " text not null," // ////
			+ ExpenseDbAdapter.KEY_AMOUNT + "  float not null," // ////////
			+ ExpenseDbAdapter.KEY_TAG + " text not null," // ////////////
			+ ExpenseDbAdapter.KEY_LOCATION + " text not null," // ////////////////////
			+ ExpenseDbAdapter.KEY_TIME + " integer not null);"; // //////////

	public ExpenseDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Method is called during creation of the database
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	// Method is called during an upgrade of the database, e.g. if you increase
	// the database version
	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		Log.w(ExpenseDatabaseHelper.class.getName(), "Upgrading database from version "
				+ oldVersion + " to " + newVersion + ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS todo");
		onCreate(database);
	}
}
