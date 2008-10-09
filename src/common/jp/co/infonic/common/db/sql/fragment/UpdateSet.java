package jp.co.infonic.common.db.sql.fragment;

public class UpdateSet implements Fragment {
	
	private Column column;
	
	private Table table;

	public String toSQL() {
		return column.toSQL();
	}
	
	public UpdateSet addSet(String name) {
		return this;
	}
}
