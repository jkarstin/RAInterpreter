package neil1648.cs360.ui.sql;

public class BasicTable extends SQLTable {

	public BasicTable(String label) {
		super(TABL_TYPE.BASIC);
		this.label = label;
	}
	
	@Override
	public String toString() {
		return this.label;
	}

}
