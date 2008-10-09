package jp.co.infonic.common.db.jdbc;

import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.infonic.common.module.log.DBAccessLogger;

/**
 * 
 * �T�v�F�v���y�A�h�X�e�C�g�����g���b�v�N���X<br>
 * �ڍׁF�v���y�A�h�X�e�C�g�����g���b�v�N���X<br>
 *       ���̃N���X��DBConnection�N���X�ɂ���ăC���X�^���X������<br>
 *       DB�ւ̃N�G���̔��s�p�̋@�\��񋟂��܂��B<br>
 *<pre>
 * [�ύX����]
 * ���t        �A��   ���O      ���e
 * --------------------------------------------------
 * 2006/03/22  VL000        �V�K
 *</pre>
 * @author higuchit
 */
public class DBPreparedStatement {

	// �v���y�A�h�X�e�C�g�����g
	private PreparedStatement preState;

	// ���̃C���X�^���X�𐶐������R�l�N�V����
	private DBConnection conn;

	// ���̃C���X�^���X�𐶐������Ăяo���N���X
	private Class className;

	// SQL��
	private String sql;

	// Bind�ϐ��u������
	private String[] bind;

	// ���U���g�Z�b�g���X�g
	private List resultSetList = new ArrayList();
	
	// SQL���s�󋵃��K�[
	private DBAccessLogger logger;

	/**
	 * �R���X�g���N�^
	 * 
	 * @param con
	 * @param prepared
	 * @param cla
	 * @param sqlStr
	 */
	DBPreparedStatement(DBConnection con, PreparedStatement prepared,
			Class cla, String sqlStr) {
		this.preState = prepared;
		this.conn = con;
		this.sql = sqlStr;
		className = cla;
		bind = new String[countParamNum(sql)];
		logger = DBAccessLogger.getLogger(className);
	}

	/**
	 * 
	 * �T�v: ���̃v���y�A�h�X�e�C�g�����g���N���[�Y���܂��B<br>
	 * �ڍ�: ���̃v���y�A�h�X�e�C�g�����g���N���[�Y���܂��B<br>
	 * ���l: �Ȃ�<br>
	 * 
	 * @throws SQLException
	 */
	public void close() throws SQLException {
		if (preState != null) {
			preState.close();
			conn.removePreState(this);
			conn = null;
		}

		preState = null;
	}

	/**
	 * 
	 * �T�v: �����������U���g�Z�b�g���S�ăN���[�Y����B<br>
	 * �ڍ�: �����������U���g�Z�b�g���S�ăN���[�Y����B<br>
	 * ���l: �Ȃ�<br>
	 * 
	 * @throws SQLException
	 */
	void cascadeClose() throws SQLException {

		SQLException sqe = null;

		for (int i = 0; resultSetList.size() > i; i++) {
			try {
				DBResultSet rs = (DBResultSet) resultSetList.get(i);
				rs.close();

			} catch (SQLException se) {
				if (sqe == null) {
					sqe = se;
				}
			}
		}

		this.close();

		if (sqe != null) {
			throw sqe;
		}
	}

	/**
	 * 
	 * �T�v: ���U���g�Z�b�g���X�g����Ώۂ̃��U���g�Z�b�g���폜���܂��B<br>
	 * �ڍ�: ���U���g�Z�b�g���X�g����Ώۂ̃��U���g�Z�b�g���폜���܂��B<br>
	 * ���l: �Ȃ�<br>
	 * 
	 * @param rs
	 */
	void removeResultSet(DBResultSet rs) {

		if (resultSetList.contains(rs)) {
			resultSetList.remove(rs);
		}
	}
	
