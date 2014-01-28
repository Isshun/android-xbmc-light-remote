package org.xbmc.lightremote.service;

import org.xbmc.lightremote.Application;
import org.xbmc.lightremote.R;

import com.squareup.picasso.Picasso;

import android.widget.ImageView;

public class ImageService {

	private static ImageService sSelf;

	public static ImageService getInstance() {
		if (sSelf == null) {
			sSelf = new ImageService();
		}
		return sSelf;
	}

	public void showThumb(ImageView image, String url) {
		Picasso.with(Application.getContext()).setDebugging(true);
		Picasso.with(Application.getContext())
        .load(url)
        .resize(240, 340)
        .placeholder(R.drawable.icon)
        .into(image);
	}

	public void showHeader(ImageView image, String url) {
		Picasso.with(Application.getContext()).setDebugging(true);
		Picasso.with(Application.getContext())
        .load(url)
        .resize(810, 540)
        .into(image);
	}

	public void showFullscreen(ImageView image, String url) {
		Picasso.with(Application.getContext()).setDebugging(true);
		Picasso.with(Application.getContext())
        .load(url)
        .into(image);
	}

}
