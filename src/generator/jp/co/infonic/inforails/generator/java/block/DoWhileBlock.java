package jp.co.infonic.inforails.generator.java.block;

import java.util.List;


public class DoWhileBlock extends WhileBlock {

	@Override
	protected void start(List<String> list) {
		if (isSameline()) {
			list.add("do {");
		} else {
			list.add("do");
			list.add("{");
		}
	}

	@Override
	protected void end(List<String> list) {
		list.add("} while(" + condition.toSrc() + ")");
	}
}
