package jp.co.infonic.inforails.generator.java;

import java.util.HashMap;
import java.util.Map;

public class JavaGenerateProperty {
	
	private static Map<String, String> props = new HashMap<String, String>();
	
	public static final String INDENT = "INDENT";
	public static final String BLOCK_SAME_LINE = "BLOCK_SAME_LINE";
	public static final String BLOCK_OMIT = "BLOCK_OMIT";
	
	static {
		props.put(INDENT, "    ");
		props.put(BLOCK_SAME_LINE, "true");
		props.put(BLOCK_OMIT, "false");
	}

	public static String getIndent() {
		return getProperty(INDENT);
	}
	public static boolean getBlockSameLine() {
		return getBooleanProperty(BLOCK_SAME_LINE, true);
	}
	public static boolean getBlockOmit() {
		return getBooleanProperty(BLOCK_OMIT, false);
	}
	
	private static boolean getBooleanProperty(String key, boolean Default) {
		return props.containsKey(key) ? Boolean.valueOf(props.get(key)) : false;
	}
	
	public static String getProperty(String key) {
		return props.containsKey(key) ? props.get(key).toString() : "";
	}
}
