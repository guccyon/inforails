package jp.co.infonic.inforails.framework.session;

import javax.servlet.http.HttpSession;

/**
 * 
 * 概要：HttpSessionをラップするクラス<br>
 * 詳細：HttpSessionをラップするクラス
 *       セッションへのアクセスは通常このラップクラスを
 *       経由する必要があります。<br>
 *<pre>
 * [変更履歴]
 * 日付        連番   名前      内容
 * --------------------------------------------------
 * 2006/03/24  VL000  樋口      新規
 *</pre>
 * @author higuchit
 */
public abstract class SessionWrapper {
	
	// HttpSession
	private HttpSession session;

	protected SessionWrapper(HttpSession session) {
		this.session = session;
		
	}

	/**
	 * 
	 * 概要: セッションＩＤの取得<br>
	 * 詳細: セッションＩＤの取得<br>
	 * 備考: なし<br>
	 *
	 * @return
	 */
	public String getId() {
		return session.getId();
	}
	
	public long getLastAccessedTime() {
		return session.getLastAccessedTime();
	}

	public void setMaxInactiveInterval(int arg0) {
		session.setMaxInactiveInterval(arg0);
		
	}

	public int getMaxInactiveInterval() {
		return session.getMaxInactiveInterval();
	}

	public boolean isNew() {
		return session.isNew();
	}
	
	protected void invalidate() {
		session.invalidate();
	}
}
