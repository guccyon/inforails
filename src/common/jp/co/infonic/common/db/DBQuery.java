package jp.co.infonic.common.db;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import jp.co.infonic.common.db.jdbc.DBConnection;
import jp.co.infonic.common.db.jdbc.DBPreparedStatement;
import jp.co.infonic.common.db.jdbc.DataSourceManager;

/**
 * 
 * 概要：ＤＢ参照アクセス時の手順をバインディングします。<br>
 * 詳細：ＤＢ参照アクセス時の手順をバインディングします。
 *       開発者は下記のコーディングをするだけで、手軽にＳＱＬが発行できます。<br>
 *       = 例 =<br>
 *       List result = new DBQuery(this.getClass(), "発行SQLID").executeQueryForList(null);<br>
 *<br>       
 *       executeQueryForMap(), executeQueryForList()　メソッドの戻り値の<br>
 *       データ構造についてはDBPreparedStatementのドキュメントを参照してください。<br>
 *<pre>
 * [変更履歴]
 * 日付        連番   名前      内容
 * --------------------------------------------------
 * 2006/04/15  VL000  樋口      新規
 *</pre>
 */
public class DBQuery {

	// SQLオブジェクト
	private SqlObj sql;
	
	// 置換文字列
	private String[] replace;

	// インスタンス生成クラス
	private Class cla;

	// コンストラクタ
	public DBQuery(Class cla, String sqlId) {
		this.cla = cla;
		sql = SqlManager.getSql(cla, sqlId);
		this.replace = new String[0];
	}

	// コンストラクタ
	public DBQuery(Class cla, String sqlId, String[] replace) {
		this.cla = cla;
		this.replace = replace;
		sql = SqlManager.getSql(cla, sqlId);
	}

	/**
	 * 
	 * 概要: ＳＱＬを発行しマップとして、結果セットを返します。<br>
	 * 詳細: ＳＱＬを発行しマップとして、結果セットを返します。<br>
	 *       結果が０件の場合nullを返します。<br>
	 * 備考: なし<br>
	 * 
	 * @param params プリペアドステイトメントバインド変数
	 * @return
	 * @throws SQLException 
	 */
	public Map executeQueryForMap(Object[] params) throws SQLException {
		Map resultMap = null;

		DBConnection conn = null;
		try {
			conn = DataSourceManager.getConnection(cla, true);

			DBPreparedStatement preState = null;
			try {
				preState = conn.prepareStatement(sql, replace);
				
				resultMap = preState.executeQueryForMap(params);
				
			} finally {
				if (preState != null) {
					preState.close();
				}
			}

		}  finally  {
			if (conn != null) {
				conn.close();
			}
		}
		
		return resultMap;
	}

	/**
	 * 
	 * 概要: ＳＱＬを発行しリストとして、結果セットを返します。<br>
	 * 詳細: ＳＱＬを発行しリストとして、結果セットを返します。<br>
	 * 備考: なし<br>
	 * 
	 * @param params プリペアドステイトメントバインド変数
	 * @return
	 * @throws SQLException
	 */
	public List executeQueryForList(Object[] params) throws SQLException {
		
		List resultList = null;

		DBConnection conn = null;
		try {
			conn = DataSourceManager.getConnection(cla, true);

			DBPreparedStatement preState = null;
			try {
				preState = conn.prepareStatement(sql);
				
				resultList = preState.executeQueryForList(params);
				
			} finally {
				if (preState != null) {
					preState.close();
				}
			}

		}  finally {
			if (conn != null) {
				conn.close();
			}
		}
		
		return resultList;
	}
	
	/**
	 * 
	 * 概要: １行分のレコードを抽出します。<br>
	 * 詳細: １行分のレコードを抽出します。<br>
	 * 　　　１行も選択されなかった場合はnullを<br>
	 * 　　　返します。<br>
	 * 備考: なし<br>
	 *
	 * @return
	 * @throws SQLException 
	 */
	public Map executeQueryOneRow(Object[] params) throws SQLException {
		List list = executeQueryForList(params);
		if (list.size() > 0) {
			return (Map)list.get(0);
		}
		
		return null;
	}

}
