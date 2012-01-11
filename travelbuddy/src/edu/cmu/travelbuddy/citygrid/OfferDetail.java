package edu.cmu.travelbuddy.citygrid;

import java.net.URI;
import java.util.Date;

import javolution.text.Text;

import com.citygrid.content.offers.CGOffersOffer;

import edu.cmu.travelbuddy.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class OfferDetail extends Activity {

	// CGOff cgPlacesSearchLocation = null;
	// CGReviewsSearchReview[] cgReviews = null;

	public static CGOffersOffer myOffer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.offerdetail);
		TextView description = (TextView) findViewById(R.id.description);
		description.setText(myOffer.getOfferDescription());
		Date mydate = myOffer.getExpirationDate();
		TextView date = (TextView) findViewById(R.id.expirydate);
		if (null != mydate) {
			date.setText(mydate.toGMTString());
		} else {
			date.setText("-");
		}
		Button url = (Button) findViewById(R.id.offerurl);
		url.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				URI myUrl = myOffer.getRedemptionUrl();
				// String UriString = myUrl.to
				if (myUrl == null) {
					Toast.makeText(getApplicationContext(),
							"No webpage for this offer",
							Toast.LENGTH_SHORT).show();
				} else {
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(myOffer
							.getRedemptionUrl().getPath()));
					startActivity(browserIntent);
				}
			}
		});

		TextView title = (TextView) findViewById(R.id.title);
		title.setText(myOffer.getTitle());
	}

	public static void setOffer(CGOffersOffer offer) {
		myOffer = offer;
	}

}
