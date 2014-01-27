package org.xbmc.lightremote.service;

import java.util.List;

import org.xbmc.lightremote.data.MovieModel;
import org.xbmc.lightremote.http.HttpTask;
import org.xbmc.lightremote.http.HttpTask.HttpTaskListener;
import org.xbmc.lightremote.http.tasks.LibraryGetMoviesTask;

public class LibraryServiceXBMC extends LibraryService {

	private List<MovieModel>	mMovies;
	
	@Override
	public void getMovies(HttpTaskListener<List<MovieModel>> listener) {
		if (mMovies != null) {
			listener.onSuccess(mMovies);
		} else {
			TaskManager taskManager = ServiceManager.getTaskManager();
			LibraryGetMoviesTask task = new LibraryGetMoviesTask();
			task.addListener(listener);
			taskManager.add(task);
		}
	}

	@Override
	public void getMovie(HttpTaskListener<MovieModel> listener) {
		listener.onFailed("not implemented", 0);
	}
}
