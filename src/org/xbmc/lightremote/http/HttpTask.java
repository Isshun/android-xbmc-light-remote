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
import org.json.JSONException;
import org.json.JSONObject;
import org.xbmc.lightremote.App;

public abstract class HttpTask extends AsyncTask<String, Integer, Boolean> {
	
	private DefaultHttpClient			mHttpClient;
	private String 						mErrorMessage;
	private int 						mId;
	private boolean 					mComplete;
	protected IWebserviceTaskDelegate 	mDelegate;
	protected JSONObject 				mJson;
	private int 						mStatusCode;
	
	public HttpTask(IWebserviceTaskDelegate delegate, int id) {
		mDelegate = delegate;
		mId = id;
		
		//Log.i(App.APP_NAME, "new WebserviceTask");
	}
	
	public IWebserviceTaskDelegate getDelegate() {
		return mDelegate;
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
		Log.d(App.APP_NAME, "New task: " + params[0]);
		
		String cmd = null;
		if (params.length == 3 && params[2] != null) {
			cmd = String.format("{\"jsonrpc\": \"2.0\", \"method\": \"%s\", \"id\": \"%s\", \"params\": %s}", params[0], params[1], params[2]);
		} else {
			cmd = String.format("{\"jsonrpc\": \"2.0\", \"method\": \"%s\", \"id\": \"%s\"}", params[0], params[1]);
		}
		
	    // Create a new HttpClient and Post Header
	    HttpClient httpclient = new DefaultHttpClient();
	    httpclient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
        HttpResponse response = null;

		HttpPost httppost = new HttpPost("http://192.168.1.22/jsonrpc");
		String name = "alex";
		String password = App.getAppContext().getSharedPreferences(App.APP_NAME, 0).getString("password", null);
		String auth = "Basic " + Base64.encodeToString((name + ':' + password).getBytes(), Base64.URL_SAFE | Base64.NO_WRAP);
        httppost.setHeader("Authorization", auth);
    	DefaultHttpClient client = new DefaultHttpClient();
    	
		try {
				StringEntity se = new StringEntity(cmd);
				se.setContentType("text/plain");
				httppost.setHeader("Content-Type","application/json");
				httppost.setEntity(se);
//			}
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
        
        // Get status code
    	mStatusCode = response.getStatusLine().getStatusCode();
    	Log.i(App.APP_NAME, String.format("code: %d", mStatusCode));

    	// Get content
	    StringBuilder sb = new StringBuilder();
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

		try {
			mJson = new JSONObject(sb.toString());

			// API error
			if (mJson.has("error")) {
				mErrorMessage = mJson.getJSONObject("error").getString("message");
				return false;
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}

        Log.v(App.APP_NAME, String.format("Response: %s", sb.toString()));
        
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

//    public void run(String url) {
//    	run(url, null);
//    }
    
	@TargetApi(11)
	public void run(String methode, String id) {
		if (android.os.Build.VERSION.SDK_INT >= 11)
			this.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, methode, id);		
		else
			this.execute(methode, id);
	}

	@TargetApi(11)
	public void run(String methode, String id, String params) {
		if (android.os.Build.VERSION.SDK_INT >= 11)
			this.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, methode, id, params);		
		else
			this.execute(methode, id, params);
	}
}
