

package edu.cmu.travelbuddy.facebook;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.LoginButton;
import com.facebook.android.Util;

import edu.cmu.travelbuddy.R;
import edu.cmu.travelbuddy.facebook.SessionEvents.AuthListener;
import edu.cmu.travelbuddy.facebook.SessionEvents.LogoutListener;

public class TravelBuddy extends ListActivity implements OnItemClickListener {

	/*
	 * Your Facebook Application ID must be set before running this example See
	 * http://www.facebook.com/developers/createapp.php
	 */
	public static final String APP_ID = "177833235639774";

	/**
	 * Custom List variables
	 */
	private LayoutInflater mInflater;
	private Vector<RowData> data;
	RowData rd;

	static final String[] title = new String[] { "Update Status",
			"Invite Your Friends", "Upload Photo", "Place Check-in",
			"Ask Your friends" };

	static final String[] detail = new String[] {
			"Tell your friends what you are doing!",
			"Invite your friends to use this app",
			"Upload your photos to your facebook profile",
			"Tell your friends where you are!",
			"Ask your friends or get introduced to new connection" };
	private Integer[] imgid = { R.drawable.status, R.drawable.ask,
			R.drawable.album, R.drawable.checkin, R.drawable.friends };

	private LoginButton mLoginButton;
	private TextView mText;
	private ImageView mUserPic;
	private Handler mHandler;
	ProgressDialog dialog;

	final int AUTHORIZE_ACTIVITY_RESULT_CODE = 0;
	final int PICK_EXISTING_PHOTO_RESULT_CODE = 1;

	private String graph_or_fql;

	private ListView list;
	String[] main_items = { "Update Status", "Invite Your Friends",
			"Upload Photo", "Place Check-in", "Ask Your friends" };
	String[] permissions = { "offline_access", "publish_stream", "user_photos",
			"publish_checkins", "photo_upload", "friends_location",
			"friends_hometown", "user_hometown", "user_location",
			"friends_checkins" };

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (APP_ID == null) {
			Util.showAlert(this, "Warning", "Facebook Applicaton ID must be "
					+ "specified before running this example: see FbAPIs.java");
			return;
		}

		setContentView(R.layout.main_list_image_text);
		mHandler = new Handler();

		mText = (TextView) TravelBuddy.this.findViewById(R.id.txt);
		mUserPic = (ImageView) TravelBuddy.this.findViewById(R.id.user_pic);

		// Create the Facebook Object using the app id.
		Utility.mFacebook = new Facebook(APP_ID);
		// Instantiate the asynrunner object for asynchronous api calls.
		Utility.mAsyncRunner = new AsyncFacebookRunner(Utility.mFacebook);

		mLoginButton = (LoginButton) findViewById(R.id.login);

		// restore session if one exists
		SessionStore.restore(Utility.mFacebook, this);
		SessionEvents.addAuthListener(new FbAPIsAuthListener());
		SessionEvents.addLogoutListener(new FbAPIsLogoutListener());

		/*
		 * Source Tag: login_tag
		 */
		mLoginButton.init(this, AUTHORIZE_ACTIVITY_RESULT_CODE,
				Utility.mFacebook, permissions);

		if (Utility.mFacebook.isSessionValid()) {
			requestUserData();
		}

		mInflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		data = new Vector<RowData>();
		for (int i = 0; i < title.length; i++) {
			try {
				rd = new RowData(i, title[i], detail[i]);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			data.add(rd);
		}
		CustomAdapter adapter = new CustomAdapter(this,
				R.layout.facebook_list_item, R.id.title, data);
		setListAdapter(adapter);
		getListView().setTextFilterEnabled(true);
		/*
		 * list = (ListView)findViewById(R.id.main_list);
		 * list.setOnItemClickListener(this); list.setAdapter(new
		 * ArrayAdapter<String>(this, R.layout.main_list_item, main_items));
		 */
	}

