package org.xbmc.lightremote.data;

import org.json.JSONException;
import org.json.JSONObject;

public class PlayingProperties {

	public int totaltime;
	public int time;
	public double position;
	public double percentage;

	public static PlayingProperties Create(JSONObject obj) throws JSONException {
		
		PlayingProperties pp = new PlayingProperties();
		
		if (obj.has("percentage"))
			pp.percentage = obj.getDouble("percentage");
		
		if (obj.has("position"))
			pp.position = obj.getDouble("position");
		
		if (obj.has("time")) {
			pp.time = obj.getJSONObject("time").getInt("seconds");
			pp.time += obj.getJSONObject("time").getInt("minutes") * 60;
			pp.time += obj.getJSONObject("time").getInt("hours") * 3600;
		}
		
		if (obj.has("totaltime")) {
			pp.totaltime = obj.getJSONObject("totaltime").getInt("seconds");
			pp.totaltime += obj.getJSONObject("totaltime").getInt("minutes") * 60;
			pp.totaltime += obj.getJSONObject("totaltime").getInt("hours") * 3600;
		}
		
		return pp;
	}

}
