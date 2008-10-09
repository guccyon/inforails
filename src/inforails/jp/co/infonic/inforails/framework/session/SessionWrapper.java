package jp.co.infonic.inforails.framework.session;

import javax.servlet.http.HttpSession;

/**
 * 
 * �T�v�FHttpSession�����b�v����N���X<br>
 * �ڍׁFHttpSession�����b�v����N���X
 *       �Z�b�V�����ւ̃A�N�Z�X�͒ʏ킱�̃��b�v�N���X��
 *       �o�R����K�v������܂��B<br>
 *<pre>
 * [�ύX����]
 * ���t        �A��   ���O      ���e
 * --------------------------------------------------
 * 2006/03/24  VL000  ���      �V�K
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
	 * �T�v: �Z�b�V�����h�c�̎擾<br>
	 * �ڍ�: �Z�b�V�����h�c�̎擾<br>
	 * ���l: �Ȃ�<br>
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
