package com.su.common.util;

import java.util.Random;
import java.util.regex.Pattern;

public class CommonUtils {
	/**
	 * 判断是否为整数
	 */
	private static final Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");

	public static boolean isInteger(String s) {
		return pattern.matcher(s).matches();
	}

	/**
	 * 随机数
	 */
	private static final Random random = new Random();
	public static int range(int min, int max) {
		return random.nextInt(max - min) + min;
	}

}
