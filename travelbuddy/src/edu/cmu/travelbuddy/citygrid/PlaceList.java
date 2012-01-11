package edu.cmu.travelbuddy.citygrid;

import java.util.ArrayList;

import com.citygrid.CGException;
import com.citygrid.CGLatLon;
import com.citygrid.CityGrid;
import com.citygrid.content.places.search.CGPlacesSearch;
import com.citygrid.content.places.search.CGPlacesSearchLocation;
import com.citygrid.content.places.search.CGPlacesSearchResults;
import com.citygrid.content.reviews.CGReviewsSearchReview;

import edu.cmu.travelbuddy.R;
import edu.cmu.travelbuddy.location.TLocationProvider;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class PlaceList extends Activity {
	
	private CGPlacesSearch mySearchHandle;
	
	CGPlacesSearchLocation[] cgPlacesSearchLocation = null;
	CGReviewsSearchReview[] cgReviews = null;
	
	private ArrayList<CGPlacesSearchLocation> myPlacesArrayList;
	
	private String[] places;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.placelist);
		
		CityGrid.setPublisher("test");
		CityGrid.setSimulation(false);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String category = extras.getString("category");
			TLocationProvider lp = TLocationProvider.getInstance();
			mySearchHandle = CityGrid.placesSearch();
			mySearchHandle.setWhere(lp.getAddress());
			mySearchHandle.setWhat(category);
		}
		
		myPlacesArrayList = new ArrayList<CGPlacesSearchLocation>();
		CGPlacesSearchResults results = null;
		try {
			//String[] places = null;
			results = mySearchHandle.search();

			if (results != null) {
				cgPlacesSearchLocation = results.getLocations();
				if (cgPlacesSearchLocation != null) {
					places = new String[cgPlacesSearchLocation.length];
					int i = 0;
					for (CGPlacesSearchLocation location : cgPlacesSearchLocation) {
						//StringBuilder sb = new StringBuilder();
						//sb.append(location.getName()).append("\n");
						//sb.append(location.getNeighborhood());
						
						//places[i++] = sb.toString();
						myPlacesArrayList.add(location);
					}
				}
			}
		} catch (CGException e) {
			Log.e("List Activity", "Exception finding ");
			//places = new String[] { "Exception finding " + this.what
			//		+ " offers: " + e.getMessage() };
		}
		
		ListView myList = (ListView)findViewById(R.id.listplaces);
		myList.setAdapter(new CityGridPlacesCustomBaseAdapter(this, myPlacesArrayList));
		
		myList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Toast.makeText(getApplicationContext(),
						"Loading..",
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(PlaceList.this, Details.class);
				intent.putExtra("category", "Places");
				String publicId = myPlacesArrayList.get(arg2).getPublicId();
				intent.putExtra("publicId", publicId);
				CGLatLon myLatLon = myPlacesArrayList.get(arg2).getLatlon();
				int myLat = (int) myLatLon.getLatitude()*1000000;
				int myLon = (int) myLatLon.getLongitude()*1000000;
				intent.putExtra("lat", myLat);
				intent.putExtra("lon", myLon);
				startActivity(intent);
			}
		});

	}
}
