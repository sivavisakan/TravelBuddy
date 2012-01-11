package edu.cmu.travelbuddy.citygrid;

import java.util.ArrayList;

import com.citygrid.content.offers.CGOffersOffer;
import com.citygrid.content.places.search.CGPlacesSearchLocation;

import edu.cmu.travelbuddy.R;

import android.R.string;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CityGridPlacesCustomBaseAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	
	private static ArrayList<CGPlacesSearchLocation> myPlaceList;

	public CityGridPlacesCustomBaseAdapter(Context context, ArrayList<CGPlacesSearchLocation> OfferList) {
		mInflater = LayoutInflater.from(context);
		myPlaceList = OfferList;
	}

	public int getCount() {
		return myPlaceList.size();
	}

	public Object getItem(int position) {
		return myPlaceList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.place_item, null);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.place_name);
			holder.neighbourhood = (TextView) convertView
					.findViewById(R.id.place_location);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.name.setText(myPlaceList.get(position).getName());
		//holder.expirydate.setText(myOfferList.get(position)
		//		.getExpirationDate().toGMTString());
		//String discount = myPlaceList.get(position).getDiscountValue() + "% dicount!";
		holder.neighbourhood.setText(myPlaceList.get(position).getNeighborhood());

		return convertView;
	}

	static class ViewHolder {
		TextView name;
		TextView neighbourhood;
	}
}