package jp.co.infonic.common.db.sql.impl.standard;

import jp.co.infonic.common.db.sql.fragment.Column;
import jp.co.infonic.common.db.sql.fragment.Join;

public class InnerJoin extends Join {

	private Column column;
	
	private Column target;
	
	public InnerJoin(Column column, Column target) {
		this.column = column;
		this.target = target;
	}
	
	@Override
	public String toSQL() {
		return "INNER JOIN " + 
			column.getTable().getName() +
			" ON " + column.getFullName()
			+ " = " + target.getFullName();
	}
}
