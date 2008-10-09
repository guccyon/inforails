package jp.co.infonic.common.util;


public class ClassUtil {

	public static boolean isSuperClass(Class superClass, Class subClass) {
		if (superClass == Object.class) return true;
		Class tmp = subClass;
		
		while((tmp = tmp.getSuperclass()) != Object.class) {
			if (tmp == superClass) return true;
			if (tmp == null) break;
		}
		
		return false;
	}
}
