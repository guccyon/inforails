package jp.co.infonic.inforails.framework.presentation.routing;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.co.infonic.inforails.base.exception.RoutingException;


public class UrlParser {
	
	private static List<RouteParam> routeTableList = new LinkedList<RouteParam>();
	private static String REGEX_URL_PATERN = "(?:/+)?(#\\{([^/}]+?)\\}|.+?)(/|$)";
	private static String REGEX_URL_PARAM = "#\\{[^/}]+?\\}";

	public static RouteParam lookup(String requestPath) {
		requestPath = requestPath.replaceFirst("^/", "");
		for (RouteParam r: routeTableList) {
			if (isMatch(r.pattern(), requestPath)) return r;
		}
		
		throw new RoutingException("Routing error:" + requestPath);
	}
	
	public static void reset() {
		routeTableList = new LinkedList<RouteParam>();
	}
	
	public static void appendTable(RouteParam param) {
		routeTableList.add(param);
	}
	
	private static boolean isMatch(String pattern, String requestPath) {
		return requestPath.matches( pattern.replaceAll(REGEX_URL_PARAM, ".+?") );
	}

	
	private Pattern pat = Pattern.compile(REGEX_URL_PATERN);
	
	private Map<String, String> params;
	
	public UrlParser(RouteParam route, String requestPath) {
		params = new HashMap<String, String>();
		parse(route.pattern(), requestPath);
	}
	
	private void parse(String pattern, String requestPath) {
		if (pattern.length() == 0) return;

		Matcher mat = pat.matcher(pattern);
		Matcher req = pat.matcher(requestPath);
		if (mat.find() && mat.group(2) != null && req.find()) { 
			params.put(mat.group(2), req.group(1));
		}
		parse(mat.replaceFirst(""), req.replaceFirst(""));
	}
	
	public Map<String, String> parameter() {
		return params;
	}
}
