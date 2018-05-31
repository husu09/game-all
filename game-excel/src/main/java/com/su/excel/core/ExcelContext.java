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
public class ExcelContext {

	/**
	 * 配置映射类
	 */
	private Map<String, ExcelMap<?>> excelMaps = new HashMap<>();

	/**
	 * 预处理数据
	 */
	private Map<String, List<Object>> preData = new HashMap<>();
	
	
	public Map<String, ExcelMap<?>> getExcelMaps() {
		return excelMaps;
	}

	/**
	 * 所有配置加载完成时调用
	 */
	public void doFinishLoadAll() {
		for (ExcelMap<?> map : excelMaps.values()) {
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
	public void savePreData(String preDataDir) {
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
