package jp.co.infonic.common.module.log;

import org.apache.log4j.Logger;

/**
 * 
 * 概要：DBアクセスSQL発行状況ロガー<br>
 * 詳細：DBアクセスSQL発行状況ロガー<br>
 *<pre>
 * [変更履歴]
 * 日付        連番   名前      内容
 * --------------------------------------------------
 * 2006/03/23  VL000        新規
 *</pre>
 * @author higuchit
 */
public class DBAccessLogger extends CommonLogger {
	
	// ロガー
	private Logger logger;
	
	// ロガー取得クラス
	private Class cla;
	
	// デバッグログ用カテゴリ名
	private static final String CATEGORY_NAME = "DBAccess.";
	
	/**
	 * プライベートコンストラクタ
	 * @param cla
	 */
	private DBAccessLogger(Class cla) {
		logger = Logger.getLogger(CATEGORY_NAME + cla.getName());
		this.cla = cla;
	}
	
	/**
	 * 
	 * 概要: ロガーを取得する。<br>
	 * 詳細: ロガーを取得する。<br>
	 * 備考: なし<br>
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
	 * 概要: デバッグレベルでメッセージを出力する。<br>
	 * 詳細: デバッグレベルでメッセージを出力する。<br>
	 * 備考: なし<br>
	 *
	 * @param msg
	 */
	public void debug(String sql, long startTime, long endTime) {
		logger.debug(createLogMsg(sql, startTime, endTime));
	}
	
	/**
	 * 
	 * 概要: インフォレベルでメッセージを出力する。<br>
	 * 詳細: インフォレベルでメッセージを出力する。<br>
	 * 備考: なし<br>
	 *
	 * @param msg
	 */
	public void info(String sql, long startTime, long endTime) {
		logger.info(createLogMsg(sql, startTime, endTime));
	}
	
	/**
	 * 
	 * 概要: ログ出力文字列を作成<br>
	 * 詳細: ログ出力文字列を作成<br>
	 * 備考: なし<br>
	 *
	 * @param msg
	 * @return
	 */
	private String createLogMsg(String sql, long startTime, long endTime) {
		
		// 実行時間
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
