package org.xbmc.lightremote.fragment;

import org.xbmc.lightremote.Application;
import org.xbmc.lightremote.R;
import org.xbmc.lightremote.http.IServiceListener;
import org.xbmc.lightremote.service.PlayerService;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;

public class PlayingGestureFragment extends Fragment implements OnClickListener, IServiceListener, OnGestureListener, OnTouchListener {
	
	private PlayerService mService;
	private View mView;
    private GestureDetector mDetector;
	
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

    	// Inflate the layout for this fragment
    	mView = inflater.inflate(R.layout.fragment_playing_gesture, container, false);
    	
    	mView.findViewById(R.id.layout_gesture).setOnTouchListener(this);

    	mDetector = new GestureDetector(getActivity(), this);

    	mService = PlayerService.getInstance();
		mService.setListener(this);

        return mView; 
    }

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onClick(View v) {
	}

	@Override
	public void onActionError(int action, String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onActionCompleted(int action) {
	}

	@Override
	public void onActionStart(int action) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return true;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		//Log.w(App.APP_NAME, String.format("%f x %f", velocityX, velocityY));

		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

		Log.w(Application.APP_NAME, String.format("%f x %f", distanceX, distanceY));
		
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		Log.w(Application.APP_NAME, "TOUCH");
		
        return mDetector.onTouchEvent(event);
	}

}