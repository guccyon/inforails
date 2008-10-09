package jp.co.infonic.inforails.framework.session;

import javax.servlet.http.HttpSession;

import jp.co.infonic.inforails.framework.presentation.control.process.ApplicationController;

public class InfoRailsSessionFactory {
	
	// セッションキー
	private static String ATTRIBUTE_KEY = "$InfoRailsSession$";

	public static InfoRailsSession create(HttpSession httpSession, Class<? extends ApplicationController>clazz) {
		SessionObject obj = (SessionObject) httpSession.getAttribute(ATTRIBUTE_KEY);
		if (obj == null) {
			obj = new SessionObject();
			httpSession.setAttribute(ATTRIBUTE_KEY, obj);
		}
		
		return new InfoRailsSession(httpSession, obj, clazz);
	}

}
