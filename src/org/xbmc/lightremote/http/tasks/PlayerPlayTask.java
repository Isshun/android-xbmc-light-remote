package org.xbmc.lightremote.http.tasks;

import org.xbmc.lightremote.http.HttpTask;
import org.xbmc.lightremote.http.IWebserviceTaskDelegate;

public class PlayerPlayTask extends HttpTask<Boolean> {

	public PlayerPlayTask(int mPlayerId) {
		super(0);
		String params = String.format("{ \"playerid\": %d }", mPlayerId);
		run("Player.PlayPause", "PlayPause", params);
	}
	
	@Override
	protected Boolean doInBackground(String... params) {
		if (super.doInBackground(params)) {
			return true;
		}
		return false;
	}
	
}
