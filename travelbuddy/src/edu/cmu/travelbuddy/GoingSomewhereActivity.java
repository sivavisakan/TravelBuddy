package edu.cmu.travelbuddy;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;

import edu.cmu.travelbuddy.alerts.CalendarEventsActivity;
import edu.cmu.travelbuddy.citygrid.WhatToDoActivity;
import edu.cmu.travelbuddy.facebook.Places;
import edu.cmu.travelbuddy.facebook.TravelBuddy;
import edu.cmu.travelbuddy.facebook.Utility;
import edu.cmu.travelbuddy.image.ClickPicture;
import edu.cmu.travelbuddy.news.ReadRssFeed;
import edu.cmu.travelbuddy.pdf.PdfGenerator;
import edu.cmu.travelbuddy.unitconversion.UnitConversion;
import android.app.Activity;
import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SlidingDrawer;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class GoingSomewhereActivity extends ActivityGroup {

	/**
	 * THis activity is responsible for putting up the tabs in the Going Somewhere perspective.
	 * 
	 */
	
	private TabHost myTabHost;
	private TabHost.TabSpec myTabSpec;
	private Context myContext;

	public static final String APP_ID = "177833235639774";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// rRemoving the default title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.maintab);
		;

		Resources res = getResources(); // Resource object to get Drawables
		
		Intent intent; // Reusable Intent for each tab

		myContext = getApplicationContext();

		

		myTabHost = (TabHost) findViewById(android.R.id.tabhost);
		myTabHost.setup(getLocalActivityManager());
		intent = new Intent().setClass(this, WhatToDoActivity.class);
		setupTab(new TextView(this), "What to do?", intent);
		intent = new Intent().setClass(this, WeatherAndNews.class);
		setupTab(new TextView(this), "Weather+News", intent);
		intent = new Intent().setClass(this, TravelBuddy.class);
		setupTab(new TextView(this), "Social", intent);

		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final PopupWindow myPopUp = new PopupWindow(inflater.inflate(
				R.layout.popup, null, false), 100, 100, true);
		// The code below assumes that the root container has an id called
		// 'main'

		Button myFbCheckInButton = (Button) findViewById(R.id.facebookCheckIn);
		myFbCheckInButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Utility.mFacebook = new Facebook(APP_ID);
				// Instantiate the asynrunner object for asynchronous api calls.
				Utility.mAsyncRunner = new AsyncFacebookRunner(
						Utility.mFacebook);
				final Intent myIntent = new Intent(GoingSomewhereActivity.this,
						Places.class);
				myIntent.putExtra("LOCATION", "times_square");
				startActivity(myIntent);

			}
		});

		Button camera = (Button) findViewById(R.id.Camera);
		// camera.setLayoutParams(new LayoutParams(50, 50));
		camera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "Loading..",
						Toast.LENGTH_SHORT).show();
				startActivity(new Intent(GoingSomewhereActivity.this,
						ClickPicture.class));
			}
		});
		
		/**
		 * This button allows user to add an event
		 */

		Button addEvent = (Button) findViewById(R.id.addEvent);
		// addEvent.setLayoutParams(new LayoutParams(50, 50));
		addEvent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(GoingSomewhereActivity.this,
						CalendarEventsActivity.class));
			}
		});

		/**
		 * This button launches the pdf generator function
		 */
		
		Button pdfEvent = (Button) findViewById(R.id.pdf);
		// addEvent.setLayoutParams(new LayoutParams(50, 50));
		pdfEvent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "Creating PDF..",
						Toast.LENGTH_LONG).show();
				PdfGenerator pdfGen = new PdfGenerator();
				pdfGen.createPDF(GoingSomewhereActivity.this);
				Toast.makeText(getApplicationContext(), "PDF created!",
						Toast.LENGTH_LONG).show();
			}
		});
		
		/**
		 * This is the handler for the slider
		 */

		ImageButton mySlider = (ImageButton) findViewById(R.id.handle);
		mySlider.setLayoutParams(new LayoutParams(50, 50));
		mySlider.setScaleType(ImageButton.ScaleType.FIT_XY);
	}
	
	/**
	 * THis method sets up the tabs by adding specs to hosts.
	 * @param view
	 * @param tag
	 * @param myIntent
	 */

	private void setupTab(final View view, final String tag, Intent myIntent) {
		View tabview = createTabView(myTabHost.getContext(), tag);


		myTabSpec = myTabHost.newTabSpec("weather").setIndicator(tabview)
				.setContent(myIntent);
		myTabHost.addTab(myTabSpec);
		// myTabHost.addTab(setContent);
	}

	private static View createTabView(final Context context, final String text) {
		View view = LayoutInflater.from(context).inflate(R.layout.tab_bg, null);
		TextView tv = (TextView) view.findViewById(R.id.tabsText);
		tv.setText(text);
		return view;
	}

}
