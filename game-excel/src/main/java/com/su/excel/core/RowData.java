package com.su.excel.core;

import java.util.HashMap;
import java.util.Map;

public class RowData {
	private Map<String, String> data  = new HashMap<>();
	
	public void put(String columnName, String value) {
		data.put(columnName, value);
	}
}