	/**
	 * �T�v: SQL�����s���܂��B<br>
	 * �ڍ�: �������ʂ�Ԃ��܂��B<br>
	 * ���l: �Ȃ�<br>
	 * @return
	 * @throws SQLException
	 * @since 2006/04/15
	 */
	public void execute() throws SQLException {

		long start = System.currentTimeMillis();
		preState.execute();

		long end = System.currentTimeMillis();
		// SQL�̔��s�󋵃��O���o�͂���B
		logger.info("##execute## " + getReplaceSql(), start, end);
	}

	/**
	 * �T�v: INSERT,UPDATE,DELETE �������s���܂��B�������ʌ�����Ԃ��܂��B<br>
	 * �ڍ�: INSERT,UPDATE,DELETE �������s���܂��B�������ʌ�����Ԃ��܂��B<br>
	 * ���l: �Ȃ�<br>
	 * 
	 * @return �������ʌ���
	 * @throws SQLException
	 */
	public int executeUpdate() throws SQLException {

		checkStatement();

		if (!conn.isReadyExecute()) {
			throw new IllegalStateException("�g�����U�N�V�������J�n����Ă��܂��� �Ăяo�����F"
					+ className.getName());
		}

		long start = System.currentTimeMillis();
		// SQL���s
		int result = preState.executeUpdate();
		
		long end = System.currentTimeMillis();
		// SQL�̔��s�󋵃��O���o�͂���B
		logger.info("##executeUpdate## " + getReplaceSql(), start, end);

		return result;
	}

	/**
	 * 
	 * �T�v: SQL�����s�����ʃZ�b�g��Ԃ��܂��B<br>
	 * �ڍ�: SQL�����s�����ʃZ�b�g��Ԃ��܂��B<br>
	 * ���l: �Ȃ�<br>
	 * 
	 * @return
	 * @throws SQLException
	 */
	public DBResultSet executeQuery() throws SQLException {

		checkStatement();
		
		long start = System.currentTimeMillis();
		// SQL���s
		ResultSet rs = preState.executeQuery();
		
		long end = System.currentTimeMillis();
		// SQL�̔��s�󋵃��O���o�͂���B
		logger.info("##executeQuery## " + getReplaceSql(), start, end);

		DBResultSet resultSet = new DBResultSet(rs, this);

		resultSetList.add(resultSet);

		return resultSet;
	}
	
	/**
	 * 
	 * �T�v: SQL�����s��Oracle��WAVE DASH���ɑΉ��������ʃZ�b�g��Ԃ��܂��B<br>
	 * �ڍ�: SQL�����s��Oracle��WAVE DASH���ɑΉ��������ʃZ�b�g��Ԃ��܂��B<br>
	 * ���l: �Ȃ�<br>
	 * 
	 * @return
	 * @throws SQLException
	 */
	public DBResultSet executeQueryForWaveDashEncode() throws SQLException {

		checkStatement();
		
		long start = System.currentTimeMillis();
		// SQL���s
		ResultSet rs = preState.executeQuery();
		
		long end = System.currentTimeMillis();
		// SQL�̔��s�󋵃��O���o�͂���B
		logger.info("##executeQuery## " + getReplaceSql(), start, end);

		DBResultSet resultSet = new DBResultSetForWaveDashEncode(rs, this);

		resultSetList.add(resultSet);

		return resultSet;
	}

