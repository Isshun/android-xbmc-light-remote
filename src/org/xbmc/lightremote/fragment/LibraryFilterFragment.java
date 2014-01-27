package org.xbmc.lightremote.fragment;

import org.xbmc.lightremote.R;
import org.xbmc.lightremote.adapters.LibraryFilterAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class LibraryFilterFragment extends Fragment {
	
	public static interface OnCategoryChangeListener {
		void onCategoryChange(String category);
	}
	
	private OnCategoryChangeListener	mListener;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    	View v = inflater.inflate(R.layout.fragment_library_filter, container, false); 
    	
    	final LibraryFilterAdapter adapter = new LibraryFilterAdapter(getActivity(), R.layout.item_category_filter);
    	
    	GridView grid = (GridView)v.findViewById(R.id.grid);
    	grid.setAdapter(adapter);

    	grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				mListener.onCategoryChange(adapter.getItem(position));
			}
		});
    	
        return v; 
    }

	public void setOnCategoryChangeListener(OnCategoryChangeListener listener) {
		mListener = listener;
	}
}
