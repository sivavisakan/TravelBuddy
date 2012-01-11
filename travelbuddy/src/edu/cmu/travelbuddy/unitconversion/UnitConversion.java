package edu.cmu.travelbuddy.unitconversion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.measure.Measure;
import javax.measure.quantity.Length;
import javax.measure.quantity.Mass;
import javax.measure.quantity.Temperature;
import javax.measure.quantity.Volume;
import javax.measure.unit.SI;
import javax.measure.unit.NonSI;
import javax.measure.unit.Unit;

import edu.cmu.travelbuddy.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * 
 * @author amol
 * 
 */
public class UnitConversion extends Activity {
	/** Called when the activity is first created. */

	ArrayList<String> myObjects;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		double gram = Measure.valueOf(5, NonSI.OUNCE).doubleValue(SI.GRAM);
		System.out.println(gram);
		String toDisp = "my gram value" + gram;
		TextView myTV = new TextView(this);
		myTV.setText(toDisp, null);
		Log.d("TRAVEL", toDisp);
		// setContentView(myTV);
		myObjects = UnitConverter.Categories;
		setContentView(R.layout.unit_conversion);

		Spinner spinner = (Spinner) findViewById(R.id.spinner);
		//ArrayAdapter<String> adapter = new ArrayAdapter(this, R.layout.unit_conversion,
		//		R.id.uctextview, myObjects);
		ArrayAdapter<String> adapter =  new ArrayAdapter<String>(this,  android.R.layout.simple_spinner_item, myObjects);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}

}

class UnitConverter {

	private static boolean userSI;

	private static final ArrayList<String> SICountries = new ArrayList<String>() {
		{
			add("India");
			add("United Kingdom");
		}
	};
	private static final ArrayList<String> NonSICountries = new ArrayList<String>() {
		{
			add("United States of America");
		}
	};
	public static final ArrayList<String> Categories = new ArrayList<String>() {
		{
			add("Length");
			add("Temperature");
			add("Weight");
			add("Area");
			add("Volume");
		}
	};

	private static final Unit[] SILengthUnit = { SI.CENTIMETER, SI.KILOMETER, SI.METER,
			SI.MILLIMETER };

	private static final Unit[] SILengthUnits = { SI.CENTIMETER, SI.KILOMETER, SI.METER,
			SI.MILLIMETER };

	private static final Unit[] NonSILengthUnits = { NonSI.FOOT, NonSI.INCH, NonSI.MILE };

	private static final Unit[] SIWeightUnits = { SI.GRAM, SI.KILOGRAM };

	private static final Unit[] NonSIWeightUnits = { NonSI.OUNCE };

	private static final Unit[] SIVolumeUnits = { SI.CUBIC_METRE };

	private static final Unit[] NonSIVolumeUnits = { NonSI.LITER };

	private static final Unit[] SITemperatureUnits = { SI.CELSIUS };

	private static final Unit[] NonSITemperatureUnits = { NonSI.FAHRENHEIT };

	public UnitConverter() {
		// on integration get country using getCountry and set userSI
		// accordingly
		userSI = true;
	}

	private static double convert(double inputVal, String category, Unit[] arrayName, int index,
			int categoryIndex) {
		// Determine Output unit specific to the user using his location
		// and the category he is looking at.

		Unit output = null;

		// The output is default set to the first metric in every category
		if (userSI) {
			if (categoryIndex == 0) {
				output = SILengthUnits[0];
			} else if (categoryIndex == 1) {
				output = SITemperatureUnits[0];
			} else if (categoryIndex == 2) {
				output = SIWeightUnits[0];
			}
		}
		if (output == null) {
			// DISPLAY ERROR
		}

		return Measure.valueOf(inputVal, arrayName[index]).doubleValue(output);
	}

}