package jp.co.infonic.common.db.sql.fragment;

import jp.co.infonic.common.db.sql.query.Query;

public abstract class SubQuery extends Table {

	protected Query query;
	
	public SubQuery(Query query) {
		this.query = query;
	}
}
