package org.xbmc.lightremote.view;

import org.xbmc.lightremote.Application;
import org.xbmc.lightremote.R;
import org.xbmc.lightremote.data.MovieModel;
import org.xbmc.lightremote.data.PlayingProperties;
import org.xbmc.lightremote.http.HttpTask.HttpTaskListener;
import org.xbmc.lightremote.http.tasks.PlayerGetCurrentTask;
import org.xbmc.lightremote.http.tasks.PlayerGetPropertiesTask;
import org.xbmc.lightremote.service.ServiceManager;

import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class PlayerView extends FrameLayout {

	private Handler mHandler;
	private int mPosX;
	private int mStartPosX;
	private PlayingProperties mProperties;
	private double mStartPercent;
	private Toast mToast;
	private double mStartTime;
	private boolean mIsSeek;

	public PlayerView(Context context) {
		super(context);
	}

	public PlayerView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PlayerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		final TextView lbPlayer = (TextView)findViewById(R.id.player_lb_name);
		if (lbPlayer != null) {
			lbPlayer.setText("...");
		}
		
		final ProgressBar progressPlayer = (ProgressBar)findViewById(R.id.player_progress);
		progressPlayer.setProgress(50);
		progressPlayer.setMax(100);
		
		final ImageView btPlayer = (ImageView)findViewById(R.id.player_bt);
		
		PlayerGetPropertiesTask propertiesTask = new PlayerGetPropertiesTask();
		propertiesTask.addListener(new HttpTaskListener<PlayingProperties>() {

			@Override
			public void onSuccess(PlayingProperties properties) {
				Log.e(Application.APP_NAME, "success");
				mProperties = properties;
				if (properties != null && progressPlayer != null) {
					progressPlayer.setProgress((int)properties.percentage);
				}
			}

			@Override
			public void onFailed(String message, int code) {
				Log.e(Application.APP_NAME, "failed: " + message);
			}
		});
		propertiesTask.run(1);
		
		PlayerGetCurrentTask task = new PlayerGetCurrentTask();
		task.addListener(new HttpTaskListener<MovieModel>() {

			@Override
			public void onSuccess(MovieModel movie) {
				Log.e(Application.APP_NAME, "success");
				if (movie != null && lbPlayer != null) {
					lbPlayer.setText(movie.getTitle());
				}
			}

			@Override
			public void onFailed(String message, int code) {
				Log.e(Application.APP_NAME, "failed: " + message);
				if (lbPlayer != null) {
					lbPlayer.setText("");
				}
			}
		});
		task.run(1);

		mToast = Toast.makeText(Application.getContext(), "", Toast.LENGTH_SHORT);
	}
	
	private void seek() {
    	mIsSeek = true;

		WindowManager wm = (WindowManager)Application.getContext().getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		final int screenWidth = size.x;
		
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				int diff = mPosX - mStartPosX;
				Log.i(Application.APP_NAME, "posx: " + mPosX);
				Log.i(Application.APP_NAME, "startpos: " + mStartPosX);
				Log.i(Application.APP_NAME, "diff: " + diff);
				
				if (diff != 0) {
					if (mProperties != null) {
						double ratio = ((double)diff / screenWidth) * 2.5;
						Log.i(Application.APP_NAME, "ratio: " + ratio);
						mProperties.percentage += ratio;
						int newTime = (int)(mProperties.totaltime * mProperties.percentage / 100);
						int diffTime = (int) (newTime - mStartTime);
						if (Math.abs(diffTime) > 1) {
							String prefix = (diffTime < 0 ? "- " : "+ ");
							diffTime = Math.abs(diffTime);
							if (diffTime < 60) {
								mToast.setText(prefix + diffTime + "sec");
							} else {
								mToast.setText(prefix + (diffTime / 60) + "min " + (diffTime % 60) + " sec");
							}
							mToast.show();
							ServiceManager.getPlayerService().setPosition(mProperties.percentage);
						}
					}
				}
				
				if (mIsSeek) {
					mHandler.postDelayed(this, 1000);
				}
			}
		};
		
		mHandler = new Handler();
		mHandler.postDelayed(runnable, 100);
	}

	@Override
	protected void onLayout (boolean changed, final int left, final int top, final int right, final int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		Log.d("onLayout", "onLayout: " + (right - left) + ", " + (bottom - top));
		
		if (changed) {
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event){ 
	        
	    int action = MotionEventCompat.getActionMasked(event);
	        
	    switch(action) {
	        case (MotionEvent.ACTION_DOWN) :
	            Log.d(Application.APP_NAME,"Action was DOWN");
	        	mStartPosX = (int)event.getRawX();
	            return true;
	        case (MotionEvent.ACTION_MOVE) :
	        	mPosX = (int)event.getRawX();
	        	if (mIsSeek == false) {
		        	mStartTime = mProperties.totaltime * mProperties.percentage / 100;
	        		seek();
	        	}
	            Log.d(Application.APP_NAME,"Action was MOVE");
	            return true;
	        case (MotionEvent.ACTION_UP) :
	        	mPosX = mStartPosX;
	        	mIsSeek = false;
	            Log.d(Application.APP_NAME,"Action was UP");
	            return true;
	        case (MotionEvent.ACTION_CANCEL) :
	            Log.d(Application.APP_NAME,"Action was CANCEL");
	            return true;
	        case (MotionEvent.ACTION_OUTSIDE) :
	            Log.d(Application.APP_NAME,"Movement occurred outside bounds of current screen element");
	            return true;      
	        default : 
	            return super.onTouchEvent(event);
	    }      
	}
}
