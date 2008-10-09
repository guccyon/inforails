package jp.co.infonic.common.db;

import java.sql.SQLException;

import jp.co.infonic.common.db.jdbc.DBConnection;
import jp.co.infonic.common.db.jdbc.DBPreparedStatement;
import jp.co.infonic.common.db.jdbc.DataSourceManager;

/**
 * 
 * �T�v�F<br>
 * �ڍׁF<br>
 *<pre>
 * [�ύX����]
 * ���t        �A��   ���O      ���e
 * --------------------------------------------------
 * 2006/09/24  VL000  ���      �V�K
 *</pre>
 * @author ���
 */
public class DBUpdate {
	
	// SQL�I�u�W�F�N�g
	private SqlObj sql;
	
	// �u��������
	private String[] replace;

	// �C���X�^���X�����N���X
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
