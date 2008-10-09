package jp.co.infonic.inforails.generator.java.block;



public class InnerClassBlock extends ClassBlock {
	
	protected String[] modifies() {
		return new String[]{"", "protected", "private"};
	}
	
	public InnerClassBlock(String name) {
		super(name, "");
	}
	public InnerClassBlock(String name, String modify) {
		super(name, modify, false);
	}
	public InnerClassBlock(String name, String modify, boolean Abstract) {
		super(name, modify, Abstract);
	}

}