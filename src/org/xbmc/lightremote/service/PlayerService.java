package org.xbmc.lightremote.service;

import java.util.List;

import org.xbmc.lightremote.data.MovieModel;
import org.xbmc.lightremote.data.PlayingProperties;
import org.xbmc.lightremote.http.HttpTask;
import org.xbmc.lightremote.http.HttpTask.HttpTaskListener;
import org.xbmc.lightremote.http.IDownloadTaskDelegate;
import org.xbmc.lightremote.http.IServiceListener;
import org.xbmc.lightremote.http.IWebserviceTaskDelegate;
import org.xbmc.lightremote.http.IPlayerTask;
import org.xbmc.lightremote.http.tasks.LibraryGetMoviesTask;
import org.xbmc.lightremote.http.tasks.PlayerGetActivesTask;
import org.xbmc.lightremote.http.tasks.PlayerGetCurrentTask;
import org.xbmc.lightremote.http.tasks.PlayerGetPropertiesTask;
import org.xbmc.lightremote.http.tasks.PlayerOpenTask;
import org.xbmc.lightremote.http.tasks.PlayerPlayTask;
import org.xbmc.lightremote.http.tasks.PlayerSeekTask;
import org.xbmc.lightremote.http.tasks.PlayerSetVolumeTask;
import org.xbmc.lightremote.http.tasks.PlayerStopTask;

public class PlayerService {

	public static final int ACTION_OPEN = 1;
	public static final int ACTION_PLAY = 2;
	public static final int ACTION_STOP = 3;
	public static final int ACTION_SEEK = 4;
	public static final int ACTION_VOLUME = 9;
	public static final int GET_PLAYERS = 5;
	public static final int GET_PLAYING = 6;
	public static final int GET_PROPERTIES = 7;
	public static final int GET_MOVIES = 8;
	
	private int mPlayerId;
	private IServiceListener mListener;
	private int mMoviesCount;

	private List<MovieModel> mMovies;
	private PlayingProperties mProperties;
	private MovieModel mPlaying;

	private static PlayerService	sThis = new PlayerService(); 
	
	public static PlayerService getInstance() {
		return sThis;
	}
	
	private PlayerService() {
		
	}
	
	public void SetCurrentPlayerId(int playerId) {
		mPlayerId = playerId;
	}

