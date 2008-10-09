package jp.co.infonic.inforails.framework.presentation.control.process;

import jp.co.infonic.common.util.StringUtil;


public class DefaultAction extends ActionHandler {

	
	public String getViewName() {
		return 	StringUtil.decamelize(paramsS("action"));
	}
	
	@Override
	public void execute() throws Exception {
	}

	@Override
	public void validate() {
	}

}
