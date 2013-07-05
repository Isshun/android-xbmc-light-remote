package org.xbmc.lightremote.http.tasks;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xbmc.lightremote.App;
import org.xbmc.lightremote.data.Movie;
import org.xbmc.lightremote.http.HttpTask;
import org.xbmc.lightremote.http.IDownloadTaskDelegate;
import org.xbmc.lightremote.http.IWebserviceTaskDelegate;

public class LibraryGetMoviesTask extends HttpTask {

	private List<Movie> mList;

	public LibraryGetMoviesTask(IWebserviceTaskDelegate delegate, IDownloadTaskDelegate fileDelegate) {
		super(delegate, 0);
		
//		run("http://192.168.1.22/jsonrpc", "{\"jsonrpc\": \"2.0\", \"method\": \"VideoLibrary.GetMovies\", \"params\": { \"filter\": {\"field\": \"playcount\", \"operator\": \"is\", \"value\": \"0\"}, \"limits\": { \"start\" : 0, \"end\": 75 }, \"properties\" : [\"art\", \"rating\", \"thumbnail\", \"playcount\", \"file\"], \"sort\": { \"order\": \"ascending\", \"method\": \"label\", \"ignorearticle\": true } }, \"id\": \"libMovies\"}");
		run("http://192.168.1.22/jsonrpc", "{\"jsonrpc\": \"2.0\", \"method\": \"VideoLibrary.GetMovies\", \"id\": \"GetMovies\", \"params\": { \"properties\" : [\"art\", \"rating\", \"thumbnail\", \"playcount\", \"file\"], \"sort\": { \"order\": \"ascending\", \"method\": \"label\", \"ignorearticle\": true } }}");
	}
	
	@Override
	protected Boolean doInBackground(String... params) {
		if (super.doInBackground(params)) {

			mList = new ArrayList<Movie>();
			
		    String cacheDir = App.getAppContext().getFilesDir().getAbsolutePath();
			
			try {
				JSONObject response = new JSONObject(mJson);
				JSONObject result = response.getJSONObject("result");
				JSONArray movies = result.getJSONArray("movies");
				for (int i = 0; i < movies.length(); i++) {
					JSONObject data = movies.getJSONObject(i);
					
					Movie m = Movie.Create(data);
					
					// Thumbnail
					if (m.thumbnail != null) {
						int start = m.thumbnail.lastIndexOf("%2f");
						String filename = m.thumbnail.substring(start + 3, m.thumbnail.length() - 5);
						m.thumbnailPath = String.format("%s/%s.jpg", cacheDir, filename);
					}
					
					mList.add(m);
				}
				
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
			mDelegate.onTaskCompleted(this, mList);
		} else {
			mDelegate.onTaskError(this, errorMessage, statusCode);
		}
	}
}
