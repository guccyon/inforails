package jp.co.infonic.inforails.generator.core;

import java.util.LinkedList;
import java.util.List;


public abstract class Block implements SrcElement, Formattable {
	
	protected List<SrcElement> childs = new LinkedList<SrcElement>();
	
	/**
	 * ブロックの子要素を追加する
	 * @param state
	 * @return
	 */
	public Block addChild(Statement state) {
		childs.add(state);
		return this;
	}
	
	/**
	 * ブロックの子要素を追加する
	 * @param block
	 * @return
	 */
	public Block addChild(Block block) {
		childs.add(block);
		return this;
	}
}