	@Override
	public void onResume() {
		super.onResume();
		if (Utility.mFacebook != null && !Utility.mFacebook.isSessionValid()) {
			mText.setText("You are logged out! ");
			mUserPic.setImageBitmap(null);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		/*
		 * if this is the activity result from authorization flow, do a call
		 * back to authorizeCallback Source Tag: login_tag
		 */
		case AUTHORIZE_ACTIVITY_RESULT_CODE: {
			Utility.mFacebook.authorizeCallback(requestCode, resultCode, data);
			break;
		}
		/*
		 * if this is the result for a photo picker from the gallery, upload the
		 * image after scaling it. You can use the Utility.scaleImage() function
		 * for scaling
		 */
		case PICK_EXISTING_PHOTO_RESULT_CODE: {
			if (resultCode == Activity.RESULT_OK) {
				Uri photoUri = data.getData();
				if (photoUri != null) {
					Bundle params = new Bundle();
					try {
						params.putByteArray("photo", Utility.scaleImage(
								getApplicationContext(), photoUri));
					} catch (IOException e) {
						e.printStackTrace();
					}
					params.putString("caption",
							"FbAPIs Sample App photo upload");
					Utility.mAsyncRunner.request("me/photos", params, "POST",
							new PhotoUploadListener(), null);
				} else {
					Toast.makeText(getApplicationContext(),
							"Error selecting image from the gallery.",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(getApplicationContext(),
						"No image selected for upload.", Toast.LENGTH_SHORT)
						.show();
			}
			break;
		}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
		switch (position) {
		/*
		 * Source Tag: update_status_tag Update user's status by invoking the
		 * feed dialog To post to a friend's wall, provide his uid in the 'to'
		 * parameter Refer to
		 * https://developers.facebook.com/docs/reference/dialogs/feed/ for more
		 * info.
		 */
		case 0: {
			Bundle params = new Bundle();
			params.putString("caption", getString(R.string.app_name));
			params.putString("description", getString(R.string.app_desc));
			params.putString("picture", Utility.HACK_ICON_URL);
			params.putString("name", getString(R.string.app_action));
			Utility.mFacebook.dialog(TravelBuddy.this, "feed", params,
					new UpdateStatusListener());
			String access_token = Utility.mFacebook.getAccessToken();
			System.out.println(access_token);
			break;
		}

		/*
		 * Source Tag: app_requests Send an app request to friends. If no friend
		 * is specified, the user will see a friend selector and will be able to
		 * select a maximum of 50 recipients. To send request to specific
		 * friend, provide the uid in the 'to' parameter Refer to
		 * https://developers.facebook.com/docs/reference/dialogs/requests/ for
		 * more info.
		 */
		case 1: {
			Bundle params = new Bundle();
			params.putString("message", getString(R.string.request_message));
			Utility.mFacebook.dialog(TravelBuddy.this, "apprequests", params,
					new AppRequestsListener());
			break;
		}

		case 4: {
			Bundle params = new Bundle();
			params.putString("message", getString(R.string.request_message));
			Utility.mFacebook.dialog(TravelBuddy.this, "apprequests", params,
					new AppRequestsListener());
			Bundle parameters = new Bundle();
			String mAccessToken = Utility.mFacebook.getAccessToken();

			try {

				parameters.putString("format", "json");
				parameters.putString("access_token", mAccessToken);

				String url = "https://graph.facebook.com/me/friends";
				String response = Util.openUrl(url, "GET", parameters);
				JSONObject obj = Util.parseJson(response);

				Log.i("json Response", obj.toString());
				JSONArray array = obj.optJSONArray("data");

				if (array != null) {
					for (int i = 0; i < 5; i++) {
						String hometown;
						String location;
						String name = array.getJSONObject(i).getString("name");
						String id = array.getJSONObject(i).getString("id");
						String jsonRequest = "https://graph.facebook.com/" + id;
						String responseLocation = Util.openUrl(jsonRequest,
								"GET", parameters);
						JSONObject objectLocation = Util
								.parseJson(responseLocation);
						JSONArray arrayLocation = obj.optJSONArray("location");

						if (arrayLocation != null)
							hometown = arrayLocation.getJSONObject(0)
									.getString("hometown");
						if (!array.getJSONObject(i).isNull("location"))
							location = array.getJSONObject(i).getString(
									"location");
						Log.i(name, id);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} catch (FacebookError e) {
				e.printStackTrace();
			}

			break;
		}

		/*
		 * Source Tag: upload_photo You can upload a photo from the media
		 * gallery or from a remote server How to upload photo:
		 * https://developers.facebook.com/blog/post/498/
		 */
		case 2: {
			if (!Utility.mFacebook.isSessionValid()) {
				Util.showAlert(this, "Warning", "You must first log in.");
			} else {
				dialog = ProgressDialog.show(TravelBuddy.this, "",
						getString(R.string.please_wait), true, true);
				new AlertDialog.Builder(this)
						.setTitle(R.string.gallery_remote_title)
						.setMessage(R.string.gallery_remote_msg)
						.setPositiveButton(R.string.gallery_button,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										Intent intent = new Intent(
												Intent.ACTION_PICK,
												(MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
										startActivityForResult(intent,
												PICK_EXISTING_PHOTO_RESULT_CODE);
									}

								})
						.setNegativeButton(R.string.remote_button,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										/*
										 * Source tag: upload_photo_tag
										 */
										Bundle params = new Bundle();
										params.putString("url",
												"http://www.facebook.com/images/devsite/iphone_connect_btn.jpg");
										params.putString("caption",
												"FbAPIs Sample App photo upload");
										Utility.mAsyncRunner
												.request(
														"me/photos",
														params,
														"POST",
														new PhotoUploadListener(),
														null);
									}

								})
						.setOnCancelListener(
								new DialogInterface.OnCancelListener() {
									@Override
									public void onCancel(DialogInterface d) {
										dialog.dismiss();
									}
								}).show();
			}
			break;
		}

		/*
		 * User can check-in to a place, you require publish_checkins permission
		 * for that. You can use the default Times Square location or fetch
		 * user's current location Get user's checkins -
		 * https://developers.facebook.com/docs/reference/api/checkin/
		 */
		case 3: {
			final Intent myIntent = new Intent(getApplicationContext(),
					Places.class);
			new AlertDialog.Builder(this)
			.setTitle(R.string.get_location)
			.setMessage(R.string.get_default_or_new_location)
			.setPositiveButton(R.string.current_location_button,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							myIntent.putExtra("LOCATION", "current");
							startActivity(myIntent);
						}
					})
			.setNegativeButton(R.string.times_square_button,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							myIntent.putExtra("LOCATION",
									"times_square");
							startActivity(myIntent);
						}

					}).show();
	
			break;
		}

		}
	}

	/*
	 * callback for the feed dialog which updates the profile status
	 */
	public class UpdateStatusListener extends BaseDialogListener {
		public void onComplete(Bundle values) {
			final String postId = values.getString("post_id");
			if (postId != null) {
				new UpdateStatusResultDialog(TravelBuddy.this,
						"Update Status executed", values).show();
			} else {
				Toast toast = Toast.makeText(getApplicationContext(),
						"No wall post made", Toast.LENGTH_SHORT);
				toast.show();
			}
		}

		public void onFacebookError(FacebookError error) {
			Toast.makeText(getApplicationContext(),
					"Facebook Error: " + error.getMessage(), Toast.LENGTH_SHORT)
					.show();
		}

		public void onCancel() {
			Toast toast = Toast.makeText(getApplicationContext(),
					"Update status cancelled", Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	/*
	 * callback for the apprequests dialog which sends an app request to user's
	 * friends.
	 */
	public class AppRequestsListener extends BaseDialogListener {
		public void onComplete(Bundle values) {
			Toast toast = Toast.makeText(getApplicationContext(),
					"App request sent", Toast.LENGTH_SHORT);
			toast.show();
		}

		public void onFacebookError(FacebookError error) {
			Toast.makeText(getApplicationContext(),
					"Facebook Error: " + error.getMessage(), Toast.LENGTH_SHORT)
					.show();
		}

		public void onCancel() {
			Toast toast = Toast.makeText(getApplicationContext(),
					"App request cancelled", Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	/*
	 * callback after friends are fetched via me/friends or fql query.
	 */
	public class FriendsRequestListener extends BaseRequestListener {

		public void onComplete(final String response, final Object state) {
			dialog.dismiss();
			Intent myIntent = new Intent(getApplicationContext(),
					FriendsList.class);
			myIntent.putExtra("API_RESPONSE", response);
			myIntent.putExtra("METHOD", graph_or_fql);
			startActivity(myIntent);
		}

		public void onFacebookError(FacebookError error) {
			dialog.dismiss();
			Toast.makeText(getApplicationContext(),
					"Facebook Error: " + error.getMessage(), Toast.LENGTH_SHORT)
					.show();
		}
	}

	/*
	 * callback for the photo upload
	 */
	public class PhotoUploadListener extends BaseRequestListener {

		public void onComplete(final String response, final Object state) {
			dialog.dismiss();
			mHandler.post(new Runnable() {
				public void run() {
					new UploadPhotoResultDialog(TravelBuddy.this,
							"Upload Photo executed", response).show();
				}
			});
		}

		public void onFacebookError(FacebookError error) {
			dialog.dismiss();
			Toast.makeText(getApplicationContext(),
					"Facebook Error: " + error.getMessage(), Toast.LENGTH_LONG)
					.show();
		}
	}

	public class FQLRequestListener extends BaseRequestListener {

		public void onComplete(final String response, final Object state) {
			mHandler.post(new Runnable() {
				public void run() {
					Toast.makeText(getApplicationContext(),
							"Response: " + response, Toast.LENGTH_LONG).show();
				}
			});
		}

		public void onFacebookError(FacebookError error) {
			Toast.makeText(getApplicationContext(),
					"Facebook Error: " + error.getMessage(), Toast.LENGTH_LONG)
					.show();
		}
	}

	/*
	 * Callback for fetching current user's name, picture, uid.
	 */
	public class UserRequestListener extends BaseRequestListener {

		public void onComplete(final String response, final Object state) {
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(response);

				final String picURL = jsonObject.getString("picture");
				final String name = jsonObject.getString("name");
				Utility.userUID = jsonObject.getString("id");

				mHandler.post(new Runnable() {
					public void run() {
						mText.setText("Welcome " + name + "!");
						mUserPic.setImageBitmap(Utility.getBitmap(picURL));
					}
				});

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/*
	 * The Callback for notifying the application when authorization succeeds or
	 * fails.
	 */

	public class FbAPIsAuthListener implements AuthListener {

		public void onAuthSucceed() {
			requestUserData();
		}

		public void onAuthFail(String error) {
			mText.setText("Login Failed: " + error);
		}
	}

	/*
	 * The Callback for notifying the application when log out starts and
	 * finishes.
	 */
	public class FbAPIsLogoutListener implements LogoutListener {
		public void onLogoutBegin() {
			mText.setText("Logging out...");
		}

		public void onLogoutFinish() {
			mText.setText("You have logged out! ");
			mUserPic.setImageBitmap(null);
		}
	}

	/*
	 * Request user name, and picture to show on the main screen.
	 */
	public void requestUserData() {
		mText.setText("Fetching user name, profile pic...");
		Bundle params = new Bundle();
		params.putString("fields", "name, picture");
		Utility.mAsyncRunner.request("me", params, new UserRequestListener());
	}

	/**
	 * Definition of the list adapter
	 */
	public class MainListAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public MainListAdapter() {
			mInflater = LayoutInflater.from(TravelBuddy.this.getBaseContext());
		}

		@Override
		public int getCount() {
			return main_items.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View hView = convertView;
			if (convertView == null) {
				hView = mInflater.inflate(R.layout.main_list_item, null);
				ViewHolder holder = new ViewHolder();
				holder.main_list_item = (TextView) hView
						.findViewById(R.id.main_api_item);
				hView.setTag(holder);
			}

			ViewHolder holder = (ViewHolder) hView.getTag();

			holder.main_list_item.setText(main_items[position]);

			return hView;
		}

	}

	class ViewHolder {
		TextView main_list_item;
	}

	/**
	 * Custom View list methods
	 */
	public void onListItemClick(ListView parent, View v, int position,
			long identity) {
		Toast.makeText(getApplicationContext(),
				"You have selected " + (position + 1) + "th item",
				Toast.LENGTH_SHORT).show();

		switch (position) {
		/*
		 * Source Tag: update_status_tag Update user's status by invoking the
		 * feed dialog To post to a friend's wall, provide his uid in the 'to'
		 * parameter Refer to
		 * https://developers.facebook.com/docs/reference/dialogs/feed/ for more
		 * info.
		 */
		case 0: {
			Bundle params = new Bundle();
			params.putString("caption", getString(R.string.app_name));
			params.putString("description", getString(R.string.app_desc));
			params.putString("picture", Utility.HACK_ICON_URL);
			params.putString("name", getString(R.string.app_action));
			Utility.mFacebook.dialog(TravelBuddy.this, "feed", params,
					new UpdateStatusListener());
			String access_token = Utility.mFacebook.getAccessToken();
			System.out.println(access_token);
			break;
		}

		/*
		 * Source Tag: app_requests Send an app request to friends. If no friend
		 * is specified, the user will see a friend selector and will be able to
		 * select a maximum of 50 recipients. To send request to specific
		 * friend, provide the uid in the 'to' parameter Refer to
		 * https://developers.facebook.com/docs/reference/dialogs/requests/ for
		 * more info.
		 */
		case 1: {
			Bundle params = new Bundle();
			params.putString("message", getString(R.string.request_message));
			Utility.mFacebook.dialog(TravelBuddy.this, "apprequests", params,
					new AppRequestsListener());
			break;
		}

		case 4: {
			Toast.makeText(getApplicationContext(),	"Friends data fetched",Toast.LENGTH_SHORT).show();
			Bundle params = new Bundle();
			params.putString("message", getString(R.string.request_message));
			Bundle parameters = new Bundle();
			String mAccessToken = Utility.mFacebook.getAccessToken();
			ArrayList<String> friends  = new ArrayList<String>();
			ArrayList<String> desc  = new ArrayList<String>();
			ArrayList<String> friendId = new ArrayList<String>();
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
								friendId.add(id);
						
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
			String friendIDs[]  = friendId.toArray(new String[friendId.size()]);
			String checker = "wer";
			Intent myIntent = new Intent(TravelBuddy.this, CustomAdapterList.class);
        	myIntent.putExtra("friendId", friendIDs);
			myIntent.putExtra("name", names);
        	myIntent.putExtra("desc", description);
        	startActivity(myIntent);

			break;
		}

		/*
		 * Source Tag: upload_photo You can upload a photo from the media
		 * gallery or from a remote server How to upload photo:
		 * https://developers.facebook.com/blog/post/498/
		 */
		case 2: {
			if (!Utility.mFacebook.isSessionValid()) {
				Util.showAlert(this, "Warning", "You must first log in.");
			} else {
				dialog = ProgressDialog.show(TravelBuddy.this, "",
						getString(R.string.please_wait), true, true);
				new AlertDialog.Builder(this)
						.setTitle(R.string.gallery_remote_title)
						.setMessage(R.string.gallery_remote_msg)
						.setPositiveButton(R.string.gallery_button,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										Intent intent = new Intent(
												Intent.ACTION_PICK,
												(MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
										startActivityForResult(intent,
												PICK_EXISTING_PHOTO_RESULT_CODE);
									}

								})
						.setNegativeButton(R.string.remote_button,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										/*
										 * Source tag: upload_photo_tag
										 */
										Bundle params = new Bundle();
										params.putString("url",
												"http://www.facebook.com/images/devsite/iphone_connect_btn.jpg");
										params.putString("caption",
												"FbAPIs Sample App photo upload");
										Utility.mAsyncRunner
												.request(
														"me/photos",
														params,
														"POST",
														new PhotoUploadListener(),
														null);
									}

								})
						.setOnCancelListener(
								new DialogInterface.OnCancelListener() {
									@Override
									public void onCancel(DialogInterface d) {
										dialog.dismiss();
									}
								}).show();
			}
			break;
		}

		/*
		 * User can check-in to a place, you require publish_checkins permission
		 * for that. You can use the default Times Square location or fetch
		 * user's current location Get user's checkins -
		 * https://developers.facebook.com/docs/reference/api/checkin/
		 */
		case 3: {
			final Intent myIntent = new Intent(getApplicationContext(),
					Places.class);

			new AlertDialog.Builder(this)
					.setTitle(R.string.get_location)
					.setMessage(R.string.get_default_or_new_location)
					.setPositiveButton(R.string.current_location_button,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									myIntent.putExtra("LOCATION", "current");
									startActivity(myIntent);
								}
							})
					.setNegativeButton(R.string.times_square_button,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									myIntent.putExtra("LOCATION",
											"times_square");
									startActivity(myIntent);
								}

							}).show();
			break;
		}

		}
	}

	public class RowData {
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

	public class CustomAdapter extends ArrayAdapter<RowData> {
		public CustomAdapter(Context context, int resource,
				int textViewResourceId, List<RowData> objects) {
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
				convertView = mInflater.inflate(R.layout.facebook_list_item,
						null);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			}
			holder = (ViewHolder) convertView.getTag();
			title = holder.gettitle();
			title.setText(rowData.mTitle);
			detail = holder.getdetail();
			detail.setText(rowData.mDetail);
			i11 = holder.getImage();
			i11.setImageResource(imgid[rowData.mId]);
			return convertView;
		}

		public class ViewHolder {
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
