package jp.co.infonic.inforails.framework.session;

import java.util.HashMap;
import java.util.Map;

import jp.co.infonic.inforails.framework.presentation.control.process.ApplicationController;

/**
 * �Z�b�V�����̎��ԃN���X
 * �A�v���P�[�V�����A�R���g���[���[�A�V�X�e���̊e�X�R�[�v����
 * �Z�b�V�����p�����[�^��ێ�����B
 * @author higuchi
 *
 */
public class SessionObject {

	// �A�v���P�[�V�����X�R�[�v�Z�b�V�������
	private Map <String, Object> application;
	
	// �R���g���[���[�X�R�[�v�Z�b�V�������
	private Map <Class<? extends ApplicationController>, Map<String, Object>> contoroller;
	
	// �V�X�e�����
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
