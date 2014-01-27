package org.xbmc.lightremote.adapters;

import org.xbmc.lightremote.R;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

public class LibraryFilterAdapter extends ArrayAdapter<String> {

	public static String mCategories[] = {"Comedy", "Thriller", "Sci-Fi", "Horror", "Romance", "Adventure", "Survival", "test", "test", "test", "test", "test", "test", "test", "test", "test", "test", "test", "test", "test", "test"};
	
	public LibraryFilterAdapter(Context context, int resource) {
		super(context, resource);
	}

	@Override
	public int getCount() {
		return mCategories.length;
	}

	@Override
	public String getItem(int position) {
		return mCategories[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup container) {
		if (view == null) {
			view = LayoutInflater.from(getContext()).inflate(R.layout.item_category_filter, container, false);
		}
		
		TextView lbCategory = (TextView)view.findViewById(R.id.lb_category);
    	lbCategory.setText(getItem(position));

		return view;
	}

}
