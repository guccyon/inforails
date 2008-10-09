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
 * 概要：データソース管理<br>
 * 詳細：データソース管理。ＤＢ接続を一元的に管理します。<br>
 *       JDBCドライバによるＤＢ接続時の各クラスをラップし、<br>
 *       返します。取得されたインスタンスへの参照はデータソース管理クラスにより<br>
 *       一元管理され、リソースのクローズ漏れを防ぎます。<br>
 *       <br>
 *       = 利用方法例 =<br>
 *       <br>
 *       DBConnection conn = null;<br>
 *       try {<br>
 *       	// コネクション取得 コンテキストよりプーリングされたコネクションを取得します。<br>
 *       	conn = DataSourceManager.getConnection(this.getClass(), DataSourceManager.DS_NAME_ORACLE, true);<br>
 *<br>       
 *       	// SQLマネージャよりSQLを取得<br>
 *        	SqlObj sql = SqlManager.getSql(this.getClass(), "LoginCheckSql");<br>
 *<br>        
 *        	// プリペアドステイトメント取得<br>
 *       	DBPreparedStatement preState = conn.prepareStatement(sql);<br>
 *<br>       
 *       	// バインド変数に値を埋め込む<br>
 *       	preState.setString(1, "userId");<br>
 *       			…<br>
 *<br>       
 *       	// 結果セットの取得<br>
 *       	DBResultSet result = preState.executeQuery();<br>
 *<br>       
 *       	// リソースのクローズ<br>
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
 *   [変更履歴]
 *   日付        連番   名前      内容
 *   --------------------------------------------------
 *   2006/03/20  VL000  樋口      新規
 * </pre>
 * 
 * @author higuchit
 */
public final class DataSourceManager {

	/** データソース名：ORACLE JDBC */
	public static String DS_NAME_DEFAULT = "DS_DEFAULT_JDBC";

	// スレッドローカル変数 データソースを保持
	private static ThreadLocal threadLocal = new ThreadLocal();

	// キー：クラス名
	private static final String CLASS_NAME = "CLASSNAME";

	// キー：コネクション
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
	 * 概要: コネクションを取得します。
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
	 * 概要: ネストされたトランザクションの為のコネクションを取得する。<br>
	 * 詳細: ネストされたトランザクションの為のコネクションを取得する。<br>
	 * 備考: なし<br>
	 * 
	 * @param self
	 * @param detaSourceName
	 * @param parent
	 * @return
	 * @throws SQLException
	 */
	public static DBConnection getConnection(Class self, DBConnection parent) throws SQLException {
		if (parent == null) {
			throw new IllegalArgumentException("親コネクションがnullです。");
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
	 * 概要: コネクションのクローズ漏れをチェックします。<br>
	 * 詳細: コネクションのクローズ漏れをチェックします。 クローズされていればtrueを返します。
	 * クローズ漏れの場合はクローズしてfalseを返します。<br>
	 * 備考: なし<br>
	 * 
	 * @return クローズ済み:true クローズ漏れ:false
	 */
	public static boolean checkClosed() {

		boolean result = true;
		Map dsManMap = (Map) threadLocal.get();
		if (dsManMap == null) {
			// コネクション未取得の場合はtrue
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
				// 例外発生時もfalseとする
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
			// クローズ漏れ検知
			DBConnection beforeConn = (DBConnection) dsManageMap.get(detaSourceName).get(DS_CONNECTION);
			if (!beforeConn.isClosed()) {

				Class beforeClass = (Class) dsManageMap.get(detaSourceName).get(CLASS_NAME);

				DebugLogger.getLogger(DataSourceManager.class).error(
						"コネクション取得時にクローズ漏れを検知しました。 前回取得時クラス : "
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
	 * 概要: データソースからコネクションを取得します。<br>
	 * 詳細: データソースからコネクションを取得します。<br>
	 * 備考: なし<br>
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
			// エラー処理
			throw new RuntimeException("JNDIの解決に失敗しました。", e);
		}

	}
}
