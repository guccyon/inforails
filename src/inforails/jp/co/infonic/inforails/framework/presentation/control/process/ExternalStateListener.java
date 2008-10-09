package jp.co.infonic.inforails.framework.presentation.control.process;

import jp.co.infonic.inforails.framework.presentation.control.Parameter;
import jp.co.infonic.inforails.framework.presentation.view.RenderHandler;
import jp.co.infonic.inforails.framework.session.InfoRailsSession;

public abstract class ExternalStateListener implements StateListener {
	
	public abstract void execute(Parameter params, RenderHandler view, InfoRailsSession session);

	public void execute() {
		// –¢Žg—p
	}
}