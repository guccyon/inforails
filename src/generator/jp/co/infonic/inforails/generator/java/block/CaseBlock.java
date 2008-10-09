package jp.co.infonic.inforails.generator.java.block;

import java.util.LinkedList;
import java.util.List;

import jp.co.infonic.common.util.Assertion;
import jp.co.infonic.inforails.generator.java.JavaBlock;
import jp.co.infonic.inforails.generator.java.state.BreakState;

public class CaseBlock extends JavaBlock {
	
	private String value;
	
	public CaseBlock(String value) {
		Assertion.isNotNull(value, "case value is null");
		this.value = value;
	}

	@Override
	protected String firstLine() {
		return "case " + value + ":";
	}
	
	@Override
	public List<String> toFormatLines() {
		BreakState b = new BreakState();
		if (!childs.contains(b)) addChild(new BreakState());
		
		
		List <String> result = new LinkedList<String>();
		result.add(firstLine());
		
		super.formatChilds(result);
		
		return result;
	}
}
