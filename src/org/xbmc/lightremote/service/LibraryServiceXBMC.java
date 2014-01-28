package org.xbmc.lightremote.service;

import java.util.List;

import org.xbmc.lightremote.data.MovieModel;
import org.xbmc.lightremote.http.HttpTask;
import org.xbmc.lightremote.http.HttpTask.HttpTaskListener;
import org.xbmc.lightremote.http.tasks.LibraryGetMoviesTask;

public class LibraryServiceXBMC extends LibraryService {

	private List<MovieModel>	mMovies;
	private LibraryGetMoviesTask mTask;
	
	@Override
	public void getMovies(HttpTaskListener<List<MovieModel>> listener) {
		if (mMovies != null) {
			listener.onSuccess(mMovies);
		} else if (mTask != null) {
			mTask.addListener(listener);
		} else {
			TaskManager taskManager = ServiceManager.getTaskManager();
			mTask = new LibraryGetMoviesTask();
			mTask.addListener(listener);
			mTask.addListener(new HttpTaskListener<List<MovieModel>>() {

				@Override
				public void onSuccess(List<MovieModel> result) {
					mMovies = result;
					mTask = null;
				}

				@Override
				public void onFailed(String message, int code) {
					// TODO Auto-generated method stub
					
				}
			});
			taskManager.add(mTask);
		}
	}

	@Override
	public void getMovie(final HttpTaskListener<MovieModel> listener, final int movieId) {
		if (mMovies != null) {
			listener.onSuccess(getMovie(mMovies, movieId));
		} else {
			getMovies(new HttpTaskListener<List<MovieModel>>() {

				@Override
				public void onSuccess(List<MovieModel> result) {
					listener.onSuccess(getMovie(result, movieId));
				}

				@Override
				public void onFailed(String message, int code) {
					listener.onFailed(message, code);
				}
			});
		}
	}

	private MovieModel getMovie(List<MovieModel> movies, int movieId) {
		for (MovieModel movie: movies) {
			if (movie.getMovieId() == movieId) {
				return movie;
			}
		}
		return null;
	}
}
