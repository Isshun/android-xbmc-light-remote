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
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

public class LibrarySeriesActivity extends BaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_library_series);
	}

}
