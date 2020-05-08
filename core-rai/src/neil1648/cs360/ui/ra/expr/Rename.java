package neil1648.cs360.ui.ra.expr;

public class Rename extends RAExpression {

	private String name;
	
	public Rename(String name) {
		super(EXPR_TYPE.RNAM);
		this.name = name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	@Override
	public void setTarget(RAExpression t) {
		this.target = t;
	}
	
	@Override
	public RAExpression getTarget() {
		return this.target;
	}
	
	@Override
	public String toString() {
		return super.toString() + "{name=" + this.name + " trgt=" + this.target + "}";
	}
	
}
