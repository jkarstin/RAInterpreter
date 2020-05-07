package neil1648.cs360.ui.sql;

import java.util.ArrayList;

public class SQLQuery {
	
	private ArrayList<String> selections;
	private String fromTarget;
	private ArrayList<String> conditions;
	
	public SQLQuery() {
		this.selections = new ArrayList<String>();
		this.fromTarget = "";
		this.conditions = new ArrayList<String>();
	}
	
	public int addSelection(String selectioni) {
		int i = this.selections.size();
		this.selections.add(selectioni);
		return i;
	}
	
	public void addSelections(ArrayList<String> selections) {
		for (String selection : selections)
			this.addSelection(selection);
	}
	
	public void setFromTarget(String target) {
		this.fromTarget = target;
	}
	
	public void setFromTarget(SQLQuery target) {
		this.fromTarget = "(" + target.toString() + ")";
	}
	
	public int addCondition(String conditioni) {
		int i = this.conditions.size();
		this.conditions.add(conditioni);
		return i;
	}
	
	public void addConditions(ArrayList<String> conditions) {
		for (String condition : conditions)
			this.addCondition(condition);
	}
	
	@Override
	public String toString() {
		String str = "SELECT";
		if (this.selections.size() > 0)
			for (String selection : this.selections)
				str += " " + selection;
		else str += " *";
		str += "\nFROM " + fromTarget;
		str += "\nWHERE {";
		for (String condition : this.conditions)
			str += "\n\t" + condition;
		str += "\n}";
		return str;
	}

}
