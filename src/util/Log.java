package util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumSet;

public class Log {
	public enum LogType {
		IO, /* Import/Export */
		GUI /* User interface */
	}

	public enum LogLevel {
		DEBUG, WARNING, ERROR, INFO
	}

	private static final EnumSet<LogType> types = EnumSet.allOf(LogType.class);
	private static final EnumSet<LogLevel> levels = EnumSet.allOf(LogLevel.class);

	public static void print(LogType type, LogLevel level, String s) {
		if (types.contains(type) && levels.contains(level)) {
			Date dNow = new Date();
			SimpleDateFormat ft = new SimpleDateFormat("H:m:s:S");
			System.out.println(ft.format(dNow) + " [" + type + "," + level
					+ "] " + s);
		}
	}

	public static void enableType(LogType type) {
		types.add(type);
	}

	public static void disableType(LogType type) {
		types.remove(type);
	}

	public static void enableLevel(LogLevel level) {
		levels.add(level);
	}

	public static void disableLevel(LogLevel level) {
		levels.remove(level);
	}
}
