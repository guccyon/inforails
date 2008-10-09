package jp.co.infonic.inforails.framework.presentation.control.process;

import java.io.File;
import java.util.Map;

import jp.co.infonic.inforails.framework.presentation.control.ActionAgent;
import jp.co.infonic.inforails.framework.presentation.control.Parameter;
import jp.co.infonic.inforails.framework.presentation.view.RenderHandler;
import jp.co.infonic.inforails.framework.session.InfoRailsSession;


public abstract class ApplicationController extends ExtendController {

	private Parameter params;
	
	private InfoRailsSession session;
	
	private Map<String, String> environments;
	
	protected RenderHandler view;
	
	void setSession(InfoRailsSession session) {
		this.session = session;
	}
	void setParameter(Parameter params) {
		this.params = params;
	}
	void setEnvironments(Map<String, String> environments) {
		this.environments = environments;
	}
	
	protected final Object session(String key) {
		return session != null ? session.get(key) : null;
	}
	
	protected final void setSession(String key, String value) {
		session.set(key, value);
	}
	
	protected Object params(String key) {
		return params.get(key);
	}
	
	protected String paramsS(String key) {
		return (String) params.get(key);
	}
	
	protected File paramsF(String key) {
		return params.getFile(key);
	}
	
	protected String ENV(String key) {
		return environments.get(key);
	}
	
	public void setRender(RenderHandler view) {
		this.view = view;
	}

	private boolean exceptionRised = false;

	protected boolean isProcessFinished() {
		return isNotStatusError() && view.isRendered();
	}
	
	protected boolean isNotStatusError() {
		return !exceptionRised;
	}
	
	// イベント処理の前に呼び出される
	final void beforeFilterAction() {
		exceptionRised = true;
		filterAction(getFilterManager(BEFORE_FILTER_NAME));
		exceptionRised = false;
	}
	

	final void process(ActionHandler action) {
		exceptionRised = true;
		action.setParameter(params);
		action.setSession(session);
		action.setRender(view);
		action.setEnvironments(environments);
		
		ActionAgent.call(action);
		
		exceptionRised = false;
	}
	
	// イベント処理の直後、レンダリング前に呼び出される
	final void renderBeforeFilterAction() {
		exceptionRised = true;
		filterAction(getFilterManager(RENDER_BEFORE_FILTER_NAME));
		exceptionRised = false;
	}

	// レンダリング後に呼び出される
	final void afterFilterAction() {
		filterAction(getFilterManager(AFTER_FILTER_NAME));
	}
	
	private void filterAction(FilterManager fm) {
		fm.stateActionStart(paramsS("action"));
		
		StateListener listener;
		while((listener = fm.nextListener()) != null) {
			if (listener instanceof ExternalStateListener) {
				((ExternalStateListener) listener).execute(params, view, session);
			} else {
				listener.execute();
			}
			
			if (isProcessFinished()) break;
		}
	}
}
