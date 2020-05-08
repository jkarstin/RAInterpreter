package neil1648.cs360.ui.util;

import java.util.ArrayList;

import neil1648.cs360.ui.ra.RAParser;

public class Debug {
	
	public static enum DEBUG_MODE {
		OFF,
		ON,
		ON_VERBOSE
	}
	
	private static DEBUG_MODE MODE;
	
	public static void log(String s) {
		switch (Debug.MODE) {
		case ON_VERBOSE:
			System.out.print("[[DEBUG]]:\t");
		default:
			System.out.println(s);
		case OFF:
			break;
		};
	}
	
	public static void log() { Debug.log(""); }
	
	public static void logv(String s) {
		switch (Debug.MODE) {
		default:
			System.out.println("[[VERBOSE]]:\t" + s);
		case ON:
		case OFF:
			break;
		};
	}
	
	public static void logerr(String s) {
		switch (Debug.MODE) {
		case ON_VERBOSE:
			System.err.print("[[ERROR]]:\t");
		default:
			System.err.println("[!!!]" + s + "[!!!]");
		case OFF:
			break;
		};
	}
	
	//Overloaded wrapper versions for object logging
	
	public static void log(ILoggable l) {
		if (l == null) return;
		Debug.log(l.toString());
	}
	
	public static void log(ArrayList<? extends ILoggable> list) {
		if (list == null) return;
		Debug.log("ArrayList[" + list.size() + "] contents:");
		for (ILoggable elem : list) Debug.log(elem);
	}
	
	public static void logv(ILoggable l) {
		if (l == null) return;
		Debug.logv(l.toString());
	}
	
	public static void logv(ArrayList<? extends ILoggable> list) {
		if (list == null) return;
		Debug.logv("ArrayList[" + list.size() + "] contents:");
		for (ILoggable elem : list) Debug.logv(elem);
	}
	
	public static void logerr(ILoggable l) {
		if (l == null) return;
		Debug.logerr(l.toString());
	}
	
	public static void logerr(ArrayList<? extends ILoggable> list) {
		if (list == null) return;
		Debug.logerr("ArrayList[" + list.size() + "] contents:");
		for (ILoggable elem : list) Debug.logerr(elem);
	}
	
	public static void log(RAParser.STATUS state) {
		Debug.log(state.toString());
	}
	
	public static void logv(RAParser.STATUS state) {
		Debug.logv(state.toString());
	}
	
	public static void setMode(DEBUG_MODE m) {
		Debug.MODE = m;
	}
}
