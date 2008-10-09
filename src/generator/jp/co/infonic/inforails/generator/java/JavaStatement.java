package jp.co.infonic.inforails.generator.java;

import jp.co.infonic.inforails.generator.core.BlankStatement;
import jp.co.infonic.inforails.generator.core.Statement;

public abstract class JavaStatement implements Statement {
	protected static BlankStatement blank = new BlankStatement();

	public String toSrc() {
		return toState() + ";";
	}
	
	public abstract String toState();
}
