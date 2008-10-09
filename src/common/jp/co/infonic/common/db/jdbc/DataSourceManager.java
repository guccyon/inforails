package jp.co.infonic.common.db.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import jp.co.infonic.common.db.jdbc.properties.ConnectPropeties;
import jp.co.infonic.common.module.log.DebugLogger;
import jp.co.infonic.common.util.Assertion;

/**
 * 
 * �T�v�F�f�[�^�\�[�X�Ǘ�<br>
 * �ڍׁF�f�[�^�\�[�X�Ǘ��B�c�a�ڑ����ꌳ�I�ɊǗ����܂��B<br>
 *       JDBC�h���C�o�ɂ��c�a�ڑ����̊e�N���X�����b�v���A<br>
 *       �Ԃ��܂��B�擾���ꂽ�C���X�^���X�ւ̎Q�Ƃ̓f�[�^�\�[�X�Ǘ��N���X�ɂ��<br>
 *       �ꌳ�Ǘ�����A���\�[�X�̃N���[�Y�R���h���܂��B<br>
 *       <br>
 *       = ���p���@�� =<br>
 *       <br>
 *       DBConnection conn = null;<br>
 *       try {<br>
 *       	// �R�l�N�V�����擾 �R���e�L�X�g���v�[�����O���ꂽ�R�l�N�V�������擾���܂��B<br>
 *       	conn = DataSourceManager.getConnection(this.getClass(), DataSourceManager.DS_NAME_ORACLE, true);<br>
 *<br>       
 *       	// SQL�}�l�[�W�����SQL���擾<br>
 *        	SqlObj sql = SqlManager.getSql(this.getClass(), "LoginCheckSql");<br>
 *<br>        
 *        	// �v���y�A�h�X�e�C�g�����g�擾<br>
 *       	DBPreparedStatement preState = conn.prepareStatement(sql);<br>
 *<br>       
 *       	// �o�C���h�ϐ��ɒl�𖄂ߍ���<br>
 *       	preState.setString(1, "userId");<br>
 *       			�c<br>
 *<br>       
 *       	// ���ʃZ�b�g�̎擾<br>
 *       	DBResultSet result = preState.executeQuery();<br>
 *<br>       
 *       	// ���\�[�X�̃N���[�Y<br>
 *       	result.close();<br>
 *<br>       
 *       	preState.close();<br>
 *<br>       	
 *       } catch (SQLException sqe) {<br>
 *       } finally {<br>
 *       	if (conn != null) {<br>
 *				try {<br>
 *					conn.close();<br>
 *				} catch (Exception e) {<br>
 *					e.printStackTrace();<br>
 *					APErrorLogger apError = APErrorLogger.getLogger();<br>
 *					apError.outputErrorLog(this.getClass().getName(),"execute", "Connection close failed", false, null ,e);<br>
 *				}<br>
 *       	}<br>
 *       }<br>
 * <br>
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
public final class DataSourceManager {

	/** �f�[�^�\�[�X���FORACLE JDBC */
	public static String DS_NAME_DEFAULT = "DS_DEFAULT_JDBC";

	// �X���b�h���[�J���ϐ� �f�[�^�\�[�X��ێ�
	private static ThreadLocal threadLocal = new ThreadLocal();

	// �L�[�F�N���X��
	private static final String CLASS_NAME = "CLASSNAME";

	// �L�[�F�R�l�N�V����
	private static final String DS_CONNECTION = "DS_CONNECTION";
	
	private static final String DRIVER_NAME = "DRIVER_NAME";
	
	private static ConnectPropeties prop;
	
	public static synchronized void setProperties(ConnectPropeties p) {
		Assertion.isNotNull(p);
		prop = p;
	}
	
	public static DBConnection getConnection(Class self, boolean autoCommit) throws SQLException {
		return getConnection(self, DS_NAME_DEFAULT, autoCommit);
	}
	
	/**
	 * 
	 * �T�v: �R�l�N�V�������擾���܂��B
	 * 
	 * @param self
	 * @param detaSourceName
	 * @param autoCommit
	 * @return
	 * @throws SQLException
	 * @throws
	 */
	public static DBConnection getConnection(Class self, String detaSourceName,
			boolean autoCommit) throws SQLException {

		checkBeforeConnection(getDsManageMap(), detaSourceName);

		Connection connection = prop == null
									? getConnection(detaSourceName, autoCommit)
									: getConnection(autoCommit);
				
		if (connection != null) {
			DBConnection con = new DBConnection(connection, self, autoCommit);
			con.setDataSourceName(detaSourceName);

			Map map = new HashMap();
			map.put(DS_CONNECTION, con);
			map.put(CLASS_NAME, self);
			getDsManageMap().put(detaSourceName, map);
			
			return con;
		}

		throw new SQLException("Create Connection Missing");
			
	}

	/**
	 * 
	 * �T�v: �l�X�g���ꂽ�g�����U�N�V�����ׂ̈̃R�l�N�V�������擾����B<br>
	 * �ڍ�: �l�X�g���ꂽ�g�����U�N�V�����ׂ̈̃R�l�N�V�������擾����B<br>
	 * ���l: �Ȃ�<br>
	 * 
	 * @param self
	 * @param detaSourceName
	 * @param parent
	 * @return
	 * @throws SQLException
	 */
	public static DBConnection getConnection(Class self, DBConnection parent) throws SQLException {
		if (parent == null) {
			throw new IllegalArgumentException("�e�R�l�N�V������null�ł��B");
		}

		Connection connection = getConnection(parent.getDataSourceName(), false);

		if (connection != null) {
			DBConnection con = new DBConnection(parent, connection, self, false);

			parent.setChildCon(con);
			return con;
		}

		throw new SQLException("Create Connection Missing");
	}

	/**
	 * 
	 * �T�v: �R�l�N�V�����̃N���[�Y�R����`�F�b�N���܂��B<br>
	 * �ڍ�: �R�l�N�V�����̃N���[�Y�R����`�F�b�N���܂��B �N���[�Y����Ă����true��Ԃ��܂��B
	 * �N���[�Y�R��̏ꍇ�̓N���[�Y����false��Ԃ��܂��B<br>
	 * ���l: �Ȃ�<br>
	 * 
	 * @return �N���[�Y�ς�:true �N���[�Y�R��:false
	 */
	public static boolean checkClosed() {

		boolean result = true;
		Map dsManMap = (Map) threadLocal.get();
		if (dsManMap == null) {
			// �R�l�N�V�������擾�̏ꍇ��true
			return result;
		}

		Iterator iter = dsManMap.keySet().iterator();
		while (iter.hasNext()) {
			try {
				Map dsMap = (Map) iter.next();

				DBConnection conn = (DBConnection) dsMap.get(DS_CONNECTION);
				if (!conn.isClosed()) {
					result = false;
				}
			} catch (SQLException e) {
				// ��O��������false�Ƃ���
				result = false;
			}
		}

		return result;
	}
	
	private static Map getDsManageMap() {

		if (threadLocal.get() == null)	threadLocal.set(new HashMap());
		
		return (Map)threadLocal.get();
	}
	
	private static void checkBeforeConnection(Map<String, Map> dsManageMap, String detaSourceName) throws SQLException {

		if (dsManageMap.containsKey(detaSourceName)) {
			// �N���[�Y�R�ꌟ�m
			DBConnection beforeConn = (DBConnection) dsManageMap.get(detaSourceName).get(DS_CONNECTION);
			if (!beforeConn.isClosed()) {

				Class beforeClass = (Class) dsManageMap.get(detaSourceName).get(CLASS_NAME);

				DebugLogger.getLogger(DataSourceManager.class).error(
						"�R�l�N�V�����擾���ɃN���[�Y�R������m���܂����B �O��擾���N���X : "
								+ beforeClass.getName());
			}
		}
	}

	private static Connection getConnection(boolean autoCommit) throws SQLException {

		try {
			Class.forName(prop.getDriverName());
			
			Connection conn = DriverManager.getConnection(
					prop.getURL(), 
					prop.getUserName(), 
					prop.getPassword());
			
			return conn;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * 
	 * �T�v: �f�[�^�\�[�X����R�l�N�V�������擾���܂��B<br>
	 * �ڍ�: �f�[�^�\�[�X����R�l�N�V�������擾���܂��B<br>
	 * ���l: �Ȃ�<br>
	 * 
	 * @param dataSourceName
	 * @return
	 * @throws SQLException
	 */
	private static Connection getConnection(String dataSourceName, boolean autoCommit) throws SQLException {
		try {
			InitialContext context = new InitialContext();

			DataSource ds = (DataSource) context.lookup("java:comp/env/" + dataSourceName);

			Connection connection = ds.getConnection();

			connection.setAutoCommit(autoCommit);

			return connection;

		} catch (NamingException e) {
			// �G���[����
			throw new RuntimeException("JNDI�̉����Ɏ��s���܂����B", e);
		}

	}
}
