package jp.co.infonic.inforails.generator.java.state;

import jp.co.infonic.common.util.Assertion;
import jp.co.infonic.inforails.generator.java.JavaStatement;

public class ImportState extends JavaStatement {
	
	private static final String PATTERN = "(\\p{Alpha}+\\.)+(\\p{Alpha}+|\\*)$";
	
	private String name;
	
	public ImportState(String name) {
		Assertion.isMatch(name, PATTERN, "Illegal import name : " + name);
		this.name = name;
	}
	
	public ImportState(Package pack) {
		Assertion.isNotNull(pack, "package is null");
		this.name = pack.getName() + ".*";
	}
	
	public ImportState(Class klass) {
		Assertion.isNotNull(klass, "class is null");
		this.name = klass.getName();
	}

	@Override
	public String toState() {
		return "import " + name;
	}

}
