package com.su.excel.core;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class ExcelManager {

	private Map<String, ExcelMapping<?>> excelMappingMap = new HashMap<>();

	public void put(String name, ExcelMapping<?> mapping) {
		excelMappingMap.put(name, mapping);
	}

	public boolean contains(String mappingName) {
		return excelMappingMap.containsKey(mappingName);
	}

}
