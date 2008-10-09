package jp.co.infonic.common.db.sql.phrase;

import java.util.List;

import jp.co.infonic.common.db.sql.fragment.Column;

public abstract class OrderBy implements Phrase {
	
	protected List <Column> columns;
	
	public OrderBy add(Column column) {
		columns.add(column);
		return this;
	}
}
