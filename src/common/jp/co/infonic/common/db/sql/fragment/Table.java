package jp.co.infonic.common.db.sql.fragment;

public abstract class Table implements Fragment {

	protected Alias alias;
	
	private String name;
	
	public Table() {};
	
	public Table setName(String name) {
		this.name = name;
		return this;
	}
	
	public Table setAlias(Alias alias) {
		this.alias = alias;
		return this;
	}
	
	public String toSQL() {
		return alias == null ? name : name + " " + alias.toSQL();
	}
	
	public String getName() {
		return alias == null ? name : alias.toSQL();
	}
}
