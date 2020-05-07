package neil1648.cs360.ui.ra.expr;

import java.util.ArrayList;

public class Aggregation extends RAExpression {
	
	private ArrayList<String> groups;
	private ArrayList<String> actions;
	
	public Aggregation(String act0) {
		super(EXPR_TYPE.AGGR);
		this.groups = new ArrayList<String>();
		this.actions = new ArrayList<String>();
		this.actions.add(act0);
	}
	
	public int addGroup(String grpi) {
		int i = this.groups.size();
		this.groups.add(grpi);
		return i;
	}
	
	public int addAction(String acti) {
		int i = this.actions.size();
		this.actions.add(acti);
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
		str += "{actions=[" + actions.get(0);
		for (int c=1; c < this.actions.size(); c++) {
			str += " " + actions.get(c);
		}
		if (this.groups.size() > 0) {
			str += "] groups=[" + groups.get(0);
			for (int c=1; c < this.groups.size(); c++) {
				str += " " + groups.get(c);
			}
		}
		str += "] trgt=" + this.target + "}";
		return str;
	}
	
}
