package jp.co.infonic.inforails.framework.presentation.control.process;

import java.io.File;
import java.util.Map;

import jp.co.infonic.common.module.log.DebugLogger;
import jp.co.infonic.common.util.StringUtil;
import jp.co.infonic.inforails.base.property.SystemProperty;
import jp.co.infonic.inforails.framework.presentation.control.Parameter;
import jp.co.infonic.inforails.framework.presentation.view.RenderHandler;
import jp.co.infonic.inforails.framework.session.InfoRailsSession;

import org.apache.log4j.Logger;

public abstract class ActionHandler {
	
	private Parameter params;
	
	private InfoRailsSession session;
	
	private Map<String, String> environments;
	
	protected RenderHandler view;
	
	protected Logger logger = DebugLogger.getLogger(this.getClass());
	
	Parameter getParams() {
		return params;
	}
	
	void setSession(InfoRailsSession session) {
		this.session = session;
	}
	void setParameter(Parameter params) {
		this.params = params;
	}
	void setEnvironments(Map<String, String> environments) {
		this.environments = environments;
	}
	
	public void setRender(RenderHandler view) {
		this.view = view;
	}
	
	protected Object params(String key) {
		return params.get(key);
	}
	
	protected String paramsS(String key) {
		return (String)params.get(key);
	}
	
	protected File paramsF(String key) {
		return params.getFile(key);
	}
	
	protected Object session(String key) {
		return session.get(key);
	}
	
	protected void setSession(String key, Object value) {
		session.set(key, value);
	}
	
	protected String ENV(String key) {
		return environments.get(key);
	}
	
	protected final void setApplicationScopeSession(String key, Object value) {
		session.setForApp(key, value);
	}
	
	public String getViewName() {
		return getViewName(this.getClass());
	}
	
	public static String getViewName(Class<? extends ActionHandler>clazz) {
		return StringUtil.decamelize(getId(clazz));
	}
	
	public String getId() {
		return getId(this.getClass());
	}
	
	public static String getId(Class<? extends ActionHandler>clazz) {
		return clazz.getSimpleName().replaceFirst("Action$", "");
	}
	
	/**
	 * ���N�G�X�g�p�����[�^�̃G���R�[�h���f�t�H���g�l�iUTF-8�j
	 * ���ύX����ꍇ�̓I�[�o�[���C�h����B
	 * @return
	 */
	public String requestEncode() {
		return SystemProperty.defaultCharset();
	}
	
	/**
	 * �T�v: ���̓`�F�b�N���s���܂��B<br>
	 * �ڍ�: ���̓`�F�b�N���s���܂��B<br>
	 * ���l: �Ȃ�<br>
	 * 
	 * @param cbean
	 */
	public abstract void validate();
	
	public abstract void execute() throws Exception;
}
