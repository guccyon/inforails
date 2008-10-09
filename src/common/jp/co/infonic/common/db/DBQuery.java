package jp.co.infonic.common.db;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import jp.co.infonic.common.db.jdbc.DBConnection;
import jp.co.infonic.common.db.jdbc.DBPreparedStatement;
import jp.co.infonic.common.db.jdbc.DataSourceManager;

/**
 * 
 * �T�v�F�c�a�Q�ƃA�N�Z�X���̎菇���o�C���f�B���O���܂��B<br>
 * �ڍׁF�c�a�Q�ƃA�N�Z�X���̎菇���o�C���f�B���O���܂��B
 *       �J���҂͉��L�̃R�[�f�B���O�����邾���ŁA��y�ɂr�p�k�����s�ł��܂��B<br>
 *       = �� =<br>
 *       List result = new DBQuery(this.getClass(), "���sSQLID").executeQueryForList(null);<br>
 *<br>       
 *       executeQueryForMap(), executeQueryForList()�@���\�b�h�̖߂�l��<br>
 *       �f�[�^�\���ɂ��Ă�DBPreparedStatement�̃h�L�������g���Q�Ƃ��Ă��������B<br>
 *<pre>
 * [�ύX����]
 * ���t        �A��   ���O      ���e
 * --------------------------------------------------
 * 2006/04/15  VL000  ���      �V�K
 *</pre>
 */
public class DBQuery {

	// SQL�I�u�W�F�N�g
	private SqlObj sql;
	
	// �u��������
	private String[] replace;

	// �C���X�^���X�����N���X
	private Class cla;

	// �R���X�g���N�^
	public DBQuery(Class cla, String sqlId) {
		this.cla = cla;
		sql = SqlManager.getSql(cla, sqlId);
		this.replace = new String[0];
	}

	// �R���X�g���N�^
	public DBQuery(Class cla, String sqlId, String[] replace) {
		this.cla = cla;
		this.replace = replace;
		sql = SqlManager.getSql(cla, sqlId);
	}

	/**
	 * 
	 * �T�v: �r�p�k�𔭍s���}�b�v�Ƃ��āA���ʃZ�b�g��Ԃ��܂��B<br>
	 * �ڍ�: �r�p�k�𔭍s���}�b�v�Ƃ��āA���ʃZ�b�g��Ԃ��܂��B<br>
	 *       ���ʂ��O���̏ꍇnull��Ԃ��܂��B<br>
	 * ���l: �Ȃ�<br>
	 * 
	 * @param params �v���y�A�h�X�e�C�g�����g�o�C���h�ϐ�
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
	 * �T�v: �r�p�k�𔭍s�����X�g�Ƃ��āA���ʃZ�b�g��Ԃ��܂��B<br>
	 * �ڍ�: �r�p�k�𔭍s�����X�g�Ƃ��āA���ʃZ�b�g��Ԃ��܂��B<br>
	 * ���l: �Ȃ�<br>
	 * 
	 * @param params �v���y�A�h�X�e�C�g�����g�o�C���h�ϐ�
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
	 * �T�v: �P�s���̃��R�[�h�𒊏o���܂��B<br>
	 * �ڍ�: �P�s���̃��R�[�h�𒊏o���܂��B<br>
	 * �@�@�@�P�s���I������Ȃ������ꍇ��null��<br>
	 * �@�@�@�Ԃ��܂��B<br>
	 * ���l: �Ȃ�<br>
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
