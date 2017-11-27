package com.su.core;

import java.util.regex.Pattern;

public class CoreUtils {
	/**
	 * 判断是否为整数
	 */
	public static boolean isInteger(String s) {
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		return pattern.matcher(s).matches();
	}
}
