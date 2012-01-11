package edu.cmu.travelbuddy.citygrid;

import java.util.ArrayList;

import com.citygrid.content.offers.CGOffersOffer;

import edu.cmu.travelbuddy.R;

import android.R.string;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CityGridCustomBaseAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	
	private static ArrayList<CGOffersOffer> myOfferList;

	public CityGridCustomBaseAdapter(Context context, ArrayList<CGOffersOffer> OfferList) {
		mInflater = LayoutInflater.from(context);
		myOfferList = OfferList;
	}

	public int getCount() {
		return myOfferList.size();
	}

	public Object getItem(int position) {
		return myOfferList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.offerlistitem, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.expirydate = (TextView) convertView
					.findViewById(R.id.expirydate);
			holder.discount = (TextView)convertView.findViewById(R.id.discount);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.title.setText(myOfferList.get(position).getTitle());
		//holder.expirydate.setText(myOfferList.get(position)
		//		.getExpirationDate().toGMTString());
		String discount = myOfferList.get(position).getDiscountValue() + "% dicount!";
		holder.discount.setText(discount);

		return convertView;
	}

	static class ViewHolder {
		TextView title;
		TextView expirydate;
		TextView discount;
	}
}