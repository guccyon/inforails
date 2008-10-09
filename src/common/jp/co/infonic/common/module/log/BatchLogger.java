package jp.co.infonic.common.module.log;

import java.util.LinkedList;
import java.util.List;

import jp.co.infonic.common.util.ArrayUtil;

import org.apache.log4j.Logger;

/**
 * 
 * 概要：バッチロガー<br>
 * 詳細：バッチロガー<br>
 *<pre>
 * [変更履歴]
 * 日付        連番   名前      内容
 * --------------------------------------------------
 * 2006/10/01  VL000  樋口      新規
 *</pre>
 * @author higuchit
 */
public class BatchLogger {
	
	// バッチログ用カテゴリ名
	private static final String CATEGORY_NAME = "Batch.";
	
	/**
	 * 概要: ロガーを取得する。<br>
	 * 詳細: ロガーを取得する。<br>
	 * 備考: なし<br>
	 *
	 * @param cla
	 * @return
	 */
	public static BatchLogger getLogger(Class batchClass) {
		return new BatchLogger(batchClass);
	}
	
	private String name;
	
	// ロガー
	private Logger logger;
	
	// バッチ開始時間
	private long starttime;
	
	// バッチ終了時間
	private long endtime;
	
	private BatchLogger(Class batchClass) {
		Class category = batchClass == null ? BatchLogger.class : batchClass;
		this.logger = Logger.getLogger(CATEGORY_NAME + category.getName());
		this.name = batchClass.getSimpleName();
	}
	
	/**
	 * 
	 * 概要: デバッグレベルでメッセージを出力する。<br>
	 * 詳細: デバッグレベルでメッセージを出力する。<br>
	 * 備考: なし<br>
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
	 * 概要: デバッグレベルでメッセージを出力する。<br>
	 * 詳細: デバッグレベルでメッセージを出力する。<br>
	 * 備考: なし<br>
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
	 * 概要: インフォレベルでメッセージを出力する。<br>
	 * 詳細: インフォレベルでメッセージを出力する。<br>
	 * 備考: なし<br>
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
	 * 概要: エラー出力を行います。<br>
	 * 詳細: エラー出力を行います。<br>
	 * 備考: なし<br>
	 *
	 * @param message
	 * @param th
	 */
	public void error(Throwable th) {
		logger.error(th);
	}
	
	/**
	 * 概要: バッチログに例外のトレースを出力します。<br>
	 * 詳細: バッチログに例外のトレースを出力します。<br>
	 * 備考: なし<br>
	 *
	 * @param e
	 */
	public void exception(Throwable e) {
		logger.error(e);
	}
	
	public void start() {
		starttime = System.currentTimeMillis();
		List <String>contents = createLogMsg();
		contents.add("開始");
		logger.info(ArrayUtil.join(contents, ","));
	}
	
	/**
	 * 
	 * 概要: バッチの終了ログを出力します。<br>
	 * 詳細: バッチの終了ログを出力します。<br>
	 * 備考: なし<br>
	 *
	 * @param status
	 */
	public void end(int status) {
		endtime = System.currentTimeMillis();
		List <String>contents = createLogMsg();
		contents.add("終了");
		contents.add("[result]" + status);
		contents.add("[starttime]" + starttime);
		contents.add("[endtime]" + endtime);
		contents.add("[processtime]" + (endtime - starttime));
		logger.info(ArrayUtil.join(contents, ","));
	}
	
	/**
	 * 概要: バッチログ共通メッセージを生成
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
