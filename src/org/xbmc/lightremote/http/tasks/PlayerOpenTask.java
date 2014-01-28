package org.xbmc.lightremote.http.tasks;

import org.json.JSONObject;
import org.xbmc.lightremote.http.HttpTask;
import org.xbmc.lightremote.http.IWebserviceTaskDelegate;

public class PlayerOpenTask extends HttpTask<Boolean> {

	String mPath;
	
	public PlayerOpenTask(String path) {
		super(0);
		
		mPath = path;

		String params = String.format("{ \"item\": { \"file\": \"%s\" } }", mPath);
		run("Player.Open", "Open", params);
		
		mStrategy = new HttpTaskStrategy<Boolean>() {

			@Override
			public Boolean execute(JSONObject obj) {
				return true;
			}
		};
	}
	
}
