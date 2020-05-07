package neil1648.cs360.ui.ra.expr;

public class SimpleExpression extends RAExpression {
	
	private String value;
	
	public SimpleExpression(String val) {
		super(EXPR_TYPE.SMPL);
		this.value = val;
	}
	
	public void setValue(String val) {
		this.value = val;
	}
	
	public String getValue() {
		return this.value;
	}

	@Override
	public void setTarget(RAExpression t) { }

	@Override
	public RAExpression getTarget() { return null; }
	
	@Override
	public String toString() {
		return value;
	}

}
