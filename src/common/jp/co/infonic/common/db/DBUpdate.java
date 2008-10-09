package jp.co.infonic.common.db;

import java.sql.SQLException;

import jp.co.infonic.common.db.jdbc.DBConnection;
import jp.co.infonic.common.db.jdbc.DBPreparedStatement;
import jp.co.infonic.common.db.jdbc.DataSourceManager;

/**
 * 
 * 概要：<br>
 * 詳細：<br>
 *<pre>
 * [変更履歴]
 * 日付        連番   名前      内容
 * --------------------------------------------------
 * 2006/09/24  VL000  樋口      新規
 *</pre>
 * @author 樋口
 */
public class DBUpdate {
	
	// SQLオブジェクト
	private SqlObj sql;
	
	// 置換文字列
	private String[] replace;

	// インスタンス生成クラス
	private Class cla;


	public DBUpdate(Class cla, String sqlId) {
		this.cla = cla;
		sql = SqlManager.getSql(cla, sqlId);
		this.replace = new String[0];
	}


	public DBUpdate(Class cla, String sqlId, String[] replace) {
		this.cla = cla;
		sql = SqlManager.getSql(cla, sqlId);
		this.replace = replace;
	}
	
	public int execute(Object[] params) throws SQLException {

		DBConnection conn = null;
		try {
			conn = DataSourceManager.getConnection(cla, true);

			DBPreparedStatement pstmt = conn.prepareStatement(sql, replace);
			pstmt.setBindDataAll(params);
			
			return pstmt.executeUpdate();
			
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}
}
