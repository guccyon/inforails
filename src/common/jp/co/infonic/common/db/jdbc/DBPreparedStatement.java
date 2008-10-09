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
 * 概要：プリペアドステイトメントラップクラス<br>
 * 詳細：プリペアドステイトメントラップクラス<br>
 *       このクラスはDBConnectionクラスによってインスタンス化され<br>
 *       DBへのクエリの発行用の機能を提供します。<br>
 *<pre>
 * [変更履歴]
 * 日付        連番   名前      内容
 * --------------------------------------------------
 * 2006/03/22  VL000        新規
 *</pre>
 * @author higuchit
 */
public class DBPreparedStatement {

	// プリペアドステイトメント
	private PreparedStatement preState;

	// このインスタンスを生成したコネクション
	private DBConnection conn;

	// このインスタンスを生成した呼び出しクラス
	private Class className;

	// SQL文
	private String sql;

	// Bind変数置換文字
	private String[] bind;

	// リザルトセットリスト
	private List resultSetList = new ArrayList();
	
	// SQL発行状況ロガー
	private DBAccessLogger logger;

	/**
	 * コンストラクタ
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
	 * 概要: このプリペアドステイトメントをクローズします。<br>
	 * 詳細: このプリペアドステイトメントをクローズします。<br>
	 * 備考: なし<br>
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
	 * 概要: 生成したリザルトセットも全てクローズする。<br>
	 * 詳細: 生成したリザルトセットも全てクローズする。<br>
	 * 備考: なし<br>
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
	 * 概要: リザルトセットリストから対象のリザルトセットを削除します。<br>
	 * 詳細: リザルトセットリストから対象のリザルトセットを削除します。<br>
	 * 備考: なし<br>
	 * 
	 * @param rs
	 */
	void removeResultSet(DBResultSet rs) {

		if (resultSetList.contains(rs)) {
			resultSetList.remove(rs);
		}
	}
	
	/**
	 * 概要: SQLを実行します。<br>
	 * 詳細: 処理結果を返します。<br>
	 * 備考: なし<br>
	 * @return
	 * @throws SQLException
	 * @since 2006/04/15
	 */
	public void execute() throws SQLException {

		long start = System.currentTimeMillis();
		preState.execute();

		long end = System.currentTimeMillis();
		// SQLの発行状況ログを出力する。
		logger.info("##execute## " + getReplaceSql(), start, end);
	}

	/**
	 * 概要: INSERT,UPDATE,DELETE 文を実行します。処理結果件数を返します。<br>
	 * 詳細: INSERT,UPDATE,DELETE 文を実行します。処理結果件数を返します。<br>
	 * 備考: なし<br>
	 * 
	 * @return 処理結果件数
	 * @throws SQLException
	 */
	public int executeUpdate() throws SQLException {

		checkStatement();

		if (!conn.isReadyExecute()) {
			throw new IllegalStateException("トランザクションが開始されていません 呼び出し元："
					+ className.getName());
		}

		long start = System.currentTimeMillis();
		// SQL実行
		int result = preState.executeUpdate();
		
		long end = System.currentTimeMillis();
		// SQLの発行状況ログを出力する。
		logger.info("##executeUpdate## " + getReplaceSql(), start, end);

		return result;
	}

	/**
	 * 
	 * 概要: SQLを実行し結果セットを返します。<br>
	 * 詳細: SQLを実行し結果セットを返します。<br>
	 * 備考: なし<br>
	 * 
	 * @return
	 * @throws SQLException
	 */
	public DBResultSet executeQuery() throws SQLException {

		checkStatement();
		
		long start = System.currentTimeMillis();
		// SQL実行
		ResultSet rs = preState.executeQuery();
		
		long end = System.currentTimeMillis();
		// SQLの発行状況ログを出力する。
		logger.info("##executeQuery## " + getReplaceSql(), start, end);

		DBResultSet resultSet = new DBResultSet(rs, this);

		resultSetList.add(resultSet);

		return resultSet;
	}
	
