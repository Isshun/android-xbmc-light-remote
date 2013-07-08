package org.xbmc.lightremote.fragments;

import org.xbmc.lightremote.R;
import org.xbmc.lightremote.activities.DetailActivity;
import org.xbmc.lightremote.adapters.MovieAdapter;
import org.xbmc.lightremote.http.IServiceDelegate;
import org.xbmc.lightremote.http.services.PlayerService;
import android.app.NotificationManager;
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
import android.support.v4.app.NotificationCompat.Builder;

public class LibraryMoviesFragment extends Fragment implements IServiceDelegate, OnItemClickListener, OnClickListener {
    private NotificationManager	mNotifyManager;
	private Builder 			mBuilder;

	private PlayerService 		mService;
	private MovieAdapter 		mMovieAdapter;
	private ProgressBar 		mProgress;
	private ListView 			mListview;
	private GridView 			mGridview;
	private TextView 			mLbError;
	private View 				mLayoutError;

	private int 				mMoviesCount;
	private int 				mThumbnailProgress;
	private int 				mActionRetry;
	private boolean 			mGridMode = true; 

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    	// Inflate the layout for this fragment
    	View v = inflater.inflate(R.layout.fragment_library, container, false); 

		mMovieAdapter = new MovieAdapter(getActivity());
		//mMovieAdapter.setLayout(R.layout.list_item_movie);
		mMovieAdapter.setLayout(R.layout.grid_item_movie);

		mService = new PlayerService(this);
		
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
    	
        return v; 
    }
	
	@Override
	public void onResume() {
		super.onResume();
		mService.reqMoviesLibrary();
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
	public void onActionStart(int action) {
		switch (action) {
			case PlayerService.GET_MOVIES:
				showProgress();
			break;
		}
	}

	@Override
	public void onActionError(int action, String message) {
		switch (action) {
			case PlayerService.GET_MOVIES:
				mActionRetry = action;
				mLbError.setText(message);
				showError();
				break;
		}
	}

	@Override
	public void onActionCompleted(int action) {
		switch (action) {
			case PlayerService.GET_MOVIES:
				mMovieAdapter.setData(mService.getMovies());
				mMovieAdapter.notifyDataSetChanged();
				if (mGridMode)
					showGrid();
				else
					showList();
				break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		//mService.open(adapter.getItem(pos).file);
		Intent intent = new Intent(getActivity(), DetailActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		intent.putExtra("movie", mMovieAdapter.getItem(pos));
		startActivityForResult(intent, 1);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.bt_retry:
				mService.reqMoviesLibrary();
			break;
		}
	}

}