package jp.co.infonic.inforails.framework.presentation.control;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import jp.co.infonic.inforails.framework.presentation.fileupload.MultipartRequestParser;

public class Parameter {
	
	Map<String, Object> params = new HashMap<String, Object>();
	
	void set(String key, Object value) {
		params.put(key, value);
		if (key.indexOf(".") != -1) {
			keyToTreeMap(params, key, value);
		}
	}
	
	/**
	 * パラメータのキーをピリオドで分割し、ツリー構造として構築する。
	 * @param pMap
	 * @param key
	 * @param value
	 */
	private void keyToTreeMap(Map<String, Object> pMap, String key, Object value) {
		int index = key.indexOf(".");
		if (index == -1) {
			pMap.put(key, value);
		} else {
			String parent = key.substring(0, index);
			if (!pMap.containsKey(parent))
				pMap.put(parent, new HashMap());
			keyToTreeMap((Map)pMap.get(parent), key.substring(index + 1), value);
		}
	}
	
	Parameter setAll(Map<String, Object> map) {
		for(String key: map.keySet())
			this.set(key, map.get(key));
		return this;
	}
	
	public Object get(String key) {
		return params.get(key);
	}
	
	public String getString(String key) {
		return (String) params.get(key);
	}
	
	public String[] getStrings(String key) {
		return (String[]) params.get(key);
	}
	
	public File getFile(String key) {
		return MultipartRequestParser.getTmpFile(params, key);
	}
	
	public Object getObject(Class klazz) {
		return null;
	}
	
	Map getAll() {
		return params;
	}
}
