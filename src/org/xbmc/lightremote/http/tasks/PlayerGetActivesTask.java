package org.xbmc.lightremote.http.tasks;

import org.json.JSONException;
import org.json.JSONObject;
import org.xbmc.lightremote.App;
import org.xbmc.lightremote.http.HttpTask;
import org.xbmc.lightremote.http.IWebserviceTaskDelegate;

import android.util.Log;

public class PlayerGetActivesTask extends HttpTask {

	private int mId;
	
	public PlayerGetActivesTask(IWebserviceTaskDelegate delegate) {
		super(delegate, 0);
//		run(\"http://192.168.1.22/jsonrpc\", "{\"jsonrpc\": \"2.0\", \"method\": \"VideoLibrary.GetMovies\", \"params\": { \"filter\": {\"field\": \"playcount\", \"operator\": \"is\", \"value\": \"0\"}, \"limits\": { \"start\" : 0, \"end\": 75 }, \"properties\" : [\"art\", \"rating\", \"thumbnail\", \"playcount\", \"file\"], \"sort\": { \"order\": \"ascending\", \"method\": \"label\", \"ignorearticle\": true } }, \"id\": \"libMovies\"}");
		
		String cmd = "{\"jsonrpc\": \"2.0\", \"method\": \"Player.GetActivePlayers\", \"id\": 1}";
		run("http://192.168.1.22/jsonrpc", cmd);
	}
	
	@Override
	protected Boolean doInBackground(String... params) {
		if (super.doInBackground(params)) {
			
			try {
				mId = (new JSONObject(mJson)).getJSONArray("result").getJSONObject(0).getInt("playerid");
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
