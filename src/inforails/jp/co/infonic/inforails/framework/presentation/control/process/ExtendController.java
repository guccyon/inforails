package jp.co.infonic.inforails.framework.presentation.control.process;

import java.util.HashMap;
import java.util.Map;

import jp.co.infonic.common.util.StringUtil;

abstract class ExtendController extends BaseController {
	
	protected static final String BEFORE_FILTER_NAME = "before";
	protected static final String AFTER_FILTER_NAME = "after";
	protected static final String RENDER_BEFORE_FILTER_NAME = "renderBefore";
	
	private Map<String, FilterManager> filterManagerMap = new HashMap<String, FilterManager>();

	public final String getId() {
		return getId(this.getClass());
	}
	
	public static final String getId(Class<? extends BaseController> clazz) {
		return clazz.getSimpleName().replaceFirst("Controller$", "");
	}
	
	public String getViewName() {
		return getViewName(this.getClass());
	}
	
	public static String getViewName(Class<? extends BaseController>clazz) {
		return StringUtil.decamelize(getId(clazz));
	}
	
	protected void beforeFilter(StateListener listener) {
		getFilterManager(BEFORE_FILTER_NAME).addListener(listener);
	}
	protected void beforeFilterOnly(StateListener listener, String[] actions) {
		getFilterManager(BEFORE_FILTER_NAME).addListener(listener, actions, true);
	}
	protected void beforeFilterExcept(StateListener listener, String[] actions) {
		getFilterManager(BEFORE_FILTER_NAME).addListener(listener, actions, false);
	}
	
	protected void renderBeforeFilter(StateListener listener) {
		getFilterManager(RENDER_BEFORE_FILTER_NAME).addListener(listener);
	}
	protected void renderBeforeFilterOnly(StateListener listener, String[] actions) {
		getFilterManager(RENDER_BEFORE_FILTER_NAME).addListener(listener, actions, true);
	}
	protected void renderBeforeFilterExcept(StateListener listener, String[] actions) {
		getFilterManager(RENDER_BEFORE_FILTER_NAME).addListener(listener, actions, false);
	}
	
	protected void afterFilter(StateListener listener) {
		getFilterManager(AFTER_FILTER_NAME).addListener(listener);
	}
	protected void afterFilterOnly(StateListener listener, String[] actions) {
		getFilterManager(AFTER_FILTER_NAME).addListener(listener, actions, true);
	}
	protected void afterFilterExcept(StateListener listener, String[] actions) {
		getFilterManager(AFTER_FILTER_NAME).addListener(listener, actions, false);
	}
	
	protected FilterManager getFilterManager(String state) {
		if (!filterManagerMap.containsKey(state))
			filterManagerMap.put(state, new FilterManager());
		return filterManagerMap.get(state);
	}
}
