package jp.co.infonic.inforails.generator.java.state;

import jp.co.infonic.common.util.Assertion;
import jp.co.infonic.inforails.generator.java.JavaStatement;

public class PackageState extends JavaStatement {
	
	private static final String NAME_PATTERN = "(\\p{Alpha}+\\.)*\\p{Alpha}+$";
	
	String name;
	
	public PackageState(String name) {
		Assertion.isMatch(name, NAME_PATTERN, "Unmatched package Name : " + name);
		this.name = name;
	}

	@Override
	public String toState() {
		return "package " + name;
	}

}