	/**
	 * 
	 * 概要: SQLを実行しOracleのWAVE DASH問題に対応した結果セットを返します。<br>
	 * 詳細: SQLを実行しOracleのWAVE DASH問題に対応した結果セットを返します。<br>
	 * 備考: なし<br>
	 * 
	 * @return
	 * @throws SQLException
	 */
	public DBResultSet executeQueryForWaveDashEncode() throws SQLException {

		checkStatement();
		
		long start = System.currentTimeMillis();
		// SQL実行
		ResultSet rs = preState.executeQuery();
		
		long end = System.currentTimeMillis();
		// SQLの発行状況ログを出力する。
		logger.info("##executeQuery## " + getReplaceSql(), start, end);

		DBResultSet resultSet = new DBResultSetForWaveDashEncode(rs, this);

		resultSetList.add(resultSet);

		return resultSet;
	}

	/**
	 * 概要: ＳＱＬを発行しマップとして、結果セットを返します。<br>
	 * 詳細: ＳＱＬを発行しマップとして、結果セットを返します。<br>
	 * 備考: なし<br>
	 *       マップの構造
	 *       Map(カラム名1, List) , (カラム名2, List), (カラム名3, List)
	 *       Listの中に１件目から～最終レコードまでの値が格納されています。
	 *
	 * @param params バインド変数
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
	 * 概要: ＳＱＬを発行しリストとして、結果セットを返します。<br>
	 * 詳細: ＳＱＬを発行しリストとして、結果セットを返します。<br>
	 * 備考: なし<br>
	 *       リストの構造
	 *       項番
	 *       1    Map(カラム名, １件目の値)
	 *       2    Map(カラム名, ２件目の値)
	 *       3    Map(カラム名, ３件目の値)
	 *       4    Map(カラム名, ４件目の値)
	 *       
	 *       上記のようにMapの配列がリストの中に格納されています。
	 *
	 * @param params バインド変数
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
	 * 概要: 指定されたパラメータをステイトメントにセットします。<br>
	 * 詳細: 指定されたパラメータをステイトメントにセットします。<br>
	 * 備考: なし<br>
	 * 
	 * @param parameterIndex
	 * @param s
	 * @throws SQLException
	 */
	public void setString(int parameterIndex, String s) throws SQLException {
		
		if (s == null) {
			throw new IllegalArgumentException("パラメータがnullです。");
		}

		/*
		 * セットしようとしているデータが、JDBCドライバVARCHAR型の文字数制限が超えて
		 * いる場合、setStringではエラーとなる為、setCharacterStreamを利用する
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
	 * 概要: プリペアドステイトメントのバインド変数と値を変換します。<br>
	 * 詳細: プリペアドステイトメントのバインド変数と値を変換します。<br>
	 * 備考: なし<br>
	 * 
	 * @param params
	 * @throws SQLException
	 */
	public void setBindDataAll(Object[] params) throws SQLException {

		// 引数がnullの場合何も行わない。
		if (params == null) {
			return;
		}

		for (int i = 0; i < params.length; i++) {
			Object x = params[i];
			int paramIndex = i + 1;
			if (x == null) {
				throw new IllegalArgumentException("パラメータがnullです。index :" + i);
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
						"引数の型がサポートされていない型です。 index :" + i + " 型 :"
								+ x.getClass().getName());
			}

			bind[i] = x.toString();
		}
	}

	/**
	 * 
	 * 概要: プリペアドステイトメントのバインド変数と値を変換します。<br>
	 * 詳細: プリペアドステイトメントのバインド変数と値を変換します。<br>
	 * 備考: なし<br>
	 * 
	 * @param paramList
	 * @throws SQLException
	 */
	public void setBindDataAll(List paramList) throws SQLException {
		setBindDataAll(paramList.toArray(new Object[0]));
	}

	/**
	 * 概要: プリペアドステイトメントがクローズされているかチェックします。<br>
	 * 詳細: プリペアドステイトメントがクローズされているかチェックします。<br>
	 * 既にクローズされていた場合はIllegalStateExceptionを送出します。<br>
	 * 備考: なし<br>
	 * 
	 */
	private void checkStatement() {
		if (preState == null) {
			throw new IllegalStateException("プリペアドステイトメントがクローズされています。");
		}
	}
	
	/**
	 * 
	 * 概要: SQL文中のバインド変数をセットされた文字に置き換えます。<br>
	 * 詳細: SQL文中のバインド変数をセットされた文字に置き換えます。<br>
	 * 備考: なし<br>
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
	 * 概要: SQL文中のバインド変数”？”の個数をカウントします。<br>
	 * 詳細: SQL文中のバインド変数”？”の個数をカウントします。<br>
	 * 備考: なし<br>
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
