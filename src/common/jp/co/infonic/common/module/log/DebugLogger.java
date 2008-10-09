package jp.co.infonic.common.module.log;

import org.apache.log4j.Logger;

/**
 * 
 * 概要：デバッグ用ログを出力する為の機能を提供します。<br>
 * 詳細：デバッグ用ログを出力する為の機能を提供します。<br>
 *<pre>
 * [変更履歴]
 * 日付        連番   名前      内容
 * --------------------------------------------------
 * 2006/03/23  VL000  樋口      新規
 *</pre>
 * @author higuchit
 */
public class DebugLogger extends CommonLogger {
	
	// ロガー
	private Logger logger;
	
	// ロガー取得クラス
	private Class cla;
	
	// デバッグログ用カテゴリ名
	private static final String CATEGORY_NAME = "Debug.";
	
	private DebugLogger(Class cla) {
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
	public static Logger getLogger(Class cla) {
		
		DebugLogger log = new DebugLogger(cla == null ? DebugLogger.class : cla);
		
		return log.logger;
	}
	
	/**
	 * 
	 * 概要: デバッグレベルでメッセージを出力する。<br>
	 * 詳細: デバッグレベルでメッセージを出力する。<br>
	 * 備考: なし<br>
	 *
	 * @param msg
	 */
	public void debug(String msg) {
		logger.debug(createLogMsg(msg));
	}
	
	/**
	 * 
	 * 概要: インフォレベルでメッセージを出力する。<br>
	 * 詳細: インフォレベルでメッセージを出力する。<br>
	 * 備考: なし<br>
	 *
	 * @param msg
	 */
	public void info(String msg) {
		logger.info(createLogMsg(msg));
	}
	
	/**
	 * エラーレベルでログを出力する
	 * @param msg
	 */
	public void error(String msg) {
		logger.error(createLogMsg(msg));
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
	private String createLogMsg(String msg) {
		
		StringBuffer sb = new StringBuffer();
		
		sb.append(Thread.currentThread().getName()).append(",");
		
		sb.append(cla.getName());
		
		if (commonInfo() != null) {
			sb.append(",").append(commonInfo().toLogFormat());
		}
		
		sb.append(",").append(msg);
		
		return sb.toString();
	}
}
