package jp.co.infonic.common.module.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class InputReader {
	private BufferedReader br = null;
	
	private int index = 0;
	
	public InputReader(InputStream in) throws IOException {
		this(in, null);
	}
	public InputReader(InputStream in, String charset) throws IOException {
		br = new BufferedReader(charset == null ?
			new InputStreamReader(in) : new InputStreamReader(in, charset));
	}

	public void each(final Iterator iter) throws IOException {
		String line;
		while((line = br.readLine()) != null) {
			if (iter.yield(line, index++)) break;
		}
	}
	
	public static interface Iterator {
		boolean yield(String line, int index);
	}
}
