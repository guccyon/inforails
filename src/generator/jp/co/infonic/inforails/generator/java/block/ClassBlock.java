package jp.co.infonic.inforails.generator.java.block;

import jp.co.infonic.common.util.ArrayUtil;
import jp.co.infonic.inforails.generator.java.JavaBlock;

public class ClassBlock extends JavaBlock {
	private String name;
	
	private String modify;
	
	private boolean Abstract;
	
	private boolean Final = false;
	
	protected String[] modifies() {
		return new String[]{"public", "", "protected"};
	}
	
	public ClassBlock(String name) {
		this(name, "public");
	}
	public ClassBlock(String name, String modify) {
		this(name, modify, false);
	}
	public ClassBlock(String name, String modify, boolean Abstract) {
		if (!ArrayUtil.isInclude(modifies(), modify)){
			throw new IllegalArgumentException("illegal modify : " + modify);
		}
		this.name = name;
		this.modify = modify;
		this.Abstract = Abstract;
	}
	public ClassBlock setFinal() {
		if (Abstract) {
			throw new IllegalStateException("could not set \"final\" to abstract class");
		}
		Final = true;
		return this;
	}

	@Override
	protected String firstLine() {
		StringBuffer sb = new StringBuffer();
		if (Abstract) sb.append("abstract ");
		sb.append(modify.equals("") ? "" : modify + " ");
		if (Final) sb.append("final ");
		sb.append("class ");
		sb.append(name);
		return sb.toString();
	}
}
