package jp.co.infonic.common.batch;

import java.util.TimerTask;

/**
 * 
 * 概要：バッチをスケジュールに合わせて実行するクラス<br>
 * 詳細：このタスクがタイマークラスにより一定周期で<br>
 *       実行されます。実行時にスケジューラに登録された<br>
 *       有効なバッチに対して、処理時間を確認し、<br>
 *       処理時間に達していた場合、そのバッチをキックします。<br>
 *       <br>
 *       キックの方法には非同期処理と同期処理が存在します。<br>
 *       同期処理の場合、実行中の先行バッチの処理が重く、<br>
 *       時間がかかる場合、後続のバッチの処理開始が遅れます。<br>
 *<pre>
 * [変更履歴]
 * 日付        連番   名前      内容
 * --------------------------------------------------
 * 2006/08/31  VL000  樋口      新規
 *</pre>
 * @author 樋口
 */
class BatchExecuter extends TimerTask {

	public void run() {
		
		CnkBatch[] batchAry = BatchScheduler.i.getBatchAll();
		long current = System.currentTimeMillis();
		
		// スケジューラに登録されている全てのバッチ分ループ
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
