package jp.co.infonic.common.module.log;

/**
 * 
 * 概要：ロガー共通クラス<br>
 * 詳細：ロガー共通クラス<br>
 *<pre>
 * [変更履歴]
 * 日付        連番   名前      内容
 * --------------------------------------------------
 * 2006/03/23  VL000  樋口      新規
 *</pre>
 * @author higuchit
 */
public class CommonLogger {
	
	// スレッドローカル情報
	protected static ThreadLocal<LogCommonInfo> threadLocal = new ThreadLocal<LogCommonInfo>();
	
	/**
	 * 
	 * 概要: ログ出力時の共通情報をセットします。<br>
	 * 詳細: ログ出力時の共通情報をセットします。<br>
	 * 備考: なし<br>
	 *
	 * @param commonInfo
	 */
	public static void setCommonInfo(LogCommonInfo commonInfo) {
		threadLocal.set(commonInfo);
	}
	
	/**
	 * 
	 * 概要: スレッドローカルに保持している共通情報を削除します。<br>
	 * 詳細: スレッドローカルに保持している共通情報を削除します。<br>
	 * 備考: なし<br>
	 *
	 */
	public static void removeCommonInfo() {
		threadLocal.set(null);
	}
	
	protected LogCommonInfo commonInfo() {
		return threadLocal.get();
	}
}
