package neil1648.cs360.ui.sql;

import neil1648.cs360.ui.util.ILoggable;

public abstract class SQLTable implements ILoggable {
	
	public static enum TABL_TYPE implements ILoggable {
		BASIC,
		QUERY_RESULT,
		JOIN_RESULT
	}
	
	protected TABL_TYPE type;
	protected String label;
	private boolean labelIsSet;
	
	public SQLTable(TABL_TYPE t) {
		this.type = t;
		this.labelIsSet = false;
	}
	
	public void setLabel(String l) {
		this.label = l;
		this.labelIsSet = true;
	}
	
	public String getLabel() {
		return this.label;
	}
	
	public boolean hasLabel() {
		return this.labelIsSet;
	}
	
	@Override
	public String toString() {
		return this.type.toString() + "_TABL";
	}

}
