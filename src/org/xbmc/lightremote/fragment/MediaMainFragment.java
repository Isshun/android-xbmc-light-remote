package org.xbmc.lightremote.fragment;

import org.xbmc.lightremote.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MediaMainFragment extends Fragment {
	public MediaMainFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_media_main, container, false);

		return rootView;
	}
}
