package jp.co.infonic.inforails.framework.presentation.control;

import jp.co.infonic.inforails.framework.presentation.control.process.ActionHandler;


public class ActionAgent {

	public static void call(ActionHandler action) {
		try {
			ActionParameterMapper.map(action);
			
			action.validate();
			
			action.execute();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
