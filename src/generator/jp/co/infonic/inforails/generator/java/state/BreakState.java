package jp.co.infonic.inforails.generator.java.state;

import jp.co.infonic.inforails.generator.java.JavaStatement;

public class BreakState extends JavaStatement {

	@Override
	public String toState() {
		return "break";
	}

}
