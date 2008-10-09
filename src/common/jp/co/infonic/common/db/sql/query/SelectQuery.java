package jp.co.infonic.common.db.sql.query;

import java.util.LinkedList;
import java.util.List;

import jp.co.infonic.common.db.sql.fragment.Column;
import jp.co.infonic.common.db.sql.fragment.Table;
import jp.co.infonic.common.db.sql.impl.standard.ColumnSelect;
import jp.co.infonic.common.db.sql.phrase.From;
import jp.co.infonic.common.module.ex.ExArray;

public class SelectQuery extends CommonQuery {

	private List<Column> columns;
	
	private From from;
	
	public String toSQL() {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT ");
		sb.append(toColumnStr()).append(" ");
		sb.append(from.toSQL());
		
		return sb.toString();
	}
	
	public SelectQuery addTable(Table table) {
		if (from == null) from	= (From)fact.create("From");
		from.add(table);
		return this;
	}
	
	public SelectQuery addTable(String tableName) {
		addTable( ((Table) fact.create("Table")).setName(tableName) );
		return this;
	}
	
	public SelectQuery addColumn(String columnName) {
		addColumn(columnName, null);
		return this;
	}
	
	public SelectQuery addColumn(String columnName, Table table) {
		if (columns == null) {
			columns = new LinkedList<Column>();
		}
		columns.add(createColumn(table).setName(columnName));
		return this;
	}
	
	private ColumnSelect createColumn(Table table) {
		ColumnSelect column = (ColumnSelect) fact.create("ColumnSelect");
		if (table != null) {
			column.setTable(table);
		}
		return column;
	}
	
	private String toColumnStr() {
		if (columns == null) return "*";
		
		ExArray<Column> ary = new ExArray<Column>(columns.toArray(new Column[0]));
		
		return ary.map(new ExArray.ReturnIterator<Column>(){
			public String yield(int i, Column e, Object... obj) {
				return e.toSQL();
			}
		}).join(",");
	}
}
