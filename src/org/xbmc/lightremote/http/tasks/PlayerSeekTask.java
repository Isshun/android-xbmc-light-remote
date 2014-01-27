package org.xbmc.lightremote.http.tasks;

import java.util.Locale;

import org.xbmc.lightremote.http.HttpTask;
import org.xbmc.lightremote.http.IWebserviceTaskDelegate;

public class PlayerSeekTask extends HttpTask<Boolean> {

	public PlayerSeekTask(int mPlayerId, double percent) {
		super(0);
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
	
}
