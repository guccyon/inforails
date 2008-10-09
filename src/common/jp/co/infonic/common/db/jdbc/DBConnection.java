package jp.co.infonic.common.db.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jp.co.infonic.common.db.SqlObj;
import jp.co.infonic.common.module.log.DebugLogger;
import jp.co.infonic.common.util.Assertion;

import org.apache.log4j.Logger;

/**
 * 
 * �T�v�F�c�a�R�l�N�V�������b�p�N���X<br>
 * �ڍׁF�c�a�R�l�N�V�������b�p�N���X<br>
 * ���̃N���X�̃��\�b�h�̓}���`�X���b�h��<br>
 * �T�|�[�g���Ă��܂���B<br>
 * 
 * <pre>
 *   [�ύX����]
 *   ���t        �A��   ���O      ���e
 *   --------------------------------------------------
 *   2006/03/20  VL000  ���      �V�K
 * </pre>
 * 
 * @author higuchit
 */
public class DBConnection {

	// DB�ւ�Connection�C���X�^���X
	private Connection connection = null;

	// �I�[�g�R�~�b�g
	private boolean autoCommit;

	// �g�����U�N�V�����J�n���t���O
	private boolean transaction = false;

	// �R�l�N�V�����쐬�N���X
	private Class className;

	private List preStateList;

	// �l�X�g�g�����U�N�V�����p�q���c�a�R�l�N�V����
	private List childCon;
	
	// �e�̂c�a�R�l�N�V����
	private DBConnection parent;
	
	private String dataSourceName;
	
	// �f�o�b�O���K�[
	private Logger logger = DebugLogger.getLogger(this.getClass());

	/**
	 * �R���X�g���N�^
	 * 
	 * @param con
	 * @param autoCommit
	 */
	DBConnection(Connection con, Class cla, boolean autoCommit) {
		Assertion.isNotNull(cla, "�����N���X��null�ł��B");
		
		this.connection = con;
		this.className = cla;
		this.autoCommit = autoCommit;
		preStateList = new ArrayList();
	}

	/**
	 * �l�X�g�g�����U�N�V�����p�R���X�g���N�^
	 * 
	 * @param con
	 * @param autoCommit
	 */
	DBConnection(DBConnection parent, Connection con, Class cla, boolean autoCommit) {
		Assertion.isNotNull(cla, "�����N���X��null�ł��B");

		this.parent = parent;
		this.connection = con;
		this.className = cla;
		this.autoCommit = autoCommit;
		preStateList = new ArrayList();
	}
	
	void setDataSourceName(String name) {
		this.dataSourceName = name;
	}
	
	String getDataSourceName() {
		return this.dataSourceName;
	}

	/**
	 * 
	 * �T�v: �l�X�g���ꂽ�g�����U�N�V�����ׂ̈̃R�l�N�V�������q�ɃZ�b�g<br>
	 * �ڍ�: �l�X�g���ꂽ�g�����U�N�V�����ׂ̈̃R�l�N�V�������q�ɃZ�b�g<br>
	 * ���l: �Ȃ�<br>
	 * 
	 * @param con
	 */
	void setChildCon(DBConnection con) {
		if (childCon == null) {
			childCon = new ArrayList();
		}
		
		childCon.add(con);
	}
	
	/**
	 * 
	 * �T�v: �l�X�g���ꂽ�g�����U�N�V�����ׂ̈̃R�l�N�V�������폜����B<br>
	 * �ڍ�: �l�X�g���ꂽ�g�����U�N�V�����ׂ̈̃R�l�N�V�������폜����B<br>
	 * ���l: �Ȃ�<br>
	 *
	 * @param con
	 */
	void removeChildCon(DBConnection con) {
		if (childCon != null && childCon.contains(con)) {
			childCon.remove(con);
		}
	}

