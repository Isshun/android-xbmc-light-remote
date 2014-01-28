package org.xbmc.lightremote.adapters;

import java.util.List;

import org.xbmc.lightremote.Application;
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

	private List<String> mGenres;

	public LibraryFilterAdapter(Context context, int resource) {
		super(context, resource);
	}

	public void setGenres(List<String> genres) {
		mGenres = genres;
	}
	
	@Override
	public int getCount() {
		return mGenres != null ? mGenres.size() : 0;
	}

	@Override
	public String getItem(int position) {
		return mGenres.get(position);
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
    	lbCategory.setText(position == 0 ? Application.getResString(R.string.home_item_all_genres) : getItem(position));

		return view;
	}

}
