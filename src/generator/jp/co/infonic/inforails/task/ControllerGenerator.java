package jp.co.infonic.inforails.task;

import java.io.File;

import jp.co.infonic.inforails.generator.core.Block;
import jp.co.infonic.inforails.generator.core.SrcGenerator;
import jp.co.infonic.inforails.generator.core.SrcStructure;
import jp.co.infonic.inforails.generator.java.JavaStructure;
import jp.co.infonic.inforails.generator.java.block.CaseBlock;
import jp.co.infonic.inforails.generator.java.block.ClassBlock;
import jp.co.infonic.inforails.generator.java.block.DoWhileBlock;
import jp.co.infonic.inforails.generator.java.block.ForBlock;
import jp.co.infonic.inforails.generator.java.block.InnerClassBlock;
import jp.co.infonic.inforails.generator.java.block.SwitchBlock;
import jp.co.infonic.inforails.generator.java.block.WhileBlock;
import jp.co.infonic.inforails.generator.java.state.ImportState;
import jp.co.infonic.inforails.generator.java.state.PackageState;

public class ControllerGenerator extends SrcGenerator {

	public ControllerGenerator(File file, SrcStructure struct) {
		super(file, struct);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	
	// テスト用
	public static void main(String[] args) {
		JavaStructure java = new JavaStructure(new ClassBlock("Test"));
		
		java.setPackage(new PackageState("jp.co.infonic"));
		
		java.addImport(new ImportState("java.lang.*"));
		java.addImport(new ImportState("java.util.*"));
		
		
		Block b1 = new ForBlock();
		java.c().addChild(b1);
		
		java.c().addChild(new WhileBlock().addChild(new WhileBlock()));
		
		java.c().addChild(new DoWhileBlock());
		
		SwitchBlock b4 = new SwitchBlock("a");
		b4.addCase(new CaseBlock("1"));
		b4.addCase(new CaseBlock("2"));
		java.c().addChild(b4);
		
		java.addInnerClass(new InnerClassBlock("InTest"));
		
		new SrcGenerator(new File(""), java).generate();
	}
}
