package jp.co.infonic.common.db.sql.impl.oracle;

import jp.co.infonic.common.db.sql.fragment.Column;
import jp.co.infonic.common.db.sql.impl.FragmentsFactory;

public class OracleFactory extends FragmentsFactory {

	private static OracleFactory i = new OracleFactory();
	
	public static OracleFactory getFactory() {
		return i;
	}
	
	private OracleFactory() {
		fragments.put("Column", Column.class);
	}
}
