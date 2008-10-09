package jp.co.infonic.common.db.sql.fragment;

public abstract class Column implements Fragment {

	private String name;
	
	private Table table;
	
	public Column setName(String name) {
		this.name = name;
		return this;
	}
	
	public String getSimpleName() {
		return name;
	}
	
	public String getFullName() {
		return table.getName() + "." + name;
	}
	
	public String toSQL() {
		return getSimpleName();
	}
	
	public Table getTable() {
		return table;
	}
	
	public void setTable(Table table) {
		this.table = table;
	}
}
