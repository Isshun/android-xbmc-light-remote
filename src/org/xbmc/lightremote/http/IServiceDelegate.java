package org.xbmc.lightremote.http;

import org.xbmc.lightremote.data.Movie;
import org.xbmc.lightremote.data.PlayingProperties;

public interface IServiceDelegate {
	void onActionError(int action, String message);
	void onActionCompleted(int action);
	void onGetPlaying(Movie movie);
	void onGetProperties(PlayingProperties data);
}
