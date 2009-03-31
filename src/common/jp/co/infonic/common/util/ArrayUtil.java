package jp.co.infonic.common.util;

import static jp.co.infonic.common.module.ex.ExArray.ExA;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import jp.co.infonic.common.module.ex.ExArray;
import jp.co.infonic.common.module.ex.ExArray.ResultIterator;
import jp.co.infonic.common.module.ex.ExArray.ReturnDynamicIterator;
import jp.co.infonic.common.module.ex.ExArray.ReturnIterator;

public class ArrayUtil {

	public static String join(String[] strAry, String delimiter) {
	    return new ExArray<String>(strAry).join(delimiter);
	}
	public static String join(Object[] strAry, String delimiter) {
		return new ExArray<Object>(strAry).join(delimiter);
	}
	public static String join(List<?> strList, String delimiter) {
	    return join(strList.toArray(new Object[0]), delimiter);
	}
	
	public static Object[] add(Object[] dest, Object element) {
		return ExA(dest).push(element).toArray();
	}
	
	public static Object[] map(Object[] objects, ReturnIterator<Object> iter){
		return ExA(objects).map(iter).toArray(); 
	}
	public static List<Object> map(List<Object> list, ReturnIterator<Object> iter){
		return Arrays.asList(ExA(list).map(iter).toArray());
	}
	
	public static Object detect(Object[] objects, ResultIterator<Object> iter) {
		return ExA(objects).detect(iter);
	}
	public static Object detect(List<Object> list, ResultIterator<Object> iter) {
		return ExA(list).detect(iter);
	}
	
	public static boolean isInclude(final List<Object> list, Object object) {
		return ExA(list).isInclude(object);
	}
	public static boolean isInclude(Object[] objects, Object object) {
		return ExA(objects).isInclude(object);
	}
	
	public static String inject(List<String> list, String memorize, 
		ReturnDynamicIterator<String> iter) {
		return (String)new ExArray<String>(list.toArray(new String[0]))
					.inject(memorize, iter);
	}
	public static Object inject(List<Object> list, Object memorize, 
			ReturnDynamicIterator<Object> iter) {
		return ExA(list).inject(memorize, iter);
	}
	
	public static void bubbleSort(Object[] objects, Comparator comp) {
		ExA(objects).bubbleSort_(comp);
	}
	
}
