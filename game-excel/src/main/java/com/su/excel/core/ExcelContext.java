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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

@Component
public class ExcelContext {

	@Value("${excel.preData.dir}")
	private String preDataDir;

	/**
	 * 配置映射类
	 */
	private Map<String, ExcelMap<?>> maps = new HashMap<>();

	/**
	 * 预处理数据
	 */
	private Map<String, List<Object>> preData = new HashMap<>();

	public void addMap(String name, ExcelMap<?> map) {
		maps.put(name, map);
	}

	public boolean containsMap(String mapName) {
		return maps.containsKey(mapName);
	}

	public ExcelMap<?> getMap(String mapName) {
		return maps.get(mapName);
	}

	/**
	 * 所有配置加载完成时调用
	 */
	public void callFinishLoadAll() {
		for (ExcelMap<?> map : maps.values()) {
			map.finishLoadAll();
		}
	}

	/**
	 * 添加预处理数据
	 */
	public void addPreData(String mapName, Object value) {
		if (!preData.containsKey(mapName))
			preData.put(mapName, new ArrayList<>());
		preData.get(mapName).add(value);
	}

	/**
	 * 保存预处理数据
	 */
	public void savePreData() {
		File dir = new File(preDataDir);
		if (!dir.exists())
			dir.mkdirs();
		for (Entry<String, List<Object>> e : preData.entrySet()) {
			try {
				PrintWriter out = new PrintWriter(new FileWriter(preDataDir + e.getKey()));
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
