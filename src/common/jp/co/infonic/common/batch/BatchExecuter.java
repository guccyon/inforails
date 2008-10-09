package jp.co.infonic.common.batch;

import java.util.TimerTask;

/**
 * 
 * �T�v�F�o�b�`���X�P�W���[���ɍ��킹�Ď��s����N���X<br>
 * �ڍׁF���̃^�X�N���^�C�}�[�N���X�ɂ���������<br>
 *       ���s����܂��B���s���ɃX�P�W���[���ɓo�^���ꂽ<br>
 *       �L���ȃo�b�`�ɑ΂��āA�������Ԃ��m�F���A<br>
 *       �������ԂɒB���Ă����ꍇ�A���̃o�b�`���L�b�N���܂��B<br>
 *       <br>
 *       �L�b�N�̕��@�ɂ͔񓯊������Ɠ������������݂��܂��B<br>
 *       ���������̏ꍇ�A���s���̐�s�o�b�`�̏������d���A<br>
 *       ���Ԃ�������ꍇ�A�㑱�̃o�b�`�̏����J�n���x��܂��B<br>
 *<pre>
 * [�ύX����]
 * ���t        �A��   ���O      ���e
 * --------------------------------------------------
 * 2006/08/31  VL000  ���      �V�K
 *</pre>
 * @author ���
 */
class BatchExecuter extends TimerTask {

	public void run() {
		
		CnkBatch[] batchAry = BatchScheduler.i.getBatchAll();
		long current = System.currentTimeMillis();
		
		// �X�P�W���[���ɓo�^����Ă���S�Ẵo�b�`�����[�v
		for(int i = 0; i < batchAry.length; i++) {
			try {
				
				if (!batchAry[i].isProcessing()) {
					continue;
				}
				
				batchAry[i].countdown();
				
				if(batchAry[i].isExeTime(current)) {
					if (batchAry[i].isSynchronous()) {
						batchAry[i].run();
					} else {
						Thread thread = new Thread(batchAry[i]);
						thread.start();
					}
					
					batchAry[i].reset();
				}
			} catch (Exception e) {
				batchAry[i].pause();
			}
		}
	}
}
