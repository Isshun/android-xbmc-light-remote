package org.xbmc.lightremote.fragment;

import java.util.List;

import org.xbmc.lightremote.R;
import org.xbmc.lightremote.activity.MediaActivity;
import org.xbmc.lightremote.adapters.MovieAdapter;
import org.xbmc.lightremote.data.MovieModel;
import org.xbmc.lightremote.http.HttpTask.HttpTaskListener;
import org.xbmc.lightremote.service.PlayerService;
import org.xbmc.lightremote.service.ServiceManager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class LibraryMoviesFragment extends Fragment implements OnItemClickListener, OnClickListener {
	private MovieAdapter 		mMovieAdapter;
	private ProgressBar 		mProgress;
	private ListView 			mListview;
	private GridView 			mGridview;
	private TextView 			mLbError;
	private View 				mLayoutError;
	private boolean 			mGridMode = true;
	private String 				mGenre; 

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    	// Inflate the layout for this fragment
    	View v = inflater.inflate(R.layout.fragment_library_collection, container, false); 

    	mGenre = getArguments().getString("genre");
    	
		mMovieAdapter = new MovieAdapter(getActivity());
		//mMovieAdapter.setLayout(R.layout.list_item_movie);
		mMovieAdapter.setLayout(R.layout.grid_item_movie);

		mLbError = (TextView)v.findViewById(R.id.lb_error);
		mLayoutError = (View)v.findViewById(R.id.layout_error);
		v.findViewById(R.id.bt_retry).setOnClickListener(this);
		
		mProgress = (ProgressBar)v.findViewById(R.id.progress_library);
		mListview = (ListView)v.findViewById(R.id.list_library);
		mListview.setOnItemClickListener(this);
		mListview.setAdapter(mMovieAdapter);
		mListview.setVisibility(View.GONE);

		mGridview = (GridView)v.findViewById(R.id.grid_library);
		mGridview.setOnItemClickListener(this);
		mGridview.setAdapter(mMovieAdapter);
		mGridview.setVisibility(View.VISIBLE);
		
//    	mNotifyManager = (NotificationManager) getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);
//    	mBuilder = new NotificationCompat.Builder(getActivity());
//    	mBuilder.setContentTitle("Download Thumbnails")
////    	    .setContentText("Download in progress")
//    	    .setSmallIcon(android.R.drawable.ic_dialog_info);

		refresh();
		
        return v; 
    }
	
	private void refresh() {
		showProgress();

		ServiceManager.getLibraryService().getMovies(new HttpTaskListener<List<MovieModel>>() {

			@Override
			public void onSuccess(List<MovieModel> movies) {
				mMovieAdapter.setData(movies);
				mMovieAdapter.notifyDataSetChanged();
				if (mGridMode)
					showGrid();
				else
					showList();
			}

			@Override
			public void onFailed(String message, int code) {
				mLbError.setText(message);
				showError();
			}
		}, mGenre);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	private void showProgress() {
		mLayoutError.setVisibility(View.GONE);
		mListview.setVisibility(View.GONE);
		mProgress.setVisibility(View.VISIBLE);
		mGridview.setVisibility(View.GONE);
	}

	private void showError() {
		mLayoutError.setVisibility(View.VISIBLE);
		mListview.setVisibility(View.GONE);
		mProgress.setVisibility(View.GONE);
		mGridview.setVisibility(View.VISIBLE);
	}

	private void showList() {
		mLayoutError.setVisibility(View.GONE);
		mProgress.setVisibility(View.GONE);
		mListview.setVisibility(View.VISIBLE);
		mGridview.setVisibility(View.GONE);
	}

	private void showGrid() {
		mLayoutError.setVisibility(View.GONE);
		mProgress.setVisibility(View.GONE);
		mListview.setVisibility(View.GONE);
		mGridview.setVisibility(View.VISIBLE);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		//mService.open(adapter.getItem(pos).file);
		Intent intent = new Intent(getActivity(), MediaActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		intent.putExtra("movie", mMovieAdapter.getItem(pos));
		intent.putExtra("genre", mGenre);
		intent.putExtra("position", pos);
		startActivityForResult(intent, 1);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.bt_retry:
				refresh();
			break;
		}
	}

}