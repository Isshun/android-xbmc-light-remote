package org.xbmc.lightremote.http.tasks;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xbmc.lightremote.App;
import org.xbmc.lightremote.data.Movie;
import org.xbmc.lightremote.http.HttpTask;
import org.xbmc.lightremote.http.IPlayerTask;
import org.xbmc.lightremote.http.IWebserviceTaskDelegate;

import android.util.Log;

public class PlayerOpenTask extends HttpTask {

	String mPath;
	
	public PlayerOpenTask(IWebserviceTaskDelegate delegate, String path) {
		super(delegate, 0);
		
		mPath = path;

		String cmd = String.format("{ \"jsonrpc\": \"2.0\", \"method\": \"Player.Open\", \"params\": { \"item\": { \"file\": \"%s\" } }}", mPath);
		run("http://192.168.1.22/jsonrpc", cmd);
	}
	
//	@Override
//	public void run(int playerId) {
//		String cmd = String.format("{ \"jsonrpc\": \"2.0\", \"method\": \"Player.Open\", \"params\": { \"item\": { \"file\": \"%s\" } }}", mPath);
//		run("http://192.168.1.22/jsonrpc", cmd);
//	}

	@Override
	protected Boolean doInBackground(String... params) {
		if (super.doInBackground(params)) {
			return true;
		}
		return false;
	}
	
	@Override
	protected void onTaskCompleted(Boolean result, String errorMessage, int statusCode) {
		if (mDelegate == null) return;
		
		if (result) {
			mDelegate.onTaskCompleted(this, null);
		} else {
			mDelegate.onTaskError(this, errorMessage, statusCode);
		}
	}
}
