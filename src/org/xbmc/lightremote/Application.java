package org.xbmc.lightremote;

import org.xbmc.lightremote.service.CacheManager;

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
    }
	
    public static Context getContext() {
    	return sContext;
    }
	
}
