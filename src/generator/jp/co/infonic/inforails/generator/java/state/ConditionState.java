package jp.co.infonic.inforails.generator.java.state;

import jp.co.infonic.inforails.generator.java.JavaStatement;

public class ConditionState extends JavaStatement {
	
	private String conditionstr;

	public ConditionState(boolean bool) {
		conditionstr = String.valueOf(bool);
	}
	
	public String toState() {
		return conditionstr;
	}

	@Override
	public String toSrc() {
		return this.toState();
	}

}
