package org.xbmc.lightremote.fragments;

import java.util.List;

import org.xbmc.lightremote.App;
import org.xbmc.lightremote.R;
import org.xbmc.lightremote.R.id;
import org.xbmc.lightremote.R.layout;
import org.xbmc.lightremote.activities.DetailActivity;
import org.xbmc.lightremote.adapters.MovieAdapter;
import org.xbmc.lightremote.data.Movie;
import org.xbmc.lightremote.data.PlayingProperties;
import org.xbmc.lightremote.http.HttpTask;
import org.xbmc.lightremote.http.IDownloadTaskDelegate;
import org.xbmc.lightremote.http.IServiceDelegate;
import org.xbmc.lightremote.http.IWebserviceTaskDelegate;
import org.xbmc.lightremote.http.TestDelegate;
import org.xbmc.lightremote.http.services.LibraryService;
import org.xbmc.lightremote.http.services.PlayerService;
import org.xbmc.lightremote.http.tasks.PlayerOpenTask;
import org.xbmc.lightremote.http.tasks.LibraryGetMoviesTask;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.support.v4.app.NotificationCompat.Builder;

public class LibraryMoviesFragment extends Fragment implements IServiceDelegate {
    private NotificationManager mNotifyManager;
	private Builder mBuilder;
	private int mMoviesCount;
	private int mThumbnailProgress;
	private PlayerService mService;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    	// Inflate the layout for this fragment
    	View v = inflater.inflate(R.layout.fragment_library, container, false); 

		final MovieAdapter adapter = new MovieAdapter(getActivity());

		mService = new PlayerService(this);
		
    	mNotifyManager = (NotificationManager) getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);
    	mBuilder = new NotificationCompat.Builder(getActivity());
    	mBuilder.setContentTitle("Download Thumbnails")
//    	    .setContentText("Download in progress")
    	    .setSmallIcon(android.R.drawable.ic_dialog_info);
    	
    	final IDownloadTaskDelegate fileDelegate = new IDownloadTaskDelegate() {

			@Override
			public void onProgress(int progress) {
			}
    		
			@Override
			public void onTaskError(HttpTask task, String message, int statusCode) {
//				if (++mThumbnailProgress == mMoviesCount) {
//					mBuilder.setContentText("Download complete").setProgress(0,0,false);
//					mNotifyManager.notify(0, mBuilder.build());
//				}
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onTaskCompleted(HttpTask task, Object data) {
				//Log.i(App.APP_NAME, String.format("Download complete: %s", data));
				
//				if (++mThumbnailProgress < mMoviesCount) {
//		            mBuilder.setProgress(mMoviesCount, mThumbnailProgress, false);
//		            mBuilder.setContentText(String.format("%d / %d", mThumbnailProgress, mMoviesCount));
//		            mNotifyManager.notify(0, mBuilder.build());
//				} else {
//					mBuilder.setContentText("Download complete").setProgress(0,0,false);
//					mNotifyManager.notify(0, mBuilder.build());
//					mNotifyManager.cancel(0);
//				}
				
				adapter.notifyDataSetChanged();
			}
    	};
    	
		
    	IWebserviceTaskDelegate delegate = new IWebserviceTaskDelegate() {

			@Override
			public void onTaskError(HttpTask task, String message, int statusCode) {
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onTaskCompleted(HttpTask task, Object data) {
				List<Movie> movies = (List<Movie>) data;
				mMoviesCount = movies.size();
				
				adapter.setData(movies);

				for (Movie m: (movies)) {
					if (m.thumbnailPath != null) {
						LibraryService service = new LibraryService();
						service.download(fileDelegate, m.thumbnail, m.thumbnailPath);
					}
				}
			}
    	};
    	
    	
    	mThumbnailProgress = 0;
    	LibraryGetMoviesTask task = new LibraryGetMoviesTask(delegate, fileDelegate);
		
		ListView list = (ListView) v.findViewById(R.id.listView);
		
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				//mService.open(adapter.getItem(pos).file);
				Intent intent = new Intent(getActivity(), DetailActivity.class);
				intent.putExtra("movie", adapter.getItem(pos));
				startActivityForResult(intent, 1);
			}
		});
		
		list.setAdapter(adapter);
    	
        return v; 
    }

	@Override
	public void onActionError(int action, String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onActionCompleted(int action) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGetPlaying(Movie movie) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGetProperties(PlayingProperties data) {
		// TODO Auto-generated method stub
		
	}
}