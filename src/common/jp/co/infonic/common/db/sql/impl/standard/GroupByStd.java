package jp.co.infonic.common.db.sql.impl.standard;

import jp.co.infonic.common.db.sql.phrase.GroupBy;
import jp.co.infonic.common.util.ArrayUtil;

public class GroupByStd extends GroupBy {

	public String toSQL() {
		return "GROUP BY " +
			ArrayUtil.join(columns, ", ");
	}

}
