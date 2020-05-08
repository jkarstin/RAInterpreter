package neil1648.cs360.ui.ra.expr;

import neil1648.cs360.ui.util.ILoggable;

public abstract class RAExpression implements ILoggable {
	
	public static enum EXPR_TYPE implements ILoggable {
		SMPL,
		PROJ,
		SLCT,
		RNAM,
		AGGR,
		JOIN,
	}
	
	protected EXPR_TYPE type;
	protected RAExpression target;
	
	public RAExpression(EXPR_TYPE t) {		
		this.type = t;
		this.target = null;
	}
	
	public EXPR_TYPE getType() {
		return this.type;
	}
	
	public void setTarget(String t) {
		this.target = new SimpleExpression(t);
	}
	
	public abstract void setTarget(RAExpression t);
	public abstract RAExpression getTarget();
	
	@Override
	public String toString() {
		return this.type.toString() + "_EXPR";
	}
	
}
