package jp.co.infonic.common.module.log;

import jp.co.infonic.inforails.base.exception.SystemException;

import org.apache.log4j.Logger;

/**
 * 
 * 概要：エラーログを出力します。<br>
 * 詳細：エラーログを出力します。<br>
 *<pre>
 * [変更履歴]
 * 日付        連番   名前      内容
 * --------------------------------------------------
 * 2006/04/02  VL000  樋口      新規
 *</pre>
 * @author Tetsuro
 */
public class APErrorLogger extends CommonLogger {

	// ロガー
	private Logger logger;

	// デバッグログ用カテゴリ名
	private static final String CATEGORY_NAME = "APError.";

	// システム改行コード
	private static final String LINE_SEP = System.getProperty("line.separator");

	// ログセパレータ
	private static final String LOG_SEP = "------------------------------------------------------------------------------";

	/**
	 * プライベートコンストラクタ
	 * 
	 * @param cla
	 */
	private APErrorLogger(String categoryName) {

		logger = Logger.getLogger(CATEGORY_NAME + categoryName);

	}

	/**
	 * 
	 * 概要: ロガーの取得を行います。<br>
	 * 詳細: ロガーの取得を行います。<br>
	 * 備考: なし<br>
	 * 
	 * @param cla
	 * @return
	 */
	public static APErrorLogger getLogger() {

		return getLogger(APErrorLogger.class.getName());

	}

	/**
	 * 
	 * 概要: ロガーの取得を行います。カテゴリ別に出力したい場合に引数を与えます。<br>
	 * 詳細: ロガーの取得を行います。カテゴリ別にログの出力制御を行いたい場合に引数を与えます。
	 *       プロパティファイルでカテゴリを記述する際はAPError.引数カテゴリ名で指定します。<br>
	 * 備考: なし<br>
	 * 
	 * @param cla
	 * @return
	 */
	public static APErrorLogger getLogger(String categoryName) {
		
		if (categoryName == null) {
			categoryName = APErrorLogger.class.getName();
		}
		
		APErrorLogger log = new APErrorLogger(categoryName);

		return log;

	}

	/**
	 * 
	 * 概要: エラーログの出力を行います。<br>
	 * 詳細: エラーログの出力を行います。<br>
	 * 備考: なし<br>
	 * 
	 * @param className
	 * @param methodName
	 * @param debugInfo
	 * @param noticeFlg
	 * @param noticeMsg
	 * @param excep
	 */
	public void outputErrorLog(String className, String methodName,
			String debugInfo, boolean noticeFlg, String noticeMsg,
			Throwable excep) {

		StringBuffer sb = new StringBuffer();
		editErrorLogMsg(sb, className, methodName, debugInfo, noticeFlg,
				noticeMsg);
		sb.append(LINE_SEP);

		if (excep != null) {
			if (excep instanceof SystemException) {

				SystemException sysExc = (SystemException) excep;
				Throwable th = sysExc.getCause();
				if (th != null) {
					sb.append(LINE_SEP);
				}

				while (th != null) {
					editStackTrace(sb, th);
					th = th.getCause();
				}
			} else {
				sb.append(LOG_SEP);
				logger.error(sb.toString(), excep);
				return;
			}
		}

		sb.append(LOG_SEP);
		logger.error(sb.toString());

		if (noticeFlg) {
			noticeMsg(sb);
		}
	}

	/**
	 * 
	 * 概要: 管理者へ通知を行います。<br>
	 * 詳細: 管理者へ通知を行います。（未実装）<br>
	 * 備考: なし<br>
	 * 
	 * @param log
	 */
	private void noticeMsg(StringBuffer log) {

	}

	/**
	 * 
	 * 概要: エラーログ用メッセージを編集します。<br>
	 * 詳細: エラーログ用メッセージを編集します。<br>
	 * 備考: なし<br>
	 * 
	 * @param sb
	 * @param className
	 * @param methodName
	 * @param debugInfo
	 * @param noticeFlg
	 * @param noticeMsg
	 * @return
	 */
	private StringBuffer editErrorLogMsg(StringBuffer sb, String className,
			String methodName, String debugInfo, boolean noticeFlg,
			String noticeMsg) {
		if (methodName == null) {
			methodName = "";
		}
		if (debugInfo == null) {
			debugInfo = "";
		}
		if(noticeMsg == null) {
			noticeMsg = "";
		}

		sb.append(className).append(",");
		sb.append(methodName).append(",");
		sb.append(debugInfo).append(",");

		if (commonInfo() != null) {
			sb.append(",").append(commonInfo().toLogFormat());
		}

		sb.append(noticeFlg).append(",");
		sb.append(noticeMsg);
		return sb;
	}

	/**
	 * 
	 * 概要: 例外トレースを編集する。<br>
	 * 詳細: 例外トレースを編集する。<br>
	 * 備考: なし<br>
	 * 
	 * @param sb
	 * @param th
	 * @return
	 */
	private StringBuffer editStackTrace(StringBuffer sb, Throwable th) {

		// 例外メッセージを編集
		sb.append(th.getClass().getName()).append(th.getMessage()).append(
				LINE_SEP);

		// 例外詳細トレース取得
		StackTraceElement[] element = th.getStackTrace();
		for (int i = 0; i < element.length; i++) {

			// 例外詳細トレース編集
			sb.append("\tat ").append(element[i]).append(LINE_SEP);
		}

		// 編集バッファを返却
		return sb;
	}

}
