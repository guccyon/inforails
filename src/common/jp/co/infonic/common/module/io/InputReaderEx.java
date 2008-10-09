package jp.co.infonic.common.module.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

public class InputReaderEx<T> extends InputReader {
	
	InputReaderEx(InputStream in) throws IOException {
		super(in);
	}
	
	public List<T> map(final ReturnIterator<T> iter) throws IOException {
		final List<T> result = new LinkedList<T>();
		each(new InputReader.Iterator(){
			public boolean yield(String line, int i) {
				return result.add(iter.yield(line, i));
			}
		});
		return result;
	}
	
	public interface ReturnIterator<T> {
		T yield(String line, int index);
	}
}
