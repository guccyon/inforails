package jp.co.infonic.inforails.generator.java;

import java.util.LinkedList;
import java.util.List;

import jp.co.infonic.common.util.ConditionSupport;
import jp.co.infonic.inforails.generator.core.BlankStatement;
import jp.co.infonic.inforails.generator.core.Block;
import jp.co.infonic.inforails.generator.core.SrcStructure;
import jp.co.infonic.inforails.generator.core.Statement;
import jp.co.infonic.inforails.generator.java.block.ClassBlock;
import jp.co.infonic.inforails.generator.java.block.InnerClassBlock;
import jp.co.infonic.inforails.generator.java.state.ImportState;
import jp.co.infonic.inforails.generator.java.state.PackageState;

public class JavaStructure implements SrcStructure {
	
	public static final String SYMBOL_ILLEGAL_CHARS = "[^!-\\.:-?\\[-`{-~]";

	private Statement pkg;
	
	private List <Statement> imports = new LinkedList<Statement>();
	
	private ClassBlock claBlock;

	private List <Block> innerClassBlocks = new LinkedList<Block>();
	
	private static BlankStatement blank = new BlankStatement();
	
	public JavaStructure(ClassBlock cls) {
		this.claBlock = cls;
	}
	
	public ClassBlock c() {
		return claBlock;
	}

	public List<String> struct() {
		List<String> list = new LinkedList<String>();
		// パッケージ宣言部
		if (ConditionSupport.isExist(pkg)) {
			list.add(pkg.toSrc());
			list.add(blank.toSrc());
		}
		
		// インポート部
		if (ConditionSupport.isExist(imports)) {
			for(Statement s: imports)
				list.add(s.toSrc());
			list.add(blank.toSrc());
		}
		
		// メインクラス部
		list.addAll(claBlock.toFormatLines());
		
		list.add(blank.toSrc());
		
		// インナークラス部
		for(Block b: innerClassBlocks)
			list.addAll(b.toFormatLines());
		
		return list;
	}
	
	public void setPackage(PackageState pkg) {
		this.pkg = pkg;
	}
	
	public void addImport(ImportState importState) {
		this.imports.add(importState);
	}
	
	public void addInnerClass(InnerClassBlock in) {
		this.innerClassBlocks.add(in);
	}
	 
}
