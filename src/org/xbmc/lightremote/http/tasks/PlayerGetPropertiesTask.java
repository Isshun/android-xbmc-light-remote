package org.xbmc.lightremote.http.tasks;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xbmc.lightremote.data.PlayingProperties;
import org.xbmc.lightremote.http.HttpTask;
import org.xbmc.lightremote.http.IWebserviceTaskDelegate;
import org.xbmc.lightremote.http.IPlayerTask;
import org.xbmc.lightremote.http.HttpTask.HttpTaskStrategy;

public class PlayerGetPropertiesTask extends HttpTask<PlayingProperties> implements IPlayerTask {

	private PlayingProperties mProperties;
	
	public PlayerGetPropertiesTask() {
		super(0);
		
		mStrategy = new HttpTaskStrategy<PlayingProperties>() {
			@Override
			public PlayingProperties execute(JSONObject obj) {
				try {
					return PlayingProperties.Create(mJson.getJSONObject("result"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return null;
			}
		};
	}
	
	public void run(int playerId) {
		if (playerId == 0) {
			onPostExecute(null);
		} else {
			String params = String.format("{ \"properties\": [\"percentage\", \"totaltime\", \"time\", \"position\"], \"playerid\": %d }", playerId);
			run("Player.GetProperties", "GetProperties", params);
		}
	}
	
}
