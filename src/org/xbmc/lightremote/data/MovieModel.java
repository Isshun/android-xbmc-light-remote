package org.xbmc.lightremote.data;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieModel implements Parcelable  {
	
	public String file;
	public String label;
	public String thumbnailPath;
	public int duration;
	public int movieId;
	public int playCount;
	public double rating;
	public String thumbnail;

	public static MovieModel Create(JSONObject obj) throws JSONException {
		MovieModel m = new MovieModel();
		
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
			if (streamdetails.has("video") && streamdetails.getJSONArray("video").length() > 0) {
				JSONObject video = streamdetails.getJSONArray("video").getJSONObject(0);

				// Duration
				if (video.has("duration"))
					m.duration = video.getInt("duration");
			}
		}
		
		return m;
	}

	public MovieModel() {
	}
	
	public MovieModel(Parcel in) {
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
}
