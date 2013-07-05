package org.xbmc.lightremote.activities;

import org.xbmc.lightremote.R;
import org.xbmc.lightremote.data.Movie;
import org.xbmc.lightremote.data.PlayingProperties;
import org.xbmc.lightremote.fragments.LibraryMoviesFragment;
import org.xbmc.lightremote.http.IServiceDelegate;
import org.xbmc.lightremote.http.services.PlayerService;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class DetailActivity extends Activity implements OnClickListener, IServiceDelegate {

	private PlayerService 	mService;
	private Movie 			mMovie;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);

		mService = new PlayerService(this);
		
		Bundle bundle = getIntent().getExtras();
		mMovie = bundle.getParcelable("movie");
		
		((TextView)findViewById(R.id.lb_id)).setText(String.valueOf(mMovie.movieId));
		((TextView)findViewById(R.id.lb_label)).setText(mMovie.label);
		((TextView)findViewById(R.id.lb_file)).setText(mMovie.file);
		((TextView)findViewById(R.id.lb_rating)).setText(String.valueOf(mMovie.rating));
		((TextView)findViewById(R.id.lb_playcount)).setText(String.valueOf(mMovie.playCount));
		((TextView)findViewById(R.id.lb_duration)).setText(String.valueOf(mMovie.duration));

		findViewById(R.id.bt_play).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.bt_play:
			mService.open(mMovie.file);
			setResult(1);
			finish();
			break;
		}
	}

//	@Override
//	public void onActionError(int action, String message) {
//	}
//
//	@Override
//	public void onActionCompleted(int action) {
//		switch (action) {
//			case PlayerService.ACTION_OPEN:
//				mService.getPlaying();
//				break;
//		}
//	}

	@Override
	public void onGetPlaying(Movie movie) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGetProperties(PlayingProperties data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onActionError(int action, String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onActionCompleted(int action) {
		// TODO Auto-generated method stub
		
	}

}