package jp.co.infonic.inforails.framework.session;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpSession;

/**
 * 
 * 概要：ログイン中有効なセッション機能を提供します。<br>
 * 詳細：ログイン中有効なセッション機能を提供します。<br>
 *       通常のセッションと同じです。<br>
 * @author higuchit
 */
public class ApplicationScopeSession extends SessionWrapper {
	
	private Map<String, Object> attribute;
	
	private Map<String, String> sysinfo; 
	
	/**
	 * コンストラクタ
	 * @param session
	 */
	public ApplicationScopeSession(HttpSession httpSession, SessionObject session) {
		super(httpSession);
		attribute = session.getApplicationScope();
		sysinfo = session.getSystem();
	}
	
	/**
	 * 
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
	
	/**
	 * 
	 * 概要: セッションからオブジェクトを取得する。<br>
	 * 詳細: セッションからオブジェクトを取得する。<br>
	 * 備考: なし<br>
	 *
	 * @param key
	 * @return
	 */
	public Object get(String key) {
		return attribute.get(key);
	}
	
	/**
	 * 
	 * 概要: セッションから不要になったオブジェクトを削除します。<br>
	 * 詳細: セッションから不要になったオブジェクトを削除します。<br>
	 * 備考: なし<br>
	 *
	 * @param key
	 */
	public void remove(String key) {
		attribute.remove(key);
	}
	
	/**
	 * アプリケーションスコープに登録されているセッション情報のキー一覧を取得
	 * @return
	 */
	public String[] getKeys() {
		Set<String> keyset = attribute.keySet();
		if (keyset != null) {
			return (String[])keyset.toArray(new String[0]);
		}
		
		return new String[0];
	}
	
	public Set<Entry<String, Object>> getEntry() {
		return attribute.entrySet();
	}

	/**
	 * 
	 * 概要: セッションを破棄します。<br>
	 * 詳細: セッションを破棄します。<br>
	 * 備考: なし<br>
	 *
	 */
	public void invalidate() {
		super.invalidate();
		this.reset();
	}
	
	public void reset() {
		attribute = new HashMap<String, Object>();
	}
	
	public Map<String, Object> getAll() {
		return new HashMap<String, Object>(attribute);
	}
}
