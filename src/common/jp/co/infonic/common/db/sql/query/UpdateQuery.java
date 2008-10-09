package jp.co.infonic.common.db.sql.query;

import java.util.List;

import jp.co.infonic.common.db.sql.fragment.Table;
import jp.co.infonic.common.db.sql.impl.standard.ColumnSelect;
import jp.co.infonic.common.db.sql.impl.standard.ColumnUpdate;

public class UpdateQuery extends CommonQuery {
	
	private List<ColumnUpdate> columns;
	
	private Table targetTable;
	
	public UpdateQuery(Table tarTable) {
		this.targetTable = tarTable;
	}
	
	public UpdateQuery addSet(ColumnUpdate column) {
		this.columns.add(column);
		return this;
	}

	public String toSQL() {
		return null;
	}
	
	private ColumnSelect createColumn() {
		return (ColumnSelect)fact.create("ColumnUpdate");
	}
}
