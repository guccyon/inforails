package jp.co.infonic.common.util;

import java.util.Collection;

public class ConditionSupport {

	public static boolean isBlank(String value) {
		return value == null || value.equals("");
	}
	
	public static boolean isExist(String value) {
		return !isBlank(value);
	}
	
	public static boolean isBlank(Collection<?> obj) {
		return obj == null || obj.size() == 0;
	}
	
	public static boolean isExist(Collection<?> obj) {
		return ! isBlank(obj);
	}
	
	public static boolean isBlank(Object obj) {
		return obj == null;
	}
	
	public static boolean isExist(Object obj) {
		return ! isBlank(obj);
	}
}