	private void reqPlayers(final IPlayerTask t) {
		PlayerGetActivesTask task = new PlayerGetActivesTask();
		task.addListener(new HttpTaskListener<Integer>() {

			@Override
			public void onSuccess(Integer result) {
				mPlayerId = result;
				t.run(result);
			}

			@Override
			public void onFailed(String message, int code) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	public void reqPlay() {
		mListener.onActionStart(ACTION_PLAY);
		PlayerPlayTask task = new PlayerPlayTask(mPlayerId);
		task.addListener(new HttpTaskListener<Boolean>() {

			@Override
			public void onSuccess(Boolean result) {
				mListener.onActionCompleted(ACTION_PLAY);
			}

			@Override
			public void onFailed(String message, int code) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	public void reqStop() {
		mListener.onActionStart(ACTION_STOP);
		PlayerStopTask task = new PlayerStopTask(mPlayerId);
		task.addListener(new HttpTaskListener<Boolean>() {

			@Override
			public void onSuccess(Boolean result) {
				mListener.onActionCompleted(ACTION_STOP);
			}

			@Override
			public void onFailed(String message, int code) {
				mListener.onActionError(ACTION_STOP, message);
			}
		});
	}

	public void reqSeek(double percent) {
		mListener.onActionStart(ACTION_SEEK);
		PlayerSeekTask task = new PlayerSeekTask(mPlayerId, percent);
		task.addListener(new HttpTaskListener<Boolean>() {

			@Override
			public void onSuccess(Boolean result) {
				mListener.onActionCompleted(ACTION_SEEK);
			}

			@Override
			public void onFailed(String message, int code) {
				mListener.onActionError(ACTION_SEEK, message);
			}
		});
	}
	
	public void reqVolume(int volume) {
		mListener.onActionStart(ACTION_VOLUME);
		PlayerSetVolumeTask task = new PlayerSetVolumeTask(volume);
		task.addListener(new HttpTaskListener<Boolean>() {

			@Override
			public void onSuccess(Boolean result) {
				mListener.onActionCompleted(ACTION_VOLUME);
			}

			@Override
			public void onFailed(String message, int code) {
				mListener.onActionError(ACTION_VOLUME, message);
			}
		});
		
	}

	public void reqOpen(String path) {
		mListener.onActionStart(ACTION_OPEN);
		PlayerOpenTask task = new PlayerOpenTask(path);
		task.addListener(new HttpTaskListener<Boolean>() {

			@Override
			public void onSuccess(Boolean result) {
				mListener.onActionCompleted(ACTION_OPEN);
			}

			@Override
			public void onFailed(String message, int code) {
				mListener.onActionError(ACTION_OPEN, message);
			}
		});
	}
	
	public void reqPlaying() {
		mListener.onActionStart(GET_PLAYING);
		PlayerGetCurrentTask task = new PlayerGetCurrentTask();
		task.addListener(new HttpTaskListener<MovieModel>() {

			@Override
			public void onSuccess(MovieModel result) {
				mPlaying = result;
				mListener.onActionCompleted(GET_PLAYING);
			}

			@Override
			public void onFailed(String message, int code) {
				mListener.onActionError(GET_PLAYING, message);
			}
		});
		reqPlayers(task);
	}

	public void reqProperties() {
		mListener.onActionStart(GET_PROPERTIES);
		PlayerGetPropertiesTask task = new PlayerGetPropertiesTask();
		task.addListener(new HttpTaskListener<PlayingProperties>() {

			@Override
			public void onSuccess(PlayingProperties result) {
				mProperties = result;
				mListener.onActionCompleted(GET_PROPERTIES);
			}

			@Override
			public void onFailed(String message, int code) {
				mListener.onActionError(GET_PROPERTIES, message);
			}
		});
		reqPlayers(task);
	}

	public void reqMoviesLibrary() {
		mListener.onActionStart(GET_MOVIES);
		
    	final IDownloadTaskDelegate fileDelegate = new IDownloadTaskDelegate() {

			@Override
			public void onProgress(int progress) {
			}
    		
			@Override
			public void onTaskError(HttpTask task, String message, int statusCode) {
//				if (++mThumbnailProgress == mMoviesCount) {
//					mBuilder.setContentText("Download complete").setProgress(0,0,false);
//					mNotifyManager.notify(0, mBuilder.build());
//				}
				mListener.onActionError(GET_MOVIES, message);
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onTaskCompleted(HttpTask task, Object data) {
				//Log.i(App.APP_NAME, String.format("Download complete: %s", data));
				
//				if (++mThumbnailProgress < mMoviesCount) {
//		            mBuilder.setProgress(mMoviesCount, mThumbnailProgress, false);
//		            mBuilder.setContentText(String.format("%d / %d", mThumbnailProgress, mMoviesCount));
//		            mNotifyManager.notify(0, mBuilder.build());
//				} else {
//					mBuilder.setContentText("Download complete").setProgress(0,0,false);
//					mNotifyManager.notify(0, mBuilder.build());
//					mNotifyManager.cancel(0);
//				}
				
				mListener.onActionCompleted(GET_MOVIES);
			}
    	};
    	
    	
    	//mThumbnailProgress = 0;
    	LibraryGetMoviesTask task = new LibraryGetMoviesTask();
    	task.addListener(new HttpTaskListener<List<MovieModel>>() {

			@Override
			public void onSuccess(List<MovieModel> result) {
				mMovies = result;
				mMoviesCount = mMovies.size();
				
//				for (Movie m: (mMovies)) {
//					if (m.thumbnailPath != null) {
//						LibraryService service = new LibraryService();
//						service.download(fileDelegate, m.thumbnail, m.thumbnailPath);
//					}
//				}
				mListener.onActionCompleted(GET_MOVIES);
			}

			@Override
			public void onFailed(String message, int code) {
				mListener.onActionError(GET_MOVIES, message);
			}
		});
    	task.run();
	}

	public List<MovieModel> getMovies() {
		return mMovies;
	}

	public MovieModel getPlaying() {
		return mPlaying;
	}

	public PlayingProperties getProperties() {
		return mProperties;
	}

	public void setListener(IServiceListener listener) {
		mListener = listener;
	}

}
