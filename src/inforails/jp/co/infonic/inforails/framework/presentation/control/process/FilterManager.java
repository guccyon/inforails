package jp.co.infonic.inforails.framework.presentation.control.process;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jp.co.infonic.common.util.ArrayUtil;

public class FilterManager {
	private static final String LISTENER = "LISTENER";
	private static final String ONLY = "ONLY";
	private static final String EXCEPT = "EXCEPT";
	private List<Map> listeners;
		
	public FilterManager() {
		listeners = new LinkedList<Map>();
	}
	
	void addListener(StateListener listener, String[] actions, boolean onlyOrExcept) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(LISTENER, listener);
		if (onlyOrExcept) {
			map.put(ONLY, actions);
		} else {
			map.put(EXCEPT, actions);
		}
		listeners.add(map);
	}
	
	void addListener(StateListener listener) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(LISTENER, listener);
		listeners.add(map);
	}
	
	private Iterator<Map> iter;
	
	private String actionId;
	
	StateListener nextListener() {
		if (iter == null) throw new RuntimeException("Not Start FilterAction");
		
		while(iter.hasNext()) {
			Map map = iter.next();
			if (map.containsKey(ONLY)) {
				if (!ArrayUtil.isInclude((String[])map.get(ONLY), actionId)) {
					continue;
				}
			} else if (map.containsKey(EXCEPT)) {
				if (ArrayUtil.isInclude((String[])map.get(EXCEPT), actionId)) {
					continue;
				}
			}
			
			
			return (StateListener)map.get(LISTENER);
		}
		
		return null;
	}
	
	void stateActionStart(String actionId) {
		if (iter != null) throw new RuntimeException("FilterAction already started");
		iter = listeners.iterator();
		this.actionId = actionId;
	}
}
