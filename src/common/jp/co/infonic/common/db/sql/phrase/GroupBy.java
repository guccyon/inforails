package jp.co.infonic.common.db.sql.phrase;

import java.util.List;

import jp.co.infonic.common.db.sql.fragment.Column;

public abstract class GroupBy implements Phrase {
	
	protected List <Column> columns;
	
	public GroupBy add(Column column) {
		columns.add(column);
		return this;
	}
}
