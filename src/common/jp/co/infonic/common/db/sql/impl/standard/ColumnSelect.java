package jp.co.infonic.common.db.sql.impl.standard;

import jp.co.infonic.common.db.sql.fragment.Alias;
import jp.co.infonic.common.db.sql.fragment.Column;

public class ColumnSelect extends Column {
	
	private Alias alias;
	
	public ColumnSelect setAlias(Alias alias) {
		this.alias = alias;
		return this;
	}

	public String toSQL() {
		return alias != null ? getFullName() + " " + alias.toSQL() : getFullName();
	}
}
