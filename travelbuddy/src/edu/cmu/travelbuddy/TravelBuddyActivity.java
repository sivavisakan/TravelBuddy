package edu.cmu.travelbuddy;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import edu.cmu.travelbuddy.location.TLocationProvider;

public class TravelBuddyActivity extends Activity {

	/** Called when the activity is first created. */

	String enteredValue;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);

		final TLocationProvider locationProvider = TLocationProvider.getInstance();
		locationProvider.setContext(this);

		TextView txt = (TextView) findViewById(R.id.maintext1);
		Typeface font = Typeface.createFromAsset(getAssets(), "Acens.ttf");
		txt.setTypeface(font);

		TextView txt2 = (TextView) findViewById(R.id.maintext2);
		txt2.setTypeface(font);
		// txt2.setHeight(30);
		// txt2.setWidth(30);

		TextView txt3 = (TextView) findViewById(R.id.maintext3);
		txt3.setTypeface(font);
		// txt3.setHeight(30);
		// txt3.setWidth(30);
		TextView txt4 = (TextView) findViewById(R.id.maintext4);
		txt4.setTypeface(font);
		// txt4.setHeight(30);
		// txt4.setWidth(30);

		final EditText myET = (EditText) findViewById(R.id.edittext);
		myET.getBackground().setAlpha(120);

		Button myGoButton = (Button) findViewById(R.id.go);
		myGoButton.getBackground().setAlpha(120);
		myGoButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Get city/zip from text box
				enteredValue = myET.getText().toString().trim();
				if (enteredValue == null || enteredValue.equalsIgnoreCase("")) {
					Toast.makeText(getApplicationContext(), "Please enter ZIP or Country",
							Toast.LENGTH_SHORT).show();
				} else {
					Intent intent = new Intent(TravelBuddyActivity.this,
							GoingSomewhereActivity.class);
					/* Travel City */
					locationProvider.setLocationFromString(enteredValue);
					startActivity(intent);
				}
			}
		});

		txt3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/* Currently in town */
				locationProvider.setAutoLocation();
				startActivity(new Intent(TravelBuddyActivity.this, StayingAroundActivity.class));
			}
		});

		txt4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {/* Currently in town */
				locationProvider.setAutoLocation();
				startActivity(new Intent(TravelBuddyActivity.this, StayingAroundActivity.class));

			}
		});

		// EditText et = (EditText) findViewById(R.id.edittext);
		// et.append(" Sree= " + et.getText().toString());
		// et.setText("Welcome to CETEX2010 " + et.getText().toString());

		// Translate a single English String to French
		/*
		 * Translator translator = new Translator();
		 * System.out.println("Saying goodbye in French:"); String str =
		 * translator.translate("goodbye", Language.ENGLISH, Language.FRENCH);
		 * System.out.println(translator.translate("goodbye", Language.ENGLISH,
		 * Language.FRENCH));
		 * 
		 * System.out.println();
		 * 
		 * // Translate a single English String to various different languages
		 * System.out.println("Saying goodbye in different languages:");
		 * ArrayList<String> translations = translator.translate("goodbye",
		 * Language.ENGLISH, Language.ITALIAN, Language.DUTCH,
		 * Language.CHINESE_TRADITIONAL); for (String translation :
		 * translations) { System.out.println(translation); }
		 * 
		 * System.out.println();
		 * 
		 * // Translate multiple strings from one language to another
		 * System.out.println("Saying goodbye and hello in French:");
		 * translations = translator.translate(Language.ENGLISH,
		 * Language.FRENCH, "goodbye", "hello"); for (String translation :
		 * translations) { System.out.println(translation); }
		 * 
		 * // // //Print the translated text to the LogCat // //
		 * Log.v(“MyActivity”, translatedText); //
		 * 
		 * // Initializing the Location Provider Singleton TLocationProvider
		 * locationProvider = new TLocationProvider(this);
		 * 
		 * // locationProvider.getLocale().get
		 * 
		 * CurrencyProvider currencyProvider = new CurrencyProvider(); String
		 * convertedValue =
		 * currencyProvider.getConvertedCurrenyValue(locationProvider
		 * .getLocale()); /*
		 * 
		 * /* FOR WEATHER REPORT
		 */
		/*
		 * WeatherProvider weatherProvider = new WeatherProvider();
		 * weatherProvider
		 * .checkLatestWeatherConditions(locationProvider.getPostalCode());
		 * List<WeatherInfo> forecastList = weatherProvider.getForecastList();
		 */

		LinearLayout myLayout4 = (LinearLayout) findViewById(R.id.wrapLayout);
		myLayout4.setBackgroundResource(R.drawable.customshape);
	}
}