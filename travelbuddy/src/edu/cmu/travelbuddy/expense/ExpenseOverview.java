package edu.cmu.travelbuddy.expense;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import edu.cmu.travelbuddy.R;
import edu.cmu.travelbuddy.database.ExpenseDbAdapter;
import edu.cmu.travelbuddy.facebook.ShowMap;
import edu.cmu.travelbuddy.citygrid.Details;

public class ExpenseOverview extends ListActivity {
	private ExpenseDbAdapter dbHelper;
	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;
	private static final int DELETE_ID = Menu.FIRST + 1;
	private Cursor cursor;

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.expense_list);
		this.getListView().setDividerHeight(2);
		dbHelper = new ExpenseDbAdapter(this);
		dbHelper.open();
		fillData();
		final Context c = this;
		//final Button button = (Button) findViewById(R.id.chart);
//        button.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//            	cursor = dbHelper.fetchAllTodos();
//            	ArrayList<String> item = new ArrayList<String>();
//            	ArrayList<String> prize = new ArrayList<String>();
//            	while(cursor.moveToNext()){
//            	item.add(cursor.getString(cursor.getColumnIndex(ExpenseDbAdapter.KEY_TAG)));
//            	prize.add(cursor.getString(cursor.getColumnIndex(ExpenseDbAdapter.KEY_AMOUNT)));
//            	}
//            	String[] from =  item.toArray(new String[item.size()]);
//        		String[] to =  (String[]) prize.toArray(new String[prize.size()]);
//        		int[] prizes = new int[to.length];
//        		for(int i = 0 ; i < to.length ; i++)
//        			prizes[i] = Integer.parseInt(to[i]);
//          	/*String[] from = {"asd","asdsd"};
//            	int[] prizes ={23,23};		*/
////            	
//            	//Intent achartIntent = new ExpenseChart().execute(c,from,prizes);
//            	//startActivity(achartIntent);
//            	/*Intent myIntent = new Intent(Details.this, ShowMap.class);
//            	myIntent.putExtra("lat", lat);
//            	myIntent.putExtra("lon", lon);
//            	startActivity(myIntent);*/
//            	
//            }
//        });
		registerForContextMenu(getListView());
	}

	// Create the menu based on the XML defintion
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.listmenu, menu);
		return true;
	}

	// Reaction to the menu selection
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.insert:
			createTodo();
			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.insert:
			createTodo();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case DELETE_ID:
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
			dbHelper.deleteTodo(info.id);
			fillData();
			return true;
		}
		return super.onContextItemSelected(item);
	}

	private void createTodo() {
		Intent i = new Intent(this, ExpenseActivity.class);
		startActivityForResult(i, ACTIVITY_CREATE);
	}

	// ListView and view (row) on which was clicked, position and
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent i = new Intent(this, ExpenseActivity.class);
		i.putExtra(ExpenseDbAdapter.KEY_ROWID, id);
		// Activity returns an result if called with startActivityForResult

		startActivityForResult(i, ACTIVITY_EDIT);
	}

	// Called with the result of the other activity
	// requestCode was the origin request code send to the activity
	// resultCode is the return code, 0 is everything is ok
	// intend can be use to get some data from the caller
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		fillData();

	}

	private void fillData() {
		cursor = dbHelper.fetchAllTodos();
		startManagingCursor(cursor);
		String[] from = new String[] { ExpenseDbAdapter.KEY_NAME };
		int[] to = new int[] { R.id.label };

		// Now create an array adapter and set it to display using our row
		SimpleCursorAdapter notes = new SimpleCursorAdapter(this, R.layout.expense_row, cursor, from,
				to);
		setListAdapter(notes);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, DELETE_ID, 0, R.string.menu_delete);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (dbHelper != null) {
			dbHelper.close();
		}
	}
}