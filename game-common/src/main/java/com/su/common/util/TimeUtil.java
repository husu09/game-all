package com.su.common.util;

public class TimeUtil {
	
	public final static int ONE_DAY = 1000 * 60 * 24;
	
	/**
	 * 获取当前时间/毫秒
	 * @return
	 */
	public static long getCurrTime() {
		return System.currentTimeMillis();
	}
}
