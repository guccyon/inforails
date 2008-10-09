package jp.co.infonic.common.util;

import java.io.File;
import java.io.FileNotFoundException;

public class Assertion {

	public static void isNotNull(Object obj) {
		isNotNull(obj,"");
	}

	public static void isNotNull(Object obj, String message) {
		if (obj == null) {
			throw new NullPointerException(message);
		}
	}

	public static void isExist(String str) {
		isExist(str,"");
	}
	public static void isExist(String str, String message) {
		if (ConditionSupport.isBlank(str)) {
			throw new IllegalArgumentException(message);
		}
	}
	public static void isExist(File file) {
		if (!file.exists()) {
			throw new IllegalArgumentException(new FileNotFoundException(file.getPath()));
		}
	}

	public static void isMatch(String value, String regexp) {
		isMatch(value, regexp);
	}
	public static void isMatch(String value, String regexp, String message) {
		if (!value.matches(regexp)) {
			throw new IllegalArgumentException(message);
		}
	}
	
	public static void isEquals(int a, int b, String message) {
		if (a != b)	throw new IllegalArgumentException(message);
	}
	public static void isEquals(int a, int b) {
		isEquals(a, b, "");
	}
}
