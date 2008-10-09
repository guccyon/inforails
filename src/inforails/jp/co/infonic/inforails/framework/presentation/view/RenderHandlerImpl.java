package jp.co.infonic.inforails.framework.presentation.view;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jp.co.infonic.common.util.Assertion;
import jp.co.infonic.inforails.base.exception.DoubleRenderException;
import jp.co.infonic.inforails.framework.presentation.control.ViewDispatcher;
import jp.co.infonic.inforails.framework.presentation.control.process.ApplicationController;
import jp.co.infonic.inforails.framework.presentation.control.process.ActionHandler;

public class RenderHandlerImpl implements RenderHandler {
	
	private boolean rendered = false;
	
	private Map<String, Object> renderOption = new HashMap<String, Object>();

	private Map<String, Object> requestScope = new HashMap<String, Object>();
	
	private Map<String, List> errors = new HashMap<String, List>();
	private boolean error = false;
	
	public void setRender(File file) {
		doubleRenderCheck();
		renderOption.put(ViewDispatcher.FILE, file);
	}
	
	public void setRender(Class<? extends ApplicationController> controller, Class<? extends ActionHandler> action) {
		doubleRenderCheck();
		setOptionController(controller);
		setOptionAction(ActionHandler.getId(action));
	}
	
	public void setRender(boolean layout) {
		doubleRenderCheck();
		renderOption.put(ViewDispatcher.LAYOUT, layout);
	}

	public void setRender(Class<? extends ActionHandler> action, boolean layout) {
		doubleRenderCheck();
		renderOption.put(ViewDispatcher.LAYOUT, layout);
		setOptionAction(ActionHandler.getId(action));
	}

	public void setRender(Class<? extends ActionHandler> action) {
		doubleRenderCheck();
		setOptionAction(ActionHandler.getId(action));
	}
	
	public void setRender(String actionId) {
		doubleRenderCheck();
		setOptionAction(actionId);
	}

	public void setRenderText(String text) {
		doubleRenderCheck();
		renderOption.put(ViewDispatcher.TEXT, text);
	}
	
	public void redirectTo(Class<? extends ActionHandler> action) {
		redirectTo(ActionHandler.getId(action));
		setRedirect();
	}

	public void redirectTo(Class<? extends ApplicationController> controller, Class<? extends ActionHandler> action) {
		setOptionController(controller);
		setOptionAction(ActionHandler.getId(action));
		setRedirect();
	}
	
	public void redirectTo(String actionId) {
		setRedirect();
		setOptionAction(actionId);
	}

	public void redirectTo(Class<? extends ApplicationController> controller, String actionId) {
		setRedirect();
		setOptionAction(actionId);
		setOptionController(controller);
	}

	public void redirectForUrl(String url) {
		setRedirect();
		renderOption.put(":url", url);
	}
	
	private void setRedirect() {
		renderOption.put(ViewDispatcher.REDIRECT, true);
	}

	public void addErrors(String key, String message) {
		if (!errors.containsKey(key)) {
			errors.put(key, new LinkedList());
		}
		error = true;
		errors.get(key).add(message);
	}

	public void addErrors(String message) {
		if (!errors.containsKey("GLOBAL")) {
			errors.put("GLOBAL", new LinkedList());
		}
		error = true;
		errors.get("GLOBAL").add(message);
	}

	public void setAttribute(String key, Object value) {
		requestScope.put(key, value);
	}
	
	private void setOptionController(Class<? extends ApplicationController> controller) {
		Assertion.isNotNull(controller, "controller value is null");
		renderOption.put(ViewDispatcher.CONTROLLER, controller);
	}
	
	private void setOptionAction(String actionId) {
		Assertion.isNotNull(actionId, "actionId is null");
		renderOption.put(ViewDispatcher.ACTION, actionId);
	}

	public Map option() {
		return renderOption;
	}
	
	public Map requestScope() {
		return requestScope;
	}
	
	private void doubleRenderCheck() {
		if (rendered) {
			throw new DoubleRenderException();
		}
		rendered = true;
	}

	public boolean isRendered() {
		return rendered || renderOption.containsKey(ViewDispatcher.REDIRECT);
	}

	public boolean hasError() {
		return error;
	}
}
