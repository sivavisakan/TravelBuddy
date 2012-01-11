/*
 * Created by Christophe VONG
 * Copyright 2011 CityGrid Media, LLC All rights reserved.
 */

package edu.cmu.travelbuddy.citygrid;

import java.text.SimpleDateFormat;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.citygrid.CGException;
import com.citygrid.CGLatLon;
import com.citygrid.CityGrid;
import com.citygrid.content.offers.CGOffersOffer;
import com.citygrid.content.offers.search.CGOffersSearch;
import com.citygrid.content.offers.search.CGOffersSearchResults;
import com.citygrid.content.places.search.CGPlacesSearch;
import com.citygrid.content.places.search.CGPlacesSearchLocation;
import com.citygrid.content.places.search.CGPlacesSearchResults;
import com.citygrid.content.reviews.CGReviewsSearch;
import com.citygrid.content.reviews.CGReviewsSearchResults;
import com.citygrid.content.reviews.CGReviewsSearchReview;

import edu.cmu.travelbuddy.R;

public class List extends ListActivity implements
		AdapterView.OnItemClickListener {
	CGOffersOffer[] cgOffers = null;
	CGPlacesSearchLocation[] cgPlacesSearchLocation = null;
	CGReviewsSearchReview[] cgReviews = null;
	String zipCode;
	String what;
	String category;
	Boolean sorted;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		zipCode = getIntent().getExtras().getString("zipCode");
		what = getIntent().getExtras().getString("what");
		category = getIntent().getExtras().getString("category");
		sorted = getIntent().getExtras().getBoolean("sort");

		if (category.equalsIgnoreCase("Places")) {
			String result[] = findPlaces();
			Toast.makeText(getApplicationContext(),
					"Number of places found: " + result.length,
					Toast.LENGTH_LONG).show();
			setListAdapter(new ArrayAdapter<String>(this, R.layout.city_grid_list, result));
		} else if (category.equalsIgnoreCase("Offers")) {
			String result[] = findOffers();
			Toast.makeText(getApplicationContext(),
					"Number of offers found: " + result.length,
					Toast.LENGTH_LONG).show();
			setListAdapter(new ArrayAdapter<String>(this, R.layout.city_grid_list, result));
		} else if (category.equalsIgnoreCase("Reviews")) {
			String result[] = findReviews();
			Toast.makeText(getApplicationContext(),
					"Number of reviews found: " + result.length,
					Toast.LENGTH_LONG).show();
			setListAdapter(new ArrayAdapter<String>(this, R.layout.city_grid_list, result));
		}

		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		lv.setOnItemClickListener(this);

	}

	private String[] findPlaces() {
		String[] places = null;

		CGPlacesSearch search = CityGrid.placesSearch();
		search.setWhat(this.what);
		search.setWhere(this.zipCode);

		CGPlacesSearchResults results = null;
		try {
			results = search.search();

			if (results != null) {
				cgPlacesSearchLocation = results.getLocations();
				if (cgPlacesSearchLocation != null) {
					places = new String[cgPlacesSearchLocation.length];
					int i = 0;
					for (CGPlacesSearchLocation location : cgPlacesSearchLocation) {
						StringBuilder sb = new StringBuilder();
						sb.append(location.getName()).append("\n");
						sb.append(location.getNeighborhood());
						places[i++] = sb.toString();
					}
				}
			}
		} catch (CGException e) {
			Log.e("List Activity", "Exception finding " + this.what
					+ " offers in " + this.zipCode);
			places = new String[] { "Exception finding " + this.what
					+ " offers: " + e.getMessage() };
		}
		return places;
	}

	private String[] findOffers() {
		String[] offers = null;

		CGOffersSearch search = CityGrid.offersSearch();
		search.setWhat(this.what);
		search.setWhere(this.zipCode);

		CGOffersSearchResults results = null;
		try {
			results = search.search();

			if (results != null) {
				cgOffers = results.getOffers();
				if (cgOffers != null) {
					offers = new String[cgOffers.length];
					int i = 0;
					for (CGOffersOffer offer : cgOffers) {
						StringBuilder sb = new StringBuilder();
						sb.append(offer.getTitle()).append("\n");
						if (offer.getExpirationDate() != null) {
							sb.append("Ends: ").append(
									offer.getExpirationDate());
						}
						sb.append("Location: ")
								.append(offer.getLocations()[0].getName())
								.append("\n");
						offers[i++] = sb.toString();
					}
				}
			}
		} catch (CGException e) {
			Log.e("List Activity", "Exception findinOffersg " + this.what
					+ " offers in " + this.zipCode);
			offers = new String[] { "Exception finding " + this.what
					+ " offers: " + e.getMessage() };
		}
		return offers;
	}

	private String[] findReviews() {
		String[] reviews = null;

		CGReviewsSearch search = CityGrid.reviewsSearch();
		search.setWhat(this.what);
		search.setWhere(this.zipCode);
		if (sorted) {
			search.setSort("createdate");
		}

		CGReviewsSearchResults results = null;
		try {
			results = search.search();

			if (results != null) {
				cgReviews = results.getReviews();
				if (cgReviews != null) {
					reviews = new String[cgReviews.length];
					int i = 0;
					for (CGReviewsSearchReview review : cgReviews) {
						StringBuilder sb = new StringBuilder();
						sb.append(review.getTitle()).append("\n");
						SimpleDateFormat format = new SimpleDateFormat(
								"dd/MM/yyyy");
						sb.append(format.format(review.getDate()));
						reviews[i++] = sb.toString();
					}
				}
			}
		} catch (CGException e) {
			Log.e("List Activity", "Exception finding " + this.what
					+ " reviews in " + this.zipCode);
			reviews = new String[] { "Exception finding " + this.what
					+ " reviews: " + e.getMessage() };
		}
		return reviews;
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view,
			int position, long id) {

		Intent intent = new Intent(this, Details.class);

		if (category.equalsIgnoreCase("Places")) {
			Toast.makeText(getApplicationContext(),
					"Loading..",
					Toast.LENGTH_SHORT).show();
			String publicId = cgPlacesSearchLocation[position].getPublicId();
			intent.putExtra("category", this.category);
			intent.putExtra("publicId", publicId);
			CGLatLon latLon = cgPlacesSearchLocation[position].getLatlon();
			int lat =(int)(latLon.getLatitude()*1000000);
			int lon =(int)(latLon.getLongitude()*1000000);
			intent.putExtra("lat",lat);
			intent.putExtra("lon",lon);
			startActivity(intent);
		} else if (category.equalsIgnoreCase("Offers")) {
			Toast.makeText(getApplicationContext(),
					"Getting details of your offer, sit tight...",
					Toast.LENGTH_SHORT).show();
			String offerId = cgOffers[position].getOfferId();
			intent.putExtra("category", this.category);
			intent.putExtra("offerId", offerId);
			startActivity(intent);
		} else if (category.equalsIgnoreCase("Reviews")) {
			Toast.makeText(getApplicationContext(),
					"Getting details of your review, sit tight...",
					Toast.LENGTH_SHORT).show();
			CGReviewsSearchReview review = cgReviews[position];
			StringBuilder reviewDetail = new StringBuilder();
			reviewDetail.append("Title : ").append(review.getTitle())
					.append("\n");
			reviewDetail.append("Author : ").append(review.getAuthor())
					.append("\n");
			reviewDetail.append("Rating : ").append(review.getRating())
					.append("\n\n");
			reviewDetail.append(review.getText()).append("\n");
			intent.putExtra("category", this.category);
			intent.putExtra("reviewId", review.getReviewId());
			intent.putExtra("reviewDetail", reviewDetail.toString());
			startActivity(intent);
		}
	}
}
