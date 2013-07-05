package org.xbmc.lightremote.http.tasks;

import org.xbmc.lightremote.http.HttpTask;
import org.xbmc.lightremote.http.IWebserviceTaskDelegate;

public class PlayerOpenTask extends HttpTask {

	String mPath;
	
	public PlayerOpenTask(IWebserviceTaskDelegate delegate, String path) {
		super(delegate, 0);
		
		mPath = path;

		String params = String.format("{ \"item\": { \"file\": \"%s\" } }", mPath);
		run("Player.Open", "Open", params);
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
		if (mDelegate == null) return;
		
		if (result) {
			mDelegate.onTaskCompleted(this, null);
		} else {
			mDelegate.onTaskError(this, errorMessage, statusCode);
		}
	}
}
