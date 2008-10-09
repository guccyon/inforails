package jp.co.infonic.common.db.sql.query;

import jp.co.infonic.common.db.sql.impl.FragmentsFactory;

public abstract class CommonQuery implements Query {
	protected FragmentsFactory fact;
	
	public void addFactory(FragmentsFactory factory) {
		this.fact = factory;
	}
	
}
