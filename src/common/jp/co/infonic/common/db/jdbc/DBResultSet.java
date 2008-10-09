package jp.co.infonic.common.db.jdbc;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;

/**
 * 
 * 概要：リザルトセットをラップします。<br>
 * 詳細：リザルトセットをラップします。<br>
 *<pre>
 * [変更履歴]
 * 日付        連番   名前      内容
 * --------------------------------------------------
 * 2006/03/20  VL000  樋口      新規
 *</pre>
 * @author higuchit
 */
public class DBResultSet {
	
	// 結果セット
	private ResultSet rs;
	
	// このインスタンスを生成したプリペアドステイトメント
	private DBPreparedStatement preState;
	
	DBResultSet(ResultSet rs, DBPreparedStatement preState) {
		this.rs = rs;
		this.preState = preState;
	}
	
	/**
	 * 
	 * 概要: リソースをクローズします。<br>
	 * 詳細: リソースをクローズします。<br>
	 * 備考: なし<br>
	 *
	 * @throws SQLException
	 */
	public void close() throws SQLException {
		
		if (rs != null) {
			rs.close();
			preState.removeResultSet(this);
		}
		
		rs = null;
		preState = null;
	}
	
	/**
	 * 
	 * 概要: 結果セットのメタデータを取得します。<br>
	 * 詳細: 結果セットのメタデータを取得します。<br>
	 * 備考: なし<br>
	 *
	 * @return
	 * @throws SQLException
	 */
	public ResultSetMetaData getMetaData() throws SQLException {
		return rs.getMetaData();
	}
	
	/**
	 * 
	 * 概要: 結果セットのカーソルの現在の位置より次のレコードが存在するか確認する<br>
	 * 詳細: 結果セットのカーソルの現在の位置より次のレコードが存在するか確認する<br>
	 *       存在する場合は、カーソルを一個進めてtrueを返す。<br>
	 *       存在しない場合はfalseを返す。<br>
	 * 備考: なし<br>
	 *
	 * @return
	 * @throws SQLException
	 */
	public boolean next() throws SQLException {
		return rs.next();
	}
	
	/**
	 * 
	 * 概要: 結果レコードから該当カラムのオブジェクトを取得します。<br>
	 * 詳細: 結果レコードから該当カラムのオブジェクトを取得します。<br>
	 * 備考: なし<br>
	 *
	 * @param rowIndex
	 * @return
	 * @throws SQLException
	 */
	public Object getObject(int colIndex) throws SQLException {
		return rs.getObject(colIndex);
	}
	
	/**
	 * 
	 * 概要: 結果レコードから該当カラムのオブジェクトを取得します。<br>
	 * 詳細: 結果レコードから該当カラムのオブジェクトを取得します。<br>
	 * 備考: なし<br>
	 *
	 * @param rowIndex
	 * @return
	 * @throws SQLException
	 */
	public Object getObject(String colStr) throws SQLException {
		return rs.getObject(colStr);
	}
	
	/**
	 * 
	 * 概要: 結果レコードから該当カラムの文字列を取得します。<br>
	 * 詳細: 結果レコードから該当カラムの文字列を取得します。<br>
	 * 備考: なし<br>
	 *
	 * @param colIndex
	 * @return
	 */
	public String getString(int colIndex) throws SQLException {
		return rs.getString(colIndex);
	}
	
	/**
	 * 
	 * 概要: 結果レコードから該当カラムの文字列を取得します。<br>
	 * 詳細: 結果レコードから該当カラムの文字列を取得します。<br>
	 * 備考: なし<br>
	 *
	 * @param colIndex
	 * @return
	 */
	public String getString(String colStr) throws SQLException {
		return rs.getString(colStr);
	}
	
	/**
	 * 
	 * 概要: 結果レコードから該当カラムの数値を取得します。<br>
	 * 詳細: 結果レコードから該当カラムの数値を取得します。<br>
	 * 備考: なし<br>
	 *
	 * @param colIndex
	 * @return
	 */
	public int getInt(int colIndex) throws SQLException {
		return rs.getInt(colIndex);
	}
	
	/**
	 * 
	 * 概要: 結果レコードから該当カラムの数値を取得します。<br>
	 * 詳細: 結果レコードから該当カラムの数値を取得します。<br>
	 * 備考: なし<br>
	 *
	 * @param colIndex
	 * @return
	 */
	public int getInt(String colStr) throws SQLException {
		return rs.getInt(colStr);
	}
	
	/**
	 * 
	 * 概要: 結果レコードから該当カラムの日付を取得します。<br>
	 * 詳細: 結果レコードから該当カラムの日付を取得します。<br>
	 * 備考: なし<br>
	 *
	 * @param colIndex
	 * @return
	 */
	public Date getDate(int colIndex) throws SQLException {
		return rs.getDate(colIndex);
	}
	
	/**
	 * 
	 * 概要: 結果レコードから該当カラムの日付を取得します。<br>
	 * 詳細: 結果レコードから該当カラムの日付を取得します。<br>
	 * 備考: なし<br>
	 *
	 * @param colIndex
	 * @return
	 */
	public Date getDate(String colStr) throws SQLException {
		return rs.getDate(colStr);
	}
	
	/**
	 * 
	 * 概要: 結果レコードから該当カラムの日付を取得します。<br>
	 * 詳細: 結果レコードから該当カラムの日付を取得します。<br>
	 * 備考: なし<br>
	 *
	 * @param colIndex
	 * @return
	 */
	public Date getTimestamp(int colIndex) throws SQLException {
		return rs.getTimestamp(colIndex);
	}
	
	/**
	 * 
	 * 概要: 結果レコードから該当カラムの日付を取得します。<br>
	 * 詳細: 結果レコードから該当カラムの日付を取得します。<br>
	 * 備考: なし<br>
	 *
	 * @param colIndex
	 * @return
	 */
	public Date getTimestamp(String colStr) throws SQLException {
		return rs.getTimestamp(colStr);
	}
	
	
	
	/**
	 * 概要: 現在レコードから該当カラムの値をBlob型で取得します。<br>
	 * 詳細: 現在レコードから該当カラムの値をBlob型で取得します。<br>
	 * 備考: なし<br>
	 *
	 * @param colIndex
	 * @return
	 * @throws SQLException
	 */
	public Blob getBlob(int colIndex) throws SQLException {
		return rs.getBlob(colIndex);
	}
	
	/**
	 * 概要: 現在レコードから該当カラムの値をBlob型で取得します。<br>
	 * 詳細: 現在レコードから該当カラムの値をBlob型で取得します。<br>
	 * 備考: なし<br>
	 *
	 * @param colIndex
	 * @return
	 * @throws SQLException
	 */
	public Blob getBlob(String colStr) throws SQLException {
		return rs.getBlob(colStr);
	}
}
