package org.xbmc.lightremote.http.tasks;

import java.util.Locale;

import org.xbmc.lightremote.http.HttpTask;
import org.xbmc.lightremote.http.IWebserviceTaskDelegate;

public class PlayerSeekTask extends HttpTask {

	public PlayerSeekTask(IWebserviceTaskDelegate delegate, int mPlayerId, double percent) {
		super(delegate, 0);
		String params = String.format(Locale.ENGLISH, "{ \"playerid\": %d, \"value\": %f }", mPlayerId, percent);
		run("Player.Seek", "Seek", params);
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
