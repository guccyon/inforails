package jp.co.infonic.inforails.framework.presentation.view;

import java.io.File;
import java.util.Map;

import jp.co.infonic.inforails.framework.presentation.control.process.ApplicationController;
import jp.co.infonic.inforails.framework.presentation.control.process.ActionHandler;

public interface RenderHandler {
	
	void setRender(Class<? extends ApplicationController> controller, Class<? extends ActionHandler> action);
	
	void setRender(Class<? extends ActionHandler> action);
	
	void setRender(Class<? extends ActionHandler> action, boolean layout);
	
	void setRender(String actionId);
	
	void setRender(boolean layout);
	
	void setRender(File file);
	
	void setRenderText(String text);
	
	void redirectTo(Class<? extends ActionHandler> action);
	
	void redirectTo(Class<? extends ApplicationController> controller, Class<? extends ActionHandler> action);
	
	void redirectTo(String actionId);
	
	void redirectTo(Class<? extends ApplicationController> controller, String action);
	
	void redirectForUrl(String url);
	
	void addErrors(String key, String message);
	
	void addErrors(String message);
	
	void setAttribute(String key, Object value);
	
	public Map option();

	public Map requestScope();
	
	public boolean isRendered();
	
	boolean hasError();
}
