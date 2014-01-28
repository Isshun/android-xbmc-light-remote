package org.xbmc.lightremote.http.tasks;

import java.util.Locale;

import org.xbmc.lightremote.http.HttpTask;

public class PlayerSetVolumeTask extends HttpTask<Boolean> {

	public static interface OnVolumeChangeListener {
		void onVolumeChange(int volume);
	}
	
	private int mVolume;
	private OnVolumeChangeListener mListener;

	public PlayerSetVolumeTask(int volume) {
		super(0);
		mVolume = volume;
	}

	public void run() {
		String params = String.format(Locale.ENGLISH, "{ \"volume\": %d }", mVolume);
		run("Application.SetVolume", "SetVolume", params);
	}
	
	public void setOnVolumeChangeListener(OnVolumeChangeListener listener) {
		mListener = listener;
	}
	
}
