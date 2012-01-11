package edu.cmu.travelbuddy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import com.google.android.maps.MyLocationOverlay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import edu.cmu.travelbuddy.citygrid.List;
import edu.cmu.travelbuddy.location.TLocationProvider;
import edu.cmu.travelbuddy.news.MyCustomBaseAdapter;
import edu.cmu.travelbuddy.news.NewsItem;
import edu.cmu.travelbuddy.news.ParseRSSforNews;
import edu.cmu.travelbuddy.weather.WeatherDetailActivity;
import edu.cmu.travelbuddy.weather.WeatherInfo;
import edu.cmu.travelbuddy.weather.WeatherProvider;

public class WeatherAndNews extends Activity {

	private Gallery gallery;
	private ImageView imgView;

	private Gallery gallery2;
	private ImageView imgView2;

	private static int width;
	private static int height;

	private ArrayList<Integer> myImageList = new ArrayList<Integer>();

	java.util.List<WeatherInfo> myWeatherList;

	private HashMap<String, Integer> nameToID = new HashMap<String, Integer>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab1);

		// imgView = (ImageView) findViewById(R.id.ImageView01);
		// imgView.setImageResource(Imgid[0]);

		// Check this TODO
		nameToID.put("rain.bmp", R.drawable.rain);
		nameToID.put("sunny.bmp", R.drawable.sunny);
		nameToID.put("chance_of_storm.bmp", R.drawable.chance_of_storm);
		nameToID.put("chance_of_rain.bmp", R.drawable.chance_of_rain);
		nameToID.put("mostly_sunny.bmp", R.drawable.mostly_sunny);

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		height = metrics.heightPixels;
		width = metrics.widthPixels;

		Log.d("HEIGHT", "height is : " + height);
		Log.d("WIDTH", "width is : " + width);

		WeatherProvider myWeather = new WeatherProvider();
		TLocationProvider locationProvider = TLocationProvider.getInstance();
		String address = locationProvider.getAddress();
		myWeather.checkLatestWeatherConditions(address);
		myWeatherList = myWeather.getForecastList();

		for (WeatherInfo wInfo : myWeatherList) {
			// String filename = wInfo.getImage();
			// String uri = "drawable/" + filename;
			// Log.d("URI","URI : "+uri);
			// Log.d("Image for gallery",
			// "image name : "+getResources().getIdentifier(uri, "drawable",
			// this.getPackageName()));
			// Log.d("Package", "package : "+getPackageName());
			// myImageList.add(getResources().getIdentifier(uri, "drawable",
			// "edu.cmu."));
			myImageList.add(wInfo.getDayIcon());
		}

		gallery = (Gallery) findViewById(R.id.examplegallery);
		gallery.setAdapter(new AddImgAdp(this));
		alignGalleryToLeft(300, gallery);
		// imgView2 = (ImageView) findViewById(R.id.ImageView02);
		// imgView2.setImageResource(Imgid[0]);

		gallery.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView parent, View v, int position, long id) {
				Toast.makeText(getApplicationContext(), "Loading..", Toast.LENGTH_SHORT).show();
				startActivity(new Intent(WeatherAndNews.this, WeatherDetailActivity.class));
			}
		});

		/*
		 * gallery2 = (Gallery) findViewById(R.id.examplegallery1);
		 * gallery2.setAdapter(new AddImgAdp(this)); alignGalleryToLeft(300,
		 * gallery2);
		 * 
		 * gallery.setOnItemClickListener(new OnItemClickListener() { public
		 * void onItemClick(AdapterView parent, View v, int position, long id) {
		 * //imgView.setImageResource(Imgid[position]); } });
		 */

		final ListView lv1 = (ListView) findViewById(R.id.NewsList);
		if (lv1 == null) {
			Log.d("List", "List is null");
		}
		ParseRSSforNews newsHandle = new ParseRSSforNews(address);
		ArrayList<NewsItem> myList = new ArrayList<NewsItem>();
		myList = newsHandle.getListOfNewsItems();
		lv1.setAdapter(new MyCustomBaseAdapter(this, myList));

		lv1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position, long id) {
				Object o = lv1.getItemAtPosition(position);
				NewsItem item = (NewsItem) o;
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getLink()));
				startActivity(browserIntent);
				// SearchResults fullObject = (SearchResults)o;
				// Toast.makeText(ListViewBlogPost.this, "You have chosen: " +
				// " " + fullObject.getName(), Toast.LENGTH_LONG).show();
			}
		});

	}

	// public class AddImgAdp extends BaseAdapter {
	// int GalItemBg;
	// private Context cont;
	//
	// public AddImgAdp(Context c) {
	// cont = c;
	// TypedArray typArray = obtainStyledAttributes(R.styleable.GalleryTheme);
	// GalItemBg = typArray.getResourceId(
	// R.styleable.GalleryTheme_android_galleryItemBackground, 0);
	// typArray.recycle();
	// }
	//
	// public int getCount() {
	// return myImageList.size();
	// }
	//
	// public Object getItem(int position) {
	// return position;
	// }
	//
	// public long getItemId(int position) {
	// return position;
	// }
	//
	// public View getView(int position, View convertView, ViewGroup parent) {
	//
	//
	//
	// ImageView imgView = new ImageView(cont);
	// //imgView.setImageDrawable("");
	// //imgView.setImageBitmap(BitmapFactory.decodeFile(myImageList.get(position)));
	// //imgView.setImageResource(nameToID.get(myImageList.get(position)));
	// imgView.setImageResource(myImageList.get(position));
	// imgView.setLayoutParams(new Gallery.LayoutParams(200, 200));
	// imgView.setScaleType(ImageView.ScaleType.FIT_XY);
	// imgView.setBackgroundResource(GalItemBg);
	//
	// return imgView;
	// }
	// }

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
			return myImageList.size();
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

			String titolo = "Test";

			if (position == 0) {
				titolo = "Mon";
			} else if (position == 1) {
				titolo = "Tue";
			} else if (position == 2) {
				titolo = "Wed";
			} else if (position == 3) {
				titolo = "Thu";
			}

			holder.image.setImageResource(myImageList.get(position));
			holder.image.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
			holder.image.setScaleType(ImageView.ScaleType.FIT_XY);
			holder.description.setText(titolo);
			// holder.setBackgroundResource(GalItemBg);
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

	public class ImageAdapter extends BaseAdapter {
		int GalItemBg;
		private Context ctx;

		public ImageAdapter(Context c) {
			ctx = c;
			TypedArray typArray = obtainStyledAttributes(R.styleable.GalleryTheme);
			GalItemBg = typArray.getResourceId(
					R.styleable.GalleryTheme_android_galleryItemBackground, 0);
			typArray.recycle();
		}

		@Override
		public int getCount() {
			return myImageList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int elemento, View arg1, ViewGroup arg2) {
			LinearLayout layout = new LinearLayout(getApplicationContext());
			layout.setOrientation(LinearLayout.VERTICAL);
			// FrameLayout layout = new FrameLayout(getApplicationContext());
			// layout.setLayoutParams(new
			// FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT,
			// FrameLayout.LayoutParams.FILL_PARENT));

			ImageView img = null;

			if (arg1 == null) {
				img = new ImageView(ctx);
			} else {
				img = (ImageView) arg1;
			}

			img.setImageResource(myImageList.get(elemento));
			img.setScaleType(ImageView.ScaleType.FIT_XY);
			img.setLayoutParams(new Gallery.LayoutParams(200, 200));
			// img.setBackgroundResource(itemBackground);

			TextView tv = new TextView(ctx);

			String titolo = "Test";

			if (elemento == 0) {
				titolo = "Mon";
			} else if (elemento == 1) {
				titolo = "Tue";
			} else if (elemento == 2) {
				titolo = "Wed";
			} else if (elemento == 3) {
				titolo = "Thu";
			}

			tv.setText(titolo);
			tv.setTextColor(0xffffffff);
			tv.setTextSize(8);
			tv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT));
			// tv.setTextColor(0xffffffff);
			tv.setGravity(Gravity.CENTER);
			// tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
			// LayoutParams.WRAP_CONTENT));

			// Utilizzo l'AssetManager per cambiare il font
			// AssetManager assetManager = getResources().getAssets();
			// Typeface typeface = Typeface.createFromAsset(assetManager,
			// "fonts/CALIFR.TTF");
			// tv.setTypeface(typeface);
			// tv.setTextSize(50);
			tv.setPadding(0, 0, 0, 40); // imposto il margine di bottom del
										// testo

			layout.addView(img);
			layout.addView(tv);
			layout.setBackgroundResource(GalItemBg);

			return layout;
		}

	}

	static class ViewHolder {
		ImageView image;
		TextView description;
	}

}
