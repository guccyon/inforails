package jp.co.infonic.common.module.csv;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CSVRecordMap extends HashMap<String, String> {
	
	public CSVRecordMap(){}
	
	public CSVRecordMap(Map<String, String> map) {super(map);}
	
	private List<String> keys = new LinkedList<String>();
	
	private String defaultString = "";
	
	private String[] columnValue;

	public String put(String key, String value) {
		keys.add(key);
		return super.put(key, value);
	}
	
	void setColumnValue(String[] columnValue) {
		this.columnValue = columnValue;
	}
	
	public String[] columnValue() {
		return columnValue;
	}
	
	public String get(int index) {
		if (index > keys.size()) {
			throw new ArrayIndexOutOfBoundsException("列数のサイズを超えています。");
		}
		return (String)super.get(keys.get(index));
	}
	
	public String get(Object obj) {
		return get(obj.toString());
	}
	
	public String get(String key) {
		return super.containsKey(key) ? super.get(key) : "";
	}

	public String[] getValues(String[] keys) {	
		List<String> tmp = new LinkedList<String>();
		for (int i = 0; i < keys.length; i++) {
			String value = this.get(keys[i]);
			tmp.add(value == null ? "" : value);
		}
		return tmp.toArray(new String[0]);
	}
	
	public String[] getColumnNames() {
		return this.keySet().toArray(new String[0]);
	}
	
	public String getDefaultString() {
		return defaultString;
	}
	public void setDefaultString(String line) {
		this.defaultString = line;
	}
	
	public String[] toArray(String[] keys) {
		List<String> list = new LinkedList<String>();
		for (int i = 0; i < keys.length; i++) {
			list.add(this.get(keys[i]));
		}
		
		return list.toArray(new String[0]);
	}
}
