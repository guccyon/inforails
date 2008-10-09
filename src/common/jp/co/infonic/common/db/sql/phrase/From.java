package jp.co.infonic.common.db.sql.phrase;

import java.util.LinkedList;
import java.util.List;

import jp.co.infonic.common.db.sql.fragment.Table;

public abstract class From implements Phrase {
	
	protected List<Table> tables = new LinkedList();
	
	public From add(Table table) {
		tables.add(table);
		return this;
	}
}
