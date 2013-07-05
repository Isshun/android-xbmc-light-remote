package org.xbmc.lightremote.fragments;

import org.xbmc.lightremote.R;
import org.xbmc.lightremote.data.Movie;
import org.xbmc.lightremote.data.PlayingProperties;
import org.xbmc.lightremote.http.IServiceDelegate;
import org.xbmc.lightremote.http.services.PlayerService;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class PlayingFragment extends Fragment implements OnClickListener, IServiceDelegate {
	
	private PlayerService mService;
	private View mView;
	
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

    	// Inflate the layout for this fragment
    	mView = inflater.inflate(R.layout.fragment_playing, container, false); 

    	mView.findViewById(R.id.bt_start).setOnClickListener(this);
    	mView.findViewById(R.id.bt_stop).setOnClickListener(this);
    	mView.findViewById(R.id.bt_refresh).setOnClickListener(this);
    	
    	mService = new PlayerService(this);
//    	mService.getPlaying();

        return mView; 
    }

	@Override
	public void onResume() {
		super.onResume();
		mService.getPlaying();
		mService.getProperties();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_start:
			mService.play();
			break;
		case R.id.bt_stop:
			mService.stop();
			break;
		case R.id.bt_refresh:
			mService.getPlaying();
			mService.getProperties();
			break;
		}
	}

	@Override
	public void onActionError(int action, String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onActionCompleted(int action) {
//		switch (action) {
//		case PlayerService.GET_PLAYING:
//			break;
		}

	@Override
	public void onGetPlaying(Movie movie) {
		if (movie != null) {
			mView.findViewById(R.id.layout_no_playing).setVisibility(View.GONE);
			mView.findViewById(R.id.layout_area).setVisibility(View.VISIBLE);

			((TextView)mView.findViewById(R.id.lb_id)).setText(String.valueOf(movie.movieId));
			((TextView)mView.findViewById(R.id.lb_label)).setText(movie.label);
			((TextView)mView.findViewById(R.id.lb_file)).setText(movie.file);
			((TextView)mView.findViewById(R.id.lb_rating)).setText(String.valueOf(movie.rating));
			((TextView)mView.findViewById(R.id.lb_playcount)).setText(String.valueOf(movie.playCount));
			((TextView)mView.findViewById(R.id.lb_duration)).setText(String.valueOf(movie.duration));

			SeekBar progress = (SeekBar)mView.findViewById(R.id.seekProgress);
			progress.setMax(movie.duration);
			progress.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					mService.seek((double)seekBar.getProgress() / seekBar.getMax() * 100);
				}
				
				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
					// TODO Auto-generated method stub
					
				}
			});
			
			SeekBar volume = (SeekBar)mView.findViewById(R.id.seekVolume);
			volume.setMax(100);
			volume.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					mService.setVolume(seekBar.getProgress());
				}
				
				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
					// TODO Auto-generated method stub
					
				}
			});
			
		} else {
			mView.findViewById(R.id.layout_no_playing).setVisibility(View.VISIBLE);
			mView.findViewById(R.id.layout_area).setVisibility(View.GONE);
		}
	}

	@Override
	public void onGetProperties(PlayingProperties properties) {
		if (properties != null) {
			((TextView)mView.findViewById(R.id.lb_progress_time)).setText(String.valueOf(properties.percentage));
		}
	}
}