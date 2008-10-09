package jp.co.infonic.common.module.log;

import java.util.LinkedList;
import java.util.List;

import jp.co.infonic.common.util.ArrayUtil;

import org.apache.log4j.Logger;

/**
 * 
 * �T�v�F�o�b�`���K�[<br>
 * �ڍׁF�o�b�`���K�[<br>
 *<pre>
 * [�ύX����]
 * ���t        �A��   ���O      ���e
 * --------------------------------------------------
 * 2006/10/01  VL000  ���      �V�K
 *</pre>
 * @author higuchit
 */
public class BatchLogger {
	
	// �o�b�`���O�p�J�e�S����
	private static final String CATEGORY_NAME = "Batch.";
	
	/**
	 * �T�v: ���K�[���擾����B<br>
	 * �ڍ�: ���K�[���擾����B<br>
	 * ���l: �Ȃ�<br>
	 *
	 * @param cla
	 * @return
	 */
	public static BatchLogger getLogger(Class batchClass) {
		return new BatchLogger(batchClass);
	}
	
	private String name;
	
	// ���K�[
	private Logger logger;
	
	// �o�b�`�J�n����
	private long starttime;
	
	// �o�b�`�I������
	private long endtime;
	
	private BatchLogger(Class batchClass) {
		Class category = batchClass == null ? BatchLogger.class : batchClass;
		this.logger = Logger.getLogger(CATEGORY_NAME + category.getName());
		this.name = batchClass.getSimpleName();
	}
	
	/**
	 * 
	 * �T�v: �f�o�b�O���x���Ń��b�Z�[�W���o�͂���B<br>
	 * �ڍ�: �f�o�b�O���x���Ń��b�Z�[�W���o�͂���B<br>
	 * ���l: �Ȃ�<br>
	 *
	 * @param msg
	 */
	public void debug(String message) {
		List <String>contents = createLogMsg();
		contents.add(message);
		logger.debug(ArrayUtil.join(contents, ","));
	}
	
	/**
	 * 
	 * �T�v: �f�o�b�O���x���Ń��b�Z�[�W���o�͂���B<br>
	 * �ڍ�: �f�o�b�O���x���Ń��b�Z�[�W���o�͂���B<br>
	 * ���l: �Ȃ�<br>
	 *
	 * @param msg
	 */
	public void debug(String message, Throwable e) {
		List <String>contents = createLogMsg();
		contents.add(message);
		logger.debug(ArrayUtil.join(contents, ","), e);
	}
	
	/**
	 * 
	 * �T�v: �C���t�H���x���Ń��b�Z�[�W���o�͂���B<br>
	 * �ڍ�: �C���t�H���x���Ń��b�Z�[�W���o�͂���B<br>
	 * ���l: �Ȃ�<br>
	 *
	 * @param msg
	 */
	public void info(String message) {
		List <String>contents = createLogMsg();
		contents.add(message);
		logger.info(ArrayUtil.join(contents, ","));
	}
	
	/**
	 * 
	 * �T�v: �G���[�o�͂��s���܂��B<br>
	 * �ڍ�: �G���[�o�͂��s���܂��B<br>
	 * ���l: �Ȃ�<br>
	 *
	 * @param message
	 * @param th
	 */
	public void error(Throwable th) {
		logger.error(th);
	}
	
	/**
	 * �T�v: �o�b�`���O�ɗ�O�̃g���[�X���o�͂��܂��B<br>
	 * �ڍ�: �o�b�`���O�ɗ�O�̃g���[�X���o�͂��܂��B<br>
	 * ���l: �Ȃ�<br>
	 *
	 * @param e
	 */
	public void exception(Throwable e) {
		logger.error(e);
	}
	
	public void start() {
		starttime = System.currentTimeMillis();
		List <String>contents = createLogMsg();
		contents.add("�J�n");
		logger.info(ArrayUtil.join(contents, ","));
	}
	
	/**
	 * 
	 * �T�v: �o�b�`�̏I�����O���o�͂��܂��B<br>
	 * �ڍ�: �o�b�`�̏I�����O���o�͂��܂��B<br>
	 * ���l: �Ȃ�<br>
	 *
	 * @param status
	 */
	public void end(int status) {
		endtime = System.currentTimeMillis();
		List <String>contents = createLogMsg();
		contents.add("�I��");
		contents.add("[result]" + status);
		contents.add("[starttime]" + starttime);
		contents.add("[endtime]" + endtime);
		contents.add("[processtime]" + (endtime - starttime));
		logger.info(ArrayUtil.join(contents, ","));
	}
	
	/**
	 * �T�v: �o�b�`���O���ʃ��b�Z�[�W�𐶐�
	 *
	 * @return
	 */
	private List<String> createLogMsg() {
		List<String> contents = new LinkedList<String>();
		contents.add(this.name);
		contents.add(Thread.currentThread().getName());
		return contents;
	}

}
