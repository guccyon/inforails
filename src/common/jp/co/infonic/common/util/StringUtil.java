package jp.co.infonic.common.util;


public class StringUtil {
	
	public static String trimL(String value) {
	    return value.replaceAll("^\\s*", "");
	}

	public static String trimR(String value) {
	    return value.replaceAll("\\s*$", "");
	}

	public static String trim(String value) {
	    return trimR(trimL(value));
	}
	
	public static String paddingL(String value, char c, int length) {
	    return paddingL(new StringBuffer(value), c, length);
	}
	
	private static String paddingL(StringBuffer sb, char c, int length) {
	    return sb.length() < length ? paddingL(sb.insert(0, c), c, length) : sb.toString();
	}

	public static String paddingR(String value, char c, int length) {
	    return paddingR(new StringBuffer(value), c, length);
	}
	private static String paddingR(StringBuffer sb, char c, int length) {
	    return sb.length() < length ? paddingR(sb.append(c), c, length) : sb.toString();
	}
	
	public static int countChar(String value, char c) {
	    return countChar(value, c, 0);
	}

	public static int countChar(String value, char c, int start, int end) {
	    return countChar(value.substring(start, end), c);
	}

	public static int countChar(String value, char c, int start) {
	    int i = 0, index = value.indexOf(c, start);
	    return index == -1 ? i : countChar(value, c, index + 1) + 1;
	}
	
	public static String camelize(String value) {
	    String[] words = value.split("[_-]");
	    if (words.length == 0)  return "";
	    
	    StringBuffer sb = new StringBuffer(words[0]);
	    for(int i = 1; i < words.length; i++) {
	    	if (words[i].length() == 0) continue;
    		sb.append(Character.toUpperCase(words[i].charAt(0)));
    		sb.append(words[i].substring(1));
	    }
	    return sb.toString();
	}

	/**
	 * キャメル記法のクラス名をJSPファイル名に変換する。
	 * 大文字を全て小文字に変換し、単語毎に_(アンダースコア)で繋ぐ。
	 * @param id
	 * @return
	 */
	public static String decamelize(String value) {
		String[] values = value.split("(?<=[a-z])(?=[A-Z])");
		for(int i = 0; i < values.length; i++){
			if (values[i].length() == 0) continue;
			values[i] = values[i].substring(0,1).toLowerCase() + values[i].substring(1);
		}
		return ArrayUtil.join(values, "_");
	}
	
	public static String interpret(String value) {
		return value == null ? "": value;
	}
}
