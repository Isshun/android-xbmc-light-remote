package org.xbmc.lightremote.http.services;

import java.util.List;

import org.xbmc.lightremote.data.Movie;
import org.xbmc.lightremote.data.PlayingProperties;
import org.xbmc.lightremote.http.HttpTask;
import org.xbmc.lightremote.http.IDownloadTaskDelegate;
import org.xbmc.lightremote.http.IServiceDelegate;
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
	private IServiceDelegate mDelegate;
	private int mMoviesCount;

	private List<Movie> mMovies;
	private PlayingProperties mProperties;
	private Movie mPlaying;

	public PlayerService(IServiceDelegate delegate) {
		mDelegate = delegate;
	}

	public void SetCurrentPlayerId(int playerId) {
		mPlayerId = playerId;
	}

	private void reqPlayers(final IPlayerTask t) {
		new PlayerGetActivesTask(new IWebserviceTaskDelegate() {
			@Override
			public void onTaskError(HttpTask task, String message, int statusCode) {
				//delegate.onActionError(action, message);
			}
			
			@Override
			public void onTaskCompleted(HttpTask task, Object data) {
				mPlayerId = (Integer)data;
				t.run((Integer)data);
			}
		});
	}

	public void reqPlay() {
		mDelegate.onActionStart(ACTION_PLAY);
		new PlayerPlayTask(new IWebserviceTaskDelegate() {
			@Override
			public void onTaskError(HttpTask task, String message, int statusCode) {
			}
			
			@Override
			public void onTaskCompleted(HttpTask task, Object data) {
				mDelegate.onActionCompleted(ACTION_PLAY);
			}
		}, mPlayerId);
	}

	public void reqStop() {
		mDelegate.onActionStart(ACTION_STOP);
		new PlayerStopTask(new IWebserviceTaskDelegate() {
			@Override
			public void onTaskError(HttpTask task, String message, int statusCode) {
				mDelegate.onActionError(ACTION_STOP, message);
			}
			
			@Override
			public void onTaskCompleted(HttpTask task, Object data) {
				mDelegate.onActionCompleted(ACTION_STOP);
			}
		}, mPlayerId);
	}

	public void reqSeek(double percent) {
		mDelegate.onActionStart(ACTION_SEEK);
		new PlayerSeekTask(new IWebserviceTaskDelegate() {
			@Override
			public void onTaskError(HttpTask task, String message, int statusCode) {
				mDelegate.onActionError(ACTION_SEEK, message);
			}
			
			@Override
			public void onTaskCompleted(HttpTask task, Object data) {
				mDelegate.onActionCompleted(ACTION_SEEK);
			}
		}, mPlayerId, percent);
	}
	
	public void reqVolume(int volume) {
		mDelegate.onActionStart(ACTION_VOLUME);
		new PlayerSetVolumeTask(new IWebserviceTaskDelegate() {
			@Override
			public void onTaskError(HttpTask task, String message, int statusCode) {
				mDelegate.onActionError(ACTION_VOLUME, message);
			}
			
			@Override
			public void onTaskCompleted(HttpTask task, Object data) {
				mDelegate.onActionCompleted(ACTION_VOLUME);
			}
		}, volume);
	}

	public void reqOpen(String path) {
		mDelegate.onActionStart(ACTION_OPEN);
		new PlayerOpenTask(new IWebserviceTaskDelegate() {
			@Override
			public void onTaskError(HttpTask task, String message, int statusCode) {
				mDelegate.onActionError(ACTION_OPEN, message);
			}
			
			@Override
			public void onTaskCompleted(HttpTask task, Object data) {
				mDelegate.onActionCompleted(ACTION_OPEN);
			}
		}, path);
	}
	
	public void reqPlaying() {
		mDelegate.onActionStart(GET_PLAYING);
		IPlayerTask task = new PlayerGetCurrentTask(new IWebserviceTaskDelegate() {
			@Override
			public void onTaskError(HttpTask task, String message, int statusCode) {
				mDelegate.onActionError(GET_PLAYING, message);
			}
			
			@Override
			public void onTaskCompleted(HttpTask task, Object data) {
				mPlaying = (Movie)data;
				mDelegate.onActionCompleted(GET_PLAYING);
			}
		});
		reqPlayers(task);
	}

	public void reqProperties() {
		mDelegate.onActionStart(GET_PROPERTIES);
		IPlayerTask task = new PlayerGetPropertiesTask(new IWebserviceTaskDelegate() {
			@Override
			public void onTaskError(HttpTask task, String message, int statusCode) {
				mDelegate.onActionError(GET_PROPERTIES, message);
			}
			
			@Override
			public void onTaskCompleted(HttpTask task, Object data) {
				mProperties = (PlayingProperties)data;
				mDelegate.onActionCompleted(GET_PROPERTIES);
			}
		});
		reqPlayers(task);
	}

	public void reqMoviesLibrary() {
		mDelegate.onActionStart(GET_MOVIES);
		
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
				mDelegate.onActionError(GET_MOVIES, message);
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
				
				mDelegate.onActionCompleted(GET_MOVIES);
			}
    	};
    	
		
    	IWebserviceTaskDelegate delegate = new IWebserviceTaskDelegate() {

			@Override
			public void onTaskError(HttpTask task, String message, int statusCode) {
				mDelegate.onActionError(GET_MOVIES, message);
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onTaskCompleted(HttpTask task, Object data) {
				mMovies = (List<Movie>) data;
				mMoviesCount = mMovies.size();
				
				for (Movie m: (mMovies)) {
					if (m.thumbnailPath != null) {
						LibraryService service = new LibraryService();
						service.download(fileDelegate, m.thumbnail, m.thumbnailPath);
					}
				}
			}
    	};
    	
    	//mThumbnailProgress = 0;
    	LibraryGetMoviesTask task = new LibraryGetMoviesTask(delegate, fileDelegate);
    	task.run();
	}

	public List<Movie> getMovies() {
		return mMovies;
	}

	public Movie getPlaying() {
		return mPlaying;
	}

	public PlayingProperties getProperties() {
		return mProperties;
	}

}
