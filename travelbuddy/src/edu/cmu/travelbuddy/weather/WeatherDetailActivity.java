package edu.cmu.travelbuddy.weather;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;
import edu.cmu.travelbuddy.R;
import edu.cmu.travelbuddy.WeatherAndNews.AddImgAdp;
import edu.cmu.travelbuddy.location.TLocationProvider;

/**
 * Activity to populate weather
 * 
 * @author Ashwin Das
 * 
 */

public class WeatherDetailActivity extends Activity {

	private ViewFlipper vf;
	private float oldTouchValue;
	private Gallery gallery;
	
	private static int width;
	private static int height;

	private ArrayList<Integer> myImageList = new ArrayList<Integer>();
	private ArrayList<Integer> myBGImageList = new ArrayList<Integer>();
	java.util.List<WeatherInfo> myWeatherList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weather_main);

		WeatherProvider weatherProvider = new WeatherProvider();
		/* Change location - hard coded now */
		weatherProvider.checkLatestWeatherConditions("15213");

		vf = (ViewFlipper) findViewById(R.id.layoutswitcher);
		
		WeatherProvider myWeather =  new WeatherProvider();
		myWeather.checkLatestWeatherConditions("15213");
		myWeatherList = myWeather.getForecastList();

		List<WeatherInfo> forecastList = weatherProvider.getForecastList();
		for (WeatherInfo wInfo : myWeatherList) {
			//String filename = wInfo.getImage();
			//String uri = "drawable/" + filename;
			//Log.d("URI","URI : "+uri);
			//Log.d("Image for gallery", "image name : "+getResources().getIdentifier(uri, "drawable", this.getPackageName()));
			//Log.d("Package", "package : "+getPackageName());
			//myImageList.add(getResources().getIdentifier(uri, "drawable", "edu.cmu."));
			myImageList.add(wInfo.getDayIcon());
			myBGImageList.add(wInfo.getBackgroundImage());
		}

	
		
		FrameLayout myFrame1 = (FrameLayout) findViewById(R.id.wframe1);
		myFrame1.setBackgroundResource(R.drawable.customshape);
		FrameLayout myFrame2 = (FrameLayout) findViewById(R.id.wframe2);
		myFrame2.setBackgroundResource(R.drawable.customshape);
		FrameLayout myFrame3 = (FrameLayout) findViewById(R.id.wframe3);
		myFrame3.setBackgroundResource(R.drawable.customshape);
		FrameLayout myFrame4 = (FrameLayout) findViewById(R.id.wframe4);
		myFrame4.setBackgroundResource(R.drawable.customshape);

		populateWeatherUI(forecastList);
		
		
		
		
		
	

	}
	


	/**
	 * Populates the UI of weather
	 * 
	 * @param forecastList
	 */
	private void populateWeatherUI(List<WeatherInfo> forecastList) {
		TextView currentDay = (TextView) findViewById(R.id.currentDay);
		TextView next1Day = (TextView) findViewById(R.id.next1Day);
		TextView next2Day = (TextView) findViewById(R.id.next2Day);
		TextView next3Day = (TextView) findViewById(R.id.next3Day);

		currentDay.setText(forecastList.get(0).getDayOfWeek());
		next1Day.setText(forecastList.get(1).getDayOfWeek());
		next2Day.setText(forecastList.get(2).getDayOfWeek());
		next3Day.setText(forecastList.get(3).getDayOfWeek());

		TextView currentConditions = (TextView) findViewById(R.id.currentConditions);
		TextView next1Conditions = (TextView) findViewById(R.id.next1Conditions);
		TextView next2Conditions = (TextView) findViewById(R.id.next2Conditions);
		TextView next3Conditions = (TextView) findViewById(R.id.next3Conditions);

		currentConditions.setText("Conditions: " + forecastList.get(0).getCondition());
		next1Conditions.setText("Conditions: " + forecastList.get(1).getCondition());
		next2Conditions.setText("Conditions: " + forecastList.get(2).getCondition());
		next3Conditions.setText("Conditions: " + forecastList.get(3).getCondition());

		TextView currentHigh = (TextView) findViewById(R.id.currentHigh);
		TextView next1High = (TextView) findViewById(R.id.next1High);
		TextView next2High = (TextView) findViewById(R.id.next2High);
		TextView next3High = (TextView) findViewById(R.id.next3High);

		currentHigh.setText("High: " + forecastList.get(0).getHighTemperature());
		next1High.setText("High: " + forecastList.get(1).getHighTemperature());
		next2High.setText("High: " + forecastList.get(2).getHighTemperature());
		next3High.setText("High: " + forecastList.get(3).getHighTemperature());

		TextView currentLow = (TextView) findViewById(R.id.currentLow);
		TextView next1Low = (TextView) findViewById(R.id.next1Low);
		TextView next2Low = (TextView) findViewById(R.id.next2Low);
		TextView next3Low = (TextView) findViewById(R.id.next3Low);

		currentLow.setText("Low: " + forecastList.get(0).getLowTemperature());
		next1Low.setText("Low: " + forecastList.get(1).getLowTemperature());
		next2Low.setText("Low: " + forecastList.get(2).getLowTemperature());
		next3Low.setText("Low: " + forecastList.get(3).getLowTemperature());

		
		ImageView currentImage = (ImageView)findViewById(R.id.currentWeatherImage);
		ImageView next1Image = (ImageView) findViewById(R.id.next1Image);
		ImageView next2Image = (ImageView) findViewById(R.id.next2Image);
		ImageView next3Image = (ImageView) findViewById(R.id.next3Image);
		
		currentImage.setImageResource(myImageList.get(0));
		next1Image.setImageResource(myImageList.get(1));
		next2Image.setImageResource(myImageList.get(2));
		next3Image.setImageResource(myImageList.get(3));
		
		LinearLayout wlayout1 = (LinearLayout)findViewById(R.id.wlayout1);
		wlayout1.setBackgroundResource(myBGImageList.get(0));
		LinearLayout wlayout2 = (LinearLayout)findViewById(R.id.wlayout2);
		wlayout2.setBackgroundResource(myBGImageList.get(1));
		LinearLayout wlayout3 = (LinearLayout)findViewById(R.id.wlayout3);
		wlayout3.setBackgroundResource(myBGImageList.get(2));
		LinearLayout wlayout4 = (LinearLayout)findViewById(R.id.wlayout4);
		wlayout4.setBackgroundResource(myBGImageList.get(3));
		
		/* Set images here */

		// ImageView currentImage = (ImageView)
		// findViewById(R.id.currentWeatherImage);
		// ImageView next1Image = (ImageView)
		// findViewById(R.id.next1WeatherImage);
		// ImageView next2Image = (ImageView)
		// findViewById(R.id.next2WeatherImage);
		// ImageView next3Image = (ImageView)
		// findViewById(R.id.next3WeatherImage);
		//
		// currentImage.setImageResource(R.drawable.w_mostlysunny);
		// next1Image.setImageResource(R.drawable.w_showers);
		// next2Image.setImageResource(R.drawable.w_chances_of_shower);
		// next3Image.setImageResource(R.drawable.w_chances_of_shower);
	}

	@Override
	public boolean onTouchEvent(MotionEvent touchevent) {
		switch (touchevent.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			oldTouchValue = touchevent.getX();
			break;
		}
		case MotionEvent.ACTION_UP: {
			// if(this.searchOk==false) return false;
			float currentX = touchevent.getX();
			if (oldTouchValue < currentX) {
				vf.setInAnimation(AnimationHelper.inFromLeftAnimation());
				vf.setOutAnimation(AnimationHelper.outToRightAnimation());
				vf.showNext();
			}
			if (oldTouchValue > currentX) {
				vf.setInAnimation(AnimationHelper.inFromRightAnimation());
				vf.setOutAnimation(AnimationHelper.outToLeftAnimation());
				vf.showPrevious();
			}
			break;
		}
		}
		return false;
	}



}
