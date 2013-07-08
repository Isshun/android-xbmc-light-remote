package org.xbmc.lightremote.activities;

import java.io.File;

import org.xbmc.lightremote.App;
import org.xbmc.lightremote.R;
import org.xbmc.lightremote.data.Movie;
import org.xbmc.lightremote.fragments.LibraryMoviesFragment;
import org.xbmc.lightremote.fragments.PlayingFragment;
import org.xbmc.lightremote.fragments.PlayingGestureFragment;
import org.xbmc.lightremote.http.IServiceDelegate;
import org.xbmc.lightremote.http.services.PlayerService;

import com.androidquery.AQuery;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenListener;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
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

	private PlayerService 			mService;
	private Menu mMenu;

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
		menu.setOnOpenListener(new OnOpenListener() {
			@Override
			public void onOpen() {
				Log.d(App.APP_NAME, "Open menu");				
				mService.reqPlaying();
				mService.reqProperties();
			}
		});
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
		mService.reqPlaying();
		mService.reqProperties();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		mMenu = menu;
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
	        case R.id.menu_gestures:
	    		FragmentManager fragmentManager = getSupportFragmentManager();
	    		FragmentTransaction ft = fragmentManager.beginTransaction();
				ft.replace(R.id.layout_main, new PlayingGestureFragment());
				ft.commit();
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
			mMenu.clear();
			getMenuInflater().inflate(R.menu.playing, mMenu);
			break;
		case R.id.bt_library_movies:
		    ft.replace(R.id.layout_main, new LibraryMoviesFragment());
			break;
		case R.id.bt_library_series:
			break;
		}
		
		ft.commit();
		
		getSlidingMenu().showContent();
	}
//
//	
//	/**
//	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
//	 * one of the sections/tabs/pages.
//	 */
//	public class SectionsPagerAdapter extends FragmentPagerAdapter {
//
//		public SectionsPagerAdapter(FragmentManager fm) {
//			super(fm);
//		}
//
//		@Override
//		public Fragment getItem(int position) {
//			// getItem is called to instantiate the fragment for the given page.
//			// Return a DummySectionFragment (defined as a static inner class
//			// below) with the page number as its lone argument.
//			if (position == 0) {
//				return new LibraryMoviesFragment();
//			} else if (position == 1) {
//				return new PlayingFragment();
//			} else {
//				Fragment fragment = new DummySectionFragment();
//				Bundle args = new Bundle();
//				args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
//				fragment.setArguments(args);
//				return fragment;
//			}
//		}
//
//		@Override
//		public int getCount() {
//			// Show 3 total pages.
//			return 3;
//		}
//
//		@Override
//		public CharSequence getPageTitle(int position) {
//			switch (position) {
//			case 0:
//				return getString(R.string.title_library).toUpperCase();
//			case 1:
//				return getString(R.string.title_playing).toUpperCase();
//			case 2:
//				return getString(R.string.title_section3).toUpperCase();
//			}
//			return null;
//		}
//	}
//
//	/**
//	 * A dummy fragment representing a section of the app, but that simply
//	 * displays dummy text.
//	 */
//	public static class DummySectionFragment extends Fragment {
//		/**
//		 * The fragment argument representing the section number for this
//		 * fragment.
//		 */
//		public static final String ARG_SECTION_NUMBER = "section_number";
//
//		public DummySectionFragment() {
//		}
//
//		@Override
//		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//			// Create a new TextView and set its text to the fragment's section
//			// number argument value.
//			TextView textView = new TextView(getActivity());
//			textView.setGravity(Gravity.CENTER);
//			textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
//			return textView;
//		}
//	}

	@Override
	public void onActionStart(int action) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onActionError(int action, String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onActionCompleted(int action) {
		switch (action) {
			case PlayerService.GET_PLAYING:
				View view = getSlidingMenu().getMenu();
				Movie movie = mService.getPlaying();
				
				if (movie != null) {
					((TextView)view.findViewById(R.id.lb_playing_name)).setText(movie.label);

					// Thumbnail path 
					String thumbnailPath = movie.thumbnailPath; 
					if (movie.thumbnailPath == null) {
						for (Movie m: App.movies) {
							if (m.movieId == movie.movieId && m.thumbnailPath != null && new File(m.thumbnailPath).exists()) {
								thumbnailPath = m.thumbnailPath;
//						        AQuery aq = new AQuery(view);
//						        aq.id(R.id.img_playing).image(m.thumbnailPath, true, true, 120, 0, null, AQuery.FADE_IN);
							}
						}
					}
					if (thumbnailPath != null && new File(thumbnailPath).exists()) {
						ImageView img = (ImageView)view.findViewById(R.id.img_playing);
				        AQuery aq = new AQuery(view);
				        aq.id(img).image(thumbnailPath, true, true, 120, 0, null, AQuery.FADE_IN);
					}
	
					view.findViewById(R.id.view_playing).setVisibility(View.VISIBLE);
				} else {
					view.findViewById(R.id.view_playing).setVisibility(View.GONE);
				}
				break;
		}
	}

}
