package org.xbmc.lightremote.http.tasks;

import org.json.JSONArray;
import org.json.JSONException;
import org.xbmc.lightremote.http.HttpTask;
import org.xbmc.lightremote.http.IWebserviceTaskDelegate;

public class PlayerGetActivesTask extends HttpTask {

	private int mId;
	
	public PlayerGetActivesTask(IWebserviceTaskDelegate delegate) {
		super(delegate, 0);

		run("Player.GetActivePlayers", "GetActivePlayers");
	}
	
	@Override
	protected Boolean doInBackground(String... params) {
		if (super.doInBackground(params)) {
			
			try {
				JSONArray results = mJson.getJSONArray("result");
				if (results.length() > 0)
					mId = results.getJSONObject(0).getInt("playerid");
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return true;
		}
		return false;
	}

	@Override
	protected void onTaskCompleted(Boolean result, String errorMessage, int statusCode) {
		if (result) {
			mDelegate.onTaskCompleted(this, mId);
		} else {
			mDelegate.onTaskError(this, errorMessage, statusCode);
		}
	}
}
