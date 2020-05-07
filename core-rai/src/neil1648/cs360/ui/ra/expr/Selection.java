package neil1648.cs360.ui.ra.expr;

import java.util.ArrayList;

public class Selection extends RAExpression {
	
	private ArrayList<String> conditions;
	
	public Selection(String cnd0) {
		super(EXPR_TYPE.SLCT);
		this.conditions = new ArrayList<String>();
		this.conditions.add(cnd0);
	}
	
	public int addCondition(String cndi) {
		int i = this.conditions.size();
		this.conditions.add(cndi);
		return i;
	}
	
	public ArrayList<String> getConditions() {
		return this.conditions;
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
		str += "{conditions=[" + conditions.get(0);
		for (int c=1; c < this.conditions.size(); c++) {
			str += " " + conditions.get(c);
		}
		str += "] trgt=" + this.target + "}";
		return str;
	}
	
}
