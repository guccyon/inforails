package jp.co.infonic.inforails.framework.session;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import jp.co.infonic.inforails.framework.presentation.control.process.ApplicationController;

/**
 * �e�R���g���[���[�A�C�x���g�������ł̃Z�b�V�����ւ̃A�N�Z�T��񋟂���N���X�B
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
	 * �T�v: �Z�b�V�����ɃI�u�W�F�N�g���Z�b�g����B<br>
	 * �ڍ�: �Z�b�V�����ɃI�u�W�F�N�g���Z�b�g����B<br>
	 * ���l: �Ȃ�<br>
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
	 * �Z�b�V��������I�u�W�F�N�g���擾����B<br>
	 * key�ɑΉ�����l���R���g���[���[�X�R�[�v�ɑ��݂��Ȃ��ꍇ��
	 * �A�v���P�[�V�����X�R�[�v���Q�Ƃ���B
	 * 
	 * ���l: �Ȃ�<br>
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
	 * �T�v: �Z�b�V��������I�u�W�F�N�g���폜����B<br>
	 * �ڍ�: �Z�b�V��������I�u�W�F�N�g���폜����B<br>
	 * ���l: �Ȃ�<br>
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
