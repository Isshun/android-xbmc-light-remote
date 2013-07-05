package org.xbmc.lightremote.http;

public interface IDownloadTaskDelegate {
	void onTaskError(HttpTask task, String message, int statusCode);
	void onTaskCompleted(HttpTask task, Object data);
	void onProgress(int progress);
}
