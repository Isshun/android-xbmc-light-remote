package org.xbmc.lightremote.http.services;

import org.xbmc.lightremote.App;
import org.xbmc.lightremote.activities.DetailActivity;
import org.xbmc.lightremote.data.Movie;
import org.xbmc.lightremote.data.PlayingProperties;
import org.xbmc.lightremote.http.HttpTask;
import org.xbmc.lightremote.http.IServiceDelegate;
import org.xbmc.lightremote.http.IWebserviceTaskDelegate;
import org.xbmc.lightremote.http.IPlayerTask;
import org.xbmc.lightremote.http.tasks.PlayerGetActivesTask;
import org.xbmc.lightremote.http.tasks.PlayerGetCurrentTask;
import org.xbmc.lightremote.http.tasks.PlayerGetPropertiesTask;
import org.xbmc.lightremote.http.tasks.PlayerOpenTask;
import org.xbmc.lightremote.http.tasks.PlayerPlayTask;
import org.xbmc.lightremote.http.tasks.PlayerSeekTask;
import org.xbmc.lightremote.http.tasks.PlayerSetVolumeTask;
import org.xbmc.lightremote.http.tasks.PlayerStopTask;

import android.util.Log;

public class PlayerService implements IWebserviceTaskDelegate{

	public static final int ACTION_OPEN = 1;
	public static final int ACTION_PLAY = 2;
	public static final int ACTION_STOP = 3;
	public static final int ACTION_SEEK = 4;
	public static final int GET_PLAYERS = 5;
	public static final int GET_PLAYING = 6;
	public static final int GET_PROPERTIES = 7;
	
	private Movie mCurrentPlaying;
	private int mPlayerId;
	private IServiceDelegate mDelegate;

	public PlayerService(IServiceDelegate delegate) {
		mDelegate = delegate;
	}

	public void play() {
		new PlayerPlayTask(new IWebserviceTaskDelegate() {
			@Override
			public void onTaskError(HttpTask task, String message, int statusCode) {
			}
			
			@Override
			public void onTaskCompleted(HttpTask task, Object data) {
			}
		}, mPlayerId);
	}

	public void stop() {
		new PlayerStopTask(new IWebserviceTaskDelegate() {
			@Override
			public void onTaskError(HttpTask task, String message, int statusCode) {
			}
			
			@Override
			public void onTaskCompleted(HttpTask task, Object data) {
			}
		}, mPlayerId);
	}

	public void seek(double percent) {
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
	
	public void setVolume(int volume) {
		new PlayerSetVolumeTask(new IWebserviceTaskDelegate() {
			@Override
			public void onTaskError(HttpTask task, String message, int statusCode) {
				mDelegate.onActionError(ACTION_SEEK, message);
			}
			
			@Override
			public void onTaskCompleted(HttpTask task, Object data) {
				mDelegate.onActionCompleted(ACTION_SEEK);
			}
		}, volume);
	}
	
	public void SetCurrentPlayerId(int playerId) {
		mPlayerId = playerId;
	}

	public void open(String path) {
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

	private void getPlayers(final IPlayerTask t) {
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

	public void getPlaying() {
		IPlayerTask task = new PlayerGetCurrentTask(new IWebserviceTaskDelegate() {
			@Override
			public void onTaskError(HttpTask task, String message, int statusCode) {
				mDelegate.onActionError(GET_PLAYING, message);
			}
			
			@Override
			public void onTaskCompleted(HttpTask task, Object data) {
				mDelegate.onGetPlaying((Movie)data);
			}
		});
		getPlayers(task);
	}

	public void getProperties() {
		IPlayerTask task = new PlayerGetPropertiesTask(new IWebserviceTaskDelegate() {
			@Override
			public void onTaskError(HttpTask task, String message, int statusCode) {
				mDelegate.onActionError(GET_PROPERTIES, message);
			}
			
			@Override
			public void onTaskCompleted(HttpTask task, Object data) {
				mDelegate.onGetProperties((PlayingProperties)data);
			}
		});
		getPlayers(task);
	}

	@Override
	public void onTaskError(HttpTask task, String message, int statusCode) {
	}

	@Override
	public void onTaskCompleted(HttpTask task, Object data) {
	}

}
