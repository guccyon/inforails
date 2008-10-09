package jp.co.infonic.inforails.framework.presentation.control;

import java.lang.reflect.Field;

import jp.co.infonic.inforails.framework.presentation.control.process.ActionHandler;

public class ActionParameterMapper {

	static void map(ActionHandler action) {
		Field[] fields = action.getClass().getFields();
		
		for (int i = 0; i < fields.length; i++) {
			System.out.println("field Name" + fields[i].getName());
		}
	}
}
