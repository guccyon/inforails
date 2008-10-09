package jp.co.infonic.common.batch;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;

/**
 * 
 * �T�v�F�o�b�`�̊Ǘ����s���N���X<br>
 * �ڍׁF��x�o�^���ꂽ�o�b�`�͋N�����E��~����<br>
 *       �ւ�炸�Ǘ�����܂��B<br>
 *       �܂��o�b�`�̎��s�Ԋu�▼�O�A���̑��̃o�b�`����<br>
 *       �����̓o�b�`�N���X���ŕێ����܂��B<br>
 *<pre>
 * [�ύX����]
 * ���t        �A��   ���O      ���e
 * --------------------------------------------------
 * 2006/08/31  VL000  ���      �V�K
 *</pre>
 * @author ���
 */
public class BatchScheduler {
	
	// �o�b�`���s���ԍŏ��P�ʁF�b�i�f�o�b�O���j
	private static long SECOND = 1000L;
	
	// �o�b�`���s���ԍŏ��P�ʁF���i�ʏ펞�j
	private static long MINUTE = SECOND * 60L;
	
	// �X�P�W���[���C���X�^���X
	public static BatchScheduler i = new BatchScheduler();
	
	// �ŏ����s����
	private long minimumTime;
	
	// �o�^����Ă���o�b�`�̃��X�g
	private List batchList;
	
	// �o�b�`��ID�̃}�b�v
	private Map batchMap;
	
	// ID���s�p�V�[�P���X
	int serial = 1;
	
	private Timer timer;

	private BatchScheduler() {
		batchList = new LinkedList();
		batchMap = new HashMap();
		
		//if (SystemInfo.isDebugMode()) {
		//	minimumTime = SECOND;
		//} else {
			minimumTime = MINUTE;
		//}
	}

	/**
	 * �T�v: �V�����^�X�N���X�P�W���[���ɒǉ����܂��B<br>
	 * �ڍ�: �V�����^�X�N���X�P�W���[���ɒǉ����܂��B<br>
	 * ���l: �Ȃ��B<br>
	 * @param batch
	 */
	public void schedule(CnkBatch batch) {
		
		String id = (serial++) + "";
		batch.setId(id);
		batchList.add(batch);
		batchMap.put(id, batch);
		
		if (batch.isProcessing()) {
			startExecuter();
		}
	}
	
	/**
	 * �T�v: �����Ŏw�肳�ꂽ�o�b�`��Ԃ��܂��B<br>
	 * �ڍ�: �����Ŏw�肳�ꂽ�o�b�`��Ԃ��܂��B<br>
	 * ���l: �Ȃ��B<br>
	 * @param id
	 * @return
	 */
	public CnkBatch getBatch(String id) {
		return (CnkBatch) batchMap.get(id);
	}
	
	/**
	 * �T�v: �X�P�W���[���ɃZ�b�g���ꂽ�S�Ẵo�b�`��Ԃ��܂��B<br>
	 * �ڍ�: �X�P�W���[���ɃZ�b�g���ꂽ�S�Ẵo�b�`��Ԃ��܂��B<br>
	 * ���l: �Ȃ��B<br>
	 * @return
	 */
	public CnkBatch[] getBatchAll() {
		return (CnkBatch[]) batchList.toArray(new CnkBatch[0]);
	}
	
	/**
	 * �T�v: �^�X�N���X�P�W���[������폜���܂��B<br>
	 * �ڍ�: �^�X�N���X�P�W���[������폜���܂��B<br>
	 * ���l: �Ȃ��B<br>
	 * @param id
	 */
	public synchronized void delete(String id) {
		
		CnkBatch batch = (CnkBatch) batchMap.get(id);
		batchMap.remove(id);
		batchList.remove(batch);

		if (batchList.size() == 0) {
			stopExecuter();
		}
	}
	
	/**
	 * �T�v: ��~���̃^�X�N���J�n���܂��B<br>
	 * �ڍ�: ��~���̃^�X�N���J�n���܂��B<br>
	 * ���l: �Ȃ��B<br>
	 * @param id
	 */
	public void start(String id) {
		
		CnkBatch batch = (CnkBatch) batchMap.get(id);
		if (batch != null && !batch.isProcessing()) {
			batch.start();
			startExecuter();
		}
	}
	
	/**
	 * �T�v: ���s���̃^�X�N���~���܂��B<br>
	 * �ڍ�: ���s���̃^�X�N���~���܂��B<br>
	 * ���l: �Ȃ��B<br>
	 * @param id
	 */
	public void cancel(String id) {
		
		CnkBatch batch = (CnkBatch) batchMap.get(id);
		if (batch != null) {
			batch.pause();
		}
	}
	
	/**
	 * 
	 * �T�v: �o�b�`�̔j�����s���B<br>
	 * �ڍ�: �o�b�`�̔j�����s���B�V�X�e���I�����ɌĂяo��<br>
	 * ���l: �Ȃ�<br>
	 *
	 */
	public void destoroy() {
		stopExecuter();
	}
	
	
	
	private void startExecuter() {
		if (timer == null) {
			timer = new Timer();
			timer.schedule(new BatchExecuter(), minimumTime, minimumTime);
		}
	}
	
	private void stopExecuter() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}
}
