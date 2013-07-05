package org.xbmc.lightremote.http.tasks;

import org.json.JSONException;
import org.xbmc.lightremote.data.Movie;
import org.xbmc.lightremote.http.HttpTask;
import org.xbmc.lightremote.http.IWebserviceTaskDelegate;
import org.xbmc.lightremote.http.IPlayerTask;

public class PlayerGetCurrentTask extends HttpTask implements IPlayerTask {

	private Movie mMovie;
	
	public PlayerGetCurrentTask(IWebserviceTaskDelegate delegate) {
		super(delegate, 0);
	}
	
	public void run(int playerId) {
		if (playerId == 0) {
			mDelegate.onTaskCompleted(this, null);
			return;
		}

		String params = String.format("{ \"properties\": [\"title\", \"album\", \"artist\", \"season\", \"episode\", \"duration\", \"showtitle\", \"tvshowid\", \"thumbnail\", \"file\", \"fanart\", \"streamdetails\"], \"playerid\": %d }", playerId);
		run("Player.GetItem", "GetItem", params);
	}
	
	@Override
	protected Boolean doInBackground(String... params) {
		if (super.doInBackground(params)) {
			
			try {
				mMovie = Movie.Create(mJson.getJSONObject("result").getJSONObject("item"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			
//			{"id":"VideoGetItem","jsonrpc":"2.0","result":{"item":{"album":"","artist":[],"episode":-1,"fanart":"image://http%3a%2f%2fcf2.imgobject.com%2ft%2fp%2foriginal%2fsIpnn6MpNyKfBaWEnED063pmH9Z.jpg/","file":"smb://192.168.1.2/media/MOVIES/Star Trek/Star Trek 10 - Nemesis (2002).mp4","id":374,"label":"Star Trek 10 - Nemesis (2002).mp4","season":-1,"showtitle":"","streamdetails":{"audio":[{"channels":2,"codec":"aac","language":"und"}],"subtitle":[],"video":[{"aspect":2.3529410362243652344,"codec":"avc1","duration":6990,"height":544,"width":1280}]},"thumbnail":"image://http%3a%2f%2fcf2.imgobject.com%2ft%2fp%2foriginal%2fn4TpLWPi062AofIq4kwmaPNBSvA.jpg/","title":"Star Trek X : Nemesis","tvshowid":-1,"type":"movie"}}}n


			return true;
		}
		return false;
	}
	
	@Override
	protected void onTaskCompleted(Boolean result, String errorMessage, int statusCode) {
		if (result) {
			mDelegate.onTaskCompleted(this, mMovie);
		} else {
			mDelegate.onTaskError(this, errorMessage, statusCode);
		}
	}
}
