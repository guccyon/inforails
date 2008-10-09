package jp.co.infonic.inforails.generator.java.block;

import java.util.List;

import jp.co.infonic.inforails.generator.java.JavaBlock;

public class DefaultCaseBlock extends JavaBlock {

	@Override
	protected String firstLine() {
		return "default:";
	}
	
	@Override
	protected void start(List<String> list) {
		list.add(firstLine());
	}
	
	@Override
	protected void end(List<String> list) {
	}	
}
