package org.xbmc.lightremote.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.xbmc.lightremote.data.MovieModel;
import org.xbmc.lightremote.http.FileDownloadTask;
import org.xbmc.lightremote.http.HttpTask;
import org.xbmc.lightremote.http.IDownloadTaskDelegate;
import org.xbmc.lightremote.http.HttpTask.HttpTaskListener;

public abstract class LibraryService {

//	public static interface OnGetMoviesListener {
//		void onGetMovies(List<MovieModel> movies);
//	}
//	
//	public static interface OnGetMovieListener {
//		void getMovies(MovieModel movie);
//	}
//	
	private FileDownloadTask mFileDownloadTask;
	protected Map<String, List<MovieModel>> mGenres;

	protected LibraryService() {
		mGenres = new HashMap<String, List<MovieModel>>();
		mGenres.put("", new ArrayList<MovieModel>());
	}
	
	public abstract void getMovies(HttpTaskListener<List<MovieModel>> listener);
	public abstract void getMovie(HttpTaskListener<MovieModel> listener, int movieId);
	public abstract void getMovies(HttpTaskListener<List<MovieModel>> httpTaskListener, String genre);

	public void addMovieToGenre(String genre, MovieModel m) {
		if (mGenres.containsKey(genre) == false) {
			mGenres.put(genre, new ArrayList<MovieModel>());
		}
		mGenres.get(genre).add(m);
	}

	public List<String> getGenres() {
		List<String> genres = new ArrayList<String>(mGenres.keySet());
		Collections.sort(genres);
		return genres;
	}

//	public void download(final IDownloadTaskDelegate mFileDelegate, String thumbnail, final String thumbnailPath) {
////		Log.i(App.APP_NAME, String.format("file download: %s", thumbnail));
//
////	    String cacheDir = App.getAppContext().getFilesDir().getAbsolutePath();
////		int start = path.lastIndexOf("%2f");
////		String filename = path.substring(start + 3, path.length() - 5);
////	    final String imagePath = String.format("%s/%s.jpg", cacheDir, filename);
//	    
//	    if(new File(thumbnailPath).exists()) {
//	    	mFileDelegate.onTaskCompleted(null, thumbnailPath);
//	    } else if (!new File(thumbnailPath + ".tmp").exists()) {
//			mFileDownloadTask = new FileDownloadTask(new IDownloadTaskDelegate() {
//				
//				@Override
//				public void onTaskError(HttpTask task, String message, int statusCode) {
//					mFileDelegate.onTaskError(task, message, statusCode);
//				}
//				
//				@Override
//				public void onTaskCompleted(HttpTask task, Object data) {
//					mFileDelegate.onTaskCompleted(task, thumbnailPath);
//				}
//
//				@Override
//				public void onProgress(int progress) {
//					mFileDelegate.onProgress(progress);
//				}
//			}, thumbnail);
//			mFileDownloadTask.execute(thumbnail, thumbnailPath);
//	    }
//	}
}
