package jp.co.infonic.inforails.framework.presentation.routing;

import java.util.HashMap;
import java.util.Map;

import jp.co.infonic.common.module.log.DebugLogger;
import jp.co.infonic.common.util.Assertion;
import jp.co.infonic.common.util.ClassUtil;
import jp.co.infonic.inforails.base.exception.RoutingException;
import jp.co.infonic.inforails.base.loader.ClassSearcherFactory;
import jp.co.infonic.inforails.base.property.FactoryManager;
import jp.co.infonic.inforails.framework.presentation.control.process.ApplicationController;
import jp.co.infonic.inforails.framework.presentation.control.process.DefaultAction;
import jp.co.infonic.inforails.framework.presentation.control.process.ActionHandler;

import org.apache.log4j.Logger;

public class ProcessMapper {

	private Map<String, ControllerInfo> controllers;

	public static ProcessMapper i = new ProcessMapper();
	
	private static final String WEB_CLASS_SEARCHER_FACTORY = "WEB_CLASS_SEARCHER";
	
	private Logger logger = DebugLogger.getLogger(this.getClass());

	private ProcessMapper() {
		controllers = new HashMap<String, ControllerInfo>();
	}

	public void initAll() {
		controllers = new HashMap<String, ControllerInfo>();
		try {
			ClassSearcherFactory fact = (ClassSearcherFactory) FactoryManager.i.getFactory(WEB_CLASS_SEARCHER_FACTORY);
			Assertion.isNotNull(fact, "Not Found ClassSercherFactory");
			for (String s : fact.getClassSearcher().getClasses()) {
				if (s.endsWith("Controller") && ClassUtil.isSuperClass(ApplicationController.class, Class.forName(s))) {
					ControllerInfo conInfo = new ControllerInfo(s);
					controllers.put(conInfo.getControllerId(), conInfo);
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void clear() {
		controllers = new HashMap<String, ControllerInfo>();
	}

	public Class<? extends ApplicationController> getController(String controllerName) {
		if (controllers.containsKey(controllerName)) {
			return controllers.get(controllerName).getController();
		}

		throw new RoutingException("Unknown Controller:" + controllerName);
	}

	public Class<? extends ActionHandler> getAction(String controllerName, String actionId) {
		ControllerInfo conInfo = controllers.get(controllerName);
		Assertion.isNotNull(conInfo, "could not found controllers info");
		if (conInfo.contains(actionId))
			return conInfo.getAction(actionId);

		Class<? extends ActionHandler> action = null; 
		String actionClass = actionClassName(conInfo.getController(), actionId);
		try {
			if (ClassUtil.isSuperClass(ActionHandler.class, Class.forName(actionClass))) {
				conInfo.setAction(actionId, actionClass);
				action = conInfo.getAction(actionId);
			}
		} catch (ClassNotFoundException e) {
			action = DefaultAction.class;
		}
		return action;
	}

	private String actionClassName(Class<? extends ApplicationController> con, String actionId) {
		String pkg = con.getPackage().getName() + ".action";
		return pkg + "." + actionId + "Action";
	}

	private class ControllerInfo {
		Class<? extends ApplicationController> clazz;

		Map<String, Class<? extends ActionHandler>> actions;
		
		ControllerInfo(String className) throws ClassNotFoundException {
			this.clazz = Class.forName(className).asSubclass(ApplicationController.class);
			this.actions = new HashMap<String, Class<? extends ActionHandler>>();
		}
		
		String getControllerId() {
			return this.clazz.getSimpleName().replaceFirst("Controller$", "");
		}

		void setAction(String actionId, String className) throws ClassNotFoundException {
			actions.put(actionId, Class.forName(className).asSubclass(ActionHandler.class));
		}

		Class<? extends ActionHandler> getAction(String actionId) {
			return actions.get(actionId);
		}

		boolean contains(String actionId) {
			return actions.containsKey(actionId);
		}

		Class<? extends ApplicationController> getController() {
			return clazz;
		}
	}
}
