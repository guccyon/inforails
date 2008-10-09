package jp.co.infonic.common.module.csv;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class CSVWriter {
	
	public static final String DEFAULT_CHARSET = CSVReader.DEFAULT_ENCODE;
	
	public static final char DEFAULT_DELIMITER = CSVLineParser.DEFAULT_DELIMITER;
	
	private char delimiter;
	
	private File file;
	
	private String encode;

	public CSVWriter(String filename) {
		this(filename, DEFAULT_DELIMITER);
	}

	protected CSVWriter(String filename, char delimiter) {
		this(filename, delimiter, DEFAULT_CHARSET);
	}
	
	protected CSVWriter(String filename, char delimiter, String encode) {
		this.delimiter = delimiter;
		this.file = new File(filename);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
			//throw new IllegalArgumentException("ディレクトリが存在しません");
		}
		this.encode = encode;
	}
	
	public void writeWithCommentOut(String[] data) throws IOException {
		String[] title = (String[]) data.clone();
		title[0] = "#" + title[0];    // 先頭行はコメントアウト
		write(title);
	}

	public void write(Object[] data) throws IOException {
		write(data, true);
	}

	public void write(Object[] data, boolean append) throws IOException {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			if (i != 0) {
				sb.append(delimiter);
			}
			sb.append(convert(data[i]));
		}

		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, append), encode));
			PrintWriter pw = new PrintWriter(bw);
			pw.println(sb.toString());
			pw.flush();
			pw.close();
		} finally {
			if (bw != null) {
				bw.close();
			}
		}
	}
	
	private String convert(Object value) {
		if (value instanceof String) {
			return "\"" + value.toString().replaceAll("\"", "\"\"") + "\"";
		} else {
			return value != null ? value.toString() : "";
		}
	}
	
	public File getFile() {
		return file;
	}
}
