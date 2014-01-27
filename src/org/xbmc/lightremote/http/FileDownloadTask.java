	package org.xbmc.lightremote.http;

	import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

	import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.crypto.spec.OAEPParameterSpec;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xbmc.lightremote.Application;

import android.content.Context;

	public class FileDownloadTask extends AsyncTask<String, Integer, Boolean> {
		private String					mErrorMessage;
		private HttpURLConnection 		mUrlConnection;
		private IDownloadTaskDelegate mDelegate;
		private String					mPath;
		private int 					mProgress;
	        
		public FileDownloadTask(IDownloadTaskDelegate iDownloadTaskDelegate, String path) {
			mDelegate = iDownloadTaskDelegate;
			
			Log.v("evencity", "New download");
		}
	        
		/**
		 * Android will invoke this method when this async task was started, or when user came back to our application.
		 */
		protected void onPreExecute() {
			super.onPreExecute();
	    }
	        
		/**
		 * Will be invoked when calling execute(). Everything the task need to do, will be implement here
		 */
		protected Boolean doInBackground(String... params) {
			
			Log.i(Application.APP_NAME, "DownloadTask: " + params[0]);
			
			FileOutputStream out = null;

			String tmpPath = params[1] + ".tmp";

			
			try {
				String url = "http://192.168.1.22/vfs/" + URLEncoder.encode(params[0], "utf-8");
				String auth = "Basic " + Base64.encodeToString(("alex" + ':' + "Schrodinger").getBytes(), Base64.URL_SAFE | Base64.NO_WRAP);
				
	 		   	URLConnection cn = new URL(url).openConnection();
	 		   	cn.addRequestProperty("Authorization", auth);
	 		   	cn.addRequestProperty("Content-Type","application/json");
	 		    cn.setDoOutput(true);
	 		   	
			    cn.connect();
			    cn.setConnectTimeout(Application.CONNECT_TIMEOUT);
			    cn.setReadTimeout(Application.IMAGE_READ_TIMEOUT);
			    InputStream stream = cn.getInputStream();

			    File downloadingMediaFile = new File(tmpPath);
			    
			    out = new FileOutputStream(downloadingMediaFile);
			    byte buf[] = new byte[16384];
			    do {
			        int numread = stream.read(buf);
			        if (numread <= 0) break;   
			        out.write(buf, 0, numread);
					publishProgress(++mProgress, 100);
			    } while (true);
				
			} catch (Exception e) {
				e.printStackTrace();
				mErrorMessage = e.getMessage();
				return false;
			} finally {
				try {
					if (out != null)
						out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			mPath = tmpPath;
			
	        return true;
		}

	    /**
	     * This method will be invoke be UI thread. The purpose is to update UI
	     */
	    protected void onProgressUpdate(Integer... args) {
//	    	Log.d(App.APP_NAME, "progress: " + args[0]);
	    	mDelegate.onProgress(args[0]);
	    }
	        
	    protected void onCancelled() {
	    	mUrlConnection.disconnect();
	    }
	    
	    /**
	     * After doInBackground has been completed, this method will be called, by UI thread
	     */
	    protected void onPostExecute(Boolean result) {
	    	notifyActivityTaskCompleted(result);
	    }
	    
	    /**
	     * Helper method to notify the activity that this task was completed.
	     */
	    private void notifyActivityTaskCompleted(Boolean result) {
	    	if (result) {
	    		// Move .tmp file to real location
	    		String finalPath = mPath.substring(0, mPath.length() - 4);
//	    		Log.e(App.APP_NAME, "move from: " + mPath);
//	    		Log.e(App.APP_NAME, "move to: " + finalPath);
	    		(new File(mPath)).renameTo(new File(finalPath));

	    		mDelegate.onTaskCompleted(null, finalPath);
	    	} else {
	    		mDelegate.onTaskError(null, null, 0);
	    	}
	    }
	}
