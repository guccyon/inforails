package jp.co.infonic.common.db.sql.impl.standard;

import jp.co.infonic.common.db.sql.fragment.Column;

public class ColumnUpdate extends Column {

	public String toSQL() {
		return getSimpleName() + " = ?";
	}

}
