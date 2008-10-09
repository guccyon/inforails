package jp.co.infonic.common.util;

public class NumberUtil {
	
	public static long TB(int i) {
		return 1024L * GB(i);
	}

	public static long GB(int i) {
		return 1024L * (long)MB(i);
	}
	
	public static int MB(int i) {
		return 1024 * KB(i);
	}
	public static int KB(int i) {
		return 1024 * i;
	}
}
