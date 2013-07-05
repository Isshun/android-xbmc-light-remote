package org.xbmc.lightremote.http.tasks;

import org.json.JSONException;
import org.xbmc.lightremote.data.PlayingProperties;
import org.xbmc.lightremote.http.HttpTask;
import org.xbmc.lightremote.http.IWebserviceTaskDelegate;
import org.xbmc.lightremote.http.IPlayerTask;

public class PlayerGetPropertiesTask extends HttpTask implements IPlayerTask {

	private PlayingProperties mProperties;
	
	public PlayerGetPropertiesTask(IWebserviceTaskDelegate delegate) {
		super(delegate, 0);
	}
	
	public void run(int playerId) {
		if (playerId == 0) {
			mDelegate.onTaskCompleted(this, null);
			return;
		}

		String params = String.format("{ \"properties\": [\"percentage\", \"totaltime\", \"time\", \"position\"], \"playerid\": %d }", playerId);
		run("Player.GetProperties", "GetProperties", params);
	}
	
	@Override
	protected Boolean doInBackground(String... params) {
		if (super.doInBackground(params)) {
			
			try {
				mProperties = PlayingProperties.Create(mJson.getJSONObject("result"));
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
			mDelegate.onTaskCompleted(this, mProperties);
		} else {
			mDelegate.onTaskError(this, errorMessage, statusCode);
		}
	}
}
