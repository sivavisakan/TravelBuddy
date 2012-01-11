/*
 * Created by Lolay, Inc.
 * Modified by Christophe VONG
 * Copyright 2011 CityGrid Media, LLC All rights reserved.
 */

package edu.cmu.travelbuddy.citygrid;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.citygrid.CGAddress;
import com.citygrid.CGException;
import com.citygrid.CGLatLon;
import com.citygrid.CGSize;
import com.citygrid.CityGrid;
import com.citygrid.ads.mobile.CGAdsMobile;
import com.citygrid.ads.mobile.CGAdsMobileAd;
import com.citygrid.ads.mobile.CGAdsMobileCollection;
import com.citygrid.content.offers.CGOffersLocation;
import com.citygrid.content.offers.CGOffersOffer;
import com.citygrid.content.offers.detail.CGOffersDetail;
import com.citygrid.content.offers.detail.CGOffersDetailResults;
import com.citygrid.content.places.detail.CGPlacesDetail;
import com.citygrid.content.places.detail.CGPlacesDetailLocation;
import com.citygrid.content.places.detail.CGPlacesDetailResults;

import edu.cmu.travelbuddy.R;
import edu.cmu.travelbuddy.facebook.ShowMap;

public class Details extends Activity {

	CGAdsMobileAd ad;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details);

		String category       = getIntent().getExtras().getString("category");
		final int lat      = getIntent().getExtras().getInt("lat");
    	final int lon      = getIntent().getExtras().getInt("lon");
		if (category.equalsIgnoreCase("Places")) {
			this.displayPlace(); //this.displayReview();
		} else if (category.equalsIgnoreCase("Offers")) {
			this.displayOffer();
		} else if (category.equalsIgnoreCase("Reviews")) {
			this.displayReview();
		}
		final Button button = (Button) findViewById(R.id.button_map);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent myIntent = new Intent(Details.this, ShowMap.class);
            	myIntent.putExtra("lat", lat);
            	myIntent.putExtra("lon", lon);
            	startActivity(myIntent);
            	
            }
        });
	}

	private void displayPlace() {
		String publicId = getIntent().getExtras().getString("publicId");
		Log.d("Details", "Got publicId: " + publicId);

		CGPlacesDetail detail = CityGrid.placesDetail();
		detail.setPublicId(publicId);

		CGPlacesDetailResults results = null;
		String placeDetail = "You are seeing some placeholder text, something went wrong.  Rant goes here......";
		try {
			results = detail.detail();
		} catch (CGException e) {
			e.printStackTrace();
			StringBuilder sb = new StringBuilder(
					"Exception retrieving place details for publicId : ")
					.append(publicId).append("Exception message: \n")
					.append(e.getMessage());
			placeDetail = sb.toString();
		}

		if (results != null) {
			CGPlacesDetailLocation place = results.getLocation();
			Log.d("Details", "Got CGPlacesdetailLocation: " + place.toString());
			StringBuilder sb = new StringBuilder();

			CGAddress address = place.getAddress();
			sb.append("Name: ").append(place.getName()).append("\n");

			sb.append("Public Id: ").append(place.getPublicId()).append("\n\n");

			sb.append("Address: ").append(address.getStreet()).append("; ")
					.append(address.getCity()).append(", ")
					.append(address.getState()).append(", ")
					.append(address.getZip()).append("\n");
			if (place.getPhone() != null) {
				sb.append("Phone: ").append(place.getPhone()).append("\n");
			}
			placeDetail = sb.toString();

			Log.d("Details", "Got place detail: " + placeDetail);
			TextView textDetail = (TextView) findViewById(R.id.textDetail);
			textDetail.setText(placeDetail);
			textDetail.setMovementMethod(new ScrollingMovementMethod());

			URI uriImage = place.getImage();
			if (uriImage != null) {
				displayImage(uriImage);
			}

			displayAdsMobile(place.getCategories()[0].getName(), place
					.getAddress().getZip());
		} else {
			TextView textDetail = (TextView) findViewById(R.id.textDetail);
			textDetail.setText("No results");
			textDetail.setMovementMethod(new ScrollingMovementMethod());
			ImageButton adButton = (ImageButton) findViewById(R.id.banner);
			adButton.setClickable(false);
			adButton.setVisibility(4);
		}
	}

	private void displayOffer() {

		String offerId = getIntent().getExtras().getString("offerId");
		Log.d("Details", "Got offerId: " + offerId);

		CGOffersDetail detail = CityGrid.offersDetail();
		detail.setOfferId(offerId);

		CGOffersDetailResults results = null;
		String offerDetail = "You are seeing some placeholder text, something went wrong.  Rant goes here......";
		try {
			results = detail.detail();
		} catch (CGException e) {
			e.printStackTrace();
			StringBuilder sb = new StringBuilder(
					"Exception retrieving offer details for offer ")
					.append(offerId).append("Exception message: \n")
					.append(e.getMessage());
			offerDetail = sb.toString();
		}

		if (results != null) {
			CGOffersOffer offer = results.getOffer();
			Log.d("Details", "Got CGOffersOffer: " + offer.toString());
			StringBuilder sb = new StringBuilder();
			CGOffersLocation location = offer.getLocations()[0];
			CGAddress address = location.getAddress();
			sb.append("Name: ").append(location.getName()).append("\n");

			sb.append("Offer: ").append(offer.getTitle()).append("\n\n");

			sb.append("Description: ").append(offer.getOfferDescription())
					.append("\n\n");

			if (offer.getStartDate() != null) {
				sb.append("Start Date: ").append(offer.getStartDate())
						.append("\n");
			}
			if (offer.getExpirationDate() != null) {
				sb.append("Expires: ").append(offer.getExpirationDate())
						.append("\n\n");
			}

			sb.append("Terms: ").append(offer.getTerms()).append("\n\n");

			sb.append("Address: ").append(address.getStreet()).append("; ")
					.append(address.getCity()).append(", ")
					.append(address.getState()).append(", ")
					.append(address.getZip()).append("\n");
			if (location.getPhone() != null) {
				sb.append("Phone: ").append(location.getPhone()).append("\n");
			}
			offerDetail = sb.toString();

			Log.d("Details", "Got offer detail: " + offerDetail);
			TextView textDetail = (TextView) findViewById(R.id.textDetail);
			textDetail.setText(offerDetail);
			textDetail.setMovementMethod(new ScrollingMovementMethod());

			URI uriImage = location.getImage();
			if (uriImage != null) {
				displayImage(uriImage);
			}

			try {
				displayAdsMobile(location.placesDetailLocation()
						.getCategories()[0].getName(), location.getAddress()
						.getZip());
			} catch (CGException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			TextView textDetail = (TextView) findViewById(R.id.textDetail);
			textDetail.setText("No results");
			textDetail.setMovementMethod(new ScrollingMovementMethod());
			ImageButton adButton = (ImageButton) findViewById(R.id.banner);
			adButton.setClickable(false);
			adButton.setVisibility(4);
		}
	}

	private void displayReview() {
		String reviewId = getIntent().getExtras().getString("reviewId");
		Log.d("Details", "Got reviewId: " + reviewId);

		StringBuilder sb = new StringBuilder();
		sb.append(getIntent().getExtras().getString("reviewDetail"));

		TextView textDetail = (TextView) findViewById(R.id.textDetail);
		textDetail.setText(sb.toString());
		textDetail.setMovementMethod(new ScrollingMovementMethod());

		ImageButton adButton = (ImageButton) findViewById(R.id.banner);
		adButton.setClickable(false);
		adButton.setVisibility(4);

	}

	private void displayAdsMobile(String what, String where) {
		CGAdsMobile search = CityGrid.adsMobile();
		search.setWhat(what);
		search.setWhere(where);
		search.setCollection(CGAdsMobileCollection.Collection320x50cat);
		search.setSize(new CGSize(320.0f, 50.0f));
		// Unique identifier for device
		TelephonyManager tm = (TelephonyManager) getBaseContext()
				.getSystemService(Context.TELEPHONY_SERVICE);
		search.setMuid(tm.getDeviceId());

		this.ad = null;
		try {
			this.ad = search.banner().getAd();
		} catch (CGException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (this.ad != null) {

			StringBuilder sb = new StringBuilder();

			sb.append(ad.toString());

			Log.d("Ads", "Got ad : " + sb.toString());

			URI uriImage = this.ad.getImage();
			Bitmap bitmap = null;
			URL urlImage;
			if (uriImage != null) {
				try {
					urlImage = new URL(uriImage.toURL().toString());
					HttpURLConnection connection = (HttpURLConnection) urlImage
							.openConnection();
					InputStream inputStream = (InputStream) connection
							.getInputStream();
					bitmap = BitmapFactory.decodeStream(inputStream);
					ImageButton adButton = (ImageButton) findViewById(R.id.banner);
					adButton.setImageBitmap(bitmap);

					Log.d("AdBanner", "Got ad banner @ " + urlImage);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private void displayImage(URI uriImage) {
		// Source :
		// http://www.ace-art.fr/wordpress/2010/07/25/tutoriel-android-partie-8-chargement-des-images-et-donnees/
		try {
			Bitmap bitmap = null;
			URL urlImage = new URL(uriImage.toURL().toString());
			HttpURLConnection connection = (HttpURLConnection) urlImage
					.openConnection();
			InputStream inputStream = (InputStream) connection.getInputStream();
			bitmap = BitmapFactory.decodeStream(inputStream);
			ImageView imageDetail = (ImageView) findViewById(R.id.imageDetail);
			imageDetail.setImageBitmap(bitmap);
			Log.d("imageDetails", "Got image detail @ " + urlImage);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Event handler for clicking the ad button.
	 * 
	 * @param view
	 */
	public void goAds(View view) {
		URI uriAds = this.ad.getDestinationUrl();
		if (uriAds != null) {
			Intent browserIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse(uriAds.toString()));
			startActivity(browserIntent);
		}
	}
}
