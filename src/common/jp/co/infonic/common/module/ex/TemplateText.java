package jp.co.infonic.common.module.ex;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文字列中の変数を変換する為のクラス
 * デフォルトで${～}で宣言された変数を変換
 */
public class TemplateText {
	
	private static final String TPML_REGEXP = "\\$\\{([^}]+?)\\}";
	
	private Pattern pat;
	
	private String str;

	public TemplateText(String str){
		this(str, TPML_REGEXP);
	}
	
	public TemplateText(String str, String template) {
		this.str = interpret(str);
		pat = Pattern.compile(template);
	}
	
	private String interpret(String str) {
		return str == null ? "" : str;
	}
	
	private void each(Iterator iter) {
		Matcher mat = Pattern.compile(TPML_REGEXP).matcher(str);
		while(mat.find()) {
			iter.yield(mat.group(1));
		}
	}
	
	private static interface Iterator {
		public void yield(String value);
	}
	
	public String pattern(){ return pat.pattern(); };
	
	public String[] findAll() {
		final Set<String> result = new HashSet<String>();
		each(new Iterator(){
			public void yield(String value) {
				result.add(value);
			}
		});
		return result.toArray(new String[0]);
	}
	
	public String replaceAll(final Map <String, String> replace) {
		return replaceValue(str, new ExString.Iterator<String>(){
			public String yield(String value) {
				return interpret(replace.get(value));
			}
		});
	}
	
	public String replaceAll(ExString.Iterator<String> iter) {
		return replaceValue(str, iter);
	}
	
	private String replaceValue(String value, ExString.Iterator<String> iter) {
		Matcher mat = pat.matcher(value);
		if (!mat.find()) return value;
		String tmp = iter.yield(mat.group(1));
		return replaceValue(value.replaceFirst(TPML_REGEXP, tmp), iter);
	}
}
