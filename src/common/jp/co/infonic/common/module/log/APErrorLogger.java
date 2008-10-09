package jp.co.infonic.common.module.log;

import jp.co.infonic.inforails.base.exception.SystemException;

import org.apache.log4j.Logger;

/**
 * 
 * �T�v�F�G���[���O���o�͂��܂��B<br>
 * �ڍׁF�G���[���O���o�͂��܂��B<br>
 *<pre>
 * [�ύX����]
 * ���t        �A��   ���O      ���e
 * --------------------------------------------------
 * 2006/04/02  VL000  ���      �V�K
 *</pre>
 * @author Tetsuro
 */
public class APErrorLogger extends CommonLogger {

	// ���K�[
	private Logger logger;

	// �f�o�b�O���O�p�J�e�S����
	private static final String CATEGORY_NAME = "APError.";

	// �V�X�e�����s�R�[�h
	private static final String LINE_SEP = System.getProperty("line.separator");

	// ���O�Z�p���[�^
	private static final String LOG_SEP = "------------------------------------------------------------------------------";

	/**
	 * �v���C�x�[�g�R���X�g���N�^
	 * 
	 * @param cla
	 */
	private APErrorLogger(String categoryName) {

		logger = Logger.getLogger(CATEGORY_NAME + categoryName);

	}

	/**
	 * 
	 * �T�v: ���K�[�̎擾���s���܂��B<br>
	 * �ڍ�: ���K�[�̎擾���s���܂��B<br>
	 * ���l: �Ȃ�<br>
	 * 
	 * @param cla
	 * @return
	 */
	public static APErrorLogger getLogger() {

		return getLogger(APErrorLogger.class.getName());

	}

	/**
	 * 
	 * �T�v: ���K�[�̎擾���s���܂��B�J�e�S���ʂɏo�͂������ꍇ�Ɉ�����^���܂��B<br>
	 * �ڍ�: ���K�[�̎擾���s���܂��B�J�e�S���ʂɃ��O�̏o�͐�����s�������ꍇ�Ɉ�����^���܂��B
	 *       �v���p�e�B�t�@�C���ŃJ�e�S�����L�q����ۂ�APError.�����J�e�S�����Ŏw�肵�܂��B<br>
	 * ���l: �Ȃ�<br>
	 * 
	 * @param cla
	 * @return
	 */
	public static APErrorLogger getLogger(String categoryName) {
		
		if (categoryName == null) {
			categoryName = APErrorLogger.class.getName();
		}
		
		APErrorLogger log = new APErrorLogger(categoryName);

		return log;

	}

	/**
	 * 
	 * �T�v: �G���[���O�̏o�͂��s���܂��B<br>
	 * �ڍ�: �G���[���O�̏o�͂��s���܂��B<br>
	 * ���l: �Ȃ�<br>
	 * 
	 * @param className
	 * @param methodName
	 * @param debugInfo
	 * @param noticeFlg
	 * @param noticeMsg
	 * @param excep
	 */
	public void outputErrorLog(String className, String methodName,
			String debugInfo, boolean noticeFlg, String noticeMsg,
			Throwable excep) {

		StringBuffer sb = new StringBuffer();
		editErrorLogMsg(sb, className, methodName, debugInfo, noticeFlg,
				noticeMsg);
		sb.append(LINE_SEP);

		if (excep != null) {
			if (excep instanceof SystemException) {

				SystemException sysExc = (SystemException) excep;
				Throwable th = sysExc.getCause();
				if (th != null) {
					sb.append(LINE_SEP);
				}

				while (th != null) {
					editStackTrace(sb, th);
					th = th.getCause();
				}
			} else {
				sb.append(LOG_SEP);
				logger.error(sb.toString(), excep);
				return;
			}
		}

		sb.append(LOG_SEP);
		logger.error(sb.toString());

		if (noticeFlg) {
			noticeMsg(sb);
		}
	}

	/**
	 * 
	 * �T�v: �Ǘ��҂֒ʒm���s���܂��B<br>
	 * �ڍ�: �Ǘ��҂֒ʒm���s���܂��B�i�������j<br>
	 * ���l: �Ȃ�<br>
	 * 
	 * @param log
	 */
	private void noticeMsg(StringBuffer log) {

	}

	/**
	 * 
	 * �T�v: �G���[���O�p���b�Z�[�W��ҏW���܂��B<br>
	 * �ڍ�: �G���[���O�p���b�Z�[�W��ҏW���܂��B<br>
	 * ���l: �Ȃ�<br>
	 * 
	 * @param sb
	 * @param className
	 * @param methodName
	 * @param debugInfo
	 * @param noticeFlg
	 * @param noticeMsg
	 * @return
	 */
	private StringBuffer editErrorLogMsg(StringBuffer sb, String className,
			String methodName, String debugInfo, boolean noticeFlg,
			String noticeMsg) {
		if (methodName == null) {
			methodName = "";
		}
		if (debugInfo == null) {
			debugInfo = "";
		}
		if(noticeMsg == null) {
			noticeMsg = "";
		}

		sb.append(className).append(",");
		sb.append(methodName).append(",");
		sb.append(debugInfo).append(",");

		if (commonInfo() != null) {
			sb.append(",").append(commonInfo().toLogFormat());
		}

		sb.append(noticeFlg).append(",");
		sb.append(noticeMsg);
		return sb;
	}

	/**
	 * 
	 * �T�v: ��O�g���[�X��ҏW����B<br>
	 * �ڍ�: ��O�g���[�X��ҏW����B<br>
	 * ���l: �Ȃ�<br>
	 * 
	 * @param sb
	 * @param th
	 * @return
	 */
	private StringBuffer editStackTrace(StringBuffer sb, Throwable th) {

		// ��O���b�Z�[�W��ҏW
		sb.append(th.getClass().getName()).append(th.getMessage()).append(
				LINE_SEP);

		// ��O�ڍ׃g���[�X�擾
		StackTraceElement[] element = th.getStackTrace();
		for (int i = 0; i < element.length; i++) {

			// ��O�ڍ׃g���[�X�ҏW
			sb.append("\tat ").append(element[i]).append(LINE_SEP);
		}

		// �ҏW�o�b�t�@��ԋp
		return sb;
	}

}
