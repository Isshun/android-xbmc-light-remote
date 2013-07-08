package org.xbmc.lightremote.http;

public interface IServiceDelegate {
	void onActionStart(int action);
	void onActionError(int action, String message);
	void onActionCompleted(int action);
}
