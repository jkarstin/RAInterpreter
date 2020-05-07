package neil1648.cs360.ui.ra.expr;

import java.util.ArrayList;

public class Join extends RAExpression {

	private ArrayList<String> arguments;
	
	public Join(String arg0) {
		super(EXPR_TYPE.JOIN);
		this.arguments = new ArrayList<String>();
		this.arguments.add(arg0);
	}

	public int addArgument(String argi) {
		int i = this.arguments.size();
		this.arguments.add(argi);
		return i;
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
