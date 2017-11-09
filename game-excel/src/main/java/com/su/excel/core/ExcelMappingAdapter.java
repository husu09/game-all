package com.su.excel.core;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public abstract class ExcelMappingAdapter<T> implements ExcelMapping<T> {

	@Autowired
	private ExcelManager excelManager;

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

}
