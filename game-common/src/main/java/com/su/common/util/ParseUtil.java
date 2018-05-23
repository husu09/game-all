package com.su.common.util;

import com.su.common.obj.Item;

public class ParseUtil {

	public static int getInt(String value) {
		if (StringUtil.isNone(value))
			return 0;
		return Integer.parseInt(value);
	}

	public static boolean getBoolean(String value) {
		if (ParseUtil.getInt(value) > 0)
			return true;
		return false;
	}

	public static Item getItem(String value) {
		String[] splits = value.split("_");
		Item item = new Item();
		item.setType(Integer.parseInt(splits[0]));
		item.setSysId(Integer.parseInt(splits[1]));
		item.setCount(Integer.parseInt(splits[2]));
		return item;
	}
}
