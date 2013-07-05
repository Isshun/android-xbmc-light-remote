package org.xbmc.lightremote.activities;

import org.xbmc.lightremote.R;
import org.xbmc.lightremote.R.id;
import org.xbmc.lightremote.R.layout;
import org.xbmc.lightremote.R.menu;
import org.xbmc.lightremote.R.string;
import org.xbmc.lightremote.fragments.LibraryMoviesFragment;
import org.xbmc.lightremote.fragments.PlayingFragment;
import org.xbmc.lightremote.http.HttpTask;
import org.xbmc.lightremote.http.TestDelegate;
import org.xbmc.lightremote.http.tasks.LibraryGetMoviesTask;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

public class BaseActivity extends SlidingFragmentActivity implements OnClickListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		SlidingMenu menu = getSlidingMenu();
        //menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
//        menu.setShadowWidthRes(R.dimen.shadow_width);
//        menu.setShadowDrawable(R.drawable.shadow);
//        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        //menu.setFadeDegree(0.35f);
        //menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        //menu.setMenu(R.layout.menu);
        menu.setBehindOffset(50);
        //menu.setAboveOffset(100);
        //menu.setBehindWidth(500);
        menu.setShadowWidth(30);
		menu.setBehindScrollScale(0.25f);
		
		setBehindContentView(R.layout.menu);
		
		findViewById(R.id.bt_playing).setOnClickListener(this);
		findViewById(R.id.bt_library_movies).setOnClickListener(this);
		findViewById(R.id.bt_library_series).setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		
		switch(v.getId()) {
		case R.id.bt_playing:
			intent = new Intent(this, PlayingActivity.class);
			break;
		case R.id.bt_library_movies:
			intent = new Intent(this, LibraryMoviesActivity.class);
			break;
		case R.id.bt_library_series:
			intent = new Intent(this, LibrarySeriesActivity.class);
			break;
		}
		
		startActivity(intent);
	}

}
