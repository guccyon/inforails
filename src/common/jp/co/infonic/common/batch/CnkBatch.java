package jp.co.infonic.common.batch;

import java.sql.SQLException;
import java.util.Date;

import jp.co.infonic.common.db.jdbc.DBConnection;
import jp.co.infonic.common.db.jdbc.DataSourceManager;
import jp.co.infonic.common.module.log.BatchLogger;

public abstract class CnkBatch implements Runnable {
	
	// ������s���Ԃ܂ł̎c�莞�ԁi���j
	private int timer;
	
	// �������s�����t���O
	private boolean synchronous = true;
	
	// ���s�Ԋu�i���j
	private int period = 1;
	
	// �O����s���ԁi�~���b�j
	private long lastTime;
	
	// �^�X�N�L���t���O
	private boolean processing = false;
	
	// ���s��ID
	private String id;
	
	// ���s��ID���Z�b�g����B
	final void setId(String id) {
		this.id = id;
	}
	
	/**
	 * �T�v: ���݂����s�\���ǂ�����Ԃ��t���O<br>
	 * �ڍ�: ���݂����s�\���ǂ�����Ԃ��t���O<br>
	 *       true��Ԃ����ꍇ�A���������s�����B<br>
	 *       �I�[�o�[���C�h���鎖�Œ�����s�ȊO��<br>
	 *       ���t�w����s�����s����悤�ɐݒ�ł���B<br>
	 * ���l: �Ȃ�<br>
	 *
	 * @param current
	 * @return
	 */
	boolean isExeTime(long current) {
		return timer <= 0;
	}
	
	// ���s���ԃ^�C�}�[�̃J�E���g�_�E��
	final void countdown() {
		timer--;
	}
	
	// ���������t���O��Ԃ�
	final boolean isSynchronous() {
		return synchronous;
	}

	/**
	 * �T�v: ���̃^�X�N�̎��s���J�n���܂��B<br>
	 * �ڍ�: ���̃^�X�N�̎��s���J�n���܂��B<br>
	 *       ���s�^�C�}�[��period�ŏ���������܂��B<br>
	 * ���l: �Ȃ�<br>
	 */
	public final void start() {
		processing = true;
	}

	/**
	 * �T�v: ���̃^�X�N�̎��s���ꎞ��~���܂��B<br>
	 * �ڍ�: ���̃^�X�N�̎��s���ꎞ��~���܂��B<br>
	 * ���l: �Ȃ�<br>
	 */
	public final void pause() {
		processing = false;
	}
	
	/**
	 * �T�v: ���s��ID��Ԃ��B<br>
	 * �ڍ�: ���s��ID��Ԃ��B<br>
	 * ���l: �Ȃ�<br>
	 * @return
	 */
	public final String getId() {
		return id;
	}

	/**
	 * �T�v: �O����s���Ԃ�Ԃ��B <br>
	 * �ڍ�: �O����s���Ԃ���t�^�Ƃ��ĕԂ��B <br>
	 * ���l: �Ȃ�<br>
	 */
	public Date getLasttime() {
		if (lastTime == 0) {
			return null;
		}
		return new Date(lastTime);
	}

	/**
	 * �T�v: ���s���Ԃ̊Ԋu��Ԃ��B<br>
	 * �ڍ�: ���s���Ԃ̊Ԋu��Ԃ��B<br>
	 *       �P�ʂ͕��B<br>
	 * ���l: �Ȃ�<br>
	 */
	public int getPeriod() {
		return period;
	}
	
	/**
	 * 
	 * �T�v: ���s�Ԋu�̕�����\����Ԃ��B<br>
	 * �ڍ�: ���s�Ԋu�̕�����\����Ԃ��B<br>
	 *       �����ȊO�Ŏ��s����ꍇ�A�����\�b�h��<br>
	 *       �I�[�o�[���C�h����B<br>
	 * ���l: �Ȃ�<br>
	 *
	 * @return
	 */
	public String getTiming() {
		return period + "����";
	}

	/**
	 * �T�v: ������s���Ԃ܂ł̎c�莞�Ԃ�Ԃ��B<br>
	 * �ڍ�: ������s���Ԃ܂ł̎c�莞�Ԃ�Ԃ��B<br>
	 * ���l: �Ȃ�<br>
	 */
	public int getNextTime() {
		return timer;
	}

	/**
	 * �T�v: ������s���Ԃ܂ł̎��Ԃ��Đݒ肷��B<br>
	 * �ڍ�: ������s���Ԃ܂ł̎��Ԃ��Đݒ肷��B<br>
	 *       �P�ʂ͕��B<br>
	 * ���l: �Ȃ�<br>
	 */
	public void setNextTime(int minute) {
		timer = minute;
	}

