package util;

import util.Log.LogType;

public class TimeCounter {

	private final String message;
	private final long startTime;

	public TimeCounter(String message) {
		this.message = message;
		this.startTime = System.currentTimeMillis();
		Log.info(LogType.TIME, message + ": Starting now.");
	}

	public void stop() {
		float elapsedTime = (System.currentTimeMillis() - startTime) / 1000F;
		Log.info(LogType.TIME, message + ": " + elapsedTime + " seconds.");
	}
}
