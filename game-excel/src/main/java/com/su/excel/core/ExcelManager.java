package com.su.excel.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;


@Component
public class ExcelManager {
	
	/**
	 * 配置映射类
	 * */
	private Map<String, ExcelMapping<?>> excelMappingMap = new HashMap<>();
	
	/**
	 * 预处理数据
	 * */
	private Map<String, List<Object>> preData = new HashMap<>();

	public void put(String name, ExcelMapping<?> mapping) {
		excelMappingMap.put(name, mapping);
	}

	public boolean contains(String mappingName) {
		return excelMappingMap.containsKey(mappingName);
	}
	
	public ExcelMapping<?> get(String mappingName) {
		return excelMappingMap.get(mappingName);
	}
	
	/**
	 * 所有配置加载完成时调用
	 * */
	public void completeAll() {
		for (ExcelMapping<?> mapping : excelMappingMap.values()) {
			mapping.completeAll();
		}
	}
	
	/**
	 * 添加预处理数据
	 * */
	public void addPreData(String mappingName, Object value) {
		if (!preData.containsKey(mappingName))
			preData.put(mappingName, new ArrayList<>());
		preData.get(mappingName).add(value);
	}

}
