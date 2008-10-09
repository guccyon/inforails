package jp.co.infonic.common.module.log;

/**
 * 
 * 概要：ログ出力時の共通情報を保持する抽象クラス<br>
 * 詳細：ログ出力時の共通情報を保持する抽象クラス<br>
 *<pre>
 * [変更履歴]
 * 日付        連番   名前      内容
 * --------------------------------------------------
 * 2006/03/23  VL000  樋口      新規
 *</pre>
 * @author higuchit
 */
public abstract class LogCommonInfo {
	
	protected static final String COMMA = ",";
	
	/**
	 * 
	 * 概要: ログ出力時のログ内挿入文字列を返す。<br>
	 * 詳細: ログ出力時のログ内挿入文字列を返す。<br>
	 *       通常は出力したいフィールド情報をカンマ区切りで繋いで<br>
	 *       返す。<br>
	 *       この時、作成文字列の先頭と最後尾にカンマを付けてはならない。<br>
	 *       カンマ処理は出力側で行うものとする。<br>
	 * 備考: なし<br>
	 *
	 * @return
	 */
	public abstract String toLogFormat();

}
