package org.xbmc.lightremote;

import java.util.ArrayList;
import java.util.List;
import org.xbmc.lightremote.data.Movie;

import android.app.Application;
import android.content.Context;

public class App extends Application {

	public final static String APP_NAME = "XMBCLightRemote";

	public static final int CONNECT_TIMEOUT = 6000;
	public static final int READ_TIMEOUT = 10000;
	public static final int IMAGE_READ_TIMEOUT = 10000;

	private static Context context;

	public static List<Movie> movies;

    public void onCreate() {
        super.onCreate();

        movies = new ArrayList<Movie>();
        
    	App.context = getApplicationContext();
    }
	
    public static Context getAppContext() {
    	return App.context;
    }
	
}
