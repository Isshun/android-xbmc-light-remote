package org.xbmc.lightremote.http.tasks;

import org.xbmc.lightremote.http.HttpTask;
import org.xbmc.lightremote.http.IWebserviceTaskDelegate;

public class PlayerOpenTask extends HttpTask<Boolean> {

	String mPath;
	
	public PlayerOpenTask(String path) {
		super(0);
		
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
	
}
