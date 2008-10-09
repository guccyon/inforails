package jp.co.infonic.inforails.generator.core;

import java.io.File;

import jp.co.infonic.common.util.ArrayUtil;

public class SrcGenerator {
	
	private SrcStructure struct;
	
	private File file;
	
	private String returnCode;
	
	private String charset;
	
	public SrcGenerator(File file, SrcStructure struct) {
		this.file = file;
		this.struct = struct;
		returnCode = "\r\n";
		charset = "Windows-31J";
	}
	
	public void setReturnCode() {
		this.returnCode = returnCode;
	}

	public void generate() {
		System.out.println(ArrayUtil.join(struct.struct(), returnCode));
	}
}
