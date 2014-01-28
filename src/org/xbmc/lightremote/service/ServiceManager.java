package org.xbmc.lightremote.service;

public abstract class ServiceManager {

	private static LibraryService sLibraryService;
	private static PlayerService sPlayerService;
	private static TaskManager sTaskManager;
	
	public static LibraryService getLibraryService() {
		if (sLibraryService == null) {
			sLibraryService = new LibraryServiceXBMC();
		}
		return sLibraryService;
	}

	public static TaskManager getTaskManager() {
		if (sTaskManager == null) {
			sTaskManager = new TaskManager();
		}
		return sTaskManager;
	}

	public static PlayerService getPlayerService() {
		if (sPlayerService == null) {
			sPlayerService = new PlayerServiceXBMC();
		}
		return sPlayerService;
	}
	
}
