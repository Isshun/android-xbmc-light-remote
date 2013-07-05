package org.xbmc.lightremote.http.tasks;

import org.xbmc.lightremote.App;
import org.xbmc.lightremote.http.HttpTask;
import org.xbmc.lightremote.http.IWebserviceTaskDelegate;

import android.util.Log;

public class PlayerPlayTask extends HttpTask {

	public PlayerPlayTask(IWebserviceTaskDelegate delegate, int mPlayerId) {
		super(delegate, 0);
		String cmd = String.format("{\"jsonrpc\": \"2.0\", \"method\": \"Player.PlayPause\", \"params\": { \"playerid\": %d }, \"id\": 1}", mPlayerId);
		Log.d(App.APP_NAME, String.format("CMD: %s", cmd));
		run("http://192.168.1.22/jsonrpc", cmd);
	}
	
	@Override
	protected Boolean doInBackground(String... params) {
		if (super.doInBackground(params)) {
			return true;
		}
		return false;
	}
	
	@Override
	protected void onTaskCompleted(Boolean result, String errorMessage, int statusCode) {
		if (result) {
			mDelegate.onTaskCompleted(this, null);
		} else {
			mDelegate.onTaskError(this, errorMessage, statusCode);
		}
	}
}
