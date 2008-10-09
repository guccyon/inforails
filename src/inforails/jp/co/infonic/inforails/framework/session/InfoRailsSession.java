package jp.co.infonic.inforails.framework.session;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import jp.co.infonic.inforails.framework.presentation.control.process.ApplicationController;

/**
 * 各コントローラー、イベント処理内でのセッションへのアクセサを提供するクラス。
 * @author higuchit
 *
 */
public class InfoRailsSession extends ApplicationScopeSession{
	
	private Map<String, Object> attribute;
	
	private Map<Class<? extends ApplicationController>, Map<String, Object>> session;
	
	InfoRailsSession(HttpSession httpSession, SessionObject session, Class<? extends ApplicationController>clazz) {
		super(httpSession, session);
		this.session = session.getContorollerScope();
		this.attribute = getControllerScope(clazz);
	}
	
	
	private Map<String, Object> getControllerScope(Class<? extends ApplicationController> clazz) {
		Map<String, Object> result = session.get(clazz);
		if (result == null) {
			result = new HashMap<String, Object>();
			session.put(clazz, result);
		}
		return result;
	}
	
	/**
	 * 概要: セッションにオブジェクトをセットする。<br>
	 * 詳細: セッションにオブジェクトをセットする。<br>
	 * 備考: なし<br>
	 *
	 * @param key
	 * @param value
	 */
	public void set(String key, Object value) {
		attribute.put(key, value);
	}
	
	public void setForApp(String key, Object value) {
		super.set(key, value);
	}
	
	/**
	 * セッションからオブジェクトを取得する。<br>
	 * keyに対応する値がコントローラースコープに存在しない場合は
	 * アプリケーションスコープより参照する。
	 * 
	 * 備考: なし<br>
	 *
	 * @param key
	 * @return
	 */
	public Object get(String key) {
		Object obj = attribute.get(key);
		return obj == null ? super.get(key) : obj;
	}
	
	public Object getForController(String key, Class <? extends ApplicationController> clazz) {
		if (session.containsKey(clazz)) {
			return getControllerScope(clazz).get(key);
		}
		return null;
	}
	
	/**
	 * 
	 * 概要: セッションからオブジェクトを削除する。<br>
	 * 詳細: セッションからオブジェクトを削除する。<br>
	 * 備考: なし<br>
	 *
	 * @param key
	 */
	public void remove(String key) {
		attribute.remove(key);
	}
	
	public Map<String, Object> getAll() {
		Map<String, Object> result = new HashMap<String, Object>(attribute);
		result.put("AP", super.getAll());
		return result;
	}
}
