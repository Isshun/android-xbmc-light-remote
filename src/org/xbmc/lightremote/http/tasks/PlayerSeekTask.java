package org.xbmc.lightremote.http.tasks;

import java.util.Locale;

import org.xbmc.lightremote.http.HttpTask;
import org.xbmc.lightremote.http.IWebserviceTaskDelegate;

public class PlayerSeekTask extends HttpTask<Boolean> {

	private double mPercent;

	public PlayerSeekTask(double percent) {
		super(0);
		mPercent = percent;
	}
	
	public void run(int playerId) {
		String params = String.format(Locale.ENGLISH, "{ \"playerid\": %d, \"value\": %f }", playerId, mPercent);
		run("Player.Seek", "Seek", params);
	}
	
}
