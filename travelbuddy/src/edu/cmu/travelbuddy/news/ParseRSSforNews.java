package edu.cmu.travelbuddy.news;

import java.net.URL;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

public class ParseRSSforNews {

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

	public ParseRSSforNews(String address) {
		address = address.replaceAll(" ", "%20");
		String parseString = "http://news.google.com/news?pz=1&cf=all&ned=us&hl=en&geo=" + address
				+ "&output=rss";
		parse(parseString);
	}

	public ArrayList<NewsItem> getListOfNewsItems() {
		return myList;
	}

	private void parse(String rssFeed) {
		myList = new ArrayList<NewsItem>();
		XmlPullParser parser = Xml.newPullParser();
		try {
			// auto-detect the encoding from the stream
			parser.setInput(new URL(rssFeed).openConnection().getInputStream(), null);
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
