package jp.co.infonic.inforails.framework.presentation.control.process;

import jp.co.infonic.inforails.framework.presentation.control.Parameter;
import jp.co.infonic.inforails.framework.presentation.view.RenderHandler;
import jp.co.infonic.inforails.framework.session.InfoRailsSession;

public class ProcessHolder {

	ApplicationController controller_;
	
	ActionHandler action_;
	
	InfoRailsSession session_;
	
	Parameter parameter_;
	
	RenderHandler view;
	
	public void setController(ApplicationController controller) {
		controller_ = controller;
	}
	
	public void setAction(ActionHandler action) {
		action_ = action;
	}
	
	public void setSession(InfoRailsSession session) {
		session_ = session;
	}
	
	public void setParameter(Parameter parameter) {
		parameter_ = parameter;
	}
	
	public ApplicationController controller() {
		return controller_;
	}
	
	public ActionHandler action() {
		return action_;
	}
	
	public InfoRailsSession session() {
		return session_;
	}
	
	public Parameter parameter() {
		return parameter_;
	}
	
	public void setRender(RenderHandler view) {
		this.view = view;
	}
	
	public RenderHandler view() {
		return view;
	}
}
