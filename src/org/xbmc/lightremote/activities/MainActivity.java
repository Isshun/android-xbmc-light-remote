package org.xbmc.lightremote.activities;

import java.io.File;

import org.xbmc.lightremote.R;
import org.xbmc.lightremote.data.Movie;
import org.xbmc.lightremote.data.PlayingProperties;
import org.xbmc.lightremote.fragments.LibraryMoviesFragment;
import org.xbmc.lightremote.fragments.PlayingFragment;
import org.xbmc.lightremote.http.IServiceDelegate;
import org.xbmc.lightremote.http.services.PlayerService;

import com.androidquery.AQuery;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends SlidingFragmentActivity implements OnClickListener, IServiceDelegate {

	private SectionsPagerAdapter	mSectionsPagerAdapter;
	private ViewPager 				mViewPager;
	private PlayerService 			mService;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

    	mService = new PlayerService(this);
		
		// Configure ActionBar
		ActionBar bar = getActionBar();
		bar.setDisplayHomeAsUpEnabled(true);
		
		// Configure SlidingMenu
		SlidingMenu menu = getSlidingMenu();
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        menu.setBehindOffset(150);
        menu.setShadowWidth(30);
		menu.setBehindScrollScale(0.25f);
		setBehindContentView(R.layout.menu);
		findViewById(R.id.view_playing).setOnClickListener(this);
		findViewById(R.id.bt_library_movies).setOnClickListener(this);
		findViewById(R.id.bt_library_series).setOnClickListener(this);

		// Initialize first fragment
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction ft = fragmentManager.beginTransaction();
		ft.replace(R.id.layout_main, new LibraryMoviesFragment());
		ft.commit();
	}

	@Override
	public void onResume() {
		super.onResume();
		mService.getPlaying();
		mService.getProperties();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    	case android.R.id.home:
	    		getSlidingMenu().showMenu();
	    		return true;
	        case R.id.menu_settings:
	        	Intent intent = new Intent(this, SettingsActivity.class);
	        	startActivity(intent);
	        	return true;
	    }
		return false;
	}
	
	@Override
	public void onClick(View v) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction ft = fragmentManager.beginTransaction();
		
		switch(v.getId()) {
		case R.id.view_playing:
			ft.replace(R.id.layout_main, new PlayingFragment());
//			intent = new Intent(this, PlayingActivity.class);
			break;
		case R.id.bt_library_movies:
		      ft.replace(R.id.layout_main, new LibraryMoviesFragment());
//			intent = new Intent(this, LibraryMoviesActivity.class);
			break;
		case R.id.bt_library_series:
//			intent = new Intent(this, LibrarySeriesActivity.class);
			break;
		}
		
		ft.commit();
		
		getSlidingMenu().showContent();
	}

	
	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			if (position == 0) {
				return new LibraryMoviesFragment();
			} else if (position == 1) {
				return new PlayingFragment();
			} else {
				Fragment fragment = new DummySectionFragment();
				Bundle args = new Bundle();
				args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
				fragment.setArguments(args);
				return fragment;
			}
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return getString(R.string.title_library).toUpperCase();
			case 1:
				return getString(R.string.title_playing).toUpperCase();
			case 2:
				return getString(R.string.title_section3).toUpperCase();
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			// Create a new TextView and set its text to the fragment's section
			// number argument value.
			TextView textView = new TextView(getActivity());
			textView.setGravity(Gravity.CENTER);
			textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
			return textView;
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
	public void onGetPlaying(Movie movie) {
		View view = getSlidingMenu().getMenu();
		
		if (movie != null) {
			((TextView)view.findViewById(R.id.lb_playing_name)).setText(movie.label);
			if (movie.thumbnailPath != null && new File(movie.thumbnailPath).exists()) {
				ImageView img = (ImageView)view.findViewById(R.id.img_playing);
		        AQuery aq = new AQuery(view);
		        aq.id(img).image(movie.thumbnailPath, true, true, 120, 0, null, AQuery.FADE_IN);
			}
		} else {
			view.findViewById(R.id.view_playing).setVisibility(View.GONE);
		}
	}

	@Override
	public void onGetProperties(PlayingProperties data) {
		// TODO Auto-generated method stub
		
	}

}
