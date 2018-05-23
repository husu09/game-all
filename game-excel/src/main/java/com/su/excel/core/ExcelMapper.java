package com.su.excel.core;

import java.util.Collection;

/**
 * 配置文件映射接口
 * */
public interface ExcelMapper<T> {
	
	/**
	 * 返回文件名
	 * */
	public String getName();
	
	/**
	 * 映射
	 * */
	public T map(RowData rowData);
	/**
	 * 添加配置数据
	 * */
	public void add(Object obj);
	
	/**
	 * 加载完当前配置表时调用
	 * */
	public void finishLoad();
	
	/**
	 * 加载完所有配置时调用
	 * */
	public void finishLoadAll();
	
	/**
	 * 通过 id 获取配置
	 * */
	public T get(int id);
	
	/**
	 * 获取所有配置
	 * */
	public Collection<T> all();
	
	/**
	 * 添加配置数据
	 * */
	public void add(String value);

}
