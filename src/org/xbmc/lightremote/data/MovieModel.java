package org.xbmc.lightremote.data;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieModel implements Parcelable  {
	
	private String 	file;
	private String 	label;
	private String 	thumbnailPath;
	private int 	duration;
	private int 	movieId;

	public static Parcelable.Creator<MovieModel> getCreator() {
		return CREATOR;
	}

	private int 	playCount;
	private double 	rating;
	private String 	thumbnail;
	private String 	poster;
	private String 	fanart;
	private String 	plot;
	private String 	title;
	private String 	writer;
	private String 	director;

	public static MovieModel fromJSON(JSONObject obj) throws JSONException {
		MovieModel m = new MovieModel();
		
		// Id
		m.movieId = getInt(obj, "movieid");
		if (m.movieId == 0) {
			m.movieId = getInt(obj, "id");
		}

		// Properties
		m.title= getString(obj, "title");
		m.label = getString(obj, "label");
		m.thumbnail = cleanUrl(getString(obj, "thumbnail"));
		m.plot = getString(obj, "plot");
		m.director= getString(obj, "director");
		m.writer= getString(obj, "writer");
		m.playCount = getInt(obj, "playcount");
		if (obj.has("rating")) {
			m.rating = obj.getDouble("rating");
		}
		m.file = getString(obj, "file");
		
		if (obj.has("art")) {
			JSONObject art = obj.getJSONObject("art");
			m.poster = cleanUrl(getString(art, "poster"));
			m.fanart = cleanUrl(getString(art, "fanart"));
		}
		
		// Streamdetails
		if (obj.has("streamdetails")) {
			JSONObject streamdetails = obj.getJSONObject("streamdetails");
			if (streamdetails.has("video") && streamdetails.getJSONArray("video").length() > 0) {
				JSONObject video = streamdetails.getJSONArray("video").getJSONObject(0);

				// Duration
				if (video.has("duration"))
					m.duration = video.getInt("duration");
			}
		}
		
		return m;
	}

	private static int getInt(JSONObject obj, String key) {
		if (obj.has(key)) {
			try {
				return obj.getInt(key);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	private static String getString(JSONObject obj, String key) {
		if (obj.has(key)) {
			try {
				return obj.getString(key);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private static String cleanUrl(String url) {
		if (url != null) {
			url = url.replace("image://", "");
			try {
				url = URLDecoder.decode(url, "UTF-8");
				return url;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public MovieModel() {
	}
	
	public MovieModel(Parcel in) {
		this.file= in.readString();
		this.label = in.readString();
		this.thumbnailPath = in.readString();
		this.thumbnail = in.readString();
		this.duration = in.readInt();
		this.movieId = in.readInt();
		this.playCount = in.readInt();
		this.rating = in.readDouble();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(file);
		dest.writeString(label);
		dest.writeString(thumbnailPath);
		dest.writeString(thumbnail);
		dest.writeInt(duration);
		dest.writeInt(movieId);
		dest.writeInt(playCount);
		dest.writeDouble(rating);
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	public static final Parcelable.Creator<MovieModel> CREATOR = new Parcelable.Creator<MovieModel>()
	{
		@Override
		public MovieModel createFromParcel(Parcel source)
		{
			return new MovieModel(source);
		}

		@Override
		public MovieModel[] newArray(int size)
		{
			return new MovieModel[size];
		}
	};

	public String getThumbnail() {
		return thumbnail;
	}

	public String getFanart() {
		return fanart;
	}
	
	public String getFile() {
		return file;
	}

	public String getLabel() {
		return label;
	}

	public String getThumbnailPath() {
		return thumbnailPath;
	}

	public int getDuration() {
		return duration;
	}

	public int getMovieId() {
		return movieId;
	}

	public int getPlayCount() {
		return playCount;
	}

	public double getRating() {
		return rating;
	}

	public String getPoster() {
		return poster;
	}

	public String getPlot() {
		return plot;
	}

	public String getTitle() {
		return title;
	}

	public String getWriter() {
		return writer;
	}

	public String getDirector() {
		return director;
	}

}
