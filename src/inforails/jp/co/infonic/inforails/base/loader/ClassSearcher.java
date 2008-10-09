package jp.co.infonic.inforails.base.loader;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import jp.co.infonic.common.module.log.DebugLogger;

import org.apache.log4j.Logger;

public class ClassSearcher {
	
	protected Logger logger = DebugLogger.getLogger(this.getClass()); 

	/**
	 * 同一のクラスローダー内からすべてのクラスを取得します。
	 * 
	 * @return
	 */
	public String[] getClasses() {
		return getClasses(new String[0], false);
	}
	
	public String[] getClasses(String packageName) {
		return getClasses(new String[]{packageName}, true);
	}

	/**
	 * パッケージ以下のクラスを取得する。
	 * except : trueで指定パッケージのみを結果セットに含める。 falseで指定パッケージを含めない。
	 * @param packages
	 * @param include
	 * @return
	 */
	public String[] getClasses(String[] packages, boolean include) {
		
		try {
			Set<String> classSet = new HashSet<String>();
			for(ClassPath target: getClassPathObjects()) {
				target.listup();
				List<String> list = include? includes(packages, target) : expects(packages, target);
				classSet.addAll(list);
			}
			
			String[] cary = classSet.toArray(new String[0]);
			Arrays.sort(cary);
			return cary;
		} catch (IOException e) {
			new RuntimeException("File Io Missing", e);
		}
		
		
		return new String[0];
	}
	
	protected List<ClassPath> getClassPathObjects() throws IOException {

		String cp = System.getProperty("java.class.path");
		String[] paths = cp.split(File.pathSeparator);

		List <ClassPath> urlList = new LinkedList <ClassPath> ();
		for (String path : paths) {
			urlList.add(new ClassPath(path));
		}
		return urlList;
	}
	
	protected static List<String> includes(String[] packages, ClassPath target) {
		List<String> result = new LinkedList<String>();
		for (String p : packages) {
			result.addAll(target.getClasses(p));
		}
		return result;
	}
	
	protected static List<String> expects(String[] packages, ClassPath target) {
		List<String> result = new LinkedList<String>();
		List pkgList = Arrays.asList(packages);
		for (String pkg : target.getAllPackages()) {
			if (!pkgList.contains(pkg))
				result.addAll(target.getClasses(pkg));
		}
		return result;
	}
}
