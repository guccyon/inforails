package jp.co.infonic.inforails.framework.presentation.control;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.infonic.common.util.Assertion;

public class ServiceInfo {
	
	public static final String CONTROLLER_ID = "controller";
	
	public static final String ACTION_ID = "action";

	private String controller = "";
	
	private String action = "";
	
	private String contextRoot;
	
	private Map<String, String> parameter = new HashMap<String, String>();
	
	HttpServletRequest request;
	
	HttpServletResponse response;
	
	ServletContext context;
	
	public String controller() {
		return this.controller;
	}
	public String action() {
		return this.action;
	}
	
	public String getRealPath(String relativePath) {
		return this.contextRoot + "/" + relativePath;
	}
	
	void margeParam(Map map) {
		parameter.putAll(map);
	}
	
	Map defaultParams() {
		return parameter;
	}
	
	void setController(String controllerId) {
		Assertion.isNotNull(controller, "controller is null");
		this.controller = controllerId;
		if(parameter.containsKey(CONTROLLER_ID)) 
			System.out.println("Already exist key:" + CONTROLLER_ID + " override to new value:" + controllerId);
		parameter.put(CONTROLLER_ID, controllerId);
	}
	void setAction(String actionId) {
		Assertion.isNotNull(actionId, "action is null");
		this.action = actionId;
		if(parameter.containsKey(ACTION_ID)) 
			System.out.println("Already exist key:" + ACTION_ID + " override to new value:" + actionId);
		parameter.put(ACTION_ID, actionId);
	}
	void setContextRoot(String str) {
		this.contextRoot = str;
	}
}