	/**
	 * �T�v: ���s�Ԋu���Đݒ肷��B<br>
	 * �ڍ�: ���s�Ԋu���Đݒ肷��B<br>
	 *       �P�ʂ͕��B<br>
	 * ���l: �Ȃ�<br>
	 */
	public void setPeriod(int minute) {
		period = minute;
	}

	/**
	 * �T�v: ���ݎ��s�����ǂ�����Ԃ��B<br>
	 * �ڍ�: ���ݎ��s�����ǂ�����Ԃ��B<br>
	 *       true  �ˁ@���s��<br>
	 *       false �ˁ@��~��<br>
	 * ���l: �Ȃ�<br>
	 */
	public boolean isProcessing() {
		return processing;
	}
	
	/**
	 * �o�b�`�������s���ʂ�Ԃ��B
	 * @return
	 */
	public int getResult() {
		return result;
	}

	/**
	 * ���^�X�N�̎��s��ꃁ�\�b�h
	 */
	public void run() {
		
		BatchLogger logger = null;
		try {
			logger = BatchLogger.getLogger(this.getClass());
			logger.start();
			
			result = execute();
			
		} catch (Exception e) {
			logger.error(e);
			result = STATUS_ERROR;
		} finally {
			
			lastTime = System.currentTimeMillis();
		}
		if (logger != null) {
			logger.end(result);
		}
	}
	
	// ��������
	private int result;

	/**
	 * 
	 * �T�v: ������s���Ԃ܂ł̃^�C�}�[�����Z�b�g����B<br>
	 * �ڍ�: ������s���Ԃ܂ł̃^�C�}�[�����Z�b�g����B<br>
	 * ���l: �Ȃ�<br>
	 */
	protected void reset() {
		timer = period;
	}
	
	/**
	 * �R���X�g���N�^
	 * @param period ���s�Ԋu
	 */
	public CnkBatch(int period) {
		if (period < 1) {
			throw new IllegalArgumentException("���s�Ԋu�͂P�ȏ�łȂ���΂����܂���B");
		}
		this.period = period;
		this.timer = period;
	}
	
	/**
	 * �R���X�g���N�^
	 * @param name �o�b�`��
	 * @param period ���s�Ԋu
	 * @param synchronous �������s�t���O true:�������� false:�񓯊�����
	 */
	public CnkBatch(int period, boolean synchronous) {
		this(period);
		this.synchronous = synchronous;
	}

	/**
	 * �T�v: �o�b�`����Ԃ��B<br>
	 * �ڍ�: �o�b�`����Ԃ��B<br>
	 * ���l: �Ȃ�<br>
	 */
	public abstract String getName();
	
	/**
	 * 
	 * �T�v: �o�b�`�̐�����Ԃ��B<br>
	 * �ڍ�: �o�b�`�̐�����Ԃ��B<br>
	 * ���l: �Ȃ�<br>
	 *
	 * @return
	 */
	public abstract String getDescription();
	
	/**
	 * �T�v: ���C������<br>
	 * �ڍ�: ���C������<br>
	 * ���l: �Ȃ�<br>
	 *
	 * @return
	 */
	protected abstract int execute() throws Exception;
	
	/**
	 * �T�v: �o�b�`�pDB�R�l�N�V�����擾<br>
	 * �ڍ�: �o�b�`�pDB�R�l�N�V�����擾<br>
	 * ���l: �Ȃ�<br>
	 *
	 * @return
	 * @throws SQLException 
	 */
	protected DBConnection getConnection(boolean autoCommit) throws SQLException {
		if (onServer) {
			return DataSourceManager.getConnection(this.getClass(), autoCommit);
		} else {
			return BatchDetaSourceManager.getConnection(autoCommit);
		}
	}

	/**
	 * 
	 * �T�v: ���C���֐�<br>
	 * �ڍ�: AP�T�[�o�ƕʃv���Z�X�Ƃ��Ď��s����ꍇ�̊�ꃁ�\�b�h<br>
	 * ���l: �Ȃ�<br>
	 *
	 * @param args
	 * @see jp.co.infonic.cnksystem.parts.schedule.CnkBatch#main(java.lang.String[])
	 */
	public static void main(String[] args) {
		
		if (args.length < 1) {
			System.out.print("usage: java CnkBatch batchname...");
			System.exit(0);
		}
		
		int status;
		BatchLogger logger = null;
		try {
			CnkBatch batch = (CnkBatch)Class.forName(args[0]).newInstance();
			batch.onServer = false;
			logger = BatchLogger.getLogger(batch.getClass());
			logger.start();
			
			status = batch.execute();
			
		} catch (Exception e) {
			status = 1;
			if (logger != null) {
				logger.exception(e);
			}
		}
		
		if (logger != null) {
			logger.end(status);
		}
		
		System.exit(status);
	}
	
	protected boolean onServer = true;
	
	// ��������:OK
	public final static int STATUS_NORMAL = 0;
	
	// ��������:NG
	public final static int STATUS_ERROR = 1;
}
