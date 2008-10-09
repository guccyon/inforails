package jp.co.infonic.inforails.generator.core;

import jp.co.infonic.common.module.ex.ExArray;
import jp.co.infonic.common.util.ArrayUtil;

public class GenerateUtil {

	public static String[] toSrcArray(Statement[] statements) {
		return (String[])ArrayUtil.map(statements, new ExArray.ReturnIterator(){
			public Object yield(int i, Object e, Object... obj) {
				return ((Statement) e).toSrc();
			}
		});
	}
}
