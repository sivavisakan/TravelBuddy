package edu.cmu.travelbuddy.location;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

/**
 * 
 * A Singleton which provides easy access to current location.
 * 
 * @author Ashwin Das
 * 
 */
public class TLocationProvider {

	private static TLocationProvider singletonObject;

	/** A private Constructor prevents any other class from instantiating. */
	private TLocationProvider() {

	}

	public static synchronized TLocationProvider getInstance() {
		if (singletonObject == null) {
			singletonObject = new TLocationProvider();
		}
		return singletonObject;
	}

	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	public void setContext(Context context) {
		this.activity = context;

		// Acquire a reference to the system Location Manager
		LocationManager locationManager = (LocationManager) activity
				.getSystemService(Context.LOCATION_SERVICE);

		// Define a listener that responds to location updates
		// LocationListener locationListener = new LocationListener() {
		// public void onLocationChanged(Location location) {
		// // Called when a new location is found by the network location
		// // provider.
		// setLocation(location);
		// }
		//
		// public void onStatusChanged(String provider, int status, Bundle
		// extras) {
		// }
		//
		// public void onProviderEnabled(String provider) {
		// }
		//
		// public void onProviderDisabled(String provider) {
		// }
		// };

		/*
		 * Register the listener with the Location Manager to receive location
		 * updates
		 */
		// if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
		// {
		// locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
		// 0, 0,
		// locationListener);
		// } else {
		// locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
		// 0, 0,
		// locationListener);
		// }

		/* For the first run - Getting a fast fix with the last known location */
		setLocation(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));

	}

	private Location location;
	private Context activity;
	private String address;
	private String postalCode;
	private Locale locale;
	private double latitude;
	private double longitude;

	// public TLocationProvider(Activity activity) {
	// this.activity = activity;
	//
	// // Acquire a reference to the system Location Manager
	// LocationManager locationManager = (LocationManager) activity
	// .getSystemService(Context.LOCATION_SERVICE);
	//
	// // Define a listener that responds to location updates
	// LocationListener locationListener = new LocationListener() {
	// public void onLocationChanged(Location location) {
	// // Called when a new location is found by the network location
	// // provider.
	// setLocation(location);
	// }
	//
	// public void onStatusChanged(String provider, int status, Bundle extras) {
	// }
	//
	// public void onProviderEnabled(String provider) {
	// }
	//
	// public void onProviderDisabled(String provider) {
	// }
	// };
	//
	// /*
	// * Register the listener with the Location Manager to receive location
	// * updates
	// */
	// if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
	// locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
	// 0,
	// locationListener);
	// } else {
	// locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
	// 0, 0,
	// locationListener);
	// }
	//
	// /* For the first run - Getting a fast fix with the last known location */
	// setLocation(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
	// }

	public String getAddress() {
		return address;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public Locale getLocale() {
		return locale;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void refreshLocationDetails() {
		Geocoder geocoder = new Geocoder(activity, Locale.getDefault());

		/*
		 * Get an array of Addresses that are known to describe the area
		 * immediately surrounding the given latitude and longitude.
		 */
		try {
			List<Address> addresses = geocoder.getFromLocation(getLatitude(), getLongitude(), 1);
			StringBuilder addressStringBuilder = new StringBuilder();
			for (int i = 0; i < addresses.get(0).getMaxAddressLineIndex(); i++) {
				addressStringBuilder.append(addresses.get(0).getAddressLine(i)).append(" ");
			}
			address = addressStringBuilder.toString();
			postalCode = addresses.get(0).getPostalCode();
			locale = addresses.get(0).getLocale();
		} catch (IOException e) {
			Log.w("T_LocationException", e);
		}

	}

	public void setAutoLocation() {
		if (null != location) {
			latitude = this.location.getLatitude();
			longitude = this.location.getLongitude();
			refreshLocationDetails();
		} else {
			setLocationFromString("Carnegie Mellon University, Pittsburgh");
		}
	}

	public void setLocationFromString(String locationHint) {

		Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
		try {
			List<Address> addresses = geocoder.getFromLocationName(locationHint, 1);
			StringBuilder addressStringBuilder = new StringBuilder();
			for (int i = 0; i < addresses.get(0).getMaxAddressLineIndex(); i++) {
				addressStringBuilder.append(addresses.get(0).getAddressLine(i)).append(" ");
			}
			latitude = addresses.get(0).getLatitude();
			longitude = addresses.get(0).getLongitude();
			address = addressStringBuilder.toString();
			postalCode = addresses.get(0).getPostalCode();
			locale = addresses.get(0).getLocale();
		} catch (IOException e) {
			Log.w("T_LocationException", e);
		}
	}

	private void setLocation(Location location) {
		this.location = location;
		if (null != location) {
			latitude = this.location.getLatitude();
			longitude = this.location.getLongitude();
			refreshLocationDetails();
		} else {
			setLocationFromString("Carnegie Mellon University, Pittsburgh");
		}
	}

}