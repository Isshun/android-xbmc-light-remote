package org.xbmc.lightremote.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Base64;

public class CacheManager {

	private static CacheManager sSelf;

	private Context mContext;

	public static CacheManager getInstance() {
		if (sSelf == null) {
			sSelf = new CacheManager();
		}
		return sSelf;
	}

	public boolean isCached(String url) {
		if (mContext == null)
			return false;
		
		File f = getFile(url);
		
		return f != null && f.exists();
	}

	public String get(String url) {
		if (mContext == null)
			return null;

		File f = getFile(url);

		if (f.exists()) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(f));
				StringBuilder sb = new StringBuilder();
				char[] buff = new char[4096];
				while (br.read(buff) != -1) {
					sb.append(buff);
				}
				br.close();
				return sb.toString();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
	public void set(String url, String data) {
		try {
			File f = getFile(url);
			FileWriter fw = new FileWriter(f);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(data);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setContext(Context context) {
		mContext = context;
	}
	
	public File getFile(String url) {
		return new File(mContext.getExternalCacheDir(), md5(url));
	}
	
	public static String md5(String s) {
	    MessageDigest digest;
	    try {
	        digest = MessageDigest.getInstance("MD5");
	        digest.update(s.getBytes(),0,s.length());
	        String hash = new BigInteger(1, digest.digest()).toString(16);
	        return hash;
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    }
	    return "";
	}

}
