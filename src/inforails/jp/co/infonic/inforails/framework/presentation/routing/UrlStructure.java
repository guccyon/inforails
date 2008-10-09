package jp.co.infonic.inforails.framework.presentation.routing;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jp.co.infonic.common.util.ArrayUtil;
import jp.co.infonic.common.util.ConditionSupport;
import jp.co.infonic.common.util.StringUtil;
import jp.co.infonic.inforails.framework.presentation.control.process.ApplicationController;
import jp.co.infonic.inforails.framework.presentation.control.process.ActionHandler;

public class UrlStructure {

	public static String struct(String prefix,Class controller, Class action) {
		return struct(prefix, controller, action, "");
	}

	public static String struct(String prefix,Class controller, Class action, Map<String, Object> params) {
		return struct(prefix, controller, action, mapToQueryString(params));
	}
	
	public static String struct(String prefix,Class controller, Class action, String params) {
		return struct(prefix, controller, ActionHandler.getId(action), params);
	}

	public static String struct(String prefix, Class controller, String actionId, Map<String, Object> params) {
		return struct(prefix, controller, actionId, mapToQueryString(params));
	}
	
	public static String struct(String prefix, Class controller, String actionId, String params) {
		StringBuffer sb = new StringBuffer("");
		if (!"".equals(prefix)) {
			sb.append(prefix).append("/");
		}
		sb.append(ApplicationController.getViewName(controller));
		sb.append("/");
		sb.append(StringUtil.decamelize(actionId));
		if (ConditionSupport.isExist(params)) {
			sb.append("?").append(params);
		}
		return sb.toString();
	}
	
	private static String mapToQueryString(Map<String, Object> map) {
		Iterator<String> iter = map.keySet().iterator();
		List<String> list = new LinkedList<String>();
		while(iter.hasNext()) {
			String key = iter.next();
			list.add(key + "=" + map.get(key));
		}
		
		return ArrayUtil.join(list, "&");
	}
}