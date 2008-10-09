package jp.co.infonic.common.db.sql;

import jp.co.infonic.common.db.sql.impl.FragmentsFactory;
import jp.co.infonic.common.db.sql.impl.oracle.OracleFactory;
import jp.co.infonic.common.db.sql.query.DeleteQuery;
import jp.co.infonic.common.db.sql.query.InsertQuery;
import jp.co.infonic.common.db.sql.query.Query;
import jp.co.infonic.common.db.sql.query.SelectQuery;
import jp.co.infonic.common.db.sql.query.UpdateQuery;

public class QueryStructure {

	private FragmentsFactory factory;
	
	private Query query;
	
	public QueryStructure(FragmentsFactory factory) {
		this.factory = factory;
	}
	
	public Query createSelect() {
		return query = new SelectQuery();
	}
	
	public Query createInsert() {
		return query = new InsertQuery();
	}
	
	public Query createUpdate() {
		return query = new UpdateQuery(factory.createTable());
	}
	
	public Query createDelete() {
		return query = new DeleteQuery();
	}
	
	public static void main(String[] args) {
		
		QueryStructure qstruct = new QueryStructure(OracleFactory.getFactory());
		qstruct.createSelect();
		
		System.out.println();
	}
}
