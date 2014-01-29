package org.xbmc.lightremote.activity;

import java.util.List;
import java.util.Locale;

import org.xbmc.lightremote.R;
import org.xbmc.lightremote.R.id;
import org.xbmc.lightremote.R.layout;
import org.xbmc.lightremote.R.menu;
import org.xbmc.lightremote.R.string;
import org.xbmc.lightremote.data.MovieModel;
import org.xbmc.lightremote.fragment.MediaMainFragment;
import org.xbmc.lightremote.http.HttpTask.HttpTaskListener;
import org.xbmc.lightremote.http.IServiceListener;
import org.xbmc.lightremote.service.ImageService;
import org.xbmc.lightremote.service.PlayerService;
import org.xbmc.lightremote.service.ServiceManager;

import com.viewpagerindicator.TabPageIndicator;
import com.viewpagerindicator.TitlePageIndicator;

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
import android.widget.ImageView;
import android.widget.TextView;

public class MediaActivity extends FragmentActivity {

	private SectionsPagerAdapter	mSectionsPagerAdapter;
	private ViewPager 				mViewPager;
	private List<MovieModel> 		mMovies;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_media);
		
		final int position = getIntent().getExtras().getInt("position", 0);
		final String genre = getIntent().getExtras().getString("genre");
		
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setPersistentDrawingCache(ViewGroup.PERSISTENT_ALL_CACHES);
		
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(mSectionsPagerAdapter);

//		TitlePageIndicator titleIndicator = (TitlePageIndicator)findViewById(R.id.indicator);
//		titleIndicator.setViewPager(mViewPager);
		
		ServiceManager.getLibraryService().getMovies(new HttpTaskListener<List<MovieModel>>() {

			@Override
			public void onSuccess(List<MovieModel> movies) {
				mMovies = movies;
				mViewPager.setCurrentItem(position);
				mSectionsPagerAdapter.notifyDataSetChanged();
			}

			@Override
			public void onFailed(String message, int code) {
				// TODO Auto-generated method stub
				
			}
		}, genre);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.media, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected (int featureId, MenuItem item) {
		int position = mViewPager.getCurrentItem();
		MovieModel model = mMovies.get(position);
		
		PlayerService.getInstance().reqOpen(model.getFile());
		
		return true;
	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			MediaMainFragment fragment = new MediaMainFragment();
			Bundle args = new Bundle();
			args.putInt("movie_id", mMovies.get(position).getMovieId());
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			return mMovies != null ? mMovies.size() : 0;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return "movie";
		}
	}

}
