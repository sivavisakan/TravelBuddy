package edu.cmu.travelbuddy.expense;

import java.util.ArrayList;
import java.util.Locale;

import edu.cmu.travelbuddy.R;
import edu.cmu.travelbuddy.database.ExpenseDbAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;

public class ManageActivity extends Activity{

	
	private ExpenseDbAdapter dbHelper;
	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;
	private static final int DELETE_ID = Menu.FIRST + 1;
	private Cursor cursor;

	
	private ViewGroup myViewGroup;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manage);
		dbHelper = new ExpenseDbAdapter(this);
		dbHelper.open();
		fillData();
		final Context c = this;
		
		LinearLayout myLayout = (LinearLayout) findViewById(R.id.manageLayout);
		//myLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
         //       LayoutParams.WRAP_CONTENT));
		
		cursor = dbHelper.fetchAllTodos();
    	ArrayList<String> item = new ArrayList<String>();
    	ArrayList<String> prize = new ArrayList<String>();
    	while(cursor.moveToNext()){
    	item.add(cursor.getString(cursor.getColumnIndex(ExpenseDbAdapter.KEY_TAG)));
    	prize.add(cursor.getString(cursor.getColumnIndex(ExpenseDbAdapter.KEY_AMOUNT)));
    	}
    	String[] from =  item.toArray(new String[item.size()]);
		String[] to =  (String[]) prize.toArray(new String[prize.size()]);
		int[] prizes = new int[to.length];
		for(int i = 0 ; i < to.length ; i++)
			prizes[i] = Integer.parseInt(to[i]);
		//View myView = findViewById(R.id.chartView);
		View myView = new ExpenseChart().execute(c,from,prizes);
		myView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
		             200));
		myLayout.addView(myView, 0);
		//Button showExpenses = new Button(c);
		//showExpenses.set
		//showExpenses.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
         //       LayoutParams.WRAP_CONTENT));
		
		
		//Button addExpense =  new Button(c);
		//addExpense.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
        //        LayoutParams.WRAP_CONTENT));
		//
		//myLayout.addView(addExpense);
		//myLayout.addView(showExpenses);
		//myView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
         //       200));
		//myLayout.addView(myView);
		
		//myView.set
		
		Button addExpense = (Button) findViewById(R.id.addExpense);
		addExpense.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(ManageActivity.this, ExpenseActivity.class));
				
			}
		});
		Button showExpense = (Button) findViewById(R.id.showExpense);
		showExpense.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(ManageActivity.this, ExpenseOverview.class));
			}
		});
		
		//Locale l = new Locale("English", "")
		
		//setContentView(myLayout);
	}
	
	private void fillData() {
		cursor = dbHelper.fetchAllTodos();
		startManagingCursor(cursor);
		String[] from = new String[] { ExpenseDbAdapter.KEY_NAME };
		int[] to = new int[] { R.id.label };

		// Now create an array adapter and set it to display using our row
		//SimpleCursorAdapter notes = new SimpleCursorAdapter(this, R.layout.expense_row, cursor, from,
		//		to);
		//setListAdapter(notes);
	}
	
	
}
