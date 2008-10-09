package jp.co.infonic.inforails.generator.java;

import java.util.LinkedList;
import java.util.List;

import jp.co.infonic.common.util.ArrayUtil;
import jp.co.infonic.common.util.ConditionSupport;
import jp.co.infonic.inforails.generator.core.BlankStatement;
import jp.co.infonic.inforails.generator.core.Block;
import jp.co.infonic.inforails.generator.core.SrcElement;
import jp.co.infonic.inforails.generator.core.Statement;

public abstract class JavaBlock extends Block {
	
	protected static BlankStatement blank = new BlankStatement();

	public String toSrc() {
		
		return ArrayUtil.join(toFormatLines(), " ");
	}

	public List<String> toFormatLines() {
		List<String> list = new LinkedList<String>();
		
		start(list);
		
		formatChilds(list);
		
		if (ConditionSupport.isExist(childs))
			list.add(blank.toSrc());
		
		end(list);
		
		return list;
	}
	
	// �J�n�s
	protected void start(List<String> list) {
		// �u���b�N�̊��ʂ��ȗ��ł���ꍇ
		if (isOmit()) {
			list.add(firstLine());
			
		// �u���b�N�̊��ʂ̊J�n�ʒu���u���b�N���Ɠ����s�̏ꍇ
		} else if (isSameline()) {
			list.add(firstLine() == null ? "{" : firstLine() + " {");
		} else {
			list.add(firstLine());
			list.add("{");
		}
	}
	
	// �q�v�f��ǉ�
	protected void formatChilds(List<String>list) {
		for(SrcElement child: childs) {
			if (child instanceof Block) {
				list.add(blank.toSrc());
				list.addAll(getChildBlockForList((Block)child));
			} else {
				list.add(getIndent() + ((Statement)child).toSrc());
			}
		}
	}
	
	// �ŏI�s
	protected void end(List<String> list) {
		if (!isOmit()) {
			list.add("}");
		}
	}
	
	protected List<String> getChildBlockForList(Block block) {
		List<String> result = new LinkedList<String>();
		for(String s: block.toFormatLines())
			result.add(getIndent() + s);
		return result;
	}
	
	protected String getChildBlock(Block block) {
		return block.toSrc();
	}
	
	protected final String getIndent() {
		return JavaGenerateProperty.getIndent();
	}
	
	protected final boolean isSameline() {
		return JavaGenerateProperty.getBlockSameLine();
	}
	
	protected final boolean isOmit() {
		return JavaGenerateProperty.getBlockOmit() && isOneState();
	}
	
	protected boolean isOneState() {
		return childs.size() == 1;
	}
	
	protected abstract String firstLine();
}
