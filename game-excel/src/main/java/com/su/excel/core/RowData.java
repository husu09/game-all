package com.su.excel.core;

import java.util.HashMap;
import java.util.Map;

import com.su.common.obj.Item;
import com.su.common.util.StringUtil;

/**
 * 行数据
 */
public class RowData {

	private Map<String, String> data = new HashMap<>();

	public void put(String columnName, String value) {
		data.put(columnName, value);
	}

	public int getInt(String key) {
		if (StringUtil.isNone(data.get(key)))
			return 0;
		return Integer.parseInt(data.get(key));
	}

	public String getString(String key) {
		return data.get(key);
	}

	public boolean getBoolean(String key) {
		if (getInt(key) > 0)
			return true;
		return false;
	}

	public Item getItem(String key) {
		String data = getString(key);
		String[] splits = data.split("_");
		Item item = new Item();
		item.setType(Integer.parseInt(splits[0]));
		item.setSysId(Integer.parseInt(splits[1]));
		item.setCount(Integer.parseInt(splits[2]));
		return item;
	}
}
