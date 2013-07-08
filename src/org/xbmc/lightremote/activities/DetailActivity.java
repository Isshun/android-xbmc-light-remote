package org.xbmc.lightremote.activities;

import org.xbmc.lightremote.R;
import org.xbmc.lightremote.data.Movie;
import org.xbmc.lightremote.http.IServiceDelegate;
import org.xbmc.lightremote.http.services.PlayerService;
import android.app.Activity;
import android.os.Bundle;
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
			mService.reqOpen(mMovie.file);
			setResult(1);
			finish();
			break;
		}
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
