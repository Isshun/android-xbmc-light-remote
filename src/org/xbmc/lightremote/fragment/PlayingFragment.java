package org.xbmc.lightremote.fragment;

import java.io.File;
import java.util.List;

import org.xbmc.lightremote.Application;
import org.xbmc.lightremote.R;
import org.xbmc.lightremote.data.MovieModel;
import org.xbmc.lightremote.data.PlayingProperties;
import org.xbmc.lightremote.http.IServiceListener;
import org.xbmc.lightremote.service.ImageService;
import org.xbmc.lightremote.service.PlayerService;
import org.xbmc.lightremote.service.ServiceManager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class PlayingFragment extends Fragment implements OnClickListener, IServiceListener {
	
	private PlayerService mService;
	private View mView;
	
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

    	// Inflate the layout for this fragment
    	mView = inflater.inflate(R.layout.fragment_playing, container, false); 

    	mView.findViewById(R.id.bt_start).setOnClickListener(this);
    	mView.findViewById(R.id.bt_stop).setOnClickListener(this);
    	mView.findViewById(R.id.bt_refresh).setOnClickListener(this);
    	
    	mService = PlayerService.getInstance();
		mService.setListener(this);

        return mView; 
    }

	@Override
	public void onResume() {
		super.onResume();
		mService.reqPlaying();
		mService.reqProperties();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_start:
			mService.reqPlay();
			break;
		case R.id.bt_stop:
			mService.reqStop();
			break;
		case R.id.bt_refresh:
			mService.reqPlaying();
			mService.reqProperties();
			break;
		}
	}

	@Override
	public void onActionError(int action, String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onActionCompleted(int action) {
		switch (action) {
		case PlayerService.ACTION_STOP:
			mView.findViewById(R.id.layout_no_playing).setVisibility(View.VISIBLE);
			mView.findViewById(R.id.layout_area).setVisibility(View.GONE);
			break;
		case PlayerService.GET_PLAYING:
			final MovieModel movie = mService.getPlaying();
			
			if (movie != null) {
				mView.findViewById(R.id.layout_no_playing).setVisibility(View.GONE);
				mView.findViewById(R.id.layout_area).setVisibility(View.VISIBLE);

				((TextView)mView.findViewById(R.id.lb_id)).setText(String.valueOf(movie.getMovieId()));
				((TextView)mView.findViewById(R.id.lb_label)).setText(movie.getTitle());
				((TextView)mView.findViewById(R.id.lb_file)).setText(movie.getFile());
				((TextView)mView.findViewById(R.id.lb_rating)).setText(String.valueOf(movie.getRating()));
				((TextView)mView.findViewById(R.id.lb_playcount)).setText(String.valueOf(movie.getPlayCount()));
				((TextView)mView.findViewById(R.id.lb_duration)).setText(String.valueOf(movie.getDuration()));

				// Thumbnail path 
//				String thumbnailPath = movie.thumbnailPath; 
//				if (movie.thumbnailPath == null) {
//					ServiceManager.getLibraryService().getMovies(new OnGetMoviesListener() {
//						@Override
//						public void onGetMovies(List<MovieModel> movies) {
//							for (MovieModel m: movies) {
//								if (m.movieId == movie.movieId && m.thumbnailPath != null && new File(m.thumbnailPath).exists()) {
//									thumbnailPath = m.thumbnailPath;
////							        AQuery aq = new AQuery(view);
////							        aq.id(R.id.img_playing).image(m.thumbnailPath, true, true, 120, 0, null, AQuery.FADE_IN);
//								}
//							}
//						}
//					});
//				}
//				if (thumbnailPath != null && new File(thumbnailPath).exists()) {
//					ImageView img = (ImageView)mView.findViewById(R.id.img_playing);
//					ImageService.getInstance().showThumb(img, thumbnailPath);
//				}
				
				// Seek bar
				SeekBar progress = (SeekBar)mView.findViewById(R.id.seekProgress);
				progress.setMax(movie.getDuration());
				progress.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						mService.reqSeek((double)seekBar.getProgress() / seekBar.getMax() * 100);
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
				
				// Volume bar
				SeekBar volume = (SeekBar)mView.findViewById(R.id.seekVolume);
				volume.setMax(100);
				volume.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						mService.reqVolume(seekBar.getProgress());
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
			break;
		case PlayerService.GET_PROPERTIES:
			PlayingProperties properties = mService.getProperties();
			
			if (properties != null) {
				((TextView)mView.findViewById(R.id.lb_progress_time)).setText(String.valueOf(properties.percentage));
			}

			break;
		}
	}

	@Override
	public void onActionStart(int action) {
		// TODO Auto-generated method stub
		
	}

}