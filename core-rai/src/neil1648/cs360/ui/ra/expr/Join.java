package neil1648.cs360.ui.ra.expr;

import java.util.ArrayList;

public class Join extends RAExpression {

	private ArrayList<RAExpression> arguments;
	
	public Join(RAExpression arg0) {
		super(EXPR_TYPE.JOIN);
		this.arguments = new ArrayList<RAExpression>();
		this.arguments.add(arg0);
	}
	public Join(String arg0) { this(new SimpleExpression(arg0)); }
	
	public int addArgument(RAExpression argi) {
		int i = this.arguments.size();
		this.arguments.add(argi);
		return i;
	}
	
	public int addArgument(String argi) {
		return this.addArgument(new SimpleExpression(argi));
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
		String str = super.toString();
		str += "{arguments=[" + arguments.get(0);
		for (int c=1; c < this.arguments.size(); c++) {
			str += " " + arguments.get(c);
		}
		str += "] trgt=" + this.target + "}";
		return str;
	}

}
