package jp.co.infonic.common.db.sql.impl.standard;

import jp.co.infonic.common.db.sql.fragment.Alias;

public class AliasStd extends Alias {

	public String toSQL() {
		return "AS " + name;
	}

}
