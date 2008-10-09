package jp.co.infonic.common.module.log;

/**
 * 
 * �T�v�F���K�[���ʃN���X<br>
 * �ڍׁF���K�[���ʃN���X<br>
 *<pre>
 * [�ύX����]
 * ���t        �A��   ���O      ���e
 * --------------------------------------------------
 * 2006/03/23  VL000  ���      �V�K
 *</pre>
 * @author higuchit
 */
public class CommonLogger {
	
	// �X���b�h���[�J�����
	protected static ThreadLocal<LogCommonInfo> threadLocal = new ThreadLocal<LogCommonInfo>();
	
	/**
	 * 
	 * �T�v: ���O�o�͎��̋��ʏ����Z�b�g���܂��B<br>
	 * �ڍ�: ���O�o�͎��̋��ʏ����Z�b�g���܂��B<br>
	 * ���l: �Ȃ�<br>
	 *
	 * @param commonInfo
	 */
	public static void setCommonInfo(LogCommonInfo commonInfo) {
		threadLocal.set(commonInfo);
	}
	
	/**
	 * 
	 * �T�v: �X���b�h���[�J���ɕێ����Ă��鋤�ʏ����폜���܂��B<br>
	 * �ڍ�: �X���b�h���[�J���ɕێ����Ă��鋤�ʏ����폜���܂��B<br>
	 * ���l: �Ȃ�<br>
	 *
	 */
	public static void removeCommonInfo() {
		threadLocal.set(null);
	}
	
	protected LogCommonInfo commonInfo() {
		return threadLocal.get();
	}
}
