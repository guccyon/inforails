package jp.co.infonic.common.module.ex;

import static org.junit.Assert.*;
import static jp.co.infonic.common.module.ex.ExString.*;

import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class ExStringTest {
	
	private ExString s;
	
	private String val = "test string #{a} and #{b}";

	@Before
	public void setUp() throws Exception {
		s = new ExString(val);
	}

	@Test
	public void testTmplStringMap() {
		Map m = new ExMap<String, String>(
					new String[]{"a", "b"}, new String[]{"aaa", "bbb"} );
		assertEquals(s.tmplString(m),"test string aaa and bbb");
	}

	@Test
	public void testTmplStringStringIteratorOfString() {
		String result = s.tmplString(new Iterator<String>(){
			public String yield(String value) {
				if ("a".equals(value)) {
					value = "@a";
				} else if ("c".equals(value)) {
					value = "@c";
				} else {
					value = "nothing";
				}
				return value;
			}}
		);
		assertEquals(result, "test string @a and nothing");
	}

	@Ignore
	@Test
	public void testEachChar() {
		fail("Ç‹Çæé¿ëïÇ≥ÇÍÇƒÇ¢Ç‹ÇπÇÒÅB");
	}

	@Test
	public void testToString() {
		assertEquals(s.toString(), val);
	}
}
