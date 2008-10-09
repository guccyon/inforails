package jp.co.infonic.common.db.sql.impl.standard;

import jp.co.infonic.common.db.sql.phrase.OrderBy;
import jp.co.infonic.common.util.ArrayUtil;

public class OrderByStd extends OrderBy {

	public String toSQL() {
		return "ORDER BY " +
			ArrayUtil.join(columns, ", ");
	}

}
