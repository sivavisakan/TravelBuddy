package edu.cmu.travelbuddy.news;

import java.util.ArrayList;

import edu.cmu.travelbuddy.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyCustomBaseAdapter extends BaseAdapter {
	private static ArrayList<NewsItem> myArrayList;

	private LayoutInflater mInflater;

	public MyCustomBaseAdapter(Context context, ArrayList<NewsItem> results) {
		myArrayList = results;
		mInflater = LayoutInflater.from(context);
	}

	public int getCount() {
		return myArrayList.size();
	}

	public Object getItem(int position) {
		return myArrayList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.newslistitem, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.date = (TextView) convertView
					.findViewById(R.id.date);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.title.setText(myArrayList.get(position).getTitle());
		holder.date.setText(myArrayList.get(position)
				.getDate());

		return convertView;
	}

	static class ViewHolder {
		TextView title;
		TextView date;
	}
}