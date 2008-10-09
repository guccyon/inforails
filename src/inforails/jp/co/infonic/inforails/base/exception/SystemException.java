package jp.co.infonic.inforails.base.exception;

import jp.co.infonic.common.module.log.APErrorLogger;


/**
 * 
 * 概要：CNKSYSTEMシステムエラー<br>
 * 詳細：CNKSYSTEM内で起こった全ての想定外例外発生時にthrowされます。<br>
 *<pre>
 * [変更履歴]
 * 日付        連番   名前      内容
 * --------------------------------------------------
 * 2006/07/02  VL000  樋口      新規
 *</pre>
 */
public class SystemException extends RuntimeException {
	
	private static final long serialVersionUID = 9039802049377902246L;

	// エラー発生クラス名
	private String className = "";
	
	// エラー発生メソッド名
	private String methodName = "";
	
	// エラー解析用情報
	private String debugInfo = "";
	
	// 管理者通知フラグ
	private boolean noticeFlg;
	
	// 管理者通知メッセージ
	private String noticeMsg = "";
	
	// ルート例外
	private Throwable cause;
	
	// nullフィールド置き換え文字列
	private static final String DEFAULT = "未設定";
	
	/**
	 * コンストラクタ（管理者通知無し）
	 * @param e
	 * @param className
	 * @param methodName
	 * @param debugInfo
	 */
	public SystemException (Exception e, String className, String methodName, String debugInfo) {
		this(e, className, methodName, debugInfo, false, "");
	}
	
	/**
	 * 
	 * @param e
	 * @param className
	 * @param methodName
	 * @param debugInfo
	 * @param noticeFlg
	 * @param noticeMsg
	 */
	public SystemException(Exception e, String className, String methodName, String debugInfo, boolean noticeFlg, String noticeMsg) {
		this.cause = e;
		this.className = className;
		this.methodName = methodName;
		this.debugInfo = debugInfo;
		this.noticeFlg = noticeFlg;
		this.noticeMsg = noticeMsg;
		
		checkNull();
	}
	
	/**
	 * 
	 * 概要: エラーログを出力します。<br>
	 * 詳細: エラーログを出力します。<br>
	 * 備考: なし<br>
	 *
	 * @param cla
	 */
	public void outputErrorInfo() {
		APErrorLogger logger = APErrorLogger.getLogger();
		
		logger.outputErrorLog(className, methodName, debugInfo, noticeFlg, noticeMsg, this);
	}
	
	/**
	 * 
	 * 概要: チェインされている例外を取得します。<br>
	 * 詳細: チェインされている例外を取得します。<br>
	 * 備考: なし<br>
	 *
	 * @return
	 * @see java.lang.Throwable#getCause()
	 */
	public Throwable getCause() {
		return cause;
	}
	
	/**
	 * 
	 * 概要: フィールドに設定された値にnullが含まれないかチェックします。<br>
	 * 詳細: フィールドに設定された値にnullが含まれないかチェックします。
	 *       ルート例外についてはnullを許可します。<br>
	 * 備考: なし<br>
	 *
	 */
	private void checkNull() {
		if (className == null) {
			className = DEFAULT;
		}
		if (methodName == null) {
			methodName = DEFAULT;
		}
		if (debugInfo == null) {
			debugInfo = DEFAULT;
		}
		if (noticeMsg == null) {
			noticeMsg = DEFAULT;
		}
	}
}
