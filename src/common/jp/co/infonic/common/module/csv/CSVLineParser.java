package jp.co.infonic.common.module.csv;

import java.util.LinkedList;

/**
 * CSV１行分のレコードを解析するクラス
 * @author higuchi
 *
 */
public class CSVLineParser {

	public static final char DEFAULT_DELIMITER = ',';
	
	private StringBuffer buffer;
	
	int quotNum = 0;
	
	/**
	 * １レコード分のデータに文字列を追加する
	 * @param line
	 */
	CSVLineParser appendString(String line) {
		if (buffer == null) buffer = new StringBuffer();
		
		countQuot(line);
		buffer.append(line);
		return this;
	}
	
	/**
	 * １レコード分のデータが正しく解析できるデータか返す
	 * @return
	 */
	boolean isCorrectRecord() {
		return isEven(quotNum);
	}
	
	/**
	 * １レコード分の文字列をカンマで分割し配列にして返す。
	 * @return
	 */
	String[] getSeparatedValues() {
		return getSeparatedValues(DEFAULT_DELIMITER);
	}
	
	/**
	 * １レコード分の文字列を列ごとにデリミタを指定で分割し配列にして返す。
	 * @param delimiter デリミタ
	 * @return
	 */
	String[] getSeparatedValues(char delimiter) {
		String[] cols = splitTokens(String.valueOf(delimiter));
		for (int i = 0; i < cols.length; i++)
			cols[i] = trim(cols[i]).replaceFirst("^\"", "").replaceFirst("\"$", "");
		return cols;
	}
	
	/**
	 * 読み込んだままの１行分の文字列を返す
	 * @return
	 */
	String getDefaultString() {
		return buffer.toString();
	}
	
	boolean isNull() {
		return buffer == null;
	}
	
	// バッファ内の文字列を列ごとに分割する
	private String[] splitTokens(String delimiter) {
		LinkedList tokens = new LinkedList();
		
		String[] splits = buffer.toString().split(delimiter, -1);
		for (int i = 0; i < splits.length; i++) {
			if (tokens.isEmpty() || isCorrectElement((String)tokens.getLast())) {
				tokens.add(splits[i]);
			} else {
				tokens.add( tokens.removeLast() + delimiter + splits[i] );
			}
		}
		
		return (String[])tokens.toArray(new String[0]);
	}
	
	// 列要素のデータが正しいかチェックする
	private boolean isCorrectElement(String str) {
		return isEven(countChar(str, '"', 0));
	}
	
	// 文字列中のダブルクォーテーションの数を数える
	private int countQuot(String str) {
		return quotNum += countChar(str, '"', 0);
	}
	
	// 文字列中のキャラ（文字）の数を数える
	private int countChar(String str, char c, int start) {
		int i = 0, index = str.indexOf(c, start);
		return index == -1 ? i : countChar(str, c, index + 1) + 1;
	}
	
	// 文字列をトリミングする
	private String trim(String str){
		return str.replaceFirst("^\\s*", "").replaceFirst("\\s*$", "");
	}
	
	private boolean isEven(int value) {
		return value % 2 == EVEN;
	}
	private boolean isOdd(int value) {
		return value % 2 == ODD;
	}
	
	private static final int ODD = 1;
	private static final int EVEN = 0;
}
