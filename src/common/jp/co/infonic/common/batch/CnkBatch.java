package jp.co.infonic.common.batch;

import java.sql.SQLException;
import java.util.Date;

import jp.co.infonic.common.db.jdbc.DBConnection;
import jp.co.infonic.common.db.jdbc.DataSourceManager;
import jp.co.infonic.common.module.log.BatchLogger;

public abstract class CnkBatch implements Runnable {
	
	// 次回実行時間までの残り時間（分）
	private int timer;
	
	// 同期実行処理フラグ
	private boolean synchronous = true;
	
	// 実行間隔（分）
	private int period = 1;
	
	// 前回実行時間（ミリ秒）
	private long lastTime;
	
	// タスク有効フラグ
	private boolean processing = false;
	
	// 実行時ID
	private String id;
	
	// 実行時IDをセットする。
	final void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 概要: 現在が実行可能かどうかを返すフラグ<br>
	 * 詳細: 現在が実行可能かどうかを返すフラグ<br>
	 *       trueを返した場合、処理が実行される。<br>
	 *       オーバーライドする事で定期実行以外に<br>
	 *       日付指定実行等を行えるように設定できる。<br>
	 * 備考: なし<br>
	 *
	 * @param current
	 * @return
	 */
	boolean isExeTime(long current) {
		return timer <= 0;
	}
	
	// 実行時間タイマーのカウントダウン
	final void countdown() {
		timer--;
	}
	
	// 同期処理フラグを返す
	final boolean isSynchronous() {
		return synchronous;
	}

	/**
	 * 概要: このタスクの実行を開始します。<br>
	 * 詳細: このタスクの実行を開始します。<br>
	 *       実行タイマーはperiodで初期化されます。<br>
	 * 備考: なし<br>
	 */
	public final void start() {
		processing = true;
	}

	/**
	 * 概要: このタスクの実行を一時停止します。<br>
	 * 詳細: このタスクの実行を一時停止します。<br>
	 * 備考: なし<br>
	 */
	public final void pause() {
		processing = false;
	}
	
	/**
	 * 概要: 実行時IDを返す。<br>
	 * 詳細: 実行時IDを返す。<br>
	 * 備考: なし<br>
	 * @return
	 */
	public final String getId() {
		return id;
	}

	/**
	 * 概要: 前回実行時間を返す。 <br>
	 * 詳細: 前回実行時間を日付型として返す。 <br>
	 * 備考: なし<br>
	 */
	public Date getLasttime() {
		if (lastTime == 0) {
			return null;
		}
		return new Date(lastTime);
	}

	/**
	 * 概要: 実行時間の間隔を返す。<br>
	 * 詳細: 実行時間の間隔を返す。<br>
	 *       単位は分。<br>
	 * 備考: なし<br>
	 */
	public int getPeriod() {
		return period;
	}
	
	/**
	 * 
	 * 概要: 実行間隔の文字列表現を返す。<br>
	 * 詳細: 実行間隔の文字列表現を返す。<br>
	 *       分毎以外で実行する場合、当メソッドを<br>
	 *       オーバーライドする。<br>
	 * 備考: なし<br>
	 *
	 * @return
	 */
	public String getTiming() {
		return period + "分毎";
	}

	/**
	 * 概要: 次回実行時間までの残り時間を返す。<br>
	 * 詳細: 次回実行時間までの残り時間を返す。<br>
	 * 備考: なし<br>
	 */
	public int getNextTime() {
		return timer;
	}

	/**
	 * 概要: 次回実行時間までの時間を再設定する。<br>
	 * 詳細: 次回実行時間までの時間を再設定する。<br>
	 *       単位は分。<br>
	 * 備考: なし<br>
	 */
	public void setNextTime(int minute) {
		timer = minute;
	}

	/**
	 * 概要: 実行間隔を再設定する。<br>
	 * 詳細: 実行間隔を再設定する。<br>
	 *       単位は分。<br>
	 * 備考: なし<br>
	 */
	public void setPeriod(int minute) {
		period = minute;
	}

	/**
	 * 概要: 現在実行中かどうかを返す。<br>
	 * 詳細: 現在実行中かどうかを返す。<br>
	 *       true  ⇒　実行中<br>
	 *       false ⇒　停止中<br>
	 * 備考: なし<br>
	 */
	public boolean isProcessing() {
		return processing;
	}
	
	/**
	 * バッチ処理実行結果を返す。
	 * @return
	 */
	public int getResult() {
		return result;
	}

	/**
	 * 当タスクの実行基底メソッド
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
	
	// 処理結果
	private int result;

	/**
	 * 
	 * 概要: 次回実行時間までのタイマーをリセットする。<br>
	 * 詳細: 次回実行時間までのタイマーをリセットする。<br>
	 * 備考: なし<br>
	 */
	protected void reset() {
		timer = period;
	}
	
	/**
	 * コンストラクタ
	 * @param period 実行間隔
	 */
	public CnkBatch(int period) {
		if (period < 1) {
			throw new IllegalArgumentException("実行間隔は１以上でなければいけません。");
		}
		this.period = period;
		this.timer = period;
	}
	
	/**
	 * コンストラクタ
	 * @param name バッチ名
	 * @param period 実行間隔
	 * @param synchronous 同期実行フラグ true:同期処理 false:非同期処理
	 */
	public CnkBatch(int period, boolean synchronous) {
		this(period);
		this.synchronous = synchronous;
	}

	/**
	 * 概要: バッチ名を返す。<br>
	 * 詳細: バッチ名を返す。<br>
	 * 備考: なし<br>
	 */
	public abstract String getName();
	
	/**
	 * 
	 * 概要: バッチの説明を返す。<br>
	 * 詳細: バッチの説明を返す。<br>
	 * 備考: なし<br>
	 *
	 * @return
	 */
	public abstract String getDescription();
	
	/**
	 * 概要: メイン処理<br>
	 * 詳細: メイン処理<br>
	 * 備考: なし<br>
	 *
	 * @return
	 */
	protected abstract int execute() throws Exception;
	
	/**
	 * 概要: バッチ用DBコネクション取得<br>
	 * 詳細: バッチ用DBコネクション取得<br>
	 * 備考: なし<br>
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
	 * 概要: メイン関数<br>
	 * 詳細: APサーバと別プロセスとして実行する場合の基底メソッド<br>
	 * 備考: なし<br>
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
	
	// 処理結果:OK
	public final static int STATUS_NORMAL = 0;
	
	// 処理結果:NG
	public final static int STATUS_ERROR = 1;
}
