//package org.xbmc.lightremote.fragments;
//
//import org.xbmc.lightremote.R;
//import org.xbmc.lightremote.activities.MainActivity;
//
//import android.app.Activity;
//import android.app.Fragment;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//
//public class MenuFragment extends Fragment implements OnClickListener {
//
//	private Context 	mContext;
//
//	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		
//		mContext = inflater.getContext();
//		
//		View v = inflater.inflate(R.layout.fragment_menu, container, false);
//		
////		v.findViewById(R.id.btFavorites).setOnClickListener(this);
////		v.findViewById(R.id.btSettings).setOnClickListener(this);
////		v.findViewById(R.id.btInvite).setOnClickListener(this);
////		v.findViewById(R.id.btFeedback).setOnClickListener(this);
////		v.findViewById(R.id.btFollow).setOnClickListener(this);
////		v.findViewById(R.id.btHelp).setOnClickListener(this);
////		v.findViewById(R.id.btNotice).setOnClickListener(this);
////		v.findViewById(R.id.btPresentation).setOnClickListener(this);
////		v.findViewById(R.id.btConditions).setOnClickListener(this);
////		v.findViewById(R.id.btIssue).setOnClickListener(this);
//		
//		return v;
//	}
//
//	@Override
//	public void onClick(View v) {
//		
//		Intent intent = null;
////		
////		switch (v.getId())
////		{
////		case R.id.btFavorites:
////			intent = new Intent(mContext, FavoritesActivity.class);
////			break;
////		case R.id.btSettings:
////			intent = new Intent(mContext, WebviewActivity.class);
////			intent.putExtra("url", "http://evencity.fr/activity/v/509cec125e64661758d206c1");
////			break;
////		case R.id.btInvite:
////			intent = new Intent(mContext, WebviewActivity.class);
////			intent.putExtra("url", "http://evencity.fr");
////			break;
////		case R.id.btFeedback:
////			intent = new Intent(mContext, WebviewActivity.class);
////			intent.putExtra("url", "http://evencity.fr");
////			break;
////		case R.id.btFollow:
////			intent = new Intent(mContext, WebviewActivity.class);
////			intent.putExtra("url", "http://evencity.fr");
////			break;
////		case R.id.btHelp:
////			intent = new Intent(mContext, WebviewActivity.class);
////			intent.putExtra("url", "http://evencity.fr");
////			break;
////		case R.id.btNotice:
////			intent = new Intent(mContext, WebviewActivity.class);
////			intent.putExtra("url", "http://evencity.fr");
////			break;
////		case R.id.btPresentation:
////			intent = new Intent(mContext, WebviewActivity.class);
////			intent.putExtra("url", "http://evencity.fr/about");
////			break;
////		case R.id.btConditions:
////			intent = new Intent(mContext, WebviewActivity.class);
////			intent.putExtra("url", "http://evencity.fr/conditions");
////			break;
////		case R.id.btIssue:
////			intent = new Intent(mContext, WebviewActivity.class);
////			intent.putExtra("url", "http://evencity.fr");
////			break;
////		}
//
//		mContext.startActivity(intent);
//
//		// Close sliding menu if still open on resume
//		Handler mHandler = new Handler();
//        mHandler.postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				Activity a = getActivity();
//				if(a instanceof MainActivity) {
//				    ((MainActivity)a).closeMenu();
//				}
//			}
//		}, 250);
//	}
//}
