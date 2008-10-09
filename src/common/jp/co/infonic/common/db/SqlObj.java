package jp.co.infonic.common.db;



/**
 * 
 * 概要：SQLファイルオブジェクト<br>
 * 詳細：SQLファイルと１対１でマッピングされます。<br>
 *<pre>
 * [変更履歴]
 * 日付        連番   名前      内容
 * --------------------------------------------------
 * 2006/03/17  VL000  樋口      新規
 *</pre>
 * @author higuchit
 */
public class SqlObj {
	
	// ファイルより取得したＳＱＬ文
	private String content;
	
	// 当ファイルのＩＤ
	private String sqlId;
	
	private int queryType;
	
	public int getQueryType() {
		return queryType;
	}
	public void setQueryType(int queryType) {
		this.queryType = queryType;
	}
	/**
	 * コンストラクタ
	 * @param query SQL文字列
	 */
	SqlObj(String query) {
		content = query;
		sqlId = "";
	}
	/**
	 * コンストラクタ
	 * @param query SQL文字列
	 */
	SqlObj(String query, String id) {
		content = query;
		this.sqlId = id;
	}
	
	/**
	 * 
	 * 概要: ＳＱＬ文を返します。<br>
	 * 詳細: ＳＱＬ文を返します。<br>
	 * 備考: なし<br>
	 *
	 * @return
	 */
	public String getSqlString() {
		return content.toString();
	}
	
	/**
	 * 
	 * 概要: ＳＱＬ文中の {n} を置換文字列に置換したＳＱＬ文を返します。
	 * 備考: なし<br>
	 *
	 * @param replace
	 * @return
	 */
	public String getSqlString(String[] replace) {

		return replace(content.toString(), replace);
	}
	
	/**
	 * 
	 * 概要: ＩＤを返します。<br>
	 * 詳細: ＩＤを返します。<br>
	 * 備考: なし<br>
	 *
	 * @return
	 */
	public String getId() {
		return sqlId;
	}
	
	/**
	 * 
	 * 概要: 第２引数の置換文字列をSQL文中の{0},{1},({0} -> [0])の文字列とそれぞれ置換する。<br>
	 * 詳細: 第２引数の置換文字列をSQL文中の{0},{1},({0} -> [0])の文字列とそれぞれ置換する。<br>
	 * 備考: なし<br>
	 *
	 * @param targetStr 対象文字列
	 * @param replaceStr 置換文字列
	 * @return 置換後の文字列
	 */
    private String replace(String targetStr, String[] replaceStr) {
    	
    	if (replaceStr == null || targetStr == null) {
    		throw new IllegalArgumentException("引数がnullです。 arg1 :" + targetStr + " // arg2 :" + replaceStr);
    	}
    	
        for (int i = 0; i < replaceStr.length; i++) {
            String replace = "{" + (i) + "}";
            int index;
            while ((index = targetStr.indexOf(replace)) != -1) {
                String before = targetStr.substring(0, index);
                String left = targetStr.substring(index + replace.length());
                targetStr = before + replaceStr[i] + left;
            }
        }
        return targetStr;
    }
	
	public String toString() {
		return content.toString();
	}
}
