package jp.co.infonic.common.db.sql.phrase;

import java.util.LinkedList;
import java.util.List;

import jp.co.infonic.common.db.sql.fragment.Condition;
import jp.co.infonic.common.module.ex.ExArray.ResultIterator;
import jp.co.infonic.common.util.ArrayUtil;

public abstract class Where implements Phrase {
	
	protected List conditions = new LinkedList();
	
	protected List join = new LinkedList();
	
	public Where and(Condition condition) {
		join.add(true);
		conditions.add(condition);
		return this;
	}
	
	public Where or(Condition condition) {
		join.add(false);
		conditions.add(condition);
		return this;
	}
	
	public Where and(List<Condition> condition) {
		and(condition.toArray(new Condition[0]));
		return this;
	}
	
	public Where or(List<Condition> condition) {
		or(condition.toArray(new Condition[0]));
		return this;
	}
	
	public Where and(Condition[] condition) {
		if (conditions.size() > 0) {
			add(condition);
		} else {
			conditions.add(condition);
		}
		join.add(true);
		return this;
	}
	
	public Where or(Condition[] condition) {
		if (conditions.size() > 0) {
			add(condition);
		} else {
			conditions.add(condition);
		}
		join.add(false);
		return this;
	}
	
	private void add(Condition[] conds) {
		if (existConditions()) {
			conditions.add(conds);
		} else {
			Condition[] old = (Condition[])conditions.toArray(new Condition[0]);
			conditions.clear();
			and(old);
			conditions.add(conds);
		}
	}
	
	private boolean existConditions() {
		return ArrayUtil.detect(conditions, new ResultIterator<Object>(){
			public boolean yield(int i, Object e) {
				return e instanceof Condition[];
			}
		}) != null;
	}
}
