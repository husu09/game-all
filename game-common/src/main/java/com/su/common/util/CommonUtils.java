package com.su.common.util;

import java.util.regex.Pattern;

public class CommonUtils {
	/**
	 * 判断是否为整数
	 */
	private static final Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
	public static boolean isInteger(String s) {
		return pattern.matcher(s).matches();
	}
}
