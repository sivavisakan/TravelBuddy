/*
 * Created by Lolay, Inc.
 * Modified by Christophe VONG
 * Copyright 2011 CityGrid Media, LLC All rights reserved.
 */

package edu.cmu.travelbuddy.citygrid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.citygrid.CGException;
import com.citygrid.CityGrid;
import com.citygrid.content.offers.CGOffersLocation;
import com.citygrid.content.offers.CGOffersOffer;
import com.citygrid.content.offers.search.CGOffersSearch;
import com.citygrid.content.offers.search.CGOffersSearchResults;
import com.citygrid.content.places.search.CGPlacesSearch;

import edu.cmu.travelbuddy.R;
import edu.cmu.travelbuddy.location.TLocationProvider;
import edu.cmu.travelbuddy.news.MyCustomBaseAdapter;
import edu.cmu.travelbuddy.news.NewsItem;

public class WhatToDoActivity extends Activity {

	
	String categories[];
	private Gallery gallery;

	private static int width;
	private static int height;

	private Integer[] Imgid = { R.drawable.theatre, R.drawable.restaurant, R.drawable.shopping, R.drawable.bar,
			R.drawable.restaurant, R.drawable.shopping };

	private String[] OptionsID = { "Movies", "Restaurants", "Hotels", "Bars and Clubs",
			"Spa and Beauty", "Shopping" };

