package jp.co.infonic.common.db.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 概要：Oracle用のWAVE DASH置換機能つきDBResultSet<br>
 * 詳細：<br>
 *<pre>
 * [変更履歴]
 * 日付        連番   名前      内容
 * --------------------------------------------------
 * 2006/07/05  VL000        新規
 *</pre>
 * @author sin
 */
public class DBResultSetForWaveDashEncode extends DBResultSet {

	/**
	 * @param rs
	 * @param preState
	 */
	public DBResultSetForWaveDashEncode(ResultSet rs,
			DBPreparedStatement preState) {
		super(rs, preState);
	}

	public Object getObject(int colIndex) throws SQLException {
		Object object = super.getObject(colIndex);
		if(object instanceof String){
			return replaceWaveDash((String)object);
		}else{
			return object;
		}
	}

	public String getString(int colIndex) throws SQLException {
		return replaceWaveDash(super.getString(colIndex));
	}

	public String getString(String colStr) throws SQLException {
		return replaceWaveDash(super.getString(colStr));
	}
	
	/**
	 * 概要: WAVE DASHをFULL WIDTH TILDEに置換<br>
	 * 詳細: <br>
	 * 備考: なし<br>
	 *
	 * @param str
	 * @return
	 */
	public String replaceWaveDash(String str){
		if(str == null){
			return null;
		}else{
			return str.replace('\u301C', '\uFF5E');
		}
	}
}
