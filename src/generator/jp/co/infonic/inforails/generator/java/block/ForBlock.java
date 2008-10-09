package jp.co.infonic.inforails.generator.java.block;

import jp.co.infonic.common.util.ArrayUtil;
import jp.co.infonic.common.util.ConditionSupport;
import jp.co.infonic.inforails.generator.core.GenerateUtil;
import jp.co.infonic.inforails.generator.core.Statement;
import jp.co.infonic.inforails.generator.java.JavaBlock;
import jp.co.infonic.inforails.generator.java.state.ConditionState;

public class ForBlock extends JavaBlock {
	
	private Statement[] init;
	
	private ConditionState condition;
	
	private Statement[] loop;
	
	public ForBlock addInit(Statement state) {
		ArrayUtil.add(init, state);
		return this;
	}
	
	public ForBlock addLoop(Statement state) {
		ArrayUtil.add(loop, state);
		return this;
	}
	
	public ForBlock setConditons(ConditionState condition) {
		this.condition = condition;
		return this;
	}

	@Override
	protected String firstLine() {
		StringBuffer sb = new StringBuffer();
		sb.append("for(");
		if (ConditionSupport.isExist(init)) {
			sb.append(ArrayUtil.join(GenerateUtil.toSrcArray(init), ","));
		}
		
		sb.append("; ");
		
		if (ConditionSupport.isExist(condition)) {
			sb.append(condition.toSrc());
		}
		
		sb.append("; ");
		
		if (ConditionSupport.isExist(loop)){
			sb.append(ArrayUtil.join(GenerateUtil.toSrcArray(loop), ","));
		}
		
		sb.append(")");
		return sb.toString();
	}

	
}
