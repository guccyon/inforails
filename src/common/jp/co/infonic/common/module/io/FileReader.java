package jp.co.infonic.common.module.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import jp.co.infonic.common.module.io.FileAccessManager.Read;

public class FileReader {
	
	private File f;

	public FileReader(String filename) {
		this(new File(filename));
	}

	public FileReader(File filename) {
		f = filename;
	}
	
	public void eachLine(final InputReader.Iterator iter) throws IOException {
		new FileAccessManager(f).open(new Read(){
			public void delegate(InputStream in) throws IOException {
				new InputReader(in).each(iter);
			}
		});
	}
	
	public void eachByte(final int num, final ByteReader.Iterator iter) throws IOException {
		new FileAccessManager(f).open(new Read(){
			public void delegate(InputStream in) throws IOException {
				new ByteReader(in, num).each(iter);
			}
		});
	}
}
