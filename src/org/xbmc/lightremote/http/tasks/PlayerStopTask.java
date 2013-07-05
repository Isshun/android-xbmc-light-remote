package org.xbmc.lightremote.http.tasks;

import org.xbmc.lightremote.http.HttpTask;
import org.xbmc.lightremote.http.IWebserviceTaskDelegate;

public class PlayerStopTask extends HttpTask {

	public PlayerStopTask(IWebserviceTaskDelegate delegate, int mPlayerId) {
		super(delegate, 0);
		
		String params = String.format("{ \"playerid\": %d }", mPlayerId);
		run("Player.Stop", "Player.Stop", params);
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
