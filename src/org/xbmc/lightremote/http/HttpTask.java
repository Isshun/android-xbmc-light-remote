package org.xbmc.lightremote.http;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.annotation.TargetApi;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xbmc.lightremote.App;

public abstract class HttpTask extends AsyncTask<String, Integer, Boolean> {
	
	private DefaultHttpClient			mHttpClient;
	private String 						mErrorMessage;
	private int 						mId;
	private boolean 					mComplete;
	protected IWebserviceTaskDelegate 	mDelegate;
	protected String 					mJson;
	private int mStatusCode;
	
	public HttpTask(IWebserviceTaskDelegate delegate, int id) {
		mDelegate = delegate;
		mId = id;
		
		Log.i(App.APP_NAME, "new WebserviceTask");
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
	// TODO: Use HttpURLConnection instead DefaultHttpClient
	protected Boolean doInBackground(String... params) {
		Log.i(App.APP_NAME, "WebserviceTask: " + params[0]);
		
	    StringBuilder sb = new StringBuilder();

	    // Create a new HttpClient and Post Header
	    HttpClient httpclient = new DefaultHttpClient();
	    httpclient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
        HttpResponse response = null;

		HttpPost httppost = new HttpPost(params[0]);
		String name = "alex";
		String password = App.getAppContext().getSharedPreferences(App.APP_NAME, 0).getString("password", null);
		String auth = "Basic " + Base64.encodeToString((name + ':' + password).getBytes(), Base64.URL_SAFE | Base64.NO_WRAP);
        httppost.setHeader("Authorization", auth);
    	DefaultHttpClient client = new DefaultHttpClient();
    	
		try {
			if (params.length == 2 && params[1] != null) {
				StringEntity se = new StringEntity(params[1]);
				se.setContentType("text/plain");
				httppost.setHeader("Content-Type","application/json");
				httppost.setEntity(se);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

        try {
			response = client.execute(httppost);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	mStatusCode = response.getStatusLine().getStatusCode();
    	Log.i(App.APP_NAME, String.format("code: %d", mStatusCode));

		try {
			InputStream is = response.getEntity().getContent();
	        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"), 8);

			String line = null;
			while ((line = reader.readLine()) != null) {
			    sb.append(line + "n");
			}
			is.close();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

        mJson = sb.toString();
		Log.d(App.APP_NAME, String.format("JSON: %s", mJson));
        
		return true;
	}

    protected void onCancelled(Boolean result) {
    	Log.i(App.APP_NAME, "task canceled");
    	
    	if (mHttpClient != null)
        	mHttpClient.getConnectionManager().shutdown();

    	mHttpClient = null;
    }
    
    /**
     * After doInBackground has been completed, this method will be called, by UI thread
     */
    protected void onPostExecute(Boolean result) {
    	Log.i(App.APP_NAME, "task complete: " + result);

    	mComplete = true;
    	onTaskCompleted(result, mErrorMessage, mStatusCode);
    }

    public boolean isComplete() {
    	return mComplete;
    }
    
    /**
     * Helper method to notify the activity that this task was completed.
     */
    protected abstract void onTaskCompleted(Boolean result, String errorMessage, int statusCode);

    public void run(String url) {
    	run(url, null);
    }
    
	@TargetApi(11)
    public void run(String url, String login, String pwd) {
		if (android.os.Build.VERSION.SDK_INT >= 11)
			this.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url, login, pwd);		
		else
			this.execute(url, login, pwd);
    }
    
	@TargetApi(11)
	public void run(String url, String body) {
		if (android.os.Build.VERSION.SDK_INT >= 11)
			this.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url, body);		
		else
			this.execute(url, body);
	}
}
