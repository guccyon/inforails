package jp.co.infonic.inforails.base.loader;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import jp.co.infonic.common.util.FileUtil;

public class ClassPath {
	
	public static final String DEFAULT_PACKAGE = "$default$";

	private URL url;

	private int sourceType;
	
	private Map <String, Set<String>> classesMap;

	private static final int TYPE_DIRCTORY = 0;

	private static final int TYPE_ARCHIVE = 1;
	
	private static final FileFilter classFileFilter = new FileFilter() {
		public boolean accept(File pathname) {
			return pathname.getName().toLowerCase().endsWith(".class");
		}
	};

	ClassPath(String classPath) throws MalformedURLException, IOException {
		this.url = new File(new File(classPath).getCanonicalPath()).toURL();
		classesMap = new HashMap <String, Set<String>> ();
	}

	public void listup() throws IOException {
		if (new File(url.getFile()).isDirectory()) {
			listupFromDirectory();
			sourceType = TYPE_DIRCTORY;
		} else if (url.getFile().endsWith(".jar")
				|| url.getFile().endsWith(".zip")) {
			listupFromArchive();
			sourceType = TYPE_ARCHIVE;
		} else {
			String s = "classpath is unavailable: "+ url.getFile() ;
			System.err.println(s);
		}
	}
	
	public List<String> getClasses() {
		return getClasses(null);
	}
	
	public List<String> getClasses(String packageName) {
		List <String> result = new LinkedList<String>();
		if (packageName != null) {
			if (classesMap.containsKey(packageName)) {
				Iterator<String> iter = classesMap.get(packageName).iterator();
				while(iter.hasNext()) {
					result.add(concatClassName(packageName, iter.next()));
				}
			}
		} else {
			Iterator <String> packages = classesMap.keySet().iterator();
			while(packages.hasNext()) {
				result.addAll(getClasses(packages.next()));
			}
		}
		return result;
	}
	
	public Set<String> getAllPackages() {
		return classesMap.keySet();
	}
	
	int getSourceType() {
		return sourceType;
	}
	
	private String concatClassName(String packageName, String className) {
		return DEFAULT_PACKAGE.equals(packageName) 	? 
				className : packageName + "." + className;
	}

	private void listupFromDirectory() {
		File directory = new File(url.getFile());
		File[] files = FileUtil.listFilesRecursion(directory,classFileFilter);
		String[] classNames = extractClassName(directory.getAbsolutePath(), files);
		appendClasses(classNames);

	}

	private void listupFromArchive() throws IOException {
		List <String> list = new LinkedList <String> ();
		InputStream in = url.openStream(); 
		
		ZipInputStream zin = new ZipInputStream(in);
		ZipEntry ze;
		while( (ze= zin.getNextEntry()) != null ) {
			String filename = ze.getName();
			if (classFileFilter.accept(new File(filename)))
				list.add(convertClassName(filename));
		}
		zin.close();
		
		String[] classNames = (String[])list.toArray(new String[0]);
		appendClasses(classNames);
	}
	
	private void appendClasses(String[] classNames) {
		for(String clazz: classNames) {
			String pkg = clazz.indexOf(".") != -1 
				? clazz.replaceFirst("\\.[^.]+$", "") : DEFAULT_PACKAGE;
			String clsName = clazz.replaceFirst("^.+\\.", "");
			Set <String> classSet;
			if ((classSet = classesMap.get(pkg)) == null) {
				classSet = new HashSet <String> ();
				classesMap.put(pkg, classSet);
			}
			classSet.add(clsName);
		}
	}

	private String[] extractClassName(String dir, File[] files) {
		List <String> list = new LinkedList <String> ();
		for (File file : files) {
			String path = file.getAbsolutePath();
			if (path.startsWith(dir)) path = path.substring(dir.length() + 1);
			list.add(convertClassName(path));
		}
		return (String[]) list.toArray(new String[0]);
	}

	private String convertClassName(String fileName) {
		String classname = fileName.replace('/', '.').replace('\\', '.');
		if (classname.startsWith("class "))
			classname = classname.substring(6);
		if (classname.endsWith(".class"))
			classname = classname.substring(0, classname.length() - 6);
		return classname;
		
	}
}