	/**
	 * 
	 * �T�v: �g�����U�N�V�������J�n���Ă��邩�ǂ����`�F�b�N����B<br>
	 * �ڍ�: �g�����U�N�V�������J�n���Ă��邩�ǂ����`�F�b�N����B<br>
	 * ���l: �Ȃ�<br>
	 * 
	 * @return
	 */
	boolean isReadyExecute() {
		if (autoCommit) {
			return true;
		} else if (transaction) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 
	 * �T�v: �v���y�A�h�X�e�C�g�����g���X�g����Ώۂ̃X�e�C�g�����g���폜����B<br>
	 * �ڍ�: �v���y�A�h�X�e�C�g�����g���X�g����Ώۂ̃X�e�C�g�����g���폜����B<br>
	 * ���l: �Ȃ�<br>
	 *
	 * @param preState
	 */
	void removePreState(DBPreparedStatement preState) {
		if (preStateList.contains(preState)) {
			preStateList.remove(preState);
		}
	}

	/**
	 * 
	 * �T�v: �g�����U�N�V�����J�n���ɌĂяo���B<br>
	 * �ڍ�: �g�����U�N�V�����J�n���ɌĂяo���B<br>
	 * ���l: �Ȃ�<br>
	 * 
	 */
	public void transactionStart() {

		checkConnection();

		if (!autoCommit) {

			// �g�����U�N�V���������ɊJ�n���Ă����
			if (!transaction) {
				// �g�����U�N�V�����̊J�n�t���OON
				transaction = true;
			} else {
				String msg = "�l�X�g�g�����U�N�V���������m���܂����B,�ďo���N���X��="
						+ className.getName();
				
				logger.error(msg);
			}

		}
	}

	/**
	 * 
	 * �T�v: �v���y�A�h�X�e�C�g�����g���擾���܂��B<br>
	 * �ڍ�: �v���y�A�h�X�e�C�g�����g���擾���܂��B<br>
	 * ���l: �Ȃ�<br>
	 * 
	 * @param sqlObj
	 * @param replaceStr
	 * @return
	 * @throws SQLException
	 */
	public DBPreparedStatement prepareStatement(SqlObj sqlObj)
			throws SQLException {
		return prepareStatement(sqlObj, new String[0]);
	}

	/**
	 * 
	 * �T�v: �v���y�A�h�X�e�C�g�����g���擾���܂��B<br>
	 * �ڍ�: �v���y�A�h�X�e�C�g�����g���擾���܂��B<br>
	 *       SQL�����̒u���Ώە�����������F�u��������ƒu��������<br>
	 *       �쐬���܂��B
	 *       �u��������̒u���������@
	 *       SQL������ {1},{2},�c{n} ����������ꂼ��
	 *       {1} -> ����[0], {2} -> ����[1]
	 *       �ƒu�������܂��B
	 * <br>
	 * ���l: �Ȃ�<br>
	 * 
	 * @param sqlObj SQL�I�u�W�F�N�g
	 * @param replaceStr �u��������
	 * @return
	 * @throws SQLException
	 */
	public DBPreparedStatement prepareStatement(SqlObj sqlObj,
			String[] replaceStr) throws SQLException {

		checkConnection();

		String sql = sqlObj.getSqlString(replaceStr);

		PreparedStatement prepared = connection.prepareStatement(sql);
		DBPreparedStatement preState = new DBPreparedStatement(this, prepared, className, sql);

		this.preStateList.add(preState);

		return preState;
	}

	/**
	 * 
	 * �T�v: �g�����U�N�V���������[���o�b�N���܂��B<br>
	 * �ڍ�: �g�����U�N�V���������[���o�b�N���܂��B<br>
	 * ���l: �Ȃ�<br>
	 * 
	 * @throws SQLException
	 */
	public void rollback() throws SQLException {

		checkConnection();

		if (autoCommit) {
			throw new IllegalStateException("�����R�~�b�g���[�h�ł��B");

		} else {
			// ���[���o�b�N
			this.connection.rollback();
			// �g�����U�N�V�����̊J�n�t���OOFF
			this.transaction = false;
		}
	}

	/**
	 * 
	 * �T�v: �g�����U�N�V�������R�~�b�g���܂��B<br>
	 * �ڍ�: �g�����U�N�V�������R�~�b�g���܂��B<br>
	 * ���l: �Ȃ�<br>
	 * 
	 * @throws SQLException
	 */
	public void commit() throws SQLException {

		checkConnection();

		// �R�~�b�g
		this.connection.commit();
		// �g�����U�N�V�����̊J�n�t���OOFF
		this.transaction = false;
	}

	/**
	 * 
	 * �T�v: �R�l�N�V�������N���[�Y���܂��B<br>
	 * �ڍ�: �R�l�N�V�������N���[�Y���܂��B<br>
	 * ���̃R�l�N�V��������쐬���ꂽ�X�e�C�g�����g��<br>
	 * �S�ăN���[�Y���܂��B<br>
	 * ���l: �Ȃ�<br>
	 * 
	 * @throws SQLException
	 */
	public void close() throws SQLException {

		// ��O�������������𑱂��邽�߁A�ŏ��ɔ���������O��ێ�����
		SQLException se = null;

		// null�̏ꍇ�̓N���[�Y��
		if (this.connection == null) {
			return;
		}
		
		// �l�X�g���Ď擾���ꂽ�R�l�N�V������S�ĕ���
		if (childCon != null) {
			for (int i = 0; i < childCon.size(); i++) {
				try {
					DBConnection child = (DBConnection) childCon.get(i);
					child.isClosed();
				} catch (SQLException sqe) {
					if (se == null) {
						se = sqe;
					}
				}
			}
		}

		// ���X�g���̃v���y�A�h�X�e�C�g�����g��S�N���[�Y
		for (int i = 0; i < this.preStateList.size(); i++) {
			DBPreparedStatement preState = (DBPreparedStatement)preStateList.get(i);
			try {
				preState.cascadeClose();
				
			} catch (SQLException e) {
				if (se == null) {
					se = e;
				}
			}
		}

		// �R�~�b�g�A���[���o�b�N�R��̌��o
		if (transaction) {
			String msg = "�R�~�b�g�܂��̓��[���o�b�N�R������m���܂����B���������[���o�b�N���܂��B,�ďo���N���X��=" + className.getName();

			logger.error(msg);
			
			try {
				// ���[���o�b�N
				connection.rollback();
				transaction = false;
			} catch (SQLException e) {
				if (se == null) {
					se = e;
				}
			}
		}

		try {
			// �R�l�N�V�����̃N���[�Y
			connection.close();
			this.connection = null;
		} catch (SQLException e) {
			if (se == null) {
				se = e;
			}
		}
		
		if (parent != null) {
			parent.removeChildCon(this);
			parent = null;
		}

		// ��O���������Ă���΃X���[����
		if (se != null) {
			throw se;
		}
	}

	/**
	 * 
	 * �T�v: �R�l�N�V�������N���[�Y����Ă��邩���肵�܂��B<br>
	 * �ڍ�: �R�l�N�V�������N���[�Y����Ă��邩���肵�܂��B<br>
	 * ���l: �Ȃ�<br>
	 * 
	 * @return �N���[�Y�ς݁Ftrue �N���[�Y�R��:false
	 * @throws SQLException
	 */
	boolean isClosed() throws SQLException {

		if (this.connection != null) {

			this.close();

			String msg = "�N���[�Y�R�ꔭ�������m���܂����B,�ďo���N���X��=" + className.getName() + " �X���b�h=" + Thread.currentThread().getName();
			
			logger.error(msg);
			
			return false;
		} else {
			return true;
		}
	}

	/**
	 * �T�v: �R�l�N�V�������N���[�Y����Ă��邩�`�F�b�N���܂��B<br>
	 * �ڍ�: �R�l�N�V�������N���[�Y����Ă��邩�`�F�b�N���܂��B<br>
	 * ���ɃN���[�Y����Ă����ꍇ��IllegalStateException�𑗏o���܂��B<br>
	 * ���l: �Ȃ�<br>
	 * 
	 */
	private void checkConnection() {
		if (connection == null) {
			throw new IllegalStateException("�R�l�N�V�������N���[�Y����Ă��܂��B");
		}
	}

}
