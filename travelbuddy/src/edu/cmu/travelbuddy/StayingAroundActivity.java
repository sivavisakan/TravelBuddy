package edu.cmu.travelbuddy;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;

import edu.cmu.travelbuddy.alerts.CalendarEventsActivity;
import edu.cmu.travelbuddy.citygrid.WhatToDoActivity;
import edu.cmu.travelbuddy.expense.ExpenseActivity;
import edu.cmu.travelbuddy.expense.ManageActivity;
import edu.cmu.travelbuddy.facebook.Places;
import edu.cmu.travelbuddy.facebook.TravelBuddy;
import edu.cmu.travelbuddy.facebook.Utility;
import edu.cmu.travelbuddy.image.ClickPicture;
import edu.cmu.travelbuddy.news.ReadRssFeed;
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
import android.widget.PopupWindow;
import android.widget.SlidingDrawer;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;



public class StayingAroundActivity extends ActivityGroup{

	private TabHost myTabHost;
	private TabHost.TabSpec myTabSpec;
	private Context myContext;
	
	public static final String APP_ID = "177833235639774";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.maintab);
		//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar);

		Resources res = getResources(); // Resource object to get Drawables
		//TabHost tabHost = getTabHost();  // The activity TabHost
		//TabHost.TabSpec spec;  // Resusable TabSpec for each tab
		Intent intent; // Reusable Intent for each tab
		
		myContext = getApplicationContext();
		
		//DisplayMetrics metrics = new DisplayMetrics();
		//this.getWindowManager().getDefaultDisplay().getMetrics(metrics);

		//Gallery gallery = (Gallery) this.findViewById(R.id.examplegallery);

		//MarginLayoutParams mlp = (MarginLayoutParams) gallery.getLayoutParams();
		//mlp.setMargins(-(400), 
		//               mlp.topMargin, 
		//               mlp.rightMargin, 
		//               mlp.bottomMargin
		//);
		//gallery.requestLayout();
		//gallery.setSelection(4, true);
		
		// Create an Intent to launch an Activity for the tab (to be reused)
		

		// Initialize a TabSpec for each tab and add it to the TabHost
		/*
		 * spec = tabHost.newTabSpec("weather").setIndicator("Weather")
		 * .setContent(intent); tabHost.addTab(spec);
		 * 
		 * // Do the same for the other tabs intent = new
		 * Intent().setClass(this, archiveActivity.class); spec =
		 * tabHost.newTabSpec("archives").setIndicator("Archives")
		 * .setContent(intent); tabHost.addTab(spec);
		 * 
		 * 
		 * intent = new Intent().setClass(this, subscriptionActivity.class);
		 * spec = tabHost.newTabSpec("subscription").setIndicator("Subscribe")
		 * .setContent(intent); tabHost.addTab(spec);
		 * 
		 * tabHost.setCurrentTab(0);
		 */

		
		
		myTabHost = (TabHost) findViewById(android.R.id.tabhost);
		myTabHost.setup(getLocalActivityManager());
		intent = new Intent().setClass(this, WhatToDoActivity.class);
		setupTab(new TextView(this), "What to do?", intent);
		intent = new Intent().setClass(this, WeatherAndNews.class);
		setupTab(new TextView(this), "Weather+News", intent);
		intent = new Intent().setClass(this, ManageActivity.class);
		setupTab(new TextView(this), "Manage", intent);
		intent = new Intent().setClass(this, TravelBuddy.class);
		setupTab(new TextView(this), "FB", intent);
		
		//SlidingDrawer myDrawer = (SlidingDrawer) this.findViewById(R.id.SlidingDrawer);
		//myDrawer.
		
		LayoutInflater inflater = (LayoutInflater)
			       this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final PopupWindow myPopUp = new PopupWindow(
			       inflater.inflate(R.layout.popup, null, false), 
			       100, 
			       100, 
			       true);
			    // The code below assumes that the root container has an id called 'main'
			    
		
		Button myFbCheckInButton = (Button) findViewById(R.id.facebookCheckIn);
		myFbCheckInButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//myPopUp.showAtLocation((View)findViewById(R.id.facebookCheckIn), Gravity.CENTER, 0, 0); 
				//TravelBuddy myFBHandler = new TravelBuddy();
				//myFBHandler.facebookActions(3, myContext);
				Utility.mFacebook = new Facebook(APP_ID);
		       	//Instantiate the asynrunner object for asynchronous api calls.
		       	Utility.mAsyncRunner = new AsyncFacebookRunner(Utility.mFacebook);
				final Intent myIntent = new Intent(StayingAroundActivity.this, Places.class);
				myIntent.putExtra("LOCATION", "times_square");
            	startActivity(myIntent);
				
				/*
final Intent myIntent = new Intent(getApplicationContext(), Places.class);
                
    			new AlertDialog.Builder(HelloTabWidget.this)
    	        .setTitle(R.string.get_location)
    	        .setMessage(R.string.get_default_or_new_location)
    	        .setPositiveButton(R.string.current_location_button, new DialogInterface.OnClickListener() {
    	            @Override
    	            public void onClick(DialogInterface dialog, int which) {
    	            	myIntent.putExtra("LOCATION", "current");
    	            	startActivity(myIntent);
    	            }
    	        })
    	        .setNegativeButton(R.string.times_square_button, new DialogInterface.OnClickListener() {
    	            @Override
    	            public void onClick(DialogInterface dialog, int which) {
    	            	myIntent.putExtra("LOCATION", "times_square");
    	            	startActivity(myIntent);
    	            }
    	
    	        })
    	        .show();
    	        */
			}
		});
		
		Button camera = (Button)findViewById(R.id.Camera);
		//camera.setLayoutParams(new LayoutParams(50, 50));
		camera.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(),
						"Loading..",
						Toast.LENGTH_SHORT).show();
				startActivity(new Intent(StayingAroundActivity.this, ClickPicture.class));
			}
		});
		
		Button addEvent = (Button)findViewById(R.id.addEvent);
		//addEvent.setLayoutParams(new LayoutParams(50, 50));
		addEvent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(StayingAroundActivity.this, CalendarEventsActivity.class));	
			}
		});
		
		ImageButton mySlider =  (ImageButton)findViewById(R.id.handle);
		mySlider.setLayoutParams(new LayoutParams(50, 50));
		mySlider.setScaleType(ImageButton.ScaleType.FIT_XY);
	}


	private void setupTab(final View view, final String tag, Intent myIntent) {
		View tabview = createTabView(myTabHost.getContext(), tag);
		//TabSpec setContent = myTabHost.newTabSpec(tag).setIndicator(tabview).setContent(new TabContentFactory() {
		//		public View createTabContent(String tag) {return view;}
		//});
		
	   
	    myTabSpec = myTabHost.newTabSpec("weather").setIndicator(tabview)
	                  .setContent(myIntent);
	    myTabHost.addTab(myTabSpec);
		//myTabHost.addTab(setContent);
	}

	private static View createTabView(final Context context, final String text) {
		View view = LayoutInflater.from(context).inflate(R.layout.tab_bg, null);
		TextView tv = (TextView) view.findViewById(R.id.tabsText);
		tv.setText(text);
		return view;
	}
	
	
	
	

}
