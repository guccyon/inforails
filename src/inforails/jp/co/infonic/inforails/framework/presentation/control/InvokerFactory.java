package jp.co.infonic.inforails.framework.presentation.control;

import jp.co.infonic.inforails.base.property.IFactory;
import jp.co.infonic.inforails.framework.presentation.control.process.StandardInvoker;

public class InvokerFactory implements IFactory {
	
	static final String FACTORY_ID = "INVOKER_FACTORY";
	
	public String getId() {
		return FACTORY_ID;
	}
	
	MainInvoker getInvoker(ServiceInfo info) {
		return new StandardInvoker();
	}

}
