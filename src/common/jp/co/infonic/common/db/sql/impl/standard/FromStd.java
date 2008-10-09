package jp.co.infonic.common.db.sql.impl.standard;

import jp.co.infonic.common.db.sql.fragment.Table;
import jp.co.infonic.common.db.sql.phrase.From;
import jp.co.infonic.common.module.ex.ExArray;
import jp.co.infonic.common.module.ex.ExArray.ReturnIterator;

public class FromStd extends From {
	
	public String toSQL() {
		return "FROM " + new ExArray<Table>(tables.toArray(new Table[0]))
						.map(new ReturnIterator<Table>(){
								public Object yield(int i, Table e, Object... obj) {
									return e.toSQL();
								}})
						.join(", ");
	}

}
