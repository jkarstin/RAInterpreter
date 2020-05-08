package neil1648.cs360.ui.sql;

import neil1648.cs360.ui.util.ILoggable;

public abstract class SQLTable implements ILoggable {
	
	public static enum TABL_TYPE implements ILoggable {
		BASIC,
		QUERY_RESULT
	}
	
	protected TABL_TYPE type;
	protected String label;
	
	public SQLTable(TABL_TYPE t) {
		this.type = t;
	}
	
	public void setLabel(String l) {
		this.label = l;
	}
	
	public String getLabel() {
		return this.label;
	}
	
	@Override
	public String toString() {
		return this.type.toString() + "_TABL";
	}

}
