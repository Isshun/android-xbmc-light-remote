package org.xbmc.lightremote.http.tasks;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xbmc.lightremote.data.MovieModel;
import org.xbmc.lightremote.http.HttpTask;
import org.xbmc.lightremote.http.IWebserviceTaskDelegate;
import org.xbmc.lightremote.http.IPlayerTask;
import org.xbmc.lightremote.http.HttpTask.HttpTaskStrategy;

public class PlayerGetCurrentTask extends HttpTask<MovieModel> implements IPlayerTask {

	private MovieModel mMovie;
	
	public PlayerGetCurrentTask() {
		super(0);
		
		mStrategy = new HttpTaskStrategy<MovieModel>() {
			@Override
			public MovieModel execute(JSONObject obj) {
				try {
					return MovieModel.fromJSON(obj.getJSONObject("result").getJSONObject("item"));
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
			String params = String.format("{ \"properties\": [\"title\", \"album\", \"artist\", \"season\", \"episode\", \"duration\", \"showtitle\", \"tvshowid\", \"thumbnail\", \"file\", \"fanart\", \"streamdetails\"], \"playerid\": %d }", playerId);
			run("Player.GetItem", "GetItem", params);
		}
	}
	
}
