package jp.co.infonic.inforails.framework.presentation.routing;

import jp.co.infonic.common.util.Assertion;
import jp.co.infonic.inforails.framework.presentation.control.process.ApplicationController;
import jp.co.infonic.inforails.framework.presentation.control.process.ActionHandler;



public class RouteParam {

	private String pattern;
	
	private Class <? extends ApplicationController> controller;
	
	private String action;
	
	public RouteParam(String urlPattern) {
		this.pattern = urlPattern;
	}
	
	public RouteParam controller(Class controller) {
		Class tmp = controller;
		while(tmp.getSuperclass() != Object.class) {
			if (ApplicationController.class == tmp.getSuperclass()) {
				this.controller = controller;
				break;
			} else {
				tmp = tmp.getSuperclass();
			}
		}
		return this;
	}
	
	public RouteParam action(Class action) {
		Class tmp = action;
		while(tmp.getSuperclass() != Object.class) {
			if (ActionHandler.class == tmp.getSuperclass()) {
				action(ActionHandler.getId(action));
				break;
			} else {
				tmp = tmp.getSuperclass();
			}
		}
		return this;
	}
	
	public RouteParam action(String action) {
		Assertion.isNotNull(action, "invalid actionId : " + action);
		this.action = action;
		return this;
	}
	
	public String pattern() {
		return pattern;
	}
	
	public Class<? extends ApplicationController> controller() {
		return controller;
	}
	
	public String action() {
		return action;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof RouteParam) {
			RouteParam target = (RouteParam) obj;
			return target.pattern.equals(this.pattern) 
				 && target.controller == this.controller
				 && target.action == this.action;
		}
		return false;
	}
}
