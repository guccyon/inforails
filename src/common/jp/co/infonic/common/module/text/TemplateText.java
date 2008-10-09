package jp.co.infonic.common.module.text;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * 概要：テンプレートから埋め込み文字を置き換える為のクラス<br>
 * 詳細：テンプレートから埋め込み文字を置き換える為のクラス<br>
 *<pre>
 * [変更履歴]
 * 日付        連番   名前      内容
 * --------------------------------------------------
 * 2006/10/05  VL000  樋口      新規
 *</pre>
 * @author 樋口
 */
public class TemplateText {
	
	private static final String REGEXP_VAR = "\\$\\{([^}]+?)\\}";
	
	private static final String DEFAULT_CHARSET = "Windows-31J";
	
	private List replaceVar = new LinkedList();
	
	private String[] sourceAry;
	
	public TemplateText(String source) {
		parse(source);
	}
	
	public TemplateText(File source) {
		this(source, DEFAULT_CHARSET);
	}
	
	public TemplateText(File source, String charset) {
		
		String sourceStr = readFile(source, charset);
		parse(sourceStr);
	}
	
	public TemplateText(String resourceName, Class klass) {
		this(resourceName, klass, DEFAULT_CHARSET);
	}
	
	public TemplateText(String resourceName, Class klass, String charset) {
		try {
			parse(readAsStream(klass.getResourceAsStream(resourceName), charset));
		} catch (IOException e) {
			String pkg = klass.getPackage().getName();
			try {
				parse(readAsStream(klass.getResourceAsStream(pkg + "." + resourceName), charset));
			} catch (IOException e1) {
				throw new RuntimeException("リソースが読み込めません:" + resourceName, e1);
			}
		}
	}
	
	/**
	 * 
	 * 概要: 引数に指定された置き換え文字を置き換えたテンプレート文字列を返します。<br>
	 * 詳細: 引数に指定された置き換え文字を置き換えたテンプレート文字列を返します。<br>
	 * 備考: なし<br>
	 *
	 * @param replace
	 * @return
	 */
	public String getReplaceString(Map replace) {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < sourceAry.length; i++) {
			if (i != 0) {
				String targetName = (String) replaceVar.get(i - 1);
				if (replace.get(targetName) == null) {
					result.append("${").append(targetName).append("}");
				} else {
					String value = (String) replace.get(targetName);
					result.append(value);
				}
			}
			result.append(sourceAry[i]);
		}
		
		return result.toString();
	}
	
	private void parse(String src) {
		Pattern pattern = Pattern.compile(REGEXP_VAR);
		Matcher mat = pattern.matcher(src);
		
		while (mat.find()) {
			replaceVar.add(mat.group(1));
		}
		
		sourceAry = pattern.split(src + "###");
		sourceAry[sourceAry.length-1] = sourceAry[sourceAry.length-1].replaceFirst("###$", "");
	}
	
	public static String getReplaceString(String source, Map replace) {
		Iterator iter = replace.keySet().iterator();
		String result = source;
		while (iter.hasNext()) {
			String varName = (String)iter.next();
			String target = "\\$\\{" + varName + "\\}";
			String value = (String) replace.get(varName);
			
			result = result.replaceAll(target, value);
			
		}
		
		return result;
	}
	
	public static String getReplaceString(File source, Map replace) {
		String result = readFile(source, DEFAULT_CHARSET);
		
		return getReplaceString(result, replace);
	}
	
	private static String readFile(File source , String charset) {
		
		try {
			return readAsStream(new FileInputStream(source), charset);
		} catch (IOException e) {
			throw new RuntimeException("ファイルの読み込みに失敗しました。" + source.getName(), e);
		}
	}
	
	private static String readAsStream(InputStream in, String charset) throws IOException {
		BufferedInputStream bis = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bis = new BufferedInputStream(in);
			byte[] buffer = new byte[256];
			int length = 0;
			while ((length = bis.read(buffer)) != -1) {
				baos.write(buffer, 0, length);
			}
			
			return new String(baos.toByteArray(), charset);
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
}
