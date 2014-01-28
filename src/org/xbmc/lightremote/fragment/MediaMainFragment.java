package org.xbmc.lightremote.fragment;

import org.xbmc.lightremote.R;
import org.xbmc.lightremote.data.MovieModel;
import org.xbmc.lightremote.http.HttpTask.HttpTaskListener;
import org.xbmc.lightremote.service.ImageService;
import org.xbmc.lightremote.service.ServiceManager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MediaMainFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		int movieId = getArguments().getInt("movie_id");
		final View rootView = inflater.inflate(R.layout.fragment_media_main, container, false);
		final TextView lbTitle = (TextView)rootView.findViewById(R.id.lb_title);
		final TextView lbSaga = (TextView)rootView.findViewById(R.id.lb_saga);
		final TextView lbDuration= (TextView)rootView.findViewById(R.id.lb_duration);
		final TextView lbDesc = (TextView)rootView.findViewById(R.id.lb_desc);
		final ImageView imgCover = (ImageView)rootView.findViewById(R.id.img_cover);
		final ImageView imgFan= (ImageView)rootView.findViewById(R.id.img_fan);


		ServiceManager.getLibraryService().getMovie(new HttpTaskListener<MovieModel>() {

			@Override
			public void onSuccess(MovieModel movie) {
				if (movie != null) {
					lbTitle.setText(movie.getTitle());
					lbSaga.setText(String.valueOf(movie.getRating()));
					lbDuration.setText(String.valueOf(movie.getDuration()));
					lbDesc.setText(movie.getPlot());
					ImageService.getInstance().showThumb(imgCover, movie.getThumbnail());
					ImageService.getInstance().showHeader(imgFan, movie.getFanart());
				}
			}

			@Override
			public void onFailed(String message, int code) {
				// TODO Auto-generated method stub
				
			}
		}, movieId);
		
		return rootView;
	}

}
