package org.xbmc.lightremote.http;

public interface IWebserviceTaskDelegate {
	void onTaskError(HttpTask task, String message, int statusCode);
	void onTaskCompleted(HttpTask task, Object data);
}
