package org.xbmc.lightremote.data;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable  {
	
	public String file;
	public String label;
	public String thumbnailPath;
	public int duration;
	public int movieId;
	public int playCount;
	public double rating;
	public String thumbnail;

	public static Movie Create(JSONObject obj) throws JSONException {
		Movie m = new Movie();
		
		// Label
		if (obj.has("title"))
			m.label = obj.getString("title");
		else if (obj.has("label"))
			m.label = obj.getString("label");

		// Thumbnail
		if (obj.has("thumbnail")) {
			m.thumbnail = obj.getString("thumbnail");
		}
		
		// Id
		if (obj.has("movieid"))
			m.movieId = obj.getInt("movieid");
		else if (obj.has("id"))
			m.movieId = obj.getInt("id");
		
		// Playcount
		if (obj.has("playcount"))
			m.playCount = obj.getInt("playcount");
		
		// Rating
		if (obj.has("rating"))
			m.rating = obj.getDouble("rating");
		
		//  File
		if (obj.has("file"))
			m.file = obj.getString("file");

		// Streamdetails
		if (obj.has("streamdetails")) {
			JSONObject streamdetails = obj.getJSONObject("streamdetails");
			if (streamdetails.has("video")) {
				JSONObject video = streamdetails.getJSONArray("video").getJSONObject(0);

				// Duration
				if (video.has("duration"))
					m.duration = video.getInt("duration");
			}
		}
		
		return m;
	}

	public Movie() {
	}
	
	public Movie(Parcel in) {
		this.file= in.readString();
		this.label = in.readString();
		this.thumbnailPath = in.readString();
		this.duration = in.readInt();
		this.movieId = in.readInt();
		this.playCount = in.readInt();
		this.rating = in.readDouble();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeString(file);
		dest.writeString(label);
		dest.writeString(thumbnailPath);
		dest.writeInt(duration);
		dest.writeInt(movieId);
		dest.writeInt(playCount);
		dest.writeDouble(rating);
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>()
	{
		@Override
		public Movie createFromParcel(Parcel source)
		{
			return new Movie(source);
		}

		@Override
		public Movie[] newArray(int size)
		{
			return new Movie[size];
		}
	};
}
