package jp.co.infonic.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class FileUtil {

	public static File[] listFilesRecursion(File directory) {
		return listFilesRecursion(directory, null);
	}
	
	public static File[] listFilesRecursion(File directory, FileFilter filter) {
		return (File[])listChildsRecursion(directory, filter).toArray(new File[0]);
	}
	
	private static List <File> listChildsRecursion(File directory, FileFilter filter) {
		List <File> result = new LinkedList <File> ();
		File[] childs = directory.listFiles();
		for (File child: childs) {
			if (filter == null || filter.accept(child)) result.add(child);
			if (child.isDirectory()) result.addAll(listChildsRecursion(child, filter));
		}
		return result;
	}

	// ファイル名として利用できない記号、特殊文字
	public static final String CANNOT_USE_CHAR = "/\\:;,*?\"<>|";
	
	public static boolean isCorrectFileName(String fileName) {
		if (fileName.equals(" ")) return false;
		
        if (fileName.getBytes().length > 126) return false;
        
    	// 禁止文字チェック
        for (int i = 0; i < CANNOT_USE_CHAR.length(); i++) {
            if (fileName.indexOf(CANNOT_USE_CHAR.charAt(i)) != -1) {
            	return false;
            }
        }
        
    	return true;
	}
	
	public static File mkdir(String dirName) {
		File directory = new File(dirName);
		if (!directory.exists())
			if (directory.mkdirs())
				throw new RuntimeException(" New directory create Missing : " + directory.getAbsolutePath());
		return directory;
	}
	
	public static void copy(File source, File dest) throws IOException {
		
		BufferedOutputStream bos = null;
		BufferedInputStream bis = null;
		try {
			bis = new BufferedInputStream(new FileInputStream(source));
			bos = new BufferedOutputStream(new FileOutputStream(dest));
			
			byte[] buffer = new byte[512];
			int len = 0;
			while ((len = bis.read(buffer)) > -1)
				bos.write(buffer, 0, len);
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (bos != null) {
				bos.close();
			}
		}
	}

	/**
	 * 各OSに基づいた、ファイルセパレーターで、 フルパスの中からファイル名だけを取り出す。
	 * @param tmpFileName
	 *            フルパスで送られてくるファイル名
	 * @return ファイル名からパスの部分を除き、“ファイル名のみ”を返す。
	 */
	public static String getOnlyFileName(String fullPath) {
		return fullPath.replaceFirst(".+(\\|/|:)", "");
	}
}
