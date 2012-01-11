package edu.cmu.travelbuddy;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;

import edu.cmu.travelbuddy.facebook.Utility;
import edu.cmu.travelbuddy.news.MyCustomBaseAdapter;
import edu.cmu.travelbuddy.news.NewsItem;
import edu.cmu.travelbuddy.news.ParseRSSforNews;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class Social extends Activity {
	
	private LayoutInflater mInflater;
	private Vector<RowData> data;
	RowData rd;
	
	public static final String APP_ID = "177833235639774";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.socialtab);
		
		TextView myTv = (TextView)findViewById(R.id.titletextsocial);
		myTv.setText("Friends in");
		
		final ListView lv1 = (ListView) findViewById(R.id.SocialList);
		if(lv1 == null){
			Log.d("List", "List is null");
		}

		
		lv1.setAdapter(getFacebookList());

		lv1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {
				//Object o = lv1.getItemAtPosition(position);
				//NewsItem item = (NewsItem)o;
				//Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getLink()));
				//startActivity(browserIntent);
				// SearchResults fullObject = (SearchResults)o;
				// Toast.makeText(ListViewBlogPost.this, "You have chosen: " +
				// " " + fullObject.getName(), Toast.LENGTH_LONG).show();
			}
		});
	}

	private CustomAdapter getFacebookList() {
		Utility.mFacebook = new Facebook(APP_ID);
       	//Instantiate the asynrunner object for asynchronous api calls.
       	Utility.mAsyncRunner = new AsyncFacebookRunner(Utility.mFacebook);
		Bundle params = new Bundle();
		params.putString("message", getString(R.string.request_message));
		Bundle parameters = new Bundle();
		String mAccessToken = Utility.mFacebook.getAccessToken();
		ArrayList<String> friends  = new ArrayList<String>();
		ArrayList<String> desc  = new ArrayList<String>();

		try {

			parameters.putString("format", "json");
			parameters.putString("access_token", mAccessToken);

			String url = "https://graph.facebook.com/me/friends";
			String response = Util.openUrl(url, "GET", parameters);
			JSONObject obj = Util.parseJson(response);
			JSONArray array = obj.optJSONArray("data");
			String place = "Pittsburgh";
			JSONObject objectLocation = null;
			
			if (array != null) {
				for (int i = 0; i < 20; i++) {
					StringBuilder check = new StringBuilder("");
					StringBuilder details = new StringBuilder("");
					try {
						String name            = array.getJSONObject(i).getString("name");
						String id               = array.getJSONObject(i).getString("id");
						String jsonRequest      = "https://graph.facebook.com/"+ id;
						String responseLocation = Util.openUrl(jsonRequest,"GET", parameters);
						objectLocation = Util.parseJson(responseLocation);
						if (!objectLocation.isNull("location")) {
							JSONObject loc = objectLocation.getJSONObject("location");
							String address = loc.getString("name");
							check.append(address);
							details.append("This friend currently lives here;");
						}
						if (!objectLocation.isNull("hometown")) {
							JSONObject homeObject = objectLocation.getJSONObject("hometown");
							String home = homeObject.getString("name");
							details.append("This friend's hometown is "+home);
							check.append(home);
						}
						if (check.toString().contains(place)) {
							friends.add(name);
							desc.add(details.toString());
						}
						

					} catch (JSONException j) {

					}

				}
			}
		}

		catch (Exception e) {
			e.printStackTrace();
		} catch (FacebookError e) {
			e.printStackTrace();
		}
		String names[] = friends.toArray(new String[friends.size()]);
		String description[] = desc.toArray(new String[desc.size()]);
		String checker = "wer";
		//Intent myIntent = new Intent(TravelBuddy.this, CustomAdapterList.class);
    	//myIntent.putExtra("name", names);
    	//myIntent.putExtra("desc", description);
    	//startActivity(myIntent);
    	
    	mInflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		data = new Vector<RowData>();
		//String[] title = getIntent().getExtras().getStringArray("name");
		//String[] desc = getIntent().getExtras().getStringArray("desc");
		for (int i = 0; i < names.length; i++) {
			try {
				rd = new RowData(i, names[i], description[i]);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			data.add(rd);
		}
		CustomAdapter adapter = new CustomAdapter(this, R.layout.facebookfriends_list_item,
				R.id.title, data);
		return adapter;
		//setListAdapter(adapter);
		//getListView().setTextFilterEnabled(true);
		
	}
	private class RowData {
		protected int mId;
		protected String mTitle;
		protected String mDetail;

		RowData(int id, String title, String detail) {
			mId = id;
			mTitle = title;
			mDetail = detail;
		}

		@Override
		public String toString() {
			return mId + " " + mTitle + " " + mDetail;
		}
	}
	
	private class CustomAdapter extends ArrayAdapter<RowData> {
		public CustomAdapter(Context context, int resource, int textViewResourceId,
				List<RowData> objects) {
			super(context, resource, textViewResourceId, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			TextView title = null;
			TextView detail = null;
			ImageView i11 = null;
			RowData rowData = getItem(position);
			if (null == convertView) {
				convertView = mInflater.inflate(R.layout.facebookfriends_list_item, null);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			}
			holder = (ViewHolder) convertView.getTag();
			title = holder.gettitle();
			title.setText(rowData.mTitle);
			detail = holder.getdetail();
			detail.setText(rowData.mDetail);
			i11 = holder.getImage();
			i11.setImageResource(R.drawable.icon);
			return convertView;
		}

		private class ViewHolder {
			private View mRow;
			private TextView title = null;
			private TextView detail = null;
			private ImageView i11 = null;

			public ViewHolder(View row) {
				mRow = row;
			}

			public TextView gettitle() {
				if (null == title) {
					title = (TextView) mRow.findViewById(R.id.title);
				}
				return title;
			}

			public TextView getdetail() {
				if (null == detail) {
					detail = (TextView) mRow.findViewById(R.id.detail);
				}
				return detail;
			}

			public ImageView getImage() {
				if (null == i11) {
					i11 = (ImageView) mRow.findViewById(R.id.img);
				}
				return i11;
			}
		}
	}
	
}
