package util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumSet;

/** This class provide a easy way to classify and filter debug message,
 *  as well as a common timestamped format to print info. */
public class Log {

	public enum LogType {
		APP, /* Application */
		IO, /* Import/Export */
		GUI, /* User interface */
		MODEL, /* 3D Morphable Model */
		FITTING, /* Fitting part */
		TIME /* Time counter */
	}

	public enum LogLevel {
		DEBUG, WARNING, ERROR, INFO
	}

	private static final EnumSet<LogType> types = EnumSet.allOf(LogType.class);
	private static final EnumSet<LogLevel> levels = EnumSet.allOf(LogLevel.class);

	/** Print a log message with the DEBUG LogLevel. */
	public static void debug(LogType type, String s) {
		print(type, LogLevel.DEBUG, s);
	}

	/** Print a log message with the WARNING LogLevel. */
	public static void warning(LogType type, String s) {
		print(type, LogLevel.WARNING, s);
	}

	/** Print a log message with the ERROR LogLevel. */
	public static void error(LogType type, String s) {
		print(type, LogLevel.ERROR, s);
	}

	/** Print a log message with the INFO LogLevel. */
	public static void info(LogType type, String s) {
		print(type, LogLevel.INFO, s);
	}

	/** Enable a LogType. */
	public static void enableType(LogType type) {
		types.add(type);
	}

	/** Disable a LogType. */
	public static void disableType(LogType type) {
		types.remove(type);
	}

	/** Enable a LogLevel. */
	public static void enableLevel(LogLevel level) {
		levels.add(level);
	}

	/** Disable a LogLevel */
	public static void disableLevel(LogLevel level) {
		levels.remove(level);
	}

	/** Actual private printing method. Use the public API instead. */
	private static void print(LogType type, LogLevel level, String s) {
		if (types.contains(type) && levels.contains(level)) {
			Date dNow = new Date();
			SimpleDateFormat ft = new SimpleDateFormat("H:m:s:S");
			System.out.println(ft.format(dNow) + " [" + type + "," + level
					+ "] " + s);
		}
	}
}
