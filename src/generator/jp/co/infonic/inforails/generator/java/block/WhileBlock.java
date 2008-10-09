package jp.co.infonic.inforails.generator.java.block;

import jp.co.infonic.inforails.generator.java.JavaBlock;
import jp.co.infonic.inforails.generator.java.state.ConditionState;

public class WhileBlock extends JavaBlock {
	
	protected ConditionState condition;

	public WhileBlock() {
		condition = new ConditionState(true);
	}
	
	public WhileBlock(ConditionState condition) {
		this.condition = condition;
	}
	@Override
	protected String firstLine() {
		return "while(" + condition.toSrc() + ")";
	}

}
