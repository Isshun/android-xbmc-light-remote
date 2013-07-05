package org.xbmc.lightremote.adapters;

import java.io.File;
import java.util.List;

import org.xbmc.lightremote.App;
import org.xbmc.lightremote.R;
import org.xbmc.lightremote.data.Movie;

import com.androidquery.AQuery;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MovieAdapter extends BaseAdapter {

	static class ViewHolder {
		public ImageView imgThumb;
		public TextView lbName;
	}

	private Context 		mContext;
	private List<Movie> 	mMovies;

	public MovieAdapter(Context context) {
		mContext = context;
	}
	
	@Override
	public int getCount() {
		//return mAreas != null ? (mAreas.size() > 2 ? mAreas.size() + 1 : mAreas.size()) : 0;
		return mMovies != null ? mMovies.size() : 0;
	}

	@Override
	public Movie getItem(int position) {
		return mMovies != null ? mMovies.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return mMovies != null ? mMovies.get(position).movieId : 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(mContext);

		Movie m = mMovies.get(position);
		
        ViewHolder holder;
        
        if (view == null) {                                        
    		view = inflater.inflate(R.layout.list_item_movie, parent, false);
            holder = new ViewHolder();
            holder.imgThumb = (ImageView) view.findViewById(R.id.imgThumb);
            holder.lbName = (TextView) view.findViewById(R.id.lb_name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
		
		if (m.thumbnailPath != null && new File(m.thumbnailPath).exists()) {
	        AQuery aq = new AQuery(view);
	        aq.id(holder.imgThumb).image(m.thumbnailPath, true, true, 120, 0, null, AQuery.FADE_IN);
		}

		holder.lbName.setText(m.label);

		return view;
	}

	public void setData(List<Movie> data) {
		mMovies = data;
		notifyDataSetChanged();
	}
}
