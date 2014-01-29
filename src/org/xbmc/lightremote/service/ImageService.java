package org.xbmc.lightremote.service;

import java.util.List;

import org.xbmc.lightremote.Application;
import org.xbmc.lightremote.R;
import org.xbmc.lightremote.data.MovieModel;
import org.xbmc.lightremote.http.FileDownloadTask;
import org.xbmc.lightremote.http.HttpTask;
import org.xbmc.lightremote.http.IDownloadTaskDelegate;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Picasso.Listener;
import com.squareup.picasso.Picasso.Builder;

import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

public class ImageService {
	
	private static ImageService sSelf;
	private Picasso mPicasso;

	private ImageService() {
		Builder builder = new Picasso.Builder(Application.getContext());
		builder.listener(new Listener() {
			
			@Override
			public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
				Log.d(Application.APP_NAME, "ImageService: load " + exception.getMessage());
			}
		});
		
		mPicasso = builder.build();
		mPicasso.setDebugging(true);
	}
	
	public static ImageService getInstance() {
		if (sSelf == null) {
			sSelf = new ImageService();
		}
		return sSelf;
	}

	public void showThumb(ImageView image, String url) {
		Log.d(Application.APP_NAME, "ImageService: load " + url);

		mPicasso.load(url)
        .resize(240, 320)
        .placeholder(R.drawable.placeholder)
        .into(image);
	}

	public void showHeader(ImageView image, String url) {
		WindowManager wm = (WindowManager)Application.getContext().getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		final int width = size.x;
		final int height = (int) Application.getContext().getResources().getDimension(R.dimen.media_header_image_height);
		
		mPicasso
        .load(url)
        .resize(width, height)
        .into(image);
	}

	public void showFullscreen(ImageView image, String url) {
		mPicasso
        .load(url)
        .into(image);
	}

	public void init(List<MovieModel> movies) {
		Log.d(Application.APP_NAME, "ImageService: init");
		
		for (MovieModel movie: movies) {
			mPicasso.load(movie.getThumbnail()).fetch();
////			String filename = "thumb_" + movie.getMovieId() + ".jpg";
//			String filename = Base64.encodeToString(movie.getThumbnail().getBytes(), Base64.DEFAULT);
//			File path = new File(Application.getContext().getExternalCacheDir(), filename);
//			if (!path.exists()) {
//				download(movie.getThumbnail(), path.getAbsolutePath());
//			} else {
//				Log.d(Application.APP_NAME, "ImageService: path " + path + " exists");
//			}
		}
	}

	private void download(String url, String path) {
		Log.d(Application.APP_NAME, "ImageService: download to" + path);

		FileDownloadTask task = new FileDownloadTask(new IDownloadTaskDelegate() {
			
			@Override
			public void onTaskError(HttpTask task, String message, int statusCode) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onTaskCompleted(HttpTask task, Object data) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgress(int progress) {
				// TODO Auto-generated method stub
				
			}
		}, null);
		
		task.execute(url, path);
	}

}
