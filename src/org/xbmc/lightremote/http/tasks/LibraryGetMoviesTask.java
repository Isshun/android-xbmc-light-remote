package org.xbmc.lightremote.http.tasks;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xbmc.lightremote.Application;
import org.xbmc.lightremote.data.MovieModel;
import org.xbmc.lightremote.http.HttpTask;
import org.xbmc.lightremote.http.IDownloadTaskDelegate;
import org.xbmc.lightremote.http.IWebserviceTaskDelegate;

public class LibraryGetMoviesTask extends HttpTask<List<MovieModel>> {
	
	public LibraryGetMoviesTask() {
		super(0);
		
		mStrategy = new HttpTaskStrategy<List<MovieModel>>() {
			@Override
			public List<MovieModel> execute(JSONObject obj) {
				List<MovieModel> movies = new ArrayList<MovieModel>();
			    String cacheDir = Application.getContext().getFilesDir().getAbsolutePath();
				
				try {
					JSONObject result = mJson.getJSONObject("result");
					JSONArray array = result.getJSONArray("movies");
					for (int i = 0; i < array.length(); i++) {
						JSONObject data = array.getJSONObject(i);
						
						MovieModel m = MovieModel.Create(data);
						
						// Thumbnail
						if (m.thumbnail != null) {
							int start = m.thumbnail.lastIndexOf("%2f");
							String filename = m.thumbnail.substring(start + 3, m.thumbnail.length() - 5);
							m.thumbnailPath = String.format("%s/%s.jpg", cacheDir, filename);
						}
						
						movies.add(m);
					}

					return movies;
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return null;
			}
		};
	}

	public void run() {
		String params = "{ \"properties\" : [\"art\", \"rating\", \"thumbnail\", \"playcount\", \"file\"], \"sort\": { \"order\": \"ascending\", \"method\": \"label\", \"ignorearticle\": true } }";
		run("VideoLibrary.GetMovies", "GetMovies", params);
	}
}
