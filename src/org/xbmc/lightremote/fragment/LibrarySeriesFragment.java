package org.xbmc.lightremote.fragment;

import java.util.List;

import org.xbmc.lightremote.R;
import org.xbmc.lightremote.R.id;
import org.xbmc.lightremote.R.layout;
import org.xbmc.lightremote.activities.DetailActivity;
import org.xbmc.lightremote.adapters.MovieAdapter;
import org.xbmc.lightremote.data.MovieModel;
import org.xbmc.lightremote.data.PlayingProperties;
import org.xbmc.lightremote.http.HttpTask;
import org.xbmc.lightremote.http.IDownloadTaskDelegate;
import org.xbmc.lightremote.http.IServiceListener;
import org.xbmc.lightremote.http.IWebserviceTaskDelegate;
import org.xbmc.lightremote.http.TestDelegate;
import org.xbmc.lightremote.http.tasks.PlayerOpenTask;
import org.xbmc.lightremote.http.tasks.LibraryGetMoviesTask;
import org.xbmc.lightremote.service.LibraryService;
import org.xbmc.lightremote.service.PlayerService;

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

public class LibrarySeriesFragment extends Fragment implements IServiceListener {
    private NotificationManager mNotifyManager;
	private Builder mBuilder;
	private int mMoviesCount;
	private int mThumbnailProgress;
	private PlayerService mService;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return container;
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
	public void onActionStart(int action) {
		// TODO Auto-generated method stub
		
	}
}