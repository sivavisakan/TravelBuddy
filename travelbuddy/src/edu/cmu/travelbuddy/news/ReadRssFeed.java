package edu.cmu.travelbuddy.news;

import java.net.URL;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import edu.cmu.travelbuddy.R;

/**
 * 
 * @author amol
 * 
 */
public class ReadRssFeed extends Activity {

	static final String PUB_DATE = "pubDate";
	static final String DESCRIPTION = "description";
	static final String CHANNEL = "channel";
	static final String LINK = "link";
	static final String TITLE = "title";
	static final String ITEM = "item";

	static final String PREPARE_LINK_FIRSTPART = "http://news.google.com/news?pz=1&cf=all&ned=us&hl=en&geo=";
	static final String PREPARE_LINK_LASTPART = "&output=rss";
	private static String NEWS_LINK;

	private ArrayList<NewsItem> myList;
	private NewsItem myItem;

	private int ignoreTitleFirst = 2;
	private int ignoreLinkFirst = 2;
	private boolean ignorePubDateFirst = true;

	/**
	 * Constructor to initialize the link for news parsing
	 * 
	 * @param zipcode
	 *            Creates the link for Google news based on the location code
	 *            passed by main application
	 */
	// public ReadRssFeed(String zipcode){
	// NEWS_LINK = PREPARE_LINK_FIRSTPART + zipcode + PREPARE_LINK_LASTPART;
	// }

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newslist);
		// parse("http://news.google.com/news?pz=1&cf=all&ned=us&hl=en&geo=77281&output=rss");
		// parse(NEWS_LINK);
		parse("http://news.google.com/news?pz=1&cf=all&ned=us&hl=en&geo=77281&output=rss");
		final ListView lv1 = (ListView) findViewById(R.id.NewsList);
		if(lv1 == null){
			Log.d("List", "List is null");
		}
		lv1.setAdapter(new MyCustomBaseAdapter(this, myList));

		lv1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {
				Object o = lv1.getItemAtPosition(position);
				// SearchResults fullObject = (SearchResults)o;
				// Toast.makeText(ListViewBlogPost.this, "You have chosen: " +
				// " " + fullObject.getName(), Toast.LENGTH_LONG).show();
			}
		});
	}

	public void parse(String rssFeed) {
		myList = new ArrayList<NewsItem>();
		XmlPullParser parser = Xml.newPullParser();
		try {
			// auto-detect the encoding from the stream
			parser.setInput(new URL(rssFeed).openConnection().getInputStream(),
					null);
			int eventType = parser.getEventType();
			boolean done = false;
			while (eventType != XmlPullParser.END_DOCUMENT && !done) {
				String name = null;
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					myList = new ArrayList<NewsItem>();
					break;
				case XmlPullParser.START_TAG:
					name = parser.getName();
					if (name.equalsIgnoreCase(ITEM)) {
						myItem = new NewsItem();
					} else {
						if (name.equalsIgnoreCase(LINK)) {
							// Log.i("link", parser.nextText());
							if (ignoreLinkFirst > 0) {
								ignoreLinkFirst--;
							} else
								myItem.setLink(parser.nextText());
						} else if (name.equalsIgnoreCase(DESCRIPTION)) {

							// Log.i("descr", parser.nextText());
							myItem.setDescription(parser.nextText());
							myList.add(myItem);
						} else if (name.equalsIgnoreCase(PUB_DATE)) {
							// Log.i("date", parser.nextText());
							if (ignorePubDateFirst) {
								ignorePubDateFirst = false;
							} else
								myItem.setDate(parser.nextText());
						} else if (name.equalsIgnoreCase(TITLE)) {
							// Log.i("title", parser.nextText());
							if (ignoreTitleFirst > 0) {
								ignoreTitleFirst--;
							} else
								myItem.setTitle(parser.nextText());
						}
					}
					break;
				case XmlPullParser.END_TAG:
					name = parser.getName();
					if (name.equalsIgnoreCase(ITEM)) {
						myList.add(myItem);
					} else if (name.equalsIgnoreCase(CHANNEL)) {
						done = true;
					}
					break;
				}
				eventType = parser.next();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
