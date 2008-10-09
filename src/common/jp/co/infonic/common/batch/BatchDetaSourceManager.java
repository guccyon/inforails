package jp.co.infonic.common.batch;

import java.sql.SQLException;

import jp.co.infonic.common.db.jdbc.DBConnection;

/**
 * 
 * 概要：バッチモジュール用DBコネクション管理クラス<br>
 * 詳細：バッチ処理よりコネクションを取得する場合に
 *       当クラス経由でDB接続を取得します。
 *       コネクションプーリング等の処理は行っていません。<br>
 *<pre>
 * [変更履歴]
 * 日付        連番   名前      内容
 * --------------------------------------------------
 * 2006/09/02  VL000  樋口      新規
 *</pre>
 * @author 樋口
 */
class BatchDetaSourceManager {
	
	public static DBConnection getConnection(boolean autoCommit) throws SQLException {
		return null;
	}

}
