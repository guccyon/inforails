package jp.co.infonic.common.db.sql.impl;

import java.util.HashMap;
import java.util.Map;

import jp.co.infonic.common.db.DataAccessException;
import jp.co.infonic.common.db.sql.fragment.Column;
import jp.co.infonic.common.db.sql.fragment.Fragment;
import jp.co.infonic.common.db.sql.fragment.Table;
import jp.co.infonic.common.db.sql.impl.standard.AliasStd;
import jp.co.infonic.common.db.sql.impl.standard.ColumnSelect;
import jp.co.infonic.common.db.sql.impl.standard.ColumnStd;
import jp.co.infonic.common.db.sql.impl.standard.ColumnUpdate;
import jp.co.infonic.common.db.sql.impl.standard.FromStd;
import jp.co.infonic.common.db.sql.impl.standard.GroupByStd;
import jp.co.infonic.common.db.sql.impl.standard.InnerJoin;
import jp.co.infonic.common.db.sql.impl.standard.OrderByStd;
import jp.co.infonic.common.db.sql.impl.standard.TableStd;

public class FragmentsFactory {
	
	protected Map<String, Class<? extends Fragment>> fragments;

	protected FragmentsFactory() {
		fragments = new HashMap();
		
		fragments.put("Alias", AliasStd.class);
		fragments.put("Column", ColumnStd.class);
		fragments.put("ColumnSelect", ColumnSelect.class);
		fragments.put("ColumnUpdate", ColumnUpdate.class);
		fragments.put("From", FromStd.class);
		fragments.put("GroupBy", GroupByStd.class);
		fragments.put("OrderBy", OrderByStd.class);
		fragments.put("InnerJoin", InnerJoin.class);
		fragments.put("Table", TableStd.class);
	}
	
	public Fragment create(String key) {
		try {
			if (fragments.containsKey(key)) {
				return fragments.get(key).newInstance();
			}

			throw new DataAccessException("NotFound fragment key : " + key);
			
		} catch (Exception e) {
			throw new DataAccessException(e);
		}
	}
	
	public Column createColumn() {
		return (Column)create("Column");
	}
	
	public Table createTable() {
		return (Table) create("Table");
	}
}
