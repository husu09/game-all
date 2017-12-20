package com.su.excel.core;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;

/**
 * 配置文件映射基类
 */
public abstract class ExcelMappingAdapter<T> implements ExcelMapping<T> {

	@Autowired
	private ExcelManager excelManager;
	
	/**
	 * 存储解析后的数据
	 * */
	private Map<Integer, T> storageMap = new HashMap<>();

	@Override
	public void complete() {

	}

	@Override
	public void completeAll() {

	}

	@Override
	public T get(int id) {
		return storageMap.get(id);
	}

	@PostConstruct
	public void init() {
		excelManager.put(name(), this);
	}

	@Override
	public Class<T> getTypeClass() {
		Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
		return entityClass;
	}

	@Override
	public void add(String value) {
		T t = JSON.parseObject(value, getTypeClass());
		if (t instanceof Identity) {
			Identity identity = (Identity) t;
			storageMap.put(identity.getId(), t);
		} else {
			throw new RuntimeException(t.getClass() + " 没有实现 Identity 接口");
		}
	}

}
