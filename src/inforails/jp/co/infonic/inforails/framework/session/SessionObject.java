package jp.co.infonic.inforails.framework.session;

import java.util.HashMap;
import java.util.Map;

import jp.co.infonic.inforails.framework.presentation.control.process.ApplicationController;

/**
 * セッションの実態クラス
 * アプリケーション、コントローラー、システムの各スコープ毎の
 * セッションパラメータを保持する。
 * @author higuchi
 *
 */
public class SessionObject {

	// アプリケーションスコープセッション情報
	private Map <String, Object> application;
	
	// コントローラースコープセッション情報
	private Map <Class<? extends ApplicationController>, Map<String, Object>> contoroller;
	
	// システム情報
	private Map <String, String> system = new HashMap<String, String>();
	
	public SessionObject() {
		this.resetApplication();
		this.resetController();
	}
	
	void resetApplication() {
		application = new HashMap<String, Object>();
	}
	
	void resetController() {
		contoroller = new HashMap<Class<? extends ApplicationController>, Map<String, Object>>();
	}
	
	Map <String, Object> getApplicationScope() {
		return application;
	}
	
	Map<Class<? extends ApplicationController>, Map<String, Object>> getContorollerScope() {
		return contoroller;
	}
	
	Map<String, String> getSystem() {
		return system;
	}
}
