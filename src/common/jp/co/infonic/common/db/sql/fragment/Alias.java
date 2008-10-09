package jp.co.infonic.common.db.sql.fragment;

public abstract class Alias implements Fragment {

	protected String name;
	
	public Alias setName(String name) {
		this.name = name;
		return this;
	}
}
