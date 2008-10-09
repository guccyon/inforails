package jp.co.infonic.common.db.ormap;

import java.lang.reflect.Field;
import java.util.Map;

import jp.co.infonic.common.util.Assertion;

public class ModelCreator<T extends Model> {
	
	public static Object create(Class <? extends Model> klass) {
		return new ModelCreator<Model>(klass).create();
	}
	
	public static Object create(Class <? extends Model> klass, Map<String, Object>map) {
		return new ModelCreator<Model>(klass).create(map);
	}
	
	private Class<? extends T> klass;
	
	public ModelCreator(Class<? extends T> klass) {
		Assertion.isNotNull(klass, "Model class is null");
		this.klass = klass;
	}
	
	public T create() {
		try {
			return (T) klass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public T create(Map<String, Object> map) {
		T model = create();
		
		try {
			injectValue(model, map);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return model;
	}
	
	private void injectValue(Object target, Map<String, Object> map) throws Exception {
		for(Field f: target.getClass().getDeclaredFields()) {
			if (!map.containsKey(f.getName())) continue;

			Object value = map.get(f.getName());
			f.setAccessible(true);
			if (!(value instanceof Map)) {
				f.set(target, castCorrect(f, value));
			} else {
				Object child =f.getType().newInstance(); 
				f.set(target, child);
				injectValue(child, (Map) value);
			}
		}
	}
	
	private Object castCorrect(Field f, Object value) {
		String fType = f.getType().getName();
		if (fType.equals("int")) {
			value = new Integer(value.toString());
		} else if (fType.equals("boolean")) {
			value = new Boolean(value.toString());
		} else if (fType.equals("double")) {
			value = new Double(value.toString());
		} else if (fType.equals("float")) {
			value = new Float(value.toString());
		} else if (fType.matches(".*java.io.File")) {
			
		}
		return value;
	}
}
