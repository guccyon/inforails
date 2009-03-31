package jp.co.infonic.common.util;

import java.lang.reflect.Field;
import java.util.Map;

public class MapUtil {

	public static <T> T convertObject(Class<T> klazz, Map<String, Object> map) {
		try {
			T obj = klazz.newInstance();
			injectValue(obj, map);
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	private static void injectValue(Object target, Map<String, Object> map) throws Exception {
		for(Field f: target.getClass().getDeclaredFields()) {
			if (!map.containsKey(f.getName())) continue;

			Object value = map.get(f.getName());
			f.setAccessible(true);
			if (!(value instanceof Map)) {
				f.set(target, castCorrect(f, value));
			} else {
				Object child =f.getType().newInstance(); 
				f.set(target, child);
				injectValue(child, (Map<String, Object>) value);
			}
		}
	}
	
	private static Object castCorrect(Field f, Object value) {
		String fType = f.getType().getName();
		if (fType.equals("int")) {
			value = new Integer(value.toString());
		} else if (fType.equals("boolean")) {
			value = new Boolean(value.toString());
		} else if (fType.equals("double")) {
			value = new Double(value.toString());
		} else if (fType.equals("float")) {
			value = new Float(value.toString());
		}
		return value;
	}
}
