package jp.co.infonic.common.module.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 汎用CSVファイル読込ライブラリ
 * 
 * 【使い方】
 * CSVReader csv = new CSVReader(読み込み対象ファイル).preLoadTitleRow(); //--※1
 * 
 * while(csv.next()) {
 *   String col1 = csv.get("column1");
 *   String col2 = csv.get("column2");
 * }
 * 
 * JDBCのResultSetのような感覚で使用します。
 * 
 * ※1
 * CSVファイルの先頭行が列名になっている場合のみ
 * preLoadTitleRowを実行します。
 * なっていない場合は new後、
 * setColumnName(String[] columns)で列名をセットする必要があります。
 * 
 * 
 * @author higuchi
 *
 */
public class CSVReader {
	
	public static final String DEFAULT_ENCODE = "Windows-31J";
	
	private File file;
	
	private String encode;
	
	private String[] colmunName;
	
	private Iterator iter;
	
	private CSVRecordMap currentRow;
	
	private String defaultString;
	
	private int currentRowNum = 0;
	
	private boolean EOF = false;

	// 読み込み済みのCSVデータ上の行数
	private int readBuffer = 1000;

	private int offset = 0;
	
	private int readIndex;
	
	public CSVReader(String fileName) {
		this(new File(fileName), DEFAULT_ENCODE);
	}
	
	public CSVReader(File file) {
		this(file, DEFAULT_ENCODE);
	}
	
	public CSVReader(File file, String encode) {
		this.file = file;
		this.encode = encode;
		iter = new LinkedList().iterator();
	}
	
	/**
	 * ファイルの先頭行よりタイトルを読み込みます
	 * @return
	 * @throws IOException
	 */
	public CSVReader preLoadTitleRow() throws IOException {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file),encode));
			CSVLineParser parser = readLine(br, new CSVLineParser());
			offset = readIndex;
			colmunName = parser.getSeparatedValues();
		} finally {
			if (br != null)
				br.close();
		}
		return this;
	}
	
	public void setColumnName(String[] title) {
		this.colmunName = title;
	}
	
	/**
	 * カラム名リストを取得します
	 * @return
	 */
	public String[] columnName() {
		return colmunName;
	}
	
	/**
	 * ファイルを先読みしておく行数を指定する。
	 * ファイルの読み込みは毎回open,closeを行う為、
	 * ある程度バッファしておく方が早い。
	 * デフォルトは1000レコード。
	 * @param num
	 */
	public void setBufferSize(int num) {
		this.readBuffer = num;
	}
	
	/**
	 * 現在のレコードをマップデータとして返す。
	 * @return
	 */
	public CSVRecordMap currentRow() {
		return currentRow;
	}
	
	/**
	 * 現在のレコードをファイルから読み込んだ文字列のまま返す。
	 * @return
	 */
	public String defaultString() {
		return defaultString;
	}
	
	/**
	 * 現在の行番号を取得します。
	 * @return
	 */
	public int currentRowNumber() {
		return currentRowNum;
	}
	
	/**
	 * 次のレコードの有無を返す。
	 * @return
	 */
	public boolean next() {
		if (!iter.hasNext() && !EOF) {
			try {
				iter = readBuffer().iterator();
			} catch (IOException ioe) {
				ioe.printStackTrace();
				return false;
			}
		}

		if (iter.hasNext()) {
			CSVLineParser parser = (CSVLineParser) iter.next();
			defaultString = parser.getDefaultString();
			currentRow = new CSVRecordMap();
			String[] cols = parser.getSeparatedValues();
			for (int i = 0; i < cols.length; i++)
				currentRow.put(colmunName[i], cols[i]);

			currentRowNum++;
			return true;
		} else {
			defaultString = null;
			currentRow = null;
			return false;
		}
	}
	
	/**
	 * カラム名に対応する現在レコードの値を返す。
	 * @param columnName
	 * @return
	 */
	public String get(String columnName) {
		if (currentRow != null) {
			return (String) currentRow.get(columnName);
		}
		return null;
	}

	private List readBuffer() throws IOException {
		List result = new LinkedList();
		BufferedReader br = null;
		readIndex = 0;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file), encode));
			seekOffset(br);

			while(result.size() < readBuffer) {
				CSVLineParser record = readLine(br, new CSVLineParser());
				if (EOF = record.isNull())
					break;
				else
					result.add(record);
			}
			offset = readIndex;
		} finally {
			if (br != null) br.close();
		}
		

		return result;
	}
	
	private CSVLineParser readLine(BufferedReader br, CSVLineParser parser) throws IOException {
		String line = br.readLine();
		readIndex++;
		if (line != null && !parser.appendString(line).isCorrectRecord()){
			return readLine(br, parser.appendString(System.getProperty("line.separator")));
		}
		
		return parser;
	}
	
	private void seekOffset(BufferedReader br) throws IOException {
		while (br.ready() && readIndex < offset) {
			br.readLine();
			readIndex++;
		}
	}
}
