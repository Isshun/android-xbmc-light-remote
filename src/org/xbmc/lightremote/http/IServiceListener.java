package org.xbmc.lightremote.http;

public interface IServiceListener {
	void onActionStart(int action);
	void onActionError(int action, String message);
	void onActionCompleted(int action);
}
