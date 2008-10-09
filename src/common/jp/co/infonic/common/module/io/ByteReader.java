package jp.co.infonic.common.module.io;

import java.io.IOException;
import java.io.InputStream;

public class ByteReader {
	
	private InputStream in;
	
	private int buffsize;
	
	public ByteReader(InputStream in) throws IOException {
		this(in, 256);
	}
	public ByteReader(InputStream in, int bufferSize) throws IOException {
		this.in = in;
		setBufferSize(bufferSize);
	}
	
	public void setBufferSize(int bufferSize) {
		this.buffsize = bufferSize;
	}

	public void each(final Iterator iter) throws IOException {
		byte[] buffer = new byte[buffsize]; int size;
		while((size = in.read(buffer)) !=  0) {
			if (iter.yield(buffer, size)) break;
		}
	}
	
	public static interface Iterator {
		boolean yield(byte[] buff, int size);
	}
}
