package neil1648.cs360.ui.sql;

import java.util.ArrayList;

public class JoinTable extends SQLTable {
	
	private ArrayList<SQLTable> joinedTables;
	
	public JoinTable(SQLTable t) {
		super(TABL_TYPE.JOIN_RESULT);
		this.joinedTables = new ArrayList<SQLTable>();
		this.joinedTables.add(t);
	}
	public JoinTable(String s) { this(new BasicTable(s)); }
	
	public void join(SQLTable sqlt) {
		this.joinedTables.add(sqlt);
	}
	
	public void join(String lbl) {
		this.join(new BasicTable(lbl));
	}
	
	public boolean hasTables() {
		return this.joinedTables.size() > 0;
	}
	
	@Override
	public String toString() {
		if (this.hasTables()) {
			String str = this.joinedTables.get(0).toString();
			for (int t=1; t < this.joinedTables.size(); t++)
				str += " JOIN " + this.joinedTables.get(t);
			return str;
		}
		return null;
	}

}
