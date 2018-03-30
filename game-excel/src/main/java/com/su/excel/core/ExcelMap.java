package com.su.excel.core;

/**
 * 配置文件映射接口
 * */
public interface ExcelMap<T> {
	
	/**
	 * 返回文件名
	 * */
	public String getName();
	
	/**
	 * 映射
	 * */
	public T map(RowData rowData);
	
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
	 * 获取当前配置的类型
	 * */
	public Class<?> getTypeClass();
	
	/**
	 * 添加配置数据
	 * */
	public void add(String value);
}
