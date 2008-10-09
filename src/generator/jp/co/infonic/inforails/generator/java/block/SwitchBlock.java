package jp.co.infonic.inforails.generator.java.block;

import java.util.LinkedList;
import java.util.List;

import jp.co.infonic.common.util.Assertion;
import jp.co.infonic.inforails.generator.java.JavaBlock;

public class SwitchBlock extends JavaBlock {
	
	private String judgeVar;
	
	private List<CaseBlock> cases = new LinkedList<CaseBlock>();
	
	public SwitchBlock(String variable) {
		Assertion.isExist(variable, "switch condition is null");
		this.judgeVar = variable;
	}
	
	public SwitchBlock addCase(CaseBlock c) {
		cases.add(c);
		return this;
	}

	@Override
	protected String firstLine() {
		return "switch(" + judgeVar + ")";
	}

	@Override
	public List<String> toFormatLines() {
		List<String> list = new LinkedList<String>();
		super.start(list);
		
		for(CaseBlock c: cases) {
			list.addAll(c.toFormatLines());
		}
		
		super.end(list);
		return list;
	}
}
