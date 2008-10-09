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
 * �T�v�F�e���v���[�g���疄�ߍ��ݕ�����u��������ׂ̃N���X<br>
 * �ڍׁF�e���v���[�g���疄�ߍ��ݕ�����u��������ׂ̃N���X<br>
 *<pre>
 * [�ύX����]
 * ���t        �A��   ���O      ���e
 * --------------------------------------------------
 * 2006/10/05  VL000  ���      �V�K
 *</pre>
 * @author ���
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
				throw new RuntimeException("���\�[�X���ǂݍ��߂܂���:" + resourceName, e1);
			}
		}
	}
	
	/**
	 * 
	 * �T�v: �����Ɏw�肳�ꂽ�u������������u���������e���v���[�g�������Ԃ��܂��B<br>
	 * �ڍ�: �����Ɏw�肳�ꂽ�u������������u���������e���v���[�g�������Ԃ��܂��B<br>
	 * ���l: �Ȃ�<br>
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
			throw new RuntimeException("�t�@�C���̓ǂݍ��݂Ɏ��s���܂����B" + source.getName(), e);
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