	/**
	 * �T�v: �r�p�k�𔭍s���}�b�v�Ƃ��āA���ʃZ�b�g��Ԃ��܂��B<br>
	 * �ڍ�: �r�p�k�𔭍s���}�b�v�Ƃ��āA���ʃZ�b�g��Ԃ��܂��B<br>
	 * ���l: �Ȃ�<br>
	 *       �}�b�v�̍\��
	 *       Map(�J������1, List) , (�J������2, List), (�J������3, List)
	 *       List�̒��ɂP���ڂ���`�ŏI���R�[�h�܂ł̒l���i�[����Ă��܂��B
	 *
	 * @param params �o�C���h�ϐ�
	 * @return
	 * @throws SQLException
	 */
	public Map executeQueryForMap(Object[] params) throws SQLException {

		DBResultSet rs = null;
		try {
			setBindDataAll(params);
			rs = executeQuery();

			ResultSetMetaData rsmd = rs.getMetaData();
			Map map = new HashMap();

			while (rs.next()) {

				for (int columIndex = 1; columIndex <= rsmd.getColumnCount(); columIndex++) {
					String columnName = rsmd.getColumnName(columIndex);
					List columnList = (List) map.get(columnName);
					if (columnList == null) {
						columnList = new ArrayList();
						map.put(columnName, columnList);
					}

					columnList.add(rs.getObject(columIndex));
				}
			}

			return map;

		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

	/**
	 * �T�v: �r�p�k�𔭍s�����X�g�Ƃ��āA���ʃZ�b�g��Ԃ��܂��B<br>
	 * �ڍ�: �r�p�k�𔭍s�����X�g�Ƃ��āA���ʃZ�b�g��Ԃ��܂��B<br>
	 * ���l: �Ȃ�<br>
	 *       ���X�g�̍\��
	 *       ����
	 *       1    Map(�J������, �P���ڂ̒l)
	 *       2    Map(�J������, �Q���ڂ̒l)
	 *       3    Map(�J������, �R���ڂ̒l)
	 *       4    Map(�J������, �S���ڂ̒l)
	 *       
	 *       ��L�̂悤��Map�̔z�񂪃��X�g�̒��Ɋi�[����Ă��܂��B
	 *
	 * @param params �o�C���h�ϐ�
	 * @return
	 * @throws SQLException
	 */
	public List executeQueryForList(Object[] params) throws SQLException {

		DBResultSet rs = null;
		try {
			setBindDataAll(params);
			rs = executeQuery();

			ResultSetMetaData rsmd = rs.getMetaData();
			List list = new ArrayList();

			while (rs.next()) {

				Map record = new HashMap();
				
				for (int columIndex = 1; columIndex <= rsmd.getColumnCount(); columIndex++) {
					String columnName = rsmd.getColumnName(columIndex);
					record.put(columnName, rs.getObject(columIndex));
				}
				
				list.add(record);
			}

			return list;

		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

	/**
	 * �T�v: �w�肳�ꂽ�p�����[�^���X�e�C�g�����g�ɃZ�b�g���܂��B<br>
	 * �ڍ�: �w�肳�ꂽ�p�����[�^���X�e�C�g�����g�ɃZ�b�g���܂��B<br>
	 * ���l: �Ȃ�<br>
	 * 
	 * @param parameterIndex
	 * @param s
	 * @throws SQLException
	 */
	public void setString(int parameterIndex, String s) throws SQLException {
		
		if (s == null) {
			throw new IllegalArgumentException("�p�����[�^��null�ł��B");
		}

		/*
		 * �Z�b�g���悤�Ƃ��Ă���f�[�^���AJDBC�h���C�oVARCHAR�^�̕�����������������
		 * ����ꍇ�AsetString�ł̓G���[�ƂȂ�ׁAsetCharacterStream�𗘗p����
		 */
		if (s.getBytes().length > 667) {
			preState.setCharacterStream(parameterIndex, new StringReader(s),
					4000);
		} else {
			preState.setString(parameterIndex, s);
		}
		
		bind[parameterIndex - 1] = s;

	}

	/**
	 * 
	 * �T�v: �v���y�A�h�X�e�C�g�����g�̃o�C���h�ϐ��ƒl��ϊ����܂��B<br>
	 * �ڍ�: �v���y�A�h�X�e�C�g�����g�̃o�C���h�ϐ��ƒl��ϊ����܂��B<br>
	 * ���l: �Ȃ�<br>
	 * 
	 * @param params
	 * @throws SQLException
	 */
	public void setBindDataAll(Object[] params) throws SQLException {

		// ������null�̏ꍇ�����s��Ȃ��B
		if (params == null) {
			return;
		}

		for (int i = 0; i < params.length; i++) {
			Object x = params[i];
			int paramIndex = i + 1;
			if (x == null) {
				throw new IllegalArgumentException("�p�����[�^��null�ł��Bindex :" + i);
			} else if (x instanceof String) {
				setString(paramIndex, (String) x);
			} else if (x instanceof Date) {
				preState.setDate(paramIndex, (Date) x);
			} else if (x instanceof Integer) {
				preState.setInt(paramIndex, ((Integer) x).intValue());
			} else if (x instanceof BigDecimal) {
				preState.setBigDecimal(paramIndex, (BigDecimal) x);
			} else if (x instanceof Blob) {
				preState.setBlob(paramIndex, (Blob) x);
			} else if (x instanceof java.util.Date) {
				preState.setTimestamp(paramIndex, new Timestamp(((java.util.Date)x).getTime()));
			} else {
				throw new IllegalArgumentException(
						"�����̌^���T�|�[�g����Ă��Ȃ��^�ł��B index :" + i + " �^ :"
								+ x.getClass().getName());
			}

			bind[i] = x.toString();
		}
	}

	/**
	 * 
	 * �T�v: �v���y�A�h�X�e�C�g�����g�̃o�C���h�ϐ��ƒl��ϊ����܂��B<br>
	 * �ڍ�: �v���y�A�h�X�e�C�g�����g�̃o�C���h�ϐ��ƒl��ϊ����܂��B<br>
	 * ���l: �Ȃ�<br>
	 * 
	 * @param paramList
	 * @throws SQLException
	 */
	public void setBindDataAll(List paramList) throws SQLException {
		setBindDataAll(paramList.toArray(new Object[0]));
	}

	/**
	 * �T�v: �v���y�A�h�X�e�C�g�����g���N���[�Y����Ă��邩�`�F�b�N���܂��B<br>
	 * �ڍ�: �v���y�A�h�X�e�C�g�����g���N���[�Y����Ă��邩�`�F�b�N���܂��B<br>
	 * ���ɃN���[�Y����Ă����ꍇ��IllegalStateException�𑗏o���܂��B<br>
	 * ���l: �Ȃ�<br>
	 * 
	 */
	private void checkStatement() {
		if (preState == null) {
			throw new IllegalStateException("�v���y�A�h�X�e�C�g�����g���N���[�Y����Ă��܂��B");
		}
	}
	
	/**
	 * 
	 * �T�v: SQL�����̃o�C���h�ϐ����Z�b�g���ꂽ�����ɒu�������܂��B<br>
	 * �ڍ�: SQL�����̃o�C���h�ϐ����Z�b�g���ꂽ�����ɒu�������܂��B<br>
	 * ���l: �Ȃ�<br>
	 *
	 * @return
	 */	
	private String getReplaceSql() {
		
		String result = sql;
		int offset = 0;
		for (int i = 0; i < bind.length; i++) {
			if ((offset = result.indexOf("?", offset)) != -1) {
				String before = result.substring(0, offset);
				if (offset + 1 < result.length()) {
					result = before + "'" + bind[i] + "'" + result.substring(offset + 1);
					offset += bind[i].length() + 2;
				} else {
					result = before + "'" + bind[i] + "'";
				}
			}
		}
			
		return result;
	}
	

	/**
	 * 
	 * �T�v: SQL�����̃o�C���h�ϐ��h�H�h�̌����J�E���g���܂��B<br>
	 * �ڍ�: SQL�����̃o�C���h�ϐ��h�H�h�̌����J�E���g���܂��B<br>
	 * ���l: �Ȃ�<br>
	 * 
	 * @param sql
	 * @return
	 */
	private int countParamNum(String sql) {

		int result = 0;
		int start = 0;
		while ((start = sql.indexOf("?", start)) != -1) {
			result++;
			start++;
		}
		return result;
	}

}
