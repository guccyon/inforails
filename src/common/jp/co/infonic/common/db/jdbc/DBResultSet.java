package jp.co.infonic.common.db.jdbc;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;

/**
 * 
 * �T�v�F���U���g�Z�b�g�����b�v���܂��B<br>
 * �ڍׁF���U���g�Z�b�g�����b�v���܂��B<br>
 *<pre>
 * [�ύX����]
 * ���t        �A��   ���O      ���e
 * --------------------------------------------------
 * 2006/03/20  VL000  ���      �V�K
 *</pre>
 * @author higuchit
 */
public class DBResultSet {
	
	// ���ʃZ�b�g
	private ResultSet rs;
	
	// ���̃C���X�^���X�𐶐������v���y�A�h�X�e�C�g�����g
	private DBPreparedStatement preState;
	
	DBResultSet(ResultSet rs, DBPreparedStatement preState) {
		this.rs = rs;
		this.preState = preState;
	}
	
	/**
	 * 
	 * �T�v: ���\�[�X���N���[�Y���܂��B<br>
	 * �ڍ�: ���\�[�X���N���[�Y���܂��B<br>
	 * ���l: �Ȃ�<br>
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
	 * �T�v: ���ʃZ�b�g�̃��^�f�[�^���擾���܂��B<br>
	 * �ڍ�: ���ʃZ�b�g�̃��^�f�[�^���擾���܂��B<br>
	 * ���l: �Ȃ�<br>
	 *
	 * @return
	 * @throws SQLException
	 */
	public ResultSetMetaData getMetaData() throws SQLException {
		return rs.getMetaData();
	}
	
	/**
	 * 
	 * �T�v: ���ʃZ�b�g�̃J�[�\���̌��݂̈ʒu��莟�̃��R�[�h�����݂��邩�m�F����<br>
	 * �ڍ�: ���ʃZ�b�g�̃J�[�\���̌��݂̈ʒu��莟�̃��R�[�h�����݂��邩�m�F����<br>
	 *       ���݂���ꍇ�́A�J�[�\������i�߂�true��Ԃ��B<br>
	 *       ���݂��Ȃ��ꍇ��false��Ԃ��B<br>
	 * ���l: �Ȃ�<br>
	 *
	 * @return
	 * @throws SQLException
	 */
	public boolean next() throws SQLException {
		return rs.next();
	}
	
	/**
	 * 
	 * �T�v: ���ʃ��R�[�h����Y���J�����̃I�u�W�F�N�g���擾���܂��B<br>
	 * �ڍ�: ���ʃ��R�[�h����Y���J�����̃I�u�W�F�N�g���擾���܂��B<br>
	 * ���l: �Ȃ�<br>
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
	 * �T�v: ���ʃ��R�[�h����Y���J�����̃I�u�W�F�N�g���擾���܂��B<br>
	 * �ڍ�: ���ʃ��R�[�h����Y���J�����̃I�u�W�F�N�g���擾���܂��B<br>
	 * ���l: �Ȃ�<br>
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
	 * �T�v: ���ʃ��R�[�h����Y���J�����̕�������擾���܂��B<br>
	 * �ڍ�: ���ʃ��R�[�h����Y���J�����̕�������擾���܂��B<br>
	 * ���l: �Ȃ�<br>
	 *
	 * @param colIndex
	 * @return
	 */
	public String getString(int colIndex) throws SQLException {
		return rs.getString(colIndex);
	}
	
	/**
	 * 
	 * �T�v: ���ʃ��R�[�h����Y���J�����̕�������擾���܂��B<br>
	 * �ڍ�: ���ʃ��R�[�h����Y���J�����̕�������擾���܂��B<br>
	 * ���l: �Ȃ�<br>
	 *
	 * @param colIndex
	 * @return
	 */
	public String getString(String colStr) throws SQLException {
		return rs.getString(colStr);
	}
	
	/**
	 * 
	 * �T�v: ���ʃ��R�[�h����Y���J�����̐��l���擾���܂��B<br>
	 * �ڍ�: ���ʃ��R�[�h����Y���J�����̐��l���擾���܂��B<br>
	 * ���l: �Ȃ�<br>
	 *
	 * @param colIndex
	 * @return
	 */
	public int getInt(int colIndex) throws SQLException {
		return rs.getInt(colIndex);
	}
	
	/**
	 * 
	 * �T�v: ���ʃ��R�[�h����Y���J�����̐��l���擾���܂��B<br>
	 * �ڍ�: ���ʃ��R�[�h����Y���J�����̐��l���擾���܂��B<br>
	 * ���l: �Ȃ�<br>
	 *
	 * @param colIndex
	 * @return
	 */
	public int getInt(String colStr) throws SQLException {
		return rs.getInt(colStr);
	}
	
	/**
	 * 
	 * �T�v: ���ʃ��R�[�h����Y���J�����̓��t���擾���܂��B<br>
	 * �ڍ�: ���ʃ��R�[�h����Y���J�����̓��t���擾���܂��B<br>
	 * ���l: �Ȃ�<br>
	 *
	 * @param colIndex
	 * @return
	 */
	public Date getDate(int colIndex) throws SQLException {
		return rs.getDate(colIndex);
	}
	
	/**
	 * 
	 * �T�v: ���ʃ��R�[�h����Y���J�����̓��t���擾���܂��B<br>
	 * �ڍ�: ���ʃ��R�[�h����Y���J�����̓��t���擾���܂��B<br>
	 * ���l: �Ȃ�<br>
	 *
	 * @param colIndex
	 * @return
	 */
	public Date getDate(String colStr) throws SQLException {
		return rs.getDate(colStr);
	}
	
	/**
	 * 
	 * �T�v: ���ʃ��R�[�h����Y���J�����̓��t���擾���܂��B<br>
	 * �ڍ�: ���ʃ��R�[�h����Y���J�����̓��t���擾���܂��B<br>
	 * ���l: �Ȃ�<br>
	 *
	 * @param colIndex
	 * @return
	 */
	public Date getTimestamp(int colIndex) throws SQLException {
		return rs.getTimestamp(colIndex);
	}
	
	/**
	 * 
	 * �T�v: ���ʃ��R�[�h����Y���J�����̓��t���擾���܂��B<br>
	 * �ڍ�: ���ʃ��R�[�h����Y���J�����̓��t���擾���܂��B<br>
	 * ���l: �Ȃ�<br>
	 *
	 * @param colIndex
	 * @return
	 */
	public Date getTimestamp(String colStr) throws SQLException {
		return rs.getTimestamp(colStr);
	}
	
	
	
	/**
	 * �T�v: ���݃��R�[�h����Y���J�����̒l��Blob�^�Ŏ擾���܂��B<br>
	 * �ڍ�: ���݃��R�[�h����Y���J�����̒l��Blob�^�Ŏ擾���܂��B<br>
	 * ���l: �Ȃ�<br>
	 *
	 * @param colIndex
	 * @return
	 * @throws SQLException
	 */
	public Blob getBlob(int colIndex) throws SQLException {
		return rs.getBlob(colIndex);
	}
	
	/**
	 * �T�v: ���݃��R�[�h����Y���J�����̒l��Blob�^�Ŏ擾���܂��B<br>
	 * �ڍ�: ���݃��R�[�h����Y���J�����̒l��Blob�^�Ŏ擾���܂��B<br>
	 * ���l: �Ȃ�<br>
	 *
	 * @param colIndex
	 * @return
	 * @throws SQLException
	 */
	public Blob getBlob(String colStr) throws SQLException {
		return rs.getBlob(colStr);
	}
}
