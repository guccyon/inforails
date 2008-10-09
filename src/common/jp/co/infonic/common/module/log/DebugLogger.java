package jp.co.infonic.common.module.log;

import org.apache.log4j.Logger;

/**
 * 
 * �T�v�F�f�o�b�O�p���O���o�͂���ׂ̋@�\��񋟂��܂��B<br>
 * �ڍׁF�f�o�b�O�p���O���o�͂���ׂ̋@�\��񋟂��܂��B<br>
 *<pre>
 * [�ύX����]
 * ���t        �A��   ���O      ���e
 * --------------------------------------------------
 * 2006/03/23  VL000  ���      �V�K
 *</pre>
 * @author higuchit
 */
public class DebugLogger extends CommonLogger {
	
	// ���K�[
	private Logger logger;
	
	// ���K�[�擾�N���X
	private Class cla;
	
	// �f�o�b�O���O�p�J�e�S����
	private static final String CATEGORY_NAME = "Debug.";
	
	private DebugLogger(Class cla) {
		logger = Logger.getLogger(CATEGORY_NAME + cla.getName());
		this.cla = cla;
	}
	
	/**
	 * 
	 * �T�v: ���K�[���擾����B<br>
	 * �ڍ�: ���K�[���擾����B<br>
	 * ���l: �Ȃ�<br>
	 *
	 * @param cla
	 * @return
	 */
	public static Logger getLogger(Class cla) {
		
		DebugLogger log = new DebugLogger(cla == null ? DebugLogger.class : cla);
		
		return log.logger;
	}
	
	/**
	 * 
	 * �T�v: �f�o�b�O���x���Ń��b�Z�[�W���o�͂���B<br>
	 * �ڍ�: �f�o�b�O���x���Ń��b�Z�[�W���o�͂���B<br>
	 * ���l: �Ȃ�<br>
	 *
	 * @param msg
	 */
	public void debug(String msg) {
		logger.debug(createLogMsg(msg));
	}
	
	/**
	 * 
	 * �T�v: �C���t�H���x���Ń��b�Z�[�W���o�͂���B<br>
	 * �ڍ�: �C���t�H���x���Ń��b�Z�[�W���o�͂���B<br>
	 * ���l: �Ȃ�<br>
	 *
	 * @param msg
	 */
	public void info(String msg) {
		logger.info(createLogMsg(msg));
	}
	
	/**
	 * �G���[���x���Ń��O���o�͂���
	 * @param msg
	 */
	public void error(String msg) {
		logger.error(createLogMsg(msg));
	}
	
	/**
	 * 
	 * �T�v: ���O�o�͕�������쐬<br>
	 * �ڍ�: ���O�o�͕�������쐬<br>
	 * ���l: �Ȃ�<br>
	 *
	 * @param msg
	 * @return
	 */
	private String createLogMsg(String msg) {
		
		StringBuffer sb = new StringBuffer();
		
		sb.append(Thread.currentThread().getName()).append(",");
		
		sb.append(cla.getName());
		
		if (commonInfo() != null) {
			sb.append(",").append(commonInfo().toLogFormat());
		}
		
		sb.append(",").append(msg);
		
		return sb.toString();
	}
}
