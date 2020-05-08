package neil1648.cs360.ui.sql;

import java.util.ArrayList;

public class SQLQuery extends SQLTable {
	
	private boolean distinct;
	private ArrayList<String> selections;
	private SQLTable fromTarget;
	private ArrayList<String> conditions;
	private ArrayList<String> groups;
	
	public SQLQuery() {
		super(TABL_TYPE.QUERY_RESULT);
		this.distinct = true;
		this.selections = new ArrayList<String>();
		this.fromTarget = null;
		this.conditions = new ArrayList<String>();
		this.groups = new ArrayList<String>();
	}
	
	public boolean isDistinct() {
		return this.distinct;
	}
	
	public void setDistinct(boolean d) {
		this.distinct = d;
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
	
	public boolean hasSelections() {
		return this.selections.size() > 0;
	}
	
	public void setFromTarget(SQLTable target) {
		this.fromTarget = target;
	}
	
	public void setFromTarget(String target) {
		this.setFromTarget(new BasicTable(target));
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
	
	public boolean hasConditions() {
		return this.conditions.size() > 0;
	}
	
	public int addGroup(String groupi) {
		int i = this.groups.size();
		this.groups.add(groupi);
		return i;
	}
	
	public void addGroups(ArrayList<String> groups) {
		for (String group : groups)
			this.addGroup(group);
	}
	
	public boolean hasGroups() {
		return this.groups.size() > 0;
	}
	
	@Override
	public String toString() {
		String str = "SELECT";
		if (this.distinct) str += " DISTINCT";
		if (this.hasSelections())
			for (String selection : this.selections)
				str += " " + selection;
		else str += " *";
		str += "\nFROM ";
		if (fromTarget.type == TABL_TYPE.QUERY_RESULT)
			str += "( " + fromTarget + " )";
		else str += fromTarget;
		str += "\nWHERE {";
		for (String condition : this.conditions)
			str += "\n\t" + condition;
		str += "\n}";
		if (this.hasGroups()) {
			str += "\nGROUP BY";
			for (String group : this.groups)
				str += " " + group;
		}
		return str;
	}

}
