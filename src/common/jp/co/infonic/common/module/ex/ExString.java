package jp.co.infonic.common.module.ex;

import java.util.Map;

public class ExString  extends ExObject {
	private String self;

	public ExString(String literal) {
		self = literal;
	}
	
	public String tmplString(final Map<String, String> replace) {
		return new TemplateText(self).replaceAll(replace);
	}
	
	public String tmplString(Iterator<String> iter) {
		return new TemplateText(self).replaceAll(iter);
	}
	
	public void eachChar(Iterator<Character> iter) {
		try {
			for(char c: self.toCharArray())	iter.yield(c);
		} catch(BreakException e){}
	}
	
	public String interpret() {
		return self == null ? "" : new String(self);
	}
	
	@Override
	public String toString() {
		return self.toString();
	}
	
	public static interface Iterator<T> {
		public String yield(T value);
	}
}
