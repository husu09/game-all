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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

@Component
public class ExcelContext {

	private Logger logger = LoggerFactory.getLogger(ExcelProcessor.class);

	/**
	 * 配置映射类
	 */
	private Map<String, ExcelMapper<?>> excelMappers = new HashMap<>();

	/**
	 * 预处理数据
	 */
	private Map<String, List<Object>> preData = new HashMap<>();

	public Map<String, ExcelMapper<?>> getExcelMappers() {
		return excelMappers;
	}

	/**
	 * 所有配置加载完成时调用
	 */
	public void doFinishLoadAll() {
		for (ExcelMapper<?> map : excelMappers.values()) {
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

	public boolean isEmpty(String mapName) {
		if (preData.get(mapName) == null || preData.get(mapName).size() == 0)
			return true;
		return false;
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
			logger.info("{}：{}", e.getKey(), e.getValue().size());
		}
	}

}
