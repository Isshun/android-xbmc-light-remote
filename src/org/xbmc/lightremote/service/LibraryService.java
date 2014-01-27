package org.xbmc.lightremote.service;

import java.io.File;
import java.util.List;

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

	public abstract void getMovies(HttpTaskListener<List<MovieModel>> listener);
	public abstract void getMovie(HttpTaskListener<MovieModel> listener);
	
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
