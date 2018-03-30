package com.su.excel.core;

import java.util.HashMap;
import java.util.Map;

/**
 * 行数据
 */
public class RowData {

	private Map<String, String> data = new HashMap<>();

	public void put(String columnName, String value) {
		data.put(columnName, value);
	}

	public int getInt(String key) {
		return (int) Double.parseDouble(data.get(key));
	}

	public String getString(String key) {
		return data.get(key);
	}
}
