package org.xbmc.lightremote.activity;

import java.io.File;

import org.xbmc.lightremote.Application;
import org.xbmc.lightremote.R;
import org.xbmc.lightremote.data.MovieModel;
import org.xbmc.lightremote.fragment.LibraryFilterFragment;
import org.xbmc.lightremote.fragment.LibraryFilterFragment.OnCategoryChangeListener;
import org.xbmc.lightremote.fragment.LibraryMoviesFragment;
import org.xbmc.lightremote.fragment.PlayingFragment;
import org.xbmc.lightremote.fragment.PlayingGestureFragment;
import org.xbmc.lightremote.http.HttpTask.HttpTaskListener;
import org.xbmc.lightremote.http.IServiceListener;
import org.xbmc.lightremote.http.tasks.PlayerGetCurrentTask;
import org.xbmc.lightremote.service.ImageService;
import org.xbmc.lightremote.service.PlayerService;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenListener;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class MainActivity extends SlidingFragmentActivity implements OnClickListener, IServiceListener {

	private PlayerService 			mService;
	private Menu mMenu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

    	mService = PlayerService.getInstance();
		mService.setListener(this);

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
				Log.d(Application.APP_NAME, "Open menu");				
				mService.reqPlaying();
				mService.reqProperties();
			}
		});
		setBehindContentView(R.layout.menu);
		findViewById(R.id.view_playing).setOnClickListener(this);
		findViewById(R.id.bt_library_movies).setOnClickListener(this);
		findViewById(R.id.bt_library_series).setOnClickListener(this);

		// Initialize first fragment
		final FragmentManager fragmentManager = getSupportFragmentManager();
		final FragmentTransaction ft = fragmentManager.beginTransaction();
		
		LibraryFilterFragment fragment = new LibraryFilterFragment();
		fragment.setOnCategoryChangeListener(new OnCategoryChangeListener() {

			@Override
			public void onCategoryChange(String category) {
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.layout_main, new LibraryMoviesFragment());
				fragmentTransaction.commit();
			}
			
		});
		ft.replace(R.id.layout_main, fragment);
		ft.commit();
		
		// Adapter
		SpinnerAdapter adapter = ArrayAdapter.createFromResource(this, R.array.actions, android.R.layout.simple_spinner_dropdown_item);

		// Callback
		OnNavigationListener callback = new OnNavigationListener() {
		    String[] items = getResources().getStringArray(R.array.actions); // List items from res

		    @Override
		    public boolean onNavigationItemSelected(int position, long id) {
		        Log.d("NavigationItemSelected", items[position]); // Debug
		        return true;
		    }

		};

		// Action Bar
		ActionBar actions = getActionBar();
		actions.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		actions.setDisplayShowTitleEnabled(false);
		actions.setListNavigationCallbacks(adapter, callback);
	}

	@Override
	public void onResume() {
		super.onResume();
//		mService.reqPlaying();
//		mService.reqProperties();
		
		// Fill player
		final TextView lbPlayer = (TextView)findViewById(R.id.lb_player_name);
		lbPlayer.setText("...");
		final ImageView btPlayer = (ImageView)findViewById(R.id.bt_player);
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
				MovieModel movie = mService.getPlaying();
				
				if (movie != null) {
					((TextView)view.findViewById(R.id.lb_playing_name)).setText(movie.getTitle());

//					// Thumbnail path 
//					String thumbnailPath = movie.thumbnailPath; 
//					if (movie.thumbnailPath == null) {
//						for (MovieModel m: Application.sMovies) {
//							if (m.movieId == movie.movieId && m.thumbnailPath != null && new File(m.thumbnailPath).exists()) {
//								thumbnailPath = m.thumbnailPath;
////						        AQuery aq = new AQuery(view);
////						        aq.id(R.id.img_playing).image(m.thumbnailPath, true, true, 120, 0, null, AQuery.FADE_IN);
//							}
//						}
//					}
//					if (thumbnailPath != null && new File(thumbnailPath).exists()) {
//						ImageView img = (ImageView)view.findViewById(R.id.img_playing);
//						ImageService.getInstance().showThumb(img, thumbnailPath);
//					}
	
					view.findViewById(R.id.view_playing).setVisibility(View.VISIBLE);
				} else {
					view.findViewById(R.id.view_playing).setVisibility(View.GONE);
				}
				break;
		}
	}

}
