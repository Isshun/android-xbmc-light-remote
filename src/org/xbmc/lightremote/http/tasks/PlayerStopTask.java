package org.xbmc.lightremote.http.tasks;

import org.xbmc.lightremote.http.HttpTask;
import org.xbmc.lightremote.http.IWebserviceTaskDelegate;

public class PlayerStopTask extends HttpTask<Boolean> {

	public PlayerStopTask(int mPlayerId) {
		super(0);
		
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
	
}