	private HashMap<String, String> nameToCategory = new HashMap<String, String>() {
		{
			put("Movies", "movie");
			put("Restaurants", "restaurant");
			put("Hotels", "hotel");
			put("Bars and Clubs", "barclub");
			put("Spa and Beauty", "spabeauty");
			put("Shopping", "shopping");
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.whattodo);
		Toast.makeText(getApplicationContext(), "Loading..",
				Toast.LENGTH_SHORT).show();
		
		CityGrid.setPublisher("test");
		CityGrid.setSimulation(false);
		// CityGrid.setPublisher("test");
		// CityGrid.setSimulation(false);
		// Toast.makeText(getApplicationContext(), "Choose a category",
		// Toast.LENGTH_LONG).show();
		// categories = new String [3];
		// categories[0] = "Places"; categories[1] = "Offers"; categories[2] =
		// "Reviews";
		// setListAdapter(new ArrayAdapter<String>(this, R.layout.main,
		// categories));
		// ListView lv = getListView();
		// lv.setTextFilterEnabled(true);
		// lv.setOnItemClickListener(this);

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		height = metrics.heightPixels;
		width = metrics.widthPixels;

		Log.d("HEIGHT", "height is : " + height);
		Log.d("WIDTH", "width Myis : " + width);

		gallery = (Gallery) findViewById(R.id.placesgallery);
		gallery.setAdapter(new AddImgAdp(this));
		alignGalleryToLeft(300, gallery);

		gallery.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView parent, View v, int position, long id) {
				Intent newIntent = new Intent(WhatToDoActivity.this, PlaceList.class);
				newIntent.putExtra("category", nameToCategory.get(OptionsID[position]));
				startActivity(newIntent);
			}
		});

		CGOffersSearch search = CityGrid.offersSearch();
		TLocationProvider lp = TLocationProvider.getInstance();
		search.setWhere(lp.getAddress());

		ArrayList<CGOffersOffer> offersList = new ArrayList<CGOffersOffer>();

		for (String categories : nameToCategory.values()) {
			search.setWhat(categories);
			try {
				CGOffersSearchResults results = search.search();
				if (results != null) {
					CGOffersOffer[] offers = results.getOffers();
					
					offersList.addAll(new ArrayList<CGOffersOffer>(Arrays.asList(offers)));
					// CGOffersLocation[] location = offers[0].getLocations();
					// location[0].getImage();
				}
			} catch (CGException e) {
				Log.w("Exception in Offers", e);
			}
		}
		
		final ListView lv1 = (ListView) findViewById(R.id.offerslist);
		if (lv1 == null) {
			Log.d("List", "List is null");
		}
		lv1.setAdapter(new CityGridCustomBaseAdapter(this, offersList));
		lv1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
				// TODO Auto-generated method stub
				Object o = lv1.getItemAtPosition(position);
				CGOffersOffer item = (CGOffersOffer)o;
				OfferDetail.setOffer(item);
				startActivity(new Intent(WhatToDoActivity.this, OfferDetail.class));
			}
		});
	}

	public class AddImgAdp extends BaseAdapter {
		int GalItemBg;
		private Context cont;
		private LayoutInflater mInflater;

		public AddImgAdp(Context c) {
			cont = c;
			mInflater = LayoutInflater.from(cont);
			TypedArray typArray = obtainStyledAttributes(R.styleable.GalleryTheme);

			GalItemBg = typArray.getResourceId(
					R.styleable.GalleryTheme_android_galleryItemBackground, 0);
			typArray.recycle();
		}

		public int getCount() {
			return Imgid.length;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.customgalleryitem, null);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView.findViewById(R.id.galleryImage);
				holder.description = (TextView) convertView.findViewById(R.id.galleryText);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.image.setImageResource(Imgid[position]);
			holder.image.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
			holder.image.setScaleType(ImageView.ScaleType.FIT_XY);
			holder.description.setText(OptionsID[position]);
			//holder.setBackgroundResource(GalItemBg);
			/*
			 * View myView = mInflater.inflate(R.layout.customgalleryitem,
			 * null); //View myView = findViewById(R.layout.customgalleryitem);
			 * ImageView myImage = (ImageView)findViewById(R.id.galleryImage);
			 * myImage.setImageResource(Imgid[position]);
			 * myImage.setLayoutParams(new Gallery.LayoutParams(200, 200));
			 * myImage.setScaleType(ImageView.ScaleType.FIT_XY);
			 * 
			 * TextView myText = (TextView)findViewById(R.id.galleryText);
			 * myText.setText(""); /* ImageView imgView = new ImageView(cont);
			 * //imgView.setImageDrawable("");
			 * //imgView.setImageBitmap(BitmapFactory
			 * .decodeFile(myImageList.get(position)));
			 * imgView.setImageResource(
			 * nameToID.get(myImageList.get(position)));
			 * imgView.setLayoutParams(new Gallery.LayoutParams(200, 200));
			 * imgView.setScaleType(ImageView.ScaleType.FIT_XY);
			 * imgView.setBackgroundResource(GalItemBg);
			 */
			convertView.setBackgroundResource(GalItemBg);
			return convertView;
		}
	}

	static class ViewHolder {
		ImageView image;
		TextView description;
	}

	private void alignGalleryToLeft(int galleryWidth, Gallery gallery) {

		// We are taking the item widths and spacing from a dimension resource
		// because:
		// 1. No way to get spacing at runtime (no accessor in the Gallery
		// class)
		// 2. There might not yet be any item view created when we are calling
		// this
		// function
		int itemWidth = this.getResources().getDimensionPixelSize(R.dimen.gallery_item_width);
		int spacing = this.getResources().getDimensionPixelSize(R.dimen.gallery_spacing);

		// The offset is how much we will pull the gallery to the left in order
		// to simulate
		// left alignment of the first item
		int offset;
		if (galleryWidth <= itemWidth) {
			offset = width / 2 - spacing;
			// - itemWidth / 2 - spacing;
			Log.d("OFFSET", "offset is : " + offset);
		} else {
			offset = width / 2 - spacing;
			// - itemWidth/2 - 2 * spacing;
			Log.d("OFFSET", "offset is : " + offset);
		}
		// offset = 0;

		// Now update the layout parameters of the gallery in order to set the
		// left margin
		MarginLayoutParams mlp = (MarginLayoutParams) gallery.getLayoutParams();
		mlp.setMargins(-offset - 10, mlp.topMargin, mlp.rightMargin, mlp.bottomMargin);
	}

}
