package jp.co.infonic.common.module.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import jp.co.infonic.common.util.Assertion;

public class FileAccessManager {
	private File file;
	
	public FileAccessManager(String filename) throws IOException {
		this(new File(filename));
	}
	
	public FileAccessManager(File file) throws IOException {
		Assertion.isExist(file);
		this.file = file;
	}
	
	public void open(Read read) throws IOException {
		BufferedInputStream bis = null;
		try {
			bis = new BufferedInputStream(new FileInputStream(file));
			read.delegate(bis);
		} finally {
			if (bis != null) bis.close();
		}
	}
	
	public void open(Write write) throws IOException {
		open(write, false);
	}
	public void open(Write write, boolean append) throws IOException {
		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(file, append));
			write.delegate(bos);
		} finally {
			if (bos != null) bos.close();
		}
	}
	
	
	public static interface Read {
		void delegate(InputStream in) throws IOException;
	}
	
	public static interface Write {
		void delegate(OutputStream out) throws IOException;
	}
}
