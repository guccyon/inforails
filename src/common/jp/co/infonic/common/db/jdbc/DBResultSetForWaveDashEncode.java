package jp.co.infonic.common.db.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * �T�v�FOracle�p��WAVE DASH�u���@�\��DBResultSet<br>
 * �ڍׁF<br>
 *<pre>
 * [�ύX����]
 * ���t        �A��   ���O      ���e
 * --------------------------------------------------
 * 2006/07/05  VL000        �V�K
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
	 * �T�v: WAVE DASH��FULL WIDTH TILDE�ɒu��<br>
	 * �ڍ�: <br>
	 * ���l: �Ȃ�<br>
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
