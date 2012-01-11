package edu.cmu.travelbuddy.expense;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import edu.cmu.travelbuddy.R;
import edu.cmu.travelbuddy.database.ExpenseDbAdapter;
import edu.cmu.travelbuddy.database.TravelLogger;
import edu.cmu.travelbuddy.location.TLocationProvider;

public class ExpenseActivity extends Activity {
	private EditText mNameText;
	private EditText mDescriptionText;
	private EditText mTagText;
	private EditText mAmountText;
	private Long mRowId;
	private ExpenseDbAdapter mDbHelper;

	// private Spinner mCategory;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		mDbHelper = new ExpenseDbAdapter(this);
		mDbHelper.open();
		setContentView(R.layout.expense_edit);
		mNameText = (EditText) findViewById(R.id.todo_edit_name);
		mDescriptionText = (EditText) findViewById(R.id.todo_edit_description);
		mTagText = (EditText) findViewById(R.id.todo_edit_tag);
		mAmountText = (EditText) findViewById(R.id.todo_edit_amount);

		Button confirmButton = (Button) findViewById(R.id.todo_edit_button);
		mRowId = null;
		Bundle extras = getIntent().getExtras();
		mRowId = (bundle == null) ? null : (Long) bundle
				.getSerializable(ExpenseDbAdapter.KEY_ROWID);
		if (extras != null) {
			mRowId = extras.getLong(ExpenseDbAdapter.KEY_ROWID);
		}
		populateFields();
		confirmButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				setResult(RESULT_OK);
				//finish();
				String desc = mAmountText + "$ spent on" + mDescriptionText;
				TravelLogger.getInstance(ExpenseActivity.this).log("Expense", "New expense: " + mNameText, desc, "");
				startActivity(new Intent(ExpenseActivity.this, ManageActivity.class));
			}

		});
	}

	private void populateFields() {
		if (mRowId != null) {
			Cursor todo = mDbHelper.fetchTodo(mRowId);
			startManagingCursor(todo);
			mNameText
					.setText(todo.getString(todo.getColumnIndexOrThrow(ExpenseDbAdapter.KEY_NAME)));
			mDescriptionText.setText(todo.getString(todo
					.getColumnIndexOrThrow(ExpenseDbAdapter.KEY_DESCRIPTION)));
			mAmountText.setText(todo.getString(todo
					.getColumnIndexOrThrow(ExpenseDbAdapter.KEY_AMOUNT)));
			mTagText.setText(todo.getString(todo.getColumnIndexOrThrow(ExpenseDbAdapter.KEY_TAG)));
		}
	}

	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		saveState();
		outState.putSerializable(ExpenseDbAdapter.KEY_ROWID, mRowId);
	}

	@Override
	protected void onPause() {
		super.onPause();
		saveState();
	}

	@Override
	protected void onResume() {
		super.onResume();
		populateFields();
	}

	private void saveState() {
		String name = mNameText.getText().toString();
		String description = mDescriptionText.getText().toString();
		
		String tag = mTagText.getText().toString();
		if(tag.equals("") || tag == null)
			tag = "unsorted";
		
		String amtStr = mAmountText.getText().toString().trim();
		if(amtStr.equals("") || amtStr == null)
			amtStr = "0";
		
		float amount = Float.valueOf(amtStr).floatValue();
		String location = TLocationProvider.getInstance().getAddress();
//		String location = "Area";
		if (mRowId == null) {
			long id = mDbHelper.createTodo(name, description, amount, tag, location);
			if (id > 0) {
				mRowId = id;
			}
		} else {
			mDbHelper.updateTodo(mRowId, name, description, amount, tag, location);
		}
	}
}