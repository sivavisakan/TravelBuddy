package edu.cmu.travelbuddy.alerts;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;


/**
 * 
 * @author amol
 *
 */
public class CalendarEventsActivity extends Activity {
    /** Called when the activity is first created. */
	
	private static long ALERT_TIME;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Calendar cal = Calendar.getInstance();              
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
       // intent.putExtra("beginTime", cal.getTimeInMillis());
        //intent.putExtra("allDay", true);
        //intent.putExtra("rrule", "FREQ=YEARLY");
       // intent.putExtra("endTime", cal.getTimeInMillis()+1*60*1000);
        intent.putExtra("title", "A Test Event from android app");
        startActivity(intent);
        finish();
    }
    
}