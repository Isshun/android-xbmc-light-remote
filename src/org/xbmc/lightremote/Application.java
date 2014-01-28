package org.xbmc.lightremote;

import java.util.List;

import org.xbmc.lightremote.data.MovieModel;
import org.xbmc.lightremote.http.HttpTask.HttpTaskListener;
import org.xbmc.lightremote.service.CacheManager;
import org.xbmc.lightremote.service.ImageService;
import org.xbmc.lightremote.service.ServiceManager;

import android.content.Context;

public class Application extends android.app.Application {

	public final static String APP_NAME = "XMBCLightRemote";

	public static final int CONNECT_TIMEOUT = 6000;
	public static final int READ_TIMEOUT = 10000;
	public static final int IMAGE_READ_TIMEOUT = 10000;

	private static Context sContext;

    public void onCreate() {
        super.onCreate();

        sContext = getApplicationContext();
        CacheManager.getInstance().setContext(sContext);
        
        ServiceManager.getLibraryService().getMovies(new HttpTaskListener<List<MovieModel>>() {

			@Override
			public void onSuccess(List<MovieModel> result) {
		        ImageService.getInstance().init(result);
			}

			@Override
			public void onFailed(String message, int code) {
				// TODO Auto-generated method stub
				
			}
		});
        
    }
	
    public static String getResString(int resId) {
    	return sContext.getString(resId);
    }
    
    public static Context getContext() {
    	return sContext;
    }
	
}
