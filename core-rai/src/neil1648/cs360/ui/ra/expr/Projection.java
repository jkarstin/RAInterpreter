package neil1648.cs360.ui.ra.expr;

import java.util.ArrayList;

public class Projection extends RAExpression {
	
	private ArrayList<String> columns;
	
	public Projection(String col0) {
		super(EXPR_TYPE.PROJ);
		this.columns = new ArrayList<String>();
		this.columns.add(col0);
	}
	
	public int addColumn(String coli) {
		int i = this.columns.size();
		this.columns.add(coli);
		return i;
	}
	
	public ArrayList<String> getColumns() {
		return columns;
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
		str += "{columns=[" + columns.get(0);
		for (int c=1; c < this.columns.size(); c++) {
			str += " " + columns.get(c);
		}
		str += "] trgt=" + this.target + "}";
		return str;
	}

}
