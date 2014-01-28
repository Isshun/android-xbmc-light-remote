package org.xbmc.lightremote.http.tasks;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xbmc.lightremote.Application;
import org.xbmc.lightremote.data.MovieModel;
import org.xbmc.lightremote.http.HttpTask;
import org.xbmc.lightremote.http.IWebserviceTaskDelegate;
import org.xbmc.lightremote.http.HttpTask.HttpTaskStrategy;

public class PlayerGetActivesTask extends HttpTask<Integer> {

	public PlayerGetActivesTask() {
		super(0);

		mStrategy = new HttpTaskStrategy<Integer>() {
			@Override
			public Integer execute(JSONObject obj) {
				try {
					JSONArray results = obj.getJSONArray("result");
					if (results.length() > 0) {
						return results.getJSONObject(0).getInt("playerid");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return null;
			}
		};

		run("Player.GetActivePlayers", "GetActivePlayers");
	}
	
}
