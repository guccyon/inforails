package jp.co.infonic.common.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class SqlReader {
	
	private List list = new LinkedList();
	
	private BufferedReader br;
	
	private int i = 0;
	
	public SqlReader(InputStream in) throws IOException {
		this(in, "Windows-31J");
	}
	
	public SqlReader(InputStream in, String encode) throws IOException {
		this(new InputStreamReader(in, encode));
	}
	
	public SqlReader(InputStreamReader in) throws IOException {
		br = new BufferedReader(in);
		try {
			readAll();
		} finally {
			br.close();	
		}
	}
	
	/**
	 * 次のSQLが読み込めるか返す
	 * @return
	 */
	public boolean hasNext() {
		return list.size() > i;
	}
	
	/**
	 * 次のSQLを取得する。
	 * @return
	 */
	public String nextSQL() {
		return (String)list.get(i++);
	}
	
	/**
	 * 読み込んだSQLの数を返す
	 * @return
	 */
	public int size() {
		return list.size();
	}
	
	/**
	 * 行コメント：　// or -- or 先頭が #
	 * 複数行コメント：　/* ～　*\/
	 * 行の最後に;（セミコロン）又は先頭に/（スラッシュ）が
	 * あった場合はSQLの終了と判断し、
	 * 次の行からを次のSQLとして読み込みます。
	 * @throws IOException
	 */
	private void readAll() throws IOException {
		StringBuffer sb = new StringBuffer();
		String line;
		while((line = br.readLine()) != null) {
			
			if (line.matches("^\\s*#.*")) continue;
			
			if (line.indexOf("//") != -1) line = line.replaceFirst("//.*", "");
			
			if (line.indexOf("--") != -1) line = line.replaceFirst("--.*", "");
			
			if (line.matches(".*;\\s*$") || line.matches("/.*")) {
				line = line.replaceFirst(";\\s*$", "").replaceFirst("/.*", "");
				sb.append(line);
				list.add(fomat(sb.toString()));
				sb = new StringBuffer();
			} else {
				sb.append(line).append(" ");	
			}
		}
		
		if (sb.length() > 0) {
			list.add(fomat(sb.toString()));
		}
	}
	
	/**
	 * 余分なスペース、コメントアウトを除去する。
	 */
	private String fomat(String sql) {
		sql = sql.replaceAll("\t", " ");
		sql = sql.replaceAll("/\\*.*?(\\*/|$)", "");
		while(sql.indexOf("  ") != -1) {
			sql = sql.replaceFirst("  ", " ");
		}
		sql = sql.replaceAll(" ,|, ", ",");
		return sql.trim();
	}
}
