package edu.cmu.travelbuddy.expense;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;

public class ExpenseChart {

	public View execute(Context context, String[] item, int[] prize) {

		CategorySeries categorySeries = new CategorySeries("Expense Chart");
		HashMap<String, Integer> chart = new HashMap<String, Integer>();
		for (int i = 0; i < item.length; i++) {
			if (chart.containsKey(item[i])) {
				int oldPrize = chart.get(item[i]);
				int totalPrize = oldPrize + prize[i];
				chart.remove(item[i]);
				chart.put(item[i], totalPrize);
			} else {
				chart.put(item[i], prize[i]);
			}
		}
		Iterator<Entry<String, Integer>> iterator = chart.entrySet().iterator();
		int dataSetLength = 0;
		while (iterator.hasNext()) {
			Map.Entry<String, Integer> pairs = (Map.Entry<String, Integer>) iterator.next();
			String tag = (String) pairs.getKey();
			int cost = pairs.getValue().intValue();
			categorySeries.add(tag, cost);
			dataSetLength++;
			iterator.remove(); // avoids a ConcurrentModificationException
		}

		int[] colors = new int[dataSetLength];
		for (int i = 0; i < dataSetLength; i++) {
			Random rnd = new Random();
			colors[i] = Color.rgb(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255));
		}
		DefaultRenderer renderer = buildCategoryRenderer(colors);
		View v= ChartFactory.getPieChartView(context, categorySeries, renderer);
		return v;

		//return ChartFactory.getPieChartIntent(context, categorySeries, renderer, "Expense Chart");
	}

	protected DefaultRenderer buildCategoryRenderer(int[] colors) {
		DefaultRenderer renderer = new DefaultRenderer();
		for (int color : colors) {
			SimpleSeriesRenderer r = new SimpleSeriesRenderer();
			r.setColor(color);
			renderer.addSeriesRenderer(r);
		}
		return renderer;
	}
}
