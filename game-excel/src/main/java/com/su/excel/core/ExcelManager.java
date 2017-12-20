package com.su.excel.core;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

@Component
public class ExcelManager {

	/**
	 * 配置映射类
	 */
	private Map<String, ExcelMapping<?>> mappings = new HashMap<>();

	/**
	 * 预处理数据
	 */
	private Map<String, List<Object>> preData = new HashMap<>();

	public void put(String name, ExcelMapping<?> mapping) {
		mappings.put(name, mapping);
	}

	public boolean contains(String mappingName) {
		return mappings.containsKey(mappingName);
	}

	public ExcelMapping<?> get(String mappingName) {
		return mappings.get(mappingName);
	}

	/**
	 * 所有配置加载完成时调用
	 */
	public void completeAll() {
		for (ExcelMapping<?> mapping : mappings.values()) {
			mapping.completeAll();
		}
	}

	/**
	 * 添加预处理数据
	 */
	public void addPreData(String mappingName, Object value) {
		if (!preData.containsKey(mappingName))
			preData.put(mappingName, new ArrayList<>());
		preData.get(mappingName).add(value);
	}

	/**
	 * 保存预处理数据
	 */
	public void savePreData() {
		String basePath = getClass().getResource("/").getFile() + "preData/";
		File dir = new File(basePath);
		if (!dir.exists())
			dir.mkdirs();
		for (Entry<String, List<Object>> e : preData.entrySet()) {
			try {
				PrintWriter out = new PrintWriter(new FileWriter(basePath + e.getKey()));
				for (Object o : e.getValue()) {
					String jsonString = JSON.toJSONString(o);
					out.println(jsonString);
				}
				out.flush();
				out.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}
	}

}
