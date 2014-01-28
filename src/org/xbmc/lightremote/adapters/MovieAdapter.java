package org.xbmc.lightremote.adapters;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import org.xbmc.lightremote.Application;
import org.xbmc.lightremote.R;
import org.xbmc.lightremote.data.MovieModel;
import org.xbmc.lightremote.service.ImageService;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MovieAdapter extends BaseAdapter {

	static class ViewHolder {
		public ImageView imgThumb;
		public TextView lbName;
	}

	private Context 		mContext;
	private List<MovieModel> 	mMovies;
	private int mLayout;

	public MovieAdapter(Context context) {
		mContext = context;
	}
	
	@Override
	public int getCount() {
		//return mAreas != null ? (mAreas.size() > 2 ? mAreas.size() + 1 : mAreas.size()) : 0;
		return mMovies != null ? mMovies.size() : 0;
	}

	@Override
	public MovieModel getItem(int position) {
		return mMovies != null ? mMovies.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return mMovies != null ? mMovies.get(position).getMovieId() : 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if (mLayout == 0) throw new IllegalArgumentException("setLayout must be call before view rendering");
		
		MovieModel m = mMovies.get(position);

        ViewHolder holder;
        
        if (view == null) {                                        
    		view = LayoutInflater.from(mContext).inflate(mLayout, parent, false);
            holder = new ViewHolder();
            holder.imgThumb = (ImageView) view.findViewById(R.id.imgThumb);
            holder.lbName = (TextView) view.findViewById(R.id.lb_name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
		
        
//		if (holder.imgThumb != null && m.thumbnailPath != null && new File(m.thumbnailPath).exists()) {
		ImageService.getInstance().showThumb(holder.imgThumb, m.getThumbnail());
//		}

		if (holder.lbName != null)
			holder.lbName.setText(m.getTitle());

		return view;
	}

	public void setData(List<MovieModel> data) {
		mMovies = data;
	}

	public void setLayout(int layout) {
		mLayout = layout;
	}
}
