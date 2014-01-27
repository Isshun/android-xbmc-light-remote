package org.xbmc.lightremote.http.tasks;

import java.util.Locale;

import org.xbmc.lightremote.http.HttpTask;
import org.xbmc.lightremote.http.IWebserviceTaskDelegate;

public class PlayerSetVolumeTask extends HttpTask<Boolean> {

	public PlayerSetVolumeTask(int volume) {
		super(0);

		String params = String.format(Locale.ENGLISH, "{ \"volume\": %d }", volume);
		run("Application.SetVolume", "SetVolume", params);
	}
	
	@Override
	protected Boolean doInBackground(String... params) {
		if (super.doInBackground(params)) {
			return true;
		}
		return false;
	}
	
}
