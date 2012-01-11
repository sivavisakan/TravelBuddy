package edu.cmu.travelbuddy.facebook;

import java.util.List;


import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import edu.cmu.travelbuddy.R;
import edu.cmu.travelbuddy.location.TLocationProvider;

public class ShowMap extends MapActivity {

	private MapController mapController;
	private MapView mapView;
	private LocationManager locationManager;

	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.main1); // bind the layout to the activity

		// create a map view
		MapView mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);

		List<Overlay> mapOverlays = mapView.getOverlays();
		Drawable drawablePlace = this.getResources().getDrawable(R.drawable.markerblue);
		Drawable drawable = this.getResources().getDrawable(R.drawable.marker);
		Markers markerPlace = new Markers(drawablePlace, this);
		Markers markerYou = new Markers(drawable, this);
		TLocationProvider yourLocation = TLocationProvider.getInstance();
		yourLocation.refreshLocationDetails();
		Double l = yourLocation.getLatitude();
		Double g = yourLocation.getLongitude();
		int myLat = (int) (l * 1000000);
		int myLon = (int) (g * 1000000);
		GeoPoint startPoint = new GeoPoint(myLat, myLon);
		OverlayItem startMarker = new OverlayItem(startPoint, "Hello",
				"You are here!");
		startMarker.setMarker(drawable);
		markerYou.addOverlay(startMarker);
		mapOverlays.add(markerYou);

		int lat = getIntent().getExtras().getInt("lat");
		int lon = getIntent().getExtras().getInt("lon");
		GeoPoint stopPoint = new GeoPoint(lat, lon);
		OverlayItem stopMarker = new OverlayItem(stopPoint, "",
				"I'm in Mexico City!");
		stopMarker.setMarker(drawablePlace);
		markerPlace.addOverlay(stopMarker);
		mapOverlays.add(markerPlace);
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

}