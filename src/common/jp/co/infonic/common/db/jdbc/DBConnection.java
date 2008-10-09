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
 * 概要：ＤＢコネクションラッパクラス<br>
 * 詳細：ＤＢコネクションラッパクラス<br>
 * このクラスのメソッドはマルチスレッドを<br>
 * サポートしていません。<br>
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
public class DBConnection {

	// DBへのConnectionインスタンス
	private Connection connection = null;

	// オートコミット
	private boolean autoCommit;

	// トランザクション開始中フラグ
	private boolean transaction = false;

	// コネクション作成クラス
	private Class className;

	private List preStateList;

	// ネストトランザクション用子供ＤＢコネクション
	private List childCon;
	
	// 親のＤＢコネクション
	private DBConnection parent;
	
	private String dataSourceName;
	
	// デバッグロガー
	private Logger logger = DebugLogger.getLogger(this.getClass());

	/**
	 * コンストラクタ
	 * 
	 * @param con
	 * @param autoCommit
	 */
	DBConnection(Connection con, Class cla, boolean autoCommit) {
		Assertion.isNotNull(cla, "引数クラスがnullです。");
		
		this.connection = con;
		this.className = cla;
		this.autoCommit = autoCommit;
		preStateList = new ArrayList();
	}

	/**
	 * ネストトランザクション用コンストラクタ
	 * 
	 * @param con
	 * @param autoCommit
	 */
	DBConnection(DBConnection parent, Connection con, Class cla, boolean autoCommit) {
		Assertion.isNotNull(cla, "引数クラスがnullです。");

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
	 * 概要: ネストされたトランザクションの為のコネクションを子にセット<br>
	 * 詳細: ネストされたトランザクションの為のコネクションを子にセット<br>
	 * 備考: なし<br>
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
	 * 概要: ネストされたトランザクションの為のコネクションを削除する。<br>
	 * 詳細: ネストされたトランザクションの為のコネクションを削除する。<br>
	 * 備考: なし<br>
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
	 * 概要: トランザクションが開始しているかどうかチェックする。<br>
	 * 詳細: トランザクションが開始しているかどうかチェックする。<br>
	 * 備考: なし<br>
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
	 * 概要: プリペアドステイトメントリストから対象のステイトメントを削除する。<br>
	 * 詳細: プリペアドステイトメントリストから対象のステイトメントを削除する。<br>
	 * 備考: なし<br>
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
	 * 概要: トランザクション開始時に呼び出す。<br>
	 * 詳細: トランザクション開始時に呼び出す。<br>
	 * 備考: なし<br>
	 * 
	 */
	public void transactionStart() {

		checkConnection();

		if (!autoCommit) {

			// トランザクションが既に開始していれば
			if (!transaction) {
				// トランザクションの開始フラグON
				transaction = true;
			} else {
				String msg = "ネストトランザクションを検知しました。,呼出元クラス名="
						+ className.getName();
				
				logger.error(msg);
			}

		}
	}

	/**
	 * 
	 * 概要: プリペアドステイトメントを取得します。<br>
	 * 詳細: プリペアドステイトメントを取得します。<br>
	 * 備考: なし<br>
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
	 * 概要: プリペアドステイトメントを取得します。<br>
	 * 詳細: プリペアドステイトメントを取得します。<br>
	 *       SQL文中の置換対象文字列を引数：置換文字列と置き換えて<br>
	 *       作成します。
	 *       置換文字列の置き換え方法
	 *       SQL文中の {1},{2},…{n} 文字列をそれぞれ
	 *       {1} -> 引数[0], {2} -> 引数[1]
	 *       と置き換えます。
	 * <br>
	 * 備考: なし<br>
	 * 
	 * @param sqlObj SQLオブジェクト
	 * @param replaceStr 置換文字列
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
	 * 概要: トランザクションをロールバックします。<br>
	 * 詳細: トランザクションをロールバックします。<br>
	 * 備考: なし<br>
	 * 
	 * @throws SQLException
	 */
	public void rollback() throws SQLException {

		checkConnection();

		if (autoCommit) {
			throw new IllegalStateException("自動コミットモードです。");

		} else {
			// ロールバック
			this.connection.rollback();
			// トランザクションの開始フラグOFF
			this.transaction = false;
		}
	}

	/**
	 * 
	 * 概要: トランザクションをコミットします。<br>
	 * 詳細: トランザクションをコミットします。<br>
	 * 備考: なし<br>
	 * 
	 * @throws SQLException
	 */
	public void commit() throws SQLException {

		checkConnection();

		// コミット
		this.connection.commit();
		// トランザクションの開始フラグOFF
		this.transaction = false;
	}

	/**
	 * 
	 * 概要: コネクションをクローズします。<br>
	 * 詳細: コネクションをクローズします。<br>
	 * このコネクションから作成されたステイトメントを<br>
	 * 全てクローズします。<br>
	 * 備考: なし<br>
	 * 
	 * @throws SQLException
	 */
	public void close() throws SQLException {

		// 例外発生時も処理を続けるため、最初に発生した例外を保持する
		SQLException se = null;

		// nullの場合はクローズ済
		if (this.connection == null) {
			return;
		}
		
		// ネストして取得されたコネクションを全て閉じる
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

		// リスト中のプリペアドステイトメントを全クローズ
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

		// コミット、ロールバック漏れの検出
		if (transaction) {
			String msg = "コミットまたはロールバック漏れを検知しました。処理をロールバックします。,呼出元クラス名=" + className.getName();

			logger.error(msg);
			
			try {
				// ロールバック
				connection.rollback();
				transaction = false;
			} catch (SQLException e) {
				if (se == null) {
					se = e;
				}
			}
		}

		try {
			// コネクションのクローズ
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

		// 例外が発生していればスローする
		if (se != null) {
			throw se;
		}
	}

	/**
	 * 
	 * 概要: コネクションがクローズされているか判定します。<br>
	 * 詳細: コネクションがクローズされているか判定します。<br>
	 * 備考: なし<br>
	 * 
	 * @return クローズ済み：true クローズ漏れ:false
	 * @throws SQLException
	 */
	boolean isClosed() throws SQLException {

		if (this.connection != null) {

			this.close();

			String msg = "クローズ漏れ発生を検知しました。,呼出元クラス名=" + className.getName() + " スレッド=" + Thread.currentThread().getName();
			
			logger.error(msg);
			
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 概要: コネクションがクローズされているかチェックします。<br>
	 * 詳細: コネクションがクローズされているかチェックします。<br>
	 * 既にクローズされていた場合はIllegalStateExceptionを送出します。<br>
	 * 備考: なし<br>
	 * 
	 */
	private void checkConnection() {
		if (connection == null) {
			throw new IllegalStateException("コネクションがクローズされています。");
		}
	}

}
