package org.xbmc.lightremote.http.tasks;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xbmc.lightremote.Application;
import org.xbmc.lightremote.R;
import org.xbmc.lightremote.data.MovieModel;
import org.xbmc.lightremote.http.HttpTask;
import org.xbmc.lightremote.http.IDownloadTaskDelegate;
import org.xbmc.lightremote.http.IWebserviceTaskDelegate;
import org.xbmc.lightremote.service.CacheManager;
import org.xbmc.lightremote.service.LibraryService;
import org.xbmc.lightremote.service.ServiceManager;

import android.util.Log;

public class LibraryGetMoviesTask extends HttpTask<List<MovieModel>> {
	
	private String mProperties[] = {
			"title", 
		      "genre", 
		      "year", 
		      "rating", 
		      "director", 
		      "trailer", 
		      "tagline", 
		      "plot", 
		      "plotoutline", 
		      "originaltitle", 
		      "lastplayed", 
		      "playcount", 
		      "writer", 
		      "studio", 
		      "mpaa", 
		      "cast", 
		      "country", 
		      "imdbnumber", 
		      "runtime", 
		      "set", 
		      "showlink", 
		      "streamdetails", 
		      "top250", 
		      "votes", 
		      "fanart", 
		      "thumbnail", 
		      "file", 
		      "sorttitle", 
		      "resume", 
		      "setid", 
		      "dateadded", 
		      "tag", 
		      "art"
	};
	
	public LibraryGetMoviesTask() {
		super(0);
		
		mCache = CacheManager.getInstance();

		final LibraryService library = ServiceManager.getLibraryService();

		mStrategy = new HttpTaskStrategy<List<MovieModel>>() {
			@Override
			public List<MovieModel> execute(JSONObject obj) {
				List<MovieModel> movies = new ArrayList<MovieModel>();
				
				try {
					JSONObject result = obj.getJSONObject("result");
					JSONArray array = result.getJSONArray("movies");
					for (int i = 0; i < array.length(); i++) {
						JSONObject data = array.getJSONObject(i);
						
						MovieModel m = MovieModel.fromJSON(data);
						for (String genre: m.getGenres()) {
							library.addMovieToGenre(genre, m);
						}
						
//						// Thumbnail
//						if (m.getThumbnail() != null) {
//							int start = m.getThumbnail().lastIndexOf("%2f");
//							String filename = m.getThumbnail().substring(start + 3, m.getThumbnail().length() - 5);
//							m.thumbnailPath = String.format("%s/%s.jpg", cacheDir, filename);
//						}
						
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
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (String property: mProperties) {
			if (!first) sb.append(',');
			sb.append('"').append(property).append('"');
			first = false;
		}
		
		String params = "{ \"properties\" : [" + sb.toString() + "], \"sort\": { \"order\": \"ascending\", \"method\": \"label\", \"ignorearticle\": true } }";
		Log.e("params", "params" + params);
		run("VideoLibrary.GetMovies", "GetMovies", params);
	}
}
