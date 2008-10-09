package jp.co.infonic.inforails.framework.presentation.control.process;

import jp.co.infonic.inforails.base.property.SystemProperty;
import jp.co.infonic.inforails.framework.presentation.control.MainInvoker;
import jp.co.infonic.inforails.framework.presentation.control.ViewDispatcher;

public class StandardInvoker extends MainInvoker {

	protected void invoke(ProcessHolder holder, ViewDispatcher dispatcher) {

		holder.controller().setParameter(holder.parameter());
		holder.controller().setSession(holder.session());
		holder.controller().setRender(dispatcher.getRenderHander());
		holder.controller().setEnvironments(
				SystemProperty.getPropertiesForController(holder.controller().getClass()));
		
		holder.controller().beforeFilterAction();

		try {
			
			if (!holder.controller().isProcessFinished()) {
				holder.controller().process(holder.action());	
			}

			if (!holder.controller().isProcessFinished()) {
				holder.controller().renderBeforeFilterAction();
			}
 
			dispatcher.render(holder);
			
		} finally {
			if (holder.controller().isNotStatusError()) {
				holder.controller().afterFilterAction();
			}
		}
	}
}
