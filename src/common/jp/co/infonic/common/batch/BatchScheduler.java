package jp.co.infonic.common.batch;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;

/**
 * 
 * 概要：バッチの管理を行うクラス<br>
 * 詳細：一度登録されたバッチは起動中・停止中に<br>
 *       関わらず管理されます。<br>
 *       またバッチの実行間隔や名前、その他のバッチ毎の<br>
 *       属性はバッチクラス側で保持します。<br>
 *<pre>
 * [変更履歴]
 * 日付        連番   名前      内容
 * --------------------------------------------------
 * 2006/08/31  VL000  樋口      新規
 *</pre>
 * @author 樋口
 */
public class BatchScheduler {
	
	// バッチ実行時間最小単位：秒（デバッグ時）
	private static long SECOND = 1000L;
	
	// バッチ実行時間最小単位：分（通常時）
	private static long MINUTE = SECOND * 60L;
	
	// スケジューラインスタンス
	public static BatchScheduler i = new BatchScheduler();
	
	// 最小実行時間
	private long minimumTime;
	
	// 登録されているバッチのリスト
	private List batchList;
	
	// バッチとIDのマップ
	private Map batchMap;
	
	// ID発行用シーケンス
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
	 * 概要: 新しいタスクをスケジュールに追加します。<br>
	 * 詳細: 新しいタスクをスケジュールに追加します。<br>
	 * 備考: なし。<br>
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
	 * 概要: 引数で指定されたバッチを返します。<br>
	 * 詳細: 引数で指定されたバッチを返します。<br>
	 * 備考: なし。<br>
	 * @param id
	 * @return
	 */
	public CnkBatch getBatch(String id) {
		return (CnkBatch) batchMap.get(id);
	}
	
	/**
	 * 概要: スケジューラにセットされた全てのバッチを返します。<br>
	 * 詳細: スケジューラにセットされた全てのバッチを返します。<br>
	 * 備考: なし。<br>
	 * @return
	 */
	public CnkBatch[] getBatchAll() {
		return (CnkBatch[]) batchList.toArray(new CnkBatch[0]);
	}
	
	/**
	 * 概要: タスクをスケジューラから削除します。<br>
	 * 詳細: タスクをスケジューラから削除します。<br>
	 * 備考: なし。<br>
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
	 * 概要: 停止中のタスクを開始します。<br>
	 * 詳細: 停止中のタスクを開始します。<br>
	 * 備考: なし。<br>
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
	 * 概要: 実行中のタスクを停止します。<br>
	 * 詳細: 実行中のタスクを停止します。<br>
	 * 備考: なし。<br>
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
	 * 概要: バッチの破棄を行う。<br>
	 * 詳細: バッチの破棄を行う。システム終了時に呼び出す<br>
	 * 備考: なし<br>
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
