package org.xbmc.lightremote.service;

import org.xbmc.lightremote.http.tasks.LibraryGetMoviesTask;

public class TaskManager {

	public void add(LibraryGetMoviesTask task) {
    	task.run();
	}

}
