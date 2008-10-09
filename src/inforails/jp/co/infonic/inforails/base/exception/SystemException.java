package jp.co.infonic.inforails.base.exception;

import jp.co.infonic.common.module.log.APErrorLogger;


/**
 * 
 * �T�v�FCNKSYSTEM�V�X�e���G���[<br>
 * �ڍׁFCNKSYSTEM���ŋN�������S�Ă̑z��O��O��������throw����܂��B<br>
 *<pre>
 * [�ύX����]
 * ���t        �A��   ���O      ���e
 * --------------------------------------------------
 * 2006/07/02  VL000  ���      �V�K
 *</pre>
 */
public class SystemException extends RuntimeException {
	
	private static final long serialVersionUID = 9039802049377902246L;

	// �G���[�����N���X��
	private String className = "";
	
	// �G���[�������\�b�h��
	private String methodName = "";
	
	// �G���[��͗p���
	private String debugInfo = "";
	
	// �Ǘ��Ғʒm�t���O
	private boolean noticeFlg;
	
	// �Ǘ��Ғʒm���b�Z�[�W
	private String noticeMsg = "";
	
	// ���[�g��O
	private Throwable cause;
	
	// null�t�B�[���h�u������������
	private static final String DEFAULT = "���ݒ�";
	
	/**
	 * �R���X�g���N�^�i�Ǘ��Ғʒm�����j
	 * @param e
	 * @param className
	 * @param methodName
	 * @param debugInfo
	 */
	public SystemException (Exception e, String className, String methodName, String debugInfo) {
		this(e, className, methodName, debugInfo, false, "");
	}
	
	/**
	 * 
	 * @param e
	 * @param className
	 * @param methodName
	 * @param debugInfo
	 * @param noticeFlg
	 * @param noticeMsg
	 */
	public SystemException(Exception e, String className, String methodName, String debugInfo, boolean noticeFlg, String noticeMsg) {
		this.cause = e;
		this.className = className;
		this.methodName = methodName;
		this.debugInfo = debugInfo;
		this.noticeFlg = noticeFlg;
		this.noticeMsg = noticeMsg;
		
		checkNull();
	}
	
	/**
	 * 
	 * �T�v: �G���[���O���o�͂��܂��B<br>
	 * �ڍ�: �G���[���O���o�͂��܂��B<br>
	 * ���l: �Ȃ�<br>
	 *
	 * @param cla
	 */
	public void outputErrorInfo() {
		APErrorLogger logger = APErrorLogger.getLogger();
		
		logger.outputErrorLog(className, methodName, debugInfo, noticeFlg, noticeMsg, this);
	}
	
	/**
	 * 
	 * �T�v: �`�F�C������Ă����O���擾���܂��B<br>
	 * �ڍ�: �`�F�C������Ă����O���擾���܂��B<br>
	 * ���l: �Ȃ�<br>
	 *
	 * @return
	 * @see java.lang.Throwable#getCause()
	 */
	public Throwable getCause() {
		return cause;
	}
	
	/**
	 * 
	 * �T�v: �t�B�[���h�ɐݒ肳�ꂽ�l��null���܂܂�Ȃ����`�F�b�N���܂��B<br>
	 * �ڍ�: �t�B�[���h�ɐݒ肳�ꂽ�l��null���܂܂�Ȃ����`�F�b�N���܂��B
	 *       ���[�g��O�ɂ��Ă�null�������܂��B<br>
	 * ���l: �Ȃ�<br>
	 *
	 */
	private void checkNull() {
		if (className == null) {
			className = DEFAULT;
		}
		if (methodName == null) {
			methodName = DEFAULT;
		}
		if (debugInfo == null) {
			debugInfo = DEFAULT;
		}
		if (noticeMsg == null) {
			noticeMsg = DEFAULT;
		}
	}
}
