package jp.co.infonic.inforails.framework.session;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpSession;

/**
 * 
 * �T�v�F���O�C�����L���ȃZ�b�V�����@�\��񋟂��܂��B<br>
 * �ڍׁF���O�C�����L���ȃZ�b�V�����@�\��񋟂��܂��B<br>
 *       �ʏ�̃Z�b�V�����Ɠ����ł��B<br>
 * @author higuchit
 */
public class ApplicationScopeSession extends SessionWrapper {
	
	private Map<String, Object> attribute;
	
	private Map<String, String> sysinfo; 
	
	/**
	 * �R���X�g���N�^
	 * @param session
	 */
	public ApplicationScopeSession(HttpSession httpSession, SessionObject session) {
		super(httpSession);
		attribute = session.getApplicationScope();
		sysinfo = session.getSystem();
	}
	
	/**
	 * 
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
	
	/**
	 * 
	 * �T�v: �Z�b�V��������I�u�W�F�N�g���擾����B<br>
	 * �ڍ�: �Z�b�V��������I�u�W�F�N�g���擾����B<br>
	 * ���l: �Ȃ�<br>
	 *
	 * @param key
	 * @return
	 */
	public Object get(String key) {
		return attribute.get(key);
	}
	
	/**
	 * 
	 * �T�v: �Z�b�V��������s�v�ɂȂ����I�u�W�F�N�g���폜���܂��B<br>
	 * �ڍ�: �Z�b�V��������s�v�ɂȂ����I�u�W�F�N�g���폜���܂��B<br>
	 * ���l: �Ȃ�<br>
	 *
	 * @param key
	 */
	public void remove(String key) {
		attribute.remove(key);
	}
	
	/**
	 * �A�v���P�[�V�����X�R�[�v�ɓo�^����Ă���Z�b�V�������̃L�[�ꗗ���擾
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
	 * �T�v: �Z�b�V������j�����܂��B<br>
	 * �ڍ�: �Z�b�V������j�����܂��B<br>
	 * ���l: �Ȃ�<br>
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
