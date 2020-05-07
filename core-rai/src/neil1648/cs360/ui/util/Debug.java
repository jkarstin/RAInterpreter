package neil1648.cs360.ui.util;

import neil1648.cs360.ui.ra.RAParser;
import neil1648.cs360.ui.ra.expr.RAExpression;
import neil1648.cs360.ui.sql.SQLQuery;

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
			System.out.print("[[ERROR]]:\t");
		default:
			System.out.println("[!!!]" + s + "[!!!]");
		case OFF:
			break;
		};
	}
	
	//Overloaded wrapper versions for object logging
	
	public static void log(RAExpression rae) {
		if (rae == null) return;
		Debug.log(rae.toString());
	}
	
	public static void logv(RAExpression rae) {
		if (rae == null) return;
		Debug.logv(rae.toString());
	}
	
	public static void log(SQLQuery sqlq) {
		if (sqlq == null) return;
		Debug.log(sqlq.toString());
	}
	
	public static void logv(SQLQuery sqlq) {
		if (sqlq == null) return;
		Debug.logv(sqlq.toString());
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
