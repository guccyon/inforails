package jp.co.infonic.common.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;

/**
 * 
 * 概要：ＳＱＬオブジェクト管理クラス<br>
 * 詳細：ＳＱＬオブジェクト管理クラス<br>
 *       ＳＱＬを発行するクラスと同じパス上に.sqlを<br>
 *       配置することで、拡張子を除いたファイル名をキーに<br>
 *       ＳＱＬファイルを読み込みSqlObjインスタンスを生成し<br>
 *       返します。<br>
 *       ※SQLファイルは文字コードにShift_JISを指定する必要があります。
 *       
 *<pre>
 * [変更履歴]
 * 日付        連番   名前      内容
 * --------------------------------------------------
 * 2006/03/17  VL000  樋口      新規
 *</pre>
 * @author higuchit
 */
public class SqlManager {
	
	// 一度読み込んだSQLはキャッシュする
	private static Map sqlCache = new HashMap();
	
	// 文字コード
	private static String ENCODE = "Shift_JIS";
	
	/** クエリ種別：不明 */
	public static final int TYPE_UNKNOWN = 0;
	
	/** クエリ種別：検索 */
	public static final int TYPE_SELECT = 1;
	
	/** クエリ種別：登録 */
	public static final int TYPE_INSERT = 2;
	
	/** クエリ種別：更新  */
	public static final int TYPE_UPDATE = 3;
	
	/** クエリ種別：削除  */
	public static final int TYPE_DELETE = 4;
	
	/**
	 * 
	 * 概要: SQLファイルの文字コードを指定します。<br>
	 * 詳細: SQLファイルの文字コードを指定します。
	 *       ディフォルトではShift_JISになります。<br>
	 * 備考: なし<br>
	 *
	 * @param newEncode
	 * @throws UnsupportedEncodingException
	 */
    public static void setSqlFileEncode(String newEncode) throws UnsupportedEncodingException {
    	"dummy".getBytes(newEncode);
    	
    	ENCODE = newEncode;
    }
	
	/**
	 * 
	 * 概要: ＩＤに対応するＳＱＬオブジェクトを返します。<br>
	 * 詳細: ＩＤに対応するＳＱＬオブジェクトを返します。<br>
	 *       該当SQLファイルは利用クラスと同じパッケージに<br>
	 *       配置する必要があります。<br>
	 * 備考: なし<br>
	 *
	 * @param selfClass
	 * @param sqlId
	 * @return
	 */
	public static SqlObj getSql(Class selfClass, String sqlId) {
		
		String id = selfClass.getPackage().getName() + "." + sqlId;
		
		id = id.replace('.', '/');
		
		
		if (!sqlCache.containsKey(id)) {
			InputStream in = selfClass.getClassLoader().getResourceAsStream(id + ".sql");
			if (in == null) {
				throw new MissingResourceException("対象のSQLファイルが見つかりません。 -> " + id, "", "");
			}
			
			try {
				SqlReader reader = new SqlReader(in);

				for(int i = 0;reader.hasNext();i++) {
					String sqlstr = reader.nextSQL();
					
					SqlObj sql = new SqlObj(sqlstr, id + "[" + i + "]");

					if (sql != null) {
						sqlCache.put(sql.getId(), sql);
						if (i == 0) sqlCache.put(id, sql);
					}
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return (SqlObj)sqlCache.get(id);
	}
	
	/**
	 * 
	 * 概要: 入力ストリームからＳＱＬオブジェクトを作成します。<br>
	 * 詳細: 入力ストリームからＳＱＬオブジェクトを作成します。<br>
	 *       引数で渡された入力ストリームは内部でクローズされます。<br>
	 *       通常はgetSql(class, sqlId)を利用してください。<br>
	 *       利用した場合、ＳＱＬオブジェクトがキャッシュされるので<br>
	 *       パフォーマンスの向上にもつながります。<br>
	 * 備考: なし<br>
	 * @param in
	 * @return
	 * @throws IOException 
	 * @since 2006/04/15
	 */
	public static SqlObj getSql(InputStream in) throws IOException {
		SqlReader reader = new SqlReader(in, ENCODE);
		
		return reader.hasNext() ? new SqlObj(reader.nextSQL()) : null;
	}
	
	/**
	 * 
	 * 概要: SQL文字列から直接ＳＱＬオブジェクトを作成します。<br>
	 * 詳細: SQL文字列から直接ＳＱＬオブジェクトを作成します。<br>
	 *       通常はgetSql(class, sqlId)を利用してください。<br>
	 *       利用した場合、ＳＱＬオブジェクトがキャッシュされるので<br>
	 *       パフォーマンスの向上にもつながります。<br>
	 * 備考: なし<br>
	 * @param query
	 * @return
	 * @since 2006/04/15
	 */
	public static SqlObj getSql(String query) {
		return new SqlObj(query);
	}
	
	/**
	 * 
	 * 概要: 入力ストリームからSQLを読み込み、SQLObjインスタンスを生成する。<br>
	 * 詳細: 一つのSQLに複数の命令が記述されている場合に当メソッドを利用します。<br>
	 *       複数の命令の区切り文字は、行の先頭に/(半角スラッシュ)<br>
	 *       または一つの命令文の最後に;(セミコロン)を置く事で、<br>
	 *       命令文の区切り文字と解釈し、区切られた数分のSqlObjインスタンスを作成し<br>
	 *       配列にして返します。<br>
	 * 備考: なし<br>
	 *
	 * @param in
	 * @return
	 */
	public static SqlObj[] getSqls(InputStream in) {

		BufferedReader br = null;
		List list = new ArrayList();
		try {
			SqlReader reader = new SqlReader(in);
			while(reader.hasNext()) {
				list.add(new SqlObj(reader.nextSQL()));
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return (SqlObj[])list.toArray(new SqlObj[0]);
	}
}
