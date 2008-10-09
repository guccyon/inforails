package jp.co.infonic.common.module.ex;

import java.util.HashMap;
import java.util.Map;

import jp.co.infonic.common.util.Assertion;

public class ExMap<K, V> extends HashMap<K, V> implements IObject{
	private static final long serialVersionUID = 3694186690986255248L;
	
	public ExMap(){ super(); }
	
	public ExMap(int initialCapacity) {
		super(initialCapacity);
	}
	
	public ExMap(Map<K, V> m) {
		super(m);
	}
	
	public ExMap(final K[] keys, final V[] values) {
		Assertion.isEquals(keys.length, values.length);
		new ExArray<K>(keys).each(new ExArray.Iterator<K>(){
			public void yield(int i, K e) {	put(e, values[i]);	}
		});
	}
	
	public void each(Iterator<K, V> iter) {
		try {
			for(K k: keySet()) iter.yield(k, get(k));
		} catch(BreakException e){}
	}
	
	public static interface Iterator<K, V> {
		public void yield(K k, V v);
	}

	public boolean isBlank() { return false; }
	public boolean isExist() { return true; }
}
