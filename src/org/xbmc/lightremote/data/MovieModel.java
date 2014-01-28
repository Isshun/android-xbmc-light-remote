package org.xbmc.lightremote.data;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieModel implements Parcelable  {
	
	private String 			mMediaFile;
	private String 			mLabel;
	private int 			mDuration;
	private int 			movieId;
	private int 			mPlayCount;
	private double 			mRating;
	private String 			mThumbnail;
	private String 			mToster;
	private String 			mFanart;
	private String 			mPlot;
	private String 			mTitle;
	private String 			mWriter;
	private String 			mDirector;
	private List<String>	mGenres;
	private String			mFormatedGenre;

	public static Parcelable.Creator<MovieModel> getCreator() {
		return CREATOR;
	}

	private MovieModel() {
		mGenres = new ArrayList<String>();
	}
	
	public static MovieModel fromJSON(JSONObject obj) throws JSONException {
		MovieModel m = new MovieModel();
		
		// Id
		m.movieId = getInt(obj, "movieid");
		if (m.movieId == 0) {
			m.movieId = getInt(obj, "id");
		}

		// Properties
		m.mTitle= getString(obj, "title");
		m.mLabel = getString(obj, "label");
		m.mThumbnail = cleanUrl(getString(obj, "thumbnail"));
		m.mPlot = getString(obj, "plot");
		m.mDirector = getString(obj, "director");
		m.mWriter = getString(obj, "writer");
		m.mPlayCount = getInt(obj, "playcount");
		if (obj.has("rating")) {
			m.mRating = (double)((int)(obj.getDouble("rating") * 10)) / 10;
		}
		m.mMediaFile = getString(obj, "file");

		if (obj.has("genre")) {
			JSONArray array = obj.getJSONArray("genre");
			for (int i = 0; i < array.length(); i++) {
				String genre = array.getString(i);
				m.mGenres.add(genre);
			}
		}
		
		if (obj.has("art")) {
			JSONObject art = obj.getJSONObject("art");
			m.mToster = cleanUrl(getString(art, "poster"));
			m.mFanart = cleanUrl(getString(art, "fanart"));
		}
		
		// Streamdetails
		if (obj.has("streamdetails")) {
			JSONObject streamdetails = obj.getJSONObject("streamdetails");
			if (streamdetails.has("video") && streamdetails.getJSONArray("video").length() > 0) {
				JSONObject video = streamdetails.getJSONArray("video").getJSONObject(0);

				// Duration
				if (video.has("duration"))
					m.mDuration = video.getInt("duration");
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
		try {
			return "http://192.168.1.22/vfs/" + URLEncoder.encode(url, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		return null;

		
//		if (url != null) {
//			url = url.replace("image://", "");
//			try {
//				url = URLDecoder.decode(url, "UTF-8");
//				return url;
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//			}
//		}
//		return null;
	}

	public MovieModel(Parcel in) {
		this.mMediaFile= in.readString();
		this.mLabel = in.readString();
//		this.mThumbnailPath = in.readString();
		this.mThumbnail = in.readString();
		this.mDuration = in.readInt();
		this.movieId = in.readInt();
		this.mPlayCount = in.readInt();
		this.mRating = in.readDouble();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mMediaFile);
		dest.writeString(mLabel);
//		dest.writeString(mThumbnailPath);
		dest.writeString(mThumbnail);
		dest.writeInt(mDuration);
		dest.writeInt(movieId);
		dest.writeInt(mPlayCount);
		dest.writeDouble(mRating);
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
		return mThumbnail;
	}

	public String getFanart() {
		return mFanart;
	}
	
	public String getFile() {
		return mMediaFile;
	}

	public String getLabel() {
		return mLabel;
	}

	public int getDuration() {
		return mDuration;
	}

	public int getMovieId() {
		return movieId;
	}

	public int getPlayCount() {
		return mPlayCount;
	}

	public double getRating() {
		return mRating;
	}

	public String getPoster() {
		return mToster;
	}

	public String getPlot() {
		return mPlot;
	}

	public String getTitle() {
		return mTitle;
	}

	public String getWriter() {
		return mWriter;
	}

	public String getDirector() {
		return mDirector;
	}

	public List<String> getGenres() {
		return mGenres;
	}

	public String getFormatedGenres() {
		if (mFormatedGenre == null) {
			StringBuilder sb = new StringBuilder();
			if (mGenres != null) {
				boolean first = true;
				for (String genre: mGenres) {
					if (!first) sb.append(" | ");
					sb.append(genre);
					first = false;
				}
				mFormatedGenre = sb.toString();
			}
		}
		return mFormatedGenre;
	}

	public String getFormatedDuration() {
		int duration = mDuration / 60;
		int min = (int)(duration % 60);
		if (duration > 60) {
			return (int)(duration / 60) + "h" + (min < 10 ? "0" : "") + min + "min";
		}
		return (min < 10 ? "0" : "") + min + "min";
	}

}
