package org.xbmc.lightremote;

import java.util.UUID;
import java.util.regex.Pattern;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.text.style.SuperscriptSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.Display;
import android.view.WindowManager;

public class App extends Application {

	public final static String APP_NAME = "XMBCLightRemote";

	public static final int CONNECT_TIMEOUT = 6000;
	public static final int READ_TIMEOUT = 10000;
	public static final int IMAGE_READ_TIMEOUT = 10000;

	private static Context context;

    public void onCreate() {
        super.onCreate();

    	App.context = getApplicationContext();
    }
	
    public static Context getAppContext() {
    	return App.context;
    }
	
}
