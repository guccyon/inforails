package jp.co.infonic.common.module.log;

import org.apache.log4j.Logger;

/**
 * 
 * �T�v�FDB�A�N�Z�XSQL���s�󋵃��K�[<br>
 * �ڍׁFDB�A�N�Z�XSQL���s�󋵃��K�[<br>
 *<pre>
 * [�ύX����]
 * ���t        �A��   ���O      ���e
 * --------------------------------------------------
 * 2006/03/23  VL000        �V�K
 *</pre>
 * @author higuchit
 */
public class DBAccessLogger extends CommonLogger {
	
	// ���K�[
	private Logger logger;
	
	// ���K�[�擾�N���X
	private Class cla;
	
	// �f�o�b�O���O�p�J�e�S����
	private static final String CATEGORY_NAME = "DBAccess.";
	
	/**
	 * �v���C�x�[�g�R���X�g���N�^
	 * @param cla
	 */
	private DBAccessLogger(Class cla) {
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
	public static DBAccessLogger getLogger(Class cla) {
		if (cla == null) {
			cla = DBAccessLogger.class;
		}
		
		DBAccessLogger log = new DBAccessLogger(cla);
		
		return log;
	}
	
	/**
	 * 
	 * �T�v: �f�o�b�O���x���Ń��b�Z�[�W���o�͂���B<br>
	 * �ڍ�: �f�o�b�O���x���Ń��b�Z�[�W���o�͂���B<br>
	 * ���l: �Ȃ�<br>
	 *
	 * @param msg
	 */
	public void debug(String sql, long startTime, long endTime) {
		logger.debug(createLogMsg(sql, startTime, endTime));
	}
	
	/**
	 * 
	 * �T�v: �C���t�H���x���Ń��b�Z�[�W���o�͂���B<br>
	 * �ڍ�: �C���t�H���x���Ń��b�Z�[�W���o�͂���B<br>
	 * ���l: �Ȃ�<br>
	 *
	 * @param msg
	 */
	public void info(String sql, long startTime, long endTime) {
		logger.info(createLogMsg(sql, startTime, endTime));
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
	private String createLogMsg(String sql, long startTime, long endTime) {
		
		// ���s����
		long procTime = endTime - startTime;
		
		StringBuffer sb = new StringBuffer();
		
		sb.append(Thread.currentThread().getName()).append(",");
		
		sb.append(cla.getName());
		
		if (commonInfo() != null) {
			sb.append(",").append(commonInfo().toLogFormat());
		}
		sb.append(",").append(startTime);
		sb.append(",").append(endTime);
		sb.append(",").append(procTime);
		sb.append(",").append(sql);
		
		return sb.toString();
	}

}
